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
package name.xen0n.cytosol.config;


import name.xen0n.cytosol.config.UIConfigConstants.ExitBehavior;

import android.content.Context;


public class UIConfigUtil extends BaseConfigUtil {
    public UIConfigUtil(Context context) {
        super(context);
    }

    public ExitBehavior getExitBehavior() {
        String cfg = getString(UIConfigConstants.EXIT_BEHAVIOR);

        if (cfg == null) {
            return UIConfigConstants.DEFAULT_EXIT_BEHAVIOR;
        } else if (UIConfigConstants.EXIT_DIRECT.equals(cfg)) {
            return ExitBehavior.DIRECT;
        } else if (UIConfigConstants.EXIT_DOUBLECLICK.equals(cfg)) {
            return ExitBehavior.DOUBLECLICK;
        } else if (UIConfigConstants.EXIT_DIALOG.equals(cfg)) {
            return ExitBehavior.DIALOG;
        } else {
            // Not supposed to happen, but we'd better return default
            return UIConfigConstants.DEFAULT_EXIT_BEHAVIOR;
        }
    }

    public long getExitDoubleclickTimeout() {
        return (long) getInt(
                UIConfigConstants.EXIT_DOUBLECLICK_TIMEOUT,
                UIConfigConstants.DEFAULT_EXIT_DOUBLECLICK_TIMEOUT);
    }
}
