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
package org.jnrain.mobile;

import org.jnrain.mobile.network.SectionListRequest;
import org.jnrain.mobile.ui.ux.ExitPointActivity;
import org.jnrain.weiyu.collection.ListSections;
import org.jnrain.weiyu.entity.Section;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;


public class SectionListActivity extends ExitPointActivity<ListSections> {
    @InjectView(R.id.listSections)
    ListView listSections;

    private static final String TAG = "SectionListActivity";
    private static final String CACHE_KEY = "secs_json";

    public static final String SEC_ORD = "org.jnrain.mobile.SEC_ORD";

    private ListSections _secs;

    public synchronized void updateData() {
        SectionListAdapter adapter = new SectionListAdapter(
                getApplicationContext(),
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

                    Intent intent = new Intent(
                            SectionListActivity.this,
                            BoardListActivity.class);
                    intent.putExtra(SEC_ORD, sec.getOrd());
                    startActivity(intent);
                }
            });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_list);

        spiceManager.execute(
                new SectionListRequest(),
                CACHE_KEY,
                DurationInMillis.ONE_WEEK,
                new SectionListRequestListener());
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
