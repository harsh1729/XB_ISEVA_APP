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
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.iseva.app.source.Activity_AdverImageView;
import com.iseva.app.source.R;

import org.json.JSONException;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends Activity implements NavigationView.OnNavigationItemSelectedListener,BaseSliderView.OnSliderClickListener,ViewPagerEx.OnPageChangeListener{

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
    private int volley_timeout = 15000;

    public static int save_per = 10;

    ArrayList<String> promo_image;
    SliderLayout sliderLayout_main;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(isNetworkConnected())
        {
            get_commition();
        }

        initialize();
        setclicklistener();
    }

    public void get_commition()
    {
        StringRequest promocodeapplyrequest = new StringRequest(Request.Method.POST,
                Constants.get_commition_extra_charge, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {

                Log.e("vikas",s);
                JSONObject response = null;
                try
                {
                    response = new JSONObject(s);


                    if(response != null)
                    {
                        save_per = Integer.parseInt(response.getString("commition"));

                    }

                } catch (JSONException e) {


                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {



            }
        }) {
            @Override
            public Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();






                return params;

            }
        };

        promocodeapplyrequest.setRetryPolicy(new DefaultRetryPolicy(
                volley_timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(promocodeapplyrequest);
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



        CardView slider_layout = (CardView)findViewById(R.id.slider_layout_main) ;
        sliderLayout_main = (SliderLayout)findViewById(R.id.slider_main);

        promo_image = new ArrayList<>();
        Intent i = getIntent();
        promo_image = i.getStringArrayListExtra("promo_image");
        if(promo_image.size() > 0)
        {
            slider_layout.setVisibility(View.VISIBLE);
            for(int k =0;k<promo_image.size();k++)
            {
                Log.e("vikas promourl",promo_image.get(k));
                TextSliderView textSliderView = new TextSliderView(MainActivity.this);
                textSliderView

                        .image(promo_image.get(k))
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(this);
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putString("extra",""+k);
                sliderLayout_main.addSlider(textSliderView);

            }

            sliderLayout_main.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            sliderLayout_main.setCustomAnimation(null);


            sliderLayout_main.setDuration(4000);
            sliderLayout_main.addOnPageChangeListener(this);
            if(promo_image.size() > 1)
            {
                sliderLayout_main.startAutoCycle();

            }
            else
            {
                sliderLayout_main.stopAutoCycle();
            }

        }
        else
        {
            slider_layout.setVisibility(View.GONE);
        }





        if(isNetworkConnected())
        {
            LoginAuth Loginrequest = new LoginAuth();
            Loginrequest.execute();

        }
        else
        {
            progress.dismiss();

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
        LayoutInflater inflater = (LayoutInflater)MainActivity.this.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        View v =  inflater.inflate(R.layout.textview,null);


        TextView title_tv = (TextView)v.findViewById(R.id.alert_title);
        title_tv.setText(getResources().getString(R.string.internet_connection_error_title));
       /* TextView title_tv = new TextView(this);
        title_tv.setPadding(0,10,0,0);
        title_tv.setTextColor(ContextCompat.getColor(MainActivity.this,R.color.black));
        title_tv.setTextSize(16);
        title_tv.setGravity(Gravity.CENTER);
        title_tv.setText(getResources().getString(R.string.internet_connection_error_title));*/

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCustomTitle(title_tv)
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

   /* public void showAlertDialog(String title,String message,String buttonlabel)
    {

        TextView title_tv = new TextView(this);
        title_tv.setPadding(0,10,0,0);
        title_tv.setTextColor(ContextCompat.getColor(MainActivity.this,R.color.black));
        title_tv.setTextSize(getResources().getDimension(R.dimen.text_size_small));

        title_tv.setGravity(Gravity.CENTER);
        title_tv.setText(title);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCustomTitle(title_tv)
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
    }*/

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

                    Global.showAlertDialog(MainActivity.this,getResources().getString(R.string.validating_error_title),"Please select origin city !","Ok");

                }
                else if(Get_To_Cities_et.getText().length() == 0)
                {
                    Global.showAlertDialog(MainActivity.this,getResources().getString(R.string.validating_error_title),"Please select destination city !","Ok");

                }
                else if(Journey_Date_et.getText().length() == 0)
                {
                    Global.showAlertDialog(MainActivity.this,getResources().getString(R.string.validating_error_title),"Please select journey date !","Ok");

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
                        Global.showAlertDialog(MainActivity.this,getResources().getString(R.string.internet_connection_error_title),getResources().getString(R.string.internet_connection_error_message),"Ok");
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
                    Global.showAlertDialog(MainActivity.this,getResources().getString(R.string.internet_connection_error_title),getResources().getString(R.string.internet_connection_error_message),"Ok");
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


        String final_date = day+"-"+months[month_int-1]+"-"+year+", "+day_of_weak;


        return final_date;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    /**
     * Called when an item in the navigation menu is selected.
     *
     * @param item The selected item
     * @return true to display the item as the selected item
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        int position = Integer.parseInt(slider.getBundle().get("extra").toString());
        Intent i = new Intent(MainActivity.this,Activity_AdverImageView.class);
        i.putExtra("id",position);
        i.putStringArrayListExtra("imageList", promo_image);
        startActivity(i);
    }

    /**
     * This method will be invoked when the current page is scrolled, either as part
     * of a programmatically initiated smooth scroll or a user initiated touch scroll.
     *
     * @param position             Position index of the first page currently being displayed.
     *                             Page position+1 will be visible if positionOffset is nonzero.
     * @param positionOffset       Value from [0, 1) indicating the offset from the page at position.
     * @param positionOffsetPixels Value in pixels indicating the offset from position.
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * This method will be invoked when a new page becomes selected. Animation is not
     * necessarily complete.
     *
     * @param position Position index of the new selected page.
     */
    @Override
    public void onPageSelected(int position) {

    }

    /**
     * Called when the scroll state changes. Useful for discovering when the user
     * begins dragging, when the pager is automatically settling to the current page,
     * or when it is fully stopped/idle.
     *
     * @param state The new scroll state.
     * @see ViewPagerEx#SCROLL_STATE_IDLE
     * @see ViewPagerEx#SCROLL_STATE_DRAGGING
     * @see ViewPagerEx#SCROLL_STATE_SETTLING
     */
    @Override
    public void onPageScrollStateChanged(int state) {

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

                        callAlertBox();
                    }
                }
                else
                {
                    Global.showAlertDialog(MainActivity.this,getResources().getString(R.string.validating_error_title),((SoapObject)loginAuth_result.getProperty("Response")).getPrimitivePropertyAsString("Message"),"Ok");
                }
            }
            else
            {
                Global.showAlertDialog(MainActivity.this,getResources().getString(R.string.validating_error_title),"Some error accured please try again !","Ok");
            }


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            SoapObject request = new SoapObject(Constants.GLOBEL_NAMESPACE, Constants.METHOD_AUNTHENTICATION);


        /*    PropertyInfo loginid = new PropertyInfo();
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
            request.addProperty(logincode);*/

            PropertyInfo loginid = new PropertyInfo();
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
            //logincode.setValue("7304");
            logincode.setType(Integer.class);
            request.addProperty(logincode);




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
        Search_Buses_Key.From_City_name = null;
        Search_Buses_Key.To_City_name = null;
        this.finish();
        overridePendingTransition(R.anim.anim_none, R.anim.anim_out);
    }


    @Override
    public void onBackPressed() {

        Search_Buses_Key.From_City_name = null;
        Search_Buses_Key.To_City_name = null;
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
