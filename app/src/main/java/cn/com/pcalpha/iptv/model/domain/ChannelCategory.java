package cn.com.pcalpha.iptv.model.domain;

import java.io.Serializable;

/**
 * Created by caiyida on 2018/6/24.
 */

public class ChannelCategory implements Serializable {
    private Integer id;
    private String name;
    private Integer lastPlay = 0;//上次播放标记

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

    public Integer getLastPlay() {
        return lastPlay;
    }

    public void setLastPlay(Integer lastPlay) {
        this.lastPlay = lastPlay;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChannelCategory) {
            ChannelCategory o = (ChannelCategory) obj;
            if (o.getName().equals(this.getName())) {
                return true;
            }
        }
        return false;
    }
}
