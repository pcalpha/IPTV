package cn.com.pcalpha.iptv.model.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by caiyida on 2018/8/11.
 */

public class Setting implements Serializable {
    private String key;
    private String label;
    private String value;
    private List<SettingOption> options;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<SettingOption> getOptions() {
        return options;
    }

    public void setOptions(List<SettingOption> options) {
        this.options = options;
    }


    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Setting){
            Setting o = (Setting)obj;
            if(o.getLabel().equals(this.getLabel())){
                return true;
            }
        }
        return false;
    }
}
