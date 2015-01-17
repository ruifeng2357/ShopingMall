package com.damytech.yilebang;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.damytech.CommService.CommMgr;
import com.damytech.Global.CommonFunc;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.STData.STServiceData;
import com.damytech.STData.STSpecialArticleInfo;
import com.damytech.Global.GlobalData;
import com.damytech.Utils.PullToRefreshBase;
import com.damytech.Utils.PullToRefreshListView;
import com.damytech.Utils.ResolutionSet;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: KimChg
 * Date: 13-11-20
 * Time: 上午9:25
 * To change this template use File | Settings | File Templates.
 */
public class SpecialArticleActivity extends MyActivity {
    private SpecialArticleItemAdapter mAdapter;
    private PullToRefreshListView mPullRefreshList;
    private ListView mRealListView;

    private ArrayList<STSpecialArticleInfo> mArticleArray;
    private int nCurPageNumber = 1;
    private String mKeyword = "";
    public boolean bexistNext = true;

    private int mSearchMode = 0;

    final int ITEMS_REFRESH_COUNT = 10;

    private int mParentId = 0;
    private int IMG_WIDTH = 100;
    private int IMG_HEIGHT = 100;

    private JsonHttpResponseHandler handler;
    private JsonHttpResponseHandler handler1;
    private JsonHttpResponseHandler handler2;
    private ProgressDialog progDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.specialarticle);

        // initialize
        initControls();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        mArticleArray.clear();

        mSearchMode = getIntent().getExtras().getInt("SearchMode");
        if (mSearchMode == 0)
        {
            mKeyword = getIntent().getExtras().getString("Keyword");
            callGetProductOfKeyword(mKeyword, nCurPageNumber);
        }
        else
        {
            mParentId = getIntent().getExtras().getInt("PROD_ID");
            callGetProducts(nCurPageNumber);
        }
    }

    /**
     * Initialize all controls
     */
    private void initControls()
    {
        ResolutionSet._instance.iterateChild(findViewById(R.id.rlSpecialMain));

        mPullRefreshList = (PullToRefreshListView)findViewById(R.id.listArticlesView);
        mPullRefreshList.setMode(PullToRefreshBase.Mode.PULL_FROM_END);

        // Set a listener to be invoked when the list should be refreshed.
        mPullRefreshList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                // Do work to refresh the list here.
                nCurPageNumber = nCurPageNumber + 1;
                if (mSearchMode == 0)
                {
                    callGetProductOfKeyword(mKeyword, nCurPageNumber);
                }
                else if (mSearchMode == 2)
                {
                    callGetYiYuanQuanOfKeyWord(mKeyword, nCurPageNumber);
                }
                else
                {
                    callGetProducts(nCurPageNumber);
                }
               // new GetDataTask().execute();
            }
        });

        mRealListView = mPullRefreshList.getRefreshableView();
        registerForContextMenu(mRealListView);

        //listPrefer.setAdapter();
        //ResolutionSet._instance.iterateChild(findViewById(R.id.rlSpecialMain));

        mArticleArray = new ArrayList<STSpecialArticleInfo>();

        mRealListView.setDivider(new ColorDrawable(Color.LTGRAY));
        mRealListView.setCacheColorHint(Color.parseColor("#FFF1F1F1"));
        mRealListView.setDividerHeight(2);
        mAdapter = new SpecialArticleItemAdapter(SpecialArticleActivity.this, this.getApplicationContext());
        mAdapter.setData(mArticleArray);


        mPullRefreshList.setAdapter(mAdapter);

        RelativeLayout rlTopListView = (RelativeLayout)findViewById(R.id.rlTopListView);
        rlTopListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SpecialArticleActivity.this, SpecialArticleListActivity.class));
                finish();
            }
        });

        if (mParentId == STServiceData.PROD_ID_1YUANQUAN)
        {
            rlTopListView.setVisibility(View.GONE);
        }

        String strMainCat = getIntent().getStringExtra("MainCatName");
        String strSubCat = getIntent().getStringExtra("SubCatName");

        if (strMainCat == null || strSubCat == null || strMainCat.isEmpty() || strSubCat.isEmpty())
        {
            RelativeLayout rlHeader = (RelativeLayout)findViewById(R.id.rlHeader);
            rlHeader.setVisibility(View.GONE);
        }
        else
        {
            TextView txtMainCat = (TextView)findViewById(R.id.txtMainCategory);
            TextView txtSubCat = (TextView)findViewById(R.id.txtSubCategory);

            txtMainCat.setText(strMainCat);
            txtSubCat.setText(strSubCat);
        }

        //////////////////////////////////////////////////////////////////////////////////
        // search image button
        ImageView findImage = (ImageView) findViewById(R.id.imageView);
        findImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFind();
            }
        });

        Button btnGoGoods = (Button) findViewById(R.id.btnGoGoods);
        btnGoGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonFunc.GoToShopCart(SpecialArticleActivity.this);
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
        mArticleArray.clear();
        nCurPageNumber = 1;
        if (mParentId == STServiceData.PROD_ID_1YUANQUAN)
        {
            mSearchMode = 2;
            callGetYiYuanQuanOfKeyWord(mKeyword, nCurPageNumber);
        }
        else
        {
            mSearchMode = 0;
            callGetProductOfKeyword(mKeyword, nCurPageNumber);
        }

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
//        RelativeLayout rlBottom_Home = (RelativeLayout) findViewById(R.id.rlBottom_Home);
//        rlBottom_Home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onImgbtnHomeClicked();
//            }
//        });

        RelativeLayout rlBootom_Back = (RelativeLayout) findViewById(R.id.rlBottom_BackArraw);
        rlBootom_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgbtnBackClicked();
            }
        });

//        RelativeLayout rlBottom_MainMenu = (RelativeLayout) findViewById(R.id.rlBottom_MainMenu);
//        rlBottom_MainMenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onImgbtnMainMenuClicked();
//            }
//        });

        RelativeLayout rlBottom_Packet = (RelativeLayout) findViewById(R.id.rlBottom_Packet);
        rlBottom_Packet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgbtnCartClicked();
            }
        });

//        RelativeLayout rlBottom_PersonInfo = (RelativeLayout) findViewById(R.id.rlBottom_PersonInfo);
//        rlBottom_PersonInfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onImgbtnAccountClicked();
//            }
//        });
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
        mAdapter.setParentId(mParentId);
        mAdapter.notifyDataSetChanged();

        // Call onRefreshComplete when the list has been refreshed.
        mPullRefreshList.onRefreshComplete();
    }


//    private class GetDataTask extends AsyncTask<Void, Void, ArrayList<STSpecialArticleInfo>> {
//
//        @Override
//        protected ArrayList<STSpecialArticleInfo> doInBackground(Void... params) {
//            // Simulates a background job.
//            try {
//                nCurPageNumber = nCurPageNumber + 1;
//
//                ArrayList<STSpecialArticleInfo> articleInfo = new ArrayList<STSpecialArticleInfo>();
//
//                // Have to add 10 row datas to the total list variable and refresh the adapter of the pulltorefreshlistview.
//                STSpecialArticleInfo oneRowData = new STSpecialArticleInfo();
//                articleInfo.add(oneRowData);
//
//                return articleInfo;
//            } catch (Exception e) {
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<STSpecialArticleInfo> result) {
//            if (result != null)
//            {
//                int nBufSize = result.size();
//                if (nBufSize == ITEMS_REFRESH_COUNT)
//                    bexistNext = true;
//                else
//                    bexistNext = false;
//
//                for (int i = 0; i < result.size(); i++)
//                    mArticleArray.add(result.get(i));
//
//                mAdapter.notifyDataSetChanged();
//
//                // Call onRefreshComplete when the list has been refreshed.
//                mPullRefreshList.onRefreshComplete();
//            }
//
//            super.onPostExecute(result);
//        }
//    }


    /////////////////////////////////////// Service Relation //////////////////////////////////////
    /**
     * Call GetProducts service
     * Get normal product list
     */
    private void callGetProducts(int pageno)
    {
        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = STServiceData.MSG_SUCCESS;

                progDialog.dismiss();

                retMsg = CommMgr.commService.parseGetProducts(object, mArticleArray);
                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    result = STServiceData.ERR_SUCCESS;
                    return;
                }
                else
                {
                    progDialog.dismiss();
                    result = STServiceData.ERR_EXCEPTION;
                }

                GlobalData.showToast(SpecialArticleActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                // update ui
                updateUI();

                if (result == STServiceData.ERR_FAIL)
                {
                    progDialog.dismiss();
                    GlobalData.showToast(SpecialArticleActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                SpecialArticleActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.GetProducts(mParentId, IMG_WIDTH, IMG_HEIGHT, nCurPageNumber, handler);

        return;
    }

    /**
     * find products by keyword
     * @param keyword [in], keyword to be find
     * @param pageno [in], current page number
     */
    private void callGetProductOfKeyword(String keyword, int pageno)
    {
        handler1 = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = STServiceData.MSG_SUCCESS;

                progDialog.dismiss();

                retMsg = CommMgr.commService.parseGetProducts(object, mArticleArray);
                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    result = STServiceData.ERR_SUCCESS;
                    return;
                }
                else
                {
                    progDialog.dismiss();
                    result = STServiceData.ERR_EXCEPTION;
                }

                GlobalData.showToast(SpecialArticleActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                // update ui
                updateUI();

                if (result == STServiceData.ERR_FAIL)
                {
                    progDialog.dismiss();
                    GlobalData.showToast(SpecialArticleActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                SpecialArticleActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.GetProductOfKeyword(keyword, IMG_HEIGHT, pageno, handler1);

        return;
    }

    /**
     * find yiyuan products to be find
     * @param keyword [in], keyword of yiyuan
     * @param pageno [in], current page
     */
    private void callGetYiYuanQuanOfKeyWord(String keyword, int pageno)
    {
        handler1 = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = STServiceData.MSG_SUCCESS;

                progDialog.dismiss();

                retMsg = CommMgr.commService.parseGetProducts(object, mArticleArray);
                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    result = STServiceData.ERR_SUCCESS;
                    return;
                }
                else
                {
                    progDialog.dismiss();
                    result = STServiceData.ERR_EXCEPTION;
                }

                GlobalData.showToast(SpecialArticleActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                // update ui
                updateUI();

                if (result == STServiceData.ERR_FAIL)
                {
                    progDialog.dismiss();
                    GlobalData.showToast(SpecialArticleActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                SpecialArticleActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.GetYiYuanQuanOfKeyWord(keyword, IMG_WIDTH, pageno, handler1);

        return;
    }
}
