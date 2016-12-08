package com.iseva.app.source;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Activity_List_Jobs extends Activity {

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_jobs);
        init();
    }

    private void init(){
        TextView txtHeader = (TextView)findViewById(R.id.txtHeader);
        txtHeader.setText("Jobs");
        ImageView imgBack = (ImageView)findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(getIntent()!=null){

            getJobs(getIntent().getIntExtra("catid",-1),getIntent().getIntExtra("cityid",-1));
        }

    }

    private void getJobs(int catid,int cityid){
        Custom_ConnectionDetector cd = new Custom_ConnectionDetector(this);
        if(!cd.isConnectingToInternet()){
            Globals.showAlert("Error",Globals.INTERNET_ERROR,this);
            return;
        }else {
            try {
                pd = Globals.showLoadingDialog(pd, this, false, "");
                //later add catid and cityid in map to get catid and cityid wise jobs
                HashMap<String,String> map = new HashMap<>();
                if(catid!=-1){
                    map.put("catid",catid+"");
                }
                if(cityid!=-1){
                    map.put("cityid",cityid+"");
                }
                Log.i("SUSHIL","map is "+map);
                Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                        Request.Method.POST,
                        Custom_URLs_Params.getURL_Get_Jobs(),
                        map, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("SUSHIL", "json Response recieved !!" + response);
                        Globals.hideLoadingDialog(pd);
                        Response(response);

                    }


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError err) {
                        Log.i("SUSHIL", "ERROR VolleyError");
                        Globals.hideLoadingDialog(pd);
                    }
                });

                Custom_VolleyAppController.getInstance().addToRequestQueue(
                        jsonObjectRQST);
            } catch (Exception e) {
                e.printStackTrace();
                Globals.hideLoadingDialog(pd);
            }
        }
    }

    private void Response(JSONObject obj){
        if(obj==null){
            return;
        }else{
            try{
                if(obj.has("data")){
                    JSONArray listJobs = obj.getJSONArray("data");
                    if(listJobs.length()!=0){
                        ArrayList<Object_Jobs> list = new ArrayList<>();
                        for (int i=0;i<listJobs.length();i++){
                            JSONObject jsonObject = listJobs.getJSONObject(i);
                            Object_Jobs objectJobs = new Object_Jobs();
                            //some values are fatch
                            objectJobs.setId(jsonObject.getInt("id"));
                            objectJobs.setJobTitle(jsonObject.getString("title"));
                            objectJobs.setContact(jsonObject.getString("personnumber"));
                            objectJobs.setEligiblity(jsonObject.getString("eligibility"));
                            objectJobs.setSalary(jsonObject.getString("salary"));

                            //add into list
                          list.add(objectJobs);
                        }
                        setDataInAdapter(list);
                    }else{
                        Globals.showShortToast(this,"No Data found. Try later.");
                    }
                }
            }catch (JSONException ex){
                ex.printStackTrace();
            }
        }
    }

    private void setDataInAdapter(ArrayList<Object_Jobs> list){
        //data set
        Custom_Adapter_JobsList adapter = new Custom_Adapter_JobsList(list,this);
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(adapter);
    }
}
