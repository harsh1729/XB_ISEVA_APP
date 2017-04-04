package com.iseva.app.source.travel;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.iseva.app.source.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class Activity_show_detail_booked_ticket extends Activity {

    private Session_manager session_manager;
    private TextView header_tv;
    private ImageView header_iv;
    private TextView header_tv_second;

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

    private ProgressDialog progress;

    private SoapObject soapresult_ticket_detail;
    private SoapObject soapresult_iscancelable;
    private SoapObject soapresult_cancel_ticket;

    private String pnr_no_txt;
    private String ticket_no_txt;
    private String iscancelable;

    private String total_fare;
    private String extra_charge;

    private int volley_timeout = 15000;

    private String refund_amount;
    private String payu_payment_id;
    private String cancel_charge_percentage;

    public static int cancel_ticket = 0;

    RequestQueue requestQueue_globel ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail_booked_ticket);

        initialize();
        setclicklistner();
        if(isNetworkConnected())
        {
            TicketDetail td = new TicketDetail();
            td.execute();

            Iscancelable ic = new Iscancelable();
            ic.execute();
            //callserver();
        }
        else
        {
            Global.showAlertDialog(Activity_show_detail_booked_ticket.this,getResources().getString(R.string.internet_connection_error_title),getResources().getString(R.string.internet_connection_error_message),"Ok");
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

        session_manager = new Session_manager(this);
        header_tv = (TextView)findViewById(R.id.header_text);
        header_iv = (ImageView)findViewById(R.id.header_back_button);

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


 private class Iscancelable extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progress.dismiss();
            if (soapresult_iscancelable != null) {
                String Is_success = ((SoapObject) soapresult_iscancelable.getProperty("Response")).getPrimitiveProperty("IsSuccess").toString();
                if (Is_success.equals("true")) {
                    //Toast.makeText(Activity_review_itinerary.this,soapresult_detail.toString(),Toast.LENGTH_LONG).show();
                    try {

                        Log.e("vikas", soapresult_iscancelable.toString());
                        if(soapresult_iscancelable.getPrimitivePropertyAsString("IsCancellable").equals("true"))
                        {
                            /*cancel_ticket_total_fare_tv.setText(soapresult_iscancelable.getPrimitivePropertyAsString("TotalFare"));*/
                            cancel_ticket_total_fare_tv.setText(total_fare);
                            cancel_charge_percentage = soapresult_iscancelable.getPrimitivePropertyAsString("ChargePct");
                            cancel_ticket_charge_percentage_tv.setText(cancel_charge_percentage+"%");
                            Float charge = (Float.parseFloat(total_fare)*Float.parseFloat(cancel_charge_percentage))/100;
                            Float refund = Float.parseFloat(total_fare)- charge;
                            cancel_ticket_refund_amount_tv.setText(""+refund);
                           /* refund_amount = soapresult_iscancelable.getPrimitivePropertyAsString("RefundAmount");*/
                            refund_amount = Float.toString(refund);
                            ticket_detail_cancel_btn_layout.setVisibility(View.VISIBLE);

                        }
                        else
                        {
                            ticket_detail_cancel_btn_layout.setVisibility(View.GONE);

                        }






                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
                else
                {
                    Global.showAlertDialog(Activity_show_detail_booked_ticket.this,getResources().getString(R.string.validating_error_title),((SoapObject)soapresult_iscancelable.getProperty("Response")).getPrimitivePropertyAsString("Message"),"Ok");
                    Toast.makeText(Activity_show_detail_booked_ticket.this, ((SoapObject) soapresult_iscancelable.getProperty("Response")).getPrimitiveProperty("Message").toString(), Toast.LENGTH_LONG).show();
                }

            } else {
                Global.showAlertDialog(Activity_show_detail_booked_ticket.this,getResources().getString(R.string.validating_error_title),"Some error accured please try again !","Ok");
                Toast.makeText(Activity_show_detail_booked_ticket.this, "Server Error !", Toast.LENGTH_LONG).show();
            }


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();




        }

        @Override
        protected Void doInBackground(Void... arg0) {

            SoapObject request = new SoapObject(Constants.GLOBEL_NAMESPACE, Constants.METHOD_ISCANCELABLE);


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

            PropertyInfo pnr_no = new PropertyInfo();
            pnr_no.setName("PNRNo");
            pnr_no.setValue(pnr_no_txt);
            userkey.setType(String.class);
            request.addProperty(pnr_no);

            PropertyInfo ticket_no = new PropertyInfo();
            ticket_no.setName("TicketNo");
            ticket_no.setValue(ticket_no_txt);
            userkey.setType(String.class);
            request.addProperty(ticket_no);
            Log.e("vikas request print",request.toString());





            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.implicitTypes = true;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransport = new HttpTransportSE(Constants.GLOBEL_URL);
            httpTransport.debug =true;


            try {
                httpTransport.call(Constants.GLOBEL_NAMESPACE+Constants.METHOD_ISCANCELABLE, envelope);
            } catch (HttpResponseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            soapresult_iscancelable = null;

            try {
                soapresult_iscancelable  = (SoapObject)envelope.getResponse();

            } catch (SoapFault e) {

                e.printStackTrace();
            }



            return null;
        }
    }


    private class TicketDetail extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (soapresult_ticket_detail != null) {
                String Is_success = ((SoapObject) soapresult_ticket_detail.getProperty("Response")).getPrimitiveProperty("IsSuccess").toString();
                if (Is_success.equals("true")) {
                    //Toast.makeText(Activity_review_itinerary.this,soapresult_detail.toString(),Toast.LENGTH_LONG).show();
                    try {

                        Log.e("vikas", soapresult_ticket_detail.toString());
                        String booking_date = change_date_form(soapresult_ticket_detail.getPrimitivePropertyAsString("JourneyDate"));
                        String from_city = soapresult_ticket_detail.getPrimitivePropertyAsString("FromCityName");
                        from_city = from_city.substring(0,1).toUpperCase() + from_city.substring(1);

                        String to_city = soapresult_ticket_detail.getPrimitivePropertyAsString("ToCityName");
                        to_city = to_city.substring(0,1).toUpperCase() + to_city.substring(1);

                        String boarding_time = gettime (((SoapObject)soapresult_ticket_detail.getProperty("Pickup")).getPrimitivePropertyAsString("PickupTime"));

                        String boarding_name = ((SoapObject)soapresult_ticket_detail.getProperty("Pickup")).getPrimitivePropertyAsString("PickupName");
                        String company_name = soapresult_ticket_detail.getPrimitivePropertyAsString("OperatorName");
                        String bus_label = soapresult_ticket_detail.getPrimitivePropertyAsString("BusTypeName");
                        String seat_no = "";
                        String passenger_name = ((SoapObject)soapresult_ticket_detail.getProperty("ContactInfo")).getPrimitivePropertyAsString("CustomerName");
                        String boarding_address = ((SoapObject)soapresult_ticket_detail.getProperty("Pickup")).getPrimitivePropertyAsString("Address");
                        String landmark =((SoapObject)soapresult_ticket_detail.getProperty("Pickup")).getPrimitivePropertyAsString("Landmark");
                        String boarding_mobile = ((SoapObject)soapresult_ticket_detail.getProperty("Pickup")).getPrimitivePropertyAsString("Phone");
                        String dropping_name = soapresult_ticket_detail.getPrimitivePropertyAsString("DropOff");
                        String ticket_no = soapresult_ticket_detail.getPrimitivePropertyAsString("TicketNo");
                        String total_fare =soapresult_ticket_detail.getPrimitivePropertyAsString("TotalFare");
                        String pnr_no = soapresult_ticket_detail.getPrimitivePropertyAsString("PNRNo");

                        for(int i=0;i<((SoapObject)soapresult_ticket_detail.getProperty("Passengers")).getPropertyCount();i++)
                        {
                            if(i == 0)
                            {
                                seat_no = seat_no+((SoapObject)((SoapObject)soapresult_ticket_detail.getProperty("Passengers")).getProperty(i)).getPrimitivePropertyAsString("SeatNo");
                            }
                            else
                            {
                                seat_no = seat_no+","+((SoapObject)((SoapObject)soapresult_ticket_detail.getProperty("Passengers")).getProperty(i)).getPrimitivePropertyAsString("SeatNo");
                            }

                        }

                        ticket_detail_booking_date_tv.setText(booking_date);
                        ticket_detail_from_city_tv.setText(from_city);
                        ticket_detail_to_city_tv.setText(to_city);
                        ticket_detail_boarding_time.setText(boarding_time);
                        ticket_detail_boarding_point_name_tv.setText(boarding_name);
                        ticket_detail_company_name_tv.setText(company_name);
                        ticket_detail_bus_label_tv.setText(bus_label);
                        ticket_detail_seat_no_tv.setText(seat_no);
                        ticket_detail_passenger_name_tv.setText(passenger_name);
                        ticket_detail_boarding_point_address_tv.setText(boarding_address);
                        ticket_detail_boarding_point_landmark_tv.setText(landmark);
                        ticket_detail_boarding_point_mobile_tv.setText(boarding_mobile);
                        ticket_detail_dropping_point_tv.setText(dropping_name);
                        ticket_detail_ticket_no_tv.setText(ticket_no);
                        ticket_detail_total_fare.setText(total_fare);
                        ticket_detail_pnr_no_tv.setText(pnr_no);






                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
                else
                {
                    Log.e("vikas", soapresult_ticket_detail.toString());
                    Global.showAlertDialog(Activity_show_detail_booked_ticket.this,getResources().getString(R.string.validating_error_title),((SoapObject)soapresult_ticket_detail.getProperty("Response")).getPrimitivePropertyAsString("Message"),"Ok");
                }

            } else {
                Global.showAlertDialog(Activity_show_detail_booked_ticket.this,getResources().getString(R.string.validating_error_title),"Some error accured please try again !","Ok");
            }


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress = new ProgressDialog(Activity_show_detail_booked_ticket.this);
            progress.setMessage("Please wait...");
            progress.setCanceledOnTouchOutside(false);
            progress.setCancelable(false);

            progress.show();


        }

        @Override
        protected Void doInBackground(Void... arg0) {

            SoapObject request = new SoapObject(Constants.GLOBEL_NAMESPACE, Constants.METHOD_TICKET_DETAIL);


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

            PropertyInfo ticket_no = new PropertyInfo();
            ticket_no.setName("TicketNo");
            ticket_no.setValue(ticket_no_txt);
            userkey.setType(String.class);
            request.addProperty(ticket_no);

            PropertyInfo pnr_no = new PropertyInfo();
            pnr_no.setName("strPNRNo");
            pnr_no.setValue(pnr_no_txt);
            userkey.setType(String.class);
            request.addProperty(pnr_no);
            Log.e("vikas request print",request.toString());





            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.implicitTypes = true;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransport = new HttpTransportSE(Constants.GLOBEL_URL);
            httpTransport.debug =true;


            try {
                httpTransport.call(Constants.GLOBEL_NAMESPACE+Constants.METHOD_TICKET_DETAIL, envelope);
            } catch (HttpResponseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            soapresult_ticket_detail = null;

            try {
                soapresult_ticket_detail  = (SoapObject)envelope.getResponse();

            } catch (SoapFault e) {

                e.printStackTrace();
            }



            return null;
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
                    Global.showAlertDialog(Activity_show_detail_booked_ticket.this,getResources().getString(R.string.internet_connection_error_title),getResources().getString(R.string.internet_connection_error_message),"Ok");
                }

            }
        });

        cancel_ticket_ok_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                cancel_ticket = 1;
                cancel_ticket_main_layout.setVisibility(View.GONE);
                if(isNetworkConnected())
                {
                    CancelTicket ct = new CancelTicket();
                    ct.execute();
                }
                else
                {
                    Global.showAlertDialog(Activity_show_detail_booked_ticket.this,getResources().getString(R.string.internet_connection_error_title),getResources().getString(R.string.internet_connection_error_message),"Ok");
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
                Constants.update_ticket_status, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();

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
                Constants.refund_amount, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();


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
    private class CancelTicket extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progress.dismiss();
            if (soapresult_cancel_ticket != null) {
                String Is_success = ((SoapObject) soapresult_cancel_ticket.getProperty("Response")).getPrimitiveProperty("IsSuccess").toString();
                if (Is_success.equals("true")) {
                    //Toast.makeText(Activity_review_itinerary.this,soapresult_detail.toString(),Toast.LENGTH_LONG).show();
                    try {

                        Log.e("vikas",soapresult_cancel_ticket.toString());
                        ticket_detail_cancel_btn_layout.setVisibility(View.GONE);
                        updateTicketStatus();
                        refund_fair();

                        TextView title_tv = new TextView(Activity_show_detail_booked_ticket.this);
                        title_tv.setPadding(0,getResources().getDimensionPixelSize(R.dimen.padding_margin_10),0,0);
                        title_tv.setTextColor(ContextCompat.getColor(Activity_show_detail_booked_ticket.this,R.color.black));
                        title_tv.setTextSize(getResources().getDimension(R.dimen.text_size_mediam));
                        title_tv.setGravity(Gravity.CENTER);
                        title_tv.setText("Message");

                        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_show_detail_booked_ticket.this);
                        builder.setCustomTitle(title_tv)
                                .setMessage("Your ticket successfully cancelled")
                                .setCancelable(false)
                                .setNegativeButton("Ok",new DialogInterface.OnClickListener() {
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



                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
                else
                {
                    Toast.makeText(Activity_show_detail_booked_ticket.this, ((SoapObject) soapresult_cancel_ticket.getProperty("Response")).getPrimitiveProperty("Message").toString(), Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(Activity_show_detail_booked_ticket.this, "Server Error !", Toast.LENGTH_LONG).show();
            }


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progress = new ProgressDialog(Activity_show_detail_booked_ticket.this);
            progress.setMessage("Please wait...");
            progress.setCanceledOnTouchOutside(false);
            progress.setCancelable(false);

            progress.show();


        }

        @Override
        protected Void doInBackground(Void... arg0) {

            SoapObject request = new SoapObject(Constants.GLOBEL_NAMESPACE, Constants.METHOD_CANCEL_TICKET);


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

            PropertyInfo pnr_no = new PropertyInfo();
            pnr_no.setName("PNRNo");
            pnr_no.setValue(pnr_no_txt);
            userkey.setType(String.class);
            request.addProperty(pnr_no);

            PropertyInfo ticket_no = new PropertyInfo();
            ticket_no.setName("TicketNo");
            ticket_no.setValue(ticket_no_txt);
            userkey.setType(String.class);
            request.addProperty(ticket_no);
            Log.e("vikas request print",request.toString());





            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.implicitTypes = true;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransport = new HttpTransportSE(Constants.GLOBEL_URL);
            httpTransport.debug =true;


            try {
                httpTransport.call(Constants.GLOBEL_NAMESPACE+Constants.METHOD_CANCEL_TICKET, envelope);
            } catch (HttpResponseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            soapresult_cancel_ticket = null;

            try {
                soapresult_cancel_ticket  = (SoapObject)envelope.getResponse();

            } catch (SoapFault e) {

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

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        overridePendingTransition(R.anim.anim_none, R.anim.anim_out);



    }

   /* public void showAlertDialog(String title,String message,String buttonlabel)
    {
        TextView title_tv = new TextView(this);
        title_tv.setPadding(0,getResources().getDimensionPixelSize(R.dimen.padding_margin_10),0,0);
        title_tv.setTextColor(ContextCompat.getColor(Activity_show_detail_booked_ticket.this,R.color.black));
        title_tv.setTextSize(getResources().getDimension(R.dimen.text_size_mediam));
        title_tv.setGravity(Gravity.CENTER);
        title_tv.setText(title);

        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_show_detail_booked_ticket.this);
        builder.setCustomTitle(title_tv)
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton(buttonlabel,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        activity_dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button b = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        b.setLayoutParams(lp);
        b.setBackgroundResource(R.drawable.btn_background);
        b.setTextColor(ContextCompat.getColor(Activity_show_detail_booked_ticket.this, R.color.app_white));
    }*/
}
