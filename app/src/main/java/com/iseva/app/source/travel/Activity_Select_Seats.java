package com.iseva.app.source.travel;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.astuetz.PagerSlidingTabStrip;
import com.iseva.app.source.Adapter.PagerAdapter_Select_Seat;
import com.iseva.app.source.Custom_VolleyAppController;
import com.iseva.app.source.Custom_VolleyObjectRequest;
import com.iseva.app.source.R;
import com.iseva.app.source.Realm_objets.Pickup_Place_Detail;
import com.iseva.app.source.Realm_objets.Realm_Seat_Details;
import com.iseva.app.source.Realm_objets.Selected_Seats;
import com.iseva.app.source.travel.Constants.JSON_KEYS;
import com.iseva.app.source.travel.Constants.SEAT_DETAILS;
import com.iseva.app.source.travel.Constants.URL_TY;
import com.iseva.app.source.travel.Global_Travel.TRAVEL_DATA;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;


public class Activity_Select_Seats extends Activity_Parent_AppCompat implements  Parent_Interface {

    Realm My_realm;

    ImageView iv_header;

    TextView tv_header;
    TextView cancellation_tv;
    TextView Total_Fare_tv;
    TextView Total_Seats_tv;
    TextView cancel_cancellation_policy_layout;


    int bus_id;
    int count;

    double comm_pct = 0.0;

    int max_row_deck_1 = 0;
    int max_col_deck_1 = 0;

    int max_row_deck_2 = 0;
    int max_col_deck_2 = 0;

    String response;
    String message;

    LinearLayout loader_layout;

    ProgressBar progressBar;

    JSONArray cancellation_Policy_data;

    SoapObject soapresult_schedule_detail;

    Button select_seat_proceed_btn;

    PagerSlidingTabStrip tabsStrip;

    public static Activity selected_seat_activity;


    LinearLayout progress_layout;
    LinearLayout instruction_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__select__seats);
        selected_seat_activity = this;

        My_realm = Realm.getDefaultInstance();
        Intent i = getIntent();

        try {

            bus_id = Integer.parseInt(i.getStringExtra("bus_id"));
            comm_pct = Double.parseDouble(i.getStringExtra("comm_pct"));

        }catch (Exception ex){

            //Exc
        }


        Total_Fare_tv = (TextView) findViewById(R.id.Total_Fare);
        Total_Seats_tv = (TextView) findViewById(R.id.Selected_Seats);
        progress_layout = (LinearLayout) findViewById(R.id.select_seat_dynamic_data_layout);
        instruction_layout = (LinearLayout) findViewById(R.id.seat_instruction_layout);
        progressBar = (ProgressBar) findViewById(R.id.activity_seat_progress_bar);
        loader_layout = (LinearLayout) findViewById(R.id.select_seat_loader_layout);
        cancel_cancellation_policy_layout = (TextView) findViewById(R.id.cancel_cancellation_layout);

        iv_header = (ImageView) findViewById(R.id.header_back_button);
        tv_header = (TextView) findViewById(R.id.header_text);

        select_seat_proceed_btn = (Button) findViewById(R.id.select_seat_proceed_btn);
        cancellation_tv = (TextView) findViewById(R.id.cancellation_tv);

        String first = "By booking this ticket you agree to the ";
        String second = "<font color='#1BAFCD'>Cancellation Policy</font>";
        String third = " of the bus operator.";

        if (Build.VERSION.SDK_INT >= 24) {

            cancellation_tv.setText(Html.fromHtml(first + second + third, 0));
            // for 24 api and more
        } else {

            cancellation_tv.setText(Html.fromHtml(first + second + third));
        }

        tv_header.setText("Select Seats");

        setOnclicklister();


        if (isNetworkConnected(true)) {
            getChartSeatLayouts();
        }


        progress_layout.post(new Runnable() {
            public void run() {
                int seat_layout_height = progress_layout.getHeight();
                Log.e("vikas seatlaououth =", "" + seat_layout_height);
                if (seat_layout_height <= 180) {
                    instruction_layout.setVisibility(View.GONE);
                }
            }
        });

    }


    public void setOnclicklister() {
        cancellation_tv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                LinearLayout cl = (LinearLayout) findViewById(R.id.cancellation_layout);
                cl.setVisibility(View.VISIBLE);
            }
        });

        cancel_cancellation_policy_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                LinearLayout cl = (LinearLayout) findViewById(R.id.cancellation_layout);
                cl.setVisibility(View.GONE);
            }
        });
        iv_header.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                activity_dismiss();
            }
        });

        select_seat_proceed_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                My_realm.beginTransaction();
                RealmResults<Selected_Seats> All_row = My_realm.where(Selected_Seats.class).findAll();
                My_realm.commitTransaction();

                if (All_row.size() == 0) {
                    Global_Travel.showAlertDialog(Activity_Select_Seats.this, getResources().getString(R.string.validating_error_title), "Select atleast one seat !", "ok");
                } else {
                    Intent i = new Intent(Activity_Select_Seats.this, Activity_Passenger_Details.class);
                    i.putExtra("bus_id", "" + bus_id);
                    if (cancellation_Policy_data.length() > 0) {
                        i.putExtra("cancellation_data", cancellation_Policy_data.toString());
                    } else {
                        i.putExtra("cancellation_data", "");
                    }

                    startActivity(i);
                    overridePendingTransition(R.anim.anim_in, R.anim.anim_none);
                }


            }
        });

    }

    public void activity_dismiss() {
        this.finish();
        overridePendingTransition(R.anim.anim_none, R.anim.anim_out);
    }

    @Override
    public void onBackPressed() {
        LinearLayout cl = (LinearLayout) findViewById(R.id.cancellation_layout);
        if (cl.getVisibility() == View.VISIBLE) {
            cl.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.anim_none, R.anim.anim_out);
        }


    }


    public void retryServiceCall() {

//        if (isNetworkConnected(true)) {
//
//            getChartSeatLayouts();
//
//        }

        this.activity_dismiss();

    }

    public void update_seat_fare() {
        float Total_Fare = 0;
        String Total_Seat = "";

        My_realm.beginTransaction();
        RealmResults<Selected_Seats> All_row = My_realm.where(Selected_Seats.class).findAll();
        My_realm.commitTransaction();
        for (int i = 0; i < All_row.size(); i++) {
            Total_Fare = Total_Fare + All_row.get(i).getFare_after_offer();
            if (i == 0) {
                Total_Seat = Total_Seat + All_row.get(i).getSeatNo();
            } else {
                Total_Seat = Total_Seat + ", " + All_row.get(i).getSeatNo();
            }

        }


        Total_Fare_tv.setText("\u20B9 " + Total_Fare);
        Total_Seats_tv.setText(Total_Seat);

    }

    public void set_dynamic_data() {


        My_realm.beginTransaction();

        RealmResults<Realm_Seat_Details> deck2_seat = My_realm.where(Realm_Seat_Details.class).equalTo("Deck", 2).findAll();
        My_realm.commitTransaction();
        if (deck2_seat.size() == 0) {
            ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_select_seat);

            PagerAdapter_Select_Seat adap = new PagerAdapter_Select_Seat(getSupportFragmentManager(), 1,max_col_deck_1,max_col_deck_2);

            viewPager.setAdapter(adap);

            tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs_select_seat);

            tabsStrip.setViewPager(viewPager);

        } else {

            ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_select_seat);
            viewPager.setAdapter(new PagerAdapter_Select_Seat(getSupportFragmentManager(), 2,max_col_deck_1,max_col_deck_2));

            tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs_select_seat);

            tabsStrip.setViewPager(viewPager);
            set_fist_tab_color();
            tabsStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                // This method will be invoked when a new page becomes selected.
                @Override
                public void onPageSelected(int position) {
                    LinearLayout mTabsLinearLayout = ((LinearLayout) tabsStrip.getChildAt(0));
                    for (int i = 0; i < mTabsLinearLayout.getChildCount(); i++) {
                        TextView tv = (TextView) mTabsLinearLayout.getChildAt(i);

                        if (i == position) {
                            tv.setTextColor(ContextCompat.getColor(Activity_Select_Seats.this, R.color.tab_active_text_color));
                        } else {
                            tv.setTextColor(Color.GRAY);
                        }

                    }

                }


                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            tabsStrip.setVisibility(View.VISIBLE);
            if (Global_Travel.build_type == 0) {
                Log.e("vikas", "deck2 not 0");
            }




        }

        setCancellationTerms();

    }

    private void setCancellationTerms() {

        loader_layout.setVisibility(View.GONE);
        LinearLayout cancellation_text = (LinearLayout) findViewById(R.id.cancellation_text);
        try {
            for (int j = 0; j < cancellation_Policy_data.length(); j++) {


                LinearLayout divider = new LinearLayout(Activity_Select_Seats.this);
                LinearLayout single_layout = new LinearLayout(Activity_Select_Seats.this);
                single_layout.setOrientation(LinearLayout.HORIZONTAL);
                single_layout.setPadding(getResources().getDimensionPixelSize(R.dimen.padding_margin_15), getResources().getDimensionPixelSize(R.dimen.padding_margin_15), getResources().getDimensionPixelSize(R.dimen.padding_margin_15), getResources().getDimensionPixelSize(R.dimen.padding_margin_15));


                LinearLayout.LayoutParams divider_param = new LinearLayout.LayoutParams(
                        ActionBar.LayoutParams.MATCH_PARENT, 1
                );

                divider.setLayoutParams(divider_param);
                divider.setBackgroundColor(Color.GRAY);

                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        0,
                        ActionBar.LayoutParams.MATCH_PARENT, 6.0f);

                LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                        0,
                        ActionBar.LayoutParams.MATCH_PARENT, 4.0f);

                TextView tv1 = new TextView(Activity_Select_Seats.this);
                TextView tv2 = new TextView(Activity_Select_Seats.this);


                tv1.setLayoutParams(param);
                tv2.setLayoutParams(param1);

                if( j == cancellation_Policy_data.length() - 1){ //Last Element

                    tv1.setText("After " + cancellation_Policy_data.getJSONObject(j).getString("condition") + " Hrs. ");


                }else{

                    tv1.setText("Between " + cancellation_Policy_data.getJSONObject(j).getString("condition") + " Hrs. to " + cancellation_Policy_data.getJSONObject(j+1).getString("condition") + "Hrs.");

                }



                int percentage_int = Integer.parseInt(cancellation_Policy_data.getJSONObject(j).getString("percentage"));

                tv2.setText("" + (100 - percentage_int)+"%");

                single_layout.addView(tv1);
                single_layout.addView(tv2);
                cancellation_text.addView(divider);
                cancellation_text.addView(single_layout);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void set_fist_tab_color() {
        LinearLayout mTabsLinearLayout = ((LinearLayout) tabsStrip.getChildAt(0));
        for (int i = 0; i < mTabsLinearLayout.getChildCount(); i++) {
            TextView tv = (TextView) mTabsLinearLayout.getChildAt(i);

            if (i == 0) {
                tv.setTextColor(ContextCompat.getColor(Activity_Select_Seats.this, R.color.tab_active_text_color));
            } else {
                tv.setTextColor(Color.GRAY);
            }
        }
    }


    private void getChartSeatLayouts() {

        try {

            // HashMap<String, String> paramsMap = new HashMap<String, String>();


            String url = URL_TY.GET_CHART + "?fromCityId=" + TRAVEL_DATA.FROM_CITY_ID + "&toCityId=" + TRAVEL_DATA.TO_CITY_ID + "&journeyDate=" + TRAVEL_DATA.JOURNEY_DATE + "&busId=" + bus_id;


            //  Params has to be sent in url for GET req

            Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                    Method.GET, url,
                    null, new Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.i("HARSH", "json Response recieved !! -> " + response);
                    try {

                        if (response.getBoolean(JSON_KEYS.SUCCESS)) {

                            JSONObject objJsonData = response.getJSONObject(Constants.JSON_KEYS.DATA);

                            //SET CANCELATION POLICY
                            if (objJsonData.has("Canc")) {

                                JSONArray arrJSONCanc = objJsonData.getJSONArray("Canc");

                               try{

                                    cancellation_Policy_data = new JSONArray();

                                    for (int i = 0; i < arrJSONCanc.length(); i++) {

                                        JSONObject objCanc = arrJSONCanc.getJSONObject(i);

                                        int minsBeforeDep = objCanc.getInt("Mins");
                                        int hoursBeforeDep = minsBeforeDep / 60;


                                        int percentage = objCanc.getInt("Pct");

                                        JSONObject jb = new JSONObject();
                                        jb.put("mins", Integer.toString(minsBeforeDep));
                                        jb.put("condition", Integer.toString(hoursBeforeDep));
                                        jb.put("percentage", Integer.toString(percentage));
                                        cancellation_Policy_data.put(jb);


                                    }


                                } catch (Exception ex) {


                                }

                                Realm thread_realm = Realm.getDefaultInstance();

                                thread_realm.beginTransaction();
                                thread_realm.delete(Selected_Seats.class);
                                thread_realm.delete(Pickup_Place_Detail.class);
                                thread_realm.delete(Realm_Seat_Details.class);
                                thread_realm.commitTransaction();


                                try{

                                    //TODO : Avail Seats check and Drop, Pick
                                    
                                    JSONObject objJsonL1_ChartLayout =  objJsonData.getJSONObject("ChartLayout");
                                    JSONObject objJsonL1_ChartSeats =  objJsonData.getJSONObject("ChartSeats");
                                    JSONObject objJsonL1_SeatsStatus =  objJsonData.getJSONObject("SeatsStatus");
                                    JSONObject objJsonL1_AvailSeats =  objJsonData.getJSONObject("AvailSeats");
                                    //JSONArray objJsonL1_Dropoffs =  objJsonData.getJSONArray("Dropoffs"); //Not using dropoffs for now
                                    JSONArray objJsonL1_Pickups =  objJsonData.getJSONArray("Pickups");

                                    JSONObject objJsonL2_Layout =  objJsonL1_ChartLayout.getJSONObject("Layout");
                                    JSONObject objJsonL2_Info =  objJsonL1_ChartLayout.getJSONObject("Info");

                                    JSONArray arrJsonL2_SeatNos = objJsonL1_ChartSeats.getJSONArray("Seats");

                                    JSONArray arrJsonL2_Fares = objJsonL1_SeatsStatus.getJSONArray("Fares");

                                    JSONArray arrJsonL2_Status = objJsonL1_SeatsStatus.getJSONArray("Status");


                                    if(objJsonL2_Info.has("Lower")){

                                        JSONObject objJsonL3_InfoLower =  objJsonL2_Info.getJSONObject("Lower");

                                        max_col_deck_1 = objJsonL3_InfoLower.getInt("MaxCols");
                                        max_row_deck_1 = objJsonL3_InfoLower.getInt("MaxRows");
                                    }

                                    if(objJsonL2_Info.has("Upper")){

                                        JSONObject objJsonL3_InfoUpper =  objJsonL2_Info.getJSONObject("Upper");

                                        max_col_deck_2 = objJsonL3_InfoUpper.getInt("MaxCols");
                                        max_row_deck_2 = objJsonL3_InfoUpper.getInt("MaxRows");
                                    }





                                    thread_realm.beginTransaction();

                                    if (objJsonL2_Layout.has("Lower")) {

                                        JSONArray arrJsonL3_LowerDeckSeats = objJsonL2_Layout.getJSONArray("Lower");

                                        setSeatLayout(SEAT_DETAILS.VALUE_DECK_1, arrJsonL3_LowerDeckSeats, arrJsonL2_SeatNos, arrJsonL2_Status, arrJsonL2_Fares, thread_realm);


                                    }

                                    if (objJsonL2_Layout.has("Upper")) {

                                        JSONArray arrJsonL3_UpperDeckSeats = objJsonL2_Layout.getJSONArray("Upper");

                                        setSeatLayout(SEAT_DETAILS.VALUE_DECK_2, arrJsonL3_UpperDeckSeats, arrJsonL2_SeatNos, arrJsonL2_Status, arrJsonL2_Fares, thread_realm);


                                    }



                                    thread_realm.commitTransaction();

                                    //Add Pickup Points

                                    thread_realm.beginTransaction();

                                    for(int p = 0 ; p < objJsonL1_Pickups.length();p++){

                                        JSONObject objPickup = objJsonL1_Pickups.getJSONObject(p);

                                        Pickup_Place_Detail  pickup_place_detail  = thread_realm.createObject(Pickup_Place_Detail.class);

                                        pickup_place_detail.setPickupCode(objPickup.optString("PickupCode"));
                                        pickup_place_detail.setPickupArea(objPickup.optString("PickupArea"));
                                        pickup_place_detail.setPickupName(objPickup.optString("PickupName"));
                                        pickup_place_detail.setPkpTime(objPickup.optString("PickupTime"));
                                        pickup_place_detail.setAddress(objPickup.optString("Address"));
                                        pickup_place_detail.setLandmark(objPickup.optString("Landmark"));
                                        pickup_place_detail.setContact(objPickup.optString("Contact"));

                                    }

                                   thread_realm.commitTransaction();



                                }catch (Exception ex){

                                }


                            }



                            set_dynamic_data();

                        } else {

                            callAlertBox(getResources().getString(R.string.server_error_title), getResources().getString(R.string.server_error_message_try_again));
                        }



                    } catch (JSONException e) {

                        callAlertBox(getResources().getString(R.string.server_error_title), getResources().getString(R.string.server_error_message_try_again));
                        e.printStackTrace();

                    }
                }


            }, new ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError err) {
                    Log.i("SUSHIL", "ERROR VolleyError");

                    callAlertBox(getResources().getString(R.string.server_error_title), getResources().getString(R.string.server_error_message_try_again));
                }
            }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();

                    headers.put("access-token", TRAVEL_DATA.TOKEN_ID);

                    return headers;
                }

            };


            Custom_VolleyAppController.getInstance().addToRequestQueue(
                    jsonObjectRQST);
        } catch (Exception ex) {

            ex.printStackTrace();

        }
    }


    private void setSeatLayout(int deck, JSONArray arrJsonL3_DeckSeats , JSONArray arrJsonL2_SeatNos , JSONArray arrJsonL2_Status ,JSONArray arrJsonL2_Fares , Realm thread_realm ){

        for (int i = 0; i < arrJsonL3_DeckSeats.length() ; i++){

            try {
                JSONArray arrJsonL4_Seat = arrJsonL3_DeckSeats.getJSONArray(i);
                Realm_Seat_Details seat_details = thread_realm.createObject(Realm_Seat_Details.class);

                seat_details.setRow(arrJsonL4_Seat.getInt(SEAT_DETAILS.INDEX_SEAT_LAYOUT_ROW));
                seat_details.setCol(arrJsonL4_Seat.getInt(SEAT_DETAILS.INDEX_SEAT_LAYOUT_COL));
                seat_details.setHeight(arrJsonL4_Seat.getInt(SEAT_DETAILS.INDEX_SEAT_LAYOUT_HEIGHT));
                seat_details.setWidth(arrJsonL4_Seat.getInt(SEAT_DETAILS.INDEX_SEAT_LAYOUT_WIDTH));

                if (arrJsonL2_SeatNos.length() > arrJsonL4_Seat.getInt(SEAT_DETAILS.INDEX_SEAT_LAYOUT_SEQ_NO)) {

                    seat_details.setSeatNo(arrJsonL2_SeatNos.getString(arrJsonL4_Seat.getInt(SEAT_DETAILS.INDEX_SEAT_LAYOUT_SEQ_NO)));
                } else {

                    seat_details.setSeatNo(arrJsonL4_Seat.getInt(SEAT_DETAILS.INDEX_SEAT_LAYOUT_SEQ_NO) + "");
                }

                int seatType = arrJsonL4_Seat.getInt(SEAT_DETAILS.INDEX_SEAT_LAYOUT_SEAT_TYPE);

                seat_details.setSeatType(seatType);

                boolean isSleeper = false;
                switch (seatType) {

                    case SEAT_DETAILS.VALUE_SEAT_TYPE_SEATING:
                        isSleeper = false;
                        break;

                    case SEAT_DETAILS.VALUE_SEAT_TYPE_SEMI_SLEEPER:
                        isSleeper = false;
                        break;

                    case SEAT_DETAILS.VALUE_SEAT_TYPE_SLEEPER:
                        isSleeper = true;
                        break;

                }


                int gender = SEAT_DETAILS.VALUE_GENDER_ALL;
                boolean isAvl = false;
                int seatStatus = arrJsonL2_Status.getInt(arrJsonL4_Seat.getInt(SEAT_DETAILS.INDEX_SEAT_LAYOUT_SEQ_NO));

                switch (seatStatus) {

                    case SEAT_DETAILS.VALUE_SEAT_STATUS_AVL_FOR_ALL:

                        gender = SEAT_DETAILS.VALUE_GENDER_ALL;
                        isAvl = true;
                        break;

                    case SEAT_DETAILS.VALUE_SEAT_STATUS_AVL_FOR_FEMALE:

                        gender = SEAT_DETAILS.VALUE_GENDER_FEMALE;
                        isAvl = true;
                        break;

                    case SEAT_DETAILS.VALUE_SEAT_STATUS_AVL_FOR_MALE:

                        gender = SEAT_DETAILS.VALUE_GENDER_MALE;
                        isAvl = true;
                        break;

                    case SEAT_DETAILS.VALUE_SEAT_STATUS_NOT_AVL:

                        gender = SEAT_DETAILS.VALUE_GENDER_ALL;
                        isAvl = false;
                        break;

                    case SEAT_DETAILS.VALUE_SEAT_STATUS_BOOKED_BY_FEMALE:
                        gender = SEAT_DETAILS.VALUE_GENDER_FEMALE;
                        isAvl = false;

                        break;

                    case SEAT_DETAILS.VALUE_SEAT_STATUS_BOOKED_BY_MALE:

                        gender = SEAT_DETAILS.VALUE_GENDER_MALE;
                        isAvl = false;
                        break;

                }


                int total_fare = 0;

                JSONArray arrJsonFaresForSeat = arrJsonL2_Fares.getJSONArray(SEAT_DETAILS.INDEX_SEAT_LAYOUT_SEQ_NO);

                int totalFare = arrJsonFaresForSeat.getInt(SEAT_DETAILS.INDEX_FARE_TOTAL);

                double after_offer_seat_fare = Global_Travel.getFareAfterDiscount((double) totalFare, comm_pct);

                seat_details.setGender(gender);
                seat_details.setDeck(deck);

                seat_details.setIsSleeper(isSleeper);
                seat_details.setIsAvailable(isAvl);
                seat_details.setFare(totalFare);
                seat_details.setFare_after_offer(after_offer_seat_fare);

            }
            catch (Exception ex){

                //Exp
            }
        }

    }

}

//    private class RoutesDetail extends AsyncTask<Void,Void,Void>
//    {
//
//
//
//        @Override
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//
//            if(soapresult_schedule_detail != null)
//            {
//                if(((SoapObject)soapresult_schedule_detail.getProperty("Response")).getPrimitivePropertyAsString("IsSuccess").equals("true"))
//                {
//                  /*  LinearLayout cancellation_text = (LinearLayout) findViewById(R.id.cancellation_text);
//                    TextView tv = new TextView(Activity_Select_Seats.this);
//                    tv.setText(soapresult_schedule_detail.toString());
//                    cancellation_text.addView(tv);*/
//                    if(Global_Travel.build_type ==0)
//                    {
//                        Log.e("vikas",soapresult_schedule_detail.toString().trim());
////                        Log.e("vikas",Integer.toString(TRAVEL_DATA.MAX_COL));
//                    }
//
//                    set_dynamic_data();
//                }
//                else
//                {
//                    Global_Travel.showAlertDialog(Activity_Select_Seats.this,getResources().getString(R.string.validating_error_title),((SoapObject)soapresult_schedule_detail.getProperty("Response")).getPrimitivePropertyAsString("Message"),"Ok");
//                }
//            }
//            else
//            {
//                Global_Travel.showAlertDialog(Activity_Select_Seats.this,getResources().getString(R.string.validating_error_title),"Some error accured please try again !","Ok");
//            }
//
//
//
//
//        }

//
//        @Override
//        protected Void doInBackground(Void... voids) {
//
//            Realm thread_realm = Realm.getDefaultInstance();
//            SoapObject request = new SoapObject("","");
//
//            //TODO HArsh implement new API
//            //SoapObject request = new SoapObject(Constants.GLOBEL_NAMESPACE,Constants.METHOD_GetRouteScheduleDetail);
//
//            SoapObject sa = new SoapObject(null,"Authentication");



//            PropertyInfo userid = new PropertyInfo();
//            userid.setName("UserID");
//            userid.setValue(LoginCridantial.UserId.trim());
//            userid.setType(Integer.class);
//            sa.addProperty(userid);
//
//            PropertyInfo usertype = new PropertyInfo();
//            usertype.setName("UserType");
//            usertype.setValue(LoginCridantial.UserType.trim());
//            usertype.setType(String.class);
//            sa.addProperty(usertype);
//
//            PropertyInfo userkey = new PropertyInfo();
//            userkey.setName("Key");
//            userkey.setValue(LoginCridantial.UserKey.trim());
//            userkey.setType(String.class);
//            sa.addProperty(userkey);
//
//            request.addSoapObject(sa);
//
//            PropertyInfo scheduleid = new PropertyInfo();
//            scheduleid.setName("RouteScheduleId");
//            scheduleid.setValue(bus_id);
//            scheduleid.setType(Integer.class);
//            request.addProperty(scheduleid);
//
//
//            PropertyInfo journeydate = new PropertyInfo();
//            journeydate.setName("JourneyDate");
//            //journeydate.setValue(Search_Buses_Key.Selected_date);
//            journeydate.setType(String.class);
//            request.addProperty(journeydate);

//
//
//
//
//
//
//            if(Global_Travel.build_type ==0)
//            {
//                Log.e("vikas envolop",request.toString());
//            }
//
//
//            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//            envelope.implicitTypes = true;
//            envelope.setAddAdornments(false);
//            envelope.dotNet = true;
//
//            envelope.setOutputSoapObject(request);

//             HttpTransportSE httpTransport = new HttpTransportSE(Constants.GLOBEL_URL);
//            httpTransport.debug = true;
//
//            try {
//                httpTransport.call(Constants.GLOBEL_NAMESPACE+Constants.METHOD_GetRouteScheduleDetail, envelope);
//            } catch (HttpResponseException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            } catch (XmlPullParserException e) {
////                // TODO Auto-generated catch block
////                e.printStackTrace();
////            } //send request
//           // SoapObject result = null;
//            soapresult_schedule_detail = null;
//
//            try {
//
//               // result = (SoapObject)envelope.getResponse();
//                soapresult_schedule_detail = (SoapObject)envelope.getResponse();
//
//            } catch (SoapFault e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//            if(soapresult_schedule_detail != null)
//            {
//
//                String Is_success =((SoapObject)soapresult_schedule_detail.getProperty("Response")).getPrimitiveProperty("IsSuccess").toString();
//                if(Is_success.equals("true"))
//                {
//                    try {
//                        cancellation_Policy_data = new JSONArray();
//
//                        for(int i=0;i< ((SoapObject)soapresult_schedule_detail.getProperty("CancellationCharges")).getPropertyCount();i++)
//                        {
//                            String condition = ((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("CancellationCharges")).getProperty(i)).getProperty("MinsBeforeDeparture").toString();
//                            int int_condition = Integer.parseInt(condition);
//                            int_condition = int_condition/60;
//                            String percentage =((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("CancellationCharges")).getProperty(i)).getProperty("ChargePercentage").toString();
//                            JSONObject jb  = new JSONObject();
//
//                            jb.put("condition",Integer.toString(int_condition));
//                            jb.put("percentage",percentage);
//
//
//
//
//                            cancellation_Policy_data.put(jb);
//                        }
//
//                        count =  ((SoapObject)soapresult_schedule_detail.getProperty("Pickup")).getPropertyCount();
//                        response = soapresult_schedule_detail.toString();
//                    }
//                    catch (Exception e)
//                    {
//                        e.printStackTrace();
//                    }
//
//
//
//
//                    //message = Integer.toString(((SoapObject)soapresult_schedule_detail.getProperty(5)).getPropertyCount());
//
//
//
//
//                    thread_realm.beginTransaction();
//                    thread_realm.delete(Selected_Seats.class);
//                    thread_realm.delete(Schedule_Details.class);
//                    thread_realm.delete(Pickup_Place_Detail.class);
//                    thread_realm.delete(Seat_details.class);
//                    thread_realm.commitTransaction();
//
//                    try
//                    {
//
//                        if(soapresult_schedule_detail !=null)
//                        {
//
//                            //String Is_success =((SoapObject)soapresult_schedule_detail.getProperty(0)).getPrimitiveProperty("IsSuccess").toString();
//                            if(Is_success.equals("true")) {
//
//
//
//                                int RouteScheduleId = Integer.parseInt(((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("RouteScheduleId").toString());
//                                String JourneyDate = ((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("JourneyDate").toString();
//                                int CompanyId = Integer.parseInt(((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("CompanyId").toString());
//                                String CompanyName = ((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("CompanyName").toString();
//                                int FromCityId = Integer.parseInt(((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("FromCityId").toString());
//                                String FromCityName = ((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("FromCityName").toString();
//                                int ToCityId = Integer.parseInt(((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("ToCityId").toString());
//                                String ToCityName = ((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("ToCityName").toString();
//                                // Date DepartureTime;
//                                String DepTime = ((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("DepTime").toString();
//                                //Date ArrivalTime;
//                                String ArrTime = ((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("ArrTime").toString();
//                                float Fare = Float.parseFloat(((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("Fare").toString());
//                                float SeaterFareNAC = Float.parseFloat(((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("SeaterFareNAC").toString());
//                                float SeaterFareAC = Float.parseFloat(((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("SeaterFareAC").toString());
//                                float SleeperFareNAC = Float.parseFloat(((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("SleeperFareNAC").toString());
//                                float SleeperFareAC = Float.parseFloat(((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("SleeperFareAC").toString());
//                                Boolean HasAC = Boolean.parseBoolean(((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("HasAC").toString());
//                                Boolean HasNAC = Boolean.parseBoolean(((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("HasNAC").toString());
//                                Boolean HasSeater = Boolean.parseBoolean(((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("HasSeater").toString());
//                                Boolean HasSleeper = Boolean.parseBoolean(((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("HasSleeper").toString());
//                                String BusLabel = ((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("BusLabel").toString();
//                                String BusTypeName = ((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("BusTypeName").toString();
//                                float CommPCT = (int)Float.parseFloat(((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("CommPCT").toString()) ;
//                                float CommAmount = Float.parseFloat(((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("CommAmount").toString());
//
//
//                                float offer_per;
//                                float after_offer_fare;
//
//
//                                if(TRAVEL_DATA.ISEVA_SHARE_PCT <= CommPCT)
//                                {
//                                    offer_per = CommPCT - TRAVEL_DATA.ISEVA_SHARE_PCT;
//                                }
//                                else
//                                {
//                                    offer_per =0;
//                                }
//
//
//                                after_offer_fare = (Fare * (100 - offer_per)) / 100;
//
//                                after_offer_fare = (float) Math.round(after_offer_fare);
//
//                                if(Global_Travel.build_type ==0)
//                                {
//                                    Log.e("HARSH","after_fare = "+after_offer_fare);
//                                    Log.e("HARSH"," percentage = "+offer_per);
//                                    Log.e("HARSH ","ISEVA_SHARE_PCT per =" + TRAVEL_DATA.ISEVA_SHARE_PCT);
//                                }
//
//
//
//
//
//                                thread_realm.beginTransaction();
//                                Schedule_Details schedule_details = thread_realm.createObject(Schedule_Details.class);
//                                schedule_details.setRouteScheduleId(RouteScheduleId);
//                                schedule_details.setJourneyDate(JourneyDate);
//                                schedule_details.setCompanyId(CompanyId);
//                                schedule_details.setCompanyName(CompanyName);
//                                schedule_details.setFromCityId(FromCityId);
//                                schedule_details.setFromCityName(FromCityName);
//                                schedule_details.setToCityId(ToCityId);
//                                schedule_details.setToCityName(ToCityName);
//                                schedule_details.setDepTime(DepTime);
//                                schedule_details.setArrTime(ArrTime);
//                                schedule_details.setFare(Fare);
//                                schedule_details.setFare_after_offer(after_offer_fare);
//                                schedule_details.setSeaterFareNAC(SeaterFareNAC);
//                                schedule_details.setSeaterFareAC(SeaterFareAC);
//                                schedule_details.setSleeperFareNAC(SleeperFareNAC);
//                                schedule_details.setSleeperFareAC(SleeperFareAC);
//                                schedule_details.setHasAC(HasAC);
//                                schedule_details.setHasNAC(HasNAC);
//                                schedule_details.setHasSeater(HasSeater);
//                                schedule_details.setHasSleeper(HasSleeper);
//                                schedule_details.setBusLabel(BusLabel);
//                                schedule_details.setBusTypeName(BusTypeName);
//                                schedule_details.setCommPCT(CommPCT);
//                                schedule_details.setCommAmount(CommAmount);
//
//                                thread_realm.commitTransaction();
//
//
//                                maxrow = Integer.parseInt(((SoapObject)soapresult_schedule_detail.getProperty("Layout")).getProperty("MaxRows").toString());
//                                maxcol = Integer.parseInt(((SoapObject)soapresult_schedule_detail.getProperty("Layout")).getProperty("MaxColumns").toString());
//
////                                Search_Buses_Key.maxcol = maxcol;
////                                Search_Buses_Key.maxrow = maxrow;
//
//
//                                for(int i =0;i< ((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Layout")).getProperty("SeatDetails")).getPropertyCount();i++)
//                                {
//
//
//
//                                    int Row = Integer.parseInt(((SoapObject)((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Layout")).getProperty("SeatDetails")).getProperty(i)).getProperty("Row").toString());
//                                    int Col = Integer.parseInt(((SoapObject)((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Layout")).getProperty("SeatDetails")).getProperty(i)).getProperty("Col").toString()) ;
//                                    int Height = Integer.parseInt(((SoapObject)((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Layout")).getProperty("SeatDetails")).getProperty(i)).getProperty("Height").toString());
//                                    int Width = Integer.parseInt(((SoapObject)((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Layout")).getProperty("SeatDetails")).getProperty(i)).getProperty("Width").toString());
//                                    String SeatNo = ((SoapObject)((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Layout")).getProperty("SeatDetails")).getProperty(i)).getProperty("SeatNo").toString();
//                                    String Gender = ((SoapObject)((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Layout")).getProperty("SeatDetails")).getProperty(i)).getProperty("Gender").toString();
//                                    // private    Boolean IsAisle;
//                                    int Deck = Integer.parseInt(((SoapObject)((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Layout")).getProperty("SeatDetails")).getProperty(i)).getProperty("Deck").toString());
//                                    Boolean IsAC = Boolean.parseBoolean(((SoapObject)((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Layout")).getProperty("SeatDetails")).getProperty(i)).getProperty("IsAC").toString());
//                                    Boolean IsSleeper = Boolean.parseBoolean(((SoapObject)((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Layout")).getProperty("SeatDetails")).getProperty(i)).getProperty("IsSleeper").toString());
//                                    Boolean IsAvailable = Boolean.parseBoolean(((SoapObject)((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Layout")).getProperty("SeatDetails")).getProperty(i)).getProperty("IsAvailable").toString());
//                                    Boolean IsAisle = Boolean.parseBoolean(((SoapObject)((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Layout")).getProperty("SeatDetails")).getProperty(i)).getProperty("IsAisle").toString());
//                                    float Fare_local = Float.parseFloat(((SoapObject)((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Layout")).getProperty("SeatDetails")).getProperty(i)).getProperty("Fare").toString());
//                                    float ChildFare = Float.parseFloat(((SoapObject)((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Layout")).getProperty("SeatDetails")).getProperty(i)).getProperty("ChildFare").toString());
//                                    float InfantFare = Float.parseFloat(((SoapObject)((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Layout")).getProperty("SeatDetails")).getProperty(i)).getProperty("InfantFare").toString());
//
//                                    float after_offer_seat_fare;
//                                    if(Global_Travel.build_type ==0)
//                                    {
//                                        Log.e("vikas seat_o_fare",""+Fare_local);
//                                        Log.e("vikas seat comm",""+CommPCT);
//                                        Log.e("vikas seat o_commi",""+offer_per);
//                                    }
//
//
//
//                                    after_offer_seat_fare = (Fare_local *(100-offer_per))/100;
//
//                                    after_offer_seat_fare = (float) Math.round(after_offer_seat_fare);
//
//
//                                    thread_realm.beginTransaction();
//                                    Seat_details seat_details = thread_realm.createObject(Seat_details.class);
//                                    seat_details.setRow(Row);
//                                    seat_details.setCol(Col);
//                                    seat_details.setHeight(Height);
//                                    seat_details.setWidth(Width);
//                                    seat_details.setSeatNo(SeatNo);
//                                    seat_details.setGender(Gender);
//                                    seat_details.setDeck(Deck);
//                                    seat_details.setIsAc(IsAC);
//                                    seat_details.setIsSleeper(IsSleeper);
//                                    seat_details.setIsAvailable(IsAvailable);
//                                    seat_details.setIsAisle(IsAisle);
//                                    seat_details.setFare(Fare_local);
//                                    seat_details.setFare_after_offer(after_offer_seat_fare);
//                                    seat_details.setChildFare(ChildFare);
//                                    seat_details.setInfantFare(InfantFare);
//                                    thread_realm.commitTransaction();
//
//                                }
//
//                                for(int k=0; k <((SoapObject)soapresult_schedule_detail.getProperty("Pickup")).getPropertyCount();k++)
//                                {
//
//
//                                    int ProviderId = Integer.parseInt(((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Pickup")).getProperty(k)).getProperty("ProviderId").toString());
//                                    int PickupId = Integer.parseInt(((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Pickup")).getProperty(k)).getProperty("PickupId").toString());
//                                    String PickupName = ((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Pickup")).getProperty(k)).getProperty("PickupName").toString();
//                                    String PkpTime = ((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Pickup")).getProperty(k)).getProperty("PkpTime").toString();
//                                    String Address = ((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Pickup")).getProperty(k)).getProperty("Address").toString();
//                                    String Landmark = ((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Pickup")).getProperty(k)).getProperty("Landmark").toString();
//                                    String ProviderPickupId = ((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Pickup")).getProperty(k)).getProperty("ProviderPickupId").toString();
//                                    String Phone = ((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Pickup")).getProperty(k)).getProperty("Phone").toString();
//
//                                    thread_realm.beginTransaction();
//                                    Pickup_Place_Detail  pickup_place_detail  = thread_realm.createObject(Pickup_Place_Detail.class);
//                                    pickup_place_detail.setProviderId(ProviderId);
//                                    pickup_place_detail.setPickupId(PickupId);
//                                    pickup_place_detail.setPickupName(PickupName);
//                                    pickup_place_detail.setPkpTime(PkpTime);
//                                    pickup_place_detail.setAddress(Address);
//                                    pickup_place_detail.setLandmark(Landmark);
//                                    pickup_place_detail.setProviderPickupId(ProviderPickupId);
//                                    pickup_place_detail.setPhone(Phone);
//                                    thread_realm.commitTransaction();
//                                }
//
//
//
//                            }
//                            else
//                            {
//                                message = "response success false";
//                            }
//                        }
//                        else
//                        {
//                            message = "response not success";
//                        }
//
//                    }
//                    catch (Exception e)
//                    {
//                        e.printStackTrace();
//                    }
//
//                }
//                else
//                {
//                    message = "response success false";
//                }
//
//
//            }
//            else
//            {
//                message ="response not seccess";
//            }
//
//
//            return null;
//        }
//    }



