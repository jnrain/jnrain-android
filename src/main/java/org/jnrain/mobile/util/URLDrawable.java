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
package org.jnrain.mobile.util;

import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;


/*
 * this code taken from
 * stackoverflow.com/questions/7424512/android-html-imagegetter-as-asynctask
 */
public class URLDrawable extends BitmapDrawable {
    protected Drawable _drawable;

    @SuppressWarnings("deprecation")
    public URLDrawable(Drawable drawable) {
        _drawable = drawable;
    }

    @Override
    public void draw(Canvas canvas) {
        if (_drawable != null)
            _drawable.draw(canvas);
    }

    public void setDrawable(Drawable drawable) {
        _drawable = drawable;
    }
}
