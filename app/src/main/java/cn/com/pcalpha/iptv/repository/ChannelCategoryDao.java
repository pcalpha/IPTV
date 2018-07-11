package cn.com.pcalpha.iptv.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import cn.com.pcalpha.iptv.model.Channel;
import cn.com.pcalpha.iptv.model.ChannelCategory;

/**
 * Created by caiyida on 2018/6/24.
 */

public class ChannelCategoryDao extends BaseDao {


    public static final String TABLE_NAME = "CHANNEL_CATEGORY";

    public static final String COLUMN_NO = "NO";
    public static final String COLUMN_NAME = "NAME";

    public static final String ALL_COLUMNS[] = new String[]{
            COLUMN_NO,
            COLUMN_NAME
    };

    public static final String SQL_CREATE =
            " CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    " (" +
                    COLUMN_NO + " INTEGER PRIMARY KEY, " +
                    COLUMN_NAME + " VARCHAR " +
                    ") ";
    private static final String SQL_DROP = "DROP TABLE IF EXISTS "+ TABLE_NAME;

    public ChannelCategoryDao(Context context) {
        super(context);
    }

    public void insertAsync(final ChannelCategory channelCategory) {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... params) {
                insert(channelCategory);
                return null;
            }
        }.execute();
    }

    public void insert(ChannelCategory channelCategory) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NO,channelCategory.getNo());
        cv.put(COLUMN_NAME, channelCategory.getName());
        writeDb.insert(TABLE_NAME, null, cv);
    }

    public void delete(Integer channelNo){
        String[] whereArgs = new  String[]{ channelNo.toString() };
        writeDb.delete(TABLE_NAME,"no=?",whereArgs);
    }

    public void clear(){
        writeDb.delete(TABLE_NAME,null,null);
    }

    public void update(ChannelCategory channelCategory){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NO, channelCategory.getNo());
        cv.put(COLUMN_NAME, channelCategory.getName());

        String[] whereArgs = new  String[]{ channelCategory.getNo().toString() };
        writeDb.update(TABLE_NAME,cv,"no=?",whereArgs);
    }

    List<ChannelCategory> channelCategoryList;//缓存频道列表
    public List<ChannelCategory> findAll(){
        if(null!=channelCategoryList){
            return channelCategoryList;
        }
        Cursor cursor = readDb.query(TABLE_NAME, ALL_COLUMNS, null, null, null, null,
                COLUMN_NO + " ASC",null);

        if(0==cursor.getCount()){
            return null;
        }

        Integer mIndex_no = cursor.getColumnIndex(COLUMN_NO);
        Integer mIndex_name = cursor.getColumnIndex(COLUMN_NAME);

        channelCategoryList = new ArrayList<>();
        while(cursor.moveToNext()){
            ChannelCategory channel = new ChannelCategory(
                    cursor.getInt(mIndex_no),
                    cursor.getString(mIndex_name)
            );

            channelCategoryList.add(channel);
        }

        return channelCategoryList;
    }
}
