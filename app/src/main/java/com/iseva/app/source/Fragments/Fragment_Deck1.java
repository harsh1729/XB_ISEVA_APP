package com.iseva.app.source.Fragments;


import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.iseva.app.source.R;
import com.iseva.app.source.Realm_objets.Realm_Seat_Details;
import com.iseva.app.source.Realm_objets.Realm_Selected_Seats;
import com.iseva.app.source.travel.Activity_Select_Seats;
import com.iseva.app.source.travel.Constants.SEAT_DETAILS;

import io.realm.Realm;
import io.realm.RealmResults;

public class Fragment_Deck1 extends Fragment_Parent_Deck {



    int Total_screen_width;
    double Seat_height_percentage = 110;
    double Seat_width_percentage = 11;
    double fix_left_per = 5.50;
    double fix_right_per = 5.50;

    int fix_margin_left = 0;
    int fix_margin_right =0;
    public int max_col = 0;
    int Seat_height;
    int Seat_width;
    int all_col;
    int margin;

    double margin_top_seat_per = 4.50;
    int margin_top_seat = 0;
    int fixtopmargin = 70;
    int extramargin_top =0;

    //ArrayList<Integer> colm_numbers;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment__deck1, container, false);
        My_realm = Realm.getDefaultInstance();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);


        //colm_numbers = new ArrayList<Integer>();
        RealmResults<Realm_Seat_Details> all_seats = My_realm.where(Realm_Seat_Details.class).equalTo("Deck",1).notEqualTo("SeatNo",".DOOR").findAll();

            if(all_seats.size() == 0)
            {
                showAlertDialog(getResources().getString(R.string.validating_error_title),"There are no seats please choose another bus","Ok");
            }




        layout = (FrameLayout)view.findViewById(R.id.layout_deck1);
        Total_screen_width = displaymetrics.widthPixels;

        fix_margin_left = (int)(Total_screen_width * fix_left_per)/100;
        fix_margin_right = (int)(Total_screen_width * fix_right_per)/100;

        margin_top_seat = (int)(Total_screen_width * margin_top_seat_per)/100;

        Seat_width = (int)(Total_screen_width * Seat_width_percentage)/100;
        Seat_height = (int)(Seat_width * Seat_height_percentage)/100;
        margin = (int)(Total_screen_width-((Seat_width *max_col)+(fix_margin_left+fix_margin_right)))/(max_col-1);

        fixtopmargin = Seat_height + fix_margin_left;



        FrameLayout.LayoutParams lp  = new FrameLayout.LayoutParams(Seat_height,Seat_height);
        lp.leftMargin = ((Seat_width+margin)*(max_col-1)+fix_margin_left);

        TextView stearing = new TextView(getActivity());
        stearing.setLayoutParams(lp);
        stearing.setBackgroundResource(R.drawable.driver_icon);
        layout.addView(stearing);


        for(int i=0;i < all_seats.size(); i++) {


                    int row = all_seats.get(i).getRow();
                    int col = all_seats.get(i).getCol();

                    final int height = all_seats.get(i).getHeight();
                    final int width = all_seats.get(i).getWidth();

                    if(height == 2 && width == 1)
                    {
                        int realseatheight = (Seat_height*height*85)/100;
                        extramargin_top = ((Seat_height*2)+margin_top_seat - realseatheight)/2;
                        params = new FrameLayout.LayoutParams(Seat_width,realseatheight);
                        params.topMargin = ((margin_top_seat+Seat_height)*(row))+fixtopmargin+extramargin_top;
                    }
                    else if(height == 1 && width == 2)
                    {
                        params = new FrameLayout.LayoutParams(Seat_width*width,Seat_height*height);
                        params.topMargin = ((margin_top_seat+Seat_height)*(row))+fixtopmargin;
                    }
                    else
                    {
                        params = new FrameLayout.LayoutParams(Seat_width,(Seat_height*height));
                        params.topMargin = ((margin_top_seat+Seat_height)*(row))+fixtopmargin;
                    }

                    params.leftMargin = ((Seat_width+margin)*(col)+fix_margin_left);


                    Seat_view = new TextView(getActivity());
                    Seat_view.setTextSize(10);
                    Seat_view.setLayoutParams(params);
                    Seat_view.setGravity(Gravity.CENTER);
                    Seat_view.setText(all_seats.get(i).getSeatNo());
                    Seat_view.setTag(R.string.SeatNo,all_seats.get(i).getSeatNo());
                    Seat_view.setTag(R.string.IsSelected,"false");
                    Seat_view.setTag(R.string.Deck,all_seats.get(i).getDeck());
                    Seat_view.setTag(R.string.Gender,all_seats.get(i).getGender());
                    Seat_view.setTag(R.string.Fare,all_seats.get(i).getFare());
                    Seat_view.setTag(R.string.Offer_Fare,all_seats.get(i).getFare_after_offer());//
                    Seat_view.setTag(R.string.Seat_Type,all_seats.get(i).getSeatType());

                    Seat_view.setTag(R.string.IsSleeper,all_seats.get(i).getIsSleeper());


                    if(all_seats.get(i).getIsAvailable() && (all_seats.get(i).getGender() == SEAT_DETAILS.VALUE_GENDER_ALL || all_seats.get(i).getGender() == SEAT_DETAILS.VALUE_GENDER_MALE) )
                    {
                        if(height == 2 && width == 1)
                        {
                            Seat_view.setBackgroundResource(R.drawable.seat_layout_available_sleeper_seat_port);
                        }
                        else if(height == 1 && width == 2)
                        {
                            Seat_view.setBackgroundResource(R.drawable.seat_layout_available_sleeper_seat_land);
                        }
                        else
                        {
                            Seat_view.setBackgroundResource(R.drawable.seat_layout_available_seat_port);
                        }

                        Seat_view.setOnClickListener(new View.OnClickListener(){

                            @Override
                            public void onClick(View v) {


                                if(v.getTag(R.string.IsSelected).equals("false"))
                                {
                                    selectSeat(v,height,width);

                                }
                                else
                                {

                                    v.setTag(R.string.IsSelected,"false");
                                    if(height == 2 && width ==1)
                                    {
                                        v.setBackgroundResource(R.drawable.seat_layout_available_sleeper_seat_port);
                                    }
                                    else if(height == 1 && width == 2)
                                    {
                                        v.setBackgroundResource(R.drawable.seat_layout_available_sleeper_seat_land);
                                    }
                                    else
                                    {
                                        v.setBackgroundResource(R.drawable.seat_layout_available_seat_port);
                                    }

                                    My_realm.beginTransaction();
                                    RealmResults<Realm_Selected_Seats> single_row = My_realm.where(Realm_Selected_Seats.class).equalTo("SeatNo",v.getTag(R.string.SeatNo).toString()).findAll();
                                    single_row.deleteAllFromRealm();
                                    My_realm.commitTransaction();

                                    ((Activity_Select_Seats) getActivity()).update_seat_fare();
                                }



                            }
                        });


                        layout.addView(Seat_view);
                    }
                    else if(!all_seats.get(i).getIsAvailable() && (all_seats.get(i).getGender() == SEAT_DETAILS.VALUE_GENDER_ALL || all_seats.get(i).getGender() == SEAT_DETAILS.VALUE_GENDER_MALE))
                    {

                        if(height == 2 && width == 1)
                        {
                            Seat_view.setBackgroundResource(R.drawable.seat_layout_booked_sleeper_seat_port);
                        }
                        else if(height == 1 && width == 2)
                        {
                            Seat_view.setBackgroundResource(R.drawable.seat_layout_booked_sleeper_seat_land);
                        }
                        else
                        {
                            Seat_view.setBackgroundResource(R.drawable.seat_layout_booked_seat_port);
                        }


                        layout.addView(Seat_view);
                    }
                    else if(all_seats.get(i).getIsAvailable() && all_seats.get(i).getGender() == SEAT_DETAILS.VALUE_GENDER_FEMALE)
                    {
                        if(height == 2 && width == 1)
                        {
                            Seat_view.setBackgroundResource(R.drawable.seat_layout_ladies_sleeper_seat_port);
                        }
                        else if(height == 1 && width == 2)
                        {
                            Seat_view.setBackgroundResource(R.drawable.seat_layout_ladies_sleeper_seat_land);
                        }
                        else

                        {
                            Seat_view.setBackgroundResource(R.drawable.seat_layout_ladies_seat_port);
                        }

                        Seat_view.setOnClickListener(new View.OnClickListener(){

                            @Override
                            public void onClick(View v) {

                                if(v.getTag(R.string.IsSelected).equals("false"))
                                {

                                    selectSeat(v,height,width);

                                }
                                else
                                {
                                    v.setTag(R.string.IsSelected,"false");
                                    if(height == 2 && width ==1)
                                    {
                                        v.setBackgroundResource(R.drawable.seat_layout_ladies_sleeper_seat_port);
                                    }
                                    else if(height == 1 && width == 2)
                                    {
                                        v.setBackgroundResource(R.drawable.seat_layout_ladies_sleeper_seat_land);
                                    }
                                    else
                                    {
                                        v.setBackgroundResource(R.drawable.seat_layout_ladies_seat_port);
                                    }

                                    My_realm.beginTransaction();
                                    RealmResults<Realm_Selected_Seats> single_row = My_realm.where(Realm_Selected_Seats.class).equalTo("SeatNo",v.getTag(R.string.SeatNo).toString()).findAll();
                                    single_row.deleteAllFromRealm();
                                    My_realm.commitTransaction();

                                    ((Activity_Select_Seats) getActivity()).update_seat_fare();

                                }



                            }
                        });

                        layout.addView(Seat_view);
                    }
                    else if(!all_seats.get(i).getIsAvailable() && all_seats.get(i).getGender() == SEAT_DETAILS.VALUE_GENDER_FEMALE)
                    {
                        if(height == 2 && width == 1)
                        {
                            Seat_view.setBackgroundResource(R.drawable.seat_layout_ladies_booked_sleeper_seat_port);
                        }
                        else if(height == 1 && width == 2)
                        {
                            Seat_view.setBackgroundResource(R.drawable.seat_layout_ladies_booked_sleeper_seat_land);
                        }
                        else
                        {
                            Seat_view.setBackgroundResource(R.drawable.seat_layout_ladies_booked_seat_port);
                        }

                        layout.addView(Seat_view);
                    }


        }


        return view;
    }


}
