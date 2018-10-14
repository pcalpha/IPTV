package cn.com.pcalpha.iptv.channel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import cn.com.pcalpha.iptv.category.ChannelCategoryDao;
import cn.com.pcalpha.iptv.tools.DbHelper;

/**
 * Created by caiyida on 2018/8/8.
 */

public class ChannelStreamDao {
    private DbHelper mDbHelper;

    public static final String TABLE_NAME = "CHANNEL_STREAM";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_URL = "URL";
    public static final String COLUMN_CARRIER = "CARRIER";
    public static final String COLUMN_CHANNEL_NAME = "CHANNEL_NAME";
    public static final String COLUMN_LAST_PLAY = "LAST_PLAY";

    public static final String ALL_COLUMNS[] = new String[]{
            COLUMN_ID,
            COLUMN_NAME,
            COLUMN_URL,
            COLUMN_CARRIER,
            COLUMN_CHANNEL_NAME,
            COLUMN_LAST_PLAY
    };

    private static ChannelStreamDao singleton;
    public static ChannelStreamDao getInstance(Context context) {
        if (null == singleton) {
            synchronized (ChannelStreamDao.class) {
                if (null == singleton) {
                    singleton = new ChannelStreamDao(context);
                }
            }
        }
        return singleton;
    }

    private ChannelStreamDao(Context context) {
        this.mDbHelper = DbHelper.getInstance(context);
    }

    public void insert(ChannelStream channelStream) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, channelStream.getId());
        cv.put(COLUMN_NAME, channelStream.getName());
        cv.put(COLUMN_URL, channelStream.getUrl());
        cv.put(COLUMN_CARRIER, channelStream.getCarrier());
        cv.put(COLUMN_LAST_PLAY, channelStream.getLastPlay());
        cv.put(COLUMN_CHANNEL_NAME, channelStream.getChannelName());
        db.insert(TABLE_NAME, null, cv);
    }

    public void insertBatch(List<ChannelStream> channelStreamList) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        if (null != channelStreamList) {
            db.beginTransaction();
            for (ChannelStream channelStream : channelStreamList) {
                insert(channelStream);
            }
            db.setTransactionSuccessful();
        }
    }

    public void delete(Integer id) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String[] whereArgs = new String[]{id.toString()};
        db.delete(TABLE_NAME, COLUMN_ID + "=?", whereArgs);
    }

    public void delete(String name) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String[] whereArgs = new String[]{name};
        db.delete(TABLE_NAME, COLUMN_NAME + "=?", whereArgs);
    }

    public void clear() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }

    public void update(ChannelStream channelStream) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, channelStream.getId());
        cv.put(COLUMN_NAME, channelStream.getName());
        cv.put(COLUMN_URL, channelStream.getUrl());
        cv.put(COLUMN_CARRIER, channelStream.getCarrier());
        cv.put(COLUMN_LAST_PLAY, channelStream.getLastPlay());
        cv.put(COLUMN_CHANNEL_NAME, channelStream.getChannelName());

        String[] whereArgs = new String[]{String.valueOf(channelStream.getId())};
        db.update(TABLE_NAME, cv, COLUMN_ID + "=?", whereArgs);
    }

    public ChannelStream get(Integer id) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String selection = COLUMN_ID + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(id)};
        Cursor cursor = db.query(TABLE_NAME, ALL_COLUMNS, selection, selectionArgs, null, null,
                COLUMN_ID + " ASC", null);
        if (0 == cursor.getCount()) {
            return null;
        }

        List<ChannelStream> resultList = buildResult(cursor);
        if (null == resultList || resultList.size() < 1) {
            return null;
        }
        return resultList.get(0);
    }


    public List<ChannelStream> find(Param4ChannelStream param) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String selection = "";
        List<String> args = new ArrayList<>();
        if (null != param.getChannelName() && !"".equals(param.getChannelName())) {
            if(null!=selection&&selection.length()>0){
                selection +=" AND ";
            }
            selection += COLUMN_CHANNEL_NAME + " = ? ";
            args.add(param.getChannelName());
        }
        if (null != param.getCarrier() && !"".equals(param.getCarrier())) {
            if(null!=selection&&selection.length()>0){
                selection +=" AND ";
            }
            selection += COLUMN_CARRIER + " = ? ";
            args.add(param.getCarrier());
        }

        String[] selectionArgs = args.toArray(new String[args.size()]);

        Cursor cursor = db.query(TABLE_NAME, ALL_COLUMNS, selection, selectionArgs, null, null,
                COLUMN_ID + " ASC", null);
        return buildResult(cursor);
    }

    private List<ChannelStream> buildResult(Cursor cursor) {
        if (0 == cursor.getCount()) {
            return new ArrayList<>();
        }

        Integer mIndex_id = cursor.getColumnIndex(COLUMN_ID);
        Integer mIndex_name = cursor.getColumnIndex(COLUMN_NAME);
        Integer mIndex_url = cursor.getColumnIndex(COLUMN_URL);
        Integer mIndex_carrier = cursor.getColumnIndex(COLUMN_CARRIER);
        Integer mIndex_last_play = cursor.getColumnIndex(COLUMN_LAST_PLAY);
        Integer mIndex_channel_name = cursor.getColumnIndex(COLUMN_CHANNEL_NAME);

        List<ChannelStream> channelStreamList = new ArrayList<>();
        while (cursor.moveToNext()) {
            ChannelStream channelStream = new ChannelStream();
            channelStream.setId(cursor.getInt(mIndex_id));
            channelStream.setName(cursor.getString(mIndex_name));
            channelStream.setUrl(cursor.getString(mIndex_url));
            channelStream.setCarrier(cursor.getString(mIndex_carrier));
            channelStream.setLastPlay(cursor.getInt(mIndex_last_play));
            channelStream.setChannelName(cursor.getString(mIndex_channel_name));

            channelStreamList.add(channelStream);
        }
        cursor.close();
        return channelStreamList;
    }

//    public Map<String, List<ChannelStream>> getChannelStreamMap(String channelName) {
//        Param4ChannelStream param = Param4ChannelStream.build().setChannelName(channelName);
//        List<ChannelStream> channelStreamList = find(param);
//
//        Map<String, List<ChannelStream>> result = new HashMap<>();
//        for (ChannelStream channelStream : channelStreamList) {
//            List<ChannelStream> streamList = result.get(channelStream.getChannelName());
//            if(null==streamList){
//                streamList = new ArrayList<>();
//                result.put(channelStream.getChannelName(),streamList);
//            }
//            streamList.add(channelStream);
//        }
//        return result;
//    }

    public void setLastPlay(int id) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String sql = "UPDATE " + TABLE_NAME + " SET " + COLUMN_LAST_PLAY + " = 0 ";
        db.execSQL(sql);
        String sql2 = "UPDATE " + TABLE_NAME + " SET " + COLUMN_LAST_PLAY + " = 1 WHERE " + COLUMN_ID + " = " + id;
        db.execSQL(sql2);
    }

    public ChannelStream getLastPlay() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String selection = COLUMN_LAST_PLAY + " = 1 ";
        Cursor cursor = db.query(TABLE_NAME, ALL_COLUMNS, selection, null, null, null,
                COLUMN_ID + " ASC", null);
        List<ChannelStream> resultList = buildResult(cursor);

        if(null==resultList||resultList.size()<1){
            return null;
        }
        cursor.close();
        return resultList.get(0);
    }


}
