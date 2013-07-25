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
package org.jnrain.mobile;

import java.text.MessageFormat;

import org.jnrain.mobile.config.ConfigHub;
import org.jnrain.mobile.config.UpdaterConfigUtil;
import org.jnrain.mobile.network.CheckUpdateRequest;
import org.jnrain.mobile.network.listeners.CheckUpdateRequestListener;
import org.jnrain.mobile.ui.ux.DialogHelper;
import org.jnrain.mobile.updater.UpdateInfo;
import org.jnrain.mobile.util.AppVersionHelper;
import org.jnrain.mobile.util.GlobalState;
import org.jnrain.mobile.util.SpicedRoboActivity;

import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class AboutActivity extends SpicedRoboActivity<UpdateInfo> {
    @InjectView(R.id.textVersion)
    TextView textVersion;
    @InjectView(R.id.textUpdateChannel)
    TextView textUpdateChannel;
    @InjectView(R.id.btnCheckUpdate)
    Button btnCheckUpdate;

    @InjectResource(R.string.app_full_name_with_ver)
    public String APP_FULL_NAME_WITH_VER;
    @InjectResource(R.string.current_update_channel)
    public String CURRENT_UPDATE_CHANNEL;

    // private static final String TAG = "AboutActivity";

    private ProgressDialog loadingDlg;
    private Handler mHandler;
    private UpdaterConfigUtil updaterUtil;

    public static void show(Context context) {
        final Intent intent = new Intent(context, AboutActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        updaterUtil = ConfigHub.getUpdaterUtil(getApplicationContext());

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                loadingDlg = DialogHelper.showProgressDialog(
                        AboutActivity.this,
                        R.string.check_updates_dlg_title,
                        R.string.please_wait,
                        false,
                        false);
            }
        };

        textVersion.setText(MessageFormat.format(
                APP_FULL_NAME_WITH_VER,
                GlobalState.getVersionName()));

        String currentChannel = updaterUtil.getCurrentUpdateChannel();
        textUpdateChannel.setText(Html.fromHtml(MessageFormat.format(
                CURRENT_UPDATE_CHANNEL,
                currentChannel,
                AppVersionHelper.getLocalizedNameForUpdateChannel(
                        this,
                        currentChannel))));

        btnCheckUpdate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doCheckUpdates();
            }
        });
    }

    public void doCheckUpdates() {
        mHandler.sendMessage(new Message());
        spiceManager.execute(
                new CheckUpdateRequest(),
                new CheckUpdateRequestListener(this));
    }

    public ProgressDialog getLoadingDialog() {
        return loadingDlg;
    }
}
