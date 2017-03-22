package com.iseva.app.source.travel;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.iseva.app.source.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Activity_register extends Activity {

    private TextView header_tv;
    private ImageView header_iv;
    private TextView header_tv_second;

    private EditText register_name_et;
    private EditText register_email_et;
    private EditText register_phone_et;
    private EditText register_password_et;
    private EditText register_confirm_password_et;

    private String register_name_et_text;
    private String register_email_et_text;
    private String register_phone_et_text;
    private String register_password_et_text;
    private String register_password_confirm_et_text;

    private ProgressDialog progressBar;
    private Session_manager session_manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initialize();
        setclicklistner();
    }

    public void initialize()
    {
        header_tv = (TextView)findViewById(R.id.header_text);
        header_iv = (ImageView)findViewById(R.id.header_back_button);

        register_email_et = (EditText)findViewById(R.id.activity_register_et_email);
        register_name_et = (EditText)findViewById(R.id.activity_register_et_username);
        register_phone_et = (EditText)findViewById(R.id.activity_register_et_phone);
        register_password_et = (EditText)findViewById(R.id.activity_register_et_password);
        register_confirm_password_et = (EditText)findViewById(R.id.activity_register_et_password_confirm);

        header_tv.setText("Register");
    }

    public void register(View view)
    {
        register_name_et_text = register_name_et.getText().toString().trim();
        register_email_et_text = register_email_et.getText().toString().trim();
        register_phone_et_text = register_phone_et.getText().toString().trim();
        register_password_et_text = register_password_et.getText().toString().trim();
        register_password_confirm_et_text = register_confirm_password_et.getText().toString().trim();

        if(validate())
        {
            progressBar = new ProgressDialog(Activity_register.this);
            progressBar.setCancelable(false);
            progressBar.setMessage("Loading ...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            progressBar.show();

            StringRequest loginreq = new StringRequest(Request.Method.POST,
                    Constants.Signup_url, new Response.Listener<String>() {

                @Override
                public void onResponse(String s) {
                    progressBar.dismiss();
                    VolleyLog.d("vikas", "Response: " + s.toString());
                    JSONObject response = null;
                    try {
                        response = new JSONObject(s);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.d(String.valueOf(getApplicationContext()), "Response generated");
                    if (response != null)
                    {

                        try
                        {
                            if(response.getInt("success") == 1)
                            {
                                session_manager = new Session_manager(Activity_register.this);
                                session_manager.createLoginSession(response.getJSONObject("data").getString("id")
                                        ,response.getJSONObject("data").getString("name"),response.getJSONObject("data").getString("email")
                                        ,response.getJSONObject("data").getString("phone"));
                                Toast.makeText(Activity_register.this,response.getString("message"),Toast.LENGTH_LONG).show();
                                activity_dismiss();


                            }
                            else
                            {
                                Toast.makeText(Activity_register.this,response.getString("message"),Toast.LENGTH_LONG).show();
                            }

                        }
                        catch (JSONException e)
                        {

                        }

                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                   progressBar.dismiss();
                }
            }) {
                @Override
                public Map<String, String> getParams(){
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("username",register_name_et_text);
                    params.put("email", register_email_et_text);
                    params.put("phone",register_phone_et_text);
                    params.put("password",register_password_et_text);
                    return params;

                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(Activity_register.this);
            requestQueue.add(loginreq);
        }
    }

    public Boolean validate()
    {



        Boolean Flag = true;
        if(register_name_et_text.length() == 0)
        {
            showAlertDialog(getResources().getString(R.string.validating_error_title),"Please enter name","Ok");
            Flag = false;
            return Flag;
        }
        else if(register_email_et_text.trim().length() == 0 || !validatemail(register_email_et_text))
        {
            showAlertDialog(getResources().getString(R.string.validating_error_title),"Please enter valid email","Ok");

            Flag = false;
            return Flag;
        }
        else if(register_phone_et_text.trim().length() == 0)
        {
            showAlertDialog(getResources().getString(R.string.validating_error_title),"Please enter phone number","Ok");

            Flag = false;
            return Flag;
        }
        else if(register_password_et_text.trim().length() == 0)
        {
            showAlertDialog(getResources().getString(R.string.validating_error_title),"Please enter password","Ok");

            Flag = false;
            return Flag;
        }
        else if(register_password_confirm_et_text.trim().length() == 0)
        {
            showAlertDialog(getResources().getString(R.string.validating_error_title),"Please enter confirm password","Ok");

            Flag = false;
            return Flag;
        }
        else if(register_password_et_text.trim().equals(register_password_confirm_et_text.trim()) == false )
        {
            showAlertDialog(getResources().getString(R.string.validating_error_title),"Password and Password Confirm Should be Same !","Ok");

            Flag = false;
            return Flag;
        }
        else
        {
            Flag = true;
            return  Flag;
        }
    }
    public void setclicklistner()
    {
        header_iv.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                activity_dismiss();
            }
        });
    }

    public void activity_dismiss()
    {
        this.finish();
        overridePendingTransition(R.anim.anim_none, R.anim.anim_out);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        overridePendingTransition(R.anim.anim_none, R.anim.anim_out);



    }

    public void showAlertDialog(String title,String message,String buttonlabel)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_register.this);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton(buttonlabel,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button b = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        b.setLayoutParams(lp);
        b.setBackgroundResource(R.drawable.btn_background);
        b.setTextColor(ContextCompat.getColor(Activity_register.this, R.color.app_white));
    }

    public static boolean validatemail(String emailStr) {
        Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

}
