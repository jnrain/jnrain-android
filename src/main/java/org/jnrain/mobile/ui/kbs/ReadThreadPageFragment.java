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

import org.jnrain.kbs.collection.ListPosts;
import org.jnrain.kbs.entity.Post;
import org.jnrain.mobile.R;
import org.jnrain.mobile.network.requests.ThreadRequest;
import org.jnrain.mobile.util.CacheKeyManager;
import org.jnrain.mobile.util.GlobalState;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;


public class ReadThreadPageFragment extends RoboSherlockFragment {
    @InjectView(R.id.listPosts)
    ListView listPosts;

    private String _brd_id;
    private long _tid;
    private int _page;
    private ListPosts _posts;

    protected ReadThreadFragmentListener _listener;

    private static final String TAG = "ReadThreadFragment";

    public ReadThreadPageFragment(String brd_id, long tid, int page) {
        super();

        initState(brd_id, tid, page, null);
    }

    public ReadThreadPageFragment() {
        super();
    }

    protected void initState(
            String brdId,
            long tid,
            int page,
            ListPosts posts) {
        _brd_id = brdId;
        _tid = tid;
        _page = page;
        _posts = posts;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _listener = (ReadThreadFragmentListener) getParentFragment();

        if (savedInstanceState != null) {
            initState(
                    savedInstanceState.getString(KBSUIConstants.BOARD_ID_STORE),
                    savedInstanceState
                        .getLong(KBSUIConstants.THREAD_ID_STORE),
                    savedInstanceState.getInt(KBSUIConstants.PAGE_STORE),
                    (ListPosts) savedInstanceState
                        .getSerializable(KBSUIConstants.POSTS_LIST_STORE));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(KBSUIConstants.BOARD_ID_STORE, _brd_id);
        outState.putLong(KBSUIConstants.THREAD_ID_STORE, _tid);
        outState.putInt(KBSUIConstants.PAGE_STORE, _page);
        outState.putSerializable(KBSUIConstants.POSTS_LIST_STORE, _posts);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.frag_kbs_read_thread,
                container,
                false);

        return view;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (_posts != null) {
            updateData();
        } else {
            // fetch posts
            _listener.makeSpiceRequest(
                    new ThreadRequest(_brd_id, _tid, _page),
                    CacheKeyManager.keyForPagedPostList(
                            _brd_id,
                            _tid,
                            _page,
                            GlobalState.getUserName()),
                    DurationInMillis.ONE_MINUTE,
                    new ThreadRequestListener());
        }
    }

    @Override
    public void onCreateContextMenu(
            ContextMenu menu,
            View v,
            ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = this.getActivity().getMenuInflater();
        inflater.inflate(R.menu.read_thread_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
            .getMenuInfo();
        switch (item.getItemId()) {
            case R.id.action_reply:
                Post post = ((ThreadAdapter) listPosts.getAdapter())
                    .getItem(info.position);

                _listener.showReplyUIFor(post);

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public synchronized void updateData() {
        if (getActivity() == null) {
            return;
        }

        @SuppressWarnings("unchecked")
        ThreadAdapter adapter = new ThreadAdapter(
                getActivity(),
                _listener,
                _posts,
                _listener);
        listPosts.setAdapter(adapter);

        listPosts
            .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(
                        AdapterView<?> parent,
                        View view,
                        int position,
                        long id) {
                    Post post = _posts.getPosts().get(position);

                    Log.i(TAG, "clicked: " + position + ", id=" + id
                            + ", post=" + post.toString());
                }
            });

        // context menu
        registerForContextMenu(listPosts);
    }

    private class ThreadRequestListener
            implements RequestListener<ListPosts> {
        @Override
        public void onRequestFailure(SpiceException arg0) {
            Log.d(TAG, "err on req: " + arg0.toString());
        }

        @Override
        public void onRequestSuccess(ListPosts posts) {
            Log.v(TAG, "got posts list: " + posts.toString());
            _posts = posts;

            updateData();
        }
    }
}
