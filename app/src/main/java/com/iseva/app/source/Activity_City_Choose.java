package com.iseva.app.source;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Activity_City_Choose extends Activity {

    private Custom_Adapter_City adapter = null;
   private ProgressDialog pd = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city__choose);

        inti();
        //getAllCity();
        //Object_AppConfig object_appConfig = new Object_AppConfig(this);
       // if (object_appConfig.getVersionNo() == -1) {
            getAllCity();
      //  }

    }

    private void inti() {

        TextView txtHeader = (TextView) findViewById(R.id.txtHeader);
        txtHeader.setText("Choose Your City");
        ImageView imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setVisibility(View.GONE);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity_City_Choose.this.finish();
            }
        });
        TextView txtGo = (TextView) findViewById(R.id.txtGo);
        txtGo.setVisibility(View.VISIBLE);
        EditText edtSearch = (EditText) findViewById(R.id.edtSearch);
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
                    adapter.getFilter().filter(s.toString().toLowerCase());
                }/* else {
                    Globals.showAlert("Alert", "No content for searchable!", Activity_City_Choose.this);
                }*/

            }
        });
        // downloadImage("");
        txtGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navigationHome();
            }
        });





    }




    private void navigationHome() {
        if(adapter!=null) {
            if(adapter.mapCity.size()!=0) {
                submitCity(adapter.mapCity);
                /*Intent i = new Intent(this, Activity_Home.class);
                startActivity(i);*/
            }else{
                Globals.showShortToast(this,"Please select a city");
            }
        }

    }


    private void submitCity(HashMap<String,Integer> city){
        Log.i("SUSHIL", "SUSHIl map is " + city);
        Custom_ConnectionDetector connection = new Custom_ConnectionDetector(this);
        if (!connection.isConnectingToInternet()) {
            Globals.showAlert("ERROR", Globals.INTERNET_ERROR, this);
        } else {
            try{
                pd = Globals.showLoadingDialog(pd, this, false, "");
                HashMap<String, String> map = new HashMap<String, String>();
                ArrayList<Integer> ids = new ArrayList<>();
                for (Map.Entry<String,Integer> entry : city.entrySet()) {
                   int value = entry.getValue();
                    ids.add(value);
                    // do stuff
                }
                map.put("imei",Globals.getdeviceId(this));
                map.put("cities", ids.toString());
                Log.i("SUSHIL", "id s array !!  " + map);

                /*Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(Method.POST,
                        Custom_URLs_Params.getURL_SubmitAllCity(),
                        map, new Responce.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Globals.hideLoadingDialog(pd);
                        Log.i("SUSHIL", "json Response recieved !!");
                        parseCityResponseSubmit(response);

                    }


                }, new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError err) {
                        Globals.hideLoadingDialog(pd);
                        Log.i("SUSHIL", "ERROR VolleyError");
                        Globals.showShortToast(Activity_City_Choose.this, Globals.MSG_SERVER_ERROR);
                    }
                });

                Custom_VolleyAppController.getInstance().addToRequestQueue(
                        jsonObjectRQST);*/
                Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                        Request.Method.POST,
                        Custom_URLs_Params.getURL_SubmitAllCity(),
                        map,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Globals.hideLoadingDialog(pd);
                                Log.i("SUSHIL", "json Response recieved !!" + response);
                                parseCityResponseSubmit(response);

                            }

                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError err) {
                        Globals.hideLoadingDialog(pd);
                        Log.i("SUSHIL", "ERROR VolleyError" + err);
                        Globals.showShortToast(
                                Activity_City_Choose.this,
                                Globals.MSG_SERVER_ERROR);

                    }
                });

                Custom_VolleyAppController.getInstance().addToRequestQueue(
                        jsonObjectRQST);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }



    private void parseCityResponseSubmit(JSONObject obj){
        if(obj==null){
            Globals.showShortToast(Activity_City_Choose.this,"please select your city and retry");
            return;
        }else{
            if(obj.has("success")){
                adapter.mapCity = null;
                adapter = null;
                Intent i = new Intent(this, Activity_Home.class);
                startActivity(i);
                this.finish();
            }else{
                Globals.showShortToast(Activity_City_Choose.this,"please select your city and retry");
                return;
            }
        }

    }

    private void getAllCity() {

        Custom_ConnectionDetector connection = new Custom_ConnectionDetector(this);
        if (!connection.isConnectingToInternet()) {
            Globals.showAlert("ERROR", Globals.INTERNET_ERROR, this);
        } else {
            try{
                pd = Globals.showLoadingDialog(pd, this, false, "");
				HashMap<String, String> map = new HashMap<String, String>();
                map.put("imei", Globals.getdeviceId(this));
                Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                        Request.Method.POST,
                        Custom_URLs_Params.getURL_AllCity(),
                        map,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Globals.hideLoadingDialog(pd);
                                Log.i("SUSHIL", "json Response recieved !!" + response);
                                parseResponceCity(response);

                            }

                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError err) {
                        Globals.hideLoadingDialog(pd);
                        Log.i("SUSHIL", "ERROR VolleyError" + err);
                        Globals.showShortToast(
                                Activity_City_Choose.this,
                                Globals.MSG_SERVER_ERROR);

                    }
                });

                Custom_VolleyAppController.getInstance().addToRequestQueue(
                        jsonObjectRQST);


		  } catch (Exception ex) {
			Globals.hideLoadingDialog(pd);
		}
           /* try {
                pd = Globals.showLoadingDialog(pd, this, false, "");
                HashMap<String, String> map = new HashMap<String, String>();

                Custom_VolleyArrayRequest jsonArrayRQST = new Custom_VolleyArrayRequest(Custom_URLs_Params.getURL_AllCity(), map,
                        new Listener<JSONArray>() {

                            @Override
                            public void onResponse(JSONArray response) {
                                Globals.hideLoadingDialog(pd);
                                Log.i("SUSHIL", "json Response recieved !!");
                                parseResponceCity(response);

                            }

                        },
                        new ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError err) {
                                Globals.hideLoadingDialog(pd);
                                Log.i("SUSHIL", "ERROR VolleyError");
                                Globals.showShortToast(Activity_City_Choose.this,
                                        Globals.MSG_SERVER_ERROR);
                            }
                        });

                Custom_VolleyAppController.getInstance().addToRequestQueue(jsonArrayRQST);
            } catch (Exception e) {
                Globals.hideLoadingDialog(pd);
                e.printStackTrace();
            }*/

        }
    }

    private void parseResponceCity(JSONObject object) {
        ArrayList<Object_City> listCity = new ArrayList<Object_City>();
        try {
            if (object != null) {
                JSONArray Array = object.getJSONArray("cities");
                if (Array != null) {
                    for (int i = 0; i < Array.length(); i++) {
                        try {
                            JSONObject obj = Array.getJSONObject(i);
                            Object_City objCity = new Object_City();
                            objCity.cityId = Integer.parseInt(obj.getString("id"));
                            objCity.name = obj.getString("name");
                            //Log.i("SUSHIL","SUSHIl city name "+objCity.name);
                           if(obj.has("is_select")) {
                               if (obj.getInt("is_select") == 1) {
                                   objCity.seLectedCity = true;
                               } else {
                                   objCity.seLectedCity = false;
                               }
                           }
                            listCity.add(objCity);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    showData(listCity);
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    /*private void downloadImage(String url) {


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

    }*/


    private void showData(ArrayList<Object_City> list) {

        adapter = new Custom_Adapter_City(this, list);
        ListView listCity = (ListView) findViewById(R.id.ListViewCity);
        listCity.setAdapter(adapter);

    }

   /* private void getAllCategory() {
        // get all category first time
        Custom_ConnectionDetector connection = new Custom_ConnectionDetector(this);
        if (!connection.isConnectingToInternet()) {
            Globals.showAlert("ERROR", Globals.INTERNET_ERROR, this);
        } else {
            try {
                pd = Globals.showLoadingDialog(pd,this,false,"");
                Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                        Request.Method.POST,
                        Custom_URLs_Params.getURL_AllCity(),
                        Custom_URLs_Params.getParams_Category(this), new Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("SUSHIL", "json Response recieved !!");
                        insertCategoryDatabase(response);
                    }


                }, new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError err) {
                        Log.i("SUSHIL", "ERROR VolleyError");
                        Globals.hideLoadingDialog(pd);

                    }
                });

                Custom_VolleyAppController.getInstance().addToRequestQueue(
                        jsonObjectRQST);
            } catch (Exception ex) {
                Globals.hideLoadingDialog(pd);
                ex.printStackTrace();

            }
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
                                String url = objCateJson.getJSONObject("image").getString("url");
                                if (url != null && !url.isEmpty()) {
                                    downloadImage(url);
                                    if (bit != null) {
                                        objCate.image = getBitmaptoByteArray(bit);
                                    }
                                }
                            }
                            listCate.add(objCate);
                        }

                    }
                    DBHandler_Access databaseAccess = DBHandler_Access.getInstance(this);
                    databaseAccess.open();
                    databaseAccess.insertCategory(listCate);
                    databaseAccess.close();;

                } else if (obj.has("cities")) {
                    JSONArray cityArray = obj.getJSONArray("cities");
                    ArrayList<Object_City> listCity = new ArrayList<>();
                    for (int i = 0; i < cityArray.length(); i++) {
                            JSONObject objCityJson = cityArray.getJSONObject(i);
                             if(objCityJson!=null){
                                 Object_City objCity = new Object_City();
                                 objCity.cityId = objCityJson.getInt("id");
                                 objCity.name  = objCityJson.getString("name");
                                 objCity.seLectedCity = objCityJson.getInt("selectedcity");
                                 listCity.add(objCity);
                             }
                    }
                    *//*DBHandler_Access databaseAccess = DBHandler_Access.getInstance(this);
                    databaseAccess.open();
                    databaseAccess.insertCategory(listCate);
                    databaseAccess.close();;*//*
                    Globals.hideLoadingDialog(pd);
                    showData(listCity);

                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }*/


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




}
