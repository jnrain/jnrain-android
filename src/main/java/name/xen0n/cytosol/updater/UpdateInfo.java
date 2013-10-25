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
package name.xen0n.cytosol.updater;

import java.util.HashMap;
import java.util.Iterator;

import name.xen0n.cytosol.config.ConfigHub;
import name.xen0n.cytosol.config.UpdaterConfigUtil;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import android.content.Context;


@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateInfo {
    @JsonProperty("_V")
    private int version;
    private HashMap<String, UpdateChannel> channels;
    private HashMap<Integer, VersionInfo> versions;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public HashMap<String, UpdateChannel> getChannels() {
        return channels;
    }

    protected void possiblyAssociateChannels() {
        if (channels == null || versions == null) {
            return;
        }

        Iterator<String> iter = this.channels.keySet().iterator();
        while (iter.hasNext()) {
            String name = iter.next();
            UpdateChannel chan = this.channels.get(name);

            chan.associateVersions(versions);
        }
    }

    public void setChannels(HashMap<String, UpdateChannel> channels) {
        this.channels = channels;

        // inject channel names
        Iterator<String> iter = this.channels.keySet().iterator();
        while (iter.hasNext()) {
            String name = iter.next();
            UpdateChannel chan = this.channels.get(name);

            chan.setName(name);
        }

        possiblyAssociateChannels();
    }

    public HashMap<Integer, VersionInfo> getVersions() {
        return versions;
    }

    public void setVersions(HashMap<Integer, VersionInfo> versions) {
        this.versions = versions;

        // inject the keys (version codes) into the VersionInfo objects
        Iterator<Integer> iter = this.versions.keySet().iterator();
        while (iter.hasNext()) {
            int ver = iter.next();
            this.versions.get(ver).setCode(ver);
        }

        possiblyAssociateChannels();
    }

    public String toString() {
        return "<UpdateInfo v" + version + ": channels="
                + channels.toString() + ", versions=" + versions.toString()
                + ">";
    }

    @JsonIgnore
    public UpdateChannel getCurrentChannel(Context ctx) {
        UpdaterConfigUtil updUtil = ConfigHub.getUpdaterUtil(ctx);
        String channelName = updUtil.getCurrentUpdateChannel();

        return channels.get(channelName);
    }

    @JsonIgnore
    public final VersionInfo getLatestVersion(Context ctx) {
        return getCurrentChannel(ctx).getLatestVersion();
    }

    @JsonIgnore
    public final boolean isCurrentVersionLatest(Context ctx) {
        return getCurrentChannel(ctx).isCurrentVersionLatest();
    }
}
