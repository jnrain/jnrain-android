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
import org.jnrain.mobile.ui.base.JNRainFragment;

import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;


public class NewPostFragment extends JNRainFragment<SimpleReturnCode> {
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

    private static final String TAG = "NewPostFragment";

    private String _brd_id;
    private boolean _is_new_thread;
    private long _tid;
    private long _reid;
    private String _title = null;

    public NewPostFragment(String brdId) {
        super();

        initState(brdId);
    }

    public NewPostFragment(String brdId, long tid, long reid, String title) {
        super();

        initState(brdId, tid, reid, title);
    }

    public NewPostFragment() {
        super();
    }

    protected void initState(String brdId) {
        _brd_id = brdId;
        _is_new_thread = true;
    }

    protected void initState(String brdId, long tid, long reid, String title) {
        _brd_id = brdId;
        _is_new_thread = false;
        _tid = tid;
        _reid = reid;
        _title = title;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        if (savedInstanceState != null) {
            boolean isNewThread = savedInstanceState.getBoolean(
                    KBSUIConstants.IS_NEW_THREAD_STORE,
                    false);

            if (isNewThread) {
                initState(savedInstanceState
                    .getString(KBSUIConstants.BOARD_ID_STORE));
            } else {
                initState(
                        savedInstanceState.getString(KBSUIConstants.BOARD_ID_STORE),
                        savedInstanceState
                            .getLong(KBSUIConstants.THREAD_ID_STORE),
                        savedInstanceState
                            .getLong(KBSUIConstants.POST_REPLY_ID_STORE),
                        savedInstanceState
                            .getString(KBSUIConstants.POST_TITLE_STORE));
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(
                KBSUIConstants.IS_NEW_THREAD_STORE,
                _is_new_thread);
        outState.putString(KBSUIConstants.BOARD_ID_STORE, _brd_id);

        if (_is_new_thread) {
            outState.putLong(KBSUIConstants.THREAD_ID_STORE, _tid);
            outState.putLong(KBSUIConstants.POST_REPLY_ID_STORE, _reid);
            outState.putString(KBSUIConstants.POST_TITLE_STORE, _title);
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.activity_new_post,
                container,
                false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Show the Up button in the action bar.
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // set title
        if (_title != null) {
            editTitle.setText(_title);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.new_post, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                // TODO: confirmation
                finishFragment();

                return true;

            case R.id.action_do_post:
                Log.d(TAG, "do post activated");

                String title = editTitle.getText().toString();
                String content = editContent.getText().toString();

                // TODO: proper signature selection
                if (_is_new_thread) {
                    _listener.makeSpiceRequest(
                            new NewPostRequest(
                                    _brd_id,
                                    title,
                                    content,
                                    NewPostRequest.SIGN_RANDOM),
                            new NewPostRequestListener());
                } else {
                    _listener.makeSpiceRequest(
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
            NewPostFragment.this.setEditableStatus(true);
        }

        @Override
        public void onRequestSuccess(SimpleReturnCode result) {
            int status = result.getStatus();

            switch (status) {
                case 0:
                    // toast according to action type
                    Toast.makeText(
                            getActivity(),
                            _is_new_thread
                                    ? MSG_POST_SUCCESS
                                    : MSG_POST_SUCCESS_REPLY,
                            Toast.LENGTH_SHORT).show();
                    // finish off self
                    finishFragment();

                    break;
                case 1:
                    Toast.makeText(
                            getActivity(),
                            MSG_POST_NO_GUEST_POST,
                            Toast.LENGTH_SHORT).show();

                    break;
                case 2:
                    Toast.makeText(
                            getActivity(),
                            MSG_POST_NO_BRD_GIVEN,
                            Toast.LENGTH_SHORT).show();

                    break;
                case 3:
                case 12:
                    Toast.makeText(
                            getActivity(),
                            MSG_POST_NO_SUCH_BRD,
                            Toast.LENGTH_SHORT).show();

                    break;
                case 4:
                    Toast.makeText(
                            getActivity(),
                            MSG_POST_EPERM,
                            Toast.LENGTH_SHORT).show();

                    break;
                case 5:
                    Toast.makeText(
                            getActivity(),
                            MSG_POST_EROFS,
                            Toast.LENGTH_SHORT).show();

                    break;
                case 6:
                case 15:
                    Toast.makeText(
                            getActivity(),
                            MSG_POST_DENIED,
                            Toast.LENGTH_SHORT).show();

                    break;
                case 7:
                case 14:
                    Toast.makeText(
                            getActivity(),
                            MSG_POST_NO_TITLE,
                            Toast.LENGTH_SHORT).show();

                    break;
                case 8:
                    Toast.makeText(
                            getActivity(),
                            MSG_POST_NO_CONTENT,
                            Toast.LENGTH_SHORT).show();
                case 9:
                    Toast.makeText(
                            getActivity(),
                            MSG_POST_EBADF,
                            Toast.LENGTH_SHORT).show();

                    break;
                case 10:
                case 19:
                    Toast.makeText(
                            getActivity(),
                            MSG_POST_LOCKED,
                            Toast.LENGTH_SHORT).show();

                    break;
                case 11:
                    Toast.makeText(
                            getActivity(),
                            MSG_POST_RIVER_CRAB,
                            Toast.LENGTH_SHORT).show();

                    break;
                case 13:
                    Toast.makeText(
                            getActivity(),
                            MSG_POST_DIRECTORY,
                            Toast.LENGTH_SHORT).show();

                    break;
                case 16:
                    Toast.makeText(
                            getActivity(),
                            MSG_POST_BANNED,
                            Toast.LENGTH_SHORT).show();

                    break;
                case 17:
                    Toast.makeText(
                            getActivity(),
                            MSG_POST_EAGAIN,
                            Toast.LENGTH_SHORT).show();

                    break;
                case 18:
                    Toast.makeText(
                            getActivity(),
                            MSG_POST_IDX_IO,
                            Toast.LENGTH_SHORT).show();

                    break;
                case 20:
                    Toast.makeText(
                            getActivity(),
                            MSG_POST_INTERNAL_ERR,
                            Toast.LENGTH_SHORT).show();

                    break;
                case 21:
                    Toast.makeText(
                            getActivity(),
                            MSG_POST_SILENT_NIGHT,
                            Toast.LENGTH_SHORT).show();

                    break;
                default:
                    Toast
                        .makeText(
                                getActivity(),
                                MessageFormat.format(
                                        MSG_UNKNOWN_STATUS,
                                        status),
                                Toast.LENGTH_SHORT).show();

                    break;
            }

            // restore editable status
            NewPostFragment.this.setEditableStatus(true);
        }
    }
}
