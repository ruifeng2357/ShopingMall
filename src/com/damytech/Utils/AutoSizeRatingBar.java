package com.damytech.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;

import com.damytech.yilebang.R;

import java.util.ArrayList;
import java.util.Iterator;

public class AutoSizeRatingBar extends View{

	private float 	fRateValue = 0.0f;
	private int		MaxRate = 5;
	private int		MinRate = 0;
	
	private static float fOffset = 8.0f;
	
	private Bitmap 	bmpOrgOn;
	private Bitmap 	bmpOrgOff;
	private Bitmap 	bmpOrgHalf;
	
	private Bitmap	m_bmpHalfStar;
	private Bitmap 	m_bmpOffStar;
	private Bitmap 	m_bmpOnStar;
		
	private int		m_nScaledBmpSize = 48;
	
	private static final int STARTYPE_ON = 0;
	private static final int STARTYPE_OFF = 1;
	private static final int STARTYPE_HALF = 2;
	
	private boolean CenterFlag = false;


    // Event handler stuff
    ArrayList<OnRatingBarChangeListener> listeners = new ArrayList<OnRatingBarChangeListener> ();

    public interface OnRatingBarChangeListener {
        public void onRatingChanged(AutoSizeRatingBar ratingBar, float rating);
    }

    public void setOnRatingBarChangeListener (OnRatingBarChangeListener listener)
    {
        // Store the listener object
        this.listeners.add(listener);
    }


	public AutoSizeRatingBar(Context context) {
		super(context);
		init();
	}
	
	public AutoSizeRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
	}
	
	/**
	 * initialize object
	 */
	private void init() {
		getOrgBitmap();
        initBitmaps();

        setOnTouchListener( new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int width = v.getWidth();
                float pos = event.getX();

                fRateValue = (int) ((pos * MaxRate / width) + 0.5);

                invalidate();

                // notify listeners
                Iterator<OnRatingBarChangeListener> iterator = listeners.iterator();
                while (iterator.hasNext()) {
                    OnRatingBarChangeListener  listener = iterator.next();

                    listener.onRatingChanged(AutoSizeRatingBar.this, fRateValue);
                }

                return false;
            }
        });
	}
	
	public void setHeight(int pxHeight) {
        m_nScaledBmpSize = pxHeight;
	}
	
	public void setCenter() {
		CenterFlag = true;
	}

    public float getNumStars () {
        return fRateValue;
    }
	
	/**
	 * initialize star bitmaps
	 */
	private void getOrgBitmap() {
		bmpOrgOn = BitmapFactory.decodeResource(getResources()
				, R.drawable.star_full);
		bmpOrgOff = BitmapFactory.decodeResource(getResources()
				, R.drawable.star_empty);
		bmpOrgHalf = BitmapFactory.decodeResource(getResources()
				, R.drawable.star_half);
		
	}
	
	/**
	 * change current rate value
	 * @param value [in], value to be set
	 */
	public void setRate(float value) {
		fRateValue = value;
	}
	
	/**
	 * set max rate value (star count)
	 * @param value [in], value to be set
	 */
	public void setMaxRate(int value) {
		MaxRate = value;
	}
	
	/**
	 * initialize bitmaps as scaled object
	 */
	private void initBitmaps() {
		m_bmpOnStar = Bitmap.createScaledBitmap(Bitmap.createBitmap(bmpOrgOn)
        		, m_nScaledBmpSize, m_nScaledBmpSize, true);
		m_bmpOffStar = Bitmap.createScaledBitmap(Bitmap.createBitmap(bmpOrgOff)
        		, m_nScaledBmpSize, m_nScaledBmpSize, true);
		m_bmpHalfStar = Bitmap.createScaledBitmap(Bitmap.createBitmap(bmpOrgHalf)
        		, m_nScaledBmpSize, m_nScaledBmpSize, true);
	}
	
	/**
	 * draw rating stars on canvas
	 * @param canvas
	 */
	private void drawRatingStar(Canvas canvas) {
		int i = 0;
		int posx = 0;
		
		for (i = MinRate; i < MaxRate; i ++) {
			posx = i * m_nScaledBmpSize;
			if (i >= fRateValue) {
				drawOneStar(STARTYPE_OFF, posx, canvas);
			} 
			else if ((i + 1) > fRateValue) {
				drawOneStar(STARTYPE_HALF, posx, canvas);
			}
			else {
				drawOneStar(STARTYPE_ON, posx, canvas);
			}
		}
	}
	
	/**
	 * calc offset value of x position (to draw at center)
	 * @return offset value
	 */
	private int calcOffsetX() {
		int dsp_cx = this.getWidth();
		
		int offset_x = dsp_cx - (MaxRate * m_nScaledBmpSize);
		offset_x = offset_x / 2;
		
		return offset_x;
	}
	
	/**
	 * calc offset value of y position (to draw at center)
	 * @return offset value
	 */
	private int calcOffsetY() {
		int dsp_cy = this.getHeight();
		
		int offset_y = dsp_cy - m_nScaledBmpSize;
		offset_y = offset_y / 2;
		
		return offset_y;
	}
	
	/**
	 * draw one star image on canvas
	 * @param type [in], star image type
	 * @param posX [in], image position
	 * @param canvas
	 */
	private void drawOneStar(int type, int posX, Canvas canvas) {
		int x = posX, y = 0;
		
		if (CenterFlag == true) {
			x = calcOffsetX() + posX;
			y = calcOffsetY();
		}
		switch (type) {
		case STARTYPE_ON :
			canvas.drawBitmap(m_bmpOnStar, x, y, null);
			break;
		case STARTYPE_HALF :
			canvas.drawBitmap(m_bmpHalfStar, x, y, null);
			break;
		case STARTYPE_OFF :
			canvas.drawBitmap(m_bmpOffStar, x, y, null);
			break;
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		initBitmaps();
		drawRatingStar(canvas);
		
		super.onDraw(canvas);
	}
}
