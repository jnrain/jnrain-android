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
package org.jnrain.mobile.ui.ux;

import org.jnrain.mobile.ui.base.JNRainSlidingFragmentActivity;
import org.jnrain.mobile.util.AccountStateListener;

import android.os.Bundle;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityHelper.SlidingActivityBackAction;


public class ExitPointActivity<T> extends JNRainSlidingFragmentActivity<T>
        implements AccountStateListener {
    private JNRainExitPointBehaviorHelper exitHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Renren-like back to menu first behavior
        setBackAction(SlidingActivityBackAction.BACK_TO_MENU);

        // exit helper
        exitHelper = new JNRainExitPointBehaviorHelper(this);
    }

    @Override
    public void onBackPressed() {
        exitHelper.onBackPressed();
    }

    @Override
    public void onAccountLoggedIn(String uid) {
        // intentionally left blank, since this is an activity that deals
        // with logouts
    }

    @Override
    public void onAccountLoggedOut() {
        // Equivalent to a delayed "press" of Back button d-:
        // super.onBackPressed();
        finish();
    }
}
