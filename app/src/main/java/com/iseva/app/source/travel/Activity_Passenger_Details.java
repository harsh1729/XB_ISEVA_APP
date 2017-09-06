package com.iseva.app.source.travel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.iseva.app.source.R;
import com.iseva.app.source.Realm_objets.Pickup_Place_Detail;
import com.iseva.app.source.Realm_objets.Selected_Seats;
import com.iseva.app.source.travel.Global_Travel.TRAVEL_DATA;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import java.lang.reflect.Field;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;





public class Activity_Passenger_Details extends Activity {

    Session_manager session_manager;

    Realm My_realm;
    LinearLayout header_text_layout;
    TextView header_tv;
    RealmResults<Pickup_Place_Detail> pickup_place_detail_list;
    RealmResults<Selected_Seats> Selected_seat_list;
    Spinner boarding_point_spinner;
    ImageView header_iv;
    Button proceed_to_book_btn;

    SoapObject soapresult_detail;
    String message;
    int bus_id;

    String cancellation_data_string;

    int spinner_position = 0;
    String pickup_id = "";

    EditText contact_mobile;
    EditText contact_email;
    EditText contact_name;

    String Contact_email_text;
    String Contact_mobile_text;
    String Contact_name_text;

    float Total_offer_fare = 0;

    JSONArray passenger_list;

    SoapObject passengers_object;

    View v;

    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passanger__details);




        My_realm = Realm.getDefaultInstance();

        Intent i = getIntent();
        session_manager = new Session_manager(this);
        bus_id = Integer.parseInt(i.getStringExtra("bus_id"));
        cancellation_data_string = i.getStringExtra("cancellation_data");

        My_realm.beginTransaction();
        pickup_place_detail_list = My_realm.where(Pickup_Place_Detail.class).findAll();
        Selected_seat_list = My_realm.where(Selected_Seats.class).findAll();
        My_realm.commitTransaction();

        boarding_point_spinner  = (Spinner)findViewById(R.id.boarding_point_spinner);

        header_tv = (TextView)findViewById(R.id.header_text);
        header_text_layout = (LinearLayout)findViewById(R.id.layout_header_text);
        header_text_layout.setGravity(Gravity.CENTER_VERTICAL);

        header_iv = (ImageView)findViewById(R.id.header_back_button);

        contact_mobile = (EditText)findViewById(R.id.contact_mobile);
        contact_email = (EditText)findViewById(R.id.contact_email);
        contact_name = (EditText)findViewById(R.id.contact_name);

        proceed_to_book_btn = (Button)findViewById(R.id.proceed_to_book_btn);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        params.setMargins(getResources().getDimensionPixelSize(R.dimen.padding_margin_10),0,0,0);

        header_tv.setLayoutParams(params);
        header_tv.setText("Passenger Details");

        setClickListner();
        set_boarding_points();
        setLayout_passanger();

        v =  (View)findViewById(R.id.passengers);

        if(session_manager.isLoggedIn())
        {
            contact_name.setText(session_manager.getname());
            contact_mobile.setText(session_manager.getphone());
            contact_email.setText(session_manager.getusername());
        }


        float Total_Fare = 0;


        My_realm.beginTransaction();
        RealmResults<Selected_Seats> All_row = My_realm.where(Selected_Seats.class).findAll();
        My_realm.commitTransaction();

        for(int n=0;n<All_row.size();n++)
        {
            Total_Fare = Total_Fare + All_row.get(n).getFare_after_offer();
        }

        Total_offer_fare  = Total_Fare;

    }

    public void setClickListner()
    {
        header_iv.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                activity_dismiss();
            }
        });

        proceed_to_book_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                try  {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
                String validate_result = validate();
                if(validate_result.equals("success"))
                {
                    if(isNetworkConnected())
                    {
                        BookingTicket bt = new BookingTicket();
                        bt.execute();
                    }
                    else
                    {
                        Global_Travel.showAlertDialog(Activity_Passenger_Details.this,getResources().getString(R.string.internet_connection_error_title),getResources().getString(R.string.internet_connection_error_message),"Ok");
                    }

                    //showAlertDialog("Alert","this is success","ok");
                }
                else if(validate_result.equals("boarding_point"))
                {
                    Global_Travel.showAlertDialog(Activity_Passenger_Details.this,getResources().getString(R.string.validating_error_title),"Please select a boarding point !","Ok");
                }
                else if(validate_result.contains("Passenger"))
                {
                    Global_Travel.showAlertDialog(Activity_Passenger_Details.this,getResources().getString(R.string.validating_error_title),"Complete "+validate_result+" information","Ok");
                }
                else if(validate_result.equals("name"))
                {
                    Global_Travel.showAlertDialog(Activity_Passenger_Details.this,getResources().getString(R.string.validating_error_title),"Please insert contact name !","Ok");
                }
                else if(validate_result.equals("phone"))
                {
                    Global_Travel.showAlertDialog(Activity_Passenger_Details.this,getResources().getString(R.string.validating_error_title),"Please insert a 10 disit mobile no !","Ok");
                }
                else if(validate_result.equals("email"))
                {
                    Global_Travel.showAlertDialog(Activity_Passenger_Details.this,getResources().getString(R.string.validating_error_title),"Please insert valid email id !","Ok");
                }

            }
        });

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public String validate()
    {


        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        String passenger_validate_result = passangers_validate();

        if(spinner_position == 0)
        {
            return "boarding_point";
        }
        else if(!passenger_validate_result.equals("success"))
        {
            return passenger_validate_result;
        }
        else if(contact_name.getText().toString().trim().length() < 2)
        {
            return "name";
        }
        else if(contact_mobile.getText().toString().trim().length() != 10 || contact_mobile.getText().toString().trim().equals("0000000000"))
        {

            return "phone";
        }
        else if(!contact_email.getText().toString().trim().matches(emailPattern))
        {

            return "email";
        }
        else
        {
            return "success";
        }


    }

    public String passangers_validate()
    {

       for(int i=0;i<Selected_seat_list.size();i++)
        {
            String gender = "";
            RadioGroup rg = (RadioGroup)v.findViewWithTag("radio_group"+(i+1));
            int selected_radio_id = rg.getCheckedRadioButtonId();
            if(Global_Travel.build_type == 0)
            {
                Log.e("vikas","selected_id="+selected_radio_id);
                Log.e("vikas","rg tag="+rg.getTag());
            }

            RadioButton radioButton = (RadioButton) findViewById(selected_radio_id);
            String radiobtnTag = radioButton.getTag().toString();
            if(radiobtnTag.equals("radio_mr"+(i+1)))
            {
                gender = "M";
                if(Global_Travel.build_type == 0)
                {
                    Log.e("vikas","Passenger "+(i+1)+" "+"gender"+":"+"M");
                }

            }
            else
            {
                gender = "F";
                if(Global_Travel.build_type == 0)
                {
                    Log.e("vikas","Passenger "+(i+1)+" "+"gender"+":"+"F");
                }

            }

            EditText p_name = (EditText)v.findViewWithTag("edittext_name"+(i+1));
            EditText p_age = (EditText)v.findViewWithTag("edittext_age"+(i+1));

            if(p_name.getText().toString().trim().equals(""))
            {
                return "Passenger "+(i+1);
            }
            else if(p_age.getText().toString().trim().equals(""))
            {
                return "Passenger "+(i+1);
            }
        }
        return "success";
    }



    private class BookingTicket extends AsyncTask<Void,Void,Void>
    {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress = new ProgressDialog(Activity_Passenger_Details.this);
            progress.setMessage("Booking...");
            progress.setCanceledOnTouchOutside(false);
            progress.setCancelable(false);
            progress.show();

            passenger_list = new JSONArray();
            Contact_email_text = contact_email.getText().toString().trim();
            Contact_mobile_text = contact_mobile.getText().toString().trim();
            Contact_name_text = contact_name.getText().toString().trim();
            for (int j =0;j<Selected_seat_list.size();j++)
            {
                JSONObject jo = new JSONObject();
                EditText p_name = (EditText)v.findViewWithTag("edittext_name"+(j+1));
                EditText p_age = (EditText)v.findViewWithTag("edittext_age"+(j+1));
                try {
                    jo.put("name",p_name.getText().toString().trim());
                    jo.put("age",p_age.getText().toString().trim());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                passenger_list.put(jo);
            }



            passengers_object = new SoapObject(null, "Passengers");


            for (int k=0;k<Selected_seat_list.size();k++)
            {
                String temp_name ="";
                String temp_age = "";
                try
                {
                    temp_name = passenger_list.getJSONObject(k).getString("name");
                    temp_age = passenger_list.getJSONObject(k).getString("age");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    if(Global_Travel.build_type == 0)
                    {
                        Log.e("vikas","error json"+e);
                    }

                }
                String seatno = Selected_seat_list.get(k).getSeatNo();
                String fare = Float.toString(Selected_seat_list.get(k).getFare());
                String seattype ="";
                if(Selected_seat_list.get(k).getIsSleeper())
                {
                    seattype = "Sleeper";
                }
                else
                {
                    seattype = "Seater";
                }

                String gender = "";
                RadioGroup rg = (RadioGroup)v.findViewWithTag("radio_group"+(k+1));
                int selected_radio_id = rg.getCheckedRadioButtonId();

                RadioButton radioButton = (RadioButton) findViewById(selected_radio_id);
                String radiobtnTag = radioButton.getTag().toString();
                if(radiobtnTag.equals("radio_mr"+(k+1)))
                {
                    gender = "M";
                }
                else
                {
                    gender = "F";
                }


                SoapObject passenger = new SoapObject(null, "Passenger");

                PropertyInfo Pro_name =  new PropertyInfo();
                Pro_name.setName("Name");
                Pro_name.setValue(temp_name);
                Pro_name.setType(String.class);
                passenger.addProperty(Pro_name);

                PropertyInfo Pro_age =  new PropertyInfo();
                Pro_age.setName("Age");
                Pro_age.setValue(temp_age);
                Pro_age.setType(Integer.class);
                passenger.addProperty(Pro_age);

                PropertyInfo Pro_gender =  new PropertyInfo();
                Pro_gender.setName("Gender");
                Pro_gender.setValue(gender);
                Pro_gender.setType(String.class);
                passenger.addProperty(Pro_gender);

                PropertyInfo Pro_seatno =  new PropertyInfo();
                Pro_seatno.setName("SeatNo");
                Pro_seatno.setValue(seatno);
                Pro_seatno.setType(String.class);
                passenger.addProperty(Pro_seatno);

                PropertyInfo Pro_fare =  new PropertyInfo();
                Pro_fare.setName("Fare");
                Pro_fare.setValue(fare);
                Pro_fare.setType(Double.class);
                passenger.addProperty(Pro_fare);

                PropertyInfo Pro_seatType =  new PropertyInfo();
                Pro_seatType.setName("SeatType");
                Pro_seatType.setValue(seattype);
                Pro_seatType.setType(String.class);
                passenger.addProperty(Pro_seatType);


                passengers_object.addSoapObject(passenger);

            }

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (soapresult_detail != null) {

                if(((SoapObject)soapresult_detail.getProperty("Response")).getPrimitiveProperty("IsSuccess").toString().equals("true"))
                {
                    try {
                        message = soapresult_detail.toString();
                        if(Global_Travel.build_type == 0)
                        {
                            Log.e("vikas",message);
                        }

                        String holdkey = soapresult_detail.getPrimitiveProperty("HoldKey").toString();
                       // String totalfare = soapresult_detail.getPrimitiveProperty("TotalFare").toString();
                        String totalfare = Float.toString(Total_offer_fare);
                        String boarding_point_name = pickup_place_detail_list.get(spinner_position - 1).getPickupName();
                        String boarding_point_address = pickup_place_detail_list.get(spinner_position-1).getAddress();
                        String boarding_point_phone = pickup_place_detail_list.get(spinner_position-1).getContact();
                        String boarding_point_landmark = pickup_place_detail_list.get(spinner_position-1).getLandmark();
                        String time = getTime(pickup_place_detail_list.get(spinner_position - 1).getPkpTime());

                        Intent i = new Intent(Activity_Passenger_Details.this,Activity_review_itinerary.class);
                        i.putExtra("HoldKey",holdkey);
                        i.putExtra("TotalFare",totalfare);
                        i.putExtra("BoardingPoint",boarding_point_name);
                        i.putExtra("BoardingTime",time);
                        i.putExtra("cancellation_data",cancellation_data_string);
                        i.putExtra("contact_name",Contact_name_text);
                        i.putExtra("contact_email",Contact_email_text);
                        i.putExtra("contact_phone",Contact_mobile_text);
                        i.putExtra("boarding_point_address",boarding_point_address);
                        i.putExtra("boarding_point_phone",boarding_point_phone);
                        i.putExtra("boarding_point_landmark",boarding_point_landmark);
                        startActivity(i);
                        Activity_Select_Seats.selected_seat_activity.finish();
                        Activity_Passenger_Details.this.finish();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Global_Travel.showAlertDialog(Activity_Passenger_Details.this,getResources().getString(R.string.validating_error_title),((SoapObject)soapresult_detail.getProperty("Response")).getPrimitivePropertyAsString("Message"),"Ok");
                }



            }
            else
            {
                Global_Travel.showAlertDialog(Activity_Passenger_Details.this,getResources().getString(R.string.validating_error_title),getResources().getString(R.string.slow_internet_error),"Ok");
            }

            progress.dismiss();
        }


        @Override
        protected Void doInBackground(Void... voids) {

            //TODO : HARSH Implement new API

            SoapObject request = new SoapObject("", "");

            //SoapObject request = new SoapObject(Constants.GLOBEL_NAMESPACE, Constants.METHOD_HoldSeatsForSchedule);

            SoapObject sa = new SoapObject(null, "Authentication");


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

            request.addSoapObject(sa);

            PropertyInfo scheduleid = new PropertyInfo();
            scheduleid.setName("RouteScheduleId");
            scheduleid.setValue(bus_id);
            scheduleid.setType(Integer.class);
            request.addProperty(scheduleid);


            PropertyInfo journeydate = new PropertyInfo();
            journeydate.setName("JourneyDate");
            journeydate.setValue(TRAVEL_DATA.JOURNEY_DATE);
            journeydate.setType(String.class);
            request.addProperty(journeydate);

            PropertyInfo pickupid = new PropertyInfo();
            pickupid.setName("PickUpID");
            pickupid.setValue(pickup_id);
            pickupid.setType(Integer.class);
            request.addProperty(pickupid);


            SoapObject contact_information = new SoapObject(null, "ContactInformation");

            PropertyInfo customername = new PropertyInfo();
            customername.setName("CustomerName");
            customername.setValue(Contact_name_text);
            customername.setType(String.class);
            contact_information.addProperty(customername);


            PropertyInfo email = new PropertyInfo();
            email.setName("Email");
            email.setValue(Contact_email_text);
            email.setType(String.class);
            contact_information.addProperty(email);

            PropertyInfo Phone = new PropertyInfo();
            Phone.setName("Phone");
            Phone.setValue(Contact_mobile_text);
            Phone.setType(String.class);
            contact_information.addProperty(Phone);


            PropertyInfo mobile = new PropertyInfo();
            mobile.setName("Mobile");
            mobile.setValue(Contact_mobile_text);
            mobile.setType(String.class);
            contact_information.addProperty(mobile);


            request.addSoapObject(contact_information);
            request.addSoapObject(passengers_object);

            if(Global_Travel.build_type == 0)
            {
                Log.e("vikas envolop", request.toString());
            }


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.implicitTypes = true;
            envelope.setAddAdornments(false);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            if (Global_Travel.build_type == 0)
            {
                Log.e("vikas envolop",envelope.toString());
            }


//               HttpTransportSE httpTransport = new HttpTransportSE(Constants.GLOBEL_URL);
//               httpTransport.debug = true;

//               try {
//                   httpTransport.call(Constants.GLOBEL_NAMESPACE + Constants.METHOD_HoldSeatsForSchedule, envelope);
//               } catch (HttpResponseException e) {
//                   // TODO Auto-generated catch block
//                   e.printStackTrace();
//                   if(Global_Travel.build_type == 0)
//                   {
//                       Log.e("vikas","error http:"+e);
//                   }
//
//               } catch (IOException e) {
//                   // TODO Auto-generated catch block
//                   e.printStackTrace();
//               } catch (XmlPullParserException e) {
//                   // TODO Auto-generated catch block
//                   e.printStackTrace();
//               } //send request
               // SoapObject result = null;
               soapresult_detail = null;

               try {


                   soapresult_detail = (SoapObject) envelope.getResponse();

               } catch (SoapFault e) {
                   // TODO Auto-generated catch block
                   e.printStackTrace();
               }






            return null;
        }
    }





    public void activity_dismiss()
    {
        this.finish();
        overridePendingTransition(R.anim.anim_none, R.anim.anim_out);
    }

    public void setLayout_passanger()
    {
        LinearLayout passengers = (LinearLayout)findViewById(R.id.passengers);
        for(int i=0;i<Selected_seat_list.size();i++)
        {

            LayoutInflater inflater = (LayoutInflater)Activity_Passenger_Details.this.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);

           View v =  inflater.inflate(R.layout.passenger_single,null);

           TextView textView_label =  (TextView)v.findViewById(R.id.label_textview);
            if(i > 0)
            {
                textView_label.setText("Passenger"+" "+(i+1));
            }
            else
            {
                textView_label.setText("Passenger");
            }


            RadioGroup rg = (RadioGroup)v.findViewById(R.id.radioGroup);
            rg.setTag("radio_group"+(i+1));


            RadioButton mr = (RadioButton)v.findViewById(R.id.radio_mr);
            mr.setId((Constants.RADIO_MR +(i+1)));
            mr.setTag("radio_mr"+(i+1));

            RadioButton mrs = (RadioButton)v.findViewById(R.id.radio_mrs);
            mrs.setId(Constants.RADIO_MRS+(i+1));
            mrs.setTag("radio_mrs"+(i+1));

            RadioButton ms = (RadioButton)v.findViewById(R.id.radio_ms);
            ms.setId(Constants.RADIO_MS+(i+1));
            ms.setTag("radio_ms"+(i+1));

            if(Selected_seat_list.get(i).getGender().equals("F"))
            {
                rg.check(Constants.RADIO_MRS+(i+1));
                mr.setClickable(false);
                mr.setOnTouchListener(new View.OnTouchListener(){

                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        Global_Travel.showAlertDialog(Activity_Passenger_Details.this,getResources().getString(R.string.validating_error_title),"Seat reserved for ladies","Ok");
                        return false;

                    }
                });

            }
            else
            {
                rg.check(Constants.RADIO_MR+(i+1));
            }



            EditText name_edittext = (EditText)v.findViewById(R.id.edittext_name);
            name_edittext.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    if(Global_Travel.build_type == 0)
                    {
                        Log.e("vikas",""+view.getTag());
                    }

                }
            });
            name_edittext.setTag("edittext_name"+(i+1));
            if(i == 0)
            {
                if(session_manager.isLoggedIn())
                {
                   // name_edittext.setText(session_manager.getname());
                }
            }

            EditText age_edittext = (EditText)v.findViewById(R.id.edittext_age);
            age_edittext.setTag("edittext_age"+(i+1));
            age_edittext.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    if(Global_Travel.build_type == 0)
                    {
                        Log.e("vikas",""+view.getTag());
                    }

                }
            });


            passengers.addView(v);



        }
    }







    public void set_boarding_points()
    {
        if(Global_Travel.build_type == 0)
        {
            Log.e("vikas","boarding call");
        }

        final ArrayList<String> boarding_list = new ArrayList<String>();
        boarding_list.add("Select Boarding Point..");
        for (int i=0;i<pickup_place_detail_list.size();i++)
        {
            String time = getTime(pickup_place_detail_list.get(i).getPkpTime());
            boarding_list.add(time+" "+pickup_place_detail_list.get(i).getPickupName());

            if(Global_Travel.build_type == 0)
            {
                Log.e("vikas",time);
                Log.e("vikas",pickup_place_detail_list.get(i).getPickupName());
            }

        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner_single_row, boarding_list);

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_style);
        boarding_point_spinner.setAdapter(adapter);



        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            ListPopupWindow popupWindow = (ListPopupWindow) popup.get(boarding_point_spinner);
            popupWindow.setHeight(400);
            popupWindow.setContentWidth(ListPopupWindow.WRAP_CONTENT);


        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
        }


        boarding_point_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(position != 0)
                {
                    spinner_position = position;
                    if(position != 0)
                    {
                        pickup_id = pickup_place_detail_list.get(position - 1).getPickupCode();
                    }

                    TextView tv = (TextView) findViewById(R.id.spinnerTarget_list);
                    if(tv != null)
                    {
                        tv.setTextColor(Color.BLACK);
                    }

                }
                else
                {
                    spinner_position = position;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


    }







    public String getTime(String time)
    {
        String final_time="";
        time = time.substring(11,16);
        String post="";
        int hour = Integer.parseInt(time.substring(0,2));
        String min = time.substring(2,time.length());

        if(hour > 12)
        {
            hour = hour%12;
            post = "PM";
        }
        else if(hour == 0)
        {
            hour = 12;
            post = "AM";
        }
        else if(hour == 12)
        {
            post = "PM";
        }
        else
        {
            post = "AM";
        }

        if(hour >9)
        {
            final_time = hour+min+" "+post;
        }
        else
        {
            final_time = "0"+hour+min+" "+post;
        }

        return final_time;

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        overridePendingTransition(R.anim.anim_none, R.anim.anim_out);



    }
}
