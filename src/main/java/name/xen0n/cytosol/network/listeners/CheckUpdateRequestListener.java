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
package name.xen0n.cytosol.network.listeners;

import name.xen0n.cytosol.config.ConfigHub;
import name.xen0n.cytosol.config.UpdaterConfigUtil;
import name.xen0n.cytosol.ui.util.ToastHelper;
import name.xen0n.cytosol.updater.UpdateChannel;
import name.xen0n.cytosol.updater.UpdateInfo;
import name.xen0n.cytosol.updater.UpdateInfoFile;

import org.jnrain.mobile.R;
import org.jnrain.mobile.util.GlobalState;

import android.content.Context;
import android.util.Log;

import com.octo.android.robospice.persistence.exception.SpiceException;


public abstract class CheckUpdateRequestListener
        extends ContextRequestListener<UpdateInfo> {
    private static final String TAG = "ChkUpdReqListener";

    public CheckUpdateRequestListener(Context ctx) {
        super(ctx);
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {
        Log.d(TAG, "err on req: " + spiceException.toString());
        ToastHelper.makeTextToast(ctx, R.string.msg_network_fail);
    }

    @Override
    public void onRequestSuccess(UpdateInfo result) {
        // update global update info
        GlobalState.setUpdateInfo(result);

        // record update info in cache
        UpdateInfoFile.toFile(ctx, result);

        // record last check time
        UpdaterConfigUtil updUtil = ConfigHub.getUpdaterUtil(ctx);
        updUtil.setLastCheckTime();

        UpdateChannel chan = result.getCurrentChannel(ctx);
        if (chan.isCurrentVersionLatest()) {
            onVersionLatest();
        } else {
            onNewVersionAvailable(chan);
        }
    }

    public abstract void onVersionLatest();

    public abstract void onNewVersionAvailable(UpdateChannel channel);
}
