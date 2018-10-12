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

package cn.com.pcalpha.iptv.channel;

import android.content.Context;

import java.util.List;

import cn.com.pcalpha.iptv.category.ChannelCategoryService;

public class ChannelService {

    private ChannelDao channelDao;
    private ChannelStreamService channelStreamService;
    private ChannelCategoryService channelCategoryService;

    private ChannelService(Context context) {
        this.channelDao = ChannelDao.getInstance(context);
        this.channelStreamService = ChannelStreamService.getInstance(context);
        this.channelCategoryService = ChannelCategoryService.getInstance(context);
    }

    private static ChannelService singleton;
    public static ChannelService getInstance(Context context){
        if (null == singleton) {
            synchronized (ChannelService.class) {
                if (null == singleton) {
                    singleton = new ChannelService(context);
                }
            }
        }
        return singleton;
    }

    public void clear() {
        channelDao.clear();
    }

    public void save(Channel channel) {
        channelDao.insert(channel);
    }

    public void delete(String name) {
        channelDao.delete(name);
    }

    public void update(Channel channel) {
        channelDao.update(channel);
    }

    public Channel get(String channelName) {
        return channelDao.get(channelName);
    }

    public List<Channel> find(Param4Channel param) {
        return channelDao.find(param);
    }

    public void setLastPlay(Channel channel) {
        channelDao.setLastPlay(channel.getName());
        channelCategoryService.setLastPlay(channel.getCategoryName());
    }

    public void setLastPlayStream(String channelName, int stream) {
        channelDao.setLastPlayStream(channelName, stream);
    }

    public Channel getLastPlay() {
        return channelDao.getLastPlay();
    }

}
