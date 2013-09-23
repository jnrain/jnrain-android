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
package org.jnrain.mobile;

import org.jnrain.luohua.collection.ListBoards;
import org.jnrain.luohua.collection.ListPosts;
import org.jnrain.luohua.entity.Board;
import org.jnrain.mobile.network.requests.BoardListRequest;
import org.jnrain.mobile.ui.base.JNRainSlidingActivity;
import org.jnrain.mobile.util.CacheKeyManager;
import org.jnrain.mobile.util.GlobalState;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;


public class BoardListActivity extends JNRainSlidingActivity<ListPosts> {
    @InjectView(R.id.listBoards)
    ListView listBoards;

    public static final String BRD_ID = "org.jnrain.mobile.BRD_ID";
    public static final String NUM_POSTS = "org.jnrain.mobile.NUM_POSTS";

    private static final String TAG = "BoardListActivity";

    private String _sec_ord;
    private ListBoards _boards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_list);
        setBehindContentView(R.layout.hello);

        Intent intent = getIntent();
        _sec_ord = intent.getStringExtra(SectionListActivity.SEC_ORD);
        spiceManager.execute(
                new BoardListRequest(this._sec_ord),
                CacheKeyManager.keyForBoardList(
                        _sec_ord,
                        GlobalState.getUserName()),
                DurationInMillis.ONE_HOUR,
                new BoardListRequestListener());
    }

    public synchronized void updateData() {
        BoardListAdapter adapter = new BoardListAdapter(
                getApplicationContext(),
                _boards);
        listBoards.setAdapter(adapter);

        listBoards
            .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(
                        AdapterView<?> parent,
                        View view,
                        int position,
                        long id) {
                    Board brd = _boards.getBoards().get(position);

                    Log.i(TAG, "clicked: " + position + ", id=" + id
                            + ", sec=" + brd.toString());

                    Intent intent = new Intent(
                            BoardListActivity.this,
                            ThreadListActivity.class);
                    intent.putExtra(BRD_ID, brd.getID());
                    intent.putExtra(NUM_POSTS, brd.getThreads());

                    startActivity(intent);
                }
            });
    }

    private class BoardListRequestListener
            implements RequestListener<ListBoards> {
        @Override
        public void onRequestFailure(SpiceException arg0) {
            Log.d(TAG, "err on req: " + arg0.toString());
        }

        @Override
        public void onRequestSuccess(ListBoards boards) {
            Log.v(TAG, "got section list: " + boards.toString());
            _boards = boards;

            updateData();
        }
    }
}
