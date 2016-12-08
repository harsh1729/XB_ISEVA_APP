package com.iseva.app.source;

import android.content.Context;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by com.bitsandbytes on 11/30/2015.
 */
public class DBHandler_Main extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "iServiceDatabase.db";
    private static final int DATABASE_VERSION = 2;

    public DBHandler_Main(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
