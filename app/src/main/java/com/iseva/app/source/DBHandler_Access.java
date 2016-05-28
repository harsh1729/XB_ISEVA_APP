package com.iseva.app.source;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by com.bitsandbytes on 11/30/2015.
 */
public class DBHandler_Access {

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DBHandler_Access instance;

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    //Table Name
    private static final String TABLE_NAME = "Category";
    //private static final String TABLE_NAME_CITY = "City";
    //Column Name
    private static final String CATE_ID = "id";
    private static final String CATE_SORT_ORDER = "sort_order";
    private static final String CATE_NAME = "name";
    private static final String CATE_IMAGE = "image";
    private static final String CATE_PARENT_ID = "parent_id";
    private static final String CATE_CATEID = "cat_id";
    private static final String CATE_ISENABLE = "isenable";
    private static final String CATE_ISBUSINESS = "isbusiness";


    //Table Name
    private static final String TABLE_NAME_CONTROL = "Control";
    private static final String TABLE_NAME_DATA = "Data";
    //Column Name control

    public static final String KEY_ID = "id";
    public static final String KEY_LABEL = "label";
    public static final String KEY_CONTROL_ID = "control_id";
    public static final String KEY_TYPE_ID = "type_id";
    public static final String KEY_SCREEN_ID = "screen_id";
    public static final String KEY_SORT_ORDER = "sort_order";
    public static final String KEY_HINT = "hint";
    public static final String KEY_ISREQUIRED = "isrequired";

    // CREATE TABLE "Control" ("id" INTEGER PRIMARY KEY ,"label" TEXT DEFAULT (null) ,
    // "control_id" INTEGER, "type_id" INTEGER, "screen_id" INTEGER, "sort_order" INTEGER)
    //Column Name DAta TAble
    public static final String KEY_DATA_ID = "id";
    public static final String KEY__DATA_TEXT = "value";
    public static final String KEY_DATA_CONTROL_ID = "controlId";
    public static final String KEY_DATA_SELECTED_VALUE = "selectedValue";
    public static final String KEY_DATA_IMAGE_NAME= "image_name";
    public static final String KEY_DATA_IS_LINK = "is_link";


/// column name city table
   /* public static final String KEY_CITY_ID = "id";
    public static final String KEY_CITY_NAME = "name";
    public static final String KEY_CITY_SERVERID = "cityId";
    public static final String KEY_CITY_ISSELECTED = "isSelected";*/
    /*CREATE TABLE "Category" ("id" INTEGER PRIMARY KEY  NOT NULL ,"sort_order" INTEGER NOT NULL  DEFAULT (null) ,"name" TEXT,"image" BLOB,"parent_id" INTEGER DEFAULT (null) , "cat_id" INTEGER)*/


    private DBHandler_Access(Context context) {
        this.openHelper = new DBHandler_Main(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DBHandler_Access getInstance(Context context) {
        if (instance == null) {
            instance = new DBHandler_Access(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }


    public ArrayList<Object_Category> getCategory() {

        ArrayList<Object_Category> listCate = new ArrayList<Object_Category>();
        String sqlQuery = "Select * From "+TABLE_NAME+" WHERE "+CATE_ISENABLE+" = 1 Order By "+CATE_SORT_ORDER+" ASC ";
        Cursor cursor = database.rawQuery(sqlQuery,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                do{
                    Object_Category obj = new Object_Category();
                    obj.id = cursor.getInt(cursor.getColumnIndex(CATE_ID));
                    obj.sort_order = cursor.getInt(cursor.getColumnIndex(CATE_CATEID));
                    obj.name = cursor.getString(cursor.getColumnIndex(CATE_NAME));
                    obj.image = cursor.getString(cursor.getColumnIndex(CATE_IMAGE));
                    obj.parentId  = cursor.getInt(cursor.getColumnIndex(CATE_PARENT_ID));
                    obj.catId  = cursor.getInt(cursor.getColumnIndex(CATE_CATEID));
                    obj.isbusiness  = cursor.getInt(cursor.getColumnIndex(CATE_ISBUSINESS));
                    listCate.add(obj);

                }while (cursor.moveToNext());
            }
        }
        cursor.close();
        Log.i("SUSHIL","cat list size is "+listCate.size());
        return listCate;


        /*Cursor cursor = database.rawQuery("SELECT * FROM Test", null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                do{
                    list.add(cursor.getString(cursor.getColumnIndex("name")));
                }while (cursor.moveToNext());
            }
        }

        cursor.moveToFirst();
       *//* while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }*//*
        cursor.close();
        return list;*/
    }


    public ArrayList<Object_Category> getCategoryForRegister() {

        ArrayList<Object_Category> listCate = new ArrayList<Object_Category>();
        String sqlQuery = "Select * From "+TABLE_NAME+" where "+CATE_ISBUSINESS+" = 1 and "+CATE_ISENABLE+" = 1 Order By "+CATE_SORT_ORDER+" ASC ";
        Cursor cursor = database.rawQuery(sqlQuery,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                do{
                    Object_Category obj = new Object_Category();
                    obj.id = cursor.getInt(cursor.getColumnIndex(CATE_ID));
                    obj.sort_order = cursor.getInt(cursor.getColumnIndex(CATE_CATEID));
                    obj.name = cursor.getString(cursor.getColumnIndex(CATE_NAME));
                    obj.image = cursor.getString(cursor.getColumnIndex(CATE_IMAGE));
                    obj.parentId  = cursor.getInt(cursor.getColumnIndex(CATE_PARENT_ID));
                    obj.catId  = cursor.getInt(cursor.getColumnIndex(CATE_CATEID));
                    listCate.add(obj);

                }while (cursor.moveToNext());
            }
        }
        cursor.close();
        return listCate;


        /*Cursor cursor = database.rawQuery("SELECT * FROM Test", null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                do{
                    list.add(cursor.getString(cursor.getColumnIndex("name")));
                }while (cursor.moveToNext());
            }
        }

        cursor.moveToFirst();
       *//* while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }*//*
        cursor.close();
        return list;*/
    }



    public ArrayList<Object_Category> getCategorySerchable(String CateName) {

        ArrayList<Object_Category> listCate = new ArrayList<Object_Category>();
        String sqlQuery = "Select * From "+TABLE_NAME+" WHERE "+CATE_NAME+" LIKE '%"+CateName+"%'"+" AND "+CATE_ISENABLE+" = 1 Order By "+CATE_SORT_ORDER+" ASC ";
        Cursor cursor = database.rawQuery(sqlQuery,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                do{
                    Object_Category obj = new Object_Category();
                    obj.id = cursor.getInt(cursor.getColumnIndex(CATE_ID));
                    obj.sort_order = cursor.getInt(cursor.getColumnIndex(CATE_CATEID));
                    obj.name = cursor.getString(cursor.getColumnIndex(CATE_NAME));
                    obj.image = cursor.getString(cursor.getColumnIndex(CATE_IMAGE));
                    obj.parentId  = cursor.getInt(cursor.getColumnIndex(CATE_PARENT_ID));
                    obj.catId  = cursor.getInt(cursor.getColumnIndex(CATE_CATEID));
                    obj.isbusiness  = cursor.getInt(cursor.getColumnIndex(CATE_ISBUSINESS));
                    listCate.add(obj);

                }while (cursor.moveToNext());
            }
        }
        cursor.close();
        return listCate;

    }


    public void insertCategory(ArrayList<Object_Category> list){

        for(Object_Category obj : list) {
            ContentValues values = new ContentValues();
            values.put(CATE_SORT_ORDER, obj.sort_order);
            values.put(CATE_CATEID, obj.catId);
            values.put(CATE_NAME, obj.name);
            values.put(CATE_IMAGE,obj.image);
            values.put(CATE_IMAGE, obj.parentId);
            database.insert(TABLE_NAME, null, values);
        }
      database.close();
    }

  /*  public void insertCity(ArrayList<Object_City> list){

        for(Object_City obj : list) {

            ContentValues values = new ContentValues();
            values.put(KEY_CITY_SERVERID, obj.cityId);
            values.put(KEY_CITY_NAME, obj.name);
            values.put(KEY_CITY_ISSELECTED, obj.seLectedCity);

            database.insert(TABLE_NAME_CITY, null, values);
        }
        database.close();
    }*/

    public void deleteCategory(){
        String sql = "Delete from "+TABLE_NAME;
        //database.delete("Contact", null, new String[]{contact.getPhone()});
        database.execSQL(sql);
        database.close();

    }

    /*public void deleteCity(){
        String sql = "Delete from "+TABLE_NAME_CITY;
        //database.delete("Contact", null, new String[]{contact.getPhone()});
        database.execSQL(sql);
        database.close();

    }*/


    public void insertControlTag(ArrayList<Object_Control_Maping> list){
        if (list!=null) {
            for (Object_Control_Maping obj : list) {
                ContentValues values = new ContentValues();
                values.put(KEY_LABEL, obj.label);
                values.put(KEY_CONTROL_ID, obj.controlID);
                values.put(KEY_TYPE_ID, obj.typeId);
                values.put(KEY_SCREEN_ID, obj.screenId);
                values.put(KEY_SORT_ORDER, obj.sortOrder);
                values.put(KEY_HINT,obj.hint);
                values.put(KEY_ISREQUIRED,obj.isrequired);

                // values.put(CATE_CATEID, obj.catId);

                long id = database.insert(TABLE_NAME_CONTROL, null, values);
                Log.i("SUSHIL", "id screen obj.screenId ...." +obj.typeId);

                if (obj.values.size() != 0) {
                    for (int i = 0; i < obj.values.size(); i++) {
                        //ArrayList<Object_Data> listValues = obj.values.get()
                        values.clear();
                        Object_Data objdata = obj.values.get(i);
                        values.put(KEY_DATA_CONTROL_ID, id);
                        values.put(KEY__DATA_TEXT, objdata.value);
                        values.put(KEY_DATA_IMAGE_NAME,objdata.imageName);
                        if(objdata.islink){
                            values.put(KEY_DATA_IS_LINK,1);
                        }
                        database.insert(TABLE_NAME_DATA, null, values);

                    }
                } else {
                    if(obj.typeId==Custom_Control.EditextID) {
                        //obj.typeId==Custom_Control.MultiImageID && obj.typeId!=Custom_Control.SpinnerID
                        values.clear();
                        values.put(KEY_DATA_CONTROL_ID, id);
                        values.put(KEY__DATA_TEXT, "");
                        values.put(KEY_DATA_IMAGE_NAME, "");
                        values.put(KEY_DATA_SELECTED_VALUE, 1);
                        database.insert(TABLE_NAME_DATA, null, values);
                    }
                }
            }
            // database.close();
        }

    }

    public int insertdataNew(String value,int idControll){
        if(idControll!=-1){
            ContentValues values = new ContentValues();
            values.put(KEY_DATA_CONTROL_ID, idControll);
            values.put(KEY__DATA_TEXT, value);
            values.put(KEY_DATA_SELECTED_VALUE,1);
            int id = (int)database.insert(TABLE_NAME_DATA, null, values);
            return  id;
        }
        return 0;
    }

    public void insertdataWithChange(String value,int idControll){

        String updateQuery = "";

        if(!value.trim().isEmpty())
            updateQuery = "Update "+TABLE_NAME_DATA+" SET "+KEY__DATA_TEXT+" = '"+value+"' ,"+KEY_DATA_IS_LINK+" = '0' ,"+KEY_DATA_SELECTED_VALUE+" = '1' WHERE "+KEY_DATA_ID+" = "+idControll;
        else
            updateQuery = "Update "+TABLE_NAME_DATA+" SET "+KEY_DATA_SELECTED_VALUE+" = '1' WHERE "+KEY_DATA_ID+" = "+idControll;

        Log.i("SUSHIL","update query for "+updateQuery);
        database.execSQL(updateQuery);

    }

    public void updateimageName(String value,int idControll){
        String updateQuery = "";

        if(!value.trim().isEmpty())
            updateQuery = "Update "+TABLE_NAME_DATA+" SET "+KEY_DATA_IMAGE_NAME+" = '"+value+"' ,"+KEY_DATA_IS_LINK+" = '0' ,"+KEY_DATA_SELECTED_VALUE+" = '1' WHERE "+KEY_DATA_ID+" = "+idControll;

        Log.i("SUSHIL","update query for "+updateQuery);
        database.execSQL(updateQuery);

    }

    public ArrayList<Object_Control_Maping> getAllData(){

        ArrayList<Object_Control_Maping> list = new ArrayList<>();
        String selectQuery = "Select * from "+TABLE_NAME_CONTROL+" Inner join "+TABLE_NAME_DATA+" ON "+TABLE_NAME_CONTROL+"."+KEY_ID+" = "+TABLE_NAME_DATA+"."+KEY_DATA_CONTROL_ID+" WHERE "+TABLE_NAME_DATA+"."+KEY_DATA_SELECTED_VALUE+" = 1";
        Cursor cursor = database.rawQuery(selectQuery,null);
        if(cursor!=null){
            if(cursor.moveToFirst()) {
                do {
                    Object_Control_Maping obj = new Object_Control_Maping();
                    obj.controlID = cursor.getInt(cursor.getColumnIndex(KEY_CONTROL_ID));
                    obj.hint = cursor.getString(cursor.getColumnIndex(KEY_HINT));
                    obj.isrequired = cursor.getInt(cursor.getColumnIndex(KEY_ISREQUIRED));
                   // Log.i("SUSHIL","SUSHIL image control id is "+obj.controlID);
                    obj.typeId = cursor.getInt(cursor.getColumnIndex(KEY_TYPE_ID));
                    if(obj.typeId==Custom_Control.EditextID) {
                        obj.value = cursor.getString(cursor.getColumnIndex(KEY__DATA_TEXT));
                    }
                    else{
                        obj.value = cursor.getString(cursor.getColumnIndex(KEY_DATA_IMAGE_NAME));
                    }
                    obj.controlID = cursor.getInt(cursor.getColumnIndex(KEY_CONTROL_ID));
                    obj.label = cursor.getString(cursor.getColumnIndex(KEY_LABEL));

                    list.add(obj);

                } while (cursor.moveToNext());
            }
        }
        Log.i("SUSHIL", "list size is  " + list.size());
        return list;

    }

    public ArrayList<Object_Control_Maping> getDataWithPosition(int screenID){

        ArrayList<Object_Control_Maping> list = new ArrayList<>();
        String selectQuery = "Select * from "+TABLE_NAME_CONTROL+" where "+KEY_SCREEN_ID+" = "+screenID;

        Cursor cursor = database.rawQuery(selectQuery,null);
        if (cursor!=null){
            if(cursor.moveToFirst()){
                do{
                    Object_Control_Maping obj = new Object_Control_Maping();
                    obj.id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                    obj.label = cursor.getString(cursor.getColumnIndex(KEY_LABEL));
                    obj.typeId = cursor.getInt(cursor.getColumnIndex(KEY_TYPE_ID));
                    obj.controlID = cursor.getInt(cursor.getColumnIndex(KEY_CONTROL_ID));
                    obj.screenId = cursor.getInt(cursor.getColumnIndex(KEY_CONTROL_ID));
                    obj.sortOrder = cursor.getInt(cursor.getColumnIndex(KEY_SORT_ORDER));
                    obj.hint = cursor.getString(cursor.getColumnIndex(KEY_HINT));
                    obj.isrequired = cursor.getInt(cursor.getColumnIndex(KEY_ISREQUIRED));

                    ArrayList<Object_Data> listValues = new ArrayList<>();
                    String SelectSub = "Select * from "+TABLE_NAME_DATA+" Where "+KEY_DATA_CONTROL_ID+" = "+obj.id;
                    Cursor cursorSub = database.rawQuery(SelectSub,null);
                    if(cursorSub!=null){
                        if(cursorSub.moveToFirst()){
                            do {
                                Object_Data objData = new Object_Data();
                                objData.id = cursorSub.getInt(cursorSub.getColumnIndex(KEY_DATA_ID));
                                //objData.controlId = cursorSub.getInt(cursorSub.getColumnIndex(KEY_DATA_CONTROL_ID));
                                objData.value = cursorSub.getString(cursorSub.getColumnIndex(KEY__DATA_TEXT));
                                if(cursorSub.getInt(cursorSub.getColumnIndex(KEY_DATA_IS_LINK))==0){
                                    objData.islink = false;
                                }else{
                                    objData.islink = true;
                                }
                                objData.selectedValue = cursorSub.getInt(cursorSub.getColumnIndex(KEY_DATA_SELECTED_VALUE));
                                listValues.add(objData);
                            }while (cursorSub.moveToNext());
                        }
                    }
                    obj.values = listValues;
                    list.add(obj);
                }while(cursor.moveToNext());
            }
        }
        Log.i("SUSHIL", "SUSHIL list size return " + list.size());
        return  list;
    }

    public ArrayList<Object_Image_Upload> getImageforUpload(){

        ArrayList<Object_Image_Upload> list = new ArrayList<>();
        String query = "select id2,"+KEY__DATA_TEXT+" from(select *,Data.id id2 from "+TABLE_NAME_CONTROL+" inner join "+TABLE_NAME_DATA+" on "+TABLE_NAME_CONTROL+".id = "+TABLE_NAME_DATA+"."+KEY_DATA_CONTROL_ID+" where "+TABLE_NAME_CONTROL+"."+KEY_TYPE_ID+" = "+Custom_Control.ImageViewID+" or "+TABLE_NAME_CONTROL+"."+KEY_TYPE_ID+" = "+Custom_Control.MultiImageID+") where "+KEY_DATA_IS_LINK +" = 0";
        Cursor cursor = database.rawQuery(query,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                do{
                  Object_Image_Upload obj = new Object_Image_Upload();
                    obj.id = cursor.getInt(cursor.getColumnIndex("id2"));
                    obj.value = cursor.getString(cursor.getColumnIndex(KEY__DATA_TEXT));
                    list.add(obj);
                }while (cursor.moveToNext());
            }
        }
        Log.i("SUSHIL", "SUSHIL list size return image " + list.size());
        return list;
    }


    public void deleteSomeData(int id){
        String deleteData = "Delete from "+TABLE_NAME_DATA+" where "+KEY_DATA_ID+" = "+id;
        database.execSQL(deleteData);
    }


    public void deleteDataControl(){
        String deleteControl = "Delete from "+TABLE_NAME_CONTROL;
        String deleteData = "Delete from "+TABLE_NAME_DATA;
        database.execSQL(deleteControl);
        database.execSQL(deleteData);
        //database.close();
    }


}
