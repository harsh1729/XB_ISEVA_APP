package com.iseva.app.source.Adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.iseva.app.source.Fragments.Fragment_Routes_Duration;
import com.iseva.app.source.Fragments.Fragment_Routes_Populer;
import com.iseva.app.source.Fragments.Fragment_Routes_Price;
import com.iseva.app.source.Fragments.Fragment_Routes_Time;


/**
 * Created by xb_sushil on 1/25/2017.
 */

public class PagerAdapter extends FragmentPagerAdapter {
   // final int PAGE_COUNT = 4;
    // Tab Titles
    private String tabtitles[] = new String[] { "POPULAR", "TIME", "DURATION","PRICE" };

    int PAGE_COUNT;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.PAGE_COUNT = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                 Fragment_Routes_Populer tab1 = new Fragment_Routes_Populer();
                return tab1;
            case 1:
                Fragment_Routes_Time tab2 = new Fragment_Routes_Time();
                return tab2;
            case 2:
                Fragment_Routes_Duration tab3 = new Fragment_Routes_Duration();
                return tab3;
            case 3:
                Fragment_Routes_Price tab4 = new Fragment_Routes_Price();
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabtitles[position];
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }



}
