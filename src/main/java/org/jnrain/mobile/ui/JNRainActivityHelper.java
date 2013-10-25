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

import name.xen0n.cytosol.app.SpiceRequestListener;
import name.xen0n.cytosol.network.util.ConnectivityState;
import name.xen0n.cytosol.updater.AppVersionHelper;
import name.xen0n.cytosol.updater.UpdateManagerManager;

import org.jnrain.mobile.R;
import org.jnrain.mobile.updater.JNRainUpdateManager;
import org.jnrain.mobile.util.GlobalState;

import android.content.Context;

import com.actionbarsherlock.app.ActionBar;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


public class JNRainActivityHelper {
    protected ConnectivityState netState;
    protected boolean _isFrag;
    protected SpiceRequestListener<?> _activity;

    public JNRainActivityHelper(SpiceRequestListener<?> activity) {
        _activity = activity;

        // inject update manager
        // do this as early as possible, strangely if it's done instead
        // at line 51 then UpdateInfoFile.fromFile will NPE
        UpdateManagerManager.ensureUpdateManager(JNRainUpdateManager.class);
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
        UpdateManagerManager.getUpdateManager().doAutoCheckUpdate(_activity);
    }

    public static void setUpActionBar(ActionBar bar) {
        if (bar == null) {
            return;
        }

        // default is to show an icon signifying sliding menu's presence
        bar.setIcon(R.drawable.ic_actionbar_navmenu);
        bar.setDisplayHomeAsUpEnabled(true);
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
