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
package org.jnrain.mobile.ui.ux;

import org.jnrain.mobile.R;
import org.jnrain.mobile.accounts.kbs.KBSLogoutRequest;
import org.jnrain.mobile.accounts.kbs.KBSLogoutRequestListener;
import org.jnrain.mobile.ui.base.JNRainFragment;
import org.jnrain.mobile.ui.kbs.GlobalHotPostsListFragment;
import org.jnrain.mobile.ui.kbs.SectionListFragment;
import org.jnrain.mobile.ui.preferences.SettingsActivity;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;


@SuppressWarnings("rawtypes")
public class NavFragment extends JNRainFragment {
    @InjectView(R.id.btnHotPosts)
    Button btnHotPosts;
    @InjectView(R.id.btnSections)
    Button btnSections;
    @InjectView(R.id.btnPrefs)
    Button btnPrefs;
    @InjectView(R.id.btnRemoveAccount)
    Button btnRemoveAccount;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater
            .inflate(R.layout.nav_fragment, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnHotPosts.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                NavFragment.this.clearBackStack();
                NavFragment.this.switchMainContentTo(
                        new GlobalHotPostsListFragment(),
                        false);
            }
        });

        btnSections.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                NavFragment.this.clearBackStack();
                NavFragment.this.switchMainContentTo(
                        new SectionListFragment(),
                        false);
            }
        });

        btnPrefs.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsActivity.show(getActivity().getApplicationContext());
            }
        });

        btnRemoveAccount.setOnClickListener(new OnClickListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onClick(View v) {
                _listener.makeSpiceRequest(
                        new KBSLogoutRequest(),
                        new KBSLogoutRequestListener(getActivity(), true));
            }
        });
    }
}
