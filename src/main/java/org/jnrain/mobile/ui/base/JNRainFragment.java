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
package org.jnrain.mobile.ui.base;

import org.jnrain.mobile.util.SpiceRequestListener;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;


public class JNRainFragment<T> extends RoboSherlockFragment {
    protected SpiceRequestListener<T> _listener;
    private ContentFragmentHost fragHost;

    @SuppressWarnings("unchecked")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            _listener = (SpiceRequestListener<T>) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement SpiceRequestListener");
        }

        // Also try to acquire the switchable fragments feature
        try {
            fragHost = (ContentFragmentHost) activity;
        } catch (ClassCastException e) {
            fragHost = null;
        }
    }

    public void switchMainContentTo(Fragment fragment, boolean addToBackStack) {
        fragHost.switchContentTo(fragment, addToBackStack);
    }

    public void clearBackStack() {
        fragHost.clearBackStack();
    }
}
