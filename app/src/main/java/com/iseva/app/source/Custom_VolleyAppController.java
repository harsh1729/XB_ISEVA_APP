package com.iseva.app.source;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;



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



		 try{
			 Fabric.with(this, new Crashlytics());
		 }catch (Exception e){
			 e.printStackTrace();
		 }




		 Realm.init(this);

		 RealmConfiguration realmConfiguration = new RealmConfiguration
				 .Builder()
				 .deleteRealmIfMigrationNeeded()
				 .build();
		 Realm.setDefaultConfiguration(realmConfiguration);
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
