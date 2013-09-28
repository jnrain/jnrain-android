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
package org.jnrain.mobile.ui.kbs;

import org.jnrain.kbs.collection.ListPosts;
import org.jnrain.kbs.entity.Post;
import org.jnrain.mobile.R;
import org.jnrain.mobile.ui.base.JNRainFragment;

import roboguice.inject.InjectView;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;
import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitlePageIndicator.IndicatorStyle;


public class ThreadListFragment extends JNRainFragment<ListPosts>
        implements ThreadListFragmentListener {

    @InjectView(R.id.pager)
    ViewPager viewPager;
    @InjectView(R.id.indicator)
    TitlePageIndicator indicator;

    ThreadListPageFragmentAdapter _adapter;

    public static final String THREAD_ID = "org.jnrain.mobile.THREAD_ID";
    public static final String THREAD_TITLE = "org.jnrain.mobile.THREAD_TITLE";
    public static final String PAGE = "org.jnrain.mobile.PAGE";
    public static final int THREADS_PER_PAGE = 30;

    private static final String BRD_ID_STORE = "_brdId";
    private static final String TOTAL_THREADS_STORE = "_totalthreads";

    private String _brd_id;
    private int _page;
    private int _totalthreads;
    private int _totalpages;

    private static final String TAG = "ThreadListActivity";

    public ThreadListFragment(String brdId, int totalThreads) {
        super();
        initState(brdId, totalThreads);
    }

    public ThreadListFragment() {
        super();
    }

    protected void initState(String brdId, int totalThreads) {
        _brd_id = brdId;
        _totalthreads = totalThreads;

        _page = 1;
        _totalpages = (int) Math.ceil((double) _totalthreads
                / THREADS_PER_PAGE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        if (savedInstanceState != null) {
            // restore original ctor parameters
            initState(
                    savedInstanceState.getString(BRD_ID_STORE),
                    savedInstanceState.getInt(TOTAL_THREADS_STORE));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(BRD_ID_STORE, _brd_id);
        outState.putInt(TOTAL_THREADS_STORE, _totalthreads);
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

        // tabHost.setup();
        _adapter = new ThreadListPageFragmentAdapter(
                getChildFragmentManager(),
                getThisActivity());
        viewPager.setAdapter(_adapter);
        indicator.setViewPager(viewPager);
        indicator.setFooterIndicatorStyle(IndicatorStyle.Triangle);

        // tabsAdapter = new TabsAdapter(this, tabHost, viewPager);

        // Show the Up button in the action bar.
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // update title of action bar
        // getSupportActionBar().setTitle(this._brd_id);

        // add the initial fragments
        addPage(1);
        if (_totalpages > 1) {
            this._page = 2;
            addPage(2);
        }

        // if (savedInstanceState != null) {
        // tabHost.setCurrentTabByTag(savedInstanceState.getString("currTab"));
        // }

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
                            addPage(_page);
                        }
                    }
                }
            });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.thread_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_new_post:
                Log.d(TAG, "new post menu item selected");
                /*
                 * Intent intent = new Intent(); intent.setClass(
                 * ThreadListFragment.this, NewPostActivity.class);
                 * intent.putExtra(BoardListFragment.BRD_ID, _brd_id);
                 * intent.putExtra(NewPostActivity.IS_NEW_THREAD, true);
                 * 
                 * startActivity(intent);
                 */
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // @Override
    // protected void onSaveInstanceState(Bundle outState) {
    // super.onSaveInstanceState(outState);
    // outState.putString("currTab", tabHost.getCurrentTabTag());
    // }

    public void addPage(int page) {
        // Bundle args = new Bundle();

        // args.putString(BoardListActivity.BRD_ID, this._brd_id);
        // args.putInt(PAGE, _page);

        // tabsAdapter.addTab(tabHost.newTabSpec("page_" +
        // Integer.toString(page))
        // .setIndicator("Page " + Integer.toString(page)),
        // ThreadListFragment.class, args);
        _adapter.addItem(this._brd_id, page);

        // getSupportActionBar().setDisplayShowTitleEnabled(false);
        // getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    }

    @Override
    public void sendReadThreadIntent(Post post) {
        /*
         * Intent intent = new Intent( ThreadListFragment.this,
         * ReadThreadActivity.class);
         * intent.putExtra(BoardListFragment.BRD_ID, post.getBoard());
         * intent.putExtra(THREAD_ID, post.getID());
         * intent.putExtra(THREAD_TITLE, post.getTitle());
         * intent.putExtra(BoardListFragment.NUM_POSTS, post.getReplies() +
         * 1);
         * 
         * startActivity(intent);
         */
    }

    @Override
    public Activity getThisActivity() {
        return getActivity();
    }

    @Override
    public void makeSpiceRequest(
            SpiceRequest<ListPosts> request,
            RequestListener<ListPosts> requestListener) {
        _listener.makeSpiceRequest(request, requestListener);
    }

    @Override
    public void makeSpiceRequest(
            SpiceRequest<ListPosts> request,
            String requestCacheKey,
            long cacheDuration,
            RequestListener<ListPosts> requestListener) {
        _listener.makeSpiceRequest(
                request,
                requestCacheKey,
                cacheDuration,
                requestListener);
    }
}
