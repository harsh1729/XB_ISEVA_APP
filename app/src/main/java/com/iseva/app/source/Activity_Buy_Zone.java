package com.iseva.app.source;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;



public class Activity_Buy_Zone extends Activity {
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_zone);
        inti();

    }

    private void inti() {
        TextView txtHeader = (TextView) findViewById(R.id.txtHeader);
        txtHeader.setText("Buy Zone");
        ImageView imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity_Buy_Zone.this.finish();
            }
        });
        setDataParseAble();
    }

    /*private void getNotiAndOffersToBuy() {
        Custom_ConnectionDetector cd = new Custom_ConnectionDetector(this);
        if (!cd.isConnectingToInternet()) {
            Globals.showAlert("Error", Globals.INTERNET_ERROR, this);
        } else {
            try {
                pd = Globals.showLoadingDialog(pd, this, false, "");
                Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                        Request.Method.POST,
                        Custom_URLs_Params.getURL_NotificationRemaining(),
                        Custom_URLs_Params.getParams_RemainingNoti(this), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("SUSHIL", "json Response recieved !!");
                        Globals.hideLoadingDialog(pd);
                        parseResponse(response);

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

    private void parseResponse(JSONObject response) {
        if (response == null) {
            return;
        } else {
            try {
                if (response.has("data")) {
                    JSONArray jsonArray = response.getJSONArray("data");
                    ArrayList<Object_Buy_Zone> list = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        Object_Buy_Zone objZone = new Object_Buy_Zone();
                        if(obj.has("id")){
                            objZone.id = obj.getInt("id");
                        }
                        if (obj.has("name")) {
                            objZone.name = obj.getString("name");
                        }
                        if (obj.has("amount")){
                            objZone.amount = obj.getString("amount");
                        }
                        if(obj.has("google_id")){
                            objZone.googleProductId = obj.getString("google_id");
                        }
                        if(obj.has("is_notification")){
                            objZone.isNotification = obj.getInt("is_notification");
                        }
                        list.add(objZone);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }*/


    private void setDataParseAble() {
        Custom_Adapter_Buy_Zone Adapter = new Custom_Adapter_Buy_Zone(this);
        ListView lv = (ListView) findViewById(R.id.listItemPurchaseNoti);
        lv.setAdapter(Adapter);

    }


}
