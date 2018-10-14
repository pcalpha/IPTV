package cn.com.pcalpha.iptv.category;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import cn.com.pcalpha.iptv.tools.DbHelper;

/**
 * Created by caiyida on 2018/6/24.
 */

public class ChannelCategoryDao {
    private DbHelper mDbHelper;

    public static final String TABLE_NAME = "CHANNEL_CATEGORY";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_LAST_PLAY = "LAST_PLAY";

    public static final String ALL_COLUMNS[] = new String[]{
            COLUMN_ID,
            COLUMN_NAME,
            COLUMN_LAST_PLAY
    };

    private static ChannelCategoryDao singleton;

    public static ChannelCategoryDao getInstance(Context context) {
        if (null == singleton) {
            synchronized (ChannelCategoryDao.class) {
                if (null == singleton) {
                    singleton = new ChannelCategoryDao(context);
                }
            }
        }
        return singleton;
    }

    private ChannelCategoryDao(Context context) {
        this.mDbHelper = DbHelper.getInstance(context);
    }

    public void insert(ChannelCategory channelCategory) {
        SQLiteDatabase db =mDbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, channelCategory.getId());
        cv.put(COLUMN_NAME, channelCategory.getName());
        cv.put(COLUMN_LAST_PLAY, channelCategory.getLastPlay());
        db.insert(TABLE_NAME, null, cv);
    }

    public void delete(Integer channelNo) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String[] whereArgs = new String[]{channelNo.toString()};
        db.delete(TABLE_NAME, "no=?", whereArgs);
    }

    public void clear() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }

    public void update(ChannelCategory channelCategory) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, channelCategory.getId());
        cv.put(COLUMN_NAME, channelCategory.getName());
        cv.put(COLUMN_LAST_PLAY, channelCategory.getLastPlay());
        String[] whereArgs = new String[]{channelCategory.getId().toString()};
        db.update(TABLE_NAME, cv, "no=?", whereArgs);
    }

    public ChannelCategory get(Integer id) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String selection = COLUMN_ID + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(id)};
        Cursor cursor = db.query(TABLE_NAME, ALL_COLUMNS, selection, selectionArgs, null, null,
                COLUMN_ID + " ASC", null);
        if (0 == cursor.getCount()) {
            return null;
        }

        List<ChannelCategory> resultList = buildResult(cursor);
        if (null == resultList || resultList.size() < 1) {
            return null;
        }
        return resultList.get(0);
    }

    //private List<ChannelCategory> channelCategoryList;//缓存列表
    public List<ChannelCategory> findAll() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        //init();
//        if (null != channelCategoryList) {
//            return channelCategoryList;
//        }

        Cursor cursor = db.query(TABLE_NAME, ALL_COLUMNS, null, null, null, null,
                COLUMN_ID + " ASC", null);

        List<ChannelCategory> result = buildResult(cursor);
//        channelCategoryList = result;
        return result;
    }

    private List<ChannelCategory> buildResult(Cursor cursor) {
        if (0 == cursor.getCount()) {
            return new ArrayList<>();
        }

        Integer mIndex_id = cursor.getColumnIndex(COLUMN_ID);
        Integer mIndex_name = cursor.getColumnIndex(COLUMN_NAME);
        Integer mIndex_last_play = cursor.getColumnIndex(COLUMN_LAST_PLAY);

        List<ChannelCategory> channelCategoryList = new ArrayList<>();
        while (cursor.moveToNext()) {
            ChannelCategory channelCategory = new ChannelCategory();

            channelCategory.setId(cursor.getInt(mIndex_id));
            channelCategory.setName(cursor.getString(mIndex_name));
            channelCategory.setLastPlay(cursor.getInt(mIndex_last_play));
            channelCategoryList.add(channelCategory);
        }

        return channelCategoryList;
    }

    public void setLastPlay(final String categoryName) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String sql = "UPDATE " + TABLE_NAME + " SET " + COLUMN_LAST_PLAY + " = 0 ";
                db.execSQL(sql);
                String sql2 = "UPDATE " + TABLE_NAME + " SET " + COLUMN_LAST_PLAY + " = 1 WHERE " + COLUMN_NAME + " = " + "'" + categoryName + "'";
                db.execSQL(sql2);
            }
        }).start();
    }

    public ChannelCategory getLastPlay() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String selection = COLUMN_LAST_PLAY + " = 1 ";
        Cursor cursor = db.query(TABLE_NAME, ALL_COLUMNS, selection, null, null, null,
                COLUMN_ID + " ASC", null);
        if (0 == cursor.getCount()) {
            return null;
        }

        List<ChannelCategory> resultList = buildResult(cursor);
        if (null == resultList || resultList.size() < 1) {
            return null;
        }
        return resultList.get(0);
    }

    public void init() {
        clear();
        this.insert(new ChannelCategory(1, "央视频道"));
        this.insert(new ChannelCategory(2, "卫视频道"));
    }
}
