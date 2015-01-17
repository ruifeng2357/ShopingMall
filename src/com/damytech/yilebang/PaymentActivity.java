package com.damytech.yilebang;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.*;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;
import com.alipay.*;
import com.damytech.CommService.CommMgr;
import com.damytech.Global.GlobalData;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.STData.STDouble;
import com.damytech.STData.STOrderDetail;
import com.damytech.STData.STServiceData;
import com.damytech.Utils.ResolutionSet;
import com.damytech.yilebang.HuiYuanZhongXin.HuiYuanZhongXinActivity;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.DecimalFormat;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-24
 * Time: 上午10:19
 * To change this template use File | Settings | File Templates.
 */
public class PaymentActivity extends MyActivity {
    // UI control variables
    RelativeLayout  m_rlMain;
    RelativeLayout  m_rlBody;
    RadioButton  m_rdGiftCard;
    RadioButton  m_rdBangBi;
    EditText  m_edtGiftCardNo;
    EditText  m_edtGiftCardPassword;
    TextView  m_txtGiftCardValue;
    TextView  m_txtLookBalance;
    TextView  m_txtGiftCardBalance;
    CheckBox  m_chkUseGiftCard;
    EditText m_edtGiftCardMoneyToUse;
    TextView  m_txtMoneyNotPaid;
    RadioButton m_rdZhiFuBao;
    RelativeLayout m_rlFooter;
    ImageButton m_imgbtnBack;
    TextView m_txtSubmitPay;

    TextView m_txtDescUseMode;
    TextView m_txtTitleUseMode;
    ImageView m_imgMask;

    private STDouble fLeftMoney = new STDouble();
    private STOrderDetail mOrderDetail = new STOrderDetail();

    /*
    구조체가 있다고 가정하고 구현한 부분
     */

    String strCardNo;
    String strCardPass;

    private String mOrderNo = "";
    private double mTotalMoney = 0.0;
    private double mCardLeftMoney = 0.0;
    private double mUse1Money = 0.0;
    private double mUse2Money = 0.0;

    private ProgressDialog progDialog = null;
    private JsonHttpResponseHandler handlerLeftMoney = null;
    private JsonHttpResponseHandler handlerPay = null;
    private JsonHttpResponseHandler handlerBangbi = null;


    public static final int PAY_ZHIFUBAO_MODE = 1;
    public static final int PAY_CARD_MODE = 3;
    public static final int PAY_BANGBI_MODE = 6;

    private ProgressDialog mProgress = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pay);

        ResolutionSet._instance.iterateChild(findViewById(R.id.rlMain));

        try
        {
            initMainMenu();
            getBaseData();
            getControlVariables ();

            connectSignalHandlers();

            loadInitialData();
            initAlipay();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
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

    private void getBaseData()
    {
        mOrderNo = getIntent().getExtras().getString("OrderNo");

        callGetOrderInfo(mOrderNo);
    }

    private void getControlVariables () {

        m_txtDescUseMode = (TextView) findViewById(R.id.txtDescUse);
        m_txtTitleUseMode = (TextView) findViewById(R.id.textView7);
        m_imgMask = (ImageView) findViewById(R.id.imageMask);
        m_rlMain = (RelativeLayout) findViewById(R.id.rlMain);
        m_rlBody = (RelativeLayout) findViewById(R.id.rlBody);
        m_rdGiftCard = (RadioButton) findViewById(R.id.rdGiftCard);
        m_rdGiftCard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked)
                {
                    m_edtGiftCardNo.setEnabled(false);
                    m_edtGiftCardPassword.setEnabled(false);

                    m_txtTitleUseMode.setText(getString(R.string.Pay_Notice11));
                    m_txtDescUseMode.setText(getString(R.string.Pay_Notice31));
                    m_imgMask.setVisibility(View.VISIBLE);

                    callGetBangbiValue();
                }
                else
                {
                    m_edtGiftCardNo.setEnabled(true);
                    m_edtGiftCardPassword.setEnabled(true);

                    m_txtTitleUseMode.setText(getString(R.string.Pay_Notice1));
                    m_txtDescUseMode.setText(getString(R.string.Pay_Notice3));
                    m_imgMask.setVisibility(View.GONE);

                    try
                    {
                        mCardLeftMoney = Double.parseDouble(m_txtGiftCardValue.getText().toString());
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                        mCardLeftMoney = 0.0;
                    }
                    m_txtGiftCardBalance.setText(Double.toString(mCardLeftMoney));
                    updateZhifuMoney();
                }
            }
        });
        m_rdBangBi = (RadioButton) findViewById(R.id.rdBangBi);
        m_edtGiftCardNo = (EditText) findViewById(R.id.edtGiftCardNo);
        m_edtGiftCardPassword = (EditText) findViewById(R.id.edtGiftCardPassword);
        m_txtGiftCardValue = (TextView) findViewById(R.id.edtGiftCardBalance);
        m_txtLookBalance = (TextView) findViewById(R.id.txtLookBalance);
        m_txtLookBalance.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getBalanceAmount();

            }
        });

        m_txtGiftCardBalance = (TextView) findViewById(R.id.txtGiftCardBalance);
        m_chkUseGiftCard = (CheckBox) findViewById(R.id.chkUseGiftCard);
        m_chkUseGiftCard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                changeUseState(isChecked);
            }
        });
        m_edtGiftCardMoneyToUse = (EditText) findViewById(R.id.edtGiftCardMoneyToUse);
        m_edtGiftCardMoneyToUse.setEnabled(false);
        m_edtGiftCardMoneyToUse.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void afterTextChanged(Editable s) {
                //To change body of implemented methods use File | Settings | File Templates.
                updateZhifuMoney();
            }
        });
        m_txtMoneyNotPaid = (TextView) findViewById(R.id.txtMoneyNotPaid);
        m_rdZhiFuBao = (RadioButton) findViewById(R.id.rdZhiFuBao);
        m_rlFooter = (RelativeLayout) findViewById(R.id.rlFooter);
        m_imgbtnBack = (ImageButton) findViewById(R.id.imgbtnBack);
        m_txtSubmitPay = (TextView) findViewById(R.id.txtSubmitPay);
        m_txtSubmitPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mUse2Money > 0)
                {
                    performPay();
                }
                else
                {
                    callPayService();
                }
            }
        });

    }

    private void connectSignalHandlers () {

    }

    /**
     * change text of money to be used
     * @param isChecked [in], check box state
     */
    private void changeUseState(boolean isChecked)
    {
        m_edtGiftCardMoneyToUse.setText("");
        m_edtGiftCardMoneyToUse.setEnabled(isChecked);
    }

    private void gotoMain()
    {
        Intent intent = new Intent(PaymentActivity.this, HuiYuanZhongXinActivity.class);
        startActivity(intent);
    }

    /**
     * Update UI of money to be purchase
     */
    private void updateZhifuMoney()
    {
        double sUseMoney = 0;

        try
        {
            if (m_edtGiftCardMoneyToUse.length() == 0)
            {
                sUseMoney = 0;
            }
            else
            {
                // get input value
                sUseMoney = Double.parseDouble(m_edtGiftCardMoneyToUse.getText().toString());
            }
            // check with left money
            if (sUseMoney > mCardLeftMoney)
            {
                sUseMoney = mCardLeftMoney;
                m_edtGiftCardMoneyToUse.setText(Double.toString(sUseMoney));
            }
            if (sUseMoney > mTotalMoney)
            {
                sUseMoney = mTotalMoney;
                m_edtGiftCardMoneyToUse.setText(Double.toString(sUseMoney));
            }

            mUse1Money = sUseMoney;
            mUse2Money = mTotalMoney - mUse1Money;
            // set changed value
            m_txtMoneyNotPaid.setText(Double.toString(mUse2Money));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            m_edtGiftCardMoneyToUse.setText("");
        }

    }

    private void callPayService()
    {
        progDialog = ProgressDialog.show(
                PaymentActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        int paytype1 = 0;
        int paytype2 = PAY_ZHIFUBAO_MODE;
        if (m_chkUseGiftCard.isChecked())
        {
            if (m_rdGiftCard.isChecked())
            {
                paytype1 = PAY_CARD_MODE;
            }
            else
            {
                paytype1 = PAY_BANGBI_MODE;
            }
        }

        CommMgr.commService.PayOrder(GlobalData.token, mOrderNo, paytype1, strCardNo, strCardPass,
                    mUse1Money, paytype2, mUse2Money, handlerPay);
    }

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

                    // get total money
                    DecimalFormat df = new DecimalFormat("#.##");
                    String strTotalMoney = df.format(mOrderDetail.prodPrice);
                    mTotalMoney = Double.parseDouble(strTotalMoney);
                    mUse1Money = 0;
                    mUse2Money = mTotalMoney;

                    // update UI
                    TextView txtTotalMoney = (TextView) findViewById(R.id.txtTotalMoney);
                    txtTotalMoney.setText(strTotalMoney);
                    TextView txtMoneyNotPaid = (TextView) findViewById(R.id.txtMoneyNotPaid);
                    txtMoneyNotPaid.setText(strTotalMoney);
                    return;
                }
                else
                {
                    result = STServiceData.ERR_EXCEPTION;
                }

                GlobalData.showToast(PaymentActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                if (result == STServiceData.ERR_FAIL)
                {
                    progDialog.dismiss();
                    GlobalData.showToast(PaymentActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                PaymentActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.GetOrderInfo(GlobalData.token, orderNo, handler);
    }

    /**
     * Get left amount of card
     */
    private void getBalanceAmount()
    {
        strCardNo = m_edtGiftCardNo.getText().toString();
        strCardPass = m_edtGiftCardPassword.getText().toString();

        if (strCardNo == null  || strCardNo.length() == 0)
            return;
        if (strCardPass == null || strCardPass.length() == 0)
            return;

        progDialog = ProgressDialog.show(
                PaymentActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.RequestGetLPCardLeftMoney(GlobalData.token, strCardNo, strCardPass, handlerLeftMoney);
    }

    private void callGetBangbiValue () {
       progDialog = ProgressDialog.show(
                PaymentActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.GetBangbiValue ( GlobalData.token, handlerBangbi );
    }

    private void loadInitialData ()
    {
        m_txtDescUseMode.setText(getString(R.string.Pay_Notice3));
        m_imgMask.setVisibility(View.GONE);

        handlerLeftMoney = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;
            String retMsg = "";

            @Override
            public void onSuccess(JSONObject object)
            {
                progDialog.dismiss();

                retMsg = CommMgr.commService.parseGetLPCardLeftMoney(object, fLeftMoney);
                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    result = STServiceData.ERR_SUCCESS;
                    m_txtGiftCardValue.setText(Double.toString(fLeftMoney.fVal));
                    m_txtGiftCardBalance.setText(Double.toString(fLeftMoney.fVal));
                    mCardLeftMoney = fLeftMoney.fVal;
                }
                else
                {
                    result = STServiceData.ERR_EXCEPTION;
                    GlobalData.showToast(PaymentActivity.this, retMsg);
                    strCardNo = "";
                    strCardPass = "";
                }
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                progDialog.dismiss();
                if (result == STServiceData.ERR_FAIL)
                {
                    GlobalData.showToast(PaymentActivity.this, getString(R.string.server_connection_error));
                    strCardNo = "";
                    strCardPass = "";
                }

                result = 0;
            }

        };

        handlerPay = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;
            String retMsg = "";

            @Override
            public void onSuccess(JSONObject object)
            {
                progDialog.dismiss();

                retMsg = CommMgr.commService.parsePayOrder(object);
                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    result = STServiceData.ERR_SUCCESS;
                    gotoMain();
                }
                else
                {
                    result = STServiceData.ERR_EXCEPTION;
                    GlobalData.showToast(PaymentActivity.this, retMsg);
                }
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                progDialog.dismiss();
                if (result == STServiceData.ERR_FAIL)
                {
                    GlobalData.showToast(PaymentActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };


        handlerBangbi = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;
            String retMsg = "";

            @Override
            public void onSuccess(JSONObject object)
            {
                progDialog.dismiss();

                retMsg = CommMgr.commService.parseGetBangbiValue(object, fLeftMoney);
                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    result = STServiceData.ERR_SUCCESS;

                    m_txtGiftCardBalance.setText(Double.toString(fLeftMoney.fVal));
                    mCardLeftMoney = fLeftMoney.fVal;

                    return;
                }
                else
                {
                    result = STServiceData.ERR_EXCEPTION;
                }

                GlobalData.showToast(PaymentActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                progDialog.dismiss();
                if (result == STServiceData.ERR_FAIL)
                {
                    GlobalData.showToast(PaymentActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

    }


    /************************************** Alipay Function **********************************/

    private void initAlipay()
    {
        //
        // check to see if the MobileSecurePay is already installed.
        MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(this);
        mspHelper.detectMobile_sp();

        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filter.addDataScheme("package");
        registerReceiver(mPackageInstallationListener, filter);
    }


    private BroadcastReceiver mPackageInstallationListener = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String packageName = intent.getDataString();
            if (!TextUtils
                    .equals(packageName, "package:com.alipay.android.app")) {
                return;
            }

            if (!mOrderNo.isEmpty()) {
                performPay();
            }
        }
    };

    //
    // return strOrderInfo;
    // }
    String getOrderInfo() {
        String strOrderInfo = "partner=" + "\"" + PartnerConfig.PARTNER + "\"";
        strOrderInfo += "&";
        strOrderInfo += "seller=" + "\"" + PartnerConfig.SELLER + "\"";
        strOrderInfo += "&";
        strOrderInfo += "out_trade_no=" + "\"" + mOrderNo + "\"";
        strOrderInfo += "&";
        strOrderInfo += "subject=" + "\"" + mOrderNo
                + "\"";
        strOrderInfo += "&";
        strOrderInfo += "body=" + "\"" + mOrderNo + "\"";
        strOrderInfo += "&";
        strOrderInfo += "total_fee=" + "\""
                + mTotalMoney + "\"";
        strOrderInfo += "&";
        strOrderInfo += "notify_url=" + "\""
                + "http://notify.java.jpxx.org/index.jsp" + "\"";

        return strOrderInfo;
    }


    //
    //
    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param signType
     *            签名方式
     * @param content
     *            待签名订单信息
     * @return
     */
    String sign(String signType, String content) {
        return Rsa.sign(content, PartnerConfig.RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     *
     * @return
     */
    String getSignType() {
        String getSignType = "sign_type=" + "\"" + "RSA" + "\"";
        return getSignType;
    }

    /**
     * get the char set we use. 获取字符集
     *
     * @return
     */
    String getCharset() {
        String charset = "charset=" + "\"" + "utf-8" + "\"";
        return charset;
    }

    private void performPay() {
        //
        // check to see if the MobileSecurePay is already installed.
        // 检测安全支付服务是否安装
        MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(this);
        boolean isMobile_spExist = mspHelper.detectMobile_sp();
        if (!isMobile_spExist) {
            return;
        }

        // check some info.
        // 检测配置信息
        if (!checkInfo()) {
            BaseHelper
                    .showDialog(
                            PaymentActivity.this,
                            "提示",
                            "缺少partner或者seller，请在src/com/alipay/android/appDemo4/PartnerConfig.java中增加。",
                            R.drawable.infoicon);
            return;
        }

        // start pay for this order.
        // 根据订单信息开始进行支付
        try {
            // prepare the order info.
            // 准备订单信息
            String orderInfo = getOrderInfo();
            // 这里根据签名方式对订单信息进行签名
            String signType = getSignType();
            String strsign = sign(signType, orderInfo);
            Log.v("sign:", strsign);
            // 对签名进行编码
            strsign = URLEncoder.encode(strsign, "UTF-8");
            // 组装好参数
            String info = orderInfo + "&sign=" + "\"" + strsign + "\"" + "&"
                    + getSignType();
            Log.v("orderInfo:", info);
            // start the pay.
            // 调用pay方法进行支付
            MobileSecurePayer msp = new MobileSecurePayer();
            boolean bRet = msp.pay(info, mHandler, AlixId.RQF_PAY, this);

            if (bRet) {
                // show the progress bar to indicate that we have started
                // paying.
                // 显示“正在支付”进度条
                closeProgress();
                mProgress = BaseHelper.showProgress(this, null, "正在支付", false,
                        true);
            } else
                ;
        } catch (Exception ex) {
            Toast.makeText(PaymentActivity.this, R.string.remote_call_failed,
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * check some info.the partner,seller etc. 检测配置信息
     * partnerid商户id，seller收款帐号不能为空
     *
     * @return
     */
    private boolean checkInfo() {
        String partner = PartnerConfig.PARTNER;
        String seller = PartnerConfig.SELLER;
        if (partner == null || partner.length() <= 0 || seller == null
                || seller.length() <= 0)
            return false;

        return true;
    }

    //
    // the handler use to receive the pay result.
    // 这里接收支付结果，支付宝手机端同步通知
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            try {
                String ret = (String) msg.obj;

                switch (msg.what) {
                    case AlixId.RQF_PAY: {
                        //
                        closeProgress();


                        // 处理交易结果
                        try {
                            // 获取交易状态码，具体状态代码请参看文档
                            String tradeStatus = "resultStatus={";
                            int imemoStart = ret.indexOf("resultStatus=");
                            imemoStart += tradeStatus.length();
                            int imemoEnd = ret.indexOf("};memo=");
                            tradeStatus = ret.substring(imemoStart, imemoEnd);

                            // 先验签通知
                            ResultChecker resultChecker = new ResultChecker(ret);
                            int retVal = resultChecker.checkSign();
                            // 验签失败
                            if (retVal == ResultChecker.RESULT_CHECK_SIGN_FAILED) {
                                BaseHelper.showDialog(
                                        PaymentActivity.this,
                                        "提示",
                                        getResources().getString(
                                                R.string.check_sign_failed),
                                        android.R.drawable.ic_dialog_alert);
                            } else {// 验签成功。验签成功后再判断交易状态码
                                if (tradeStatus.equals("9000"))// 判断交易状态码，只有9000表示交易成功
                                {
                                    BaseHelper.showDialog(PaymentActivity.this, "提示",
                                            "支付成功。交易状态码：" + tradeStatus,
                                            R.drawable.infoicon);

                                    callPayService();
                                }
                                else
                                {
                                    BaseHelper.showDialog(PaymentActivity.this, "提示",
                                            "支付失败。交易状态码:" + tradeStatus,
                                            R.drawable.infoicon);
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            BaseHelper.showDialog(PaymentActivity.this, "提示", ret,
                                    R.drawable.infoicon);
                        }
                    }
                    break;
                }

                super.handleMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    //
    //
    /**
     * the OnCancelListener for lephone platform. lephone系统使用到的取消dialog监听
     */
//    static class AlixOnCancelListener implements
//            DialogInterface.OnCancelListener {
//        Activity mcontext;
//
//        AlixOnCancelListener(Activity context) {
//            mcontext = context;
//        }
//
//        public void onCancel(DialogInterface dialog) {
//            mcontext.onKeyDown(KeyEvent.KEYCODE_BACK, null);
//        }
//    }

    //
    // close the progress bar
    // 关闭进度框
    void closeProgress() {
        try {
            if (mProgress != null) {
                mProgress.dismiss();
                mProgress = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //
    @Override
    public void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mPackageInstallationListener);

        try {
            mProgress.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
