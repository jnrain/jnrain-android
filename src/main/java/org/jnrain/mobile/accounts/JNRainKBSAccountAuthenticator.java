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
package org.jnrain.mobile.accounts;

import org.jnrain.mobile.LoginActivity;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;


public class JNRainKBSAccountAuthenticator
        extends AbstractAccountAuthenticator {
    private static final String TAG = "JNRainKBSAuth";

    protected Context ctx;

    public JNRainKBSAccountAuthenticator(final Context context) {
        super(context);
        ctx = context;
    }

    @Override
    public Bundle addAccount(
            final AccountAuthenticatorResponse response,
            final String accountType,
            final String authTokenType,
            final String[] requiredFeatures,
            final Bundle options) throws NetworkErrorException {
        Log.d(
                TAG,
                "addAccount [accountType="
                        + (accountType == null ? "null" : accountType)
                        + ", authTokenType="
                        + (authTokenType == null ? "null" : authTokenType)
                        + ", requiredFeatures="
                        + (requiredFeatures == null
                                ? "null"
                                : requiredFeatures.toString()) + "]");

        final Intent intent = new Intent(ctx, LoginActivity.class);
        intent.putExtra(LoginActivity.PARAM_AUTHTOKEN_TYPE, authTokenType);
        intent.putExtra(
                AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE,
                response);

        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);

        return bundle;
    }

    @Override
    public Bundle confirmCredentials(
            final AccountAuthenticatorResponse response,
            final Account account,
            final Bundle options) throws NetworkErrorException {
        Log.d(TAG, "confirmCredentials");

        return null;
    }

    @Override
    public Bundle editProperties(
            final AccountAuthenticatorResponse response,
            final String accountType) {
        Log.d(TAG, "editProperties");

        return null;
    }

    @Override
    public Bundle getAuthToken(
            final AccountAuthenticatorResponse response,
            final Account account,
            final String authTokenType,
            final Bundle options) throws NetworkErrorException {
        Log.d(TAG, "getAuthToken");

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getAuthTokenLabel(final String authTokenType) {
        if (AccountConstants.ACCOUNT_TYPE_KBS.equals(authTokenType))
            return authTokenType;

        return null;
    }

    @Override
    public Bundle hasFeatures(
            final AccountAuthenticatorResponse response,
            final Account account,
            final String[] features) throws NetworkErrorException {
        Log.d(TAG, "hasFeatures: " + features.toString());

        final Bundle result = new Bundle();
        result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);

        return result;
    }

    @Override
    public Bundle updateCredentials(
            final AccountAuthenticatorResponse response,
            final Account account,
            final String authTokenType,
            final Bundle options) throws NetworkErrorException {
        Log.d(TAG, "updateCredentials [account='" + account.name
                + "', authTokenType='" + authTokenType + "']");

        final Intent intent = new Intent(ctx, LoginActivity.class);
        intent.putExtra(LoginActivity.PARAM_AUTHTOKEN_TYPE, authTokenType);
        intent.putExtra(
                AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE,
                response);

        if (!TextUtils.isEmpty(account.name))
            intent.putExtra(LoginActivity.PARAM_USERNAME, account.name);

        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);

        return bundle;
    }
}
