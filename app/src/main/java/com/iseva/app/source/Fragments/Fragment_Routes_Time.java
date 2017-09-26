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

import io.realm.RealmQuery;
import io.realm.Sort;

/**
 * Created by xb_sushil on 1/25/2017.
 */

public class Fragment_Routes_Time extends Fragment_Routes_Parent {

    LinearLayout routes_loader_time;


    ListView all_routes_list;
    LinearLayout listview_layout;
    LinearLayout message_layout;

    TextView tv;
   /* String[] from = new String[] {"company_name","fare","bus_label","time","Availabel_Seats","duration"};
    int[] to = new int[] {R.id.company_name,R.id.fare,R.id.bus_label,R.id.time_view,R.id.available_seat,R.id.duration};*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_routes_time, container, false);

        routes_loader_time =(LinearLayout)view.findViewById(R.id.routes_loader_time) ;
        listview_layout = (LinearLayout)view.findViewById(R.id.list_view_layout);
        message_layout = (LinearLayout)view.findViewById(R.id.message_layout);

        all_routes_list = (ListView)view.findViewById(R.id.list_all_routes_time);
        tv = (TextView)view.findViewById(R.id.response);
        tv.setText( Globals.getStringFromResources(getActivity(),R.string.no_buses_found));


        return view;
    }


    private class Loading extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if(all_routes_count > 0)
            {
                //SimpleAdapter adapter = new SimpleAdapter(getActivity(),routes_hashmap,R.layout.routes_single_row,from,to);
                Listview_adapter adapter = new Listview_adapter(getActivity(),routes_hashmap);
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
        setRoutesHashMap();

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }



public void makequery() {

    RealmQuery<Bus_routes_detail> realmQuery = super.getQueryWithFilters();


    if (((Activity_Bus_Routes) getActivity()).time_flag == 0) {
        My_realm.beginTransaction();
        rout = realmQuery.findAllSorted("DepTimeVal", Sort.ASCENDING);
        My_realm.commitTransaction();
    } else {
        My_realm.beginTransaction();
        rout = realmQuery.findAllSorted("DepTimeVal", Sort.DESCENDING);
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

    }
}
