package com.iseva.app.source;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Activity_List_Resume extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_resume);

        init();
    }

    private void init(){
        TextView txtHeader = (TextView)findViewById(R.id.txtHeader);
        txtHeader.setText("Resume");
        ImageView imgBack = (ImageView)findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        parseResume(intent.getStringExtra("resume"));
    }

    private void parseResume(String resume){
        try{
            JSONObject object = new JSONObject(resume);
            JSONArray listArray = object.getJSONArray("data");
            ArrayList<Object_Resume> list = new ArrayList<>();
            for (int i=0;i<listArray.length();i++){
                JSONObject obj = listArray.getJSONObject(i);
                Object_Resume objResume = new Object_Resume();
                objResume.setId(obj.getInt("id"));
                objResume.setName(obj.getString("name"));
                objResume.setExperience(obj.getString("experience"));
                objResume.setQualification(obj.getString("qualification"));
                objResume.setGender(obj.getString("gender"));
                objResume.setImgUrl(obj.getJSONObject("image").getString("imageurl"));
                list.add(objResume);

            }
            setAdapter(list);
        }catch (Exception e){
            Globals.showShortToast(this,"Data Error");
            finish();
        }
    }
    private void setAdapter(ArrayList<Object_Resume>list){
        Custom_Adapter_ResumeList adapter = new Custom_Adapter_ResumeList(list,this);
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(adapter);
    }
}
