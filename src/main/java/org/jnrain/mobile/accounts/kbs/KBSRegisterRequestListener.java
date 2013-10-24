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

import name.xen0n.cytosol.data.SimpleReturnCode;
import name.xen0n.cytosol.network.listeners.ContextRequestListener;
import name.xen0n.cytosol.ui.util.ToastHelper;

import org.jnrain.mobile.R;
import org.jnrain.mobile.ui.base.RegisterPoint;

import android.accounts.Account;
import android.app.Activity;
import android.util.Log;

import com.octo.android.robospice.persistence.exception.SpiceException;


public class KBSRegisterRequestListener
        extends ContextRequestListener<SimpleReturnCode> {
    private static final String TAG = "RegisterRequestListener";
    private RegisterPoint _ui;
    private Account _account;
    private String _uid;
    private String _psw;

    private boolean isPreflight;

    public KBSRegisterRequestListener(Activity activity) {
        super(activity);

        isPreflight = true;
        _ui = (RegisterPoint) activity;
    }

    public KBSRegisterRequestListener(
            Activity activity,
            String uid,
            String psw) {
        super(activity);

        isPreflight = false;
        _ui = (RegisterPoint) activity;
        _uid = uid;
        _psw = psw;
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {
        if (!isPreflight) {
            _ui.getLoadingDialog().dismiss();
        }

        Log.d(TAG, "err on req: " + spiceException.toString());
        ToastHelper.makeTextToast(ctx, R.string.msg_network_fail);
    }

    @Override
    public void onRequestSuccess(SimpleReturnCode result) {
        if (isPreflight) {
            // ignore the result, it's a constant 0 anyway
            // tell UI to fetch captcha
            _ui.fetchCaptcha();

            return;
        }

        _ui.getLoadingDialog().dismiss();

        int status = result.getStatus();
        switch (status) {
            case 0:
                // TODO
                break;

            default:
                ToastHelper.makeTextToast(
                        ctx,
                        R.string.msg_unknown_status,
                        status);
                break;
        }
    }
}
