package com.iseva.app.source.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.iseva.app.source.R;
import com.iseva.app.source.Realm_objets.Bus_routes_detail;
import com.iseva.app.source.Realm_objets.Filter;
import com.iseva.app.source.travel.Activity_Bus_Routes;
import com.iseva.app.source.travel.Constants.BUS_TYPE;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class Fragment_Routes_Parent extends Fragment {

    Realm My_realm;
    RealmResults<Bus_routes_detail> rout;
    int all_routes_count= 0;
    ArrayList<HashMap<String, String>> routes_hashmap;

    public Fragment_Routes_Parent() {

    }
        // Required empty public constructor


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    public RealmQuery<Bus_routes_detail>  getQueryWithFilters()
    {
        My_realm = Realm.getDefaultInstance();

        My_realm.beginTransaction();
        RealmResults<Filter> all_row = My_realm.where(Filter.class).findAll();
        My_realm.commitTransaction();

        RealmQuery<Bus_routes_detail> realmQuery =  My_realm.where(Bus_routes_detail.class);

       //boolean isBusTypeSetOnce = false;
        //boolean isBusBrandSetOnce = false; // used for OR, not needed now as we need to use AND

        for(int i =0;i<all_row.size();i++)
        {
            if(all_row.get(i).getFilter_tag().equals("Bustype"))
            {


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
                            realmQuery.contains("Axel","Multi");

                    }


            }
            else if(all_row.get(i).getFilter_tag().equals("Busbrand"))
            {
                    if(all_row.get(i).getFilter_name().equals("Volvo"))
                    {

                            realmQuery.contains("Make", BUS_TYPE.MAKE_VOLVO);
                            //realmQuery.or().contains("Make", BUS_TYPE.MAKE_VOLVO_ISHIFT);

                    }
                    else if(all_row.get(i).getFilter_name().equals("Mercesdes"))
                    {

                            realmQuery.contains("Make", BUS_TYPE.MAKE_MERCEDES);

                    }
                    else {

                            realmQuery.contains("Make", BUS_TYPE.MAKE_SCANIA);


                    }


            }
            else
            {
                realmQuery.between("Fare_after_offer",(double) Activity_Bus_Routes.filter_min_price,(double) Activity_Bus_Routes.filter_max_price);
            }


        }
        realmQuery.greaterThan("AvailableSeats",0);
        realmQuery.greaterThan("Fare",(double)0);


        return realmQuery;



    }


    protected  void setRoutesHashMap(){

        routes_hashmap = new ArrayList<HashMap<String, String>>();
        all_routes_count = 0;
        if(!rout.isEmpty())
        {

            for (int i=0;i <rout.size();i++)
            {
                HashMap<String, String> single_map = new HashMap<String, String>();
                single_map.put("bus_id",Integer.toString(rout.get(i).getRouteBusId()));
                single_map.put("company_id",Integer.toString(rout.get(i).getCompanyId()));
                single_map.put("company_name",rout.get(i).getCompanyName());
                single_map.put("fare", getResources().getString(R.string.Rs)+" "+rout.get(i).getFare());
                single_map.put("fare_offer",getResources().getString(R.string.Rs)+" "+ rout.get(i).getFare_after_offer());
                single_map.put("bus_label",rout.get(i).getBusLabel());
                single_map.put("time",rout.get(i).getDepTime()+" - "+rout.get(i).getArrTime());
                single_map.put("Availabel_Seats",""+rout.get(i).getAvailableSeats());
                single_map.put("duration",rout.get(i).getDuration());
                single_map.put("timeDep",rout.get(i).getDepTime());
                single_map.put("timeArr",rout.get(i).getArrTime());
                single_map.put("dateTimeDep",rout.get(i).getDepDateTime());
                single_map.put("dateTimeArr",rout.get(i).getArrDateTime());
                single_map.put("isAC",String.valueOf(rout.get(i).getHasAC()));
                single_map.put("commPCT",Double.toString(rout.get(i).getCommPCT()));

                routes_hashmap.add(single_map);

            }

            all_routes_count = rout.size();
        }
    }




}
