package com.iseva.app.source;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Activity_Job_Resume_Search extends Activity {

    private Spinner spinCity;
    private Spinner spinCategory;
    private ProgressDialog pd;
    private int cityID = -1;
    private int categoryID = -1;
    private boolean isJobSearch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__search_job);

        init();
    }

    private void init(){

        TextView txtHeader = (TextView)findViewById(R.id.txtHeader);
        spinCity = (Spinner)findViewById(R.id.spinLocation);
        spinCategory = (Spinner)findViewById(R.id.spinCate);
        getCateCityData();
        Button btnFind = (Button)findViewById(R.id.btnFindResume);
        ImageView imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        isJobSearch = getIntent().getBooleanExtra("isJobSearch", false);
        if(isJobSearch) {

            btnFind.setText("Find Jobs");
            txtHeader.setText("Find Jobs");
           // setFooterAndHeader(-1,"Find Jobs");
        }else{
           // setFooterAndHeader(R.id.imgBtnFooterFind,"Find Resume");
            btnFind.setText("Find Resume");
            txtHeader.setText("Find Resume");
        }

        Log.i("SUSHIL","is job search "+isJobSearch);



        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isJobSearch)
                     getResume();
                else
                    naviJobList();
            }
        });

    }


    private void naviJobList(){
        Intent i = new Intent(this,Activity_List_Jobs.class);
        i.putExtra("catid",categoryID);
        i.putExtra("cityid",cityID);
        startActivity(i);
    }
    private void getCateCityData() {
        Custom_ConnectionDetector connection = new Custom_ConnectionDetector(this);
        if (!connection.isConnectingToInternet()) {
            Globals.showAlert("ERROR", Globals.INTERNET_ERROR, this);
        } else {
            try {
                pd = Globals.showLoadingDialog(pd, this, false, "");
                HashMap<String, String> map = new HashMap<String, String>();
               // map.put("imei", Globals.getdeviceId(this));
                Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                        Request.Method.POST,
                        Custom_URLs_Params.getURL_citycate(),
                        map,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Globals.hideLoadingDialog(pd);
                                Log.i("SUSHIL", "json Response recieved !!" + response);
                                parseResponce(response);

                            }

                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError err) {
                        Globals.hideLoadingDialog(pd);
                        Log.i("SUSHIL", "ERROR VolleyError" + err);
                        Globals.showShortToast(
                                Activity_Job_Resume_Search.this,
                                Globals.MSG_SERVER_ERROR);

                    }
                });

                Custom_VolleyAppController.getInstance().addToRequestQueue(
                        jsonObjectRQST);


            } catch (Exception ex) {
                Globals.hideLoadingDialog(pd);
            }
        }

    }

    private void parseResponce(JSONObject object) {
        ArrayList<Object_City> listCity = new ArrayList<Object_City>();
        try {
            if (object != null) {
                JSONArray Array = object.getJSONArray("cities");
                JSONArray arrayCate = object.getJSONArray("category");
                if (Array != null) {
                    for (int i = 0; i < Array.length(); i++) {
                        try {
                            JSONObject obj = Array.getJSONObject(i);
                            Object_City objCity = new Object_City();
                            objCity.cityId = Integer.parseInt(obj.getString("id"));
                            objCity.name = obj.getString("name");
                            //Log.i("SUSHIL","SUSHIl city name "+objCity.name);
                            /*if(obj.has("is_select")) {
                                if (obj.getInt("is_select") == 1) {
                                    objCity.seLectedCity = true;
                                } else {
                                    objCity.seLectedCity = false;
                                }
                            }*/
                            listCity.add(objCity);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    setCitySpinner(listCity);
                }
                if(arrayCate.length()!=0){
                    ArrayList<Object_Category> list = new ArrayList<>();
                    for (int i=0;i<arrayCate.length();i++){
                        try {
                            JSONObject obj = arrayCate.getJSONObject(i);
                            Object_Category objCate = new Object_Category();
                            objCate.catId = Integer.parseInt(obj.getString("id"));
                            objCate.name = obj.getString("name");
                            //Log.i("SUSHIL","SUSHIl city name "+objCity.name);
                            /*if(obj.has("is_select")) {
                                if (obj.getInt("is_select") == 1) {
                                    objCity.seLectedCity = true;
                                } else {
                                    objCity.seLectedCity = false;
                                }
                            }*/
                            list.add(objCate);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    setCateSpinner(list);
                }
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void setCitySpinner(final ArrayList<Object_City>cityList) {

        ArrayList<String> listName = new ArrayList<>();
        int i = -1;
        for (Object_City city : cityList) {
            i++;
            if(i==0){
                listName.add("Select All");
            }
            listName.add(city.name);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listName);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCity.setAdapter(dataAdapter);
        spinCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {
                if(position!=0)
                    cityID = cityList.get(position-1).cityId;
                else
                    cityID = -1;

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

    }

    private void setCateSpinner(final ArrayList<Object_Category>cityList) {

        ArrayList<String> listName = new ArrayList<>();
        int i = -1;
        for (Object_Category city : cityList) {
            i++;
            if(i==0){
                listName.add("Select All");
            }
            listName.add(city.name);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listName);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCategory.setAdapter(dataAdapter);
        spinCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {
                if(position!=0)
                    categoryID = cityList.get(position-1).catId;
                else
                    categoryID = -1;

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

    }





    private void getResume(){
        Custom_ConnectionDetector connection = new Custom_ConnectionDetector(this);
        if (!connection.isConnectingToInternet()) {
            Globals.showAlert("ERROR", Globals.INTERNET_ERROR, this);
        } else {
            try {
                pd = Globals.showLoadingDialog(pd, this, false, "");
                HashMap<String, String> map = new HashMap<String, String>();
                if(cityID!=-1){
                    map.put("cityid",cityID+"");
                }
                if(categoryID!=-1){
                    map.put("catid",categoryID+"");
                }
                Log.i("SUSHIL","map is "+map);
                // map.put("imei", Globals.getdeviceId(this));
                Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                        Request.Method.POST,
                        Custom_URLs_Params.getURL_GetResume(),
                        map,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Globals.hideLoadingDialog(pd);
                                Log.i("SUSHIL", "json Response recieved !!" + response);
                                parseResume(response);

                            }

                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError err) {
                        Globals.hideLoadingDialog(pd);
                        Log.i("SUSHIL", "ERROR VolleyError" + err);
                        Globals.showShortToast(
                                Activity_Job_Resume_Search.this,
                                Globals.MSG_SERVER_ERROR);

                    }
                });

                Custom_VolleyAppController.getInstance().addToRequestQueue(
                        jsonObjectRQST);


            } catch (Exception ex) {
                Globals.hideLoadingDialog(pd);
            }
        }
    }


    private void parseResume(JSONObject obj){
        if(obj!=null){
            try {
                JSONArray listJson = obj.getJSONArray("data");
                if(listJson.length()!=0){
                     naviListResume(obj);
                }else{
                    Globals.showShortToast(this,"Data not found");
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    private void naviListResume(JSONObject json){
        Intent i = new Intent(this,Activity_List_Resume.class);
        i.putExtra("resume",json.toString());
        startActivity(i);
    }
}
