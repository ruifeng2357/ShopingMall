package com.damytech.yilebang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.widget.*;
import com.damytech.Global.CommonFunc;
import com.damytech.Global.GlobalData;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.Utils.ResolutionSet;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.app.Activity;

public class RegisterRequestSMSActivity extends Activity {

    private CheckBox chkAgree;
	private Button btnRequestSMS;
    private EditText txtPhoneNum;
    private TextView txtPhoneHeader;

    private JsonHttpResponseHandler handler;
    private ProgressDialog progDialog;

    @Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registerrequestsms);
		
        initControls();
	}

    private void initControls()
    {
        String sPhone = "";
        try
        {
            sPhone = getIntent().getExtras().getString("PhoneNum", "");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        txtPhoneNum = (EditText) findViewById(R.id.txtRegisterRequestSMS_PhoneNumberBody);
        txtPhoneNum.setText(sPhone);

        txtPhoneHeader = (TextView) findViewById(R.id.txtRegisterRequestSMS_PhoneNumberHeader);

        btnRequestSMS = (Button) findViewById(R.id.btnRegisterRequestSMS_RequestSMS);
        btnRequestSMS.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v)
            {
                OnClickedNext();
            }
        });

        chkAgree = (CheckBox) findViewById(R.id.chkRegisterRequestSMS_Agree);
//        chkAgree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                btnRequestSMS.setEnabled(isChecked);
//            }
//        });

        ResolutionSet._instance.iterateChild(findViewById(R.id.rlRegisterRequestSMSLayout));
    }

    private void OnClickedNext()
    {
        // check box state
        if (!chkAgree.isChecked())
        {
            GlobalData.showToast(this, getString(R.string.Register_MSG_SelectChkBox));
            return;
        }

        // get phone number string
        String sPhoneNum = txtPhoneNum.getText().toString();
        if (sPhoneNum.length() <= 0)
        {
            GlobalData.showToast(this, getString(R.string.RegisterRequest_InputPhoneNum));
            return;
        }

        // check phone number pattern
        if (!GlobalData.isValidPhone(sPhoneNum))
        {
            GlobalData.showToast(this, getString(R.string.MSG_Invalid_PhoneNum));
            return;
        }

        Intent intent = new Intent(RegisterRequestSMSActivity.this, RegisterRequestVerifySMSActivity.class);
        intent.putExtra("PhoneNum", sPhoneNum);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(RegisterRequestSMSActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
