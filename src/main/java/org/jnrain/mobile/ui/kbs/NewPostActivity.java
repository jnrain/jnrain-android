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
package org.jnrain.mobile.ui.kbs;

import java.text.MessageFormat;

import org.jnrain.kbs.entity.SimpleReturnCode;
import org.jnrain.mobile.R;
import org.jnrain.mobile.network.requests.NewPostRequest;
import org.jnrain.mobile.ui.base.JNRainActivity;

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


public class NewPostActivity extends JNRainActivity<SimpleReturnCode> {
    @InjectView(R.id.editTitle)
    EditText editTitle;
    @InjectView(R.id.editContent)
    EditText editContent;

    @InjectResource(R.string.msg_unknown_status)
    public String MSG_UNKNOWN_STATUS;
    @InjectResource(R.string.msg_post_success)
    public String MSG_POST_SUCCESS;
    @InjectResource(R.string.msg_post_success_reply)
    public String MSG_POST_SUCCESS_REPLY;
    @InjectResource(R.string.msg_post_no_guest_post)
    public String MSG_POST_NO_GUEST_POST;
    @InjectResource(R.string.msg_post_no_brd_given)
    public String MSG_POST_NO_BRD_GIVEN;
    @InjectResource(R.string.msg_post_no_such_brd)
    public String MSG_POST_NO_SUCH_BRD;
    @InjectResource(R.string.msg_post_eperm)
    public String MSG_POST_EPERM;
    @InjectResource(R.string.msg_post_erofs)
    public String MSG_POST_EROFS;
    @InjectResource(R.string.msg_post_denied)
    public String MSG_POST_DENIED;
    @InjectResource(R.string.msg_post_no_title)
    public String MSG_POST_NO_TITLE;
    @InjectResource(R.string.msg_post_no_content)
    public String MSG_POST_NO_CONTENT;
    @InjectResource(R.string.msg_post_ebadf)
    public String MSG_POST_EBADF;
    @InjectResource(R.string.msg_post_locked)
    public String MSG_POST_LOCKED;
    @InjectResource(R.string.msg_post_river_crab)
    public String MSG_POST_RIVER_CRAB;
    @InjectResource(R.string.msg_post_directory)
    public String MSG_POST_DIRECTORY;
    @InjectResource(R.string.msg_post_banned)
    public String MSG_POST_BANNED;
    @InjectResource(R.string.msg_post_eagain)
    public String MSG_POST_EAGAIN;
    @InjectResource(R.string.msg_post_idx_io)
    public String MSG_POST_IDX_IO;
    @InjectResource(R.string.msg_post_internal_err)
    public String MSG_POST_INTERNAL_ERR;
    @InjectResource(R.string.msg_post_silent_night)
    public String MSG_POST_SILENT_NIGHT;

    private static final String TAG = "NewPostActivity";
    public static final String IS_NEW_THREAD = "org.jnrain.mobile.IS_NEW_THREAD";
    public static final String IN_REPLY_TO = "org.jnrain.mobile.IN_REPLY_TO";

    private String _brd_id;
    private boolean _is_new_thread;
    private long _tid;
    private long _reid;
    private String _title = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // dummy code, going to be deleted anyway
        Intent intent = getIntent();
        _brd_id = intent.getStringExtra(KBSUIConstants.BOARD_ID_STORE);
        _is_new_thread = intent.getBooleanExtra(IS_NEW_THREAD, false);
        if (!_is_new_thread) {
            _tid = intent.getLongExtra(KBSUIConstants.THREAD_ID_STORE, 0);
            _reid = intent.getLongExtra(IN_REPLY_TO, 0);
            _title = intent.getStringExtra(KBSUIConstants.POST_TITLE_STORE);
        }

        // set title
        if (_title != null) {
            editTitle.setText(_title);
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
            case android.R.id.home:
                // TODO: confirmation
                finish();
                return true;

            case R.id.action_do_post:
                Log.d(TAG, "do post activated");

                String title = editTitle.getText().toString();
                String content = editContent.getText().toString();

                // TODO: proper signature selection
                if (_is_new_thread) {
                    makeSpiceRequest(
                            new NewPostRequest(
                                    _brd_id,
                                    title,
                                    content,
                                    NewPostRequest.SIGN_RANDOM),
                            new NewPostRequestListener());
                } else {
                    makeSpiceRequest(
                            new NewPostRequest(
                                    _brd_id,
                                    _tid,
                                    _reid,
                                    title,
                                    content,
                                    NewPostRequest.SIGN_RANDOM),
                            new NewPostRequestListener());
                }

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
                    // toast according to action type
                    Toast.makeText(
                            getApplicationContext(),
                            _is_new_thread
                                    ? MSG_POST_SUCCESS
                                    : MSG_POST_SUCCESS_REPLY,
                            Toast.LENGTH_SHORT).show();
                    // finish off self
                    finish();

                    break;
                case 1:
                    Toast.makeText(
                            getApplicationContext(),
                            MSG_POST_NO_GUEST_POST,
                            Toast.LENGTH_SHORT).show();

                    break;
                case 2:
                    Toast.makeText(
                            getApplicationContext(),
                            MSG_POST_NO_BRD_GIVEN,
                            Toast.LENGTH_SHORT).show();

                    break;
                case 3:
                case 12:
                    Toast.makeText(
                            getApplicationContext(),
                            MSG_POST_NO_SUCH_BRD,
                            Toast.LENGTH_SHORT).show();

                    break;
                case 4:
                    Toast.makeText(
                            getApplicationContext(),
                            MSG_POST_EPERM,
                            Toast.LENGTH_SHORT).show();

                    break;
                case 5:
                    Toast.makeText(
                            getApplicationContext(),
                            MSG_POST_EROFS,
                            Toast.LENGTH_SHORT).show();

                    break;
                case 6:
                case 15:
                    Toast.makeText(
                            getApplicationContext(),
                            MSG_POST_DENIED,
                            Toast.LENGTH_SHORT).show();

                    break;
                case 7:
                case 14:
                    Toast.makeText(
                            getApplicationContext(),
                            MSG_POST_NO_TITLE,
                            Toast.LENGTH_SHORT).show();

                    break;
                case 8:
                    Toast.makeText(
                            getApplicationContext(),
                            MSG_POST_NO_CONTENT,
                            Toast.LENGTH_SHORT).show();
                case 9:
                    Toast.makeText(
                            getApplicationContext(),
                            MSG_POST_EBADF,
                            Toast.LENGTH_SHORT).show();

                    break;
                case 10:
                case 19:
                    Toast.makeText(
                            getApplicationContext(),
                            MSG_POST_LOCKED,
                            Toast.LENGTH_SHORT).show();

                    break;
                case 11:
                    Toast.makeText(
                            getApplicationContext(),
                            MSG_POST_RIVER_CRAB,
                            Toast.LENGTH_SHORT).show();

                    break;
                case 13:
                    Toast.makeText(
                            getApplicationContext(),
                            MSG_POST_DIRECTORY,
                            Toast.LENGTH_SHORT).show();

                    break;
                case 16:
                    Toast.makeText(
                            getApplicationContext(),
                            MSG_POST_BANNED,
                            Toast.LENGTH_SHORT).show();

                    break;
                case 17:
                    Toast.makeText(
                            getApplicationContext(),
                            MSG_POST_EAGAIN,
                            Toast.LENGTH_SHORT).show();

                    break;
                case 18:
                    Toast.makeText(
                            getApplicationContext(),
                            MSG_POST_IDX_IO,
                            Toast.LENGTH_SHORT).show();

                    break;
                case 20:
                    Toast.makeText(
                            getApplicationContext(),
                            MSG_POST_INTERNAL_ERR,
                            Toast.LENGTH_SHORT).show();

                    break;
                case 21:
                    Toast.makeText(
                            getApplicationContext(),
                            MSG_POST_SILENT_NIGHT,
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

            // restore editable status
            NewPostActivity.this.setEditableStatus(true);
        }
    }
}
