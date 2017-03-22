package com.iseva.app.source.travel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.iseva.app.source.Adapter.PagerAdapter_Select_Seat;
import com.iseva.app.source.R;
import com.iseva.app.source.Realm_objets.Pickup_Place_Detail;
import com.iseva.app.source.Realm_objets.Schedule_Details;
import com.iseva.app.source.Realm_objets.Seat_details;
import com.iseva.app.source.Realm_objets.Selected_Seats;


import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmResults;




public class Activity_Select_Seats extends AppCompatActivity {

    Realm My_realm;

    ImageView iv_header;

    TextView tv_header;
    TextView cancellation_tv;
    TextView Total_Fare_tv;
    TextView Total_Seats_tv;
    TextView cancel_cancellation_policy_layout;

    int schedule_id;
    int count;
    int maxrow;
    int maxcol;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__select__seats);
        selected_seat_activity = this;

        My_realm = Realm.getInstance(getApplicationContext());
        Intent i = getIntent();
        schedule_id = Integer.parseInt(i.getStringExtra("schedule_id"));

        Total_Fare_tv = (TextView)findViewById(R.id.Total_Fare);
        Total_Seats_tv = (TextView)findViewById(R.id.Selected_Seats);
        progress_layout = (LinearLayout)findViewById(R.id.select_seat_dynamic_data_layout);
        progressBar = (ProgressBar)findViewById(R.id.activity_seat_progress_bar);
        loader_layout = (LinearLayout)findViewById(R.id.select_seat_loader_layout);
        cancel_cancellation_policy_layout = (TextView)findViewById(R.id.cancel_cancellation_layout);

        iv_header = (ImageView)findViewById(R.id.header_back_button);
        tv_header = (TextView)findViewById(R.id.header_text);

        select_seat_proceed_btn = (Button)findViewById(R.id.select_seat_proceed_btn);
        cancellation_tv = (TextView)findViewById(R.id.cancellation_tv);

        String first = "By booking this ticket you agree to the ";
        String second = "<font color='#1BAFCD'>Cancellation Policy</font>";
        String third = " of the bus operato.";

        if (Build.VERSION.SDK_INT >= 24) {

            cancellation_tv.setText(Html.fromHtml(first + second+third,0));
           // for 24 api and more
        }
        else {

            cancellation_tv.setText(Html.fromHtml(first + second+third));
        }

        tv_header.setText("Select Seats");

        setOnclicklister();



        if(isNetworkConnected())
        {
            RoutesDetail routesDetail = new RoutesDetail();
            routesDetail.execute();
        }
        else
        {
           callalertbox();
        }

    }

    public void callalertbox()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.internet_connection_error_title))
                .setMessage(getResources().getString(R.string.internet_connection_error_message_try_again))
                .setCancelable(false)
                .setPositiveButton("Retry",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(TryAgaintoCallService())
                        {
                            dialog.cancel();
                        }
                        else
                        {
                            callalertbox();
                        }

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

        Button b = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        b.setTextColor(ContextCompat.getColor(this, R.color.app_theme_color));
    }

    public Boolean TryAgaintoCallService()
    {
        if(isNetworkConnected())
        {
            RoutesDetail routesDetail = new RoutesDetail();
            routesDetail.execute();
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public void setOnclicklister()
    {
        cancellation_tv.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                LinearLayout cl = (LinearLayout)findViewById(R.id.cancellation_layout);
                cl.setVisibility(View.VISIBLE);
            }
        });

        cancel_cancellation_policy_layout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                LinearLayout cl = (LinearLayout)findViewById(R.id.cancellation_layout);
                cl.setVisibility(View.GONE);
            }
        });
        iv_header.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                activity_dismiss();
            }
        });

        select_seat_proceed_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                My_realm.beginTransaction();
                RealmResults<Selected_Seats> All_row = My_realm.where(Selected_Seats.class).findAll();
                My_realm.commitTransaction();

                if(All_row.size() == 0)
                {
                    showAlertDialog(getResources().getString(R.string.validating_error_title),"Select Atleast 1 Seat !","ok");
                }
                else
                {
                    Intent i = new Intent(Activity_Select_Seats.this,Activity_Passenger_Details.class);
                    i.putExtra("schedule_id",""+schedule_id);
                    if(cancellation_Policy_data.length() > 0)
                    {
                        i.putExtra("cancellation_data",cancellation_Policy_data.toString());
                    }
                    else
                    {
                        i.putExtra("cancellation_data","");
                    }

                    startActivity(i);
                    overridePendingTransition(R.anim.anim_in, R.anim.anim_none);
                }


            }
        });
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

    public void showAlertDialog(String title,String message,String buttonlabel)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Select_Seats.this);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton(buttonlabel,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button b = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        b.setLayoutParams(lp);
        b.setBackgroundResource(R.drawable.btn_background);
        b.setTextColor(ContextCompat.getColor(Activity_Select_Seats.this, R.color.app_white));
    }


    public void update_seat_fare()
    {
        float Total_Fare = 0;
        String Total_Seat ="";

        My_realm.beginTransaction();
        RealmResults<Selected_Seats> All_row = My_realm.where(Selected_Seats.class).findAll();

        for(int i=0;i<All_row.size();i++)
        {
            Total_Fare = Total_Fare + All_row.get(i).getFare();
            if(i == 0)
            {
                Total_Seat = Total_Seat+All_row.get(i).getSeatNo();
            }
            else
            {
                Total_Seat = Total_Seat+", "+All_row.get(i).getSeatNo();
            }

        }

        My_realm.commitTransaction();

        Total_Fare_tv.setText(""+Total_Fare);
        Total_Seats_tv.setText(Total_Seat);

    }

    public void set_dynamic_data()
    {


        My_realm.beginTransaction();

        RealmResults<Seat_details> deck2_seat = My_realm.where(Seat_details.class).equalTo("Deck",2).findAll();
        My_realm.commitTransaction();
        if(deck2_seat.size() == 0)
        {
            ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_select_seat);
            viewPager.setAdapter(new PagerAdapter_Select_Seat(getSupportFragmentManager(),1));

            tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs_select_seat);

            tabsStrip.setViewPager(viewPager);



        }
        else
        {



            ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_select_seat);
            viewPager.setAdapter(new PagerAdapter_Select_Seat(getSupportFragmentManager(),2));

           tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs_select_seat);

            tabsStrip.setViewPager(viewPager);
            set_fist_tab_color();
            tabsStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                // This method will be invoked when a new page becomes selected.
                @Override
                public void onPageSelected(int position) {
                    LinearLayout   mTabsLinearLayout = ((LinearLayout)tabsStrip.getChildAt(0));
                    for(int i=0; i < mTabsLinearLayout.getChildCount(); i++) {
                        TextView tv = (TextView) mTabsLinearLayout.getChildAt(i);

                        if(i == position){
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

            Log.e("vikas","deck2 not 0");
        }
        loader_layout.setVisibility(View.GONE);
        LinearLayout cancellation_text = (LinearLayout) findViewById(R.id.cancellation_text);
        try {
            for (int j = 0; j < cancellation_Policy_data.length(); j++) {


                LinearLayout divider = new LinearLayout(Activity_Select_Seats.this);
                LinearLayout single_layout = new LinearLayout(Activity_Select_Seats.this);
                single_layout.setOrientation(LinearLayout.HORIZONTAL);
                single_layout.setPadding(15, 15, 15, 15);


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

                if (j == 0) {
                    tv1.setText("Between 0 Hrs. to " + cancellation_Policy_data.getJSONObject(j).getString("condition") + " Hrs.");
                } else {
                    tv1.setText("Between " + cancellation_Policy_data.getJSONObject(j - 1).getString("condition") + " Hrs. to " + cancellation_Policy_data.getJSONObject(j).getString("condition") + "Hrs.");
                }

                int percentage_int = Integer.parseInt(cancellation_Policy_data.getJSONObject(j).getString("percentage"));

                tv2.setText(""+(100-percentage_int));

                single_layout.addView(tv1);
                single_layout.addView(tv2);
                cancellation_text.addView(divider);
                cancellation_text.addView(single_layout);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void set_fist_tab_color()
    {
        LinearLayout mTabsLinearLayout = ((LinearLayout)tabsStrip.getChildAt(0));
        for(int i=0; i < mTabsLinearLayout.getChildCount(); i++){
            TextView tv = (TextView) mTabsLinearLayout.getChildAt(i);

            if(i == 0){
                tv.setTextColor(ContextCompat.getColor(Activity_Select_Seats.this, R.color.tab_active_text_color));
            } else {
                tv.setTextColor(Color.GRAY);
            }
        }
    }



    private class RoutesDetail extends AsyncTask<Void,Void,Void>
    {



        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if(soapresult_schedule_detail != null)
            {
                if(((SoapObject)soapresult_schedule_detail.getProperty("Response")).getPrimitivePropertyAsString("IsSuccess").equals("true"))
                {
                  /*  LinearLayout cancellation_text = (LinearLayout) findViewById(R.id.cancellation_text);
                    TextView tv = new TextView(Activity_Select_Seats.this);
                    tv.setText(soapresult_schedule_detail.toString());
                    cancellation_text.addView(tv);*/
                    Log.e("vikas",soapresult_schedule_detail.toString().trim());
                    Log.e("vikas",Integer.toString(Search_Buses_Key.maxcol));
                    set_dynamic_data();
                }
                else
                {
                    showAlertDialog(getResources().getString(R.string.validating_error_title),((SoapObject)soapresult_schedule_detail.getProperty("Response")).getPrimitivePropertyAsString("Message"),"Ok");
                }
            }
            else
            {
                showAlertDialog(getResources().getString(R.string.validating_error_title),"Some Error Accured Please Try Again !","Ok");
            }




        }


        @Override
        protected Void doInBackground(Void... voids) {

            Realm thread_realm = Realm.getInstance(getApplicationContext());
            SoapObject request = new SoapObject(Constants.GLOBEL_NAMESPACE,Constants.METHOD_GetRouteScheduleDetail);

            SoapObject sa = new SoapObject(null,"Authentication");



            PropertyInfo userid = new PropertyInfo();
            userid.setName("UserID");
            userid.setValue(LoginCridantial.UserId.trim());
            userid.setType(Integer.class);
            sa.addProperty(userid);

            PropertyInfo usertype = new PropertyInfo();
            usertype.setName("UserType");
            usertype.setValue(LoginCridantial.UserType.trim());
            usertype.setType(String.class);
            sa.addProperty(usertype);

            PropertyInfo userkey = new PropertyInfo();
            userkey.setName("Key");
            userkey.setValue(LoginCridantial.UserKey.trim());
            userkey.setType(String.class);
            sa.addProperty(userkey);

            request.addSoapObject(sa);

            PropertyInfo scheduleid = new PropertyInfo();
            scheduleid.setName("RouteScheduleId");
            scheduleid.setValue(schedule_id);
            scheduleid.setType(Integer.class);
            request.addProperty(scheduleid);


            PropertyInfo journeydate = new PropertyInfo();
            journeydate.setName("JourneyDate");
            journeydate.setValue(Search_Buses_Key.Selected_date);
            journeydate.setType(String.class);
            request.addProperty(journeydate);








            Log.e("vikas envolop",request.toString());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.implicitTypes = true;
            envelope.setAddAdornments(false);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

             HttpTransportSE httpTransport = new HttpTransportSE("http://affapi.mantistechnologies.com/Service.asmx");
            httpTransport.debug = true;

            try {
                httpTransport.call(Constants.GLOBEL_NAMESPACE+Constants.METHOD_GetRouteScheduleDetail, envelope);
            } catch (HttpResponseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } //send request
           // SoapObject result = null;
            soapresult_schedule_detail = null;

            try {

               // result = (SoapObject)envelope.getResponse();
                soapresult_schedule_detail = (SoapObject)envelope.getResponse();

            } catch (SoapFault e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if(soapresult_schedule_detail != null)
            {

                String Is_success =((SoapObject)soapresult_schedule_detail.getProperty("Response")).getPrimitiveProperty("IsSuccess").toString();
                if(Is_success.equals("true"))
                {
                    try {
                        cancellation_Policy_data = new JSONArray();

                        for(int i=0;i< ((SoapObject)soapresult_schedule_detail.getProperty("CancellationCharges")).getPropertyCount();i++)
                        {
                            String condition = ((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("CancellationCharges")).getProperty(i)).getProperty("MinsBeforeDeparture").toString();
                            int int_condition = Integer.parseInt(condition);
                            int_condition = int_condition/60;
                            String percentage =((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("CancellationCharges")).getProperty(i)).getProperty("ChargePercentage").toString();
                            JSONObject jb  = new JSONObject();

                            jb.put("condition",Integer.toString(int_condition));
                            jb.put("percentage",percentage);




                            cancellation_Policy_data.put(jb);
                        }

                        count =  ((SoapObject)soapresult_schedule_detail.getProperty("Pickup")).getPropertyCount();
                        response = soapresult_schedule_detail.toString();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }




                    //message = Integer.toString(((SoapObject)soapresult_schedule_detail.getProperty(5)).getPropertyCount());




                    thread_realm.beginTransaction();
                    thread_realm.clear(Selected_Seats.class);
                    thread_realm.clear(Schedule_Details.class);
                    thread_realm.clear(Pickup_Place_Detail.class);
                    thread_realm.clear(Seat_details.class);
                    thread_realm.commitTransaction();

                    try
                    {

                        if(soapresult_schedule_detail !=null)
                        {

                            //String Is_success =((SoapObject)soapresult_schedule_detail.getProperty(0)).getPrimitiveProperty("IsSuccess").toString();
                            if(Is_success.equals("true")) {



                                int RouteScheduleId = Integer.parseInt(((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("RouteScheduleId").toString());
                                String JourneyDate = ((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("JourneyDate").toString();
                                int CompanyId = Integer.parseInt(((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("CompanyId").toString());
                                String CompanyName = ((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("CompanyName").toString();
                                int FromCityId = Integer.parseInt(((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("FromCityId").toString());
                                String FromCityName = ((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("FromCityName").toString();
                                int ToCityId = Integer.parseInt(((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("ToCityId").toString());
                                String ToCityName = ((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("ToCityName").toString();
                                // Date DepartureTime;
                                String DepTime = ((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("DepTime").toString();
                                //Date ArrivalTime;
                                String ArrTime = ((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("ArrTime").toString();
                                float Fare = Float.parseFloat(((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("Fare").toString());
                                float SeaterFareNAC = Float.parseFloat(((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("SeaterFareNAC").toString());
                                float SeaterFareAC = Float.parseFloat(((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("SeaterFareAC").toString());
                                float SleeperFareNAC = Float.parseFloat(((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("SleeperFareNAC").toString());
                                float SleeperFareAC = Float.parseFloat(((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("SleeperFareAC").toString());
                                Boolean HasAC = Boolean.parseBoolean(((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("HasAC").toString());
                                Boolean HasNAC = Boolean.parseBoolean(((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("HasNAC").toString());
                                Boolean HasSeater = Boolean.parseBoolean(((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("HasSeater").toString());
                                Boolean HasSleeper = Boolean.parseBoolean(((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("HasSleeper").toString());
                                String BusLabel = ((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("BusLabel").toString();
                                String BusTypeName = ((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("BusTypeName").toString();
                                float CommPCT = Float.parseFloat(((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("CommPCT").toString()) ;
                                float CommAmount = Float.parseFloat(((SoapObject)soapresult_schedule_detail.getProperty("Route")).getProperty("CommAmount").toString());

                                thread_realm.beginTransaction();
                                Schedule_Details schedule_details = thread_realm.createObject(Schedule_Details.class);
                                schedule_details.setRouteScheduleId(RouteScheduleId);
                                schedule_details.setJourneyDate(JourneyDate);
                                schedule_details.setCompanyId(CompanyId);
                                schedule_details.setCompanyName(CompanyName);
                                schedule_details.setFromCityId(FromCityId);
                                schedule_details.setFromCityName(FromCityName);
                                schedule_details.setToCityId(ToCityId);
                                schedule_details.setToCityName(ToCityName);
                                schedule_details.setDepTime(DepTime);
                                schedule_details.setArrTime(ArrTime);
                                schedule_details.setFare(Fare);
                                schedule_details.setSeaterFareNAC(SeaterFareNAC);
                                schedule_details.setSeaterFareAC(SeaterFareAC);
                                schedule_details.setSleeperFareNAC(SleeperFareNAC);
                                schedule_details.setSleeperFareAC(SleeperFareAC);
                                schedule_details.setHasAC(HasAC);
                                schedule_details.setHasNAC(HasNAC);
                                schedule_details.setHasSeater(HasSeater);
                                schedule_details.setHasSleeper(HasSleeper);
                                schedule_details.setBusLabel(BusLabel);
                                schedule_details.setBusTypeName(BusTypeName);
                                schedule_details.setCommPCT(CommPCT);
                                schedule_details.setCommAmount(CommAmount);

                                thread_realm.commitTransaction();


                                maxrow = Integer.parseInt(((SoapObject)soapresult_schedule_detail.getProperty("Layout")).getProperty("MaxRows").toString());
                                maxcol = Integer.parseInt(((SoapObject)soapresult_schedule_detail.getProperty("Layout")).getProperty("MaxColumns").toString());

                                Search_Buses_Key.maxcol = maxcol;
                                Search_Buses_Key.maxrow = maxrow;


                                for(int i =0;i< ((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Layout")).getProperty("SeatDetails")).getPropertyCount();i++)
                                {



                                    int Row = Integer.parseInt(((SoapObject)((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Layout")).getProperty("SeatDetails")).getProperty(i)).getProperty("Row").toString());
                                    int Col = Integer.parseInt(((SoapObject)((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Layout")).getProperty("SeatDetails")).getProperty(i)).getProperty("Col").toString()) ;
                                    int Height = Integer.parseInt(((SoapObject)((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Layout")).getProperty("SeatDetails")).getProperty(i)).getProperty("Height").toString());
                                    int Width = Integer.parseInt(((SoapObject)((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Layout")).getProperty("SeatDetails")).getProperty(i)).getProperty("Width").toString());
                                    String SeatNo = ((SoapObject)((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Layout")).getProperty("SeatDetails")).getProperty(i)).getProperty("SeatNo").toString();
                                    String Gender = ((SoapObject)((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Layout")).getProperty("SeatDetails")).getProperty(i)).getProperty("Gender").toString();
                                    // private    Boolean IsAisle;
                                    int Deck = Integer.parseInt(((SoapObject)((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Layout")).getProperty("SeatDetails")).getProperty(i)).getProperty("Deck").toString());
                                    Boolean IsAC = Boolean.parseBoolean(((SoapObject)((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Layout")).getProperty("SeatDetails")).getProperty(i)).getProperty("IsAC").toString());
                                    Boolean IsSleeper = Boolean.parseBoolean(((SoapObject)((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Layout")).getProperty("SeatDetails")).getProperty(i)).getProperty("IsSleeper").toString());
                                    Boolean IsAvailable = Boolean.parseBoolean(((SoapObject)((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Layout")).getProperty("SeatDetails")).getProperty(i)).getProperty("IsAvailable").toString());
                                    Boolean IsAisle = Boolean.parseBoolean(((SoapObject)((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Layout")).getProperty("SeatDetails")).getProperty(i)).getProperty("IsAisle").toString());
                                    float Fare_local = Float.parseFloat(((SoapObject)((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Layout")).getProperty("SeatDetails")).getProperty(i)).getProperty("Fare").toString());
                                    float ChildFare = Float.parseFloat(((SoapObject)((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Layout")).getProperty("SeatDetails")).getProperty(i)).getProperty("ChildFare").toString());
                                    float InfantFare = Float.parseFloat(((SoapObject)((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Layout")).getProperty("SeatDetails")).getProperty(i)).getProperty("InfantFare").toString());

                                    thread_realm.beginTransaction();
                                    Seat_details seat_details = thread_realm.createObject(Seat_details.class);
                                    seat_details.setRow(Row);
                                    seat_details.setCol(Col);
                                    seat_details.setHeight(Height);
                                    seat_details.setWidth(Width);
                                    seat_details.setSeatNo(SeatNo);
                                    seat_details.setGender(Gender);
                                    seat_details.setDeck(Deck);
                                    seat_details.setIsAc(IsAC);
                                    seat_details.setIsSleeper(IsSleeper);
                                    seat_details.setIsAvailable(IsAvailable);
                                    seat_details.setIsAisle(IsAisle);
                                    seat_details.setFare(Fare_local);
                                    seat_details.setChildFare(ChildFare);
                                    seat_details.setInfantFare(InfantFare);
                                    thread_realm.commitTransaction();

                                }

                                for(int k=0; k <((SoapObject)soapresult_schedule_detail.getProperty("Pickup")).getPropertyCount();k++)
                                {


                                    int ProviderId = Integer.parseInt(((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Pickup")).getProperty(k)).getProperty("ProviderId").toString());
                                    int PickupId = Integer.parseInt(((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Pickup")).getProperty(k)).getProperty("PickupId").toString());
                                    String PickupName = ((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Pickup")).getProperty(k)).getProperty("PickupName").toString();
                                    String PkpTime = ((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Pickup")).getProperty(k)).getProperty("PkpTime").toString();
                                    String Address = ((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Pickup")).getProperty(k)).getProperty("Address").toString();
                                    String Landmark = ((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Pickup")).getProperty(k)).getProperty("Landmark").toString();
                                    String ProviderPickupId = ((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Pickup")).getProperty(k)).getProperty("ProviderPickupId").toString();
                                    String Phone = ((SoapObject)((SoapObject)soapresult_schedule_detail.getProperty("Pickup")).getProperty(k)).getProperty("Phone").toString();

                                    thread_realm.beginTransaction();
                                    Pickup_Place_Detail  pickup_place_detail  = thread_realm.createObject(Pickup_Place_Detail.class);
                                    pickup_place_detail.setProviderId(ProviderId);
                                    pickup_place_detail.setPickupId(PickupId);
                                    pickup_place_detail.setPickupName(PickupName);
                                    pickup_place_detail.setPkpTime(PkpTime);
                                    pickup_place_detail.setAddress(Address);
                                    pickup_place_detail.setLandmark(Landmark);
                                    pickup_place_detail.setProviderPickupId(ProviderPickupId);
                                    pickup_place_detail.setPhone(Phone);
                                    thread_realm.commitTransaction();
                                }



                            }
                            else
                            {
                                message = "response success false";
                            }
                        }
                        else
                        {
                            message = "response not success";
                        }

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
                else
                {
                    message = "response success false";
                }


            }
            else
            {
                message ="response not seccess";
            }


            return null;
        }
    }



}
