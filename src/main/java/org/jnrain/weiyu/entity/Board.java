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
package org.jnrain.weiyu.entity;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Board {
	private String _id;
	private String _name;
	private List<String> _bm;
	private List<String> _topics;

	public String getID() {
		return this._id;
	}

	public void setID(String id) {
		this._id = id;
	}

	public String getName() {
		return this._name;
	}

	public void setName(String name) {
		this._name = name;
	}

	public List<String> getBM() {
		return this._bm;
	}

	public void setBM(List<String> bm) {
		this._bm = bm;
	}

	public List<String> getTopics() {
		return this._topics;
	}

	public void setTopics(List<String> topics) {
		this._topics = topics;
	}

	public String toString() {
		return ("<Board: " + this._id + " (" + this._name + "), BM: "
				+ this._bm.toString() + ", topics: " + this._topics.toString() + ">");
	}
}
