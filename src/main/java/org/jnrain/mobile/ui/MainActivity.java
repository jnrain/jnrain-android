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
package org.jnrain.mobile.ui;

import java.io.IOException;

import name.xen0n.cytosol.app.ContentFragmentHost;
import name.xen0n.cytosol.ui.util.DialogHelper;

import org.jnrain.mobile.R;
import org.jnrain.mobile.accounts.AccountConstants;
import org.jnrain.mobile.accounts.kbs.KBSLoginRequest;
import org.jnrain.mobile.accounts.kbs.KBSLoginRequestListener;
import org.jnrain.mobile.ui.base.LoginPoint;
import org.jnrain.mobile.ui.kbs.GlobalHotPostsListFragment;
import org.jnrain.mobile.ui.nav.NavFragment;
import org.jnrain.mobile.ui.ux.ExitPointActivity;
import org.jnrain.mobile.util.GlobalState;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
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
        implements ContentFragmentHost, LoginPoint,
        AccountManagerCallback<Account[]> {
    private static final String TAG = "MainActivity";
    private static final String CONTENT_FRAGMENT_STORE = "_content";

    private Fragment _content;

    // TODO: remove these KBS kludges once KBS is declared dead
    private Handler _loginHandler;
    private ProgressDialog _loadingDlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // SlidingMenu layout setup
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

        // TODO: refactor this!
        initAccount();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // FIXME: this will crash when fragments are not put into fm
        // in advance, then activity got paused
        getSupportFragmentManager().putFragment(
                outState,
                CONTENT_FRAGMENT_STORE,
                _content);
    }

    @Override
    protected void onStop() {
        // reset account init level, so that canceled logins don't make
        // re-entering app impossible w/o killing the backgrounded activity
        GlobalState.resetAccountInitLevel();

        super.onStop();
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
    public CharSequence getActionBarTitle() {
        return getSupportActionBar().getTitle();
    }

    @Override
    public void setActionBarTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void setActionBarTitle(int resId) {
        getSupportActionBar().setTitle(resId);
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

    public void initAccount() {
        // TODO: move this code out to accounts package
        if (GlobalState.getAccount() == null) {
            // prevent infinite recursion
            GlobalState.incrementAccountInitLevel();
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... arg0) {
                    AccountManager am = AccountManager
                        .get(getThisActivity());

                    am.getAccountsByTypeAndFeatures(
                            AccountConstants.ACCOUNT_TYPE_KBS,
                            null,
                            MainActivity.this,
                            null);

                    return null;
                }
            }.execute((Void) null);
        }
    }

    @Override
    public ProgressDialog getLoadingDialog() {
        return _loadingDlg;
    }

    @Override
    public void onAuthenticationSuccess(
            Account account,
            String uid,
            String psw) {
        // set global account on successful login
        GlobalState.setAccount(account);
    }

    @SuppressWarnings("unchecked")
    public void onAccountAcquired(Account account) {
        Log.d(TAG, "Account info: " + account.toString());

        String psw = AccountManager.get(this).getPassword(account);

        _loginHandler.sendMessage(new Message());
        makeSpiceRequest(
                new KBSLoginRequest(account.name, psw),
                new KBSLoginRequestListener(this, account, account.name, psw));
    }

    @Override
    public void run(AccountManagerFuture<Account[]> response) {
        Account[] accounts;

        try {
            accounts = response.getResult();

            if (accounts.length > 0) {
                onAccountAcquired(accounts[0]);
                return;
            }

            // no account
            // try to create one, but don't recurse infinitely
            if (GlobalState.getAccountInitLevel() > 2) {
                // finish self
                finish();
                return;
            }

            // create account
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... arg0) {
                    AccountManager am = AccountManager
                        .get(getThisActivity());

                    try {
                        am.addAccount(
                                AccountConstants.ACCOUNT_TYPE_KBS,
                                null,
                                null,
                                null,
                                getThisActivity(),
                                new AccountManagerCallback<Bundle>() {
                                    @Override
                                    public void run(
                                            AccountManagerFuture<Bundle> response) {
                                        try {
                                            response.getResult();
                                        } catch (OperationCanceledException e) {
                                            // TODO Auto-generated catch
                                            // block
                                            e.printStackTrace();
                                        } catch (AuthenticatorException e) {
                                            // TODO Auto-generated catch
                                            // block
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            // TODO Auto-generated catch
                                            // block
                                            e.printStackTrace();
                                        }

                                        MainActivity.this.initAccount();
                                    }
                                },
                                null)
                            .getResult();
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

                    return null;
                }
            }.execute((Void) null);
        } catch (OperationCanceledException e) {
            // TODO Auto-generated catch block
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
