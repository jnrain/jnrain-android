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

import org.jnrain.mobile.network.ThreadRequest;
import org.jnrain.mobile.util.CacheKeyManager;
import org.jnrain.mobile.util.GlobalState;
import org.jnrain.weiyu.collection.ListPosts;
import org.jnrain.weiyu.entity.Post;

import roboguice.inject.InjectView;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;


public class ReadThreadFragment extends RoboSherlockFragment {
    @InjectView(R.id.listPosts)
    ListView listPosts;

    private String _brd_id;
    private long _tid;
    private int _page;
    private ListPosts _posts;

    protected ReadThreadActivityListener _listener;

    private static final String TAG = "ReadThreadFragment";

    public ReadThreadFragment(String brd_id, long tid, int page) {
        this._brd_id = brd_id;
        this._tid = tid;
        this._page = page;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            _listener = (ReadThreadActivityListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ReadThreadActivityListener");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.read_thread_fragment,
                container,
                false);

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

        return view;
    }

    public synchronized void updateData() {
        @SuppressWarnings("unchecked")
        ThreadAdapter adapter = new ThreadAdapter(
                this.getActivity(),
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
