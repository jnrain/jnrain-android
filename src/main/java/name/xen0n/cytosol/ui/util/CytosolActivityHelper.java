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
package name.xen0n.cytosol.ui.util;

import name.xen0n.cytosol.app.SpiceRequestListener;
import name.xen0n.cytosol.network.util.ConnectivityState;
import name.xen0n.cytosol.updater.AppVersionHelper;
import name.xen0n.cytosol.updater.BaseUpdateManager;
import name.xen0n.cytosol.updater.UpdateManagerManager;
import android.content.Context;

import com.actionbarsherlock.app.ActionBar;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


public abstract class CytosolActivityHelper {
    protected ConnectivityState netState;
    protected SpiceRequestListener<?> _activity;

    public final Class<? extends BaseUpdateManager> APP_UPDATEMANAGER_CLASS;

    protected CytosolActivityHelper(
            final SpiceRequestListener<?> activity,
            final Class<? extends BaseUpdateManager> appUpdateMgrClass) {
        APP_UPDATEMANAGER_CLASS = appUpdateMgrClass;

        _activity = activity;

        // inject update manager
        // do this as early as possible, strangely if it's done instead
        // at just before ensureVersionInited then UpdateInfoFile.fromFile
        // will NPE
        UpdateManagerManager.ensureUpdateManager(APP_UPDATEMANAGER_CLASS);
    }

    public void doPreOnStart() {
        final Context ctx = _activity
            .getThisActivity()
            .getApplicationContext();

        doAppPrePreOnStart(ctx);

        // init global version info
        AppVersionHelper.ensureVersionInited(ctx);

        doAppPostPreOnStart(ctx);
    }

    public void doPostOnStart() {
        final Context ctx = _activity
            .getThisActivity()
            .getApplicationContext();

        // connectivity state object
        netState = new ConnectivityState(ctx);

        // auto update things
        UpdateManagerManager.getUpdateManager().doAutoCheckUpdate(_activity);

        doAppPostOnStart(ctx);
    }

    public abstract void doAppPrePreOnStart(final Context ctx);

    public abstract void doAppPostPreOnStart(final Context ctx);

    public abstract void doAppPostOnStart(final Context ctx);

    public abstract void setUpActionBar(final ActionBar bar);

    public abstract void setUpSlidingMenu(final SlidingMenu menu);
}
