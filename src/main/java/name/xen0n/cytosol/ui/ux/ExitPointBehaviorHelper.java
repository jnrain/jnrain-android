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
package name.xen0n.cytosol.ui.ux;

import name.xen0n.cytosol.config.UIConfigConstants.ExitBehavior;
import name.xen0n.cytosol.config.UIConfigUtil;
import name.xen0n.cytosol.ui.util.ToastHelper;

import org.jnrain.mobile.R;
import org.jnrain.mobile.config.ConfigHub;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.SystemClock;


public abstract class ExitPointBehaviorHelper {
    // private static final String TAG = "ExitPointBehaviorHelper";

    private final int exitDlgTitleId;
    private final int exitDlgMsgId;
    private final int exitDlgYesId;
    private final int exitDlgNoId;

    private Context ctx;
    private Resources res;

    protected long lastBackPressed = 0;

    protected ExitPointBehaviorHelper(
            Context ctx,
            int exitDlgTitleId,
            int exitDlgMsgId,
            int exitDlgYesId,
            int exitDlgNoId) {
        this.ctx = ctx;
        this.exitDlgTitleId = exitDlgTitleId;
        this.exitDlgMsgId = exitDlgMsgId;
        this.exitDlgYesId = exitDlgYesId;
        this.exitDlgNoId = exitDlgNoId;

        res = ctx.getResources();
    }

    public void onBackPressed() {
        // invoke user-selected exit behavior
        UIConfigUtil uiUtil = ConfigHub.getUIUtil(ctx);
        ExitBehavior exitBehavior = uiUtil.getExitBehavior();

        switch (exitBehavior) {
            case DIRECT:
                // Directly exit w/o confirmation.
                doExit();
                break;

            case DOUBLECLICK:
                long timeout = uiUtil.getExitDoubleclickTimeout();
                long curtime = SystemClock.elapsedRealtime();
                long delay = curtime - lastBackPressed;

                // update last press time
                lastBackPressed = curtime;

                if (delay < timeout * 1000) {
                    // Double click happened, trigger exit.
                    doExit();
                } else {
                    // Toast the confirmation message.
                    ToastHelper.makeTextToast(
                            ctx,
                            R.string.msg_exit_doubleclick,
                            timeout);
                }

                break;

            case DIALOG:
                // Fire up a confirmation dialog.
                new AlertDialog.Builder(ctx)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(res.getString(exitDlgTitleId))
                    .setMessage(res.getString(exitDlgMsgId))
                    .setPositiveButton(
                            res.getString(exitDlgYesId),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(
                                        DialogInterface dialog,
                                        int which) {
                                    doExit();
                                }
                            })
                    .setNegativeButton(res.getString(exitDlgNoId), null)
                    .show();

                break;

            default:
                // Should not happen; let's treat it as a direct exit.
                doExit();
                break;
        }
    }

    protected abstract void doExit();
}
