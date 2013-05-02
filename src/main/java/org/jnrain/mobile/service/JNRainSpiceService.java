/*
 * Copyright 2012 JNRain
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
package org.jnrain.mobile.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.app.Application;
import android.util.Log;
import android.webkit.CookieManager;

import com.octo.android.robospice.SpringAndroidContentService;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.binary.InFileInputStreamObjectPersister;
import com.octo.android.robospice.persistence.json.jackson.JacksonObjectPersisterFactory;
import com.octo.android.robospice.persistence.string.InFileStringObjectPersister;

public class JNRainSpiceService extends SpringAndroidContentService {
	private ArrayList<ClientHttpRequestInterceptor> _interceptors;

	public JNRainSpiceService() {
		super();

		// session interceptor should be a singleton
		Log.d(getClass().getSimpleName(), ">>> setting up interceptor(s)");
		_interceptors = new ArrayList<ClientHttpRequestInterceptor>();
		_interceptors.add(new AuthCookieHttpRequestInterceptor());
	}

	public CacheManager createCacheManager(Application application) {
		CacheManager cacheManager = new CacheManager();

		InFileStringObjectPersister inFileStringObjectPersister = new InFileStringObjectPersister(
				application);
		InFileInputStreamObjectPersister inFileInputStreamObjectPersister = new InFileInputStreamObjectPersister(
				application);
		JacksonObjectPersisterFactory inJSonFileObjectPersisterFactory = new JacksonObjectPersisterFactory(
				application);

		inFileStringObjectPersister.setAsyncSaveEnabled(true);
		inFileInputStreamObjectPersister.setAsyncSaveEnabled(true);
		inJSonFileObjectPersisterFactory.setAsyncSaveEnabled(true);

		cacheManager.addPersister(inFileStringObjectPersister);
		cacheManager.addPersister(inFileInputStreamObjectPersister);
		cacheManager.addPersister(inJSonFileObjectPersisterFactory);
		return cacheManager;
	}

	@Override
	public RestTemplate createRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		// find more complete examples in RoboSpice Motivation app
		// to enable Gzip compression and setting request timeouts.

		// web services support json responses
		MappingJacksonHttpMessageConverter jsonConverter = new MappingJacksonHttpMessageConverter();
		FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
		StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
		final List<HttpMessageConverter<?>> listHttpMessageConverters = restTemplate
				.getMessageConverters();

		listHttpMessageConverters.add(jsonConverter);
		listHttpMessageConverters.add(formHttpMessageConverter);
		listHttpMessageConverters.add(stringHttpMessageConverter);
		restTemplate.setMessageConverters(listHttpMessageConverters);

		// session interceptor
		restTemplate.setInterceptors(_interceptors);

		return restTemplate;
	}

	/*
	 * source code referenced from SO question
	 * 7101677/spring-android-using-resttemplate-with-https-and-cookies
	 */
	class AuthCookieHttpRequestInterceptor implements
			ClientHttpRequestInterceptor {
		public static final String COOKIE = "cookie";
		public static final String SET_COOKIE = "set-cookie";
		public static final String COOKIE_STORE = "cookieStore";

		@Override
		public ClientHttpResponse intercept(HttpRequest request, byte[] body,
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
						CookieManager.getInstance().getCookie(
								"http://bbs.jnrain.com/"));
			}

			ClientHttpResponse response = execution.execute(request, body);

			cookies = response.getHeaders().get(SET_COOKIE);
			if (cookies != null) {
				for (String cookie : cookies) {
					Log.d(getClass().getSimpleName(), ">>> response cookie = "
							+ cookie);
					CookieManager.getInstance().setCookie(
							"http://bbs.jnrain.com/", cookie);
				}
				// StaticCacheHelper.storeObjectInCache(COOKIE_STORE, cookies);
			}

			Log.d(getClass().getSimpleName(), ">>> leaving intercept");

			return response;
		}
	}
}
