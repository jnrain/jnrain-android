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
package org.jnrain.mobile.network.util;

import java.io.IOException;
import java.net.URI;

import name.xen0n.cytosol.network.util.GzipRestTemplate;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;


public class JNRainRestTemplate extends GzipRestTemplate {
    @Override
    protected ClientHttpRequest createRequest(URI url, HttpMethod method)
            throws IOException {
        ClientHttpRequest request = super.createRequest(url, method);

        // override the default User-Agent if an app-specific one is ready
        HttpHeaders hdrs = request.getHeaders();
        if (UserAgentHelper.isUserAgentReady()) {
            hdrs.setUserAgent(UserAgentHelper.getUserAgentString());
        }

        return request;
    }
}
