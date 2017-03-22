package com.iseva.app.source.travel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.iseva.app.source.Activity_AdverImageView;
import com.iseva.app.source.Activity_City_Choose;
import com.iseva.app.source.Activity_Home;
import com.iseva.app.source.Activity_Splash;
import com.iseva.app.source.Custom_ConnectionDetector;
import com.iseva.app.source.Custom_URLs_Params;
import com.iseva.app.source.Custom_VolleyAppController;
import com.iseva.app.source.Custom_VolleyObjectRequest;
import com.iseva.app.source.Globals;
import com.iseva.app.source.Object_AppConfig;
import com.iseva.app.source.Object_BusinessExtraData;
import com.iseva.app.source.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Activity_first extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,BaseSliderView.OnSliderClickListener,ViewPagerEx.OnPageChangeListener{


    SliderLayout sliderLayout;
    private ArrayList<String> listImageUrls;

    LinearLayout bus_ticket_btn;
    LinearLayout my_city_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_activity);
        bus_ticket_btn = (LinearLayout)findViewById(R.id.activity_first_bus_ticket_btn);
        my_city_btn = (LinearLayout)findViewById(R.id.activity_first_my_city_btn);

        bus_ticket_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                booking_ticket();
            }
        });


        my_city_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                  Object_AppConfig config = new Object_AppConfig(Activity_first.this);
                if (config.getIsCitySelected()) {
                    Intent i = new Intent(Activity_first.this, Activity_Home.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(Activity_first.this, Activity_City_Choose.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.anim_in, R.anim.anim_none);
                }
            }
        });

        getAddver();

    }

    private void booking_ticket()
    {
        Intent i = new Intent(Activity_first.this,MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.anim_in, R.anim.anim_none);
    }

    private void getAddver() {
        Custom_ConnectionDetector cd = new Custom_ConnectionDetector(this);
        if (!cd.isConnectingToInternet()) {
            Globals.showAlert("ERROR", Globals.INTERNET_ERROR, this);
        } else {

            try {
                // pd = Globals.showLoadingDialog(pd, this, false, "");

                HashMap<String, String> map = new HashMap<>();
                map.put("imei", Globals.getdeviceId(this));

                Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                        Request.Method.POST,
                        Custom_URLs_Params.getURL_OffersRandom(),
                        map, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("SUSHIL", "json Response recieved !!" + response);
                        // Globals.hideLoadingDialog(pd);
                        adverParcer(response);

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

    private void adverParcer(JSONObject obj) {
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
                                CardView cv = (CardView) findViewById(R.id.card_viewHome);
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
    private void setAdapterAddver(ArrayList<Object_BusinessExtraData> offers) {
        Log.i("SUSHIL", "sushil list offers size is " + offers.size());
        if (offers.size() != 0) {
            CardView cv = (CardView) findViewById(R.id.card_viewHome);
            cv.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams lpCard = (LinearLayout.LayoutParams) cv.getLayoutParams();
            int width = Globals.getScreenSize(this).x;
            //height = height/3;
            lpCard.height = (int) (width * 0.60);
            cv.setLayoutParams(lpCard);

            listImageUrls = new ArrayList<>();
            for (int i = 0; i < offers.size(); i++)
            {
                listImageUrls.add(offers.get(i).images.get(0));
            }
            sliderLayout = (SliderLayout)findViewById(R.id.slider);

            for(int k =0;k<listImageUrls.size();k++)
            {
                TextSliderView textSliderView = new TextSliderView(Activity_first.this);
                textSliderView

                        .image(listImageUrls.get(k))
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(this);
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putString("extra",""+k);
                sliderLayout.addSlider(textSliderView);

            }


            sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            sliderLayout.setCustomAnimation(null);


            sliderLayout.setDuration(4000);
            sliderLayout.addOnPageChangeListener(this);
            sliderLayout.startAutoCycle();

            //Custom_Adapter_Home_Addver adapter = new Custom_Adapter_Home_Addver(this, false, offers);
            // mRollPagerView = (com.jude.rollviewpager.RollPagerView)findViewById(R.id.viewPager);
            //  mRollPagerView.setHintView(new ColorPointHintView(this, Color.WHITE, Color.BLACK));
            // mRollPagerView.setAdapter(adapter); // vikas



          /*  mViewPager = (ViewPager) findViewById(R.id.view_pager);
            mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
            Custom_Adapter_Home_Addver adapter = new Custom_Adapter_Home_Addver(this, false, offers);
            mViewPager.setOffscreenPageLimit(1);
            mViewPager.setAdapter(adapter);
            mIndicator.setViewPager(mViewPager);
            size = offers.size();
            pageSwitcher();*/
        } else {
            CardView cv = (CardView) findViewById(R.id.card_viewHome);
            cv.setVisibility(View.GONE);
        }
    }

    /**
     * Called when an item in the navigation menu is selected.
     *
     * @param item The selected item
     * @return true to display the item as the selected item
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        int position = Integer.parseInt(slider.getBundle().get("extra").toString());
        Intent i = new Intent(Activity_first.this,Activity_AdverImageView.class);
        i.putExtra("id",position);
        i.putStringArrayListExtra("imageList", listImageUrls);
        startActivity(i);
    }

    /**
     * This method will be invoked when the current page is scrolled, either as part
     * of a programmatically initiated smooth scroll or a user initiated touch scroll.
     *
     * @param position             Position index of the first page currently being displayed.
     *                             Page position+1 will be visible if positionOffset is nonzero.
     * @param positionOffset       Value from [0, 1) indicating the offset from the page at position.
     * @param positionOffsetPixels Value in pixels indicating the offset from position.
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * This method will be invoked when a new page becomes selected. Animation is not
     * necessarily complete.
     *
     * @param position Position index of the new selected page.
     */
    @Override
    public void onPageSelected(int position) {

    }

    /**
     * Called when the scroll state changes. Useful for discovering when the user
     * begins dragging, when the pager is automatically settling to the current page,
     * or when it is fully stopped/idle.
     *
     * @param state The new scroll state.
     * @see ViewPagerEx#SCROLL_STATE_IDLE
     * @see ViewPagerEx#SCROLL_STATE_DRAGGING
     * @see ViewPagerEx#SCROLL_STATE_SETTLING
     */
    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
