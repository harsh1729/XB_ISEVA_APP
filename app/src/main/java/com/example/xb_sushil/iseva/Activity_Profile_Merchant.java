package com.example.xb_sushil.iseva;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Activity_Profile_Merchant extends Activity {


    private ProgressDialog pd;
    private JSONObject objString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_merchant);
        inti();
        getProfile();
    }

    private void inti() {

        TextView txtHeader = (TextView) findViewById(R.id.txtHeader);
        txtHeader.setText("Profile");
        ImageView imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity_Profile_Merchant.this.finish();
            }
        });
        Globals.setAppFontTextView(this, txtHeader);
        //set font
        TextView txtName = (TextView) findViewById(R.id.txtUser);
        TextView txtProfile = (TextView) findViewById(R.id.txtProfile);
        TextView txtPromo = (TextView) findViewById(R.id.txtPromo);
        TextView txtPromoRealText = (TextView) findViewById(R.id.txtPromoRealText);
        TextView txtWallet = (TextView) findViewById(R.id.txtMyWallet);
        TextView txtMywalletRs = (TextView) findViewById(R.id.txtMywalletRs);
        // TextView txtLogOut = (TextView) findViewById(R.id.txtLogOut);

        TextView[] txtArray = {txtName, txtProfile,txtPromo,txtPromoRealText,txtWallet,txtMywalletRs};
        for (TextView txt : txtArray) {
            Globals.setAppFontTextView(this, txt);
        }

        txtProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
            }
        });

    }

    private void editProfile() {

        Object_AppConfig obj = new Object_AppConfig(this);
        if (obj.getboolIslogin()) {
            Intent i = new Intent(this, Activity_Category_Choose.class);
            i.putExtra("intent","edit");
            i.putExtra("object",objString.toString());
            startActivity(i);
        } else {
            Intent i = new Intent(this, Activity_Login_Merchant.class);
            startActivity(i);
        }
    }

    private void getProfile() {
        Custom_ConnectionDetector cd = new Custom_ConnectionDetector(this);
        if (!cd.isConnectingToInternet()) {

            Globals.showAlert("ERROR", Globals.INTERNET_ERROR, this);

        } else {
            try {
                pd = Globals.showLoadingDialog(pd, this, false, "");
                HashMap<String,String> map  = new HashMap<>();
                Object_AppConfig config = new Object_AppConfig(this);
                map.put("userid",config.getUserId()+"");
                Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                        Request.Method.POST,
                        Custom_URLs_Params.getURL_Get_Service_Provider_Data(),
                        map, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("SUSHIL", "json Response recieved !!");
                        Globals.hideLoadingDialog(pd);
                        //parseCateResponse(response);
                        parseProfiledata(response);
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
            } catch (Exception e) {
                e.printStackTrace();
                Globals.hideLoadingDialog(pd);
            }
        }
    }

    private void parseProfiledata(JSONObject obj) {
        if (obj == null) {
            return;
        } else {
            try {
                objString = obj;
                if (obj.getInt("success") == 1) {
                    if (obj.has("firmcontact")) {
                        if (obj.getString("firmcontact") != null) {
                            String contact = obj.getString("firmcontact");
                          //  TextView txtEmail = (TextView) view.findViewById(R.id.txtCompanyProfileEmail);
                           // txtEmail.setText(contact);
                        }
                    }
                    if (obj.has("image")) {
                        JSONObject objectImageProfile = obj.getJSONObject("image");
                        ImageView img = (ImageView)findViewById(R.id.imgprofile);
                        Globals.loadImageIntoImageView(img, objectImageProfile.getString("imageurl"), this, R.drawable.default_user, R.drawable.default_user);
                    }


                    if (obj.has("firmname")) {
                        if (obj.getString("firmname") != null) {
                            TextView txtName = (TextView) findViewById(R.id.txtUser);
                            txtName.setText(obj.getString("firmname"));

                        }
                    }

                }


            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }



   /* private void parseGetProfile(JSONObject obj) {
       if(obj==null){
           return;
       }else{
           try {
               if (obj.has("image")) {
                   String url = obj.getString("image");
                   ImageView img = (ImageView)findViewById(R.id.imgprofile);
                   if (url != null && !url.isEmpty())
                       Globals.loadImageIntoImageView(img, url, this);
               }
               if (obj.has("name")) {
                   TextView txtName = (TextView) findViewById(R.id.txtUser);
                   if (obj.getString("name") != null) {
                       txtName.setText(obj.getString("name"));
                   }
               }
               if (obj.has("promocode")) {
                   TextView txtPromoRealText = (TextView) findViewById(R.id.txtPromoRealText);
                   if (obj.getString("promocode") != null) {
                       txtPromoRealText.setText(obj.getString("promocode"));
                   }
               }
               if (obj.has("mywallet")) {
                   TextView txtMywalletRs = (TextView) findViewById(R.id.txtMywalletRs);
                   if (obj.getString("mywallet") != null) {
                       txtMywalletRs.setText(obj.getString("mywallet"));
                   }
               }
           }catch (JSONException ex){
               ex.printStackTrace();
           }
       }
    }*/


}
