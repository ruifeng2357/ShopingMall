package com.damytech.yilebang.HuiYuanZhongXin;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.damytech.CommService.CommMgr;
import com.damytech.Global.GlobalData;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.Utils.ResolutionSet;
import com.damytech.yilebang.MyActivity;
import com.damytech.yilebang.R;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-22
 * Time: 下午7:25
 * To change this template use File | Settings | File Templates.
 */
public class BangKeZhongXinActivity extends MyActivity {
    // UI control variables
    RelativeLayout  m_rlMain;
    RelativeLayout  m_rlHeader;
    ImageButton  m_imgbtnRefresh;
    RelativeLayout  m_rlBody;
    EditText  m_edtBangBi;
    TextView  m_txtBangBiApplyRule;
    TextView  m_txtBangBiHistory;
    EditText  m_edtIDCardNo;
    EditText  m_edtBankCard;
    EditText  m_edtBank;
    EditText m_edtApplyResult;
    RelativeLayout m_rlFooter;
    ImageButton  m_imgbtnBack;
    ImageButton m_imgbtnAccount;
    TextView m_txtSubmit;
	ImageButton m_btnSubmit;

	private int state_id = 0;
	private double bangbi = 0;
	private String id_no = "";
	private String bank_card = "";
	private String bank_name = "";

	private boolean is_submittable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.huiyuanzhongxin_bangkezhongxin);

        ResolutionSet._instance.iterateChild(findViewById(R.id.rlMain));

        getControlVariables ();

		loadExtras();

		initControls();

        connectSignalHandlers();

        loadInitialData();

    }

    private void getControlVariables () {
        m_rlMain = (RelativeLayout) findViewById(R.id.rlMain);
        m_rlHeader = (RelativeLayout) findViewById(R.id.rlHeader);
        m_imgbtnRefresh = (ImageButton) findViewById(R.id.imgbtnRefresh);
        m_rlBody = (RelativeLayout) findViewById(R.id.rlBody);
        m_edtBangBi = (EditText) findViewById(R.id.edtBangBi);
        m_txtBangBiApplyRule = (TextView) findViewById(R.id.txtBangBiApplyRule);
        m_txtBangBiHistory = (TextView) findViewById(R.id.txtBangBiHistory);
        m_edtIDCardNo = (EditText) findViewById(R.id.edtIDCardNo);
        m_edtBankCard = (EditText) findViewById(R.id.edtBankCard);
        m_edtBank = (EditText) findViewById(R.id.edtBank);
        m_edtApplyResult = (EditText) findViewById(R.id.edtApplyResult);
        m_rlFooter = (RelativeLayout) findViewById(R.id.rlFooter);
        m_imgbtnBack = (ImageButton) findViewById(R.id.imgbtnBack);
        m_imgbtnAccount = (ImageButton) findViewById(R.id.imgbtnAccount);
        m_txtSubmit = (TextView) findViewById(R.id.txtSubmit);
		m_btnSubmit = (ImageButton)findViewById(R.id.btnSubmit);
    }

    private void connectSignalHandlers () {
		m_imgbtnBack.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(getApplicationContext(), HuiYuanZhongXinActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});

		m_btnSubmit.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onSubmit();
			}
		});
    }

    private void loadInitialData () {

    }

	private void loadExtras()
	{
		Object objPhotoUri = null;

		state_id = getIntent().getIntExtra("StateID", 0);
		bangbi = getIntent().getDoubleExtra("BangBi", 0);
		id_no = getIntent().getStringExtra("IdNo");
		bank_card = getIntent().getStringExtra("BankCard");
		bank_name = getIntent().getStringExtra("Bank");

		if (state_id == 2)
			is_submittable = true;
		else
			is_submittable = false;
	}

	private void initControls()
	{
		m_edtApplyResult.setText(GlobalData.BkStateID2String(BangKeZhongXinActivity.this, state_id));
		m_edtApplyResult.setEnabled(false);

//		if (is_submittable)
		{
			m_edtBangBi.setText(String.format("%.2f", bangbi));
//			m_edtBangBi.setEnabled(false);

			m_edtIDCardNo.setText(id_no);
			m_edtIDCardNo.setEnabled(false);

			m_edtBankCard.setText(bank_card);
			m_edtBankCard.setEnabled(false);

			m_edtBank.setText(bank_name);
			m_edtBank.setEnabled(false);
		}
//		else
//		{
//			m_edtBangBi.setText(String.format("%.2f", bangbi));
//			m_edtIDCardNo.setText("");
//			m_edtBankCard.setText("");
//			m_edtBank.setText("");
//		}
	}

	private void onSubmit()
	{
		if (is_submittable)
		{
			String szBangBi = m_edtBangBi.getText().toString();
			double fBangBi = Double.parseDouble(szBangBi);
			if (fBangBi > bangbi)
			{
				GlobalData.showToast(BangKeZhongXinActivity.this, getResources().getString(R.string.InputCorrectBangBi));
			}
			else if (bangbi < 50)
			{
				GlobalData.showToast(BangKeZhongXinActivity.this, getResources().getString(R.string.HuiYuanZhongXin_NotEnoughBangBi));
			}
			else
			{
				CommMgr.commService.RequestDrawBangbi(GlobalData.token, id_no, bank_card, fBangBi, submit_handler);
			}
		}
		else
		{
			GlobalData.showToast(BangKeZhongXinActivity.this, getResources().getString(R.string.StateNotAbleToSubmit));
		}
	}

	private JsonHttpResponseHandler submit_handler  = new JsonHttpResponseHandler()
	{
		@Override
		public void onSuccess(JSONObject response)
		{
			super.onSuccess(response);    //To change body of overridden methods use File | Settings | File Templates.

			String szRes = CommMgr.commService.parseDrawBangbi(response);
			if (szRes.equals(""))
			{
				GlobalData.showToast(BangKeZhongXinActivity.this, getResources().getString(R.string.MSG_Success));
			}
			else
			{
				GlobalData.showToast(BangKeZhongXinActivity.this, szRes);
			}
		}

		@Override
		public void onFailure(Throwable e, JSONObject errorResponse)
		{
			super.onFailure(e, errorResponse);    //To change body of overridden methods use File | Settings | File Templates.
			GlobalData.showToast(BangKeZhongXinActivity.this, getResources().getString(R.string.server_connection_error));
		}
	};
}
