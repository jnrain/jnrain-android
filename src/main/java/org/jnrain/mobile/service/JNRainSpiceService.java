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
package org.jnrain.mobile.service;

import name.xen0n.cytosol.network.util.CookieHttpRequestInterceptor;
import name.xen0n.cytosol.service.CytosolSpiceService;
import android.util.Log;


public class JNRainSpiceService extends CytosolSpiceService {
    private static final String TAG = "JNRainSpiceService";

    /**
     * Network request timeout in milliseconds.
     */
    public static final int REQUEST_TIMEOUT = 5000;

    public static final String JNRAIN_COOKIE_DOMAIN = "http://bbs.jnrain.com/";

    public JNRainSpiceService() {
        super(REQUEST_TIMEOUT);

        // cookie interceptor
        Log.d(TAG, ">>> setting up interceptor(s)");
        this.addInterceptor(new JNRainCookieHttpRequestInterceptor());
    }

    class JNRainCookieHttpRequestInterceptor
            extends CookieHttpRequestInterceptor {
        public JNRainCookieHttpRequestInterceptor() {
            super(JNRAIN_COOKIE_DOMAIN);
        }
    }
}
