package com.iseva.app.source;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;


import org.json.JSONException;
import org.json.JSONObject;

public class Activity_RegisterUser extends AppCompatActivity {

    EditText edtPassword;
    ProgressDialog pd;
    private CheckBox promoCodeCheckBox;
    String promotext = "";
    private boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
         //JSONObject obj=new JSONObject();
       // merchantRegister(obj);
        inti();
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void inti() {
        TextView txtHeader = (TextView) findViewById(R.id.txtHeader);
        txtHeader.setText("Register");
        ImageView imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity_RegisterUser.this.finish();
            }
        });

        edtPassword = (EditText) findViewById(R.id.edtPassword);
        final CheckBox showPasswordCheckBox = (CheckBox) findViewById(R.id.checkBoxPassword);
        showPasswordCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showPasswordCheckBox.isChecked()) {
                    edtPassword.setTransformationMethod(null);
                } else {
                    edtPassword.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });

        promoCodeCheckBox = (CheckBox) findViewById(R.id.checkBoxPromoCode);
        final LinearLayout linearPromo = (LinearLayout) findViewById(R.id.PromoCode);
        promoCodeCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (promoCodeCheckBox.isChecked()) {
                    linearPromo.setVisibility(View.VISIBLE);
                } else {
                    linearPromo.setVisibility(View.GONE);
                }
            }
        });
        //register button click
        Button btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser(v);
                //btnClick(null);
            }
        });


    }

    private void btnClick(JSONObject obj) {
        if (obj == null) {
            return;
        } else {
           // CheckBox checkMerchant = (CheckBox) findViewById(R.id.checkBoxRegister);
            //if (checkMerchant.isChecked()) {
                merchantRegister(obj);
            /*}else{
                Log.i("SUSHIL","Call finish method ");
                this.finish();

            }*/
       }

    }

    private void merchantRegister(JSONObject obj) {
        Intent i = new Intent(this, Activity_Category_Choose.class);
        //i.putExtra("userdata",obj.toString());
        i.putExtra("object",obj.toString());
        i.putExtra("intent","");
        startActivity(i);
        this.finish();
    }

    private void registerUser(View v) {

        Custom_ConnectionDetector con = new Custom_ConnectionDetector(this);
        if (!con.isConnectingToInternet()) {
            Globals.showAlert("Internet", Globals.INTERNET_ERROR, this);
        } else {
            promotext = "";
            EditText edtUsername = (EditText) findViewById(R.id.edtDoctorName);
            //EditText edtUserEmail = (EditText) findViewById(R.id.edtEmail);
            EditText edtUserNumber = (EditText) findViewById(R.id.edtNumber);
            EditText edtUsername_login = (EditText) findViewById(R.id.edtUsername);
            EditText edtUserPassword = (EditText) findViewById(R.id.edtPassword);

            edtUsername.setError(null);
           // edtUserEmail.setError(null);
            edtUsername_login.setError(null);
            edtUserNumber.setError(null);
            edtUserPassword.setError(null);

            if (edtUsername.getText().toString().isEmpty()) {
                //Toast.makeText(this,"Please add name", Toast.LENGTH_SHORT).show();
                edtUsername.setError("Please add Name");
                edtUsername.requestFocus();
                return;
            }
            /*if (edtUserEmail.getText().toString().isEmpty()) {
                //Toast.makeText(this,"Please add Email Address", Toast.LENGTH_SHORT).show();
                edtUserEmail.setError("Please add Email Address");
                edtUserEmail.requestFocus();
                return;
            } else {*/
               /* if (!isValidEmail(edtUserEmail.getText().toString())) {
                    edtUserEmail.setError("Please add a Valid Email Address");
                    edtUserEmail.requestFocus();
                    return;
                }*/
           // }
            if (edtUserNumber.getText().toString().isEmpty()) {
                //Toast.makeText(this,"Please add Number", Toast.LENGTH_SHORT).show();
                edtUserNumber.setError("Please add Phone Number");
                edtUserNumber.requestFocus();
                return;
            } /*else {
                if (!isPhoneValid(edtUserNumber.getText().toString())) {
                    edtUserNumber.setError("Please add Valid Phone Number");
                    edtUserNumber.requestFocus();
                    return;
                }
            }*/
            if (edtUsername_login.getText().toString().isEmpty()) {
                //Toast.makeText(this,"Please add Username", Toast.LENGTH_SHORT).show();
                edtUsername_login.setError("Please add Username");
                edtUsername_login.requestFocus();
                return;

            } else {
                if (!Globals.isUsernameValid(edtUsername_login.getText().toString())) {
                    //Toast.makeText(this,"Please add Valid Username.length upto 8 ", Toast.LENGTH_SHORT).show();
                    edtUsername_login.setError("Username length should be greater than 6 characters.");
                    edtUsername_login.requestFocus();
                    return;
                }
            }
            if (edtUserPassword.getText().toString().isEmpty()) {
                //Toast.makeText(this,"Please add Valid Password", Toast.LENGTH_SHORT).show();
                edtUserPassword.setError("Please add Valid Password");
                edtUserPassword.requestFocus();
                return;
            } else {
                if (!isPasswordValid(edtUserPassword.getText().toString())) {
                    //Toast.makeText(this,"Please add Valid Password.length upto 4 ", Toast.LENGTH_SHORT).show();
                    edtUserPassword.setError("Password should be alpha numeric a combination of numbers & alphabets.");
                    edtUserPassword.requestFocus();
                    return;
                }
            }

            if (promoCodeCheckBox.isChecked()) {
                EditText edtUserPromo = (EditText) findViewById(R.id.edtPromo);
                edtUserPromo.setError(null);
                if (edtUserPromo.getText().toString().isEmpty()) {
                    edtUserPromo.setError("Please add Promo Code");
                }else{
                    promotext = edtUserPromo.getText().toString();
                }
            }

            //add volley call here
            try {
                pd = Globals.showLoadingDialog(pd, this, false, "");
                Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                        Request.Method.POST,
                        Custom_URLs_Params.getURL_UserRegister(),
                        Custom_URLs_Params.getParams_RegisterUser(this,edtUsername.getText().toString(), edtUserNumber.getText().toString(),
                                edtUsername_login.getText().toString(), edtUserPassword.getText().toString(),promotext),
                        new Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {/*edtUserEmail.getText().toString()*/
                                Globals.hideLoadingDialog(pd);
                                Log.i("SUSHIL", "json Response recieved !!" + response);
                                parseRegisterNewUser(response);

                            }

                        }, new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError err) {
                        Globals.hideLoadingDialog(pd);
                        Log.i("SUSHIL", "ERROR VolleyError" + err);
                        Globals.showShortToast(
                                Activity_RegisterUser.this,
                                Globals.MSG_SERVER_ERROR);

                    }
                });

                Custom_VolleyAppController.getInstance().addToRequestQueue(
                        jsonObjectRQST);
            } catch (Exception ex) {
                ex.printStackTrace();
                Globals.hideLoadingDialog(pd);
                Globals.showShortToast(Activity_RegisterUser.this,
                        Globals.MSG_SERVER_ERROR);
            }

        }

    }


    private void parseRegisterNewUser(JSONObject obj) {
        if (obj == null) {
            return;
        } else {
            try {
                if (obj.has("success")) {
                    //Globals.showShortToast(this,responce.getString("states"));
                    int i = obj.getInt("success");
                    if (i == 0) {
                        Globals.showShortToast(this, obj.getString("error_message"));
                    } else {
                        if (obj.has("userdata")) {
                            JSONObject objectJson = obj.getJSONObject("userdata");
                            //JSONObject obj = responce.getJSONObject("userdata");
                            if (objectJson.has("userid")) {
                                int id = objectJson.getInt("userid");
                                Object_AppConfig config = new Object_AppConfig(this);
                                config.setUserId(id);
                                //config.setboolIslogin(true);
                                config.setPromoCode(objectJson.getString("promocode"));

                                //Changed by Harsh
                                //btnClick(objectJson);

                                btnClick(obj);
                            }

                        }
                    }
                }
            } catch (JSONException ex) {

            }
        }
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        // String patternNumeric   = "[0-9]";
        String pattern = "^(?=.*(\\d|\\W)).{5,20}$";
        if (password.matches(pattern)) {
            return true;
        }
        return false;
    }



}
