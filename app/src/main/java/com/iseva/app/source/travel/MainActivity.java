package com.iseva.app.source.travel;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderLayout.PresetIndicators;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.BaseSliderView.OnSliderClickListener;
import com.daimajia.slider.library.SliderTypes.BaseSliderView.ScaleType;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.daimajia.slider.library.Tricks.ViewPagerEx.OnPageChangeListener;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.iseva.app.source.Activity_AdverImageView;
import com.iseva.app.source.Custom_VolleyAppController;
import com.iseva.app.source.Custom_VolleyObjectRequest;
import com.iseva.app.source.R;
import com.iseva.app.source.Realm_objets.Realm_City;
import com.iseva.app.source.travel.Constants.JSON_KEYS;
import com.iseva.app.source.travel.Constants.URL_XB;
import com.iseva.app.source.travel.Global_Travel.TRAVEL_DATA;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;


public class MainActivity extends Activity implements OnNavigationItemSelectedListener, OnSliderClickListener, OnPageChangeListener {

    ImageView iv_header;
    TextView tv_header;
    private EditText Get_From_Cities_et;
    private EditText Get_To_Cities_et;
    private EditText Journey_Date_et;
    private Button search_routes_btn;
    private ProgressDialog progress;

    Session_manager session_manager;
    Button show_booked_ticket;

    LinearLayout activity_main_login_alert_layout;
    TextView activity_main_login_alert_cancel_btn;
    TextView activity_main_login_alert_login_btn;
    TextView activity_main_login_alert_signup_btn;

    Realm My_realm;

    public ArrayList<HashMap<String, String>> map_main_cities;

    public ArrayList<String> list_main_cities = new ArrayList<>(Arrays.asList("New Delhi","Mumbai","Pune","Bengaluru","Jaipur","Kolkata","Hyderabad","Chennai","Ahmedabad","Visakhapatnam","Surat","Kanpur","Lucknow","Nagpur","Mysuru"));


    public static int save_per = 10;

    ArrayList<String> promo_image;
    SliderLayout sliderLayout_main;

    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        session_manager = new Session_manager(this);

        My_realm = Realm.getInstance(this);

        if (isNetworkConnected(true)) {
            get_default_travel_data();
        }

        initialize();
        setclicklistener();

        client = new Builder(this).addApi(AppIndex.API).build();
    }

    public void get_default_travel_data() {

        try {

            HashMap<String, String> paramsMap = new HashMap<String, String>();
            paramsMap.put("pass_key", "XBlue-98767689723");

            Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                    Method.POST,
                    URL_XB.GET_DEFAULT_TRAVEL_DATA,
                    paramsMap, new Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.i("Gopal", "json Response recieved !!" + response);

                    try {
                        if (response.getBoolean(JSON_KEYS.SUCCESS)) {

                            JSONObject data = response.getJSONObject("data");
                            TRAVEL_DATA.TOKEN_ID = response.getString("secret_key").replace("\"", "");



                            save_per = Integer.parseInt(data.getString("commition"));


                            if (isNetworkConnected(true)) {

                               get_cities();
                            }

                            Log.i("Gopal", "save_per = " + save_per);

                        } else {


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

                }
            });

            Custom_VolleyAppController.getInstance().addToRequestQueue(
                    jsonObjectRQST);
        } catch (Exception ex) {

            ex.printStackTrace();

        }


    }


    public void get_cities() {

        try {

            HashMap<String, String> paramsMap = new HashMap<String, String>(); // No Patrams required to fetch cities


            Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                    Method.GET,
                    Constants.URL_TY.GET_CITY_LIST,
                    paramsMap, new Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.i("Gopal", "json Response recieved !!" + response);

                    if(progress != null) {
                        progress.dismiss();
                    }

                    try {
                        //if (response.getBoolean(JSON_KEYS.SUCCESS)) {


                            map_main_cities = new ArrayList<HashMap<String, String>>();

                            ArrayList<HashMap<String, String>> map_Previous_cities = new ArrayList<HashMap<String, String>>();

                            String previousSelected = getPreviousSelectedCities();


                            JSONArray data = response.getJSONArray(Constants.JSON_KEYS.DATA);

                        My_realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                RealmResults<Realm_City> result = realm.where(Realm_City.class).findAll();
                                result.clear();
                            }
                        });

                        My_realm.beginTransaction();
                            for(int i=0;i<data.length();i++){


                                String city_id = data.getJSONObject(i).getString(JSON_KEYS.CITY_ID);
                                String city_name = data.getJSONObject(i).getString(JSON_KEYS.CITY_NAME).replace(" ","");


                                Realm_City city = My_realm.createObject(Realm_City.class);

                                city.setCityId(city_id);
                                city.setCityName(city_name);


                                if( previousSelected.contains(city_name)){

                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put(JSON_KEYS.CITY_NAME, city_name);
                                    map.put(JSON_KEYS.CITY_ID, city_id);

                                    map_Previous_cities.add(map);

                                }else if(list_main_cities.contains(city_name)){

                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put(JSON_KEYS.CITY_NAME, city_name);
                                    map.put(JSON_KEYS.CITY_ID, city_id);

                                    map_main_cities.add(map);
                                }

                            }
                        My_realm.commitTransaction();


                            if(map_Previous_cities.isEmpty() == false){
                                for (HashMap<String, String> map:
                                        map_Previous_cities) {

                                    map_main_cities.add(0,map);
                                }
                            }


                        //} else {


                            //callAlertBox(getResources().getString(R.string.server_error_title),getResources().getString(R.string.server_error_message_try_again));

                        //}
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


    public void initialize() {

        iv_header = (ImageView) findViewById(R.id.header_back_button);
        tv_header = (TextView) findViewById(R.id.header_text);
        tv_header.setText("Travel");
        Get_From_Cities_et = (EditText) findViewById(R.id.Get_City_From);
        Get_To_Cities_et = (EditText) findViewById(R.id.Get_City_To);
        Journey_Date_et = (EditText) findViewById(R.id.journey_date);

        activity_main_login_alert_layout = (LinearLayout) findViewById(R.id.activity_main_login_alert_layout);
        activity_main_login_alert_cancel_btn = (TextView) findViewById(R.id.activity_main_login_alert_cancel_btn);
        activity_main_login_alert_login_btn = (TextView) findViewById(R.id.activity_main_login_alert_login_btn);
        activity_main_login_alert_signup_btn = (TextView) findViewById(R.id.activity_main_login_alert_signup_btn);

        search_routes_btn = (Button) findViewById(R.id.search_routes_btn);
        show_booked_ticket = (Button) findViewById(R.id.show_booked_ticket_btn);
        progress = new ProgressDialog(MainActivity.this);
        progress.setMessage("Please wait...");
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);

        progress.show();
        String today_date = get_today_date();
        Journey_Date_et.setText(change_date_form(today_date));
        TRAVEL_DATA.SELECTED_DATE = today_date;


        CardView slider_layout = (CardView) findViewById(R.id.slider_layout_main);
        sliderLayout_main = (SliderLayout) findViewById(R.id.slider_main);

        promo_image = new ArrayList<>();
        Intent i = getIntent();
        promo_image = i.getStringArrayListExtra("promo_image");
        if (promo_image.size() > 0) {
            slider_layout.setVisibility(View.VISIBLE);
            for (int k = 0; k < promo_image.size(); k++) {
                if (Global_Travel.build_type == 0) {
                    Log.e("vikas promourl", promo_image.get(k));
                }

                TextSliderView textSliderView = new TextSliderView(MainActivity.this);
                textSliderView

                        .image(promo_image.get(k))
                        .setScaleType(ScaleType.Fit)
                        .setOnSliderClickListener(this);
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putString("extra", "" + k);
                sliderLayout_main.addSlider(textSliderView);

            }

            sliderLayout_main.setPresetIndicator(PresetIndicators.Center_Bottom);
            sliderLayout_main.setCustomAnimation(null);


            sliderLayout_main.setDuration(4000);
            sliderLayout_main.addOnPageChangeListener(this);
            if (promo_image.size() > 1) {
                sliderLayout_main.startAutoCycle();

            } else {
                sliderLayout_main.stopAutoCycle();
            }

        } else {
            slider_layout.setVisibility(View.GONE);
        }


    }


    public void tryAgaintoCallService() {
        if (isNetworkConnected(true)) {

            progress = new ProgressDialog(MainActivity.this);
            progress.setMessage("Please wait...");
            progress.setCanceledOnTouchOutside(false);
            progress.setCancelable(false);
            progress.show();
            get_default_travel_data();

        }
    }


    public void setclicklistener() {

        iv_header.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                activity_dismiss();
            }
        });

        Get_From_Cities_et.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                if (isNetworkConnected(true)) {
                    if (map_main_cities != null ) {
                        Intent i = new Intent(MainActivity.this, Activity_SelectCityFrom.class);

                        i.putExtra("main_cities", map_main_cities);
                        startActivity(i);
                        overridePendingTransition(R.anim.anim_in, R.anim.anim_none);
                    } else {
                        tryAgaintoCallService();
                    }

                }


            }
        });


        Get_To_Cities_et.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                if (isNetworkConnected(true)) {
                    if (map_main_cities != null ) {
                        Intent i = new Intent(MainActivity.this, Activity_SelectCityTo.class);
                        i.putExtra("main_cities", map_main_cities);
                        startActivity(i);
                        overridePendingTransition(R.anim.anim_in, R.anim.anim_none);
                    } else {
                        tryAgaintoCallService();
                    }

                }

            }
        });

        search_routes_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                if (Get_From_Cities_et.getText().length() == 0) {

                    Global_Travel.showAlertDialog(MainActivity.this, getResources().getString(R.string.validating_error_title), "Please select origin city !", "Ok");

                } else if (Get_To_Cities_et.getText().length() == 0) {
                    Global_Travel.showAlertDialog(MainActivity.this, getResources().getString(R.string.validating_error_title), "Please select destination city !", "Ok");

                } else if (Journey_Date_et.getText().length() == 0) {
                    Global_Travel.showAlertDialog(MainActivity.this, getResources().getString(R.string.validating_error_title), "Please select journey date !", "Ok");

                } else if (TRAVEL_DATA.FROM_CITY_ID.equals(TRAVEL_DATA.TO_CITY_ID)) {
                    Global_Travel.showAlertDialog(MainActivity.this, getResources().getString(R.string.validating_error_title), "Please choose a different destination city.", "Ok");
                } else {
                    if (isNetworkConnected(false)) {


                        setSelectedCities(Get_To_Cities_et.getText().toString(), Get_From_Cities_et.getText().toString());

                        Intent i = new Intent(MainActivity.this, Activity_loading.class);
                        i.putExtra("Loading_text", "SEARCHING BUSES");
                        startActivity(i);
                    } else {
                        Global_Travel.showAlertDialog(MainActivity.this, getResources().getString(R.string.internet_connection_error_title), getResources().getString(R.string.internet_connection_error_message), "Ok");
                    }

                }

            }
        });

        Journey_Date_et.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {

                DialogFragment dialogfragment = new Datepicker();
                dialogfragment.show(getFragmentManager(), "Datepicker");
            }
        });

        show_booked_ticket.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {

                session_manager = new Session_manager(MainActivity.this);
                if (isNetworkConnected(false)) {
                    if (session_manager.isLoggedIn()) {
                        Intent i = new Intent(MainActivity.this, Activity_show_booked_ticket.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.anim_in, R.anim.anim_none);
                    } else {
                        activity_main_login_alert_layout.setVisibility(View.VISIBLE);
                    }

                } else {
                    Global_Travel.showAlertDialog(MainActivity.this, getResources().getString(R.string.internet_connection_error_title), getResources().getString(R.string.internet_connection_error_message), "Ok");
                }

            }
        });

        activity_main_login_alert_cancel_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                activity_main_login_alert_layout.setVisibility(View.GONE);
            }
        });

        activity_main_login_alert_login_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                activity_main_login_alert_layout.setVisibility(View.GONE);
                Intent i = new Intent(MainActivity.this, Activity_login.class);
                startActivity(i);
                overridePendingTransition(R.anim.anim_in, R.anim.anim_none);
            }
        });

        activity_main_login_alert_signup_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                activity_main_login_alert_layout.setVisibility(View.GONE);
                Intent i = new Intent(MainActivity.this, Activity_register.class);
                startActivity(i);
                overridePendingTransition(R.anim.anim_in, R.anim.anim_none);
            }
        });
    }


    private String getPreviousSelectedCities(){

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String selectedCitiesList = sharedPref.getString("selectedCitiesList", "");

        return  selectedCitiesList;
    }


    private void setSelectedCities(String cityTo, String cityFrom){

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String selectedCitiesList = sharedPref.getString("selectedCitiesList", "");

        boolean updateList = false;

        if(selectedCitiesList.contains(cityTo) == false){

            updateList = true;
            selectedCitiesList = selectedCitiesList + "-"+cityTo;
        }

        if(selectedCitiesList.contains(cityFrom) == false){

            updateList = true;
            selectedCitiesList = selectedCitiesList + "-"+cityFrom;
        }

        if(updateList){

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("selectedCitiesList", selectedCitiesList);
            editor.commit();
        }

    }

    public void callAlertBox(String title,String error ) {
        LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        View v = inflater.inflate(R.layout.textview, null);


        TextView title_tv = (TextView) v.findViewById(R.id.alert_title);
        title_tv.setText(title);//getResources().getString(R.string.internet_connection_error_title));

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCustomTitle(title_tv)
                .setMessage(error)
                .setCancelable(false)
                .setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        tryAgaintoCallService();

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

        if(progress != null) {
            progress.dismiss();
        }

        Button b = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        b.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.app_theme_color));

    }

    public String get_today_date() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String today_date = "";
        if (month > 9 && day > 9) {
            today_date = year + "-" + month + "-" + day;
        } else if (month < 10 && day < 10) {
            today_date = year + "-" + "0" + month + "-" + "0" + day;
        } else if (month > 9 && day < 10) {
            today_date = year + "-" + month + "-" + "0" + day;
        } else if (month < 10 && day > 9) {
            today_date = year + "-" + "0" + month + "-" + day;
        }


        return today_date;
    }


    public String change_date_form(String date) {
        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        String day = date.substring(8, 10);
        String month = date.substring(5, 7);
        String year = date.substring(0, 4);

        int day_int = Integer.parseInt(day);
        int month_int = Integer.parseInt(month);
        int year_int = Integer.parseInt(year);

        Calendar calendar = Calendar.getInstance();
        calendar.set(year_int, month_int - 1, day_int);

        int day_of_weak_int = calendar.get(calendar.DAY_OF_WEEK);
        String day_of_weak = days[day_of_weak_int - 1];


        String final_date = day + "-" + months[month_int - 1] + "-" + year + ", " + day_of_weak;


        return final_date;
    }

    private boolean isNetworkConnected(boolean reconnect) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean status = cm.getActiveNetworkInfo() != null;


        if(status){

           return true;
        }else{

            if(reconnect){

                callAlertBox(getResources().getString(R.string.internet_connection_error_title),getResources().getString(R.string.internet_connection_error_message_try_again));

            }
            return false;
        }

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
        Intent i = new Intent(MainActivity.this, Activity_AdverImageView.class);
        i.putExtra("id", position);
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

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }




    @Override
    protected void onResume() {
        super.onResume();


        if (TRAVEL_DATA.FROM_CITY_NAME != null) {
            Get_From_Cities_et.setText(TRAVEL_DATA.FROM_CITY_NAME);
        }

        if (TRAVEL_DATA.TO_CITY_NAME != null) {
            Get_To_Cities_et.setText(TRAVEL_DATA.TO_CITY_NAME);
        }


    }


    public void activity_dismiss() {
        TRAVEL_DATA.FROM_CITY_NAME = null;
        TRAVEL_DATA.TO_CITY_NAME = null;
        this.finish();
        overridePendingTransition(R.anim.anim_none, R.anim.anim_out);
    }


    @Override
    public void onBackPressed() {

        TRAVEL_DATA.FROM_CITY_NAME = null;
        TRAVEL_DATA.TO_CITY_NAME = null;
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_none, R.anim.anim_out);


    }


}
