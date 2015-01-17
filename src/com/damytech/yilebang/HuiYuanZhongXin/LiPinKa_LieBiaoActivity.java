package com.damytech.yilebang.HuiYuanZhongXin;

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
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.STData.STGiftCardInfo;
import com.damytech.STData.STServiceData;
import com.damytech.Global.GlobalData;
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
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-22
 * Time: 下午7:30
 * To change this template use File | Settings | File Templates.
 */
public class LiPinKa_LieBiaoActivity extends MyActivity {

    private final static int CARD_LIST_ONESHOW_COUNT = 10;

    // UI control variables
    RelativeLayout  m_rlMain;
    RelativeLayout  m_rlHeader;
    TextView m_txtActivate;
    RelativeLayout  m_rlBody;
    RelativeLayout m_rlFooter;
    ImageButton m_imgbtnBack;
    ImageButton  m_imgbtnAccount;

    PullToRefreshListView m_lstGiftCards;
    private ListView listRealAddressList;

    ArrayList<STGiftCardInfo>  m_arrGiftCards = new ArrayList<STGiftCardInfo>(0);
    MyAdapter  mAdapter;

    private int nRequestPageNo = 1;
    boolean bExistNext = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.huiyuanzhongxin_lipinka_liebiao);

        ResolutionSet._instance.iterateChild(findViewById(R.id.rlMain));

        getControlVariables ();

        connectSignalHandlers();
    }

    private void getControlVariables () {
        m_rlMain = (RelativeLayout) findViewById(R.id.rlMain);
        m_rlHeader = (RelativeLayout) findViewById(R.id.rlHeader);
        m_txtActivate = (TextView) findViewById(R.id.txtActivate);
        m_txtActivate.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LiPinKa_LieBiaoActivity.this, LiPinKaActivity.class);
                startActivity(intent);
            }
        });
        m_rlBody = (RelativeLayout) findViewById(R.id.rlBody);
        m_lstGiftCards = (PullToRefreshListView) findViewById(R.id.lstGiftCards);
        m_lstGiftCards.setMode(PullToRefreshBase.Mode.PULL_FROM_END);

        m_lstGiftCards.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

                new GetDataTask().execute();
            }
        });

        m_lstGiftCards.setOnLastItemVisibleListener( new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {}
        });
        listRealAddressList = m_lstGiftCards.getRefreshableView();

        m_rlFooter = (RelativeLayout) findViewById(R.id.rlFooter);
        m_imgbtnBack = (ImageButton) findViewById(R.id.imgbtnBack);
        m_imgbtnAccount = (ImageButton) findViewById(R.id.imgbtnAccount);
    }

    private void connectSignalHandlers () {
        m_imgbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgbtnBackClicked();
            }
        });
    }

    @Override
    public  void onStart()
    {
        super.onStart();

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
                retMsg = CommMgr.commService.parseGetLPCardList(object, m_arrGiftCards);

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
                    GlobalData.showToast(LiPinKa_LieBiaoActivity.this, getString(R.string.server_connection_error));
                    LiPinKa_LieBiaoActivity.this.finish();
                }

                result = 0;
            }
        };

        progDialog = ProgressDialog.show(
                LiPinKa_LieBiaoActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        nRequestPageNo = 1;
        CommMgr.commService.GetLPCardList(GlobalData.token, nRequestPageNo, handler);
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
        if (m_arrGiftCards == null)
            return;

        if (m_arrGiftCards.size() == CARD_LIST_ONESHOW_COUNT)
        {
            bExistNext = true;
            m_lstGiftCards.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        }
        else
        {
            bExistNext = false;
            m_lstGiftCards.setMode(PullToRefreshBase.Mode.DISABLED);
        }

        if (listRealAddressList != null)
        {
            listRealAddressList.setCacheColorHint(Color.parseColor("#FFF1F1F1"));
            listRealAddressList.setDivider(new ColorDrawable(Color.parseColor("#FFCCCCCC")));
            listRealAddressList.setDividerHeight(2);

            mAdapter = new MyAdapter(LiPinKa_LieBiaoActivity.this, 0, m_arrGiftCards);
            listRealAddressList.setAdapter(mAdapter);
        }
    }

    class MyAdapter extends ArrayAdapter<STGiftCardInfo> {
        ArrayList<STGiftCardInfo> list;
        Context ctx;

        public MyAdapter(Context ctx, int resourceId, ArrayList<STGiftCardInfo> list) {
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
                v = inflater.inflate(R.layout.huiyuanzhongxin_lipinka_liebiao_item, null);
                ResolutionSet._instance.iterateChild(v.findViewById(R.id.rlItemMain));
            }

            TextView txtView = (TextView) v.findViewById(R.id.txtGiftCardNo);
            txtView.setText(list.get(position).name);

            txtView = (TextView) v.findViewById(R.id.txtNominalValue);
            txtView.setText(Double.toString(list.get(position).orgPrice));

            txtView = (TextView) v.findViewById(R.id.txtBalance);
            txtView.setText(Double.toString(list.get(position).remPrice));

            return v;
        }
    }

    private class GetDataTask extends AsyncTask<Void, Void, ArrayList<STGiftCardInfo>> {

        @Override
        protected ArrayList<STGiftCardInfo> doInBackground(Void... params)
        {
            try {
                nRequestPageNo = nRequestPageNo + 1;
                String responseBody = RequestPairHistoryListWithParamNoDelay( nRequestPageNo);

                JSONObject jsonData = new JSONObject(responseBody);
                ArrayList<STGiftCardInfo> extraInfoList = new ArrayList<STGiftCardInfo>();
                String strRet = CommMgr.commService.parseGetLPCardList(jsonData, extraInfoList);

                return extraInfoList;
            } catch (Exception e) {
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<STGiftCardInfo> result) {
            if (result != null)
            {
                int nBufSize = result.size();
                if (nBufSize == CARD_LIST_ONESHOW_COUNT)
                    bExistNext = true;
                else
                    bExistNext = false;

                for (int i = 0; i < nBufSize; i++)
                    m_arrGiftCards.add(result.get(i));

                mAdapter.notifyDataSetChanged();
                m_lstGiftCards.onRefreshComplete();
            }

            super.onPostExecute(result);
        }
    }

    @SuppressWarnings("unchecked")
    public String RequestPairHistoryListWithParamNoDelay( int nPageNo)
    {
        String connectUrl = STServiceData.serviceAddr + STServiceData.cmdGetLPCardList + "?token=" + GlobalData.token + "&pageno=" + Integer.toString(nPageNo);

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
