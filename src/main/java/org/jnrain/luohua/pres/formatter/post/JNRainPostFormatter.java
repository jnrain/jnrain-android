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
package org.jnrain.luohua.pres.formatter.post;

import org.jnrain.luohua.entity.Post;


public class JNRainPostFormatter implements PostFormatter {
    public static final String LINESEP_STR = "<br />";
    public static final int LINESEP_STRLEN = LINESEP_STR.length();

    @Override
    public String preprocessContent(Post post) {
        String tmp = post.getContent();

        // Strip the first 4 lines out
        // They are the (redundant) author, source and title lines
        // And an empty line
        int idx = tmp.indexOf(LINESEP_STR) + LINESEP_STRLEN;
        idx = tmp.indexOf(LINESEP_STR, idx) + LINESEP_STRLEN;
        idx = tmp.indexOf(LINESEP_STR, idx) + LINESEP_STRLEN;
        idx = tmp.indexOf(LINESEP_STR, idx) + LINESEP_STRLEN;

        // trim
        tmp = tmp.substring(idx);

        // XXX kludge to get rid of all the IE6 icons
        tmp = tmp.replace("<img src=\"pic/url.gif\" />", "");

        return tmp;
    }

    @Override
    public String preprocessSign(Post post) {
        // TODO Auto-generated method stub
        return post.getSign();
    }

}
