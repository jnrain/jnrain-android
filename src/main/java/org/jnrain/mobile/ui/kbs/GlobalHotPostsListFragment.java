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

import org.jnrain.kbs.collection.ListHotPosts;
import org.jnrain.kbs.entity.Post;
import org.jnrain.mobile.R;
import org.jnrain.mobile.network.requests.HotPostsListRequest;
import org.jnrain.mobile.ui.base.JNRainFragment;
import org.jnrain.mobile.util.CacheKeyManager;

import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;


public class GlobalHotPostsListFragment extends JNRainFragment<ListHotPosts> {
    @InjectView(R.id.textGlobalHotPostsStatus)
    TextView textStatus;
    @InjectView(R.id.listGlobalHotPosts)
    ListView listHotPosts;

    @InjectResource(R.string.fail_load_global_hot_posts)
    public String LOAD_FAIL_MSG;

    private static final String TAG = "GlobalHotPostsListActivity";

    private ListHotPosts _posts;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.frag_kbs_global_hot_posts_list,
                container,
                false);

        if (savedInstanceState != null) {
            _posts = (ListHotPosts) savedInstanceState
                .getSerializable(KBSUIConstants.GLOBAL_HOT_POSTS_STORE);
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setActionBarTitle(R.string.title_activity_global_hot_posts_list);

        if (_posts != null) {
            updateData();
        } else {
            _listener.makeSpiceRequest(
                    new HotPostsListRequest(ListHotPosts.GLOBAL),
                    CacheKeyManager.keyForHotPosts(ListHotPosts.GLOBAL),
                    DurationInMillis.ONE_MINUTE,
                    new GlobalHotPostsListRequestListener());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(
                KBSUIConstants.GLOBAL_HOT_POSTS_STORE,
                _posts);
    }

    public synchronized void updateData() {
        if (_listener.getThisActivity() == null) {
            return;
        }

        // empty the status display
        textStatus.setText("");

        // populate list
        HotPostsListAdapter adapter = new HotPostsListAdapter(
                _listener.getThisActivity(),
                _posts);
        listHotPosts.setAdapter(adapter);

        // attach click event
        listHotPosts
            .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(
                        AdapterView<?> parent,
                        View view,
                        int position,
                        long id) {
                    Post post = _posts.getPosts().get(position);

                    Log.i(TAG, "clicked: " + position + ", post.title="
                            + post.getTitle());

                    Fragment frag = new ReadThreadFragment(
                            post.getBoard(),
                            post.getID(),
                            post.getTitle(),
                            post.getReplies());
                    GlobalHotPostsListFragment.this.switchMainContentTo(
                            frag,
                            true);
                }
            });

    }

    private class GlobalHotPostsListRequestListener
            implements RequestListener<ListHotPosts> {
        @Override
        public void onRequestFailure(SpiceException arg0) {
            Log.e(TAG, "err on req: " + arg0.toString());
            textStatus.setText(LOAD_FAIL_MSG);
        }

        @Override
        public void onRequestSuccess(ListHotPosts posts) {
            Log.v(TAG, "got hot posts list: " + posts.toString());
            _posts = posts;
            updateData();
        }
    }
}
