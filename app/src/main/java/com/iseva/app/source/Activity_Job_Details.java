package com.iseva.app.source;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class Activity_Job_Details extends Activity {

    private ProgressDialog pd;
    private String num = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);

        init();


    }


    private void init(){
        TextView txtHeader = (TextView)findViewById(R.id.txtHeader);
        txtHeader.setText("Details");
        ImageView imgBack = (ImageView)findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button btnCall = (Button)findViewById(R.id.callBtn);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!num.equals(""))
                    Globals.call(Activity_Job_Details.this,num);
            }
        });
        getJobs(getIntent().getIntExtra("id",-1));
    }

    private void getJobs(int id){
        Custom_ConnectionDetector cd = new Custom_ConnectionDetector(this);
        if(!cd.isConnectingToInternet()){
            Globals.showAlert("Error",Globals.INTERNET_ERROR,this);
            return;
        }else {
            try {
                pd = Globals.showLoadingDialog(pd, this, false, "");
                //later add catid and cityid in map to get catid and cityid wise jobs
                HashMap<String,String> map = new HashMap<>();
                if(id!=-1){
                    map.put("id",id+"");
                }

                Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                        Request.Method.POST,
                        Custom_URLs_Params.getURL_Get_Jobs(),
                        map, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("SUSHIL", "json Response recieved !!" + response);
                        Globals.hideLoadingDialog(pd);
                        Response(response);

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

    private void Response(JSONObject obj){
        if(obj==null){
            return;
        }else{
            try{
                if(obj.has("data")){
                    JSONArray listJobs = obj.getJSONArray("data");
                    if(listJobs.length()!=0){
                        JSONObject object = listJobs.getJSONObject(0);
                        if(object!=null){
                            if(object.has("title")){
                                if (object.getString("title") != null) {
                                    LinearLayout linear = (LinearLayout)findViewById(R.id.linearTitle);
                                    linear.setVisibility(View.VISIBLE);
                                    TextView txt = (TextView) findViewById(R.id.txtTitle);
                                    txt.setText(object.getString("title"));
                                }
                            }

                            if(object.has("personnumber") && object.has("personname")){
                                if (object.getString("personnumber") != null && object.getString("personname") != null) {
                                    LinearLayout linear = (LinearLayout)findViewById(R.id.linearEmployer);
                                    linear.setVisibility(View.VISIBLE);
                                    TextView txt = (TextView) findViewById(R.id.txtEmplyerName);
                                    TextView txtNum = (TextView) findViewById(R.id.txtNumberEmplyer);
                                    txt.setText("Address"+object.getString("personname"));
                                    txtNum.setText(object.getString("personnumber"));
                                }
                            }


                            if(object.has("eligibility")){
                                if (object.getString("eligibility") != null) {
                                    LinearLayout linear = (LinearLayout)findViewById(R.id.linearEligi);
                                    linear.setVisibility(View.VISIBLE);
                                    TextView txt = (TextView) findViewById(R.id.txtEligiblity);
                                    if(object.has("mexp") && object.getString("mexp")!=null && !object.getString("mexp").equals("") ) {
                                        txt.setText(object.getString("eligibility") + "\n" + "Minimum Experience:- "+object.getString("mexp"));
                                    }else{
                                        txt.setText(object.getString("eligibility"));
                                    }
                                }
                            }
                            if(object.has("timings")){
                                if (object.getString("timings") != null && !object.getString("timings").equals("")) {
                                    LinearLayout linear = (LinearLayout)findViewById(R.id.linearTimings);
                                    linear.setVisibility(View.VISIBLE);
                                    TextView txt = (TextView) findViewById(R.id.txtTimings);
                                    txt.setText(object.getString("timings"));
                                }
                            }

                            if(object.has("holidays")){
                                if (object.getString("holidays") != null && !object.getString("holidays").equals("")) {
                                    LinearLayout linear = (LinearLayout)findViewById(R.id.linearHolidays);
                                    linear.setVisibility(View.VISIBLE);
                                    TextView txt = (TextView) findViewById(R.id.txtHolidays);
                                    txt.setText(object.getString("holidays"));
                                }
                            }

                            if(object.has("salary")){
                                if (object.getString("salary") != null && !object.getString("salary").equals("")) {
                                    LinearLayout linear = (LinearLayout)findViewById(R.id.linearPayScale);
                                    linear.setVisibility(View.VISIBLE);
                                    TextView txt = (TextView) findViewById(R.id.txtPayScale);
                                    txt.setText(object.getString("salary")+"/-");
                                }
                            }

                            if(object.has("job_profile")){
                                if (object.getString("job_profile") != null && !object.getString("job_profile").equals("")) {
                                    LinearLayout linear = (LinearLayout)findViewById(R.id.linearJobProfile);
                                    linear.setVisibility(View.VISIBLE);
                                    TextView txt = (TextView) findViewById(R.id.txtJobProfile);
                                    txt.setText(object.getString("job_profile"));
                                }
                            }

                            if(object.has("others")){
                                if (object.getString("others") != null && !object.getString("others").equals("")) {
                                    LinearLayout linear = (LinearLayout)findViewById(R.id.linearOthers);
                                    linear.setVisibility(View.VISIBLE);
                                    TextView txt = (TextView) findViewById(R.id.txtOthers);
                                    txt.setText(object.getString("others"));
                                }
                            }

                            if(object.has("personnumber")){
                                if (object.getString("personnumber") != null) {
                                   num = object.getString("personnumber");
                                }
                            }
                        }
                    }else{
                        Globals.showShortToast(this,"No Data found. Try later.");
                    }
                }
            }catch (JSONException ex){
                ex.printStackTrace();
            }
        }
    }
}
