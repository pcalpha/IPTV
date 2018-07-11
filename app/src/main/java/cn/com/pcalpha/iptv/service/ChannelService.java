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

import cn.com.pcalpha.iptv.model.Channel;
import cn.com.pcalpha.iptv.repository.ChannelDao;

public class ChannelService {
    private ChannelDao channelDao;

    public ChannelService(Context context) {
        this.channelDao = new ChannelDao(context);
    }

    public void clear(){
        channelDao.clear();
    }

    public void save(Channel channel){
        channelDao.insert(channel);
    }

    public Channel get(int channelNo){
        return channelDao.get(channelNo);
    }

    public void setLastAccess(int channelNo){
        channelDao.setLastAccess(channelNo);
    }

    public Channel getLastAccess(){
        return channelDao.getLastAccess();
    }

}
