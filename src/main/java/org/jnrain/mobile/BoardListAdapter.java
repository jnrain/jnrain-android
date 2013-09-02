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

import org.jnrain.luohua.collection.ListBoards;
import org.jnrain.luohua.entity.Board;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class BoardListAdapter extends BaseAdapter {
    // private static final String TAG = "BoardListAdapter";
    private LayoutInflater _inflater;
    private ListBoards _data;

    public BoardListAdapter(Context context, ListBoards data) {
        this._inflater = LayoutInflater.from(context);
        this._data = data;
    }

    @Override
    public int getCount() {
        return this._data.getBoards().size();
    }

    @Override
    public Board getItem(int position) throws IndexOutOfBoundsException {
        return this._data.getBoards().get(position);
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
        Board brd = getItem(position);

        if (convertView == null) {
            convertView = this._inflater.inflate(R.layout.board_item, null);
        }

        TextView textBrdID = (TextView) convertView
            .findViewById(R.id.textBoardID);
        TextView textBrdName = (TextView) convertView
            .findViewById(R.id.textBoardName);

        textBrdID.setText(brd.getID());
        textBrdName.setText(brd.getName());

        return convertView;
    }
}
