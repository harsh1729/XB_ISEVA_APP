package com.iseva.app.source;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.iseva.app.slidingmenu.SlidingMenu;
import com.iseva.app.slidingmenu.app.SlidingFragmentActivity;
import com.iseva.app.zoomslidersample.ImageSource;
import com.iseva.app.zoomslidersample.SubsamplingScaleImageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONObject;

public class Activity_EPaperReader extends SlidingFragmentActivity {

	public ProgressDialog mDialog;
	private ListView expListPapers;
	private SlidingMenu slidingMenu;
	SubsamplingScaleImageView imageViewEpaper ;
	ArrayList<Object_Epaper> list;
	//static Object_Cities selectedCity;
	String image_url;
	int currentPos = 1;
	private int totalpages = 0;
	private String thumb_image_url = "";
	private String pdf_url = "";
	private int userid;
	TextView txtDate;
	private int year;
	private int month;
	private int day;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_epaper_reader);
		setBehindContentView(R.layout.container_expandable_list_epaper);
		initReader();
	}



	private void unbindDrawables(View view) {
		if (view.getBackground() != null) {
			view.getBackground().setCallback(null);
		}
		if (view instanceof ViewGroup) {
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
				unbindDrawables(((ViewGroup) view).getChildAt(i));
			}
			((ViewGroup) view).removeAllViews();
		}
	}


	private void initReader(){
		Intent i = getIntent();
		userid = i.getIntExtra("userid", -1);
		//if ( != -1)
		txtDate = (TextView)findViewById(R.id.txtDate);
		ImageButton imgButtonToggle = (ImageButton)(findViewById(R.id.imgHeaderBtnLeft));

		imgButtonToggle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickToggle(v);
			}
		});


		//Custom_ThemeUtil.onActivityCreateSetTheme(this);

		TextView txtHeading = ((TextView)findViewById(R.id.txtHeader));
		txtHeading.setText("Epaper");//
		
		txtHeading.setPadding(30, 0, 0, 0);
		
		imageViewEpaper = (SubsamplingScaleImageView)findViewById(R.id.imageViewEpaper);
		
		mDialog = Globals.showLoadingDialog(mDialog, this,false,"");

		expListPapers = (ListView) findViewById(R.id.expListPapers);
		expListPapers.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int pos, long id) {
				onClickNewsPage(pos+1);
			}
			
		});
		
		int slidingWidth = Globals.getScreenSize(this).x*2/5;
		slidingMenu = getSlidingMenu();
		//slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		slidingMenu.setShadowDrawable(R.drawable.shadow_sliding_menu);
		slidingMenu.setFadeDegree(0.85f);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu.setBehindWidth(slidingWidth);


		ImageView imagedate = (ImageView)findViewById(R.id.imgHeaderBtnRight);
		imagedate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// showDialog(DATE_DIALOG_ID);
				setDate();

			}
		});

		txtDate.setClickable(true);
		txtDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setDate();
			}
		});
		
		//configureEpaper();
		getEPaperDetailsFromServer("");
		
	}

	private void setDate(){
		DatePickerDialog dpd = new DatePickerDialog(this,
				new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int selectedYear,
										  int selectedMonth, int selectedDay) {
                      /* txtDate.setText(dayOfMonth + "-"
                               + (monthOfYear + 1) + "-" + year);*/
						year = selectedYear;
						month= selectedMonth;
						day = selectedDay;
						//txtDate.setText(stringComplete(year)+"-"+stringComplete(month+1)+"-"+stringComplete(day));
						//txtDate.setText(stringComplete(day)+"-"+stringComplete(month+1)+"-"+stringComplete(year));
						txtDate.setText(Globals.getdateFormat(day, month + 1, year));
						//getQuesFromServer(true);
						getEPaperDetailsFromServer(stringComplete(year)+"-"+stringComplete(month+1)+"-"+stringComplete(day));
					}
				}, year, (month), day);
		dpd.show();
	}

	private void getDayMonYear(String date){
		try {
			String str[] = date.split("-");
			year = Integer.parseInt(str[0]);
			month = Integer.parseInt(str[1]);
			day = Integer.parseInt(str[2]);
			month = month-1;
			Log.i("SUSHIL","month is "+year+" "+month+" "+day);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	private String stringComplete(int c){
		String st = "";
		if(c<10){
			st = "0"+c;
		}else{
			st = c+"";
		}
		return st;
	}



	public void onClickPrev(View v){
		if(currentPos > 1)
		{
			onClickNewsPage(currentPos - 1);
			
		}
	}
	
	public void onClickNext(View v){
		
		if(currentPos < totalpages)
		{
			onClickNewsPage(currentPos + 1);
			
		}
	}

	
	
	
	private void configureEpaper() {

		try{
			 list = new ArrayList<Object_Epaper>();
			
				
				for(int i=1; i <= totalpages; i++)
				{
					Object_Epaper ob = new Object_Epaper();
					ob.url = getEpaperThumbImageUrl(i, thumb_image_url);
					ob.id = i;
					list.add(ob);
				}
				
			if(list.size() > 0)
				setSlidingMenu(list);
			else
				Toast.makeText(this, "No paper found !", Toast.LENGTH_SHORT).show();
			
		}catch(Exception ex){
			
		}
		
	}
	
	private String getEpaperThumbImageUrl(int pos , String url){
		
		return url.replace("_1.jpg", "_"+pos+".jpg");
	}
	
private String getEpaperBigImageUrl(int pos , String url){
		
		return url.replace(".pdf", "_"+pos+".jpg");
	}
	
	public void onClickNewsPage(int pos) {

		if(totalpages >= pos){
			
			currentPos = pos;			
			image_url = getEpaperBigImageUrl(pos, pdf_url);
			this.showContent();
				
			mDialog = Globals.showLoadingDialog(mDialog, this, false,"");
			Bitmap bitImage = loadFromExternalMemory(image_url);
			if( bitImage != null){
				showImage(bitImage);
				Log.i("DARSH", "Image Found");
			}else{
				Picasso.with(this).load(image_url).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(target);		//.downloader(new OkHttpDownloader(this))
			}
				
			setNextPrevButtonsState();	
		}   
	}
	
	private void setNextPrevButtonsState(){
		ImageButton imgBtnNext = (ImageButton)findViewById(R.id.imgBtnNext);
		ImageButton imgBtnPrev = (ImageButton)findViewById(R.id.imgBtnPrev);
		
		if(currentPos == 1){
			imgBtnPrev.setVisibility(View.GONE);
		}else{
			imgBtnPrev.setVisibility(View.VISIBLE);
		}
		
		if(currentPos == list.size()){
			imgBtnNext.setVisibility(View.GONE);
		}else{
			imgBtnNext.setVisibility(View.VISIBLE);
		}
		if(currentPos < list.size()){
			//txtHead.setText(listAllCurrentNewsItem.get(currentSubItemNo).getNewsHeading());
			//txtIndicator.setText(currentSubItemNo+1 +" / "+ listAllCurrentNewsItem.size());
		}
	}
	private Bitmap loadFromExternalMemory(String url){
		
		String imageName = "";
		if(url != null && !url.trim().isEmpty()){
			  String[] array = url.split(File.separator);
			  
			  if(array.length > 0){
				  imageName = array[array.length -1];
			  }
		  }else{
			  return null;
		  }
		
		String filePath =  Environment.getExternalStorageDirectory()
	            + File.separator +Globals.getStringFromResources(this, R.string.app_name);
		String imagePath = filePath  + File.separator +imageName;
	    
		Bitmap bitmap = null;
		try{
			bitmap = BitmapFactory.decodeFile(imagePath);
		}catch(OutOfMemoryError ex){
			
			//Toast.makeText(Activity_EPaperReader.this, "Failed to load image, please try again.",Toast.LENGTH_SHORT ).show();
		}
		 return bitmap;  	
	}
	
	private void saveToExternalMemory(Bitmap bitmap, String url){
		
		String imageName = "";
		if(url != null && !url.trim().isEmpty()){
			  String[] array = url.split(File.separator);
			  
			  if(array.length > 0){
				  imageName = array[array.length -1];
			  }
		  }else{
			  return;
		  }
		
		String filePath =  Environment.getExternalStorageDirectory()
	            + File.separator +Globals.getStringFromResources(this, R.string.app_name);
		String imagePath = filePath  + File.separator +imageName;
		
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
	    File f = new File(filePath);
	    try {
	    	if (!f.exists()) {
                f.mkdirs();
            }
	    	
	    	f= new File(imagePath);
	        f.createNewFile();
	        new FileOutputStream(f).write(bytes.toByteArray());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	}
	
	private void showImage(Bitmap bitmap){
		imageViewEpaper.setImage(ImageSource.bitmap(bitmap));;
    	image_url = "";
    	Globals.hideLoadingDialog(mDialog);
	}
	
	private Target target = new Target() {
	    @Override
	     public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
	    	saveToExternalMemory(bitmap, image_url);
	    	showImage(bitmap);
	     }
		@Override
		public void onBitmapFailed(Drawable arg0) {
			
			Toast.makeText(Activity_EPaperReader.this, "Failed to load image, please try again.",Toast.LENGTH_SHORT ).show();
			Globals.hideLoadingDialog(mDialog);
			
			//picassoCache.clear();
			
		}
		@Override
		public void onPrepareLoad(Drawable arg0) {
			
		}
	};
	
	@Override 
	public void onDestroy() {  // could be in onPause or onStop
		Log.i("DARSH", "onDestroy");

		//selectedCity = null;
	   Picasso.with(this).cancelRequest(target);
	   super.onDestroy();
		unbindDrawables(findViewById(R.id.rlytRoot));
		System.gc();
	}

	private void setSlidingMenu(ArrayList<Object_Epaper> list){
		Custom_AdapterEpaper adapter = new Custom_AdapterEpaper(this,
				list);
		expListPapers.setAdapter(adapter);
		onClickNewsPage(1);
	}
	
	public void onClickToggle(View v) {
		this.toggle();
	}


	private void getEPaperDetailsFromServer(String date){

		try {
			if (userid != -1) {
				mDialog = Globals.showLoadingDialog(mDialog, this, false, "");

				String url = Custom_URLs_Params.getURL_getEpaperUrl();
				HashMap<String, String> map = new HashMap<>();
				Object_AppConfig obj = new Object_AppConfig(this);
				map.put("userid", userid + "");
				if (!date.equals(""))
					map.put("date", date);

				Log.i("SUSHIL", "map and url " + url + "" + map);
				Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
						Request.Method.POST,
						url,
						map,
						new Response.Listener<JSONObject>() {

							@Override
							public void onResponse(JSONObject response) {
								Globals.hideLoadingDialog(mDialog);
								Log.i("SUSHIL", "json Response recieved !!" + response);
								parser(response);

							}

						}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError err) {
						Globals.hideLoadingDialog(mDialog);
						Globals.showAlertDialogOneButton(
								Globals.TEXT_CONNECTION_ERROR_HEADING,
								Globals.TEXT_CONNECTION_ERROR_DETAIL_TOAST,
								Activity_EPaperReader.this, "OK", null, false);
						if (err != null) {
							Log.i("DARSH", "ERROR Details getLocalizedMessage : " + err.getLocalizedMessage());
							Log.i("DARSH", "ERROR Details getMessage : " + err.getMessage());
							Log.i("DARSH", "ERROR Details getStackTrace : " + err.getStackTrace());
						}

					}
				});
			/*Custom_VolleyObjectRequestRequest jsonObjectRQST = new Custom_VolleyObjectRequestRequest(Request.Method.POST,
					url, map,
					new Listener<JSONArray>() {

						@Override
						public void onResponse(JSONArray response) {
							parser(response);
						}
					}, new ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError err) {
					Log.i("DARSH", "ERROR VolleyError");

					Globals.hideLoadingDialog(mDialog);
					Globals.showAlertDialogOneButton(
							Globals.TEXT_CONNECTION_ERROR_HEADING,
							Globals.TEXT_CONNECTION_ERROR_DETAIL_TOAST,
							Activity_EPaperReader.this, "OK", null, false);
					if(err != null){
						Log.i("DARSH", "ERROR Details getLocalizedMessage : "+err.getLocalizedMessage());
						Log.i("DARSH", "ERROR Details getMessage : "+err.getMessage());
						Log.i("DARSH", "ERROR Details getStackTrace : "+err.getStackTrace());
					}

				}
			});*/

				Custom_VolleyAppController.getInstance().addToRequestQueue(
						jsonObjectRQST);

			}
		}
	catch (Exception e) {
			Log.i("HARSH",
					"Excetion FIRSTCALL getEPaperDetailsFromServer");
			Globals.hideLoadingDialog(mDialog);


		}

	}
	
   private void parser(JSONObject obj){
          if(obj==null){
			  Toast.makeText(this,Globals.MSG_SERVER_ERROR,Toast.LENGTH_SHORT).show();
			  return;
		  }else{
			if(obj.has("data")){
				try {
					JSONArray array = obj.getJSONArray("data");
					if(array.length()!=0) {
						JSONObject objectEpaper = array.getJSONObject(0);
						totalpages = objectEpaper.getInt("totalpages");
						thumb_image_url = objectEpaper.getString("locationpath");
						pdf_url = objectEpaper.getString("pdf_url");
						getDayMonYear(objectEpaper.getString("date"));
						txtDate.setText(Globals.getdateFormat(day, month + 1, year));
						if(totalpages!=0 && !pdf_url.equals("")){
							configureEpaper();
						}
					}else{
						Toast.makeText(this,"Epaper not found",Toast.LENGTH_SHORT).show();
						if(pdf_url.equals(""))
						   this.finish();
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		  }
    }
}
