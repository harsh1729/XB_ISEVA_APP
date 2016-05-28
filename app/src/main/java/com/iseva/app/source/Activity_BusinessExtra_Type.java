package com.iseva.app.source;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

public class Activity_BusinessExtra_Type extends Activity {
    private ProgressDialog pd;
    private ArrayList<Object_BusinessExtra> listType;
    //private int busiExtraID = 0;
    private int position = 0;
    private String object;
    private int maxlimit;
    private int userpost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers_type);
        inti();
    }


    private void inti() {
        TextView txtHeader = (TextView) findViewById(R.id.txtHeader);
        txtHeader.setText("Business Zone");
        ImageView imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity_BusinessExtra_Type.this.finish();
            }
        });


        Button btnMyOffers = (Button) findViewById(R.id.btnMyOffers);

        btnMyOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationMyOffers();
            }
        });

        Button btnAddNew = (Button) findViewById(R.id.btnAdnewOffers);
        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (maxlimit > userpost) {
                    navigationAddNew();
                    Activity_BusinessExtra_Type.this.finish();
                }
                else {
                    Globals.showAlertDialog(
                            "Alert",
                            "You have reached the max post limit! Please contact us to increase your post limit. ",
                            Activity_BusinessExtra_Type.this,
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    contactus();
                                }
                            }, "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    return;
                                }
                            }, false);
                }
            }
        });
        getBasinessType();
    }


     private void contactus(){
           Intent i = new Intent(this,Activity_About_Us.class);
           startActivity(i);
     }
    private void navigationMyOffers() {
        if (position != 0) {
            Intent i = new Intent(this, Activity_MyBusinessExtra.class);
            Object_BusinessExtra objType = listType.get(position-1);
            i.putExtra("extraName",objType.name);
            startActivity(i);
        } else {
            Globals.showShortToast(this, "Please choose an option.");
        }
    }

    private void navigationAddNew() {
        if (position != 0) {
            Intent i = new Intent(this, Activity_Add_New_BusinessExtra.class);
            Object_BusinessExtra objType = listType.get(position-1);
            i.putExtra("city",object);
            i.putExtra("extraId",objType.id);
            i.putExtra("extraName",objType.name);
            startActivity(i);
        } else {
             Globals.showShortToast(this, "Please choose an option.");

        }
    }

    private void getBasinessType() {
        Custom_ConnectionDetector cd = new Custom_ConnectionDetector(this);
        if (!cd.isConnectingToInternet()) {
            Globals.showAlert("ERROR", Globals.INTERNET_ERROR, this);
        } else {

            try {
                Object_AppConfig config = new Object_AppConfig(this);
                HashMap<String,String> map = new HashMap<>();
                map.put("userid",config.getUserId()+"");
                pd = Globals.showLoadingDialog(pd, this, false, "");
                Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                        Request.Method.POST,
                        Custom_URLs_Params.getURL_BusinessExtraType(),
                        map, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("SUSHIL", "json Response recieved !!");
                        Globals.hideLoadingDialog(pd);
                        //parseCateResponse(response);
                        parse(response);
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

    private void parse(JSONObject obj) {
        if (obj == null) {
            return;
        } else {
            try {
                if (obj.has("success")) {
                    if(obj.has("maxlimit")){
                      maxlimit = obj.getInt("maxlimit");
                    }
                    if(obj.has("userpost")){
                        userpost = obj.getInt("userpost");
                    }
                    if (obj.has("data")) {
                        JSONArray itemArray = obj.getJSONArray("data");

                        listType = new ArrayList<>();
                        for (int i = 0; i < itemArray.length(); i++) {
                            JSONObject object = itemArray.getJSONObject(i);
                            Object_BusinessExtra objType = new Object_BusinessExtra();
                            objType.id = object.getInt("id");
                            objType.name = object.getString("name");
                            objType.catid = object.getInt("catid");
                            listType.add(objType);
                        }
                        setSpinnerData();
                    }
                    if(obj.has("city")){
                        object = obj.toString();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setSpinnerData() {
        Spinner spin = (Spinner) findViewById(R.id.spinnerBusiExtra);
        ArrayList<String> list = new ArrayList<>();
        int cnt = 0;
        for (Object_BusinessExtra obj : listType) {
            if (cnt == 0) {
                list.add("--Select--");
            }
            list.add(obj.name);
            cnt++;
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(dataAdapter);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {
                if (position != 0) {
                    Object_AppConfig objConfig = new Object_AppConfig(Activity_BusinessExtra_Type.this);
                    objConfig.setBusiExtraId(listType.get(position - 1).id);
                    //busiExtraID = listType.get(position - 1).id;
                    Activity_BusinessExtra_Type.this.position = position;
                }

                //cateId = listCate.get(position).catId;

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

}
