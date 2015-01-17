package com.damytech.yilebang.HuiYuanZhongXin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.damytech.CommService.CommMgr;
import com.damytech.Global.GlobalData;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.STData.STNewsInfo;
import com.damytech.STData.STServiceData;
import com.damytech.Utils.PullToRefreshBase;
import com.damytech.Utils.PullToRefreshListView;
import com.damytech.Utils.ResolutionSet;
import com.damytech.yilebang.MyActivity;
import com.damytech.yilebang.MyAlertDialog;
import com.damytech.yilebang.R;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-22
 * Time: 下午7:31
 * To change this template use File | Settings | File Templates.
 */
public class XiaoXiActivity extends MyActivity {
    // UI control variables
    RelativeLayout  m_rlMain;
    RelativeLayout  m_rlHeader;
    ImageButton  m_imgbtnRefresh;
    PullToRefreshListView m_lstNews;
    RelativeLayout m_rlFooter;
    ImageButton  m_imgbtnBack;
    ImageButton  m_imgbtnDelete;
    ImageButton m_imgbtnAccount;

    ArrayList<STNewsInfo> m_arrNews = new ArrayList<STNewsInfo>(0);
    NewsListAdapter  mAdapter;

    int  m_nPageCount;
    public static String g_strTokenFileName="YLBToken.log";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.huiyuanzhongxin_xiaoxi);

        ResolutionSet._instance.iterateChild(findViewById(R.id.rlMain));

        getControlVariables ();

        connectSignalHandlers();

        loadInitialData();
    }

    private void getControlVariables () {
        m_rlMain = (RelativeLayout) findViewById(R.id.rlMain);
        m_rlHeader = (RelativeLayout) findViewById(R.id.rlHeader);
        m_imgbtnRefresh = (ImageButton) findViewById(R.id.imgbtnRefresh);
        m_lstNews = (PullToRefreshListView) findViewById(R.id.lstNews);
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
                    GlobalData.showToast(XiaoXiActivity.this, getString(R.string.no_selected_item));
                    return;
                } else {
                    new AlertDialog.Builder(XiaoXiActivity.this)
                            .setTitle(getString(R.string.HuiYuanZhongXin_XiaoXi_Msg_Title))
                            .setMessage(getString(R.string.HuiYuanZhongXin_XiaoXi_Msg_Contents))
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    callDeleteNewsInfo();
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

        m_lstNews.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        m_lstNews.setOnRefreshListener( new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                // Do work to refresh the list here.
                m_nPageCount = m_nPageCount + 1;
                callGetNewsInfo(m_nPageCount);
                // new GetDataTask().execute();
            }
        });
    }

    private void loadInitialData () {
        m_nPageCount = 1;
        m_arrNews.clear();
        callGetNewsInfo(m_nPageCount);
    }

    private void updateUI () {
        fillNewsList();

        // Call onRefreshComplete when the list has been refreshed.
        m_lstNews.onRefreshComplete();
    }

    private void fillNewsList () {
        if ( mAdapter == null ) {
            mAdapter = new NewsListAdapter(XiaoXiActivity.this, this.getApplicationContext());
            mAdapter.setData(m_arrNews);

            ListView mRealListView = m_lstNews.getRefreshableView();
            registerForContextMenu(mRealListView);

            mRealListView.setDivider(new ColorDrawable(Color.LTGRAY));
            mRealListView.setCacheColorHint(Color.parseColor("#FFF1F1F1"));
            mRealListView.setDividerHeight(2);
            m_lstNews.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    private void callGetNewsInfo ( int pageno ) {
        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = "";

                retMsg = CommMgr.commService.parseGetNewsInfo(object, m_arrNews);
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

                GlobalData.showToast(XiaoXiActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                progDialog.dismiss();

                if (result == STServiceData.ERR_FAIL)
                {
                    GlobalData.showToast(XiaoXiActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                XiaoXiActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);


        try {
            String strToken = fileReadToken(XiaoXiActivity.this);
            CommMgr.commService.GetNewsInfo(strToken, pageno, handler);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public String fileReadToken(Context context) throws IOException {
        String strFilePath = "";
        strFilePath = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/" + g_strTokenFileName;

        File file = new File(strFilePath);
        if (file.exists() == false)
            return "";

        FileInputStream input = new FileInputStream(file);
        InputStream is = new BufferedInputStream(input);

        try {
            InputStreamReader rdr = new InputStreamReader(is, "UTF-8");
            StringBuilder contents = new StringBuilder();
            char[] buff = new char[256];
            int len = rdr.read(buff);
            return String.valueOf(buff, 0, len);
        } finally {
            try {
                is.close();
            } catch (Exception e) {
                return "";
            }
        }
    }

    private void callDeleteNewsInfo () {
        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = "";

                progDialog.dismiss();

                retMsg = CommMgr.commService.parseDeleteNewsInfo(object);
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

                GlobalData.showToast(XiaoXiActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                progDialog.dismiss();

                if (result == STServiceData.ERR_FAIL)
                {
                    GlobalData.showToast(XiaoXiActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                XiaoXiActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        String  selectedIds = mAdapter.getSelectedIds();
        String token = null;
        try {
            token = fileReadToken(XiaoXiActivity.this);
            CommMgr.commService.DeleteNewsInfo(token, selectedIds, handler);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
