package com.iseva.app.source.travel;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iseva.app.source.R;
import com.iseva.app.source.Realm_objets.Bus_routes_detail;
import com.iseva.app.source.Realm_objets.Filter;
import com.iseva.app.source.Realm_objets.Selected_Seats;


import org.florescu.android.rangeseekbar.RangeSeekBar;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.iseva.app.source.travel.Activity_Bus_Routes.Busbrand_Mercesdes_flag;
import static com.iseva.app.source.travel.Activity_Bus_Routes.Busbrand_Scania_flag;
import static com.iseva.app.source.travel.Activity_Bus_Routes.Busbrand_Volvo_flag;
import static com.iseva.app.source.travel.Activity_Bus_Routes.Bustype_MultiAxle_flag;
import static com.iseva.app.source.travel.Activity_Bus_Routes.Bustype_ac_flag;
import static com.iseva.app.source.travel.Activity_Bus_Routes.Bustype_sleeper_flag;
import static com.iseva.app.source.travel.Activity_Bus_Routes.filter_max_price;
import static com.iseva.app.source.travel.Activity_Bus_Routes.filter_min_price;
import static com.iseva.app.source.travel.Activity_Bus_Routes.price_range_flag;


public class Activity_filter extends Activity{

    Realm My_realm;

    LinearLayout header_text_layout;
    private TextView header_tv;
    private ImageView header_iv;
    private TextView header_tv_second;

    TextView minvalue_tv;
    TextView maxvalue_tv;

    TextView Bustype_Ac;
    TextView Bustype_Sleeper;
    TextView Bustype_MultiAxle;
    TextView Busbrand_Volvo;
    TextView Busbrand_Mercesdes;
    TextView Busbrand_Scania;

    Button filter_apply_btn;

    LinearLayout all_filter_name ;
    RangeSeekBar seekBar;

    public static Boolean Click_On_Apply =false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        My_realm = Realm.getInstance(getApplicationContext());


        header_iv = (ImageView)findViewById(R.id.header_back_button);
        header_tv = (TextView)findViewById(R.id.header_text);
        header_tv_second = (TextView)findViewById(R.id.header_second_text);
        header_text_layout = (LinearLayout)findViewById(R.id.layout_header_text);
        header_text_layout.setGravity(Gravity.CENTER_VERTICAL);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        lp.setMargins(0,0,10,0);
        header_tv_second.setLayoutParams(lp);

        header_tv_second.setText("Reset All");
        header_tv_second.setVisibility(View.VISIBLE);
        header_tv_second.setBackgroundResource(R.drawable.bus_routes_reset_btn_click);
        header_tv_second.setPadding(5,3,5,3);

        header_tv.setText("Filter Results");
        header_tv.setPadding(10,0,0,0);
        filter_apply_btn = (Button)findViewById(R.id.filter_apply_button);

        Click_On_Apply = false;

        minvalue_tv = (TextView)findViewById(R.id.min_value);
        maxvalue_tv = (TextView)findViewById(R.id.max_value);

        Bustype_Ac = (TextView)findViewById(R.id.Bustype_Ac);
        Bustype_Sleeper = (TextView)findViewById(R.id.Bustype_Sleeper);
        Bustype_MultiAxle = (TextView)findViewById(R.id.Bustype_MultiAxle);

        Busbrand_Volvo = (TextView)findViewById(R.id.Busbrand_Volvo);
        Busbrand_Mercesdes = (TextView)findViewById(R.id.Busbrand_Mercesdes);
        Busbrand_Scania = (TextView)findViewById(R.id.Busbrand_Scania);

        all_filter_name = (LinearLayout)findViewById(R.id.all_filters_name);

        seekBar = (RangeSeekBar) findViewById(R.id.seekbar1);
        if(My_realm.where(Bus_routes_detail.class).findAll().size() != 0)
        {

            Activity_Bus_Routes.filter_max_price_fixed = My_realm.where(Bus_routes_detail.class).max("Fare").intValue() ;
        }



        seekBar.setRangeValues(Activity_Bus_Routes.filter_min_price_fixed,Activity_Bus_Routes.filter_max_price_fixed);
        seekBar.setSelectedMaxValue(Activity_Bus_Routes.filter_max_price_fixed);
        seekBar.setSelectedMinValue(Activity_Bus_Routes.filter_min_price_fixed);

        minvalue_tv.setText("\u20B9"+" "+Activity_Bus_Routes.filter_min_price_fixed);
        maxvalue_tv.setText("\u20B9"+" "+Activity_Bus_Routes.filter_max_price_fixed);

        seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>(){

            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                bar.setNotifyWhileDragging(true);
                filter_max_price = maxValue;
                filter_min_price = minValue;
                if(!price_range_flag)
                {
                    price_range_flag = true;
                    showfiltername();
                }
                else
                {


                    View v = (View)findViewById(R.id.all_filters_name);
                    TextView t = (TextView)v.findViewWithTag(Integer.toString(bar.getId()));
                    t.setText("Price:"+minValue+"-"+maxValue);
                }
                minvalue_tv.setText("\u20B9"+" "+minValue);
                maxvalue_tv.setText("\u20B9"+" "+maxValue);
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
                resetall();

            }
        });

        filter_apply_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Click_On_Apply = true;
                My_realm.beginTransaction();
                My_realm.clear(Filter.class);
                My_realm.commitTransaction();

                if(Bustype_ac_flag)
                {
                    My_realm.beginTransaction();
                    Filter filter = My_realm.createObject(Filter.class);
                    filter.setId(Integer.toString(Bustype_Ac.getId()));
                    filter.setFilter_name("A/C");
                    filter.setFilter_tag("Bustype");
                    filter.setFilter_value(Boolean.toString(Bustype_ac_flag));
                    My_realm.commitTransaction();
                }

                if(Bustype_sleeper_flag)
                {
                    My_realm.beginTransaction();
                    Filter filter = My_realm.createObject(Filter.class);
                    filter.setId(Integer.toString(Bustype_Sleeper.getId()));
                    filter.setFilter_name("Sleeper");
                    filter.setFilter_tag("Bustype");
                    filter.setFilter_value(Boolean.toString(Bustype_sleeper_flag));
                    My_realm.commitTransaction();
                }

                if(Bustype_MultiAxle_flag)
                {
                    My_realm.beginTransaction();
                    Filter filter = My_realm.createObject(Filter.class);
                    filter.setId(Integer.toString(Bustype_MultiAxle.getId()));
                    filter.setFilter_name("Multi Axle");
                    filter.setFilter_tag("Bustype");
                    filter.setFilter_value(Boolean.toString(Bustype_MultiAxle_flag));
                    My_realm.commitTransaction();
                }

                if(Busbrand_Volvo_flag)
                {
                    My_realm.beginTransaction();
                    Filter filter = My_realm.createObject(Filter.class);
                    filter.setId(Integer.toString(Busbrand_Volvo.getId()));
                    filter.setFilter_name("Volvo");
                    filter.setFilter_tag("Busbrand");
                    filter.setFilter_value(Boolean.toString(Busbrand_Volvo_flag));
                    My_realm.commitTransaction();
                }
                if(Busbrand_Mercesdes_flag)
                {
                    My_realm.beginTransaction();
                    Filter filter = My_realm.createObject(Filter.class);
                    filter.setId(Integer.toString(Busbrand_Mercesdes.getId()));
                    filter.setFilter_name("Mercesdes");
                    filter.setFilter_tag("Busbrand");
                    filter.setFilter_value(Boolean.toString(Busbrand_Mercesdes_flag));
                    My_realm.commitTransaction();
                }
                if(Busbrand_Scania_flag)
                {
                    My_realm.beginTransaction();
                    Filter filter = My_realm.createObject(Filter.class);
                    filter.setId(Integer.toString(Busbrand_Scania.getId()));
                    filter.setFilter_name("Scania");
                    filter.setFilter_tag("Busbrand");
                    filter.setFilter_value(Boolean.toString(Busbrand_Scania_flag));
                    My_realm.commitTransaction();
                }
                if(price_range_flag)
                {
                    My_realm.beginTransaction();
                    Filter filter = My_realm.createObject(Filter.class);
                    filter.setId(Integer.toString(seekBar.getId()));
                    filter.setFilter_name("Price:"+filter_min_price+"-"+filter_max_price);
                    filter.setFilter_tag("price");
                    filter.setFilter_value(filter_min_price+"-"+filter_max_price);
                    My_realm.commitTransaction();
                }

                activity_dismiss();

            }
        });

        showfiltername();
        setdynamiclayout();

        setclicklistner();

    }



    public void resetall()
    {
        My_realm.beginTransaction();
        My_realm.clear(Filter.class);
        My_realm.commitTransaction();

        Bustype_ac_flag = false;
        Bustype_MultiAxle_flag = false;
        Bustype_sleeper_flag = false;
        Busbrand_Mercesdes_flag = false;
        Busbrand_Volvo_flag = false;
        Busbrand_Scania_flag = false;

        filter_min_price =0 ;
        filter_max_price = 1000;

        price_range_flag = false;
        showfiltername();
        setdynamiclayout();

    }


    public void setclicklistner()
    {
        Bustype_Ac.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                if(Bustype_ac_flag)
                {
                    Bustype_ac_flag = false;
                    Bustype_Ac.setTextColor(ContextCompat.getColor(Activity_filter.this, R.color.filter_screen_button_border));
                    Bustype_Ac.setBackgroundResource(R.drawable.filter_layout_button);
                    showfiltername();
                }
                else
                {

                    Bustype_ac_flag =true;
                    Bustype_Ac.setTextColor(ContextCompat.getColor(Activity_filter.this, R.color.app_theme_color));
                    Bustype_Ac.setBackgroundResource(R.drawable.filter_layout_button_focused);

                    showfiltername();

                }
            }
        });

        Bustype_Sleeper.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(!Bustype_sleeper_flag)
                {
                    Bustype_sleeper_flag = true;
                    Bustype_Sleeper.setTextColor(ContextCompat.getColor(Activity_filter.this, R.color.app_theme_color));
                    Bustype_Sleeper.setBackgroundResource(R.drawable.filter_layout_button_focused);

                    showfiltername();
                }
                else
                {

                    Bustype_sleeper_flag = false;
                    Bustype_Sleeper.setTextColor(ContextCompat.getColor(Activity_filter.this, R.color.filter_screen_text));
                    Bustype_Sleeper.setBackgroundResource(R.drawable.filter_layout_button);
                    showfiltername();
                }
            }
        });

        Bustype_MultiAxle.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(!Bustype_MultiAxle_flag)
                {
                    Bustype_MultiAxle_flag = true;
                    Bustype_MultiAxle.setTextColor(ContextCompat.getColor(Activity_filter.this, R.color.app_theme_color));
                    Bustype_MultiAxle.setBackgroundResource(R.drawable.filter_layout_button_focused);
                    showfiltername();
                }
                else
                {
                    Bustype_MultiAxle_flag = false;
                    Bustype_MultiAxle.setTextColor(ContextCompat.getColor(Activity_filter.this, R.color.filter_screen_text));
                    Bustype_MultiAxle.setBackgroundResource(R.drawable.filter_layout_button);

                    showfiltername();
                }
            }
        });

        Busbrand_Volvo.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(!Busbrand_Volvo_flag)
                {
                    Busbrand_Volvo_flag = true;
                    Busbrand_Volvo.setTextColor(ContextCompat.getColor(Activity_filter.this, R.color.app_theme_color));
                    Busbrand_Volvo.setBackgroundResource(R.drawable.filter_layout_button_focused);
                    showfiltername();

                }
                else
                {
                    Busbrand_Volvo_flag = false;
                    Busbrand_Volvo.setTextColor(ContextCompat.getColor(Activity_filter.this, R.color.filter_screen_text));
                    Busbrand_Volvo.setBackgroundResource(R.drawable.filter_layout_button);
                    showfiltername();
                }
            }
        });

        Busbrand_Mercesdes.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(!Busbrand_Mercesdes_flag)
                {
                    Busbrand_Mercesdes_flag = true;
                    Busbrand_Mercesdes.setTextColor(ContextCompat.getColor(Activity_filter.this, R.color.app_theme_color));
                    Busbrand_Mercesdes.setBackgroundResource(R.drawable.filter_layout_button_focused);
                    showfiltername();
                }
                else
                {
                    Busbrand_Mercesdes_flag = false;
                    Busbrand_Mercesdes.setTextColor(ContextCompat.getColor(Activity_filter.this, R.color.filter_screen_text));
                    Busbrand_Mercesdes.setBackgroundResource(R.drawable.filter_layout_button);
                    showfiltername();
                }
            }
        });
        Busbrand_Scania.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(!Busbrand_Scania_flag)
                {
                    Busbrand_Scania_flag = true;
                    Busbrand_Scania.setTextColor(ContextCompat.getColor(Activity_filter.this, R.color.app_theme_color));
                    Busbrand_Scania.setBackgroundResource(R.drawable.filter_layout_button_focused);
                    showfiltername();
                }
                else
                {
                    Busbrand_Scania_flag = false;
                    Busbrand_Scania.setTextColor(ContextCompat.getColor(Activity_filter.this, R.color.filter_screen_text));
                    Busbrand_Scania.setBackgroundResource(R.drawable.filter_layout_button);
                    showfiltername();
                }
            }
        });
    }

    public void showfiltername()
    {

        all_filter_name.removeAllViews();
        if(!Busbrand_Scania_flag && !Bustype_sleeper_flag && !Busbrand_Mercesdes_flag && !Busbrand_Volvo_flag
                && !Bustype_MultiAxle_flag && !Bustype_ac_flag && !price_range_flag)
        {
            TextView textView = new TextView(Activity_filter.this);
            LinearLayout.LayoutParams lv = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(lv);
            textView.setText(R.string.no_filter_selectet);
            textView.setTextColor(ContextCompat.getColor(Activity_filter.this,R.color.filter_screen_text));
            textView.setPadding(0,20,0,20);
            all_filter_name.addView(textView);
        }
        else
        {
            if(Bustype_ac_flag)
            {
                addTextviewTofilter_all_name("A/C",Integer.toString(Bustype_Ac.getId()));
            }
            if(Bustype_sleeper_flag)
            {
                addTextviewTofilter_all_name("Sleeper",Integer.toString(Bustype_Sleeper.getId()));
            }
            if(Bustype_MultiAxle_flag)
            {
                addTextviewTofilter_all_name("Multi Axle", Integer.toString(Bustype_MultiAxle.getId()));
            }
            if(Busbrand_Volvo_flag)
            {
                addTextviewTofilter_all_name("Volvo",Integer.toString(Busbrand_Volvo.getId()));
            }
            if(Busbrand_Mercesdes_flag)
            {
                addTextviewTofilter_all_name("Mercesdes",Integer.toString(Busbrand_Mercesdes.getId()));
            }
            if(Busbrand_Scania_flag)
            {
                addTextviewTofilter_all_name("Scania",Integer.toString(Busbrand_Scania.getId()));
            }
            if(price_range_flag)
            {
                addTextviewTofilter_all_name("Price:"+filter_min_price+"-"+filter_max_price,Integer.toString(seekBar.getId()));
            }
        }
    }


    public void addTextviewTofilter_all_name(String filter_name,String filter_id)
    {
        TextView textView = new TextView(Activity_filter.this);
        LinearLayout.LayoutParams lv = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lv.setMargins(0,20,10,20);
        textView.setLayoutParams(lv);
        textView.setText(filter_name);
        textView.setTextColor(ContextCompat.getColor(Activity_filter.this,R.color.filter_screen_text));
        textView.setBackgroundResource(R.drawable.filter_layout_filter_tag_bg);
        textView.setPadding(10,5,15,5);

        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.filter_close_marketplace, 0);
        textView.setCompoundDrawablePadding(10);
        textView.setTag(filter_id);
        textView.setTag(R.string.tag_id,filter_id);
        textView.setTag(R.string.tag_name,filter_name);
        textView.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                switch (motionEvent.getAction())
                {
                    case MotionEvent.ACTION_UP:
                        int view_width = view.getWidth();

                        int touchable_width = (view_width*70)/100;

                        int touchpoint = (int)motionEvent.getX();
                        if(touchpoint > touchable_width) {

                            if (view.getTag(R.string.tag_name).toString().equals("A/C")) {
                                Bustype_ac_flag = false;
                                setdynamiclayout();
                            } else if (view.getTag(R.string.tag_name).toString().equals("Sleeper")) {
                                Bustype_sleeper_flag = false;
                                setdynamiclayout();
                            } else if (view.getTag(R.string.tag_name).toString().equals("Multi Axle")) {
                                Bustype_MultiAxle_flag = false;
                                setdynamiclayout();
                            } else if (view.getTag(R.string.tag_name).toString().equals("Volvo")) {
                                Busbrand_Volvo_flag = false;
                                setdynamiclayout();
                            } else if (view.getTag(R.string.tag_name).toString().equals("Mercesdes")) {
                                Busbrand_Mercesdes_flag = false;
                                setdynamiclayout();
                            } else if (view.getTag(R.string.tag_name).toString().equals("Scania")) {
                                Busbrand_Scania_flag = false;
                                setdynamiclayout();
                            } else if (view.getTag(R.string.tag_name).toString().contains("Price")) {
                                Toast.makeText(Activity_filter.this, "price calling", Toast.LENGTH_LONG).show();
                                price_range_flag = false;
                                setdynamiclayout();

                            }

                            showfiltername();
                            break;
                        }
                }




                return true;
            }
        });


        all_filter_name.addView(textView);
    }

    public void setdynamiclayout()
    {



        if(Bustype_ac_flag)
        {
            Bustype_Ac.setTextColor(ContextCompat.getColor(Activity_filter.this, R.color.app_theme_color));
            Bustype_Ac.setBackgroundResource(R.drawable.filter_layout_button_focused);
        }
        else
        {
            Bustype_Ac.setTextColor(ContextCompat.getColor(Activity_filter.this, R.color.filter_screen_text));
            Bustype_Ac.setBackgroundResource(R.drawable.filter_layout_button);
        }

        if(Bustype_sleeper_flag)
        {
            Bustype_Sleeper.setTextColor(ContextCompat.getColor(Activity_filter.this, R.color.app_theme_color));
            Bustype_Sleeper.setBackgroundResource(R.drawable.filter_layout_button_focused);
        }
        else
        {
            Bustype_Sleeper.setTextColor(ContextCompat.getColor(Activity_filter.this, R.color.filter_screen_text));
            Bustype_Sleeper.setBackgroundResource(R.drawable.filter_layout_button);
        }

        if(Bustype_MultiAxle_flag)
        {
            Bustype_MultiAxle.setTextColor(ContextCompat.getColor(Activity_filter.this, R.color.app_theme_color));
            Bustype_MultiAxle.setBackgroundResource(R.drawable.filter_layout_button_focused);
        }
        else
        {
            Bustype_MultiAxle.setTextColor(ContextCompat.getColor(Activity_filter.this, R.color.filter_screen_text));
            Bustype_MultiAxle.setBackgroundResource(R.drawable.filter_layout_button);
        }


        if(Busbrand_Volvo_flag)
        {
            Busbrand_Volvo.setTextColor(ContextCompat.getColor(Activity_filter.this, R.color.app_theme_color));
            Busbrand_Volvo.setBackgroundResource(R.drawable.filter_layout_button_focused);
        }
        else
        {
            Busbrand_Volvo.setTextColor(ContextCompat.getColor(Activity_filter.this, R.color.filter_screen_text));
            Busbrand_Volvo.setBackgroundResource(R.drawable.filter_layout_button);
        }


        if(Busbrand_Mercesdes_flag)
        {
            Busbrand_Mercesdes.setTextColor(ContextCompat.getColor(Activity_filter.this, R.color.app_theme_color));
            Busbrand_Mercesdes.setBackgroundResource(R.drawable.filter_layout_button_focused);
        }
        else
        {
            Busbrand_Mercesdes.setTextColor(ContextCompat.getColor(Activity_filter.this, R.color.filter_screen_text));
            Busbrand_Mercesdes.setBackgroundResource(R.drawable.filter_layout_button);
        }

        if(Busbrand_Scania_flag)
        {
            Busbrand_Scania.setTextColor(ContextCompat.getColor(Activity_filter.this, R.color.app_theme_color));
            Busbrand_Scania.setBackgroundResource(R.drawable.filter_layout_button_focused);
        }
        else
        {
            Busbrand_Scania.setTextColor(ContextCompat.getColor(Activity_filter.this, R.color.filter_screen_text));
            Busbrand_Scania.setBackgroundResource(R.drawable.filter_layout_button);
        }

        if(price_range_flag)
        {

            seekBar.setSelectedMinValue(filter_min_price);
            seekBar.setSelectedMaxValue(filter_max_price);
            minvalue_tv.setText("\u20B9"+" "+ filter_min_price);
            maxvalue_tv.setText("\u20B9"+" "+ filter_max_price);

        }
        else
        {

            seekBar.setSelectedMinValue(Activity_Bus_Routes.filter_min_price_fixed);
            seekBar.setSelectedMaxValue(Activity_Bus_Routes.filter_max_price_fixed);
            minvalue_tv.setText("\u20B9"+" "+Activity_Bus_Routes.filter_min_price_fixed);
            maxvalue_tv.setText("\u20B9"+" "+Activity_Bus_Routes.filter_max_price_fixed);

        }

    }


    public void activity_dismiss()
    {
        this.finish();
        overridePendingTransition(R.anim.anim_none, R.anim.anim_out);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        overridePendingTransition(R.anim.anim_none, R.anim.anim_out);



    }

}
