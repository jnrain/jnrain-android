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
package org.jnrain.mobile.config;

import android.content.Context;


public class LoginInfoUtil extends BaseConfigUtil {
    public final static String USER_ID = "user_id";
    public final static String USER_PSW = "user_psw";
    public final static String IS_REMEMBER_LOGIN_INFO = "is_remember_login_info";
    public final static String IS_AUTO_LOGIN = "is_auto_login";

    protected LoginInfoUtil(Context context) {
        super(context);
    }

    public String getUserID() {
        return getString(USER_ID);
    }

    public void saveUserID(String value) {
        setString(USER_ID, value);
    }

    public String getUserPSW() {
        return getString(USER_PSW);
    }

    public void saveUserPSW(String value) {
        setString(USER_PSW, value);
    }

    public boolean isRememberLoginInfo() {
        return getBoolean(IS_REMEMBER_LOGIN_INFO);
    }

    public void setRemember(boolean value) {
        setBoolean(IS_REMEMBER_LOGIN_INFO, value);
    }

    public boolean isAutoLogin() {
        return getBoolean(IS_AUTO_LOGIN);
    }

    public void setAutoLogin(boolean value) {
        setBoolean(IS_AUTO_LOGIN, value);
    }
}
