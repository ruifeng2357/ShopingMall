package com.damytech.yilebang;

import android.app.Activity;
import android.app.ProgressDialog;
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
import com.damytech.STData.STPreferInfo;
import com.damytech.STData.STServiceData;
import com.damytech.Global.GlobalData;
import com.damytech.Utils.PullToRefreshBase;
import com.damytech.Utils.PullToRefreshListView;
import com.damytech.Utils.ResolutionSet;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: KimChg
 * Date: 13-11-19
 * Time: 下午4:19
 * To change this template use File | Settings | File Templates.
 */
public class PreferentialListInfoActivity extends MyActivity {
    private PreferentialItemAdapter mAdapter;

    private PullToRefreshListView mPullRefreshList;
    private ListView mRealListView;

    private int mListId = 0;
    private ArrayList<STPreferInfo> mPreferDatalist = new ArrayList<STPreferInfo>(0);

    private JsonHttpResponseHandler handler;
    private ProgressDialog progDialog;

    private int ITEM_IMG_HEIGHT = 100;
    private int ITEM_IMG_WIDTH = 100;

    private static int IMG_HEIGHT = 100;
    private int mCurPageNo = 1;
    private String mKeyword = "";

    private int mSearchMode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferentiallistinfo);

        // initialize
        initControls();

        mSearchMode = getIntent().getExtras().getInt("SearchMode");
        if (mSearchMode == 0)
        {
            mKeyword = getIntent().getExtras().getString("Keyword");
            callGetCourtesyShopsOfKeyWord(mKeyword, ITEM_IMG_HEIGHT, mCurPageNo);
        }
        else
        {
            mListId = getIntent().getExtras().getInt("ItemId");
            callGetCourtesyShopsOfType(mListId);
        }
    }

    /**
     * Initialize all controls
     */
    private void initControls()
    {
        ResolutionSet._instance.iterateChild(findViewById(R.id.rlPreferListInfoMain));

        ///////////////////////////////////////////////////////////////////////////////////
        // initialize list view (PullToRefreshListView)
        mPullRefreshList = (PullToRefreshListView)findViewById(R.id.listInfoView);
        mPullRefreshList.setMode(PullToRefreshBase.Mode.PULL_FROM_END);

        // Set a listener to be invoked when the list should be refreshed.
        mPullRefreshList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                // Do work to refresh the list here.
                mCurPageNo = mCurPageNo + 1;
                if (mSearchMode == 0)
                    callGetCourtesyShopsOfKeyWord(mKeyword, ITEM_IMG_HEIGHT, mCurPageNo);
                else
                    callGetCourtesyShopsOfType(mListId);
                // new GetDataTask().execute();
            }
        });

        mRealListView = mPullRefreshList.getRefreshableView();

        mRealListView.setDivider(new ColorDrawable(Color.TRANSPARENT));
        mRealListView.setCacheColorHint(Color.parseColor("#FFF1F1F1"));
        mRealListView.setDividerHeight(2);

        mAdapter = new PreferentialItemAdapter(PreferentialListInfoActivity.this, this.getApplicationContext());
        mAdapter.setData(mPreferDatalist);

        mRealListView.setAdapter(mAdapter);


        //////////////////////////////////////////////////////////////////////////////////
        // search image button
        ImageView findImage = (ImageView) findViewById(R.id.imageView);
        findImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFind();
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
        mPreferDatalist.clear();
        mCurPageNo = 1;
        mSearchMode = 0;
        callGetCourtesyShopsOfKeyWord(mKeyword, ITEM_IMG_HEIGHT, mCurPageNo);
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


    /////////////////////////////////////// Service Relation //////////////////////////////////////
    /**
     * Call GetCourtesyChildTypes service
     */
    private void callGetCourtesyShopsOfType(int parentId)
    {
        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = STServiceData.MSG_SUCCESS;

                progDialog.dismiss();

                retMsg = CommMgr.commService.parseCourtesyShopsOfType(object, mPreferDatalist);
                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    result = STServiceData.ERR_SUCCESS;
                    // update ui
                    updateUI();
                    return;
                }
                else
                {
                    progDialog.dismiss();
                    result = STServiceData.ERR_EXCEPTION;
                }

                GlobalData.showToast(PreferentialListInfoActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                if (result == STServiceData.ERR_FAIL)
                {
                    progDialog.dismiss();
                    GlobalData.showToast(PreferentialListInfoActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                PreferentialListInfoActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.GetCourtesyShopsOfType(parentId, IMG_HEIGHT, mCurPageNo, handler);

        return;
    }

    private void callGetCourtesyShopsOfKeyWord(String keyword, int height, int pageno)
    {
        handler1 = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = STServiceData.MSG_SUCCESS;

                retMsg = CommMgr.commService.parseCourtesyShopsOfType(object, mPreferDatalist);
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

                GlobalData.showToast(PreferentialListInfoActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                if (result == STServiceData.ERR_FAIL)
                {
                    progDialog.dismiss();
                    GlobalData.showToast(PreferentialListInfoActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        CommMgr.commService.GetCourtesyShopsOfKeyWord(keyword, height, pageno, handler1);

        return;
    }
}
