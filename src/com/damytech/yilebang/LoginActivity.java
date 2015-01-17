package com.damytech.yilebang;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.TextView;
import com.damytech.CommService.CommMgr;
import com.damytech.Global.CommonFunc;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.Global.GlobalData;
import com.damytech.STData.STInteger;
import com.damytech.Utils.ResolutionSet;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import org.json.JSONObject;
import com.damytech.STData.STServiceData;

public class LoginActivity extends Activity {

    private Button btnLogin;
	private Button btnRegister;
    private TextView txtSkip;

    private EditText txtUserName;
    private EditText txtPassword;

    private JsonHttpResponseHandler handler;
    private JsonHttpResponseHandler cartRefHandler;
    private ProgressDialog progDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

        // initialize all controls
        initControls();
	}

    /**
     * Initialize all controls
     */
    private void initControls()
    {
        txtUserName = (EditText) findViewById(R.id.txtLogin_UserNameValue);
        txtPassword = (EditText) findViewById(R.id.txtLogin_PasswordValue);

        btnLogin = (Button) findViewById(R.id.btnLogin_Login);
        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickedLogin();
            }
        });

        btnRegister = (Button) findViewById(R.id.btnLogin_Register);
        btnRegister.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v)
            {
                OnClickedRegister();
            }
        });

        txtSkip = (TextView)findViewById(R.id.lblLogin_Skip);
        txtSkip.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v)
            {
                GoToNext();
            }
        });

        ResolutionSet._instance.iterateChild(findViewById(R.id.rlLoginLayout));

        loadAccount();
    }

    /**
     * Load last user account
     */
    private void loadAccount()
    {
        // load last account
        String sName = CommonFunc.LoadAccountName(this);
        String sPwd = CommonFunc.LoadAccountPwd(this);
        if (sName.isEmpty() || sPwd.isEmpty())
            return;

        txtUserName.setText(sName);
        txtPassword.setText(sPwd);

        // call login service
        callLoginService(sName, sPwd);
    }

    /**
     * Implementation of login button clicked event
     */
    private void OnClickedLogin()
    {
        String sName = txtUserName.getText().toString();
        String sPwd = txtPassword.getText().toString();

        if (sName.isEmpty())
        {
            GlobalData.showToast(LoginActivity.this, getString(R.string.Register_Username_Error));
            return;
        }
        if (sPwd.isEmpty())
        {
            GlobalData.showToast(LoginActivity.this, getString(R.string.Register_Password_Error));
            return;
        }

        // call login service
        callLoginService(sName, sPwd);
    }

    /**
     * Save current user account to preference
     */
    private void saveUserAccount()
    {
        String sName = txtUserName.getText().toString();
        String sPwd = txtPassword.getText().toString();

        CommonFunc.SaveAccount(this, sName, sPwd);
    }

    /**
     * Implementation of register button clicked event
     */
    private void OnClickedRegister()
    {
        Intent intent = new Intent(LoginActivity.this, RegisterRequestSMSActivity.class);
        startActivity(intent);
        finish();
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
            String retMsg = STServiceData.MSG_SUCCESS;

            @Override
            public void onSuccess(JSONObject object)
            {
                retMsg = CommMgr.commService.parseLoginUser(object);
                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    result = STServiceData.ERR_SUCCESS;
                    // get advertisement list of main page

                    GlobalData.fileWriteToken(LoginActivity.this, GlobalData.token);

                    NewsService.m_ctxMain = LoginActivity.this;
                    Intent intent = new Intent(LoginActivity.this, NewsService.class);
                    LoginActivity.this.startService(intent);

                    callGetCartProductsCount();

                    saveUserAccount();

                    return;
                }
                else
                {
                    progDialog.dismiss();
                    result = STServiceData.ERR_EXCEPTION;
                }

                GlobalData.showToast(LoginActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                if (result == STServiceData.ERR_FAIL)
                {
                    progDialog.dismiss();
                    GlobalData.showToast(LoginActivity.this, getString(R.string.server_connection_error));
                }
                else if (result != STServiceData.ERR_SUCCESS)
                {
                    progDialog.dismiss();
                    GlobalData.showToast(LoginActivity.this, retMsg);
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                LoginActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.LoginUser(sName, sPwd, handler);

        return;
    }

    public void callGetCartProductsCount()
    {
        cartRefHandler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = "";

                progDialog.dismiss();

                retMsg = CommMgr.commService.parseCartProductsCount(object);
                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    result = STServiceData.ERR_SUCCESS;
                    GoToNext();
                    return;
                }
                else
                {
                    result = STServiceData.ERR_EXCEPTION;
                    GlobalData.showToast(LoginActivity.this, retMsg);
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
                    GlobalData.cartProdCount = 0;
                }

                result = 0;
            }

        };

        CommMgr.commService.GetCartProductsCount(GlobalData.token, cartRefHandler);
    }


    //////////////////////////////////// Activity Action ///////////////////////////////////
    private void GoToNext()
    {
        if (!GlobalData.isOnline(this))
        {
            GlobalData.showToast(this, getString(R.string.MSG_NoInternet_Connection));
            return;
        }

        Intent intent = new Intent(LoginActivity.this, BaseServiceActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Exit Current Application
     */
    private void ExitApp()
    {
        finish();
        System.exit(0);
    }

    @Override
    public void onBackPressed()
    {
        ExitApp();
    }

}
