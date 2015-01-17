package com.damytech.yilebang;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
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
import com.damytech.Global.GlobalData;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.STData.STAdvertInfo;
import com.damytech.STData.STPreferInfo;
import com.damytech.STData.STServiceData;
import com.damytech.Utils.*;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import org.json.JSONObject;
import com.baidu.mapapi.*;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA.
 * Company: D.M.Y
 * Date: 13-11-15
 * Time: 下午9:18
 * To change this template use File | Settings | File Templates.
 */
public class PreferentialActivity  extends MyActivity {
    private PreferentialItemAdapter mAdapter;
    private HorizontalPager mGallery = null;
    private ImageView mImgSlidePos[];

    private PullToRefreshListView mPullRefreshList;
    private ListView mRealListView;
//    private ListView listPrefer;

    private static int TIME_INTERVAL = 2000;
    Timer mTimer = new Timer();

    final int nMaxSlideImgCnt = 5;
    int nCurSlideImgCnt = 5;

    private ArrayList<STPreferInfo> mDatalist = new ArrayList<STPreferInfo>(0);
    private ArrayList<STAdvertInfo> mAdvertList;

    private JsonHttpResponseHandler handler;
    private JsonHttpResponseHandler handler1;
    private JsonHttpResponseHandler handlerNearst;
    private ProgressDialog progDialog;

    private int BANNER_IMG_WIDTH = 100;
    private int ITEM_IMG_HEIGHT = 100;
    private int ITEM_IMG_WIDTH = 100;

    private int nCurPageNumber = 1;
    private String mKeyword = "";

    DisplayImageOptions options;

    private int m_nWithPosFind = 0;
    private double m_fLatitude = 0.0f;
    private double m_fLongitude = 0.0f;

    private static BMapManager m_baiduMapMan = null;
    private static MKSearch m_mKsearch = null;
    private static final String BM_KEY = "A737CDFA6D523EC18453F98BB399A8E04D9C51C8";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferential);

        m_nWithPosFind = getIntent().getIntExtra("WithPos", 0);

//        m_baiduMapMan = new BMapManager(this);
//        m_baiduMapMan.init(BM_KEY, new BMGeneralListener());
//
//        m_mKsearch = new MKSearch();
//        m_mKsearch.init(m_baiduMapMan, new MySearchListener());
//        m_baiduMapMan.start();
//
//        Location loc = m_baiduMapMan.getLocationManager().getLocationInfo();
//        if (loc != null)
//        {
//            m_fLatitude = loc.getLatitude();
//            m_fLongitude = loc.getLongitude();
//        }

        // initialize
        initControls();
        callGetAdvertList(STServiceData.ADVERT_PREFER, BANNER_IMG_WIDTH);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (m_baiduMapMan != null)
            m_baiduMapMan.stop();
    }

    public static  class BMGeneralListener implements MKGeneralListener {
        @Override
        public void onGetNetworkState(int iError) {}
        @Override
        public void onGetPermissionState(int iError) {}
    }

    public class MySearchListener implements MKSearchListener {
        @Override
        public void onGetAddrResult(MKAddrInfo result, int iError) {}
        @Override
        public void onGetDrivingRouteResult(MKDrivingRouteResult result, int iError) {}
        @Override
        public void onGetPoiResult(MKPoiResult result, int type, int iError) {}
        @Override
        public void onGetTransitRouteResult(MKTransitRouteResult result, int iError) {}
        @Override
        public void onGetWalkingRouteResult(MKWalkingRouteResult result, int iError) {}
        @Override
        public void onGetRGCShareUrlResult(java.lang.String s, int i){}
        @Override
        public void onGetBusDetailResult(com.baidu.mapapi.MKBusLineResult mkBusLineResult, int i) {}
        @Override
        public void onGetSuggestionResult(com.baidu.mapapi.MKSuggestionResult mkSuggestionResult, int i){}
        @Override
        public  void onGetPoiDetailSearchResult(int i, int i1){}
    }

    /**
     * Initialize all controls
     */
    private void initControls()
    {
        ResolutionSet._instance.iterateChild(findViewById(R.id.rlPreferentialLayout));

        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_stub)
                .showImageOnFail(R.drawable.ic_stub)
                .cacheInMemory()
                .cacheOnDisc()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading()
                .build();

        ///////////////////////////////////////////////////////////////////////////////////
        // initialize list view (PullToRefreshListView)
        mPullRefreshList = (PullToRefreshListView)findViewById(R.id.listPreferential);
        mPullRefreshList.setMode(PullToRefreshBase.Mode.PULL_FROM_END);

        // Set a listener to be invoked when the list should be refreshed.
        mPullRefreshList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                // Do work to refresh the list here.
                nCurPageNumber = nCurPageNumber + 1;
                if (m_nWithPosFind == 0)
                    callGetCourtesyShopsOfKeyWord(mKeyword, ITEM_IMG_HEIGHT, nCurPageNumber);
                else
                    callGetNearbyCourtesyShops(ITEM_IMG_WIDTH, ITEM_IMG_HEIGHT, nCurPageNumber);
                // new GetDataTask().execute();
            }
        });

        mRealListView = mPullRefreshList.getRefreshableView();
        registerForContextMenu(mRealListView);

        mRealListView.setDivider(new ColorDrawable(Color.TRANSPARENT));
        mRealListView.setCacheColorHint(Color.parseColor("#FFF1F1F1"));
        mRealListView.setDividerHeight(2);


        mAdapter = new PreferentialItemAdapter(PreferentialActivity.this, this.getApplicationContext());
        mAdapter.setData(mDatalist);

        mRealListView.setAdapter(mAdapter);

        //////////////////////////////////////////////////////////////////////////////////
        // search image button
        ImageView findImage = (ImageView) findViewById(R.id.findImage);
        findImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFind();
            }
        });


        ////////////////////////////////////////////////////////////////////
        // advertisement banner image
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
        mGallery.setOnScreenSwitchListener(new HorizontalPager.OnScreenSwitchListener()	{
            @Override
            public void onScreenSwitched(int screen) {
                mGallery.setCurrentScreen(screen, false);
                setSeparateColor(screen);
            }
        });

        ImageView mPrefImgDetail = (ImageView)findViewById(R.id.imgTop_ListView);
        mPrefImgDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(PreferentialActivity.this, PreferentialListMainActivity.class);
                startActivity(newIntent);
            }
        });

        initCartBadge();
        initMainMenu();
        initFindTextView();

    }

    private void startFind()
    {
        InputMethodManager inputMethodManager = (InputMethodManager)  getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        // get keyword string to be find
        TextView txtKeyword = (TextView) findViewById(R.id.txtFind);
        mKeyword = txtKeyword.getText().toString();

        // initialize & find data
        mDatalist.clear();
        nCurPageNumber = 1;
        if (m_nWithPosFind == 0)
            callGetCourtesyShopsOfKeyWord(mKeyword, ITEM_IMG_HEIGHT, nCurPageNumber);
        else
            callGetNearbyCourtesyShops(ITEM_IMG_WIDTH, ITEM_IMG_HEIGHT, nCurPageNumber);
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
        mAdapter.notifyDataSetChanged();
        // Call onRefreshComplete when the list has been refreshed.
        mPullRefreshList.onRefreshComplete();
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

    /**
     * Load & Initialize advertise images
     */
    public void loadAdvertiseView()
    {
        try
        {
            nCurSlideImgCnt = mAdvertList.size();

            if (nCurSlideImgCnt == 0)
                return;

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
                    loadAdvertiseView();
                    if (m_nWithPosFind == 0)
                        callGetCourtesyShopsOfKeyWord(mKeyword, ITEM_IMG_HEIGHT, nCurPageNumber);
                    else
                        callGetNearbyCourtesyShops(ITEM_IMG_WIDTH, ITEM_IMG_HEIGHT, nCurPageNumber);
                    return;
                }
                else
                {
                    result = STServiceData.ERR_EXCEPTION;
                }

                GlobalData.showToast(PreferentialActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                progDialog.dismiss();
                if (result == STServiceData.ERR_FAIL)
                    GlobalData.showToast(PreferentialActivity.this, getString(R.string.server_connection_error));

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                PreferentialActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.GetAdvertList(type, width, handler);

        return;
    }

    /**
     * Get courtesy shop by keyword
     * @param keyword [in], keyword to be find
     * @param height [in], image height
     * @param pageno [in], current page number
     */
    private void callGetCourtesyShopsOfKeyWord(String keyword, int height, int pageno)
    {
        handler1 = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = STServiceData.MSG_SUCCESS;

                retMsg = CommMgr.commService.parseCourtesyShopsOfType(object, mDatalist);
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

                GlobalData.showToast(PreferentialActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                if (result == STServiceData.ERR_FAIL)
                {
                    progDialog.dismiss();
                    GlobalData.showToast(PreferentialActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        CommMgr.commService.GetCourtesyShopsOfKeyWord(keyword, height, pageno, handler1);

        return;
    }

    private void callGetNearbyCourtesyShops(int width, int height, int pageno)
    {
        handlerNearst = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = STServiceData.MSG_SUCCESS;

                retMsg = CommMgr.commService.parseCourtesyShopsOfType(object, mDatalist);
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

                GlobalData.showToast(PreferentialActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                if (result == STServiceData.ERR_FAIL)
                {
                    progDialog.dismiss();
                    GlobalData.showToast(PreferentialActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        CommMgr.commService.RequestGetNearbyCourtesyShops(m_fLatitude, m_fLongitude,  width, height, pageno, handlerNearst);

        return;
    }
}
