package com.example.xb_sushil.iseva;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class Activity_ImageViewer extends Activity {
    ViewPager pager;
    ImageButton btnClose;
    String contectNumber;
    private ArrayList<String> listImageUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
       inti();
    }

    private void inti() {
        listImageUrls = new ArrayList<>();
        final TextView txtout = (TextView) findViewById(R.id.txtoutTxt);
        TextView txtName = (TextView) findViewById(R.id.txtimgViewerName);
        /*setFontSize(txtName);
        setFontSize(txtout);*/
        btnClose = (ImageButton) findViewById(R.id.btnClose);
        Intent intent = getIntent();
        contectNumber = intent.getStringExtra("number");
        String name = intent.getStringExtra("name");
        listImageUrls = intent.getStringArrayListExtra("imageList");
        int id = intent.getIntExtra("id", -1);
        pager = (ViewPager) findViewById(R.id.pager);
        txtName.setText(name);
        Custom_adapterImage_Display adapter = new Custom_adapterImage_Display(this,listImageUrls);
        pager.setAdapter(adapter);
        if (id != -1)
            pager.setCurrentItem(id);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity_ImageViewer.this.finish();
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

    private void setFontSize(TextView txt){
        if(txt!=null)
            txt.setTextSize(16 * getResources().getDisplayMetrics().density);
        return;
    }


   /* public void call(View v) {

        if (contectNumber != null) {
            if (!contectNumber.trim().isEmpty()) {
                //on call
                Globals.showAlertDialog("Alert", "Are you sure to Call ?",
                        this, "Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + contectNumber));
                                //startActivity(callIntent);


                            }
                        }, "Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {


                            }
                        }, false);

            }
        }

    }*/

}
