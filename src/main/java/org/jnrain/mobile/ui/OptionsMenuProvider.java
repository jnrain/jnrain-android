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
package org.jnrain.mobile.ui;

import org.jnrain.mobile.R;
import org.jnrain.mobile.ui.preferences.SettingsActivity;

import android.app.Activity;
import android.util.Log;
import android.view.KeyEvent;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;


public class OptionsMenuProvider {
    private static final String TAG = "OptionsMenuProvider";

    private static OptionsMenuProvider optionsMenuProvider;

    private OptionsMenuProvider() {
    };

    public static OptionsMenuProvider getOptionsMenuProvider() {
        if (optionsMenuProvider == null) {
            optionsMenuProvider = new OptionsMenuProvider();
        }
        return optionsMenuProvider;
    }

    public boolean createOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.options_items, menu);
        return true;
    }

    public boolean optionsItemSelected(MenuItem aitem, Activity activity) {
        switch (aitem.getItemId()) {
            case android.R.id.home:
                // simulate a back press instead of force-finishing
                boolean result;
                activity.onKeyDown(KeyEvent.KEYCODE_BACK, new KeyEvent(
                        KeyEvent.ACTION_DOWN,
                        KeyEvent.KEYCODE_BACK));
                result = activity.onKeyUp(
                        KeyEvent.KEYCODE_BACK,
                        new KeyEvent(
                                KeyEvent.ACTION_UP,
                                KeyEvent.KEYCODE_BACK));

                Log.d(TAG, result
                        ? "onKeyUp processed"
                        : "onKeyUp wasn't processed");
                if (!result) {
                    activity.onBackPressed();
                }

                return true;

            case R.id.menu_item_settings:
                SettingsActivity.show(activity.getApplicationContext());
                return false;

            case R.id.menu_item_about:
                AboutActivity.show(activity.getApplicationContext());
                return false;

            default:
                return false;

        }

    }
}
