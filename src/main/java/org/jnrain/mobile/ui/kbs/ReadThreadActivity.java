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
import org.jnrain.mobile.R.id;
import org.jnrain.mobile.R.layout;
import org.jnrain.mobile.ui.base.JNRainFragmentActivity;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitlePageIndicator.IndicatorStyle;


@SuppressWarnings("rawtypes")
public class ReadThreadActivity extends JNRainFragmentActivity
        implements ReadThreadActivityListener {
    @InjectView(R.id.pager)
    ViewPager viewPager;
    @InjectView(R.id.indicator)
    TitlePageIndicator indicator;

    ReadThreadFragmentAdapter _adapter;

    private String _brd_id;
    private String _title;
    private long _tid;
    private int _totalposts;

    private int _page;
    private int _totalpages;

    public static final int POSTS_PER_PAGE = 10;
    private static final String TAG = "ReadThreadActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dyn_pages);
        _adapter = new ReadThreadFragmentAdapter(
                getSupportFragmentManager(),
                getApplicationContext());
        viewPager.setAdapter(_adapter);
        indicator.setViewPager(viewPager);
        indicator.setFooterIndicatorStyle(IndicatorStyle.Triangle);

        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        this._brd_id = intent.getStringExtra(BoardListActivity.BRD_ID);
        this._tid = intent.getLongExtra(ThreadListActivity.THREAD_ID, -1);
        this._title = intent.getStringExtra(ThreadListActivity.THREAD_TITLE);
        this._totalposts = intent
            .getIntExtra(BoardListActivity.NUM_POSTS, 1);

        // update title of action bar
        getSupportActionBar().setTitle(this._title);

        _totalpages = (int) Math.ceil((double) _totalposts / POSTS_PER_PAGE);

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

    public void showReplyActivityFor(Post post) {
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

        // fire up post activity
        Intent intent = new Intent();
        intent.setClass(this, NewPostActivity.class);
        intent.putExtra(BoardListActivity.BRD_ID, _brd_id);
        intent.putExtra(NewPostActivity.IS_NEW_THREAD, false);
        intent.putExtra(ThreadListActivity.THREAD_ID, _tid);
        intent.putExtra(NewPostActivity.IN_REPLY_TO, post.getID());
        intent.putExtra(ThreadListActivity.THREAD_TITLE, replyTitle);

        startActivity(intent);
    }
}
