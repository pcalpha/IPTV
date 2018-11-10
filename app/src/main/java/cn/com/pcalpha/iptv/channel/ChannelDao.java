package cn.com.pcalpha.iptv.channel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by caiyida on 2018/6/24.
 */
public class ChannelDao extends SQLiteOpenHelper {
    private static final String DB_NAME = "Channel.db";
    private static final String TABLE_NAME = "CHANNEL";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_NO = "NO";
    private static final String COLUMN_NAME = "NAME";
    private static final String COLUMN_CATEGORY_NAME = "CATEGORY_NAME";
    private static final String COLUMN_PLAY_TIME = "PLAY_TIME";
    private static final String COLUMNS[] = new String[]{
            COLUMN_ID,
            COLUMN_NO,
            COLUMN_NAME,
            COLUMN_CATEGORY_NAME,
            COLUMN_PLAY_TIME
    };

    private ChannelDao(Context context) {
        super(context, DB_NAME, null, 20);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createChannel(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        recreateChannel(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        recreateChannel(db);
    }

    private void createChannel(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + ChannelDao.TABLE_NAME
                + " ("
                + "'" + ChannelDao.COLUMN_ID + "' INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "'" + ChannelDao.COLUMN_NO + "' VARCHAR, "
                + "'" + ChannelDao.COLUMN_NAME + "' VARCHAR, "
                + "'" + ChannelDao.COLUMN_CATEGORY_NAME + "' VARCHAR, "
                + "'" + ChannelDao.COLUMN_PLAY_TIME + "' INTEGER "
                + ") ");
    }

    private void recreateChannel(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + ChannelDao.TABLE_NAME);
        createChannel(db);
    }

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

    public void insert(Channel channel) {
        List<Channel> channelList = Arrays.asList(channel);
        new InsertChannelTask(channelList).execute(new Void[0]);
    }

    public void insert(List<Channel> channelList) {
        new InsertChannelTask(channelList).execute(new Void[0]);
    }

    public void update(Channel channel) {
        List<Channel> channelList = Arrays.asList(channel);
        new UpdateChannelTask(channelList).execute(new Void[0]);
    }

    public void update(List<Channel> channelList) {
        new UpdateChannelTask(channelList).execute(new Void[0]);
    }

    public void delete(Integer id) {
        SQLiteDatabase db = getWritableDatabase();
        String[] whereArgs = new String[]{String.valueOf(id)};
        db.delete(TABLE_NAME, COLUMN_ID + "=?", whereArgs);
    }

    public void delete(String name) {
        SQLiteDatabase db = getWritableDatabase();
        String[] whereArgs = new String[]{String.valueOf(name)};
        db.delete(TABLE_NAME, COLUMN_NAME + "=?", whereArgs);
    }

    public void clear() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }

    public Channel get(String channelName) {
        SQLiteDatabase db = getReadableDatabase();
        String selection = COLUMN_NAME + " = ? ";
        String[] selectionArgs = new String[]{channelName};
        Cursor cursor = db.query(
                TABLE_NAME,
                COLUMNS,
                selection,
                selectionArgs,
                null,
                null,
                COLUMN_NO + " ASC",
                null);
        if (0 == cursor.getCount()) {
            return null;
        }

        List<Channel> resultList = buildResult(cursor);
        if (null == resultList || resultList.size() <= 0) {
            return null;
        }
        return resultList.get(0);
    }

    public Channel get(Integer id) {
        SQLiteDatabase db = getReadableDatabase();
        String selection = COLUMN_ID + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(id)};
        Cursor cursor = db.query(
                TABLE_NAME,
                COLUMNS,
                selection,
                selectionArgs,
                null,
                null,
                COLUMN_NO + " ASC",
                null);
        if (0 == cursor.getCount()) {
            return null;
        }

        List<Channel> resultList = buildResult(cursor);
        if (null == resultList || resultList.size() <= 0) {
            return null;
        }
        return resultList.get(0);
    }

    //private List<Channel> channelList;//缓存列表

    public List<Channel> find(Param4Channel param) {
        SQLiteDatabase db = getReadableDatabase();
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

        Cursor cursor = db.query(
                TABLE_NAME,
                COLUMNS,
                selection,
                selectionArgs,
                null,
                null,
                COLUMN_NO + " ASC",
                null);
        List<Channel> result = buildResult(cursor);
        //channelList = result;
        return result;
    }

    private List<Channel> buildResult(Cursor cursor) {
        if (0 == cursor.getCount()) {
            return new ArrayList<>();
        }

        Integer indexId = cursor.getColumnIndex(COLUMN_ID);
        Integer indexNo = cursor.getColumnIndex(COLUMN_NO);
        Integer indexName = cursor.getColumnIndex(COLUMN_NAME);
        Integer indexPlayTime = cursor.getColumnIndex(COLUMN_PLAY_TIME);
        Integer indexCategoryName = cursor.getColumnIndex(COLUMN_CATEGORY_NAME);
        List<Channel> channelList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Channel channel = new Channel();
            channel.setId(cursor.getInt(indexId));
            channel.setNo(cursor.getString(indexNo));
            channel.setName(cursor.getString(indexName));
            channel.setPlayTime(new Date(cursor.getInt(indexPlayTime)));
            channel.setCategoryName(cursor.getString(indexCategoryName));

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

    public void setLastPlay(Channel channel) {
        new UpdatePlayTimeTask(channel).execute(new Void[0]);
    }

    public Channel getLastPlay() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME,
                COLUMNS,
                null,
                null,
                null,
                null,
                COLUMN_PLAY_TIME + " DESC",
                null);
        if (0 == cursor.getCount()) {
            return null;
        }

        List<Channel> resultList = buildResult(cursor);
        if (null == resultList || resultList.size() <= 0) {
            return null;
        }
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

    class InsertChannelTask extends AsyncTask<Void, Void, Void> {
        List<Channel> mChannelList;

        public InsertChannelTask(List<Channel> channelList) {
            this.mChannelList = channelList;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SQLiteDatabase db = getWritableDatabase();
            db.beginTransaction();
            Date date = new Date();
            for (Channel channel : mChannelList) {
                ContentValues cv = new ContentValues();
                cv.put(COLUMN_ID, channel.getId());
                cv.put(COLUMN_NO, channel.getNo());
                cv.put(COLUMN_NAME, channel.getName());
                cv.put(COLUMN_PLAY_TIME, date.getTime());
                cv.put(COLUMN_CATEGORY_NAME, channel.getCategoryName());
                db.insert(TABLE_NAME, null, cv);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            return null;
        }
    }

    class UpdateChannelTask extends AsyncTask<Void, Void, Void> {
        private List<Channel> mChannelList;

        public UpdateChannelTask(List<Channel> mChannelList) {
            this.mChannelList = mChannelList;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SQLiteDatabase db = getWritableDatabase();
            db.beginTransaction();
            for (Channel channel : mChannelList) {
                ContentValues cv = new ContentValues();
                cv.put(COLUMN_ID, channel.getId());
                cv.put(COLUMN_NO, channel.getNo());
                cv.put(COLUMN_NAME, channel.getName());
                cv.put(COLUMN_PLAY_TIME, channel.getPlayTime().getTime());
                cv.put(COLUMN_CATEGORY_NAME, channel.getCategoryName());

                String[] whereArgs = new String[]{
                        channel.getId().toString()
                };
                db.update(TABLE_NAME, cv, COLUMN_ID + "=?", whereArgs);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            return null;
        }
    }

    class UpdatePlayTimeTask extends AsyncTask<Void, Void, Void> {
        private Channel mChannel;

        public UpdatePlayTimeTask(Channel mChannel) {
            this.mChannel = mChannel;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            final SQLiteDatabase db = getWritableDatabase();
            db.beginTransaction();
            Date date = new Date();
            String sql = "UPDATE " + TABLE_NAME
                    + " SET " + COLUMN_PLAY_TIME + " =  " + date.getTime()
                    + " WHERE " + COLUMN_NAME + " = " + "'" + mChannel.getName() + "'";
            db.execSQL(sql);
            db.setTransactionSuccessful();
            db.endTransaction();
            return null;
        }
    }

}
