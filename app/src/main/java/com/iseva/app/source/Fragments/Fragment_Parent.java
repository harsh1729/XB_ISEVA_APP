package com.iseva.app.source.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.iseva.app.source.Realm_objets.Bus_routes_detail;
import com.iseva.app.source.Realm_objets.Filter;
import com.iseva.app.source.travel.Activity_Bus_Routes;
import com.iseva.app.source.travel.Constants.BUS_TYPE;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class Fragment_Parent extends Fragment {

    Realm My_realm;
    RealmResults<Bus_routes_detail> rout;


    public Fragment_Parent() {

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
                            realmQuery.or().contains("Make", BUS_TYPE.MAKE_VOLVO_ISHIFT);

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



}
