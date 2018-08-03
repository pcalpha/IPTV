package cn.com.pcalpha.iptv.model.bo;

/**
 * Created by caiyida on 2018/7/30.
 */

public class Param4Channel {
    private String categoryName;

    public static Param4Channel build(){
        return new Param4Channel();
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Param4Channel setCategoryName(String categoryName) {
        this.categoryName = categoryName;
        return this;
    }
}
