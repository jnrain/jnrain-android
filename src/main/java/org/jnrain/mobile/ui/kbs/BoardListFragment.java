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
import org.jnrain.kbs.entity.Section;
import org.jnrain.mobile.R;
import org.jnrain.mobile.network.requests.BoardListRequest;
import org.jnrain.mobile.ui.base.JNRainFragment;
import org.jnrain.mobile.util.CacheKeyManager;
import org.jnrain.mobile.util.GlobalState;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

    private static final String TAG = "BoardListFragment";

    private Section _sec;
    private ListBoards _boards;

    public BoardListFragment(Section sec) {
        super();
        _sec = sec;
    }

    public BoardListFragment() {
        // You must implement this, or you'll get a
        // android.support.v4.app.Fragment$InstantiationException: Unable to
        // instantiate fragment org.jnrain.mobile.ui.kbs.BoardListFragment:
        // make sure class name exists, is public, and has an empty
        // constructor that is public
        super();
        _sec = null;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.frag_kbs_board_list,
                container,
                false);

        if (savedInstanceState != null) {
            _sec = (Section) savedInstanceState
                .getSerializable(KBSUIConstants.SECTION_STORE);
            _boards = (ListBoards) savedInstanceState
                .getSerializable(KBSUIConstants.BOARDS_LIST_STORE);
        } else {
            _listener.makeSpiceRequest(
                    new BoardListRequest(_sec.getOrd()),
                    CacheKeyManager.keyForBoardList(
                            _sec.getOrd(),
                            GlobalState.getUserName()),
                    DurationInMillis.ONE_HOUR,
                    new BoardListRequestListener());
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setActionBarTitle(_sec.getName());

        if (_boards != null) {
            updateData();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(KBSUIConstants.SECTION_STORE, _sec);
        outState.putSerializable(KBSUIConstants.BOARDS_LIST_STORE, _boards);
    }

    public synchronized void updateData() {
        if (getActivity() == null) {
            return;
        }

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

                    Fragment frag = new ThreadListFragment(brd.getID(), brd
                        .getThreads());
                    switchMainContentTo(frag, true);
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
