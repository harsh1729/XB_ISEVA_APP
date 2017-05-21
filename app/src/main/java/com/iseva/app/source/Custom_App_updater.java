package com.iseva.app.source;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Custom_App_updater {




    public static boolean app_launched(Context mContext,String force_update) {
        Boolean returnStatus = false;

                showRateDialog(mContext,force_update);
                returnStatus = true;
                return returnStatus;
    }

    private static void showRateDialog(final Context mContext,String force_update) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setCancelable(false);

        dialog.setTitle("New Update Available !");


        //Window window = dialog.getWindow();
        //window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams l1lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        l1lp.setMargins(20,0,20,0);

        LinearLayout ll = new LinearLayout(mContext);
        ll.setLayoutParams(l1lp);
        ll.setPadding(20,0,20,10);
        ll.setOrientation(LinearLayout.VERTICAL);
        TextView tv = new TextView(mContext);
        tv.setText("Please update your iSeva app to latest version.");
        tv.setWidth(240);
        tv.setPadding(6, 10, 6, 10);
        ll.addView(tv);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
             layoutParams.setMargins(0,10,0,0);
        Button b1 = new Button(mContext);
        b1.setLayoutParams(layoutParams);
        b1.setText("Update");
        b1.setBackground(ContextCompat.getDrawable(mContext,R.drawable.btn_background));
        b1.setTextColor(ContextCompat.getColor(mContext,R.color.app_white));
        b1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                updateit(mContext);
                dialog.dismiss();

            }
        });
        ll.addView(b1);

        if(!force_update.trim().equals("3"))
        {
            Button b2 = new Button(mContext);
            b2.setLayoutParams(layoutParams);
            b2.setText("Remind Me Later");
            b2.setBackground(ContextCompat.getDrawable(mContext,R.drawable.btn_background));
            b2.setTextColor(ContextCompat.getColor(mContext,R.color.app_white));
            b2.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            ll.addView(b2);
        }





        dialog.setContentView(ll);
        dialog.show();
    }

    public static void updateit(Context mContext)
    {
        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                .parse("market://details?id="+Globals.PNAME)));
    }


}
