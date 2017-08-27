package com.iseva.app.source.travel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.iseva.app.source.Custom_VolleyAppController;
import com.iseva.app.source.Custom_VolleyObjectRequest;
import com.iseva.app.source.R;
import com.iseva.app.source.Realm_objets.Bus_routes_detail;
import com.iseva.app.source.travel.Constants.BUS_TYPE;
import com.iseva.app.source.travel.Constants.JSON_KEYS;
import com.iseva.app.source.travel.Constants.URL_TY;
import com.iseva.app.source.travel.Global_Travel.TRAVEL_DATA;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

public class Activity_loading extends Activity_Parent_Travel implements Parent_Interface {

    // DotProgressBar dotprogressBar;
    TextView loading_text;
    private String response;
    SoapObject search_bus_result;

    LinearLayout loader_layout;

    private Realm My_realm;

    int count ;
    DilatingDotsProgressBar mDilatingDotsProgressBar;
    private int volley_timeout = 15000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        My_realm = Realm.getDefaultInstance();

        loading_text = (TextView)findViewById(R.id.Loading_text);
        loader_layout = (LinearLayout)findViewById(R.id.activity_loading_loader_layout);
        loading_text.setText(getIntent().getStringExtra("Loading_text"));
        mDilatingDotsProgressBar = (DilatingDotsProgressBar) findViewById(R.id.progress);

// show progress bar and start animating
        mDilatingDotsProgressBar.showNow();
        mDilatingDotsProgressBar.setNumberOfDots(6);
        mDilatingDotsProgressBar.setGrowthSpeed(600);
        mDilatingDotsProgressBar.setDotScaleMultpiplier(2);



        if (isNetworkConnected(true)) {

            searchBuses();

        }


    }


    public void retryServiceCall() {

//        if (isNetworkConnected(true)) {
//
//            searchBuses();
//
//        }


        //HARSH go back to last screen

        activity_dismiss();
    }

    public void searchBuses() {

        try {

           // HashMap<String, String> paramsMap = new HashMap<String, String>();

            //TODO Remove hardcoding when going LIVE
            TRAVEL_DATA.FROM_CITY_ID = "4292";
            TRAVEL_DATA.TO_CITY_ID = "4562";
            //TRAVEL_DATA.JOURNEY_DATE = "2017-08-25";
            //TODO Remove hardcoding when going LIVE

            String url = URL_TY.SEARCH_BUSES + "?fromCityId="+ TRAVEL_DATA.FROM_CITY_ID  +"&toCityId="+ TRAVEL_DATA.TO_CITY_ID +"&journeyDate="+ TRAVEL_DATA.JOURNEY_DATE;


            //  Params has to be sent in url for GET req

            Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                    Method.GET,url,
                    null, new Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    //Log.i("HARSH", "json Response recieved !! -> " + response)
                    try {

                        if (response.getBoolean(JSON_KEYS.SUCCESS)) {




                            My_realm.beginTransaction();
                            My_realm.delete(Bus_routes_detail.class);
                            My_realm.commitTransaction();

                            My_realm.beginTransaction();

                            JSONObject objJsonData = response.getJSONObject(Constants.JSON_KEYS.DATA);


                            JSONArray arrJsonBuses = objJsonData.getJSONArray("Buses");

                            for (int i = 0; i < arrJsonBuses.length(); i++) {

                                Bus_routes_detail bus_rout = My_realm.createObject(Bus_routes_detail.class);

                                JSONObject objJsonBus = arrJsonBuses.getJSONObject(i);

                                bus_rout.setDepTime(gettime(objJsonBus.optString("DeptTime")));
                                bus_rout.setArrTime(gettime(objJsonBus.optString("ArrTime")));

                                String durationStr =  objJsonBus.optString("Duration"); //7:30
                                bus_rout.setDuration( durationStr + " Hours"); //



    //                          For SOrting by duration
                                try {

                                    String durDoubleStr =  durationStr.replace(":",".");
                                    double durDouble = Double.parseDouble(durDoubleStr);
                                    bus_rout.setDurationVal(durDouble);
                                }catch (NumberFormatException ex){

                                    //Exception in parsing
                                }
                                catch (Exception ex){

                                }


                                bus_rout.setRouteBusId(objJsonBus.optInt("RouteBusId"));

                                double commition_per = objJsonBus.optDouble("CommPct");
                                bus_rout.setCommPCT(commition_per);

                                if (objJsonBus.has("BusStatus")) {

                                    JSONObject objJsonBusStatus = objJsonBus.getJSONObject("BusStatus");

                                    bus_rout.setAvailableSeats(objJsonBusStatus.optInt("Availability"));

                                    double total_fare = 0.0;
                                    double total_tax = objJsonBusStatus.getDouble("TotalTax");
                                    double total_commition_amount = 0.0;

                                    total_fare = total_fare + total_tax;

                                    if (objJsonBusStatus.has("BaseFares")) {

                                        JSONArray arrJsonBaseFares = objJsonBusStatus.getJSONArray("BaseFares");

                                        if (arrJsonBaseFares.length() > 0) {

                                            //Consider first element only for fare as per travelyari Tech support!
                                            total_fare = total_fare + arrJsonBaseFares.getDouble(0);
                                        }

                                    }

                                    // HARSH : SET FARES AND AMOUNT AFTER COMMITION
                                    bus_rout.setFare(total_fare);


                                    double offer_per;
                                    double fare_after_total_discount;

                                    if (TRAVEL_DATA.ISEVA_SHARE_PCT <= commition_per) {
                                        offer_per = commition_per - TRAVEL_DATA.ISEVA_SHARE_PCT;
                                    } else {
                                        offer_per = 0;
                                    }

                                    fare_after_total_discount = (total_fare * (100 - offer_per)) / 100;

                                    total_commition_amount = (double)(total_fare * (commition_per)) / 100;
                                    fare_after_total_discount = (double) Math.round(fare_after_total_discount);

                                    bus_rout.setFare_after_offer(fare_after_total_discount);

                                    bus_rout.setCommAmount(total_commition_amount);
                                }


                                bus_rout.setCompanyId(objJsonBus.optInt("CompanyId"));

                                String company_name = objJsonBus.optString("CompanyName");

                                if (company_name.equals("anyType")) {

                                    bus_rout.setCompanyName("");

                                } else {

                                    bus_rout.setCompanyName(company_name);
                                }


                                //SET BUS TYPE

                                if (objJsonBus.has("BusType")) {

                                    JSONObject objJsonBusType = objJsonBus.getJSONObject("BusType");

                                    String ac_type = objJsonBusType.optString("IsAC");

                                    if (ac_type.equals(BUS_TYPE.AC)) {

                                        bus_rout.setHasAC(Boolean.TRUE);
                                        bus_rout.setHasNAC(Boolean.FALSE);

                                    } else if (ac_type.equals(BUS_TYPE.NON_AC)) {

                                        bus_rout.setHasAC(Boolean.FALSE);
                                        bus_rout.setHasNAC(Boolean.TRUE);
                                    }


                                    String seater_type = objJsonBusType.optString("Seating");

                                    if (seater_type.equals(BUS_TYPE.SEATER)) {

                                        bus_rout.setHasSeater(Boolean.TRUE);
                                        bus_rout.setHasSleeper(Boolean.FALSE);

                                    } else if (seater_type.equals(BUS_TYPE.SEATER_SEMI_SLEEPER)) {
                                        bus_rout.setHasSeater(Boolean.TRUE);
                                        bus_rout.setHasSleeper(Boolean.FALSE);

                                    } else if (seater_type.equals(BUS_TYPE.SLEEPER)) {
                                        bus_rout.setHasSeater(Boolean.FALSE);
                                        bus_rout.setHasSleeper(Boolean.TRUE);

                                    }else if(seater_type.equals(BUS_TYPE.SEMI_SLEEPER)){
                                        bus_rout.setHasSeater(Boolean.TRUE);
                                        bus_rout.setHasSleeper(Boolean.FALSE);


                                    }else if (seater_type.equals(BUS_TYPE.SEATER_SLEEPER)){
                                        bus_rout.setHasSeater(Boolean.TRUE);
                                        bus_rout.setHasSleeper(Boolean.TRUE);

                                    }else if (seater_type.equals(BUS_TYPE.SEMI_SLEEPER_SLEEPER)){
                                        bus_rout.setHasSeater(Boolean.TRUE);
                                        bus_rout.setHasSleeper(Boolean.TRUE);

                                    }

                                    bus_rout.setSeaterType(seater_type);

                                    String make = objJsonBusType.optString("Make");

                                    if (make.equals(BUS_TYPE.MAKE_VOLVO)) {

                                        bus_rout.setIsVolvo(Boolean.TRUE);
                                    }

                                    bus_rout.setMake(make);


                                    String axle = objJsonBusType.optString("Axle");

                                    bus_rout.setAxel(axle); // I think this is re;ated to setBusTypeName

                                }
                                bus_rout.setBusLabel(objJsonBus.optString("BusLabel"));




                            }
                            My_realm.commitTransaction();


                            //Navigate to Next
                            Intent i = new Intent(Activity_loading.this,Activity_Bus_Routes.class);
                            i.putExtra("response",""+response);
                            startActivity(i);
                            overridePendingTransition(R.anim.anim_in, R.anim.anim_none);

                            activity_dismiss();

                        }else{

                            callAlertBox(getResources().getString(R.string.server_error_title),getResources().getString(R.string.server_error_message_try_again));
                        }

                    } catch (JSONException e) {

                        callAlertBox(getResources().getString(R.string.server_error_title),getResources().getString(R.string.server_error_message_try_again));
                        e.printStackTrace();

                    }
                }


            }, new ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError err) {
                    Log.i("SUSHIL", "ERROR VolleyError");

                    callAlertBox(getResources().getString(R.string.server_error_title),getResources().getString(R.string.server_error_message_try_again));
                }
            })
            {




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
    public void onBackPressed() {



        super.onBackPressed();
        overridePendingTransition(R.anim.anim_none, R.anim.anim_out);


    }



}
//
//
//    public String getduration(Date startdate, Date endate)
//    {
//
//
//        int min = 0;
//        int hour = 0;
//        long diff = Math.abs(endate.getTime() - startdate.getTime());
//
//        min = (int) diff/(1000*60);
//        hour = min/60;
//        min = min%60;
//        String min_string = "";
//        String  hour_string = "";
//        if(hour > 9)
//        {
//            hour_string = Integer.toString(hour);
//        }
//        else
//        {
//            hour_string = "0"+hour;
//        }
//
//        if (min > 9)
//        {
//            min_string = Integer.toString(min);
//        }
//        else
//        {
//            min_string = "0"+min;
//        }
//
//
//        String final_string = hour_string+" "+"Hrs "+min_string+" "+"Mins";
//
//        return final_string;
//    }
//
//    public Date getdate(String date)
//    {
//        date = date.replace("T"," ");
//        Date d = null;
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//
//        try {
//            d = sdf.parse(date);
//        } catch (ParseException ex) {
//
//        }
//
//
//
//        return d;
//    }

//    private class SearchRoutes extends AsyncTask<Void,Void,Void>
//    {
//
//
//        @Override
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//
//            if(search_bus_result != null)
//            {
//                if(((SoapObject)search_bus_result.getProperty(0)).getPrimitiveProperty("IsSuccess").toString().equals("true"))
//                {
//
//                    Intent i = new Intent(Activity_loading.this,Activity_Bus_Routes.class);
//                    i.putExtra("response",""+response);
//                    startActivity(i);
//                    overridePendingTransition(R.anim.anim_in, R.anim.anim_none);
//
//                    activity_dismiss();
//                }
//                else
//                {
//                    Intent i = new Intent(Activity_loading.this,Activity_Bus_Routes.class);
//                    i.putExtra("response",""+response);
//                    startActivity(i);
//                    overridePendingTransition(R.anim.anim_in, R.anim.anim_none);
//
//                    activity_dismiss();
//
//               }
//
//
//            }
//            else
//            {
//                loader_layout.setVisibility(View.GONE);
//               showAlertDialog(getResources().getString(R.string.slow_internet_title),getResources().getString(R.string.slow_internet_error),"Ok");
//
//
//            }
//            if(Global_Travel.build_type == 0)
//            {
//                Log.e("vikas",search_bus_result.toString());
//            }
//
//
//        }
//
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//
//            Realm thread_realm = Realm.getDefaultInstance();
//
//            //TODO : HARSH Implement new API
//            SoapObject request = new SoapObject("","");
//            SoapObject search_request = new SoapObject(null,"SearchRequest");
//
////           SoapObject request = new SoapObject(Constants.GLOBEL_NAMESPACE,Constants.METHOD_GET_ROUTES1);
////
////            SoapObject sa = new SoapObject(null,"Authentication");
////            SoapObject search_request = new SoapObject(null,"SearchRequest");
////
////
////            PropertyInfo userid = new PropertyInfo();
////            userid.setName("UserID");
////            userid.setValue(LoginCridantial.UserId.trim());
////            userid.setType(Integer.class);
////            sa.addProperty(userid);
////
////            PropertyInfo usertype = new PropertyInfo();
////            usertype.setName("UserType");
////            usertype.setValue(LoginCridantial.UserType.trim());
////            usertype.setType(String.class);
////            sa.addProperty(usertype);
////
////            PropertyInfo userkey = new PropertyInfo();
////            userkey.setName("Key");
////            userkey.setValue(LoginCridantial.UserKey.trim());
////            userkey.setType(String.class);
////            sa.addProperty(userkey);
////
////            request.addSoapObject(sa);
//
//
//
//
//
//
//            PropertyInfo pro_Fromcity = new PropertyInfo();
//            pro_Fromcity.setName("FromCityId");
//            pro_Fromcity.setValue(TRAVEL_DATA.FROM_CITY_ID.trim());
//            pro_Fromcity.setType(Integer.class);
//            search_request.addProperty(pro_Fromcity);
//
//
//
//            PropertyInfo pro_Tocity = new PropertyInfo();
//            pro_Tocity.setName("ToCityId");
//            pro_Tocity.setValue(TRAVEL_DATA.TO_CITY_ID.trim());
//            pro_Tocity.setType(Integer.class);
//            search_request.addProperty(pro_Tocity);
//
//
//            PropertyInfo date = new PropertyInfo();
//            date.setName("JourneyDate");
//            date.setValue(TRAVEL_DATA.SELECTED_DATE);
//            date.setType(String.class);
//            search_request.addProperty(date);
//
//         /*   PropertyInfo NoOfSeats = new PropertyInfo();
//            NoOfSeats.setName("NoOfSeats");
//            NoOfSeats.setValue("40");
//            NoOfSeats.setType(Integer.class);
//            search_request.addProperty(NoOfSeats);
//
//
//            PropertyInfo pro_Searchid = new PropertyInfo();
//            pro_Searchid.setName("SearchId");
//            pro_Searchid.setValue("1");
//            pro_Searchid.setType(Integer.class);
//            search_request.addProperty(pro_Searchid);*/
//
//
//
//            request.addSoapObject(search_request);
//
//            if(Global_Travel.build_type == 0)
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
//
//            if(Global_Travel.build_type == 0)
//            {
//                Log.e("vikas envolop",envelope.toString());
//            }
//
//
//            //HttpTransportSE httpTransport = new HttpTransportSE(Constants.GLOBEL_URL);
//            //httpTransport.debug = true;
//
////            try {
////                httpTransport.call(Constants.GLOBEL_NAMESPACE+Constants.METHOD_GET_ROUTES1, envelope);
////            } catch (HttpResponseException e) {
////
////                e.printStackTrace();
////            } catch (IOException e) {
////
////                e.printStackTrace();
////            } catch (XmlPullParserException e) {
////
////                e.printStackTrace();
////            }
//
//            search_bus_result = null;
//
//            try {
//                search_bus_result = (SoapObject)envelope.getResponse();
//
//
//            } catch (SoapFault e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//            thread_realm.beginTransaction();
//            thread_realm.delete(Bus_routes_detail.class);
//            thread_realm.commitTransaction();
//
//            try {
//                if(search_bus_result != null)
//                {
//
//                    if(((SoapObject)search_bus_result.getProperty("Response")).getPrimitiveProperty("IsSuccess").toString().equals("true"))
//                    {
//                        count = search_bus_result.getPropertyCount();
//
//                        for(int i=0;i< ((SoapObject)search_bus_result.getProperty("Route")).getPropertyCount();i++) {
//
//                            String arrval = ((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("ArrTime").toString();
//                            String Departure = ((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("DepTime").toString();
//                            Date dep = getdate(Departure);
//                            Date arr = getdate(arrval);
//                            String time_departure = gettime(Departure);
//                            String time_arrival = gettime(arrval);
//                            String duration = getduration(dep,arr);
//
//
//
//                            float offer_per;
//                            float after_offer_fare;
//
//                            float commition_per = (int)Float.parseFloat(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("CommPCT").toString());
//                            float fare = Float.parseFloat(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("Fare").toString());
//
//                            if(Activity_Main.save_per <= commition_per)
//                            {
//                                offer_per = commition_per - Activity_Main.save_per;
//                            }
//                            else
//                            {
//                                offer_per = 0;
//                            }
//                            int temp_fare_offer = (int)(fare *(100-offer_per))/100;
//                            if(Activity_Main.save_per <= commition_per && Activity_Main.save_per != 0) {
//                                temp_fare_offer = temp_fare_offer + 1;
//                            }
//                            after_offer_fare = (float)temp_fare_offer;
//                            if(Global_Travel.build_type == 0)
//                            {
//                                Log.e("vikas after_fare_load=",""+after_offer_fare);
//                                Log.e("vikas percentage_loadi=",""+offer_per);
//                                Log.e("vikas total per loadi=",""+ Activity_Main.save_per);
//                            }

//
//                            thread_realm.beginTransaction();
//                            Bus_routes_detail bus_rout = thread_realm.createObject(Bus_routes_detail.class);
//
//                            bus_rout.setDuration(duration);
//                            bus_rout.setArrivaltime(time_arrival);
//                            bus_rout.setDeparturetime(time_departure);
//                            bus_rout.setRouteScheduleId(Integer.parseInt(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("RouteScheduleId").toString()));
//                            bus_rout.setCompanyId(Integer.parseInt(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("CompanyId").toString()));
//                            if(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("CompanyName").toString().contains("anyType"))
//                            {
//                                bus_rout.setCompanyName("");
//                            }
//                            else
//                            {
//                                bus_rout.setCompanyName(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("CompanyName").toString());
//                            }
//
//                            // bus_rout.setDepartureTime((DateTimePatternGenerator)((SoapObject)((SoapObject)result.getProperty("Route")).getProperty(i)).getProperty("DepartureTime"));
//                            bus_rout.setDepTime(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("DepTime").toString());
//                            //  bus_rout.setArrivalTime((DateTimePatternGenerator)((SoapObject)((SoapObject)result.getProperty("Route")).getProperty(i)).getProperty("ArrivalTime"));
//                            bus_rout.setArrTime(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("ArrTime").toString());
//                            bus_rout.setFare(Float.parseFloat(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("Fare").toString()));
//                            bus_rout.setFare_after_offer(after_offer_fare);
//                            bus_rout.setSeaterFareNAC(Float.parseFloat(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("SeaterFareNAC").toString()));
//                            bus_rout.setSeaterFareAC(Float.parseFloat(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("SeaterFareAC").toString()));
//                            bus_rout.setSleeperFareNAC(Float.parseFloat(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("SleeperFareNAC").toString()));
//                            bus_rout.setSleeperFareAC(Float.parseFloat(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("SleeperFareAC").toString()));
//                            bus_rout.setHasAC(Boolean.parseBoolean(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("HasAC").toString()));
//                            bus_rout.setHasNAC(Boolean.parseBoolean(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("HasNAC").toString()));
//                            bus_rout.setHasSeater(Boolean.parseBoolean(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("HasSeater").toString()));
//                            bus_rout.setHasSleeper(Boolean.parseBoolean(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("HasSleeper").toString()));
//                            bus_rout.setIsVolvo(Boolean.parseBoolean(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("IsVolvo").toString()));
//                            if(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("BusLabel").toString().contains("anyType"))
//                            {
//                                bus_rout.setBusLabel("");
//                            }
//                            else
//                            {
//                                bus_rout.setBusLabel(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("BusLabel").toString());
//                            }
//
//                            bus_rout.setCommPCT(Float.parseFloat(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("CommPCT").toString()));
//                            bus_rout.setCommAmount(Float.parseFloat(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("CommAmount").toString()));
//                            bus_rout.setAvailableSeats(Integer.parseInt(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("AvailableSeats").toString()));
//                            if(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("BusTypeName").toString().contains("anyType"))
//                            {
//                                bus_rout.setBusTypeName("");
//                            }
//                            else
//                            {
//                                bus_rout.setBusTypeName(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("BusTypeName").toString());
//                            }
//
//                            bus_rout.setBusNumber(((SoapObject) ((SoapObject) search_bus_result.getProperty("Route")).getProperty(i)).getProperty("BusNumber").toString());
//                            thread_realm.commitTransaction();
//                        }
//                    }
//                    else
//                    {
//                        Global_Travel.showAlertDialog(Activity_loading.this,getResources().getString(R.string.validating_error_title),((SoapObject)search_bus_result.getProperty("Response")).getPrimitivePropertyAsString("Message"),"Ok");
//
//                    }
//
//
//
//
//                }
//                else
//                {
//                    Global_Travel.showAlertDialog(Activity_loading.this,getResources().getString(R.string.validating_error_title),getResources().getString(R.string.slow_internet_error),"Ok");
//                }
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//
//
//
//
//            return null;
//        }
//    }
//




