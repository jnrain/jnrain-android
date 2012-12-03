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

import java.util.List;

import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.octo.android.robospice.SpringAndroidContentService;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.binary.InFileInputStreamObjectPersister;
import com.octo.android.robospice.persistence.json.jackson.JacksonObjectPersisterFactory;
import com.octo.android.robospice.persistence.string.InFileStringObjectPersister;

import android.app.Application;

public class JNRainSpiceService extends SpringAndroidContentService {
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
		return restTemplate;
	}
}
