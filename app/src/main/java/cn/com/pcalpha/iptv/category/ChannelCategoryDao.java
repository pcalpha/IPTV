package cn.com.pcalpha.iptv.category;

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

public class ChannelCategoryDao extends SQLiteOpenHelper {
    private static final String DB_NAME = "ChannelCategory.db";
    private static final String TABLE_NAME = "CHANNEL_CATEGORY";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_NAME = "NAME";
    private static final String COLUMN_PLAY_TIME = "PLAY_TIME";
    private static final String COLUMNS[] = new String[]{
            COLUMN_ID,
            COLUMN_NAME,
            COLUMN_PLAY_TIME
    };

    private ChannelCategoryDao(Context context) {
        super(context, DB_NAME, null, 20);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createChannelCategory(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        recreateChannelCategory(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        recreateChannelCategory(db);
    }

    private void createChannelCategory(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + ChannelCategoryDao.TABLE_NAME
                + " ("
                + "'" + ChannelCategoryDao.COLUMN_ID + "' INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "'" + ChannelCategoryDao.COLUMN_NAME + "' VARCHAR, "
                + "'" + ChannelCategoryDao.COLUMN_PLAY_TIME + "' INTEGER "
                + ")");
    }

    private void recreateChannelCategory(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + ChannelCategoryDao.TABLE_NAME);
        createChannelCategory(db);
    }


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

    public void insert(ChannelCategory channelCategory) {
        List<ChannelCategory> channelCategoryList = Arrays.asList(channelCategory);
        new InsertChannelCategoryTask(channelCategoryList).execute(new Void[0]);
    }

    public void insert(List<ChannelCategory> channelCategoryList) {
        new InsertChannelCategoryTask(channelCategoryList).execute(new Void[0]);
    }

    public void update(ChannelCategory channelCategory) {
        List<ChannelCategory> channelCategoryList = Arrays.asList(channelCategory);
        new UpdateChannelCategoryTask(channelCategoryList).execute(new Void[0]);
    }

    public void update(List<ChannelCategory> channelCategoryList) {
        new UpdateChannelCategoryTask(channelCategoryList).execute(new Void[0]);
    }

    public void delete(Integer id) {
        SQLiteDatabase db = getWritableDatabase();
        String[] whereArgs = new String[]{String.valueOf(id)};
        db.delete(TABLE_NAME, "id=?", whereArgs);
    }

    public void clear() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }

    public ChannelCategory get(Integer id) {
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

        List<ChannelCategory> resultList = buildResult(cursor);
        if (null == resultList || resultList.size() <= 0) {
            return null;
        }
        cursor.close();
        return resultList.get(0);
    }

    public List<ChannelCategory> findAll() {
        SQLiteDatabase db = getReadableDatabase();
        //init();
        Cursor cursor = db.query(
                TABLE_NAME,
                COLUMNS,
                null,
                null,
                null,
                null,
                COLUMN_ID + " ASC",
                null);
        List<ChannelCategory> result = buildResult(cursor);
        return result;
    }

    private List<ChannelCategory> buildResult(Cursor cursor) {
        if (0 == cursor.getCount()) {
            return new ArrayList<>();
        }

        Integer indexId = cursor.getColumnIndex(COLUMN_ID);
        Integer indexName = cursor.getColumnIndex(COLUMN_NAME);
        Integer indexLastPlay = cursor.getColumnIndex(COLUMN_PLAY_TIME);

        List<ChannelCategory> channelCategoryList = new ArrayList<>();
        while (cursor.moveToNext()) {
            ChannelCategory channelCategory = new ChannelCategory();

            channelCategory.setId(cursor.getInt(indexId));
            channelCategory.setName(cursor.getString(indexName));
            channelCategory.setPlayTime(new Date(cursor.getInt(indexLastPlay)));
            channelCategoryList.add(channelCategory);
        }
        cursor.close();
        return channelCategoryList;
    }

    public void setLastPlay(String channelCategoryName) {
        new UpdatePlayTimeTask(channelCategoryName).execute(new Void[0]);
    }

    public ChannelCategory getLastPlay() {
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

        List<ChannelCategory> resultList = buildResult(cursor);
        if (null == resultList || resultList.size() <= 0) {
            return null;
        }
        return resultList.get(0);
    }

    public void init() {
        clear();
        this.insert(new ChannelCategory(1, "央视频道"));
        this.insert(new ChannelCategory(2, "卫视频道"));
    }


    class InsertChannelCategoryTask extends AsyncTask<Void, Void, Void> {
        List<ChannelCategory> mChannelCategoryList;

        public InsertChannelCategoryTask(List<ChannelCategory> mChannelCategoryList) {
            this.mChannelCategoryList = mChannelCategoryList;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SQLiteDatabase db = getWritableDatabase();
            db.beginTransaction();
            Date date = new Date();
            for (ChannelCategory mChannelCategory : mChannelCategoryList) {
                ContentValues values = new ContentValues();
                values.put(COLUMN_ID, mChannelCategory.getId());
                values.put(COLUMN_NAME, mChannelCategory.getName());
                values.put(COLUMN_PLAY_TIME, date.getTime());
                db.insert(TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            return null;
        }
    }

    class UpdateChannelCategoryTask extends AsyncTask<Void, Void, Void> {
        List<ChannelCategory> mChannelCategoryList;

        public UpdateChannelCategoryTask(List<ChannelCategory> mChannelCategoryList) {
            this.mChannelCategoryList = mChannelCategoryList;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SQLiteDatabase db = getWritableDatabase();
            db.beginTransaction();
            for (ChannelCategory mChannelCategory : mChannelCategoryList) {
                ContentValues cv = new ContentValues();
                cv.put(COLUMN_ID, mChannelCategory.getId());
                cv.put(COLUMN_NAME, mChannelCategory.getName());
                cv.put(COLUMN_PLAY_TIME, mChannelCategory.getPlayTime().getTime());
                String[] whereArgs = new String[]{
                        mChannelCategory.getId().toString()
                };
                db.update(TABLE_NAME, cv, "id=?", whereArgs);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            return null;
        }
    }

    class UpdatePlayTimeTask extends AsyncTask<Void, Void, Void> {
        private String channelCategoryName;

        public UpdatePlayTimeTask(String channelCategoryName) {
            this.channelCategoryName = channelCategoryName;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            final SQLiteDatabase db = getWritableDatabase();
            Date date = new Date();
            db.beginTransaction();
            String sql = "UPDATE " + TABLE_NAME
                    + " SET " + COLUMN_PLAY_TIME + " =  "+ date.getTime()
                    + " WHERE " + COLUMN_NAME + " = " + "'" + channelCategoryName + "'";
            db.execSQL(sql);
            db.setTransactionSuccessful();
            db.endTransaction();
            return null;
        }
    }
}
