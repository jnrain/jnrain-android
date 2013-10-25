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
package name.xen0n.cytosol.util;

import name.xen0n.cytosol.updater.AppVersionHelper;
import name.xen0n.cytosol.updater.UpdateInfo;
import android.content.Context;


public class GlobalUpdaterState {
    protected static boolean _versionInited = false;
    protected static int _versionCode = -1;
    protected static String _versionName = "VERSION_NOT_INITED";
    protected static UpdateInfo _updInfo = null;

    public static synchronized void possiblyInitState(Context ctx) {
        // init global version info
        AppVersionHelper.ensureVersionInited(ctx);
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
