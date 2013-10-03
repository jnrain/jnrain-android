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
package org.jnrain.mobile.ui.nav;

import java.util.ArrayList;
import java.util.List;

import org.jnrain.mobile.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


public class NavMenuAdapter extends BaseAdapter {
    // private static final String TAG = "NavMenuAdapter";
    private LayoutInflater _inflater;
    private List<NavItem> _data;

    public NavMenuAdapter(Context context) {
        _inflater = LayoutInflater.from(context);
        _data = new ArrayList<NavItem>();
    }

    public void addItem(NavItem item) {
        _data.add(item);
    }

    @Override
    public int getCount() {
        return this._data.size();
    }

    @Override
    public NavItem getItem(int position) throws IndexOutOfBoundsException {
        return this._data.get(position);
    }

    @Override
    public long getItemId(int position) throws IndexOutOfBoundsException {
        if (position < getCount() && position >= 0) {
            return ((long) position);
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NavItem item = getItem(position);

        if (convertView == null) {
            convertView = _inflater.inflate(R.layout.nav_menu_item, null);
        }

        NavItemView view = (NavItemView) convertView;
        view.setText(item.getTextId());
        view.setItemIcon(item.getIconId());

        return convertView;
    }
}
