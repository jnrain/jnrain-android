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
import android.content.SharedPreferences;

public class ConfigManager {
    private final static String APP_CONFIG = "config";
    
    private static Context mContext;
    private static ConfigManager configManager;
    private static SharedPreferences appPreferences;

    private static SharedPreferences.Editor appPreferencesEditor;

    public static ConfigManager getConfigManager(Context context) {
        if (configManager == null) {
            configManager = new ConfigManager();
            mContext = context;
            appPreferences = mContext.getSharedPreferences(APP_CONFIG, Context.MODE_PRIVATE);
            appPreferencesEditor = appPreferences.edit();

        }
        return configManager;
    }
    
    private ConfigManager(){};
    
    public SharedPreferences.Editor getAppPreferencesEditor() {
        return appPreferencesEditor;
    }
    
    public SharedPreferences getAppPreferences() {
        return appPreferences;
    }
    
    public LoginInfoUtil createLoginInfoUtil(Context context) {
        return new LoginInfoUtil(context);       
    }
}
