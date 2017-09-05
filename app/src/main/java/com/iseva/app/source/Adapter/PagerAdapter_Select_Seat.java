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

    public int max_column_deck_1 = 0;
    public int max_column_deck_2 = 0;

    public PagerAdapter_Select_Seat(FragmentManager fm, int NumOfTabs , int max_col_1, int max_col_2) {
        super(fm);
        this.PAGE_COUNT = NumOfTabs;
        this.max_column_deck_1 = max_col_1;
        this.max_column_deck_2 = max_col_2;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Fragment_Deck1 tab1 = new Fragment_Deck1();
                tab1.max_col = max_column_deck_1;
                return tab1;
            case 1:
                Fragment_Deck2 tab2 = new Fragment_Deck2();
                tab2.max_col = max_column_deck_2;
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
