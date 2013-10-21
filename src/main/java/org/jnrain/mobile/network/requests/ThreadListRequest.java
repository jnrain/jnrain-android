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


public class ThreadListRequest extends SpringAndroidSpiceRequest<ListPosts> {
    private String _brd;
    private int _page;

    public ThreadListRequest(String board) {
        super(ListPosts.class);

        this._brd = board;
        this._page = 1;
    }

    public ThreadListRequest(String board, int page) {
        super(ListPosts.class);

        this._brd = board;
        this._page = page;
    }

    @Override
    public ListPosts loadDataFromNetwork() throws Exception {
        return getRestTemplate().getForObject(
        // "http://rhymin.jnrain.com/api/hot/board/" + this._brd,
                "http://bbs.jnrain.com/rainstyle/board_json.php?name="
                        + this._brd + "&page=" + this._page,
                ListPosts.class);
    }
}
