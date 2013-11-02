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

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import name.xen0n.cytosol.app.SpiceRequestListener;
import name.xen0n.cytosol.ui.imagegetter.URLImageConsumer;
import name.xen0n.cytosol.ui.imagegetter.URLImageGetter;

import android.app.Activity;
import android.content.Context;
import android.view.View;


public class JNRainURLImageGetter extends URLImageGetter {

    public JNRainURLImageGetter(
            Activity activity,
            View t,
            Context c,
            String baseURL,
            Class<? extends URLImageConsumer> consumerClass,
            SpiceRequestListener<InputStream> listener) {
        super(activity, t, c, baseURL, consumerClass, listener);
    }

    @Override
    protected String transformRelativeSource(String source) {
        // XXX Kludge to avoid having to deal with 302 and cookies
        // This is only needed to interface w/ KBS, will be thrown once
        // we switch to the weiyu infrastructure.
        String tmp = source;
        if (tmp.startsWith("downip.php")) {
            tmp = source.replace("downip.php", "bbscon.php");
        }

        // Bypass the thumbnail gateway for GIF files
        if (tmp.endsWith(".gif")) {
            return tmp;
        }

        try {
            return "http://mobpic.jnrain.com/"
                    + URLEncoder.encode(tmp, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return tmp;
    }
}
