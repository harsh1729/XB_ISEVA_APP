package com.iseva.app.source;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;


import org.json.JSONException;
import org.json.JSONObject;

public class Activity_Send_Notification extends Activity {

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);
        inti();
      //  getRemainingNotification();
    }

    private void inti() {

        TextView txtHeader = (TextView) findViewById(R.id.txtHeader);
        txtHeader.setText("Send Notification");
        ImageView imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity_Send_Notification.this.finish();
            }
        });
        final TextInputLayout headingWrapper = (TextInputLayout) findViewById(R.id.headingWrapper);
        final TextInputLayout contentWrapper = (TextInputLayout) findViewById(R.id.TextWrapper);
        final EditText edt = (EditText)findViewById(R.id.edtNotiHeading);
        final EditText edtCon = (EditText)findViewById(R.id.edtNotiContent);
        Button btnSend = (Button)findViewById(R.id.btnSendNoti);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globals.showAlertDialog(
                        "Alert",
                        "Are you sure to send notification ?",
                        Activity_Send_Notification.this,
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                uploadata(edt.getText().toString(), edtCon.getText().toString());
                            }
                        }, "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                return;
                            }
                        }, false);


            }
        });
    }

    private void getRemainingNotification() {
        Custom_ConnectionDetector cd = new Custom_ConnectionDetector(this);
        if (!cd.isConnectingToInternet()) {
            Globals.showAlert("ERROR", Globals.INTERNET_ERROR, this);
        } else {

            try {
                pd = Globals.showLoadingDialog(pd, this, false, "");
                Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                        Request.Method.POST,
                        "",
                        Custom_URLs_Params.getParams_RemainingNoti(this), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("SUSHIL", "json Response recieved !!");
                        Globals.hideLoadingDialog(pd);
                        //parseCateResponse(response);
                        parseRemainNoti(response);
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

    private void parseRemainNoti(JSONObject obj) {
        if (obj == null) {
            return;
        } else {
            try {
                if (obj.has("remain")) {
                    int remainNoti = obj.getInt("remain");
                    if (remainNoti == 0) {
                        Globals.showAlertDialog("Buy", "Do you want to buy Notification ?",
                                this, "Buy", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        Intent i = new Intent(Activity_Send_Notification.this, Activity_Buy_Zone.class);
                                        startActivity(i);

                                    }
                                }, "Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        Activity_Send_Notification.this.finish();

                                    }
                                }, false);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadata(String head, String con) {

        if (head.isEmpty()) {
            Globals.showShortToast(this, "Heading is empty.");
            return;
        }
        if (con.isEmpty()){
            Globals.showShortToast(this, "Content is empty.");
            return;
        }

            try {
                Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                        Request.Method.POST,
                       "",
                        Custom_URLs_Params.getParams_Send_Notification(this, head, con), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("SUSHIL", "json Response recieved !!" + response);
                        contentResponce(response);
                    }


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError err) {
                        Log.i("SUSHIL", "ERROR VolleyError");

                    }
                });

                Custom_VolleyAppController.getInstance().addToRequestQueue(
                        jsonObjectRQST);
            } catch (Exception ex) {

                ex.printStackTrace();

            }

    }

    private void contentResponce(JSONObject response) {
        if (response == null) {
            return;
        } else {
            try {
                if (response.has("success")) {
                    if (response.getInt("success") == 1) {
                        //Globals.showAlert("Success", "You offers succesfully saved", this);
                        Globals.showShortToast(this, "Succesfully Sent");
                        this.finish();
                    }
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
                ;
            }
        }
    }
}
