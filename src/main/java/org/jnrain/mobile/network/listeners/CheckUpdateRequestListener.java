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
package org.jnrain.mobile.network.listeners;

import org.jnrain.mobile.R;
import org.jnrain.mobile.config.ConfigHub;
import org.jnrain.mobile.config.UpdaterConfigUtil;
import org.jnrain.mobile.ui.ux.ToastHelper;
import org.jnrain.mobile.updater.UpdateChannel;
import org.jnrain.mobile.updater.UpdateInfo;
import org.jnrain.mobile.updater.UpdateInfoFile;
import org.jnrain.mobile.util.GlobalState;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.octo.android.robospice.persistence.exception.SpiceException;


public abstract class CheckUpdateRequestListener
        extends ActivityRequestListener<UpdateInfo> {
    private static final String TAG = "ChkUpdReqListener";
    protected Activity m_activity;

    public CheckUpdateRequestListener(Activity activity) {
        super(activity);
        m_activity = activity;
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {
        Log.d(TAG, "err on req: " + spiceException.toString());
        ToastHelper.makeTextToast(ctx, R.string.msg_network_fail);
    }

    @Override
    public void onRequestSuccess(UpdateInfo result) {
        Context ctx = m_activity.getApplicationContext();

        // update global update info
        GlobalState.setUpdateInfo(result);

        // record update info in cache
        UpdateInfoFile.toFile(ctx, result);

        // record last check time
        UpdaterConfigUtil updUtil = ConfigHub.getUpdaterUtil(ctx);
        updUtil.setLastCheckTime();

        UpdateChannel chan = result.getCurrentChannel(m_activity);
        if (chan.isCurrentVersionLatest()) {
            onVersionLatest();
        } else {
            onNewVersionAvailable(chan);
        }
    }

    public abstract void onVersionLatest();

    public abstract void onNewVersionAvailable(UpdateChannel channel);
}
