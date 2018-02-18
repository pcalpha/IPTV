package cn.com.pcalpha.iptv.widget;

import android.content.Context;
import android.support.v17.leanback.widget.ImageCardView;

/**
 * Created by caiyida on 2018/2/12.
 */

public class CustomImageCardView extends ImageCardView {
    private boolean focus;

    public CustomImageCardView(Context context) {
        super(context);
    }

    public boolean isFocus() {
        return focus;
    }

    public void setFocus(boolean focus) {
        this.focus = focus;
    }
}
