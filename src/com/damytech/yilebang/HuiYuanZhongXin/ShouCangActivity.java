package com.damytech.yilebang.HuiYuanZhongXin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.damytech.CommService.CommMgr;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.STData.STFavoriteInfo;
import com.damytech.STData.STServiceData;
import com.damytech.Global.GlobalData;
import com.damytech.Utils.PullToRefreshBase;
import com.damytech.Utils.PullToRefreshListView;
import com.damytech.Utils.ResolutionSet;
import com.damytech.yilebang.MyActivity;
import com.damytech.yilebang.MyAlertDialog;
import com.damytech.yilebang.R;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-18
 * Time: 上午10:32
 * To change this template use File | Settings | File Templates.
 */
public class ShouCangActivity extends MyActivity {
    // UI control variables
    RelativeLayout  m_rlMain;
    RelativeLayout  m_rlHeader;
    ImageButton  m_imgbtnRefresh;
    PullToRefreshListView m_lstFavorites;
    RelativeLayout m_rlFooter;
    ImageButton  m_imgbtnBack;
    ImageButton  m_imgbtnDelete;
    ImageButton m_imgbtnAccount;

    ArrayList<STFavoriteInfo>  m_arrFavoriteItems = new ArrayList<STFavoriteInfo>(0);
    FavoriteListAdapter  mAdapter;

    int  m_nPageCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.huiyuanzhongxin_shoucang);

        ResolutionSet._instance.iterateChild(findViewById(R.id.rlMain));

        getControlVariables ();

        connectSignalHandlers();

        loadInitialData();
    }

    private void getControlVariables () {
        m_rlMain = (RelativeLayout) findViewById(R.id.rlMain);
        m_rlHeader = (RelativeLayout) findViewById(R.id.rlHeader);
        m_imgbtnRefresh = (ImageButton) findViewById(R.id.imgbtnRefresh);
        m_lstFavorites = (PullToRefreshListView) findViewById(R.id.lstFavorites);
        m_rlFooter = (RelativeLayout) findViewById(R.id.rlFooter);
        m_imgbtnBack = (ImageButton) findViewById(R.id.imgbtnBack);
        m_imgbtnDelete = (ImageButton) findViewById(R.id.imgbtnDelete);
        m_imgbtnAccount = (ImageButton) findViewById(R.id.imgbtnAccount);
    }

    private void connectSignalHandlers () {
        m_imgbtnRefresh.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadInitialData();
            }
        });

        m_imgbtnDelete.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  selectedIds = mAdapter.getSelectedIds();

                if ( selectedIds.isEmpty() ) {
                    GlobalData.showToast(ShouCangActivity.this, getString(R.string.no_selected_item));
                    return;
                } else {
                    new AlertDialog.Builder(ShouCangActivity.this)
                            .setTitle(getString(R.string.HuiYuanZhongXin_ShouCang_Msg_Title))
                            .setMessage(getString(R.string.HuiYuanZhongXin_ShouCang_Msg_Contents))
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    callDeleteFavoriteItems();
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            }
        });

        m_imgbtnBack.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgbtnBackClicked();
            }
        });

        m_lstFavorites.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        m_lstFavorites.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                //To change body of implemented methods use File | Settings | File Templates.
                // Do work to refresh the list here.
                m_nPageCount = m_nPageCount + 1;
                callGetFavoriteItems(m_nPageCount);
                // new GetDataTask().execute();
            }
        });
    }

    private void loadInitialData () {
        m_nPageCount = 1;
        m_arrFavoriteItems.clear();
        callGetFavoriteItems(m_nPageCount);
    }

    private void updateUI () {
        fillFavoriteItemList ();

        // Call onRefreshComplete when the list has been refreshed.
        m_lstFavorites.onRefreshComplete();
    }

    private void fillFavoriteItemList () {
        if ( mAdapter == null ) {
            mAdapter = new FavoriteListAdapter(ShouCangActivity.this, this.getApplicationContext());
            mAdapter.setData(m_arrFavoriteItems);

            ListView mRealListView = m_lstFavorites.getRefreshableView();
            registerForContextMenu(mRealListView);

            mRealListView.setDivider(new ColorDrawable(Color.LTGRAY));
            mRealListView.setCacheColorHint(Color.parseColor("#FFF1F1F1"));
            mRealListView.setDividerHeight(2);
            m_lstFavorites.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    private void callGetFavoriteItems ( int pageno ) {
        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = "";

                retMsg = CommMgr.commService.parseGetFavoriteItems(object, m_arrFavoriteItems);
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

                GlobalData.showToast(ShouCangActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                progDialog.dismiss();

                if (result == STServiceData.ERR_FAIL)
                {
                    GlobalData.showToast(ShouCangActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }
        };

        progDialog = ProgressDialog.show(
                ShouCangActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        int  desiredWidth = (int) (ResolutionSet.fXpro * 110);
        int  desiredHeight = (int) (ResolutionSet.fYpro * 110);
        CommMgr.commService.GetCollectionProducts(GlobalData.token, desiredWidth, desiredHeight, pageno, handler);
    }

    private void callDeleteFavoriteItems () {
        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = "";

                progDialog.dismiss();

                retMsg = CommMgr.commService.parseDeleteCollectedProducts(object);
                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    result = STServiceData.ERR_SUCCESS;

                    loadInitialData();
                    return;
                }
                else
                {
                    result = STServiceData.ERR_EXCEPTION;
                }

                GlobalData.showToast(ShouCangActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                progDialog.dismiss();

                if (result == STServiceData.ERR_FAIL)
                {
                    GlobalData.showToast(ShouCangActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                ShouCangActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        String  selectedIds = mAdapter.getSelectedIds();
        CommMgr.commService.DeleteCollectedProducts(GlobalData.token, selectedIds, handler);
    }
}
