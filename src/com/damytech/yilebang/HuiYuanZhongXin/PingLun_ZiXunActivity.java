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
import android.widget.TextView;
import com.damytech.CommService.CommMgr;
import com.damytech.Global.GlobalData;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.STData.STConsultingInfo;
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
 * Time: 下午7:30
 * To change this template use File | Settings | File Templates.
 */
public class PingLun_ZiXunActivity extends MyActivity {
    // UI control variables
    RelativeLayout  m_rlMain;
    RelativeLayout  m_rlHeader;
    TextView m_txtComment;
    PullToRefreshListView m_lstConsultings;
    RelativeLayout m_rlFooter;
    ImageButton  m_imgbtnBack;
    ImageButton m_imgbtnAccount;

    ArrayList<STConsultingInfo> m_arrConsultings = new ArrayList<STConsultingInfo>(0);
    ConsultingListAdapter  mAdapter;

    int  m_nPageCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.huiyuanzhongxin_pinglun_zixun);

        ResolutionSet._instance.iterateChild(findViewById(R.id.rlMain));

        getControlVariables ();

        connectSignalHandlers();

        loadInitialData();
    }

    private void getControlVariables () {
        m_rlMain = (RelativeLayout) findViewById(R.id.rlMain);
        m_rlHeader = (RelativeLayout) findViewById(R.id.rlHeader);
        m_txtComment = (TextView) findViewById(R.id.txtComment);
        m_lstConsultings = (PullToRefreshListView) findViewById(R.id.lstConsultings);
        m_rlFooter = (RelativeLayout) findViewById(R.id.rlFooter);
        m_imgbtnBack = (ImageButton) findViewById(R.id.imgbtnBack);
        m_imgbtnAccount = (ImageButton) findViewById(R.id.imgbtnAccount);
    }

    private void connectSignalHandlers () {
        m_imgbtnBack.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgbtnBackClicked();
            }
        });

        m_lstConsultings.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        m_lstConsultings.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                //To change body of implemented methods use File | Settings | File Templates.
                // Do work to refresh the list here.
                m_nPageCount = m_nPageCount + 1;
                callGetConsultProducts(m_nPageCount);
                // new GetDataTask().execute();
            }
        });

        m_txtComment.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPingLun ();
            }
        });
    }

    private void goToPingLun () {
        // FIXME: 131209 should i go back to PingLun, or create a new PingLun?
        finish();
    }

    private void loadInitialData () {
        m_nPageCount = 1;
        m_arrConsultings.clear();
        callGetConsultProducts(m_nPageCount);
    }

    private void updateUI () {
        fillConsultingList();

        // Call onRefreshComplete when the list has been refreshed.
        m_lstConsultings.onRefreshComplete();

        // check data count ( display error message )
        RelativeLayout rlEmpty = (RelativeLayout) findViewById(R.id.rlEmptyMsg);
        if (m_arrConsultings.size() <= 0)
        {
            rlEmpty.setVisibility(View.VISIBLE);
        }
        else
        {
            rlEmpty.setVisibility(View.GONE);
        }
    }

    private void fillConsultingList () {
        if ( mAdapter == null ) {
            mAdapter = new ConsultingListAdapter(PingLun_ZiXunActivity.this, this.getApplicationContext());
            mAdapter.setData(m_arrConsultings);

            ListView mRealListView = m_lstConsultings.getRefreshableView();
            registerForContextMenu(mRealListView);

            mRealListView.setDivider(new ColorDrawable(Color.LTGRAY));
            mRealListView.setCacheColorHint(Color.parseColor("#FFF1F1F1"));
            mRealListView.setDividerHeight(2);
            m_lstConsultings.setAdapter(mAdapter);
        } else {
//            mAdapter.setData(m_arrFavoriteItems);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void callGetConsultProducts ( int pageno ) {
        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = "";

                progDialog.dismiss();

                retMsg = CommMgr.commService.parseGetConsultProducts(object, m_arrConsultings);
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

                GlobalData.showToast(PingLun_ZiXunActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                progDialog.dismiss();

                if (result == STServiceData.ERR_FAIL)
                {
                    GlobalData.showToast(PingLun_ZiXunActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                PingLun_ZiXunActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.GetConsultProducts(GlobalData.token, pageno, handler);
    }
}
