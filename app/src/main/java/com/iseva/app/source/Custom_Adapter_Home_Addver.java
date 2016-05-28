package com.iseva.app.source;

/**
 * Created by com.bitsandbytes on 12/18/2015.
 */

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;



import java.util.ArrayList;


public class Custom_Adapter_Home_Addver extends PagerAdapter {

    FragmentActivity activity;

   private boolean isTwo;
    private ArrayList<String> listImageUrls;
    ///int[] image = {R.drawable.xerces_logo, R.drawable.default_user, R.drawable.doctor, R.drawable.ic_action_back};
   private ArrayList<Object_BusinessExtraData> listOffres;
    public Custom_Adapter_Home_Addver(FragmentActivity activity
    ,boolean isTwo,ArrayList<Object_BusinessExtraData> offers) {
        this.activity = activity;
             this.isTwo = isTwo;
         this.listOffres = offers;
        listImageUrls = new ArrayList<>();
        for (int i=0;i<listOffres.size();i++){
            Object_BusinessExtraData obj = listOffres.get(i);
            listImageUrls.add(obj.images.get(0));
        }
    }

    @Override
    public int getCount() {
        return listOffres.size();

    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.vp_image, container, false);

        ImageView mImageView = (ImageView) view


                .findViewById(R.id.image_display);
       // mImageView.setBackgroundDrawable(activity.getResources().getDrawable(l));
        //Log.i("SUSHIL", "offers list size    " + listOffres.get(position).offersimage.size());
        Globals.loadImageIntoImageView(mImageView, listOffres.get(position).images.get(0), activity, R.drawable.default_add, R.drawable.default_add);
        mImageView.setClickable(true);
        mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
      // final int offersid = listOffres.get(position).id;
       /* mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOffres(offersid);
            }
        });*/
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navi(position);
            }
        });
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



    /*private void clickOffres(int id){
        Intent i = new Intent(activity,Activity_BusinessExtraShow.class);
        i.putExtra("type","home");
        i.putExtra("id",id);
        activity.startActivity(i);
    }*/

    private void navi(int position){
        Intent i = new Intent(activity,Activity_AdverImageView.class);
        i.putExtra("id",position);
        i.putStringArrayListExtra("imageList", listImageUrls);
        activity.startActivity(i);
    }
}
