package com.example.xb_sushil.iseva;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class Activity_SubCategory extends FragmentActivity {
    private ViewPager mViewPager;
    private PageIndicator mIndicator;
    private Timer timer;
    private int pageIndex = 1;
    private int catid =-1;
    private ProgressDialog pd;
    private int size;
    private String json = "{'categories':[{'catid':'1','name':'Doctors','parentid':'0','sortorder':'1','isenable':'1','image':{'id':'163','name':'14515436061451544008.jpg','url':'http://northtexassmiles.com/wp-content/uploads/2015/10/NTS-Logo-Icon-Color-01.png','thumburl':'http://northtexassmiles.com/wp-content/uploads/2015/10/NTS-Logo-Icon-Color-01.png','datetime':'12:03:26 31-12-2015','size':'127388'," +
            "'width':'1600','height':'1067','userid':'1'}},{'catid':'2','name':'Hospitals','parentid':'1'," +
            "'sortorder':'1','isenable':'1','image':{'id':'164','name':'14515440241451544392.jpg'," +
            "'url':'http://northtexassmiles.com/wp-content/uploads/2015/10/NTS-Logo-Icon-Color-01.png'," +
            "'thumburl':'http://northtexassmiles.com/wp-content/uploads/2015/10/NTS-Logo-Icon-Color-01.png'," +
            "'datetime':'12:10:24 31-12-2015','size':'125566','width':'1772','height':'1181','userid':'1'}}],'offers':[{'id':1,'heading':'sushil','content':'vdfgvdgfhdf'," +
            "'image':['http://smallbiztrends.com/wp-content/uploads/2015/06/Small-Business-Trends-logo-400w.png']}" +
            ",{'id':2,'heading':'sushil','content':'vdfgvdgfhdf','image':['http://www.slh.com/Static/images/logos/logo-slh.svg']}]}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        inti();
        //categoryView();
        // getsubCateOffers();
        // responseCategory(null);


    }

    /*private void getsubCateOffers() {
        try {
            pd = Globals.showLoadingDialog(pd, this, false, "");
            Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                    Request.Method.POST,
                    Custom_URLs_Params.getURL_AllCity(),
                    Custom_URLs_Params.getParams_Category(this), new Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.i("SUSHIL", "json Response recieved !!");
                    Globals.hideLoadingDialog(pd);
                    responseCategory(response);
                }


            }, new ErrorListener() {
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
        }
    }*/


    private void getAdds() {
        Custom_ConnectionDetector cd = new Custom_ConnectionDetector(this);
        if (!cd.isConnectingToInternet()) {
            Globals.showAlert("ERROR", Globals.INTERNET_ERROR, this);
        } else {

            try {
                // pd = Globals.showLoadingDialog(pd, this, false, "");
                Object_AppConfig config = new Object_AppConfig(this);
                HashMap<String, String> map = new HashMap<>();
                map.put("catid",config.getCatId()+"");
                map.put("imei", Globals.getdeviceId(this));
                map.put("isbusiness",1+"");
                Log.i("SUSHIL","get adds map "+map);
                Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                        Request.Method.POST,
                        Custom_URLs_Params.getURL_OffersRandomcat(),
                        map, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("SUSHIL", "json Response recieved !!" + response);
                        // Globals.hideLoadingDialog(pd);
                        offersParcer(response);

                    }


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError err) {
                        Log.i("SUSHIL", "ERROR VolleyError");
                        //Globals.hideLoadingDialog(pd);
                    }
                });

                Custom_VolleyAppController.getInstance().addToRequestQueue(
                        jsonObjectRQST);
            } catch (Exception e) {
                e.printStackTrace();
                // Globals.hideLoadingDialog(pd);
            }
        }
    }


    private void offersParcer(JSONObject obj) {
       /* try {
            if (obj.has("offers")) {
                JSONArray offersArray = obj.getJSONArray("offers");
                ArrayList<Object_Offers> listoffers = new ArrayList<>();
                for (int i = 0; i < offersArray.length(); i++) {
                    JSONObject objoffersJson = offersArray.getJSONObject(i);
                    if (objoffersJson != null) {
                        Object_Offers objOffers = new Object_Offers();
                        objOffers.id = objoffersJson.getInt("id");
                        objOffers.heading = objoffersJson.getString("heading");
                        objOffers.content = objoffersJson.getString("content");
                        JSONArray offersArrayImage = objoffersJson.getJSONArray("image");
                        ArrayList<String> listImage = new ArrayList<>();
                        for (int j = 0; j < offersArrayImage.length(); j++) {
                             JSONObject objImage = offersArrayImage.getJSONObject(j);
                            String url = objImage.getString("imageurl");
                            if (url != null && !url.isEmpty()) {
                                //downloadImage(url);
                                listImage.add(url);
                            }

                        }
                        objOffers.offersimage = listImage;
                        listoffers.add(objOffers);
                    }

                }
                setAdapterAddver(listoffers);

            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }*/

        if (obj == null) {
            return;
        } else {
            try {
                if (obj.has("success")) {
                    if (obj.getInt("success") == 1) {
                        JSONArray array = obj.getJSONArray("advertisement");
                        if (array != null) {
                            if (array.length() != 0) {
                                ArrayList<Object_BusinessExtraData> list = new ArrayList<>();
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);
                                    if (object != null) {
                                        Object_BusinessExtraData objOffers = new Object_BusinessExtraData();
                                        if (object.has("id")) {
                                            objOffers.id = object.getInt("id");
                                        }
                                        /*if (object.has("heading")) {

                                            objOffers.heading = object.getString("heading");

                                        }*/
                                        if (object.has("content")) {
                                            objOffers.content = object.getString("content");
                                        }
                                        if (object.has("image")) {
                                            ArrayList<String> listImage = new ArrayList<>();
                                            listImage.add(object.getJSONObject("image").getString("imageurl"));
                                            /*JSONArray imageArray = object.getJSONArray("images");
                                            ArrayList<String> listImage = new ArrayList<>();
                                            for (int j = 0; j < imageArray.length(); j++) {
                                                // String url = imageArray.getString(j);
                                                JSONObject objImage = imageArray.getJSONObject(j);
                                                String url = objImage.getString("imageurl");
                                                if (url != null) {
                                                    listImage.add(url);
                                                }
                                            }*/
                                            objOffers.images = listImage;
                                        }
                                        list.add(objOffers);
                                    }
                                }
                                setAdapterAddver(list);
                            } else {
                                CardView cv = (CardView) findViewById(R.id.card_viewSub);
                                cv.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


   /* private void getsubCategory(int cateId) {
        try {
            pd = Globals.showLoadingDialog(pd, this, false, "");
            HashMap<String, String> map = new HashMap<String, String>();

            *//*Custom_VolleyArrayRequest jsonArrayRQST = new Custom_VolleyArrayRequest(Custom_URLs_Params.getURL_AllCity(), map,
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

            Custom_VolleyAppController.getInstance().addToRequestQueue(jsonArrayRQST);*//*
            map.put("parentid", cateId + "");
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
                            Activity_SubCategory.this,
                            Globals.MSG_SERVER_ERROR);

                }
            });

            Custom_VolleyAppController.getInstance().addToRequestQueue(
                    jsonObjectRQST);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    private void parseSubCateGory(String arrayCategory) {

            try {
                 JSONObject object = new JSONObject(arrayCategory);
                 JSONArray array = object.getJSONArray("categories");
                  ArrayList<Object_Category> listSubCate = new ArrayList<>();

                     for (int i = 0; i < array.length(); i++) {
                            try {
                                JSONObject obj = array.getJSONObject(i);
                                Object_Category cate = new Object_Category();

                                cate.catId = obj.getInt("catid");
                                cate.name = obj.getString("name");
                                cate.isbusiness = obj.getInt("isbusiness");
                                if (obj.has("image")) {
                                    JSONObject objImage = obj.getJSONObject("image");
                                    cate.image = objImage.getString("thumburl");
                                }

                                listSubCate.add(cate);
                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }
                        }
                        Custom_Adapter_SubCategory adapter = new Custom_Adapter_SubCategory(this, listSubCate);
                        ListView listView = (ListView) findViewById(R.id.listSub);
                        listView.setAdapter(adapter);


            } catch (JSONException ex) {
                ex.printStackTrace();
            }


    }


   /* private void responseCategory(JSONObject obj) {

       *//* if (obj == null) {
            return;
        } else {*//*
        //json
        try {
            obj = new JSONObject(json);
            if (obj.has("categories")) {
                JSONArray cateArray = obj.getJSONArray("categories");
                ArrayList<Object_Category> listCate = new ArrayList<>();
                for (int i = 0; i < cateArray.length(); i++) {
                    JSONObject objCateJson = cateArray.getJSONObject(i);
                    if (objCateJson != null) {

                        Object_Category objCate = new Object_Category();
                        objCate.catId = objCateJson.getInt("catid");
                        objCate.name = objCateJson.getString("name");
                        objCate.parentId = objCateJson.getInt("parentid");
                        objCate.sort_order = objCateJson.getInt("sortorder");
                        objCate.isnable = objCateJson.getInt("isenable");
                        if (objCateJson.getJSONObject("image") != null) {
                            String url = objCateJson.getJSONObject("image").getString("url");
                            if (url != null && !url.isEmpty()) {
                                //downloadImage(url);
                                objCate.image = url;
                            }
                        }
                        listCate.add(objCate);
                    }

                }
                Custom_Adapter_SubCategory adapter = new Custom_Adapter_SubCategory(this, listCate,catid);
                ListView listView = (ListView) findViewById(R.id.listSub);
                listView.setAdapter(adapter);

            }
            if (obj.has("offers")) {

                JSONArray offersArray = obj.getJSONArray("offers");
                Log.i("SUSHIL", "offers in sub   " + offersArray);
                ArrayList<Object_BusinessExtraData> listoffers = new ArrayList<>();
                for (int i = 0; i < offersArray.length(); i++) {
                    JSONObject objoffersJson = offersArray.getJSONObject(i);
                    if (objoffersJson != null) {

                        Object_BusinessExtraData objOffers = new Object_BusinessExtraData();
                        objOffers.id = objoffersJson.getInt("id");
                        objOffers.heading = objoffersJson.getString("heading");
                        objOffers.content = objoffersJson.getString("content");
                        JSONArray offersArrayImage = objoffersJson.getJSONArray("image");
                        ArrayList<String> listImage = new ArrayList<>();
                        for (int j = 0; j < offersArrayImage.length(); j++) {

                            String url = offersArrayImage.getString(j);
                            if (url != null && !url.isEmpty()) {
                                //downloadImage(url);
                                listImage.add(url);
                            }

                        }
                        objOffers.images = listImage;
                        listoffers.add(objOffers);
                    }

                }

                setAdapterAddver(listoffers);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        //}
    }*/


   /* private void categoryView(){
        int width = Globals.getScreenSize(this).x/2;
        // int heightLinear = Globals.getScreenSize(this).y/3;
        int height = (int)(Globals.getScreenSize(this).y/3.5);
        LinearLayout scrollView = (LinearLayout)findViewById(R.id.scRollLinearSub);
        for (int i=0;i<11;i++){
            LinearLayout layout = new LinearLayout(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(Globals.getScreenSize(this).x, LinearLayout.LayoutParams.WRAP_CONTENT);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setLayoutParams(lp);
            if(i%2==0) {
                View view = getLayoutInflater().inflate(R.layout.custom_card_view_home, layout, false);
                ImageView img = (ImageView) view.findViewById(R.id.imageViewCate_Home);
                LinearLayout.LayoutParams lpLinear = new LinearLayout.LayoutParams(width-4, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams lpImg = new LinearLayout.LayoutParams(width, height);
                img.setLayoutParams(lpImg);
                view.setLayoutParams(lpLinear);
                layout.addView(view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goServiceProvider();
                    }
                });
                i++;
                if(i<11) {
                    View view1 = getLayoutInflater().inflate(R.layout.custom_card_view_home, layout, false);
                    ImageView img1 = (ImageView) view1.findViewById(R.id.imageViewCate_Home);
                    LinearLayout.LayoutParams lpLinear1 = new LinearLayout.LayoutParams(width-4, LinearLayout.LayoutParams.WRAP_CONTENT);
                    LinearLayout.LayoutParams lpImg1 = new LinearLayout.LayoutParams(width, height);
                    img1.setLayoutParams(lpImg1);
                    view1.setLayoutParams(lpLinear1);
                    layout.addView(view1);
                    view1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goServiceProvider();
                        }
                    });
                }

            }

            scrollView.addView(layout);
        }
    }*/



    private void inti() {
        //Intent i = getIntent();
        Object_AppConfig objConfig = new Object_AppConfig(this);
        TextView txtHeader = (TextView) findViewById(R.id.txtHeader);
        txtHeader.setText(objConfig.getCatName());//("Category");
        ImageView imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity_SubCategory.this.finish();
            }
        });

       // Log.i("SUSHIL", "cat id is !!" + i.getIntExtra("id", -1));
        /*if (i.getIntExtra("id", -1) != -1){
            catid = i.getIntExtra("id", -1);
            getsubCategory(i.getIntExtra("id", -1));

        }*/
        Intent intent = getIntent();
        parseSubCateGory(intent.getStringExtra("category"));


        getAdds();


    }

    private void setAdapterAddver(ArrayList<Object_BusinessExtraData> offers) {
        if (offers.size() != 0) {
            CardView cv = (CardView) findViewById(R.id.card_viewSub);
            cv.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams lpCard = (LinearLayout.LayoutParams) cv.getLayoutParams();
            int height = Globals.getScreenSize(this).y;
            height = height/3;
            lpCard.height = height;
            cv.setLayoutParams(lpCard);
            mViewPager = (ViewPager) findViewById(R.id.view_pagerSub);
            mIndicator = (CirclePageIndicator) findViewById(R.id.indicatorSub);
            Custom_Adapter_Home_Addver adapter = new Custom_Adapter_Home_Addver(this, true, offers);
            mViewPager.setAdapter(adapter);
            mIndicator.setViewPager(mViewPager);
            size = offers.size();
            pageSwitcher();
        } else {
            CardView cv = (CardView) findViewById(R.id.card_viewSub);
            cv.setVisibility(View.GONE);
        }
    }

    public void pageSwitcher() {
        timer = new Timer(); // At this line a new Thread will be created
        timer.scheduleAtFixedRate(new RemindTask(), 0, 3000); // delay
        // in
        // milliseconds
    }

    // this is an inner class...
    class RemindTask extends TimerTask {

        @Override
        public void run() {

            // As the TimerTask run on a seprate thread from UI thread we have
            // to call runOnUiThread to do work on UI thread.
            runOnUiThread(new Runnable() {
                public void run() {

                    /*if (page > 4) { // In my case the number of pages are 5
                        timer.cancel();
                        // Showing a toast for just testing purpose
                        Toast.makeText(getApplicationContext(), "Timer stoped",
                                Toast.LENGTH_LONG).show();
                    } else {*/

                    // }
                    if (pageIndex <= size) {
                        mViewPager.setCurrentItem(pageIndex);
                        pageIndex++;
                    } else {
                        mViewPager.setCurrentItem(0);
                        pageIndex = 1;
                    }

                }
            });

        }
    }
}
