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
package org.jnrain.mobile.network.requests;

import org.jnrain.kbs.collection.ListPosts;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;


public class ThreadRequest extends SpringAndroidSpiceRequest<ListPosts> {
    private String _brd;
    private long _tid;
    private int _page;

    public ThreadRequest(String board, long tid) {
        super(ListPosts.class);

        this._brd = board;
        this._tid = tid;
        this._page = 1;
    }

    public ThreadRequest(String board, long tid, int page) {
        super(ListPosts.class);

        this._brd = board;
        this._tid = tid;
        this._page = page;
    }

    @Override
    public ListPosts loadDataFromNetwork() throws Exception {
        return getRestTemplate().getForObject(
                "http://bbs.jnrain.com/rainstyle/thread_json.php?boardName="
                        + this._brd + "&ID=" + Long.toString(this._tid)
                        + "&page=" + Integer.toString(this._page),
                ListPosts.class);
    }
}
