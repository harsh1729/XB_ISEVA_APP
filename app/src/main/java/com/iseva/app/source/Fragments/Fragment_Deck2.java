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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iseva.app.source.R;
import com.iseva.app.source.Realm_objets.Realm_Seat_Details;
import com.iseva.app.source.Realm_objets.Selected_Seats;
import com.iseva.app.source.travel.Activity_Select_Seats;
import com.iseva.app.source.travel.Constants;
import com.iseva.app.source.travel.Constants.SEAT_DETAILS;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Deck2 extends Fragment {

    Realm My_realm;

    int Total_screen_width;
    double Seat_height_percentage = 100;
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
    int extra_height = 5;
    double margin_top_seat_per = 4.50;
    int margin_top_seat = 0;
    int fixtopmargin = 30;

   // ArrayList<Integer> colm_numbers;

    TextView Seat_view;
    FrameLayout.LayoutParams params;

    FrameLayout layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__deck2, container, false);
        My_realm = Realm.getDefaultInstance();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

//        colm_numbers = new ArrayList<Integer>();
        RealmResults<Realm_Seat_Details> all_seats = My_realm.where(Realm_Seat_Details.class).equalTo("Deck",2).notEqualTo("SeatNo",".DOOR").findAll();



//        for(int l=0;l<all_seats.size();l++)
//        {
//
//            if(!colm_numbers.contains(all_seats.get(l).getCol()))
//                {
//                    colm_numbers.add(all_seats.get(l).getCol());
//                }
//            }
//
//        Collections.sort(colm_numbers);

//        colm_numbers.add((colm_numbers.size()/2),99);

//        max_col = colm_numbers.size();
        Log.e("vikas all seat=",""+all_seats.size());
        Log.e("vikas max col=",""+max_col);


        layout = (FrameLayout)view.findViewById(R.id.layout_deck2);

        Total_screen_width = displaymetrics.widthPixels;

        fix_margin_left = (int)(Total_screen_width * fix_left_per)/100;
        fix_margin_right = (int)(Total_screen_width * fix_right_per)/100;

        margin_top_seat = (int)(Total_screen_width * margin_top_seat_per)/100;

        Seat_width = (int)(Total_screen_width * Seat_width_percentage)/100;
        Seat_height = (int)(Seat_width * Seat_height_percentage)/100;
        margin = (int)(Total_screen_width-((Seat_width *max_col)+(fix_margin_left+fix_margin_right)))/(max_col-1);




        for(int i=0;i < all_seats.size(); i++)
        {

                    int row = all_seats.get(i).getRow();
                    int col = all_seats.get(i).getCol();

                    final int height = all_seats.get(i).getHeight();
                    final int width = all_seats.get(i).getWidth();

                    if(height == 2 && width == 1)
                    {
                        int realseatheight = (Seat_height*height*92)/100;
                        params = new FrameLayout.LayoutParams(Seat_width,realseatheight);
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
                    Seat_view.setTag(R.string.Offer_Fare,all_seats.get(i).getFare_after_offer());
                    Seat_view.setTag(R.string.IsSleeper,all_seats.get(i).getIsSleeper());


                    if(all_seats.get(i).getIsAvailable() &&  (all_seats.get(i).getGender() == SEAT_DETAILS.VALUE_GENDER_ALL || all_seats.get(i).getGender() == SEAT_DETAILS.VALUE_GENDER_MALE))
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

                                    if(Selected_seat_list.size() >= Constants.MAX_SEATS)
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
                                        selected_seats.setFare_after_offer(Float.parseFloat(v.getTag(R.string.Offer_Fare).toString()));
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

                                    My_realm.beginTransaction();
                                    RealmResults<Selected_Seats> Selected_seat_list = My_realm.where(Selected_Seats.class).findAll();
                                    My_realm.commitTransaction();

                                    if(Selected_seat_list.size() >= Constants.MAX_SEATS)
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
                                        selected_seats.setFare_after_offer(Float.parseFloat(v.getTag(R.string.Offer_Fare).toString()));
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


    public void showAlertDialog(String title,String message,String buttonlabel)
    {
        TextView title_tv = new TextView(getContext());
        title_tv.setPadding(0,10,0,0);
        title_tv.setTextColor(ContextCompat.getColor(getContext(),R.color.black));
        title_tv.setTextSize(getResources().getDimension(R.dimen.text_size_mediam));
        title_tv.setGravity(Gravity.CENTER);
        title_tv.setText(title);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCustomTitle(title_tv)
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton(buttonlabel,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button b = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        b.setLayoutParams(lp);
        b.setBackgroundResource(R.drawable.btn_background);
        b.setTextColor(ContextCompat.getColor(getContext(), R.color.app_white));
    }
}
