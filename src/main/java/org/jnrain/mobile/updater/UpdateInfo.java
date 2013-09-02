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
package org.jnrain.mobile.updater;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.jnrain.mobile.util.GlobalState;


@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateInfo {
    @JsonProperty("_V")
    private int version;
    private HashMap<String, UpdateChannel> channels;
    private HashMap<Integer, VersionInfo> versions;

    @JsonIgnore
    private int _latestVersion;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public HashMap<String, UpdateChannel> getChannels() {
        return channels;
    }

    public void setChannels(HashMap<String, UpdateChannel> channels) {
        this.channels = channels;
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

        refreshLatestVersion();
    }

    public String toString() {
        return "<UpdateInfo v" + version + ": channels="
                + channels.toString() + ", versions=" + versions.toString()
                + ">";
    }

    protected void refreshLatestVersion() {
        Iterator<Integer> iter = versions.keySet().iterator();
        int maxver = -1;

        while (iter.hasNext()) {
            int ver = iter.next();

            if (ver > maxver) {
                maxver = ver;
            }
        }

        _latestVersion = maxver;
    }

    @JsonIgnore
    public final VersionInfo getLatestVersion() {
        return versions.get(_latestVersion);
    }

    @JsonIgnore
    public final boolean isCurrentVersionLatest() {
        return GlobalState.getVersionCode() >= _latestVersion;
    }

    @JsonIgnore
    public final List<VersionInfo> getVersionsSinceUpdate() {
        ArrayList<VersionInfo> result = new ArrayList<VersionInfo>();
        int currVersion = GlobalState.getVersionCode();
        Iterator<Integer> iter = versions.keySet().iterator();

        while (iter.hasNext()) {
            int ver = iter.next();
            if (ver > currVersion) {
                result.add(versions.get(ver));
            }
        }

        // sort according to version codes in ascending order
        Collections.sort(result, new VersionInfoComparator());
        return result;
    }
}
