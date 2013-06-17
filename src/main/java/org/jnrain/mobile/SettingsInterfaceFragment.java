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
package org.jnrain.mobile;

import org.jnrain.mobile.config.ConfigConstants;
import org.jnrain.mobile.util.PreferenceListFragment;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;


public class SettingsInterfaceFragment extends PreferenceListFragment {
    public SettingsInterfaceFragment(int xmlId) {
        super(xmlId);
    }

    // tracking of dependency on a ListPreference value must be done
    // programmatically, according to SO question
    // 3969807/listpreference-dependency
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ListPreference exitBehavior = (ListPreference) findPreference(ConfigConstants.EXIT_BEHAVIOR);
        final Preference exitDoubleclickTimeout = findPreference(ConfigConstants.EXIT_DOUBLECLICK_TIMEOUT);

        exitBehavior
            .setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(
                        Preference preference,
                        Object newValue) {
                    exitDoubleclickTimeout
                        .setEnabled(ConfigConstants.EXIT_DOUBLECLICK
                            .equals(newValue.toString()));
                    return true;
                }
            });
    }
}
