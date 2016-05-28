package com.iseva.app.source;

import java.util.ArrayList;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class Custom_AdapterEpaper extends BaseAdapter  {
	
	private final ArrayList<Object_Epaper> listNews;
	public LayoutInflater inflater;
	public Activity activity;
	
	private final int DEFAULT_PADDING_ROW = 8;
	private  int rowHeight = 90;
	
	public Custom_AdapterEpaper(Activity act,
			ArrayList<Object_Epaper> list) {
		activity = act;
		listNews = list;
		inflater = act.getLayoutInflater();
		
		rowHeight = Globals.getScreenSize(activity).y/3;
	}


	@Override
	public int getCount() {
		if (listNews != null)
			return listNews.size();

		return 0;
	}


	@Override
	public Object getItem(int pos) {
		if (listNews != null && listNews.size() > pos)
			return listNews.get(pos);

		return null;
	}


	@Override
	public long getItemId(int groupPos) {
		if (listNews != null && listNews.size() > groupPos)
			if (listNews.get(groupPos) != null)
				return listNews.get(groupPos).id;
		return 0;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		
		Log.i("DARSH", "getView for "+position);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.row_item_epaper, null);
		}

		
		Object obj = getItem(position);
		Object_Epaper epaper;

		if (obj != null)
			epaper = (Object_Epaper) obj;
		else
			return convertView;
		

		ImageView img = (ImageView) convertView
				.findViewById(R.id.imgViewEpaperThumb);
		
		TextView txt  = (TextView)convertView
				.findViewById(R.id.txt_GroupText);
		
		if(epaper.url != null && !epaper.url.trim().isEmpty())
			Globals.loadImageIntoImageView(img, epaper.url, activity, R.drawable.default_offer, R.drawable.default_offer);
		else
			img.setImageResource(R.drawable.default_offer);
		txt.setText("Page "+(position +1));

		img.getLayoutParams().height = rowHeight;
		if(position == 0)
			convertView.setPadding(DEFAULT_PADDING_ROW , DEFAULT_PADDING_ROW, DEFAULT_PADDING_ROW, DEFAULT_PADDING_ROW/2);
		else
			convertView.setPadding(DEFAULT_PADDING_ROW , DEFAULT_PADDING_ROW/2, DEFAULT_PADDING_ROW, DEFAULT_PADDING_ROW/2);
		
		Log.i("HARSH","getGroupView " + position);
		
		return convertView;
	}

	

}
