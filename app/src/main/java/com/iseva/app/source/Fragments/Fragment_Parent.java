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

        boolean isBusTypeSetOnce = false;
        boolean isBusBrandSetOnce = false;

        for(int i =0;i<all_row.size();i++)
        {
            if(all_row.get(i).getFilter_tag().equals("Bustype"))
            {


                    if(all_row.get(i).getFilter_name().equals("A/C"))
                    {
                        if(isBusTypeSetOnce){

                            realmQuery.or().equalTo("HasAC",true);
                        }else{

                            realmQuery.equalTo("HasAC",true);
                        }

                    }
                    else if(all_row.get(i).getFilter_name().equals("Sleeper"))
                    {
                        if(isBusTypeSetOnce){

                            realmQuery.or().equalTo("HasSleeper",true);

                        }else{

                            realmQuery.equalTo("HasSleeper",true);
                        }


                    }
                    else
                    {
                        if(isBusTypeSetOnce){

                            realmQuery.or().contains("Axel","Multi");

                        }else {

                            realmQuery.contains("Axel","Multi");
                        }


                    }


                isBusTypeSetOnce = true;

            }
            else if(all_row.get(i).getFilter_tag().equals("Busbrand"))
            {
                    if(all_row.get(i).getFilter_name().equals("Volvo"))
                    {
                        if (isBusBrandSetOnce){

                            realmQuery.or().contains("Make", BUS_TYPE.MAKE_VOLVO);
                            realmQuery.or().contains("Make", BUS_TYPE.MAKE_VOLVO_ISHIFT);

                        }else{

                            realmQuery.contains("Make", BUS_TYPE.MAKE_VOLVO);
                            realmQuery.or().contains("Make", BUS_TYPE.MAKE_VOLVO_ISHIFT);
                        }

                    }
                    else if(all_row.get(i).getFilter_name().equals("Mercesdes"))
                    {
                        if (isBusBrandSetOnce){

                            realmQuery.or().contains("Make", BUS_TYPE.MAKE_MERCEDES);
                        }else{

                            realmQuery.contains("Make", BUS_TYPE.MAKE_MERCEDES);
                        }

                    }
                    else {
                        if (isBusBrandSetOnce){

                            realmQuery.or().contains("Make", BUS_TYPE.MAKE_SCANIA);
                        }else {

                            realmQuery.contains("Make", BUS_TYPE.MAKE_SCANIA);
                        }


                    }


                isBusBrandSetOnce = true;
            }
            else
            {
                realmQuery.between("Fare",(double) Activity_Bus_Routes.filter_min_price,(double) Activity_Bus_Routes.filter_max_price);
            }


        }
        realmQuery.greaterThan("AvailableSeats",0);
        realmQuery.greaterThan("Fare",(double)0);


        return realmQuery;



    }



}
