package com.damytech.yilebang.HuiYuanZhongXin;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import com.damytech.CommService.CommMgr;
import com.damytech.Global.GlobalData;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.STData.STOrderByKeyword;
import com.damytech.STData.STServiceData;
import com.damytech.Utils.PullToRefreshBase;
import com.damytech.Utils.PullToRefreshListView;
import com.damytech.Utils.ResolutionSet;
import com.damytech.yilebang.MyActivity;
import com.damytech.yilebang.R;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-22
 * Time: 下午7:26
 * To change this template use File | Settings | File Templates.
 */
public class DingDanZhuangTaiChaXunActivity extends MyActivity {
    // UI control variables
    RelativeLayout  m_rlMain;
    RelativeLayout  m_rlHeader;
    ImageButton  m_imgbtnRefresh;
    PullToRefreshListView m_lstOrders;
    EditText m_edtOrderNo;
    Button m_btnSearch;
    RelativeLayout m_rlFooter;
    ImageButton  m_imgbtnBack;
    ImageButton m_imgbtnAccount;

    ArrayList<STOrderByKeyword> m_arrOrdersByKeyword = new ArrayList<STOrderByKeyword>(0);
    OrderByKeywordListAdapter  mAdapter = null;

    String  m_szKeyword = "";
    int  m_nPageCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.huiyuanzhongxin_dingdanzhuangtaichaxun);

        ResolutionSet._instance.iterateChild(findViewById(R.id.rlMain));

        getControlVariables ();

        connectSignalHandlers();

        loadInitialData();
    }

    private void getControlVariables () {
        m_rlMain = (RelativeLayout) findViewById(R.id.rlMain);
        m_rlHeader = (RelativeLayout) findViewById(R.id.rlHeader);
        m_imgbtnRefresh = (ImageButton) findViewById(R.id.imgbtnRefresh);
        m_lstOrders = (PullToRefreshListView) findViewById(R.id.lstOrders);
        m_edtOrderNo = (EditText) findViewById(R.id.edtOrderNo);
        m_btnSearch = (Button) findViewById(R.id.btnSearch);
        m_rlFooter = (RelativeLayout) findViewById(R.id.rlFooter);
        m_imgbtnBack = (ImageButton) findViewById(R.id.imgbtnBack);
        m_imgbtnAccount = (ImageButton) findViewById(R.id.imgbtnAccount);
    }

    private void connectSignalHandlers () {
        m_imgbtnRefresh.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadInitialData();
            }
        });
        m_btnSearch.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v) {
                m_szKeyword = m_edtOrderNo.getText().toString();
                if ( m_szKeyword.isEmpty() ) {
                    GlobalData.showToast(DingDanZhuangTaiChaXunActivity.this, getString(R.string.HuiYuanZhongXin_DingDanChaXun_OrderNoShouldNotBeEmpty));
                    return;
                }

                m_nPageCount = 1;
                m_arrOrdersByKeyword.clear();
                callGetOrderInfoOfKeyword(m_nPageCount);
            }
        });
        m_imgbtnBack.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgbtnBackClicked();
            }
        });

        m_lstOrders.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        m_lstOrders.setOnRefreshListener( new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                // Do work to refresh the list here.
                m_nPageCount = m_nPageCount + 1;
                callGetOrderInfoOfKeyword(m_nPageCount);
                // new GetDataTask().execute();
            }
        });
    }

    private void loadInitialData () {
        m_nPageCount = 1;
        m_arrOrdersByKeyword.clear();
        callGetOrderInfoOfKeyword(m_nPageCount);
    }

    private void updateUI () {
        fillOrdersList();

        // Call onRefreshComplete when the list has been refreshed.
        m_lstOrders.onRefreshComplete();
    }

    private void fillOrdersList () {
        if ( mAdapter == null ) {
            mAdapter = new OrderByKeywordListAdapter(DingDanZhuangTaiChaXunActivity.this, this.getApplicationContext());
            mAdapter.setData(m_arrOrdersByKeyword);

            ListView mRealListView = m_lstOrders.getRefreshableView();
            registerForContextMenu(mRealListView);

            mRealListView.setDivider(new ColorDrawable(Color.LTGRAY));
            mRealListView.setCacheColorHint(Color.parseColor("#FFF1F1F1"));
            mRealListView.setDividerHeight(2);
            m_lstOrders.setAdapter(mAdapter);
        } else {
            mAdapter.setData(m_arrOrdersByKeyword);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void callGetOrderInfoOfKeyword ( int pageno ) {
        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = "";

                progDialog.dismiss();

                retMsg = CommMgr.commService.parseGetOrderInfoOfKeyword(object, m_arrOrdersByKeyword);
                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    result = STServiceData.ERR_SUCCESS;

                    updateUI();
                    return;
                }
                else
                {
                    result = STServiceData.ERR_EXCEPTION;
                }

                GlobalData.showToast(DingDanZhuangTaiChaXunActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                progDialog.dismiss();

                if (result == STServiceData.ERR_FAIL)
                {
                    GlobalData.showToast(DingDanZhuangTaiChaXunActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                DingDanZhuangTaiChaXunActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.GetOrderInfoOfKeyword(GlobalData.token, m_szKeyword, pageno, handler);
    }
}
