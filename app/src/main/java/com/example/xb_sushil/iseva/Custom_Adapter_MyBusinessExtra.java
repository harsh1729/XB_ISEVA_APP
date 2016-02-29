package com.example.xb_sushil.iseva;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xb_sushil on 1/8/2016.
 */
public class Custom_Adapter_MyBusinessExtra extends BaseAdapter implements Filterable {


    private ProgressDialog pd;
    Context mContext;
    private ArrayList<Object_BusinessExtraData> originalList = null;
    private ArrayList<Object_BusinessExtraData> filteredList = null;
    private OffersFilter mFilter = new OffersFilter();

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     *
     */
    // Gets the context so it can be used later
    public Custom_Adapter_MyBusinessExtra(Context c, ArrayList<Object_BusinessExtraData> list) {
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

        if(convertView==null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_row_offers, parent, false);
        }

        TextView txtNameOffers = (TextView)convertView.findViewById(R.id.txtNameOffers);
        //TextView txtContent = (TextView)convertView.findViewById(R.id.txtContent);

        //Globals.setAppFontTextView(mContext,txtContent);
        Globals.setAppFontTextView(mContext, txtNameOffers);
        final ImageView imgOffers = (ImageView)convertView.findViewById(R.id.imgOffers);
        int totalContent = Globals.getScreenSize((Activity) mContext).x;
        int imgWidth = totalContent;
        // - ((totalContent * 75) / 100)


        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) imgOffers
                .getLayoutParams();
        lp.width = imgWidth;
        lp.height = (int) (imgWidth * 0.3);

        imgOffers.setLayoutParams(lp);
       final Object_BusinessExtraData obj = filteredList.get(position);
        if(obj.heading!=null){
            txtNameOffers.setText(obj.heading);
        }
        /*if(obj.content!=null){
            txtContent.setText(obj.content);
        }*/
        if(obj.images.size()!=0){
            String url = obj.images.get(0);
            Globals.loadImageIntoImageView(imgOffers,url,mContext,R.drawable.default_offer,R.drawable.default_offer);
        }

          Log.i("SUSHIL","id is "+obj.id);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dailog(obj.id);
            }
        });

        return convertView;
    }

    /*private void navigationOffers(View v){

        Intent i = new Intent(mContext,Activity_dataBase.class);
        ((Activity) mContext).startActivity(i);


    }*/
    private void dailog(final int idMAin){

        final CharSequence[] items = {"Remove"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Choose Action!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (items[item].equals("Remove")){

                        Globals.showAlertDialog(
                                "Alert",
                                "Are you sure to Delete?",
                                mContext,
                                "Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        remove(idMAin);
                                    }
                                }, "No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        return;
                                    }
                                }, false);

                    }
                 }
            });
            builder.show();

    }

    public void remove(int id){
        Custom_ConnectionDetector cd = new Custom_ConnectionDetector(mContext);
        if (!cd.isConnectingToInternet()) {
            Globals.showAlert("ERROR", Globals.INTERNET_ERROR, mContext);
        } else {

            try {
                pd = Globals.showLoadingDialog(pd, (Activity)mContext, false, "");
                HashMap<String,String> map = new HashMap<>();
                map.put("id",id+"");
                Log.i("SUSHIL","SUSHIL MAP FOR DELETE "+map);
                Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                        Request.Method.POST,
                        Custom_URLs_Params.getURL_BusinessExtraDelete(),
                        map, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("SUSHIL", "json Response recieved !!");
                        Globals.hideLoadingDialog(pd);
                        responce(response);
                       // offersResponse(response);


                    }


                }, new Response.ErrorListener() {
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
                Globals.hideLoadingDialog(pd);
            }
        }
    }

   private void responce(JSONObject obj){
       if(obj!=null){
           if(obj.has("success")){
               Globals.showShortToast(mContext,"Successfully delete");
                       ((Activity) mContext).finish();
           }
       }
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
