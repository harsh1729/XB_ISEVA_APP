package com.iseva.app.source.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.iseva.app.source.Fragments.Fragment_Deck1;
import com.iseva.app.source.Fragments.Fragment_Deck2;


/**
 * Created by xb_sushil on 1/31/2017.
 */

public class PagerAdapter_Select_Seat extends FragmentStatePagerAdapter {

    private String tabtitles[] = new String[] { "LOWER", "UPPER"};
    int PAGE_COUNT;

    public PagerAdapter_Select_Seat(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.PAGE_COUNT = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Fragment_Deck1 tab1 = new Fragment_Deck1();
                return tab1;
            case 1:
                Fragment_Deck2 tab2 = new Fragment_Deck2();

                return tab2;

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
