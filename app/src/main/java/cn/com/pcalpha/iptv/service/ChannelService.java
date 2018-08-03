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

import android.content.Context;

import java.util.List;

import cn.com.pcalpha.iptv.model.bo.Param4Channel;
import cn.com.pcalpha.iptv.model.domain.Channel;
import cn.com.pcalpha.iptv.repository.ChannelDao;

public class ChannelService {

    private ChannelDao channelDao;
    private ChannelCategoryService channelCategoryService;

    public ChannelService(Context context) {
        this.channelDao = new ChannelDao(context);
        this.channelCategoryService = new ChannelCategoryService(context);
    }

    public void clear() {
        channelDao.clear();
    }

    public void save(Channel channel) {
        channelDao.insert(channel);
    }

    public void delete(String no) {
        channelDao.delete(no);
    }

    public void update(Channel channel) {
        channelDao.update(channel);
    }

    public Channel get(String channelNo) {
        return channelDao.get(channelNo);
    }

    public List<Channel> find(Param4Channel param) {
        return channelDao.find(param);
    }

    public void setLastPlay(Channel channel){
        channelDao.setLastPlay(channel.getNo());
        channelCategoryService.setLastPlay(channel.getCategoryName());
    }

    public Channel getLastPlay(){
        return channelDao.getLastPlay();
    }
}
