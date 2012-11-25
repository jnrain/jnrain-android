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
package org.jnrain.weiyu.collection;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.jnrain.weiyu.entity.Board;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ListBoards {
	private String _sec_id;
	private List<Board> _boards;

	public String getSecID() {
		return this._sec_id;
	}

	public void setSecID(String sec_id) {
		this._sec_id = sec_id;
	}

	public List<Board> getBoards() {
		return this._boards;
	}

	public void setBoards(List<Board> boards) {
		this._boards = boards;
	}

	public String toString() {
		return ("<ListBoards: sec " + this._sec_id + ", len="
				+ this._boards.size() + ", " + this._boards.toString() + ">");
	}
}
