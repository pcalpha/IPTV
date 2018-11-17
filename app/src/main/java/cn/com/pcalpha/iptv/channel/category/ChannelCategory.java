package cn.com.pcalpha.iptv.channel.category;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by caiyida on 2018/6/24.
 */

public class ChannelCategory implements Serializable {
    private Integer id;
    private String name;
    private Date playTime;//上次播放标记

    public ChannelCategory() {
    }

    public ChannelCategory(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getPlayTime() {
        return playTime;
    }

    public void setPlayTime(Date playTime) {
        this.playTime = playTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (obj instanceof ChannelCategory) {
            ChannelCategory o = (ChannelCategory) obj;
            if (o.getName().equals(this.getName())) {
                return true;
            }
        }
        return false;
    }
}
