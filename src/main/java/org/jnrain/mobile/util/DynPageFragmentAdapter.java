package org.jnrain.mobile.util;

import java.util.ArrayList;

import org.jnrain.mobile.R;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.viewpagerindicator.IconPagerAdapter;

public abstract class DynPageFragmentAdapter<T> extends FragmentPagerAdapter
		implements IconPagerAdapter {
	protected ArrayList<T> _contents;

	public DynPageFragmentAdapter(FragmentManager fm) {
		super(fm);
		_contents = new ArrayList<T>();
	}

	@Override
	public Fragment getItem(int position) {
		return (Fragment) _contents.get(position);
	}

	@Override
	public int getCount() {
		return _contents.size();
	}

	@Override
	public abstract CharSequence getPageTitle(int position);

	@Override
	public int getIconResId(int index) {
		// TODO
		return R.drawable.icon;
	}

	public void addItem() {
		notifyDataSetChanged();
	}
}