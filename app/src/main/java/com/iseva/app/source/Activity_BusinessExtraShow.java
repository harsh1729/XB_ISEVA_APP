package com.iseva.app.source;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class Activity_BusinessExtraShow extends FragmentActivity {
    private Custom_Adapter_BusinessExtra adapter = null;
    private ProgressDialog pd;
    private EditText edtSearch;
    private ViewPager mViewPager;
    private PageIndicator mIndicator;
    private Timer timer;
    private int pageIndex = 1;
    private int size;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        inti();

        /*ListView listOffers = (ListView)findViewById(R.id.listViewOffers);
        Custom_Adapter_BusinessExtra adpter = new Custom_Adapter_BusinessExtra(this);
        listOffers.setAdapter(adpter);*/
    }

    private void inti(){
        getAdds();
        Object_AppConfig objConfig = new Object_AppConfig(this);
        final TextView txtHeader = (TextView) findViewById(R.id.txtHeader);
        txtHeader.setText(objConfig.getCatName());//("Category");
        ImageView imgBack = (ImageView) findViewById(R.id.imgBack);
        final ImageView imgSearch = (ImageView) findViewById(R.id.imgSearch);
        ImageView imgCancel = (ImageView) findViewById(R.id.imgCancel);
        imgSearch.setVisibility(View.VISIBLE);
        final LinearLayout linearSearch = (LinearLayout)findViewById(R.id.linearSearch);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity_BusinessExtraShow.this.finish();
            }
        });
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtHeader.setVisibility(View.GONE);
                imgSearch.setVisibility(View.GONE);
                linearSearch.setVisibility(View.VISIBLE);
            }
        });
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSearch.setText("");
                txtHeader.setVisibility(View.VISIBLE);
                imgSearch.setVisibility(View.VISIBLE);
                linearSearch.setVisibility(View.GONE);
            }
        });
        edtSearch = (EditText)findViewById(R.id.edtSearchHeader);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (adapter != null) {
                    adapter.getFilter().filter(s.toString());
                }
            }
        });

        Intent i = getIntent();
        if(i.getStringExtra("array").equals("")) {
            Log.i("SUSHIL","call for new ");
            int bmasterid = i.getIntExtra("bextraid", -1);
            if (bmasterid != -1) {
                getBusinessExtra(bmasterid);
            }
        }else{
            try{
                JSONObject object = new JSONObject(i.getStringExtra("array"));
                offersResponse(object);
            }catch (JSONException ex){
               ex.printStackTrace();
            }
        }
       /* if(i.getStringExtra("type").equals("all"))
            getOffers();
        else if(i.getStringExtra("type").equals("home")){
            int offersid = i.getIntExtra("id",-1);
            if(offersid!=-1)
                getOffersWithid(offersid);
        }*/


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

       // unbindDrawables(findViewById(R.id.RootView));
       // System.gc();
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


    private void getBusinessExtra(int bmasterid){
        Custom_ConnectionDetector cd = new Custom_ConnectionDetector(this);
        if (!cd.isConnectingToInternet()) {
            Globals.showAlert("ERROR", Globals.INTERNET_ERROR, this);
        } else {

            try {
                pd = Globals.showLoadingDialog(pd, this, false, "");

                HashMap<String,String> map = new HashMap<>();
                map.put("bmasterid",bmasterid+"");
                map.put("deviceid",Globals.getdeviceId(this)+"");
                Log.i("SUSHIL","map is "+map);
                Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                        Request.Method.POST,
                        Custom_URLs_Params.getURL_BusinessExtra(),
                        map, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("SUSHIL", "json Response recieved !!");
                        Globals.hideLoadingDialog(pd);
                        offersResponse(response);

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

    /*private void getOffersWithid(int id){
        Custom_ConnectionDetector cd = new Custom_ConnectionDetector(this);
        if (!cd.isConnectingToInternet()) {
            Globals.showAlert("ERROR", Globals.INTERNET_ERROR, this);
        } else {

            try {
                pd = Globals.showLoadingDialog(pd, this, false, "");
                HashMap<String,String> map = new HashMap<>();
                map.put("offerid",id+"");
                Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                        Request.Method.POST,
                        Custom_URLs_Params.getURL_OffersWithid(),
                        map, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("SUSHIL", "json Response recieved !!"+response);
                        Globals.hideLoadingDialog(pd);
                        offersResponse(response);

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
    }*/


    private void offersResponse(JSONObject obj){
        if(obj==null){
            return;
        }else{
            try {
                Log.i("SUSHIL","object "+obj);
                if (obj.has("success")) {
                    if(obj.getInt("success")==1) {
                        JSONArray array = obj.getJSONArray("data");
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
                                        if (object.has("heading")) {
                                              objOffers.heading = object.getString("heading");
                                        }
                                        if(object.has("bmasterid")){
                                            objOffers.masterid = object.getInt("bmasterid");
                                        }
                                        if (object.has("content")) {
                                            objOffers.content = object.getString("content");
                                        }
                                        if(object.has("contact")){
                                            objOffers.contact = object.getString("contact");
                                        }
                                        if (object.has("images")) {
                                            JSONArray imageArray = object.getJSONArray("images");
                                            ArrayList<String> listImage = new ArrayList<>();
                                            for (int j = 0; j < imageArray.length(); j++) {
                                               // String url = imageArray.getString(j);
                                                try {
                                                    JSONObject objImage = imageArray.getJSONObject(j);
                                                    String url = objImage.getString("imageurl");
                                                    if (url != null) {
                                                        listImage.add(url);
                                                    }
                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }
                                            }
                                            objOffers.images = listImage;
                                        }
                                        list.add(objOffers);
                                    }
                                }
                                setdata(list);
                            }else{
                                Globals.showShortToast(this,"Data not found");
                                this.finish();
                            }
                        }
                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    private void setdata(ArrayList<Object_BusinessExtraData> list){
        Log.i("SUSHIL","sushil call adapter");
        adapter = new Custom_Adapter_BusinessExtra(this,list);
        ListView listView = (ListView)findViewById(R.id.listViewOffers);
        listView.setAdapter(adapter);
    }
    /*public void rating(View v){

        LinearLayout linearRating = (LinearLayout)findViewById(R.id.linearratingView);
        if(linearRating.getVisibility()== View.GONE)
        linearRating.setVisibility(View.VISIBLE);
        else
            linearRating.setVisibility(View.GONE);

    }*/


    private void getAdds() {
        Custom_ConnectionDetector cd = new Custom_ConnectionDetector(this);
        if (!cd.isConnectingToInternet()) {
            Globals.showAlert("ERROR", Globals.INTERNET_ERROR, this);
        } else {

            try {
                // pd = Globals.showLoadingDialog(pd, this, false, "");
                Object_AppConfig config = new Object_AppConfig(this);
                HashMap<String, String> map = new HashMap<>();
                map.put("catid",config.getCatId()+"");
                map.put("imei", Globals.getdeviceId(this));
                map.put("isbusiness",0+"");
                Log.i("SUSHIL","get adds map "+map);
                Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                        Request.Method.POST,
                        Custom_URLs_Params.getURL_OffersRandomcat(),
                        map, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("SUSHIL", "json Response recieved !!" + response);
                        // Globals.hideLoadingDialog(pd);
                        offersParcer(response);

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


    private void offersParcer(JSONObject obj) {
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
                                CardView cv = (CardView) findViewById(R.id.card_viewbusinessExtra);
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
        if (offers.size() != 0) {
            CardView cv = (CardView) findViewById(R.id.card_viewbusinessExtra);
            cv.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams lpCard = (LinearLayout.LayoutParams) cv.getLayoutParams();
            int width = Globals.getScreenSize(this).x;
            //height = height/3;
            lpCard.height = (int) (width*0.60);
            cv.setLayoutParams(lpCard);
            mViewPager = (ViewPager) findViewById(R.id.view_pagerbusinessExtra);
            mIndicator = (CirclePageIndicator) findViewById(R.id.indicatorBusinessExtra);
            Custom_Adapter_Home_Addver adapter = new Custom_Adapter_Home_Addver(this, true, offers);
            mViewPager.setAdapter(adapter);
            mIndicator.setViewPager(mViewPager);
            size = offers.size();
            pageSwitcher();
        } else {
            CardView cv = (CardView) findViewById(R.id.card_viewbusinessExtra);
            cv.setVisibility(View.GONE);
        }
    }

    public void pageSwitcher() {
        timer = new Timer(); // At this line a new Thread will be created
        timer.scheduleAtFixedRate(new RemindTask(), 0, 3000); // delay
        // in
        // milliseconds
    }

    // this is an inner class...
    class RemindTask extends TimerTask {

        @Override
        public void run() {

            // As the TimerTask run on a seprate thread from UI thread we have
            // to call runOnUiThread to do work on UI thread.
            runOnUiThread(new Runnable() {
                public void run() {

                    /*if (page > 4) { // In my case the number of pages are 5
                        timer.cancel();
                        // Showing a toast for just testing purpose
                        Toast.makeText(getApplicationContext(), "Timer stoped",
                                Toast.LENGTH_LONG).show();
                    } else {*/

                    // }
                    if (pageIndex <= size) {
                        mViewPager.setCurrentItem(pageIndex);
                        pageIndex++;
                    } else {
                        mViewPager.setCurrentItem(0);
                        pageIndex = 1;
                    }

                }
            });

        }
    }

}
