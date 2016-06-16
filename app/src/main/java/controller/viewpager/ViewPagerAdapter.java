package controller.viewpager;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import controller.fragments.ShowListFragment;

/**
 * A class controlling the ViewOager for the fragments.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private final List<ShowListFragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public ShowListFragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFrag(ShowListFragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);

    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    // This needs to be overridden to update the fragment,
    // It is called when data is changed
    @Override
    public int getItemPosition(Object item) {
        ShowListFragment fragment = (ShowListFragment)item;
        String title = fragment.getTag();
        int position = mFragmentTitleList.indexOf(title);

        if (position >= 0) {
            return position;
        } else {
            return POSITION_NONE;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}