package cn.com.pcalpha.iptv.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by caiyida on 2018/2/7.
 */

public class BaseService {
    protected Context mAppContext;
    protected SQLiteDatabase readDb;
    protected SQLiteDatabase writeDb;

    BaseService(Context context){
        mAppContext = context.getApplicationContext();
        //初始化数据库工具
        OpenHelper openHelper = new OpenHelper(mAppContext);
        readDb = openHelper.getReadableDatabase();
        writeDb = openHelper.getWritableDatabase();
    }

    public static class OpenHelper extends SQLiteOpenHelper {
        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "IPTV.db";


        public OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {}

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
    }
}
