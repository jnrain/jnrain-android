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
package org.jnrain.mobile.accounts.kbs;

import java.io.InputStream;

import name.xen0n.cytosol.app.SpiceRequestListener;
import name.xen0n.cytosol.ui.imagegetter.URLImageGetter;


import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;


public class KBSCaptchaHelper {
    public static final String CAPTCHA_BASE_URL = "http://bbs.jnrain.com/wForum/img_rand/";
    public static final String CAPTCHA_URL = "img_rand.php";

    // private static final String TAG = "KBSCaptchaHelper";

    private SpiceRequestListener<InputStream> listener;
    private ImageView iv;

    @SuppressWarnings("unused")
    private Drawable dummy;

    public KBSCaptchaHelper(
            SpiceRequestListener<InputStream> listener,
            ImageView view) {
        this.listener = listener;
        this.iv = view;
    }

    public void doFetchCaptcha() {
        Activity activity = listener.getThisActivity();
        URLImageGetter getter = new URLImageGetter(
                activity,
                iv,
                activity,
                CAPTCHA_BASE_URL,
                KBSCaptchaURLImageConsumer.class,
                -1, // FIXME: correct (i.e. non-deprecated) way to never
                    // cache captchas
                listener);

        // result URLDrawable is intentionally not used
        dummy = getter.getDrawable(CAPTCHA_URL);
    }

}
