package org.jnrain.mobile.ui.kbs;

import java.text.MessageFormat;

import org.jnrain.mobile.R;
import org.jnrain.mobile.util.DynPageFragmentAdapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;


class ThreadListFragmentAdapter
        extends DynPageFragmentAdapter<ThreadListFragment> {
    protected Context _ctx;

    public ThreadListFragmentAdapter(FragmentManager fm, Context ctx) {
        super(fm);
        _ctx = ctx;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // return "Page" + Integer.toString(position + 1);
        return MessageFormat.format(
                _ctx.getString(R.string.page_nr),
                Integer.toString(position + 1));
    }

    public void addItem(String brd_id, int page) {
        ThreadListFragment frag = new ThreadListFragment(brd_id, page);

        // retain state
        frag.setRetainInstance(true);

        _contents.add(frag);
        notifyDataSetChanged();
    }
}
