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
package org.jnrain.mobile.ui;

import org.jnrain.mobile.R;
import org.jnrain.mobile.network.util.ConnectivityState;
import org.jnrain.mobile.updater.UpdateManager;
import org.jnrain.mobile.util.AppVersionHelper;
import org.jnrain.mobile.util.GlobalState;
import org.jnrain.mobile.util.SpiceRequestListener;

import android.content.Context;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


public class JNRainActivityHelper {
    protected ConnectivityState netState;
    protected boolean _isFrag;
    protected SpiceRequestListener<?> _activity;

    public JNRainActivityHelper(SpiceRequestListener<?> activity) {
        _activity = activity;
    }

    public void doPreOnStart() {
        Context ctx = _activity.getThisActivity().getApplicationContext();

        GlobalState.possiblyInitState(ctx);

        // init global version info
        AppVersionHelper.ensureVersionInited(ctx);
    }

    public void doPostOnStart() {
        Context ctx = _activity.getThisActivity().getApplicationContext();

        // connectivity state object
        netState = new ConnectivityState(ctx);

        // auto update things
        UpdateManager.doAutoCheckUpdate(_activity);
    }

    public static void setUpSlidingMenu(SlidingMenu menu) {
        // default values
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        menu.setFadeEnabled(false);
        menu.setShadowWidthRes(R.dimen.slidingmenu_shadow_width);
        menu.setShadowDrawable(R.drawable.slidingmenu_shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setBehindScrollScale(0.3f);

        // menu overdrawing manipulation
        // disabled for removing unwanted background transparency
        // menu.setAddBackground(false);
    }
}
