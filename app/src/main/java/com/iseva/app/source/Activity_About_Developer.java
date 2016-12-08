package com.iseva.app.source;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Activity_About_Developer extends Activity {

    TextView txtCall;
    TextView txtWebsite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_dev);
        TextView txtHeader = (TextView)findViewById(R.id.txtHeader);
        txtHeader.setText("Developed n Maintained by");
        ImageView imgBack = (ImageView)findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtCall  = (TextView)findViewById(R.id.btnCall);
        txtWebsite = (TextView)findViewById(R.id.website);

        txtWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.xercesblue.in"));
                startActivity(browserIntent);
            }
        });

        txtCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globals.call(Activity_About_Developer.this,txtCall.getText().toString());
            }
        });
    }
}
