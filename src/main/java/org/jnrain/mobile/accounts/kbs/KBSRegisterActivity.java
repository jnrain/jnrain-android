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
import org.jnrain.mobile.ui.ux.DialogHelper;
import org.jnrain.mobile.ui.ux.FormatHelper;

import roboguice.inject.InjectView;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.LinkMovementMethod;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;


public class KBSRegisterActivity extends JNRainActivity {
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
    @InjectView(R.id.editStudID)
    EditText editStudID;
    @InjectView(R.id.editRealName)
    EditText editRealName;
    @InjectView(R.id.checkIsEthnicMinority)
    CheckBox checkIsEthnicMinority;
    @InjectView(R.id.btnSubmitRegister)
    Button btnSubmitRegister;

    // private static final String TAG = "KBSRegisterActivity";

    private ProgressDialog loadingDlg;
    private Handler mHandler;

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

        // HTML-formatted register disclaimer
        FormatHelper.setHtmlText(
                this,
                textRegisterDisclaimer,
                R.string.register_disclaimer);
        textRegisterDisclaimer.setMovementMethod(LinkMovementMethod
            .getInstance());
    }

    public ProgressDialog getLoadingDialog() {
        return loadingDlg;
    }
}
