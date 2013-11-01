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
package org.jnrain.mobile.ui.ux;

import name.xen0n.cytosol.app.SpiceRequestListener;
import name.xen0n.cytosol.ui.ux.ExitPointBehaviorHelper;

import org.jnrain.mobile.R;
import org.jnrain.mobile.accounts.kbs.KBSLogoutRequest;
import org.jnrain.mobile.accounts.kbs.KBSLogoutRequestListener;
import org.jnrain.mobile.ui.nav.NavManager;


public class JNRainExitPointBehaviorHelper extends ExitPointBehaviorHelper {
    @SuppressWarnings("rawtypes")
    private SpiceRequestListener listener;

    public JNRainExitPointBehaviorHelper(SpiceRequestListener<?> listener) {
        super(
                listener.getThisActivity().getApplicationContext(),
                R.string.dlg_exit_confirm_title,
                R.string.dlg_exit_confirm_msg,
                R.string.dlg_exit_confirm_yes,
                R.string.dlg_exit_confirm_no);

        this.listener = listener;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void doExit() {
        // this SHOULD be the last activity on the task stack. logout
        // set user name to "logging out"
        NavManager.getNavFragment().setUserName(R.string.uid_logging_out);

        // super.onBackPressed() is called via shim in listener
        listener.makeSpiceRequest(
                new KBSLogoutRequest(),
                new KBSLogoutRequestListener(
                        listener.getThisActivity(),
                        false));
    }
}
