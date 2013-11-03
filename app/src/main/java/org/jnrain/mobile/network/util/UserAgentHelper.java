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
package org.jnrain.mobile.network.util;

import java.text.MessageFormat;

import name.xen0n.cytosol.util.GlobalUpdaterState;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;


@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class UserAgentHelper {
    private static final String TAG = "UAHelper";
    private static final String UA_FORMAT = "JNRain/{0} (Android {1} {2}; {3}; id={4}; rv={5})";
    private static String preprocessedUserAgent;

    public static boolean isUserAgentReady() {
        return preprocessedUserAgent != null;
    }

    public static void initUserAgent(Context ctx) {
        initUserAgent("", ctx);
    }

    public static void initUserAgent(String baseUA, Context ctx) {
        Log.v(TAG, "BOARD = " + Build.BOARD);
        Log.v(TAG, "BOOTLOADER = " + Build.BOOTLOADER);
        Log.v(TAG, "BRAND = " + Build.BRAND);
        Log.v(TAG, "CPU_ABI = " + Build.CPU_ABI);
        Log.v(TAG, "CPU_ABI2 = " + Build.CPU_ABI2);
        Log.v(TAG, "DEVICE = " + Build.DEVICE);
        Log.v(TAG, "DISPLAY = " + Build.DISPLAY);
        Log.v(TAG, "FINGERPRINT = " + Build.FINGERPRINT);
        Log.v(TAG, "HARDWARE = " + Build.HARDWARE);
        Log.v(TAG, "HOST = " + Build.HOST);
        Log.v(TAG, "ID = " + Build.ID);
        Log.v(TAG, "MANUFACTURER = " + Build.MANUFACTURER);
        Log.v(TAG, "MODEL = " + Build.MODEL);
        Log.v(TAG, "PRODUCT = " + Build.PRODUCT);
        Log.v(TAG, "SERIAL = " + Build.SERIAL);
        Log.v(TAG, "TAGS = " + Build.TAGS);

        String ua = MessageFormat.format(
                UA_FORMAT,
                GlobalUpdaterState.getVersionName(),
                Integer.toString(Build.VERSION.SDK_INT),
                Build.CPU_ABI,
                Build.MODEL,
                Build.SERIAL,
                Integer.toString(GlobalUpdaterState.getVersionCode()));

        ua = (baseUA.length() == 0) ? ua : baseUA + " " + ua;
        preprocessedUserAgent = ua.replaceAll("\\r?\\n", "");

        Log.v(TAG, "Prepared UA = " + preprocessedUserAgent);
    }

    public static String getUserAgentString() {
        return preprocessedUserAgent;
    }
}
