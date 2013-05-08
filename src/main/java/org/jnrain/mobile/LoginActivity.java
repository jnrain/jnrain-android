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

import org.jnrain.mobile.network.LoginRequest;
import org.jnrain.mobile.network.listeners.LoginRequestListener;
import org.jnrain.mobile.util.GlobalState;
import org.jnrain.mobile.util.SpicedRoboActivity;
import org.jnrain.weiyu.entity.SimpleReturnCode;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.EditText;


public class LoginActivity extends SpicedRoboActivity<SimpleReturnCode> {
    @InjectView(R.id.editUID)
    EditText editUID;
    @InjectView(R.id.editPassword)
    EditText editPassword;
    @InjectView(R.id.btnLogin)
    Button btnLogin;
    @InjectView(R.id.btnGuestLogin)
    Button btnGuestLogin;

    private static final String TAG = "LoginActivity";
    public static final String GUEST_UID = "guest";
    public static final String GUEST_PSW = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // cookie manager
        synchronized (this) {
            if (!GlobalState.getCookieInited()) {
                CookieSyncManager.createInstance(getApplicationContext());
                GlobalState.setCookieInited(true);
            }
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

        btnGuestLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Guest login button clicked");

                doLogin(GUEST_UID, GUEST_PSW);
            }
        });
    }

    public void doLogin(String uid, String psw) {
        spiceManager.execute(
                new LoginRequest(uid, psw),
                new LoginRequestListener(this, uid));
    }
}
