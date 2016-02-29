package com.example.xb_sushil.iseva;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Activity_BusinessExtraDetails_Show extends Activity {

    private ProgressDialog pd;
    private int userid;
    private ArrayList<String> listImageUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers_details_show);
        inti();

    }

    private void inti() {
        listImageUrls = new ArrayList<>();
        TextView txtHeader = (TextView) findViewById(R.id.txtHeader);
        //TextView txtGoto = (TextView) findViewById(R.id.txtGotoServiceProvider);
        txtHeader.setText("Details");
        ImageView imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity_BusinessExtraDetails_Show.this.finish();
            }
        });

       /* txtGoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigation();
            }
        });*/
        if (getIntent().getIntExtra("id", -1) != -1) {

            getAlldetailsOffers(getIntent().getIntExtra("bmasterid", -1),getIntent().getIntExtra("id", -1));
        }

    }

    private void getAlldetailsOffers(int masterid,int id) {
        //add volley call here
        try {
            pd = Globals.showLoadingDialog(pd, this, false, "");
            HashMap<String, String> map = new HashMap<>();
            map.put("bmasterid", masterid + "");
            map.put("id", id+"");
            Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                    Request.Method.POST,
                    Custom_URLs_Params.getURL_OffersWithid(),
                    map,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Globals.hideLoadingDialog(pd);
                            Log.i("SUSHIL", "json Response recieved !!" + response);
                            responseOffersDetails(response);

                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError err) {
                    Globals.hideLoadingDialog(pd);
                    Log.i("SUSHIL", "ERROR VolleyError" + err);
                    Globals.showShortToast(
                            Activity_BusinessExtraDetails_Show.this,
                            Globals.MSG_SERVER_ERROR);

                }
            });

            Custom_VolleyAppController.getInstance().addToRequestQueue(
                    jsonObjectRQST);
        } catch (Exception ex) {
            ex.printStackTrace();
            Globals.hideLoadingDialog(pd);
            Globals.showShortToast(Activity_BusinessExtraDetails_Show.this,
                    Globals.MSG_SERVER_ERROR);
        }
    }

    private void responseOffersDetails(JSONObject obj) {
        if (obj == null) {
            return;
        } else {
            try {
               /* if (obj.has("details")) {
                    JSONObject objectDeatils = obj.getJSONObject("details");
                    if(objectDeatils.has("heading")){
                        TextView txtNameOffers = (TextView)findViewById(R.id.txtOffersDetaolsHeading);
                        if(objectDeatils.getString("heading")!=null){
                            txtNameOffers.setText(objectDeatils.getString("heading"));
                        }
                    }
                    if(objectDeatils.has("content")){
                        TextView txtContentOffers = (TextView)findViewById(R.id.txtOffersDetaolsContent);
                        if(objectDeatils.getString("content")!=null){
                            txtContentOffers.setText(objectDeatils.getString("content"));
                        }
                    }
                    if(objectDeatils.has("image")){
                        LinearLayout layout = (LinearLayout)findViewById(R.id.scrollLinearHorOffersDetails);
                        JSONArray arrayImage = objectDeatils.getJSONArray("image");
                        for(int i=0;i<arrayImage.length();i++){
                            JSONObject ob = arrayImage.getJSONObject(i);
                            if(ob.has("url")){
                                if(ob.getString("url")!=null) {
                                    ImageView img = Custom_Control.getImageView(this, 100, 100, 1);
                                    Globals.loadImageIntoImageView(img,ob.getString("url"),this);
                                    layout.addView(img);
                                }


                            }
                        }
                    }*/
                    /*if(objectDeatils.has("userid")){
                      int serId = objectDeatils.getInt("userid");
                        Object_AppConfig config = new Object_AppConfig(this);
                        config.setServiceProviderId(serId);
                    }*/
                //}

                if (obj.has("success")) {
                    if (obj.getInt("success") == 1) {
                        JSONArray array = obj.getJSONArray("data");
                        if (array != null) {
                            if (array.length() != 0) {
                                // ArrayList<Object_Offers> list = new ArrayList<>();
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    if (object != null) {
                                        Object_Offers objOffers = new Object_Offers();
                                        if (object.has("id")) {
                                            objOffers.id = object.getInt("id");
                                        }
                                        if (object.has("heading")) {
                                            objOffers.heading = object.getString("heading");
                                            TextView txtNameOffers = (TextView) findViewById(R.id.txtOffersDetaolsHeading);
                                            if (objOffers.heading != null) {
                                                txtNameOffers.setText(objOffers.heading);
                                            }

                                        }
                                        if (object.has("content")) {
                                            objOffers.content = object.getString("content");
                                            TextView txtContentOffers = (TextView) findViewById(R.id.txtOffersDetaolsContent);
                                            if (objOffers.content != null) {
                                                txtContentOffers.setText(objOffers.content);
                                            }
                                        }
                                        if (object.has("userid")) {
                                            int serId = object.getInt("userid");
                                            Object_AppConfig config = new Object_AppConfig(this);
                                            config.setServiceProviderId(serId);
                                            userid = serId;
                                        }
                                        if(object.has("contactdetails")){
                                            LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linearBusinessExtUserDetails);
                                            if(object.getString("contactdetails").isEmpty()){
                                                linearLayout.setVisibility(View.GONE);
                                            }else{
                                                linearLayout.setVisibility(View.VISIBLE);
                                                TextView txt = (TextView)findViewById(R.id.txtContactDetails);
                                                txt.setText(object.getString("contactdetails"));
                                            }
                                        }
                                        if (object.has("images")) {

                                            LinearLayout layoutImage = (LinearLayout) findViewById(R.id.linearOffersImage);
                                            layoutImage.setVisibility(View.VISIBLE);
                                            LinearLayout layout = (LinearLayout) findViewById(R.id.scrollLinearHorOffersDetails);
                                            JSONArray imageArray = object.getJSONArray("images");
                                            // ArrayList<String> listImage = new ArrayList<>();
                                            if(imageArray.length()!=0) {
                                                for (int j = 0; j < imageArray.length(); j++) {
                                                    // String url = imageArray.getString(j);
                                                    Log.i("SUSHIL","BUSINESS EXTAR IMAGE SIZE "+j);
                                                    JSONObject objImage = imageArray.getJSONObject(j);
                                                    String url = objImage.getString("imageurl");
                                                    //if (url != null) {
                                                        //listImage.add(url);
                                                        if(j==0){
                                                            int height = Globals.getScreenSize(this).y;
                                                            height = height / 3;
                                                            final ImageView imagMain = (ImageView)findViewById(R.id.mainImage);
                                                            LinearLayout.LayoutParams lpImage = (LinearLayout.LayoutParams)imagMain.getLayoutParams();
                                                            lpImage.height = height;
                                                            imagMain.setLayoutParams(lpImage);
                                                            Globals.loadImageIntoImageView(imagMain, url, this, R.drawable.default_offer, R.drawable.default_offer);
                                                            listImageUrls.add(url);
                                                            imagMain.setTag(j);
                                                            imagMain.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    navigationImageViewer((int) imagMain.getTag());
                                                                }
                                                            });
                                                            layoutImage.setVisibility(View.GONE);
                                                        }else {
                                                            layoutImage.setVisibility(View.VISIBLE);
                                                            final ImageView img = Custom_Control.getImageView(this, 100, 100, 1);
                                                            img.setTag(j);
                                                            Globals.loadImageIntoImageView(img, url, this, R.drawable.default_offer, R.drawable.default_offer);
                                                            listImageUrls.add(url);
                                                            layout.addView(img);
                                                            img.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    navigationImageViewer((int) img.getTag());
                                                                }
                                                            });

                                                        }
                                                   // }
                                                }
                                            }else{
                                                layoutImage.setVisibility(View.GONE);
                                            }
                                            // objOffers.offersimage = listImage;
                                        }
                                        //list.add(objOffers);
                                    }
                                }

                            }
                        }
                    }
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }

    }


    private void navigation() {
        Intent i = new Intent(this, Activity_ServiceProviderDetails_Show.class);
        i.putExtra("userid", userid);
        startActivity(i);
    }

    private void navigationImageViewer(int id) {
        if (listImageUrls.size() != 0) {
            Intent i = new Intent(this, Activity_ImageViewer.class);
            i.putExtra("name", "");
            i.putExtra("id", id);
            i.putExtra("number", "");
            i.putStringArrayListExtra("imageList", listImageUrls);
            startActivity(i);
        }
    }


}
