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

import org.jnrain.luohua.entity.SimpleReturnCode;
import org.jnrain.mobile.R;
import org.jnrain.mobile.accounts.AccountConstants;
import org.jnrain.mobile.config.ConfigHub;
import org.jnrain.mobile.config.LoginInfoUtil;
import org.jnrain.mobile.ui.ux.DialogHelper;
import org.jnrain.mobile.ui.ux.ToastHelper;
import org.jnrain.mobile.util.GlobalState;
import org.jnrain.mobile.util.JNRainAccountAuthenticatorActivity;

import roboguice.inject.InjectView;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;


@SuppressLint("DefaultLocale")
public class KBSLoginActivity
        extends JNRainAccountAuthenticatorActivity<SimpleReturnCode> {
    public static final String PARAM_AUTHTOKEN_TYPE = "org.jnrain.mobile.authtoken";
    public static final String PARAM_USERNAME = "org.jnrain.mobile.username";
    public static final String PARAM_CONFIRM_CREDENTIALS = "org.jnrain.mobile.confirm_creds";

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
    public KBSLoginActivity loginActivity;
    private ProgressDialog loadingDlg;
    private Handler mHandler;

    // Authentication framework things
    private AccountManager accountManager;
    private String authTokenType;
    private String userName;
    private boolean requestNewAccount;
    private boolean confirmCredentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loginActivity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // account manager
        accountManager = AccountManager.get(this);

        Intent intent = getIntent();
        userName = intent.getStringExtra(PARAM_USERNAME);
        authTokenType = intent.getStringExtra(PARAM_AUTHTOKEN_TYPE);
        requestNewAccount = userName == null;
        confirmCredentials = intent.getBooleanExtra(
                PARAM_CONFIRM_CREDENTIALS,
                false);

        if (!TextUtils.isEmpty(userName)) {
            // set username to fixed
            editUID.setText(userName);
            editUID.setEnabled(false);
            editUID.setFocusable(false);
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
                        KBSLoginActivity.this,
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
                new KBSLoginRequest(uid, psw),
                new KBSLoginRequestListener(loginActivity, uid, psw));
    }

    public ProgressDialog getLoadingDialog() {
        return loadingDlg;
    }

    /* Authentication things */
    protected void finishConfirmCredentials(
            String uid,
            String psw,
            boolean result) {
        final Account account = new Account(
                uid,
                AccountConstants.ACCOUNT_TYPE_KBS);

        accountManager.setPassword(account, psw);

        final Intent intent = new Intent();
        intent.putExtra(AccountManager.KEY_BOOLEAN_RESULT, result);

        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }

    protected void finishLogin(final String uid, final String psw) {
        final Intent intent = new Intent();

        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, uid);
        intent.putExtra(
                AccountManager.KEY_ACCOUNT_TYPE,
                AccountConstants.ACCOUNT_TYPE_KBS);

        if (AccountConstants.ACCOUNT_TYPE_KBS.equals(authTokenType))
            intent.putExtra(AccountManager.KEY_AUTHTOKEN, psw);

        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }

    public void onAuthenticationSuccess(String uid, String psw) {
        // record user name in global state
        assert uid.length() > 0;
        GlobalState.setUserName(uid);

        // successful
        ToastHelper.makeTextToast(this, R.string.msg_login_success);

        // save login info
        LoginInfoUtil loginInfoUtil = ConfigHub
            .getLoginInfoUtil(getApplicationContext());
        if (!LoginInfoUtil.GUEST_UID.equals(uid.toLowerCase())) {
            if (loginInfoUtil.isRememberLoginInfo()) {
                loginInfoUtil.saveUserID(uid);
                loginInfoUtil.saveUserPSW(psw);
            }
        }

        if (confirmCredentials) {
            finishConfirmCredentials(uid, psw, true);
            return;
        }

        Account account = new Account(uid, AccountConstants.ACCOUNT_TYPE_KBS);
        if (requestNewAccount) {
            accountManager.addAccountExplicitly(account, psw, null);
        } else {
            accountManager.setPassword(account, psw);
        }

        finishLogin(uid, psw);

        /*
         * // go to hot posts activity Intent intent = new Intent();
         * intent.setClass(this, GlobalHotPostsListActivity.class);
         * startActivity(intent);
         * 
         * // finish off self finish();
         */
    }
}
