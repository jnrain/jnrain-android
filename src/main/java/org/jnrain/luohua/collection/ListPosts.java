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
package org.jnrain.luohua.collection;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.jnrain.luohua.entity.Post;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ListPosts {
    private List<Post> _posts;

    public List<Post> getPosts() {
        return this._posts;
    }

    public void setPosts(List<Post> boards) {
        this._posts = boards;
    }

    public String toString() {
        return ("<ListHotPosts: len=" + this._posts.size() + ", "
                + this._posts.toString() + ">");
    }
}
