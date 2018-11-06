package cn.com.pcalpha.iptv.channel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import cn.com.pcalpha.iptv.category.ChannelCategory;

/**
 * Created by caiyida on 2018/2/5.
 */

public class Channel implements Serializable {
    private Integer id;
    private String no;
    private String name;//名字 例如:央视一套
    private Date playTime;//上次播放标记 0，1
    private String categoryName;//1.央视;2.卫视;9.其他

    private ChannelStream lastPlayStream;//记录播放源
    private ChannelCategory channelCategory;
    private List<ChannelStream> channelStreams;

    public Channel() {
    }

    public Channel(String no, String name, String categoryName) {
        this.no = no;
        this.name = name;
        this.categoryName = categoryName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

//    public void setSrc(String src) {
//        this.src = src;
//    }
//
//    public String getSrc() {
//        return src;
//    }
//
//    public String getSrc(Integer srcIndex) {
//        String[] srcArr = src.split(",");
//        if (srcIndex != null || srcIndex < srcArr.length) {
//            return srcArr[srcIndex];
//        }
//        return srcArr[0];
//    }


    public Date getPlayTime() {
        return playTime;
    }

    public void setPlayTime(Date playTime) {
        this.playTime = playTime;
    }

    public ChannelCategory getChannelCategory() {
        return channelCategory;
    }

    public void setChannelCategory(ChannelCategory channelCategory) {
        this.channelCategory = channelCategory;
    }


    public ChannelStream getLastPlayStream() {
        return lastPlayStream;
    }

    public void setLastPlayStream(ChannelStream lastPlayStream) {
        this.lastPlayStream = lastPlayStream;
    }

    public List<ChannelStream> getChannelStreams() {
        return channelStreams;
    }

    public void setChannelStreams(List<ChannelStream> channelStreams) {
        this.channelStreams = channelStreams;
    }

    public ChannelStream preStream() {
        if (null != channelStreams && channelStreams.size() > 0 && null != lastPlayStream) {
            int prePosition = channelStreams.indexOf(lastPlayStream) - 1;

            if (prePosition >= 0 && prePosition < channelStreams.size()) {
                lastPlayStream = channelStreams.get(prePosition);
            } else {
                //lastPlayStream = channelStreams.get(0);
                return null;
            }
        }
        return lastPlayStream;
    }

    public ChannelStream nextStream() {
        if (null != channelStreams && channelStreams.size() > 0 && null != lastPlayStream) {
            int nextPosition = channelStreams.indexOf(lastPlayStream) + 1;

            if (nextPosition >= 0 && nextPosition < channelStreams.size()) {
                lastPlayStream = channelStreams.get(nextPosition);
            } else {
                //lastPlayStream = channelStreams.get(channelStreams.size() - 1);
                return null;
            }
        }
        return lastPlayStream;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (obj instanceof Channel) {
            Channel o = (Channel) obj;
            if (o.getName().equals(this.getName())) {
                return true;
            }
        }
        return false;
    }
}

