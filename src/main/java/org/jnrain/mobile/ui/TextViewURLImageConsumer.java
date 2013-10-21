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
package org.jnrain.mobile.ui;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;


public class TextViewURLImageConsumer extends URLImageConsumer {
    public TextViewURLImageConsumer(View v) {
        super(v);
    }

    @Override
    public void doPostprocess(Drawable drawable) {
        TextView v = (TextView) _container;

        // according to this SO question,
        // stackoverflow.com/questions/7739649/async-imagegetter-not-functioning-properly
        // we need to force a reflow of the container.
        // TODO: extract interface for reusability here
        v.setText(v.getText());
        v.invalidate();
    }
}
