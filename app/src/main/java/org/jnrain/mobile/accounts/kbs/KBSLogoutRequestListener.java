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

import java.io.IOException;

import name.xen0n.cytosol.data.SimpleReturnCode;
import name.xen0n.cytosol.network.listeners.ContextRequestListener;
import name.xen0n.cytosol.ui.util.ToastHelper;

import org.jnrain.mobile.R;
import org.jnrain.mobile.util.AccountStateListener;
import org.jnrain.mobile.util.GlobalState;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.util.Log;

import com.octo.android.robospice.persistence.exception.SpiceException;


public class KBSLogoutRequestListener
        extends ContextRequestListener<SimpleReturnCode> {
    private static final String TAG = "LogoutRequestListener";

    private AccountStateListener _accountListener;
    private boolean _removeAccount;

    public KBSLogoutRequestListener(Activity activity, boolean removeAccount) {
        super(activity);

        try {
            _accountListener = (AccountStateListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement AccountStateListener");
        }

        _removeAccount = removeAccount;
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {
        Log.d(TAG, "err on req: " + spiceException.toString());
        ToastHelper.makeTextToast(ctx, R.string.msg_network_fail);

        // Should not block user exit...
        // TODO: Finer granularity of callback
        _accountListener.onAccountLoggedOut();
    }

    @Override
    public void onRequestSuccess(SimpleReturnCode result) {
        int status = result.getStatus();

        switch (status) {
            case 0:
                String uid = GlobalState.getUserName();

                // remove account if requested
                if (_removeAccount) {
                    AccountManager am = AccountManager.get(ctx);
                    am.removeAccount(
                            GlobalState.getAccount(),
                            new AccountManagerCallback<Boolean>() {
                                @Override
                                public void run(
                                        AccountManagerFuture<Boolean> response) {
                                    try {
                                        boolean result = response
                                            .getResult();

                                        Log
                                            .d(
                                                    TAG,
                                                    "AccountManager.removeAccount: "
                                                            + Boolean
                                                                .toString(result));
                                    } catch (OperationCanceledException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    } catch (AuthenticatorException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                }
                            },
                            null);
                }

                GlobalState.setAccount(null);

                // successful
                ToastHelper.makeTextToast(
                        ctx,
                        R.string.msg_logout_success,
                        uid);

                break;

            default:
                ToastHelper.makeTextToast(
                        ctx,
                        R.string.msg_unknown_status,
                        status);
                break;
        }

        // callback into activity
        // TODO: also, finer granularity callbacks in case statements would
        // be nice
        _accountListener.onAccountLoggedOut();
    }
}
