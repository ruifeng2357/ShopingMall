package com.damytech.yilebang;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import com.damytech.CommService.CommMgr;
import com.damytech.Global.GlobalData;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.STData.STDouble;
import com.damytech.STData.STGiftCardInfo;
import com.damytech.STData.STReceiverInfo;
import com.damytech.STData.STServiceData;
import com.damytech.Utils.ResolutionSet;
import com.damytech.yilebang.HuiYuanZhongXin.DiZhiGuanLi_EditActivity;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-29
 * Time: 下午2:56
 * To change this template use File | Settings | File Templates.
 */
public class SelectReceiverActivity extends MyActivity {

    TextView lblAdd;
    TextView lblOk;
    ListView listReceivers;

    TextView[] m_arrImage = null;
    int m_nSelectUid = 0;

    ArrayList<STReceiverInfo> m_arrReceivers = new  ArrayList<STReceiverInfo>();
    MyAdapter mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dizhiguanlilist);

        lblAdd = (TextView) findViewById(R.id.lblSelectReceiver_Add);
        lblAdd.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectReceiverActivity.this, DiZhiGuanLi_EditActivity.class);
                intent.putExtra("create_flag", true);
                startActivity(intent);
            }
        });

        lblOk = (TextView) findViewById(R.id.lblSelectReceiver_Ok);
        lblOk.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callSetDefaultReceiver();
            }
        });

        listReceivers = (ListView)findViewById(R.id.listSelectReceiver);

        ResolutionSet._instance.iterateChild(findViewById(R.id.rlDlgMain));
    }

    class MyAdapter extends ArrayAdapter<STReceiverInfo> {
        ArrayList<STReceiverInfo> list;
        Context ctx;

        public MyAdapter(Context ctx, int resourceId, ArrayList<STReceiverInfo> list) {
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
                v = inflater.inflate(R.layout.dizhiguanli_item, null);
                ResolutionSet._instance.iterateChild(v.findViewById(R.id.rlItemMain));

                m_arrImage[position] = (TextView)v.findViewById(R.id.imgSelect);
                if (list.get(position).isDefault == 1)
                {
                    m_nSelectUid = list.get(position).uid;
                    m_arrImage[position].setBackgroundResource(R.drawable.redcheck_check);
                }
                m_arrImage[position].setTag(position);
                m_arrImage[position].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int nVal = (Integer)v.getTag();
                        for ( int i = 0; i < m_arrImage.length; i++)
                        {
                            if ( i == nVal ) {
                                m_nSelectUid = list.get(i).uid;
                                list.get(i).isDefault = 1;
                            } else {
                                list.get(i).isDefault = 0;
                            }

                            if ( m_arrImage[i] == null )
                                continue;

                            if (i == nVal) {
                                m_arrImage[i].setBackgroundResource(R.drawable.redcheck_check);
                            } else {
                                m_arrImage[i].setBackgroundResource(R.drawable.redcheck_uncheck);
                            }
                        }
                    }
                });
            }

            TextView txtView = (TextView) v.findViewById(R.id.txtReceiverName);
            txtView.setText(list.get(position).name);

            txtView = (TextView) v.findViewById(R.id.txtReceiverPhone);
            txtView.setText(list.get(position).phone);

            txtView = (TextView) v.findViewById(R.id.txtReceiverAddress);
            txtView.setText(getString(R.string.HuiYuanZhongXin_DiZhiGuanLi_DiZhi) + list.get(position).province + " " + list.get(position).city + " " + list.get(position).area);

            ImageView img = (ImageView) v.findViewById(R.id.imgViewMore);
            img.setVisibility(View.INVISIBLE);

            return v;
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();

        m_arrReceivers.clear();
        RunBackgroundHandler();
    }

    private void updateUI()
    {
        if (m_arrReceivers == null || m_arrReceivers.size() == 0)
            return;

        m_arrImage = new TextView[m_arrReceivers.size()] ;
        mAdapter = new MyAdapter(SelectReceiverActivity.this, 0, m_arrReceivers);
        listReceivers.setAdapter(mAdapter);

        // remove the default divider
        listReceivers.setDivider(new ColorDrawable(Color.LTGRAY));
        listReceivers.setCacheColorHint(Color.parseColor("#00F1F1F1"));
        listReceivers.setDividerHeight(0);

        return;
    }

    private void callSetDefaultReceiver()
    {
        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;
            String retMsg = "";

            @Override
            public void onSuccess(JSONObject object)
            {
                progDialog.dismiss();
                STDouble  transPrice = new STDouble();
                retMsg = CommMgr.commService.parseSetDefaultReceiver(object, transPrice);

                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    // Set delivery price
                    GlobalData.g_UserInfo.transPrice = transPrice.fVal;

                    result = STServiceData.ERR_SUCCESS;
                    SelectReceiverActivity.this.finish();
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
                    GlobalData.showToast(SelectReceiverActivity.this, getString(R.string.server_connection_error));
                    SelectReceiverActivity.this.finish();
                }

                result = 0;
            }
        };

        if ( m_nSelectUid == 0 ) {
            GlobalData.showToast(SelectReceiverActivity.this, getString(R.string.SelectReceiver_NoSelected));
            return;
        }

        progDialog = ProgressDialog.show(
                SelectReceiverActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.RequestSetDefaultReceiver(GlobalData.token, m_nSelectUid, handler);
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
                    GlobalData.showToast(SelectReceiverActivity.this, getString(R.string.server_connection_error));
                    SelectReceiverActivity.this.finish();
                }

                result = 0;
            }
        };

        progDialog = ProgressDialog.show(
                SelectReceiverActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.GetReceivers(GlobalData.token, handler);
    }
}
