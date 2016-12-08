package com.iseva.app.source;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Activity_Post_Job extends Activity {

    private Spinner spinCity;
    private Spinner spinCategory;
    private ProgressDialog pd;
    private int cityID = -1;
    private int categoryID = -1;
    private EditText edtJobTitle;
    private EditText edtAddress;
    private EditText edtSalary;
    private EditText edtProfile;
    private  EditText edtNumber;
   // private EditText edtDate;

    private EditText edtTimigs;
    private EditText edtHolidays;
    private  EditText edtEligi;

    private EditText edtOthers;
    private EditText edtMinExper;

    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;
    private TextView txtLastDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_job);
        init();

    }


    private void init(){
        //setFooterAndHeader(R.id.imgBtnFooterPostJob,"Post Job");
        TextView txtH = (TextView)this.findViewById(R.id.txtHeader);
        if(txtH!=null)
            txtH.setText("Post Job");
        spinCity = (Spinner)findViewById(R.id.spinCity);
        spinCategory = (Spinner)findViewById(R.id.spinCategory);
        getCateCityData();
        Button btnSave = (Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadJob();
            }
        });
        ImageView imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        edtJobTitle = (EditText)findViewById(R.id.edtJobTitle);
         edtAddress = (EditText)findViewById(R.id.edtAddress);
        edtSalary = (EditText)findViewById(R.id.edtSalary);
         edtProfile = (EditText)findViewById(R.id.edtProfile);
         edtNumber = (EditText)findViewById(R.id.edtNumber);
        //edtDate = (EditText)findViewById(R.id.edtEmail);

         edtTimigs = (EditText)findViewById(R.id.edtTimings);
         edtHolidays = (EditText)findViewById(R.id.edtHoliday);
         edtEligi = (EditText)findViewById(R.id.edtEligibility);

        edtOthers = (EditText)findViewById(R.id.edtOthers);
        edtMinExper = (EditText)findViewById(R.id.edtMinExp);

        EditText[] editTextsRequired = {edtJobTitle,edtNumber,edtAddress,edtSalary,edtEligi};
        Globals.setHintRequired(editTextsRequired);

        txtLastDate = (TextView) findViewById(R.id.txtLastDate);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);

        ImageView img = (ImageView)findViewById(R.id.calender);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(v);
            }
        });
    }


    public void setDate(View view) {
        showDialog(999);
        /*Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT)
                .show();*/
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2+1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        txtLastDate.setText(new StringBuilder().append(year).append("/")
                .append(Globals.bindZero(month)).append("/").append(Globals.bindZero(day)));
    }

    private void getCateCityData() {
        Custom_ConnectionDetector connection = new Custom_ConnectionDetector(this);
        if (!connection.isConnectingToInternet()) {
            Globals.showAlert("ERROR", Globals.INTERNET_ERROR, this);
        } else {
            try {
                pd = Globals.showLoadingDialog(pd, this, false, "");
                HashMap<String, String> map = new HashMap<String, String>();
                //map.put("imei", Globals.getdeviceId(this));
                Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                        Request.Method.POST,
                        Custom_URLs_Params.getURL_citycate(),
                        map,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Globals.hideLoadingDialog(pd);
                                Log.i("SUSHIL", "json Response recieved !!" + response);
                                parseResponce(response);

                            }

                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError err) {
                        Globals.hideLoadingDialog(pd);
                        Log.i("SUSHIL", "ERROR VolleyError" + err);
                        Globals.showShortToast(
                                Activity_Post_Job.this,
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

    private void parseResponce(JSONObject object) {
        ArrayList<Object_City> listCity = new ArrayList<Object_City>();
        try {
            if (object != null) {
                JSONArray Array = object.getJSONArray("cities");
                JSONArray arrayCate = object.getJSONArray("category");
                if (Array != null) {
                    for (int i = 0; i < Array.length(); i++) {
                        try {
                            JSONObject obj = Array.getJSONObject(i);
                            Object_City objCity = new Object_City();
                            objCity.cityId = Integer.parseInt(obj.getString("id"));
                            objCity.name = obj.getString("name");
                            //Log.i("SUSHIL","SUSHIl city name "+objCity.name);
                            /*if(obj.has("is_select")) {
                                if (obj.getInt("is_select") == 1) {
                                    objCity.seLectedCity = true;
                                } else {
                                    objCity.seLectedCity = false;
                                }
                            }*/
                            listCity.add(objCity);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    setCitySpinner(listCity);
                }
                if(arrayCate.length()!=0){
                    ArrayList<Object_Category> list = new ArrayList<>();
                    for (int i=0;i<arrayCate.length();i++){
                        try {
                            JSONObject obj = arrayCate.getJSONObject(i);
                            Object_Category objCate = new Object_Category();
                            objCate.catId = Integer.parseInt(obj.getString("id"));
                            objCate.name = obj.getString("name");
                            //Log.i("SUSHIL","SUSHIl city name "+objCity.name);
                            /*if(obj.has("is_select")) {
                                if (obj.getInt("is_select") == 1) {
                                    objCity.seLectedCity = true;
                                } else {
                                    objCity.seLectedCity = false;
                                }
                            }*/
                            list.add(objCate);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    setCateSpinner(list);
                    }
                }

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void setCitySpinner(final ArrayList<Object_City>cityList) {

        ArrayList<String> listName = new ArrayList<>();
        int i = -1;
        for (Object_City city : cityList) {
            i++;
            if(i==0){
                listName.add("Select City");
            }
            listName.add(city.name);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listName);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCity.setAdapter(dataAdapter);
        spinCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {
                if(position!=0)
                    cityID = cityList.get(position-1).cityId;
                else{
                    cityID = -1;
                }

                 Log.i("SUSHIl","city id "+cityID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

    }

    private void setCateSpinner(final ArrayList<Object_Category>cityList) {

        ArrayList<String> listName = new ArrayList<>();
        int i = -1;
        for (Object_Category city : cityList) {
            i++;
            if(i==0){
                listName.add("Select Category");
            }
            listName.add(city.name);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listName);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCategory.setAdapter(dataAdapter);
        spinCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {
                if(position!=0)
                    categoryID = cityList.get(position-1).catId;
                else{
                    categoryID = -1;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

    }





    private void uploadJob(){



          if(cityID==-1){
              Globals.showShortToast(this,"Choose a city");
              return;
          }
        if(categoryID==-1){
            Globals.showShortToast(this,"Choose a job category");
            return;
        }
        if(edtJobTitle.getText().toString().equals("")){
            Globals.showShortToast(this,"Enter Business name");
            return;
        }
        if(edtAddress.getText().toString().toString().equals("")){
            Globals.showShortToast(this,"Enter Address");
            return;
        }
        if(edtSalary.getText().toString().toString().equals("")){
            Globals.showShortToast(this,"Enter Salary");
            return;
        }
        if(edtNumber.getText().toString().toString().equals("")){
            Globals.showShortToast(this,"Please add number.");
            return;
        }
        if(edtEligi.getText().toString().toString().equals("")){
            Globals.showShortToast(this,"Enter Eligibility");
            return;
        }
        /*if(edtDate.getText().toString().toString().equals("")){
            Globals.showShortToast(this,"Enter last date in YYYY/MM/DD format");
            return;
        }else{
           boolean isValid = Globals.checkDateFormat(edtDate.getText().toString());
            if(!isValid){
                Globals.showShortToast(this,"Enter Job Expiry Date in YYYY/MM/DD format");
                return;
            }
        }*/

        Custom_ConnectionDetector cd = new Custom_ConnectionDetector(this);
        if (cd.isConnectingToInternet()) {
            try {
                Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                        Request.Method.POST,
                        Custom_URLs_Params.getURL_PostJob(),
                        Custom_URLs_Params.getParams_UploadJob(this,edtJobTitle.getText().toString(),edtAddress.getText().toString(),edtNumber.getText().toString(),txtLastDate.getText().toString(),edtEligi.getText().toString(),edtProfile.getText().toString(),
                                edtSalary.getText().toString(),edtHolidays.getText().toString(),edtTimigs.getText().toString(),edtOthers.getText().toString(),cityID,categoryID,edtMinExper.getText().toString()), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("SUSHIL", "json Response recieved !!"+response);
                        contentResponce(response);
                    }


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError err) {
                        Log.i("SUSHIL", "ERROR VolleyError");

                    }
                });

                Custom_VolleyAppController.getInstance().addToRequestQueue(
                        jsonObjectRQST);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            Globals.showShortToast(this,Globals.INTERNET_ERROR);
        }
    }

    private void contentResponce(JSONObject obj){
        if(obj==null){
            return;
        }else{
            try{
                if(obj.has("success")){
                    if(obj.getInt("success")==1){
                        Globals.showShortToast(this,"Your Job Succesfully Upload.");
                        Globals.clearForm((ViewGroup) findViewById(R.id.linear));
                    }
                }
            }catch (JSONException ex){
                ex.printStackTrace();
            }
        }
    }


}


