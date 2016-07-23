package com.bo.cloudshoppinglist.activity;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bo.cloudshoppinglist.activity.fragment.FriendsFragment;
import com.bo.cloudshoppinglist.activity.fragment.ShopListFragment;
/**
 * Created by bowang on 28/03/15.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;
    // Tab Titles
    private String tabtitles[] = new String[]{"Shopping List", "Friends"};
    Context context;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

            // Open FragmentTab1.java
            case 0:
                ShopListFragment fragmenttab1 = new ShopListFragment();
                return fragmenttab1;

            // Open FragmentTab2.java
            case 1:
                FriendsFragment fragmenttab2 = new FriendsFragment();
                return fragmenttab2;

            // Open FragmentTab3.java

        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabtitles[position];
    }
}
