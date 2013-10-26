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
import name.xen0n.cytosol.ui.util.CytosolActivityHelper;

import org.jnrain.mobile.R;
import org.jnrain.mobile.updater.JNRainUpdateManager;
import org.jnrain.mobile.util.GlobalState;

import android.content.Context;

import com.actionbarsherlock.app.ActionBar;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


public class JNRainActivityHelper extends CytosolActivityHelper {
    public JNRainActivityHelper(final SpiceRequestListener<?> activity) {
        super(activity, JNRainUpdateManager.class);
    }

    @Override
    public void doAppPrePreOnStart(final Context ctx) {
        GlobalState.possiblyInitState(ctx);
    }

    @Override
    public void doAppPostPreOnStart(final Context ctx) {
        // nothing to do
    }

    @Override
    public void doAppPostOnStart(final Context ctx) {
        // nothing to do
    }

    @Override
    public void setUpActionBar(final ActionBar bar) {
        if (bar == null) {
            return;
        }

        // default is to show an icon signifying sliding menu's presence
        bar.setIcon(R.drawable.ic_actionbar_navmenu);
        bar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void setUpSlidingMenu(final SlidingMenu menu) {
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
