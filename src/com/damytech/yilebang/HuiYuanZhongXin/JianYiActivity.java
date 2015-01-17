package com.damytech.yilebang.HuiYuanZhongXin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.damytech.CommService.CommMgr;
import com.damytech.Global.GlobalData;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.STData.STServiceData;
import com.damytech.STData.STSuggestionInfo;
import com.damytech.Utils.PullToRefreshBase;
import com.damytech.Utils.PullToRefreshListView;
import com.damytech.Utils.ResolutionSet;
import com.damytech.yilebang.MyActivity;
import com.damytech.yilebang.R;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-22
 * Time: 下午7:27
 * To change this template use File | Settings | File Templates.
 */
public class JianYiActivity extends MyActivity {

    private final static int JIANYI_LIST_ONESHOW_COUNT = 10;

    // UI control variables
    RelativeLayout  m_rlMain;
    RelativeLayout  m_rlHeader;
    ImageButton  m_imgbtnRefresh;

    PullToRefreshListView m_lstSuggestions;
    private ListView listRealAddressList;

    private int nRequestPageNo = 1;
    boolean bExistNext = true;

    RelativeLayout m_rlFooter;
    ImageButton  m_imgbtnBack;
    ImageButton  m_imgbtnDelete;
    ImageButton m_imgbtnAccount;
    TextView m_txtNewSuggestion;

    ArrayList<STSuggestionInfo> m_arrSuggestions = new ArrayList<STSuggestionInfo>(0);
    MyAdapter  mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.huiyuanzhongxin_jianyi);

        ResolutionSet._instance.iterateChild(findViewById(R.id.rlMain));

        getControlVariables ();
        connectSignalHandlers();

        RunBackgroundHandler();
    }

    private void RunBackgroundHandler()
    {
        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;
            String retMsg = "";

            @Override
            public void onSuccess(JSONObject object)
            {
                progDialog.dismiss();
                retMsg = CommMgr.commService.parseGetProposeInfo(object, m_arrSuggestions);

                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    result = STServiceData.ERR_SUCCESS;
                    initContents();

                    return;
                }
                else
                {
                    result = STServiceData.ERR_EXCEPTION;
                }
            }

            @Override
            public void onFailure(Throwable ex, String exception) {
            }

            @Override
            public void onFinish()
            {
                progDialog.dismiss();

                if (result == STServiceData.ERR_FAIL)
                {
                    GlobalData.showToast(JianYiActivity.this, getString(R.string.server_connection_error));
                    JianYiActivity.this.finish();
                }

                result = 0;
            }
        };

        progDialog = ProgressDialog.show(
                JianYiActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        nRequestPageNo = 1;
        CommMgr.commService.GetProposeInfo(GlobalData.token, nRequestPageNo, handler);
    }

    private void getControlVariables () {
        m_rlMain = (RelativeLayout) findViewById(R.id.rlMain);
        m_rlHeader = (RelativeLayout) findViewById(R.id.rlHeader);
        m_imgbtnRefresh = (ImageButton) findViewById(R.id.imgbtnRefresh);
        m_lstSuggestions = (PullToRefreshListView) findViewById(R.id.lstSuggestions);
        m_lstSuggestions.setMode(PullToRefreshBase.Mode.PULL_FROM_END);

        m_lstSuggestions.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

                new GetDataTask().execute();
            }
        });

        m_lstSuggestions.setOnLastItemVisibleListener( new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {}
        });
        listRealAddressList = m_lstSuggestions.getRefreshableView();

        m_rlFooter = (RelativeLayout) findViewById(R.id.rlFooter);
        m_imgbtnBack = (ImageButton) findViewById(R.id.imgbtnBack);
        m_imgbtnDelete = (ImageButton) findViewById(R.id.imgbtnDelete);
        m_imgbtnAccount = (ImageButton) findViewById(R.id.imgbtnAccount);
        m_txtNewSuggestion = (TextView) findViewById(R.id.txtNewSuggestion);
    }

    private void connectSignalHandlers () {
        m_imgbtnRefresh.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listRealAddressList.setAdapter(null);

                if (m_arrSuggestions != null)
                  {
                      m_arrSuggestions = null;
                  }
                m_arrSuggestions = new ArrayList<STSuggestionInfo>(0);

                RunBackgroundHandler();
            }
        });
        m_imgbtnBack.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgbtnBackClicked();
            }
        });

        m_txtNewSuggestion.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JianYiActivity.this, JianYiDetailActivity.class);
                startActivity(intent);
                JianYiActivity.this.finish();
            }
        });
    }

    private void initContents()
    {
        if (listRealAddressList != null)
        {
            getShowListFromData();
        }
    }

    private void getShowListFromData()
    {
        if (m_arrSuggestions == null)
            return;

//        if (m_arrSuggestions.size() == JIANYI_LIST_ONESHOW_COUNT)
//        {
//            bExistNext = true;
//            m_lstSuggestions.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
//        }
//        else
//        {
//            bExistNext = false;
//            m_lstSuggestions.setMode(PullToRefreshBase.Mode.DISABLED);
//        }

        if (listRealAddressList != null)
        {
            listRealAddressList.setCacheColorHint(Color.parseColor("#FFF1F1F1"));
            listRealAddressList.setDivider(new ColorDrawable(Color.parseColor("#FFCCCCCC")));
            listRealAddressList.setDividerHeight(2);

            mAdapter = new MyAdapter(JianYiActivity.this, 0, m_arrSuggestions);
            //mAdapter.setData(m_arrSuggestions);
            listRealAddressList.setAdapter(mAdapter);
        }

        // check data count ( display error message )
        RelativeLayout rlEmpty = (RelativeLayout) findViewById(R.id.rlEmptyMsg);
        if (m_arrSuggestions.size() <= 0)
        {
            rlEmpty.setVisibility(View.VISIBLE);
        }
        else
        {
            rlEmpty.setVisibility(View.GONE);
        }
    }

    class MyAdapter extends ArrayAdapter<STSuggestionInfo> {
        ArrayList<STSuggestionInfo> list;
        Context ctx;

        public MyAdapter(Context ctx, int resourceId, ArrayList<STSuggestionInfo> list) {
            super(ctx, resourceId, list);
            this.ctx = ctx;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null)
            {
                LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.huiyuanzhongxin_jianyi_item, null);
                ResolutionSet._instance.iterateChild(v.findViewById(R.id.rlItemMain));
            }

            TextView txtView = (TextView) v.findViewById(R.id.txtSubject);
            txtView.setText(list.get(position).title);

            txtView = (TextView) v.findViewById(R.id.txtContent);
            txtView.setText(list.get(position).content);

            txtView = (TextView) v.findViewById(R.id.txtSubmitTime);
            txtView.setText(list.get(position).time);

            return v;
        }
    }

    private class GetDataTask extends AsyncTask<Void, Void, ArrayList<STSuggestionInfo>> {

        @Override
        protected ArrayList<STSuggestionInfo> doInBackground(Void... params)
        {
            try {
                nRequestPageNo = nRequestPageNo + 1;
                String responseBody = RequestPairHistoryListWithParamNoDelay( nRequestPageNo);

                JSONObject jsonData = new JSONObject(responseBody);
                ArrayList<STSuggestionInfo> extraInfoList = new ArrayList<STSuggestionInfo>();
                String strRet = CommMgr.commService.parseGetProposeInfo(jsonData, extraInfoList);

                return extraInfoList;
            } catch (Exception e) {
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<STSuggestionInfo> result) {
            if (result != null)
            {
                int nBufSize = result.size();
                if (nBufSize == JIANYI_LIST_ONESHOW_COUNT)
                    bExistNext = true;
                else
                    bExistNext = false;

                for (int i = 0; i < nBufSize; i++)
                    m_arrSuggestions.add(result.get(i));

                mAdapter.notifyDataSetChanged();
                m_lstSuggestions.onRefreshComplete();
            }

            super.onPostExecute(result);
        }
    }

    @SuppressWarnings("unchecked")
    public String RequestPairHistoryListWithParamNoDelay( int nPageNo)
    {
        String connectUrl = STServiceData.serviceAddr + STServiceData.cmdGetProposeInfo + "?token=" + GlobalData.token + "&pageno=" + Integer.toString(nPageNo);

        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(connectUrl);

        @SuppressWarnings("rawtypes")
        ResponseHandler responseHandler = new BasicResponseHandler();

        String responseBody = "";
        try {
            responseBody = client.execute(get, responseHandler).toString();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return responseBody;
    }
}
