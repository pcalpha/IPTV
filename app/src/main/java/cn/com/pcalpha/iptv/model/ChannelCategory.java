package cn.com.pcalpha.iptv.model;

/**
 * Created by caiyida on 2018/2/5.
 */

public class ChannelCategory {
    public static final Integer CCTV= 1;
    public static final Integer STV= 2;
    public static final Integer OTHER= 99;

    private Integer no;
    private String name;

    public ChannelCategory() {
    }

    public ChannelCategory(Integer no, String name) {
        this.no = no;
        this.name = name;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
