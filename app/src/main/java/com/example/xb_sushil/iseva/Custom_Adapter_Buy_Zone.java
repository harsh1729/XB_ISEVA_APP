package com.example.xb_sushil.iseva;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


public class Custom_Adapter_Buy_Zone extends BaseAdapter {

    private Context mContext;
    public Custom_Adapter_Buy_Zone(Context mContext){
        this.mContext = mContext;
    }
    @Override
    public int getCount() {
        return 10;
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
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_row_buy_zone, parent, false);
        }
        return convertView;
    }
}
