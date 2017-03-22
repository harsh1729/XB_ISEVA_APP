package com.iseva.app.source.travel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.iseva.app.source.R;

import java.util.ArrayList;
import java.util.HashMap;

public class Activity_SelectCityTo extends Activity{

    private AutoCompleteTextView Real_Get_To_City;
    private TextView header_tv;
    private ImageView header_iv;
    private int  text_count;
    private TextView city_category;
    public ArrayList<HashMap<String, String>> All_Cities_Map;
    public  ArrayList<HashMap<String,String>> Main_Cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__select_city_to);

        Intent i = getIntent();

        All_Cities_Map = (ArrayList<HashMap<String, String>>)i.getSerializableExtra("all_cities");
        Main_Cities = (ArrayList<HashMap<String, String>>)i.getSerializableExtra("main_cities");

        city_category = (TextView)findViewById(R.id.to_city_category);
        Real_Get_To_City = (AutoCompleteTextView)findViewById(R.id.Real_Get_To_City);
        header_tv = (TextView)findViewById(R.id.header_text);
        header_tv.setText(R.string.header_text_city_to);
        header_iv = (ImageView)findViewById(R.id.header_back_button);

        add_adapter(0);


        Real_Get_To_City.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(Real_Get_To_City.getText().length() == 0)
                {

                    add_adapter(0);
                    city_category.setText("Populur Cities");
                }
                else if(Real_Get_To_City.getText().length() == 1)
                {
                    add_adapter(1);
                    city_category.setText("All Cities");
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        Real_Get_To_City.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                if(Real_Get_To_City.isPopupShowing())
                {
                    return;
                }
                else
                {
                    Real_Get_To_City.showDropDown();
                }

            }
        });



        header_iv.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                activity_dismiss();
            }
        });

    }


    public void add_adapter(int type)
    {
        String[] from = new String[] {"cityname"};
        int[] to = new int[] {R.id.text1};

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {


                HashMap<String, String> hm = (HashMap<String, String>) arg0.getAdapter().getItem(position);
                AutoCompleteTextView et = (AutoCompleteTextView)findViewById(R.id.Real_Get_To_City);
                et.setText(hm.get(""));
                Search_Buses_Key.TO_City_id = hm.get("cityid");
                Search_Buses_Key.To_City_name = hm.get("cityname");
                activity_dismiss();
            }
        };

        SimpleAdapter adapter1 = null;

        if(type == 0)
        {
            adapter1 = new SimpleAdapter(Activity_SelectCityTo.this,Main_Cities,R.layout.show_city_single_row,from,to);
        }
        else
        {
            adapter1 = new SimpleAdapter(Activity_SelectCityTo.this,All_Cities_Map,R.layout.show_city_single_row,from,to);
        }

        adapter1.notifyDataSetChanged();
        Real_Get_To_City.setOnItemClickListener(itemClickListener);

        Real_Get_To_City.setAdapter(adapter1);
        Real_Get_To_City.setThreshold(0);
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


    @Override
    protected void onPause() {
        super.onPause();
        activity_dismiss();
    }


}
