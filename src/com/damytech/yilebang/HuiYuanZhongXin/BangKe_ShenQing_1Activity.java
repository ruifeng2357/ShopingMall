package com.damytech.yilebang.HuiYuanZhongXin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.damytech.Global.GlobalData;
import com.damytech.Utils.ResolutionSet;
import com.damytech.yilebang.MyActivity;
import com.damytech.yilebang.R;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-22
 * Time: 下午7:25
 * To change this template use File | Settings | File Templates.
 */
public class BangKe_ShenQing_1Activity extends MyActivity {
    // UI control variables
    RelativeLayout  m_rlMain;
    RelativeLayout  m_rlHeader;
    ImageButton  m_imgbtnRefresh;
    RelativeLayout  m_rlBody;
    TextView  m_txtStep1No;
    TextView  m_txtStep1Name;
    TextView  m_txtStep2No;
    TextView  m_txtStep2Name;
    TextView  m_txtStep3No;
    TextView  m_txtStep3Name;
    EditText  m_edtName;
    EditText  m_edtIDCardNo;
    EditText  m_edtBankCard;
    EditText  m_edtBank;
    EditText  m_edtDesc;
    TextView  m_txtRule;
    RelativeLayout  m_rlFooter;
    ImageButton  m_imgbtnBack;
    ImageButton  m_imgbtnAccount;
	ImageButton m_btnNext;
    TextView  m_txtNext;

//	private int state_id = 0;
//	private double bangbi = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.huiyuanzhongxin_bangke_shenqing_1);

        ResolutionSet._instance.iterateChild(findViewById(R.id.rlMain));

        getControlVariables ();

        connectSignalHandlers();

        loadInitialData();

		loadExtras();
    }

    private void getControlVariables () {
        m_rlMain = (RelativeLayout) findViewById(R.id.rlMain);
        m_rlHeader = (RelativeLayout) findViewById(R.id.rlHeader);
        m_imgbtnRefresh = (ImageButton) findViewById(R.id.imgbtnRefresh);
        m_rlBody = (RelativeLayout) findViewById(R.id.rlBody);
        m_txtStep1No = (TextView) findViewById(R.id.txtStep1No);
        m_txtStep1Name = (TextView) findViewById(R.id.txtStep1Name);
        m_txtStep2No = (TextView) findViewById(R.id.txtStep2No);
        m_txtStep2Name = (TextView) findViewById(R.id.txtStep2Name);
        m_txtStep3No = (TextView) findViewById(R.id.txtStep3No);
        m_txtStep3Name = (TextView) findViewById(R.id.txtStep3Name);
        m_edtName = (EditText) findViewById(R.id.edtName);
		m_edtName.setFocusable(true);
        m_edtIDCardNo = (EditText) findViewById(R.id.edtIDCardNo);
		m_edtIDCardNo.setFocusable(true);
        m_edtBankCard = (EditText) findViewById(R.id.edtBankCard);
		m_edtBankCard.setFocusable(true);
        m_edtBank = (EditText) findViewById(R.id.edtBank);
		m_edtBank.setFocusable(true);
        m_edtDesc = (EditText) findViewById(R.id.edtDesc);
        m_txtRule = (TextView) findViewById(R.id.txtRule);
        m_rlFooter = (RelativeLayout) findViewById(R.id.rlFooter);
        m_imgbtnBack = (ImageButton) findViewById(R.id.imgbtnBack);
        m_imgbtnAccount = (ImageButton) findViewById(R.id.imgbtnAccount);
		m_btnNext = (ImageButton)findViewById(R.id.btnNext);
        m_txtNext = (TextView) findViewById(R.id.txtNext);
    }

    private void connectSignalHandlers () {
		m_btnNext.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onClickNext();
			}
		});

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
    }

    private void loadInitialData () {

    }

	private void loadExtras()
	{
//		state_id = getIntent().getIntExtra("StateID", 0);
//		bangbi = getIntent().getDoubleExtra("BangBi", 0);
	}

	private void onClickNext()
	{
		String szName = m_edtName.getText().toString();
		String szIDNo = m_edtIDCardNo.getText().toString();
		String szBankCard = m_edtBankCard.getText().toString();
		String szBankName = m_edtBank.getText().toString();
		String szDesc = m_edtDesc.getText().toString();

		if (szName.equals(""))
		{
			GlobalData.showToast(BangKe_ShenQing_1Activity.this, getResources().getString(R.string.HuiYuanZhongXin_RealName_CanNotEmpy));
			m_edtName.requestFocus();
			m_edtName.selectAll();
		}
		else if (szIDNo.equals(""))
		{
			GlobalData.showToast(BangKe_ShenQing_1Activity.this, getResources().getString(R.string.HuiYuanZhongXin_IDNO_CanNotEmpy));
			m_edtIDCardNo.requestFocus();
			m_edtIDCardNo.selectAll();
		}
		else if (szBankCard.equals(""))
		{
			GlobalData.showToast(BangKe_ShenQing_1Activity.this, getResources().getString(R.string.HuiYuanZhongXin_BankCard_CanNotEmpy));
			m_edtBankCard.requestFocus();
			m_edtBankCard.selectAll();
		}
		else if (szBankName.equals(""))
		{
			GlobalData.showToast(BangKe_ShenQing_1Activity.this, getResources().getString(R.string.HuiYuanZhongXin_BankName_CanNotEmpy));
			m_edtBank.requestFocus();
			m_edtBank.selectAll();
		}
		else
		{
			Intent intent = new Intent(BangKe_ShenQing_1Activity.this, BangKe_ShenQing_2Activity.class);

			intent.putExtra("Name", szName);
			intent.putExtra("IDNO", szIDNo);
			intent.putExtra("BankCard", szBankCard);
			intent.putExtra("BankName", szBankName);
			intent.putExtra("Desc", szDesc);

			startActivity(intent);
		}
	}
}
