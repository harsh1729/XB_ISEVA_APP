package com.example.xb_sushil.iseva;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Custom_Adapter_ServiceProvider extends BaseAdapter implements Filterable {

    private Context mContext;
    private ArrayList<Object_Service_Provider> originalList = null;
    private ArrayList<Object_Service_Provider> filteredList = null;
    private ServiceProviderFilter mFilter = new ServiceProviderFilter();
    /*private int mPosition;
    private View view;*/
    private int serviceId;


    // Gets the context so it can be used later
    public Custom_Adapter_ServiceProvider(Context c, ArrayList<Object_Service_Provider> list) {
        mContext = c;
        this.originalList = list;
        this.filteredList = list;

    }

    // Total number of things contained within the adapter
    public int getCount() {
        return filteredList.size();
    }

    // Require for structure, not really used in my code.
    public Object getItem(int position) {
        return null;
    }

    // Require for structure, not really used in my code. Can
    // be used to get the id of an item in the adapter for
    // manual control.
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

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
        final Object_Service_Provider obj = filteredList.get(position);

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
        if (obj.name != null)
            hd.txtName.setText(obj.name);
        if (obj.contact != null)
            hd.txtContact.setText(obj.contact);
        if (obj.address != null)
            hd.txtAddress.setText(obj.address);
        /*if (obj.ratingSeviceProvider != null)
            txtBrandRate.setText(obj.ratingSeviceProvider);
        else
            linearRatingbtn.setVisibility(View.GONE);*/
        if (obj.imageUrl != null) {
            //ImageView img = (ImageView) convertView.findViewById(R.id.imgBrand);

            Globals.loadImageIntoImageView(hd.img, obj.imageUrl, mContext,R.drawable.default_user,R.drawable.default_user);
        }

        final int userid = obj.id;
        // Now we can fill the layout with the right values
        hd.btnCall.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callMerchant(obj.contact);
            }
        });
        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                navigate(userid);
            }
        });


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
        Intent callIntent = new Intent(
                Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"
                + con));
        try {
            ((Activity) mContext).startActivity(callIntent);
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }


    }

    private void navigate(int userid) {
        Intent i = new Intent(mContext, Activity_ServiceProviderDetails_Show.class);
        i.putExtra("userid",userid);
        ((Activity) mContext).startActivity(i);
    }
    /*private class AccountFilter extends Filter {

		@SuppressLint("DefaultLocale")
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			// Create a FilterResults object
			FilterResults results = new FilterResults();

			// If the constraint (search string/pattern) is null
			// or its length is 0, i.e., its empty then
			// we just set the `values` property to the
			// original contacts list which contains all of them
			if (constraint == null || constraint.length() == 0) {
				results.values = Activity_chooseCity.listCity;
				results.count = Activity_chooseCity.listCity.size();
			} else {
				// Some search copnstraint has been passed
				// so let's filter accordingly
				ArrayList<Object_City> filteredCity = new ArrayList<Object_City>();

				// We'll go through all the contacts and see
				// if they contain the supplied string
				
				for (Object_City c : Activity_chooseCity.listCity) {
					if (c.name.toUpperCase().contains(
							constraint.toString().toUpperCase())) {
						filteredCity.add(c);
					}
				}

				// Finally set the filtered values and size/count
				results.values = filteredCity;
				results.count = filteredCity.size();
				Log.i("SUSHIL", "Filtered class return result");
			}

			// Return our FilterResults object
			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			listCity = (ArrayList<Object_City>) results.values;
			notifyDataSetChanged();
		}
	}

	@Override
	public Filter getFilter() {
		if (mFilter == null)
			mFilter = new AccountFilter();

		return mFilter;
	}
*/
	/*private void navigate(){

		Intent i = new Intent(mContext,
				Activity_scroll.class);
		((Activity) mContext).startActivity(i);


	}*/

    private void openDialog(float rating) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setTitle("Rating");
        dialog.setContentView(R.layout.dailog_layout);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        final RatingBar RatingBar = (RatingBar) dialog.getWindow().findViewById(R.id.ratingBar);
        Button btnDismiss = (Button) dialog.getWindow().findViewById(R.id.dismiss);
        RatingBar.setRating(rating);
        RatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

            }
        });
        btnDismiss.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                sendRating(RatingBar.getRating());
            }
        });

        dialog.show();
    }


    public Filter getFilter() {
        return mFilter;
    }

    private class ServiceProviderFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Object_Service_Provider> list = originalList;

            int count = list.size();
            final ArrayList<Object_Service_Provider> nlist = new ArrayList<Object_Service_Provider>(count);

            Object_Service_Provider filterable;

            for (int i = 0; i < count; i++) {
                filterable = list.get(i);
                if (filterable.name.toLowerCase().contains(filterString)) {
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
            filteredList = (ArrayList<Object_Service_Provider>) results.values;
            notifyDataSetChanged();
        }

    }

    private void sendRating(float rate) {
        try {

            Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                    Request.Method.POST,
                    Custom_URLs_Params.getURL_SendRating(),
                    Custom_URLs_Params.getParams_SendRating(mContext, serviceId, rate), new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.i("SUSHIL", "json Response recieved !!");
                    parseResponse(response);

                }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError err) {
                    Log.i("SUSHIL", "ERROR VolleyError");

                }
            });

            Custom_VolleyAppController.getInstance().addToRequestQueue(
                    jsonObjectRQST);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void parseResponse(JSONObject obj) {
        if (obj == null) {
            return;
        }

    }
}

