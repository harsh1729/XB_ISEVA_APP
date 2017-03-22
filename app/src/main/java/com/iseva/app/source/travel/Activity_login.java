package com.iseva.app.source.travel;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.iseva.app.source.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Activity_login extends Activity {

    private Session_manager session_manager;
    private TextView header_tv;
    private ImageView header_iv;
    private TextView header_tv_second;

    private EditText login_email_et;
    private EditText login_password_et;

    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();
        setclicklistner();
    }

    public void initialize()
    {
        header_tv = (TextView)findViewById(R.id.header_text);
        header_iv = (ImageView)findViewById(R.id.header_back_button);
        login_email_et = (EditText)findViewById(R.id.activity_login_et_email);
        login_password_et = (EditText)findViewById(R.id.activity_login_et_password);
        header_tv.setText("Login");
    }

    public void go_signup_activity(View v)
    {
        Intent i = new Intent(Activity_login.this,Activity_register.class);
        startActivity(i);
        overridePendingTransition(R.anim.anim_in, R.anim.anim_none);
        Activity_login.this.finish();
    }

    public void loginauth(View view)
    {
        String login_email_et_text = login_email_et.getText().toString().trim();
        String login_password_et_text = login_password_et.getText().toString().trim();

        if(login_email_et_text.length() == 0 || !validatemail(login_email_et_text))
        {
            showAlertDialog(getResources().getString(R.string.validating_error_title),"Insert valid email !","Ok");
        }
        else if(login_password_et_text.length() == 0)
        {
            showAlertDialog(getResources().getString(R.string.validating_error_title),"Password can not be empty","Ok");
        }
        else
        {
            serverlogin(login_email_et_text,login_password_et_text);
        }


    }


    public void serverlogin(final String username, final String password)
    {
        progressBar = new ProgressDialog(Activity_login.this);
        progressBar.setCancelable(false);
        progressBar.setMessage("Loading ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progressBar.show();

        StringRequest loginreq = new StringRequest(Request.Method.POST,
                Constants.Login_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {


                JSONObject response = null;
                try {
                    response = new JSONObject(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if (response != null)
                {
                    Log.e("vikas",response.toString());

                    try
                    {
                        if(response.getInt("success") == 1)
                        {
                            session_manager = new Session_manager(Activity_login.this);
                            session_manager.createLoginSession(response.getJSONObject("data").getString("id")
                            ,response.getJSONObject("data").getString("name"),response.getJSONObject("data").getString("email")
                            ,response.getJSONObject("data").getString("phone"));
                            Toast.makeText(Activity_login.this,response.getString("message"),Toast.LENGTH_LONG).show();
                            activity_dismiss();

                        }
                        else
                        {
                            Toast.makeText(Activity_login.this,response.getString("message"),Toast.LENGTH_LONG).show();
                        }

                    }
                    catch (JSONException e)
                    {

                    }

                }
                progressBar.dismiss();
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
                params.put("email", username);
                params.put("password",password);
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Activity_login.this);
        requestQueue.add(loginreq);
    }

    public static boolean validatemail(String emailStr) {
        Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
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
        TextView title_tv = new TextView(this);
        title_tv.setPadding(0,10,0,0);
        title_tv.setTextColor(ContextCompat.getColor(Activity_login.this,R.color.black));
        title_tv.setTextSize(18);
        title_tv.setTypeface(null, Typeface.BOLD);
        title_tv.setGravity(Gravity.CENTER);
        title_tv.setText(title);

        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_login.this);
        builder.setCustomTitle(title_tv)
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
        b.setTextColor(ContextCompat.getColor(Activity_login.this, R.color.app_white));
    }
}
