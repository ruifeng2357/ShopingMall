package com.damytech.yilebang.HuiYuanZhongXin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.damytech.Utils.ResolutionSet;
import com.damytech.yilebang.MyActivity;
import com.damytech.yilebang.R;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-22
 * Time: 下午7:20
 * To change this template use File | Settings | File Templates.
 */
public class BangKeActivity extends MyActivity {
    // UI control variables
    RelativeLayout  m_rlMain;
    RelativeLayout  m_rlHeader;
    ImageButton  m_imgbtnRefresh;
    RelativeLayout m_rlFooter;
    ImageButton  m_imgbtnBack;
    ImageButton  m_imgbtnAccount;
    TextView  m_txtToBeBangKe;
	ImageButton m_btnToBangKe;

//	int state_id = 0;
//	double bangbi = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.huiyuanzhongxin_bangke);

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
        m_rlFooter = (RelativeLayout) findViewById(R.id.rlFooter);
        m_imgbtnBack = (ImageButton) findViewById(R.id.imgbtnBack);
        m_imgbtnAccount = (ImageButton) findViewById(R.id.imgbtnAccount);
        m_txtToBeBangKe = (TextView) findViewById(R.id.txtToBeBangKe);
		m_btnToBangKe = (ImageButton)findViewById(R.id.btnToBangKe);
    }

    private void connectSignalHandlers () {
		m_btnToBangKe.setOnClickListener(new View.OnClickListener()
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
		Intent intent = new Intent(BangKeActivity.this, BangKe_ShenQing_1Activity.class);

//		intent.putExtra("StateID", state_id);
//		intent.putExtra("BangBi", bangbi);

		startActivity(intent);
	}
}
