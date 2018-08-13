package cn.com.pcalpha.iptv.model.domain;

import java.io.Serializable;

public class SettingOption implements Serializable {
    private String label;
    private String value;

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

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (obj instanceof SettingOption) {
            SettingOption o = (SettingOption) obj;
            if (o.getLabel().equals(this.getLabel())) {
                return true;
            }
        }
        return false;
    }
}