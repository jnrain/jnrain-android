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
package org.jnrain.mobile;

import org.jnrain.mobile.network.BoardListRequest;
import org.jnrain.weiyu.collection.ListBoards;
import org.jnrain.weiyu.entity.Board;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class BoardListActivity extends SpicedRoboActivity {
	private static final String TAG = "BoardListActivity";
	private static final String CACHE_KEY = "brds_json";

	private String _sec_ord;
	private ListBoards _boards;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_board_list);

		Intent intent = getIntent();
		this._sec_ord = intent.getStringExtra(SectionListActivity.SEC_ORD);
		spiceManager.execute(new BoardListRequest(this._sec_ord), CACHE_KEY,
				DurationInMillis.NEVER, new BoardListRequestListener());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_board_list, menu);
		return true;
	}

	private class BoardListRequestListener implements
			RequestListener<ListBoards> {
		@Override
		public void onRequestFailure(SpiceException arg0) {
			Log.d(TAG, "err on req: " + arg0.toString());
		}

		@Override
		public void onRequestSuccess(ListBoards boards) {
			Log.v(TAG, "got section list: " + boards.toString());
			_boards = boards;

			ListView lv = (ListView) findViewById(R.id.listBoards);
			BoardListAdapter adapter = new BoardListAdapter(
					getApplicationContext(), _boards);
			lv.setAdapter(adapter);

			lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Board brd = _boards.getBoards().get(position);

					Log.i(TAG, "clicked: " + position + ", id=" + id + ", sec="
							+ brd.toString());

					// Intent intent = new Intent(SectionListActivity.this,
					// BoardListActivity.class);
					// intent.putExtra(SEC_ORD, brd.getOrd());
					// startActivity(intent);
				}
			});
		}
	}
}
