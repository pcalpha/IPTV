package cn.com.pcalpha.iptv.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.pcalpha.iptv.model.bo.Param4Channel;
import cn.com.pcalpha.iptv.model.bo.Param4ChannelStream;
import cn.com.pcalpha.iptv.model.domain.Channel;
import cn.com.pcalpha.iptv.model.domain.ChannelStream;

/**
 * Created by caiyida on 2018/8/8.
 */

public class ChannelStreamDao extends BaseDao {
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

    public static final String SQL_CREATE =
            " CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " VARCHAR, " +
                    COLUMN_URL + " VARCHAR, " +
                    COLUMN_CARRIER + " VARCHAR, " +
                    COLUMN_CHANNEL_NAME + " VARCHAR, " +
                    COLUMN_LAST_PLAY + " INTEGER " +
                    ") ";

    public static final String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;


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
        super(context);
    }

    public void insertAsync(final ChannelStream channelStream) {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... params) {
                insert(channelStream);
                return null;
            }
        }.execute();
    }

    public void insert(ChannelStream channelStream) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, channelStream.getId());
        cv.put(COLUMN_NAME, channelStream.getName());
        cv.put(COLUMN_URL, channelStream.getUrl());
        cv.put(COLUMN_CARRIER, channelStream.getCarrier());
        cv.put(COLUMN_LAST_PLAY, channelStream.getLastPlay());
        cv.put(COLUMN_CHANNEL_NAME, channelStream.getChannelName());
        writeDb.insert(TABLE_NAME, null, cv);
    }

    public void insertBatch(List<ChannelStream> channelStreamList) {
        if (null != channelStreamList) {
            writeDb.beginTransaction();
            for (ChannelStream channelStream : channelStreamList) {
                insert(channelStream);
            }
            writeDb.setTransactionSuccessful();
        }
    }

    public void delete(Integer id) {
        String[] whereArgs = new String[]{id.toString()};
        writeDb.delete(TABLE_NAME, COLUMN_ID + "=?", whereArgs);
    }

    public void delete(String name) {
        String[] whereArgs = new String[]{name};
        writeDb.delete(TABLE_NAME, COLUMN_NAME + "=?", whereArgs);
    }

    public void clear() {
        writeDb.delete(TABLE_NAME, null, null);
    }

    public void update(ChannelStream channelStream) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, channelStream.getId());
        cv.put(COLUMN_NAME, channelStream.getName());
        cv.put(COLUMN_URL, channelStream.getUrl());
        cv.put(COLUMN_CARRIER, channelStream.getCarrier());
        cv.put(COLUMN_LAST_PLAY, channelStream.getLastPlay());
        cv.put(COLUMN_CHANNEL_NAME, channelStream.getChannelName());

        String[] whereArgs = new String[]{String.valueOf(channelStream.getId())};
        writeDb.update(TABLE_NAME, cv, COLUMN_ID + "=?", whereArgs);
    }

    public ChannelStream get(Integer id) {
        String selection = COLUMN_ID + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(id)};
        Cursor cursor = readDb.query(TABLE_NAME, ALL_COLUMNS, selection, selectionArgs, null, null,
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
        String selection = "";
        List<String> args = new ArrayList<>();
        if (null != param.getChannelName() && !"".equals(param.getChannelName())) {
            selection = COLUMN_CHANNEL_NAME + " = ?";
            args.add(param.getChannelName());
        }
        if (null != param.getCarrier() && !"".equals(param.getCarrier())) {
            selection = COLUMN_CARRIER + " = ?";
            args.add(param.getCarrier());
        }

        String[] selectionArgs = args.toArray(new String[args.size()]);

        Cursor cursor = readDb.query(TABLE_NAME, ALL_COLUMNS, selection, selectionArgs, null, null,
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
        String sql = "UPDATE " + TABLE_NAME + " SET " + COLUMN_LAST_PLAY + " = 0 ";
        readDb.execSQL(sql);
        String sql2 = "UPDATE " + TABLE_NAME + " SET " + COLUMN_LAST_PLAY + " = 1 WHERE " + COLUMN_ID + " = " + id;
        readDb.execSQL(sql2);
    }

    public ChannelStream getLastPlay() {
        String selection = COLUMN_LAST_PLAY + " = 1 ";
        Cursor cursor = readDb.query(TABLE_NAME, ALL_COLUMNS, selection, null, null, null,
                COLUMN_ID + " ASC", null);
        List<ChannelStream> resultList = buildResult(cursor);

        if(null==resultList||resultList.size()<1){
            return null;
        }
        cursor.close();
        return resultList.get(0);
    }

}
