package com.iseva.app.source.travel;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iseva.app.source.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static java.security.AccessController.getContext;


public class MainActivity extends Activity{

    ImageView iv_header;
    TextView tv_header;
    private EditText Get_From_Cities_et;
    private EditText Get_To_Cities_et;
    private EditText Journey_Date_et;
    private  Button search_routes_btn;
    private ProgressDialog progress;
    private SoapObject loginAuth_result;

    Session_manager session_manager;
    Button show_booked_ticket;

    LinearLayout activity_main_login_alert_layout;
    TextView activity_main_login_alert_cancel_btn;
    TextView activity_main_login_alert_login_btn;
    TextView activity_main_login_alert_signup_btn;

    public  ArrayList<HashMap<String, String>> All_Cities_Map;
    public  ArrayList<HashMap<String,String>> Main_Cities;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        setclicklistener();
    }



    public void initialize()
    {
        iv_header = (ImageView)findViewById(R.id.header_back_button);
        tv_header = (TextView)findViewById(R.id.header_text);
        tv_header.setText("Travel");
        Get_From_Cities_et=(EditText)findViewById(R.id.Get_City_From);
        Get_To_Cities_et = (EditText)findViewById(R.id.Get_City_To);
        Journey_Date_et = (EditText)findViewById(R.id.journey_date);

        activity_main_login_alert_layout = (LinearLayout)findViewById(R.id.activity_main_login_alert_layout);
        activity_main_login_alert_cancel_btn = (TextView)findViewById(R.id.activity_main_login_alert_cancel_btn);
        activity_main_login_alert_login_btn = (TextView)findViewById(R.id.activity_main_login_alert_login_btn);
        activity_main_login_alert_signup_btn = (TextView)findViewById(R.id.activity_main_login_alert_signup_btn);

        search_routes_btn = (Button)findViewById(R.id.search_routes_btn);
        show_booked_ticket = (Button)findViewById(R.id.show_booked_ticket_btn);
        progress = new ProgressDialog(MainActivity.this);
        progress.setMessage("Please wait...");
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);

        progress.show();
        String today_date = get_today_date();
        Journey_Date_et.setText(change_date_form(today_date));
        Search_Buses_Key.Selected_date = today_date;
        //Toast.makeText(this,today_date,Toast.LENGTH_LONG).show();

        if(isNetworkConnected())
        {
            LoginAuth Loginrequest = new LoginAuth();
            Loginrequest.execute();

        }
        else
        {
            progress.dismiss();
            Toast.makeText(MainActivity.this,R.string.internet_connection_error_title,Toast.LENGTH_LONG).show();
            callAlertBox();
        }


    }


    public Boolean TryAgaintoCallService()
    {
        if(isNetworkConnected())
        {
            progress = new ProgressDialog(MainActivity.this);
            progress.setMessage("Please wait...");
            progress.setCanceledOnTouchOutside(false);
            progress.setCancelable(false);
            progress.show();
            LoginAuth Loginrequest = new LoginAuth();
            Loginrequest.execute();
            return true;

        }
        else
        {
            return false;
        }
    }

    public void callAlertBox()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.internet_connection_error_title)
                .setMessage(R.string.internet_connection_error_message_try_again)
                .setCancelable(false)
                .setNegativeButton("Retry",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(TryAgaintoCallService())
                        {
                            dialog.cancel();
                        }
                        else
                        {
                            callAlertBox();
                        }

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

        Button b = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        b.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.app_theme_color));

    }

    public void showAlertDialog(String title,String message,String buttonlabel)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
        b.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.app_white));
    }

    public void setclicklistener()
    {

        iv_header.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                activity_dismiss();
            }
        });

        Get_From_Cities_et.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(isNetworkConnected())
                {
                    Intent i = new Intent(MainActivity.this,Activity_SelectCityFrom.class);

                    i.putExtra("main_cities",Main_Cities);
                    i.putExtra("all_cities",All_Cities_Map);
                    startActivity(i);
                    overridePendingTransition(R.anim.anim_in, R.anim.anim_none);
                }
                else
                {
                    callAlertBox();
                }




            }
        });


        Get_To_Cities_et.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(isNetworkConnected())
                {
                    Intent i = new Intent(MainActivity.this,Activity_SelectCityTo.class);
                    i.putExtra("main_cities",Main_Cities);
                    i.putExtra("all_cities",All_Cities_Map);
                    startActivity(i);
                    overridePendingTransition(R.anim.anim_in, R.anim.anim_none);
                }
                else
                {
                    callAlertBox();
                }

            }
        });

        search_routes_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(Get_From_Cities_et.getText().length() == 0)
                {

                    showAlertDialog(getResources().getString(R.string.validating_error_title),"Please Insert Origin City !","Ok");

                }
                else if(Get_To_Cities_et.getText().length() == 0)
                {
                    showAlertDialog(getResources().getString(R.string.validating_error_title),"Please Insert Destination City !","Ok");

                }
                else if(Journey_Date_et.getText().length() == 0)
                {
                    showAlertDialog(getResources().getString(R.string.validating_error_title),"Please Insert Journey Date !","Ok");

                }
                else
                {
                    if(isNetworkConnected())
                    {
                        Intent i = new Intent(MainActivity.this,Activity_loading.class);
                        i.putExtra("Loading_text","SEARCHING BUSES");
                        startActivity(i);
                    }
                    else {
                        callAlertBox();
                    }

                }

            }
        });

        Journey_Date_et.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                DialogFragment dialogfragment = new Datepicker();
                dialogfragment.show(getFragmentManager(), "Datepicker");
            }
        });

        show_booked_ticket.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                session_manager = new Session_manager(MainActivity.this);
                if(isNetworkConnected())
                {
                    if(session_manager.isLoggedIn())
                    {
                        Intent i = new Intent(MainActivity.this,Activity_show_booked_ticket.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.anim_in, R.anim.anim_none);
                    }
                    else
                    {
                        activity_main_login_alert_layout.setVisibility(View.VISIBLE);
                    }

                }
                else
                {
                    showAlertDialog(getResources().getString(R.string.internet_connection_error_title),getResources().getString(R.string.internet_connection_error_message),"Ok");
                }

            }
        });

        activity_main_login_alert_cancel_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                activity_main_login_alert_layout.setVisibility(View.GONE);
            }
        });

        activity_main_login_alert_login_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                activity_main_login_alert_layout.setVisibility(View.GONE);
                Intent i = new Intent(MainActivity.this,Activity_login.class);
                startActivity(i);
                overridePendingTransition(R.anim.anim_in, R.anim.anim_none);
            }
        });

        activity_main_login_alert_signup_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                activity_main_login_alert_layout.setVisibility(View.GONE);
                Intent i = new Intent(MainActivity.this,Activity_register.class);
                startActivity(i);
                overridePendingTransition(R.anim.anim_in, R.anim.anim_none);
            }
        });
    }


    public String get_today_date()
    {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String today_date = "";
        if(month > 9 && day >9)
        {
            today_date = year+"-"+month+"-"+day;
        }
        else if(month < 10 && day <10)
        {
            today_date = year+"-"+"0"+month+"-"+"0"+day;
        }
        else if(month > 9 && day < 10)
        {
            today_date = year+"-"+month+"-"+"0"+day;
        }
        else if(month <10 && day > 9)
        {
            today_date = year+"-"+"0"+month+"-"+day;
        }


        return today_date;
    }


    public String change_date_form(String date)
    {
        String[] days ={"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
        String[] months={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

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


        String final_date = day+"-"+months[month_int-1]+"-"+year+","+day_of_weak;


        return final_date;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

// request classes

    private class LoginAuth extends AsyncTask<Void, Void, Void>    {


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(loginAuth_result != null)
            {
                if(((SoapObject)loginAuth_result.getProperty("Response")).getPrimitivePropertyAsString("IsSuccess").equals("true"))
                {
                    if(isNetworkConnected())
                    {
                        GetCity gc = new GetCity();
                        gc.execute();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this,R.string.internet_connection_error_title,Toast.LENGTH_LONG).show();
                        callAlertBox();
                    }
                }
                else
                {
                    showAlertDialog(getResources().getString(R.string.validating_error_title),((SoapObject)loginAuth_result.getProperty("Response")).getPrimitivePropertyAsString("Message"),"Ok");
                }
            }
            else
            {
                showAlertDialog(getResources().getString(R.string.validating_error_title),"Some Error Accured Please Try Again !","Ok");
            }


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            SoapObject request = new SoapObject(Constants.GLOBEL_NAMESPACE, Constants.METHOD_AUNTHENTICATION);


            PropertyInfo loginid = new PropertyInfo();
            loginid.setName("LoginID");
            loginid.setValue("test");
            loginid.setType(String.class);
            request.addProperty(loginid);

            PropertyInfo password = new PropertyInfo();
            password.setName("Password");
            password.setValue("test456");
            password.setType(String.class);
            request.addProperty(password);

            PropertyInfo usertype = new PropertyInfo();
            usertype.setName("UserType");
            usertype.setValue("S");
            usertype.setType(String.class);
            request.addProperty(usertype);



            PropertyInfo logincode = new PropertyInfo();
            logincode.setName("LoginCode");
            logincode.setValue("9542");
            logincode.setType(Integer.class);
            request.addProperty(logincode);

           /* PropertyInfo loginid = new PropertyInfo();
            loginid.setName("LoginID");
            loginid.setValue("ISEVA");
            loginid.setType(String.class);
            request.addProperty(loginid);

            PropertyInfo password = new PropertyInfo();
            password.setName("Password");
            password.setValue("vikrant1729");
            password.setType(String.class);
            request.addProperty(password);

            PropertyInfo usertype = new PropertyInfo();
            usertype.setName("UserType");
            usertype.setValue("S");
            usertype.setType(String.class);
            request.addProperty(usertype);



            PropertyInfo logincode = new PropertyInfo();
            logincode.setName("LoginCode");
            logincode.setValue("9542");
            logincode.setType(Integer.class);
            request.addProperty(logincode);*/




            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransport = new HttpTransportSE(Constants.GLOBEL_URL);
            httpTransport.debug =true;


            try {
                httpTransport.call(Constants.GLOBEL_NAMESPACE+Constants.METHOD_AUNTHENTICATION, envelope);
            } catch (HttpResponseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            loginAuth_result = null;

            try {
                loginAuth_result  = (SoapObject)envelope.getResponse();

            } catch (SoapFault e) {

                e.printStackTrace();
            }
            try {
                String Is_success = "false";
                if(loginAuth_result != null) {
                    Is_success = ((SoapObject) loginAuth_result.getProperty("Response")).getPrimitiveProperty("IsSuccess").toString();
                }

                if(Is_success.equals("true")) {

                    LoginCridantial.UserId = loginAuth_result.getPrimitiveProperty("UserID").toString().trim();
                    LoginCridantial.UserType = loginAuth_result.getPrimitiveProperty("UserType").toString().trim();
                    LoginCridantial.UserKey = loginAuth_result.getPrimitiveProperty("Key").toString().trim();
                }
                else
                {
                    Toast.makeText(MainActivity.this,"requst not send",Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }



            return null;
        }
    }



    private class GetCity extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);

            progress.dismiss();


        }



        @Override
        protected Void doInBackground(Void... voids) {

            SoapObject request = new SoapObject(Constants.GLOBEL_NAMESPACE,Constants.METHOD_GETCITYFROM);

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
            Log.e("vikas request print",request.toString());


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.implicitTypes = true;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            Log.e("vikas envolop",envelope.toString());

            HttpTransportSE httpTransport = new HttpTransportSE(Constants.GLOBEL_URL);

            Log.e("vikas http print",httpTransport.toString());

            try {
                httpTransport.call(Constants.GLOBEL_NAMESPACE+Constants.METHOD_GETCITYFROM, envelope);
            } catch (HttpResponseException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            } catch (XmlPullParserException e) {

                e.printStackTrace();
            }
            SoapObject result = null;

            try {
                result = (SoapObject)envelope.getResponse();

            } catch (SoapFault e) {

                e.printStackTrace();
            }



            try
            {
                String Is_success =((SoapObject)result.getProperty("Response")).getPrimitiveProperty("IsSuccess").toString();
                if(Is_success.equals("true"))
                {
                    ArrayList<HashMap<String, String>>  Temp_all_cities = new ArrayList<HashMap<String, String>>();
                    ArrayList<HashMap<String, String>>  Temp_Main_cities = new ArrayList<HashMap<String, String>>();

                    for(int i=0;i< ((SoapObject)result.getProperty(1)).getPropertyCount();i++)
                    {
                        String is_main = ((SoapObject)((SoapObject)result.getProperty("Cities")).getProperty(i)).getProperty("IsPriority").toString();
                        if(is_main.equals("true"))
                        {
                            String cityname = ((SoapObject)((SoapObject)result.getProperty("Cities")).getProperty(i)).getProperty("CityName").toString();
                            String upperstring = cityname.substring(0,1).toUpperCase() + cityname.substring(1);
                            String cityid = ((SoapObject)((SoapObject)result.getProperty("Cities")).getProperty(i)).getProperty("CityID").toString();
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("cityname", upperstring);
                            map.put("cityid",cityid);
                            Temp_Main_cities.add(map);
                        }

                        String cityname = ((SoapObject)((SoapObject)result.getProperty("Cities")).getProperty(i)).getProperty("CityName").toString();
                        String upperstring = cityname.substring(0,1).toUpperCase() + cityname.substring(1);
                        String cityid = ((SoapObject)((SoapObject)result.getProperty("Cities")).getProperty(i)).getProperty("CityID").toString();
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("cityname", upperstring);
                        map.put("cityid",cityid);
                        Temp_all_cities.add(map);


                    }

                    All_Cities_Map = Temp_all_cities;
                    Main_Cities = Temp_Main_cities;
                    Log.e("vikas",Is_success);
                }
                else
                {

                    Log.e("vikas",Is_success);

                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }


            return null;
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        if(Search_Buses_Key.From_City_name != null)
        {
            Get_From_Cities_et.setText(Search_Buses_Key.From_City_name);
        }

        if(Search_Buses_Key.To_City_name != null)
        {
            Get_To_Cities_et.setText(Search_Buses_Key.To_City_name);
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

   /* private class GetToCities extends AsyncTask<Void, Void, Void>    {


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        Toast.makeText(MainActivity.this,""+cityid,Toast.LENGTH_LONG).show();
            String[] from = new String[] {"cityname"};
            int[] to = new int[] {R.id.text1};

            AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {

                    *//** Each item in the adapter is a HashMap object.
     *  So this statement creates the currently clicked hashmap object
     * *//*
                    HashMap<String, String> hm = (HashMap<String, String>) arg0.getAdapter().getItem(position);
                    EditText et = (EditText)findViewById(R.id.Get_City_To);
                    et.setText(hm.get("cityname"));
                    // Toast.makeText(MainActivity.this,hm.get("cityid"),Toast.LENGTH_LONG).show();
                   // LoginCridantial.FromCityId = hm.get("cityid");

                    Search_Buses_Key.TO_City_id = hm.get("cityid");


                }
            };



            //ArrayAdapter adapter = new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,all_cities);
           // SimpleAdapter adapter1 = new SimpleAdapter(MainActivity.this,Tocitymap,R.layout.autotextcomplete,from,to);
            Get_To_Cities_et.setOnItemClickListener(itemClickListener);
          //  Get_To_Cities_et.setAdapter(adapter1);
            Get_To_Cities_et.setThreshold(1);


        }

        @Override
        protected Void doInBackground(Void... voids) {

            SoapObject request = new SoapObject(Constants.GLOBEL_NAMESPACE,Constants.METHOD_GETCITYTO);

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



            PropertyInfo Fromcity = new PropertyInfo();
            Fromcity.setName("FromCityID");
            Fromcity.setValue(Search_Buses_Key.From_City_id);
            Fromcity.setType(Integer.class);

            request.addProperty(Fromcity);

            Log.e("vikas request print",request.toString());


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            envelope.implicitTypes = true;
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            Log.e("vikas envolop",envelope.toString());

            HttpTransportSE httpTransport = new HttpTransportSE(Constants.GLOBEL_URL);

            Log.e("vikas http print",httpTransport.toString());

            try {
                httpTransport.call(Constants.GLOBEL_NAMESPACE+Constants.METHOD_GETCITYTO, envelope);
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
            SoapObject result = null;

            try {
                result = (SoapObject)envelope.getResponse();

            } catch (SoapFault e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

          //  all_cities = new ArrayList<String>();
           //  response =  result.getProperty(0).toString();

          //  citymap = new ArrayList<HashMap<String, String>>();


            cityid = ((SoapObject)result.getProperty(1)).getPropertyCount();


            List<HashMap<String, String>>  Tocitymap = new ArrayList<HashMap<String, String>>();





            for(int i=0;i< ((SoapObject)result.getProperty(1)).getPropertyCount();i++)
            {


                a = ((SoapObject)((SoapObject)result.getProperty(1)).getProperty(i)).getProperty("CityName").toString();
                String cityid = ((SoapObject)((SoapObject)result.getProperty(1)).getProperty(i)).getProperty("CityID").toString();
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("cityname", a);
                map.put("cityid",cityid);
                Tocitymap.add(map);

            }


            //response = LoginCridantial.UserId + " " + LoginCridantial.UserType + " " + LoginCridantial.UserKey;


            return null;
        }
    }*/



}
