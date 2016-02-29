package com.example.xb_sushil.iseva;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by xb_sushil on 11/28/2015.
 */
public class Custom_Adapter_City extends BaseAdapter implements Filterable {

    Context mContext;
    public static HashMap<String, Integer> mapCity;

    private ArrayList<Object_City> originalList = null;
    private ArrayList<Object_City> filteredList = null;
    private CityFilter mFilter = new CityFilter();

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    // Gets the context so it can be used later
    public Custom_Adapter_City(Context c, ArrayList<Object_City> list) {
        mContext = c;
        this.originalList = list;
        this.filteredList = list;
        mapCity = new HashMap<String, Integer>();

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
    public View getView(final int position, View convertView, ViewGroup parent) {

        //if (convertView == null){

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.custom_row_city, parent, false);
        // }
        TextView txtName = (TextView) convertView.findViewById(R.id.txtcityName);
        txtName.setText(filteredList.get(position).name);

        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.cityCheck);
        checkBox.setChecked(filteredList.get(position).seLectedCity);
        if (filteredList.get(position).seLectedCity)
            mapCity.put(filteredList.get(position).name, filteredList.get(position).cityId);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (mapCity != null) {
                    Object_AppConfig config = new Object_AppConfig(mContext);
                    if (mapCity.size() != 0) {
                        config.setIsCitySelected(true);
                    } else {
                        config.setIsCitySelected(false);
                    }
            /*if (mapCity.get(filteredList.get(position).name) != null) {
                checkBox.setChecked(true);
            }*/
                }
                if (isChecked) {
                    if (mapCity.get(filteredList.get(position).name) == null) {
                        mapCity.put(filteredList.get(position).name, filteredList.get(position).cityId);

                    }
                } else {
                    mapCity.remove(filteredList.get(position).name);

                }
            }
        });
        //later city change
        if (mapCity != null) {
            Object_AppConfig config = new Object_AppConfig(mContext);
            if (mapCity.size() != 0) {
                config.setIsCitySelected(true);
            } else {
                config.setIsCitySelected(false);
            }
        }

        return convertView;
    }


    public Filter getFilter() {
        return mFilter;
    }

    private class CityFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Object_City> list = originalList;

            int count = list.size();
            final ArrayList<Object_City> nlist = new ArrayList<Object_City>(count);

            Object_City filterable;

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
            filteredList = (ArrayList<Object_City>) results.values;
            notifyDataSetChanged();
        }

    }


}
