package com.iseva.app.source.travel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.iseva.app.source.Custom_VolleyAppController;
import com.iseva.app.source.Custom_VolleyObjectRequest;
import com.iseva.app.source.Globals;
import com.iseva.app.source.R;
import com.iseva.app.source.travel.Constants.URL_XB;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class Activity_show_detail_booked_ticket extends Activity_Parent_Travel {

    private Session_manager session_manager;
    private TextView header_tv;
    private ImageView header_iv;
    private TextView header_tv_second;

    LinearLayout booking_ticket_detail_main_layout;
    LinearLayout booking_ticket_detail_short_layout;
    TextView booking_ticket_detail_short_layout_date;
    TextView booking_ticket_detail_short_layout_from_city;
    TextView booking_ticket_detail_short_layout_to_city;
    TextView booking_ticket_detail_short_layout_ticket_no;
    TextView booking_ticket_detail_short_layout_pnr_no;

    TextView ticket_detail_booking_date_tv;
    TextView ticket_detail_from_city_tv;
    TextView ticket_detail_to_city_tv;
    TextView ticket_detail_boarding_time;
    TextView ticket_detail_boarding_point_name_tv;
    TextView ticket_detail_company_name_tv;
    TextView ticket_detail_bus_label_tv;
    TextView ticket_detail_seat_no_tv;
    TextView ticket_detail_passenger_name_tv;
    TextView ticket_detail_boarding_point_address_tv;
    TextView ticket_detail_boarding_point_landmark_tv;
    TextView ticket_detail_boarding_point_mobile_tv;
    TextView ticket_detail_dropping_point_tv;
    TextView ticket_detail_total_fare;
    TextView ticket_detail_ticket_no_tv;
    TextView ticket_detail_pnr_no_tv;

    Button ticket_detail_ticket_cancel_btn;
    LinearLayout ticket_detail_cancel_btn_layout;

    private LinearLayout cancel_ticket_main_layout;
    private TextView cancel_ticket_total_fare_tv;
    private TextView cancel_ticket_charge_percentage_tv;
    private TextView cancel_ticket_refund_amount_tv;
    private TextView cancel_ticket_cancel_btn;
    private TextView cancel_ticket_ok_btn;



    private String pnr_no_txt;
    private String ticket_no_txt;
    private String iscancelable;

    private String total_fare;
    private String extra_charge;

    private String booked_date;
    private String from_city;
    private String to_city;

    private int volley_timeout = 15000;

    private String refund_amount;
    private String payu_payment_id;
    private String cancel_charge_percentage;

    public static int cancel_ticket = 0;

    RequestQueue requestQueue_globel ;

    private String seatsNo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail_booked_ticket);

        initialize();
        setclicklistner();
        if(isNetworkConnected(true))
        {

            getBookTicketDetails();

        }


    }

    public void initialize()
    {
        Intent i = getIntent();
        pnr_no_txt = i.getStringExtra("pnr_no");
        ticket_no_txt = i.getStringExtra("ticket_no");

        iscancelable = i.getStringExtra("iscancelable");
        payu_payment_id = i.getStringExtra("payu_payment_id");
        total_fare = i.getStringExtra("total_fare");
        extra_charge = i.getStringExtra("extra_charge");

        booked_date=change_date_form(i.getStringExtra("booked_date"));
        from_city=i.getStringExtra("from_city");
        to_city=i.getStringExtra("to_city");


        /*{"success":true,"data":{"HoldId":19857554,
                "TotalFare":193.42,"TicketNo":"5017182280","PNRNo":"103811648-1022157"}}*/

//        pnr_no_txt = "103857280-1025528";
//        ticket_no_txt = "5017182299";
//        total_fare = "407.2";
//        iscancelable = "0";

        session_manager = new Session_manager(this);
        header_tv = (TextView)findViewById(R.id.header_text);
        header_iv = (ImageView)findViewById(R.id.header_back_button);

        booking_ticket_detail_main_layout=(LinearLayout)findViewById(R.id.activity_show_deatail_booked_main_layout);
        booking_ticket_detail_short_layout=(LinearLayout)findViewById(R.id.confirm_ticket_short_detail_layout_on_gds_error);
        booking_ticket_detail_short_layout_date = (TextView)findViewById(R.id.activity_ticket_confirmed_short_layout_date_tv);
        booking_ticket_detail_short_layout_from_city = (TextView)findViewById(R.id.activity_ticket_confirmed_short_layout_from_city_tv);
        booking_ticket_detail_short_layout_to_city = (TextView)findViewById(R.id.activity_ticket_confirmed_short_layout_to_city_tv);
        booking_ticket_detail_short_layout_pnr_no = (TextView)findViewById(R.id.activity_ticket_confirmed_short_layout_pnr_no_tv);
        booking_ticket_detail_short_layout_ticket_no = (TextView)findViewById(R.id.activity_ticket_confirmed_short_layout_ticket_no_tv);



        ticket_detail_booking_date_tv = (TextView)findViewById(R.id.activity_ticket_detail_booking_date_tv);
        ticket_detail_from_city_tv = (TextView)findViewById(R.id.activity_ticket_detail_from_city_tv);
        ticket_detail_to_city_tv = (TextView)findViewById(R.id.activity_ticket_detail_to_city_tv);
        ticket_detail_boarding_time = (TextView)findViewById(R.id.activity_ticket_detail_boarding_name_tv);
        ticket_detail_boarding_point_name_tv = (TextView)findViewById(R.id.activity_ticket_detail_boarding_name_tv);
        ticket_detail_company_name_tv = (TextView)findViewById(R.id.activity_ticket_detail_company_name_tv);
        ticket_detail_bus_label_tv = (TextView)findViewById(R.id.activity_ticket_detail_bus_label_tv);
        ticket_detail_seat_no_tv = (TextView)findViewById(R.id.activity_ticket_detail_seat_no_tv);
        ticket_detail_passenger_name_tv =(TextView)findViewById(R.id.activity_ticket_detail_passenger_name_tv);
        ticket_detail_boarding_point_address_tv =(TextView)findViewById(R.id.activity_ticket_detail_boarding_address_tv);
        ticket_detail_boarding_point_landmark_tv = (TextView)findViewById(R.id.activity_ticket_detail_boarding_landmark_tv);
        ticket_detail_boarding_point_mobile_tv = (TextView)findViewById(R.id.activity_ticket_detail_boarding_mobile_tv);
        ticket_detail_dropping_point_tv =(TextView)findViewById(R.id.activity_ticket_detail_dropping_point_tv);
        ticket_detail_total_fare = (TextView)findViewById(R.id.activity_ticket_detail_total_fare_tv);
        ticket_detail_ticket_no_tv =(TextView)findViewById(R.id.activity_ticket_detail_ticket_no_tv);
        ticket_detail_pnr_no_tv  = (TextView)findViewById(R.id.activity_ticket_detail_pnr_no_tv);

        cancel_ticket_main_layout = (LinearLayout)findViewById(R.id.activity_show_booked_ticket_cancel_ticket_main_layout);
        cancel_ticket_total_fare_tv = (TextView)findViewById(R.id.activity_show_booked_ticket_cancel_ticket_total_fare_tv);
        cancel_ticket_charge_percentage_tv = (TextView)findViewById(R.id.activity_show_booked_ticket_cancel_ticket_charge_percentage_tv);
        cancel_ticket_refund_amount_tv = (TextView)findViewById(R.id.activity_show_booked_ticket_cancel_ticket_refund_amount_tv);
        cancel_ticket_cancel_btn = (TextView)findViewById(R.id.activity_show_booked_ticket_cancel_ticket_cancel_btn);
        cancel_ticket_ok_btn = (TextView)findViewById(R.id.activity_show_booked_ticket_cancel_ticket_ok_btn);

        ticket_detail_ticket_cancel_btn = (Button)findViewById(R.id.activity_ticket_detail_cancel_btn);
        ticket_detail_cancel_btn_layout = (LinearLayout)findViewById(R.id.activity_ticket_detail_cancel_btn_layout);
        header_tv.setText("Details");

        requestQueue_globel = Volley.newRequestQueue(Activity_show_detail_booked_ticket.this);
        if(iscancelable.equals("0"))
        {
            ticket_detail_cancel_btn_layout.setVisibility(View.GONE);
        }
        else
        {
            ticket_detail_cancel_btn_layout.setVisibility(View.VISIBLE);
        }
    }



    private void getIsCancellable(){

        if(seatsNo != null && !seatsNo.isEmpty()) {
            String urlIsCancellable = Constants.URL_TY.GET_IS_CANCELLABLE +
                    "?PNRNo=" + pnr_no_txt + "&TicketNo=" + ticket_no_txt + "&seatNos=" + seatsNo;

            Log.i("SUSHIL", "url  !! -> " + urlIsCancellable);
            Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                    Request.Method.GET, urlIsCancellable,
                    null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.i("HARSH", "json Response recieved !! -> " + response);
                    try {
                         Globals.hideLoadingDialog(progress);
                         initTicketCancellableResponse(response);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError err) {
                    Log.i("SUSHIL", "ERROR VolleyError");
                    Globals.hideLoadingDialog(progress);
                }
            }){

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();

                    headers.put("access-token", Global_Travel.TRAVEL_DATA.TOKEN_ID);

                    return headers;
                }
            };
            Custom_VolleyAppController.getInstance().addToRequestQueue(
                    jsonObjectRQST);
        }
    }

    private void getBookTicketDetails(){

        if(pnr_no_txt != null && ticket_no_txt != null) {

            progress = Globals.showLoadingDialog(progress,this,false,"");

            String GET_BOOK_DETAILS_URL = Constants.URL_TY.GET_BOOKING_DETAILS +
                    "?PNR=" + pnr_no_txt + "&TicketNo=" + ticket_no_txt;

            Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                    Request.Method.GET, GET_BOOK_DETAILS_URL,
                    null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.i("HARSH", "json Response recieved !! -> " + response);
                    try {
                       Globals.hideLoadingDialog(progress);
                       initBookDetailsData(response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError err) {
                    Log.i("SUSHIL", "ERROR VolleyError");
                    Globals.hideLoadingDialog(progress);
                }
            }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();

                    headers.put("access-token", Global_Travel.TRAVEL_DATA.TOKEN_ID);

                    return headers;
                }
            };

            Custom_VolleyAppController.getInstance().addToRequestQueue(
                    jsonObjectRQST);
        }
    }



    private void cancelBookedTicket(){


        if(pnr_no_txt != null && ticket_no_txt != null) {

            Map<String,String> paramsMap = new HashMap<>();
            paramsMap.put("PNR",pnr_no_txt);
            paramsMap.put("TicketNo",ticket_no_txt);
            paramsMap.put("SeatNos",seatsNo);

            progress = Globals.showLoadingDialog(progress,this,false,"");

            Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                    Request.Method.POST, Constants.URL_TY.CANCEL_BOOKED_TICKET,
                    paramsMap, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.i("HARSH", "json Response recieved !! -> " + response);
                    try {
                        Globals.hideLoadingDialog(progress);
                        cancelRequestResponse(response);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError err) {
                    Log.i("SUSHIL", "ERROR VolleyError");
                    Globals.hideLoadingDialog(progress);
                }
            }){

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();

                    headers.put("access-token", Global_Travel.TRAVEL_DATA.TOKEN_ID);

                    return headers;
                }
            };

            Custom_VolleyAppController.getInstance().addToRequestQueue(
                    jsonObjectRQST);
        }
    }




    private void cancelRequestResponse(JSONObject response){

        if(response != null){

            try {

                if(response.has("success")) {

                    boolean isSuccess = response.getBoolean("success");

                    ticket_detail_cancel_btn_layout.setVisibility(View.GONE);

                    if (isSuccess) {

                        updateTicketStatus();
                        refund_fair();

                       LayoutInflater inflater = (LayoutInflater) Activity_show_detail_booked_ticket.this.getSystemService
                                (Context.LAYOUT_INFLATER_SERVICE);

                        View v = inflater.inflate(R.layout.textview, null);


                        TextView title_tv = (TextView) v.findViewById(R.id.alert_title);
                        title_tv.setText("Message");

                        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_show_detail_booked_ticket.this);
                        builder.setCustomTitle(title_tv)
                                .setMessage("Your ticket has been cancelled successfully! \nRefund has been initiated and will be credited back to your account.")
                                .setCancelable(false)
                                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
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
                        b.setTextColor(ContextCompat.getColor(Activity_show_detail_booked_ticket.this, R.color.app_white));


                    }else{
                        initErrorToast(false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void initTicketCancellableResponse(JSONObject response){
         if(response != null){
             try{
                 if(response.has("success")) {
                     boolean isSuccess = response.getBoolean("success");

                     if (isSuccess) {


                         JSONObject data = response.getJSONObject("data");

                         if (data != null) {

                             if(data.has("IsCancellable")){
                                 boolean isCancellable = data.getBoolean("IsCancellable");

                                 if(isCancellable){


                                     cancel_ticket_total_fare_tv.setText(total_fare);
                                     cancel_charge_percentage = data.getString("ChargePct");
                                     cancel_ticket_charge_percentage_tv.setText(cancel_charge_percentage+"%");
                                     Float charge = (Float.parseFloat(total_fare)*Float.parseFloat(cancel_charge_percentage))/100;
                                     Float refund = Float.parseFloat(total_fare)- charge;
                                     cancel_ticket_refund_amount_tv.setText(""+refund);

                                     refund_amount = Float.toString(refund);
                                     ticket_detail_cancel_btn_layout.setVisibility(View.VISIBLE);
                                 }else{
                                     ticket_detail_cancel_btn_layout.setVisibility(View.GONE);
                                 }
                             }
                         }
                     }else{
                         initErrorToast(false);
                     }
                 }
             }catch (Exception e){
                 e.printStackTrace();
             }

         }
    }


    private void initBookDetailsData(JSONObject response){
        if(response == null)
            return;
        else{
            try{
                if(response.has("success")){
                    boolean isSuccess = response.getBoolean("success");

                    if(isSuccess){
                        // data is succesfully retrive
                        if(response.has("data")) {
                            JSONObject data = response.getJSONObject("data");

                            if (data != null) {



                                if (data.has("TicketNo") && data.has("PNRNo")) {   // match pnrno or ticket no

                                    String ticketNo = data.getString("TicketNo");
                                    String pnrNo = data.getString("PNRNo");

                                    if (ticketNo != null && pnrNo != null) {

                                        if (ticketNo.equals(ticket_no_txt) && pnrNo.equals(pnr_no_txt)) {


                                            ticket_detail_pnr_no_tv.setText(pnrNo);
                                            ticket_detail_ticket_no_tv.setText(ticketNo);

                                            if (data.has("JourneyDate")) {
                                                String datetime = data.getString("JourneyDate");
                                                datetime = change_date_form(datetime);

                                                if (datetime != null) {
                                                    ticket_detail_booking_date_tv.setText(datetime);
                                                }
                                            }

                                            if (data.has("FromCityName") && data.has("ToCityName")) {

                                                ticket_detail_from_city_tv.setText(data.getString("FromCityName"));
                                                ticket_detail_to_city_tv.setText(data.getString("ToCityName"));
                                            }


                                            /*if (data.has("DepartureDateTime")) {
                                                String datetime = data.getString("DepartureDateTime");
                                                datetime = change_date_form(datetime);
                                                if (datetime != null) {
                                                    ticket_detail_boarding_time.setText(datetime);
                                                }
                                            }*/

                                            if (data.has("CompanyName")) {
                                                String companyName = data.getString("CompanyName");
                                                ticket_detail_company_name_tv.setText(companyName);
                                            }

                                            if (data.has("BusTypeName")) {
                                                ticket_detail_bus_label_tv.setText(data.getString("BusTypeName"));
                                            }

                                            if (data.has("ContactInfo")) {
                                                JSONObject contactInfo = data.getJSONObject("ContactInfo");

                                                if (contactInfo.has("CustomerName")) {

                                                    ticket_detail_passenger_name_tv.setText(contactInfo.getString("CustomerName"));
                                                }
                                            }

                                            if (data.has("PickupInfo")) {

                                                JSONObject pickUpInfo = data.getJSONObject("PickupInfo");

                                                String bordingTime = pickUpInfo.getString("PickupTime");
                                                String boardingName = pickUpInfo.getString("PickupName");
                                                String landmark = pickUpInfo.getString("Landmark");
                                                String boardingAddress = pickUpInfo.getString("Address");
                                                String boardingMobile = pickUpInfo.getString("Phone");


                                                ticket_detail_boarding_time.setText(bordingTime);
                                                ticket_detail_boarding_point_name_tv.setText(boardingName);
                                                ticket_detail_boarding_point_address_tv.setText(boardingAddress);
                                                ticket_detail_boarding_point_landmark_tv.setText(landmark);
                                                ticket_detail_boarding_point_mobile_tv.setText(boardingMobile);

                                            }


                                            if (data.has("Passengers")) {
                                                JSONArray passengersList = data.getJSONArray("Passengers");
                                                seatsNo = "";
                                                if (passengersList != null && passengersList.length() != 0) {

                                                    for (int i = 0; i < passengersList.length(); i++) {

                                                        JSONObject passenger = passengersList.getJSONObject(i);

                                                        if (passenger != null) {

                                                            if (passenger.has("SeatNo")) {

                                                                seatsNo = seatsNo + passenger.getString("SeatNo") + ",";
                                                            }
                                                        }
                                                    }

                                                    if (!seatsNo.isEmpty()) {
                                                        seatsNo = seatsNo.substring(0, seatsNo.length() - 1);
                                                    }

                                                    ticket_detail_seat_no_tv.setText(seatsNo);


                                                }
                                            }


                                            if (data.has("TotalFare")) {

                                                ticket_detail_total_fare.setText(data.getString("TotalFare"));
                                            }

                                            if(data.has("IsCancelled")){   // check if ticket is already cancelled
                                                boolean isCancelled = data.getBoolean("IsCancelled");

                                                if(!isCancelled){
                                                    getIsCancellable();
                                                }
                                            }

                                        }
                                    }
                                }
                            }
                        }

                    }else{
                        // some error on travelyari server
                        initErrorToast(true);
                    }

                }


            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    private void initErrorToast(boolean showShortLayout){

            try {
                Globals.hideLoadingDialog(progress);

                if (showShortLayout) {

                   booking_ticket_detail_main_layout.setVisibility(View.GONE);
                      booking_ticket_detail_short_layout_pnr_no.setText(pnr_no_txt);
                     booking_ticket_detail_short_layout_ticket_no.setText(ticket_no_txt);
                     booking_ticket_detail_short_layout_date.setText(booked_date);
                     booking_ticket_detail_short_layout_from_city.setText(from_city);
                     booking_ticket_detail_short_layout_to_city.setText(to_city);


                    booking_ticket_detail_short_layout.setVisibility(View.VISIBLE);


                }else{
                    // show app error msg
                    Globals.showShortToast(this, "Some error occured, Please try again!");
                }
                //this.finish();
            }catch (Exception e) {
                e.printStackTrace();
            }
    }



    public String change_date_form(String date)
    {
        date.replace("T"," ");
        String[] days ={"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
        String[] months={"January","February","March","April","May","June","July","August","September","October","November","December"};

        String day = date.substring(8,10);
        String month = date.substring(5,7);
        String year = date.substring(0,4);

        int day_int = Integer.parseInt(day);
        int month_int =Integer.parseInt(month);
        int year_int = Integer.parseInt(year);

        Calendar calendar = Calendar.getInstance();
        calendar.set(year_int,month_int-1,day_int);

        int day_of_weak_int = calendar.get(calendar.DAY_OF_WEEK);
        String day_of_weak = days[day_of_weak_int-1];


        //String final_date = day+"-"+months[month_int]+"-"+year+","+day_of_weak;

        String final_date = day_of_weak+", "+day+" "+months[month_int-1]+" "+year;


        return final_date;
    }

    public String gettime(String datetime)
    {
        String time = datetime.substring(11,16);
        return time;
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public void setclicklistner()
    {
        header_iv.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                activity_dismiss();
            }
        });
        ticket_detail_ticket_cancel_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(isNetworkConnected())
                {
                   cancel_ticket_main_layout.setVisibility(View.VISIBLE);
                }
                else
                {
                    Global_Travel.showAlertDialog(Activity_show_detail_booked_ticket.this,getResources().getString(R.string.internet_connection_error_title),getResources().getString(R.string.internet_connection_error_message),"Ok");
                }

            }
        });

        cancel_ticket_ok_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                cancel_ticket = 1;
                cancel_ticket_main_layout.setVisibility(View.GONE);
                if(isNetworkConnected(true))
                {
                    cancelBookedTicket();
                }


            }
        });

        cancel_ticket_cancel_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                cancel_ticket_main_layout.setVisibility(View.GONE);
            }
        });
    }


    public void updateTicketStatus()
    {
        StringRequest update_ticket_status = new StringRequest(Request.Method.POST,
                URL_XB.UPDATE_TICKET_STATUS, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Globals.hideLoadingDialog(progress);

                Toast.makeText(getApplicationContext(),"Error is -->> " + error.toString(),Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("status","cancelled");
                    params.put("pnr_no",pnr_no_txt);
                params.put("ticket_no",ticket_no_txt);
                params.put("cancel_charge",cancel_charge_percentage);

                return params;

            }
        };


       /* RequestQueue requestQueue = Volley.newRequestQueue(Activity_show_detail_booked_ticket.this);*/
        update_ticket_status.setRetryPolicy(new DefaultRetryPolicy(
                volley_timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue_globel.add(update_ticket_status);
    }

    public void refund_fair()
    {
        StringRequest refund_fare = new StringRequest(Request.Method.POST,
                URL_XB.REFUND_AMOUNT, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Globals.hideLoadingDialog(progress);


            }
        }) {
            @Override
            public Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("amount",refund_amount);
                params.put("payment_id",payu_payment_id);


                return params;

            }
        };


       /* RequestQueue requestQueue = Volley.newRequestQueue(Activity_show_detail_booked_ticket.this);*/
        refund_fare.setRetryPolicy(new DefaultRetryPolicy(
                volley_timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue_globel.add(refund_fare);
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
