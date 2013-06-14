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
package org.jnrain.mobile.util;

import org.jnrain.mobile.OptionsMenuProvider;
import org.jnrain.mobile.service.JNRainSpiceService;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockActivity;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;


public class SpicedRoboActivity<T> extends RoboSherlockActivity
        implements SpiceRequestListener<T> {
    protected SpiceManager spiceManager = new SpiceManager(
            JNRainSpiceService.class);

    @Override
    protected void onStart() {
        synchronized (this) {
            super.onStart();
            spiceManager.start(this);
        }
    }

    @Override
    protected void onStop() {
        synchronized (this) {
            spiceManager.shouldStop();
            super.onStop();
        }
    }

    @Override
    public void makeSpiceRequest(
            SpiceRequest<T> request,
            RequestListener<T> requestListener) {
        spiceManager.execute(request, requestListener);
    }

    @Override
    public void makeSpiceRequest(
            SpiceRequest<T> request,
            String requestCacheKey,
            long cacheDuration,
            RequestListener<T> requestListener) {
        spiceManager.execute(
                request,
                requestCacheKey,
                cacheDuration,
                requestListener);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        OptionsMenuProvider optionsMenuProvider = OptionsMenuProvider.getOptionsMenuProvider();
        return optionsMenuProvider.createOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        OptionsMenuProvider optionsMenuProvider = OptionsMenuProvider.getOptionsMenuProvider();
        return optionsMenuProvider.optionsItemSelected(item, this);
    }
}
