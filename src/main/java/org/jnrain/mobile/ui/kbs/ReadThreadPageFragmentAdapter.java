package org.jnrain.mobile.ui.kbs;

import java.text.MessageFormat;

import org.jnrain.mobile.R;
import org.jnrain.mobile.util.DynPageFragmentAdapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;


class ReadThreadPageFragmentAdapter
        extends DynPageFragmentAdapter<ReadThreadPageFragment> {
    protected Context _ctx;

    public ReadThreadPageFragmentAdapter(FragmentManager fm, Context ctx) {
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
        ReadThreadPageFragment frag = new ReadThreadPageFragment(brd_id, tid, page);

        // retain state in case of configuration change (e.g. rotation)
        frag.setRetainInstance(true);

        _contents.add(frag);
        notifyDataSetChanged();
    }
}
