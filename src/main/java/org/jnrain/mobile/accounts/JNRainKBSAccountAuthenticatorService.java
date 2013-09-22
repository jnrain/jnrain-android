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
package org.jnrain.mobile.accounts;

import android.accounts.AccountManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


public class JNRainKBSAccountAuthenticatorService extends Service {

    private static JNRainKBSAccountAuthenticator _authenticator;

    public IBinder onBind(Intent intent) {
        return intent.getAction().equals(
                AccountManager.ACTION_AUTHENTICATOR_INTENT)
                ? getAuthenticator().getIBinder()
                : null;
    }

    private JNRainKBSAccountAuthenticator getAuthenticator() {
        if (_authenticator == null)
            _authenticator = new JNRainKBSAccountAuthenticator(this);
        return _authenticator;
    }
}
