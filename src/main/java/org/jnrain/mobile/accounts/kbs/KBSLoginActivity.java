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

import org.jnrain.kbs.entity.SimpleReturnCode;
import org.jnrain.mobile.R;
import org.jnrain.mobile.accounts.AccountConstants;
import org.jnrain.mobile.ui.base.JNRainAccountAuthenticatorActivity;
import org.jnrain.mobile.ui.base.LoginPoint;
import org.jnrain.mobile.ui.ux.DialogHelper;
import org.jnrain.mobile.ui.ux.ToastHelper;
import org.jnrain.mobile.util.GlobalState;

import roboguice.inject.InjectView;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


@SuppressLint("DefaultLocale")
public class KBSLoginActivity
        extends JNRainAccountAuthenticatorActivity<SimpleReturnCode>
        implements LoginPoint {
    public static final String PARAM_AUTHTOKEN_TYPE = "org.jnrain.mobile.authtoken";
    public static final String PARAM_USERNAME = "org.jnrain.mobile.username";
    public static final String PARAM_CONFIRM_CREDENTIALS = "org.jnrain.mobile.confirm_creds";

    @InjectView(R.id.editUID)
    EditText editUID;
    @InjectView(R.id.editPassword)
    EditText editPassword;
    @InjectView(R.id.btnLogin)
    Button btnLogin;
    @InjectView(R.id.btnRegister)
    Button btnRegister;

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
        setContentView(R.layout.frag_kbs_login);

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

        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Login button clicked");

                doLogin(
                        editUID.getText().toString().toLowerCase(),
                        editPassword.getText().toString());
            }
        });

        btnRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                KBSRegisterActivity.show(KBSLoginActivity.this);
            }
        });

        setUpLoginConfig();
    }

    private void setUpLoginConfig() {
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
                new KBSLoginRequestListener(loginActivity, null, uid, psw));
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

    public void onAuthenticationSuccess(
            Account account,
            String uid,
            String psw) {
        assert uid.length() > 0;
        assert account == null;

        // successful
        ToastHelper.makeTextToast(this, R.string.msg_login_success);

        if (confirmCredentials) {
            finishConfirmCredentials(uid, psw, true);
            return;
        }

        Account acct = new Account(uid, AccountConstants.ACCOUNT_TYPE_KBS);
        if (requestNewAccount) {
            accountManager.addAccountExplicitly(acct, psw, null);
        } else {
            accountManager.setPassword(acct, psw);
        }

        // avoid repeated login on subsequent main activity launch
        GlobalState.setAccount(acct);
        finishLogin(uid, psw);
    }
}
