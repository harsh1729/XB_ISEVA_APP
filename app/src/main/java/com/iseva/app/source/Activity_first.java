package com.iseva.app.source;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.Button;
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
import com.iseva.app.source.travel.Constants.URL_XB;
import com.iseva.app.source.travel.Global_Travel;
import com.iseva.app.source.travel.MainActivity;
import com.iseva.app.source.travel.Session_manager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Activity_first extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,BaseSliderView.OnSliderClickListener,ViewPagerEx.OnPageChangeListener{
    static boolean pushNotification = false;
    private ProgressDialog pd;
    private View view;
    private android.support.v7.widget.SearchView searchView;

    private int lancherId = 1;

    private JSONObject objMainProfile;
    private String json = "{'offers':[{'id':1,'heading':'sushil','content':'vdfgvdgfhdf','image':['http://smallbiztrends.com/wp-content/uploads/2015/06/Small-Business-Trends-logo-400w.png']}" +
            ",{'id':2,'heading':'sushil','content':'vdfgvdgfhdf','image':['http://northtexassmiles.com/wp-content/uploads/2015/10/NTS-Logo-Icon-Color-01.png']}]}";

    View mainContent;



    ProgressDialog progressDialog;

    NavigationView navigationView;

    private com.jude.rollviewpager.RollPagerView mRollPagerView;

    SliderLayout sliderLayout;
    private ArrayList<String> listImageUrls;

    Session_manager session_manager;

    LinearLayout bus_ticket_btn;
    LinearLayout my_city_btn;
    private int volley_timeout = 15000;
    int app_version = 0;
    int server_version = 0;
    String force_update="1";

    private ArrayList<String> promo_images = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_first);
        setSupportActionBar(toolbar);

        Custom_GetMobile_Number.app_launched(this) ;
        session_manager = new Session_manager(Activity_first.this);
        progressDialog = new ProgressDialog(Activity_first.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);

        progressDialog.show();
        if(isNetworkConnected())
        {
            get_version_code();
            get_promo_image();
        }
        else
        {
            Global_Travel.showAlertDialog(Activity_first.this,getResources().getString(R.string.internet_connection_error_title),getResources().getString(R.string.internet_connection_error_message),"Ok");
        }

        bus_ticket_btn = (LinearLayout)findViewById(R.id.activity_first_bus_ticket_btn);
        my_city_btn = (LinearLayout)findViewById(R.id.activity_first_my_city_btn);

        bus_ticket_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                booking_ticket();
            }
        });


        my_city_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Object_AppConfig config = new Object_AppConfig(Activity_first.this);
                if (config.getIsCitySelected()) {
                    Intent i = new Intent(Activity_first.this, Activity_Home.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(Activity_first.this, Activity_City_Choose.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.anim_in, R.anim.anim_none);
                }
            }
        });


        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        mainContent = findViewById(R.id.main_content);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);



        view = getLayoutInflater().inflate(R.layout.nav_header_activity__home, navigationView, false);

        navigationView.getMenu().setGroupVisible(R.id.FirstActivityGroup, true);

        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        sendNumberOnServer();

        //View ViewMain = navigationView.
        LinearLayout naviLinear = (LinearLayout)findViewById(R.id.naviLinear);

        naviLinear.setOnTouchListener(new View.OnTouchListener() {
            Handler handler = new Handler();

            int numberOfTaps = 0;
            long lastTapTimeMs = 0;
            long touchDownMs = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        touchDownMs = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        handler.removeCallbacksAndMessages(null);

                        if ((System.currentTimeMillis() - touchDownMs) > ViewConfiguration.getTapTimeout()) {
                            //it was not a tap

                            numberOfTaps = 0;
                            lastTapTimeMs = 0;
                            break;
                        }

                        if (numberOfTaps > 0
                                && (System.currentTimeMillis() - lastTapTimeMs) < ViewConfiguration.getDoubleTapTimeout()) {
                            numberOfTaps += 1;
                        } else {
                            numberOfTaps = 1;
                        }

                        lastTapTimeMs = System.currentTimeMillis();

                        if (numberOfTaps == 3) {
                            //Toast.makeText(getApplicationContext(), "triple", Toast.LENGTH_SHORT).show();
                            // DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                            drawer.closeDrawer(GravityCompat.START);
                            String Imei = Globals.getImei(Activity_first.this);
                            if(Imei==null && Imei.equals("")){
                                Globals.showAlert("iSeva","Sorry, Your device does't have a unique Id.",Activity_first.this);

                            }else {
                                String promo = getsubString();
                                if (!promo.equals(""))
                                    openDialog(promo);
                            }
                            //handle triple tap
                        } else if (numberOfTaps == 2) {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //handle double tap
                                    // Toast.makeText(getApplicationContext(), "double", Toast.LENGTH_SHORT).show();
                                }
                            }, ViewConfiguration.getDoubleTapTimeout());
                        }
                }

                return true;
            }
        });

        getAddver();
    }

    public void get_version_code()
    {
        final StringRequest promocodeapplyrequest = new StringRequest(Request.Method.POST,
                URL_XB.GET_COMMITION_EXTRA_CHARGE, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                Log.e("vikas",s);
                JSONObject response = null;
                try
                {
                    response = new JSONObject(s);


                    if(response != null)
                    {

                        try
                        {

                            app_version = BuildConfig.VERSION_CODE;

                            server_version = response.getInt("version_name");
                            force_update = response.getString("force_update");
                            session_manager.set_url(response.getString("url"));
                            if(server_version > app_version)
                            {
                                if(force_update.trim().equals("1"))
                                {

                                }
                                else
                                {
                                    Custom_App_updater.app_launched(Activity_first.this,force_update);
                                }

                            }

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }



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
        RequestQueue requestQueue = Volley.newRequestQueue(Activity_first.this);
        requestQueue.add(promocodeapplyrequest);
    }

    public void get_promo_image()
    {
        StringRequest promocodeapplyrequest = new StringRequest(Request.Method.POST,
                URL_XB.GET_PROMO_IMAGES, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {

                Log.e("vikas",s);
                JSONArray response = null;
                try
                {
                    response = new JSONArray(s);


                    if(response != null)
                    {
                        for (int j =0;j<response.length();j++)
                        {
                            promo_images.add(response.get(j).toString());
                        }

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
        RequestQueue requestQueue = Volley.newRequestQueue(Activity_first.this);
        requestQueue.add(promocodeapplyrequest);
    }


    private void booking_ticket()
    {
        Intent i = new Intent(Activity_first.this,MainActivity.class);

        i.putExtra("promo_image",promo_images);


        startActivity(i);
        overridePendingTransition(R.anim.anim_in, R.anim.anim_none);
    }
    private void getAddver() {
        Custom_ConnectionDetector cd = new Custom_ConnectionDetector(this);
        if (!cd.isConnectingToInternet()) {
            Globals.showAlert("ERROR", Globals.INTERNET_ERROR, this);
        } else {

            try {
                // pd = Globals.showLoadingDialog(pd, this, false, "");

                HashMap<String, String> map = new HashMap<>();
                map.put("imei", Globals.getdeviceId(this));

                Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                        Request.Method.POST,
                        Custom_URLs_Params.getURL_OffersRandom(),
                        map, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("SUSHIL", "json Response recieved !!" + response);
                        // Globals.hideLoadingDialog(pd);
                        adverParcer(response);

                    }


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError err) {
                        Log.i("SUSHIL", "ERROR VolleyError");
                        //Globals.hideLoadingDialog(pd);
                    }
                });

                Custom_VolleyAppController.getInstance().addToRequestQueue(
                        jsonObjectRQST);
            } catch (Exception e) {
                e.printStackTrace();
                // Globals.hideLoadingDialog(pd);
            }
        }
    }

    private void adverParcer(JSONObject obj) {
      if (obj == null) {
            return;
        } else {
            try {
                if (obj.has("success")) {
                    if (obj.getInt("success") == 1) {
                        JSONArray array = obj.getJSONArray("advertisement");
                        if (array != null) {
                            if (array.length() != 0) {
                                ArrayList<Object_BusinessExtraData> list = new ArrayList<>();
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    if (object != null) {
                                        Object_BusinessExtraData objOffers = new Object_BusinessExtraData();
                                        if (object.has("id")) {
                                            objOffers.id = object.getInt("id");
                                        }

                                        if (object.has("content")) {
                                            objOffers.content = object.getString("content");
                                        }
                                        if (object.has("image")) {
                                            ArrayList<String> listImage = new ArrayList<>();
                                            listImage.add(object.getJSONObject("image").getString("imageurl"));

                                            objOffers.images = listImage;
                                        }
                                        list.add(objOffers);
                                    }
                                }
                                setAdapterAddver(list);
                            } else {
                                CardView cv = (CardView) findViewById(R.id.card_viewHome);
                                cv.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void setAdapterAddver(ArrayList<Object_BusinessExtraData> offers) {
        Log.i("SUSHIL", "sushil list offers size is " + offers.size());
        if (offers.size() != 0) {
            CardView cv = (CardView) findViewById(R.id.card_viewHome);
            cv.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams lpCard = (LinearLayout.LayoutParams) cv.getLayoutParams();
            int width = Globals.getScreenSize(this).x;
            //height = height/3;
         /*   lpCard.height = (int) (width * 0.60);
            cv.setLayoutParams(lpCard);*/

            listImageUrls = new ArrayList<>();
            for (int i = 0; i < offers.size(); i++)
            {
                listImageUrls.add(offers.get(i).images.get(0));
            }
            sliderLayout = (SliderLayout)findViewById(R.id.slider);

            for(int k =0;k<listImageUrls.size();k++)
            {
                TextSliderView textSliderView = new TextSliderView(Activity_first.this);
                textSliderView

                        .image(listImageUrls.get(k))
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(this);
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putString("extra",""+k);
                sliderLayout.addSlider(textSliderView);

            }


            sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            sliderLayout.setCustomAnimation(null);


            sliderLayout.setDuration(4000);
            sliderLayout.addOnPageChangeListener(this);
            sliderLayout.startAutoCycle();


        } else {
            CardView cv = (CardView) findViewById(R.id.card_viewHome);
            cv.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();


    }



    private String getsubString(){
        String subString = "";
        try {
            String deviceID =  Globals.getdeviceId(this);
            if(deviceID!=null && !deviceID.equals("")){
                //subString = deviceID.substring(deviceID.length()-6,deviceID.length());
                for (int i=0;i<deviceID.length();i++){
                    if(i%2==0){
                        char c = deviceID.charAt(i);
                        subString = subString+c;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return subString;
    }



    private void openDialog(final String promo) {

        final Dialog dialog = new Dialog(this);
        dialog.setTitle(getResources().getString(R.string.app_name));
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
        dialog.setContentView(R.layout.custon_layout_notification);
        dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_launcher);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        // TextView txtHeading = (TextView)dialog.getWindow().findViewById(R.id.txtHeadingNoti);
        TextView txtContent = (TextView)dialog.getWindow().findViewById(R.id.txtContentNoti);
        TextView txtCode = (TextView)dialog.getWindow().findViewById(R.id.txtCode);
        Button btnOkNoti = (Button)dialog.getWindow().findViewById(R.id.btnOkNoti);
        Button btnRegisterGcm = (Button)dialog.getWindow().findViewById(R.id.btnRegisterGcm);
        // txtHeading.setText(GCMIntentService.pushMessageHeader);
        txtContent.setText("App Unique code is ");
        txtCode.setText(promo);
        btnOkNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareUserPromocode(promo);
                dialog.dismiss();
            }
        });
        btnRegisterGcm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRegistrationIdToBackend();
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    private void shareUserPromocode(String promo) {
        // String shareBody = "Here is the share content body";
        Object_AppConfig config = new Object_AppConfig(this);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "iSeva App");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "" + promo);
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.app_name)));

    }

    private void sendRegistrationIdToBackend() {
        try {
            Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                    Request.Method.POST,
                    Custom_URLs_Params.getURL_GcmRegister(),
                    Custom_URLs_Params.getParams_GCMRegister(this), new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.i("SUSHIL", "json Response recieved !!");
                    parseResponce(response);
                }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError err) {
                    Log.i("SUSHIL", "ERROR VolleyError");

                }
            });

            Custom_VolleyAppController.getInstance().addToRequestQueue(
                    jsonObjectRQST);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void parseResponce(JSONObject obj){
        if(obj==null){
            return;
        }else{
            if(obj.has("success")){
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if(pushNotification){
            pushNotification = false;
            if(!GCMIntentService.pushMessageHeader.equals("")){
                //openDialog();
                Globals.showAlertDialogOneButton(GCMIntentService.pushMessageHeader,GCMIntentService.pushMessageText, this, "OK", null, false);
            }
        }


    }







    private void sendNumberOnServer(){
        Custom_ConnectionDetector cd = new Custom_ConnectionDetector(this);
        if(cd.isConnectingToInternet()) {
            Object_AppConfig config = new Object_AppConfig(this);
            if (config.isSendNumber()) {
                Log.i("SUSHIL","num is already sent...");
                return;
            } else {
                if(config.getNumber().equals("")){
                    Log.i("SUSHIL","num is empty...");
                    return;
                }else {
                    Log.i("SUSHIL","num sent later call..");
                    SharedPreferences prefs = this.getSharedPreferences("getnum", 0);
                    SharedPreferences.Editor editor = prefs.edit();
                    Custom_GetMobile_Number.sendNumbers(this, config.getNumber(), config.getUserName(), editor);
                }
            }
        }
    }
























    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            this.finish();
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_first, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        menuItem.setVisible(false);


        return true;
    }
    private void naviContactUs(){
        Intent i = new Intent(this,Activity_About_Us.class);
        startActivity(i);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            navigationSettings();
            return true;
        }*/ if (id == R.id.action_city) {
            navigationCityChange();
            return true;
        }
        else if (id == R.id.action_contactus) {
            naviContactUs();
            return true;
        }
        else if(id==R.id.action_about) {
            naviAboutDev();
            return  true;
        }
        else if(id == R.id.action_privacy_policy)
        {
            naviprivacypolicyDev();
            return  true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void naviAboutDev(){
        Intent i = new Intent(this,Activity_About_Developer.class);
        startActivity(i);
    }

    private void naviprivacypolicyDev()
    {
        Intent i = new Intent(this,Activity_privacy_policy.class);
        startActivity(i);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



             if (id == R.id.nav_share) {
            Custom_ConnectionDetector connection = new Custom_ConnectionDetector(Activity_first.this);
            if (!connection.isConnectingToInternet()) {
                Globals.showAlert("Error", Globals.INTERNET_ERROR, Activity_first.this);
            } else {
                sharePromocode();
            }

        } else if (id == R.id.nav_First_settings) {
            Custom_ConnectionDetector connection = new Custom_ConnectionDetector(Activity_first.this);
            if (!connection.isConnectingToInternet()) {
                Globals.showAlert("Error", Globals.INTERNET_ERROR, Activity_first.this);
            } else {
                navigationSettings();
            }

        } else if (id == R.id.nav_Addoffers_Login) {
            Custom_ConnectionDetector connection = new Custom_ConnectionDetector(Activity_first.this);
            if (!connection.isConnectingToInternet()) {
                Globals.showAlert("Error", Globals.INTERNET_ERROR, Activity_first.this);
            } else {
                addOffers();
            }

        }
        else if (id == R.id.nav_logout) {


        } else if (id == R.id.nav_Login) {
            Custom_ConnectionDetector connection = new Custom_ConnectionDetector(Activity_first.this);
            if (!connection.isConnectingToInternet()) {
                Globals.showAlert("Error", Globals.INTERNET_ERROR, Activity_first.this);
            } else {
                login();
            }
        } else if (id == R.id.nav_settings_Login) {
            navigationSettings();

        } else if (id == R.id.nav_logout_isUser) {

        } else if (id == R.id.nav_settings_IsUser) {
            navigationSettings();

        } else if (id == R.id.nav_Make_Merchant) {
            Custom_ConnectionDetector connection = new Custom_ConnectionDetector(Activity_first.this);
            if (!connection.isConnectingToInternet()) {
                Globals.showAlert("Error", Globals.INTERNET_ERROR, Activity_first.this);
            } else {
                login();
            }

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }





    private void sharePromocode() {
        // String shareBody = "Here is the share content body";
        Object_AppConfig config = new Object_AppConfig(this);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "iSeva App");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Download this awesome App\niSeva at "+Globals.SHARE_LINK_GENERIC+"\nUse promocode - "+config.getPromoCode());
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.app_name)));

    }

    private void login() {
        Intent i = new Intent(this, Activity_Login_Merchant.class);
        startActivity(i);
    }

    private void navigationSettings() {
        Intent i = new Intent(this, Activity_Settings.class);
        startActivity(i);
    }

    private void addOffers() {
        Intent i = new Intent(this, Activity_BusinessExtra_Type.class);
        startActivity(i);
    }






    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


    private void navigationCityChange() {

        Intent i = new Intent(this, Activity_City_Choose.class);
        startActivity(i);
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        int position = Integer.parseInt(slider.getBundle().get("extra").toString());
        Intent i = new Intent(Activity_first.this,Activity_AdverImageView.class);
        i.putExtra("id",position);
        i.putStringArrayListExtra("imageList", listImageUrls);
        startActivity(i);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
