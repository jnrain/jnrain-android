/*
 * Copyright 2013 JNRain
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.jnrain.mobile.accounts.kbs;

import java.util.Timer;
import java.util.TimerTask;

import org.jnrain.kbs.entity.SimpleReturnCode;
import org.jnrain.mobile.R;
import org.jnrain.mobile.ui.base.JNRainActivity;
import org.jnrain.mobile.ui.base.RegisterPoint;
import org.jnrain.mobile.ui.kbs.KBSUIConstants;
import org.jnrain.mobile.ui.ux.DialogHelper;
import org.jnrain.mobile.ui.ux.FormatHelper;
import org.jnrain.mobile.util.GlobalState;
import org.jnrain.mobile.util.SpiceRequestListener;

import roboguice.inject.InjectView;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class KBSRegisterActivity extends JNRainActivity<SimpleReturnCode>
        implements RegisterPoint {
    @InjectView(R.id.textRegisterDisclaimer)
    TextView textRegisterDisclaimer;

    @InjectView(R.id.editNewUID)
    EditText editNewUID;
    @InjectView(R.id.textUIDAvailability)
    TextView textUIDAvailability;

    @InjectView(R.id.editNewEmail)
    EditText editNewEmail;
    @InjectView(R.id.editNewPassword)
    EditText editNewPassword;
    @InjectView(R.id.editRetypeNewPassword)
    EditText editRetypeNewPassword;
    @InjectView(R.id.editNewNickname)
    EditText editNewNickname;

    @InjectView(R.id.editStudID)
    EditText editStudID;
    @InjectView(R.id.editRealName)
    EditText editRealName;

    @InjectView(R.id.checkIsEthnicMinority)
    CheckBox checkIsEthnicMinority;

    @InjectView(R.id.checkUseCurrentPhone)
    CheckBox checkUseCurrentPhone;
    @InjectView(R.id.editPhone)
    EditText editPhone;

    @InjectView(R.id.imageRegCaptcha)
    ImageView imageRegCaptcha;
    @InjectView(R.id.editCaptcha)
    EditText editCaptcha;

    @InjectView(R.id.btnSubmitRegister)
    Button btnSubmitRegister;

    // private static final String TAG = "KBSRegisterActivity";

    private ProgressDialog loadingDlg;
    private Handler mHandler;

    private String currentPhoneNumber;
    private boolean isCurrentPhoneNumberAvailable;

    private KBSCaptchaHelper captchaHelper;
    private Timer delayedUIDChecker;
    private long lastUIDCheckTime;

    public static void show(Context context) {
        final Intent intent = new Intent(context, KBSRegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kbs_register);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                loadingDlg = DialogHelper.showProgressDialog(
                        KBSRegisterActivity.this,
                        R.string.check_updates_dlg_title,
                        R.string.please_wait,
                        false,
                        false);
            }
        };

        lastUIDCheckTime = 0;

        // event handlers
        editNewUID.addTextChangedListener(new TextWatcher() {
            long lastCheckTime = 0;
            String lastUID;

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(
                    CharSequence s,
                    int start,
                    int count,
                    int after) {
            }

            @Override
            public void onTextChanged(
                    CharSequence s,
                    int start,
                    int before,
                    int count) {
                // FIXME: use monotonic clock...
                long curtime = System.currentTimeMillis();
                if (curtime - lastCheckTime >= KBSUIConstants.REG_CHECK_UID_INTERVAL_MILLIS) {
                    String uid = s.toString();

                    // don't check at the first char
                    if (uid.length() > 1) {
                        checkUIDAvailability(uid, curtime);
                    }

                    lastCheckTime = curtime;
                    lastUID = uid;

                    // schedule a new delayed check
                    if (delayedUIDChecker != null) {
                        delayedUIDChecker.cancel();
                        delayedUIDChecker.purge();
                    }
                    delayedUIDChecker = new Timer();
                    delayedUIDChecker.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            final String uid = getUID();

                            if (uid != lastUID) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        checkUIDAvailability(
                                                uid,
                                                System.currentTimeMillis());
                                    }
                                });

                                lastUID = uid;
                            }
                        }
                    },
                            KBSUIConstants.REG_CHECK_UID_INTERVAL_MILLIS);
                }
            }
        });

        editNewUID.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // lost focus, force check uid availability
                    checkUIDAvailability(
                            getUID(),
                            System.currentTimeMillis());
                } else {
                    // inputting, temporarily clear that notice
                    updateUIDAvailability(false, 0);
                }
            }
        });

        checkUseCurrentPhone
            .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(
                        CompoundButton buttonView,
                        boolean isChecked) {
                    setUseCurrentPhone(isChecked);
                }
            });

        // interface init
        // UID availability
        updateUIDAvailability(false, 0);

        // HTML-formatted register disclaimer
        FormatHelper.setHtmlText(
                this,
                textRegisterDisclaimer,
                R.string.register_disclaimer);
        textRegisterDisclaimer.setMovementMethod(LinkMovementMethod
            .getInstance());

        // current phone number
        currentPhoneNumber = GlobalState.getPhoneNumber(this);
        isCurrentPhoneNumberAvailable = currentPhoneNumber != null;

        if (isCurrentPhoneNumberAvailable) {
            // display the obtained number as hint
            FormatHelper.setHtmlText(
                    this,
                    checkUseCurrentPhone,
                    R.string.field_use_current_phone,
                    currentPhoneNumber);
        } else {
            // phone number unavailable, disable the choice
            checkUseCurrentPhone.setEnabled(false);
            checkUseCurrentPhone.setVisibility(View.GONE);
        }

        // default to use current phone number if available
        checkUseCurrentPhone.setChecked(isCurrentPhoneNumberAvailable);
        setUseCurrentPhone(isCurrentPhoneNumberAvailable);

        // issue preflight request
        // load captcha in success callback
        this.makeSpiceRequest(
                new KBSRegisterRequest(),
                new KBSRegisterRequestListener(this));
    }

    @Override
    protected void onStop() {
        if (delayedUIDChecker != null) {
            delayedUIDChecker.cancel();
        }

        super.onStop();
    }

    public ProgressDialog getLoadingDialog() {
        return loadingDlg;
    }

    public synchronized void setUseCurrentPhone(boolean useCurrent) {
        editPhone.setEnabled(!useCurrent);
        editPhone.setVisibility(useCurrent ? View.GONE : View.VISIBLE);
    }

    @SuppressWarnings({
            "rawtypes",
            "unchecked"
    })
    @Override
    public void fetchCaptcha() {
        // init captcha helper
        if (captchaHelper == null) {
            captchaHelper = new KBSCaptchaHelper(
                    (SpiceRequestListener) this,
                    imageRegCaptcha);
        }

        captchaHelper.doFetchCaptcha();
    }

    @Override
    public String getUID() {
        return editNewUID.getText().toString();
    }

    @Override
    public void checkUIDAvailability(String uid, long timestamp) {
        if (uid.length() == 0) {
            // directly "return"
            updateUIDAvailability(false, R.string.reg_uid_empty);
            return;
        }

        makeSpiceRequest(
                new KBSCheckIDRequest(uid),
                new KBSCheckIDRequestListener(this, timestamp));
    }

    @Override
    public void notifyUIDAvailability(int status, long timestamp) {
        switch (status) {
            case 0:
                // Success
                updateUIDAvailability(
                        true,
                        R.string.reg_uid_available,
                        timestamp);
                break;

            case 1:
                // UID empty
                updateUIDAvailability(
                        false,
                        R.string.reg_uid_empty,
                        timestamp);
                break;

            case 2:
                // Already exists
                updateUIDAvailability(
                        false,
                        R.string.reg_uid_exists,
                        timestamp);
                break;

            case 3:
                // Too short
                updateUIDAvailability(
                        false,
                        R.string.reg_uid_too_short,
                        timestamp);
                break;

            case 4:
                // Too long
                updateUIDAvailability(
                        false,
                        R.string.reg_uid_too_long,
                        timestamp);
                break;

            case 5:
                // Forbidden characters
                updateUIDAvailability(
                        false,
                        R.string.reg_uid_malformed,
                        timestamp);
                break;

            case 6:
                // Profanity
                updateUIDAvailability(
                        false,
                        R.string.reg_uid_banned,
                        timestamp);
                break;

            default:
                break;
        }
    }

    protected synchronized void updateUIDAvailability(
            boolean ok,
            int statusResId,
            long timestamp) {
        if (timestamp < lastUIDCheckTime) {
            // results of an earlier check arrived late, discard it
            return;
        }

        updateUIDAvailability(ok, statusResId);
    }

    protected synchronized void updateUIDAvailability(
            boolean ok,
            int statusResId) {
        // Color
        Resources res = this.getResources();
        textUIDAvailability.setTextColor(ok ? res
            .getColorStateList(R.color.jnrain_green_dark) : res
            .getColorStateList(R.color.error_red));

        // Message
        if (statusResId != 0) {
            textUIDAvailability.setText(statusResId);
            textUIDAvailability.setVisibility(View.VISIBLE);
        } else {
            textUIDAvailability.setText("");
            textUIDAvailability.setVisibility(View.GONE);
        }
    }
}
