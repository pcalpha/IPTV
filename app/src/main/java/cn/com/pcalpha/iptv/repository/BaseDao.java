package cn.com.pcalpha.iptv.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDao {
    protected Context context;
    protected SQLiteDatabase readDb;
    protected SQLiteDatabase writeDb;

    BaseDao(Context context){
        context = context.getApplicationContext();
        //初始化数据库工具
        OpenHelper openHelper = new OpenHelper(context);
        readDb = openHelper.getReadableDatabase();
        writeDb = openHelper.getWritableDatabase();
    }

    public static class OpenHelper extends SQLiteOpenHelper {
        private static final int DATABASE_VERSION = 5;
        private static final String DATABASE_NAME = "IPTV.db";


        public OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(ChannelDao.SQL_CREATE);
            db.execSQL(ChannelCategoryDao.SQL_CREATE);
            db.execSQL(ChannelStreamDao.SQL_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
    }
}