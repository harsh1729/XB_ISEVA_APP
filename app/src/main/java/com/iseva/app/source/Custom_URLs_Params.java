package com.iseva.app.source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Custom_URLs_Params {
    public static String getURL_GcmRegister() {

        return Globals.DEFAULT_APP_SERVER_PATH + "user/registergcm";

    }

    public static String getURL_AllCity() {

        return Globals.DEFAULT_APP_SERVER_PATH + "city/get";
    }

    public static String getURL_SubmitAllCity() {

        return Globals.DEFAULT_APP_SERVER_PATH + "city/addcity2gcmuser";
    }
    public static String getURL_getEpaperUrl() {

        return Globals.DEFAULT_APP_SERVER_PATH1 + "user/getepaper";
    }
    public static String getURL_UserRegister() {

        return Globals.DEFAULT_APP_SERVER_PATH + "user/register";
    }

    public static String getURL_citycate() {

        return Globals.DEFAULT_APP_SERVER_PATH + "jobs/getcitiescate";
    }

    public static String getURL_Login_Merchant() {

        return Globals.DEFAULT_APP_SERVER_PATH+"user/login";
    }

    public static String getURL_GET_PROFILE() {

        return Globals.DEFAULT_APP_SERVER_PATH;
    }

    public static String getURL_SaveMerchatData() {

        return Globals.DEFAULT_APP_SERVER_PATH+"user/update";
    }

    public static String getURL_Login() {

        return Globals.DEFAULT_APP_SERVER_PATH;
    }

    public static String getURL_BusinessExtraType() {

        return Globals.DEFAULT_APP_SERVER_PATH+"businessextra/getbusinessmaster";
    }

    public static String getURL_MyBusinessExtra() {

        return Globals.DEFAULT_APP_SERVER_PATH+"businessextra/getbusinessextrabyuserid";
    }

    public static String getURL_SendRating() {

        return Globals.DEFAULT_APP_SERVER_PATH;
    }

    public static String getURL_ImageUpload() {

        return Globals.DEFAULT_APP_SERVER_PATH+"gallery/upload_image";
    }

    public static String getURL_GetSubCate() {

        return Globals.DEFAULT_APP_SERVER_PATH + "category/getsub";
    }
    public static String getURL_GetSubCateHome() {

        return Globals.DEFAULT_APP_SERVER_PATH + "category/get";
    }
    public static String getURL_GetEpaperUser() {

        return Globals.DEFAULT_APP_SERVER_PATH1 + "user/getepaper_user";
    }
    public static String getURL_OnsubmitCate() {

        return Globals.DEFAULT_APP_SERVER_PATH +"category/mapuser";
    }
    public static String getURL_AddOffers() {

        return Globals.DEFAULT_APP_SERVER_PATH +"offers/add";
    }

    public static String getURL_AddNewBusinessExtra() {

        return Globals.DEFAULT_APP_SERVER_PATH +"businessextra/add";
    }

    public static String getURL_UploadResumeFile() {

        return Globals.DEFAULT_APP_SERVER_PATH +"gallery/uploadresume";
    }
    public static String getURL_PostJob() {

        return Globals.DEFAULT_APP_SERVER_PATH +"jobs/addjobs";
    }

    public static String getURL_UploadData() {

        return Globals.DEFAULT_APP_SERVER_PATH +"jobs/registerall";
    }
    public static String getURL_ResumeRegister() {

        return Globals.DEFAULT_APP_SERVER_PATH +"jobs/register";
    }
    public static String getURL_Get_Service_Provider() {

        return Globals.DEFAULT_APP_SERVER_PATH +"user/get";
    }

    public static String getURL_Get_Jobs() {

        return Globals.DEFAULT_APP_SERVER_PATH +"jobs/getjobs";
    }
    public static String getURL_Get_Service_Provider_Data() {

        return Globals.DEFAULT_APP_SERVER_PATH +"user/getdetail";
    }

    public static String getURL_SENDNUMBERS() {

        return Globals.DEFAULT_APP_SERVER_PATH +"user/registerusernumber";
    }

    public static String getURL_CallNumberRegister() {

        return Globals.DEFAULT_APP_SERVER_PATH +"user/registercallnumber";
    }

    public static String getURL_CallNumberCheck() {

        return Globals.DEFAULT_APP_SERVER_PATH +"user/checkcallnumber";
    }
    public static String getURL_OffersWithid() {

        return Globals.DEFAULT_APP_SERVER_PATH+"businessextra/getbusinessextrabyid";
    }
    public static String getURL_BusinessExtra() {

        return Globals.DEFAULT_APP_SERVER_PATH+"businessextra/getbusinessextra";
    }
    public static String getURL_BusinessExtraDelete() {

        return Globals.DEFAULT_APP_SERVER_PATH+"businessextra/delete";
    }
    public static String getURL_OffersRandom() {

        return Globals.DEFAULT_APP_SERVER_PATH+"businessextra/getrandomAdver";
    }
    public static String getURL_OffersRandomcat() {

        return Globals.DEFAULT_APP_SERVER_PATH+"businessextra/getrandomAdvercat";
    }

    public static HashMap<String, String> getParams_Profile(Context con) {

        HashMap<String, String> map = new HashMap<String, String>();
        Object_AppConfig config = new Object_AppConfig(con);
        map.put("device_id", Globals.getdeviceId(con));
        map.put("userid", config.getUserId() + "");

        Log.i("SUSHIL", "getParams_ --->" + map);
        return map;
    }
    public static HashMap<String, String> getParams_ServiceProvider(Context con,int catid) {

        HashMap<String, String> map = new HashMap<String, String>();
       // Object_AppConfig config = new Object_AppConfig(con);
        map.put("deviceid", Globals.getdeviceId(con));
        map.put("catid", catid+"");

        Log.i("SUSHIL", "getParams_ --->" + map);
        return map;
    }

    public static HashMap<String, String> getParams_Send_Notification(Context con,String heading,String cont) {

        HashMap<String, String> map = new HashMap<String, String>();
        Object_AppConfig config = new Object_AppConfig(con);
        map.put("deviceid", Globals.getdeviceId(con));
        map.put("userid", config.getUserId() + "");
        map.put("heading",heading);
        map.put("con",cont);

        Log.i("SUSHIL", "getParams_ --->" + map);
        return map;
    }

    public static HashMap<String, String> getParams_RemainingNoti(Context con) {

        HashMap<String, String> map = new HashMap<String, String>();
        Object_AppConfig config = new Object_AppConfig(con);
        map.put("device_id", Globals.getdeviceId(con));
        map.put("userid", config.getUserId() + "");

        Log.i("SUSHIL", "getParams_ --->" + map);
        return map;
    }

    public static HashMap<String, String> getParams_MerchatRegisterData(Context con) {
         //int mapImage = 0;
        JSONObject map = new JSONObject();
       // ArrayList<HashMap<Integer,Integer>> listImageid = new ArrayList<>();
        HashMap<String, String> mapMain = new HashMap<String, String>();
        HashMap<Integer,ArrayList<Integer>> mapId = new HashMap<>();
        DBHandler_Access db = DBHandler_Access.getInstance(con);
        ArrayList<Object_Control_Maping> listMap = db.getAllData();
        try {
            for (Object_Control_Maping objMap : listMap) {
                if (objMap.typeId == Custom_Control.MultiImageID) {
                    Log.i("SUSHIL", "SUSHIL muti image id is call "+objMap.controlID);

                    if (mapId.get(objMap.controlID) != null) {
                        //Arraylist is exit
                        ArrayList<Integer> list = mapId.get(objMap.controlID);
                        if (objMap.value != null)
                            list.add(Integer.parseInt(objMap.value));

                        mapId.put(objMap.controlID, list);

                    } else {
                        //first time is adding
                        ArrayList<Integer> list = new ArrayList<>();
                        if (objMap.value != null)
                            list.add(Integer.parseInt(objMap.value));

                        mapId.put(objMap.controlID, list);
                    }

                    //mapId.put(objMap.controlID+"",)
                /*if(mapImage==objMap.controlID)
               listImageid.add(Integer.parseInt(objMap.value));

               mapImage = objMap.controlID;*/
                } else {
                    map.put(objMap.controlID+"", objMap.value);
                }
            }
        /*if(mapImage!=0)
        map.put(mapImage+"",listImageid.toString());*/
            for (Map.Entry<Integer, ArrayList<Integer>> entry : mapId.entrySet()) {
                Integer key = entry.getKey();
                ArrayList<Integer> value = entry.getValue();
                map.put(key + "", value.toString());
            }
            Object_AppConfig config = new Object_AppConfig(con);
            mapMain.put("device_id", Globals.getdeviceId(con));
            mapMain.put("userid", config.getUserId()+ "");
        }catch (JSONException ex){
            ex.printStackTrace();
        }
        mapMain.put("data",map.toString());

        Log.i("SUSHIL", "getParams_ --->" + mapMain);
        return mapMain;
    }



    public static HashMap<String, String> getParams_SendRating(Context con, int id, float rate) {

        HashMap<String, String> map = new HashMap<String, String>();
        Object_AppConfig config = new Object_AppConfig(con);
        map.put("device_id", Globals.getdeviceId(con));
        map.put("userid", config.getUserId() + "");
        map.put("id", id + "");
        map.put("rating", rate + "");

        Log.i("SUSHIL", "getParams_ --->" + map);
        return map;
    }

    public static HashMap<String, String> getParams_SaveCate(Context con,String name,String contact,String address,String servi,
                                                             int imageid,int cityid,HashMap<Integer,Integer>images,String email) {

        ArrayList<Integer> list = new ArrayList<>();
        for (Map.Entry<Integer,Integer> entry : images.entrySet()) {
            list.add(entry.getValue());
            //imageid.put(entry.getKey(),id);
        }
        HashMap<String, String> map = new HashMap<String, String>();
        Object_AppConfig config = new Object_AppConfig(con);
        //map.put("device_id", Globals.getdeviceId(con));
        map.put("userid", config.getUserId()+ "");
        map.put("catid",   config.getCatId()+"");
        map.put("name",name);
        map.put("email",email);
        map.put("contact",contact);
        map.put("address",address);
        map.put("profile_image",imageid+"");
        map.put("cityid",cityid+"");
        map.put("ismerchant",1+"");
        map.put("services",servi);
        map.put("images",list.toString());


        Log.i("SUSHIL", "getParams_ --->" + map);
        return map;
    }

    public static HashMap<String, String> getParams_Category(Context con) {

        HashMap<String, String> map = new HashMap<String, String>();
        Object_AppConfig config = new Object_AppConfig(con);
        map.put("device_id", Globals.getdeviceId(con));
        map.put("version_no", config.getVersionNo() + "");
        map.put("gcm_id", config.getRegistrationId(con));
        Log.i("SUSHIL", "getParams_ --->" + map);
        return map;
    }

    public static HashMap<String, String> getParams_GCMRegister(Context con) {

        HashMap<String, String> map = new HashMap<String, String>();
        Object_AppConfig config = new Object_AppConfig(con);

        map.put("imei", Globals.getdeviceId(con));
        map.put("gcmid", config.getRegistrationId(con));

        Log.i("SUSHIL", "getParams_ --->" + map);
        return map;
    }

    public static HashMap<String, String> getParams_Login(Context con, String username, String password) {

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("username", username);
        map.put("password", password);

        Log.i("SUSHIL", "getParams_ --->" + map);
        return map;
    }

    public static HashMap<String, String> getParams_AddNewBusinessExtra(Context con, String heading, String content,HashMap<Integer,Integer> offers,int cityid) {
       // HashMap<Integer,Integer> offers
        ArrayList<Integer> list = new ArrayList<>();
        for (Map.Entry<Integer,Integer> entry : offers.entrySet()) {
              list.add(entry.getValue());
            //imageid.put(entry.getKey(),id);
        }
        Object_AppConfig config = new Object_AppConfig(con);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", config.getUserId()+"");
        map.put("heading", heading);
        map.put("content", content);
        map.put("bextraid",config.getBusiExtraId()+"");
        map.put("images",list.toString());
        map.put("cityid",cityid+"");



        Log.i("SUSHIL", "getParams_ --->" + map);
        return map;
    }

    public static HashMap<String, String> getParams_UploadResume(Context con, int profileid, String Fname,String DOB,String gender,String Address,
                                                               String qualification,String experience,String currentJob,String currentSalary,String others,int fileid,int cityID,int catid) {

        Object_AppConfig config = new Object_AppConfig(con);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("imgid", profileid+"");
        map.put("fname", Fname);
        map.put("age", DOB+" Years");
        map.put("gender",gender);
        map.put("address",Address);
       // map.put("number",number);

       // map.put("email", email);
        map.put("quali", qualification);
        map.put("experi", experience);
        map.put("currentjob",currentJob);
        map.put("currentsalary",currentSalary);
        map.put("others",others);

        map.put("resume",fileid+"");
        map.put("cityid",cityID+"");
        map.put("empid",config.getEmpId()+"");
        map.put("catid",catid+"");



        Log.i("SUSHIL", "getParams_ --->" + map);
        return map;
    }


    public static HashMap<String,String> getParams_ResumeRegister(String name,String number,String username,String password){
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("name", name);
        map.put("number", number);

        map.put("username", username);
        map.put("password",password);

        Log.i("SUSHIL","Hash Map "+map);

        return map;
    }




    public static HashMap<String, String> getParams_UploadJob(Context con, String jobTitle,String Address,String number,
                                                                 String expdate,String eligi,String profile,String salary,String holiday,String timings,String others,int cityID,int cateID) {

        //Object_AppConfig config = new Object_AppConfig(con);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("title", jobTitle);
        map.put("eligibility", eligi);

        map.put("job_profile", profile);
        map.put("salary",salary);
        map.put("personname",Address);
        map.put("personnumber",number);

        map.put("expdate", expdate);
        map.put("holidays", holiday);
        map.put("timings", timings);
        map.put("others",others);

        map.put("catid",cateID+"");
        map.put("cityid",cityID+"");



        Log.i("SUSHIL", "getParams_ --->" + map);
        return map;
    }


    public static HashMap<String, String> getParams_UploadImageStringParams(Context con, String key) {

        Object_AppConfig config = new Object_AppConfig(con);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("keys", key);
        map.put("userid", 1 + "");
        Log.i("SUSHIL", "getParams_ --->" + map);
        return map;
    }


    public static HashMap<String, String> getParams_RegisterUser(Context con, String name, String number, String userName, String password, String promo) {

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("name", name);
       // map.put("email", email);
        map.put("contact", number);
        map.put("username", userName);
        map.put("password", password);
        map.put("imei", Globals.getdeviceId(con));
        if(!promo.equals(""))
          map.put("promocode", promo);

        Log.i("SUSHIL", "getParams_ --->" + map);
        return map;
    }

	/*public static String getURL_CatData(Context context){
         Object_AppConfig config = new Object_AppConfig(context);
		  Log.i("SUSHIL", Globals.DEFAULT_APP_SERVER_PATH+"doctor/get_doctors"+"?catid = "+config.getCatId()+"&cityid = "+config.getCityId());
		  return Globals.DEFAULT_APP_SERVER_PATH+"doctor/get_doctors"+"?catid="+config.getCatId()+"&cityid="+config.getCityId();
		 }
	//http://xercesblue.in/dr/client_requests/doctor/get_doctors?catid=1&cityid=1
	public static String getURL_AllDetailsData(){
		
		  Log.i("SUSHIL", Globals.DEFAULT_APP_SERVER_PATH+"doctor/get_doctors");
		  return Globals.DEFAULT_APP_SERVER_PATH+"doctor/getDocDetail";
		 }
	
	public static String getURL_Add(){
		
		  Log.i("SUSHIL", Globals.DEFAULT_APP_SERVER_PATH+"advt/getadvt");
		  return Globals.DEFAULT_APP_SERVER_PATH+"advt/getadvt";
		 }
	
	

	 
	public static HashMap< String, String>  getParams_DoctorDetailsParams(Context con){
		
		 HashMap< String, String> map = new HashMap<String, String>();
		 Object_AppConfig config = new Object_AppConfig(con);
				map.put("docid", config.getDoctorId()+"");
				Log.i("SUSHIL", "getParams_ --->" + map);
			return map;
		}*/


}
