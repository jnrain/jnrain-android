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
package org.jnrain.mobile.util;

import org.jnrain.weiyu.collection.ListHotPosts;


public class CacheKeyManager {
    public static final String KEY_GLOBAL_HOT_POSTS = "global_hot_json";
    public static final String KEY_PREFIX_SEC_HOT_POSTS = "sec_hot_json_";
    public static final String KEY_PREFIX_BOARD_HOT_POSTS = "board_hot_json_";

    public static final String KEY_PREFIX_BOARD_LIST = "brds_sec_";

    public static String keyForHotPosts(String type) {
        // TODO: refactor this to something numeric for switch()ification
        if (type.equals(ListHotPosts.GLOBAL)) {
            return KEY_GLOBAL_HOT_POSTS;
        } else if (type.equals(ListHotPosts.SEC)) {
            throw new IllegalArgumentException("entity name needed");
        } else if (type.equals(ListHotPosts.BOARD)) {
            throw new IllegalArgumentException("entity name needed");
        }

        throw new IllegalArgumentException("invalid hot posts type");
    }

    public static String keyForHotPosts(String type, String entityName) {
        // TODO: refactor this to something numeric for switch()ification
        if (type.equals(ListHotPosts.GLOBAL)) {
            throw new IllegalArgumentException("entity name not needed");
        } else if (type.equals(ListHotPosts.SEC)) {
            return KEY_PREFIX_SEC_HOT_POSTS + entityName;
        } else if (type.equals(ListHotPosts.BOARD)) {
            return KEY_PREFIX_BOARD_HOT_POSTS + entityName;
        }

        throw new IllegalArgumentException("invalid hot posts type");
    }

    public static String keyForBoardList(String sec_id, String uid) {
        return KEY_PREFIX_BOARD_LIST + uid + "_" + sec_id;
    }
}
