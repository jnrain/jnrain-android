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

import java.util.ArrayList;

import org.jnrain.mobile.util.DynPageFragmentAdapter;
import org.jnrain.mobile.util.PreferenceListFragment;

import android.content.Context;
import android.support.v4.app.FragmentManager;


public class SettingsFragmentAdapter
        extends DynPageFragmentAdapter<PreferenceListFragment> {
    Context _ctx;
    ArrayList<String> _titles;

    public SettingsFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        _ctx = context;
        _titles = new ArrayList<String>();

        // initialize fragments
        addItem(R.xml.preferences, R.string.preference_user_configuration);
    }

    @Override
    public String getPageTitle(int position) {
        return _titles.get(position);
    }

    public void addItem(int xmlId, int titleResId) {
        PreferenceListFragment frag = new PreferenceListFragment(xmlId);

        // retain state
        frag.setRetainInstance(true);

        _contents.add(frag);
        _titles.add(_ctx.getString(titleResId));

        notifyDataSetChanged();
    }
}
