package com.iseva.app.source;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by com.bitsandbytes on 12/22/2015.
 */
public class Custom_ViewPager extends ViewPager {
    private boolean enabled;

    public Custom_ViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    /**
     * Custom implementation to enable or not swipe :)
     *
     * @param enabled
     *            true to enable swipe, false otherwise.
     */
    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }


}
