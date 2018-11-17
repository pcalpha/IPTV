package cn.com.pcalpha.iptv.channel.stream;

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
 * Created by caiyida on 2018/8/8.
 */

public class ChannelStreamDao extends SQLiteOpenHelper {
    private static final String DB_NAME = "ChannelStream.db";
    private static final String TABLE_NAME = "CHANNEL_STREAM";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_NAME = "NAME";
    private static final String COLUMN_URL = "URL";
    private static final String COLUMN_CARRIER = "CARRIER";
    private static final String COLUMN_CHANNEL_NAME = "CHANNEL_NAME";
    private static final String COLUMN_PLAY_TIME = "PLAY_TIME";
    private static final String COLUMNS[] = new String[]{
            COLUMN_ID,
            COLUMN_NAME,
            COLUMN_URL,
            COLUMN_CARRIER,
            COLUMN_CHANNEL_NAME,
            COLUMN_PLAY_TIME
    };

    private ChannelStreamDao(Context context) {
        super(context, DB_NAME, null, 20);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createChannelStream(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        recreateChannelStream(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        recreateChannelStream(db);
    }

    private void createChannelStream(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + ChannelStreamDao.TABLE_NAME
                + " ("
                + "'" + ChannelStreamDao.COLUMN_ID + "' INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "'" + ChannelStreamDao.COLUMN_NAME + "' VARCHAR, "
                + "'" + ChannelStreamDao.COLUMN_URL + "' VARCHAR, "
                + "'" + ChannelStreamDao.COLUMN_CARRIER + "' VARCHAR, "
                + "'" + ChannelStreamDao.COLUMN_CHANNEL_NAME + "' VARCHAR, "
                + "'" + ChannelStreamDao.COLUMN_PLAY_TIME + "' INTEGER "
                + ")");
    }

    private void recreateChannelStream(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + ChannelStreamDao.TABLE_NAME);
        createChannelStream(db);
    }

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

    public void insert(ChannelStream channelStream) {
        List<ChannelStream> channelStreamList = Arrays.asList(channelStream);
        new InsertChannelStreamTask(channelStreamList).execute(new Void[0]);
    }

    public void insert(List<ChannelStream> channelStreamList) {
        new InsertChannelStreamTask(channelStreamList).execute(new Void[0]);
    }


    public void update(ChannelStream channelStream) {
        List<ChannelStream> channelStreamList = Arrays.asList(channelStream);
        new UpdateChannelStreamTask(channelStreamList).execute(new Void[0]);
    }

    public void update(List<ChannelStream> channelStreamList) {
        new UpdateChannelStreamTask(channelStreamList).execute(new Void[0]);
    }

    public void delete(Integer id) {
        SQLiteDatabase db = getWritableDatabase();
        String[] whereArgs = new String[]{id.toString()};
        db.delete(TABLE_NAME, COLUMN_ID + "=?", whereArgs);
    }

    public void delete(String name) {
        SQLiteDatabase db = getWritableDatabase();
        String[] whereArgs = new String[]{name};
        db.delete(TABLE_NAME, COLUMN_NAME + "=?", whereArgs);
    }

    public void clear() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }


    public ChannelStream get(Integer id) {
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
                COLUMN_ID + " ASC",
                null);
        if (0 == cursor.getCount()) {
            return null;
        }

        List<ChannelStream> resultList = buildResult(cursor);
        if (null == resultList || resultList.size() <= 0) {
            return null;
        }
        return resultList.get(0);
    }


    public List<ChannelStream> find(Param4ChannelStream param) {
        SQLiteDatabase db = getReadableDatabase();
        String selection = "";
        List<String> args = new ArrayList<>();
        if (null != param.getChannelName() && !"".equals(param.getChannelName())) {
            if (null != selection && selection.length() > 0) {
                selection += " AND ";
            }
            selection += COLUMN_CHANNEL_NAME + " = ? ";
            args.add(param.getChannelName());
        }
        if (null != param.getCarrier() && !"".equals(param.getCarrier())) {
            if (null != selection && selection.length() > 0) {
                selection += " AND ";
            }
            selection += COLUMN_CARRIER + " = ? ";
            args.add(param.getCarrier());
        }

        String[] selectionArgs = args.toArray(new String[args.size()]);

        Cursor cursor = db.query(
                TABLE_NAME,
                COLUMNS,
                selection,
                selectionArgs,
                null,
                null,
                COLUMN_ID + " ASC",
                null);
        return buildResult(cursor);
    }

    private List<ChannelStream> buildResult(Cursor cursor) {
        if (0 == cursor.getCount()) {
            return new ArrayList<>();
        }

        Integer indexId = cursor.getColumnIndex(COLUMN_ID);
        Integer indexName = cursor.getColumnIndex(COLUMN_NAME);
        Integer indexUrl = cursor.getColumnIndex(COLUMN_URL);
        Integer indexCarrier = cursor.getColumnIndex(COLUMN_CARRIER);
        Integer indexLastPlay = cursor.getColumnIndex(COLUMN_PLAY_TIME);
        Integer indexChannelName = cursor.getColumnIndex(COLUMN_CHANNEL_NAME);

        List<ChannelStream> channelStreamList = new ArrayList<>();
        while (cursor.moveToNext()) {
            ChannelStream channelStream = new ChannelStream();
            channelStream.setId(cursor.getInt(indexId));
            channelStream.setName(cursor.getString(indexName));
            channelStream.setUrl(cursor.getString(indexUrl));
            channelStream.setCarrier(cursor.getString(indexCarrier));
            channelStream.setPlayTime(new Date(cursor.getInt(indexLastPlay)));
            channelStream.setChannelName(cursor.getString(indexChannelName));

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

    public void setLastPlay(ChannelStream channelStream) {
        new UpdatePlayTimeTask(channelStream).execute(new Void[0]);
    }

    public ChannelStream getLastPlay(String channelName, String carrier) {
        SQLiteDatabase db = getReadableDatabase();
        String selection = COLUMN_CHANNEL_NAME + " = ? "
                    +" AND "+COLUMN_CARRIER + " = ? ";
        String[] selectionArgs = new String[]{channelName,carrier};
        Cursor cursor = db.query(
                TABLE_NAME, COLUMNS,
                selection,
                selectionArgs,
                null,
                null,
                COLUMN_PLAY_TIME + " DESC",
                null);
        List<ChannelStream> resultList = buildResult(cursor);

        if (null == resultList || resultList.size() <= 0) {
            return null;
        }
        return resultList.get(0);
    }

    class InsertChannelStreamTask extends AsyncTask<Void, Void, Void> {
        private List<ChannelStream> mChannelStreamList;

        public InsertChannelStreamTask(List<ChannelStream> channelStreamList) {
            this.mChannelStreamList = channelStreamList;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SQLiteDatabase db = getWritableDatabase();
            db.beginTransaction();
            Date date = new Date();
            for (ChannelStream channelStream : mChannelStreamList) {
                ContentValues cv = new ContentValues();
                cv.put(COLUMN_ID, channelStream.getId());
                cv.put(COLUMN_NAME, channelStream.getName());
                cv.put(COLUMN_URL, channelStream.getUrl());
                cv.put(COLUMN_CARRIER, channelStream.getCarrier());
                cv.put(COLUMN_PLAY_TIME, date.getTime());
                cv.put(COLUMN_CHANNEL_NAME, channelStream.getChannelName());

                db.insert(TABLE_NAME, null, cv);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            return null;
        }
    }

    class UpdateChannelStreamTask extends AsyncTask<Void, Void, Void> {
        private List<ChannelStream> mChannelStreamList;

        public UpdateChannelStreamTask(List<ChannelStream> channelStreamList) {
            this.mChannelStreamList = channelStreamList;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SQLiteDatabase db = getWritableDatabase();
            db.beginTransaction();
            for (ChannelStream channelStream : mChannelStreamList) {
                ContentValues cv = new ContentValues();
                cv.put(COLUMN_ID, channelStream.getId());
                cv.put(COLUMN_NAME, channelStream.getName());
                cv.put(COLUMN_URL, channelStream.getUrl());
                cv.put(COLUMN_CARRIER, channelStream.getCarrier());
                cv.put(COLUMN_PLAY_TIME, channelStream.getPlayTime().getTime());
                cv.put(COLUMN_CHANNEL_NAME, channelStream.getChannelName());
                String[] whereArgs = new String[]{
                        String.valueOf(channelStream.getId())
                };
                db.update(TABLE_NAME, cv, COLUMN_ID + "=?", whereArgs);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            return null;
        }
    }

    class UpdatePlayTimeTask extends AsyncTask<Void, Void, Void> {
        private ChannelStream mChannelStream;

        public UpdatePlayTimeTask(ChannelStream channelStream) {
            this.mChannelStream = channelStream;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            final SQLiteDatabase db = getWritableDatabase();
            Date date = new Date();
            db.beginTransaction();
            String sql = "UPDATE " + TABLE_NAME
                    + " SET " + COLUMN_PLAY_TIME + " =  " + date.getTime()
                    + " WHERE " + COLUMN_ID + " = " + "'" + mChannelStream.getId() + "'";
            db.execSQL(sql);
            db.setTransactionSuccessful();
            db.endTransaction();
            return null;
        }
    }


}
