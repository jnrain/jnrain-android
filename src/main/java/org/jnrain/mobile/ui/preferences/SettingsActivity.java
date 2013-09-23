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

import org.jnrain.mobile.R;
import org.jnrain.mobile.R.id;
import org.jnrain.mobile.R.layout;
import org.jnrain.mobile.ui.preferences.PreferenceListFragment.OnPreferenceAttachedListener;

import roboguice.inject.InjectView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceScreen;
import android.support.v4.view.ViewPager;

import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitlePageIndicator.IndicatorStyle;


public class SettingsActivity extends RoboSherlockFragmentActivity
        implements OnPreferenceAttachedListener, OnPreferenceChangeListener,
        OnPreferenceClickListener {
    @InjectView(R.id.pager)
    ViewPager viewPager;
    @InjectView(R.id.indicator)
    TitlePageIndicator indicator;

    SettingsFragmentAdapter _adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dyn_pages);
        _adapter = new SettingsFragmentAdapter(
                getSupportFragmentManager(),
                getApplicationContext());
        viewPager.setAdapter(_adapter);
        indicator.setViewPager(viewPager);
        indicator.setFooterIndicatorStyle(IndicatorStyle.Underline);
    }

    static void show(Context context) {
        final Intent intent = new Intent(context, SettingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void onPreferenceAttached(PreferenceScreen root, int xmlId) {
        // TODO Auto-generated method stub
    }
}
