/*
 * Copyright 2012-2013 JNRain
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jnrain.mobile;

import java.io.InputStream;

import org.jnrain.mobile.util.JNRainURLImageGetter;
import org.jnrain.mobile.util.SpiceRequestListener;
import org.jnrain.weiyu.collection.ListPosts;
import org.jnrain.weiyu.entity.Post;
import org.jnrain.weiyu.pres.formatter.post.JNRainPostFormatter;
import org.jnrain.weiyu.pres.formatter.post.PostFormatter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ThreadAdapter extends BaseAdapter {
	// private static final String TAG = "ThreadAdapter";
	public static final String RESOURCE_BASE_URL = "http://bbs.jnrain.com/rainstyle/";
	private LayoutInflater _inflater;
	private Activity _activity;
	private ListPosts _data;
	private SpiceRequestListener<InputStream> _listener;

	public ThreadAdapter(Activity activity, ListPosts data,
			SpiceRequestListener<InputStream> listener) {
		this._inflater = LayoutInflater.from(activity.getApplicationContext());
		this._activity = activity;
		this._data = data;
		this._listener = listener;
	}

	@Override
	public int getCount() {
		return this._data.getPosts().size();
	}

	@Override
	public Post getItem(int position) throws IndexOutOfBoundsException {
		return this._data.getPosts().get(position);
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
		Post post = getItem(position);

		if (convertView == null) {
			convertView = this._inflater.inflate(R.layout.post_item, null);
		}

		TextView textTitle = (TextView) convertView
				.findViewById(R.id.textTitle);
		TextView textAuthor = (TextView) convertView
				.findViewById(R.id.textAuthor);
		TextView textContent = (TextView) convertView
				.findViewById(R.id.textContent);
		TextView textSignature = (TextView) convertView
				.findViewById(R.id.textSignature);

		textTitle.setText(post.getTitle());
		textAuthor.setText(post.getAuthor());

		// network image enabled content
		Context ctx = _activity.getApplicationContext();
		PostFormatter fmtr = new JNRainPostFormatter();

		textContent.setText(Html.fromHtml(fmtr.preprocessContent(post),
				new JNRainURLImageGetter(_activity, textContent, ctx,
						RESOURCE_BASE_URL, _listener), null));
		textSignature.setText(Html.fromHtml(fmtr.preprocessSign(post),
				new JNRainURLImageGetter(_activity, textSignature, ctx,
						RESOURCE_BASE_URL, _listener), null));

		return convertView;
	}
}
