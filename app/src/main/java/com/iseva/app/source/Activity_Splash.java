package com.iseva.app.source;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.iseva.app.source.travel.Activity_first;

import org.json.JSONObject;

import java.io.IOException;

public class Activity_Splash extends AppCompatActivity {
    int SPLASH_TIME_OUT = 1000;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console.
     */
    String SENDER_ID = "388638509308";

    GoogleCloudMessaging gcm;
    Context context;
    String regid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        inti();

    }


    private void timeHandler(){

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                navigation();

            }
        }, SPLASH_TIME_OUT);
    }

    private void navigation() {
       /* Object_AppConfig config = new Object_AppConfig(this);
        if (config.getIsCitySelected()) {
            Intent i = new Intent(Activity_Splash.this, Activity_Home.class);
            startActivity(i);
        } else {
            Intent i = new Intent(Activity_Splash.this, Activity_City_Choose.class);
            startActivity(i);
        }*/

        Intent i = new Intent(Activity_Splash.this, Activity_first.class);
        startActivity(i);

        // close this activity
        finish();
    }


    private void inti() {
        resizeImages();
        context = getApplicationContext();
        Object_AppConfig configObj = new Object_AppConfig(context);
        // Check device for Play Services APK. If check succeeds, proceed with
        //  GCM registration.
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = configObj.getRegistrationId(context);

            if (regid.isEmpty()) {
                registerInBackground();
               // timeHandler();
            }else{
                timeHandler();
            }
        } else {
            timeHandler();
            Log.i("SUSHIL", "No valid Google Play Services APK found.");
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.d("SUSHIL", "This device is not supported - Google Play Services.");
                finish();
            }
            return false;
        }
        return true;
    }

   public void registerInBackground() {
    try {
        Custom_ConnectionDetector con = new Custom_ConnectionDetector(this);
        if(con.isConnectingToInternet()) {
            new AsyncTask() {
                @Override
                protected Object doInBackground(Object... params) {
                    // TODO Auto-generated method stub
                    String msg = "";
                    try {
                        Object_AppConfig configObj = new Object_AppConfig(context);
                        if (gcm == null) {
                            gcm = GoogleCloudMessaging.getInstance(context);
                        }
                        regid = gcm.register(SENDER_ID);
                        msg = "Device registered, registration ID=" + regid;
                        Log.i("SUSHIL", "sushil " + regid);
                        // You should send the registration ID to your server over HTTP,
                        // so it can use GCM/HTTP or CCS to send messages to your app.
                        // The request to your server should be authenticated if your app
                        // is using accounts.


                        // For this demo: we don't need to send it because the device
                        // will send upstream messages to a server that echo back the
                        // message using the 'from' address in the message.

                        // Persist the registration ID - no need to register again.
                        configObj.storeRegistrationId(context, regid);
                        if (regid != null)
                            sendRegistrationIdToBackend();

                    } catch (IOException ex) {
                        msg = "Error :" + ex.getMessage();
                        timeHandler();

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return msg;
                }
            }.execute(null, null, null);
        }else{
            timeHandler();
        }
      }catch (Exception e){
         timeHandler();
        e.printStackTrace();
     }
    }

    public void resizeImages() {
        ImageView imgViewLogo = (ImageView) findViewById(R.id.imgLogoXB);

        int screenWidth = Globals.getScreenSize(this).x;
        int logoWidth = screenWidth / 2;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.xb, options);
        logo = Globals.scaleToWidth(logo, logoWidth);
        imgViewLogo.setImageBitmap(logo);
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
                    timeHandler();
                }
            });

            Custom_VolleyAppController.getInstance().addToRequestQueue(
                    jsonObjectRQST);
        } catch (Exception e) {
            e.printStackTrace();
            timeHandler();
        }
    }

    private void parseResponce(JSONObject obj){
            if(obj==null){
                return;
            }else{
               if(obj.has("success")){
                  navigation();
               }
            }
    }

}
