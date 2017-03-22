package com.iseva.app.source.Fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.iseva.app.source.R;
import com.iseva.app.source.Realm_objets.Seat_details;
import com.iseva.app.source.Realm_objets.Selected_Seats;
import com.iseva.app.source.travel.Activity_Select_Seats;
import com.iseva.app.source.travel.Constants;
import com.iseva.app.source.travel.Search_Buses_Key;
import com.iseva.app.source.travel.Seat_Single;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.media.CamcorderProfile.get;

public class Fragment_Deck1 extends Fragment {

    Realm My_realm;

    int Total_screen_width;
    double Seat_height_percentage = 88.47;
    double Seat_width_percentage = 12;
    int fix_margin_left = 15;
    int fix_margin_right =15;
    int max_col = 0;
    int Seat_height;
    int Seat_width;
    int all_col;
    int margin;
    int extra_height = 5;
    int margin_top_seat = 10;
    int fixtopmargin = 70;
    int extramargin_top =20;

    ArrayList<Integer> colm_numbers;



    TextView Seat_view;
    FrameLayout.LayoutParams params;
    FrameLayout layout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment__deck1, container, false);
        My_realm = Realm.getInstance(getActivity());
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);


        colm_numbers = new ArrayList<Integer>();
        RealmResults<Seat_details> all_seats = My_realm.where(Seat_details.class).equalTo("Deck",1).notEqualTo("SeatNo",".DOOR").findAll();

            if(all_seats.size() == 0)
            {
                showAlertDialog(getResources().getString(R.string.validating_error_title),"There is no seats please choose another bus","Ok");
            }

            for(int l=0;l<all_seats.size();l++)
            {

                    if(all_seats.get(l).getCol() == 0 && all_seats.get(l).getIsAisle() == true)
                    {

                    }
                    else
                    {
                        if(!colm_numbers.contains(all_seats.get(l).getCol()))
                        {
                            colm_numbers.add(all_seats.get(l).getCol());
                        }
                    }







            }
        Collections.sort(colm_numbers);
        max_col = colm_numbers.size();
        Log.e("vikas all seat=",""+all_seats.size());
        Log.e("vikas max col=",""+max_col);


        layout = (FrameLayout)view.findViewById(R.id.layout_deck1);
        Total_screen_width = displaymetrics.widthPixels;
        Seat_width = (int)(Total_screen_width * Seat_width_percentage)/100;
        Seat_height = (int)(Seat_width * Seat_height_percentage)/100;
        margin = (int)(Total_screen_width-((Seat_width *max_col)+(fix_margin_left+fix_margin_right+30)))/(max_col-1);

        FrameLayout.LayoutParams lp  = new FrameLayout.LayoutParams(Seat_height,Seat_height);
        lp.leftMargin = ((Seat_width+margin)*(max_col-1)+fix_margin_left);

        TextView stearing = new TextView(getActivity());
        stearing.setLayoutParams(lp);
        stearing.setBackgroundResource(R.drawable.driver_icon);
        layout.addView(stearing);


        for(int i=0;i < all_seats.size(); i++) {


            for (int j= 0;j<colm_numbers.size();j++)
            {
                Log.e("vikas colm =",""+colm_numbers.get(j));
                Log.e("vikas index = ",""+j);
                if(all_seats.get(i).getCol() == colm_numbers.get(j))
                {
                    int row = all_seats.get(i).getRow();
                    int col = j;

                    final int height = all_seats.get(i).getHeight();
                    final int width = all_seats.get(i).getWidth();

                    if(height == 2 && width == 1)
                    {
                        params = new FrameLayout.LayoutParams(Seat_width,Seat_height*height);
                        params.topMargin = ((margin_top_seat+Seat_height+extra_height)*(row))+fixtopmargin;
                    }
                    else if(height == 1 && width == 2)
                    {
                        params = new FrameLayout.LayoutParams(Seat_width*width,Seat_height*height);
                        params.topMargin = ((margin_top_seat+Seat_height+extra_height)*(row))+fixtopmargin;
                    }
                    else
                    {
                        params = new FrameLayout.LayoutParams(Seat_width,(Seat_height*height)+extra_height);
                        params.topMargin = ((margin_top_seat+Seat_height+extra_height)*(row))+fixtopmargin;
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
                    Seat_view.setTag(R.string.IsAc,all_seats.get(i).getIsAc());
                    Seat_view.setTag(R.string.IsSleeper,all_seats.get(i).getIsSleeper());


                    if(all_seats.get(i).getIsAvailable() && all_seats.get(i).getGender().equals("anyType{}") || all_seats.get(i).getIsAvailable() && all_seats.get(i).getGender().equals("M") || all_seats.get(i).getIsAvailable() && all_seats.get(i).getGender().equals(""))
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

                                    My_realm.beginTransaction();
                                    RealmResults<Selected_Seats> Selected_seat_list = My_realm.where(Selected_Seats.class).findAll();
                                    My_realm.commitTransaction();

                                    if(Selected_seat_list.size() >= Constants.MaxSeats)
                                    {
                                        showAlertDialog("Alert","Maximum 5 Tickets can be booked at one time","Ok");
                                    }
                                    else {


                                        v.setTag(R.string.IsSelected, "true");
                                        if (height == 2 && width == 1)
                                        {
                                            v.setBackgroundResource(R.drawable.seat_layout_selected_sleeper_seat_port);
                                        }
                                        else if(height == 1 && width == 2)
                                        {
                                            Seat_view.setBackgroundResource(R.drawable.seat_layout_selected_sleeper_seat_land);
                                        }
                                        else
                                        {
                                            v.setBackgroundResource(R.drawable.seat_layout_selected_seat_port);
                                        }

                                        My_realm.beginTransaction();
                                        Selected_Seats selected_seats = My_realm.createObject(Selected_Seats.class);
                                        selected_seats.setSeatNo(v.getTag(R.string.SeatNo).toString());
                                        selected_seats.setDeck(Integer.parseInt(v.getTag(R.string.Deck).toString()));
                                        selected_seats.setGender(v.getTag(R.string.Gender).toString());
                                        selected_seats.setFare(Float.parseFloat(v.getTag(R.string.Fare).toString()));
                                        selected_seats.setIsAc(Boolean.parseBoolean(v.getTag(R.string.IsAc).toString()));
                                        selected_seats.setIsSleeper(Boolean.parseBoolean(v.getTag(R.string.IsSleeper).toString()));
                                        My_realm.commitTransaction();

                                        ((Activity_Select_Seats) getActivity()).update_seat_fare();
                                    }

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
                                        Seat_view.setBackgroundResource(R.drawable.seat_layout_available_sleeper_seat_land);
                                    }
                                    else
                                    {
                                        v.setBackgroundResource(R.drawable.seat_layout_available_seat_port);
                                    }

                                    My_realm.beginTransaction();
                                    RealmResults<Selected_Seats> single_row = My_realm.where(Selected_Seats.class).equalTo("SeatNo",v.getTag(R.string.SeatNo).toString()).findAll();
                                    single_row.remove(0);
                                    My_realm.commitTransaction();

                                    ((Activity_Select_Seats) getActivity()).update_seat_fare();
                                }



                            }
                        });
                        if(all_seats.get(i).getSeatNo().equals("anyType{}") || all_seats.get(i).getIsAisle().equals("true"))
                        {

                            Seat_view.setVisibility(View.GONE);
                        }

                        layout.addView(Seat_view);
                    }
                    else if(!all_seats.get(i).getIsAvailable() && all_seats.get(i).getGender().equals("anyType{}") || !all_seats.get(i).getIsAvailable() && all_seats.get(i).getGender().equals("M") || !all_seats.get(i).getIsAvailable() && all_seats.get(i).getGender().equals(""))
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

                        if(all_seats.get(i).getSeatNo().equals("anyType{}") || all_seats.get(i).getIsAisle().equals("true"))
                        {

                            Seat_view.setVisibility(View.GONE);
                        }
                        layout.addView(Seat_view);
                    }
                    else if(all_seats.get(i).getIsAvailable() && all_seats.get(i).getGender().equals("F"))
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

                                    My_realm.beginTransaction();
                                    RealmResults<Selected_Seats> Selected_seat_list = My_realm.where(Selected_Seats.class).findAll();
                                    My_realm.commitTransaction();

                                    if(Selected_seat_list.size() >= Constants.MaxSeats)
                                    {
                                        showAlertDialog("Alert","Maximum 5 Tickets can be booked at one time","Ok");
                                    }
                                    else
                                    {

                                        v.setTag(R.string.IsSelected, "true");
                                        if (height == 2) {
                                            v.setBackgroundResource(R.drawable.seat_layout_selected_sleeper_seat_port);
                                        }
                                        else if(height == 1 && width == 2)
                                        {
                                            Seat_view.setBackgroundResource(R.drawable.seat_layout_selected_sleeper_seat_land);
                                        }else {
                                            v.setBackgroundResource(R.drawable.seat_layout_selected_seat_port);
                                        }
                                        My_realm.beginTransaction();
                                        Selected_Seats selected_seats = My_realm.createObject(Selected_Seats.class);
                                        selected_seats.setSeatNo(v.getTag(R.string.SeatNo).toString());
                                        selected_seats.setDeck(Integer.parseInt(v.getTag(R.string.Deck).toString()));
                                        selected_seats.setGender(v.getTag(R.string.Gender).toString());
                                        selected_seats.setFare(Float.parseFloat(v.getTag(R.string.Fare).toString()));
                                        selected_seats.setIsAc(Boolean.parseBoolean(v.getTag(R.string.IsAc).toString()));
                                        selected_seats.setIsSleeper(Boolean.parseBoolean(v.getTag(R.string.IsSleeper).toString()));
                                        My_realm.commitTransaction();

                                        ((Activity_Select_Seats) getActivity()).update_seat_fare();
                                    }
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
                                        Seat_view.setBackgroundResource(R.drawable.seat_layout_ladies_sleeper_seat_land);
                                    }
                                    else
                                    {
                                        v.setBackgroundResource(R.drawable.seat_layout_ladies_seat_port);
                                    }

                                    My_realm.beginTransaction();
                                    RealmResults<Selected_Seats> single_row = My_realm.where(Selected_Seats.class).equalTo("SeatNo",v.getTag(R.string.SeatNo).toString()).findAll();
                                    single_row.remove(0);
                                    My_realm.commitTransaction();

                                    ((Activity_Select_Seats) getActivity()).update_seat_fare();

                                }



                            }
                        });

                        layout.addView(Seat_view);
                    }
                    else if(!all_seats.get(i).getIsAvailable() && all_seats.get(i).getGender().equals("F"))
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
                else
                {

                }

            }





        }



        return view;
    }

    public void showAlertDialog(String title,String message,String buttonlabel)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton(buttonlabel,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

        Button b = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        b.setTextColor(ContextCompat.getColor(getActivity(), R.color.app_theme_color));
    }
}
