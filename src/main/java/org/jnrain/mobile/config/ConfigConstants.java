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
package org.jnrain.mobile.config;

public class ConfigConstants {
    public static final String EXIT_BEHAVIOR = "exit_behavior";
    public static final String EXIT_DOUBLECLICK_TIMEOUT = "exit_doubleclick_timeout";
    public static final String EXIT_DIRECT = "direct";
    public static final String EXIT_DOUBLECLICK = "doubleclick";
    public static final String EXIT_DIALOG = "dialog";

    // Enums.
    public enum ExitBehavior {
        DIRECT,
        DOUBLECLICK,
        DIALOG
    }

    // Defaults.
    public static final ExitBehavior DEFAULT_EXIT_BEHAVIOR = ExitBehavior.DOUBLECLICK;
    public static final int DEFAULT_EXIT_DOUBLECLICK_TIMEOUT = 3;
}
