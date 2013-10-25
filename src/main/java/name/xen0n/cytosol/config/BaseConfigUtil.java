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

import android.content.Context;
import android.content.SharedPreferences;


public class BaseConfigUtil {
    protected SharedPreferences _preferences;
    protected SharedPreferences.Editor _preferencesEditor;
    protected ConfigManager configManager;

    protected BaseConfigUtil(Context context) {
        initState(ConfigHub.getConfigManager(context));
    }

    protected BaseConfigUtil(String configName, Context context) {
        initState(ConfigHub.getConfigManager(configName, context));
    }

    protected void initState(ConfigManager mgr) {
        configManager = mgr;
        _preferences = configManager.getAppPreferences();
        _preferencesEditor = configManager.getAppPreferencesEditor();
    }

    protected String getString(String key) {
        return getString(key, null);
    }

    protected String getString(String key, String defValue) {
        return _preferences.getString(key, defValue);
    }

    protected boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    protected boolean getBoolean(String key, boolean defValue) {
        return _preferences.getBoolean(key, defValue);
    }

    protected int getInt(String key) {
        return getInt(key, (Integer) null);
    }

    protected int getInt(String key, int defValue) {
        return _preferences.getInt(key, defValue);
    }

    protected long getLong(String key) {
        return getLong(key, (Long) null);
    }

    protected long getLong(String key, long defValue) {
        return _preferences.getLong(key, defValue);
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
