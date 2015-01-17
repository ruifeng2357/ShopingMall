package com.damytech.yilebang.HuiYuanZhongXin;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.damytech.Utils.ResolutionSet;
import com.damytech.yilebang.MyActivity;
import com.damytech.yilebang.R;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-22
 * Time: 下午7:29
 * To change this template use File | Settings | File Templates.
 */
public class LiPinKa_JiHuoActivity extends MyActivity {
    // UI control variables
    RelativeLayout m_rlDlgMain;
    TextView  m_txtGiftCardNo;
    TextView  m_txtBalance;
    TextView m_txtOk;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.huiyuanzhongxin_lipinka_jihuo);

        ResolutionSet._instance.iterateChild(findViewById(R.id.rlMain));

        getControlVariables ();

        connectSignalHandlers();

        loadInitialData();
    }

    private void getControlVariables () {
        m_rlDlgMain = (RelativeLayout) findViewById(R.id.rlDlgMain);
        m_txtGiftCardNo = (TextView) findViewById(R.id.txtGiftCardNo);
        m_txtBalance = (TextView) findViewById(R.id.txtBalance);
        m_txtOk = (TextView) findViewById(R.id.txtOk);
    }

    private void connectSignalHandlers () {

    }

    private void loadInitialData () {

    }
}
