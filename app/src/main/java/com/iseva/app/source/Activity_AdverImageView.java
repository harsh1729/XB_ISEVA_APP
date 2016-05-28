package com.iseva.app.source;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;



import java.util.ArrayList;

public class Activity_AdverImageView extends Activity {

    ViewPager pager;
    ImageButton btnClose;
    private ArrayList<String> listImageUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adver_image_view);
        inti();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbindDrawables(findViewById(R.id.RootView));
        System.gc();
    }

    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }



    private void inti() {
        listImageUrls = new ArrayList<>();
        final TextView txtout = (TextView) findViewById(R.id.txtoutTxt);


        /*setFontSize(txtName);
        setFontSize(txtout);*/
        btnClose = (ImageButton) findViewById(R.id.btnClose);
        Intent intent = getIntent();
        listImageUrls = intent.getStringArrayListExtra("imageList");
        int id = intent.getIntExtra("id", -1);
        pager = (ViewPager) findViewById(R.id.pager);

        Custom_adapterImage_Display adapter = new Custom_adapterImage_Display(this,listImageUrls);
        pager.setAdapter(adapter);
        if (id != -1)
            pager.setCurrentItem(id);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity_AdverImageView.this.finish();
            }
        });


        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

                txtout.setText((pos + 1) + " / " + listImageUrls.size());

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });

    }
}
