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

/**
 * Java does not provide a way to write an abstract static method (see
 * http://stackoverflow.com/questions/370962/ and its friends), so here is a
 * *huge* workaround.
 * 
 * Not to mention any hassle involving abstract fields...
 * 
 * @author xen0n
 */
public final class UpdateManagerManager {
    private static BaseUpdateManager mgr;

    public static void ensureUpdateManager(
            Class<? extends BaseUpdateManager> managerClass) {
        if (mgr == null) {
            // instantiate the manager
            try {
                mgr = managerClass.newInstance();
            } catch (InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static BaseUpdateManager getUpdateManager() {
        return mgr;
    }
}
