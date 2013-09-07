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
package org.jnrain.mobile.util;

import org.jnrain.mobile.R;
import org.jnrain.mobile.network.util.ConnectivityState;
import org.jnrain.mobile.updater.UpdateManager;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


public class JNRainActivityHelper {
    protected ConnectivityState netState;
    protected boolean _isFrag;
    protected SpiceRequestListener<?> _activity;

    public JNRainActivityHelper(SpiceRequestListener<?> activity) {
        _activity = activity;
    }

    public void doPreOnStart() {
        // init global version info
        AppVersionHelper.ensureVersionInited(_activity.getThisActivity());
    }

    public void doPostOnStart() {
        // connectivity state object
        netState = new ConnectivityState(_activity.getThisActivity());

        // auto update things
        doAutoCheckUpdate();
    }

    public void doAutoCheckUpdate() {
        boolean updateReqIssued = false;

        if (GlobalState.getUpdateInfo() == null) {
            // init the update info cache
            UpdateManager.issueAutoCheckRequest(_activity);
            updateReqIssued = true;
        }

        if (UpdateManager.shouldAutoCheck(_activity
            .getThisActivity()
            .getApplicationContext())) {
            // should perform auto check of updates
            // but don't repeat the request twice
            if (!updateReqIssued) {
                UpdateManager.issueAutoCheckRequest(_activity);
            }
        }
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

        // prevent overdraw
        menu.setAddBackground(false);
    }
}
