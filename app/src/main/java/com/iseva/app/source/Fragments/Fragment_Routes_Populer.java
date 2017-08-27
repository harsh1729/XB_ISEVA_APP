package com.iseva.app.source.Fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.iseva.app.source.Adapter.Listview_adapter;
import com.iseva.app.source.Globals;
import com.iseva.app.source.R;
import com.iseva.app.source.Realm_objets.Bus_routes_detail;
import com.iseva.app.source.travel.Activity_Bus_Routes;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.RealmQuery;

/**
 * Created by xb_sushil on 1/25/2017.
 */

public class Fragment_Routes_Populer extends Fragment_Parent {

    LinearLayout routes_loader;
    ArrayList<HashMap<String, String>> routes_hashmap;
    int all_routes_count = 0;
    ListView all_routes_list;
    LinearLayout listview_layout;
    LinearLayout message_layout;

    TextView tv;


    String[] from = new String[] {"company_name","fare","bus_label","time","Availabel_Seats","duration"};
    int[] to   = new int[] {R.id.company_name,R.id.fare,R.id.bus_label,R.id.time_view,R.id.available_seat,R.id.duration};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_routes_populer, container, false);

        routes_loader = (LinearLayout)view.findViewById(R.id.routes_loader);
        listview_layout = (LinearLayout)view.findViewById(R.id.list_view_layout);
        message_layout = (LinearLayout)view.findViewById(R.id.message_layout);

        all_routes_list = (ListView)view.findViewById(R.id.list_all_routes_popular);
        tv = (TextView)view.findViewById(R.id.response);
        tv.setText( Globals.getStringFromResources(getActivity(),R.string.no_buses_found));

        if(Activity_Bus_Routes.fisttime_flag)
        {
            Activity_Bus_Routes.fisttime_flag = false;
            Loading l = new Loading();
            l.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

        return view;

    }


     private class Loading extends AsyncTask<Void, Void, Void> {



        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if(all_routes_count > 0)
            {
               // SimpleAdapter adapter = new SimpleAdapter(getActivity(),routes_hashmap,R.layout.routes_single_row,from,to);
                Listview_adapter adapter = new Listview_adapter(getActivity(),routes_hashmap);
                all_routes_list.setAdapter(adapter);
                listview_layout.setVisibility(View.VISIBLE);
                routes_loader.setVisibility(View.GONE);
                message_layout.setVisibility(View.GONE);
            }
            else
            {
                routes_loader.setVisibility(View.GONE);
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
        all_routes_count = 0;
        if(!rout.isEmpty())
        {

            for (int i=0;i <rout.size();i++)
            {
                HashMap<String, String> single_map = new HashMap<String, String>();
                single_map.put("schedule_id",Integer.toString(rout.get(i).getRouteBusId()));
                single_map.put("company_id",Integer.toString(rout.get(i).getCompanyId()));
                single_map.put("company_name",rout.get(i).getCompanyName());
                single_map.put("fare",getResources().getString(R.string.Rs)+" "+ Double.toString(rout.get(i).getFare()));
                single_map.put("fare_offer",getResources().getString(R.string.Rs)+" "+Double.toString(rout.get(i).getFare_after_offer()));
                single_map.put("bus_label",rout.get(i).getBusLabel());
                single_map.put("time",rout.get(i).getDepTime()+" - "+rout.get(i).getArrTime());
                single_map.put("Availabel_Seats",""+rout.get(i).getAvailableSeats());
                single_map.put("duration",rout.get(i).getDuration());


                routes_hashmap.add(single_map);
            }

            all_routes_count = rout.size();


        }


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



       /* all_routes_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                HashMap<String, String> hm = (HashMap<String, String>) parent.getAdapter().getItem(position);


                String scheduleid = hm.get("schedule_id");
                Intent i = new Intent(getActivity(),Activity_Select_Seats.class);
                i.putExtra("schedule_id",scheduleid);
                Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.anim_in, R.anim.anim_none).toBundle();
                startActivity(i,bundle);



            }
        });*/
    }


    public void makequery() {

        RealmQuery<Bus_routes_detail> realmQuery = super.getQueryWithFilters();


        My_realm.beginTransaction();
        rout = realmQuery.findAll();
        My_realm.commitTransaction();

    }

    public void showloader()
    {
        listview_layout.setVisibility(View.GONE);
        message_layout.setVisibility(View.GONE);
        routes_loader.setVisibility(View.VISIBLE);
    }

    public void set_list_item()
    {
        listview_layout.setVisibility(View.GONE);
        message_layout.setVisibility(View.GONE);


        Loading l = new Loading();
        l.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);





    }
}
