package com.iseva.app.source.travel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iseva.app.source.R;



/**
 * Created by xb_sushil on 4/3/2017.
 */

public class Global_Travel {

    // 0 for debug and 1 for release
    public static int build_type = 1;

    public static void showAlertDialog(Context context, String title, String message, String btn_txt)
    {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        View v =  inflater.inflate(R.layout.textview,null);


        TextView title_tv = (TextView)v.findViewById(R.id.alert_title);
        title_tv.setText(title);
       /* TextView title_tv = new TextView(context);

        title_tv.setPadding(0,10,0,0);
        title_tv.setTextColor(ContextCompat.getColor(context,R.color.black));
        title_tv.setTextSize(16);


        title_tv.setGravity(Gravity.CENTER);
        title_tv.setText(title);*/

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCustomTitle(title_tv)
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton(btn_txt,new DialogInterface.OnClickListener() {
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
        b.setTextColor(ContextCompat.getColor(context, R.color.app_white));
    }

    public static double getFareAfterDiscount(double total_fare, double commition_per){

        double offer_per;
        double fare_after_total_discount;

        if (TRAVEL_DATA.ISEVA_SHARE_PCT <= commition_per) {
            offer_per = commition_per - TRAVEL_DATA.ISEVA_SHARE_PCT;
        } else {
            offer_per = 0;
        }

        fare_after_total_discount = (total_fare * (100 - offer_per)) / 100;

        fare_after_total_discount = (double) Math.round(fare_after_total_discount);

        return fare_after_total_discount;

    }

    public static class TRAVEL_DATA {


        public static String TOKEN_ID = "";
        public static String FROM_CITY_ID = "";
        public static String FROM_CITY_NAME = "";
        public static String TO_CITY_ID ="";
        public static String TO_CITY_NAME ="";
        public static String JOURNEY_DATE ="";
        public static String ROUTE_BUS_ID ="";
        public static String IS_AC_STR = "false";

        public static int MAX_COL;
        public static int MAX_ROW;

        public static int ISEVA_SHARE_PCT = 10;


    }

}
