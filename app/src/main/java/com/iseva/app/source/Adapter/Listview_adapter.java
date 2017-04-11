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
import com.iseva.app.source.travel.Activity_Select_Seats;

import java.util.ArrayList;
import java.util.HashMap;

import static com.iseva.app.source.R.id.bus_label;

/**
 * Created by xb_sushil on 4/1/2017.
 */
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



        holder.company_name_tv = (TextView)rowView.findViewById(R.id.company_name);
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
        if(Float.parseFloat(fare.substring(2,fare.length())) > Float.parseFloat(offer_fare.substring(2,offer_fare.length())))
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
        rowView.setTag(R.string.schedule_id,routes_hashmap.get(i).get("schedule_id"));


        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context,Activity_Select_Seats.class);


                i.putExtra("schedule_id",view.getTag(R.string.schedule_id).toString());
                Bundle bundle = ActivityOptions.makeCustomAnimation(context, R.anim.anim_in, R.anim.anim_none).toBundle();
                context.startActivity(i,bundle);
            }
        });

        return rowView;
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
