package cn.com.pcalpha.iptv.service;

import android.content.Context;

import java.util.List;
import java.util.Map;

import cn.com.pcalpha.iptv.model.bo.Param4Channel;
import cn.com.pcalpha.iptv.model.bo.Param4ChannelStream;
import cn.com.pcalpha.iptv.model.domain.Channel;
import cn.com.pcalpha.iptv.model.domain.ChannelStream;
import cn.com.pcalpha.iptv.repository.ChannelCategoryDao;
import cn.com.pcalpha.iptv.repository.ChannelDao;
import cn.com.pcalpha.iptv.repository.ChannelStreamDao;

/**
 * Created by caiyida on 2018/8/8.
 */

public class ChannelStreamService {
    private ChannelStreamDao channelStreamDao;

    private ChannelStreamService(Context context) {
        channelStreamDao = ChannelStreamDao.getInstance(context);
    }

    private static ChannelStreamService singleton;
    public static ChannelStreamService getInstance(Context context){
        if (null == singleton) {
            synchronized (ChannelStreamService.class) {
                if (null == singleton) {
                    singleton = new ChannelStreamService(context);
                }
            }
        }
        return singleton;
    }

    public void clear() {
        channelStreamDao.clear();
    }

    public void save(ChannelStream channel) {
        channelStreamDao.insert(channel);
    }

    public void delete(String name) {
        channelStreamDao.delete(name);
    }

    public void update(ChannelStream channel) {
        channelStreamDao.update(channel);
    }

    public ChannelStream get(int id){
        return channelStreamDao.get(id);
    }

    public List<ChannelStream> find(Param4ChannelStream param) {
        return channelStreamDao.find(param);
    }

    public void setLastPlay(int id){
        //channelStreamDao.setLastPlay(id);
    }

    public List<ChannelStream> getChannelStreams(String channelName){
        Param4ChannelStream param = Param4ChannelStream.build().setChannelName(channelName);
        List<ChannelStream> channelStreamList = find(param);

        return channelStreamList;
    }

}
