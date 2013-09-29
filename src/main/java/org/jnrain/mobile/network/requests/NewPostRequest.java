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
package org.jnrain.mobile.network.requests;

import org.jnrain.kbs.entity.Post;
import org.jnrain.kbs.entity.SimpleReturnCode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;


public class NewPostRequest
        extends SpringAndroidSpiceRequest<SimpleReturnCode> {
    public static final int SIGN_RANDOM = -1;

    private String _brd;
    private String _title;
    private String _content;
    private long _tid;
    private long _reid;
    private int _signid;
    private boolean _is_new_thread;

    public NewPostRequest(
            String brd,
            String title,
            String content,
            int signID) {
        super(SimpleReturnCode.class);

        _is_new_thread = true;

        // TODO: validation
        _brd = brd;
        _title = title;
        _content = content;
        _signid = signID;
    }

    public NewPostRequest(
            String brd,
            long tid,
            long reid,
            String title,
            String content,
            int signID) {
        super(SimpleReturnCode.class);

        _is_new_thread = false;

        // TODO: validation
        _brd = brd;
        _tid = tid;
        _reid = reid;
        _title = title;
        _content = content;
        _signid = signID;
    }

    public NewPostRequest(
            String brd,
            Post thread,
            Post replyTo,
            String title,
            String content,
            int signID) {
        super(SimpleReturnCode.class);

        _is_new_thread = false;

        // TODO: validation
        _brd = brd;
        _tid = thread.getID();
        _reid = replyTo.getID();
        _title = title;
        _content = content;
        _signid = signID;
    }

    @Override
    public SimpleReturnCode loadDataFromNetwork() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.set("Content", _content);
        params.set("board", _brd);
        params.set("signature", Integer.toString(_signid));
        params.set("subject", _title);

        if (_is_new_thread) {
            params.set("ID", "");
            params.set("groupID", "");
            params.set("reID", "0");
        } else {
            params.set("ID", Long.toString(_tid));
            params.set("groupID", Long.toString(_tid));
            params.set("reID", Long.toString(_reid));
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> req = new HttpEntity<MultiValueMap<String, String>>(
                params,
                headers);

        return getRestTemplate().postForObject(
                /* "http://rhymin.jnrain.com/api/post/new/", */
                "http://bbs.jnrain.com/rainstyle/apipost.php",
                req,
                SimpleReturnCode.class);
    }
}
