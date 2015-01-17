package com.damytech.yilebang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;
import com.damytech.CommService.CommMgr;
import com.damytech.Global.CommonFunc;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.STData.STServiceData;
import com.damytech.Global.GlobalData;
import com.damytech.Utils.ResolutionSet;

import android.os.Bundle;
import com.damytech.yilebang.HuiYuanZhongXin.BangKeZhongXinActivity;
import com.damytech.yilebang.HuiYuanZhongXin.WangJiMiMaActivity;
import org.json.JSONObject;

public class PersonInfoActivity extends MyActivity {
    // UI control variables
    EditText  m_edtUserName;
    EditText  m_edtPassword;
    Button  m_btnRegister;
    Button m_btnLogin;
    TextView m_txtForgotPassword;

    private JsonHttpResponseHandler handler;
    private ProgressDialog progDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personinfo);
		
		ResolutionSet._instance.iterateChild(findViewById(R.id.rlPersonInfoLayout));

        getControlVariables ();

        connectSignalHandlers ();

        initMainMenu();
	}

    private void initMainMenu()
    {
        RelativeLayout rlBottom_Home = (RelativeLayout) findViewById(R.id.rlPersonInfo_Footer_Home);
        rlBottom_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgbtnHomeClicked();
            }
        });

        RelativeLayout rlBootom_Back = (RelativeLayout) findViewById(R.id.rlPersonInfo_Footer_BackArraw);
        rlBootom_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgbtnBackClicked();
            }
        });

        RelativeLayout rlBottom_MainMenu = (RelativeLayout) findViewById(R.id.rlPersonInfo_Footer_MainMenu);
        rlBottom_MainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgbtnMainMenuClicked();
            }
        });

        RelativeLayout rlBottom_Packet = (RelativeLayout) findViewById(R.id.rlPersonInfo_Footer_Packet);
        rlBottom_Packet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgbtnCartClicked();
            }
        });

        RelativeLayout rlBottom_PersonInfo = (RelativeLayout) findViewById(R.id.rlPersonInfo_Footer_PersonInfo);
        rlBottom_PersonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgbtnAccountClicked();
            }
        });
    }

    private void getControlVariables () {
        m_edtUserName = (EditText) findViewById(R.id.txtPersonInfo_UserName);
        m_edtPassword = (EditText) findViewById(R.id.txtPersonInfo_Password);
        m_btnRegister = (Button) findViewById(R.id.btnPersonInfo_Register);
        m_btnLogin = (Button) findViewById(R.id.btnPersonInfo_Login);
        m_txtForgotPassword = (TextView) findViewById(R.id.lblPersonInfo_ForgotPassword);
        m_txtForgotPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    Intent intent = new Intent(PersonInfoActivity.this, WangJiMiMaActivity.class);
                    startActivity(intent);
                    finish();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void connectSignalHandlers () {
        m_btnLogin.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v)
            {
                OnClickedLogin();
            }
        });
        m_btnRegister.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v)
            {
                OnClickedRegister();
            }
        });
    }

    /**
     * Implementation of login button clicked event
     */
    private void OnClickedLogin()
    {
        String sName = m_edtUserName.getText().toString();
        String sPwd = m_edtPassword.getText().toString();

        if (sName.isEmpty())
        {
            return;
        }
        if (sPwd.isEmpty())
        {
            return;
        }

        // call login service
        callLoginService(sName, sPwd);
    }

    /**
     * Implementation of register button clicked event
     */
    private void OnClickedRegister()
    {
        Intent intent = new Intent(PersonInfoActivity.this, RegisterRequestSMSActivity.class);
        startActivity(intent);
    }

    /**
     * Save current user account to preference
     */
    private void saveUserAccount()
    {
        String sName = m_edtUserName.getText().toString();
        String sPwd = m_edtPassword.getText().toString();

        CommonFunc.SaveAccount(this, sName, sPwd);
    }

    /////////////////////////////////////// Service Relation //////////////////////////////////////
    /**
     * Call login service
     * @param sName [in], user login name
     * @param sPwd [in], user login password
     */
    private void callLoginService(String sName, String sPwd)
    {
        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = STServiceData.MSG_SUCCESS;

                progDialog.dismiss();

                retMsg = CommMgr.commService.parseLoginUser(object);
                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    result = STServiceData.ERR_SUCCESS;
                    saveUserAccount();
                    // get advertisement list of main page
                    GoToNext();
                    return;
                }
                else
                {
                    result = STServiceData.ERR_EXCEPTION;
                }

                GlobalData.showToast(PersonInfoActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                progDialog.dismiss();
                if (result == STServiceData.ERR_FAIL)
                {
                    GlobalData.showToast(PersonInfoActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                PersonInfoActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.LoginUser(sName, sPwd, handler);

        return;
    }


    //////////////////////////////////// Activity Action ///////////////////////////////////
    private void GoToNext()
    {
        finish();
    }
}
