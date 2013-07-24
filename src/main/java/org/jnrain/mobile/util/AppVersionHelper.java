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

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;


public class AppVersionHelper {
    public static final String CHANNEL_NAME_RES_PREFIX = "update_chan_name_";

    public static void ensureVersionInited(Activity activity) {
        // init global version info if not done yet
        if (!GlobalState.isVersionInited()) {
            PackageManager mgr = activity.getPackageManager();

            try {
                PackageInfo pkg = mgr.getPackageInfo(
                        activity.getPackageName(),
                        0);

                GlobalState.setVersionInfo(pkg.versionCode, pkg.versionName);
            } catch (NameNotFoundException e) {
                // Wwwwhat? You say my package is NOT FOUND when it's
                // currently RUNNING?
                // Put something really bizarre here...
                GlobalState.setVersionInfo(0, "VERSION-INIT-FAILED");
                e.printStackTrace();
            }
        }
    }

    public static String getLocalizedNameForUpdateChannel(
            Activity activity,
            String name) {
        // SO question
        // 6999866/access-a-resource-name-programmatically
        String resourceName = CHANNEL_NAME_RES_PREFIX + name;
        Resources res = activity.getResources();
        int id = res.getIdentifier(
                resourceName,
                "string",
                activity.getPackageName());

        return res.getString(id);
    }
}
