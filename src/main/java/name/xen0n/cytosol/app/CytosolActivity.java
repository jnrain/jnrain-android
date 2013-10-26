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
package name.xen0n.cytosol.app;

import java.lang.reflect.InvocationTargetException;

import name.xen0n.cytosol.app.base.SpicedRoboActivity;
import name.xen0n.cytosol.ui.util.CytosolActivityHelper;
import android.os.Bundle;

import com.octo.android.robospice.SpiceService;


public abstract class CytosolActivity<T> extends SpicedRoboActivity<T> {
    private CytosolActivityHelper _helper;

    protected CytosolActivity(
            final Class<? extends SpiceService> spiceServiceClass,
            final Class<? extends CytosolActivityHelper> activityHelperClass) {
        super(spiceServiceClass);

        try {
            _helper = activityHelperClass.getConstructor(
                    SpiceRequestListener.class).newInstance(this);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _helper.setUpActionBar(getSupportActionBar());
    }
}
