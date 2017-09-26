package com.iseva.app.source.Fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iseva.app.source.R;
import com.iseva.app.source.Realm_objets.Realm_Selected_Seats;
import com.iseva.app.source.travel.Activity_Select_Seats;
import com.iseva.app.source.travel.Constants;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Parent_Deck extends Fragment {

    Realm My_realm;


    TextView Seat_view;
    FrameLayout.LayoutParams params;
    FrameLayout layout;

    public Fragment_Parent_Deck() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

   protected  void selectSeat(View v,int height,int width){

       My_realm.beginTransaction();
       RealmResults<Realm_Selected_Seats> Selected_seat_list = My_realm.where(Realm_Selected_Seats.class).findAll();
       My_realm.commitTransaction();

       if(Selected_seat_list.size() >= Constants.MAX_SEATS)
       {
           showAlertDialog("Limit Reached!","Maximum 5 tickets can be booked in a single booking","Ok");
       }
       else {

           float fare_after_offer = Float.parseFloat(v.getTag(R.string.Offer_Fare).toString());

           if(fare_after_offer > 0){

               v.setTag(R.string.IsSelected, "true");
               if (height == 2 && width == 1)
               {
                   v.setBackgroundResource(R.drawable.seat_layout_selected_sleeper_seat_port);
               }
               else if(height == 1 && width == 2)
               {
                   v.setBackgroundResource(R.drawable.seat_layout_selected_sleeper_seat_land);
               }
               else
               {
                   v.setBackgroundResource(R.drawable.seat_layout_selected_seat_port);
               }

               My_realm.beginTransaction();
               Realm_Selected_Seats selected_seats = My_realm.createObject(Realm_Selected_Seats.class);
               selected_seats.setSeatNo(v.getTag(R.string.SeatNo).toString());
               selected_seats.setDeck(Integer.parseInt(v.getTag(R.string.Deck).toString()));
               selected_seats.setGender(v.getTag(R.string.Gender).toString());
               selected_seats.setFare(Float.parseFloat(v.getTag(R.string.Fare).toString()));
               selected_seats.setFare_after_offer(Float.parseFloat(v.getTag(R.string.Offer_Fare).toString()));
               selected_seats.setIsSleeper(Boolean.parseBoolean(v.getTag(R.string.IsSleeper).toString()));
               selected_seats.setSeat_Type(Integer.parseInt(v.getTag(R.string.Seat_Type).toString()));
               My_realm.commitTransaction();

               ((Activity_Select_Seats) getActivity()).update_seat_fare();

           }else{

               getActivity().finish();
               Toast.makeText( getActivity(),"Please try again",Toast.LENGTH_SHORT);
           }


       }

   }


    public void showAlertDialog(String title,String message,String buttonlabel)
    {
        TextView title_tv = new TextView(getContext());
        title_tv.setPadding(0,10,0,0);
        title_tv.setTextColor(ContextCompat.getColor(getContext(),R.color.black));
        title_tv.setTextSize(getResources().getDimension(R.dimen.text_size_small));
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
