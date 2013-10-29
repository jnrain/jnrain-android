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
import java.util.regex.Pattern;

import name.xen0n.cytosol.app.SpiceRequestListener;
import name.xen0n.cytosol.data.SimpleReturnCode;
import name.xen0n.cytosol.ui.util.DialogHelper;
import name.xen0n.cytosol.ui.util.FormatHelper;
import name.xen0n.cytosol.ui.widget.GuidedEditText;
import name.xen0n.cytosol.util.TelephonyHelper;

import org.jnrain.mobile.R;
import org.jnrain.mobile.ui.base.JNRainActivity;
import org.jnrain.mobile.ui.base.RegisterPoint;
import org.jnrain.mobile.ui.kbs.KBSUIConstants;

import roboguice.inject.InjectView;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;


public class KBSRegisterActivity extends JNRainActivity<SimpleReturnCode>
        implements RegisterPoint {
    @InjectView(R.id.textRegisterDisclaimer)
    TextView textRegisterDisclaimer;

    @InjectView(R.id.editNewUID)
    GuidedEditText editNewUID;
    @InjectView(R.id.editNewEmail)
    GuidedEditText editNewEmail;
    @InjectView(R.id.editNewPassword)
    GuidedEditText editNewPassword;
    @InjectView(R.id.editRetypeNewPassword)
    GuidedEditText editRetypeNewPassword;
    @InjectView(R.id.editNewNickname)
    GuidedEditText editNewNickname;

    @InjectView(R.id.editStudID)
    GuidedEditText editStudID;
    @InjectView(R.id.editRealName)
    GuidedEditText editRealName;

    @InjectView(R.id.textEthnicMinorityIndicator)
    TextView textEthnicMinorityIndicator;
    @InjectView(R.id.checkIsEthnicMinority)
    CheckBox checkIsEthnicMinority;

    @InjectView(R.id.checkUseCurrentPhone)
    CheckBox checkUseCurrentPhone;
    @InjectView(R.id.editPhone)
    GuidedEditText editPhone;

    @InjectView(R.id.imageRegCaptcha)
    ImageView imageRegCaptcha;
    @InjectView(R.id.editCaptcha)
    GuidedEditText editCaptcha;

    @InjectView(R.id.btnSubmitRegister)
    Button btnSubmitRegister;

    // private static final String TAG = "KBSRegisterActivity";
    private static final int FIXED_VALIDATION_FIELD_IDS[] = {
            R.id.editNewUID,
            R.id.editNewEmail,
            R.id.editNewPassword,
            R.id.editRetypeNewPassword,
            R.id.editNewNickname,
            R.id.editStudID,
            R.id.editRealName,
            // R.id.editPhone,
            R.id.editCaptcha
    };

    private static final String EMAIL_CHECK_RE = "^[0-9A-Za-z]+(?:[+][0-9A-Za-z]+)?@[0-9A-Za-z]+\\.[A-Za-z]+$";
    private final Pattern EMAIL_CHECKER = Pattern.compile(EMAIL_CHECK_RE);

    private ProgressDialog loadingDlg;
    private Handler mHandler;

    private String currentPhoneNumber;
    private boolean isCurrentPhoneNumberAvailable;

    private KBSCaptchaHelper captchaHelper;
    private Timer delayedUIDChecker;
    private long lastUIDCheckTime;

    private SparseBooleanArray validatedMap;

    public static void show(Context context) {
        final Intent intent = new Intent(context, KBSRegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    protected synchronized void initValidationMapping() {
        if (validatedMap == null) {
            validatedMap = new SparseBooleanArray();

            for (int id : FIXED_VALIDATION_FIELD_IDS) {
                validatedMap.put(id, false);
            }

            // dynamic fields
            validatedMap.put(R.id.editPhone, false);
        }
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

        initValidationMapping();

        // event handlers
        // UID
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

        // E-mail
        editNewEmail.addTextChangedListener(new TextWatcher() {
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
                if (TextUtils.isEmpty(s)) {
                    updateValidation(
                            editNewEmail,
                            false,
                            true,
                            R.string.reg_email_empty);
                    return;
                }

                if (!EMAIL_CHECKER.matcher(s).matches()) {
                    updateValidation(
                            editNewEmail,
                            false,
                            true,
                            R.string.reg_email_malformed);
                    return;
                }

                updateValidation(editNewEmail, true, true, R.string.ok_short);
            }
        });

        // Password
        editNewPassword.addTextChangedListener(new TextWatcher() {
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
                if (TextUtils.isEmpty(s)) {
                    updateValidation(
                            editNewPassword,
                            false,
                            true,
                            R.string.reg_psw_empty);
                    return;
                }

                if (s.length() < 6) {
                    updateValidation(
                            editNewPassword,
                            false,
                            true,
                            R.string.reg_psw_too_short);
                    return;
                }

                updateValidation(
                        editNewPassword,
                        true,
                        true,
                        R.string.ok_short);

                updateRetypedPasswordCorrectness();
            }
        });

        // Retype password
        editRetypeNewPassword.addTextChangedListener(new TextWatcher() {
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
                updateRetypedPasswordCorrectness();
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

        checkIsEthnicMinority
            .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(
                        CompoundButton buttonView,
                        boolean isChecked) {
                    setEthnicMinority(isChecked);
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

        // is ethnic minority defaults to false
        checkIsEthnicMinority.setChecked(false);
        setEthnicMinority(false);

        // current phone number
        currentPhoneNumber = TelephonyHelper.getPhoneNumber(this);
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

    public boolean isUseCurrentPhone() {
        return checkUseCurrentPhone.isChecked();
    }

    public String getPhone() {
        return editPhone.getText().toString();
    }

    public synchronized void setUseCurrentPhone(boolean useCurrent) {
        editPhone.setEnabled(!useCurrent);
        editPhone.setVisibility(useCurrent ? View.GONE : View.VISIBLE);
    }

    public synchronized void setEthnicMinority(boolean isEthnicMinority) {
        textEthnicMinorityIndicator.setVisibility(isEthnicMinority
                ? View.VISIBLE
                : View.GONE);
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
                updateUIDAvailability(true, R.string.ok_short, timestamp);
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
                // Banned
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
        if (statusResId != 0) {
            updateValidation(editNewUID, ok, true, statusResId);
        } else {
            updateValidation(editNewUID, ok, false, "");
        }
    }

    protected synchronized void updateRetypedPasswordCorrectness() {
        if (!editNewPassword
            .getText()
            .toString()
            .equals(editRetypeNewPassword.getText().toString())) {
            updateValidation(
                    editRetypeNewPassword,
                    false,
                    true,
                    R.string.reg_psw_mismatch);
            return;
        }

        updateValidation(
                editRetypeNewPassword,
                true,
                true,
                R.string.ok_short);
    }

    protected void setValidationColor(GuidedEditText editText, boolean ok) {
        Resources res = this.getResources();
        editText.setGuideTextColor(ok ? res
            .getColorStateList(R.color.jnrain_green_dark) : res
            .getColorStateList(R.color.error_red));
    }

    protected void updateValidation(
            GuidedEditText editText,
            boolean ok,
            boolean visible,
            CharSequence text) {
        setValidationColor(editText, ok);
        editText.setGuideVisible(visible);
        editText.setGuideText(text);

        validatedMap.put(editText.getId(), ok);
        doValidation();
    }

    protected void updateValidation(
            GuidedEditText editText,
            boolean ok,
            boolean visible,
            int resId) {
        setValidationColor(editText, ok);
        editText.setGuideVisible(visible);
        editText.setGuideText(resId);

        validatedMap.put(editText.getId(), ok);
        doValidation();
    }

    protected void doValidation() {
        boolean passed = true;

        // fixed fields
        for (int itemId : FIXED_VALIDATION_FIELD_IDS) {
            if (!validatedMap.get(itemId)) {
                passed = false;
                break;
            }
        }

        // dynamic fields
        if (!isUseCurrentPhone() && !validatedMap.get(R.id.editPhone)) {
            passed = false;
        }

        btnSubmitRegister.setEnabled(passed);
    }
}
