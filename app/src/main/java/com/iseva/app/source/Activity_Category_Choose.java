package com.iseva.app.source;

import android.app.Activity;
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
import android.view.ViewGroup;
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
import java.util.Map;
import java.util.Set;

public class Activity_Category_Choose extends Activity {

    private HashMap<Integer, Integer> mainCateMap;
    private ProgressDialog pd;
    //private int cateId = -1;
    private String path = "";
    private int key = 1;
    private int imageId = -1;
    private boolean doubleBackToExitPressedOnce = false;
    private int REQUEST_CAMERA = 1;
    private int SELECT_FILE = 2;
    private ArrayList<Object_City> cityList;
    private int selectedCityID = -1;
    private HashMap<Integer, String> Images;
    private HashMap<Integer,Integer> imageid;
    private boolean mainImageAdded = false;
    private boolean isImageViewSelected = false;
    private ImageView selectedImageView;
    public int imgid = 2;
    private int servercity = 0;
    String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_choose);
        inti();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbindDrawables(findViewById(R.id.RootView));
        System.gc();
    }

    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }


    private void inti() {
        mainCateMap = new HashMap<>();
        Images = new HashMap<>();
        imageid = new HashMap<>();
        TextView txtHeader = (TextView) findViewById(R.id.txtHeader);
        txtHeader.setText("Register Details");
        ImageView imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity_Category_Choose.this.finish();
            }
        });




            Button btn = (Button)findViewById(R.id.btnUserSelect);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout linear = (LinearLayout) findViewById(R.id.linearImageUser);
                if (Globals.IMAGELIMIT > linear.getChildCount()) {
                    mainImageAdded = false;
                    selectImage(false, 0, null);
                    //alertChooseTwo(ViewImages);
                } else {
                    Globals.showShortToast(
                            Activity_Category_Choose.this,
                            "You can upload maximum "
                                    + Globals.IMAGELIMIT
                                    + " images only.");
                }
            }
        });
        setData();

    }

    private void selectImage(boolean isRemove, final int keyRemove, final LinearLayout linear) {
        final CharSequence[] items = {"Take Photo", "Choose from Library"};
        final CharSequence[] itemsRemove = {"Take Photo", "Choose from Library","Remove"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                        Images.remove(keyRemove);
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


    private void setImageViewBitmap(boolean islink, String selectedImagePath,int id) {
        if (!isImageViewSelected) {
            final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearImageUser);
            final ImageView img = Custom_Control.getImageView(this, 150, 150, 1);
            img.setTag(imgid);
            if(!islink) {
                img.setImageBitmap(BitmapFactory.decodeFile(selectedImagePath));
                Images.put((int) img.getTag(), selectedImagePath);
            }
            else {
                Globals.loadImageIntoImageView(img, selectedImagePath, this,R.drawable.default_offer,R.drawable.default_offer);
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
                Images.put((int) selectedImageView.getTag(), selectedImagePath);
            }
        }
        Log.i("SUSHIL", " hash map is " + Images);
    }


    private void setData() {
        Intent intent = getIntent();
        type = intent.getStringExtra("intent");
        if (type.equals("edit")) {
            String object = intent.getStringExtra("object");
            JSONObject obj = null;
            try {
                obj = new JSONObject(object);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            //Data("edit");
            if (obj != null) {
                Log.i("SUSHIL", "profile data is " + obj);
                JSONObject imageObj = null;
               // if(objMain.has("login")) {
                Button btn = (Button)findViewById(R.id.btnsb);
                btn.setText("Update");
                    try {
                       // JSONObject obj = objMain.getJSONObject("login");
                        if (obj.has("firmname")) {
                            EditText edt = (EditText) findViewById(R.id.edtMerchantName);
                            edt.setText(obj.getString("firmname"));

                        }
                        if (obj.has("firmcontact")) {
                            EditText edtCo = (EditText) findViewById(R.id.edtContact);
                            edtCo.setText(obj.getString("firmcontact"));
                        }
                        if (obj.has("address")) {
                            EditText edtAddress = (EditText) findViewById(R.id.edtMerchantAddress);
                            edtAddress.setText(obj.getString("address"));
                        }
                        if(obj.has("email")){
                            EditText edtEmail = (EditText)findViewById(R.id.edtEmail);
                            edtEmail.setText(obj.getString("email"));
                        }
                        if (obj.has("catid")) {
                            //cateId = obj.getInt("catid");
                            Object_AppConfig config = new Object_AppConfig(this);
                            config.setCatId(obj.getInt("catid"));
                            if(obj.getInt("catid")==0) {
                                config.setIsMerchant(false);
                                config.setboolIslogin(false);
                                Data("");
                            }
                            else {
                                config.setIsMerchant(true);
                                config.setboolIslogin(true);
                                Data("edit");
                            }
                        }
                        if (obj.has("cityid")) {
                            servercity = obj.getInt("cityid");
                        }

                        if (obj.has("city")) {
                            JSONArray city = obj.getJSONArray("city");
                            Log.i("SUSHIL","sushil city array "+city);
                            cityData(city);
                        }

                        if (obj.has("services")) {
                            //selectedCityID = obj.getInt("cityid");
                            EditText edt = (EditText) findViewById(R.id.edtMerchantServices);
                            edt.setText(obj.getString("services"));
                        }


                        ImageView img = (ImageView) findViewById(R.id.imgProfileMerchant);
                        img.setClickable(true);
                        img.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mainImageAdded = true;
                                selectImage();
                            }
                        });


                        if (obj.has("image")) {
                            imageObj = obj.getJSONObject("image");

                            if (imageObj.has("imageurl")) {
                                Globals.loadImageIntoImageView(img, imageObj.getString("imageurl"), this,R.drawable.default_offer,R.drawable.default_offer);
                            }
                            if (imageObj.has("id")) {
                                imageId = imageObj.getInt("id");
                            }
                        }

                        if(obj.has("images")){
                            JSONArray imageArray = obj.getJSONArray("images");
                            for (int i=0;i<imageArray.length();i++){
                                JSONObject objectImage = imageArray.getJSONObject(i);
                                setImageViewBitmap(true,objectImage.getString("imageurl"),objectImage.getInt("id"));
                            }
                        }

                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                //}
            }
        } else {
            Data("");
            String object = intent.getStringExtra("object");
            JSONObject obj = null;
            try {
                obj = new JSONObject(object);
              if (obj.has("city")) {
                    JSONArray city = obj.getJSONArray("city");
                    cityData(city);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ImageView img = (ImageView) findViewById(R.id.imgProfileMerchant);
            img.setClickable(true);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainImageAdded = true;
                    selectImage();
                }
            });
        }
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
                    selectedCityID = cityList.get(position).cityId;

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        int index = 0;
        if (cityList.size() != 0) {
            if (servercity != 0)
                for (Object_City objCity : cityList) {
                    if (objCity.cityId == servercity) {
                        //city is found
                        Log.i("SUSHIL","city from server"+servercity);
                        spinCity.setSelection(index);
                        break;
                    }
                    index++;
                }
        }
        //spinCity.setSelection(selectedCityID);
    }


    private void Data(final String type) {
        if (type.equals("")) {
            DBHandler_Access databaseAccess = DBHandler_Access.getInstance(this);
            databaseAccess.open();
            ArrayList<Object_Category> cateMain = databaseAccess.getCategoryForRegister();
            ArrayList<String> list = new ArrayList<>();
            if (cateMain.size() != 0) {
                int index = -1;
                for (Object_Category obj : cateMain) {
                    index++;
                    list.add(obj.name);
                    mainCateMap.put(index, obj.catId);
                }
                Spinner spin = (Spinner) findViewById(R.id.spinnerMainCategory);
                setSpinnerData(list, spin, cateMain);

            }
        } else {
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearMainCate);
            linearLayout.setVisibility(View.GONE);
        }
        Button btn = (Button) findViewById(R.id.btnsb);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("")) {
                    Globals.showAlertDialog(
                            "Alert",
                            "Please check the business category you have selected, you won't  be able to change it later !",
                            Activity_Category_Choose.this,
                            "Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    saveCategory();
                                }
                            }, "Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    return;
                                }
                            }, false);

                } else {
                    saveCategory();
                }

            }
        });
    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            ImageView img = (ImageView) findViewById(R.id.imgProfileMerchant);
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

               /* try {
                   // rotateImage(destination.getPath(),400);
                    decodeFile(destination.getPath(),500,500);
                } catch (Exception e){
                    Globals.showShortToast(this,"Exception");
                    e.printStackTrace();
                }*/
                Log.i("SUSHIL","sushil image path "+destination.getPath());
                if(!mainImageAdded)
                    setImageViewBitmap(false, destination.getPath(),0);
                else {
                    imageId = -1;
                    img.setImageBitmap(BitmapFactory.decodeFile(destination.getPath()));
                    path = destination.getAbsolutePath();

                }

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
               /* try {
                    //rotateImage(selectedImagePath,500);
                    selectedImagePath =  decodeFile(selectedImagePath,500,500);
                } catch (Exception e){
                    Globals.showShortToast(this,"IOException");
                    e.printStackTrace();
                }*/
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


                Log.i("SUSHIL","sushil image path "+selectedImagePath);
                if(!mainImageAdded)
                    setImageViewBitmap(false, selectedImagePath,0);
                else {
                    imageId = -1;
                    img.setImageBitmap(BitmapFactory.decodeFile(selectedImagePath));
                    path = selectedImagePath;
                }
            }
        }
    }

    /**
     * this function does the crop operation.
     * <p/>
     * private void performCrop() {
     * // take care of exceptions
     * try {
     * // call the standard crop action intent (the user device may not
     * // support it)
     * Intent cropIntent = new Intent("com.android.camera.action.CROP");
     * // indicate image type and Uri
     * cropIntent.setDataAndType(picUri, "image/*");
     * // set crop properties
     * cropIntent.putExtra("crop", "true");
     * // indicate aspect of desired crop
     * cropIntent.putExtra("aspectX", 2);
     * cropIntent.putExtra("aspectY", 1);
     * // indicate output X and Y
     * cropIntent.putExtra("outputX", 256);
     * cropIntent.putExtra("outputY", 256);
     * // retrieve data on return
     * cropIntent.putExtra("return-data", true);
     * // start the activity - we handle returning in onActivityResult
     * startActivityForResult(cropIntent, CROP_PIC);
     * }
     * // respond to users whose devices do not support the crop action
     * catch (ActivityNotFoundException anfe) {
     * Toast toast = Toast
     * .makeText(this, "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
     * toast.show();
     * }
     * }
     */


   /* private void setspinnerSelection(HashMap<Integer, Integer> map,
                                     Spinner spin, int id) {

        int spinSelection = -1;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue().equals(id)) {
                spinSelection = entry.getKey();
            }
        }

        if (spinSelection != -1)
            spin.setSelection(spinSelection);
    }*/

    private void setSpinnerData(ArrayList<String> list, Spinner spin, final ArrayList<Object_Category> listCate) {

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(dataAdapter);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {
                Object_AppConfig config = new Object_AppConfig(Activity_Category_Choose.this);
                config.setCatId(listCate.get(position).catId);
                getsubCategory(listCate.get(position).catId, listCate.get(position).isbusiness);
                /*Object_AppConfig config = new Object_AppConfig(Activity_Category_Choose.this);
                if (listCate.get(position).parentId == 0)
                    if(listCate.get(position).catId!=4)
                    getsubCategory(listCate.get(position).catId);
                    else{
                        config.setCatId(listCate.get(position).catId);
                        LinearLayout Linear = (LinearLayout) findViewById(R.id.linearSubCate);
                        Linear.setVisibility(View.GONE);
                    }

                else {
                    config.setCatId(listCate.get(position).catId);
                    LinearLayout Linear = (LinearLayout) findViewById(R.id.linearSubCate);
                    Linear.setVisibility(View.GONE);
                }*/


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

		/*
         * if(spinnerSelectedState!=-1) spin.setSelection(spinnerSelectedState);
		 */
        //setspinnerSelection(mapState, spin, objRegister.stateId);

    }


    private void getsubCategory(int cateId,int isbusiness) {
        try {
            pd = Globals.showLoadingDialog(pd, this, false, "");
            HashMap<String, String> map = new HashMap<String, String>();

            /*Custom_VolleyArrayRequest jsonArrayRQST = new Custom_VolleyArrayRequest(Custom_URLs_Params.getURL_AllCity(), map,
                    new Response.Listener<JSONArray>() {

                        @Override
                        public void onResponse(JSONArray response) {
                            Globals.hideLoadingDialog(pd);
                            Log.i("SUSHIL", "json Response recieved !!");
                            parseSubCateGory(response);

                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError err) {
                            Globals.hideLoadingDialog(pd);
                            Log.i("SUSHIL", "ERROR VolleyError");
                            Globals.showShortToast(Activity_Category_Choose.this,
                                    Globals.MSG_SERVER_ERROR);
                        }
                    });

            Custom_VolleyAppController.getInstance().addToRequestQueue(jsonArrayRQST);*/
            map.put("parentid", cateId + "");
            map.put("isbusiness",1+"");
            Log.i("SUSHIL", "map is call " + Custom_URLs_Params.getURL_GetSubCate() + "" + map);
            Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                    Request.Method.POST,
                    Custom_URLs_Params.getURL_GetSubCate(),
                    map,
                    new Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Globals.hideLoadingDialog(pd);
                            Log.i("SUSHIL", "json Response recieved !!" + response);
                            parseSubCateGory(response);

                        }

                    }, new ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError err) {
                    Globals.hideLoadingDialog(pd);
                    Log.i("SUSHIL", "ERROR VolleyError" + err);
                    Globals.showShortToast(
                            Activity_Category_Choose.this,
                            Globals.MSG_SERVER_ERROR);

                }
            });

            Custom_VolleyAppController.getInstance().addToRequestQueue(
                    jsonObjectRQST);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseSubCateGory(JSONObject object) {
        if (object == null) {
            return;
        } else {
            try {
                if (object.has("success")) {
                    int success = object.getInt("success");
                    JSONArray array = object.getJSONArray("categories");
                    if (array != null && array.length()>0) {

                        ArrayList<Object_Category> listSubCate = new ArrayList<>();
                        ArrayList<String> listsub = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            try {
                                JSONObject obj = array.getJSONObject(i);
                                Object_Category cate = new Object_Category();

                                cate.catId = obj.getInt("catid");
                                cate.name = obj.getString("name");

                                listsub.add(obj.getString("name"));
                                listSubCate.add(cate);
                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }
                        }
                        LinearLayout Linear = (LinearLayout) findViewById(R.id.linearSubCate);
                        Linear.setVisibility(View.VISIBLE);
                        Spinner spin = (Spinner) findViewById(R.id.spinnerSubCategory);
                        setSpinnerDataSub(listsub, spin, listSubCate);
                    }else{
                        LinearLayout Linear = (LinearLayout) findViewById(R.id.linearSubCate);
                        Linear.setVisibility(View.GONE);
                    }
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }

    }

    private void setSpinnerDataSub(ArrayList<String> list, Spinner spin, final ArrayList<Object_Category> listCate) {

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(dataAdapter);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long id) {
                Object_AppConfig objConfig = new Object_AppConfig(Activity_Category_Choose.this);
                objConfig.setCatId(listCate.get(position).catId);
                //cateId = listCate.get(position).catId;

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

		/*
         * if(spinnerSelectedState!=-1) spin.setSelection(spinnerSelectedState);
		 */
        //setspinnerSelection(mapState, spin, objRegister.stateId);

    }

    private void saveCategory() {
        boolean isUploadimage = false;
        EditText edt = (EditText) findViewById(R.id.edtMerchantName);
        EditText edtCo = (EditText) findViewById(R.id.edtContact);
        EditText edtAddress = (EditText) findViewById(R.id.edtMerchantAddress);
        EditText edtServices = (EditText) findViewById(R.id.edtMerchantServices);
        if(selectedCityID==-1){
            Globals.showShortToast(this, "Please select your city.");
            return;
        }
        if (edt.getText().toString().isEmpty()) {
            Globals.showShortToast(this, "Please add your name");
            return;
        }
        if (edtCo.getText().toString().isEmpty()) {
            Globals.showShortToast(this, "Please add your contact number");
            return;

        } else {
            if (!iscontact(edtCo.getText().toString())) {
                Globals.showShortToast(this, "Please add your valid contact number");
                return;
            }
        }
        if (edtAddress.getText().toString().isEmpty()) {
            Globals.showShortToast(this, "Please add your address");
            return;
        }
        if(edtServices.getText().toString().isEmpty()){
            Globals.showShortToast(this, "Please add your Services");
            return;
        }

        /*if (imageId == -1 && path.isEmpty()) {
            Globals.showShortToast(this, "Please add your profile image");
            return;
        }*/

        uploadImage(edt.getText().toString(), edtCo.getText().toString(), edtAddress.getText().toString(), edtServices.getText().toString());


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


    private void uploadCateData(String name, String contact, String addre,String servi, int imageId) {
        //get email
        EditText editText = (EditText)findViewById(R.id.edtEmail);
        Object_AppConfig config = new Object_AppConfig(this);
        if (config.getCatId() != -1) {
            try {
                Log.i("SUSHIL", "save cate url " + Custom_URLs_Params.getURL_OnsubmitCate());
                pd = Globals.showLoadingDialog(pd, this, false, "");
                Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                        Request.Method.POST,
                        Custom_URLs_Params.getURL_OnsubmitCate(),
                        Custom_URLs_Params.getParams_SaveCate(this, name, contact, addre,servi,imageId,selectedCityID,imageid,editText.getText().toString()),
                        new Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Globals.hideLoadingDialog(pd);
                                Log.i("SUSHIL", "json Response recieved !!" + response);
                                parseCateResponse(response);

                            }

                        }, new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError err) {
                        Globals.hideLoadingDialog(pd);
                        Log.i("SUSHIL", "ERROR VolleyError" + err);
                        Globals.showShortToast(
                                Activity_Category_Choose.this,
                                Globals.MSG_SERVER_ERROR);

                    }
                });

                Custom_VolleyAppController.getInstance().addToRequestQueue(
                        jsonObjectRQST);

            } catch (Exception e) {
                e.printStackTrace();
                Globals.hideLoadingDialog(pd);
            }
        } else {
            Globals.showShortToast(this, "Please select your category");
        }
    }
    private HashMap<String, File> getMapImageParams() {
        HashMap<String, File> imageMap = new HashMap<String, File>();
        try {
            if (Images.size() != 0) {
                for (Integer key : Images.keySet()) {
                    File file = new File(Images.get(key));
                    imageMap.put(key + "", file);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imageMap;
    }


    /*private String getCommaSeperatedKeys(Set<String> setKeys) {
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
    }*/



    private void uploadImage(final String name, final String con, final String Address,final String servi) {
        Custom_ConnectionDetector cd = new Custom_ConnectionDetector(this);
        if (cd.isConnectingToInternet()) {
            try {
                //final HashMap<String, File> imageMap = getMapImageParams(list);
                if (imageId == -1 || !path.isEmpty()) {
                    final HashMap<String, File> imageMap = new HashMap<>();
                    File file = new File(path);
                    imageMap.put(key + "", file);
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
                                        boolean re = parseimageResponse(response);
                                        if(Images.size()==0)
                                            uploadCateData(name, con, Address,servi, imageId);
                                        else
                                            uploadImageMulti(name,con,Address,servi);


                                    }
                                }, new ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError err) {
                                Log.i("SUSHIL", "ERROR VolleyError " + err);
                                Globals.hideLoadingDialog(pd);
                                Globals.showShortToast(
                                        Activity_Category_Choose.this,
                                        Globals.MSG_SERVER_ERROR);

                            }
                        });

                        Custom_VolleyAppController.getInstance().addToRequestQueue(
                                jsonObjectRQST);

                    }

                } else {
                    if(Images.size()==0)
                       uploadCateData(name, con, Address,servi, imageId);
                    else
                        uploadImageMulti(name,con,Address,servi);
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
    private void uploadImageMulti(final String name, final String con, final String Address,final String servi) {
        Custom_ConnectionDetector cd = new Custom_ConnectionDetector(this);
        if (cd.isConnectingToInternet()) {
            try {
                //final HashMap<String, File> imageMap = getMapImageParams(list);
                if (Images.size()!=0) {
                    final HashMap<String, File> imageMap = getMapImageParams();
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
                                        boolean re = imageuploadResponce(response, Images);
                                        uploadCateData(name, con, Address,servi,imageId);


                                    }
                                }, new ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError err) {
                                Log.i("SUSHIL", "ERROR VolleyError " + err);
                                Globals.hideLoadingDialog(pd);
                                Globals.showShortToast(
                                        Activity_Category_Choose.this,
                                        Globals.MSG_SERVER_ERROR);

                            }
                        });

                        Custom_VolleyAppController.getInstance().addToRequestQueue(
                                jsonObjectRQST);

                    }

                } else {
                    uploadCateData(name, con, Address,servi, imageId);
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

    private boolean parseimageResponse(JSONObject obj) {
        if (obj == null) {
            return false;
        } else {
            try {
                if (obj.has("status")) {
                    if (obj.getString("status").equals("login")) {
                        if (obj.has("imagedata")) {
                            JSONObject object = obj.getJSONObject("imagedata");
                            JSONObject object1 = object.getJSONObject(key + "");
                            imageId = object1.getInt("id");
                            Log.i("SUSHIL", "SUSHIL image id is " + imageId);
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

    private void parseCateResponse(JSONObject obj) {
        if (obj == null) {
            return;
        } else {
            if (obj.has("controls")) {
                //JSONObject objUserData = obj.getJSONObject("controls");
                try {
                    JSONArray array = obj.getJSONArray("controls");
                    if (array.length() != 0) {
                       // if (type.equals(""))
                            navigationRegisterAllData(obj);
                        }
                       /* else {
                            this.finish();
                            Globals.showShortToast(this, "You are successfully update data, contact us in case of any doubt.");
                        }*/
                    else {
                        if(type.equals("")) {
                            this.finish();
                            Globals.showShortToast(this, "Congratulations ! You are successfully registered with us.\n" +
                                    "Please wait for your data approval, contact us in case of any doubt.");
                        }else {
                            Log.i("SUSHIL","Array lenth 0");
                            this.finish();
                            Globals.showShortToast(this, "You are successfully update data, contact us in case of any doubt.");
                        }
                    }
                }catch (JSONException ex){
                    ex.printStackTrace();
                }
            }

        }
    }

    private boolean iscontact(String number) {
        if (number.length() == 10) {
            return true;
        }
        return false;
    }

    private void navigationRegisterAllData(JSONObject obj) {
        if (obj == null) {
            return;
        } else {
            Intent i = new Intent(this, Activity_Merchant_register.class);
            i.putExtra("controls", obj.toString());
            startActivity(i);
            this.finish();
        }
    }

    private String decodeFile(String path,int DESIREDWIDTH, int DESIREDHEIGHT) {
        String strMyImagePath = null;
        Bitmap scaledBitmap = null;

        try {
            // Part 1: Decode image
            Bitmap unscaledBitmap = ScalingUtilities.decodeFile(path, DESIREDWIDTH, DESIREDHEIGHT, ScalingUtilities.ScalingLogic.FIT);

            if (!(unscaledBitmap.getWidth() <= DESIREDWIDTH && unscaledBitmap.getHeight() <= DESIREDHEIGHT)) {
                // Part 2: Scale image
                scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, DESIREDWIDTH, DESIREDHEIGHT, ScalingUtilities.ScalingLogic.FIT);
            } else {
                unscaledBitmap.recycle();
                return path;
            }

            // Store to tmp file

            String extr = Environment.getExternalStorageDirectory().toString();
            File mFolder = new File(extr + "/TMMFOLDER");
            if (!mFolder.exists()) {
                mFolder.mkdir();
            }

            String s = "tmp.png";

            File f = new File(mFolder.getAbsolutePath(), s);

            strMyImagePath = f.getAbsolutePath();
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(f);
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (Exception e) {

                e.printStackTrace();
            }

            scaledBitmap.recycle();
        } catch (Throwable e) {
        }

        if (strMyImagePath == null) {
            return path;
        }
        return strMyImagePath;

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
