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

import java.util.Date;

import android.content.Context;


public class UpdaterConfigUtil extends BaseConfigUtil {
    public static final String CONFIG_FILE = "updater";
    public static final String CURRENT_UPDATE_CHANNEL = "curr_upd_channel";
    public static final String DEFAULT_UPDATE_CHANNEL = "stable";
    public static final String LAST_CHECK_TIME = "last_check_time";
    public static final String CHECK_FREQ = "update_check_freq";

    public static final int DEFAULT_CHECK_FREQ = 7;

    public BaseConfigUtil _default_config;

    protected UpdaterConfigUtil(Context context) {
        super(CONFIG_FILE, context);
        _default_config = new BaseConfigUtil(context);
    }

    public String getCurrentUpdateChannel() {
        return _default_config.getString(
                CURRENT_UPDATE_CHANNEL,
                DEFAULT_UPDATE_CHANNEL);
    }

    public void setCurrentUpdateChannel(String value) {
        _default_config.setString(CURRENT_UPDATE_CHANNEL, value);
    }

    public Date getLastCheckTime() {
        return new Date(getLong(LAST_CHECK_TIME));
    }

    public void setLastCheckTime(long value) {
        setLong(LAST_CHECK_TIME, value);
    }

    public void setLastCheckTime() {
        setLastCheckTime(System.currentTimeMillis());
    }

    public int getCheckFreq() {
        return _default_config.getInt(CHECK_FREQ, DEFAULT_CHECK_FREQ);
    }

    public void setCheckFreq(int value) {
        _default_config.setInt(CHECK_FREQ, value);
    }
}
