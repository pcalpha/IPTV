package cn.com.pcalpha.iptv.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by caiyida on 2018/2/5.
 */

public class Channel implements Serializable {
    private Integer no;
    private String name;//名字 例如:央视一套
    private Integer type;//1.央视;2.卫视;9.其他
    private String iconUrl;//图标Url
    private String src;

    private Integer srcIndex;//当前源的索引
    private Integer lastAccess;//最新查看

    private Channel pre;//上一个频道
    private Channel next;//下一个频道

    public Channel() {
    }

    public Channel(Integer no, String name, Integer type, String iconUrl, String src) {
        this.no = no;
        this.name = name;
        this.type = type;
        this.iconUrl = iconUrl;
        this.src = src;
        this.srcIndex = 0;
        this.lastAccess = 0;
    }

    public Channel(Integer no, String name, Integer type, String iconUrl, String src, Integer srcIndex, Integer lastAccess) {
        this.no = no;
        this.name = name;
        this.type = type;
        this.iconUrl = iconUrl;
        this.src = src;
        this.srcIndex = srcIndex;
        this.lastAccess = lastAccess;
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

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSrc() {
        String[] srcArr = src.split(",");
        if(srcIndex!=null||srcIndex<srcArr.length){
            return srcArr[srcIndex];
        }
        return srcArr[0];
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getSrc(Integer srcIndex) {
        String[] srcArr = src.split(",");
        if(srcIndex!=null||srcIndex<srcArr.length){
            return srcArr[srcIndex];
        }
        return srcArr[0];
    }

    public void setSrcIndex(Integer srcIndex) {
        this.srcIndex = srcIndex;
    }

    public Integer getSrcIndex() {
        String[] srcArr = src.split(",");
        if(srcIndex!=null&&srcIndex<srcArr.length){
            return srcIndex;
        }
        return 0;
    }

    public void nextSrcIndex() {
        String[] srcArr = src.split(",");
        if(srcIndex!=null&&srcIndex+1<srcArr.length){
            srcIndex+=1;
        }
    }

    public void preSrcIndex() {
        String[] srcArr = src.split(",");
        if(srcIndex!=null&&srcIndex-1>-1){
            srcIndex-=1;
        }
    }

    public Integer getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(Integer lastAccess) {
        this.lastAccess = lastAccess;
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
}

