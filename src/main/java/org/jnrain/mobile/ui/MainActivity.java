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
package org.jnrain.mobile.ui;

import org.jnrain.mobile.R;
import org.jnrain.mobile.ui.base.JNRainSlidingFragmentActivity;
import org.jnrain.mobile.ui.kbs.GlobalHotPostsListFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;


@SuppressWarnings("rawtypes")
public class MainActivity extends JNRainSlidingFragmentActivity {
    // private static final String TAG = "MainActivity";
    private static final String CONTENT_FRAGMENT_STORE = "_content";

    private Fragment _content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fm = getSupportFragmentManager();

        setContentView(R.layout.content_frame);
        setBehindContentView(R.layout.hello);

        // retrieve or construct the Above view
        if (savedInstanceState != null) {
            // Restore the saved fragment
            _content = fm.getFragment(
                    savedInstanceState,
                    CONTENT_FRAGMENT_STORE);
        } else {
            // Newly launched, set a default fragment
            _content = new GlobalHotPostsListFragment();
        }

        // insert the Above view
        fm.beginTransaction().replace(R.id.content_frame, _content).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        getSupportFragmentManager().putFragment(
                outState,
                CONTENT_FRAGMENT_STORE,
                _content);
    }

    /**
     * Change the content fragment to the one passed in.
     * 
     * 改变当前内容 Fragment 为传入的 Fragment.
     * 
     * @param fragment
     */
    public synchronized void switchContent(Fragment fragment) {
        _content = fragment;

        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.content_frame, fragment)
            .commit();

        getSlidingMenu().showContent();
    }

}
