package cn.com.pcalpha.iptv.channel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import cn.com.pcalpha.iptv.R;
import cn.com.pcalpha.iptv.category.ChannelCategoryDao;
import cn.com.pcalpha.iptv.tools.DbHelper;

/**
 * Created by caiyida on 2018/6/24.
 */
public class ChannelDao {
    private DbHelper mDbHelper;

    public static final String TABLE_NAME = "CHANNEL";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_NO = "NO";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_STREAM_ID = "SID";
    public static final String COLUMN_CATEGORY_NAME = "CATEGORY_NAME";
    public static final String COLUMN_LAST_PLAY = "LAST_PLAY";

    public static final String ALL_COLUMNS[] = new String[]{
            COLUMN_ID,
            COLUMN_NO,
            COLUMN_NAME,
            COLUMN_STREAM_ID,
            COLUMN_CATEGORY_NAME,
            COLUMN_LAST_PLAY
    };

    private static ChannelDao singleton;
    public static ChannelDao getInstance(Context context) {
        if (null == singleton) {
            synchronized (ChannelDao.class) {
                if (null == singleton) {
                    singleton = new ChannelDao(context);
                }
            }
        }
        return singleton;
    }

    private ChannelDao(Context context) {
        this.mDbHelper = DbHelper.getInstance(context);
    }

    public void insertAsync(final Channel channel) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                insert(channel);
            }
        }).start();
    }

    public void insert(Channel channel) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, channel.getId());
        cv.put(COLUMN_NO, channel.getNo());
        cv.put(COLUMN_NAME, channel.getName());
        cv.put(COLUMN_LAST_PLAY, channel.getLastPlay());
        cv.put(COLUMN_STREAM_ID, channel.getsId());
        cv.put(COLUMN_CATEGORY_NAME, channel.getCategoryName());
        db.insert(TABLE_NAME, null, cv);
    }

    public void insertBatch(List<Channel> channelList) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        if (null != channelList) {
            db.beginTransaction();
            for (Channel channel : channelList) {
                insert(channel);
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

    public void update(Channel channel) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, channel.getId());
        cv.put(COLUMN_NO, channel.getNo());
        cv.put(COLUMN_NAME, channel.getName());
        cv.put(COLUMN_LAST_PLAY, channel.getLastPlay());
        cv.put(COLUMN_STREAM_ID, channel.getsId());
        cv.put(COLUMN_CATEGORY_NAME, channel.getCategoryName());

        String[] whereArgs = new String[]{channel.getId().toString()};
        db.update(TABLE_NAME, cv, COLUMN_ID + "=?", whereArgs);
    }

    public Channel get(String channelName) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String selection = COLUMN_NAME + " = ? ";
        String[] selectionArgs = new String[]{channelName};
        Cursor cursor = db.query(TABLE_NAME, ALL_COLUMNS, selection, selectionArgs, null, null,
                COLUMN_NO + " ASC", null);
        if (0 == cursor.getCount()) {
            return null;
        }

        List<Channel> resultList = buildResult(cursor);
        if (null == resultList || resultList.size() < 1) {
            return null;
        }
        cursor.close();
        return resultList.get(0);
    }

    public Channel get(Integer id) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String selection = COLUMN_ID + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(id)};
        Cursor cursor = db.query(TABLE_NAME, ALL_COLUMNS, selection, selectionArgs, null, null,
                COLUMN_ID + " ASC", null);
        if (0 == cursor.getCount()) {
            return null;
        }

        List<Channel> resultList = buildResult(cursor);
        if (null == resultList || resultList.size() < 1) {
            return null;
        }
        return resultList.get(0);
    }

    //private List<Channel> channelList;//缓存列表

    public List<Channel> find(Param4Channel param) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        //initChannel();
//        if (null != channelList) {
//            return channelList;
//        }

        String selection = "";
        List<String> args = new ArrayList<>();
        if (null != param.getCategoryName() && !"".equals(param.getCategoryName())) {
            if (null != selection && selection.length() > 0) {
                selection += " AND ";
            }
            selection += COLUMN_CATEGORY_NAME + " = ? ";
            args.add(param.getCategoryName());
        }

        String[] selectionArgs = args.toArray(new String[args.size()]);

        Cursor cursor = db.query(TABLE_NAME, ALL_COLUMNS, selection, selectionArgs, null, null,
                COLUMN_ID + " ASC", null);
        List<Channel> result = buildResult(cursor);
        //channelList = result;
        return result;
    }

    private List<Channel> buildResult(Cursor cursor) {
        if (0 == cursor.getCount()) {
            return new ArrayList<>();
        }

        Integer mIndex_id = cursor.getColumnIndex(COLUMN_ID);
        Integer mIndex_no = cursor.getColumnIndex(COLUMN_NO);
        Integer mIndex_name = cursor.getColumnIndex(COLUMN_NAME);
        Integer mIndex_last_play = cursor.getColumnIndex(COLUMN_LAST_PLAY);
        Integer mIndex_stream_id = cursor.getColumnIndex(COLUMN_STREAM_ID);
        Integer mIndex_category_name = cursor.getColumnIndex(COLUMN_CATEGORY_NAME);
        List<Channel> channelList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Channel channel = new Channel();
            channel.setId(cursor.getInt(mIndex_id));
            channel.setNo(cursor.getString(mIndex_no));
            channel.setName(cursor.getString(mIndex_name));
            channel.setLastPlay(cursor.getInt(mIndex_last_play));
            channel.setsId(cursor.getInt(mIndex_stream_id));
            channel.setCategoryName(cursor.getString(mIndex_category_name));

            channelList.add(channel);
        }
        cursor.close();
//        buildLinkedList(channelList);
        return channelList;
    }

//    /**
//     * 将list构建成链表
//     *
//     * @param channelList
//     */
//    private void buildLinkedList(List<Channel> channelList) {
//        for (int i = 0; i < channelList.size(); i++) {
//            Channel cur = channelList.get(i);
//            //第一个元素没有前驱
//            if (0 == i) {
//                cur.setPre(null);
//            } else {
//                cur.setPre(channelList.get(i - 1));
//            }
//            //最后一个元素没有后继
//            if (i == channelList.size() - 1) {
//                cur.setNext(null);
//            } else {
//                cur.setNext(channelList.get(i + 1));
//            }
//        }
//    }

    public void setLastPlay(final String channelName) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String sql = "UPDATE " + TABLE_NAME + " SET " + COLUMN_LAST_PLAY + " = 0 ";
                db.execSQL(sql);
                String sql2 = "UPDATE " + TABLE_NAME + " SET " + COLUMN_LAST_PLAY + " = 1 " + " WHERE " + COLUMN_NAME + " = " + "'" + channelName + "'";
                db.execSQL(sql2);
            }
        }).start();
    }


    public void setLastPlayStream(final String channelName, final int streamId) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String sql = "UPDATE " + TABLE_NAME + " SET " + COLUMN_STREAM_ID + " = " + streamId + "  WHERE " + COLUMN_NAME + " = " + "'" + channelName + "'";
                db.execSQL(sql);
            }
        }).start();
    }

    public Channel getLastPlay() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String selection = COLUMN_LAST_PLAY + " = 1 ";
        Cursor cursor = db.query(TABLE_NAME, ALL_COLUMNS, selection, null, null, null,
                COLUMN_NO + " ASC", null);
        if (0 == cursor.getCount()) {
            return null;
        }

        List<Channel> resultList = buildResult(cursor);
        if (null == resultList || resultList.size() < 1) {
            return null;
        }
        cursor.close();
        return resultList.get(0);
    }


    public void initChannel() {
        clear();
//        this.insert(new Channel("1", "央视一套", "http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_16x9/gear5/prog_index.m3u8", "央视频道"));
//        this.insert(new Channel("2", "央视二套", "http://223.110.241.204:6610/gitv/live1/G_CCTV-2-HD/G_CCTV-2-HD/", "央视频道"));
//        this.insert(new Channel("3", "央视三套", "http://223.110.241.204:6610/gitv/live1/G_CCTV-3-HQ/G_CCTV-3-HQ/", "央视频道"));
//        this.insert(new Channel("4", "央视四套", "http://223.110.241.204:6610/gitv/live1/G_CCTV-4-HQ/G_CCTV-4-HQ/", "央视频道"));
//        this.insert(new Channel("5", "浙江卫视", "http://111.11.121.132:6610/000000001000/reallive-cctv1/1.m3u8?channel-id=ystenlive&IASHttpSessionId=SLB17352201802131915434727871", "卫视频道"));
//        this.insert(new Channel("6", "湖南卫视", "http://111.11.121.132:6610/000000001000/reallive-cctv1/1.m3u8?channel-id=ystenlive&IASHttpSessionId=SLB17352201802131915434727871", "卫视频道"));

    }


}
