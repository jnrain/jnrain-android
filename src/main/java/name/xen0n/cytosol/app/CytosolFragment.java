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
package name.xen0n.cytosol.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;


public abstract class CytosolFragment<T> extends RoboSherlockFragment {
    public static final String PREVIOUS_TITLE_STORE = "CytosolFragment.prevTitle";
    public static final String CURRENT_TITLE_STORE = "CytosolFragment.currTitle";

    protected SpiceRequestListener<T> _listener;
    private ContentFragmentHost fragHost;

    protected String _prevTitle;
    protected String _currTitle;

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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // remember previous action bar title
        if (savedInstanceState != null) {
            _prevTitle = savedInstanceState.getString(PREVIOUS_TITLE_STORE);
            _currTitle = savedInstanceState.getString(CURRENT_TITLE_STORE);
        } else {
            // TODO: How to retain any CharSequence in a Bundle?
            if (fragHost != null) {
                CharSequence title = fragHost.getActionBarTitle();
                _prevTitle = (title != null) ? title.toString() : null;
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(PREVIOUS_TITLE_STORE, _prevTitle);
        outState.putString(CURRENT_TITLE_STORE, (_currTitle != null)
                ? _currTitle
                : "");
    }

    @Override
    public void onStop() {
        if (fragHost != null) {
            // remember current title for resume
            CharSequence title = fragHost.getActionBarTitle();
            _currTitle = (title != null) ? title.toString() : null;

            // restore action bar title
            if (_prevTitle != null) {
                fragHost.setActionBarTitle(_prevTitle);
            }
        }

        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (fragHost != null) {
            if (_currTitle != null) {
                // restore current title
                fragHost.setActionBarTitle(_currTitle);
            }
        }
    }

    public void finishFragment() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStack();
    }

    public void switchMainContentTo(Fragment fragment, boolean addToBackStack) {
        fragHost.switchContentTo(fragment, addToBackStack);
    }

    public void clearBackStack() {
        fragHost.clearBackStack();
    }

    public CharSequence getActionBarTitle() {
        return (fragHost != null) ? fragHost.getActionBarTitle() : "";
    }

    public void setActionBarTitle(CharSequence title) {
        if (fragHost != null) {
            fragHost.setActionBarTitle(title);
        }
    }

    public void setActionBarTitle(int resId) {
        if (fragHost != null) {
            fragHost.setActionBarTitle(resId);
        }
    }
}
