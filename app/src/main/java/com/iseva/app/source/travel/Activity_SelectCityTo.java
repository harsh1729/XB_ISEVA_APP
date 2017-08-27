package com.iseva.app.source.travel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.iseva.app.source.R;
import com.iseva.app.source.Realm_objets.Realm_City;
import com.iseva.app.source.travel.Constants.JSON_KEYS;
import com.iseva.app.source.travel.Global_Travel.TRAVEL_DATA;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmResults;

public class Activity_SelectCityTo extends Activity{

    private AutoCompleteTextView Real_Get_To_City;
    private TextView header_tv;
    private ImageView header_iv;
    private int  text_count;
    private TextView city_category;
    public ArrayList<HashMap<String, String>> All_Cities_Map;
    public  ArrayList<HashMap<String,String>> Main_Cities;

    private int current_city_state = Constants.STATE_MAIN_CITIES;

    Realm My_realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__select_city_to);

        Intent i = getIntent();

        My_realm = Realm.getDefaultInstance();

        My_realm.beginTransaction();
        RealmResults<Realm_City> cities = My_realm.where(Realm_City.class).findAll();
        My_realm.commitTransaction();

        All_Cities_Map = new ArrayList<HashMap<String, String>>();

        for (Realm_City city:
                cities) {

            HashMap<String, String> map = new HashMap<String, String>();
            map.put(JSON_KEYS.CITY_NAME, city.getCityName());
            map.put(JSON_KEYS.CITY_ID, city.getCityId());

            All_Cities_Map.add(map);
        }


        Main_Cities = (ArrayList<HashMap<String, String>>)i.getSerializableExtra("main_cities");

        city_category = (TextView)findViewById(R.id.to_city_category);
        Real_Get_To_City = (AutoCompleteTextView)findViewById(R.id.Real_Get_To_City);
        header_tv = (TextView)findViewById(R.id.header_text);
        header_tv.setText(R.string.header_text_city_to);
        header_iv = (ImageView)findViewById(R.id.header_back_button);

        add_adapter();


        Real_Get_To_City.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.i("HARSH","Inside Text Change");

                if(Real_Get_To_City.getText().length() == 0)
                {

                    if (current_city_state != Constants.STATE_MAIN_CITIES){

                        current_city_state = Constants.STATE_MAIN_CITIES;
                        add_adapter();
                    }

                    city_category.setText("Popular Cities");
                }
                else if(!(s.toString().contains(JSON_KEYS.CITY_ID) && s.toString().contains(JSON_KEYS.CITY_NAME)))
                {
                    if (current_city_state != Constants.STATE_ALL_CITIES){

                        current_city_state = Constants.STATE_ALL_CITIES;
                        add_adapter();
                    }



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


    public void add_adapter()
    {
        //String[] from = new String[] {JSON_KEYS.CITY_NAME};
        //int[] to = new int[] {R.id.text1};



        //SimpleAdapter adapter1 = null;

        FilterWithSpaceAdapter adapter1 = null;//<HashMap<String, String>>

        if(current_city_state == Constants.STATE_MAIN_CITIES)
        {
            //adapter1 = new SimpleAdapter(Activity_SelectCityTo.this,Main_Cities,R.layout.show_city_single_row,from,to);

            adapter1 = new FilterWithSpaceAdapter(Activity_SelectCityTo.this,
                    R.layout.show_city_single_row,R.id.text1, Main_Cities);
        }
        else
        {
            adapter1 = new FilterWithSpaceAdapter(Activity_SelectCityTo.this,
                    R.layout.show_city_single_row,R.id.text1, All_Cities_Map);

            //adapter1 = new SimpleAdapter(Activity_SelectCityTo.this,All_Cities_Map,R.layout.show_city_single_row,from,to);
        }


        Real_Get_To_City.setAdapter(adapter1);
        Real_Get_To_City.setThreshold(1);
        adapter1.notifyDataSetChanged();
        Real_Get_To_City.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String, String> hm = (HashMap<String, String>) adapterView.getAdapter().getItem(i);
                EditText et = (EditText)findViewById(R.id.Real_Get_To_City);
                et.setText(hm.get(""));
                TRAVEL_DATA.TO_CITY_ID = hm.get(JSON_KEYS.CITY_ID);
                TRAVEL_DATA.TO_CITY_NAME = hm.get(JSON_KEYS.CITY_NAME);
                activity_dismiss();
            }
        });

        if(current_city_state == Constants.STATE_ALL_CITIES){

            Real_Get_To_City.performValidation();
            Real_Get_To_City.showDropDown();
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


    @Override
    protected void onPause() {
        super.onPause();
        activity_dismiss();
    }


}
