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

import java.text.MessageFormat;

import org.jnrain.mobile.network.NewPostRequest;
import org.jnrain.mobile.util.SpicedRoboActivity;
import org.jnrain.weiyu.entity.SimpleReturnCode;

import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;


public class NewPostActivity extends SpicedRoboActivity<SimpleReturnCode> {
    @InjectView(R.id.editTitle)
    EditText editTitle;
    @InjectView(R.id.editContent)
    EditText editContent;

    @InjectResource(R.string.msg_unknown_status)
    public String MSG_UNKNOWN_STATUS;

    public static final String IS_NEW_THREAD = "org.jnrain.mobile.IS_NEW_THREAD";
    private static final String TAG = "NewPostActivity";

    private String _brd_id;
    private boolean _is_new_thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        _brd_id = intent.getStringExtra(BoardListActivity.BRD_ID);
        _is_new_thread = intent.getBooleanExtra(IS_NEW_THREAD, false);
        if (!_is_new_thread) {
            // TODO: tid and reid extraction
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.new_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_do_post:
                Log.d(TAG, "do post activated");

                String _title = editTitle.getText().toString();
                String _content = editContent.getText().toString();

                makeSpiceRequest(
                        new NewPostRequest(
                                _brd_id,
                                _title,
                                _content,
                                NewPostRequest.SIGN_RANDOM),
                        new NewPostRequestListener());

                // disable edit while performing request
                setEditableStatus(false);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public synchronized void setEditableStatus(boolean enabled) {
        editTitle.setEnabled(enabled);
        editContent.setEnabled(enabled);
    }

    private class NewPostRequestListener
            implements RequestListener<SimpleReturnCode> {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Log.d(TAG, "err on req: " + spiceException.toString());

            // restore editable status
            NewPostActivity.this.setEditableStatus(true);
        }

        @Override
        public void onRequestSuccess(SimpleReturnCode result) {
            int status = result.getStatus();

            switch (status) {
                case 0:
                    // finish off self
                    finish();

                    break;
                default:
                    Toast
                        .makeText(
                                getApplicationContext(),
                                MessageFormat.format(
                                        MSG_UNKNOWN_STATUS,
                                        status),
                                Toast.LENGTH_SHORT).show();

                    // restore editable status
                    NewPostActivity.this.setEditableStatus(true);
                    break;
            }
        }
    }
}
