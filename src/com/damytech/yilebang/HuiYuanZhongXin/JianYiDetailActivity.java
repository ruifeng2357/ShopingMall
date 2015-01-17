package com.damytech.yilebang.HuiYuanZhongXin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.damytech.CommService.CommMgr;
import com.damytech.Global.GlobalData;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.STData.STServiceData;
import com.damytech.Utils.ResolutionSet;
import com.damytech.yilebang.MyActivity;
import com.damytech.yilebang.R;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-22
 * Time: 下午7:27
 * To change this template use File | Settings | File Templates.
 */
public class JianYiDetailActivity extends MyActivity {

    private TextView lblTijiao = null;
    private EditText txtSubject;
    private EditText txtEmail;
    private EditText txtContent;
    private ImageView imgBack;
    private ImageView imgReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.huiyuanzhongxin_jianyi_tijiao);

        lblTijiao = (TextView) findViewById(R.id.txtSubmit);
        lblTijiao.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAvailableInsertData() == false)
                    return;

                RunBackgroundHandler();
            }
        });

        txtSubject = (EditText) findViewById(R.id.edtSubject);
        txtEmail = (EditText)findViewById(R.id.txtJianYi_EmailAddress);
        txtContent = (EditText) findViewById(R.id.edtContent);

        imgBack = (ImageView) findViewById(R.id.imgbtnBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JianYiDetailActivity.this.finish();
            }
        });

        imgReset = (ImageView) findViewById(R.id.imgbtnRefresh);
        imgReset.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtSubject.setText("");
                txtEmail.setText("");
                txtContent.setText("");
            }
        });

        ResolutionSet._instance.iterateChild(findViewById(R.id.rlMain));
    }

    @Override
    public  void onStart()
    {
        super.onStart();
    }

    private boolean isAvailableInsertData()
    {
        if (txtSubject.getText().toString().length() == 0)
        {
            GlobalData.showToast(JianYiDetailActivity.this, getString(R.string.JianYi_Subject_Error));
            return false;
        }

        if (txtContent.getText().toString().length() == 0)
        {
            GlobalData.showToast(JianYiDetailActivity.this, getString(R.string.JianYi_Content_Error));
            return false;
        }

        return true;
    }

    private void RunBackgroundHandler()
    {
        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;
            String retMsg = "";

            @Override
            public void onSuccess(JSONObject object)
            {
                progDialog.dismiss();
                retMsg = CommMgr.commService.parseAdvanceOpinion(object) ;

                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    result = STServiceData.ERR_SUCCESS;
                    Intent intent = new Intent(JianYiDetailActivity.this, JianYiActivity.class);
                    startActivity(intent);
                    JianYiDetailActivity.this.finish();
                    return;
                }
                else
                {
                    result = STServiceData.ERR_EXCEPTION;
                }
            }

            @Override
            public void onFailure(Throwable ex, String exception) {
            }

            @Override
            public void onFinish()
            {
                progDialog.dismiss();

                if (result == STServiceData.ERR_FAIL)
                {
                    GlobalData.showToast(JianYiDetailActivity.this, getString(R.string.server_connection_error));
                    Intent intent = new Intent(JianYiDetailActivity.this, JianYiActivity.class);
                    startActivity(intent);
                    JianYiDetailActivity.this.finish();
                }

                result = 0;
            }
        };

        progDialog = ProgressDialog.show(
                JianYiDetailActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.AdvanceOpinion(GlobalData.token, txtSubject.getText().toString(), txtEmail.getText().toString(), txtContent.getText().toString(), handler);
        return;
    }
}
