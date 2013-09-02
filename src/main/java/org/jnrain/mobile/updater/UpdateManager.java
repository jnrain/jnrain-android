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
package org.jnrain.mobile.updater;

import java.text.MessageFormat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;


public class UpdateManager {
    /**
     * Filename of update info cache. 升级信息缓存的文件名.
     */
    public static final String UPDATE_INFO_CACHE_FILENAME = "updates.json";

    /**
     * Base URL for update packages. 升级包的 URL 基准.
     */
    public static final String UPDATE_URL_BASE = "http://dl.jnrain.com/android/";

    /**
     * Format string for APK filename. APK 文件名的格式.
     */
    public static final String UPDATE_APK_NAME = "jnrain-android-{0}.apk";

    /**
     * Returns the canonical filename of this app's official download.
     * 返回本应用官方下载使用的标准文件名.
     * 
     * @param version
     *            app version requested
     * @return a String of canonical apk filename containing version
     */
    public static String getApkNameForVersion(String version) {
        return MessageFormat.format(UPDATE_APK_NAME, version);
    }

    /**
     * Format a URL that points to a specific version of this app.
     * 格式化一个指向本应用某特定版本的 URL.
     * 
     * @param version
     *            app version requested
     * @return URL for the .apk file
     */
    public static String getApkURLForVersion(String version) {
        return UPDATE_URL_BASE + getApkNameForVersion(version);
    }

    /**
     * Open in the browser the .apk file for the specified app version.
     * 在浏览器中打开指定版本的本应用 .apk 文件.
     * 
     * @param ctx
     *            Android context to execute in
     * @param version
     *            app version requested
     */
    public static void openApkInBrowser(Context ctx, String version) {
        Intent intent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(getApkURLForVersion(version)));
        ctx.startActivity(intent);
    }
}
