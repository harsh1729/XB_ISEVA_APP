package com.iseva.app.source;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Activity_Job_Employer_Home extends Activity {

    private LinearLayout layout;
    private Button btnLogin;
    private Button btnRegister;
   // private Button btnFind;
  //  private Button btnPostJob;
    private TextView txtWelcome;
   private TextView txtDes;
    private LinearLayout imgLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_employer_home);

        init();

    }

    private void init(){
        setFooterAndHeader(R.id.imgBtnFooterPostJob);
        layout = (LinearLayout)findViewById(R.id.linear);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnRegister = (Button)findViewById(R.id.btnRegister);
       // btnFind = (Button)findViewById(R.id.btnFindResume);
       // btnPostJob = (Button)findViewById(R.id.btnPostJobs);
        txtWelcome = (TextView)findViewById(R.id.txtWelcome);
        txtDes = (TextView)findViewById(R.id.txtDes);
        imgLogout = (LinearLayout)findViewById(R.id.imgLogout);

    }


    @Override
    protected void onResume() {
        super.onResume();
        Custom_ConnectionDetector connection = new Custom_ConnectionDetector(this);
        if (!connection.isConnectingToInternet()) {
            Globals.showAlert("ERROR", Globals.INTERNET_ERROR, this);
        } else {
            Object_AppConfig config = new Object_AppConfig(this);
            if (config.getEmperId() != -1) {
                //getprofileEmployer();
                afterLogin();
            }
        }
    }

    private void afterLogin(){
        txtWelcome.setVisibility(View.VISIBLE);
        //btnPostJob.setVisibility(View.VISIBLE);
        layout.setVisibility(View.VISIBLE);
        imgLogout.setVisibility(View.VISIBLE);
        //hide btn
        txtDes.setVisibility(View.GONE);
        btnLogin.setVisibility(View.GONE);
        btnRegister.setVisibility(View.GONE);

    }

    private void logout(){
        Object_AppConfig config = new Object_AppConfig(this);
        config.setEmperId(-1);
        txtWelcome.setVisibility(View.GONE);
        imgLogout.setVisibility(View.GONE);
        //btnPostJob.setVisibility(View.VISIBLE);
        layout.setVisibility(View.GONE);
        //hide btn
        txtDes.setVisibility(View.VISIBLE);
        btnLogin.setVisibility(View.VISIBLE);
        btnRegister.setVisibility(View.VISIBLE);
    }
    public void login(View v){
        Custom_ConnectionDetector connection = new Custom_ConnectionDetector(this);
        if (!connection.isConnectingToInternet()) {
            Globals.showAlert("ERROR", Globals.INTERNET_ERROR, this);
        } else {
            Intent i = new Intent(this, Activity_Login_Jobs_Dep.class);
            i.putExtra("bool", false);
            startActivity(i);
        }
    }

    public void register(View v){
        Custom_ConnectionDetector connection = new Custom_ConnectionDetector(this);
        if (!connection.isConnectingToInternet()) {
            Globals.showAlert("ERROR", Globals.INTERNET_ERROR, this);
        } else {
            Intent i = new Intent(this, Activity_Job_Employee_Employer_Basic.class);
            i.putExtra("isEmployee", false);
            //startActivity(i);
            Globals.iAgree(this, i,getResources().getString(R.string.disclaimer));
        }
    }

    public void postJob(View v){
        Custom_ConnectionDetector connection = new Custom_ConnectionDetector(this);
        if (!connection.isConnectingToInternet()) {
            Globals.showAlert("ERROR", Globals.INTERNET_ERROR, this);
        } else {
            Intent i = new Intent(this, Activity_Post_Job.class);
            // i.putExtra("isEmployee",false);
            startActivity(i);
        }
    }

    public void findResume(View v){
        Custom_ConnectionDetector connection = new Custom_ConnectionDetector(this);
        if (!connection.isConnectingToInternet()) {
            Globals.showAlert("ERROR", Globals.INTERNET_ERROR, this);
        } else {
            Intent i = new Intent(this, Activity_Job_Resume_Search.class);
            // i.putExtra("isEmployee",false);
            startActivity(i);
        }
    }

    public void imgLogout(View v){
        logout();
    }
    public void footerBtnClick(View v){

        Class<?> nextClass = null;

        switch (v.getId()) {

            case R.id.imgBtnFooterPostResume:
                nextClass = Activity_Job_Employee_Home.class;
                break;
            case R.id.imgBtnFooterPostJob:
                nextClass = Activity_Job_Employer_Home.class;
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
