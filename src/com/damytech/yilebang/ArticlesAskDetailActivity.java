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
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.STData.ProductDetail.STProdConsult;
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
 * Date: 13-11-22
 * Time: 上午10:39
 * To change this template use File | Settings | File Templates.
 */
public class ArticlesAskDetailActivity extends MyActivity {
    private ArticlesAskItemAdapter mAskAdapter;
    private PullToRefreshListView mPullRefreshlistAsk;
    private ListView mRealListView;

    private ArrayList<STProdConsult> mAskInfoArray;
    private int nCurPageNumber = 1;
    public boolean bexistNext = true;

    final int ITEMS_REFRESH_COUNT = 10;

    private JsonHttpResponseHandler handler;

    private int mProdId = 0;
    private String mProdName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.articlesaskdetail);

        mProdId = getIntent().getExtras().getInt("ProductId");

        // initialize
        initControls();
        callGetProductConsult(mProdId, nCurPageNumber);
    }


    /**
     * Initialize all controls
     */
    private void initControls()
    {
        ResolutionSet._instance.iterateChild(findViewById(R.id.rlArticlesAskDetailMain));

        TextView lblTitle = (TextView) findViewById(R.id.txtTitle);
        lblTitle.setText(mProdName);

        mAskInfoArray = new ArrayList<STProdConsult>();
        ///////////////////////////////////////////////////////////////////////////////////
        // initialize list view (PullToRefreshListView)
        mPullRefreshlistAsk = (PullToRefreshListView)findViewById(R.id.listAsksView);
        mPullRefreshlistAsk.setMode(PullToRefreshBase.Mode.PULL_FROM_END);

        // Set a listener to be invoked when the list should be refreshed.
        mPullRefreshlistAsk.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                // Do work to refresh the list here.
                nCurPageNumber = nCurPageNumber + 1;
                callGetProductConsult(mProdId, nCurPageNumber);
                // new GetDataTask().execute();
            }
        });

        mRealListView = mPullRefreshlistAsk.getRefreshableView();
        registerForContextMenu(mRealListView);


        mRealListView.setDivider(new ColorDrawable(Color.TRANSPARENT));
        mRealListView.setCacheColorHint(Color.parseColor("#FFF1F1F1"));
        mRealListView.setDividerHeight(2);

        mAskAdapter = new ArticlesAskItemAdapter(ArticlesAskDetailActivity.this, this.getApplicationContext());
        mAskAdapter.setData(mAskInfoArray);

        mPullRefreshlistAsk.setAdapter(mAskAdapter);

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
        mAskAdapter.notifyDataSetChanged();
        // Call onRefreshComplete when the list has been refreshed.
        mPullRefreshlistAsk.onRefreshComplete();
    }

//    private class GetDataTask extends AsyncTask<Void, Void, ArrayList<STProdConsult>> {
//
//        @Override
//        protected ArrayList<STProdConsult> doInBackground(Void... params) {
//            // Simulates a background job.
//            try {
//                nCurPageNumber = nCurPageNumber + 1;
//
//                ArrayList<STProdConsult> articleInfo = new ArrayList<STProdConsult>();
//
//                // Have to add 10 row datas to the total list variable and refresh the adapter of the pulltorefreshlistview.
//                STProdConsult oneRowData = new STProdConsult();
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
//        protected void onPostExecute(ArrayList<STProdConsult> result) {
//            if (result != null)
//            {
//                int nBufSize = result.size();
//                if (nBufSize == ITEMS_REFRESH_COUNT)
//                    bexistNext = true;
//                else
//                    bexistNext = false;
//
//                for (int i = 0; i < result.size(); i++)
//                    mAskInfoArray.add(result.get(i));
//
//                mAskAdapter.notifyDataSetChanged();
//
//                // Call onRefreshComplete when the list has been refreshed.
//                mPullRefreshlistAsk.onRefreshComplete();
//            }
//
//            super.onPostExecute(result);
//        }
//    }


    private void callGetProductConsult(int prodid, int pageno)
    {
        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = STServiceData.MSG_SUCCESS;

                retMsg = CommMgr.commService.parseProductConsults(object, mAskInfoArray);
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

                GlobalData.showToast(ArticlesAskDetailActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                if (result == STServiceData.ERR_FAIL)
                {
                    GlobalData.showToast(ArticlesAskDetailActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        CommMgr.commService.GetProductConsults(prodid, pageno, handler);

        return;
    }
}
