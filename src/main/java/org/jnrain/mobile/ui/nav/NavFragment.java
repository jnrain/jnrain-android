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
package org.jnrain.mobile.ui.nav;

import org.jnrain.mobile.R;
import org.jnrain.mobile.accounts.kbs.KBSLogoutRequest;
import org.jnrain.mobile.accounts.kbs.KBSLogoutRequestListener;
import org.jnrain.mobile.ui.base.JNRainFragment;
import org.jnrain.mobile.ui.kbs.GlobalHotPostsListFragment;
import org.jnrain.mobile.ui.kbs.SectionListFragment;
import org.jnrain.mobile.ui.preferences.SettingsActivity;
import org.jnrain.mobile.util.AccountStateListener;
import org.jnrain.mobile.util.GlobalState;

import roboguice.inject.InjectView;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


@SuppressWarnings("rawtypes")
public class NavFragment extends JNRainFragment
        implements AccountStateListener {
    @InjectView(R.id.textUserID)
    TextView textUserID;
    @InjectView(R.id.listNavMenu)
    ListView listNavMenu;

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

        setUserName(GlobalState.getUserName());
        GlobalState.registerAccountStateListener(this);

        // nav menu
        final NavMenuAdapter menuAdapter = new NavMenuAdapter(getActivity());
        menuAdapter.addItem(new NavItem(
                R.string.title_activity_global_hot_posts_list,
                R.drawable.ic_nav_hotpost) {
            @Override
            public void onNavItemActivated(Context context) {
                NavFragment.this.clearBackStack();
                NavFragment.this.switchMainContentTo(
                        new GlobalHotPostsListFragment(),
                        false);
            }
        });

        menuAdapter.addItem(new NavItem(
                R.string.title_activity_section_list,
                R.drawable.ic_nav_sections) {
            @Override
            public void onNavItemActivated(Context context) {
                NavFragment.this.clearBackStack();
                NavFragment.this.switchMainContentTo(
                        new SectionListFragment(),
                        false);
            }
        });

        menuAdapter.addItem(new NavItem(
                R.string.title_activity_preference,
                R.drawable.ic_nav_settings) {
            @Override
            public void onNavItemActivated(Context context) {
                SettingsActivity.show(getActivity().getApplicationContext());
            }
        });

        menuAdapter.addItem(new NavItem(
                R.string.nav_remove_account,
                R.drawable.ic_nav_logout) {
            @SuppressWarnings("unchecked")
            @Override
            public void onNavItemActivated(Context context) {
                _listener.makeSpiceRequest(
                        new KBSLogoutRequest(),
                        new KBSLogoutRequestListener(getActivity(), true));
            }
        });

        listNavMenu.setAdapter(menuAdapter);
        listNavMenu
            .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(
                        AdapterView<?> parent,
                        View view,
                        int position,
                        long id) {
                    NavItem item = menuAdapter.getItem(position);
                    item.onNavItemActivated(getActivity());
                }
            });

    }

    public void setUserName(String uid) {
        textUserID.setText(uid);
    }

    @Override
    public void onAccountLoggedIn(String uid) {
        setUserName(uid);
    }

    @Override
    public void onAccountLoggedOut() {
        // TODO: something more user-friendly
        setUserName("");
    }
}
