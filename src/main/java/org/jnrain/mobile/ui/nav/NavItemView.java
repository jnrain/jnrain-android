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

import org.jnrain.mobile.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class NavItemView extends RelativeLayout {
    // private static final String TAG = "NavItemView";

    TextView textNavItem;
    View viewIsActive;

    public static final int LAYOUT_RES_ID = R.layout.navitemview;

    protected Context _ctx;
    // protected OnNavItemActivatedListener _onActivatedListener;
    protected CharSequence _itemText;
    protected int _itemIconRes;
    protected boolean _canBeActive;
    protected boolean _isActive;

    public NavItemView(Context context) {
        super(context);

        initLayout(context);
    }

    public NavItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initLayout(context);
        initAttrs(context, attrs);
    }

    public NavItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initLayout(context);
        initAttrs(context, attrs);
    }

    protected void initLayout(Context ctx) {
        _ctx = ctx;
        View.inflate(ctx, LAYOUT_RES_ID, this);
        textNavItem = (TextView) findViewById(R.id.textNavItem);
        viewIsActive = findViewById(R.id.viewIsActive);
        // setOnClickListener(new OnNavItemClickListener());
    }

    protected void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.NavItemView,
                0,
                0);
        try {
            String textProp = a
                .getString(R.styleable.NavItemView_android_text);
            if (textProp != null) {
                doSetText(textProp);
            } else {
                doSetText(isInEditMode() ? "Placeholder title" : "");
            }

            doSetItemIcon(isInEditMode() ? R.drawable.ic_nav_settings : a
                .getResourceId(R.styleable.NavItemView_itemIcon, 0));

            doSetCanBeActive(isInEditMode() ? true : a.getBoolean(
                    R.styleable.NavItemView_canBeActive,
                    true));
            doSetActive(isInEditMode() ? true : a.getBoolean(
                    R.styleable.NavItemView_isActive,
                    false));
        } finally {
            a.recycle();
        }
    }

    /*
     * public OnNavItemActivatedListener getOnActivatedListener() { return
     * _onActivatedListener; }
     * 
     * public void setOnActivatedListener(OnNavItemActivatedListener
     * listener) { _onActivatedListener = listener; }
     */

    public CharSequence getText() {
        return _itemText;
    }

    protected void doSetText(CharSequence text) {
        _itemText = text;
        textNavItem.setText(_itemText);
    }

    public void setText(int resId) {
        doSetText(_ctx.getString(resId));
        invalidate();
        requestLayout();
    }

    public void setText(CharSequence text) {
        doSetText(text);
        invalidate();
        requestLayout();
    }

    protected void doSetItemIcon(int resId) {
        _itemIconRes = resId;
        textNavItem.setCompoundDrawablesWithIntrinsicBounds(
                _itemIconRes,
                0,
                0,
                0);
    }

    public void setItemIcon(int resId) {
        doSetItemIcon(resId);
        invalidate();
        requestLayout();
    }

    protected void doSetActive(boolean active) {
        if (!_canBeActive && active) {
            return;
        }

        _isActive = active;
        viewIsActive.setVisibility(active ? View.VISIBLE : View.INVISIBLE);
    }

    protected void doSetCanBeActive(boolean canBeActive) {
        _canBeActive = canBeActive;

        if (!canBeActive && _isActive) {
            setActive(false);
        }
    }

    public boolean canBeActive() {
        return _canBeActive;
    }

    public void setCanBeActive(boolean canBeActive) {
        doSetCanBeActive(canBeActive);
    }

    public boolean isActive() {
        return _isActive;
    }

    public void setActive(boolean active) {
        doSetActive(active);
        invalidate();
        requestLayout();
    }

    /*
     * public class OnNavItemClickListener implements OnClickListener {
     * 
     * @Override public void onClick(View v) { Log.d(TAG,
     * "Nav item clicked");
     * 
     * if (_onActivatedListener != null) {
     * _onActivatedListener.onNavItemActivated(_ctx); } } }
     */
}
