package com.example.xb_sushil.iseva;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Activity_Settings extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        inti();
    }


    private void inti() {

        TextView txtHeader = (TextView) findViewById(R.id.txtHeader);
        txtHeader.setText("Settings");
        ImageView imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity_Settings.this.finish();
            }
        });
        //Globals.setAppFontTextView(this, txtHeader);
        //set font

        TextView txtSetttigs = (TextView) findViewById(R.id.txtSetttigs);
        TextView txtNotification = (TextView) findViewById(R.id.txtNotification);
        TextView txtAbout = (TextView) findViewById(R.id.txtAbout);
       /* TextView txtShare = (TextView) findViewById(R.id.txtShare);
        TextView txtRate = (TextView) findViewById(R.id.txtRate);*/
        final CheckBox checkBoxNoti = (CheckBox) findViewById(R.id.checkBoxNotification);

       /* TextView[] txtArray = {txtSetttigs, txtNotification, txtAbout, txtShare, txtRate};
        for (TextView txt : txtArray) {
            Globals.setAppFontTextView(this, txt);
        }*/

        /*txtShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareClick();
            }
        });

        txtRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Custom_ConnectionDetector cd = new Custom_ConnectionDetector(Activity_Settings.this);
                if (cd.isConnectingToInternet()) {
                    Custom_AppRater.rateIt(Activity_Settings.this);

                }
            }
        });*/

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linearContact);
        checkBoxNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationEn(checkBoxNoti);
            }
        });

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationAboutUs();
            }
        });

    }

    private void navigationAboutUs(){
        Intent i = new Intent(this,Activity_About_Us.class);
        startActivity(i);
    }

    private void notificationEn(CheckBox checkBoxNoti) {
        Object_AppConfig cofig = new Object_AppConfig(Activity_Settings.this);
        if (checkBoxNoti.isChecked()) {
            cofig.setNotificationEnabled(true);
        } else {
            cofig.setNotificationEnabled(false);
        }
    }

    public void shareClick() {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, Globals.SHARE_APP_MSG + "\n " + Globals.SHARE_LINK_GENERIC);
        //sendIntent.setPackage("com.whatsapp");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

}
