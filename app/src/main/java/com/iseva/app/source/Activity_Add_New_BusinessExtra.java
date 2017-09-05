package com.iseva.app.source;

import android.*;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;


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
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Activity_Add_New_BusinessExtra extends Activity {

    private int REQUEST_CAMERA = 1;
    private int SELECT_FILE = 2;
    private ProgressDialog pd;
    private HashMap<Integer, String> offersImages;
    public int imgid = 2;
    private boolean isImageViewSelected = false;
    private ImageView selectedImageView;
    private boolean doubleBackToExitPressedOnce = false;
    private HashMap<Integer,Integer> imageid;
    private ArrayList<Object_City> cityList;
    private int citySelect = -1;
    private int businessExtraId = 0;
   // ImageView imag;
    //private boolean selectingMainImage = false;
    private boolean mainImageAdded = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_offers);
        inti();
       // getRemainingOffers();
    }

    private void inti() {


        offersImages = new HashMap<>();
        imageid = new HashMap<>();
        Button btnUploadOffre = (Button) findViewById(R.id.btnUpkoadOffers);
        Button btnSelect = (Button) findViewById(R.id.btnSelect);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearImage);
                if (Globals.IMAGELIMIT > linearLayout.getChildCount()) {
                    if(offersImages.size()!=0) {
                        mainImageAdded = false;
                        selectImage(false, 0, null);
                    }
                    else
                        Globals.showShortToast(Activity_Add_New_BusinessExtra.this,"Main image added first");
                    //alertChooseTwo(ViewImages);
                } else {
                    Globals.showShortToast(
                            Activity_Add_New_BusinessExtra.this,
                            "You can upload maximum "
                                    + Globals.IMAGELIMIT
                                    + " images only.");
                }

            }
        });
       /* imag = (ImageView)findViewById(R.id.OffersImage);
        imag.setClickable(true);
        imag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(false,0,null);
            }
        });*/


        btnUploadOffre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });



        ImageView imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity_Add_New_BusinessExtra.this.finish();
            }
        });

        ImageView imgMain = (ImageView)findViewById(R.id.imgMain);
        imgMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainImageAdded = true;
                selectImage(false,0,null);

            }
        });

        /*Intent i = getIntent();
        String type = i.getStringExtra("type");
        if(type.equals("edit"))
           getofferswithid();*/
        Intent intent =  getIntent();
        try{
           JSONObject obj = new JSONObject(intent.getStringExtra("city"));
            if(obj.has("city")){
                cityData(obj.getJSONArray("city"));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        TextView txtHeader = (TextView) findViewById(R.id.txtHeader);
        txtHeader.setText("Post "+intent.getStringExtra("extraName"));
        businessExtraId = intent.getIntExtra("extraId",0);
    }



    private void cityData(JSONArray city) {
        try {
            if (city.length() != 0) {
                cityList = new ArrayList<>();
                for (int i = 0; i < city.length(); i++) {
                    JSONObject obj = city.getJSONObject(i);
                    Object_City objCity = new Object_City();
                    if (obj.has("id")) {
                        objCity.cityId = obj.getInt("id");
                    }
                    if (obj.has("name")) {
                        objCity.name = obj.getString("name");
                    }
                    /* if (obj.has("isselect")){
                         objCity.seLectedCity =
                     }*/
                    cityList.add(objCity);
                }
                setCitySpinner();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setCitySpinner() {
        Spinner spinCity = (Spinner) findViewById(R.id.spinnerCity);
        ArrayList<String> listName = new ArrayList<>();
        for (Object_City city : cityList) {
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
                 citySelect = cityList.get(position).cityId;

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        //spinCity.setSelection(selectedCityID);
    }

   /* private void getofferswithid(){
        Custom_ConnectionDetector cd = new Custom_ConnectionDetector(this);
        if (!cd.isConnectingToInternet()) {
            Globals.showAlert("ERROR", Globals.INTERNET_ERROR, this);
        } else {

            try {
                pd = Globals.showLoadingDialog(pd, this, false, "");
                Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                        Request.Method.POST,
                        Custom_URLs_Params.getURL_NotificationRemaining(),
                        Custom_URLs_Params.getParams_RemainingNoti(this), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("SUSHIL", "json Response recieved !!");
                        Globals.hideLoadingDialog(pd);
                        //parseCateResponse(response);
                        parseOffers(response);
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


    private void parseOffers(JSONObject obj){
        if(obj==null){
            return;
        }else {
            try {

                if (obj.has("offers")) {
                    JSONArray array = obj.getJSONArray("offers");
                    if (array != null) {
                        if (array.length() != 0) {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                if (object != null) {
                                    Object_Offers objOffers = new Object_Offers();
                                    if (object.has("id")) {
                                        objOffers.id = object.getInt("id");
                                    }
                                    if (object.has("heading")) {
                                         objOffers.heading = object.getString("heading");
                                        EditText txtHeading = (EditText)findViewById(R.id.txtOffersHeading);
                                        txtHeading.setText(objOffers.heading);

                                    }
                                    if (object.has("address")) {
                                        objOffers.content = object.getString("content");
                                        EditText txtContent = (EditText)findViewById(R.id.txtOffersContent);
                                        txtContent.setText(objOffers.content);
                                    }
                                    if (object.has("image")) {
                                        JSONArray imageArray = object.getJSONArray("image");
                                       // ArrayList<String> listImage = new ArrayList<>();
                                        for (int j = 0; j < imageArray.length(); j++) {
                                            JSONObject objectImage = imageArray.getJSONObject(j);
                                            if (objectImage.has("id")) {

                                              if (objectImage.has("url")) {
                                                    if (!objectImage.getString("url").isEmpty()) {
                                                        //listImage.add(url);
                                                        setImageViewBitmap(true, objectImage.getString("url"),objectImage.getInt("id"));

                                                    }
                                                }
                                            }
                                        }
                                        //objOffers.offersimage = listImage;

                                    }

                                }
                            }

                        }
                    }
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }*/


    /*private void editOffers(){

    }*/

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            navi();
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

    private void navi(){
        Intent i = new Intent(this,Activity_BusinessExtra_Type.class);
        startActivity(i);
    }

    private void selectImage(final boolean isRemove, final int keyRemove, final LinearLayout linear) {



        Dexter.withActivity(this)
                .withPermissions(
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {


            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {

                final CharSequence[] items = {"Take Photo", "Choose from Library"};
                final CharSequence[] itemsRemove = {"Take Photo", "Choose from Library","Remove"};
                AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Add_New_BusinessExtra.this);
                builder.setTitle("Add Photo!");

                if(!isRemove) {
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
                }else{
                    builder.setItems(itemsRemove, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (itemsRemove[item].equals("Take Photo")) {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, REQUEST_CAMERA);
                            } else if (itemsRemove[item].equals("Choose from Library")) {
                                Intent intent = new Intent(
                                        Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                intent.setType("image/*");
                                startActivityForResult(
                                        Intent.createChooser(intent, "Select File"),
                                        SELECT_FILE);
                            }else if(itemsRemove[item].equals("Remove")){
                                linear.removeView(selectedImageView);
                                offersImages.remove(keyRemove);
                                try{
                                    imageid.remove(keyRemove);
                                }catch (Exception e){

                                }
                            }
                        }
                    });
                    builder.show();
                }

            }
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {




            }

        }).check();


    }

    private void openDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Upload");
        dialog.setContentView(R.layout.custom_dailog_upload);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        Button btnNo = (Button) dialog.getWindow().findViewById(R.id.btnNo);
        Button btnyes = (Button) dialog.getWindow().findViewById(R.id.btnYes);

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
                // pd = Globals.showLoadingDialog(pd, Activity_Add_New_BusinessExtra.this, true, "Uploading Offers");
                imageUpload();
            }
        });

        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            isImageViewSelected = false;
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

                /*if(tempSelectingMainImage){
                    setMainImageBitmap(destination.getPath());
                }else{*/
                   // offersImages.put((int)selectedImageView.getTag(), destination.getPath());
                    //imag.setImageBitmap(thumbnail);
                if(!mainImageAdded)
                    setImageViewBitmap(false, destination.getPath(),0);
                else {
                    offersImages.put(1, destination.getPath());
                    ImageView imagMain = (ImageView)findViewById(R.id.imgMain);
                    imagMain.setImageBitmap(BitmapFactory.decodeFile(destination.getPath()));
                }

                // ivImage.setImageBitmap(thumbnail);
                /*ImageView img = Custom_Control.getImageView(this, 150, 150, 1);
                img.setImageBitmap(thumbnail);
                //img.setId(R.id.imageViewId);
                img.setTag(imgid);
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectImage();
                    }
                });
                offersImages.put((int) img.getTag(), destination.getAbsolutePath());
                imgid++;
                Log.i("SUSHIL", "offers hash map is " + offersImages);
                linearLayout.addView(img);*/
                mainImageAdded =false;

            } else if (requestCode == SELECT_FILE) {
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
               /* BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;*/
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
                if(!mainImageAdded)
                    setImageViewBitmap(false, selectedImagePath,0);
                else {
                    offersImages.put(1, selectedImagePath);
                    ImageView imagMain = (ImageView)findViewById(R.id.imgMain);
                    imagMain.setImageBitmap(BitmapFactory.decodeFile(selectedImagePath));
                }

                mainImageAdded =false;
            }
        }
    }

    /*private void setMainImageBitmap(String selectedImagePath){

        ImageView img = (ImageView)findViewById(R.id.imgMain);
        img.setImageBitmap(BitmapFactory.decodeFile(selectedImagePath));
        mainImageAdded = true;
    }*/
    private void setImageViewBitmap(boolean islink, String selectedImagePath,int id) {
        if (!isImageViewSelected) {
            final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearImage);
            final ImageView img = Custom_Control.getImageView(this, 150, 150, 1);
            img.setTag(imgid);
            if(!islink) {
                img.setImageBitmap(BitmapFactory.decodeFile(selectedImagePath));
                offersImages.put((int) img.getTag(), selectedImagePath);
            }
            else {
                Globals.loadImageIntoImageView(img, selectedImagePath, this);
                //offersImages.put((int) img.getTag(), String.valueOf(id));
                imageid.put((int) img.getTag(),id);
            }
            //img.setId(R.id.imageViewId);



            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainImageAdded = false;
                    isImageViewSelected = true;
                    selectedImageView = img;
                    selectImage(true,(int)img.getTag(),linearLayout);
                }
            });
            imgid++;
            linearLayout.addView(img);
        } else {
            if (selectedImageView != null) {
                selectedImageView.setImageBitmap(BitmapFactory.decodeFile(selectedImagePath));
                offersImages.put((int) selectedImageView.getTag(), selectedImagePath);
            }
        }
        Log.i("SUSHIL", "offers hash map is " + offersImages);
    }




   /* private void getRemainingOffers() {
        Custom_ConnectionDetector cd = new Custom_ConnectionDetector(this);
        if (!cd.isConnectingToInternet()) {
            Globals.showAlert("ERROR", Globals.INTERNET_ERROR, this);
        } else {

            try {
                pd = Globals.showLoadingDialog(pd, this, false, "");
                Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                        Request.Method.POST,
                        Custom_URLs_Params.getURL_AddNewBusinessExtra(),
                        Custom_URLs_Params.getParams_RemainingNoti(this), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("SUSHIL", "json Response recieved !!");
                        Globals.hideLoadingDialog(pd);
                        //parseCateResponse(response);
                        parseRemainOffers(response);
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

    private void parseRemainOffers(JSONObject obj) {
        if (obj == null) {
            return;
        } else {
            try {
                if (obj.has("remain")) {
                    int remainNoti = obj.getInt("remain");
                    if (remainNoti == 0) {
                        Globals.showAlertDialog("Buy", "Do you want to buy Offers ?",
                                this, "Buy", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        Intent i = new Intent(Activity_Add_New_BusinessExtra.this, Activity_Buy_Zone.class);
                                        startActivity(i);

                                    }
                                }, "Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        Activity_Add_New_BusinessExtra.this.finish();

                                    }
                                }, false);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }*/

  /*  private void  mainImageUpload(){
        Custom_ConnectionDetector cd = new Custom_ConnectionDetector(this);
        if (cd.isConnectingToInternet()) {
            try {

                final HashMap<String, File> imageMap = getMapImageParams();
                String commaSeperatedKeys = getCommaSeperatedKeys(imageMap
                        .keySet());
                pd = Globals.showLoadingDialog(pd, this, false, "");
                if (imageMap.size() != 0) {

                    Custom_VolleyImagePost jsonObjectRQST = new Custom_VolleyImagePost(
                            Custom_URLs_Params.getURL_ImageUpload(), imageMap,
                            Custom_URLs_Params
                                    .getParams_UploadImageStringParams(this, commaSeperatedKeys),
                            new Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    Globals.hideLoadingDialog(pd);
                                    Log.i("SUSHIL", "json Response recieved !!" + response);
                                    boolean upload = imageuploadResponce(response,offersImages);

                                }
                            }, new ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError err) {
                            Log.i("SUSHIL", "ERROR VolleyError");
                            Globals.hideLoadingDialog(pd);
                            Globals.showShortToast(
                                    Activity_Add_New_BusinessExtra.this,
                                    Globals.MSG_SERVER_ERROR);

                        }
                    });

                    Custom_VolleyAppController.getInstance().addToRequestQueue(
                            jsonObjectRQST);

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
    }*/

   private void imageUpload() {

       final EditText edtHeading = (EditText)findViewById(R.id.txtOffersHeading);
        final EditText edtContent = (EditText)findViewById(R.id.txtOffersContent);
       if(edtHeading.getText().toString().isEmpty()){
           Globals.showShortToast(this,"Heading is missing.");
           return;
       }
       if(edtContent.getText().toString().isEmpty()){
           Globals.showShortToast(this,"Content is missing.");
           return;
       }
       if(offersImages.size()==0){
           Globals.showShortToast(this,"Add image");
           return;
       }

      // if(!mainImageAdded){
          // Globals.showShortToast(this,"Add main image.");
           //return;
      // }

       //mainImageUpload();
        Custom_ConnectionDetector cd = new Custom_ConnectionDetector(this);
        if (cd.isConnectingToInternet()) {
            try {

                final HashMap<String, File> imageMap = getMapImageParams();
                String commaSeperatedKeys = getCommaSeperatedKeys(imageMap
                        .keySet());
                pd = Globals.showLoadingDialog(pd, this, false, "");
                if (imageMap.size() != 0) {

                    Custom_VolleyImagePost jsonObjectRQST = new Custom_VolleyImagePost(
                            Custom_URLs_Params.getURL_ImageUpload(), imageMap,
                            Custom_URLs_Params
                                    .getParams_UploadImageStringParams(this, commaSeperatedKeys),
                            new Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    Globals.hideLoadingDialog(pd);
                                    Log.i("SUSHIL", "json Response recieved !!" + response);
                                     boolean upload = imageuploadResponce(response,offersImages);
                                     uploadata(upload,edtHeading.getText().toString(),edtContent.getText().toString());

                                }
                            }, new ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError err) {
                            Log.i("SUSHIL", "ERROR VolleyError");
                            Globals.hideLoadingDialog(pd);
                            Globals.showShortToast(
                                    Activity_Add_New_BusinessExtra.this,
                                    Globals.MSG_SERVER_ERROR);

                        }
                    });

                    Custom_VolleyAppController.getInstance().addToRequestQueue(
                            jsonObjectRQST);

                } else {
                    uploadata(true,edtHeading.getText().toString(),edtContent.getText().toString());
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

    private HashMap<String, File> getMapImageParams() {
        HashMap<String, File> imageMap = new HashMap<String, File>();
        try {
            if (offersImages.size() != 0) {
                for (Integer key : offersImages.keySet()) {
                    File file = new File(offersImages.get(key));
                    imageMap.put(key + "", file);
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


     private boolean imageuploadResponce(JSONObject response ,HashMap<Integer,String> offers) {
        if (response != null) {
            try {
                if (response.has("status")) {
                    if (response.getString("status").equals("login")) {
                        DBHandler_Access data = DBHandler_Access.getInstance(this);
                        data.open();
                        if (response.has("imagedata")) {
                            JSONObject obj = response.getJSONObject("imagedata");
                            /*for (Object_Image_Upload objimage : list) {
                                JSONObject object = obj.getJSONObject(objimage.id + "");
                                String imageName = String.valueOf(object.getInt("id"));
                                data.updateimageName(imageName, objimage.id);
                            }*/
                            for (Map.Entry<Integer,String> entry : offers.entrySet()) {
                                JSONObject object = obj.getJSONObject(entry.getKey() + "");
                                int id = object.getInt("id");
                                //idsOffers.add(id);
                                imageid.put(entry.getKey(),id);
                            }
                           return true;
                        }
                    }
                }
            } catch (JSONException ex) {
                ex.printStackTrace();

            }
        } else {
            return false;
        }
        return false;
    }


    private void uploadata(boolean isImageUpload,String head,String con) {
        if (isImageUpload) {
            try {
                Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                        Request.Method.POST,
                        Custom_URLs_Params.getURL_AddNewBusinessExtra(),
                        Custom_URLs_Params.getParams_AddNewBusinessExtra(this,head,con,imageid,citySelect), new Response.Listener<JSONObject>() {

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
            } catch (Exception ex) {

                ex.printStackTrace();

            }
        }
    }

    private void contentResponce(JSONObject response) {
        if (response == null) {
            return;
        } else {
            try {
                if (response.has("success")) {
                    if (response.getInt("success")==1) {
                        //Globals.showAlert("Success", "You offers succesfully saved", this);
                        Globals.showShortToast(this,"Succesfully saved");
                        navi();
                        this.finish();
                    } else {
                        Globals.showAlert("Failed", "Failed, Retry!", this);
                    }
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
                ;
            }
        }
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
