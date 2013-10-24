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
package name.xen0n.cytosol.network.util;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import android.util.Log;
import android.webkit.CookieManager;


/*
 * source code referenced from SO question
 * 7101677/spring-android-using-resttemplate-with-https-and-cookies
 */
public class CookieHttpRequestInterceptor
        implements ClientHttpRequestInterceptor {
    public static final String COOKIE = "cookie";
    public static final String SET_COOKIE = "set-cookie";
    public static final String COOKIE_STORE = "cookieStore";

    public final String COOKIE_DOMAIN;

    protected CookieHttpRequestInterceptor(final String cookieDomain) {
        super();

        COOKIE_DOMAIN = cookieDomain;
    }

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution) throws IOException {
        Log.d(getClass().getSimpleName(), ">>> entering intercept");

        List<String> cookies = request.getHeaders().get(COOKIE);
        // if the header doesn't exist, add any existing, saved cookies
        if (cookies == null) {
            /*
             * @SuppressWarnings("unchecked") List<String> cookieStore =
             * (List<String>) StaticCacheHelper
             * .retrieveObjectFromCache(COOKIE_STORE); // if we have stored
             * cookies, add them to the headers if (cookieStore != null) {
             * for (String cookie : cookieStore) {
             * request.getHeaders().add(COOKIE, cookie); } }
             */
            request.getHeaders().add(
                    COOKIE,
                    CookieManager.getInstance().getCookie(COOKIE_DOMAIN));
        }

        ClientHttpResponse response = execution.execute(request, body);

        cookies = response.getHeaders().get(SET_COOKIE);
        if (cookies != null) {
            for (String cookie : cookies) {
                Log.d(getClass().getSimpleName(), ">>> response cookie = "
                        + cookie);
                CookieManager.getInstance().setCookie(COOKIE_DOMAIN, cookie);
            }
            // StaticCacheHelper.storeObjectInCache(COOKIE_STORE,
            // cookies);
        }

        Log.d(getClass().getSimpleName(), ">>> leaving intercept");

        return response;
    }
}
