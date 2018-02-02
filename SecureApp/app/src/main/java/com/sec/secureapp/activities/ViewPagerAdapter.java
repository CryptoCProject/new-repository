package com.sec.secureapp.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sec.secureapp.recyclerView.MyRecyclerViewFragment;

class ViewPagerAdapter extends FragmentStatePagerAdapter {
    String[] tabTitles;
    int numOfTabs;

    String openAuctions = "";
    String runningAuctions = "";

    public ViewPagerAdapter(FragmentManager manager, String[] tabTitles, int numOfTabs, String openAuctions, String runningAuctions) {
        super(manager);

        this.tabTitles = tabTitles;
        this.numOfTabs = numOfTabs;

        this.openAuctions = openAuctions;
        this.runningAuctions = runningAuctions;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {// if the position is 0 we are returning the First tab
            case 0:
                MyRecyclerViewFragment openFragment = new MyRecyclerViewFragment(false, openAuctions);
                return openFragment;
            case 1:
                MyRecyclerViewFragment runningFragment = new MyRecyclerViewFragment(true, runningAuctions);
                return runningFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
