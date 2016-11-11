package com.iseva.app.source;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.utils.FileUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Activity_Post_Resume extends Activity {


    private Spinner spinnerCity;
    private ProgressDialog pd;
    private int cityID = -1;
    private int categoryID = -1;
    private boolean isUploadResume = false;
    private int resumeName = -1;
    private File file = null;
    //profile image variable
    private ImageView img;
    public int REQUEST_CAMERA = 2;
    public int SELECT_FILE = 3;
    private String path = "";
    private int key = 1;
    private int imageId = -1;
    //**********

    private TextView txtAttacheds;
    private EditText edtFName;
    private EditText edtGender;
    private EditText edtAge;

    private EditText edtQuali;
    //  EditText edtNumber = (EditText) findViewById(R.id.edtMobNumber);
    //  EditText edtEmail = (EditText) findViewById(R.id.edtEmails);
    private EditText edtAddress;

    private EditText edtExperi;
    private EditText edtCurrentJob;
    private EditText edtCurrentSalary;
    private EditText edtOthers;
    private Spinner spinCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_resume);

        init();
    }

    private void init() {
        setFooterAndHeader(R.id.imgBtnFooterPostResume,"Post Resume");
        spinnerCity = (Spinner) findViewById(R.id.spinReCity);
        spinCategory = (Spinner)findViewById(R.id.spinCate);

         edtFName = (EditText) findViewById(R.id.edtFName);
        edtGender = (EditText) findViewById(R.id.edtGender);
        edtAge = (EditText) findViewById(R.id.edtDOB);

        edtQuali = (EditText) findViewById(R.id.edtQualification);
        //  EditText edtNumber = (EditText) findViewById(R.id.edtMobNumber);
        //  EditText edtEmail = (EditText) findViewById(R.id.edtEmails);
        edtAddress = (EditText) findViewById(R.id.edtAddress);

         edtExperi = (EditText) findViewById(R.id.edtExPerience);
         edtCurrentJob = (EditText) findViewById(R.id.edtCurrentJob);
        edtCurrentSalary = (EditText) findViewById(R.id.edtCurrentSalary);
        edtOthers = (EditText) findViewById(R.id.edtOthers);


        Button btnAttach = (Button)findViewById(R.id.btnAttach);
        btnAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachResume();
            }
        });
        ImageView imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getCateCityData();
        txtAttacheds = (TextView)findViewById(R.id.txtAttacheds);
        Button btnUpload = (Button)findViewById(R.id.btnResumeSubmit);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globals.showAlertDialog(
                        "Alert",
                        "Please check all data carefully you have entered, you won't  be able to change it later !",
                        Activity_Post_Resume.this,
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                upload(file);
                            }
                        }, "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                return;
                            }
                        }, false);
            }
        });


        img = (ImageView)findViewById(R.id.imgProfile);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
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
                                Activity_Post_Resume.this,
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
        spinnerCity.setAdapter(dataAdapter);
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {
                if(position!=0)
                    cityID = cityList.get(position-1).cityId;

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

    }

    private void setCateSpinner(final ArrayList<Object_Category> List) {

        ArrayList<String> listName = new ArrayList<>();
        int i = -1;
        for (Object_Category c : List) {
            i++;
            if(i==0){
                listName.add("Select Category");
            }
            listName.add(c.name);
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
                    categoryID = List.get(position-1).catId;

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

    }

    private void setFooterAndHeader(int footerbtnId,String headerText){
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

        TextView txtH = (TextView)this.findViewById(R.id.txtHeader);
        if(txtH!=null)
            txtH.setText(headerText);

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
    private void attachResume(){

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            try {
                startActivityForResult(
                        Intent.createChooser(intent, "Select a File to Upload"),
                        1);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "Please install a File Manager.",
                        Toast.LENGTH_SHORT).show();
            }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                   // Log.d(TAG, "File Uri: " + uri.toString());
                    // Get the path

                    file = new File(getPath(this,uri));
                    if(file!=null){
                        txtAttacheds.setText("Resume are Attached.");
                    }else{
                        txtAttacheds.setText("");
                    }
                   // Log.i("SUSHIL","path "+getPath(this,uri));

                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
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

                    Log.i("SUSHIL","sushil image path "+destination.getPath());

                    imageId = -1;
                    img.setImageBitmap(BitmapFactory.decodeFile(destination.getPath()));
                    path = destination.getAbsolutePath();

                }
                break;
            case 3:
                 if (resultCode == RESULT_OK) {
                Uri selectedImageUri = data.getData();
                // Log.i("SUSHIL","selected path ............................................."+selectedImageUri.getPath());
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null,
                        null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();

                String selectedImagePath = cursor.getString(column_index);

                Bitmap bm;
                bm = BitmapFactory.decodeFile(selectedImagePath);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                FileOutputStream fo;
                try {
                    //destination.createNewFile();
                    fo = new FileOutputStream(selectedImagePath);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    rotateImage(selectedImagePath);
                    // selectedImagePath =  decodeFile(selectedImagePath,500,500);
                } catch (Exception e){
                    // Globals.showShortToast(this,"IOException");
                    e.printStackTrace();
                }


                Log.i("SUSHIL","sushil image path "+selectedImagePath);

                imageId = -1;
                img.setImageBitmap(BitmapFactory.decodeFile(selectedImagePath));
                path = selectedImagePath;

            }
                break;
        }


        }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }



//    private String getRealPathFromURI(Uri contentURI) {
//        String result;
//        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
//        if (cursor == null) { // Source is Dropbox or other similar local file path
//            result = contentURI.getPath();
//        } else {
//            cursor.moveToFirst();
//            int idx = cursor.getColumnIndex(MediaStore.Files.ImageColumns.DATA);
//            result = cursor.getString(idx);
//            cursor.close();
//        }
//        return result;
//    }

    private void upload(final File file) {

        if(path.equals("")){
            Globals.showShortToast(this,"Select your Profile Image");
        }
         if (edtFName.getText().toString().equals("")) {
             Toast.makeText(this, "Enter your Father's Name", Toast.LENGTH_SHORT).show();
             return;
         }
         if (edtQuali.getText().toString().equals("")) {
             Toast.makeText(this, "Enter your Qualification", Toast.LENGTH_SHORT).show();
             return;
         }

         if (edtAddress.getText().toString().equals("")) {
             Toast.makeText(this, "Enter your Address", Toast.LENGTH_SHORT).show();
             return;
         }
         if (cityID == -1) {
             Toast.makeText(this, "Choose your City", Toast.LENGTH_SHORT).show();
             return;
         }
        if (categoryID == -1) {
            Toast.makeText(this, "Choose your Category", Toast.LENGTH_SHORT).show();
            return;
        }
         if (file == null) {
             Toast.makeText(this, "Choose a Resume file", Toast.LENGTH_SHORT).show();
             return;
         }

         //******upload *******//
          uploadResume(file);



     }



    private void uploadData(){
        try {
            Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                    Request.Method.POST,
                    Custom_URLs_Params.getURL_UploadData(),
                    Custom_URLs_Params.getParams_UploadResume(this,imageId,edtFName.getText().toString(),edtAge.getText().toString(),edtGender.getText().toString(),edtAddress.getText().toString(),
                            edtQuali.getText().toString(),edtExperi.getText().toString(),edtCurrentJob.getText().toString(),edtCurrentSalary.getText().toString(),edtOthers.getText().toString(),resumeName,cityID,categoryID), new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.i("SUSHIL", "json Response recieved !!"+response);
                    Globals.hideLoadingDialog(pd);
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
        } catch (Exception ex) {

            ex.printStackTrace();

        }
    }
    private void contentResponce(JSONObject obj){
         if(obj==null){
             return;
         }else{
             try{
                 if(obj.has("success")){
                     if(obj.getInt("success")==1){
                         Globals.showShortToast(this,"Your Resume Succesfully Upload.");
                         finish();
                        /* LinearLayout linear = (LinearLayout)findViewById(R.id.linear);
                         Globals.clearForm(linear);*/
                     }
                 }
             }catch (JSONException ex){
                 ex.printStackTrace();
             }
         }
    }
    private boolean uploadResume(File file){

        Custom_ConnectionDetector cd = new Custom_ConnectionDetector(this);
        if (cd.isConnectingToInternet()) {
            try {

                final HashMap<String, File> map = new HashMap<>();
                map.put("file",file);
                String commaSeperatedKeys = getCommaSeperatedKeys(map
                        .keySet());
                pd = Globals.showLoadingDialog(pd, this, false, "");
                if (map.size() != 0) {

                    Custom_VolleyImagePost jsonObjectRQST = new Custom_VolleyImagePost(
                            Custom_URLs_Params.getURL_UploadResumeFile(), map,
                            Custom_URLs_Params
                                    .getParams_UploadImageStringParams(this, commaSeperatedKeys),
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    Globals.hideLoadingDialog(pd);
                                    Log.i("SUSHIL", "json Response recieved !!" + response);
                                     parseResumeResponse(response);
                                    uploadProfileImage();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError err) {
                            Log.i("SUSHIL", "ERROR VolleyError");
                            Globals.hideLoadingDialog(pd);
                            Globals.showShortToast(
                                    Activity_Post_Resume.this,
                                    Globals.MSG_SERVER_ERROR);

                        }
                    });

                    Custom_VolleyAppController.getInstance().addToRequestQueue(
                            jsonObjectRQST);


                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            Globals.showShortToast(this,Globals.INTERNET_ERROR);
        }
        return isUploadResume;
    }


    private void uploadProfileImage(){
        Custom_ConnectionDetector cd = new Custom_ConnectionDetector(this);
        if (cd.isConnectingToInternet()) {
            try {
                final HashMap<String, File> map = new HashMap<>();
                map.put("1",new File(path));
                String commaSeperatedKeys = getCommaSeperatedKeys(map
                        .keySet());
                Log.i("SUSHIL","map image "+map);
                //pd = Globals.showLoadingDialog(pd, this, false, "");
                if (map.size() != 0) {

                    Custom_VolleyImagePost jsonObjectRQST = new Custom_VolleyImagePost(
                            Custom_URLs_Params.getURL_ImageUpload(), map,
                            Custom_URLs_Params
                                    .getParams_UploadImageStringParams(this, commaSeperatedKeys),
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                   // Globals.hideLoadingDialog(pd);
                                    Log.i("SUSHIL", "json Response recieved !!" + response);
                                    parseProfileResponse(response);
                                    uploadData();

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError err) {
                            Log.i("SUSHIL", "ERROR VolleyError");
                            Globals.hideLoadingDialog(pd);
                            Globals.showShortToast(
                                    Activity_Post_Resume.this,
                                    Globals.MSG_SERVER_ERROR);

                        }
                    });

                    Custom_VolleyAppController.getInstance().addToRequestQueue(
                            jsonObjectRQST);


                }
            } catch (Exception e) {
               e.printStackTrace();
                Globals.showShortToast(this,Globals.MSG_SERVER_ERROR);
            }
        }
    }

    private boolean parseProfileResponse(JSONObject obj){
        if(obj==null){
            return false;
        }else {
            try {
                if (obj.has("imagedata")) {
                    JSONObject obData = obj.getJSONObject("imagedata");
                    if(obData.has("1")){
                        JSONObject object = obData.getJSONObject("1");
                        imageId = object.getInt("id");
                        return true;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;

    }

    private boolean parseResumeResponse(JSONObject obj){
        if(obj==null){
            return false;
        }else{
            try {
                if(obj.has("data")){
                    //resumeName =   obj.getString("file");
                    JSONObject obData = obj.getJSONObject("data");
                    if(obData.has("file")){
                        JSONObject object = obData.getJSONObject("file");
                        resumeName = object.getInt("id");
                        Log.i("SUSHIL","id resume "+resumeName);
                        isUploadResume = true;
                    }

                }

            }catch (JSONException ex){
                ex.printStackTrace();
                return false;
            }
        }
        return false;
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






    //pick profile image

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                }
            }
        });
        builder.show();
    }


    public String rotateImage(String filepath) throws IOException{

        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filepath, bounds);

        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(filepath, opts);
        File file1 = new File(filepath);

        int rotationAngle = getCameraPhotoOrientation(this, Uri.fromFile(file1), file1.toString());

        Matrix matrix = new Matrix();

        matrix.postRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);

	   /* *//**//*if(rotationAngle==270)
		    matrix.postRotate(0, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
		else if(rotationAngle == 0)
			matrix.postRotate(0, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
		else if(rotationAngle == 90)
			matrix.postRotate(-90, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);*//**//**/



	  /**//*if(rotationAngle==0)
	    matrix.postRotate(90, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
	  else if(rotationAngle == 90)
		matrix.postRotate(-90, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
	  else if(rotationAngle == -90)
		matrix.postRotate(-270, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);*//**//*
        int bitmapHeight = bm.getHeight();
        int bitmapWidth = bm.getWidth();*/

        // scale According to WIDTH
        // int scaledHeight = (scaledWidth * bitmapHeight) / bitmapWidth;
        // int scaledHeight = (scaledWidth * bounds.outHeight) / bounds.outWidth;
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
        FileOutputStream fos=new FileOutputStream(filepath);
        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 60, fos);
        fos.flush();
        fos.close();

        return filepath;
    }

    public static int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath){
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
        Log.i("SUSHIL", "ratate is image angle "+rotate);
        return rotate;
    }
}