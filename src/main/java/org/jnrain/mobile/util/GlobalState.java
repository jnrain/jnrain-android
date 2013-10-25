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
package org.jnrain.mobile.util;

import java.util.ArrayList;
import java.util.List;

import name.xen0n.cytosol.util.GlobalUpdaterState;
import android.accounts.Account;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.webkit.CookieSyncManager;


public class GlobalState {
    protected static boolean _cookieInited = false;

    protected static Account _account = null;
    protected static List<AccountStateListener> _accountListeners = null;
    protected static int _accountInitLevel = 0;

    public static synchronized void possiblyInitState(Context ctx) {
        // init version info and updater
        GlobalUpdaterState.possiblyInitState(ctx);

        // cookie manager
        if (!getCookieInited()) {
            CookieSyncManager.createInstance(ctx);
            setCookieInited(true);
        }

        // account state listener
        ensureAccountListenersList();
    }

    protected static void ensureAccountListenersList() {
        if (_accountListeners == null) {
            _accountListeners = new ArrayList<AccountStateListener>();
        }
    }

    public static synchronized boolean getCookieInited() {
        return _cookieInited;
    }

    public static void setCookieInited(boolean inited) {
        _cookieInited = inited;
    }

    public static synchronized int getAccountInitLevel() {
        return _accountInitLevel;
    }

    public static synchronized void incrementAccountInitLevel() {
        _accountInitLevel++;
    }

    public static synchronized void resetAccountInitLevel() {
        _accountInitLevel = 0;
    }

    public static synchronized Account getAccount() {
        return _account;
    }

    public static synchronized void setAccount(Account account) {
        _account = account;

        // notify listeners
        ensureAccountListenersList();
        if (_account != null) {
            // logged in event
            for (AccountStateListener listener : _accountListeners) {
                listener.onAccountLoggedIn(_account.name);
            }
        } else {
            // logged out event
            for (AccountStateListener listener : _accountListeners) {
                listener.onAccountLoggedOut();
            }
        }
    }

    public static synchronized String getUserName() {
        if (_account == null) {
            return "";
        }

        return _account.name;
    }

    public static void registerAccountStateListener(
            AccountStateListener listener) {
        ensureAccountListenersList();
        if (_accountListeners.contains(listener)) {
            return;
        }

        _accountListeners.add(listener);
    }

    public static String getPhoneNumber(Context ctx) {
        TelephonyManager tMgr = (TelephonyManager) (ctx
            .getSystemService(Context.TELEPHONY_SERVICE));
        return tMgr.getLine1Number();
    }
}
