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
package org.jnrain.mobile.ui.preferences;

import java.util.HashMap;
import java.util.Iterator;

import name.xen0n.cytosol.config.UIConfigConstants;
import name.xen0n.cytosol.config.UpdaterConfigUtil;
import name.xen0n.cytosol.ui.preference.PreferenceListFragment;
import name.xen0n.cytosol.ui.preference.SeekBarPreference;
import name.xen0n.cytosol.ui.preference.SummarizedListPreference;
import name.xen0n.cytosol.updater.AppVersionHelper;
import name.xen0n.cytosol.updater.UpdateChannel;
import name.xen0n.cytosol.updater.UpdateInfo;

import org.jnrain.mobile.R;
import org.jnrain.mobile.config.ConfigHub;
import org.jnrain.mobile.util.GlobalState;

import android.app.Activity;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;


public class SettingsGeneralFragment extends PreferenceListFragment {
    protected ListPreference exitBehavior;
    protected SeekBarPreference exitDoubleclickTimeout;
    protected CheckBoxPreference enableAutoUpdate;
    protected SeekBarPreference updateCheckFreq;

    public SettingsGeneralFragment(int xmlId) {
        super(xmlId);
    }

    // tracking of dependency on a ListPreference value must be done
    // programmatically, according to SO question
    // 3969807/listpreference-dependency
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // dynamically add preference for update channel
        addUpdateChannelPreference();

        exitBehavior = (ListPreference) findPreference(UIConfigConstants.EXIT_BEHAVIOR);
        exitDoubleclickTimeout = (SeekBarPreference) findPreference(UIConfigConstants.EXIT_DOUBLECLICK_TIMEOUT);
        enableAutoUpdate = (CheckBoxPreference) findPreference(UpdaterConfigUtil.ENABLE_AUTO_CHECK);
        updateCheckFreq = (SeekBarPreference) findPreference(UpdaterConfigUtil.CHECK_FREQ);

        exitBehavior
            .setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(
                        Preference preference,
                        Object newValue) {
                    syncExitBehaviorDep(newValue.toString());
                    return true;
                }
            });

        enableAutoUpdate
            .setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(
                        Preference preference,
                        Object newValue) {
                    syncEnableAutoUpdateDep((Boolean) newValue);
                    return true;
                }
            });

        // initialize the dependency
        syncExitBehaviorDep(exitBehavior.getValue());
        syncEnableAutoUpdateDep(ConfigHub
            .getUpdaterUtil(getActivity())
            .isAutoCheckEnabled());
    }

    public synchronized void syncExitBehaviorDep(String newValue) {
        boolean enabled = UIConfigConstants.EXIT_DOUBLECLICK.equals(newValue
            .toString());
        exitDoubleclickTimeout.setEnabled(enabled);

        PreferenceScreen scr = getPreferenceScreen();
        if (enabled) {
            scr.addPreference(exitDoubleclickTimeout);
        } else {
            scr.removePreference(exitDoubleclickTimeout);
        }
    }

    public synchronized void syncEnableAutoUpdateDep(boolean newValue) {
        PreferenceScreen scr = getPreferenceScreen();
        if (newValue) {
            scr.addPreference(updateCheckFreq);
        } else {
            scr.removePreference(updateCheckFreq);
        }
    }

    public void addUpdateChannelPreference() {
        Activity activity = getActivity();
        UpdateInfo updInfo = GlobalState.getUpdateInfo();
        SummarizedListPreference pref = new SummarizedListPreference(
                activity);

        String[] names;
        String[] values;

        if (updInfo == null) {
            // never checked update, set to hardcoded values
            names = new String[] {
                    AppVersionHelper.getLocalizedNameForUpdateChannel(
                            activity,
                            "stable"),
                    AppVersionHelper.getLocalizedNameForUpdateChannel(
                            activity,
                            "next")
            };
            values = new String[] {
                    "stable",
                    "next"
            };
        } else {
            // generate channel list according to update info
            HashMap<String, UpdateChannel> channels = updInfo.getChannels();
            names = new String[channels.size()];
            values = new String[channels.size()];

            Iterator<String> iter = channels.keySet().iterator();
            int idx = 0;
            while (iter.hasNext()) {
                String name = iter.next();
                UpdateChannel chan = channels.get(name);

                names[idx] = chan.getLocalizedName(activity);
                values[idx] = name;

                idx++;
            }
        }

        // set up preference object
        pref.setTitle(R.string.prefs_updater_channel);
        pref.setDialogTitle(R.string.prefs_updater_channel_select);
        pref.setKey(UpdaterConfigUtil.CURRENT_UPDATE_CHANNEL);
        pref.setSummary("%s");
        pref.setOrder(3);

        pref.setEntries(names);
        pref.setEntryValues(values);
        pref.setDefaultValue(UpdaterConfigUtil.DEFAULT_UPDATE_CHANNEL);

        this.getPreferenceScreen().addPreference(pref);
    }
}
