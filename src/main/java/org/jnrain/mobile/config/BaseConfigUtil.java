package org.jnrain.mobile.config;

import android.content.Context;
import android.content.SharedPreferences;

public class BaseConfigUtil {
    protected SharedPreferences _preferences;
    protected SharedPreferences.Editor _preferencesEditor;
    protected ConfigManager configManager;
    protected BaseConfigUtil(Context context) {
        // TODO Auto-generated constructor stub
        configManager =  ConfigManager.getConfigManager(context);
        _preferences = configManager.getAppPreferences();
        _preferencesEditor = configManager.getAppPreferencesEditor();
    }
    
    protected String getString(String key) {
        return _preferences.getString(key, null);
    }
    
    protected boolean getBoolean(String key) {
        return _preferences.getBoolean(key, (Boolean) null);
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
