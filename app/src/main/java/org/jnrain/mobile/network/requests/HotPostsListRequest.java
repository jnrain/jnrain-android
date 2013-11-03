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

import org.jnrain.kbs.collection.ListHotPosts;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;


public class HotPostsListRequest
        extends SpringAndroidSpiceRequest<ListHotPosts> {

    private String _type;
    private String _name;

    public HotPostsListRequest(String type) {
        super(ListHotPosts.class);
        // TODO: validation
        this._type = type;
        this._name = null;
    }

    public HotPostsListRequest(String type, String name) {
        super(ListHotPosts.class);
        // TODO: validation
        this._type = type;
        this._name = name;
    }

    @Override
    public ListHotPosts loadDataFromNetwork() throws Exception {
        if (this._type == ListHotPosts.GLOBAL) {
            return getRestTemplate().getForObject(
                    /* "http://rhymin.jnrain.com/api/hot/", */
                    "http://bbs.jnrain.com/rainstyle/topten_json.php",
                    ListHotPosts.class);
        } else if (this._type == ListHotPosts.SEC) {
            return getRestTemplate().getForObject(
                    "http://rhymin.jnrain.com/api/hot/sec/" + this._name
                            + "/" + this._type,
                    ListHotPosts.class);
        } else {
            // FIXME: this assumes that _type == 'board' !!
            return getRestTemplate().getForObject(
                    "http://rhymin.jnrain.com/api/hot/board/" + this._name
                            + "/" + this._type,
                    ListHotPosts.class);
        }
    }
}
