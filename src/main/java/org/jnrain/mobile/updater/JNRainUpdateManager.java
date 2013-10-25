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

import name.xen0n.cytosol.updater.BaseUpdateManager;


public class JNRainUpdateManager extends BaseUpdateManager {
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

    public JNRainUpdateManager() {
        super(UPDATE_INFO_CACHE_FILENAME, UPDATE_URL_BASE, UPDATE_APK_NAME);
    }
}
