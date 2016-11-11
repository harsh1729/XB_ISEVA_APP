package com.iseva.app.source;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public class Activity_Resume_Post_Basic extends Activity {

    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_post_basic);

        init();
    }

    private void init(){
        Button btn = (Button)findViewById(R.id.btnRegister);
        TextView txtHeader = (TextView)findViewById(R.id.txtHeader);
        txtHeader.setText("Post Resume");
        ImageView imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    private void saveData() {
        EditText name = (EditText) findViewById(R.id.edtName);
        EditText number = (EditText) findViewById(R.id.edtNumber);
        EditText username = (EditText) findViewById(R.id.edtUsername);
        EditText password = (EditText) findViewById(R.id.edtPassword);


        if (name.getText().toString().equals("")) {
            Globals.showShortToast(this, "Enter your Name");
            return;
        }
        if (number.getText().toString().toString().equals("")) {
            Globals.showShortToast(this, "Enter Phone Number");
            return;
        }
        if (username.getText().toString().toString().equals("")) {
            Globals.showShortToast(this, "Enter your Email");
            return;
        }
        if (password.getText().toString().toString().equals("")) {
            Globals.showShortToast(this, "Enter your Password");
            return;
        }

            Custom_ConnectionDetector cd = new Custom_ConnectionDetector(this);
            if (cd.isConnectingToInternet()) {
                pd = Globals.showLoadingDialog(pd,this,false,"");
                try {
                    Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                            Request.Method.POST,
                            Custom_URLs_Params.getURL_ResumeRegister(),
                            Custom_URLs_Params.getParams_ResumeRegister(name.getText().toString(), number.getText().toString(), username.getText().toString(), password.getText().toString()), new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("SUSHIL", "json Response recieved !!" + response);
                            Globals.hideLoadingDialog(pd);
                            contentResponce(response);
                        }


                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError err) {
                            Log.i("SUSHIL", "ERROR VolleyError");
                            Globals.hideLoadingDialog(pd);
                            Globals.showShortToast(Activity_Resume_Post_Basic.this,Globals.MSG_SERVER_ERROR);

                        }
                    });

                    Custom_VolleyAppController.getInstance().addToRequestQueue(
                            jsonObjectRQST);
                } catch (Exception e) {
                    e.printStackTrace();
                    Globals.hideLoadingDialog(pd);
                }
            } else {
                Globals.showShortToast(this, Globals.INTERNET_ERROR);
            }

        }

    private void contentResponce(JSONObject obj){
        if(obj==null){
            return;
        }else{
            try{
                if(obj.has("success")){
                    if(obj.getInt("success")==1){
                        Globals.showShortToast(this,"You are Succesfully Register.");
                        Object_AppConfig config = new Object_AppConfig(this);
                        if(obj.has("data")){
                            JSONObject jsonObject = obj.getJSONObject("data");
                            config.setEmpId(jsonObject.getInt("empid"));
                            navigationAllData();
                        }
                        //clearForm((ViewGroup) findViewById(R.id.linear));
                    }
                }
            }catch (JSONException ex){
                ex.printStackTrace();
            }
        }
    }

    private void navigationAllData(){
        Globals.clearForm((ViewGroup) findViewById(R.id.linear));
        Intent i = new Intent(this,Activity_Post_Resume.class);
        startActivity(i);
        finish();
    }
}
