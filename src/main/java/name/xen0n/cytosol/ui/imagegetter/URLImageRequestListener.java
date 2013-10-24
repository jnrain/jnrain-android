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
package name.xen0n.cytosol.ui.imagegetter;

import java.io.InputStream;


import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;


public class URLImageRequestListener implements RequestListener<InputStream> {
    private static final String TAG = "URLImgReqListener";
    protected Activity _a;
    protected View _v;
    protected URLDrawable _u;
    protected String _url;
    protected URLImageConsumer consumer;

    public URLImageRequestListener(
            View v,
            URLDrawable u,
            String url,
            Activity a,
            URLImageConsumer consumer) {
        super();
        _v = v;
        _u = u;
        _url = url;
        _a = a;

        this.consumer = consumer;
    }

    @Override
    public void onRequestFailure(SpiceException arg0) {
        // Fetch failed, show the failed image instead
        // TODO: assign a "failed" image here
        Log.d(TAG, "err on image '" + _url + "', exc:" + arg0.toString());
    }

    @Override
    public void onRequestSuccess(InputStream result) {
        Log.v(TAG, "image '" + _url + "' fetched");

        // TODO: What to do about the density?
        Drawable drawable = Drawable.createFromStream(result, "src");

        if (drawable == null) {
            // Image decoding failed, don't progress further or we will get
            // NullPointerException
            // TODO: display indicator of failure
            Log.d(TAG, "Failed to decode image '" + _url + "'!");
            return;
        }

        drawable.setBounds(
                0,
                0,
                0 + drawable.getIntrinsicWidth(),
                0 + drawable.getIntrinsicHeight());

        _a.runOnUiThread(new URLDrawableRefresher(drawable));
    }

    protected class URLDrawableRefresher implements Runnable {
        protected Drawable _drawable;

        public URLDrawableRefresher(Drawable drawable) {
            _drawable = drawable;
        }

        @Override
        public void run() {
            // set the correct bound according to the result from HTTP call
            _u.setBounds(
                    0,
                    0,
                    0 + _drawable.getIntrinsicWidth(),
                    0 + _drawable.getIntrinsicHeight());

            // change the reference of the current drawable to the result
            // from the HTTP call
            _u.setDrawable(_drawable);

            // view-specific postprocess
            // e.g. this is required by TextView's
            if (consumer != null) {
                consumer.doPostprocess(_drawable);
            }
        }
    }
}
