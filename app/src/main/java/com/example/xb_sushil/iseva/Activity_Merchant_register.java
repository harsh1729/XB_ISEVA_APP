package com.example.xb_sushil.iseva;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Activity_Merchant_register extends FragmentActivity {

    int EditText_ID = 1;
    int TEXTVIEW_ID = 2;
    int LINEARLAYOUT_VERTICAL_ID = 3;
    int LINEARLAYOUT_HORIZONTAL_ID = 4;
    int BUTTON_ID = 5;
    private int swipeId = 1;
    private ProgressDialog pd;
    private int REQUEST_CAMERA = 1;
    private int SELECT_FILE = 2;
    private int totalScreen = 0;
    private boolean doubleBackToExitPressedOnce = false;


   /* String json = "{'control':[[{'id':1,'typeId':1,'label':'Name',contant:[sushil],'sort_order':1},{'id':2,'typeId':1,'label':'Father name',contant:['sushil'],'sort_order':2},{'id':3,'typeId':1,'label':'Number',contant:['sushil'],'sort_order':3},{'id':4,'typeId':4,'label':'Image Profile',contant:[''],'sort_order':4},{'id':5,'typeId':3,'label':'Choose City',contant:['ganganagr',suratgarh,nohar,sagria,hanumangarh],'sort_order':5}]" +
            ",[{'id':6,'typeId':1,'label':'Address',contant:[sushil],'sort_order':1},{'id':7,'typeId':1,'label':'Timing',contant:['sushil'],'sort_order':2},{'id':8,'typeId':1,'label':'Facility',contant:['sushil'],'sort_order':3},{'id':9,'typeId':3,'label':'Choose State',contant:['ganganagr',suratgarh,nohar,sagria,hanumangarh],'sort_order':4}]" +
            ",[{'id':'10','typeId':1,'label':'Email',contant:[sushil],'sort_order':1},{'id':'11','typeId':1,'label':'REG.No',contant:['sushil'],'sort_order':2},{'id':'12','typeId':1,'label':'Appon',contant:['sushil'],'sort_order':1}]" +
            ",[{'id':'13','typeId':1,'label':'name',contant:[sushil],'sort_order':1},{'id':'14','typeId':1,'label':'Father name',contant:['sushil'],'sort_order':2}]]}";*/

   /* String json = "{'control':[[{'typeId':1,'label':'name',contant:[sushil]},{'typeId':1,'label':'Father name',contant:['sushil']},{'typeId':1,'label':'Number',contant:['sushil']},{'typeId':2,'label':'Image Profile',contant:['']'sort_order':4},{'id':5,'typeId':3,'label':'Choose City',contant:['ganganagr',suratgarh,nohar,sagria,hanumangarh],'sort_order':5}]" +
            ",[{'id':6,'typeId':1,'label':'Address',contant:[sushil],'sort_order':1},{'id':7,'typeId':1,'label':'Timing',contant:['sushil'],'sort_order':2},{'id':8,'typeId':1,'label':'Facility',contant:['sushil'],'sort_order':3},{'id':9,'typeId':3,'label':'Choose State',contant:['ganganagr',suratgarh,nohar,sagria,hanumangarh],'sort_order':4}]" +
            ",[{'id':'10','typeId':1,'label':'Email',contant:[sushil],'sort_order':1},{'id':'11','typeId':1,'label':'REG.No',contant:['sushil'],'sort_order':2},{'id':'12','typeId':1,'label':'Appon',contant:['sushil'],'sort_order':1}]" +
            ",[{'id':'13','typeId':1,'label':'name',contant:[sushil],'sort_order':1},{'id':'14','typeId':1,'label':'Father name',contant:['sushil'],'sort_order':2}]]}";*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_register);
        //JSONObject obj = creatJson();
        //createControl(obj);

        setDataInDataBase();
        inti();

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }



    private void setDataInDataBase() {
        Intent in = getIntent();
        String json = in.getStringExtra("controls");
        ArrayList<Object_Control_Maping> list = null;
        try {
            JSONObject obj = new JSONObject(json);
            if (obj.has("controls")) {
                list = new ArrayList<>();
                JSONArray Array = obj.getJSONArray("controls");
                totalScreen = Array.length();
                for (int i = 0; i < Array.length(); i++) {
                    JSONArray subArray = Array.getJSONArray(i);
                    for (int j = 0; j < subArray.length(); j++) {
                        Object_Control_Maping objMap = new Object_Control_Maping();
                        ArrayList<Object_Data> dataValue = new ArrayList<>();
                        JSONObject jsonObject = subArray.getJSONObject(j);
                        if (jsonObject.has("id")) {
                            objMap.controlID = jsonObject.getInt("id");
                           // Log.i("SUSHIL","multi image view id "+objMap.controlID);
                        }
                        if (jsonObject.has("mastercontrolid")) {
                            objMap.typeId = jsonObject.getInt("mastercontrolid");

                        }
                        if (jsonObject.has("label")) {
                            objMap.label = jsonObject.getString("label");
                        }
                        if (jsonObject.has("sortorder")) {
                            objMap.sortOrder = jsonObject.getInt("sortorder");
                        }
                        if(jsonObject.has("placeholder")){
                            objMap.hint = jsonObject.getString("placeholder");
                        }
                        if(jsonObject.has("isrequired")){
                            objMap.isrequired = jsonObject.getInt("isrequired");
                        }

                        objMap.screenId = i;
                        Log.i("SUSHIL", "control type is is..." + objMap.typeId);
                        if (jsonObject.has("controldata")) {
                            JSONArray array = jsonObject.getJSONArray("controldata");
                            // Log.i("SUSHIL","list image size  "+array.length()+"obj type id is "+objMap.typeId);
                            if (array != null) {
                                for (int k = 0; k < array.length(); k++) {
                                    Object_Data object_data = new Object_Data();
                                    JSONObject objson = array.getJSONObject(k);

                                    // String value = array.getString(k);
                                    object_data.value = objson.getString("name");

                                    Log.i("SUSHIL", "control type is is..." + object_data.value);
                                    if(objMap.typeId==Custom_Control.SpinnerID){

                                    }
                                    if (k == 0) {
                                        object_data.selectedValue = 1;
                                    }
                                    if(objson.has("id"))
                                        object_data.imageName = String.valueOf(objson.getInt("id"));

                                    if (objMap.typeId == Custom_Control.MultiImageID || objMap.typeId == Custom_Control.ImageViewID) {
                                        object_data.islink = true;
                                        object_data.selectedValue = 1;
                                        if(objson.has("id"))
                                         object_data.imageName = String.valueOf(objson.getInt("id"));
                                        if(obj.has("url"))
                                            object_data.value = objson.getString("url");
                                    }
                                    dataValue.add(object_data);
                                }
                            }
                        }
                        objMap.values = dataValue;

                        list.add(objMap);
                    }
                }
            }
        } catch (Exception ex) {

        }


        DBHandler_Access databaseAccess = DBHandler_Access.getInstance(this);
        databaseAccess.open();
        databaseAccess.deleteDataControl();
        databaseAccess.insertControlTag(list);

        databaseAccess.close();
    }


    private void inti() {
        /*JSONObject obj = null;
        try{
            obj = new JSONObject(json);
        }catch (JSONException ex){
            ex.printStackTrace();
        }*/
        final Custom_ViewPager pager = (Custom_ViewPager) findViewById(R.id.customViewPager);
        pager.setPagingEnabled(true);
        Custom_Adapter_Register_Merchant adpter = new Custom_Adapter_Register_Merchant(this, totalScreen);
        pager.setAdapter(adpter);
        PageIndicator mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(pager);
        final Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setText("Upload");
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (swipeId <= pager.getChildCount()) {
                    pager.setCurrentItem((swipeId));
                    if (swipeId - 1 == pager.getChildCount()) {
                        btnSave.setText("Upload");
                        if (swipeId == pager.getChildCount())
                            uploadDataAndRegisterMerchant();
                        pager.setPagingEnabled(true);
                    }
                    swipeId++;
                }*/
                uploadDataAndRegisterMerchant();
            }
        });


        TextView txtHeader = (TextView) findViewById(R.id.txtHeader);
        txtHeader.setText("Register");
        ImageView imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity_Merchant_register.this.finish();
            }
        });



        /*pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/


    }

    private void uploadDataAndRegisterMerchant() {
        Globals.showAlertDialog(
                "Alert",
                "Do you want to submit details for verfication ? You can also change it later.",
                this,
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int id) {
                        imageUpload();
                    }
                }, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int id) {
                        return;
                    }
                }, false);

    }


    private void imageUpload() {
        DBHandler_Access db = DBHandler_Access.getInstance(this);
        ArrayList<Object_Control_Maping> listMap = db.getAllData();
        for (Object_Control_Maping objMap : listMap) {

            if (objMap.isrequired==1) {
                if(objMap.value!=null)
                if(objMap.value.isEmpty()) {
                    //if (objMap.typeId != Custom_Control.MultiImageID && objMap.typeId != Custom_Control.ImageViewID) {
                        Globals.showShortToast(this, objMap.label + " is empty");
                        return;
                    //}
                }
            }
        }
        Custom_ConnectionDetector cd = new Custom_ConnectionDetector(this);
        if (cd.isConnectingToInternet()) {
            try {
                DBHandler_Access dataBase = DBHandler_Access.getInstance(this);
                dataBase.open();
                final ArrayList<Object_Image_Upload> list = dataBase.getImageforUpload();

                final HashMap<String, File> imageMap = getMapImageParams(list);
                String commaSeperatedKeys = getCommaSeperatedKeys(imageMap
                        .keySet());
                pd = Globals.showLoadingDialog(pd, this, false, "");
                if (imageMap.size() != 0) {

                    Custom_VolleyImagePost jsonObjectRQST = new Custom_VolleyImagePost(
                            Custom_URLs_Params.getURL_ImageUpload(), imageMap, Custom_URLs_Params.getParams_UploadImageStringParams(this, commaSeperatedKeys),

                            new Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    Globals.hideLoadingDialog(pd);
                                    Log.i("SUSHIL", "json Response recieved !!" + response);
                                    boolean upload = imageResponce(response, list);
                                    uploadContent(upload);

                                }
                            }, new ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError err) {
                            Log.i("SUSHIL", "ERROR VolleyError "+err);
                            Globals.hideLoadingDialog(pd);
                            Globals.showShortToast(
                                    Activity_Merchant_register.this,
                                    Globals.MSG_SERVER_ERROR);

                        }
                    });

                    Custom_VolleyAppController.getInstance().addToRequestQueue(
                            jsonObjectRQST);

                } else {
                    uploadContent(true);
                }

            } catch (Exception ex) {
                Globals.hideLoadingDialog(pd);
                Globals.showShortToast(this,
                        Globals.MSG_SERVER_ERROR);
                ex.printStackTrace();
            }
        } else {
            Globals.showAlert("Error", Globals.INTERNET_ERROR, this);
        }


    }


    private boolean imageResponce(JSONObject response, ArrayList<Object_Image_Upload> list) {
        if (response == null) {
            return false;
        } else {
            try {
                if (response.has("status")) {
                    if (response.getString("status").equals("login")) {
                        DBHandler_Access data = DBHandler_Access.getInstance(this);
                        data.open();
                        if (response.has("imagedata")) {
                            JSONObject obj = response.getJSONObject("imagedata");
                            for (Object_Image_Upload objimage : list) {
                                JSONObject object = obj.getJSONObject(objimage.id + "");
                                String imageName = String.valueOf(object.getInt("id"));
                                data.updateimageName(imageName, objimage.id);
                            }
                            return true;
                        }
                    }
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        return false;

    }

    private HashMap<String, File> getMapImageParams(ArrayList<Object_Image_Upload> list) {
        HashMap<String, File> imageMap = new HashMap<String, File>();
        try {
            if (list.size() != 0) {
                for (Object_Image_Upload key : list) {
                    File file = new File(key.value);
                    imageMap.put(key.id + "", file);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imageMap;
    }


    private String getCommaSeperatedKeys(Set<String> setKeys) {
        String keysCommaSeperated = "";
        for (String key : setKeys) {
            keysCommaSeperated += key + ",";
        }
        if (keysCommaSeperated.contains(",")) {
            keysCommaSeperated = keysCommaSeperated.substring(0,
                    keysCommaSeperated.length() - 1);
        }
        Log.i("SUSHIL", "keysCommaSeperated " + keysCommaSeperated);
        return keysCommaSeperated;
    }

    private void uploadContent(boolean isUpload) {
        if (isUpload) {
            try {
                Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                        Request.Method.POST,
                        Custom_URLs_Params.getURL_SaveMerchatData(),
                        Custom_URLs_Params.getParams_MerchatRegisterData(this),
                        new Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Globals.hideLoadingDialog(pd);
                                Log.i("SUSHIL", "json Response recieved !!" + response);
                                parseResponce(response);


                            }

                        }, new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError err) {
                        Globals.hideLoadingDialog(pd);
                        Log.i("SUSHIL", "ERROR VolleyError");
                        err.getStackTrace();
                        Globals.showShortToast(
                                Activity_Merchant_register.this,
                                Globals.MSG_SERVER_ERROR);

                    }
                });

                Custom_VolleyAppController.getInstance().addToRequestQueue(
                        jsonObjectRQST);
            } catch (Exception ex) {
                Globals.hideLoadingDialog(pd);
                Globals.showShortToast(Activity_Merchant_register.this,
                        Globals.MSG_SERVER_ERROR);
            }

        }

    }

    private void parseResponce(JSONObject obj) {
         if(obj==null){
             return;
         }else{
           if(obj.has("success")){
               Globals.showShortToast(this,"Data Uploaded Successfully ! Wait for Approval.");
               this.finish();
           }
         }
    }

   /* private void openDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Register");
        dialog.setContentView(R.layout.custom_dailog_upload);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        Button btnNo = (Button)dialog.getWindow().findViewById(R.id.btnNo);
        Button btnyes = (Button)dialog.getWindow().findViewById(R.id.btnYes);

        btnNo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnyes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                pd = Globals.showLoadingDialog(pd, Activity_Merchant_register.this, true, "");
            }
        });

        dialog.show();
    }*/


    private JSONObject creatJson() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("mainContainer", LINEARLAYOUT_VERTICAL_ID);
            JSONArray objArray = new JSONArray();
            for (int i = 0; i < 5; i++) {
                JSONObject objSubContainer = new JSONObject();
                objSubContainer.put("Container", LINEARLAYOUT_HORIZONTAL_ID);
                JSONArray array = new JSONArray();
                array.put(0, 2);
                array.put(1, 1);
                objSubContainer.put("subArray", array);
                objArray.put(i, objSubContainer);
            }
            obj.put("subContainer", objArray);
            // obj.put("subContainer",LINEARLAYOUT_HORIZONTAL_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");

                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // ivImage.setImageBitmap(thumbnail);
               /* ImageView img = Custom_Control.getImageView(this, 150, 150, 1);
                img.setImageBitmap(thumbnail);
                Cus
                linearLayout.addView(img);*/
                /*String filepath = "";
                try {
                    filepath = resizeImage(destination.getAbsolutePath(), 400);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                Custom_Adapter_Register_Merchant.imgSelected.setImageBitmap(thumbnail);
               // if (!filepath.isEmpty())
                    imageinsertIntoDatabase(destination.getAbsolutePath());
                //else
                  //  imageinsertIntoDatabase(filepath);


            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null,
                        null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();

                String selectedImagePath = cursor.getString(column_index);

                Bitmap bm;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeFile(selectedImagePath, options);
                /*try {
                    selectedImagePath = resizeImage(selectedImagePath, 400);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                Custom_Adapter_Register_Merchant.imgSelected.setImageBitmap(bm);
                imageinsertIntoDatabase(selectedImagePath);
               /* ImageView img = Custom_Control.getImageView(this, 150, 150, 1);
                img.setImageBitmap(bm);
                linearLayout.addView(img);*/
            }
        }
    }

    private void imageinsertIntoDatabase(String filepath) {

        DBHandler_Access databaseAccess = DBHandler_Access.getInstance(this);
        databaseAccess.open();
        if (Custom_Adapter_Register_Merchant.isInsert) {
            int id = databaseAccess.insertdataNew(filepath, Custom_Adapter_Register_Merchant.controlId);
            if (id != 0) {
                Custom_Adapter_Register_Merchant.imgSelected.setTag(id);
            }
            Log.i("SUSHIL", "is new insert");
        } else {

            databaseAccess.insertdataWithChange(filepath, Custom_Adapter_Register_Merchant.dataId);
            Log.i("SUSHIL", "is new update");
        }
    }

   /* private void createControl(JSONObject obj) {
        LinearLayout scrollLinear = (LinearLayout) findViewById(R.id.scrollLinear);
        if (obj != null) {
            try {
                if (obj.has("mainContainer")) {
                    int i = obj.getInt("mainContainer");
                    if (i == LINEARLAYOUT_VERTICAL_ID) {
                        LinearLayout linearmain = new LinearLayout(this);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                                , LinearLayout.LayoutParams.MATCH_PARENT);
                        linearmain.setLayoutParams(layoutParams);
                        linearmain.setOrientation(LinearLayout.VERTICAL);
                        if (obj.has("subContainer")) {
                            JSONArray arraySub = obj.getJSONArray("subContainer");
                            if (arraySub != null) {
                                for (int j = 0; j < arraySub.length(); j++) {
                                    JSONObject objSub = arraySub.getJSONObject(j);
                                    if (objSub != null) {
                                        if (objSub.has("Container")) {

                                            int cintainerId = objSub.getInt("Container");
                                            if (cintainerId == LINEARLAYOUT_HORIZONTAL_ID) {
                                                LinearLayout layoutSub = new LinearLayout(this);
                                                LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                                                        , LinearLayout.LayoutParams.WRAP_CONTENT);
                                                layoutSub.setLayoutParams(layoutparams);
                                                layoutSub.setOrientation(LinearLayout.HORIZONTAL);
                                                if (objSub.has("subArray")) {
                                                    JSONArray ArraySub = objSub.getJSONArray("subArray");
                                                    for (int d = 0; d < ArraySub.length(); d++) {
                                                        int h = ArraySub.getInt(d);
                                                        if (h == TEXTVIEW_ID) {
                                                            TextView txt = new TextView(this);
                                                            LinearLayout.LayoutParams lpTxt = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT
                                                                    , LinearLayout.LayoutParams.WRAP_CONTENT, 1);
                                                            txt.setLayoutParams(lpTxt);
                                                            txt.setText("Name");
                                                            lpTxt.setMargins(10, 5, 0, 0);
                                                            layoutSub.addView(txt);
                                                        } else if (h == EditText_ID) {
                                                            EditText edt = new EditText(this);
                                                            LinearLayout.LayoutParams lpEdt = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT
                                                                    , LinearLayout.LayoutParams.WRAP_CONTENT, 1);
                                                            lpEdt.setMargins(0, 5, 10, 0);
                                                            edt.setLayoutParams(lpEdt);
                                                            layoutSub.addView(edt);
                                                        }
                                                    }
                                                    linearmain.addView(layoutSub);
                                                }
                                            } else if (cintainerId == LINEARLAYOUT_VERTICAL_ID) {

                                            }
                                        }
                                    }
                                }
                            }
                            //btn alert
                            Button btn = new Button(this);
                            LinearLayout.LayoutParams lpBtn = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT
                                    , LinearLayout.LayoutParams.WRAP_CONTENT);
                            btn.setLayoutParams(lpBtn);
                            btn.setText("Alert");
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    openDialog();
                                }
                            });
                            linearmain.addView(btn);

                            //btn notification
                            Button btnNoti = new Button(this);
                            LinearLayout.LayoutParams lpBtnNoti = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT
                                    , LinearLayout.LayoutParams.WRAP_CONTENT);
                            btnNoti.setLayoutParams(lpBtnNoti);
                            btnNoti.setText("Notification");
                            btnNoti.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                   // openNoti();
                                }
                            });
                            linearmain.addView(btnNoti);
                            scrollLinear.addView(linearmain);
                        }
                    } else if (i == LINEARLAYOUT_HORIZONTAL_ID) {

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private void openDialog(){
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setTitle("Animation Dialog");
        dialog.setContentView(R.layout.dailog_layout);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        Button btnDismiss = (Button)dialog.getWindow().findViewById(R.id.dismiss);

        btnDismiss.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }*/




   /* private void openNoti(){
       Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setTicker("Android Image downloaded.");
        builder.setContentTitle("Android Image downloaded.");
        builder.setContentText("View in full screen mode");
        builder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(),R.mipmap.ic_launcher));
        builder.setAutoCancel(true);

        Notification.BigPictureStyle bigPicutureStyle = new Notification.BigPictureStyle(builder);
        bigPicutureStyle.bigLargeIcon(BitmapFactory.decodeResource(this.getResources(),R.mipmap.ic_launcher));
        bigPicutureStyle.bigPicture(BitmapFactory.decodeResource(this.getResources(),R.mipmap.abc));
        bigPicutureStyle.setBigContentTitle("Android Image downloaded.");
        bigPicutureStyle.setSummaryText("Click on the image for full screen preview");

        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(01, bigPicutureStyle.build());
    }*/


   /* private File saveShort(Bitmap bitmap,int scaleWidth){

        Bitmap photo = (Bitmap) bitmap;
        //Bitmap.createScaledBitmap(photo, 400, photo.getHeight(), false);
        photo = Globals.scaleToWidth(photo, scaleWidth);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 70, bytes);

        File f = Custom_adapter_RegisterDoctor.getOutputPhotoFile(this);

        try {
            f.createNewFile();
            FileOutputStream fo = null;
            fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            fo.close();
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return f;
    }*/

    /*public String resizeImage(String filepath, int scaledWidth) throws IOException {

        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filepath, bounds);

        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(filepath, opts);
        File file1 = new File(filepath);
        int rotationAngle = getCameraPhotoOrientation(this, Uri.fromFile(file1), file1.toString());

        Matrix matrix = new Matrix();

        matrix.postRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);

	    *//*if(rotationAngle==270)
            matrix.postRotate(0, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
		else if(rotationAngle == 0)
			matrix.postRotate(0, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
		else if(rotationAngle == 90)
			matrix.postRotate(-90, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);*//*



	  *//*if(rotationAngle==0)
        matrix.postRotate(90, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
	  else if(rotationAngle == 90)
		matrix.postRotate(-90, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
	  else if(rotationAngle == -90)
		matrix.postRotate(-270, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);*//*

        int bitmapHeight = bm.getHeight();
        int bitmapWidth = bm.getWidth();

        // scale According to WIDTH
        int scaledHeight = (scaledWidth * bitmapHeight) / bitmapWidth;

        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, scaledWidth, scaledHeight, matrix, true);
        FileOutputStream fos = new FileOutputStream(filepath);
        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        fos.flush();
        fos.close();

        return filepath;
    }

    public static int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath) {
        int rotate = 0;
        try {
            context.getContentResolver().notifyChange(imageUri, null);
            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            switch (orientation) {
                case ExifInterface.ORIENTATION_NORMAL:
                    rotate = 0;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("SUSHIL", "ratate is image angle " + rotate);
        return rotate;
    }*/


}




   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_register);
        //setUpFragment();
    }

   *//* void setUpFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Custom_Sliding_fragment fragment = new Custom_Sliding_fragment();
        transaction.replace(R.id.sample_content_fragment, fragment);
        transaction.commit();
    }*//*
}*/
