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

import org.jnrain.mobile.OptionsMenuProvider;
import org.jnrain.mobile.network.util.ConnectivityState;
import org.jnrain.mobile.updater.UpdateManager;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;


public class JNRainActivity<T> extends SpicedRoboActivity<T> {
    protected ConnectivityState netState;

    @Override
    protected void onStart() {
        // init global version info
        AppVersionHelper.ensureVersionInited(this);

        super.onStart();

        // connectivity state object
        netState = new ConnectivityState(this);

        // auto update things
        doAutoCheckUpdate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        OptionsMenuProvider optionsMenuProvider = OptionsMenuProvider
            .getOptionsMenuProvider();
        return optionsMenuProvider.createOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        OptionsMenuProvider optionsMenuProvider = OptionsMenuProvider
            .getOptionsMenuProvider();
        return optionsMenuProvider.optionsItemSelected(item, this);
    }

    public void doAutoCheckUpdate() {
        boolean updateReqIssued = false;

        if (GlobalState.getUpdateInfo() == null) {
            // init the update info cache
            UpdateManager.issueAutoCheckRequest(this);
            updateReqIssued = true;
        }

        if (UpdateManager.shouldAutoCheck(getApplicationContext())) {
            // should perform auto check of updates
            // but don't repeat the request twice
            if (!updateReqIssued) {
                UpdateManager.issueAutoCheckRequest(this);
            }
        }
    }
}
