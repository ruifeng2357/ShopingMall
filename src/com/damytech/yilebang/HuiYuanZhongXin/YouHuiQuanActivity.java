package com.damytech.yilebang.HuiYuanZhongXin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.damytech.CommService.CommMgr;
import com.damytech.Global.GlobalData;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.STData.STCouponA;
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
 * Time: 下午7:32
 * To change this template use File | Settings | File Templates.
 */
public class YouHuiQuanActivity extends MyActivity {
    // UI control variables
    RelativeLayout  m_rlMain;
    ImageButton  m_imgbtnRefresh;
    PullToRefreshListView m_lstCoupons;
    RelativeLayout m_rlFooter;
    ImageButton  m_imgbtnBack;
    ImageButton m_imgbtnAccount;

    ArrayList<STCouponA> m_arrCoupons = new ArrayList<STCouponA>(0);
    CouponListAdapter  mAdapter;

    int  m_nPageCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.huiyuanzhongxin_youhuiquan);

        ResolutionSet._instance.iterateChild(findViewById(R.id.rlMain));

        getControlVariables ();

        connectSignalHandlers();

        loadInitialData();
    }

    private void getControlVariables () {
        m_rlMain = (RelativeLayout) findViewById(R.id.rlMain);
        m_imgbtnRefresh = (ImageButton) findViewById(R.id.imgbtnRefresh);
        m_lstCoupons = (PullToRefreshListView) findViewById(R.id.lstCoupons);
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
        m_imgbtnBack.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgbtnBackClicked();
            }
        });

        m_lstCoupons.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        m_lstCoupons.setOnRefreshListener( new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                // Do work to refresh the list here.
                m_nPageCount = m_nPageCount + 1;
                callGetCouponLogList(m_nPageCount);
                // new GetDataTask().execute();
            }
        });
    }

    private void loadInitialData () {
        m_nPageCount = 1;
        m_arrCoupons.clear();
        callGetCouponLogList(m_nPageCount);
    }

    private void updateUI () {
        fillOrdersList();

        // Call onRefreshComplete when the list has been refreshed.
        m_lstCoupons.onRefreshComplete();
    }

    private void fillOrdersList () {
        if ( mAdapter == null ) {
            mAdapter = new CouponListAdapter(YouHuiQuanActivity.this, this.getApplicationContext());
            mAdapter.setData(m_arrCoupons);

            ListView mRealListView = m_lstCoupons.getRefreshableView();
            registerForContextMenu(mRealListView);

            mRealListView.setDivider(new ColorDrawable(Color.LTGRAY));
            mRealListView.setCacheColorHint(Color.parseColor("#FFF1F1F1"));
            mRealListView.setDividerHeight(2);
            m_lstCoupons.setAdapter(mAdapter);
        } else {
            mAdapter.setData(m_arrCoupons);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void callGetCouponLogList ( int pageno ) {
        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = "";

                progDialog.dismiss();

                retMsg = CommMgr.commService.parseGetCouponLogList(object, m_arrCoupons);
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

                GlobalData.showToast(YouHuiQuanActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                progDialog.dismiss();

                if (result == STServiceData.ERR_FAIL)
                {
                    GlobalData.showToast(YouHuiQuanActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                YouHuiQuanActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.GetCouponLogList(GlobalData.token, (int)(200*ResolutionSet.fXpro), (int)(90*ResolutionSet.fYpro), pageno, handler);
    }
}
