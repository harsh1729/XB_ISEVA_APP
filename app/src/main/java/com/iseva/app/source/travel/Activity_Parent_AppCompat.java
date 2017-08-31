package com.iseva.app.source.travel;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.iseva.app.source.R;

public class Activity_Parent_AppCompat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    protected ProgressDialog progress;

    protected boolean isNetworkConnected(boolean reconnect) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean status = cm.getActiveNetworkInfo() != null;


        if(status){

            return true;
        }else{

            if(reconnect){

                callAlertBox(getResources().getString(R.string.internet_connection_error_title),getResources().getString(R.string.internet_connection_error_message_try_again));

            }
            return false;
        }

    }

    protected void callAlertBox(String title,String error ) {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        View v = inflater.inflate(R.layout.textview, null);


        TextView title_tv = (TextView) v.findViewById(R.id.alert_title);
        title_tv.setText(title);//getResources().getString(R.string.internet_connection_error_title));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCustomTitle(title_tv)
                .setMessage(error)
                .setCancelable(false)
                .setNegativeButton("Try Again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                        if(Activity_Parent_AppCompat.this instanceof  Parent_Interface){

                            ((Parent_Interface)Activity_Parent_AppCompat.this).retryServiceCall();
                        }

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

        if(progress != null) {
            progress.dismiss();
        }

        Button b = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        b.setTextColor(ContextCompat.getColor(this, R.color.app_theme_color));

    }

    public void activity_dismiss()
    {
        this.finish();

    }

}

