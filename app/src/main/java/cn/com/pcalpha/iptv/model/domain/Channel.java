package cn.com.pcalpha.iptv.model.domain;

import java.io.Serializable;

/**
 * Created by caiyida on 2018/2/5.
 */

public class Channel implements Serializable {
    private Integer id;
    private String no;
    private String name;//名字 例如:央视一套
    private String src;//多个源url逗号分隔
    private Integer lastPlay = 0;//上次播放标记

    private String categoryName;//1.央视;2.卫视;9.其他

    private Integer position;//绑定listview的位置

    private Channel pre;//上一个频道
    private Channel next;//下一个频道

    private ChannelCategory category;

    public Channel() {
    }

    public Channel(Integer id, String no, String name, String src, String categoryName) {
        this.id = id;
        this.no = no;
        this.name = name;
        this.src = src;
        this.lastPlay = lastPlay;
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

    public void setSrc(String src) {
        this.src = src;
    }

    public String getSrc() {
        return src;
    }

    public String getSrc(Integer srcIndex) {
        String[] srcArr = src.split(",");
        if (srcIndex != null || srcIndex < srcArr.length) {
            return srcArr[srcIndex];
        }
        return srcArr[0];
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Channel getPre() {
        return pre;
    }

    public void setPre(Channel pre) {
        this.pre = pre;
    }

    public Channel getNext() {
        return next;
    }

    public void setNext(Channel next) {
        this.next = next;
    }

    public Integer getLastPlay() {
        return lastPlay;
    }

    public void setLastPlay(Integer lastPlay) {
        this.lastPlay = lastPlay;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Channel){
            Channel o = (Channel)obj;
            if(o.getNo().equals(this.getNo())){
                return true;
            }
        }
        return false;
    }
}

