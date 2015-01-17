package com.damytech.yilebang;

import java.lang.reflect.Method;

import android.graphics.Rect;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import cn.sharesdk.framework.ShareSDK;
import com.damytech.Utils.ResolutionSet;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Display;
import android.view.Menu;
import android.view.WindowManager;

@SuppressLint("HandlerLeak")
public class MainActivity extends Activity {
    RelativeLayout mainLayout;
    boolean bInitialized = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

        mainLayout = (RelativeLayout)findViewById(R.id.rlLogoLayout);
        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    public void onGlobalLayout() {
                        if (bInitialized == false)
                        {
                            Rect r = new Rect();
                            mainLayout.getLocalVisibleRect(r);
                            ResolutionSet._instance.setResolution(r.width(), r.height());
                            ResolutionSet._instance.iterateChild(findViewById(R.id.rlLogoLayout));
                            bInitialized = true;
                        }
                    }
                }
        );

        Handler handler= new Handler()
		{
			public void handleMessage(Message msg)
			{
				startActivity(new Intent(MainActivity.this, LoginActivity.class));
				finish();
			}
		};

   		handler.sendEmptyMessageDelayed(0, 2000);
	}

    @Override
    protected void onDestroy() {
        super.onDestroy();    //To change body of overridden methods use File | Settings | File Templates.

        ShareSDK.stopSDK(this);
    }

    public int getStatusBarHeight()
	{
		int statusBarHeight = 0;

	    if (hasOnScreenSystemBar()) 
	    {
	    	int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
	        if (resourceId > 0) 
	        {
	        	statusBarHeight = getResources().getDimensionPixelSize(resourceId);
	        }
	    }

	        return statusBarHeight;
	}
	
	private boolean hasOnScreenSystemBar()
	{
    	Display display = getWindowManager().getDefaultDisplay();
    	int rawDisplayHeight = 0;
    	try 
    	{
        	Method getRawHeight = Display.class.getMethod("getRawHeight");
        	rawDisplayHeight = (Integer) getRawHeight.invoke(display);
    	} catch (Exception ex) {}

		int UIRequestedHeight = display.getHeight();

		return (rawDisplayHeight - UIRequestedHeight > 0);
	 }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
