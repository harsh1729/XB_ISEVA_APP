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
import org.ksoap2.serialization.SoapObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class Activity_show_booked_ticket extends Activity {

    private Session_manager session_manager;
    private TextView header_tv;
    private ImageView header_iv;
    private TextView header_tv_second;

    private ProgressDialog progress;

    private SoapObject soapresult_iscancelable;
    private SoapObject soapresult_cancel_ticket;

    private String pnr_no_txt;
    private String ticket_no_txt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_booked_ticket);
        initialize();
        setclicklistner();
        show_booked_list();
    }


    public void initialize()
    {
        session_manager = new Session_manager(this);
        header_tv = (TextView)findViewById(R.id.header_text);
        header_iv = (ImageView)findViewById(R.id.header_back_button);
        header_tv.setText("Booked Tickets");


    }

    public void show_booked_list()
    {
        if(isNetworkConnected())
        {
            servercall("0");
        }
        else
        {
            callAlertBox();
        }
    }

    public void callAlertBox()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_show_booked_ticket.this);
        builder.setTitle(R.string.internet_connection_error_title)
                .setMessage(R.string.internet_connection_error_message_try_again)
                .setCancelable(false)
                .setNegativeButton("Retry",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(isNetworkConnected())
                        {
                            servercall("0");
                            dialog.cancel();
                        }
                        else
                        {
                            callAlertBox();
                        }

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

        Button b = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        b.setTextColor(ContextCompat.getColor(Activity_show_booked_ticket.this, R.color.app_theme_color));

    }

    public void servercall(final String offset)
    {
        progress = new ProgressDialog(Activity_show_booked_ticket.this);
        progress.setMessage("Please wait...");
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);
        progress.show();

        StringRequest bookedTicketreq = new StringRequest(Request.Method.POST,
                Constants.Get_booked_ticket, new Response.Listener<String>() {

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

                            if(response.getJSONArray("data").length() == 0)
                            {
                                Toast.makeText(Activity_show_booked_ticket.this,"No more tickets to show",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                LinearLayout main_layout = (LinearLayout)findViewById(R.id.activity_show_booked_ticket_main_layout);
                                main_layout.removeAllViews();
                                for(int i=0;i<response.getJSONArray("data").length();i++)
                                {
                                    Log.e("vikas",""+i);
                                    LayoutInflater inflater = (LayoutInflater)Activity_show_booked_ticket.this.getSystemService
                                            (Context.LAYOUT_INFLATER_SERVICE);
                                    View v =  inflater.inflate(R.layout.booked_ticket_single_layout,null);
                                    LinearLayout main = (LinearLayout)v.findViewById(R.id.booked_ticket_single_layout_main);
                                    final TextView booked_date = (TextView)v.findViewById(R.id.booked_ticket_single_layout_booked_date);
                                    TextView From = (TextView)v.findViewById(R.id.booked_ticket_single_layout_from_city_tv);
                                    TextView To = (TextView)v.findViewById(R.id.booked_ticket_single_layout_to_city_tv);
                                    TextView ticket_status = (TextView)v.findViewById(R.id.booked_ticket_single_layout_ticket_status_tv);

                                    main.setTag(R.string.activity_show_booked_ticket_tag_id,response.getJSONArray("data").getJSONObject(i).getString("id"));
                                    main.setTag(R.string.activity_show_booked_ticket_tag_pnr_no,response.getJSONArray("data").getJSONObject(i).getString("pnr_no"));
                                    main.setTag(R.string.activity_show_booked_ticket_tag_ticket_no,response.getJSONArray("data").getJSONObject(i).getString("ticket_no"));
                                    main.setTag(R.string.activity_show_booked_ticket_tag_iscancelable,response.getJSONArray("data").getJSONObject(i).getString("cancelable"));
                                    main.setTag(R.string.activity_show_booked_ticket_tag_payment_id,response.getJSONArray("data").getJSONObject(i).getString("payumoney_payment_id"));
                                    main.setTag(R.string.activity_show_booked_ticket_tag_extra_charge,response.getJSONArray("data").getJSONObject(i).getString("extra_charge"));
                                    main.setTag(R.string.activity_show_booked_ticket_tag_total_fare,response.getJSONArray("data").getJSONObject(i).getString("total_fare"));


                                    From.setText(response.getJSONArray("data").getJSONObject(i).getString("from"));
                                    To.setText(response.getJSONArray("data").getJSONObject(i).getString("to"));
                                    booked_date.setText(change_date_form(response.getJSONArray("data").getJSONObject(i).getString("ticket_date")));
                                    if(response.getJSONArray("data").getJSONObject(i).getString("ticket_status").equals("confirmed"))
                                    {
                                        ticket_status.setText(response.getJSONArray("data").getJSONObject(i).getString("ticket_status").toUpperCase());
                                        main.setOnClickListener(new View.OnClickListener(){

                                            @Override
                                            public void onClick(View view) {
                                                Intent i = new Intent(Activity_show_booked_ticket.this,Activity_show_detail_booked_ticket.class);
                                                i.putExtra("pnr_no",view.getTag(R.string.activity_show_booked_ticket_tag_pnr_no).toString());
                                                i.putExtra("ticket_no",view.getTag(R.string.activity_show_booked_ticket_tag_ticket_no).toString());
                                                i.putExtra("iscancelable",view.getTag(R.string.activity_show_booked_ticket_tag_iscancelable).toString());
                                                i.putExtra("payu_payment_id",view.getTag(R.string.activity_show_booked_ticket_tag_payment_id).toString());
                                                i.putExtra("total_fare",view.getTag(R.string.activity_show_booked_ticket_tag_total_fare).toString());
                                                i.putExtra("extra_charge",view.getTag(R.string.activity_show_booked_ticket_tag_extra_charge).toString());
                                                startActivity(i);
                                                overridePendingTransition(R.anim.anim_in, R.anim.anim_none);
                                            }
                                        });
                                    }
                                    else
                                    {
                                        ticket_status.setText(response.getJSONArray("data").getJSONObject(i).getString("ticket_status").toUpperCase());
                                        ticket_status.setTextColor(ContextCompat.getColor(Activity_show_booked_ticket.this,R.color.red_color));
                                        main.setOnClickListener(new View.OnClickListener(){

                                            @Override
                                            public void onClick(View view) {
                                                Intent i = new Intent(Activity_show_booked_ticket.this,Activity_show_detail_cancel_ticket.class);
                                                i.putExtra("id",view.getTag(R.string.activity_show_booked_ticket_tag_id).toString());
                                                startActivity(i);
                                                overridePendingTransition(R.anim.anim_in, R.anim.anim_none);
                                            }
                                        });
                                    }




                                    main_layout.addView(v);

                                }
                            }
                        }
                        else
                        {
                            Toast.makeText(Activity_show_booked_ticket.this,"server not responding",Toast.LENGTH_LONG).show();
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
                params.put("offset", offset);
                params.put("user_id",session_manager.getid());
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(Activity_show_booked_ticket.this);
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

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_show_booked_ticket.this);
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
        b.setTextColor(ContextCompat.getColor(Activity_show_booked_ticket.this, R.color.app_white));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Activity_show_detail_booked_ticket.cancel_ticket == 1)
        {
            servercall("0");
        }
    }
}
