package com.iseva.app.source.travel;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Activity_show_detail_cancel_ticket extends Activity {


    private TextView header_tv;
    private ImageView header_iv;

    TextView cancel_ticket_detail_date;
    TextView cancel_ticket_detail_from_city;
    TextView cancel_ticket_detail_to_city;
    TextView cancel_ticket_detail_ticket_no;
    TextView cancel_ticket_detail_pnr_no;

    String ticket_id;

    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail_cancel_ticket);

        initialize();
        setclicklistner();
        if(isNetworkConnected())
        {
            getTicketdetail();
        }
        else
        {
            showAlertDialog(getResources().getString(R.string.internet_connection_error_title),getResources().getString(R.string.internet_connection_error_message),"Ok");
        }


    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public void initialize()
    {
        Intent i = getIntent();
        ticket_id = i.getStringExtra("id");

        header_tv = (TextView)findViewById(R.id.header_text);
        header_iv = (ImageView)findViewById(R.id.header_back_button);
        header_tv.setText("Cancel Ticket Detail");

        cancel_ticket_detail_date = (TextView)findViewById(R.id.activity_ticket_cancelled_date_tv);
        cancel_ticket_detail_from_city = (TextView)findViewById(R.id.activity_ticket_cancelled_from_city_tv);
        cancel_ticket_detail_to_city = (TextView)findViewById(R.id.activity_ticket_cancelled_to_city_tv);
        cancel_ticket_detail_ticket_no = (TextView)findViewById(R.id.activity_ticket_cancelled_ticket_no_tv);
        cancel_ticket_detail_pnr_no = (TextView)findViewById(R.id.activity_ticket_cancelled_pnr_no_tv);


    }
    public void getTicketdetail()
    {
        progress = new ProgressDialog(Activity_show_detail_cancel_ticket.this);
        progress.setMessage("Please wait...");
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);
        progress.show();

        StringRequest bookedTicketreq = new StringRequest(Request.Method.POST,
                Constants.Get_booked_ticket_detail, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {

                Log.e("vikas",s);
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




                                cancel_ticket_detail_date.setText(change_date_form(response.getJSONObject("data").getString("departure_date")));
                                cancel_ticket_detail_from_city.setText(response.getJSONObject("data").getString("from"));
                                cancel_ticket_detail_to_city.setText(response.getJSONObject("data").getString("to"));
                                cancel_ticket_detail_ticket_no.setText(response.getJSONObject("data").getString("ticket_no"));
                                cancel_ticket_detail_pnr_no.setText(response.getJSONObject("data").getString("pnr_no"));

                        }
                        else
                        {
                            Toast.makeText(Activity_show_detail_cancel_ticket.this,"server not responding",Toast.LENGTH_LONG).show();
                        }

                    }
                    catch (JSONException e)
                    {

                    }

                }
                progress.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();

            }
        }) {
            @Override
            public Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", ticket_id);
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Activity_show_detail_cancel_ticket.this);
        requestQueue.add(bookedTicketreq);
    }

    public String change_date_form(String date)
    {
        String[] days ={"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
        String[] months={"January","February","March","April","May","June","July","August","September","October","November","December"};

        String day = date.substring(8,10);
        String month = date.substring(5,7);
        String year = date.substring(0,4);

        int day_int = Integer.parseInt(day);
        int month_int =Integer.parseInt(month);
        int year_int = Integer.parseInt(year);

        Calendar calendar = Calendar.getInstance();
        calendar.set(year_int,month_int-1,day_int);

        int day_of_weak_int = calendar.get(calendar.DAY_OF_WEEK);
        String day_of_weak = days[day_of_weak_int-1];


        //String final_date = day+"-"+months[month_int]+"-"+year+","+day_of_weak;

        String final_date = day_of_weak+", "+day+" "+months[month_int-1];


        return final_date;
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
       /* TextView title_tv = new TextView(this);
        title_tv.setPadding(0,getResources().getDimensionPixelSize(R.dimen.padding_margin_10),0,0);
        title_tv.setTextColor(ContextCompat.getColor(Activity_show_detail_cancel_ticket.this,R.color.black));
        title_tv.setTextSize(16);
        title_tv.setGravity(Gravity.CENTER);
        title_tv.setText(title);*/

        LayoutInflater inflater = (LayoutInflater)Activity_show_detail_cancel_ticket.this.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        View v =  inflater.inflate(R.layout.textview,null);


        TextView title_tv = (TextView)v.findViewById(R.id.alert_title);
        title_tv.setText(title);

        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_show_detail_cancel_ticket.this);
        builder.setCustomTitle(title_tv)
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton(buttonlabel,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        activity_dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button b = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        b.setLayoutParams(lp);
        b.setBackgroundResource(R.drawable.btn_background);
        b.setTextColor(ContextCompat.getColor(Activity_show_detail_cancel_ticket.this, R.color.app_white));
    }
}
