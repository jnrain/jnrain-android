/*
 * Copyright 2012-2013 JNRain
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
package org.jnrain.mobile.ui;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.jnrain.mobile.R;
import org.jnrain.mobile.accounts.AccountConstants;
import org.jnrain.mobile.accounts.kbs.KBSLoginRequest;
import org.jnrain.mobile.accounts.kbs.KBSLoginRequestListener;
import org.jnrain.mobile.ui.base.ContentFragmentHost;
import org.jnrain.mobile.ui.base.LoginPoint;
import org.jnrain.mobile.ui.kbs.GlobalHotPostsListFragment;
import org.jnrain.mobile.ui.ux.DialogHelper;
import org.jnrain.mobile.ui.ux.ExitPointActivity;
import org.jnrain.mobile.ui.ux.NavFragment;
import org.jnrain.mobile.util.GlobalState;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityHelper.SlidingActivityBackAction;


@SuppressWarnings("rawtypes")
public class MainActivity extends ExitPointActivity
        implements ContentFragmentHost, LoginPoint {
    private static final String TAG = "MainActivity";
    private static final String CONTENT_FRAGMENT_STORE = "_content";
    private static final String BACK_ACTION_STORE = "_backAction";

    private Fragment _content;

    // TODO: remove these KBS kludges once KBS is declared dead
    private Handler _loginHandler;
    private ProgressDialog _loadingDlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fm = getSupportFragmentManager();

        setContentView(R.layout.content_frame);
        setBehindContentView(R.layout.menu_frame);

        // retrieve or construct the Above view
        if (savedInstanceState != null) {
            // Restore the saved fragment
            _content = fm.getFragment(
                    savedInstanceState,
                    CONTENT_FRAGMENT_STORE);
        } else {
            // Newly launched, set a default fragment
            _content = new GlobalHotPostsListFragment();
        }

        // set up the Behind view
        Fragment _behindFrag = new NavFragment();

        // insert the views
        fm
            .beginTransaction()
            .replace(R.id.content_frame, _content)
            .replace(R.id.menu_frame, _behindFrag)
            .commit();

        // handler
        _loginHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                _loadingDlg = DialogHelper.showProgressDialog(
                        MainActivity.this,
                        R.string.login_dlg_title,
                        R.string.please_wait,
                        false,
                        false);
            }
        };
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onResume() {
        super.onResume();

        // TODO: refactor this!
        Account account = getAccount();
        GlobalState.setAccount(account);

        Log.d(TAG, "Account info: " + account.toString());

        String psw = AccountManager.get(this).getPassword(account);

        _loginHandler.sendMessage(new Message());
        makeSpiceRequest(
                new KBSLoginRequest(account.name, psw),
                new KBSLoginRequestListener(this, account.name, psw));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // set default Back behavior here
        // because the action is initialized in onPostCreate of
        // Sliding*Activity, this cannot be done in onCreate or the
        // library default won't be overridden.
        //
        // we needn't do restore here, as the library already takes
        // care of this.

        if (savedInstanceState == null) {
            setBackAction(SlidingActivityBackAction.BACK_TO_MENU);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        getSupportFragmentManager().putFragment(
                outState,
                CONTENT_FRAGMENT_STORE,
                _content);

        outState.putSerializable(BACK_ACTION_STORE, getBackAction());
    }

    /**
     * Change the content fragment to the one passed in.
     * 
     * 改变当前内容 Fragment 为传入的 Fragment.
     * 
     * @param fragment
     */
    public synchronized void switchContentTo(
            Fragment fragment,
            boolean addToBackStack) {
        _content = fragment;

        FragmentTransaction xact = getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.content_frame, fragment);

        if (addToBackStack) {
            xact = xact.addToBackStack(null);

            // SlidingMenu back behavior should be updated,
            // or we'll be stuck in an endless loop activating content
            // (from ExitPointActivity) and menu (from SlidingMenuHelper).
            setBackAction(SlidingActivityBackAction.BACK_TO_CONTENT);
        } else {
            setBackAction(SlidingActivityBackAction.BACK_TO_MENU);
        }

        xact.commit();

        getSlidingMenu().showContent();
    }

    @Override
    public void clearBackStack() {
        FragmentManager fm = getSupportFragmentManager();

        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        int fragCount = fm.getBackStackEntryCount();

        Log.d(
                TAG,
                "[onBackPressed] getBackStackEntryCount() = "
                        + Integer.toString(fragCount));

        if (fragCount > 0) {
            // navigate to previous fragment
            fm.popBackStack();

            // update SlidingMenu Back behavior
            if (fragCount == 1) {
                // we've just popped the last entry, restore to
                // BACK_TO_MENU
                setBackAction(SlidingActivityBackAction.BACK_TO_MENU);
            }

            return;
        }

        super.onBackPressed();
    }

    public Account getAccount() {
        // TODO: move this code out to accounts package
        try {
            return new AsyncTask<Void, Void, Account>() {
                @Override
                protected Account doInBackground(Void... params) {
                    AccountManager am = AccountManager
                        .get(getThisActivity());
                    AccountManagerFuture<Account[]> future = am
                        .getAccountsByTypeAndFeatures(
                                AccountConstants.ACCOUNT_TYPE_KBS,
                                null,
                                null,
                                null);
                    Account[] accounts;

                    while (true) {
                        try {
                            accounts = future.getResult();

                            if (accounts != null && accounts.length > 0) {
                                return accounts[0];
                            }

                            // no account
                            @SuppressWarnings("unused")
                            Bundle result = am.addAccount(
                                    AccountConstants.ACCOUNT_TYPE_KBS,
                                    null,
                                    null,
                                    null,
                                    getThisActivity(),
                                    null,
                                    null).getResult();

                        } catch (OperationCanceledException e) {
                            e.printStackTrace();
                            finish();
                        } catch (AuthenticatorException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }.execute((Void) null).get();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public ProgressDialog getLoadingDialog() {
        return _loadingDlg;
    }

    @Override
    public void onAuthenticationSuccess(String uid, String psw) {
        // empty
    }
}
