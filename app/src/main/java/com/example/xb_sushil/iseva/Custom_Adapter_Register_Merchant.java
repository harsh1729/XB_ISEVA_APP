package com.example.xb_sushil.iseva;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by xb_sushil on 12/26/2015.
 */
public class Custom_Adapter_Register_Merchant extends PagerAdapter {

    FragmentActivity activity;
    private JSONObject obj;
    private int REQUEST_CAMERA = 1;
    private int SELECT_FILE = 2;
    public static ImageView imgSelected;
    public static boolean isInsert;
    public static int controlId = -1;
    public static int dataId = -1;
    private int cnt = 0;
    // public static boolean imgMultipal = false;

    // int[] image = {R.mipmap.abc, R.mipmap.ic_launcher, R.mipmap.abc, R.mipmap.ic_launcher};

    public Custom_Adapter_Register_Merchant(FragmentActivity activity, int cnt) {
        this.activity = activity;
        this.cnt = cnt;
        //this.obj = obj;

    }

    @Override
    public int getCount() {
        return cnt;
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.row_register_details, container, false);

       /* ImageView mImageView = (ImageView) view


                .findViewById(R.id.imageView);
        mImageView.setBackgroundDrawable(activity.getResources().getDrawable(image[position]));*/
        DBHandler_Access databaseAccess = DBHandler_Access.getInstance(activity);
        databaseAccess.open();
        ArrayList<Object_Control_Maping> dataList = databaseAccess.getDataWithPosition(position);
        intiControl(position, view, dataList);
        //intiControl(position, view);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    //
    private void intiControl(int position, View view, ArrayList<Object_Control_Maping> list) {
      /*{"control":[{"typeId":1,"label":"name","contant":["sushil"]},{"typeId":1,"label":"Father name","contant":["sushil"]},{"typeId":1,"label":"Number","contant":["sushil"]},{"typeId":2,"label":"Image Profile",
              "contant":[""]},{"typeId":3,"label":"Choose","contant":
          ["ganganagr","suratgarh","nohar","sagria","hanumangarh"]}]}*/
        //Log.i("SUSHIL", "my json object is " + obj);
     /* if(obj!=null){
          if(obj.has("control")){
              try {
                  LinearLayout layoutMain = (LinearLayout)view.findViewById(R.id.scrollLinear);
                  JSONArray objArrayMain = obj.getJSONArray("control");
                  JSONArray objArray = objArrayMain.getJSONArray(position);
                  for(int i = 0;i<objArray.length();i++){
                      JSONObject object = objArray.getJSONObject(i);
                      final LinearLayout layoutSub = new LinearLayout(activity);
                      LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                              , LinearLayout.LayoutParams.WRAP_CONTENT);
                      layoutSub.setLayoutParams(layoutparams);
                      layoutSub.setOrientation(LinearLayout.HORIZONTAL);
                      if(object.has("typeId")){
                          int typeID = object.getInt("typeId");

                          TextView txtLabel = Custom_Control.getTextView(activity,1,object.getString("label"));
                          layoutSub.addView(txtLabel);
                          if(typeID==EditextID){
                              JSONArray contentArray = object.getJSONArray("contant");
                              String content = contentArray.getString(0);
                              EditText edt = Custom_Control.getEditText(activity,1,content);
                              edt.addTextChangedListener(new TextWatcher() {
                                  @Override
                                  public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                  }

                                  @Override
                                  public void onTextChanged(CharSequence s, int start, int before, int count) {

                                  }

                                  @Override
                                  public void afterTextChanged(Editable s) {
                                     Log.i("SUSHIL","editable text ..... "+s.toString());
                                  }
                              });
                              layoutSub.addView(edt);
                          }else if(typeID==ImageView){
                            final ImageView img = Custom_Control.getImageView(activity, 500, 500, 1);
                              img.setClickable(true);
                              img.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View v) {
                                      //IntentActivity();
                                      selectImage();
                                      imgSelected = img;
                                      //imgMultipal = false;
                                  }
                              });
                              layoutSub.addView(img);

                          }else if(typeID==SpinnerID){
                              JSONArray aray = object.getJSONArray("contant");
                              ArrayList<String> list = new ArrayList<String>();
                              for(int j=0;j<aray.length();j++) {
                                  list.add(aray.getString(j));
                              }
                              Spinner spin = Custom_Control.getSpinnerView(activity,1);
                              ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity,
                                      android.R.layout.simple_spinner_item, list);
                              dataAdapter
                                      .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                              spin.setAdapter(dataAdapter);
                              layoutSub.addView(spin);
                          }else if(typeID==MultiImage){
                              Button btn =  Custom_Control.getButton(activity);
                              btn.setText("Add Image");
                              btn.setGravity(Gravity.RIGHT);
                              btn.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View v) {

                                      multiImage(layoutSub);
                                      selectImage();
                                      //imgMultipal = true;
                                  }
                              });
                              layoutSub.addView(btn);
                          }
                      }
                      layoutMain.addView(layoutSub);
                  }
              } catch (JSONException e) {
                  e.printStackTrace();
              }
          }
      }*/
        if (list.size() != 0) {
            for (Object_Control_Maping obj : list) {
                LinearLayout layoutMain = (LinearLayout) view.findViewById(R.id.scrollLinear);
                final LinearLayout layoutSub = new LinearLayout(activity);
                LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                        , LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutSub.setLayoutParams(layoutparams);
                layoutSub.setOrientation(LinearLayout.HORIZONTAL);
                TextView txtLabel = Custom_Control.getTextView(activity, 1, obj.label);
                txtLabel.setTextSize(15);
                String starRequired = "<font color='#FF0000'>*</font>";
                if(obj.isrequired==1)
                txtLabel.setText(Html.fromHtml(obj.label + starRequired));
                else
                    txtLabel.setText(obj.label);
                // Globals.setAppFontTextView(activity, txtLabel);
                layoutSub.addView(txtLabel);
                // Log.i("SUSHIL", "type id ..." + obj.typeId);
                if (obj.typeId == Custom_Control.EditextID) {
                    final Object_Data objData = obj.values.get(0);
                    EditText edt = Custom_Control.getEditText(activity, 2, objData.value);
                    Log.i("SUSHIL","hint is "+obj.hint);
                    edt.setHint(obj.hint);
                    // Globals.setAppFontEditView(activity, edt);
                    edt.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            Log.i("SUSHIL", "editable text ..... " + s.toString());
                            DBHandler_Access databaseAccess = DBHandler_Access.getInstance(activity);
                            databaseAccess.open();
                            databaseAccess.insertdataWithChange(s.toString(), objData.id);
                        }
                    });
                    layoutSub.addView(edt);
                } else if (obj.typeId == Custom_Control.ImageViewID) {
                    final ImageView img = Custom_Control.getImageView(activity, 100, 300, 1);
                    img.setClickable(true);
                    // Object_Data objData = obj.values.get(0);

                    img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //IntentActivity();
                            isInsert = false;
                            dataId = (int) img.getTag();
                            imgSelected = img;
                            selectImage(true, false, layoutSub);

                            //imgMultipal = false;
                        }
                    });
                    if (obj.values.size() != 0) {
                        Object_Data objData = obj.values.get(0);
                        img.setTag(objData.id);
                        if (objData.islink) {
                            Globals.loadImageIntoImageView(img, objData.value, activity, R.drawable.default_user, R.drawable.default_user);
                        } else {
                            img.setImageBitmap(BitmapFactory.decodeFile(objData.value));
                        }
                    }
                    layoutSub.addView(img);

                } else if (obj.typeId == Custom_Control.SpinnerID) {
                    int selectedValueId = 0;
                    final ArrayList<Object_Data> listdata = obj.values;
                    ArrayList<String> listItem = new ArrayList<String>();
                    for (int j = 0; j < listdata.size(); j++) {
                        listItem.add(listdata.get(j).value);
                        if (listdata.get(j).selectedValue == 1) {
                            selectedValueId = j;
                        }
                    }
                    Spinner spin = Custom_Control.getSpinnerView(activity, 2);
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity,
                            android.R.layout.simple_spinner_item, listItem);
                    dataAdapter
                            .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin.setAdapter(dataAdapter);
                    spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            DBHandler_Access databaseAccess = DBHandler_Access.getInstance(activity);
                            databaseAccess.open();
                            databaseAccess.insertdataWithChange("", listdata.get(position).id);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    spin.setSelection(selectedValueId);
                    layoutSub.addView(spin);
                } else if (obj.typeId == Custom_Control.MultiImageID) {
                        /*LinearLayout layoutMainHor = (LinearLayout) view.findViewById(R.id.scrollLinearHor);
                        layoutMainHor.setVisibility(View.VISIBLE);*/

                    LayoutInflater inflater = (LayoutInflater) activity
                            .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                    View viewHor = inflater.inflate(R.layout.custom_horizontal_view, null, false);
                    LinearLayout layoutMainHor = (LinearLayout) viewHor.findViewById(R.id.scrollLinearHor);
                    final Button btn = Custom_Control.getButton(activity);
                    btn.setText("Add Image");

                    btn.setGravity(Gravity.CENTER);

                    final LinearLayout layoutHor = new LinearLayout(activity);
                    ScrollView.LayoutParams layoutHorParams = new ScrollView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                            , LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutHor.setLayoutParams(layoutHorParams);
                    layoutHor.setOrientation(LinearLayout.HORIZONTAL);

                    /*controlId = obj.id;*/
                    btn.setTag(obj.id);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            isInsert = true;
                            controlId = (int) btn.getTag();
                            multiImage(layoutHor, "", false, -1);
                            selectImage(true, false, null);

                            //imgMultipal = true;
                        }
                    });
                    // scrollView.addView(layoutHor);
                    if (obj.values.size() != 0) {
                        final ArrayList<Object_Data> listdata = obj.values;
                        for (Object_Data objData : listdata) {
                            multiImage(layoutHor, objData.value, objData.islink, objData.id);
                        }
                    }
                    layoutMainHor.addView(layoutHor);
                    layoutSub.addView(btn);

                    layoutMain.addView(viewHor);
                    //layoutSub.addView(scrollView);
                }


                layoutMain.addView(layoutSub);

            }

        }


    }

    private void multiImage(final LinearLayout layout, String fileImage, boolean isLink, int tag) {
        Log.i("SUSHIL", "id is tag for " + fileImage + "is link is");
        final ImageView img = Custom_Control.getImageView(activity, 150, 150, 1);
        // if(dataId!=-1)
        img.setTag(tag);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataId = (int) img.getTag();
                if (dataId == -1) {
                    isInsert = true;
                } else {
                    isInsert = false;
                }
                imgSelected = img;
                selectImage(false, true, layout);

            }
        });
        if (!fileImage.isEmpty()) {
            if (isLink) {
                Globals.loadImageIntoImageView(img, fileImage, activity, R.drawable.default_user, R.drawable.default_user);
            } else {
                img.setImageBitmap(BitmapFactory.decodeFile(fileImage));
            }
        }
        imgSelected = img;
        layout.addView(img);
    }

    private void selectImage(boolean isAdd, final boolean isMulti, final LinearLayout layout) {

        final CharSequence[] items = {"Take Photo", "Choose from Library", "Delete"};
        final CharSequence[] itemsAdd = {"Take Photo", "Choose from Library"};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Choose Action!");
        //builder.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        if (!isAdd) {
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (items[item].equals("Take Photo")) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        activity.startActivityForResult(intent, REQUEST_CAMERA);
                    } else if (items[item].equals("Choose from Library")) {
                        Intent intent = new Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        intent.putExtra("crop","true");
                        activity.startActivityForResult(
                                Intent.createChooser(intent, "Select File"),
                                SELECT_FILE);
                    } else if (items[item].equals("Delete")) {
                        if (isMulti) {
                            layout.removeView(imgSelected);
                            DBHandler_Access data = DBHandler_Access.getInstance(activity);
                            data.open();
                            data.deleteSomeData(dataId);
                        } else {

                        }
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        } else {
            builder.setItems(itemsAdd, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (items[item].equals("Take Photo")) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        activity.startActivityForResult(intent, REQUEST_CAMERA);
                    } else if (items[item].equals("Choose from Library")) {
                        Intent intent = new Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        activity.startActivityForResult(
                                Intent.createChooser(intent, "Select File"),
                                SELECT_FILE);
                    }
                }
            });
            builder.show();
        }
    }

}

