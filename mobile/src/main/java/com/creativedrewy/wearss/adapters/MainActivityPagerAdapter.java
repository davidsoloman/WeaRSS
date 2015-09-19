package com.creativedrewy.wearss.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.creativedrewy.wearss.fragments.FeedListingFragment;
import com.creativedrewy.wearss.fragments.ReadListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * For ViewPager on main screen, allows swiping between feed list and read list
 */
public class MainActivityPagerAdapter extends FragmentPagerAdapter {
    private final String[] mTabTitles = { "Feeds", "Read List" };
    private List<Fragment> mSwipeFragments;

    /**
     * Constructor
     */
    public MainActivityPagerAdapter(FragmentManager fm) {
        super(fm);

        mSwipeFragments = new ArrayList<>();
        mSwipeFragments.add(new FeedListingFragment());
        mSwipeFragments.add(new ReadListFragment());
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabTitles[position];
    }

    @Override
    public Fragment getItem(int i) {
        return mSwipeFragments.get(i);
    }

    @Override
    public int getCount() {
        return 2;
    }

    /**
     * Get the current fragment instance by the index value
     */
    public Fragment getFragmentInstance(int index) {
        return mSwipeFragments.get(index);
    }
}
