package cn.com.pcalpha.iptv.category;

import android.content.Context;

import java.util.List;

/**
 * Created by caiyida on 2018/6/23.
 */

public class ChannelCategoryService {

    private ChannelCategoryDao channelCategoryDao;

    private ChannelCategoryService(Context context) {
        this.channelCategoryDao = ChannelCategoryDao.getInstance(context);
    }

    private static ChannelCategoryService singleton;
    public static ChannelCategoryService getInstance(Context context){
        if (null == singleton) {
            synchronized (ChannelCategoryService.class) {
                if (null == singleton) {
                    singleton = new ChannelCategoryService(context);
                }
            }
        }
        return singleton;
    }

    public void clear() {
        channelCategoryDao.clear();
    }

    public void save(ChannelCategory channelCategory) {
        channelCategoryDao.insert(channelCategory);
    }

    public void delete(Integer id) {
        channelCategoryDao.delete(id);
    }

    public void update(ChannelCategory channel) {
        channelCategoryDao.update(channel);
    }

    public ChannelCategory get(Integer id) {
        return channelCategoryDao.get(id);
    }

    public List<ChannelCategory> findAll() {
        return channelCategoryDao.findAll();
    }

    public void setLastPlay(String channelNo){
        channelCategoryDao.setLastPlay(channelNo);
    }

    public ChannelCategory getLastPlay(){
        return channelCategoryDao.getLastPlay();
    }
}
