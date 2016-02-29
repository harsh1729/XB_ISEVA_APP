package com.example.xb_sushil.iseva;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Activity_ServiceProviderDetails_Show extends FragmentActivity {
    //private ViewPager mViewPager;
    //private PageIndicator mIndicator;
    private ProgressDialog pd;
    private ArrayList<String> listImageUrls;
    private String name = "";
    private String contact = "";
    String json = "{'profile':{'url':'http://www.clipartlord.com/wp-content/uploads/2014/08/doctor11.png','name':'SUSHIL SOLANKI'},'control':[{'id':1,'typeId':1,'label':'Name',contant:['sushilzcdcvdzvzvzdvfzxdfvdvx" +
            "'],'sort_order':1},{'id':2,'typeId':1,'label':'Father name',contant:['sushilgfbhfdghgbfgbcvbdfghdd'],'sort_order':2},{'id':3,'typeId':1,'label':'Number',contant:['sushilfghdfghdgfbhgbdfghdgdghsgsdfgdfgdfgdfhsdfhdf'],'sort_order':3},{'id':4,'typeId':4,'label':'Image Profile',contant:['http://www.clipartlord.com/wp-content/uploads/2014/08/doctor11.png','http://www.clipartlord.com/wp-content/uploads/2014/08/doctor11.png','http://www.clipartlord.com/wp-content/uploads/2014/08/doctor11.png'],'sort_order':4}]}";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_show);
        //setAdapterAddver();
        inti();

        /*try{
            JSONObject obj = new JSONObject(json);
            createCustomViewer(obj);
        }catch (JSONException ex){
            ex.printStackTrace();
        }*/

    }

    private void inti() {
        listImageUrls = new ArrayList<>();
        TextView txtHeader = (TextView) findViewById(R.id.txtHeader);
        txtHeader.setText("Details");
        ImageView imgBack = (ImageView) findViewById(R.id.imgBack);
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
                callMerchant();
            }
        });
    }

    private void callMerchant() {
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
        Intent callIntent = new Intent(
                Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"
                + contact));
        try {
            this.startActivity(callIntent);
        } catch (SecurityException ex) {
            ex.printStackTrace();
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

                        //Custom_RoundedImageView imgDoctor = (Custom_RoundedImageView) findViewById(R.id.imgDoctor);
                        int totalContent = Globals.getScreenSize((Activity) this).x;
                        int imgWidth = totalContent - ((totalContent * 75) / 100);

                        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) img
                                .getLayoutParams();
                        lp.width = imgWidth;
                        lp.height = (int) (imgWidth * 1.1);
                        img.setLayoutParams(lp);


                        Globals.loadImageIntoImageView(img, objectImageProfile.getString("imageurl"), this, R.drawable.default_offer, R.drawable.default_offer);
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
                                final ImageView img = Custom_Control.getImageView(this, 130, 130, 1);
                                Globals.loadImageIntoImageView(img, objImage.getString("imageurl"), this, R.drawable.default_user, R.drawable.default_user);
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

                        } else {
                            cvImage.setVisibility(View.GONE);
                        }
                    }

                    if (obj.has("contactdetails")) {
                        LinearLayout linear = (LinearLayout) findViewById(R.id.linearAddress);
                        if (obj.getString("contactdetails").isEmpty()) {
                            linear.setVisibility(View.GONE);
                        } else {
                            linear.setVisibility(View.VISIBLE);
                            TextView txtDetails = (TextView) findViewById(R.id.txtContact);
                            txtDetails.setText(obj.getString("contactdetails"));
                        }
                    }

                    if (obj.has("services")) {
                        LinearLayout linear = (LinearLayout) findViewById(R.id.linearServices);
                        if (obj.getString("services").isEmpty()) {
                            linear.setVisibility(View.GONE);
                        } else {
                            linear.setVisibility(View.VISIBLE);
                            TextView txtDetails = (TextView) findViewById(R.id.txtServices);
                            txtDetails.setText(obj.getString("services"));
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
                                                    Globals.loadImageIntoImageView(img, objImage.getString("imageurl"), this, R.drawable.default_user, R.drawable.default_user);
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
            i.putExtra("number", "");
            i.putStringArrayListExtra("imageList", listImageUrls);
            startActivity(i);
        }
    }
}
