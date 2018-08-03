package cn.com.pcalpha.iptv.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by caiyida on 2018/7/15.
 */

public class RhythmView extends FrameLayout {
    private List<ObjectAnimator> animatorList;
    private boolean attachToWindow;
    private boolean c;
    private OnVisibilityChangedListener onVisibilityChangedListener;
    private boolean openAnimation;


    public RhythmView(Context context) {
        this(context, null);
    }

    public RhythmView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RhythmView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.animatorList = new ArrayList(3);
        this.openAnimation = true;
        a();
    }

    private void a() {
        for (int i = 0; i < 3; i++) {
            b();
            a(this.animatorList, i);
        }
    }

    private void a(ObjectAnimator objectAnimator) {
        if (objectAnimator != null && !objectAnimator.isStarted()) {
            Object target = objectAnimator.getTarget();
            if (target instanceof View) {
                a((View) target);
            }
            objectAnimator.start();
        }
    }

    private void a(View view) {
        view.setScaleY(0.1f);
    }

    private void a(List<ObjectAnimator> list, int i) {
        ObjectAnimator objectAnimator = new ObjectAnimator();
        list.add(objectAnimator);
        objectAnimator.setFloatValues(new float[]{1.0f, .03f});
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.setRepeatCount(-1);
        objectAnimator.setDuration(800);
        objectAnimator.setTarget(getChildAt(i));
        objectAnimator.setPropertyName("scaleY");
    }

    private void b() {
        View view = new View(getContext());
        view.setBackgroundColor(Color.argb(255, 255, 255, 255));
        addView(view);
    }

    private void b(ObjectAnimator objectAnimator) {
        if (null != objectAnimator && objectAnimator.isStarted()) {
            objectAnimator.cancel();
        }
    }

    private void c() {
        if (this.openAnimation && !this.c && this.attachToWindow && getVisibility() == View.VISIBLE) {
            this.c = true;
            Collections.shuffle(this.animatorList);
            for (int i = 0; i < 3; i++) {
                ObjectAnimator objectAnimator = (ObjectAnimator) this.animatorList.get(i);
                objectAnimator.setStartDelay((long) ((((float) (((long) i) * 800)) * 2.0f) / 5.0f));
                a(objectAnimator);
            }
        }
    }

    private void d() {
        if (this.c) {
            this.c = false;
            for (int i = 0; i < 3; i++) {
                b((ObjectAnimator) this.animatorList.get(i));
            }
        }
    }

    public OnVisibilityChangedListener getOnVisibilityChangedListener() {
        return this.onVisibilityChangedListener;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.attachToWindow = true;
        c();
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.attachToWindow = false;
        d();
    }

    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int measuredWidth = (int) ((((float) getMeasuredWidth()) * 1.0f) / 5.0f);
        int measuredHeight = getMeasuredHeight();
        for (int i5 = 0; i5 < 3; i5++) {
            int i6 = (i5 * 2) * measuredWidth;
            View childAt = getChildAt(i5);
            childAt.layout(i6, 0, i6 + measuredWidth, measuredHeight);
            childAt.setPivotY((float) childAt.getHeight());
        }
    }

    protected void onVisibilityChanged(@NonNull View view, int i) {
        super.onVisibilityChanged(view, i);
        if (i == 0) {
            c();
        } else {
            d();
        }
        if (view == this && this.onVisibilityChangedListener != null) {
            this.onVisibilityChangedListener.onVisibilityChangedListener(view, i);
        }
    }

    public void setColor(int i) {
        for (int i2 = 0; i2 < 3; i2++) {
            View childAt = getChildAt(i2);
            if (childAt != null) {
                childAt.setBackgroundResource(i);
            }
        }
    }

    public void setOnVisibilityChangedListener(OnVisibilityChangedListener l) {
        this.onVisibilityChangedListener = l;
    }

    public void setOpenAnimation(boolean openAnimation) {
        this.openAnimation = openAnimation;
    }

    public interface OnVisibilityChangedListener {
        void onVisibilityChangedListener(View view, int i);
    }

}
