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
package org.jnrain.mobile.ui.kbs;

import org.jnrain.kbs.collection.ListBoards;
import org.jnrain.kbs.entity.Board;
import org.jnrain.mobile.R;
import org.jnrain.mobile.network.requests.BoardListRequest;
import org.jnrain.mobile.ui.base.JNRainFragment;
import org.jnrain.mobile.util.CacheKeyManager;
import org.jnrain.mobile.util.GlobalState;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;


public class BoardListFragment extends JNRainFragment<ListBoards> {
    @InjectView(R.id.listBoards)
    ListView listBoards;

    public static final String BRD_ID = "org.jnrain.mobile.BRD_ID";
    public static final String NUM_POSTS = "org.jnrain.mobile.NUM_POSTS";

    private static final String SEC_ORD_STORE = "_secOrd";
    private static final String LIST_BOARDS_STORE = "_boards";

    private static final String TAG = "BoardListFragment";

    private String _sec_ord;
    private ListBoards _boards;

    public BoardListFragment(String secOrd) {
        super();
        _sec_ord = secOrd;
    }

    public BoardListFragment() {
        // You must implement this, or you'll get a
        // android.support.v4.app.Fragment$InstantiationException: Unable to
        // instantiate fragment org.jnrain.mobile.ui.kbs.BoardListFragment:
        // make sure class name exists, is public, and has an empty
        // constructor that is public
        super();
        _sec_ord = null;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.activity_board_list,
                container,
                false);

        if (savedInstanceState != null) {
            _sec_ord = savedInstanceState.getString(SEC_ORD_STORE);
            _boards = (ListBoards) savedInstanceState
                .getSerializable(LIST_BOARDS_STORE);
        } else {
            _listener.makeSpiceRequest(
                    new BoardListRequest(_sec_ord),
                    CacheKeyManager.keyForBoardList(
                            _sec_ord,
                            GlobalState.getUserName()),
                    DurationInMillis.ONE_HOUR,
                    new BoardListRequestListener());
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (_boards != null) {
            updateData();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(SEC_ORD_STORE, _sec_ord);
        outState.putSerializable(LIST_BOARDS_STORE, _boards);
    }

    public synchronized void updateData() {
        BoardListAdapter adapter = new BoardListAdapter(
                getActivity(),
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

                    // Intent intent = new Intent(
                    // BoardListFragment.this,
                    // ThreadListActivity.class);
                    // intent.putExtra(BRD_ID, brd.getID());
                    // intent.putExtra(NUM_POSTS, brd.getThreads());

                    // startActivity(intent);
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
