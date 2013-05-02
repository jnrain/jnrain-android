/*
 * Copyright 2013 JNRain
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
package org.jnrain.mobile.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
 * source code referenced from SO question
 * 7101677/spring-android-using-resttemplate-with-https-and-cookies
 */
public class StaticCacheHelper {
	private static final int TIME_TO_LIVE = 43200000; // 12 hours

	private static Map<String, Element> cacheMap = new HashMap<String, Element>();

	/**
	 * Retrieves an item from the cache. If found, the method compares the
	 * object's expiration date to the current time and only returns the object
	 * if the expiration date has not passed.
	 * 
	 * @param cacheKey
	 * @return
	 */
	public static Object retrieveObjectFromCache(String cacheKey) {
		Element e = cacheMap.get(cacheKey);
		Object o = null;
		if (e != null) {
			Date now = new Date();
			if (e.getExpirationDate().after(now)) {
				o = e.getObject();
			} else {
				removeCacheItem(cacheKey);
			}
		}
		return o;
	}

	/**
	 * Stores an object in the cache, wrapped by an Element object. The Element
	 * object has an expiration date, which will be set to now + this class'
	 * TIME_TO_LIVE setting.
	 * 
	 * @param cacheKey
	 * @param object
	 */
	public static void storeObjectInCache(String cacheKey, Object object) {
		Date expirationDate = new Date(System.currentTimeMillis()
				+ TIME_TO_LIVE);
		Element e = new Element(object, expirationDate);
		cacheMap.put(cacheKey, e);
	}

	/**
	 * Stores an object in the cache, wrapped by an Element object. The Element
	 * object has an expiration date, which will be set to now + the
	 * timeToLiveInMilliseconds value that is passed into the method.
	 * 
	 * @param cacheKey
	 * @param object
	 * @param timeToLiveInMilliseconds
	 */
	public static void storeObjectInCache(String cacheKey, Object object,
			int timeToLiveInMilliseconds) {
		Date expirationDate = new Date(System.currentTimeMillis()
				+ timeToLiveInMilliseconds);
		Element e = new Element(object, expirationDate);
		cacheMap.put(cacheKey, e);
	}

	public static void removeCacheItem(String cacheKey) {
		cacheMap.remove(cacheKey);
	}

	public static void clearCache() {
		cacheMap.clear();
	}

	static class Element {

		private Object object;
		private Date expirationDate;

		/**
		 * @param object
		 * @param key
		 * @param expirationDate
		 */
		private Element(Object object, Date expirationDate) {
			super();
			this.object = object;
			this.expirationDate = expirationDate;
		}

		/**
		 * @return the object
		 */
		public Object getObject() {
			return object;
		}

		/**
		 * @param object
		 *            the object to set
		 */
		public void setObject(Object object) {
			this.object = object;
		}

		/**
		 * @return the expirationDate
		 */
		public Date getExpirationDate() {
			return expirationDate;
		}

		/**
		 * @param expirationDate
		 *            the expirationDate to set
		 */
		public void setExpirationDate(Date expirationDate) {
			this.expirationDate = expirationDate;
		}
	}
}