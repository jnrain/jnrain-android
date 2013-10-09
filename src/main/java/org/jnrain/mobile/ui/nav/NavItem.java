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
package org.jnrain.mobile.ui.nav;

public abstract class NavItem implements OnNavItemActivatedListener {
    protected final int _textId;
    protected final int _iconId;
    protected final boolean _canActivate;
    protected final boolean _isDefault;

    public NavItem(int textId, int iconId, boolean canActivate) {
        this(textId, iconId, canActivate, false);
    }

    public NavItem(
            int textId,
            int iconId,
            boolean canActivate,
            boolean isDefault) {
        _textId = textId;
        _iconId = iconId;
        _canActivate = canActivate;
        _isDefault = isDefault;
    }

    public int getTextId() {
        return _textId;
    }

    public int getIconId() {
        return _iconId;
    }

    public boolean canActivate() {
        return _canActivate;
    }

    public boolean isDefault() {
        return _isDefault;
    }
}
