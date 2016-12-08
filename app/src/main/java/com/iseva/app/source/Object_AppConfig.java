package com.iseva.app.source;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Object_AppConfig {

	
	private Context context;
	
	private final String KEY_APP_CONFIG = "appConfig";
	private SharedPreferences prefs;
	private SharedPreferences.Editor editor = null;
	
	public Object_AppConfig(Context context){
		
		this.context = context;
		prefs = this.context.getSharedPreferences(KEY_APP_CONFIG, 0);
		editor = prefs.edit();
	}
	
	public String getCityName() {
		String userName = "" ;
		
		if(prefs != null)
			userName = prefs.getString("appConfig_CityName","");	
		
		return userName;
	}
	public void setCityName(String cityName) {
		
		if (editor != null) {
			editor.putString("appConfig_CityName", cityName);
			editor.commit();
		}

	}
	public int getCityId() {
		int cityId = -1;
		
		if(prefs != null)
			cityId = prefs.getInt("appConfig_cityId", -1);
		
		return cityId;
	}
	public void setCityId(int cityId) {
		
		if (editor != null) {
			editor.putInt("appConfig_cityId", cityId);
			editor.commit();
		}

	}


	public int getEmpId() {
		int appConfig_empId = -1;

		if(prefs != null)
			appConfig_empId = prefs.getInt("appConfig_empId", -1);

		return appConfig_empId;
	}
	public void setEmpId(int id) {

		if (editor != null) {
			editor.putInt("appConfig_empId", id);
			editor.commit();
		}

	}

	public int getEmperId() {
		int appConfig_empId = -1;

		if(prefs != null)
			appConfig_empId = prefs.getInt("appConfig_emperId", -1);

		return appConfig_empId;
	}
	public void setEmperId(int id) {

		if (editor != null) {
			editor.putInt("appConfig_emperId", id);
			editor.commit();
		}

	}
	public int getCatId() {
		int CatId = -1;
		
		if(prefs != null)
			CatId = prefs.getInt("appConfig_CatId",-1);	
		
		return CatId;
	}
	public void setCatId(int CatId) {
		
		if (editor != null) {
			editor.putInt("appConfig_CatId", CatId);
			editor.commit();
		}

	}

	public int getBusiExtraId() {
		int BusiExtraId = -1;

		if(prefs != null)
			BusiExtraId = prefs.getInt("appConfig_BusiExtraId",-1);

		return BusiExtraId;
	}
	public void setBusiExtraId(int BusiExtraId) {

		if (editor != null) {
			editor.putInt("appConfig_BusiExtraId", BusiExtraId);
			editor.commit();
		}

	}
	
	public String getCatName() {
		String CateName = "" ;
		
		if(prefs != null)
			CateName = prefs.getString("appConfig_CateName", "");
		
		return CateName;
	}
	public void setCateName(String CateName) {
		
		if (editor != null) {
			editor.putString("appConfig_CateName", CateName);
			editor.commit();
		}

	}


	public String getEmployeeResponse() {
		String response = "" ;

		if(prefs != null)
			response = prefs.getString("appConfig_response", "");

		return response;
	}
	public void setEmployeeResponse(String response) {

		if (editor != null) {
			editor.putString("appConfig_response", response);
			editor.commit();
		}

	}

	public boolean getboolnavHeader() {
		//false_mean = goes from cate list;
		//true_mean = goes from doctor list ;
		boolean value = false;
		
		if(prefs != null)
			value = prefs.getBoolean("appConfig_value", false);
		
		return value;
	}
	public void setboolnavHeader(boolean value) {
		
		if (editor != null) {
			editor.putBoolean("appConfig_value", value);
			editor.commit();
		}

	}

	public boolean getIsCitySelected() {
		//false_mean = goes from cate list;
		//true_mean = goes from doctor list ;
		boolean value = false;

		if(prefs != null)
			value = prefs.getBoolean("appConfig_value", false);

		return value;
	}
	public void setIsCitySelected(boolean value) {

		if (editor != null) {
			editor.putBoolean("appConfig_value", value);
			editor.commit();
		}

	}

	public String getPromoCode() {
		String PromoCode = "" ;

		if(prefs != null)
			PromoCode = prefs.getString("appConfig_PromoCode", "");

		return PromoCode;
	}
	public void setPromoCode(String PromoCode) {

		if (editor != null) {
			editor.putString("appConfig_PromoCode", PromoCode);
			editor.commit();
		}

	}

	public boolean getboolIslogin() {
		boolean value = false;

		if(prefs != null)
			value = prefs.getBoolean("appConfig_Islogin", false);

		return value;
	}
	public void setboolIslogin(boolean value) {

		if (editor != null) {
			editor.putBoolean("appConfig_Islogin", value);
			editor.commit();
		}

	}
	public int getVersionNo() {
		int VersionNo = -1;

		if(prefs != null)
			VersionNo = prefs.getInt("appConfig_VersionNo", -1);

		return VersionNo;
	}
	public void setVersionNo(int VersionNo) {

		if (editor != null) {
			editor.putInt("appConfig_VersionNo", VersionNo);
			editor.commit();
		}

	}

	public String getuserName() {
		String userName = "" ;

		if(prefs != null)
			userName = prefs.getString("appConfig_userName", "");

		return userName;
	}
	public void setuserName(String userName) {

		if (editor != null) {
			editor.putString("appConfig_userName", userName);
			editor.commit();
		}

	}

	public String getuserBusinessCategory() {
		String userName = "" ;

		if(prefs != null)
			userName = prefs.getString("appConfig_userBusiness", "");

		return userName;
	}
	public void setuserBusinessCategory(String userBusinessName) {

		if (editor != null) {
			editor.putString("appConfig_userBusiness", userBusinessName);
			editor.commit();
		}

	}


	public String getFirmName() {
		String userName = "" ;

		if(prefs != null)
			userName = prefs.getString("appConfig_firm_name", "");

		return userName;
	}
	public void setFirmName(String firmName) {

		if (editor != null) {
			editor.putString("appConfig_firm_name", firmName);
			editor.commit();
		}

	}

	public String getUserName() {
		String UserName = "" ;

		if(prefs != null)
			UserName = prefs.getString("appConfig_UserName", "");

		return UserName;
	}
	public void setUserName(String UserName) {

		if (editor != null) {
			editor.putString("appConfig_UserName", UserName);
			editor.commit();
		}

	}


	public String getNumber() {
		String num = "" ;

		if(prefs != null)
			num = prefs.getString("appConfig_num", "");

		return num;
	}
	public void setNumber(String num) {

		if (editor != null) {
			editor.putString("appConfig_num", num);
			editor.commit();
		}

	}



	public boolean isSendNumber() {
		boolean isSendNumber = false;
		if(prefs != null)
			isSendNumber = prefs.getBoolean("appConfig_isSendNumber", false);

		return isSendNumber;
	}

	public void setisSendNumber(boolean isSendNumber) {

		if (editor != null) {
			editor.putBoolean("appConfig_isSendNumber", isSendNumber);
			editor.commit();
		}
	}

	public String getUserImage() {
		String userName = "" ;

		if(prefs != null)
			userName = prefs.getString("appConfig_user_image", "");

		return userName;
	}
	public void setUserImage(String image) {

		if (editor != null) {
			editor.putString("appConfig_user_image", image);
			editor.commit();
		}

	}
	public int getUserId() {
		int UserId = -1;

		if(prefs != null)
			UserId = prefs.getInt("appConfig_UserId", -1);

		return UserId;
	}
	public void setUserId(int UserId) {

		if (editor != null) {
			editor.putInt("appConfig_UserId", UserId);
			editor.commit();
		}

	}
	public int getServiceProviderId() {
		int serId = -1;

		if(prefs != null)
			serId = prefs.getInt("appConfig_serId", -1);

		return serId;
	}
	public void setServiceProviderId(int serId) {

		if (editor != null) {
			editor.putInt("appConfig_serId", serId);
			editor.commit();
		}

	}

	public boolean isNotificationEnabled() {
		boolean notificationEnabled = true;
		if(prefs != null)
			notificationEnabled = prefs.getBoolean("appConfig_NotificationEnabled", true);

		return notificationEnabled;
	}

	public void setNotificationEnabled(boolean notificationEnabled) {

		if (editor != null) {
			editor.putBoolean("appConfig_NotificationEnabled", notificationEnabled);
			editor.commit();
		}
	}

	public boolean isMerchant() {
		boolean isMerchant = false;
		if(prefs != null)
			isMerchant = prefs.getBoolean("appConfig_isMerchant", false);

		return isMerchant;
	}

	public void setIsMerchant(boolean isMerchant) {

		if (editor != null) {
			editor.putBoolean("appConfig_isMerchant", isMerchant);
			editor.commit();
		}
	}

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGCMPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences, but
		// how you store the registration ID in your app is up to you.
		return context.getSharedPreferences(Activity_City_Choose.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}

	/**
	 * Gets the current registration token for application on GCM service.
	 * <p>
	 * If result is empty, the app needs to register.
	 *
	 * @return registration token, or empty string if there is no existing
	 *         registration token.
	 */
	public String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGCMPreferences(context);
		String registrationId = prefs.getString("PROPERTY_REG_ID", "");
		if (registrationId.isEmpty()) {
			Log.i("SUSHIL", "Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing registration ID is not guaranteed to work with
		// the new app version.
		int registeredVersion = prefs.getInt("PROPERTY_APP_VERSION", Integer.MIN_VALUE);
		int currentVersion = Globals.getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i("SUSHIL", "App version changed.");
			return "";
		}
		return registrationId;
	}

	public void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGCMPreferences(context);
		int appVersion = Globals.getAppVersion(context);
		Log.i("SUSHIL", "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("PROPERTY_REG_ID", regId);
		editor.putInt("PROPERTY_APP_VERSION", appVersion);
		editor.commit();
	}
	
}
