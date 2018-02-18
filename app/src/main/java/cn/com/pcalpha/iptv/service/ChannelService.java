/*
 * Copyright (C) 2015 Bilibili
 * Copyright (C) 2015 Zhang Rui <bbcallen@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.com.pcalpha.iptv.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import cn.com.pcalpha.iptv.model.Category;
import cn.com.pcalpha.iptv.model.Channel;

public class ChannelService extends BaseService {
    private Context context;
    public static final String TABLE_NAME = "Channel";

    public static final String COLUMN_NO = "no";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_ICON_URL = "icon_url";
    public static final String COLUMN_SRC = "src";
    public static final String COLUMN_SRC_INDEX = "src_index";
    public static final String COLUMN_LAST_ACCESS = "last_access";

    public static final String ALL_COLUMNS[] = new String[]{
            COLUMN_NO,
            COLUMN_NAME,
            COLUMN_TYPE,
            COLUMN_ICON_URL,
            COLUMN_SRC,
            COLUMN_SRC_INDEX,
            COLUMN_LAST_ACCESS
    };

    private static final String SQL_CREATE_ENTRIES =
            " CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    COLUMN_NO + " INTEGER PRIMARY KEY, " +
                    COLUMN_NAME + " VARCHAR, " +
                    COLUMN_TYPE + " INTEGER, " +
                    COLUMN_ICON_URL + " VARCHAR, " +
                    COLUMN_SRC + " VARCHAR, " +
                    COLUMN_SRC_INDEX + " VARCHAR ," +
                    COLUMN_LAST_ACCESS+" INTEGER ) ";

    private static final String SQL_DROP_ENTIES = "DROP TABLE IF EXISTS "+ TABLE_NAME;

    public ChannelService(Context context) {
        super(context);
        this.context = context;
        //dropTable();
        //createTable();
        //initChannel();
        //query();
    }

    public void createTable(){
        writeDb.execSQL(SQL_CREATE_ENTRIES);
    }

    public void dropTable(){
        writeDb.execSQL(SQL_DROP_ENTIES);
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
        cv.put(COLUMN_NO,channel.getNo());
        cv.put(COLUMN_NAME, channel.getName());
        cv.put(COLUMN_TYPE, channel.getType());
        cv.put(COLUMN_ICON_URL, channel.getIconUrl());
        cv.put(COLUMN_SRC,channel.getSrc());
        cv.put(COLUMN_SRC_INDEX,channel.getSrcIndex());
        cv.put(COLUMN_LAST_ACCESS,channel.getLastAccess());
        insert(cv);
    }

    public void insert(ContentValues contentValue) {
        writeDb.insert(TABLE_NAME, null, contentValue);
    }

    public void delete(Integer channelNo){
        String[] whereArgs = new  String[]{ channelNo.toString() };
        writeDb.delete(TABLE_NAME,"no=?",whereArgs);
    }

    public void clear(){
        writeDb.delete(TABLE_NAME,null,null);
    }

    public void update(Channel channel){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NO, channel.getNo());
        cv.put(COLUMN_NAME, channel.getName());
        cv.put(COLUMN_TYPE, channel.getType());
        cv.put(COLUMN_ICON_URL, channel.getIconUrl());
        cv.put(COLUMN_SRC, channel.getSrc());
        cv.put(COLUMN_SRC_INDEX, channel.getSrcIndex());
        cv.put(COLUMN_LAST_ACCESS, channel.getLastAccess());

        String[] whereArgs = new  String[]{ channel.getNo().toString() };
        writeDb.update(TABLE_NAME,cv,"no=?",whereArgs);
    }

    public Channel get(Integer channelNo){
        String selection = "no=?";
        String[] selectionArgs = new  String[]{ channelNo.toString() };
        Cursor cursor = readDb.query(TABLE_NAME, ALL_COLUMNS, selection, selectionArgs, null, null,
                COLUMN_NO + " ASC",null);

        Integer mIndex_no = cursor.getColumnIndex(COLUMN_NO);
        Integer mIndex_name = cursor.getColumnIndex(COLUMN_NAME);
        Integer mIndex_type = cursor.getColumnIndex(COLUMN_TYPE);
        Integer mIndex_icon_url = cursor.getColumnIndex(COLUMN_ICON_URL);
        Integer mIndex_src = cursor.getColumnIndex(COLUMN_SRC);
        Integer mIndex_src_index = cursor.getColumnIndex(COLUMN_SRC_INDEX);
        Integer mIndex_last_access = cursor.getColumnIndex(COLUMN_LAST_ACCESS);

        if(0==cursor.getCount()){
            return null;
        }

        cursor.moveToNext();
        Channel channel = new Channel(
                cursor.getInt(mIndex_no),
                cursor.getString(mIndex_name),
                cursor.getInt(mIndex_type),
                cursor.getString(mIndex_icon_url),
                cursor.getString(mIndex_src),
                cursor.getInt(mIndex_src_index),
                cursor.getInt(mIndex_last_access)
        );
        return channel;

    }

    private List<Channel> channels;
    public List<Channel> query(){
        if(null!=channels){
            return channels;
        }
        Cursor cursor = readDb.query(TABLE_NAME, ALL_COLUMNS, null, null, null, null,
                COLUMN_NO + " ASC",null);

        Integer mIndex_no = cursor.getColumnIndex(COLUMN_NO);
        Integer mIndex_name = cursor.getColumnIndex(COLUMN_NAME);
        Integer mIndex_type = cursor.getColumnIndex(COLUMN_TYPE);
        Integer mIndex_icon_url = cursor.getColumnIndex(COLUMN_ICON_URL);
        Integer mIndex_src = cursor.getColumnIndex(COLUMN_SRC);
        Integer mIndex_src_index = cursor.getColumnIndex(COLUMN_SRC_INDEX);
        Integer mIndex_last_access = cursor.getColumnIndex(COLUMN_LAST_ACCESS);

        List<Channel> channels = new ArrayList<>();
        int i = 0;
        while(cursor.moveToNext()){
            Channel channel = new Channel(
                    cursor.getInt(mIndex_no),
                    cursor.getString(mIndex_name),
                    cursor.getInt(mIndex_type),
                    cursor.getString(mIndex_icon_url),
                    cursor.getString(mIndex_src),
                    cursor.getInt(mIndex_src_index),
                    cursor.getInt(mIndex_last_access)
            );
            if(channel.getLastAccess()==1){
                lastAccessChannelIndex=i;
            }
            channels.add(channel);
            i++;
        }
        this.channels = channels;
        return channels;
    }

    public Cursor getCursor(){
        Cursor cursor = readDb.query(TABLE_NAME, ALL_COLUMNS, null, null, null, null,
                COLUMN_NO + " ASC",null);
        return cursor;
    }

    public void setLastAccess(Integer no){
        List<Channel> channels = query();

        if(null!=channels&&channels.size()>0){
            for(Channel channel : channels){
                channel.setLastAccess(0);
                update(channel);
            }
            Channel channel = get(no);
            channel.setLastAccess(1);
            update(channel);
        }
    }

    private Integer lastAccessChannelIndex;
    public Channel getLastAccess(){
        String selection = "last_access=?";
        String[] selectionArgs = new  String[]{ "1" };
        Cursor cursor = readDb.query(TABLE_NAME, ALL_COLUMNS, selection, selectionArgs, null, null,
                COLUMN_NO + " ASC",null);

        Integer mIndex_no = cursor.getColumnIndex(COLUMN_NO);
        Integer mIndex_name = cursor.getColumnIndex(COLUMN_NAME);
        Integer mIndex_type = cursor.getColumnIndex(COLUMN_TYPE);
        Integer mIndex_icon_url = cursor.getColumnIndex(COLUMN_ICON_URL);
        Integer mIndex_src = cursor.getColumnIndex(COLUMN_SRC);
        Integer mIndex_src_index = cursor.getColumnIndex(COLUMN_SRC_INDEX);
        Integer mIndex_last_access = cursor.getColumnIndex(COLUMN_LAST_ACCESS);

        if(cursor.getCount()==0){
            return null;
        }

        cursor.moveToNext();
        Channel channel = new Channel(
                cursor.getInt(mIndex_no),
                cursor.getString(mIndex_name),
                cursor.getInt(mIndex_type),
                cursor.getString(mIndex_icon_url),
                cursor.getString(mIndex_src),
                cursor.getInt(mIndex_src_index),
                cursor.getInt(mIndex_last_access)
        );
        return channel;
    }

    public Channel next(){
        List<Channel> channels = query();
        lastAccessChannelIndex+=1;
        if(lastAccessChannelIndex>=channels.size()){
            lastAccessChannelIndex=0;
        }
        Channel channel = channels.get(lastAccessChannelIndex);

        return channel;
    }

    public Channel pre(){
        List<Channel> channels = query();
        lastAccessChannelIndex-=1;
        if(lastAccessChannelIndex<0){
            lastAccessChannelIndex=channels.size()-1;
        }
        Channel channel = channels.get(lastAccessChannelIndex);

        return channel;
    }

    public void nextSrc(Channel channel) {
        String[] srcArr = channel.getSrc().split(",");
        Integer srcIndex = channel.getSrcIndex();
        if(srcIndex!=null&&srcIndex+1<srcArr.length){
            srcIndex+=1;
        }
        channel.setSrcIndex(srcIndex);
        this.update(channel);
    }

    public void preSrc(Channel channel) {
        String[] srcArr = channel.getSrc().split(",");
        Integer srcIndex = channel.getSrcIndex();
        if(srcIndex!=null&&srcIndex-1>-1){
            srcIndex-=1;
        }
        channel.setSrcIndex(srcIndex);
        this.update(channel);
    }

    public void initChannel() {
        clear();
        this.insert(new Channel(1,"央视一套", Category.CCTV,"","http://111.11.121.132:6610/000000001000/reallive-cctv1/1.m3u8?channel-id=ystenlive&IASHttpSessionId=SLB17352201802131915434727871",0,1));
        this.insert(new Channel(2,"央视二套", Category.CCTV,"","http://183.252.176.20//PLTV/88888888/224/3221225924/index.m3u8",0,0));
        this.insert(new Channel(3,"央视三套", Category.CCTV,"","http://223.110.243.136/PLTV/3/224/3221227206/index.m3u8",0,0));
        this.insert(new Channel(4,"央视四套",Category.CCTV,"","http://111.44.138.251:16416/contents40/live/CHANNEL4ff3d770b713424e9dafa96da0390915/HD_2013552811240083238/live.m3u8",0,0));
        this.insert(new Channel(5,"央视五套", Category.CCTV,"","http://111.12.130.67/PLTV/88888888/224/3221225607/index.m3u8",0,0));

        this.insert(new Channel(33,"湖南卫视", Category.STV,"","http://111.12.130.67/PLTV/88888888/224/3221225652/index.m3u8",0,0));
        this.insert(new Channel(34,"江苏卫视", Category.STV,"","http://183.252.176.35//PLTV/88888888/224/3221225930/index.m3u8",0,0));
        this.insert(new Channel(35,"浙江卫视", Category.STV,"","http://183.252.176.35//PLTV/88888888/224/3221225934/index.m3u8",0,0));
    }


}
