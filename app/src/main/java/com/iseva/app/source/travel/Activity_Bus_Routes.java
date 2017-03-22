package com.iseva.app.source.travel;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.iseva.app.source.Adapter.PagerAdapter;
import com.iseva.app.source.Fragments.Fragment_Routes_Duration;
import com.iseva.app.source.Fragments.Fragment_Routes_Populer;
import com.iseva.app.source.Fragments.Fragment_Routes_Price;
import com.iseva.app.source.Fragments.Fragment_Routes_Time;
import com.iseva.app.source.R;
import com.iseva.app.source.Realm_objets.Filter;


import io.realm.Realm;
import io.realm.RealmResults;


public class Activity_Bus_Routes extends AppCompatActivity {

    PagerSlidingTabStrip tabsStrip;
    private TextView header_tv;
    private ImageView header_iv;
    private TextView header_tv_second;

    private Realm My_realm;
    private int LastTab = 0;
    ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    public int time_flag = 0;
    public int duration_flag = 0;
    public int price_flag = 0;

    LinearLayout layout_header_text;


    public static Boolean Bustype_ac_flag = false;
    public static Boolean Bustype_sleeper_flag = false;
    public static Boolean Bustype_MultiAxle_flag = false;

    public static Boolean Busbrand_Volvo_flag = false;
    public static Boolean Busbrand_Mercesdes_flag = false;
    public static Boolean Busbrand_Scania_flag = false;

    public static int filter_min_price =0 ;
    public static int filter_max_price = 1000;

    public static int filter_min_price_fixed =0 ;
    public static int filter_max_price_fixed = 1000;

    public static Boolean price_range_flag =false;

    public static Boolean fisttime_flag = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__bus__routes);



        My_realm = Realm.getInstance(getApplicationContext());

       fisttime_flag = true;
        My_realm.beginTransaction();
        My_realm.clear(Filter.class);
        My_realm.commitTransaction();

        layout_header_text = (LinearLayout)findViewById(R.id.layout_header_text);
        layout_header_text.setGravity(Gravity.CENTER_VERTICAL);
        header_tv = (TextView)findViewById(R.id.header_text);
        header_tv.setVisibility(View.GONE);

        LayoutInflater inflater = (LayoutInflater)Activity_Bus_Routes.this.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        View v =  inflater.inflate(R.layout.header_bus_routs_screen,null);
        TextView fromcity = (TextView)v.findViewById(R.id.header_from_city);
        TextView tocity = (TextView)v.findViewById(R.id.header_to_city);
        TextView date = (TextView)v.findViewById(R.id.header_date);

        String header_date = header_format_date(Search_Buses_Key.Selected_date);
        fromcity.setText(Search_Buses_Key.From_City_name);
        tocity.setText(Search_Buses_Key.To_City_name);
        date.setText(header_date);

        layout_header_text.addView(v);




        header_iv = (ImageView)findViewById(R.id.header_back_button);
        header_tv_second = (TextView)findViewById(R.id.header_second_text);

       // header_tv_second.setPadding(10,0,20,0);
       // header_tv_second.setBackgroundResource(R.drawable.refine_normal);
        header_tv_second.setCompoundDrawablesWithIntrinsicBounds(R.drawable.refine_normal_test,0,0,0);
        header_tv_second.setText("");
        header_tv_second.setVisibility(View.VISIBLE);
       viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(2);



        pagerAdapter =  new PagerAdapter(getSupportFragmentManager(),4);
        viewPager.setAdapter(pagerAdapter);

        tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);

        tabsStrip.setViewPager(viewPager);
        set_fist_tab_color();

        viewPager.setCurrentItem(0);


        tabsStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                LastTab = position;
             LinearLayout   mTabsLinearLayout = ((LinearLayout)tabsStrip.getChildAt(0));
                for(int i=0; i < mTabsLinearLayout.getChildCount(); i++) {

                    TextView tv = (TextView) mTabsLinearLayout.getChildAt(i);

                    if(i == position){
                        if(i ==1 )
                        {
                            if(time_flag == 0)
                            {
                                tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.sort_up_test, 0);
                                tv.setCompoundDrawablePadding(3);
                            }
                            else
                            {
                                tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.sort_down_test, 0);
                                tv.setCompoundDrawablePadding(3);
                            }



                        }
                        else if(i == 2)
                        {
                            if(duration_flag == 0)
                            {
                                tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.sort_up_test, 0);
                                tv.setCompoundDrawablePadding(3);
                            }
                            else
                            {
                                tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.sort_down_test, 0);
                                tv.setCompoundDrawablePadding(3);
                            }
                        }
                        else if(i == 3)
                        {
                            if(price_flag == 0)
                            {
                                tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.sort_up_test, 0);
                                tv.setCompoundDrawablePadding(3);
                            }
                            else
                            {
                                tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.sort_down_test, 0);
                                tv.setCompoundDrawablePadding(3);
                            }
                        }
                        tv.setTextColor(ContextCompat.getColor(Activity_Bus_Routes.this, R.color.app_theme_color));
                    } else {
                        tv.setTextColor(Color.BLACK);
                        tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    }

                }

                setfragment(position);

            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        header_iv.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                activity_dismiss();
            }
        });

        header_tv_second.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent i = new Intent(Activity_Bus_Routes.this,Activity_filter.class);
                startActivity(i);
                overridePendingTransition(R.anim.anim_in, R.anim.anim_none);
            }
        });

        initializefilterdata();


    }


    public void  setfragment(int position)
    {
        FragmentManager fm = getSupportFragmentManager();

        if(position == 1)
        {
            Fragment_Routes_Populer last_fg = (Fragment_Routes_Populer) fm.findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + (position-1));
            Fragment_Routes_Time current_fg = (Fragment_Routes_Time) fm.findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + (position));
            Fragment_Routes_Duration next_fg = (Fragment_Routes_Duration) fm.findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + (position+1));

            if (current_fg != null) {
                current_fg.set_list_item();
            }
            if (last_fg != null) {
                last_fg.showloader();
            }
            if (next_fg != null) {
                next_fg.showloader();
            }

        }
        else if(position ==2)
        {
            Fragment_Routes_Time last_fg = (Fragment_Routes_Time) fm.findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + (position-1));
            Fragment_Routes_Duration current_fg = (Fragment_Routes_Duration) fm.findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + position);
            Fragment_Routes_Price next_fg = (Fragment_Routes_Price) fm.findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + (position+1));
            if (current_fg != null) {
                current_fg.set_list_item();
            }
            if (last_fg != null) {
                last_fg.showloader();
            }
            if (next_fg != null) {
                next_fg.showloader();
            }
        }
        else if(position == 3)
        {
            Fragment_Routes_Duration last_fg = (Fragment_Routes_Duration) fm.findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + (position-1));
            Fragment_Routes_Price current_fg  = (Fragment_Routes_Price) fm.findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + position);

            if (current_fg != null) {
                current_fg.set_list_item();
            }
            if (last_fg != null) {
                last_fg.showloader();
            }

        }
        else
        {

            Fragment_Routes_Populer current_fg  = (Fragment_Routes_Populer) fm.findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + position);
            Fragment_Routes_Time next_fg = (Fragment_Routes_Time) fm.findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + (position+1));
            if (current_fg != null) {
                current_fg.set_list_item();
            }
            if (next_fg != null) {
                next_fg.showloader();
            }
        }
    }

    public void initializefilterdata()
    {
        Bustype_ac_flag = false;
        Bustype_MultiAxle_flag = false;
        Bustype_sleeper_flag = false;
        Busbrand_Mercesdes_flag = false;
        Busbrand_Volvo_flag = false;
        Busbrand_Scania_flag = false;

        filter_min_price =0 ;
        filter_max_price = 1000;

        price_range_flag = false;

    }


    public String header_format_date(String date)
    {
        String[] months = { "January", "Febrauary", "March", "April","May","June","July","August","September","October","November","December" };
        String final_string = "";
       String day = date.substring(8,date.length());
        int month = Integer.parseInt(date.substring(5,7));

        final_string = day+" "+months[month-1];

        return final_string;
    }

    public void activity_dismiss()
    {
        this.finish();
        overridePendingTransition(R.anim.anim_none, R.anim.anim_out);
    }

    public void set_fist_tab_color()
    {
       LinearLayout mTabsLinearLayout = ((LinearLayout)tabsStrip.getChildAt(0));
        for(int i=0; i < mTabsLinearLayout.getChildCount(); i++){
            TextView tv = (TextView) mTabsLinearLayout.getChildAt(i);
            tv.setTag(R.string.tab_tag,i);

            if(i == 0){
                tv.setTextColor(ContextCompat.getColor(Activity_Bus_Routes.this, R.color.app_theme_color));

            } else {

                tv.setTextColor(Color.BLACK);
            }

           tv.setOnClickListener(new View.OnClickListener(){

               @Override
               public void onClick(View view) {


                   String a = view.getTag(R.string.tab_tag).toString();
                   int current_position = Integer.parseInt(a);
                   FragmentManager fm = getSupportFragmentManager();

                    if(LastTab == current_position)
                    {
                        if(current_position == 1)
                        {

                                if (time_flag == 0) {
                                    time_flag = 1;
                                    clickOnSameTab(time_flag, current_position);


                                    Fragment_Routes_Time current_fragment = (Fragment_Routes_Time) fm.findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + current_position);
                                    if (current_fragment != null) {
                                        current_fragment.set_list_item();
                                    }


                                } else {
                                    time_flag = 0;

                                    clickOnSameTab(time_flag, current_position);
                                    Fragment_Routes_Time current_fragment = (Fragment_Routes_Time) fm.findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + current_position);
                                    if (current_fragment != null) {
                                        current_fragment.set_list_item();
                                    }
                                }

                        }
                        else if(current_position ==2)
                        {


                                if (duration_flag == 0) {
                                    duration_flag = 1;

                                    clickOnSameTab(duration_flag, current_position);
                                    Fragment_Routes_Duration current_fragment = (Fragment_Routes_Duration) fm.findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + current_position);
                                    if (current_fragment != null) {
                                        current_fragment.set_list_item();
                                    }

                                } else {
                                    duration_flag = 0;

                                    clickOnSameTab(duration_flag, current_position);
                                    Fragment_Routes_Duration current_fragment = (Fragment_Routes_Duration) fm.findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + current_position);
                                    if (current_fragment != null) {
                                        current_fragment.set_list_item();
                                    }
                                }

                        }
                        else if(current_position == 3)
                        {


                                if (price_flag == 0) {
                                    price_flag = 1;

                                    clickOnSameTab(price_flag, current_position);
                                    Fragment_Routes_Price current_fragment = (Fragment_Routes_Price) fm.findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + current_position);
                                    if (current_fragment != null) {
                                        current_fragment.set_list_item();
                                    }

                                } else {
                                    price_flag = 0;

                                    clickOnSameTab(price_flag, current_position);
                                    Fragment_Routes_Price current_fragment = (Fragment_Routes_Price) fm.findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + current_position);
                                    if (current_fragment != null) {
                                        current_fragment.set_list_item();
                                    }
                                }


                        }
                    }


                        viewPager.setCurrentItem(current_position);
               }
           });
        }

    }



    public void clickOnSameTab(int time_flag_temp, int current_position)
    {
        LinearLayout   mTabsLinearLayout = ((LinearLayout)tabsStrip.getChildAt(0));
        for(int i=0; i < mTabsLinearLayout.getChildCount(); i++) {

            TextView tv = (TextView) mTabsLinearLayout.getChildAt(i);

            if(i == current_position){
                if(i != 0)
                {
                    if(time_flag_temp == 0)
                    {
                        tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.sort_up_test, 0);
                        tv.setCompoundDrawablePadding(3);
                    }
                    else
                    {
                        tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.sort_down_test, 0);
                        tv.setCompoundDrawablePadding(3);
                    }

                }
                tv.setTextColor(ContextCompat.getColor(Activity_Bus_Routes.this, R.color.app_theme_color));
            } else {
                tv.setTextColor(Color.BLACK);
                tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }

        }


    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        overridePendingTransition(R.anim.anim_none, R.anim.anim_out);



    }



    @Override
    protected void onResume() {
        super.onResume();

        if(Activity_filter.Click_On_Apply)
        {
            Activity_filter.Click_On_Apply = false;
           int current_position_fg = viewPager.getCurrentItem();
            FragmentManager fm = getSupportFragmentManager();
            if(current_position_fg == 0)
            {


                Fragment_Routes_Populer fg_current = (Fragment_Routes_Populer) fm.findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + current_position_fg);

                Fragment_Routes_Time fg_next = (Fragment_Routes_Time)fm.findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + (current_position_fg+1));
                fg_current.showloader();
                fg_next.showloader();
                fg_current.set_list_item();
               //fg_next.set_list_item();

            }
            else if(current_position_fg == 1)
            {
                Fragment_Routes_Populer fg_last = (Fragment_Routes_Populer) fm.findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + (current_position_fg-1));
                Fragment_Routes_Time fg_current = (Fragment_Routes_Time) fm.findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + current_position_fg);

                Fragment_Routes_Duration fg_next = (Fragment_Routes_Duration)fm.findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + (current_position_fg+1));
                fg_current.showloader();
                //fg_last.set_list_item();
                fg_last.showloader();
                fg_next.showloader();
                fg_current.set_list_item();
               // fg_next.set_list_item();
            }
            else if(current_position_fg == 2)
            {
                Fragment_Routes_Time fg_last = (Fragment_Routes_Time) fm.findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + (current_position_fg-1));
                Fragment_Routes_Duration fg_current = (Fragment_Routes_Duration) fm.findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + current_position_fg);

                Fragment_Routes_Price fg_next = (Fragment_Routes_Price)fm.findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + (current_position_fg+1));
                fg_current.showloader();
                fg_last.showloader();
                fg_next.showloader();
               // fg_last.set_list_item();
                fg_current.set_list_item();
              //  fg_next.set_list_item();
            }
            else if(current_position_fg == 3)
            {
                Fragment_Routes_Duration fg_last = (Fragment_Routes_Duration) fm.findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + (current_position_fg-1));
                Fragment_Routes_Price fg_current = (Fragment_Routes_Price) fm.findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + current_position_fg);
                fg_current.showloader();
                fg_last.showloader();
               // fg_last.set_list_item();
                fg_current.set_list_item();

            }

        }
        else
        {
            initializefilterdata();
            My_realm.beginTransaction();
            RealmResults<Filter> all_row = My_realm.where(Filter.class).findAll();
            My_realm.commitTransaction();
            for(int i=0;i< all_row.size();i++)
            {
                if(all_row.get(i).getFilter_name().equals("A/C"))
                {
                    Bustype_ac_flag =true;
                }
                if(all_row.get(i).getFilter_name().equals("Sleeper"))
                {
                    Bustype_sleeper_flag =true;
                }
                if(all_row.get(i).getFilter_name().equals("Multi Axle"))
                {
                    Bustype_MultiAxle_flag =true;
                }
                if(all_row.get(i).getFilter_name().equals("Volvo"))
                {
                    Busbrand_Volvo_flag = true;
                }
                if(all_row.get(i).getFilter_name().equals("Mercesdes"))
                {
                    Busbrand_Mercesdes_flag = true;
                }
                if(all_row.get(i).getFilter_name().equals("Scanio"))
                {
                    Busbrand_Scania_flag = true;
                }
                if(all_row.get(i).getFilter_name().contains("Price"))
                {
                    String value = all_row.get(i).getFilter_value();
                    int middle = value.indexOf("-");
                    int minvalue = Integer.parseInt(value.substring(0,middle));
                    int maxvalue = Integer.parseInt(value.substring((middle+1),value.length()));
                    price_range_flag = true;

                    filter_max_price = maxvalue;
                    filter_min_price  = minvalue;

                }
            }
        }


    }

}
