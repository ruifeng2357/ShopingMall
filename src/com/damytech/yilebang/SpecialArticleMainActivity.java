package com.damytech.yilebang;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.damytech.CommService.CommMgr;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.STData.*;
import com.damytech.Global.GlobalData;
import com.damytech.Utils.HorizontalPager;
import com.damytech.Utils.ResolutionSet;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA.
 * User: KimChg
 * Date: 13-11-20
 * Time: 上午10:59
 * To change this template use File | Settings | File Templates.
 */
public class SpecialArticleMainActivity extends MyActivity {
    private SpecialArticleMainItemAdapter mAdapter;
    private HorizontalPager mGallery = null;
    private ImageView mImgSlidePos[];
    private ListView listSpecialItems;
    private static int TIME_INTERVAL = 2000;
    Timer mTimer = new Timer();
    private Integer[] mImageIDs = {R.drawable.bmp1,  R.drawable.bmp2, R.drawable.bmp3, R.drawable.bmp4, R.drawable.bmp5};

    final int nMaxSlideImgCnt = 5;
    int nCurSlideImgCnt = 5;

    private ArrayList<STSpecialArticleMainInfo> mDatalist = new ArrayList<STSpecialArticleMainInfo>(0);
    private ArrayList<STAdvertInfo> mAdvertList;

    private JsonHttpResponseHandler handler;
    private JsonHttpResponseHandler handler1;
    private ProgressDialog progDialog;

    private int BANNER_IMG_WIDTH = 100;
    private int ITEM_IMG_HEIGHT = 100;
    private int ITEM_IMG_WIDTH = 100;

    DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.specialarticlemain);

        // initialize
        initControls();
        callGetAdvertList(STServiceData.ADVERT_EXTRA, BANNER_IMG_WIDTH);
    }

    /**
     * Initialize all controls
     */
    private void initControls()
    {
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_stub)
                .showImageOnFail(R.drawable.ic_stub)
                .cacheInMemory()
                .cacheOnDisc()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading()
                .build();

        mImgSlidePos = new ImageView[nMaxSlideImgCnt];
        mImgSlidePos[0] = (ImageView)findViewById(R.id.imgSlidePos1);
        mImgSlidePos[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGallery.setCurrentScreen(0, false);
                setSeparateColor(0);
            }
        });
        mImgSlidePos[1] = (ImageView)findViewById(R.id.imgSlidePos2);
        mImgSlidePos[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGallery.setCurrentScreen(1, false);
                setSeparateColor(1);
            }
        });
        mImgSlidePos[2] = (ImageView)findViewById(R.id.imgSlidePos3);
        mImgSlidePos[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGallery.setCurrentScreen(2, false);
                setSeparateColor(2);
            }
        });
        mImgSlidePos[3] = (ImageView)findViewById(R.id.imgSlidePos4);
        mImgSlidePos[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGallery.setCurrentScreen(3, false);
                setSeparateColor(3);
            }
        });
        mImgSlidePos[4] = (ImageView)findViewById(R.id.imgSlidePos5);
        mImgSlidePos[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGallery.setCurrentScreen(4, false);
                setSeparateColor(4);
            }
        });

        mGallery = (HorizontalPager)findViewById(R.id.hpBaseService_Body);
        mGallery.setCurrentScreen(0, false);
        mGallery.setOnScreenSwitchListener(new HorizontalPager.OnScreenSwitchListener() {
            @Override
            public void onScreenSwitched(int screen) {
                mGallery.setCurrentScreen(screen, false);
                setSeparateColor(screen);
            }
        });

        RelativeLayout rlDetailList = (RelativeLayout)findViewById(R.id.rlDetailList);
        rlDetailList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SpecialArticleMainActivity.this, SpecialArticleListActivity.class));
            }
        });

        listSpecialItems = (ListView)findViewById(R.id.listItemsView);
        //listPrefer.setAdapter();
        ResolutionSet._instance.iterateChild(findViewById(R.id.rlSpecialMain));


        ///////////////////////////////////////////////////////////
        // Search by keyword
        ImageView findView = (ImageView) findViewById(R.id.imageView);
        findView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFind();
            }
        });

        initCartBadge();
        initMainMenu();
        initFindTextView();
    }

    private void initFindTextView()
    {
        // get keyword to be find
        TextView editText = (TextView) findViewById(R.id.txtFind);
        editText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    startFind();
                    return true;
                }
                return false;
            }
        });
    }

    private void startFind()
    {
        InputMethodManager inputMethodManager = (InputMethodManager)  getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        // get keyword to be find
        TextView txtFind = (TextView) findViewById(R.id.txtFind);
        String keyword = txtFind.getText().toString();
        // show prefer list activity
        Intent intentDetail = new Intent(SpecialArticleMainActivity.this, SpecialArticleActivity.class);
        intentDetail.putExtra("SearchMode", 0);  // search by keyword
        intentDetail.putExtra("Keyword", keyword);
        startActivity(intentDetail);
    }

    private void initMainMenu()
    {
        RelativeLayout rlBottom_Home = (RelativeLayout) findViewById(R.id.rlBottom_Home);
        rlBottom_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgbtnHomeClicked();
            }
        });

        RelativeLayout rlBootom_Back = (RelativeLayout) findViewById(R.id.rlBottom_BackArraw);
        rlBootom_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgbtnBackClicked();
            }
        });

        RelativeLayout rlBottom_MainMenu = (RelativeLayout) findViewById(R.id.rlBottom_MainMenu);
        rlBottom_MainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgbtnMainMenuClicked();
            }
        });

        RelativeLayout rlBottom_Packet = (RelativeLayout) findViewById(R.id.rlBottom_Packet);
        rlBottom_Packet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgbtnCartClicked();
            }
        });

        RelativeLayout rlBottom_PersonInfo = (RelativeLayout) findViewById(R.id.rlBottom_PersonInfo);
        rlBottom_PersonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgbtnAccountClicked();
            }
        });
    }

    /**
     * Initialize badge icon of shopping cart
     */
    private void initCartBadge()
    {
        ImageView imgPacket = (ImageView) findViewById(R.id.imgBottom_Packet);
        this.setBadgeParent(imgPacket);
    }


    /**
     * Update UI using service data
     */
    private void updateUI()
    {
        loadAdvertiseView();

        listSpecialItems.setDivider(new ColorDrawable(Color.TRANSPARENT));
        listSpecialItems.setCacheColorHint(Color.parseColor("#FFF1F1F1"));
        listSpecialItems.setDividerHeight(10);
        mAdapter = new SpecialArticleMainItemAdapter(SpecialArticleMainActivity.this, this.getApplicationContext());
        mAdapter.setData(mDatalist);

        listSpecialItems.setAdapter(mAdapter);
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        stopTimer();
    }

    private TimerTask intro_timer_task = new TimerTask()
    {
        @Override
        public void run() {
            runOnUiThread(mRunnable);
        }
    };

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run()
        {
            int nSelection = mGallery.getCurrentScreen();
            if (nSelection == nCurSlideImgCnt - 1)
                nSelection = 0;
            else
                nSelection++;

            mGallery.setCurrentScreen(nSelection, true);
            setSeparateColor(nSelection);
        }
    };

    public void loadAdvertiseView()
    {
        nCurSlideImgCnt = mAdvertList.size();

        if (nCurSlideImgCnt == 0)
            return;

        try
        {
            int paddingWidth = (int)(10 * ResolutionSet.fXpro);
            int imgWidth = (int)(12 * ResolutionSet.fPro), imgHeight = (int)(12 * ResolutionSet.fPro);
            int baseLeft = (ResolutionSet.nWidth - imgWidth * nCurSlideImgCnt - paddingWidth * (nCurSlideImgCnt - 1)) / 2;
            int baseTop = (int)(180 * ResolutionSet.fYpro);
            for (int i = 0; i < nCurSlideImgCnt; i++)
            {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(imgWidth, imgHeight);
                layoutParams.leftMargin = baseLeft;
                layoutParams.topMargin = baseTop;
                mImgSlidePos[i].setBackgroundResource(R.drawable.gray_balloon);
                mImgSlidePos[i].setLayoutParams(layoutParams);
                mImgSlidePos[i].setVisibility(View.VISIBLE);

                ImageView imgView = new ImageView(mGallery.getContext());
                imgView.setLayoutParams(new RelativeLayout.LayoutParams(mGallery.getWidth(), mGallery.getHeight()));
                GlobalData.imageLoader.displayImage(mAdvertList.get(i).imagepath, imgView, options);
                mGallery.addView(imgView);

                baseLeft += imgWidth;
                baseLeft += paddingWidth;
            }

            setSeparateColor(0);

            startTimer();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void startTimer()
    {
        mTimer.schedule(intro_timer_task, TIME_INTERVAL, TIME_INTERVAL);
    }

    public void stopTimer()
    {
        mTimer.cancel();
    }

    private void setSeparateColor(int nNo)
    {
        for ( int i = 0; i < nCurSlideImgCnt; i++ )
        {
            if ( i == nNo )
                mImgSlidePos[i].setImageResource(R.drawable.red_balloon);
            else
                mImgSlidePos[i].setImageResource(R.drawable.gray_balloon);
        }
    }


    /////////////////////////////////////// Service Relation //////////////////////////////////////
    /**
     * Call GetAdvertList service
     * @param type [in], advertisement type
     * @param width [in], image width
     */
    private void callGetAdvertList(int type, int width)
    {
        mAdvertList = new ArrayList<STAdvertInfo>();
        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = STServiceData.MSG_SUCCESS;

                progDialog.dismiss();

                retMsg = CommMgr.commService.parseGetAdvertList(object, mAdvertList);
                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    result = STServiceData.ERR_SUCCESS;
                    callGetSpecialProducts(ITEM_IMG_WIDTH, ITEM_IMG_HEIGHT);
                    return;
                }
                else
                {
                    result = STServiceData.ERR_EXCEPTION;
                }

                GlobalData.showToast(SpecialArticleMainActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                progDialog.dismiss();
                if (result == STServiceData.ERR_FAIL)
                    GlobalData.showToast(SpecialArticleMainActivity.this, getString(R.string.server_connection_error));

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                SpecialArticleMainActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.GetAdvertList(type, width, handler);

        return;
    }

    /**
     * Get special products of TeYou
     * @param height [in], height of image
     * @param width [in], width of image
     */
    private void callGetSpecialProducts(int width, int height)
    {
        handler1 = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = STServiceData.MSG_SUCCESS;

                retMsg = CommMgr.commService.parseSpecialProducts(object, mDatalist);
                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    result = STServiceData.ERR_SUCCESS;
                    // update ui by using data
                    updateUI();
                    return;
                }
                else
                {
                    progDialog.dismiss();
                    result = STServiceData.ERR_EXCEPTION;
                }

                GlobalData.showToast(SpecialArticleMainActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                if (result == STServiceData.ERR_FAIL)
                {
                    progDialog.dismiss();
                    GlobalData.showToast(SpecialArticleMainActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        CommMgr.commService.GetSpecialProducts(width, height, handler1);

        return;
    }

}
