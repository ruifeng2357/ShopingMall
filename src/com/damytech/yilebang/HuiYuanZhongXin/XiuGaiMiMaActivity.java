package com.damytech.yilebang.HuiYuanZhongXin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
 * Time: 下午7:31
 * To change this template use File | Settings | File Templates.
 */
public class XiuGaiMiMaActivity extends MyActivity {
    // UI control variables
    RelativeLayout  m_rlMain;
    RelativeLayout  m_rlHeader;
    RelativeLayout  m_rlStep1;
    TextView  m_txtNext;
    TextView  m_txtStep1No_;
    TextView  m_txtStep1Name_;
    TextView  m_txtStep2No_;
    TextView  m_txtStep2Name_;
    TextView  m_txtBindedPhone;
    TextView  m_txtGetCaptcha;
    TextView  m_txtContactPhone;
    EditText  m_edtCaptcha;
    RelativeLayout m_rlStep2;
    TextView  m_txtSubmit;
    TextView  m_txtStep1No;
    TextView  m_txtStep1Name;
    TextView  m_txtStep2No;
    TextView m_txtStep2Name;
    EditText  m_edtNewPassword;
    EditText m_edtConfirmPassword;

    int  m_curStep = 0;
    String  m_szBindedPhone;
    String  m_szCaptcha;
    String  m_szNewPassword;
    String  m_szConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.huiyuanzhongxin_xiugaimima);

        ResolutionSet._instance.iterateChild(findViewById(R.id.rlMain));

        getControlVariables ();

        connectSignalHandlers();

        loadInitialData();
    }

    private void getControlVariables () {
        m_rlMain = (RelativeLayout) findViewById(R.id.rlMain);
        m_rlHeader = (RelativeLayout) findViewById(R.id.rlHeader);
        m_rlStep1 = (RelativeLayout) findViewById(R.id.rlStep1);
        m_txtNext = (TextView) findViewById(R.id.txtNext);
        m_txtStep1No_ = (TextView) findViewById(R.id.txtStep1No_);
        m_txtStep1Name_ = (TextView) findViewById(R.id.txtStep1Name_);
        m_txtStep2No_ = (TextView) findViewById(R.id.txtStep2No_);
        m_txtStep2Name_ = (TextView) findViewById(R.id.txtStep2Name_);
        m_txtBindedPhone = (TextView) findViewById(R.id.txtBindedPhone);
        m_txtGetCaptcha = (TextView) findViewById(R.id.txtGetCaptcha);
        m_txtGetCaptcha.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_szBindedPhone = m_txtBindedPhone.getText().toString();
                if (m_szBindedPhone != null && m_szBindedPhone.length() > 0)
                    callRequestVerifyKey ();
            }
        });

        m_txtContactPhone = (TextView) findViewById(R.id.txtContactPhone);
        m_edtCaptcha = (EditText) findViewById(R.id.edtCaptcha);
        m_rlStep2 = (RelativeLayout) findViewById(R.id.rlStep2);
        m_txtSubmit = (TextView) findViewById(R.id.txtSubmit);
        m_txtStep1No = (TextView) findViewById(R.id.txtStep1No);
        m_txtStep1Name = (TextView) findViewById(R.id.txtStep1Name);
        m_txtStep2No = (TextView) findViewById(R.id.txtStep2No);
        m_txtStep2Name = (TextView) findViewById(R.id.txtStep2Name);
        m_edtNewPassword = (EditText) findViewById(R.id.edtNewPassword);
        m_edtConfirmPassword = (EditText) findViewById(R.id.edtConfirmPassword);
    }

    private void connectSignalHandlers () {
        m_txtNext.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSecondStep();
            }
        });

        m_txtSubmit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    private void loadInitialData () {

        m_txtBindedPhone.setText(GlobalData.g_UserInfo.phone);
        goToFirstStep();
    }

    private void goToFirstStep () {
        m_rlStep1.setVisibility(View.VISIBLE);
        m_rlStep2.setVisibility(View.GONE);
        m_curStep = 0;
    }

    private void goToSecondStep () {
        m_szBindedPhone = m_txtBindedPhone.getText().toString();
        m_szCaptcha = m_edtCaptcha.getText().toString();

        if ( m_szBindedPhone.isEmpty() ) {
            GlobalData.showToast(XiuGaiMiMaActivity.this, getString(R.string.HuiYuanZhongXin_XiuGaiMiMa_Msg_PhoneNoShouldNotBeEmpty));
            return;
        }

        if ( m_szCaptcha.isEmpty() ) {
            GlobalData.showToast(XiuGaiMiMaActivity.this, getString(R.string.HuiYuanZhongXin_XiuGaiMiMa_Msg_CaptchaShouldNotBeEmpty));
            return;
        }

        callCheckVerifKey();
    }

    private void submit () {
        if ( m_curStep != 1 ) {
            return;
        }

        m_szNewPassword = m_edtNewPassword.getText().toString();
        m_szConfirmPassword = m_edtConfirmPassword.getText().toString();

        if ( m_szNewPassword.isEmpty() ) {
            GlobalData.showToast(XiuGaiMiMaActivity.this, getString(R.string.HuiYuanZhongXin_XiuGaiMiMa_Msg_PasswordShouldNotBeEmpty));
            return;
        }

        if ( !m_szNewPassword.equals(m_szConfirmPassword) ) {
            GlobalData.showToast(XiuGaiMiMaActivity.this, getString(R.string.HuiYuanZhongXin_XiuGaiMiMa_Msg_PasswordMismatch));
            return;
        }

        callResetPassword();
    }

    private void callResetPassword () {
        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = "";

                progDialog.dismiss();

                retMsg = CommMgr.commService.parseResetPassword(object);
                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    result = STServiceData.ERR_SUCCESS;
                    retMsg = getString(R.string.MSG_Success);
                    finish();
                }
                else
                {
                    result = STServiceData.ERR_EXCEPTION;
                }

                GlobalData.showToast(XiuGaiMiMaActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                progDialog.dismiss();

                if (result == STServiceData.ERR_FAIL)
                {
                    GlobalData.showToast(XiuGaiMiMaActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                XiuGaiMiMaActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.ResetPassword ( GlobalData.token, m_szCaptcha, m_szBindedPhone, m_szNewPassword, handler );
    }

    private void callRequestVerifyKey () {
        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;
            String retMsg = "";

            @Override
            public void onSuccess(JSONObject object)
            {
                progDialog.dismiss();

                retMsg = CommMgr.commService.parseRequestResetVerifyKey(object);
                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    result = STServiceData.ERR_SUCCESS;
                    retMsg = getString(R.string.MSG_Success);
                    GlobalData.showToast(XiuGaiMiMaActivity.this, retMsg);

                    return;
                }
                else
                {
                    result = STServiceData.ERR_EXCEPTION;
                }

                GlobalData.showToast(XiuGaiMiMaActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                progDialog.dismiss();

                if (result == STServiceData.ERR_FAIL)
                {
                    GlobalData.showToast(XiuGaiMiMaActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                XiuGaiMiMaActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.RequestResetVerifyKey(m_szBindedPhone, handler);
    }

    private void callCheckVerifKey () {
        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;
            String retMsg = "";

            @Override
            public void onSuccess(JSONObject object)
            {
                progDialog.dismiss();

                retMsg = CommMgr.commService.parseRequestIsValidVerifKey(object);
                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    result = STServiceData.ERR_SUCCESS;
                    //retMsg = getString(R.string.MSG_Success);
                    //GlobalData.showToast(XiuGaiMiMaActivity.this, retMsg);

                    m_rlStep1.setVisibility(View.GONE);
                    m_rlStep2.setVisibility(View.VISIBLE);
                    m_curStep = 1;

                    return;
                }
                else
                {
                    result = STServiceData.ERR_EXCEPTION;
                }

                GlobalData.showToast(XiuGaiMiMaActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                progDialog.dismiss();

                if (result == STServiceData.ERR_FAIL)
                {
                    GlobalData.showToast(XiuGaiMiMaActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                XiuGaiMiMaActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.RequestIsValidVerifKey(m_szBindedPhone, m_szCaptcha, handler);
    }

    @Override
    public void onBackPressed() {
        if ( m_curStep == 1 ) {
            goToFirstStep ();
        } else {
            finish();
        }
    }
}
