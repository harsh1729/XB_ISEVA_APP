package com.iseva.app.source;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;


public class Activity_Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,BaseSliderView.OnSliderClickListener,ViewPagerEx.OnPageChangeListener{
    static boolean pushNotification = false;
    private ProgressDialog pd;
    private View view;
    private android.support.v7.widget.SearchView searchView;
    private ViewPager mViewPager;
    private PageIndicator mIndicator;
    private Timer timer;
    private int pageIndex = 1;
    private int lancherId = 1;
    private Bitmap bit = null;
    private int size;
    private JSONObject objMainProfile;
    private String json = "{'offers':[{'id':1,'heading':'sushil','content':'vdfgvdgfhdf','image':['http://smallbiztrends.com/wp-content/uploads/2015/06/Small-Business-Trends-logo-400w.png']}" +
            ",{'id':2,'heading':'sushil','content':'vdfgvdgfhdf','image':['http://northtexassmiles.com/wp-content/uploads/2015/10/NTS-Logo-Icon-Color-01.png']}]}";

    View mainContent;

    HashMap<String,String> Hash_Offers_images ;
    SliderLayout sliderLayout;
    private ArrayList<String> listImageUrls;
    private int slider_position = 0;

    NavigationView navigationView;
    private float lastTranslate = 0.0f;
    private com.jude.rollviewpager.RollPagerView mRollPagerView;
    //private boolean alreadyHeader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*Call_PhoneListener phoneListener = new Call_PhoneListener(this);
        TelephonyManager telephony = (TelephonyManager)
                getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        Log.i("SUSHIL", "PhoneStateListener");*/
        //NavigationView nav = (NavigationView) findViewById(R.id.nav_view);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


        // setAdapterAddver();


        /*try {
            JSONObject obj = new JSONObject(json);
            offersParcer(obj);
        } catch (JSONException e) {
            e.printStackTrace();
            ;
        }*/


     final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        mainContent = findViewById(R.id.main_content);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        /*{
            @SuppressLint("NewApi")
            public void onDrawerSlide(View drawerView, float slideOffset)
            {
                float moveFactor = (navigationView.getWidth() * slideOffset);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                {
                    mainContent.setTranslationX(moveFactor);
                }
                else
                {
                    TranslateAnimation anim = new TranslateAnimation(lastTranslate, moveFactor, 0.0f, 0.0f);
                    anim.setDuration(0);
                    anim.setFillAfter(true);
                    mainContent.startAnimation(anim);

                    lastTranslate = moveFactor;
                }
            }
        };
    */




        view = getLayoutInflater().inflate(R.layout.nav_header_activity__home, navigationView, false);
        // View view = getLayoutInflater().inflate(R.layout.nav_header_activity__home,null);
        //navigationView.removeHeaderView(view);
        // alreadyHeader = false;
        navigationView.getMenu().setGroupVisible(R.id.LogoutGroup, true);
        //addHeader();
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        inti();
        /*Custom_ConnectionDetector connection = new Custom_ConnectionDetector(Activity_Home.this);
        if (!connection.isConnectingToInternet()) {
            Globals.showAlert("Error", Globals.INTERNET_ERROR, Activity_Home.this);
        }*/
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
                            String Imei = Globals.getImei(Activity_Home.this);
                            if(Imei==null && Imei.equals("")){
                                Globals.showAlert("iSeva","Sorry, Your device does't have a unique Id.",Activity_Home.this);

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
       /* try {
            if (obj.has("offers")) {
                JSONArray offersArray = obj.getJSONArray("offers");
                ArrayList<Object_Offers> listoffers = new ArrayList<>();
                for (int i = 0; i < offersArray.length(); i++) {
                    JSONObject objoffersJson = offersArray.getJSONObject(i);
                    if (objoffersJson != null) {
                        Object_Offers objOffers = new Object_Offers();
                        objOffers.id = objoffersJson.getInt("id");
                        objOffers.heading = objoffersJson.getString("heading");
                        objOffers.content = objoffersJson.getString("content");
                        JSONArray offersArrayImage = objoffersJson.getJSONArray("image");
                        ArrayList<String> listImage = new ArrayList<>();
                        for (int j = 0; j < offersArrayImage.length(); j++) {
                             JSONObject objImage = offersArrayImage.getJSONObject(j);
                            String url = objImage.getString("imageurl");
                            if (url != null && !url.isEmpty()) {
                                //downloadImage(url);
                                listImage.add(url);
                            }

                        }
                        objOffers.offersimage = listImage;
                        listoffers.add(objOffers);
                    }

                }
                setAdapterAddver(listoffers);

            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }*/

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
                                        /*if (object.has("heading")) {

                                            objOffers.heading = object.getString("heading");

                                        }*/
                                        if (object.has("content")) {
                                            objOffers.content = object.getString("content");
                                        }
                                        if (object.has("image")) {
                                            ArrayList<String> listImage = new ArrayList<>();
                                            listImage.add(object.getJSONObject("image").getString("imageurl"));
                                            /*JSONArray imageArray = object.getJSONArray("images");
                                            ArrayList<String> listImage = new ArrayList<>();
                                            for (int j = 0; j < imageArray.length(); j++) {
                                                // String url = imageArray.getString(j);
                                                JSONObject objImage = imageArray.getJSONObject(j);
                                                String url = objImage.getString("imageurl");
                                                if (url != null) {
                                                    listImage.add(url);
                                                }
                                            }*/
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
            lpCard.height = (int) (width * 0.60);
            cv.setLayoutParams(lpCard);

            listImageUrls = new ArrayList<>();
            for (int i = 0; i < offers.size(); i++)
            {
                listImageUrls.add(offers.get(i).images.get(0));
            }
            sliderLayout = (SliderLayout)findViewById(R.id.slider);

            for(int k =0;k<listImageUrls.size();k++)
            {
                TextSliderView textSliderView = new TextSliderView(Activity_Home.this);
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

            //Custom_Adapter_Home_Addver adapter = new Custom_Adapter_Home_Addver(this, false, offers);
            // mRollPagerView = (com.jude.rollviewpager.RollPagerView)findViewById(R.id.viewPager);
            //  mRollPagerView.setHintView(new ColorPointHintView(this, Color.WHITE, Color.BLACK));
            // mRollPagerView.setAdapter(adapter); // vikas



          /*  mViewPager = (ViewPager) findViewById(R.id.view_pager);
            mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
            Custom_Adapter_Home_Addver adapter = new Custom_Adapter_Home_Addver(this, false, offers);
            mViewPager.setOffscreenPageLimit(1);
            mViewPager.setAdapter(adapter);
            mIndicator.setViewPager(mViewPager);
            size = offers.size();
            pageSwitcher();*/
        } else {
            CardView cv = (CardView) findViewById(R.id.card_viewHome);
            cv.setVisibility(View.GONE);
        }
    }
    /*@Override
    public void onNewIntent(Intent intent) {

    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();

       // unbindDrawables(findViewById(R.id.RootView));
      //  System.gc();
    }

    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
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
        /*/Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("notification")) {
                //create a notification alert
                openDialog();
            }
        }*/
        if(pushNotification){
            pushNotification = false;
            if(!GCMIntentService.pushMessageHeader.equals("")){
                //openDialog();
                Globals.showAlertDialogOneButton(GCMIntentService.pushMessageHeader,GCMIntentService.pushMessageText, this, "OK", null, false);
            }
        }
        addHeader();

    }





    private void inti() {


        DBHandler_Access databaseAccess = DBHandler_Access.getInstance(this);
        databaseAccess.open();
        ArrayList<Object_Category> catList = databaseAccess.getCategory();
        databaseAccess.close();
        categoryView(catList);


        /*Custom_ConnectionDetector connection = new Custom_ConnectionDetector(this);
        if (!connection.isConnectingToInternet()) {
            Globals.showAlert("Error", Globals.INTERNET_ERROR, this);
        } else {
            Custom_AppRater.app_launched(this);
        }*/
    }




    private void addHeader() {
        //Log.i("SUSHIL", "onresume call........;" + Globals.isHeader);
        Object_AppConfig objConfig = new Object_AppConfig(this);
        NavigationView nav = (NavigationView) findViewById(R.id.nav_view);
        // setUnCheckItem();
        // view = getLayoutInflater().inflate(R.layout.nav_header_activity__home, nav, false);
        if (objConfig.getboolIslogin()) {
               getProfile();
            if (objConfig.isMerchant()) {
                if (!Globals.isHeader) {
                    nav.addHeaderView(view);
                    objConfig.setboolnavHeader(true);
                    Globals.isHeader = true;

                } else {
                    if (lancherId == 1) {
                        nav.addHeaderView(view);
                        objConfig.setboolnavHeader(true);
                        Globals.isHeader = true;
                    }
                }
                nav.getMenu().setGroupVisible(R.id.LoginGroup, true);
                nav.getMenu().setGroupVisible(R.id.LogoutGroup, false);
                nav.getMenu().setGroupVisible(R.id.isUserLogin, false);


            } else {
                nav.getMenu().setGroupVisible(R.id.LoginGroup, false);
                nav.getMenu().setGroupVisible(R.id.LogoutGroup, false);
                nav.getMenu().setGroupVisible(R.id.isUserLogin, true);
            }


            lancherId++;
        }
    }

    /*private void setUnCheckItem(){
        NavigationView nav = (NavigationView) findViewById(R.id.nav_view);
       *//* nav.getMenu().setGroupVisible(R.id.LoginGroup, true);
        nav.getMenu().setGroupVisible(R.id.LogoutGroup, true);
        nav.getMenu().setGroupVisible(R.id.isUserLogin, true);*//*
        nav.invalidate();

    }*/

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



    private void getProfile() {

        try {
            HashMap<String, String> map = new HashMap<>();
            Object_AppConfig config = new Object_AppConfig(this);
            map.put("userid",config.getUserId()+ "");
            Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                    Request.Method.POST,
                    Custom_URLs_Params.getURL_Get_Service_Provider_Data(),
                    map, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.i("SUSHIL", "json Response recieved !!"+response);

                    parseProfiledata(response);

                }


            }, new Response.ErrorListener() {
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

    private void parseProfiledata(final JSONObject obj) {
        if (obj == null) {
            return;
        } else {
            try {
                if (obj.getInt("success") == 1) {
                    /*
                    if (obj.has("firmcontact")) {
                        if (obj.getString("firmcontact") != null) {
                            String contact = obj.getString("firmcontact");
                            TextView txtEmail = (TextView) view.findViewById(R.id.txtCompanyProfileEmail);
                            txtEmail.setText(contact);
                        }
                    }*/
                    if (obj.has("name")) {
                        if (obj.getString("name") != null) {
                            String contact = obj.getString("name");
                            if(!contact.isEmpty()){
                                TextView txtname = (TextView) view.findViewById(R.id.txtUserProfileName);
                                txtname.setText("Hi " +contact );
                            }

                        }
                    }
                   if (obj.has("firmname")) {
                       TextView txtName = (TextView) view.findViewById(R.id.txtCompanyName);
                        if (obj.getString("firmname") != null && !obj.getString("firmname").isEmpty()) {

                            txtName.setText(obj.getString("firmname"));

                        }else{
                            txtName.setVisibility(View.GONE);
                        }
                    }
                    if(obj.has("wallet")){
                        ImageView ImageView = (ImageView) view.findViewById(R.id.imgWalletInfo);
                        ImageView.setVisibility(View.VISIBLE);
                        TextView txtName = (TextView) view.findViewById(R.id.txtUserWallet);
                        txtName.setText("Wallet "+obj.getInt("wallet")+" Coins");

                    }
                    if(obj.has("promocode")){
                        TextView txtName = (TextView) view.findViewById(R.id.txtPromocode);
                        Object_AppConfig con = new Object_AppConfig(this);

                        if(obj.getString("promocode").isEmpty()){
                            txtName.setVisibility(View.GONE);
                        }else{
                            txtName.setText("Promocode "+obj.getString("promocode"));
                            con.setPromoCode(obj.getString("promocode"));

                        }
                    }
                    objMainProfile = obj;
                    ImageView imgy = (ImageView)findViewById(R.id.imgWalletInfo);
                    imgy.setClickable(true);
                    imgy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                            if (drawer.isDrawerOpen(GravityCompat.START)) {
                                drawer.closeDrawer(GravityCompat.START);
                            }
                            Globals.showAlert("iSeva","When you reach 1000 coins in your wallet , contact us for cash reward !",Activity_Home.this);
                        }
                    });
                    if (obj.has("image")) {
                        JSONObject objectImageProfile = obj.getJSONObject("image");
                        Custom_RoundedImageView img = (Custom_RoundedImageView) view.findViewById(R.id.imgCompanyProfileImage);
                        img.setImageBitmap(null);
                        Globals.loadImageIntoImageView(img, objectImageProfile.getString("imageurl"), this, R.drawable.default_user, R.drawable.default_user);
                    }

                   /* Button btn = (Button)findViewById(R.id.edit);
                    btn.setVisibility(View.VISIBLE);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });*/


                }


               /* if (obj.has("image")) {
                    String url = obj.getString("image");
                    ImageView img = (ImageView) view.findViewById(R.id.imgCompanyProfileImage);
                    if (url != null && !url.isEmpty())
                        Globals.loadImageIntoImageView(img, url, this);
                }
                if (obj.has("name")) {
                    TextView txtName = (TextView) view.findViewById(R.id.txtCompanyProfileName);
                    if (obj.getString("name") != null) {
                        txtName.setText(obj.getString("name"));
                    }
                }
                if (obj.has("email")) {
                    TextView txtEmail = (TextView) view.findViewById(R.id.txtCompanyProfileEmail);
                    if (obj.getString("email") != null) {
                        txtEmail.setText(obj.getString("email"));
                    }
                }
                if (obj.has("rating")) {
                    LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linearrating);
                    linearLayout.setVisibility(View.VISIBLE);
                    TextView txtRating = (TextView) view.findViewById(R.id.textViewRating);
                    if (obj.getString("rating") != null) {
                        txtRating.setText(obj.getString("rating"));
                    }
                }*/
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }




    private void naviEditProfile(JSONObject object){
        Intent i = new Intent(this,Activity_Category_Choose.class);
        i.putExtra("object", object.toString());
        i.putExtra("intent", "edit");
        startActivity(i);
    }

    private void categoryView(ArrayList<Object_Category> catList) {
        int marginDefault =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
        int width = (Globals.getScreenSize(this).x  -(marginDefault*3))/ 2;
        // int heightLinear = Globals.getScreenSize(this).y/3;
        int height = width;//(int) (Globals.getScreenSize(this).y / 3.5);
       final ScrollView sc = (ScrollView) findViewById(R.id.scrollView1);
        //sc.setSmoothScrollingEnabled(true);
        //sc.smoothScrollBy(0, sc.getScrollY());
       final LinearLayout scrollView = (LinearLayout) findViewById(R.id.scRollLinear);
        scrollView.removeAllViews();
        for (int i = 0; i < catList.size(); i++) {
            Log.i("SUSHIL","cnt i "+i);
            LinearLayout layout = new LinearLayout(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            /*layout.setGravity(Gravity.CENTER_HORIZONTAL);*/
            layout.setLayoutParams(lp);
            if (i % 2 == 0) {
                final Object_Category objectCategory = catList.get(i);
                View view = getLayoutInflater().inflate(R.layout.custom_card_view_home, layout, false);
                ImageView img = (ImageView) view.findViewById(R.id.imageViewCate_Home);
                TextView txtName = (TextView) view.findViewById(R.id.txtcategoryName);
                String imagename = objectCategory.image;
                // img.setImageBitmap(BitmapFactory.decodeByteArray(objectCategory.image, 0, objectCategory.image.length));
                int resID = getResources().getIdentifier(imagename, "drawable", getPackageName());
                //img.setBackgroundDrawable(getResources().getDrawable(resID));
                Picasso.with(this).load(resID).into(img);
                txtName.setText(objectCategory.name);
                LinearLayout.LayoutParams lpLinear = new LinearLayout.LayoutParams(width , LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams lpImg = new LinearLayout.LayoutParams(width - marginDefault * 2, height - marginDefault *2);
                img.setLayoutParams(lpImg);
                view.setLayoutParams(lpLinear);
                layout.addView(view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*if (objectCategory.parentId == 0)
                            goSubCategory(objectCategory.name, objectCategory.catId);
                        else {
                            if (objectCategory.id==13 || objectCategory.id==7){
                                navigationAllOffers();
                            }else{
                                navigationServiceProvider(objectCategory.catId);
                            }
                        }*/
                        Custom_ConnectionDetector connection = new Custom_ConnectionDetector(Activity_Home.this);
                        if (!connection.isConnectingToInternet()) {
                            Globals.showAlert("Error", Globals.INTERNET_ERROR, Activity_Home.this);
                        } else {
                            Object_AppConfig config = new Object_AppConfig(Activity_Home.this);
                            config.setCatId(objectCategory.catId);
                            TextView txtName = (TextView) v.findViewById(R.id.txtcategoryName);
                            config.setCateName(txtName.getText().toString());
                            if(objectCategory.catId==6)
                                getepaperUser();
                            else
                                checkDataCategory(objectCategory.catId, objectCategory.isbusiness);
                        }

                    }
                });
                i++;
                if (i < catList.size()) {
                    final Object_Category objectCategory1 = catList.get(i);
                    View view1 = getLayoutInflater().inflate(R.layout.custom_card_view_home, layout, false);
                    ImageView img1 = (ImageView) view1.findViewById(R.id.imageViewCate_Home);
                    TextView txtName1 = (TextView) view1.findViewById(R.id.txtcategoryName);
                    // img1.setImageBitmap(BitmapFactory.decodeByteArray(objectCategory1.image, 0, objectCategory1.image.length));
                    //img1.setBackgroundDrawable(getResources().getDrawable(R.drawable.default_user));
                    String imagename1 = objectCategory1.image;
                    // img.setImageBitmap(BitmapFactory.decodeByteArray(objectCategory.image, 0, objectCategory.image.length));
                    int resID1 = getResources().getIdentifier(imagename1, "drawable", getPackageName());
                    //img1.setBackgroundDrawable(getResources().getDrawable(resID1));
                    Picasso.with(this).load(resID1).into(img1);
                    txtName1.setText(objectCategory1.name);
                    LinearLayout.LayoutParams lpLinear1 = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
                    LinearLayout.LayoutParams lpImg1 = new LinearLayout.LayoutParams(width - marginDefault * 2, height- marginDefault * 2);
                    lpLinear1.leftMargin = marginDefault;
                    img1.setLayoutParams(lpImg1);
                    view1.setLayoutParams(lpLinear1);

                    layout.addView(view1);
                    view1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            /*if (objectCategory1.parentId == 0)
                                goSubCategory(objectCategory1.name, objectCategory1.catId);
                            else {
                                if (objectCategory1.id==13 || objectCategory1.id==7){
                                    navigationAllOffers();
                                }else{
                                    navigationServiceProvider(objectCategory1.catId);
                                }
                            }*/
                            Custom_ConnectionDetector connection = new Custom_ConnectionDetector(Activity_Home.this);
                            if (!connection.isConnectingToInternet()) {
                                Globals.showAlert("Error", Globals.INTERNET_ERROR, Activity_Home.this);
                            } else {
                                Object_AppConfig config = new Object_AppConfig(Activity_Home.this);
                                config.setCatId(objectCategory1.catId);
                                TextView txtName = (TextView) v.findViewById(R.id.txtcategoryName);
                                config.setCateName(txtName.getText().toString());
                                if(objectCategory1.catId==6)
                                    getepaperUser();
                                else
                                    checkDataCategory(objectCategory1.catId,objectCategory1.isbusiness);
                            }
                        }
                    });
                }

            }

            scrollView.addView(layout);

        }


        /*sc.post(new Runnable() {

            @Override
            public void run() {
                Log.i("SUSHIL","smoothScroll");
                sc.smoothScrollTo(0,scrollView.getHeight());
            }
        });*/
    }


    private void getepaperUser(){
        try {
            pd = Globals.showLoadingDialog(pd, this, false, "");
            HashMap<String, String> map = new HashMap<String, String>();

           // map.put("parentid", catid + "");
           // map.put("isbusiness",isbusiness+"");
            map.put("deviceid",Globals.getdeviceId(this));
            Log.i("SUSHIL", "map is call " + Custom_URLs_Params.getURL_GetEpaperUser() + "" + map);
            Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                    Request.Method.POST,
                    Custom_URLs_Params.getURL_GetEpaperUser(),
                    map,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            Log.i("SUSHIL", "json Response recieved !!" + response);
                            parseSubCateGory(response,1);

                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError err) {
                    Globals.hideLoadingDialog(pd);
                    Log.i("SUSHIL", "ERROR VolleyError" + err);
                    Globals.showShortToast(
                            Activity_Home.this,
                            Globals.MSG_SERVER_ERROR);

                }
            });

            Custom_VolleyAppController.getInstance().addToRequestQueue(
                    jsonObjectRQST);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkDataCategory(int catid,final int isbusiness){

        try {
            pd = Globals.showLoadingDialog(pd, this, false, "");
            HashMap<String, String> map = new HashMap<String, String>();

            map.put("parentid", catid + "");
            map.put("isbusiness",isbusiness+"");
            map.put("deviceid",Globals.getdeviceId(this));
            Log.i("SUSHIL", "map is call " + Custom_URLs_Params.getURL_GetSubCateHome() + "" + map);
            Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                    Request.Method.POST,
                    Custom_URLs_Params.getURL_GetSubCateHome(),
                    map,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            Log.i("SUSHIL", "json Response recieved !!" + response);
                            parseSubCateGory(response,isbusiness);

                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError err) {
                    Globals.hideLoadingDialog(pd);
                    Log.i("SUSHIL", "ERROR VolleyError" + err);
                    Globals.showShortToast(
                            Activity_Home.this,
                            Globals.MSG_SERVER_ERROR);

                }
            });

            Custom_VolleyAppController.getInstance().addToRequestQueue(
                    jsonObjectRQST);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseSubCateGory(JSONObject object,int isbusiness) {
        if (object == null) {
            return;
        } else {
            try {
                if (object.has("success")) {
                    int success = object.getInt("success");
                    Globals.hideLoadingDialog(pd);
                    if (object.has("categories")) {
                        JSONArray array = object.getJSONArray("categories");
                        if (array.length() != 0) {
                            goSubCategory(object);
                        } else {
                            //Globals.showAlert("Error", "Data not found", Activity_Home.this);
                            if(isbusiness==1)
                            navigationServiceProvider(object);
                            else
                                navigationAllOffers(object);
                        }
                    }
                    else if(object.has("data")){
                        JSONArray array = object.getJSONArray("data");
                        if(array.length() != 0) {
                            if (isbusiness == 0) {
                                navigationAllOffers(object);
                            }else{
                                navigationServiceProvider(object);
                            }
                        }else{
                            navigationServiceProvider(object);
                        }
                    }
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }

    }


    private void navigationServiceProvider(JSONObject obj){
        Intent i = new Intent(this,Activity_ServiceProvider.class);
        i.putExtra("object",obj.toString());
        startActivity(i);
    }

    private void goSubCategory(JSONObject array) {
        Intent i = new Intent(this, Activity_SubCategory.class);
        i.putExtra("category",array.toString());
        startActivity(i);
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

    private void Dialogback() {

        this.finish();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity__home, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        searchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                DBHandler_Access databaseAccess = DBHandler_Access.getInstance(Activity_Home.this);
                databaseAccess.open();
                ArrayList<Object_Category> catList = databaseAccess.getCategorySerchable(newText);
                databaseAccess.close();
                categoryView(catList);
                return false;
            }
        });

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

        return super.onOptionsItemSelected(item);
    }

    private void naviAboutDev(){
        Intent i = new Intent(this,Activity_About_Developer.class);
        startActivity(i);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Custom_ConnectionDetector connection = new Custom_ConnectionDetector(Activity_Home.this);
            if (!connection.isConnectingToInternet()) {
                Globals.showAlert("Error", Globals.INTERNET_ERROR, Activity_Home.this);
            } else {
                if(objMainProfile!=null)
                  naviEditProfile(objMainProfile);
            }

        } else if (id == R.id.nav_share) {
            Custom_ConnectionDetector connection = new Custom_ConnectionDetector(Activity_Home.this);
            if (!connection.isConnectingToInternet()) {
                Globals.showAlert("Error", Globals.INTERNET_ERROR, Activity_Home.this);
            } else {
                sharePromocode();
            }

        } else if (id == R.id.nav_settings) {
            Custom_ConnectionDetector connection = new Custom_ConnectionDetector(Activity_Home.this);
            if (!connection.isConnectingToInternet()) {
                Globals.showAlert("Error", Globals.INTERNET_ERROR, Activity_Home.this);
            } else {
                navigationSettings();
            }

        } else if (id == R.id.nav_Addoffers_Login) {
            Custom_ConnectionDetector connection = new Custom_ConnectionDetector(Activity_Home.this);
            if (!connection.isConnectingToInternet()) {
                Globals.showAlert("Error", Globals.INTERNET_ERROR, Activity_Home.this);
            } else {
                addOffers();
            }

        }/*else if (id == R.id.nav_AddAddver_Login) {
            addAddver();

        }*/
        else if (id == R.id.nav_logout) {
            logout();

        } /*else if (id == R.id.nav_buy_zone) {
            naviBuyZone();
            ;
        }*/ else if (id == R.id.nav_Login) {
            Custom_ConnectionDetector connection = new Custom_ConnectionDetector(Activity_Home.this);
            if (!connection.isConnectingToInternet()) {
                Globals.showAlert("Error", Globals.INTERNET_ERROR, Activity_Home.this);
            } else {
                login();
            }
        } else if (id == R.id.nav_settings_Login) {
            navigationSettings();

        } else if (id == R.id.nav_logout_isUser) {
            logout();
        } else if (id == R.id.nav_settings_IsUser) {
            navigationSettings();

        } else if (id == R.id.nav_Make_Merchant) {
            Custom_ConnectionDetector connection = new Custom_ConnectionDetector(Activity_Home.this);
            if (!connection.isConnectingToInternet()) {
                Globals.showAlert("Error", Globals.INTERNET_ERROR, Activity_Home.this);
            } else {
                login();
            }

        }/* else if (id == R.id.nav_profile_IsUser) {
            editProfile();

        } *//*else if (id == R.id.nav_Offers) {

            navigationAllOffers();

        }*//* else if (id == R.id.nav_Offers_isMerchant) {
            navigationAllOffers();

        }*/ /*else if (id == R.id.nav_Offers_IsUser) {
            navigationAllOffers();

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void navigationAllOffers(JSONObject array) {
        Intent i = new Intent(this, Activity_BusinessExtraShow.class);
        i.putExtra("array",array.toString());
        startActivity(i);
    }

    private void naviBuyZone() {
        Intent i = new Intent(this, Activity_Buy_Zone.class);
        startActivity(i);
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
    private void addAddver() {
        Intent i = new Intent(this, Activity_BusinessExtra_Type.class);
        startActivity(i);
    }



    /*private void setScrollviewSmoth(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            ValueAnimator realSmoothScrollAnimation =
                    ValueAnimator.ofInt(parentScrollView.getScrollY(), targetScrollY);
            realSmoothScrollAnimation.setDuration(500);
            realSmoothScrollAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
            {
                @Override
                public void onAnimationUpdate(ValueAnimator animation)
                {
                    int scrollTo = (Integer) animation.getAnimatedValue();
                    parentScrollView.scrollTo(0, scrollTo);
                }
            });

            realSmoothScrollAnimation.start();
        }
        else
        {
            parentScrollView.smoothScrollTo(0, targetScrollY);
        }
    }*/


    private void logout() {

        //  navigationView.add*/
        NavigationView navigationViewNew = (NavigationView) findViewById(R.id.nav_view);
        //View view = getLayoutInflater().inflate(R.layout.nav_header_activity__home, null);
        //view.setVisibility(View.GONE);

        /*View headerLayout =
                navigationViewNew.inflateHeaderView(R.layout.nav_header_activity__home);*/
        //headerLayout.setVisibility(View.GONE);
        navigationViewNew.removeHeaderView(view);
        // headerLayout.findViewById(R.id.navigationLayout).setVisibility(View.GONE);


        navigationViewNew.getMenu().setGroupVisible(R.id.LoginGroup, false);
        navigationViewNew.getMenu().setGroupVisible(R.id.LogoutGroup, true);
        navigationViewNew.getMenu().setGroupVisible(R.id.isUserLogin, false);

        Object_AppConfig objConfig = new Object_AppConfig(this);
        //objConfig.setboolnavHeader(false);
        Globals.isHeader = false;
        objConfig.setboolIslogin(false);
    }

    /*private void navigationOffers() {

        Intent i = new Intent(this, Activity_BusinessExtraShow.class);
        startActivity(i);
    }*/

    private void navigationCityChange() {

        Intent i = new Intent(this, Activity_City_Choose.class);
        startActivity(i);
    }


    private void checkVersoinUpdate() {
        try {
            pd = Globals.showLoadingDialog(pd, this, false, "");
            Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                    Request.Method.POST,
                    Custom_URLs_Params.getURL_AllCity(),
                    Custom_URLs_Params.getParams_Category(this), new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.i("SUSHIL", "json Response recieved !!");
                    Globals.hideLoadingDialog(pd);
                    insertCategoryDatabase(response);

                }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError err) {
                    Log.i("SUSHIL", "ERROR VolleyError");
                    Globals.hideLoadingDialog(pd);
                }
            });

            Custom_VolleyAppController.getInstance().addToRequestQueue(
                    jsonObjectRQST);
        } catch (Exception ex) {

            ex.printStackTrace();

        }
    }









    private void insertCategoryDatabase(JSONObject obj) {
        if (obj == null) {
            return;
        } else {
            try {

                if (obj.has("categories")) {

                    JSONArray cateArray = obj.getJSONArray("categories");
                    ArrayList<Object_Category> listCate = new ArrayList<>();
                    for (int i = 0; i < cateArray.length(); i++) {
                        JSONObject objCateJson = cateArray.getJSONObject(i);
                        if (objCateJson != null) {

                            Object_Category objCate = new Object_Category();
                            objCate.catId = objCateJson.getInt("catid");
                            objCate.name = objCateJson.getString("name");
                            objCate.parentId = objCateJson.getInt("parentid");
                            objCate.sort_order = objCateJson.getInt("sortorder");
                            objCate.isnable = objCateJson.getInt("isenable");

                            if (objCateJson.getJSONObject("image") != null) {
                                //String url = objCateJson.getJSONObject("image").getString("url");
                                String url = objCateJson.getJSONObject("image").getString("url");
                                if (url != null && !url.isEmpty()) {
                                    //downloadImage(url);
                                    objCate.image = objCateJson.getJSONObject("image").getString("name");
                                    int id = getResources().getIdentifier(objCate.image, "drawable", getPackageName());
                                }
                            }
                            listCate.add(objCate);
                        }

                    }
                    DBHandler_Access databaseAccess = DBHandler_Access.getInstance(this);
                    databaseAccess.open();
                    databaseAccess.insertCategory(listCate);
                    databaseAccess.close();


                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }





    private void downloadIma(String url) {


        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        //imageLoader.displayImage("http://suptg.thisisnotatrueending.com/archive/7390790/thumbs/1262444458213s.jpg", img);
        imageLoader.loadImage(this, "http://suptg.thisisnotatrueending.com/archive/7390790/thumbs/1262444458213s.jpg", new ImageLoadingListener() {
            @Override
            public void onLoadingStarted() {

            }

            @Override
            public void onLoadingFailed(FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(Bitmap bitmap) {
                bit = bitmap;
            }

            @Override
            public void onLoadingCancelled() {

            }
        });

    }

    private byte[] getBitmaptoByteArray(Bitmap bit) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bit.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            return byteArray;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }


    @Override
    public void onSliderClick(BaseSliderView slider) {
        int position = Integer.parseInt(slider.getBundle().get("extra").toString());
        Intent i = new Intent(Activity_Home.this,Activity_AdverImageView.class);
        i.putExtra("id",position);
        i.putStringArrayListExtra("imageList", listImageUrls);
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
}
