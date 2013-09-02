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
package org.jnrain.mobile;

import org.jnrain.luohua.entity.SimpleReturnCode;
import org.jnrain.mobile.config.ConfigHub;
import org.jnrain.mobile.config.LoginInfoUtil;
import org.jnrain.mobile.network.listeners.LoginRequestListener;
import org.jnrain.mobile.network.requests.LoginRequest;
import org.jnrain.mobile.ui.ux.DialogHelper;
import org.jnrain.mobile.util.GlobalState;
import org.jnrain.mobile.util.JNRainActivity;

import roboguice.inject.InjectView;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;


@SuppressLint("DefaultLocale")
public class LoginActivity extends JNRainActivity<SimpleReturnCode> {
    @InjectView(R.id.editUID)
    EditText editUID;
    @InjectView(R.id.editPassword)
    EditText editPassword;
    @InjectView(R.id.checkBoxIsRemember)
    CheckBox checkboxIsRemember;
    @InjectView(R.id.checkBoxIsAutoLogin)
    CheckBox checkboxIsAutoLogin;
    @InjectView(R.id.btnLogin)
    Button btnLogin;
    @InjectView(R.id.btnGuestLogin)
    Button btnGuestLogin;

    private static final String TAG = "LoginActivity";
    public LoginActivity loginActivity;
    private ProgressDialog loadingDlg;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loginActivity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // cookie manager
        synchronized (this) {
            if (!GlobalState.getCookieInited()) {
                CookieSyncManager.createInstance(getApplicationContext());
                GlobalState.setCookieInited(true);
            }
        }

        // login info
        final LoginInfoUtil loginInfoUtil = ConfigHub
            .getLoginInfoUtil(getApplicationContext());

        checkboxIsRemember
            .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(
                        CompoundButton buttonView,
                        boolean isChecked) {
                    loginInfoUtil.setRemember(isChecked);
                    if (!isChecked) {
                        checkboxIsAutoLogin.setChecked(isChecked);
                    }
                }
            });

        checkboxIsAutoLogin
            .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(
                        CompoundButton buttonView,
                        boolean isChecked) {
                    loginInfoUtil.setAutoLogin(isChecked);
                    if (isChecked) {
                        checkboxIsRemember.setChecked(isChecked);
                    }
                }
            });

        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Login button clicked");

                doLogin(
                        editUID.getText().toString().toLowerCase(),
                        editPassword.getText().toString());
            }
        });

        btnGuestLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Guest login button clicked");

                doLogin(LoginInfoUtil.GUEST_UID, LoginInfoUtil.GUEST_PSW);
            }
        });
        setUpLoginConfig();
    }

    private void setUpLoginConfig() {
        final LoginInfoUtil loginInfoUtil = ConfigHub
            .getLoginInfoUtil(getApplicationContext());
        final String uid = loginInfoUtil.getUserID();
        final String psw = loginInfoUtil.getUserPSW();

        checkboxIsRemember.setChecked(loginInfoUtil.isRememberLoginInfo());
        checkboxIsAutoLogin.setChecked(loginInfoUtil.isAutoLogin());
        if (uid != null
                && (!LoginInfoUtil.GUEST_UID.equals(uid.toLowerCase()))
                && psw != null) {
            if (loginInfoUtil.isRememberLoginInfo()) {
                editUID.setText(uid);
                editPassword.setText(psw);
            }
        }
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                if (uid != null
                        && (!LoginInfoUtil.GUEST_UID.equals(uid
                            .toLowerCase())) && psw != null) {
                    if (loginInfoUtil.isAutoLogin()) {
                        doLogin(uid.toLowerCase(), psw);
                    }
                }
                return null;
            }
        }.execute((Void) null);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                loadingDlg = DialogHelper.showProgressDialog(
                        LoginActivity.this,
                        R.string.login_dlg_title,
                        R.string.please_wait,
                        false,
                        false);
            }
        };
    }

    public void doLogin(final String uid, final String psw) {
        mHandler.sendMessage(new Message());
        spiceManager.execute(
                new LoginRequest(uid, psw),
                new LoginRequestListener(loginActivity, uid, psw));
    }

    public ProgressDialog getLoadingDialog() {
        return loadingDlg;
    }
}
