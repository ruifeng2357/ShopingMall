package com.damytech.yilebang.HuiYuanZhongXin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.damytech.CommService.CommMgr;
import com.damytech.Global.GlobalData;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.STData.STOrderByState;
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
public class DingDanActivity extends MyActivity {
    // UI control variables
    RelativeLayout  m_rlMain;
    RelativeLayout  m_rlHeader;
    ImageButton  m_imgbtnRefresh;
    RelativeLayout  m_rlBody;
    TextView[]  m_txtTabs = new TextView[N];
    PullToRefreshListView[] m_lstOrderLists = new PullToRefreshListView[N];
    RelativeLayout m_rlFooter;
    ImageButton m_imgbtnBack;
    ImageButton  m_imgbtnAccount;

    int  m_nCurTab = 0;
    final static int  N = 4; // state count

    int[]  m_nPageCounts = new int[4];
    private OrderByStateListAdapter[] mAdapters = new OrderByStateListAdapter[N];
    ArrayList<STOrderByState>[] m_arrOrderListsByState = new ArrayList[N];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.huiyuanzhongxin_dingdan);

        ResolutionSet._instance.iterateChild(findViewById(R.id.rlMain));

        getControlVariables ();

        connectSignalHandlers();

        for ( int i=0; i<N; i++ ) {
            m_arrOrderListsByState[i] = new ArrayList<STOrderByState>(0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // reload data
        loadInitialData();
    }

    private void getControlVariables () {
        m_rlMain = (RelativeLayout) findViewById(R.id.rlMain);
        m_rlHeader = (RelativeLayout) findViewById(R.id.rlHeader);
        m_imgbtnRefresh = (ImageButton) findViewById(R.id.imgbtnRefresh);
        m_rlBody = (RelativeLayout) findViewById(R.id.rlBody);
        m_txtTabs[0] = (TextView) findViewById(R.id.txtTab1);
        m_txtTabs[1] = (TextView) findViewById(R.id.txtTab2);
        m_txtTabs[2] = (TextView) findViewById(R.id.txtTab3);
        m_txtTabs[3] = (TextView) findViewById(R.id.txtTab4);
        m_lstOrderLists[0] = (PullToRefreshListView) findViewById(R.id.lstOrders1);
        m_lstOrderLists[1] = (PullToRefreshListView) findViewById(R.id.lstOrders2);
        m_lstOrderLists[2] = (PullToRefreshListView) findViewById(R.id.lstOrders3);
        m_lstOrderLists[3] = (PullToRefreshListView) findViewById(R.id.lstOrders4);
        m_rlFooter = (RelativeLayout) findViewById(R.id.rlFooter);
        m_imgbtnBack = (ImageButton) findViewById(R.id.imgbtnBack);
        m_imgbtnAccount = (ImageButton) findViewById(R.id.imgbtnAccount);
    }

    private void connectSignalHandlers () {
        m_imgbtnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadInitialData();
            }
        });
        m_imgbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgbtnBackClicked();
            }
        });

        m_txtTabs[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToTab(0);
            }
        });
        m_lstOrderLists[0].setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        m_lstOrderLists[0].setOnRefreshListener( new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                // Do work to refresh the list here.
                m_nPageCounts[0] = m_nPageCounts[0] + 1;
                callGetOrderInfoOfState(0, m_nPageCounts[0]);
                // new GetDataTask().execute();
            }
        });

        m_txtTabs[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToTab(1);
            }
        });
        m_lstOrderLists[1].setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        m_lstOrderLists[1].setOnRefreshListener( new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                // Do work to refresh the list here.
                m_nPageCounts[1] = m_nPageCounts[1] + 1;
                callGetOrderInfoOfState(1, m_nPageCounts[1]);
                // new GetDataTask().execute();
            }
        });

        m_txtTabs[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToTab(2);
            }
        });
        m_lstOrderLists[2].setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        m_lstOrderLists[2].setOnRefreshListener( new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                // Do work to refresh the list here.
                m_nPageCounts[2] = m_nPageCounts[2] + 1;
                callGetOrderInfoOfState(2, m_nPageCounts[2]);
                // new GetDataTask().execute();
            }
        });

        m_txtTabs[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToTab(3);
            }
        });
        m_lstOrderLists[3].setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        m_lstOrderLists[3].setOnRefreshListener( new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                // Do work to refresh the list here.
                m_nPageCounts[3] = m_nPageCounts[3] + 1;
                callGetOrderInfoOfState(3, m_nPageCounts[3]);
                // new GetDataTask().execute();
            }
        });

        for ( int i=0; i<N; i++ ) {
            ListView realView = m_lstOrderLists[i].getRefreshableView();
            realView.setDivider(new ColorDrawable(Color.LTGRAY));
            realView.setCacheColorHint(Color.parseColor("#FFF1F1F1"));
            realView.setDividerHeight(2);
        }
    }

    public void loadInitialData () {
        progDialog = ProgressDialog.show(
                DingDanActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        for ( int i=0; i<N; i++ ) {
            m_nPageCounts[i] = 1;
            m_arrOrderListsByState[i].clear();
            callGetOrderInfoOfState(i, m_nPageCounts[i]);
        }
    }

    private void updateUI () {
        for ( int i=0; i<N; i++ ) {
            fillOrdersList(i);

            // Call onRefreshComplete when the list has been refreshed.
            m_lstOrderLists[i].onRefreshComplete();
        }
    }

    private void changeToTab ( int tab ) {
        if ( tab<0 || tab>=4 )
            return;

        m_nCurTab = tab;

        for ( int i=0; i<N; i++ ) {
            m_lstOrderLists[i].setVisibility(View.GONE);
            m_txtTabs[i].setBackgroundResource(R.drawable.tab_inactive);
            m_txtTabs[i].setTextColor(Color.parseColor("#606060"));
        }

        m_lstOrderLists[tab].setVisibility(View.VISIBLE);
        m_txtTabs[tab].setBackgroundResource(R.drawable.tab_active);
        m_txtTabs[tab].setTextColor(Color.parseColor("#000000"));
    }

    private void fillOrdersList (int tab) {
        int state = tab + 1;
        
        if ( mAdapters[tab] == null ) {
            mAdapters[tab] = new OrderByStateListAdapter(DingDanActivity.this, 0, m_arrOrderListsByState[tab], state);
            m_lstOrderLists[tab].setAdapter(mAdapters[tab]);
        } else {
            mAdapters[tab].notifyDataSetChanged();
        }
    }

    private void callGetOrderInfoOfState ( final int tab, int pageno ) {
        int state = tab + 1;

        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = "";

                if ( tab == N-1 ) { // if it is the last data retrieval
                    progDialog.dismiss();
                }

                retMsg = CommMgr.commService.parseGetOrderInfoOfState(object, m_arrOrderListsByState[tab]);
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

                GlobalData.showToast(DingDanActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                progDialog.dismiss();

                if (result == STServiceData.ERR_FAIL)
                {
                    GlobalData.showToast(DingDanActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        CommMgr.commService.GetOrderInfoOfState(GlobalData.token, state, pageno, handler);
    }
}
