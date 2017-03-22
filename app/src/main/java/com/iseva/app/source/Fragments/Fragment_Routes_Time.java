package com.iseva.app.source.Fragments;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import com.iseva.app.source.R;
import com.iseva.app.source.Realm_objets.Bus_routes_detail;
import com.iseva.app.source.Realm_objets.Filter;
import com.iseva.app.source.travel.Activity_Bus_Routes;
import com.iseva.app.source.travel.Activity_Select_Seats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by xb_sushil on 1/25/2017.
 */

public class Fragment_Routes_Time extends Fragment {

    LinearLayout routes_loader_time;
    int all_routes_count= 0;
    List<HashMap<String, String>> routes_hashmap;

    ListView all_routes_list;
    LinearLayout listview_layout;
    LinearLayout message_layout;

    Realm My_realm;
    TextView tv;
    String[] from = new String[] {"company_name","fare","bus_label","time","Availabel_Seats","duration"};
    int[] to = new int[] {R.id.company_name,R.id.fare,R.id.bus_label,R.id.time_view,R.id.available_seat,R.id.duration};
    RealmResults<Bus_routes_detail> rout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_routes_time, container, false);

        routes_loader_time =(LinearLayout)view.findViewById(R.id.routes_loader_time) ;
        listview_layout = (LinearLayout)view.findViewById(R.id.list_view_layout);
        message_layout = (LinearLayout)view.findViewById(R.id.message_layout);

        all_routes_list = (ListView)view.findViewById(R.id.list_all_routes_time);
        tv = (TextView)view.findViewById(R.id.response);
        tv.setText("No Buses For These Cities");

     /*   if(Activity_Bus_Routes.fg_time_visible)
        {
            Loading l = new Loading();
            l.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
*/

        return view;
    }


    private class Loading extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if(all_routes_count > 0)
            {
                SimpleAdapter adapter = new SimpleAdapter(getActivity(),routes_hashmap,R.layout.routes_single_row,from,to);
                all_routes_list.setAdapter(adapter);
                listview_layout.setVisibility(View.VISIBLE);
                routes_loader_time.setVisibility(View.GONE);
                message_layout.setVisibility(View.GONE);
            }
            else
            {
                routes_loader_time.setVisibility(View.GONE);
                message_layout.setVisibility(View.VISIBLE);
            }




        }

        @Override
        protected Void doInBackground(Void... arg0) {

            loading();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    public void loading()
    {

        makequery();
        routes_hashmap = new ArrayList<HashMap<String, String>>();
        if(!rout.isEmpty())
        {

            for (int i=0;i <rout.size();i++)
            {
                HashMap<String, String> single_map = new HashMap<String, String>();
                single_map.put("schedule_id",Integer.toString(rout.get(i).getRouteScheduleId()));
                single_map.put("company_id",Integer.toString(rout.get(i).getCompanyId()));
                single_map.put("company_name",rout.get(i).getCompanyName());
                single_map.put("fare", getResources().getString(R.string.Rs)+" "+Float.toString(rout.get(i).getFare()));
                single_map.put("bus_label",rout.get(i).getBusLabel());
                single_map.put("time",rout.get(i).getDeparturetime()+"-"+rout.get(i).getArrivaltime());
                single_map.put("Availabel_Seats",rout.get(i).getAvailableSeats()+" "+"Seats");
                single_map.put("duration",rout.get(i).getDuration());

                routes_hashmap.add(single_map);

            }

            all_routes_count = rout.size();
        }



    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        all_routes_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                HashMap<String, String> hm = (HashMap<String, String>) parent.getAdapter().getItem(position);

                String scheduleid = hm.get("schedule_id");
                Intent i = new Intent(getActivity(),Activity_Select_Seats.class);
                i.putExtra("schedule_id",scheduleid);
                Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.anim_in, R.anim.anim_none).toBundle();
                startActivity(i,bundle);

            }
        });
    }

    public void makequery()
    {
        My_realm = Realm.getInstance(getActivity());

        My_realm.beginTransaction();
        RealmResults<Filter> all_row = My_realm.where(Filter.class).findAll();
        My_realm.commitTransaction();

        RealmQuery<Bus_routes_detail> realmQuery =  My_realm.where(Bus_routes_detail.class);
        Boolean query_and_flaga1 = false;
        Boolean query_and_flaga2 = false;
        for(int i =0;i<all_row.size();i++)
        {
            if(all_row.get(i).getFilter_tag().equals("Bustype"))
            {
                if(query_and_flaga1)
                {
                    if(all_row.get(i).getFilter_name().equals("A/C"))
                    {
                        realmQuery.or().equalTo("HasAC",true);
                    }
                    else if(all_row.get(i).getFilter_name().equals("Sleeper"))
                    {
                        realmQuery.or().equalTo("HasSleeper",true);
                    }
                    else
                    {
                        realmQuery.or().contains("BusLabel","Multi");
                    }
                }
                else
                {
                    query_and_flaga1 = true;
                    if(all_row.get(i).getFilter_name().equals("A/C"))
                    {
                        realmQuery.equalTo("HasAC",true);
                    }
                    else if(all_row.get(i).getFilter_name().equals("Sleeper"))
                    {
                        realmQuery.equalTo("HasSleeper",true);
                    }
                    else
                    {
                        realmQuery.contains("BusLabel","Multi");
                    }
                }

            }
            else if(all_row.get(i).getFilter_tag().equals("Busbrand"))
            {
                if(query_and_flaga2)
                {
                    if(all_row.get(i).getFilter_name().equals("Volvo"))
                    {
                        realmQuery.or().equalTo("IsVolvo",true);
                        realmQuery.or().contains("BusLabel","Volvo");
                    }
                    else if(all_row.get(i).getFilter_name().equals("Mercesdes"))
                    {
                        realmQuery.or().contains("BusLabel","Mercesdes");
                    }
                    else
                    {
                        realmQuery.or().contains("BusLabel","Scania");
                    }
                }
                else
                {
                    query_and_flaga2 = true;
                    if(all_row.get(i).getFilter_name().equals("Volvo"))
                    {
                        realmQuery.equalTo("IsVolvo",true);
                    }
                    else if(all_row.get(i).getFilter_name().equals("Mercesdes"))
                    {
                        realmQuery.contains("BusLabel","Mercesdes");
                    }
                    else
                    {
                        realmQuery.contains("BusLabel","Scania");
                    }
                }
            }
            else
            {
                realmQuery.between("Fare",(float) Activity_Bus_Routes.filter_min_price,(float) Activity_Bus_Routes.filter_max_price);
            }


        }
        realmQuery.greaterThan("AvailableSeats",0);
        realmQuery.greaterThan("Fare",(float)0);
        if(((Activity_Bus_Routes) getActivity()).time_flag == 0)
        {
            My_realm.beginTransaction();
            rout =  realmQuery.findAllSorted("DepTime",Sort.ASCENDING);
            My_realm.commitTransaction();
        }
        else
        {
            My_realm.beginTransaction();
            rout = realmQuery.findAllSorted("DepTime",Sort.DESCENDING);
            My_realm.commitTransaction();
        }


    }

    public void showloader()
    {
        listview_layout.setVisibility(View.GONE);
        message_layout.setVisibility(View.GONE);
        routes_loader_time.setVisibility(View.VISIBLE);
    }

    public void set_list_item()
    {
        listview_layout.setVisibility(View.GONE);
        message_layout.setVisibility(View.GONE);

        Loading l = new Loading();
        l.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
/*
        makequery();

        List<HashMap<String, String>> routes_hashmap = new ArrayList<HashMap<String, String>>();
        if(!rout.isEmpty())
        {
            listview_layout.setVisibility(View.VISIBLE);
            for (int i=0;i <rout.size();i++)
            {
                HashMap<String, String> single_map = new HashMap<String, String>();
                single_map.put("schedule_id",Integer.toString(rout.get(i).getRouteScheduleId()));
                single_map.put("company_id",Integer.toString(rout.get(i).getCompanyId()));
                single_map.put("company_name",rout.get(i).getCompanyName());
                single_map.put("fare", Float.toString(rout.get(i).getFare()));
                single_map.put("bus_label",rout.get(i).getBusLabel());
                single_map.put("time",rout.get(i).getDeparturetime()+"-"+rout.get(i).getArrivaltime());
                single_map.put("Availabel_Seats",rout.get(i).getAvailableSeats()+" "+"Seats");
                single_map.put("duration",rout.get(i).getDuration());

                routes_hashmap.add(single_map);

            }

            SimpleAdapter adapter = new SimpleAdapter(getActivity(),routes_hashmap,R.layout.routes_single_row,from,to);
            all_routes_list.setAdapter(adapter);
        }
        else
        {
            message_layout.setVisibility(View.VISIBLE);
        }*/





    }
}
