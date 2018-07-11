package cn.com.pcalpha.iptv.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import cn.com.pcalpha.iptv.model.Channel;
import cn.com.pcalpha.iptv.model.ChannelCategory;

/**
 * Created by caiyida on 2018/6/24.
 */

public class ChannelDao extends BaseDao {
    private Context context;

    public static final String TABLE_NAME = "CHANNEL";

    public static final String COLUMN_NO = "NO";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_TYPE = "TYPE";
    public static final String COLUMN_ICON_URL = "ICON_URL";
    public static final String COLUMN_SRC = "SRC";
    public static final String COLUMN_SRC_INDEX = "SRC_INDEX";
    public static final String COLUMN_LAST_ACCESS = "LAST_ACCESS";

    public static final String ALL_COLUMNS[] = new String[]{
            COLUMN_NO,
            COLUMN_NAME,
            COLUMN_TYPE,
            COLUMN_ICON_URL,
            COLUMN_SRC,
            COLUMN_SRC_INDEX,
            COLUMN_LAST_ACCESS
    };

    public static final String SQL_CREATE =
            " CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    " (" +
                        COLUMN_NO + " INTEGER PRIMARY KEY, " +
                        COLUMN_NAME + " VARCHAR, " +
                        COLUMN_TYPE + " INTEGER, " +
                        COLUMN_ICON_URL + " VARCHAR, " +
                        COLUMN_SRC + " VARCHAR, " +
                        COLUMN_SRC_INDEX + " VARCHAR ," +
                        COLUMN_LAST_ACCESS+" INTEGER " +
                    ") ";

    private static final String SQL_DROP = "DROP TABLE IF EXISTS "+ TABLE_NAME;

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
        cv.put(COLUMN_NO,channel.getNo());
        cv.put(COLUMN_NAME, channel.getName());
        cv.put(COLUMN_TYPE, channel.getType());
        cv.put(COLUMN_ICON_URL, channel.getIconUrl());
        cv.put(COLUMN_SRC,channel.getSrc());
        cv.put(COLUMN_SRC_INDEX,channel.getSrcIndex());
        cv.put(COLUMN_LAST_ACCESS,channel.getLastAccess());
        writeDb.insert(TABLE_NAME, null, cv);
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

    List<Channel> channelList;//缓存频道列表
    public List<Channel> findAll(){
        if(null!=channelList){
            return channelList;
        }
        Cursor cursor = readDb.query(TABLE_NAME, ALL_COLUMNS, null, null, null, null,
                COLUMN_NO + " ASC",null);

        if(0==cursor.getCount()){
            return null;
        }

        Integer mIndex_no = cursor.getColumnIndex(COLUMN_NO);
        Integer mIndex_name = cursor.getColumnIndex(COLUMN_NAME);
        Integer mIndex_type = cursor.getColumnIndex(COLUMN_TYPE);
        Integer mIndex_icon_url = cursor.getColumnIndex(COLUMN_ICON_URL);
        Integer mIndex_src = cursor.getColumnIndex(COLUMN_SRC);
        Integer mIndex_src_index = cursor.getColumnIndex(COLUMN_SRC_INDEX);
        Integer mIndex_last_access = cursor.getColumnIndex(COLUMN_LAST_ACCESS);

        channelList = new ArrayList<>();
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

            channelList.add(channel);
        }

        buildLinkedList(channelList);
        return channelList;
    }

    /**
     * 将list构建成链表
     * @param channelList
     */
    private void buildLinkedList(List<Channel> channelList){
        for(int i=0;i<channelList.size();i++){
            Channel cur = channelList.get(i);
            //第一个元素没有前驱
            if(0==i){
                cur.setPre(channelList.get(channelList.size()-1));
            }else{
                cur.setPre(channelList.get(i-1));
            }
            //最后一个元素没有后继
            if(channelList.size()==i-1){
                cur.setNext(channelList.get(0));
            }else{
                cur.setNext(channelList.get(i+1));
            }
        }
    }

    public Channel get(Integer channelNo){
        List<Channel> channelList = findAll();
        if(null==channelList||0==channelList.size()){
            return null;
        }
        for(Channel c:channelList){
            if(c.getNo().equals(channelNo)){
                return c;
            }
        }
        return null;
    }

    public void setLastAccess(Integer channelNo){
        List<Channel> channels = findAll();

        if(null!=channels&&channels.size()>0){
            for(Channel channel : channels){
                channel.setLastAccess(0);
                update(channel);
            }
            Channel channel = get(channelNo);
            channel.setLastAccess(1);
            update(channel);
        }
    }

    public Channel getLastAccess(){
        List<Channel> channelList = findAll();
        if(null==channelList||0==channelList.size()){
            return null;
        }
        for(Channel c:channelList){
            if(1==c.getLastAccess()){
                return c;
            }
        }
        return channelList.get(0);
    }


    public void initChannel() {
        clear();
        this.insert(new Channel(1,"央视一套", ChannelCategory.CCTV,"","http://111.11.121.132:6610/000000001000/reallive-cctv1/1.m3u8?channel-id=ystenlive&IASHttpSessionId=SLB17352201802131915434727871",0,1));
        this.insert(new Channel(2,"央视二套", ChannelCategory.CCTV,"","http://183.252.176.20//PLTV/88888888/224/3221225924/index.m3u8",0,0));
        this.insert(new Channel(3,"央视三套", ChannelCategory.CCTV,"","http://223.110.243.136/PLTV/3/224/3221227206/index.m3u8",0,0));
        this.insert(new Channel(4,"央视四套", ChannelCategory.CCTV,"","http://111.44.138.251:16416/contents40/live/CHANNEL4ff3d770b713424e9dafa96da0390915/HD_2013552811240083238/live.m3u8",0,0));
        this.insert(new Channel(5,"央视五套", ChannelCategory.CCTV,"","http://111.12.130.67/PLTV/88888888/224/3221225607/index.m3u8",0,0));

        this.insert(new Channel(33,"湖南卫视", ChannelCategory.STV,"","http://111.12.130.67/PLTV/88888888/224/3221225652/index.m3u8",0,0));
        this.insert(new Channel(34,"江苏卫视", ChannelCategory.STV,"","http://183.252.176.35//PLTV/88888888/224/3221225930/index.m3u8",0,0));
        this.insert(new Channel(35,"浙江卫视", ChannelCategory.STV,"","http://183.252.176.35//PLTV/88888888/224/3221225934/index.m3u8",0,0));
    }
}
