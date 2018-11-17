package cn.com.pcalpha.iptv.channel.stream;

/**
 * Created by caiyida on 2018/8/8.
 */

public class Param4ChannelStream {
    private String channelName;
    private String carrier;

    public static Param4ChannelStream build(){
        return new Param4ChannelStream();
    }


    public String getChannelName() {
        return channelName;
    }

    public Param4ChannelStream setChannelName(String channelName) {
        this.channelName = channelName;
        return this;
    }

    public String getCarrier() {
        return carrier;
    }

    public Param4ChannelStream setCarrier(String carrier) {
        this.carrier = carrier;
        return this;
    }
}
