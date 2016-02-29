package com.example.xb_sushil.iseva;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xb_sushil on 11/28/2015.
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
        Holder_BusinessExtra hb;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_row_offers, parent, false);
            hb = new Holder_BusinessExtra();
            hb.txtHeading = (TextView) convertView.findViewById(R.id.txtNameOffers);
            hb.img = (ImageView) convertView.findViewById(R.id.imgOffers);
            int totalContent = Globals.getScreenSize((Activity) mContext).x;
            int imgWidth = totalContent;
            // - ((totalContent * 75) / 100)


            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) hb.img
                    .getLayoutParams();
            lp.width = imgWidth;
            lp.height = (int) (imgWidth * 0.3);

            hb.img.setLayoutParams(lp);
           convertView.setTag(hb);
    }

    else

    {
        hb = (Holder_BusinessExtra) convertView.getTag();
    }

    //TextView txtNameOffers = (TextView)convertView.findViewById(R.id.txtNameOffers);
    //TextView txtContent = (TextView)convertView.findViewById(R.id.txtContent);

       /* Globals.setAppFontTextView(mContext,txtContent);
        Globals.setAppFontTextView(mContext, txtNameOffers);*/

    final Object_BusinessExtraData obj = filteredList.get(position);
    if(obj.heading!=null)

    {
        hb.txtHeading.setText(obj.heading);
    }
       /* if(obj.content!=null){
            txtContent.setText(obj.content);
        }*/
    if(obj.images.size()!=0)

    {
        String url = obj.images.get(0);
        Globals.loadImageIntoImageView(hb.img, url, mContext, R.drawable.default_offer, R.drawable.default_offer);
    }

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


}
