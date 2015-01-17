package com.damytech.yilebang.HuiYuanZhongXin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.damytech.STData.STReceiverInfo;
import com.damytech.STData.STServiceData;
import com.damytech.Utils.ResolutionSet;
import com.damytech.yilebang.MyActivity;
import com.damytech.yilebang.MyAlertDialog;
import com.damytech.yilebang.R;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-22
 * Time: 下午7:27
 * To change this template use File | Settings | File Templates.
 */
public class DiZhiGuanLiActivity extends MyActivity {
    // UI control variables
    RelativeLayout  m_rlMain;
    RelativeLayout  m_rlHeader;
    ImageButton  m_imgbtnRefresh;
    ListView m_lstReceivers;
    RelativeLayout m_rlFooter;
    ImageButton  m_imgbtnBack;
    ImageButton  m_imgbtnDelete;
    ImageButton m_imgbtnAccount;
    TextView m_txtNewReceiver;

    ArrayList<STReceiverInfo>  m_arrReceivers = new ArrayList<STReceiverInfo>(0);
    ReceiverListAdapter  mAdapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.huiyuanzhongxin_dizhiguanli);

        ResolutionSet._instance.iterateChild(findViewById(R.id.rlMain));

        getControlVariables ();

        connectSignalHandlers();

        //loadInitialData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadInitialData();
    }

    private void getControlVariables () {
        m_rlMain = (RelativeLayout) findViewById(R.id.rlMain);
        m_rlHeader = (RelativeLayout) findViewById(R.id.rlHeader);
        m_imgbtnRefresh = (ImageButton) findViewById(R.id.imgbtnRefresh);
        m_lstReceivers = (ListView) findViewById(R.id.lstReceivers);
        m_rlFooter = (RelativeLayout) findViewById(R.id.rlFooter);
        m_imgbtnBack = (ImageButton) findViewById(R.id.imgbtnBack);
        m_imgbtnDelete = (ImageButton) findViewById(R.id.imgbtnDelete);
        m_imgbtnAccount = (ImageButton) findViewById(R.id.imgbtnAccount);
        m_txtNewReceiver = (TextView) findViewById(R.id.txtNewReceiver);
    }

    private void connectSignalHandlers () {
        m_imgbtnBack.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgbtnBackClicked();
            }
        });
        m_imgbtnDelete.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  selectedIds = mAdapter.getSelectedIds();

                if ( selectedIds.isEmpty() ) {
                    GlobalData.showToast(DiZhiGuanLiActivity.this, getString(R.string.no_selected_item));
                    return;
                } else {
                    new AlertDialog.Builder(DiZhiGuanLiActivity.this)
                            .setTitle(getString(R.string.HuiYuanZhongXin_DiZhiGuanLi_Msg_Title))
                            .setMessage(getString(R.string.HuiYuanZhongXin_DiZhiGuanLi_Msg_Contents))
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    callDelReceiver();
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
        m_imgbtnRefresh.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadInitialData();
            }
        });

        m_txtNewReceiver.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = new Intent(DiZhiGuanLiActivity.this, DiZhiGuanLi_EditActivity.class);

                intent.putExtra("create_flag", true);
                startActivity(intent);
            }
        });
    }

    private void loadInitialData () {
        m_arrReceivers.clear();
        callGetReceivers();
    }

    private void updateUI () {
        fillReceiverList();
    }

    private void fillReceiverList () {
        if ( mAdapter == null ) {
            mAdapter = new ReceiverListAdapter(DiZhiGuanLiActivity.this, this.getApplicationContext());
            mAdapter.setData(m_arrReceivers);
            m_lstReceivers.setAdapter(mAdapter);

            // do not show separator
            m_lstReceivers.setDivider(new ColorDrawable(Color.LTGRAY));
            m_lstReceivers.setCacheColorHint(Color.parseColor("#FFF1F1F1"));
            m_lstReceivers.setDividerHeight(0);
        } else {
            mAdapter.notifyDataSetChanged();
            m_lstReceivers.invalidate();
        }
    }

    private void callGetReceivers () {
        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = "";

                retMsg = CommMgr.commService.parseGetReceivers(object, m_arrReceivers);
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

                GlobalData.showToast(DiZhiGuanLiActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                progDialog.dismiss();

                if (result == STServiceData.ERR_FAIL)
                {
                    GlobalData.showToast(DiZhiGuanLiActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                DiZhiGuanLiActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.GetReceivers(GlobalData.token, handler);
    }

    private void callDelReceiver () {
        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = "";

                progDialog.dismiss();

                retMsg = CommMgr.commService.parseDelReceiver(object);
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

                GlobalData.showToast(DiZhiGuanLiActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                progDialog.dismiss();
                if (result == STServiceData.ERR_FAIL)
                {
                    GlobalData.showToast(DiZhiGuanLiActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                DiZhiGuanLiActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        String  selectedIds = mAdapter.getSelectedIds();
        CommMgr.commService.DelReceiver(GlobalData.token, selectedIds, handler);
    }
}
