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

import java.text.MessageFormat;

import org.jnrain.mobile.network.LoginRequest;
import org.jnrain.mobile.util.GlobalState;
import org.jnrain.mobile.util.SpicedRoboActivity;
import org.jnrain.weiyu.entity.SimpleReturnCode;

import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;


public class LoginActivity extends SpicedRoboActivity<SimpleReturnCode> {
    @InjectView(R.id.editUID)
    EditText editUID;
    @InjectView(R.id.editPassword)
    EditText editPassword;
    @InjectView(R.id.btnLogin)
    Button btnLogin;
    @InjectView(R.id.btnGuestLogin)
    Button btnGuestLogin;

    @InjectResource(R.string.msg_network_fail)
    public String MSG_NETWORK_FAIL;
    @InjectResource(R.string.msg_unknown_status)
    public String MSG_UNKNOWN_STATUS;
    @InjectResource(R.string.msg_login_success)
    public String MSG_LOGIN_SUCCESS;
    @InjectResource(R.string.msg_login_no_uid)
    public String MSG_LOGIN_NO_UID;
    @InjectResource(R.string.msg_login_fail)
    public String MSG_LOGIN_FAIL;
    @InjectResource(R.string.msg_login_toomany)
    public String MSG_LOGIN_TOOMANY;
    @InjectResource(R.string.msg_login_disabled)
    public String MSG_LOGIN_DISABLED;
    @InjectResource(R.string.msg_login_ip_denied)
    public String MSG_LOGIN_IP_DENIED;
    @InjectResource(R.string.msg_login_freq)
    public String MSG_LOGIN_FREQ;
    @InjectResource(R.string.msg_login_capacity)
    public String MSG_LOGIN_CAPACITY;
    @InjectResource(R.string.msg_login_ipacl)
    public String MSG_LOGIN_IPACL;

    private static final String TAG = "LoginActivity";
    public static final String GUEST_UID = "guest";
    public static final String GUEST_PSW = "";

    private String _uid;
    private String _psw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        _uid = "";
        _psw = "";

        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Login button clicked");

                String uid = editUID.getText().toString();

                // remember uid
                _uid = uid.toLowerCase();
                _psw = editPassword.getText().toString();

                spiceManager.execute(
                        new LoginRequest(_uid, _psw),
                        new LoginRequestListener());
            }
        });

        btnGuestLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Guest login button clicked");

                _uid = GUEST_UID;
                _psw = GUEST_PSW;

                spiceManager.execute(
                        new LoginRequest(GUEST_UID, GUEST_PSW),
                        new LoginRequestListener());
            }
        });
    }

    private class LoginRequestListener
            implements RequestListener<SimpleReturnCode> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Log.d(TAG, "err on req: " + spiceException.toString());
            Toast.makeText(
                    getApplicationContext(),
                    MSG_NETWORK_FAIL,
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(SimpleReturnCode result) {
            int status = result.getStatus();

            switch (status) {
                case 0:
                    // record username in global state
                    assert _uid.length() > 0;
                    GlobalState.setUserName(_uid);

                    // successful
                    Toast.makeText(
                            getApplicationContext(),
                            MSG_LOGIN_SUCCESS,
                            Toast.LENGTH_SHORT).show();

                    // go to hot posts activity
                    Intent intent = new Intent();
                    intent.setClass(
                            LoginActivity.this,
                            GlobalHotPostsListActivity.class);
                    startActivity(intent);

                    // finish off self
                    finish();

                    break;
                case 1:
                    Toast.makeText(
                            getApplicationContext(),
                            MSG_LOGIN_FAIL,
                            Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(
                            getApplicationContext(),
                            MSG_LOGIN_NO_UID,
                            Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(
                            getApplicationContext(),
                            MSG_LOGIN_TOOMANY,
                            Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(
                            getApplicationContext(),
                            MSG_LOGIN_DISABLED,
                            Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    Toast.makeText(
                            getApplicationContext(),
                            MSG_LOGIN_IP_DENIED,
                            Toast.LENGTH_SHORT).show();
                    break;
                case 6:
                    Toast.makeText(
                            getApplicationContext(),
                            MSG_LOGIN_FREQ,
                            Toast.LENGTH_SHORT).show();
                    break;
                case 7:
                    Toast.makeText(
                            getApplicationContext(),
                            MSG_LOGIN_CAPACITY,
                            Toast.LENGTH_SHORT).show();
                    break;
                case 8:
                    Toast.makeText(
                            getApplicationContext(),
                            MSG_LOGIN_IPACL,
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast
                        .makeText(
                                getApplicationContext(),
                                MessageFormat.format(
                                        MSG_UNKNOWN_STATUS,
                                        status),
                                Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
