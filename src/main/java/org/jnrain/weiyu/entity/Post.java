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
package org.jnrain.weiyu.entity;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Post {
    private long _id;
    private String _title;
    private String _author;
    private String _board;
    private Date _ctime;
    private int _replies;
    private int _popularity;
    private String _content;
    private String _sign;

    public long getID() {
        return this._id;
    }

    public void setID(long id) {
        this._id = id;
    }

    public String getTitle() {
        return this._title;
    }

    public void setTitle(String name) {
        this._title = name;
    }

    public String getAuthor() {
        return this._author;
    }

    public void setAuthor(String author) {
        this._author = author;
    }

    public String getBoard() {
        return this._board;
    }

    public void setBoard(String board) {
        this._board = board;
    }

    public Date getCtime() {
        return this._ctime;
    }

    public void setCtime(long ctime) {
        this._ctime = new Date(1000 * ctime);
    }

    public int getReplies() {
        return this._replies;
    }

    public void setReplies(int replies) {
        this._replies = replies;
    }

    public int getPopularity() {
        return this._popularity;
    }

    public void setPopularity(int popularity) {
        this._popularity = popularity;
    }

    public String getContent() {
        return this._content;
    }

    public void setContent(String content) {
        this._content = content;
    }

    public String getSign() {
        return this._sign;
    }

    public void setSign(String sign) {
        this._sign = sign;
    }

    public String toString() {
        return ("<PostMetadata: [" + this._id + "][" + this._board + "] \""
                + this._title + "\" by " + this._author + ">");
    }
}
