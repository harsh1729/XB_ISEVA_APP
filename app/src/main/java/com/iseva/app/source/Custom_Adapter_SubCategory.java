package com.iseva.app.source;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



import java.util.ArrayList;

/**
 * Created by com.bitsandbytes on 1/4/2016.
 */
public class Custom_Adapter_SubCategory extends BaseAdapter {

     private Context mContext;
    private ArrayList<Object_Category> listCate;

    public Custom_Adapter_SubCategory(Context mContext,ArrayList<Object_Category> list){
       this.mContext = mContext;
        this.listCate = list;

    }

    @Override
    public int getCount() {
        return listCate.size();
    }


    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_row_sub_category, parent, false);
            }
        ImageView img = (ImageView)convertView.findViewById(R.id.imgCate);

        TextView txtName = (TextView)convertView.findViewById(R.id.txtSubCatename);
       // Globals.setAppFontTextView(mContext, txtName);
        Globals.loadImageIntoImageView(img, listCate.get(position).image, mContext, R.drawable.default_offer, R.drawable.default_offer);
        txtName.setText(listCate.get(position).name);
         final int catid = listCate.get(position).catId;
         final int isbusiness = listCate.get(position).isbusiness;
        final String catName = listCate.get(position).name;
         convertView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 navigation(catid,isbusiness,catName);
             }
         });
         return convertView;
    }

    private void navigation(int catid,int isbusiness,String catName){
          if(isbusiness==0){
              Log.i("SUSHIL","business call id is "+catid);
              Intent i = new Intent(mContext,Activity_BusinessExtraShow.class);
              i.putExtra("array","");
              i.putExtra("bextraid", catid);
              Object_AppConfig obj = new Object_AppConfig(mContext);
              obj.setCateName(catName);
                      ((Activity) mContext).startActivity(i);
          }else{
              Intent i = new Intent(mContext, Activity_ServiceProvider.class);
              i.putExtra("object", "");
              i.putExtra("catid", catid);
              Object_AppConfig obj = new Object_AppConfig(mContext);
              obj.setCateName(catName);
              ((Activity) mContext).startActivity(i);
          }


        /*if(parentCatid!=4) {
            Intent i = new Intent(mContext, Activity_ServiceProvider.class);
            i.putExtra("catid", catid);
            ((Activity) mContext).startActivity(i);
        }else{
            Intent i = new Intent(mContext,Activity_BusinessExtraShow.class);
            i.putExtra("bextraid",catid);
            ((Activity)mContext).startActivity(i);
        }*/
    }


}
