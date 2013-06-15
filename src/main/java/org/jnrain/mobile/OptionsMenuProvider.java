/*
 * Copyright 2012 JNRain
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
package org.jnrain.mobile;

import android.app.Activity;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class OptionsMenuProvider {
    private static OptionsMenuProvider optionsMenuProvider;
    private OptionsMenuProvider(){};
    public static OptionsMenuProvider getOptionsMenuProvider() {
        if(optionsMenuProvider == null){
            optionsMenuProvider = new OptionsMenuProvider();
        }
        return optionsMenuProvider;
    }
    
    public boolean createOptionsMenu(Menu menu, MenuInflater inflater){   
        inflater.inflate(R.menu.options_items, menu);
        return true;
    }
      
    public boolean optionsItemSelected(MenuItem aitem, Activity activity){
        switch (aitem.getItemId()) {   
            case android.R.id.home:
                activity.finish();
                return true;
                
            case R.id.menu_item_settings:
                SettingsActivity.show(activity.getApplicationContext());
                return false;
                
            default:
                return false;

       }
        
    }
}
