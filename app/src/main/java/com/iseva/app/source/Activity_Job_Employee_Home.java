package com.iseva.app.source;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class Activity_Job_Employee_Home extends Activity {

    private ProgressDialog pd;
    private LinearLayout linear;
   // private LinearLayout linearBtn;
    private LinearLayout logoutOut;
    private Button imgLogin;
    private Button btnPostResume;
    private boolean isEdit = false;
    private TextView txtDes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_jobs);
        init();
    }

   private void init(){
       setFooterAndHeader(R.id.imgBtnFooterPostJob);
       txtDes = (TextView)findViewById(R.id.txtDes);
       linear = (LinearLayout)findViewById(R.id.linearProfile);
       //linearBtn = (LinearLayout)findViewById(R.id.linear);
       imgLogin = (Button)findViewById(R.id.btnLogin);
       btnPostResume = (Button)findViewById(R.id.btnpostResume);

       logoutOut = (LinearLayout)findViewById(R.id.logout);
       logoutOut.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               isEdit = false;
               linear.setVisibility(View.GONE);
               txtDes.setVisibility(View.VISIBLE);
               //linearBtn.setVisibility(View.VISIBLE);
               btnPostResume.setText("Register");
               imgLogin.setVisibility(View.VISIBLE);
               logoutOut.setVisibility(View.GONE);
               Object_AppConfig obj = new Object_AppConfig(Activity_Job_Employee_Home.this);
               obj.setEmpId(-1);
           }
       });
   }

    @Override
    protected void onResume() {
        super.onResume();
        isEdit = false;


        Object_AppConfig config = new Object_AppConfig(this);
         if(config.getEmpId()!=-1){
             getDeatils();
         }
    }


    private void getDeatils(){
        Custom_ConnectionDetector connection = new Custom_ConnectionDetector(this);
        if (!connection.isConnectingToInternet()) {
            Globals.showAlert("ERROR", Globals.INTERNET_ERROR, this);
        } else {
            try {
                pd = Globals.showLoadingDialog(pd, this, false, "");
                HashMap<String, String> map = new HashMap<String, String>();
                Object_AppConfig obj = new Object_AppConfig(this);
                map.put("id",obj.getEmpId()+"");
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
                                Activity_Job_Employee_Home.this,
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

    private void parseResume(JSONObject obj) {
        if (obj == null) {
            Globals.showShortToast(this, "Data not Found");
            finish();
        } else {
            try {
                JSONArray list = obj.getJSONArray("data");
                if (list.length() != 0) {

                    linear.setVisibility(View.VISIBLE);
                    txtDes.setVisibility(View.GONE);
                    //linearBtn.setVisibility(View.GONE);
                    imgLogin.setVisibility(View.GONE);
                    logoutOut.setVisibility(View.VISIBLE);
                    JSONObject object = list.getJSONObject(0);
                    /*if (object.has("image")) {
                        Custom_RoundedImageView img = (Custom_RoundedImageView)findViewById(R.id.view);
                        JSONObject imgObj = object.getJSONObject("image");
                        Globals.loadImageIntoImageView(img,imgObj.getString("imageurl"),this,R.drawable.default_offer,R.drawable.default_offer);
                    }*/

                    if (object.has("name")) {
                        if (object.getString("name") != null) {
                            TextView txtName = (TextView) findViewById(R.id.textView);
                            txtName.setText("Welcome "+object.getString("name"));

                        }
                        Object_AppConfig config = new Object_AppConfig(this);
                        config.setEmployeeResponse(object.toString());
                        isEdit = true;
                        btnPostResume.setText("Post Resume");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void postResume(View v){
        Custom_ConnectionDetector connection = new Custom_ConnectionDetector(this);
        if (!connection.isConnectingToInternet()) {
            Globals.showAlert("ERROR", Globals.INTERNET_ERROR, this);
        } else {
            Intent i = null;
            if (!isEdit) {
                i = new Intent(this, Activity_Job_Employee_Employer_Basic.class);
                Globals.iAgree(this, i,getResources().getString(R.string.agreement_employee));
                //i.putExtra("isEdit",isEdit);
            } else {
                i = new Intent(this, Activity_Post_Resume.class);
                i.putExtra("isEdit", true);
                startActivity(i);
            }
        }
       // startActivity(i);

    }

    public void login(View v){
        Custom_ConnectionDetector connection = new Custom_ConnectionDetector(this);
        if (!connection.isConnectingToInternet()) {
            Globals.showAlert("ERROR", Globals.INTERNET_ERROR, this);
        } else {
            Intent i = new Intent(this, Activity_Login_Jobs_Dep.class);
            i.putExtra("bool", true);
            startActivity(i);
        }
    }

    public void getJobs(View v){
        Custom_ConnectionDetector connection = new Custom_ConnectionDetector(this);
        if (!connection.isConnectingToInternet()) {
            Globals.showAlert("ERROR", Globals.INTERNET_ERROR, this);
        } else {
            Intent i = new Intent(this, Activity_Job_Resume_Search.class);
            i.putExtra("isJobSearch", true);
            startActivity(i);
        }
    }
    public void footerBtnClick(View v){

        Class<?> nextClass = null;

        switch (v.getId()) {

            case R.id.imgBtnFooterPostResume:
                nextClass = Activity_Job_Employer_Home.class;
                break;
            case R.id.imgBtnFooterPostJob:
                nextClass = Activity_Job_Employee_Home.class;
                break;

            default:
                break;
        }

        if(nextClass != null){
            if(this.getClass() != nextClass){
                Intent i = new Intent(this,nextClass);
                startActivity(i);
            }
        }

    }

    private void setFooterAndHeader(int footerbtnId){
        if(footerbtnId != -1)
        {
            View v = this.findViewById(footerbtnId);
            if(v!= null){
                v.setSelected(true);
                Log.i("FOOTER", "Set Selected");
            }else{
                Log.i("FOOTER", "Null Button");
            }
        }


    }
}
