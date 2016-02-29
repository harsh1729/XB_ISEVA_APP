package com.example.xb_sushil.iseva;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Activity_About_Us extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        inti();
    }

    private void inti(){
        TextView txtHeader = (TextView) findViewById(R.id.txtHeader);
        txtHeader.setText("Contact Us");
        ImageView imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity_About_Us.this.finish();
            }
        });

        /*TextView txtAppname = (TextView)findViewById(R.id.txtAppName);
        TextView txtAboutUs = (TextView)findViewById(R.id.txtAboutUs);

        Globals.setAppFontTextView(this,txtAppname);
        Globals.setAppFontTextView(this,txtAboutUs);*/

    }


}
