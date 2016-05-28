package com.iseva.app.source;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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

public class Activity_MyBusinessExtra extends Activity {

    private ProgressDialog pd;
    private Custom_Adapter_MyBusinessExtra adapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_offers);
        inti();


    }

    private void inti() {
        Intent intent = getIntent();
        final TextView txtHeader = (TextView) findViewById(R.id.txtHeader);
        txtHeader.setText("My "+intent.getStringExtra("extraName"));
        ImageView imgBack = (ImageView) findViewById(R.id.imgBack);
        final ImageView imgSearch = (ImageView) findViewById(R.id.imgSearch);
        ImageView imgCancel = (ImageView) findViewById(R.id.imgCancel);
        imgSearch.setVisibility(View.VISIBLE);
        final LinearLayout linearSearch = (LinearLayout) findViewById(R.id.linearSearch);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity_MyBusinessExtra.this.finish();
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
                txtHeader.setVisibility(View.VISIBLE);
                imgSearch.setVisibility(View.VISIBLE);
                linearSearch.setVisibility(View.GONE);
            }
        });
        EditText edtSearch = (EditText) findViewById(R.id.edtSearchHeader);
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

        getBusinessExtra();
      /*send userid and businessTtpe id */



    }


    private void getBusinessExtra() {
        Custom_ConnectionDetector cd = new Custom_ConnectionDetector(this);
        if (!cd.isConnectingToInternet()) {
            Globals.showAlert("ERROR", Globals.INTERNET_ERROR, this);
        } else {

            try {
                Object_AppConfig obj = new Object_AppConfig(this);
                HashMap<String,String> map = new HashMap<>();
                map.put("userid",obj.getUserId()+"");
                map.put("bmasterid",obj.getBusiExtraId()+"");
                pd = Globals.showLoadingDialog(pd, this, false, "");
                Log.i("SUSHIL", "map !!"+map);
                Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                        Request.Method.POST,
                        Custom_URLs_Params.getURL_MyBusinessExtra(),
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
    }

    private void offersResponse(JSONObject obj) {
        if (obj == null) {
            return;
        } else {
            try {
                if (obj.has("data")) {
                    JSONArray array = obj.getJSONArray("data");
                    if (array != null) {
                        if (array.length() != 0) {
                            ArrayList<Object_BusinessExtraData> list = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                if (object != null) {
                                    Object_BusinessExtraData objectData = new Object_BusinessExtraData();
                                    if (object.has("id")) {
                                        objectData.id = object.getInt("id");
                                    }
                                    if (object.has("heading")) {

                                        objectData.heading = object.getString("heading");

                                    }
                                    if (object.has("content")) {
                                        objectData.content = object.getString("content");
                                    }
                                    if (object.has("images")) {
                                        JSONArray imageArray = object.getJSONArray("images");
                                        ArrayList<String> listImage = new ArrayList<>();
                                        for (int j = 0; j < imageArray.length(); j++) {
                                            JSONObject objImage = imageArray.getJSONObject(j);
                                            if (objImage.has("thumburl")) {
                                                listImage.add(objImage.getString("thumburl"));
                                            }
                                        }
                                        objectData.images = listImage;
                                    }
                                    list.add(objectData);
                                }
                            }
                            setdata(list);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private void setdata(ArrayList<Object_BusinessExtraData> list){
        Log.i("SUSHIL","list my offers "+list);
        adapter = new Custom_Adapter_MyBusinessExtra(this,list);
        ListView listView = (ListView)findViewById(R.id.listViewMyBusiness);
        listView.setAdapter(adapter);
    }

}
