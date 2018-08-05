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
import cn.com.pcalpha.iptv.model.domain.Channel;

/**
 * Created by caiyida on 2018/6/24.
 */

public class ChannelDao extends BaseDao {
    private Context context;

    public static final String TABLE_NAME = "CHANNEL";

    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_NO = "NO";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_SRC = "SRC";
    public static final String COLUMN_CATEGORY_NAME = "CATEGORY_NAME";
    public static final String COLUMN_LAST_PLAY = "LAST_PLAY";

    public static final String ALL_COLUMNS[] = new String[]{
            COLUMN_ID,
            COLUMN_NO,
            COLUMN_NAME,
            COLUMN_SRC,
            COLUMN_CATEGORY_NAME,
            COLUMN_LAST_PLAY
    };

    public static final String SQL_CREATE =
            " CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_NO + " VARCHAR, " +
                    COLUMN_NAME + " VARCHAR, " +
                    COLUMN_SRC + " VARCHAR, " +
                    COLUMN_CATEGORY_NAME + " VARCHAR, " +
                    COLUMN_LAST_PLAY + " INTEGER " +
                    ") ";

    public static final String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public ChannelDao(Context context) {
        super(context);
    }

    public void insertAsync(final Channel channel) {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... params) {
                insert(channel);
                return null;
            }
        }.execute();
    }

    public void insert(Channel channel) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, channel.getId());
        cv.put(COLUMN_NO, channel.getNo());
        cv.put(COLUMN_NAME, channel.getName());
        cv.put(COLUMN_SRC, channel.getSrc());
        cv.put(COLUMN_LAST_PLAY, channel.getLastPlay());
        cv.put(COLUMN_CATEGORY_NAME, channel.getCategoryName());

        writeDb.insert(TABLE_NAME, null, cv);
    }

    public void delete(Integer id) {
        String[] whereArgs = new String[]{id.toString()};
        writeDb.delete(TABLE_NAME, COLUMN_ID + "=?", whereArgs);
    }

    public void delete(String no) {
        String[] whereArgs = new String[]{no};
        writeDb.delete(TABLE_NAME, COLUMN_NO + "=?", whereArgs);
    }

    public void clear() {
        writeDb.delete(TABLE_NAME, null, null);
    }

    public void update(Channel channel) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, channel.getId());
        cv.put(COLUMN_NO, channel.getNo());
        cv.put(COLUMN_NAME, channel.getName());
        cv.put(COLUMN_SRC, channel.getSrc());
        cv.put(COLUMN_LAST_PLAY, channel.getLastPlay());
        cv.put(COLUMN_CATEGORY_NAME, channel.getCategoryName());

        String[] whereArgs = new String[]{channel.getId().toString()};
        writeDb.update(TABLE_NAME, cv, COLUMN_ID + "=?", whereArgs);
    }

    public Channel get(String channelNo) {
        Channel channel = channelNoMap.get(channelNo);
        if (null != channel) {
            return channel;
        }
        return null;
    }

    public Channel get(Integer id) {
        Channel channel = channelIdMap.get(id);
        if (null != channel) {
            return channel;
        }
        return null;
    }

    private List<Channel> channelList;//缓存列表
    private Map<Integer, Channel> channelIdMap;//缓存Map <频道号.频道>
    private Map<String, Channel> channelNoMap;

    private Cursor buildQuery(Param4Channel param) {
        String selection = "";
        List<String> args = new ArrayList<>();
        if (null != param.getCategoryName() && !"".equals(param.getCategoryName())) {
            selection = COLUMN_CATEGORY_NAME + " = ?";
            args.add(param.getCategoryName());
        }

        String[] selectionArgs = args.toArray(new String[args.size()]);

        Cursor cursor = readDb.query(TABLE_NAME, ALL_COLUMNS, selection, selectionArgs, null, null,
                COLUMN_NO + " ASC", null);
        return cursor;
    }

    public List<Channel> find(Param4Channel param) {
        //initChannel();
        if (null != channelList) {
            return channelList;
        }

        Cursor cursor = buildQuery(param);

        if (0 == cursor.getCount()) {
            return new ArrayList<>();
        }

        Integer mIndex_id = cursor.getColumnIndex(COLUMN_ID);
        Integer mIndex_no = cursor.getColumnIndex(COLUMN_NO);
        Integer mIndex_name = cursor.getColumnIndex(COLUMN_NAME);
        Integer mIndex_src = cursor.getColumnIndex(COLUMN_SRC);
        Integer mIndex_last_play = cursor.getColumnIndex(COLUMN_LAST_PLAY);
        Integer mIndex_category_name = cursor.getColumnIndex(COLUMN_CATEGORY_NAME);
        channelList = new ArrayList<>();
        channelIdMap = new HashMap<>();
        channelNoMap = new HashMap<>();
        while (cursor.moveToNext()) {
            Channel channel = new Channel();
            channel.setId(cursor.getInt(mIndex_id));
            channel.setNo(cursor.getString(mIndex_no));
            channel.setName(cursor.getString(mIndex_name));
            channel.setSrc(cursor.getString(mIndex_src));
            channel.setLastPlay(cursor.getInt(mIndex_last_play));
            channel.setCategoryName(cursor.getString(mIndex_category_name));

            channelList.add(channel);
            channelIdMap.put(channel.getId(), channel);
            channelNoMap.put(channel.getNo(), channel);
        }

        buildLinkedList(channelList);
        return channelList;
    }

    /**
     * 将list构建成链表
     *
     * @param channelList
     */
    private void buildLinkedList(List<Channel> channelList) {
        for (int i = 0; i < channelList.size(); i++) {
            Channel cur = channelList.get(i);
            //第一个元素没有前驱
            if (0 == i) {
                cur.setPre(null);
            } else {
                cur.setPre(channelList.get(i - 1));
            }
            //最后一个元素没有后继
            if (i == channelList.size() - 1) {
                cur.setNext(null);
            } else {
                cur.setNext(channelList.get(i + 1));
            }
        }
    }

    public void setLastPlay(String channelNo) {
        String sql = "UPDATE " + TABLE_NAME + " SET " + COLUMN_LAST_PLAY + " = 0 ";
        readDb.execSQL(sql);
        String sql2 = "UPDATE " + TABLE_NAME + " SET " + COLUMN_LAST_PLAY + " = 1 WHERE " + COLUMN_NO + " = " + channelNo;
        readDb.execSQL(sql2);
    }

    public Channel getLastPlay() {
        String selection = COLUMN_LAST_PLAY + " = 1 ";
        Cursor cursor = readDb.query(TABLE_NAME, ALL_COLUMNS, selection, null, null, null,
                COLUMN_NO + " ASC", null);
        if (0 == cursor.getCount()) {
            return null;
        }

        Integer mIndex_id = cursor.getColumnIndex(COLUMN_ID);
        Integer mIndex_no = cursor.getColumnIndex(COLUMN_NO);
        Integer mIndex_name = cursor.getColumnIndex(COLUMN_NAME);
        Integer mIndex_src = cursor.getColumnIndex(COLUMN_SRC);
        Integer mIndex_last_play = cursor.getColumnIndex(COLUMN_LAST_PLAY);
        Integer mIndex_category_name = cursor.getColumnIndex(COLUMN_CATEGORY_NAME);

        cursor.moveToNext();

        Channel channel = new Channel();
        channel.setId(cursor.getInt(mIndex_id));
        channel.setNo(cursor.getString(mIndex_no));
        channel.setName(cursor.getString(mIndex_name));
        channel.setSrc(cursor.getString(mIndex_src));
        channel.setLastPlay(cursor.getInt(mIndex_last_play));
        channel.setCategoryName(cursor.getString(mIndex_category_name));

        return channel;
    }


    public void initChannel() {
        clear();
        this.insert(new Channel(1, "1", "央视一套", "http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_16x9/gear5/prog_index.m3u8", "央视频道"));
        this.insert(new Channel(2, "2", "央视二套", "http://223.110.241.204:6610/gitv/live1/G_CCTV-2-HD/G_CCTV-2-HD/", "央视频道"));
        this.insert(new Channel(3, "3", "央视三套", "http://223.110.241.204:6610/gitv/live1/G_CCTV-3-HQ/G_CCTV-3-HQ/", "央视频道"));
        this.insert(new Channel(4, "4", "央视四套", "http://223.110.241.204:6610/gitv/live1/G_CCTV-4-HQ/G_CCTV-4-HQ/", "央视频道"));
        this.insert(new Channel(5, "5", "浙江卫视", "http://111.11.121.132:6610/000000001000/reallive-cctv1/1.m3u8?channel-id=ystenlive&IASHttpSessionId=SLB17352201802131915434727871", "卫视频道"));
        this.insert(new Channel(6, "6", "湖南卫视", "http://111.11.121.132:6610/000000001000/reallive-cctv1/1.m3u8?channel-id=ystenlive&IASHttpSessionId=SLB17352201802131915434727871", "卫视频道"));

    }
}
