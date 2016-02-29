package com.example.xb_sushil.iseva;

import android.content.Context;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by xb_sushil on 11/30/2015.
 */
public class DBHandler_Main extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "iServiceDatabase.db";
    private static final int DATABASE_VERSION = 1;

    public DBHandler_Main(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
