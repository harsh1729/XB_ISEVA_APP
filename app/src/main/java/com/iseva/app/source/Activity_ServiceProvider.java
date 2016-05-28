package com.iseva.app.source;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class Activity_ServiceProvider extends AppCompatActivity {

    private ProgressDialog pd;
    private Custom_Adapter_ServiceProvider adapter = null;
    private EditText edtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider);
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/


         inti();



    }

    private void inti(){
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
                Activity_ServiceProvider.this.finish();
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
               if(adapter!=null){
                   adapter.getFilter().filter(s.toString());
               }
            }
        });

        Intent intent = getIntent();
        if(intent.getStringExtra("object").equals("")) {
            if (intent.getIntExtra("catid", -1) != -1)
                getServiceProvider(intent.getIntExtra("catid", -1));
        }else{
            try{
                JSONObject object = new JSONObject(intent.getStringExtra("object"));
                serviceProviderResponse(object);
            }catch (JSONException ex){
                ex.printStackTrace();
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //unbindDrawables(findViewById(R.id.RootView));
        //System.gc();
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

    private void getServiceProvider(int catid){
        Custom_ConnectionDetector cd = new Custom_ConnectionDetector(this);
        if (!cd.isConnectingToInternet()) {
            Globals.showAlert("ERROR", Globals.INTERNET_ERROR, this);
        } else {

            try {
                pd = Globals.showLoadingDialog(pd, this, false, "");
                Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                        Request.Method.POST,
                        Custom_URLs_Params.getURL_Get_Service_Provider(),
                        Custom_URLs_Params.getParams_ServiceProvider(this,catid), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("SUSHIL", "json Response recieved !!" + response);
                       serviceProviderResponse(response);

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


    private void serviceProviderResponse(JSONObject obj){
        if(obj==null){
            return;
        }else{
            try {
                if (obj.has("success")) {
                    if(obj.getInt("success")==1) {
                        JSONArray array = obj.getJSONArray("data");
                        if(pd!=null)
                            Globals.hideLoadingDialog(pd);
                        if (array != null) {
                            if (array.length() != 0) {
                                ArrayList<Object_Service_Provider> list = new ArrayList<>();
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    if (object != null) {
                                        Object_Service_Provider objService = new Object_Service_Provider();
                                        if (object.has("id")) {
                                            objService.id = object.getInt("id");
                                        }
                                        if (object.has("firmname")) {
                                            objService.name = object.getString("firmname");

                                        }
                                        if (object.has("address")) {
                                            objService.address = object.getString("address");
                                        }
                                    if(object.has("contact")){
                                        objService.contact = object.getString("contact");
                                    }
                                        if (object.has("image")) {
                                            JSONObject objectJ = object.getJSONObject("image");
                                            objService.imageUrl = objectJ.getString("imageurl");
                                        }
                                        if (object.has("rating")) {
                                            objService.ratingSeviceProvider = object.getString("rating");
                                        }
                                        if (object.has("user_rate")) {
                                            objService.userrate = object.getDouble("user_rate");
                                        }
                                        list.add(objService);
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

    private void setdata(ArrayList<Object_Service_Provider> list){



         adapter = new Custom_Adapter_ServiceProvider(this,list);
        ListView listView = (ListView)findViewById(R.id.listViewService);
        listView.setAdapter(adapter);
    }
    /*public void rating(View v){

        LinearLayout linearRating = (LinearLayout)findViewById(R.id.linearratingView);
        if(linearRating.getVisibility()== View.GONE)
        linearRating.setVisibility(View.VISIBLE);
        else
            linearRating.setVisibility(View.GONE);

    }*/
}
