/*
 * Copyright 2012-2013 JNRain
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
package org.jnrain.mobile.ui.kbs;

import java.io.InputStream;

import org.jnrain.kbs.collection.ListPosts;
import org.jnrain.kbs.entity.Post;
import org.jnrain.kbs.pres.formatter.post.JNRainPostFormatter;
import org.jnrain.kbs.pres.formatter.post.PostFormatter;
import org.jnrain.mobile.R;
import org.jnrain.mobile.ui.JNRainURLImageGetter;
import org.jnrain.mobile.util.SpiceRequestListener;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.format.DateFormat;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TextView.BufferType;


public class ThreadAdapter extends BaseAdapter {
    // private static final String TAG = "ThreadAdapter";
    public static final String RESOURCE_BASE_URL = "http://bbs.jnrain.com/rainstyle/";
    public static final String CTIME_FORMAT = "yyyy-MM-dd kk:mm:ss";
    private LayoutInflater _inflater;
    private Activity _activity;
    private ReadThreadFragmentListener _readThreadListener;
    private ListPosts _data;
    private SpiceRequestListener<InputStream> _listener;

    public ThreadAdapter(
            Activity activity,
            ReadThreadFragmentListener readThreadListener,
            ListPosts data,
            SpiceRequestListener<InputStream> listener) {
        _inflater = LayoutInflater.from(activity);
        _activity = activity;
        _readThreadListener = readThreadListener;
        _data = data;
        _listener = listener;
    }

    @Override
    public int getCount() {
        return _data.getPosts().size();
    }

    @Override
    public Post getItem(int position) throws IndexOutOfBoundsException {
        return _data.getPosts().get(position);
    }

    @Override
    public long getItemId(int position) throws IndexOutOfBoundsException {
        if (position < getCount() && position >= 0) {
            return ((long) position);
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Post post = getItem(position);

        if (convertView == null) {
            convertView = this._inflater.inflate(R.layout.post_item, null);
        }

        TextView textTitle = (TextView) convertView
            .findViewById(R.id.textTitle);
        TextView textAuthor = (TextView) convertView
            .findViewById(R.id.textAuthor);
        TextView textCtime = (TextView) convertView
            .findViewById(R.id.textCtime);
        TextView textContent = (TextView) convertView
            .findViewById(R.id.textContent);
        TextView textSignature = (TextView) convertView
            .findViewById(R.id.textSignature);
        ImageButton btnReply = (ImageButton) convertView
            .findViewById(R.id.btnReply);

        textTitle.setText(post.getTitle());
        textAuthor.setText(post.getAuthor());
        textCtime.setText(DateFormat.format(CTIME_FORMAT, post.getCtime()));

        // network image enabled content
        Context ctx = _activity.getApplicationContext();
        PostFormatter fmtr = new JNRainPostFormatter();

        // movement method
        textContent.setMovementMethod(LinkMovementMethod.getInstance());
        textSignature.setMovementMethod(LinkMovementMethod.getInstance());

        // TODO: render UBB code client-side
        textContent.setText(Html.fromHtml(
                fmtr.preprocessContent(post),
                new JNRainURLImageGetter(
                        _activity,
                        textContent,
                        ctx,
                        RESOURCE_BASE_URL,
                        _listener),
                null), BufferType.SPANNABLE);
        textSignature.setText(Html.fromHtml(
                fmtr.preprocessSign(post),
                new JNRainURLImageGetter(
                        _activity,
                        textSignature,
                        ctx,
                        RESOURCE_BASE_URL,
                        _listener),
                null), BufferType.SPANNABLE);

        // bind reply button
        btnReply.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                _readThreadListener.showReplyUIFor(post);
            }
        });

        return convertView;
    }
}
