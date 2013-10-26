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
package name.xen0n.cytosol.ui.preference;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import name.xen0n.cytosol.ui.preference.PreferenceListFragment.OnPreferenceAttachedListener;

import org.jnrain.mobile.R;

import roboguice.inject.InjectView;
import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceScreen;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.viewpagerindicator.TitlePageIndicator;


public abstract class CytosolSettingsActivity
        extends RoboSherlockFragmentActivity
        implements OnPreferenceAttachedListener, OnPreferenceChangeListener,
        OnPreferenceClickListener {
    @InjectView(R.id.pager)
    protected ViewPager viewPager;
    @InjectView(R.id.indicator)
    protected TitlePageIndicator indicator;

    public final Class<? extends BaseSettingsFragmentAdapter> SETTINGS_FRAGMENT_ADAPTER_CLASS;
    private Constructor<? extends BaseSettingsFragmentAdapter> fragAdapterCtor;

    BaseSettingsFragmentAdapter _adapter;

    protected CytosolSettingsActivity(
            final Class<? extends BaseSettingsFragmentAdapter> settingsFragmentAdapterClass) {
        SETTINGS_FRAGMENT_ADAPTER_CLASS = settingsFragmentAdapterClass;

        try {
            fragAdapterCtor = settingsFragmentAdapterClass.getConstructor(
                    FragmentManager.class,
                    Context.class);
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dyn_pages);

        try {
            _adapter = fragAdapterCtor.newInstance(
                    getSupportFragmentManager(),
                    getApplicationContext());
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        viewPager.setAdapter(_adapter);
        indicator.setViewPager(viewPager);
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
