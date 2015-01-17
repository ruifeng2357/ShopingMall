package com.damytech.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * Created with IntelliJ IDEA.
 * User: Yunsungil
 * Date: 11/25/13
 * Time: 7:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class TouchWebView extends WebView {

    public TouchWebView(Context context) {
        super(context);
    }

    public TouchWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        requestDisallowInterceptTouchEvent(true);
        return super.onTouchEvent(event);
    }
}
