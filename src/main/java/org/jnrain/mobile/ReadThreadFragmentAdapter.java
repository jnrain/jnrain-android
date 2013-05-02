package org.jnrain.mobile;

import java.text.MessageFormat;

import org.jnrain.mobile.util.DynPageFragmentAdapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;


class ReadThreadFragmentAdapter
        extends DynPageFragmentAdapter<ReadThreadFragment> {
    protected Context _ctx;

    public ReadThreadFragmentAdapter(FragmentManager fm, Context ctx) {
        super(fm);
        this._ctx = ctx;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return MessageFormat.format(
                this._ctx.getString(R.string.page_nr),
                Integer.toString(position + 1));
    }

    public void addItem(String brd_id, long tid, int page) {
        _contents.add(new ReadThreadFragment(brd_id, tid, page));
        notifyDataSetChanged();
    }
}
