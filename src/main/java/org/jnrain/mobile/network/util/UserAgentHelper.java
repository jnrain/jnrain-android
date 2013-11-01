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

import name.xen0n.cytosol.util.GlobalUpdaterState;


public class UserAgentHelper {
    private static String preprocessedUserAgent;

    public static boolean isUserAgentReady() {
        return preprocessedUserAgent != null;
    }

    public static void initUserAgent(String baseUA) {
        preprocessedUserAgent = baseUA + " JNRain/"
                + GlobalUpdaterState.getVersionName() + " (v"
                + GlobalUpdaterState.getVersionCode();
    }

    public static String getUserAgentString() {
        return preprocessedUserAgent;
    }
}
