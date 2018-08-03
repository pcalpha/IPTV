package cn.com.pcalpha.iptv.service;

import android.content.Context;

import java.util.List;

import cn.com.pcalpha.iptv.model.domain.ChannelCategory;
import cn.com.pcalpha.iptv.repository.ChannelCategoryDao;

/**
 * Created by caiyida on 2018/6/23.
 */

public class ChannelCategoryService {

    private ChannelCategoryDao channelCategoryDao;

    public ChannelCategoryService(Context context) {
        this.channelCategoryDao = new ChannelCategoryDao(context);
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
