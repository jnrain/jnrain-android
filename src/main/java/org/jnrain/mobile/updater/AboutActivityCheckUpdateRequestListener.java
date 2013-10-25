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
package org.jnrain.mobile.updater;

import name.xen0n.cytosol.network.listeners.NotifyingCheckUpdateRequestListener;
import name.xen0n.cytosol.ui.util.ToastHelper;
import name.xen0n.cytosol.updater.UpdateChannel;
import name.xen0n.cytosol.updater.UpdateInfo;

import org.jnrain.mobile.R;
import org.jnrain.mobile.ui.AboutActivity;
import org.jnrain.mobile.util.GlobalState;

import android.app.Activity;

import com.octo.android.robospice.persistence.exception.SpiceException;


public class AboutActivityCheckUpdateRequestListener
        extends NotifyingCheckUpdateRequestListener {
    private AboutActivity m_activity;

    public AboutActivityCheckUpdateRequestListener(Activity activity) {
        super(activity);
        m_activity = (AboutActivity) activity;
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {
        m_activity.getLoadingDialog().dismiss();
        super.onRequestFailure(spiceException);
    }

    @Override
    public void onRequestSuccess(UpdateInfo result) {
        m_activity.getLoadingDialog().dismiss();

        super.onRequestSuccess(result);

        m_activity.updateLastCheckedTimeDisplay();
        m_activity.updateLatestVersionDisplay(GlobalState
            .getUpdateInfo()
            .getCurrentChannel(m_activity)
            .getLatestVersion());
    }

    @Override
    public void onVersionLatest() {
        m_activity.updateDownloadButtonVisibility(false);
        ToastHelper.makeTextToast(
                ctx,
                R.string.msg_version_is_latest,
                GlobalState.getVersionName());
        super.onVersionLatest();
    }

    @Override
    public void onNewVersionAvailable(UpdateChannel channel) {
        m_activity.updateDownloadButtonVisibility(true);
        super.onNewVersionAvailable(channel);
    }
}
