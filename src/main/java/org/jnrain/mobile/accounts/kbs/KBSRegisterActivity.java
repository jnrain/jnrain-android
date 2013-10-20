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

import org.jnrain.mobile.R;
import org.jnrain.mobile.ui.base.JNRainActivity;
import org.jnrain.mobile.ui.base.RegisterPoint;
import org.jnrain.mobile.ui.ux.DialogHelper;
import org.jnrain.mobile.ui.ux.FormatHelper;
import org.jnrain.mobile.util.GlobalState;

import roboguice.inject.InjectView;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


@SuppressWarnings("rawtypes")
public class KBSRegisterActivity extends JNRainActivity
        implements RegisterPoint {
    public static final String CAPTCHA_BASE_URL = "http://bbs.jnrain.com/wForum/img_rand/";

    @InjectView(R.id.textRegisterDisclaimer)
    TextView textRegisterDisclaimer;
    @InjectView(R.id.editNewUID)
    EditText editNewUID;
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

    public static void show(Context context) {
        final Intent intent = new Intent(context, KBSRegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @SuppressWarnings("unchecked")
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

        // event handlers
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

        // init captcha helper
        captchaHelper = new KBSCaptchaHelper(this, imageRegCaptcha);

        // issue preflight request
        // load captcha in success callback
        this.makeSpiceRequest(
                new KBSRegisterRequest(),
                new KBSRegisterRequestListener(this));
    }

    public ProgressDialog getLoadingDialog() {
        return loadingDlg;
    }

    public synchronized void setUseCurrentPhone(boolean useCurrent) {
        editPhone.setEnabled(!useCurrent);
        editPhone.setVisibility(useCurrent ? View.GONE : View.VISIBLE);
    }

    @Override
    public void fetchCaptcha() {
        captchaHelper.doFetchCaptcha();
    }
}
