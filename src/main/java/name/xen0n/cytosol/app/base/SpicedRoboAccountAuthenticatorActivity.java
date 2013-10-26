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
package name.xen0n.cytosol.app.base;

import name.xen0n.cytosol.app.SpiceRequestListener;
import android.app.Activity;

import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockAccountAuthenticatorActivity;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.SpiceService;
import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;


public abstract class SpicedRoboAccountAuthenticatorActivity<T>
        extends RoboSherlockAccountAuthenticatorActivity
        implements SpiceRequestListener<T> {
    protected final SpiceManager spiceManager;

    protected SpicedRoboAccountAuthenticatorActivity(
            final Class<? extends SpiceService> spiceServiceClass) {
        super();
        spiceManager = new SpiceManager(spiceServiceClass);
    }

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
    public Activity getThisActivity() {
        return this;
    }
}
