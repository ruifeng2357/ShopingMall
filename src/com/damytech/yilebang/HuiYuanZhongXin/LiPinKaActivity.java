package com.damytech.yilebang.HuiYuanZhongXin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.damytech.CommService.CommMgr;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.STData.STServiceData;
import com.damytech.Global.GlobalData;
import com.damytech.Utils.ResolutionSet;
import com.damytech.yilebang.MyActivity;
import com.damytech.yilebang.R;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-22
 * Time: 下午7:28
 * To change this template use File | Settings | File Templates.
 */
public class LiPinKaActivity extends MyActivity {
    // UI control variables
    RelativeLayout  m_rlMain;
    RelativeLayout  m_rlHeader;
    TextView  m_txtInquire;
    RelativeLayout  m_rlBody;
    EditText  m_edtGiftCardNo;
    EditText  m_edtGiftCardPassword;
    EditText  m_edtPhone;
    EditText m_edtVerificationCode;
    TextView  m_txtSend;
    RelativeLayout m_rlFooter;
    ImageButton  m_imgbtnBack;
    ImageButton m_imgbtnAccount;
    TextView m_txtActivate;

    String  m_szCardNo;
    String  m_szCardPassword;
    String  m_szBindedPhone;
    String  m_szCaptcha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.huiyuanzhongxin_lipinka);

        ResolutionSet._instance.iterateChild(findViewById(R.id.rlMain));

        getControlVariables ();

        connectSignalHandlers();

        loadInitialData();
    }

    private void getControlVariables () {
        m_rlMain = (RelativeLayout) findViewById(R.id.rlMain);
        m_rlHeader = (RelativeLayout) findViewById(R.id.rlHeader);
        m_txtInquire = (TextView) findViewById(R.id.txtInquire);
        m_rlBody = (RelativeLayout) findViewById(R.id.rlBody);
        m_edtGiftCardNo = (EditText) findViewById(R.id.edtGiftCardNo);
        m_edtGiftCardPassword = (EditText) findViewById(R.id.edtGiftCardPassword);
        m_edtPhone = (EditText) findViewById(R.id.edtPhone);
        m_edtVerificationCode = (EditText) findViewById(R.id.edtVerificationCode);
        m_txtSend = (TextView) findViewById(R.id.txtSend);
        m_rlFooter = (RelativeLayout) findViewById(R.id.rlFooter);
        m_imgbtnBack = (ImageButton) findViewById(R.id.imgbtnBack);
        m_imgbtnAccount = (ImageButton) findViewById(R.id.imgbtnAccount);
        m_txtActivate = (TextView) findViewById(R.id.txtActivate);
    }

    private void connectSignalHandlers () {
        m_txtInquire.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGiftCardListActivity();
            }
        });

        m_txtSend.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( verifyBindedPhone() == true ) {
                    // FIXME: to do some code
                }
            }
        });

        m_txtActivate.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( verifyParams() == true ) {
                    callBindGiftCard();
                }
            }
        });

        m_imgbtnBack.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgbtnBackClicked();
            }
        });
    }

    private void loadInitialData () {
    }

    private boolean verifyParams () {
        m_szCardNo = m_edtGiftCardNo.getText().toString();
        m_szCardPassword = m_edtGiftCardPassword.getText().toString();
        m_szCaptcha = m_edtVerificationCode.getText().toString();

        if ( m_szCardNo.isEmpty() ) {
            GlobalData.showToast(LiPinKaActivity.this, getString(R.string.HuiYuanZhongXin_LiPinKa_Msg_CardNoShouldNotBeEmpty));
            return false;
        }

        if ( m_szCardPassword.isEmpty() ) {
            GlobalData.showToast(LiPinKaActivity.this, getString(R.string.HuiYuanZhongXin_LiPinKa_Msg_CardPasswordShouldNotBeEmpty));
            return false;
        }

        if ( verifyBindedPhone() == false ) {
            return false;
        }

        if ( m_szCaptcha.isEmpty() ) {
            GlobalData.showToast(LiPinKaActivity.this, getString(R.string.HuiYuanZhongXin_LiPinKa_Msg_CaptchaShouldNotBeEmpty));
            return false;
        }

        return true;
    }

    private boolean verifyBindedPhone () {
        m_szBindedPhone = m_edtPhone.getText().toString();

        if ( m_szBindedPhone.isEmpty() ) {
            GlobalData.showToast(LiPinKaActivity.this, getString(R.string.HuiYuanZhongXin_LiPinKa_Msg_PhoneNoShouldNotBeEmpty));
            return false;
        }

        return true;
    }

    private void goToGiftCardListActivity () {
        Intent intent = new Intent(LiPinKaActivity.this, LiPinKa_LieBiaoActivity.class);
        startActivity(intent);
        LiPinKaActivity.this.finish();
    }

    private void callBindGiftCard () {
        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = "";

                progDialog.dismiss();

                retMsg = CommMgr.commService.parseBindPresentCard(object);
                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    result = STServiceData.ERR_SUCCESS;

                    // FIXME: to do some code for success dialog.
                    return;
                }
                else
                {
                    result = STServiceData.ERR_EXCEPTION;
                }

                GlobalData.showToast(LiPinKaActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                progDialog.dismiss();

                if (result == STServiceData.ERR_FAIL)
                {
                    GlobalData.showToast(LiPinKaActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                LiPinKaActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.BindPresentCard(GlobalData.token, m_szCardNo, m_szCardPassword, m_szBindedPhone, m_szCaptcha, handler);
    }
}
