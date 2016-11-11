package com.iseva.app.source;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Activity_Search_Jobs extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_jobs);
        init();
    }

   private void init(){
       setFooterAndHeader(R.id.imgBtnFooterPostJob);

   }


    public void postResume(View v){
        Intent i = new Intent(this,Activity_Post_Resume.class);
        startActivity(i);
    }

    public void getJobs(View v){
        Intent i = new Intent(this,Activity_Search_Resume.class);
        startActivity(i);
    }
    public void footerBtnClick(View v){

        Class<?> nextClass = null;

        switch (v.getId()) {
            case R.id.imgBtnFooterFind:
                nextClass = Activity_Search_Resume.class;
                break;
            case R.id.imgBtnFooterPostResume:
                nextClass = Activity_Post_Resume.class;
                break;
            case R.id.imgBtnFooterPostJob:
                nextClass = Activity_Search_Jobs.class;
                break;

            default:
                break;
        }

        if(nextClass != null){
            if(this.getClass() != nextClass){
                Intent i = new Intent(this,nextClass);
                startActivity(i);
            }
        }

    }

    private void setFooterAndHeader(int footerbtnId){
        if(footerbtnId != -1)
        {
            View v = this.findViewById(footerbtnId);
            if(v!= null){
                v.setSelected(true);
                Log.i("FOOTER", "Set Selected");
            }else{
                Log.i("FOOTER", "Null Button");
            }
        }


    }
}
