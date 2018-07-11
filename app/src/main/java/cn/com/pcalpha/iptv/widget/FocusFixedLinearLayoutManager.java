package cn.com.pcalpha.iptv.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class FocusFixedLinearLayoutManager extends android.support.v7.widget.LinearLayoutManager {

    //是否可以纵向移出
    private boolean mCanFocusOutVertical = false;
    //是否可以横向移出
    private boolean mCanFocusOutHorizontal = true;

    private Activity activity;

    public FocusFixedLinearLayoutManager(Activity activity, int orientation, boolean reverseLayout) {
        super(activity, orientation, reverseLayout);
        this.activity = activity;
    }

    public FocusFixedLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr,
                                         int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public View onInterceptFocusSearch(View focused, int direction) {
        int currentPosition = getPosition(focused);
        int count = getItemCount();

        int lastVisiblePosition = findLastVisibleItemPosition();
        switch (direction){
            case View.FOCUS_DOWN:
                currentPosition++;
                break;
            case View.FOCUS_UP:
                currentPosition--;
                break;
        }
        if(currentPosition<0||currentPosition>=count){
            return focused;
        }else if(currentPosition>lastVisiblePosition){
            scrollToPosition(currentPosition);
        }

        return super.onInterceptFocusSearch(focused,direction);
    }
}