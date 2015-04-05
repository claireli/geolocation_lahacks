package com.luis.found;

/**
 * Created by Luis Meraz on 4/4/2015.
 */

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.Locale;

/**
 * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private Fragment[] fragments = new Fragment[] { new HistoryFragment(), new CameraFragment()};
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return fragments[position];
    }



    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //sample Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                // sample return getString(R.string.title_section1).toUpperCase(l);
            case 1:
                //sample return getString(R.string.title_section2).toUpperCase(l);
           //sample case 2:
                //sample return getString(R.string.title_section3).toUpperCase(l);
        }
        return null;
    }
}
