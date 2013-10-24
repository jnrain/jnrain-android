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
package name.xen0n.cytosol.config;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class ConfigManager {
    private SharedPreferences appPreferences;
    private SharedPreferences.Editor appPreferencesEditor;

    public ConfigManager(Context context) {
        initStates(PreferenceManager.getDefaultSharedPreferences(context));
    }

    public ConfigManager(String name, Context context) {
        initStates(context.getSharedPreferences(name, Context.MODE_PRIVATE));
    }

    public ConfigManager(String name, int mode, Context context) {
        initStates(context.getSharedPreferences(name, mode));
    }

    @SuppressLint("CommitPrefEdits")
    protected void initStates(SharedPreferences prefs) {
        appPreferences = prefs;
        appPreferencesEditor = appPreferences.edit();
    }

    public SharedPreferences.Editor getAppPreferencesEditor() {
        return appPreferencesEditor;
    }

    public SharedPreferences getAppPreferences() {
        return appPreferences;
    }
}
