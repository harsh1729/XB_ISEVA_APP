package com.iseva.app.source;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

public class Activity_Resume_Details extends Activity {

    private ProgressDialog pd;
    private String num = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_details);

        init();
    }

    private void init(){
        TextView txtHeader = (TextView)findViewById(R.id.txtHeader);
        txtHeader.setText("Details");
        ImageView imgBack = (ImageView)findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button btnCall = (Button)findViewById(R.id.callBtn);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!num.equals(""))
                 Globals.call(Activity_Resume_Details.this,num);
            }
        });


        getDeatils(getIntent().getIntExtra("id",-1));
    }

    private void getDeatils(int id){
        Custom_ConnectionDetector connection = new Custom_ConnectionDetector(this);
        if (!connection.isConnectingToInternet()) {
            Globals.showAlert("ERROR", Globals.INTERNET_ERROR, this);
        } else {
            try {
                pd = Globals.showLoadingDialog(pd, this, false, "");
                HashMap<String, String> map = new HashMap<String, String>();
                if(id!=-1)
                map.put("id",id+"");
                // map.put("imei", Globals.getdeviceId(this));
                Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                        Request.Method.POST,
                        Custom_URLs_Params.getURL_GetResume(),
                        map,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Globals.hideLoadingDialog(pd);
                                Log.i("SUSHIL", "json Response recieved !!" + response);
                                parseResume(response);

                            }

                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError err) {
                        Globals.hideLoadingDialog(pd);
                        Log.i("SUSHIL", "ERROR VolleyError" + err);
                        Globals.showShortToast(
                                Activity_Resume_Details.this,
                                Globals.MSG_SERVER_ERROR);

                    }
                });

                Custom_VolleyAppController.getInstance().addToRequestQueue(
                        jsonObjectRQST);


            } catch (Exception ex) {
                Globals.hideLoadingDialog(pd);
            }
        }
    }

   private void parseResume(JSONObject obj){
       if(obj==null){
           Globals.showShortToast(this,"Data not Found");
           finish();
       }else{
           try{
               JSONArray list = obj.getJSONArray("data");
               if(list.length()!=0) {
                   JSONObject object = list.getJSONObject(0);
                   if(object.has("image")) {
                       ImageView img = (ImageView)findViewById(R.id.imgDeatils);
                       int totalContent = Globals.getScreenSize((Activity) this).x;
                       int imgWidth = totalContent - ((totalContent * 75) / 100);

                       LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) img
                               .getLayoutParams();
                       lp.width = imgWidth;
                       lp.height = (int) (imgWidth * 1.1);
                       img.setLayoutParams(lp);


                       Globals.loadImageIntoImageView(img, object.getJSONObject("image").getString("imageurl"), this, R.drawable.default_offer, R.drawable.default_offer);
                   }

                   if (object.has("name")) {
                       if (object.getString("name") != null) {
                           TextView txtName = (TextView) findViewById(R.id.txtName);
                           txtName.setText(object.getString("name"));

                       }
                   }

                   if (object.has("qualification")) {
                       if (object.getString("qualification") != null) {
                           TextView txt = (TextView) findViewById(R.id.txtQualification);
                           txt.setText(object.getString("qualification"));

                       }
                   }

                   if (object.has("email")) {
                       if (object.getString("email") != null) {
                           TextView txt = (TextView) findViewById(R.id.txtemail);
                           String styledText = "<u><font color='blue'>"+object.getString("email")+"</font></u>";
                           txt.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);
                           //txt.setText(object.getString("email"));
                          // Globals.stripUnderlines(txt);
                           //txt.setText(Html.fromHtml("<a href=\"mailto:ask@me.it\">Send Feedback</a>"));

                       }
                   }

                   if(object.has("address")){
                       if (object.getString("address") != null) {
                           LinearLayout linear = (LinearLayout)findViewById(R.id.linearAddress);
                           linear.setVisibility(View.VISIBLE);
                           TextView txt = (TextView) findViewById(R.id.txtContact);
                           txt.setText(object.getString("address"));
                       }
                   }

                   if(object.has("number")){
                       if (object.getString("number") != null) {
                           LinearLayout linear = (LinearLayout)findViewById(R.id.linearContactsNo);
                           linear.setVisibility(View.VISIBLE);
                           TextView txt = (TextView) findViewById(R.id.txtnumber);
                           txt.setText(object.getString("number"));
                       }
                   }

                   if(object.has("experience")){
                       if (object.getString("experience") != null && !object.getString("experience").equals("")) {
                           LinearLayout linear = (LinearLayout)findViewById(R.id.linearExperience);
                           linear.setVisibility(View.VISIBLE);
                           TextView txt = (TextView) findViewById(R.id.txtExperience);
                           txt.setText(object.getString("experience"));
                       }
                   }

                   if(object.has("currentjob") && object.has("currentsalary") ){
                       if (object.getString("currentjob") != null && !object.getString("currentjob").equals("")) {
                           LinearLayout linear = (LinearLayout)findViewById(R.id.linearCurrentJob);
                           linear.setVisibility(View.VISIBLE);
                           TextView txt = (TextView) findViewById(R.id.txtCurrentStatus);
                           if(object.getString("currentsalary") != null && !object.getString("currentsalary").equals(""))
                               txt.setText("Job : "+object.getString("currentjob")+"\nSalary : "+object.getString("currentsalary")+"/-");
                           else
                               txt.setText(object.getString("currentjob"));

                           if(object.has("currentcompany")){
                               String txtValue = txt.getText().toString();
                               if(object.getString("currentcompany")!=null && !object.getString("currentcompany").equals(""))
                                 txt.setText(txtValue+"\n"+"Company :"+object.getString("currentcompany"));
                           }
                       }
                   }

                   if(object.has("age")){
                       if (object.getString("age") != null && !object.getString("age").equals("")) {
                           LinearLayout linear = (LinearLayout)findViewById(R.id.linearAge);
                           linear.setVisibility(View.VISIBLE);
                           TextView txt = (TextView) findViewById(R.id.txtAge);
                           String dateCaluction = "";
                           try{
                              String dob = object.getString("age");
                               Log.i("SUSHIL","dob "+dob);
                               int year = Integer.parseInt(dob.substring(0,4));
                               Log.i("SUSHIL","date year "+year);
                               int mon = Integer.parseInt(dob.substring(5,7));
                               Log.i("SUSHIL","date  mon  "+mon);
                               int day = Integer.parseInt(dob.substring(8,10));
                               Log.i("SUSHIL","date "+day);
                               dateCaluction = getAge(year,mon,day);
                           }catch (Exception e){

                           }
                           if(object.getString("gender").equals("") && object.getString("gender")==null) {
                               if (!dateCaluction.equals(""))
                                   txt.setText(dateCaluction + " Years");
                           }else {
                               if (!dateCaluction.equals(""))
                                   txt.setText(dateCaluction + " Yerars (" + object.getString("gender") + ")");
                               else
                                   txt.setText(dateCaluction + "" + object.getString("gender") + "");
                           }
                           if(object.has("mstatus")){
                               String txtVal = txt.getText().toString();
                               if(object.getString("mstatus")!=null && !object.getString("mstatus").equals("") )
                               txt.setText(txtVal+"\n"+"Marital Status : "+object.getString("mstatus"));
                           }
                       }
                   }

                   if(object.has("achive")){
                       if (object.getString("achive") != null && !object.getString("achive").equals("")) {
                           LinearLayout linear = (LinearLayout)findViewById(R.id.linearAchive);
                           linear.setVisibility(View.VISIBLE);
                           TextView txt = (TextView) findViewById(R.id.txtAchive);
                           txt.setText(object.getString("achive"));
                       }
                   }

                   if(object.has("others")){
                       if (object.getString("others") != null && !object.getString("others").equals("")) {
                           LinearLayout linear = (LinearLayout)findViewById(R.id.linearOthers);
                           linear.setVisibility(View.VISIBLE);
                           TextView txt = (TextView) findViewById(R.id.txtOthers);
                           txt.setText(object.getString("others"));
                       }
                   }

                   if(object.has("resume")){
                       if (object.getJSONObject("resume") != null) {
                           try {
                               LinearLayout linear = (LinearLayout) findViewById(R.id.linearResume);
                               final JSONObject resumeObj = object.getJSONObject("resume");
                               linear.setVisibility(View.VISIBLE);
                               TextView txt = (TextView) findViewById(R.id.txtResume);
                               String styledText = "<u><font color='blue'>"+"Download Resume"+"</font></u>";
                               txt.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);
                               //Globals.stripUnderlines(txt);
                               txt.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       // Globals.showLoadingDialog(pd,Activity_Resume_Details.this,false,"");
                                       try {
                                           startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(resumeObj.getString("imageurl"))));
                                       }catch (JSONException e){
                                           e.printStackTrace();
                                       }

                                   }
                               });
                           }catch (Exception e){
                               e.printStackTrace();
                           }
                       }
                   }

                   if(object.has("number")){
                       //if(object.getString("number")!=null && !object.getString("number"))
                       num = object.getString("number");
                   }

               }

           }catch (Exception e){
               e.printStackTrace();
           }
       }
   }

    private String getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }
}
