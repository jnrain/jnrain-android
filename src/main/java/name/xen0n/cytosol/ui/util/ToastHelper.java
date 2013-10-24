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
package name.xen0n.cytosol.ui.util;

import java.text.MessageFormat;

import android.content.Context;
import android.widget.Toast;


public class ToastHelper {
    public static void makeTextToast(Context ctx, int resId) {
        makeTextToast(ctx, resId, Toast.LENGTH_SHORT);
    }

    public static void makeTextToast(Context ctx, int resId, int duration) {
        Toast.makeText(ctx, resId, duration).show();
    }

    public static void makeTextToast(Context ctx, CharSequence text) {
        makeTextToast(ctx, text, Toast.LENGTH_SHORT);
    }

    public static void makeTextToast(
            Context ctx,
            CharSequence text,
            int duration) {
        Toast.makeText(ctx, text, duration).show();
    }

    public static void makeTextToast(
            Context ctx,
            int resId,
            Object... formatArgs) {
        Toast.makeText(
                ctx,
                MessageFormat.format(ctx.getString(resId), formatArgs),
                Toast.LENGTH_SHORT).show();
    }
}
