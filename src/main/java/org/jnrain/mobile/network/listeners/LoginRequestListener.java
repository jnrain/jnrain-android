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

import org.jnrain.mobile.GlobalHotPostsListActivity;
import org.jnrain.mobile.LoginActivity;
import org.jnrain.mobile.R;
import org.jnrain.mobile.config.ConfigHub;
import org.jnrain.mobile.config.LoginInfoUtil;
import org.jnrain.mobile.util.GlobalState;
import org.jnrain.mobile.util.ToastHelper;
import org.jnrain.weiyu.entity.SimpleReturnCode;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.octo.android.robospice.persistence.exception.SpiceException;


public class LoginRequestListener
        extends ActivityRequestListener<SimpleReturnCode> {
    private static final String TAG = "LoginRequestListener";
    private LoginActivity m_activity;
    private String _uid;
    private String _psw;

    public LoginRequestListener(Activity activity, String uid, String psw) {
        super(activity);
        m_activity = (LoginActivity) activity;
        _uid = uid;
        _psw = psw;
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {
        m_activity.getLoadingDialog().dismiss();
        Log.d(TAG, "err on req: " + spiceException.toString());
        ToastHelper.makeTextToast(ctx, R.string.msg_network_fail);
    }

    @Override
    public void onRequestSuccess(SimpleReturnCode result) {
        m_activity.getLoadingDialog().dismiss();
        int status = result.getStatus();

        switch (status) {
            case 0:
                // record user name in global state
                assert _uid.length() > 0;
                GlobalState.setUserName(_uid);

                // successful
                ToastHelper.makeTextToast(ctx, R.string.msg_login_success);

                // save login info
                LoginInfoUtil loginInfoUtil = ConfigHub
                    .getLoginInfoUtil(m_activity.getApplicationContext());
                if (!LoginActivity.GUEST_UID.equals(_uid.toLowerCase())) {
                    loginInfoUtil.saveUserID(_uid);
                    loginInfoUtil.saveUserPSW(_psw);
                }

                // go to hot posts activity
                Intent intent = new Intent();
                intent.setClass(_activity, GlobalHotPostsListActivity.class);
                _activity.startActivity(intent);

                // finish off self
                _activity.finish();

                break;
            case 1:
                ToastHelper.makeTextToast(ctx, R.string.msg_login_fail);
                break;
            case 2:
                ToastHelper.makeTextToast(ctx, R.string.msg_login_no_uid);
                break;
            case 3:
                ToastHelper.makeTextToast(ctx, R.string.msg_login_toomany);
                break;
            case 4:
                ToastHelper.makeTextToast(ctx, R.string.msg_login_disabled);
                break;
            case 5:
                ToastHelper.makeTextToast(ctx, R.string.msg_login_ip_denied);
                break;
            case 6:
                ToastHelper.makeTextToast(ctx, R.string.msg_login_freq);
                break;
            case 7:
                ToastHelper.makeTextToast(ctx, R.string.msg_login_capacity);
                break;
            case 8:
                ToastHelper.makeTextToast(ctx, R.string.msg_login_ipacl);
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
