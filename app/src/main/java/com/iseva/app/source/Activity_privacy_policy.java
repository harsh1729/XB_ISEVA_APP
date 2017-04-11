package com.iseva.app.source;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class Activity_privacy_policy extends AppCompatActivity {

    ImageView iv_header;
    TextView tv_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        iv_header = (ImageView)findViewById(R.id.header_back_button);
        tv_header = (TextView)findViewById(R.id.header_text);
        tv_header.setText("Privacy Policy");
        iv_header.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                activity_dismiss();
            }
        });

        WebView webView = (WebView) findViewById(R.id.privacy_policy_webview);
        webView.loadUrl("http://xercesblue.in/privacy_policy/iseva.html");
    }

    public void activity_dismiss()
    {
        this.finish();
        overridePendingTransition(R.anim.anim_none, R.anim.anim_out);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        overridePendingTransition(R.anim.anim_none, R.anim.anim_out);



    }

}
