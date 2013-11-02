/*
 * Copyright 2012-2013 JNRain
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
package org.jnrain.mobile.ui.kbs;

import java.text.MessageFormat;

import name.xen0n.cytosol.ui.widget.DynPageFragmentAdapter;

import org.jnrain.mobile.R;

import android.content.Context;
import android.support.v4.app.FragmentManager;


class ThreadListPageFragmentAdapter
        extends DynPageFragmentAdapter<ThreadListPageFragment> {
    protected Context _ctx;

    public ThreadListPageFragmentAdapter(FragmentManager fm, Context ctx) {
        super(fm);
        _ctx = ctx;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // return "Page" + Integer.toString(position + 1);
        return MessageFormat.format(
                _ctx.getString(R.string.page_nr),
                Integer.toString(position + 1));
    }

    public void addItem(String brd_id, int page) {
        ThreadListPageFragment frag = new ThreadListPageFragment(
                brd_id,
                page);

        // retain state
        frag.setRetainInstance(true);

        _contents.add(frag);
        notifyDataSetChanged();
    }

    @Override
    public int getIconResId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }
}
