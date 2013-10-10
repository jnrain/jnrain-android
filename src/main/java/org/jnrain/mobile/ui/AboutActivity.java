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
package org.jnrain.mobile.ui;

import java.util.Date;

import org.jnrain.mobile.R;
import org.jnrain.mobile.config.ConfigHub;
import org.jnrain.mobile.config.UpdaterConfigUtil;
import org.jnrain.mobile.network.listeners.AboutActivityCheckUpdateRequestListener;
import org.jnrain.mobile.network.requests.CheckUpdateRequest;
import org.jnrain.mobile.ui.base.JNRainActivity;
import org.jnrain.mobile.ui.ux.DialogHelper;
import org.jnrain.mobile.ui.ux.FormatHelper;
import org.jnrain.mobile.updater.UpdateInfo;
import org.jnrain.mobile.updater.UpdateManager;
import org.jnrain.mobile.updater.VersionInfo;
import org.jnrain.mobile.util.AppVersionHelper;
import org.jnrain.mobile.util.GlobalState;

import roboguice.inject.InjectView;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class AboutActivity extends JNRainActivity<UpdateInfo> {
    @InjectView(R.id.textVersion)
    TextView textVersion;
    @InjectView(R.id.textUpdateChannel)
    TextView textUpdateChannel;
    @InjectView(R.id.textLatestVersion)
    TextView textLatestVersion;
    @InjectView(R.id.textLastChecked)
    TextView textLastChecked;
    @InjectView(R.id.btnCheckUpdate)
    Button btnCheckUpdate;
    @InjectView(R.id.btnDownloadUpdate)
    Button btnDownloadUpdate;

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
        setContentView(R.layout.frag_about);

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

        textVersion.setText(GlobalState.getVersionName());

        // update channel
        String currentChannel = updaterUtil.getCurrentUpdateChannel();
        FormatHelper.setHtmlText(
                this,
                textUpdateChannel,
                R.string.current_update_channel,
                currentChannel,
                AppVersionHelper.getLocalizedNameForUpdateChannel(
                        this,
                        currentChannel));

        // last checked date
        updateLastCheckedTimeDisplay();

        // latest version
        updateLatestVersionDisplay();

        // check update button
        btnCheckUpdate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doCheckUpdates();
            }
        });

        // download update button
        UpdateInfo updInfo = GlobalState.getUpdateInfo();

        if (updInfo == null) {
            updateDownloadButtonVisibility(false);
        } else {
            updateDownloadButtonVisibility(!updInfo
                .isCurrentVersionLatest(getApplicationContext()));
        }

        btnDownloadUpdate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateManager.openApkInBrowser(
                        AboutActivity.this,
                        GlobalState.getUpdateInfo().getLatestVersion(
                                getApplicationContext()));
            }
        });
    }

    public void doCheckUpdates() {
        mHandler.sendMessage(new Message());
        spiceManager.execute(
                new CheckUpdateRequest(),
                new AboutActivityCheckUpdateRequestListener(this));
    }

    public ProgressDialog getLoadingDialog() {
        return loadingDlg;
    }

    public void updateLatestVersionDisplay(VersionInfo version) {
        String codename = version.getCodeName();

        if (codename.length() > 0) {
            // version has codename
            FormatHelper.setHtmlText(
                    this,
                    textLatestVersion,
                    R.string.latest_version_is_w_codename,
                    version.getName(),
                    codename);
        } else {
            // without codename
            FormatHelper.setHtmlText(
                    this,
                    textLatestVersion,
                    R.string.latest_version_is,
                    version.getName());
        }
    }

    public void updateLatestVersionDisplay() {
        // called at init
        UpdateInfo updInfo = GlobalState.getUpdateInfo();

        if (updInfo == null) {
            // never checked
            FormatHelper.setHtmlText(
                    this,
                    textLatestVersion,
                    R.string.latest_version_is,
                    getString(R.string.latest_version_unknown));
        } else {
            // use cached result from last check
            updateLatestVersionDisplay(updInfo
                .getLatestVersion(getApplicationContext()));
        }
    }

    public void updateLastCheckedTimeDisplay(Date lastCheckedTime) {
        String lastCheckedString;
        if (lastCheckedTime.getTime() == 0) {
            // never checked before
            lastCheckedString = getString(R.string.last_checked_never);
        } else {
            // TODO: natural representation like "3 days ago"
            lastCheckedString = DateFormat.format(
                    "yyyy-MM-dd kk:mm",
                    lastCheckedTime).toString();
        }
        FormatHelper.setHtmlText(
                this,
                textLastChecked,
                R.string.last_checked_time,
                lastCheckedString);
    }

    public void updateLastCheckedTimeDisplay() {
        updateLastCheckedTimeDisplay(updaterUtil.getLastCheckTime());
    }

    public void updateDownloadButtonVisibility(boolean show) {
        btnDownloadUpdate.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
