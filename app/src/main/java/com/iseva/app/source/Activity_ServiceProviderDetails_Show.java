package com.iseva.app.source;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;


import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookDialog;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Activity_ServiceProviderDetails_Show extends FragmentActivity {
    //private ViewPager mViewPager;
    //private PageIndicator mIndicator;
    private ProgressDialog pd;
    private ArrayList<String> listImageUrls;
    private String name = "";
    private String contact = "";
    private String contactDetails = "";
    private String services="";
    private PopupWindow popupWindow;
    private CallbackManager callbackManager;
    private int REQUEST_CODE=11;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_show);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        inti();


    }

    private void inti() {
        listImageUrls = new ArrayList<>();
        TextView txtHeader = (TextView) findViewById(R.id.txtHeader);
        txtHeader.setText("Details");
        ImageView imgBack = (ImageView) findViewById(R.id.imgBack);
        ImageView imgShareProfile = (ImageView) findViewById(R.id.imgShareProfile);
        imgShareProfile.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity_ServiceProviderDetails_Show.this.finish();
            }
        });
        Intent i = getIntent();
        if (i.getIntExtra("userid", -1) != -1)
            getAlldetails(i.getIntExtra("userid", -1));

        Button btn = (Button) findViewById(R.id.callBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globals.call(Activity_ServiceProviderDetails_Show.this,contact);
            }
        });

        imgShareProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // shareClick();
                //initShareIntent();

                popupWindowShareIntent();

            }
        });
    }

            private void popupWindowShareIntent(){


                    View popupView = getLayoutInflater().inflate(R.layout.popup_layout_share_intent, null);

                    popupWindow = new PopupWindow(popupView,
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    // Example: If you have a TextView inside `popup_layout.xml`
                    ImageView whatsapp = (ImageView) popupView.findViewById(R.id.ivWhatsapp);
                    ImageView fb = (ImageView) popupView.findViewById(R.id.ivFacebook);
                    ImageView gmail = (ImageView) popupView.findViewById(R.id.ivGmail);
                    ImageView twitter = (ImageView) popupView.findViewById(R.id.ivTwitter);

                    whatsapp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            initShareIntent(view);

                        }

                    });

                    fb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            initShareIntent(view);

                        }
                    });

                    gmail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            initShareIntent(view);

                        }
                    });

                    twitter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            initShareIntent(view);

                        }
                    });

                    // Initialize more widgets from `popup_layout.xml`

                    // If the PopupWindow should be focusable
                    popupWindow.setFocusable(true);

                    // If you need the PopupWindow to dismiss when when touched outside
                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ececec")));

                    //int location[] = new int[2];

                    // Get the View's(the one that was clicked in the Fragment) location
                    //anchorView.getLocationOnScreen(location);

                    // Using location, the PopupWindow will be displayed right under anchorView
                    popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0
                    );

            }



      private void initShareIntent(final View view){

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {

                        popupWindow.dismiss();

                        String imagePath = "http://xercesblue.website/iseva_bus.jpg";
                        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("image/jpeg");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, name + "\n\n" + contactDetails + "\n\n" + services + "\n\n"
                                + Globals.SHARE_APP_MSG + "\n " + Globals.SHARE_LINK_GENERIC);



                        switch (view.getId()) {

                                case R.id.ivWhatsapp:

                                    Picasso.with(getApplicationContext()).load(imagePath).into(new Target() {
                                        @Override
                                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                                            shareIntent.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                                            if (isPackageInstalled("com.whatsapp", Activity_ServiceProviderDetails_Show.this)) {
                                                shareIntent.setPackage("com.whatsapp");
                                                startActivity(Intent.createChooser(shareIntent, "Share News"));

                                            } else {

                                                Toast.makeText(getApplicationContext(), "Please Install Whatsapp", Toast.LENGTH_LONG).show();
                                            }


                                        }

                                        @Override
                                        public void onBitmapFailed(Drawable errorDrawable) {
                                        }

                                        @Override
                                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                                        }
                                    });



                                    break;

                                case R.id.ivGmail:

                                    Picasso.with(getApplicationContext()).load(imagePath).into(new Target() {
                                        @Override
                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                                    shareIntent.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                                    shareIntent.putExtra(Intent.EXTRA_SUBJECT,"ISeva App");

                                    if (isPackageInstalled("com.google.android.gm", Activity_ServiceProviderDetails_Show.this)) {
                                        shareIntent.setPackage("com.google.android.gm");
                                        startActivity(Intent.createChooser(shareIntent, "Share Details"));

                                    } else {

                                        Toast.makeText(getApplicationContext(), "Please Install Gmail", Toast.LENGTH_LONG).show();
                                    }


                                }

                                @Override
                                public void onBitmapFailed(Drawable errorDrawable) {
                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable) {
                                }
                        });


                                    break;

                            case R.id.ivTwitter:

                                Picasso.with(getApplicationContext()).load(imagePath).into(new Target() {
                                    @Override
                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                                        shareIntent.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                                        if (isPackageInstalled("com.twitter.android", Activity_ServiceProviderDetails_Show.this)) {
                                            shareIntent.setPackage("com.twitter.android");
                                            startActivity(Intent.createChooser(shareIntent, "Share Details"));

                                        } else {

                                            Toast.makeText(getApplicationContext(), "Please Install Twitter", Toast.LENGTH_LONG).show();
                                        }

                                    }

                                    @Override
                                    public void onBitmapFailed(Drawable errorDrawable) {
                                    }

                                    @Override
                                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                                    }
                                });


                                break;

                            case R.id.ivFacebook:
                               /* ShareDialog dialog = null;
                                callbackManager = CallbackManager.Factory.create();
                                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                                if(accessToken==null){
LoginManager.getInstance().logInWithPublishPermissions(
        Activity_ServiceProviderDetails_Show.this,Arrays.asList("publish_actions") );}*/

                                ShareDialog dialog = new ShareDialog(Activity_ServiceProviderDetails_Show.this);
                                   ShareLinkContent.Builder builder = new ShareLinkContent.Builder();
                                   builder.setContentUrl(Uri.parse(Globals.SHARE_LINK_GENERIC));
                                   builder.setQuote(name + "\n\n" + contactDetails + "\n" + services + "\n\n" + Globals.SHARE_APP_MSG_MAX_DISCOUNT);
                                   dialog.show(Activity_ServiceProviderDetails_Show.this, builder.build());



/*

                                dialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                                    @Override
                                    public void onSuccess(Sharer.Result result) {
                                     //   Toast.makeText(Activity_ServiceProviderDetails_Show.this, result.getPostId().toString(),Toast.LENGTH_LONG).show();
                                        Log.d("gopal", "shared successfully "+result.getPostId().toString());
                                        //add your code to handle successful sharing
                                        Toast.makeText(Activity_ServiceProviderDetails_Show.this,"Post Shared Successfully On Facebook",Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onCancel() {
                                        Log.d("gopal", "sharing cancelled");
                                        //add your code to handle cancelled sharing
                                        Toast.makeText(Activity_ServiceProviderDetails_Show.this,"Sharing Cancelled",Toast.LENGTH_LONG).show();


                                    }


                                    @Override
                                    public void onError(FacebookException error) {
                                        Log.d("gopal", "sharing error");
                                        //add your code to handle sharing error
                                        Toast.makeText(Activity_ServiceProviderDetails_Show.this,"Sharing Failed!\nTry Again Later.",Toast.LENGTH_LONG).show();
                                    }
                                });
*/





                                break;


                        }



                    }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {

                    }
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
    }


 /*   @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
       *//* if( resultCode == RESULT_OK) {
            Toast.makeText(Activity_ServiceProviderDetails_Show.this, "Post Shared Successfully On Facebook", Toast.LENGTH_LONG).show();
        }
         else{
            Toast.makeText(Activity_ServiceProviderDetails_Show.this, "NOt POsted", Toast.LENGTH_LONG).show();


        }*//*




        }*/


    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file =  new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Share News" + System.currentTimeMillis() + ".jpg");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    /*private void callMerchant() {
        Globals.showAlertDialog(
                "Alert",
                "Are you sure to call?",
                this,
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int id) {
                        callIntent();
                    }
                }, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int id) {
                        return;
                    }
                }, false);
    }

    private void callIntent() {
        //Call_PhoneListener cList = new Call_PhoneListener(this);
        //cList.registerNumber(Globals.getSimnumber(this));
        Intent callIntent = new Intent(
                Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"
                + contact));
        try {
            this.startActivity(callIntent);
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }


    }*/


    @Override
    protected void onDestroy() {
        super.onDestroy();

       // unbindDrawables(findViewById(R.id.RootView));
       // System.gc();
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

    private void getAlldetails(int userid) {
        //add volley call here
        try {
            pd = Globals.showLoadingDialog(pd, this, false, "");
            HashMap<String, String> map = new HashMap<>();
            map.put("userid", userid + "");
            Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                    Request.Method.POST,
                    Custom_URLs_Params.getURL_Get_Service_Provider_Data(),
                    map,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Globals.hideLoadingDialog(pd);
                            Log.i("SUSHIL", "json Response recieved !!" + response);
                            createCustomViewer(response);

                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError err) {
                    Globals.hideLoadingDialog(pd);
                    Log.i("SUSHIL", "ERROR VolleyError" + err);
                    Globals.showShortToast(
                            Activity_ServiceProviderDetails_Show.this,
                            Globals.MSG_SERVER_ERROR);

                }
            });

            Custom_VolleyAppController.getInstance().addToRequestQueue(
                    jsonObjectRQST);
        } catch (Exception ex) {
            ex.printStackTrace();
            Globals.hideLoadingDialog(pd);
            Globals.showShortToast(Activity_ServiceProviderDetails_Show.this,
                    Globals.MSG_SERVER_ERROR);
        }
    }


    private void createCustomViewer(JSONObject obj) {
        if (obj == null) {
            return;
        } else {
            try {
                if (obj.getInt("success") == 1) {
                    if (obj.has("firmcontact")) {
                        if (obj.getString("firmcontact") != null) {
                            contact = obj.getString("firmcontact");
                        }
                    }
                    if (obj.has("image")) {
                        JSONObject objectImageProfile = obj.getJSONObject("image");
                        ImageView img = (ImageView) findViewById(R.id.imgDeatils);
                        Globals.preloadImage(getApplicationContext(), objectImageProfile.getString("imageurl"));
                        //Custom_RoundedImageView imgDoctor = (Custom_RoundedImageView) findViewById(R.id.imgDoctor);
                        int totalContent = Globals.getScreenSize((Activity) this).x;
                       // int imgWidth = totalContent - ((totalContent * 75) / 100);
                        int imgWidth = totalContent - ((totalContent * 65) / 100);

                        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) img
                                .getLayoutParams();
                        lp.width = imgWidth;
                        lp.height = (int) (imgWidth * 1.1);


                        img.setLayoutParams(lp);


                        Globals.loadImageIntoImageView(img, objectImageProfile.getString("imageurl"), this, R.drawable.default_offer, R.drawable.default_offer,200,200);
                    }


                    if (obj.has("firmname")) {
                        if (obj.getString("firmname") != null) {
                            TextView txtName = (TextView) findViewById(R.id.txtName);
                            txtName.setText(obj.getString("firmname"));
                            name = obj.getString("firmname");
                        }
                    }
                    if (obj.has("address")) {
                        TextView txtNameAddress = (TextView) findViewById(R.id.txtNameAddress);
                        if (obj.getString("address") != null && !obj.getString("address").isEmpty()) {
                            txtNameAddress.setVisibility(View.VISIBLE);
                            txtNameAddress.setText(obj.getString("address"));

                        } else {
                            txtNameAddress.setVisibility(View.GONE);
                        }
                    }

                    if (obj.has("images")) {
                        JSONArray objArray = obj.getJSONArray("images");
                        CardView cvImage = (CardView) findViewById(R.id.custom_row_iservice_providImages);
                        LinearLayout layoutMainHor = (LinearLayout) findViewById(R.id.scrollLinearHorImageUser);
                        if (objArray != null && objArray.length() != 0) {
                            cvImage.setVisibility(View.VISIBLE);
                            for (int j = 0; j < objArray.length(); j++) {
                                JSONObject objImage = objArray.getJSONObject(j);

                                Globals.preloadImage(getApplicationContext(), objImage.getString("imageurl"));
                                final ImageView img = Custom_Control.getImageView(this, 130, 130, 1);
                                //downloadImag(objImage.getString("imageurl"),img);
                                Globals.loadImageIntoImageView(img, objImage.getString("imageurl"), this, R.drawable.default_offer, R.drawable.default_offer,150,150);
                                layoutMainHor.addView(img);
                                img.setTag(j);
                                listImageUrls.add(objImage.getString("imageurl"));
                                img.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        navigationImageViewer((int) img.getTag());
                                    }
                                });
                            }
                        // loadImage();
                        } else {
                            cvImage.setVisibility(View.GONE);
                        }
                    }
                    if(obj.has("latitude") && obj.has("longitude"))
                    {
                        final double latitude = Double.parseDouble(obj.getString("latitude"));
                        final double longitude = Double.parseDouble(obj.getString("longitude"));
                        if(latitude !=0 && longitude !=0)
                        {
                            LinearLayout location_layout = (LinearLayout)findViewById(R.id.layout_location);
                            ImageView location_map = (ImageView)findViewById(R.id.image_location_map);
                            location_layout.setVisibility(View.VISIBLE);
                            location_map.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {


                                    Dexter.withActivity(Activity_ServiceProviderDetails_Show.this)
                                            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                                            .withListener(new PermissionListener() {
                                                @Override public void onPermissionGranted(PermissionGrantedResponse response) {
                                                    Intent i = new Intent(Activity_ServiceProviderDetails_Show.this,Activity_location_show.class);
                                                    i.putExtra("destination_latitude",""+latitude);
                                                    i.putExtra("destination_longitude",""+longitude);
                                                    startActivity(i);
                                                }
                                                @Override public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}
                                                @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                                            }).check();

                                }
                            });
                        }
                    }
                    if (obj.has("contactdetails")) {
                        LinearLayout linear = (LinearLayout) findViewById(R.id.linearAddress);
                        if (obj.getString("contactdetails").isEmpty()) {
                            linear.setVisibility(View.GONE);
                        } else {
                            linear.setVisibility(View.VISIBLE);
                            TextView txtDetails = (TextView) findViewById(R.id.txtContact);
                            contactDetails = obj.getString("contactdetails");
                            txtDetails.setText(contactDetails);

                            Linkify.addLinks(txtDetails, Linkify.ALL);
                        }
                    }


                    /*if (obj.has("email")) {
                        LinearLayout linear = (LinearLayout) findViewById(R.id.linearEmails);
                        if (obj.getString("email").isEmpty() && obj
                                .getString("email").equals("")) {
                            linear.setVisibility(View.GONE);
                        } else {
                            linear.setVisibility(View.VISIBLE);
                            TextView txtDetails = (TextView) findViewById(R.id.txtEmail);
                            txtDetails.setText(obj.getString("email"));
                        }
                    }*/
                    if (obj.has("services")) {
                        LinearLayout linear = (LinearLayout) findViewById(R.id.linearServices);
                        if (obj.getString("services").isEmpty()) {
                            linear.setVisibility(View.GONE);
                        } else {
                            linear.setVisibility(View.VISIBLE);
                            TextView txtDetails = (TextView) findViewById(R.id.txtServices);
                            txtDetails.setText(obj.getString("services"));
                             services=obj.getString("services");
                            Linkify.addLinks(txtDetails, Linkify.ALL);
                        }
                    }


                    if (obj.has("data")) {
                        JSONArray Array = obj.getJSONArray("data");
                        if (Array.length() != 0) {
                            ScrollView scrollView = (ScrollView) findViewById(R.id.scrollViewDetails);
                            scrollView.setVisibility(View.VISIBLE);
                            LinearLayout layoutMain = (LinearLayout) findViewById(R.id.scrollLinearDetails);
                            for (int i = 0; i < Array.length(); i++) {
                                JSONObject objType = Array.getJSONObject(i);
                                if (objType != null) {

                                    if (objType.has("controlid")) {
                                        int typeId = objType.getInt("controlid");
                                        final LinearLayout layoutSub = new LinearLayout(this);
                                        LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                                                , LinearLayout.LayoutParams.WRAP_CONTENT);
                                        layoutSub.setLayoutParams(layoutparams);
                                        layoutSub.setOrientation(LinearLayout.VERTICAL);
                                        Log.i("SUSHIL", "json object is Array " + typeId);
                                        //creating a line
                                        LinearLayout linearLayout = new LinearLayout(this);
                                        LinearLayout.LayoutParams lpLine = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                                                , 1);
                                        lpLine.setMargins(4, 6, 4, 6);
                                        linearLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                        linearLayout.setLayoutParams(lpLine);


                                        if (typeId == Custom_Control.EditextID || typeId == Custom_Control.SpinnerID) {
                                            String values = objType.getString("value");

                                            if (!values.trim().equals("")) {
                                                TextView txtLabel = Custom_Control.getTextViewWithoutWeight(this, objType.getString("label"));
                                                txtLabel.setTextSize(17);
                                                txtLabel.setTextColor(getResources().getColor(android.R.color.black));
                                                txtLabel.setTypeface(Typeface.SERIF);
                                                TextView txtContent = null;

                                                txtContent = Custom_Control.getTextViewWithoutWeight(this, values);
                                                txtContent.setTextSize(15);
                                                //Linkify.addLinks(txtContent, Linkify.PHONE_NUMBERS);
                                               // Linkify.addLinks(txtContent, Linkify.ALL);
                                                //txtContent.setAutoLinkMask(Linkify.ALL);
                                                //txtContent.setLinksClickable(true);
                                                //txtContent.setMovementMethod(LinkMovementMethod.getInstance());
                                                txtContent.setTextColor(getResources().getColor(android.R.color.black));

                                                layoutSub.addView(txtLabel);
                                                if (txtContent != null)
                                                    layoutSub.addView(txtContent);

                                                layoutSub.addView(linearLayout);
                                                layoutMain.addView(layoutSub);
                                            }

                                        } else if (typeId == Custom_Control.MultiImageID || typeId == Custom_Control.ImageViewID) {
                                            JSONArray arrayContent = objType.getJSONArray("value");
                                            if (arrayContent.length() != 0 && arrayContent!=null) {
                                            LayoutInflater inflater = (LayoutInflater)
                                                    getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                                            View viewHor = inflater.inflate(R.layout.custom_horizontal_view, null, false);
                                            LinearLayout layoutMainHor = (LinearLayout) viewHor.findViewById(R.id.scrollLinearHor);
                                            TextView txtLabel = Custom_Control.getTextViewWithoutWeight(this, objType.getString("label"));
                                            txtLabel.setTextColor(getResources().getColor(android.R.color.black));
                                            txtLabel.setTextSize(17);
                                                txtLabel.setTypeface(Typeface.SERIF);

                                                for (int j = 0; j < arrayContent.length(); j++) {
                                                    JSONObject objImage = arrayContent.getJSONObject(j);
                                                    final ImageView img = Custom_Control.getImageView(this, 100, 100, 1);
                                                    Globals.preloadImage(getApplicationContext(), objImage.getString("imageurl"));
                                                    Globals.loadImageIntoImageView(img, objImage.getString("imageurl"), this, R.drawable.default_offer, R.drawable.default_offer);
                                                    layoutMainHor.addView(img);
                                                    img.setTag(j);
                                                    listImageUrls.add(objImage.getString("imageurl"));
                                                    img.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            navigationImageViewer((int) img.getTag());
                                                        }
                                                    });
                                                }

                                            layoutSub.addView(txtLabel);
                                            layoutSub.addView(viewHor);
                                            layoutSub.addView(linearLayout);

                                            layoutMain.addView(layoutSub);
                                          }
                                        }
                                    }
                                }
                            }
                        }/*else{
                            ScrollView scrollView = (ScrollView)findViewById(R.id.scrollViewDetails);
                            scrollView.setVisibility(View.GONE);
                        }*/


                        //Log.i("SUSHIL","json object is "+profile);
               /* Custom_RoundedImageView img = (Custom_RoundedImageView)findViewById(R.id.imgDeatils);

                    //Custom_RoundedImageView imgDoctor = (Custom_RoundedImageView) findViewById(R.id.imgDoctor);
                    int totalContent = Globals.getScreenSize((Activity) this).x;
                    int imgWidth = totalContent - ((totalContent * 75) / 100);

                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) img
                            .getLayoutParams();
                    lp.width = imgWidth;
                    lp.height = (int) (imgWidth * 1.2);
                    img.setLayoutParams(lp);


                    Globals.loadImageIntoImageView(img, profile.getString("url"), this, R.drawable.default_user, R.drawable.default_user);
                TextView txtName = (TextView)findViewById(R.id.txtName);
                txtName.setText(profile.getString("name"));
                    name = profile.getString("name");
            }

                if (obj.has("control")) {

                    JSONArray Array = obj.getJSONArray("control");
                    LinearLayout layoutMain = (LinearLayout) findViewById(R.id.scrollLinearDetails);
                    for (int i = 0; i < Array.length(); i++) {
                        JSONObject objType = Array.getJSONObject(i);
                        if (objType != null) {

                            if (objType.has("typeId")) {
                                int typeId = objType.getInt("typeId");
                                final LinearLayout layoutSub = new LinearLayout(this);
                                LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                                        , LinearLayout.LayoutParams.WRAP_CONTENT);
                                layoutSub.setLayoutParams(layoutparams);
                                layoutSub.setOrientation(LinearLayout.VERTICAL);
                                Log.i("SUSHIL", "json object is Array " + typeId);
                                if (typeId == Custom_Control.EditextID) {

                                    TextView txtLabel = Custom_Control.getTextViewWithoutWeight(this, objType.getString("label"));
                                    txtLabel.setTextSize(17);
                                    txtLabel.setTypeface(Typeface.DEFAULT_BOLD);
                                    JSONArray arrayContent = objType.getJSONArray("contant");
                                    TextView txtContent = null;
                                    if (arrayContent.length() != 0) {
                                        txtContent = Custom_Control.getTextViewWithoutWeight(this, arrayContent.getString(0));

                                    }
                                    layoutSub.addView(txtLabel);
                                    if (txtContent != null)
                                        layoutSub.addView(txtContent);

                                    layoutMain.addView(layoutSub);

                                } else if (typeId == Custom_Control.MultiImageID) {

                                    LayoutInflater inflater = (LayoutInflater)
                                            getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                                    View viewHor = inflater.inflate(R.layout.custom_horizontal_view, null, false);
                                    LinearLayout layoutMainHor = (LinearLayout) viewHor.findViewById(R.id.scrollLinearHor);
                                    TextView txtLabel = Custom_Control.getTextView(this, 1, objType.getString("label"));
                                    JSONArray arrayContent = objType.getJSONArray("contant");
                                    if (arrayContent.length() != 0) {
                                        for (int j = 0; j < arrayContent.length(); j++) {
                                            final ImageView img = Custom_Control.getImageView(this, 100, 100, 1);
                                            Globals.loadImageIntoImageView(img, arrayContent.getString(j), this, R.drawable.default_user, R.drawable.default_user);
                                            layoutMainHor.addView(img);
                                            img.setTag(j);
                                            listImageUrls.add(arrayContent.getString(j));
                                            img.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    navigationImageViewer((int) img.getTag());
                                                }
                                            });
                                        }
                                    }
                                    layoutMain.addView(viewHor);
                                }

                            }
                        }
                    }*/
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    /*private void setAdapterAddver() {
        mViewPager = (ViewPager) findViewById(R.id.view_pagerImage);
        mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        Custom_Adapter_Home_Addver adapter = new Custom_Adapter_Home_Addver(this, false);
        mViewPager.setAdapter(adapter);
        mIndicator.setViewPager(mViewPager);
       // pageSwitcher();
    }*/

    private void navigationImageViewer(int id) {
        if (listImageUrls.size() != 0) {
            Intent i = new Intent(this, Activity_ImageViewer.class);
            i.putExtra("name", name);
            i.putExtra("id", id);
            i.putExtra("number", contact);
            i.putStringArrayListExtra("imageList", listImageUrls);
            startActivity(i);
        }
    }

    private void downloadImag(String url,final ImageView img) {


        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        //imageLoader.displayImage("http://suptg.thisisnotatrueending.com/archive/7390790/thumbs/1262444458213s.jpg", img);
        imageLoader.loadImage(this, url, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted() {

            }

            @Override
            public void onLoadingFailed(FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(Bitmap bitmap) {
                img.setImageBitmap(bitmap);
            }

            @Override
            public void onLoadingCancelled() {

            }
        });

    }

    private boolean isPackageInstalled(String packagename, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }



}
