package com.iseva.app.source;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;

@ReportsCrashes(
		formUri = "https://iseva.cloudant.com/acra-isevalog/_design/acra-storage/_update/report",
		reportType = HttpSender.Type.JSON,
		httpMethod = HttpSender.Method.POST,
		formUriBasicAuthLogin = "allyrentookeneriestancen",
		formUriBasicAuthPassword = "3291fc2fa445d4098b953edf02d16071ec1891b5",

		customReportContent = {
				ReportField.APP_VERSION_CODE,
				ReportField.APP_VERSION_NAME,
				ReportField.ANDROID_VERSION,
				ReportField.PACKAGE_NAME,
				ReportField.REPORT_ID,
				ReportField.BUILD,
				ReportField.STACK_TRACE
		},
		mode = ReportingInteractionMode.TOAST,
		resToastText = R.string.toast_crash
)

public class Custom_VolleyAppController extends Application {

	 private RequestQueue requestQueue;
	 
	 private static Custom_VolleyAppController appControllerContext;


	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}

	 @Override
	 public void onCreate() 
	 { 
	  super.onCreate();
	  System.out.println("AppController onCreate Called!");
	  appControllerContext = this;

		 try {
			 ACRA.init(this);
		 }
		 catch (Exception e)
		 {
			 e.printStackTrace();
		 }

		/* Picasso.Builder builder = new Picasso.Builder(this);
		 builder.downloader(new OkHttpDownloader(this,Integer.MAX_VALUE));
		 Picasso built = builder.build();
		 built.setIndicatorsEnabled(true);
		 built.setLoggingEnabled(true);
		 Picasso.setSingletonInstance(built);*/
		 /*int memClass = ((ActivityManager) this
				 .getSystemService(Context.ACTIVITY_SERVICE))
				 .getLargeMemoryClass();
		 int cacheSize = 1024 * 1024 * memClass / 4;
           Picasso p = new Picasso.Builder(this).memoryCache(
                    new LruCache(cacheSize)).build();*/
		 /*Picasso.Builder builder = new Picasso.Builder(this);
		 builder.downloader(new OkHttpDownloader(getCacheDir(),250000000));
		 Picasso built = builder.build();
		 //built.setIndicatorsEnabled(true);
		 //built.setLoggingEnabled(true);
		 Picasso.setSingletonInstance(built);*/

		 /*// Size in bytes (10 MB)
		  final long PICASSO_DISK_CACHE_SIZE = 1024 * 1024 * 10;

		 // Use OkHttp as downloader
		 Downloader downloader = new OkHttpDownloader(getApplicationContext(),
				 PICASSO_DISK_CACHE_SIZE);

		 // Create memory cache
		 Cache memoryCache = new LruCache(maxSize);

		 mPicasso = new Picasso.Builder(getApplicationContext())
				 .downloader(downloader).memoryCache(memoryCache).build();*/
	 }

	 public static synchronized Custom_VolleyAppController getInstance()
	 {
		 return appControllerContext;
	 }

	 public RequestQueue getRequestQueue()
	 {
	  if(requestQueue == null)
	  {
	   requestQueue = Volley.newRequestQueue(appControllerContext); 
	  }
	  return requestQueue;
	 }
	 
	 public <T> void addToRequestQueue(Request<T> req)
	 {
		 req.setRetryPolicy(new DefaultRetryPolicy(
				 	Globals.VOLLEY_TIMEOUT_MILLISECS, 
	                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
	                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		 getRequestQueue().add(req);
	 }
	 public void cancelPendingRequest(Object tag)
	 {
	  if(requestQueue != null)
	  {
	   requestQueue.cancelAll(tag);
	  }
	 }
}
