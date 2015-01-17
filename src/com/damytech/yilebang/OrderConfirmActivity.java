package com.damytech.yilebang;

import android.app.Activity;
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
import com.damytech.STData.STParcelableBasketItem;
import com.damytech.STData.STReceiverInfo;
import com.damytech.STData.STServiceData;
import com.damytech.STData.STString;
import com.damytech.STData.ShoppingCart.STBasketItemInfo;
import com.damytech.Utils.ComboBox;
import com.damytech.Utils.ResolutionSet;
import com.damytech.yilebang.HuiYuanZhongXin.HuiYuanZhongXinActivity;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-24
 * Time: 上午10:17
 * To change this template use File | Settings | File Templates.
 */
public class OrderConfirmActivity extends MyActivity {
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

    private ArrayList<STReceiverInfo>  m_arrReceivers = new ArrayList<STReceiverInfo>(0);

    private Double m_TotalPrice = 0.0;
    private Double m_TransPrice = 0.0;

    private String m_OrderNum = "";

    private int m_nDeliveryMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.orderconfirm);

        ResolutionSet._instance.iterateChild(findViewById(R.id.rlMain));

        try
        {
            getControlVariables ();
            initControls();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        m_arrReceivers.clear();
        callGetReceivers();
    }

    private void getControlVariables () {
        m_rlMain = (RelativeLayout) findViewById(R.id.rlMain);
        m_rlReceiverInfo = (RelativeLayout) findViewById(R.id.rlReceiverInfo);
        m_rlReceiverInfo.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderConfirmActivity.this, SelectReceiverActivity.class);
                startActivity(intent);
            }
        });
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
    }

    /**
     * Initialize all controls
     */
    private void initControls()
    {
        ArrayList<STParcelableBasketItem> parcelArray = getIntent().getParcelableArrayListExtra("ItemArray");
        m_TotalPrice = getIntent().getExtras().getDouble("TotalPrice");
        DecimalFormat df = new DecimalFormat("#.##");
        m_TotalPrice = Double.parseDouble(df.format(m_TotalPrice));
        m_TransPrice = getIntent().getExtras().getDouble("TransPrice");
        if ((parcelArray == null) || (parcelArray.size() == 0))
        {
            GlobalData.showToast(OrderConfirmActivity.this, getString(R.string.OrderConfirm_MSG_Empty));
            finish();
            return;
        }

        // initialize order product array list
        mDataList = new ArrayList<STBasketItemInfo>(0);
        for (STParcelableBasketItem parcelItem : parcelArray)
        {
            mDataList.add(parcelItem.GetItem());
        }
        m_orderAdapter = new OrderItemAdapter(OrderConfirmActivity.this, this.getApplicationContext());
        m_orderAdapter.setData(mDataList);
        m_lstCommodity.setAdapter(m_orderAdapter);

        int totalHeight = 0;
        // reset listview height
        ViewGroup.LayoutParams params = m_lstCommodity.getLayoutParams();
        params.height = (int)(110 * ResolutionSet.fYpro * mDataList.size());
        m_lstCommodity.setLayoutParams(params);

        // set money data
        setMoneyData();

        String[] arrStr = getResources().getStringArray(R.array.DeliveryMode_String);
        m_txtDeliveryMethod.setText(arrStr[2]);

        // set implementation of event
        RelativeLayout rlDelivery = (RelativeLayout) findViewById(R.id.rlPayAndDeliveryMethod);
        rlDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeliveryEditDlg dlg = null;
                dlg = new DeliveryEditDlg(OrderConfirmActivity.this, m_txtDeliveryMethod.getText().toString());
                dlg.show();
            }
        });

        RelativeLayout rlComment = (RelativeLayout) findViewById(R.id.rlLeaveMessage);
        rlComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentEditDlg dlg = null;
                dlg = new CommentEditDlg(OrderConfirmActivity.this, m_txtMessage.getText().toString());
                dlg.show();
            }
        });

        m_txtGoToPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call go to pay service
                requestOrder();
            }
        });

        initMainMenu();
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

    private void setMoneyData()
    {
        m_txtCommodityPrice.setText(Double.toString(m_TotalPrice));
        m_txtPostagePrice.setText(Double.toString(m_TransPrice));
        m_txtMoneyToPay.setText(Double.toString(m_TotalPrice + m_TransPrice));
    }


    /**
     * Update UI
     */
    private void updateUI()
    {
        try
        {
            for (STReceiverInfo item : m_arrReceivers)
            {
                // check receive address for default
                if (item.isDefault == 1)
                {
                    setReceiveAddr(item);
                    break;
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Set receive address
     * @param item [in], address data to be set
     */
    public void setReceiveAddr(STReceiverInfo item)
    {
        m_txtReceiverName.setText(item.name);
        m_txtReceiverPhone.setText(item.phone);
        m_txtReceiverAddress.setText(item.province + item.city + item.area + item.addrDetail);
    }

    /**
     * Set comment about current order
     * @param comment [in], comment string
     */
    private void setCommentText(String comment)
    {
        m_txtMessage.setText(comment);
    }

    private void setDeliveryMode(String mode)
    {
        String[] mArrModeString = getResources().getStringArray(R.array.DeliveryMode_String);
        m_txtDeliveryMethod.setText(mode);

        if (!mode.equals(mArrModeString[2]))
        {
            m_TransPrice = 0.0;
        }
        else
        {
            m_TransPrice = GlobalData.g_UserInfo.transPrice;
        }

        setMoneyData();
    }

    private void requestOrder()
    {
        m_nDeliveryMode = 0;

        try
        {
            String[] arrStr = getResources().getStringArray(R.array.DeliveryMode_String);
            String[] arrIds = getResources().getStringArray(R.array.DeliveryMode_Id);
            for (int i = 0; i < arrStr.length; i++)
            {
                if (arrStr[i].equals(m_txtDeliveryMethod.getText().toString()))
                {
                    m_nDeliveryMode = Integer.parseInt(arrIds[i]);
                    break;
                }
            }

            // check receive type
            String comment = m_txtMessage.getText().toString();

            callGiveOrder(mDataList, 1, m_nDeliveryMode, comment, m_TotalPrice, m_TransPrice);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    /**
     * Go to pay activity
     */
    private void gotoPay(String orderNo)
    {
        Intent intent = new Intent(OrderConfirmActivity.this, PaymentActivity.class);
        intent.putExtra("OrderNo", orderNo);
        // intent.putExtra("TotalPrice", m_TotalPrice); // get the total price in PaymentActivity.
        startActivity(intent);
        finish();
    }

    private void gotoMain()
    {
        Intent intent = new Intent(OrderConfirmActivity.this, HuiYuanZhongXinActivity.class);
        startActivity(intent);
        finish();
    }

    ////////////////////////////////////////////////// Service Relation ///////////////////////////////////////////////////
    /**
     * Call GetReceivers service
     */
    private void callGetReceivers () {
        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = "";

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

                GlobalData.showToast(OrderConfirmActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                if (result == STServiceData.ERR_FAIL)
                {
                    progDialog.dismiss();
                    GlobalData.showToast(OrderConfirmActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                OrderConfirmActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.GetReceivers(GlobalData.token, handler);
    }


    /**
     * Call GetReceivers service
     */
    private void callGiveOrder(ArrayList<STBasketItemInfo> arrProd, int payment, int recvtype,
                               String comment, Double totalprice, Double transprice) {
        handler1 = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = "";

                progDialog.dismiss();

                STString retVal = new STString();
                retMsg = CommMgr.commService.parseGiveOrder(object, retVal);
                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    result = STServiceData.ERR_SUCCESS;

                    if (m_nDeliveryMode == 1)     // zidifukuan
                    {
                        gotoMain();
                    }
                    else
                    {
                        gotoPay(retVal.szVal);
                    }

                    return;
                }
                else
                {
                    result = STServiceData.ERR_EXCEPTION;
                }

                GlobalData.showToast(OrderConfirmActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                if (result == STServiceData.ERR_FAIL)
                {
                    progDialog.dismiss();
                    GlobalData.showToast(OrderConfirmActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                OrderConfirmActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.GiveOrder(arrProd, payment, recvtype, comment, totalprice, transprice, GlobalData.token, handler1);
    }

    //////////////////////////////////////// Comment Edit Dialog /////////////////////////////
    /**
     * Edit dialog of product count in shopping cart
     */
    public final class CommentEditDlg extends Dialog {

        Context mContext = null;

        EditText txtComment = null;
        Button btnConfirm;

        String m_sComment = "";

        public CommentEditDlg(Context context)
        {
            super(context);
            mContext = context;
        }

        public CommentEditDlg(Context context, String sCommentt)
        {
            super(context);
            mContext = context;
            m_sComment = sCommentt;
        }

        @Override
        public void onCreate(Bundle $savedInstanceState) {
            super.onCreate( $savedInstanceState );
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.order_comment_editdlg);

            // initialize
            initControls();
        }

        /**
         * Initialize all controls
         */
        private void initControls()
        {
            try
            {
                ResolutionSet._instance.iterateChild(findViewById(R.id.rlOrderCommentEditDlgMain));

                txtComment = (EditText) findViewById(R.id.txtComment);
                txtComment.setText(m_sComment);

                btnConfirm = (Button) findViewById(R.id.btnConfirm);
                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try
                        {
                            setCommentText(txtComment.getText().toString());
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }
                        dismiss();
                    }
                });
            }
            catch (Exception ex)
            {
                ex.fillInStackTrace();
            }
        }
    }

    //////////////////////////////////////// Delivery Edit Dialog /////////////////////////////
    /**
     * Edit dialog of product count in shopping cart
     */
    public final class DeliveryEditDlg extends Dialog {

        Context mContext = null;

        ComboBox mCombo = null;
        Button btnConfirm;

        String m_sMode = "";
        private String[] mArrModeString = null;

        public DeliveryEditDlg(Context context)
        {
            super(context);
            mContext = context;
        }

        public DeliveryEditDlg(Context context, String sMode)
        {
            super(context);
            mContext = context;
            m_sMode = sMode;
        }

        @Override
        public void onCreate(Bundle $savedInstanceState) {
            super.onCreate( $savedInstanceState );
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.order_deliver_editdlg);

            // initialize
            initControls();
        }

        /**
         * Initialize all controls
         */
        private void initControls()
        {
            try
            {
                mArrModeString = getResources().getStringArray(R.array.DeliveryMode_String);
                mCombo = (ComboBox) findViewById(R.id.comboDelivery);
                mCombo.setData(mArrModeString);

                btnConfirm = (Button) findViewById(R.id.btnConfirm);
                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try
                        {
                            setDeliveryMode(mCombo.getText().toString());
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }
                        dismiss();
                    }
                });

                ResolutionSet._instance.iterateChild(findViewById(R.id.rlOrderDeliveryEditDlgMain));

            }
            catch (Exception ex)
            {
                ex.fillInStackTrace();
            }
        }
    }
}
