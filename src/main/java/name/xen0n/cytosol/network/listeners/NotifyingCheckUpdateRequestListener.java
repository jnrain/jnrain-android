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
package name.xen0n.cytosol.network.listeners;

import name.xen0n.cytosol.updater.UpdateChannel;
import name.xen0n.cytosol.updater.UpdateManagerManager;
import name.xen0n.cytosol.updater.VersionInfo;
import android.content.Context;


public class NotifyingCheckUpdateRequestListener
        extends CheckUpdateRequestListener {
    public NotifyingCheckUpdateRequestListener(Context ctx) {
        super(ctx);
    }

    @Override
    public void onVersionLatest() {
        // intentionally left blank
    }

    @Override
    public void onNewVersionAvailable(UpdateChannel channel) {
        // pop up a dialog showing new version info
        VersionInfo latestVersion = channel.getLatestVersion();

        UpdateManagerManager.getUpdateManager().showUpdateNotifyDialog(
                ctx,
                latestVersion);
    }
}
