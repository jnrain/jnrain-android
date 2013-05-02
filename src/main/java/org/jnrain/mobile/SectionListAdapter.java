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
package org.jnrain.mobile;

import org.jnrain.weiyu.collection.ListSections;
import org.jnrain.weiyu.entity.Section;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class SectionListAdapter extends BaseAdapter {
    private LayoutInflater _inflater;
    private ListSections _data;

    public SectionListAdapter(Context context, ListSections data) {
        this._inflater = LayoutInflater.from(context);
        this._data = data;
    }

    @Override
    public int getCount() {
        return this._data.getSections().size();
    }

    @Override
    public Section getItem(int position) throws IndexOutOfBoundsException {
        return this._data.getSections().get(position);
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
        Section sec = getItem(position);

        if (convertView == null) {
            convertView = this._inflater
                .inflate(R.layout.section_item, null);
        }

        TextView textSecName = (TextView) convertView
            .findViewById(R.id.textName);
        TextView textSecOrd = (TextView) convertView
            .findViewById(R.id.textOrd);

        textSecOrd.setText(sec.getOrd());
        textSecName.setText(sec.getName());

        return convertView;
    }
}
