/*
 * Copyright 2012 JNRain
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
package org.jnrain.mobile.util;

import java.util.ArrayList;

import org.jnrain.mobile.R;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.viewpagerindicator.IconPagerAdapter;


public abstract class DynPageFragmentAdapter<T> extends FragmentPagerAdapter
        implements IconPagerAdapter {
    protected ArrayList<T> _contents;

    public DynPageFragmentAdapter(FragmentManager fm) {
        super(fm);
        _contents = new ArrayList<T>();
    }

    @Override
    public Fragment getItem(int position) {
        return (Fragment) _contents.get(position);
    }

    @Override
    public int getCount() {
        return _contents.size();
    }

    @Override
    public abstract CharSequence getPageTitle(int position);

    @Override
    public int getIconResId(int index) {
        // TODO
        return R.drawable.icon;
    }

    public void addItem() {
        notifyDataSetChanged();
    }
}
