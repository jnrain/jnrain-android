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

import name.xen0n.cytosol.app.CytosolFragment;

import org.jnrain.kbs.collection.ListSections;
import org.jnrain.kbs.entity.Section;
import org.jnrain.mobile.R;
import org.jnrain.mobile.network.requests.SectionListRequest;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;


public class SectionListFragment extends CytosolFragment<ListSections> {
    @InjectView(R.id.listSections)
    ListView listSections;

    private static final String TAG = "SectionListFragment";
    private static final String CACHE_KEY = "secs_json";

    private ListSections _secs;

    public synchronized void updateData() {
        if (getActivity() == null) {
            return;
        }

        SectionListAdapter adapter = new SectionListAdapter(
                getActivity(),
                _secs);
        listSections.setAdapter(adapter);

        listSections
            .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(
                        AdapterView<?> parent,
                        View view,
                        int position,
                        long id) {
                    Section sec = _secs.getSections().get(position);

                    Log.i(TAG, "clicked: " + position + ", id=" + id
                            + ", sec=" + sec.toString());

                    Fragment frag = new BoardListFragment(sec);
                    SectionListFragment.this.switchMainContentTo(frag, true);
                }
            });
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.frag_kbs_section_list,
                container,
                false);

        if (savedInstanceState != null) {
            _secs = (ListSections) savedInstanceState
                .getSerializable(KBSUIConstants.SECTIONS_LIST_STORE);
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setActionBarTitle(R.string.title_kbs_section_list);

        if (_secs != null) {
            updateData();
        } else {
            _listener.makeSpiceRequest(
                    new SectionListRequest(),
                    CACHE_KEY,
                    DurationInMillis.ONE_WEEK,
                    new SectionListRequestListener());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(KBSUIConstants.SECTIONS_LIST_STORE, _secs);
    }

    private class SectionListRequestListener
            implements RequestListener<ListSections> {
        @Override
        public void onRequestFailure(SpiceException arg0) {
            Log.d(TAG, "err on req: " + arg0.toString());
        }

        @Override
        public void onRequestSuccess(ListSections sections) {
            Log.v(TAG, "got section list: " + sections.toString());
            _secs = sections;

            updateData();
        }
    }
}
