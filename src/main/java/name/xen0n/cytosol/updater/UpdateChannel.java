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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import android.app.Activity;


@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateChannel {
    @JsonIgnore
    private String name;
    private int latest;

    @JsonIgnore
    private HashMap<Integer, VersionInfo> _versions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLatest() {
        return latest;
    }

    public void setLatest(int latest) {
        this.latest = latest;
    }

    public String toString() {
        return "<UpdateChannel " + name + ": latest=" + latest + ">";
    }

    public final String getLocalizedName(Activity activity) {
        return AppVersionHelper.getLocalizedNameForUpdateChannel(
                activity,
                name);
    }

    @JsonIgnore
    public void associateVersions(HashMap<Integer, VersionInfo> versions) {
        _versions = versions;
    }

    @JsonIgnore
    public final VersionInfo getLatestVersion() {
        return _versions.get(latest);
    }

    @JsonIgnore
    public final boolean isCurrentVersionLatest() {
        return UpdateManagerManager
            .getUpdateManager()
            .getCurrentVersionCode() >= latest;
    }

    @JsonIgnore
    public final List<VersionInfo> getVersionsSinceUpdate() {
        ArrayList<VersionInfo> result = new ArrayList<VersionInfo>();
        int currVersion = UpdateManagerManager
            .getUpdateManager()
            .getCurrentVersionCode();
        Iterator<Integer> iter = _versions.keySet().iterator();

        while (iter.hasNext()) {
            int ver = iter.next();
            if (ver > currVersion) {
                result.add(_versions.get(ver));
            }
        }

        // sort according to version codes in ascending order
        Collections.sort(result, new VersionInfoComparator());
        return result;
    }
}
