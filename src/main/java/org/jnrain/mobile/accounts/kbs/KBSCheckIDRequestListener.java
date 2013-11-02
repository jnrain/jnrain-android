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
package org.jnrain.mobile.accounts.kbs;

import name.xen0n.cytosol.network.listeners.ContextRequestListener;
import name.xen0n.cytosol.ui.util.ToastHelper;

import org.jnrain.mobile.R;
import org.jnrain.mobile.ui.base.RegisterPoint;

import android.app.Activity;
import android.util.Log;

import com.octo.android.robospice.persistence.exception.SpiceException;


public class KBSCheckIDRequestListener
        extends ContextRequestListener<KBSRegisterResult> {
    private static final String TAG = "KBSChkIDRequestListener";
    private RegisterPoint _ui;
    private long _timestamp;

    public KBSCheckIDRequestListener(Activity activity, long timestamp) {
        super(activity);

        _ui = (RegisterPoint) activity;
        _timestamp = timestamp;
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {
        Log.d(TAG, "err on req: " + spiceException.toString());
        ToastHelper.makeTextToast(ctx, R.string.msg_network_fail);
    }

    @Override
    public void onRequestSuccess(KBSRegisterResult result) {
        Log.d(TAG, "CheckIDRequest fired at " + Long.toString(_timestamp)
                + " arrived");
        _ui.notifyUIDAvailability(result.getStatus(), _timestamp);
    }
}
