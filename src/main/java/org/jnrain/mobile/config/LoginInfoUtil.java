package org.jnrain.mobile.config;

import android.content.Context;

public class LoginInfoUtil extends BaseConfigUtil{
    protected LoginInfoUtil(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public final static String USER_ID = "user_id";
    public final static String USER_PSW = "user_psw";
    public final static String IS_REMEMBER_LOGIN_INFO = "is_remember_login_info";
    
    public String getUserID() {
        return getString(USER_ID);
    }

    public void setUserID(String value) {
        setString(USER_ID, value);
    }

    public String getUserPSW() {
        return getString(USER_PSW);
    }

    public void setUserPSW(String value) {
        setString(USER_PSW, value);
    }

    public boolean isRememberLoginInfo() {
        return getBoolean(IS_REMEMBER_LOGIN_INFO);
    }

    public void setRemember(boolean value) {
        setBoolean(IS_REMEMBER_LOGIN_INFO, value);
    }
}
