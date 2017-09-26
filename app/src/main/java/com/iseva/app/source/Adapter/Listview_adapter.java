package com.iseva.app.source.Adapter;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iseva.app.source.R;
import com.iseva.app.source.Realm_objets.Realm_Selected_Bus_Details;
import com.iseva.app.source.travel.Activity_Select_Seats;
import com.iseva.app.source.travel.Global_Travel.TRAVEL_DATA;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.Realm;

import static com.iseva.app.source.R.id.bus_label;
import static com.iseva.app.source.R.id.company_name;


public class Listview_adapter extends BaseAdapter{



    Context context;
    ArrayList<HashMap<String, String>> routes_hashmap;
    private static LayoutInflater inflater=null;

    public Listview_adapter(Context mcontext, ArrayList<HashMap<String, String>> list) {



        // TODO Auto-generated constructor stub
        Log.e("vikas","constructor call");
        this.routes_hashmap = list;
        this.context = mcontext;
       this.inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
       return routes_hashmap.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = view;
        Log.e("vikas ","getview call"+i);
        Holder holder = new Holder();
        if(rowView == null)
        {
            rowView = inflater.inflate(R.layout.routes_single_row,null);

        }



        holder.company_name_tv = (TextView)rowView.findViewById(company_name);
        holder.fare_tv = (TextView)rowView.findViewById(R.id.fare);
        holder.offer_fare_tv = (TextView)rowView.findViewById(R.id.offer_fare);
        holder.bus_label_tv = (TextView)rowView.findViewById(bus_label);
        holder.time_view_tv = (TextView)rowView.findViewById(R.id.time_view);
        holder.available_seat_tv = (TextView)rowView.findViewById(R.id.available_seat);
        holder.duration_tv = (TextView)rowView.findViewById(R.id.duration);
        holder.company_name_tv.setText(routes_hashmap.get(i).get("company_name"));
        holder.fare_tv.setText(routes_hashmap.get(i).get("fare"));
        String fare = routes_hashmap.get(i).get("fare");
        String offer_fare = routes_hashmap.get(i).get("fare_offer");
        if(Float.parseFloat(fare.substring(2,fare.length())) >= Float.parseFloat(offer_fare.substring(2,offer_fare.length())))
        {
            holder.fare_tv.setTextSize(12);
            holder.fare_tv.setPaintFlags(holder.fare_tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            holder.offer_fare_tv.setText(routes_hashmap.get(i).get("fare_offer"));

        }
        else
        {
            holder.offer_fare_tv.setVisibility(View.GONE);
        }
        int availabel_seat= Integer.parseInt(routes_hashmap.get(i).get("Availabel_Seats"));
        if(availabel_seat <= 5)
        {
            holder.available_seat_tv.setTextColor(ContextCompat.getColor(context,R.color.red_color));
        }
        else
        {
            holder.available_seat_tv.setTextColor(ContextCompat.getColor(context,R.color.routes_screen_extra_light_text_color));
        }
        holder.bus_label_tv.setText(routes_hashmap.get(i).get("bus_label"));
        holder.time_view_tv.setText(routes_hashmap.get(i).get("time"));
        holder.available_seat_tv.setText(routes_hashmap.get(i).get("Availabel_Seats")+" "+"Seats");
        holder.duration_tv.setText(routes_hashmap.get(i).get("duration"));


        rowView.setTag(R.string.bus_id,routes_hashmap.get(i).get("bus_id"));
        rowView.setTag(R.string.bus_label,routes_hashmap.get(i).get("bus_label"));
        rowView.setTag(R.string.company_name,routes_hashmap.get(i).get("company_name"));

        rowView.setTag(R.string.arr_date_time,routes_hashmap.get(i).get("dateTimeArr"));
        rowView.setTag(R.string.arr_time,routes_hashmap.get(i).get("timeArr"));
        rowView.setTag(R.string.dep_time,routes_hashmap.get(i).get("timeDep"));
        rowView.setTag(R.string.dep_date_time,routes_hashmap.get(i).get("dateTimeDep"));
        rowView.setTag(R.string.commition_percentage,routes_hashmap.get(i).get("commPCT"));
        rowView.setTag(R.string.is_ac,routes_hashmap.get(i).get("isAC"));


        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String bus_id_str = view.getTag(R.string.bus_id).toString();

                String comm_pct_str =  view.getTag(R.string.commition_percentage).toString();

                int bus_id = 0;
                double comm_pct = 0;

                try {
                    bus_id = Integer.parseInt(bus_id_str);
                    comm_pct = Double.parseDouble(comm_pct_str);

                }catch (NumberFormatException ex){

                }

                String arr_date_time = view.getTag(R.string.arr_date_time).toString();
                String arr_time = view.getTag(R.string.arr_time).toString();
                String bus_label = view.getTag(R.string.bus_label).toString();
                String company_name = view.getTag(R.string.company_name).toString();
                String dep_date_time = view.getTag(R.string.dep_date_time).toString();
                String dep_time = view.getTag(R.string.dep_time).toString();
                String is_ac = view.getTag(R.string.is_ac).toString();

                addRealmBookingDetails(bus_id,bus_label,company_name,arr_time,arr_date_time ,dep_time,dep_date_time);

                Intent i = new Intent(context,Activity_Select_Seats.class);

                TRAVEL_DATA.IS_AC_STR = is_ac;


                i.putExtra("bus_id",bus_id_str);
                i.putExtra("comm_pct",comm_pct_str);

                Bundle bundle = ActivityOptions.makeCustomAnimation(context, R.anim.anim_in, R.anim.anim_none).toBundle();
                context.startActivity(i,bundle);
            }
        });

        return rowView;
    }


    private void addRealmBookingDetails(int bus_id , String bus_label, String company_name, String arr_time , String arr_date_time,String dep_time , String dep_date_time){

        Realm my_realm = Realm.getDefaultInstance();

        my_realm.beginTransaction();

        Realm_Selected_Bus_Details objSelectedBusDetails =  my_realm.createObject(Realm_Selected_Bus_Details.class);

        objSelectedBusDetails.setBusId(bus_id);
        objSelectedBusDetails.setBusLabel(bus_label);
        objSelectedBusDetails.setCompanyName(company_name);
        objSelectedBusDetails.setArrTime(arr_time);
        objSelectedBusDetails.setArrDateTime(arr_date_time);
        objSelectedBusDetails.setDepDateTime(dep_date_time);
        objSelectedBusDetails.setDepTime(dep_time);

        my_realm.commitTransaction();
    }

    public class Holder
    {
        TextView company_name_tv;
        TextView fare_tv ;
        TextView offer_fare_tv;
        TextView bus_label_tv;
        TextView time_view_tv;
        TextView available_seat_tv;
        TextView duration_tv;
    }
}
