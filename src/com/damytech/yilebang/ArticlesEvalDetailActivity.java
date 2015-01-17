package com.damytech.yilebang;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.damytech.CommService.CommMgr;
import com.damytech.Global.GlobalData;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.STData.ProductDetail.STProdComment;
import com.damytech.STData.STServiceData;
import com.damytech.Utils.*;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: KimChg
 * Date: 13-11-22
 * Time: 上午10:04
 * To change this template use File | Settings | File Templates.
 */
public class ArticlesEvalDetailActivity extends MyActivity {
    private ArticlesEvalItemAdapter mEvalAdapter;
    private PullToRefreshListView mPullRefreshlistEval;
    private ListView mRealListView;

    private ArrayList<STProdComment> mEvalInfoArray;
    private int nCurPageNumber = 1;
    public boolean bexistNext = true;

    final int ITEMS_REFRESH_COUNT = 10;

    private JsonHttpResponseHandler handler;

    private int mProdId = 0;
    private String mProdName = "";

    private double mCommonRate = 0.0f;
    private double mGoodRate = 0.0f;
    private double mMediumRate = 0.0f;
    private double mBadRate = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.articlesevaldetail);

        mProdId = getIntent().getExtras().getInt("ProductId");
        mProdName = getIntent().getExtras().getString("ProductName");
        mCommonRate = getIntent().getExtras().getDouble("CommonRate");
        mGoodRate = getIntent().getExtras().getDouble("GoodRate");
        mMediumRate = getIntent().getExtras().getDouble("MediumRate");
        mBadRate = getIntent().getExtras().getDouble("BadRate");

        // initialize
        initControls();
        callGetProductComments(mProdId, nCurPageNumber);
        ChangeProgress((int)(mCommonRate), (int)mGoodRate, (int)mMediumRate, (int)mBadRate);
    }

    /**
     * Initialize all controls
     */
    private void initControls()
    {
        ResolutionSet._instance.iterateChild(findViewById(R.id.rlArticlesEvalDetailMain));

        TextView lblTitle = (TextView) findViewById(R.id.txtTitle);
        lblTitle.setText(mProdName);

        mEvalInfoArray = new ArrayList<STProdComment>();
        ///////////////////////////////////////////////////////////////////////////////////
        // initialize list view (PullToRefreshListView)
        mPullRefreshlistEval = (PullToRefreshListView)findViewById(R.id.listEvalsView);
        mPullRefreshlistEval.setMode(PullToRefreshBase.Mode.PULL_FROM_END);

        // Set a listener to be invoked when the list should be refreshed.
        mPullRefreshlistEval.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                // Do work to refresh the list here.
                nCurPageNumber = nCurPageNumber + 1;
                callGetProductComments(mProdId, nCurPageNumber);
                // new GetDataTask().execute();
            }
        });

        mRealListView = mPullRefreshlistEval.getRefreshableView();
        registerForContextMenu(mRealListView);

        mRealListView.setDivider(new ColorDrawable(Color.TRANSPARENT));
        mRealListView.setCacheColorHint(Color.parseColor("#FFF1F1F1"));
        mRealListView.setDividerHeight(2);

        mEvalAdapter = new ArticlesEvalItemAdapter(ArticlesEvalDetailActivity.this, this.getApplicationContext());
        mEvalAdapter.setData(mEvalInfoArray);

        mPullRefreshlistEval.setAdapter(mEvalAdapter);

        initMainMenu();
    }

    private void initMainMenu()
    {
        RelativeLayout rlBack = (RelativeLayout) findViewById(R.id.rlBottom_BackArraw);
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgbtnBackClicked();
            }
        });

    }

    /**
     * Update UI using service data
     */
    private void updateUI()
    {
        mEvalAdapter.notifyDataSetChanged();
        // Call onRefreshComplete when the list has been refreshed.
        mPullRefreshlistEval.onRefreshComplete();
    }

    private void ChangeProgress(int commonpro, int highpro, int mediumpro, int lowpro)
    {
        int proWidth = 200;

        commonpro = (commonpro > 100) ? 100 : commonpro;
        commonpro = (commonpro < 0) ? 0 : commonpro;
        highpro = (highpro > 100) ? 100 : highpro;
        highpro = (highpro < 0) ? 0 : highpro;
        mediumpro = (mediumpro > 100) ? 100 : mediumpro;
        mediumpro = (mediumpro < 0) ? 0 : mediumpro;
        lowpro = (lowpro > 100) ? 100 : lowpro;
        lowpro = (lowpro < 0) ? 0 : lowpro;

        TextView txtCommon = (TextView) findViewById(R.id.txtEvalGoodPro);
        txtCommon.setText(Integer.toString(commonpro) + "%");

        RelativeLayout rlHighBar = (RelativeLayout)findViewById(R.id.rlHaoPingBar);
        RelativeLayout rlMediumBar = (RelativeLayout)findViewById(R.id.rlZhongPingBar);
        RelativeLayout rlSmallBar = (RelativeLayout)findViewById(R.id.rlChaPingBar);

        RelativeLayout.LayoutParams layoutHighBar = (RelativeLayout.LayoutParams)rlHighBar.getLayoutParams();
        RelativeLayout.LayoutParams layoutMediumBar = (RelativeLayout.LayoutParams)rlMediumBar.getLayoutParams();
        RelativeLayout.LayoutParams layoutSmalBar = (RelativeLayout.LayoutParams)rlSmallBar.getLayoutParams();

        layoutHighBar.width = (int)(proWidth * highpro * ResolutionSet.fXpro / 100 );
        layoutMediumBar.width = (int)(proWidth * mediumpro * ResolutionSet.fXpro / 100 );
        layoutSmalBar.width = (int)(proWidth * lowpro * ResolutionSet.fXpro / 100 );

        rlHighBar.setLayoutParams(layoutHighBar);
        rlHighBar.setBackgroundColor(Color.RED);
        rlMediumBar.setLayoutParams(layoutMediumBar);
        rlMediumBar.setBackgroundColor(Color.RED);
        rlSmallBar.setLayoutParams(layoutSmalBar);
        rlSmallBar.setBackgroundColor(Color.RED);
    }
//
//
//    private class GetDataTask extends AsyncTask<Void, Void, ArrayList<STProdComment>> {
//
//        @Override
//        protected ArrayList<STProdComment> doInBackground(Void... params) {
//            // Simulates a background job.
//            try {
//                nCurPageNumber = nCurPageNumber + 1;
//
//                ArrayList<STProdComment> articleInfo = new ArrayList<STProdComment>();
//
//                // Have to add 10 row datas to the total list variable and refresh the adapter of the pulltorefreshlistview.
//                STProdComment oneRowData = new STProdComment();
//                oneRowData.rate = 5;
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
//        protected void onPostExecute(ArrayList<STProdComment> result) {
//            if (result != null)
//            {
//                int nBufSize = result.size();
//                if (nBufSize == ITEMS_REFRESH_COUNT)
//                    bexistNext = true;
//                else
//                    bexistNext = false;
//
//                for (int i = 0; i < result.size(); i++)
//                    mEvalInfoArray.add(result.get(i));
//
//                mEvalAdapter.notifyDataSetChanged();
//
//                // Call onRefreshComplete when the list has been refreshed.
//                mPullRefreshlistEval.onRefreshComplete();
//            }
//
//            super.onPostExecute(result);
//        }
//    }

    private void callGetProductComments(int prodid, int pageno)
    {
        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = STServiceData.MSG_SUCCESS;

                retMsg = CommMgr.commService.parseProductComments(object, mEvalInfoArray);
                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    result = STServiceData.ERR_SUCCESS;
                    // update ui by using data
                    updateUI();
                    return;
                }
                else
                {
                    result = STServiceData.ERR_EXCEPTION;
                }

                GlobalData.showToast(ArticlesEvalDetailActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                if (result == STServiceData.ERR_FAIL)
                {
                    GlobalData.showToast(ArticlesEvalDetailActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        CommMgr.commService.GetProductComments(prodid, pageno, handler);

        return;
    }
}
