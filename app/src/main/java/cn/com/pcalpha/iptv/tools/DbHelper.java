package cn.com.pcalpha.iptv.tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import cn.com.pcalpha.iptv.category.ChannelCategoryDao;
import cn.com.pcalpha.iptv.channel.ChannelDao;
import cn.com.pcalpha.iptv.channel.ChannelStream;
import cn.com.pcalpha.iptv.channel.ChannelStreamDao;

public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 16;
    private static final String DATABASE_NAME = "IPTV.db";

    private static DbHelper singleton;

    public static DbHelper getInstance(Context context) {
        if (null == singleton) {
            synchronized (ChannelDao.class) {
                if (null == singleton) {
                    singleton = new DbHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
                }
            }
        }
        return singleton;
    }

    private DbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createChannel(db);
        createChannelCategory(db);
        createChannelStream(db);
    }

    private void createChannel(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS " + ChannelDao.TABLE_NAME
                + " ("
                +"'"+ ChannelDao.COLUMN_ID + "' INTEGER PRIMARY KEY AUTOINCREMENT, "
                +"'"+ ChannelDao.COLUMN_NO + "' VARCHAR, "
                +"'"+ ChannelDao.COLUMN_NAME + "' VARCHAR, "
                +"'"+ ChannelDao.COLUMN_CATEGORY_NAME + "' VARCHAR, "
                +"'"+ ChannelDao.COLUMN_STREAM_ID + "' VARCHAR, "
                +"'"+ ChannelDao.COLUMN_LAST_PLAY + "' INTEGER "
                + ") ");
    }



    private void createChannelCategory(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS " + ChannelCategoryDao.TABLE_NAME
                + " ("
                + "'" + ChannelCategoryDao.COLUMN_ID + "' INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "'" + ChannelCategoryDao.COLUMN_NAME + "' VARCHAR, "
                + "'" + ChannelCategoryDao.COLUMN_LAST_PLAY + "' INTEGER "
                + ")");

    }

    private void createChannelStream(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS " + ChannelStreamDao.TABLE_NAME
                + " ("
                +"'"+ ChannelStreamDao.COLUMN_ID + "' INTEGER PRIMARY KEY AUTOINCREMENT, "
                +"'"+ ChannelStreamDao.COLUMN_NAME + "' VARCHAR, "
                +"'"+ ChannelStreamDao.COLUMN_URL + "' VARCHAR, "
                +"'"+ ChannelStreamDao.COLUMN_CARRIER + "' VARCHAR, "
                +"'"+ ChannelStreamDao.COLUMN_CHANNEL_NAME + "' VARCHAR, "
                +"'"+ ChannelStreamDao.COLUMN_LAST_PLAY + "' INTEGER "
                + ")");
    }




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        recreateChannel(db);
        recreateChannelCategory(db);
        recreateChannelStream(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        recreateChannel(db);
        recreateChannelCategory(db);
        recreateChannelStream(db);
    }

    private void recreateChannel(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS " + ChannelDao.TABLE_NAME);
        createChannel(db);
    }

    private void recreateChannelCategory(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS " + ChannelCategoryDao.TABLE_NAME);
        createChannelCategory(db);
    }

    private void recreateChannelStream(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS " + ChannelStreamDao.TABLE_NAME);
        createChannelStream(db);
    }
}
