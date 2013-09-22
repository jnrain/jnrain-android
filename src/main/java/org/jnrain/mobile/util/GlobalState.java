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
package org.jnrain.mobile.util;

import org.jnrain.mobile.updater.UpdateInfo;

import android.content.Context;
import android.webkit.CookieSyncManager;


public class GlobalState {
    protected static boolean _cookieInited = false;
    protected static boolean _versionInited = false;
    protected static int _versionCode = -1;
    protected static String _versionName = "VERSION_NOT_INITED";
    protected static UpdateInfo _updInfo = null;

    protected static String _userName = "";

    public static synchronized void possiblyInitState(Context ctx) {
        // init global version info
        AppVersionHelper.ensureVersionInited(ctx);

        // cookie manager
        if (!GlobalState.getCookieInited()) {
            CookieSyncManager.createInstance(ctx);
            GlobalState.setCookieInited(true);
        }
    }

    public static synchronized boolean getCookieInited() {
        return _cookieInited;
    }

    public static void setCookieInited(boolean inited) {
        _cookieInited = inited;
    }

    public static synchronized String getUserName() {
        return _userName;
    }

    public static synchronized void setUserName(String userName) {
        _userName = userName;
    }

    public static synchronized boolean isVersionInited() {
        return _versionInited;
    }

    public static synchronized int getVersionCode() {
        return _versionCode;
    }

    public static synchronized String getVersionName() {
        return _versionName;
    }

    public static synchronized UpdateInfo getUpdateInfo() {
        return _updInfo;
    }

    public static synchronized void setVersionInfo(
            int versionCode,
            String versionName,
            UpdateInfo updInfo) {
        _versionCode = versionCode;
        _versionName = versionName;
        _updInfo = updInfo;
        _versionInited = true;
    }

    public static synchronized void setUpdateInfo(UpdateInfo updInfo) {
        _updInfo = updInfo;
    }
}
