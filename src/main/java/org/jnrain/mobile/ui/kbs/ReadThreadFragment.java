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

import org.jnrain.kbs.entity.Post;
import org.jnrain.mobile.R;
import org.jnrain.mobile.ui.base.JNRainFragment;

import roboguice.inject.InjectView;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;
import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitlePageIndicator.IndicatorStyle;


@SuppressWarnings("rawtypes")
public class ReadThreadFragment extends JNRainFragment
        implements ReadThreadFragmentListener {
    @InjectView(R.id.pager)
    ViewPager viewPager;
    @InjectView(R.id.indicator)
    TitlePageIndicator indicator;

    ReadThreadPageFragmentAdapter _adapter;

    private String _brd_id;
    private String _title;
    private long _tid;
    private int _totalposts;

    private int _page;
    private int _totalpages;

    private static final String TAG = "ReadThreadActivity";

    public ReadThreadFragment(
            String brdId,
            long threadId,
            String title,
            int totalPosts) {
        super();

        initState(brdId, threadId, title, totalPosts);
    }

    public ReadThreadFragment() {
        super();
    }

    protected void initState(
            String brdId,
            long threadId,
            String title,
            int totalPosts) {
        _brd_id = brdId;
        _tid = threadId;
        _title = title;
        _totalposts = totalPosts;

        _totalpages = (int) Math.ceil((double) _totalposts
                / KBSUIConstants.POSTS_PER_PAGE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            initState(
                    savedInstanceState.getString(KBSUIConstants.BOARD_ID_STORE),
                    savedInstanceState
                        .getLong(KBSUIConstants.THREAD_ID_STORE),
                    savedInstanceState
                        .getString(KBSUIConstants.POST_TITLE_STORE),
                    savedInstanceState
                        .getInt(KBSUIConstants.POST_COUNT_STORE));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(KBSUIConstants.BOARD_ID_STORE, _brd_id);
        outState.putLong(KBSUIConstants.THREAD_ID_STORE, _tid);
        outState.putString(KBSUIConstants.POST_TITLE_STORE, _title);
        outState.putInt(KBSUIConstants.POST_COUNT_STORE, _totalposts);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dyn_pages, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setActionBarTitle(_title);

        _adapter = new ReadThreadPageFragmentAdapter(
                getChildFragmentManager(),
                getActivity());
        viewPager.setAdapter(_adapter);
        indicator.setViewPager(viewPager);
        indicator.setFooterIndicatorStyle(IndicatorStyle.Triangle);

        // Show the Up button in the action bar.
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // update title of action bar
        // getSupportActionBar().setTitle(this._title);

        // initial tabs
        addReplyPage(1);
        if (_totalpages == 1) {
            _page = 1;
        } else {
            _page = 2;
            addReplyPage(2);
        }

        // pager listener
        indicator
            .setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrollStateChanged(int state) {
                    // intentionally left blank
                    // Log.d(TAG, "PageScrollStateChanged: " +
                    // Integer.toString(state));
                }

                @Override
                public void onPageScrolled(
                        int position,
                        float positionOffset,
                        int positionOffsetPixels) {
                    // intentionally left blank
                }

                @Override
                public void onPageSelected(int position) {
                    Log.d(TAG, "PageSelected: " + Integer.toString(position)
                            + ", _page = " + Integer.toString(_page));
                    if (position == _page - 1) {
                        // last tab, add another page if there are still
                        // enough
                        // threads to display
                        if (_page < _totalpages) {
                            _page++;
                            addReplyPage(_page);
                        }
                    }
                }
            });
    }

    public void addReplyPage(int page) {
        _adapter.addItem(this._brd_id, this._tid, page);
    }

    public void showReplyUIFor(Post post) {
        Log.d(TAG, "reply to post " + post.toString() + " pressed");

        // reply title
        String postTitle = post.getTitle();
        String replyTitle = "";
        if (postTitle.startsWith("Re:")) {
            // prevent flooding of "Re: "'s
            replyTitle = postTitle;
        } else {
            replyTitle = "Re: " + postTitle;
        }

        // fire up post fragment
        Fragment frag = new NewPostFragment(
                _brd_id,
                _tid,
                post.getID(),
                replyTitle);
        switchMainContentTo(frag, true);
    }

    @Override
    public Activity getThisActivity() {
        return getActivity();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void makeSpiceRequest(
            SpiceRequest request,
            RequestListener requestListener) {
        _listener.makeSpiceRequest(request, requestListener);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void makeSpiceRequest(
            SpiceRequest request,
            String requestCacheKey,
            long cacheDuration,
            RequestListener requestListener) {
        _listener.makeSpiceRequest(
                request,
                requestCacheKey,
                cacheDuration,
                requestListener);
    }
}
