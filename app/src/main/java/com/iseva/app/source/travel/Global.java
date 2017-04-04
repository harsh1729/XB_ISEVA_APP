package com.iseva.app.source.travel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iseva.app.source.R;



/**
 * Created by xb_sushil on 4/3/2017.
 */

public class Global {

    public static void showAlertDialog(Context context, String title, String message, String btn_txt)
    {
        TextView title_tv = new TextView(context);
        title_tv.setPadding(0,10,0,0);
        title_tv.setTextColor(ContextCompat.getColor(context,R.color.black));
        title_tv.setTextSize(context.getResources().getDimension(R.dimen.text_size_extra_small));

        title_tv.setGravity(Gravity.CENTER);
        title_tv.setText(title);

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


    public static void showalertdialogretry()
    {

    }

}
