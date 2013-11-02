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


public class KBSRegisterRequestListener
        extends ContextRequestListener<KBSRegisterResult> {
    private static final String TAG = "RegisterRequestListener";
    private RegisterPoint _ui;

    private boolean isPreflight;
    private String _uid;
    private String _psw;

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
            _ui.setSubmitButtonEnabled(true);
        }

        Log.d(TAG, "err on req: " + spiceException.toString());
        ToastHelper.makeTextToast(ctx, R.string.msg_network_fail);
    }

    @Override
    public void onRequestSuccess(KBSRegisterResult result) {
        if (isPreflight) {
            // ignore the result status, it's a constant 0 anyway
            // but tell UI to update captcha
            _ui.updateCaptcha(result.getCaptchaDrawable());

            return;
        }

        _ui.getLoadingDialog().dismiss();
        _ui.setSubmitButtonEnabled(true);

        int status = result.getStatus();
        Log.d(TAG, "Returned " + Integer.toString(status));

        switch (status) {
            case 0:
                // success, toast and send back result
                ToastHelper.makeTextToast(ctx, R.string.reg_status_0);
                _ui.onRegisterSuccess(_uid, _psw);
                break;

            // Various failures
            case 101:
                ToastHelper.makeTextToast(ctx, R.string.reg_status_101);
                break;
            case 201:
                ToastHelper.makeTextToast(ctx, R.string.reg_status_201);
                break;
            case 202:
                ToastHelper.makeTextToast(ctx, R.string.reg_status_202);
                break;
            case 203:
                ToastHelper.makeTextToast(ctx, R.string.reg_status_203);
                break;
            case 204:
                ToastHelper.makeTextToast(ctx, R.string.reg_status_204);
                break;
            case 299:
                ToastHelper.makeTextToast(ctx, R.string.reg_status_299);
                break;
            case 301:
                ToastHelper.makeTextToast(ctx, R.string.reg_status_301);
                break;
            case 302:
                ToastHelper.makeTextToast(ctx, R.string.reg_status_302);
                break;
            case 303:
                ToastHelper.makeTextToast(ctx, R.string.reg_status_303);
                break;
            case 304:
                ToastHelper.makeTextToast(ctx, R.string.reg_status_304);
                break;
            case 305:
                ToastHelper.makeTextToast(ctx, R.string.reg_status_305);
                break;
            case 306:
                ToastHelper.makeTextToast(ctx, R.string.reg_status_306);
                break;
            case 307:
                ToastHelper.makeTextToast(ctx, R.string.reg_status_307);
                break;
            case 399:
                ToastHelper.makeTextToast(ctx, R.string.reg_status_399);
                break;

            default:
                ToastHelper.makeTextToast(
                        ctx,
                        R.string.msg_unknown_status,
                        Integer.toString(status));
                break;
        }
    }
}
