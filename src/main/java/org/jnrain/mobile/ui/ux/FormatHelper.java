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
package org.jnrain.mobile.ui.ux;

import java.text.MessageFormat;

import android.content.Context;
import android.text.Html;
import android.widget.TextView;


public class FormatHelper {
    /**
     * Format an HTML fragment using a resource string as format, then
     * setText a TextView with this fragment.
     * 
     * 用资源字符串作为格式, 来格式化一个 HTML 片段, 并将其设置到一个 TextView 上.
     * 
     * @param ctx
     *            Android context to use
     * @param view
     *            TextView to be setText
     * @param resId
     *            resource ID of format string
     * @param formatArgs
     *            list of arguments passed to formatter
     */
    public static void setHtmlText(
            Context ctx,
            TextView view,
            int resId,
            Object... formatArgs) {
        view.setText(Html.fromHtml(MessageFormat.format(
                ctx.getString(resId),
                formatArgs)));
    }
}
