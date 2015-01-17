package com.damytech.yilebang;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import com.damytech.CommService.CommMgr;
import com.damytech.Global.GlobalData;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.STData.*;
import com.damytech.STData.ShoppingCart.STBasketItemInfo;
import com.damytech.Utils.ComboBox;
import com.damytech.Utils.ResolutionSet;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-24
 * Time: 上午10:17
 * To change this template use File | Settings | File Templates.
 */
public class OrderDetailActivity extends MyActivity {
    // UI control variables
    RelativeLayout  m_rlMain;
    RelativeLayout  m_rlReceiverInfo;
    TextView  m_txtReceiverName;
    TextView  m_txtReceiverPhone;
    TextView  m_txtReceiverAddress;
    RelativeLayout  m_rlPayAndDeliveryMethod;
    TextView  m_txtDeliveryMethod;
    RelativeLayout  m_rlCommodityList;
    ListView  m_lstCommodity;
    RelativeLayout  m_rlLeaveMessage;
    TextView  m_txtMessage;
    RelativeLayout  m_rlMoneyPayable;
    TextView  m_txtMoneyToPay;
    TextView  m_txtCommodityPrice;
    TextView  m_txtPostagePrice;
    TextView  m_txtPreferencePrice;
    RelativeLayout  m_rlFooter;
    ImageButton  m_imgbtnBack;
    TextView  m_txtGoToPay;

    private OrderItemAdapter m_orderAdapter;
    private ArrayList<STBasketItemInfo> mDataList;

    private String m_OrderNum = "";
    private int m_nState = 1;

    private STOrderDetail mOrderDetail = new STOrderDetail();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.orderconfirm);

        ResolutionSet._instance.iterateChild(findViewById(R.id.rlMain));

        try
        {
            m_OrderNum = getIntent().getExtras().getString("OrderNo");
            m_nState = getIntent().getExtras().getInt("State");

            initMainMenu();
            getControlVariables ();
            initWithState();
            callGetOrderInfo(m_OrderNum);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    private void initWithState()
    {
        if (m_nState != 1)
        {
            m_txtGoToPay.setVisibility(View.GONE);
        }
    }

    private void initMainMenu()
    {
        ImageButton rlBootom_Back = (ImageButton) findViewById(R.id.imgbtnBack);
        rlBootom_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgbtnBackClicked();
            }
        });
    }

    private void getControlVariables () {
        m_rlMain = (RelativeLayout) findViewById(R.id.rlMain);
        m_rlReceiverInfo = (RelativeLayout) findViewById(R.id.rlReceiverInfo);
        m_txtReceiverName = (TextView) findViewById(R.id.txtReceiverName);
        m_txtReceiverPhone = (TextView) findViewById(R.id.txtReceiverPhone);
        m_txtReceiverAddress = (TextView) findViewById(R.id.txtReceiverAddress);
        m_rlPayAndDeliveryMethod = (RelativeLayout) findViewById(R.id.rlPayAndDeliveryMethod);
        m_txtDeliveryMethod = (TextView) findViewById(R.id.txtDeliveryMethod);
        m_rlCommodityList = (RelativeLayout) findViewById(R.id.rlCommodityList);
        m_lstCommodity = (ListView) findViewById(R.id.lstCommodity);
        m_lstCommodity.setDivider(new ColorDrawable(Color.TRANSPARENT));
        m_lstCommodity.setCacheColorHint(Color.parseColor("#FFF1F1F1"));
        m_lstCommodity.setDividerHeight(0);
        m_rlLeaveMessage = (RelativeLayout) findViewById(R.id.rlLeaveMessage);
        m_txtMessage = (TextView) findViewById(R.id.txtMessage);
        m_rlMoneyPayable = (RelativeLayout) findViewById(R.id.rlMoneyPayable);
        m_txtMoneyToPay = (TextView) findViewById(R.id.txtMoneyToPay);
        m_txtCommodityPrice = (TextView) findViewById(R.id.txtCommodityPrice);
        m_txtPostagePrice = (TextView) findViewById(R.id.txtPostagePrice);
        m_txtPreferencePrice = (TextView) findViewById(R.id.txtPreferencePrice);
        m_rlFooter = (RelativeLayout) findViewById(R.id.rlFooter);
        m_imgbtnBack = (ImageButton) findViewById(R.id.imgbtnBack);
        m_txtGoToPay = (TextView) findViewById(R.id.txtGoToPay);
        m_txtGoToPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoPay();
            }
        });
    }

    /**
     * Initialize all controls
     */
    private void updateUI()
    {
        // initialize order product array list
        mDataList = mOrderDetail.arrProducts;
        m_orderAdapter = new OrderItemAdapter(OrderDetailActivity.this, this.getApplicationContext());
        m_orderAdapter.setData(mDataList);
        m_lstCommodity.setAdapter(m_orderAdapter);

        // reset listview height
        ViewGroup.LayoutParams params = m_lstCommodity.getLayoutParams();
        params.height = (int)(110 * ResolutionSet.fYpro * mDataList.size());
        m_lstCommodity.setLayoutParams(params);

        // update all ui control text
        setMoneyData();
        setReceiveAddr();
        setCommentText();
        setDeliveryMode();
    }

    private void setMoneyData()
    {
        String sCommPrice = Double.toString(mOrderDetail.prodPrice);
        String sTransPrice = Double.toString(mOrderDetail.transPrice);
        String sTotalPrice = Double.toString(mOrderDetail.prodPrice + mOrderDetail.transPrice);

        m_txtCommodityPrice.setText(sCommPrice);
        m_txtPostagePrice.setText(sTransPrice);
        m_txtMoneyToPay.setText(sTotalPrice);
    }

    private void setReceiveAddr()
    {
        m_txtReceiverName.setText(mOrderDetail.rcvName);
        m_txtReceiverPhone.setText(mOrderDetail.rcvPhone);
        m_txtReceiverAddress.setText(mOrderDetail.rcvAddress);
    }

    /**
     * Set comment about current order
     */
    private void setCommentText()
    {
        m_txtMessage.setText(mOrderDetail.comment);
    }

    private void setDeliveryMode()
    {
        String[] arrStr = getResources().getStringArray(R.array.DeliveryMode_String);
        String[] arrIds = getResources().getStringArray(R.array.DeliveryMode_Id);

        for (int i = 0; i < arrIds.length; i++)
        {
            if (mOrderDetail.rcvType == Integer.parseInt(arrIds[i]))
            {
                m_txtDeliveryMethod.setText(arrStr[i]);
                break;
            }

        }
    }

    private void gotoPay()
    {
        Intent intent = new Intent(OrderDetailActivity.this, PaymentActivity.class);
        intent.putExtra("OrderNo", m_OrderNum);
        // intent.putExtra("TotalPrice", mOrderDetail.prodPrice); // get the total price in PaymentActivity.
        startActivity(intent);
        finish();
    }

    ////////////////////////////////////////////////// Service Relation ///////////////////////////////////////////////////
    /**
     * Call GetReceivers service
     */
    private void callGetOrderInfo(String orderNo) {
        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = "";

                progDialog.dismiss();

                retMsg = CommMgr.commService.parseGetOrderInfo(object, mOrderDetail);
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

                GlobalData.showToast(OrderDetailActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                if (result == STServiceData.ERR_FAIL)
                {
                    progDialog.dismiss();
                    GlobalData.showToast(OrderDetailActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                OrderDetailActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.GetOrderInfo(GlobalData.token, orderNo, handler);
    }
}
