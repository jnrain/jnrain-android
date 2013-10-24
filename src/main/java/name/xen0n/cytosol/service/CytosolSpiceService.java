/*
 * Copyright 2012-2013 JNRain
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
package name.xen0n.cytosol.service;

import java.util.ArrayList;
import java.util.List;

import name.xen0n.cytosol.network.util.GzipRestTemplate;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.app.Application;

import com.octo.android.robospice.SpringAndroidSpiceService;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.binary.InFileInputStreamObjectPersister;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.springandroid.json.jackson.JacksonObjectPersisterFactory;
import com.octo.android.robospice.persistence.string.InFileStringObjectPersister;


public class CytosolSpiceService extends SpringAndroidSpiceService {
    /**
     * Network request timeout in milliseconds.
     */
    public final int REQUEST_TIMEOUT;
    private ArrayList<ClientHttpRequestInterceptor> _interceptors;

    protected CytosolSpiceService(final int requestTimeout) {
        super();

        REQUEST_TIMEOUT = requestTimeout;

        // session interceptor should be a singleton
        _interceptors = new ArrayList<ClientHttpRequestInterceptor>();
    }

    protected void addInterceptor(ClientHttpRequestInterceptor interceptor) {
        _interceptors.add(interceptor);
    }

    public CacheManager createCacheManager(Application application)
            throws CacheCreationException {
        CacheManager cacheManager = new CacheManager();

        InFileStringObjectPersister inFileStringObjectPersister;
        InFileInputStreamObjectPersister inFileInputStreamObjectPersister;
        JacksonObjectPersisterFactory inJSonFileObjectPersisterFactory;
        try {
            inFileStringObjectPersister = new InFileStringObjectPersister(
                    application);
            inFileInputStreamObjectPersister = new InFileInputStreamObjectPersister(
                    application);
            inJSonFileObjectPersisterFactory = new JacksonObjectPersisterFactory(
                    application);

            inFileStringObjectPersister.setAsyncSaveEnabled(true);
            inFileInputStreamObjectPersister.setAsyncSaveEnabled(true);
            inJSonFileObjectPersisterFactory.setAsyncSaveEnabled(true);

            cacheManager.addPersister(inFileStringObjectPersister);
            cacheManager.addPersister(inFileInputStreamObjectPersister);
            cacheManager.addPersister(inJSonFileObjectPersisterFactory);
        } catch (CacheCreationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw e;
        }

        return cacheManager;
    }

    @Override
    public RestTemplate createRestTemplate() {
        RestTemplate restTemplate = new GzipRestTemplate();
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

        // timeout
        manageTimeOuts(restTemplate);

        // session interceptor
        restTemplate.setInterceptors(_interceptors);

        return restTemplate;
    }

    // SO question
    // 16707357/setting-connection-timeout-in-robospice-request-android
    private void manageTimeOuts(RestTemplate restTemplate) {
        // set timeout for requests
        ClientHttpRequestFactory factory = restTemplate.getRequestFactory();
        if (factory instanceof HttpComponentsClientHttpRequestFactory) {
            HttpComponentsClientHttpRequestFactory advancedFactory = (HttpComponentsClientHttpRequestFactory) factory;
            advancedFactory.setConnectTimeout(REQUEST_TIMEOUT);
            advancedFactory.setReadTimeout(REQUEST_TIMEOUT);
        } else if (factory instanceof SimpleClientHttpRequestFactory) {
            SimpleClientHttpRequestFactory advancedFactory = (SimpleClientHttpRequestFactory) factory;
            advancedFactory.setConnectTimeout(REQUEST_TIMEOUT);
            advancedFactory.setReadTimeout(REQUEST_TIMEOUT);
        }
    }
}
