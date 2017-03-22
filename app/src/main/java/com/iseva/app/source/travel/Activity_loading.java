package com.iseva.app.source.travel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.iseva.app.source.R;
import com.iseva.app.source.Realm_objets.Bus_routes_detail;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.realm.Realm;

import static android.R.attr.format;

public class Activity_loading extends Activity {

    // DotProgressBar dotprogressBar;
    TextView loading_text;
    private String response;
    SoapObject search_bus_result;

    LinearLayout loader_layout;

    private Realm My_realm;
    int count ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        My_realm = Realm.getInstance(getApplicationContext());


        loading_text = (TextView)findViewById(R.id.Loading_text);
        loader_layout = (LinearLayout)findViewById(R.id.activity_loading_loader_layout);
        loading_text.setText(getIntent().getStringExtra("Loading_text"));

        if(isNetworkConnected())
        {
            SearchRoutes sr =new SearchRoutes();
            sr.execute();
        }
        else
        {
            Toast.makeText(Activity_loading.this,R.string.internet_connection_error_title,Toast.LENGTH_LONG).show();
        }

    }


    private class SearchRoutes extends AsyncTask<Void,Void,Void>
    {

        public String getduration(Date startdate, Date endate)
        {


            int min = 0;
            int hour = 0;
            long diff = Math.abs(endate.getTime() - startdate.getTime());

            min = (int) diff/(1000*60);
            hour = min/60;
            min = min%60;
            String min_string = "";
            String  hour_string = "";
            if(hour > 9)
            {
                hour_string = Integer.toString(hour);
            }
            else
            {
                hour_string = "0"+hour;
            }

            if (min > 9)
            {
                min_string = Integer.toString(min);
            }
            else
            {
                min_string = "0"+min;
            }


            String final_string = hour_string+" "+"Hrs"+min_string+" "+"Mins";

            return final_string;
        }

        public Date getdate(String date)
        {
            date = date.replace("T"," ");
            Date d = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            try {
                d = sdf.parse(date);
            } catch (ParseException ex) {

            }



            return d;
        }

        public String gettime(String timedate)
        {
            String finalstring = "";
            timedate = timedate.substring(11,16);
            String post = "AM";
            int hour = Integer.parseInt(timedate.substring(0,2));
            timedate = timedate.substring(2,timedate.length());
            if(hour > 12)
            {
                hour = hour % 12;
                post = "PM";
            }
            else if(hour == 0)
            {
                hour = 12;
                post = "AM";
            }
            else
            {
                post = "AM";
            }
            if(hour >9)
            {
                finalstring = hour + timedate+" "+post;
            }
            else
            {
                finalstring = "0"+hour + timedate+" "+post;
            }
            //finalstring = hour + timedate+" "+post;
            return finalstring;

        }



        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if(search_bus_result != null)
            {
                if(((SoapObject)search_bus_result.getProperty(0)).getPrimitiveProperty("IsSuccess").toString().equals("true"))
                {
                    Intent i = new Intent(Activity_loading.this,Activity_Bus_Routes.class);
                    i.putExtra("response",""+response);
                    startActivity(i);
                    overridePendingTransition(R.anim.anim_in, R.anim.anim_none);
                    activity_dismiss();
                }
                else
                {
                    /*loader_layout.setVisibility(View.GONE);
                    showAlertDialog(getResources().getString(R.string.validating_error_title),((SoapObject)search_bus_result.getProperty("Response")).getPrimitivePropertyAsString("Message"),"Ok");*/
                }




            }
            else
            {
                loader_layout.setVisibility(View.GONE);
                showAlertDialog(getResources().getString(R.string.validating_error_title),"Some Error Accured Please Try Again !","Ok");


            }

            Log.e("vikas",search_bus_result.toString());

        }


        @Override
        protected Void doInBackground(Void... voids) {

            Realm thread_realm = Realm.getInstance(getApplicationContext());

            SoapObject request = new SoapObject(Constants.GLOBEL_NAMESPACE,Constants.METHOD_GET_ROUTES);

            SoapObject sa = new SoapObject(null,"Authentication");
            SoapObject search_request = new SoapObject(null,"SearchRequest");


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






            PropertyInfo pro_Fromcity = new PropertyInfo();
            pro_Fromcity.setName("FromCityId");
            pro_Fromcity.setValue(Search_Buses_Key.From_City_id.trim());
            pro_Fromcity.setType(Integer.class);
            search_request.addProperty(pro_Fromcity);



            PropertyInfo pro_Tocity = new PropertyInfo();
            pro_Tocity.setName("ToCityId");
            pro_Tocity.setValue(Search_Buses_Key.TO_City_id.trim());
            pro_Tocity.setType(Integer.class);
            search_request.addProperty(pro_Tocity);


            PropertyInfo date = new PropertyInfo();
            date.setName("JourneyDate");
            date.setValue(Search_Buses_Key.Selected_date);
            date.setType(String.class);
            search_request.addProperty(date);

         /*   PropertyInfo NoOfSeats = new PropertyInfo();
            NoOfSeats.setName("NoOfSeats");
            NoOfSeats.setValue("40");
            NoOfSeats.setType(Integer.class);
            search_request.addProperty(NoOfSeats);


            PropertyInfo pro_Searchid = new PropertyInfo();
            pro_Searchid.setName("SearchId");
            pro_Searchid.setValue("1");
            pro_Searchid.setType(Integer.class);
            search_request.addProperty(pro_Searchid);*/



            request.addSoapObject(search_request);


            Log.e("vikas envolop",request.toString());

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.implicitTypes = true;
            envelope.setAddAdornments(false);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);


            Log.e("vikas envolop",envelope.toString());

            HttpTransportSE httpTransport = new HttpTransportSE("http://affapi.mantistechnologies.com/service.asmx");
            httpTransport.debug = true;

            try {
                httpTransport.call(Constants.GLOBEL_NAMESPACE+Constants.METHOD_GET_ROUTES, envelope);
            } catch (HttpResponseException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            } catch (XmlPullParserException e) {

                e.printStackTrace();
            }

            search_bus_result = null;

            try {
                search_bus_result = (SoapObject)envelope.getResponse();


            } catch (SoapFault e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            thread_realm.beginTransaction();
            thread_realm.clear(Bus_routes_detail.class);
            thread_realm.commitTransaction();

            try {
                if(search_bus_result != null)
                {

                    if(((SoapObject)search_bus_result.getProperty("Response")).getPrimitiveProperty("IsSuccess").toString().equals("true"))
                    {
                        count = search_bus_result.getPropertyCount();

                        for(int i=0;i< ((SoapObject)search_bus_result.getProperty("Route")).getPropertyCount();i++) {

                            String arrval = ((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("ArrTime").toString();
                            String Departure = ((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("DepTime").toString();
                            Date dep = getdate(Departure);
                            Date arr = getdate(arrval);
                            String time_departure = gettime(Departure);
                            String time_arrival = gettime(arrval);
                            String duration = getduration(dep,arr);

                            Log.e("vikas arrival=",arrval);
                            Log.e("vikas Departure = ",Departure);
                            Log.e("vikas dep date = ",""+dep);
                            Log.e("vikas arr date=",""+arr);
                            Log.e("vikas duration =",duration);




                            thread_realm.beginTransaction();
                            Bus_routes_detail bus_rout = thread_realm.createObject(Bus_routes_detail.class);

                            bus_rout.setDuration(duration);
                            bus_rout.setArrivaltime(time_arrival);
                            bus_rout.setDeparturetime(time_departure);
                            bus_rout.setRouteScheduleId(Integer.parseInt(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("RouteScheduleId").toString()));
                            bus_rout.setCompanyId(Integer.parseInt(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("CompanyId").toString()));
                            bus_rout.setCompanyName(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("CompanyName").toString());
                            // bus_rout.setDepartureTime((DateTimePatternGenerator)((SoapObject)((SoapObject)result.getProperty("Route")).getProperty(i)).getProperty("DepartureTime"));
                            bus_rout.setDepTime(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("DepTime").toString());
                            //  bus_rout.setArrivalTime((DateTimePatternGenerator)((SoapObject)((SoapObject)result.getProperty("Route")).getProperty(i)).getProperty("ArrivalTime"));
                            bus_rout.setArrTime(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("ArrTime").toString());
                            bus_rout.setFare(Float.parseFloat(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("Fare").toString()));
                            bus_rout.setSeaterFareNAC(Float.parseFloat(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("SeaterFareNAC").toString()));
                            bus_rout.setSeaterFareAC(Float.parseFloat(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("SeaterFareAC").toString()));
                            bus_rout.setSleeperFareNAC(Float.parseFloat(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("SleeperFareNAC").toString()));
                            bus_rout.setSleeperFareAC(Float.parseFloat(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("SleeperFareAC").toString()));
                            bus_rout.setHasAC(Boolean.parseBoolean(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("HasAC").toString()));
                            bus_rout.setHasNAC(Boolean.parseBoolean(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("HasNAC").toString()));
                            bus_rout.setHasSeater(Boolean.parseBoolean(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("HasSeater").toString()));
                            bus_rout.setHasSleeper(Boolean.parseBoolean(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("HasSleeper").toString()));
                            bus_rout.setIsVolvo(Boolean.parseBoolean(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("IsVolvo").toString()));
                            bus_rout.setBusLabel(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("BusLabel").toString());
                            bus_rout.setCommPCT(Float.parseFloat(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("CommPCT").toString()));
                            bus_rout.setCommAmount(Float.parseFloat(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("CommAmount").toString()));
                            bus_rout.setAvailableSeats(Integer.parseInt(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("AvailableSeats").toString()));
                            bus_rout.setBusTypeName(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("BusTypeName").toString());
                            bus_rout.setBusNumber(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("BusNumber").toString());
                            thread_realm.commitTransaction();
                        }
                    }
                    else
                    {


                    }




                }
                else
                {

                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }




            return null;
        }
    }


    public void activity_dismiss()
    {
        this.finish();

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void onBackPressed() {






    }

    public void showAlertDialog(String title,String message,String buttonlabel)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_loading.this);
        builder.setTitle(title)
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
        b.setTextColor(ContextCompat.getColor(Activity_loading.this, R.color.app_white));
    }

}
