package com.iseva.app.source.travel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iseva.app.source.R;

import org.json.JSONArray;

import java.util.Calendar;


public class Activity_ticket_confirmation extends Activity {

    LinearLayout layout_header_text;


    TextView ticket_confirmation_booking_date_tv;
    TextView ticket_confirmation_from_city_tv;
    TextView ticket_confirmation_to_city_tv;
    TextView ticket_confirmation_boarding_time;
    TextView ticket_confirmation_boarding_point_name_tv;
    TextView ticket_confirmation_company_name_tv;
    TextView ticket_confirmation_bus_label_tv;
    TextView ticket_confirmation_seat_no_tv;
    TextView ticket_confirmation_passenger_name_tv;
    TextView ticket_confirmation_boarding_point_address_tv;
    TextView ticket_confirmation_boarding_point_landmark_tv;
    TextView ticket_confirmation_boarding_point_mobile_tv;
    TextView ticket_confirmation_dropping_point_tv;
    TextView ticket_confirmation_total_fare;
    TextView ticket_confirmation_ticket_no_tv;
    TextView ticket_confirmation_pnr_no_tv;


    String ticket_confirmation_booking_date_txt;
    String ticket_confirmation_from_city_txt;
    String ticket_confirmation_to_city_txt;
    String ticket_confirmation_boarding_time_txt;
    String ticket_confirmation_boarding_point_name_txt;
    String ticket_confirmation_company_name_txt;
    String ticket_confirmation_bus_label_txt;
    String ticket_confirmation_seat_no_txt="";
    String ticket_confirmation_passenger_name_txt;
    String ticket_confirmation_boarding_point_address_txt;
    String ticket_confirmation_boarding_point_landmark_txt;
    String ticket_confirmation_boarding_point_mobile_txt;

    String ticket_confirmation_total_fare_txt;
    String ticket_confirmation_ticket_no_txt;
    String ticket_confirmation_pnr_no_txt;



    ImageView header_iv;
    TextView header_tv;


    String passengers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_confirmation);

        Intent i = getIntent();
        ticket_confirmation_pnr_no_txt = i.getStringExtra("pnr_no");
        ticket_confirmation_ticket_no_txt = i.getStringExtra("ticket_no");
        ticket_confirmation_booking_date_txt = change_date_form(i.getStringExtra("booking_date"));
        ticket_confirmation_from_city_txt = i.getStringExtra("from_city");
        ticket_confirmation_to_city_txt = i.getStringExtra("to_city");
        ticket_confirmation_boarding_time_txt = i.getStringExtra("boarding_time");
        ticket_confirmation_boarding_point_name_txt = i.getStringExtra("boarding_point_name");
        ticket_confirmation_company_name_txt = i.getStringExtra("company_name");
        ticket_confirmation_bus_label_txt = i.getStringExtra("bus_label");
        ticket_confirmation_passenger_name_txt = i.getStringExtra("passenger_name");
        ticket_confirmation_boarding_point_address_txt = i.getStringExtra("boarding_point_address");
        ticket_confirmation_boarding_point_landmark_txt = i.getStringExtra("boarding_point_landmark");
        ticket_confirmation_boarding_point_mobile_txt = i.getStringExtra("boarding_point_mobile");
        ticket_confirmation_total_fare_txt = i.getStringExtra("total_fare");

        passengers = i.getStringExtra("passanger");
        if (Global_Travel.build_type == 0)
        {
            Log.e("vikas passenger =",passengers);
        }

        try {
            JSONArray ps = new JSONArray(passengers);

            for(int j =0;j<ps.length();j++)
            {
                if(j == 0)
                {
                    ticket_confirmation_seat_no_txt = ticket_confirmation_seat_no_txt + ps.getJSONObject(j).getString("SeatNo");
                }
                else
                {
                    ticket_confirmation_seat_no_txt = ticket_confirmation_seat_no_txt +","+ ps.getJSONObject(j).getString("SeatNo");
                }


            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        header_iv = (ImageView)findViewById(R.id.header_back_button);
        header_tv = (TextView)findViewById(R.id.header_text);




        ticket_confirmation_ticket_no_tv = (TextView)findViewById(R.id.activity_ticket_confirmation_ticket_no_tv);
        ticket_confirmation_pnr_no_tv = (TextView)findViewById(R.id.activity_ticket_confirmation_pnr_no_tv);
        ticket_confirmation_booking_date_tv = (TextView)findViewById(R.id.activity_ticket_confirmation_booking_date_tv);
        ticket_confirmation_from_city_tv = (TextView)findViewById(R.id.activity_ticket_confirmation_from_city_tv);
        ticket_confirmation_to_city_tv = (TextView)findViewById(R.id.activity_ticket_confirmation_to_city_tv);
        ticket_confirmation_boarding_time = (TextView)findViewById(R.id.activity_ticket_confirmation_boarding_time_tv);
        ticket_confirmation_boarding_point_name_tv = (TextView)findViewById(R.id.activity_ticket_confirmation_boarding_name_tv);
        ticket_confirmation_company_name_tv = (TextView)findViewById(R.id.activity_ticket_confirmation_company_name_tv);
        ticket_confirmation_bus_label_tv = (TextView)findViewById(R.id.activity_ticket_confirmation_bus_label_tv);
        ticket_confirmation_seat_no_tv = (TextView)findViewById(R.id.activity_ticket_confirmation_seat_no_tv);
        ticket_confirmation_passenger_name_tv = (TextView)findViewById(R.id.activity_ticket_confirmation_passenger_name_tv);
        ticket_confirmation_boarding_point_address_tv = (TextView)findViewById(R.id.activity_ticket_confirmation_boarding_address_tv);
        ticket_confirmation_boarding_point_landmark_tv = (TextView)findViewById(R.id.activity_ticket_confirmation_boarding_landmark_tv);
        ticket_confirmation_boarding_point_mobile_tv = (TextView)findViewById(R.id.activity_ticket_confirmation_boarding_mobile_tv);
        ticket_confirmation_total_fare = (TextView)findViewById(R.id.activity_ticket_confirmation_total_fare_tv);


        header_tv.setText(getResources().getString(R.string.ticket_confirmation));
        header_iv.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                activity_dismiss();
            }
        });

        ticket_confirmation_ticket_no_tv.setText(ticket_confirmation_ticket_no_txt);
        ticket_confirmation_pnr_no_tv.setText(ticket_confirmation_pnr_no_txt);
        ticket_confirmation_booking_date_tv.setText(ticket_confirmation_booking_date_txt);
        ticket_confirmation_from_city_tv.setText(ticket_confirmation_from_city_txt);
        ticket_confirmation_to_city_tv.setText(ticket_confirmation_to_city_txt);
        ticket_confirmation_boarding_time.setText(ticket_confirmation_boarding_time_txt);
        ticket_confirmation_boarding_point_name_tv.setText(ticket_confirmation_boarding_point_name_txt);
        ticket_confirmation_company_name_tv.setText(ticket_confirmation_company_name_txt);
        ticket_confirmation_bus_label_tv.setText(ticket_confirmation_bus_label_txt);
        ticket_confirmation_seat_no_tv.setText(ticket_confirmation_seat_no_txt);
        ticket_confirmation_passenger_name_tv.setText(ticket_confirmation_passenger_name_txt);
        ticket_confirmation_boarding_point_address_tv.setText(ticket_confirmation_boarding_point_address_txt);
        ticket_confirmation_boarding_point_landmark_tv.setText(ticket_confirmation_boarding_point_landmark_txt);
        ticket_confirmation_boarding_point_mobile_tv.setText(ticket_confirmation_boarding_point_mobile_txt);
        ticket_confirmation_total_fare.setText(ticket_confirmation_total_fare_txt);

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

    public String change_date_form(String date)
    {
        date.replace("T"," ");
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

        String final_date = day_of_weak+", "+day+" "+months[month_int-1]+" "+year;


        return final_date;
    }
}
