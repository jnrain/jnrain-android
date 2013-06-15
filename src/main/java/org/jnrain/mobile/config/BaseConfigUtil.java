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


public class BaseConfigUtil {
    protected SharedPreferences _preferences;
    protected SharedPreferences.Editor _preferencesEditor;
    protected ConfigManager configManager;

    protected BaseConfigUtil(Context context) {
        configManager = ConfigManager.getConfigManager(context);
        _preferences = configManager.getAppPreferences();
        _preferencesEditor = configManager.getAppPreferencesEditor();
    }

    protected String getString(String key) {
        return _preferences.getString(key, null);
    }

    protected boolean getBoolean(String key) {
        return _preferences.getBoolean(key, false);
    }

    protected int getInt(String key) {
        return _preferences.getInt(key, (Integer) null);
    }

    protected long getLong(String key) {
        return _preferences.getLong(key, (Long) null);
    }

    protected void setString(String key, String value) {
        _preferencesEditor.putString(key, value);
        _preferencesEditor.commit();
    }

    protected void setBoolean(String key, boolean value) {
        _preferencesEditor.putBoolean(key, value);
        _preferencesEditor.commit();
    }

    protected void setInt(String key, int value) {
        _preferencesEditor.putInt(key, value);
        _preferencesEditor.commit();
    }

    protected void setLong(String key, long value) {
        _preferencesEditor.putLong(key, value);
        _preferencesEditor.commit();
    }

    protected void removeConfig(String key) {
        _preferencesEditor.remove(key);
    }
}
