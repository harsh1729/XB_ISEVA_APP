package com.iseva.app.source;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.List;

/**
 * Created by com.bitsandbytes on 11/28/2015.
 */
public class Custom_Adapter_BusinessExtra extends BaseAdapter {

    Context mContext;
    private ArrayList<Object_BusinessExtraData> originalList = null;
    private ArrayList<Object_BusinessExtraData> filteredList = null;
    private OffersFilter mFilter = new OffersFilter();

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    // Gets the context so it can be used later
    public Custom_Adapter_BusinessExtra(Context c, ArrayList<Object_BusinessExtraData> list) {
        mContext = c;
        this.originalList = list;
        this.filteredList = list;
        loadImage();

    }

    @Override
    public int getCount() {
        return filteredList.size();
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

        Holder_ServiceProvider hd;
        if (convertView == null) {
            // This a new view we inflate the new layout
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_row_iservice_provider, parent, false);

            hd = new Holder_ServiceProvider();

            hd.txtName = (TextView) convertView.findViewById(R.id.txtBrandName);
            hd.txtAddress = (TextView) convertView.findViewById(R.id.txtBrandAddress);
            hd.txtContact = (TextView) convertView.findViewById(R.id.txtBrandQuality);
            hd.img = (ImageView) convertView.findViewById(R.id.imgBrand);
            hd.btnCall = (Button) convertView.findViewById(R.id.buttonCall);
            int totalContent = Globals.getScreenSize((Activity) mContext).x;
            int imgWidth = totalContent - ((totalContent * 75) / 100);


            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) hd.img
                    .getLayoutParams();
            lp.width = imgWidth;
            lp.height = (int) (imgWidth * 1.3);

            hd.img.setLayoutParams(lp);
            convertView.setTag(hd);
        }else{
            hd = (Holder_ServiceProvider)convertView.getTag();
        }
        //final LinearLayout linearRating = (LinearLayout)convertView.findViewById(R.id.linearratingView);
        //final View viewMain= convertView;
        final Object_BusinessExtraData obj = filteredList.get(position);

       /* LinearLayout linearRatingbtn = (LinearLayout) convertView.findViewById(R.id.ratingLinearunder);
        linearRatingbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    serviceId = obj.id;
                    if (obj.userrate != 0) {
                        openDialog((float) 3.5);
                    } else {
                        openDialog(0);
                    }

                } catch (Exception e) {
                    openDialog(0);
                    e.printStackTrace();
                }
            }
        });*/
        /*Button btnCall = (Button) convertView.findViewById(R.id.buttonCall);
        TextView txtBrandName = (TextView) convertView.findViewById(R.id.txtBrandName);
        TextView txtBrandQua = (TextView) convertView.findViewById(R.id.txtBrandQuality);
        TextView txtBrandAdd = (TextView) convertView.findViewById(R.id.txtBrandAddress);*/
        //TextView txtBrandRate = (TextView) convertView.findViewById(R.id.rate);

        /*Globals.setAppFontTextView(mContext, txtBrandName);
        Globals.setAppFontTextView(mContext, txtBrandQua);
        Globals.setAppFontTextView(mContext, txtBrandAdd);*/
        //Globals.setAppFontTextView(mContext, txtBrandRate);
        // txtBrandName.setTypeface(null, Typeface.BOLD);
        if (obj.heading != null)
            hd.txtName.setText(obj.heading);
        /*if (obj.contact != null)
            hd.txtContact.setText(obj.contact);*/
        if (obj.content != null)
            hd.txtAddress.setText(obj.content);
        /*if (obj.ratingSeviceProvider != null)
            txtBrandRate.setText(obj.ratingSeviceProvider);
        else
            linearRatingbtn.setVisibility(View.GONE);*/
        if (obj.images.size() != 0) {
            //ImageView img = (ImageView) convertView.findViewById(R.id.imgBrand);
            Globals.loadImageIntoImageView(hd.img, obj.images.get(0), mContext,R.drawable.default_offer,R.drawable.default_offer);
        }
        hd.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callMerchant(obj.contact);
            }
        });


        //Holder_BusinessExtra hb;
        /*if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_row_offers, parent, false);
            hb = new Holder_BusinessExtra();
            hb.txtHeading = (TextView) convertView.findViewById(R.id.txtNameOffers);
            hb.img = (ImageView) convertView.findViewById(R.id.imgOffers);
            int imgWidth = Globals.getScreenSize((Activity) mContext).x;
            //int imgWidth = totalContent;
            // - ((totalContent * 75) / 100)


            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) hb.img
                    .getLayoutParams();
            lp.width = imgWidth;
            lp.height = (int) (imgWidth * 0.5);

            hb.img.setLayoutParams(lp);
           convertView.setTag(hb);
    }

    else

    {
        hb = (Holder_BusinessExtra) convertView.getTag();
    }

    //TextView txtNameOffers = (TextView)convertView.findViewById(R.id.txtNameOffers);
    //TextView txtContent = (TextView)convertView.findViewById(R.id.txtContent);

       *//* Globals.setAppFontTextView(mContext,txtContent);
        Globals.setAppFontTextView(mContext, txtNameOffers);*//*

    final Object_BusinessExtraData obj = filteredList.get(position);
    if(obj.heading!=null)

    {
        hb.txtHeading.setText(obj.heading);
    }
       *//* if(obj.content!=null){
            txtContent.setText(obj.content);
        }*//*
    if(obj.images.size()!=0)
   {
        String url = obj.images.get(0);
        Globals.loadImageIntoImageView(hb.img, url, mContext, R.drawable.default_offer, R.drawable.default_offer);
    }
*/
    final int id = obj.id;
    convertView.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View v){

        navigationOffers(v, id, obj.masterid);
    }
    }

    );

    return convertView;
}


    private void callMerchant(final String con) {
        Globals.showAlertDialog(
                "Alert",
                "Are you sure to call?",
                mContext,
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int id) {
                        callIntent(con);
                    }
                }, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int id) {
                        return;
                    }
                }, false);
    }


    private void callIntent(String con) {
        // Call_PhoneListener cList = new Call_PhoneListener(mContext);

        // cList.registerNumber(Globals.getSimnumber(mContext));
        Intent callIntent = new Intent(
                Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"
                + con));
        // callIntent.putExtra("com.android.phone.extra.slot", 0);
        // callIntent.putExtra("com.android.phone.extra.slot", 0);
        try {
            ((Activity) mContext).startActivity(callIntent);

        } catch (SecurityException ex) {
            ex.printStackTrace();
        }


    }


    private void navigationOffers(View v, int id, int masterid) {

        Intent i = new Intent(mContext, Activity_BusinessExtraDetails_Show.class);
        i.putExtra("bmasterid", masterid);
        i.putExtra("id", id);
        ((Activity) mContext).startActivity(i);


    }

    public Filter getFilter() {
        return mFilter;
    }

private class OffersFilter extends Filter {
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {

        String filterString = constraint.toString().toLowerCase();

        FilterResults results = new FilterResults();

        final List<Object_BusinessExtraData> list = originalList;

        int count = list.size();
        final ArrayList<Object_BusinessExtraData> nlist = new ArrayList<Object_BusinessExtraData>(count);

        Object_BusinessExtraData filterable;

        for (int i = 0; i < count; i++) {
            filterable = list.get(i);
            if (filterable.heading.toLowerCase().contains(filterString)) {
                nlist.add(filterable);
            }
        }

        results.values = nlist;
        results.count = nlist.size();

        return results;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        filteredList = (ArrayList<Object_BusinessExtraData>) results.values;
        notifyDataSetChanged();
    }

   }

    private void loadImage(){
        if(filteredList==null && filteredList.size()==0){
            return;
        }else{
            for (int i=0;i<filteredList.size();i++){
                Object_BusinessExtraData obj = filteredList.get(i);
                if(obj!=null){
                    if(obj.images.size()!=0){
                        Globals.preloadImage(mContext,obj.images.get(0));
                    }
                }
            }
        }
    }

}
