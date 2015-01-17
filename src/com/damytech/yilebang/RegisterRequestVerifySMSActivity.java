package com.damytech.yilebang;

import android.app.ProgressDialog;
import android.widget.EditText;
import com.damytech.CommService.CommMgr;
import com.damytech.Global.CommonFunc;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.STData.STServiceData;
import com.damytech.Global.GlobalData;
import com.damytech.Utils.ResolutionSet;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;
import org.json.JSONObject;

public class RegisterRequestVerifySMSActivity extends Activity {
	
	private Button btnSend;
    private EditText txtUserName;
    private EditText txtUserPwd;
    private EditText txtConfirmPwd;
    private EditText txtPhoneNum;

    private String userPhoneNum;

    private JsonHttpResponseHandler handler;
    private JsonHttpResponseHandler handler1;
    private ProgressDialog progDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registerrequestverifysms);

        userPhoneNum = getIntent().getExtras().getString("PhoneNum");

        initControls();
	}

    private void initControls()
    {
        txtUserName = (EditText) findViewById(R.id.lblRegisterRequestVerifySMS_UserNameValue);
        txtUserPwd = (EditText) findViewById(R.id.txtRegisterRequestVerifySMS_PasswordValue);
        txtConfirmPwd = (EditText) findViewById(R.id.txtRegisterRequestVerifySMS_RePasswordValue);
        txtPhoneNum = (EditText) findViewById(R.id.txtRegisterRequestVerifySMS_RecommendPhoneNumberValue);

        btnSend = (Button) findViewById(R.id.btnRegisterRequestVerify_Send);
        btnSend.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v)
            {
                OnClickedNext();
            }
        });

        ResolutionSet._instance.iterateChild(findViewById(R.id.rlRegisterRequestVerifySMSLayout));
    }

    /**
     * Implementation of login button clicked event
     */
    private void OnClickedNext()
    {
        String sUserName = txtUserName.getText().toString();
        String sUserPwd = txtUserPwd.getText().toString();
        String sConfirmPwd = txtConfirmPwd.getText().toString();
        String sPhoneNum = txtPhoneNum.getText().toString();

        if (sUserName.isEmpty())
        {
            GlobalData.showToast(RegisterRequestVerifySMSActivity.this, getString(R.string.Register_Username_Error));
            return;
        }
        if (sUserPwd.isEmpty())
        {
            GlobalData.showToast(RegisterRequestVerifySMSActivity.this, getString(R.string.Register_Password_Error));
            return;
        }
        if (sConfirmPwd.isEmpty())
        {
            GlobalData.showToast(RegisterRequestVerifySMSActivity.this, getString(R.string.Register_Password_Error));
            return;
        }
        if (!sUserPwd.equals(sConfirmPwd))
        {
            GlobalData.showToast(RegisterRequestVerifySMSActivity.this, getString(R.string.Register_Repassword_Error));
            return;
        }

        // check phone number pattern
        if (sPhoneNum.length() > 0)
        {
            if (!GlobalData.isValidPhone(sPhoneNum))
            {
                GlobalData.showToast(this, getString(R.string.MSG_Invalid_PhoneNum));
                return;
            }
        }

        if (sUserName.length() > 0)
        {
            if (!GlobalData.isValidName(sUserName))
            {
                GlobalData.showToast(this, getString(R.string.MSG_Invalid_UserName));
                return;
            }
        }

        // call register new user service
        callRegUserService(sUserName, sUserPwd, sPhoneNum);
    }

    /**
     * Save current user account to preference
     */
    private void saveUserAccount()
    {
        String sName = txtUserName.getText().toString();
        String sPwd = txtUserPwd.getText().toString();

        CommonFunc.SaveAccount(this, sName, sPwd);
    }

    /**
     * Call register new user
     * @param sUserName [in], new user name
     * @param sUserPwd [in], new user password
     * @param sPhoneNum [in], recommend user phone number
     */
    private void callRegUserService(String sUserName, String sUserPwd, String sPhoneNum)
    {
        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = STServiceData.MSG_SUCCESS;

                progDialog.dismiss();

                retMsg = CommMgr.commService.parseRegisterUser(object);
                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    result = STServiceData.ERR_SUCCESS;
                    callLoginService( txtUserName.getText().toString(), txtUserPwd.getText().toString());

                    return;
                }
                else
                {
                    result = STServiceData.ERR_EXCEPTION;
                }

                GlobalData.showToast(RegisterRequestVerifySMSActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                progDialog.dismiss();
                if (result == STServiceData.ERR_FAIL)
                    GlobalData.showToast(RegisterRequestVerifySMSActivity.this, getString(R.string.server_connection_error));

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                RegisterRequestVerifySMSActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.RegisterUser(sUserName, sUserPwd, userPhoneNum, sPhoneNum, handler);

        return;
    }

    /**
     * Call login service
     * @param sName [in], user login name
     * @param sPwd [in], user login password
     */
    private void callLoginService(String sName, String sPwd)
    {
        handler1 = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;
            String retMsg = STServiceData.MSG_SUCCESS;

            @Override
            public void onSuccess(JSONObject object)
            {
                retMsg = CommMgr.commService.parseLoginUser(object);
                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    result = STServiceData.ERR_SUCCESS;
                    // get advertisement list of main page

                    GlobalData.fileWriteToken(RegisterRequestVerifySMSActivity.this, GlobalData.token);

                    NewsService.m_ctxMain = RegisterRequestVerifySMSActivity.this;
                    Intent intent = new Intent(RegisterRequestVerifySMSActivity.this, NewsService.class);
                    RegisterRequestVerifySMSActivity.this.startService(intent);

                    saveUserAccount();


                    Intent intent1 = new Intent(RegisterRequestVerifySMSActivity.this, BaseServiceActivity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent1);
                    finish();

                    return;
                }
                else
                {
                    progDialog.dismiss();
                    result = STServiceData.ERR_EXCEPTION;
                }

                GlobalData.showToast(RegisterRequestVerifySMSActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                if (result == STServiceData.ERR_FAIL)
                {
                    progDialog.dismiss();
                    GlobalData.showToast(RegisterRequestVerifySMSActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                RegisterRequestVerifySMSActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.LoginUser(sName, sPwd, handler1);

        return;
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(RegisterRequestVerifySMSActivity.this, RegisterRequestSMSActivity.class);
        intent.putExtra("PhoneNum", userPhoneNum);
        startActivity(intent);
        finish();
    }

}
