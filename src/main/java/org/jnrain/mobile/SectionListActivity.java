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

import java.text.MessageFormat;

import org.jnrain.mobile.network.LogoutRequest;
import org.jnrain.mobile.network.SectionListRequest;
import org.jnrain.mobile.util.GlobalState;
import org.jnrain.mobile.util.SpicedRoboActivity;
import org.jnrain.weiyu.collection.ListSections;
import org.jnrain.weiyu.entity.Section;
import org.jnrain.weiyu.entity.SimpleReturnCode;

import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;


public class SectionListActivity extends SpicedRoboActivity<ListSections> {
    @InjectView(R.id.listSections)
    ListView listSections;

    @InjectResource(R.string.msg_network_fail)
    public String MSG_NETWORK_FAIL;
    @InjectResource(R.string.msg_unknown_status)
    public String MSG_UNKNOWN_STATUS;
    @InjectResource(R.string.msg_logout_success)
    public String MSG_LOGOUT_SUCCESS;

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

    @Override
    protected void onStop() {
        // logout
        spiceManager.execute(
                new LogoutRequest(),
                new LogoutRequestListener());
        super.onStop();
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

    private class LogoutRequestListener
            implements RequestListener<SimpleReturnCode> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Log.d(TAG, "err on req: " + spiceException.toString());
            Toast.makeText(
                    getApplicationContext(),
                    MSG_NETWORK_FAIL,
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(SimpleReturnCode result) {
            int status = result.getStatus();

            switch (status) {
                case 0:
                    String uid = GlobalState.getUserName();

                    // erase user name
                    GlobalState.setUserName("");

                    // successful
                    Toast.makeText(
                            getApplicationContext(),
                            MessageFormat.format(MSG_LOGOUT_SUCCESS, uid),
                            Toast.LENGTH_SHORT).show();

                    break;

                default:
                    Toast
                        .makeText(
                                getApplicationContext(),
                                MessageFormat.format(
                                        MSG_UNKNOWN_STATUS,
                                        status),
                                Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
