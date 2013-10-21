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
package org.jnrain.mobile.accounts.kbs;

import org.jnrain.kbs.entity.SimpleReturnCode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;


public class KBSRegisterRequest
        extends SpringAndroidSpiceRequest<SimpleReturnCode> {

    private String _uid;
    private String _password;
    private String _nickname;
    private String _realname;
    private String _idnumber;
    private String _email;
    private String _phone;
    private String _captcha;
    private int _gender; // 1 - male, 2 - female

    private boolean isPreflight;

    public KBSRegisterRequest() {
        super(SimpleReturnCode.class);
        isPreflight = true;
    }

    public KBSRegisterRequest(
            String uid,
            String password,
            String nickname,
            String realname,
            String idNumber,
            String email,
            String phone,
            String captcha,
            int gender) {
        super(SimpleReturnCode.class);
        isPreflight = false;

        // TODO: validation
        _uid = uid;
        _password = password;
        _nickname = nickname;
        _realname = realname;
        _idnumber = idNumber;
        _email = email;
        _phone = phone;
        _captcha = captcha;
        _gender = gender;
    }

    @Override
    public SimpleReturnCode loadDataFromNetwork() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();

        if (isPreflight) {
            // preflight request, only prereg flag is sent
            params.set("prereg", "1");
        } else {
            // please keep this in sync with rainstyle/apiregister.php
            params.set("userid", _uid);
            params.set("password", _password);
            params.set("nickname", _nickname);
            params.set("realname", _realname);
            params.set("email", _email);
            params.set("phone", _phone);
            params.set("idnumber", _idnumber);
            params.set("gender", Integer.toString(_gender));
            params.set("captcha", _captcha);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> req = new HttpEntity<MultiValueMap<String, String>>(
                params,
                headers);

        return getRestTemplate().postForObject(
                "http://bbs.jnrain.com/rainstyle/apiregister.php",
                req,
                SimpleReturnCode.class);
    }
}
