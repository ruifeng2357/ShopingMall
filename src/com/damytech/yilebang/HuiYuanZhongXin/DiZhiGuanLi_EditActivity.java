package com.damytech.yilebang.HuiYuanZhongXin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.damytech.CommService.CommMgr;
import com.damytech.Global.GlobalData;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.STData.STReceiverInfo;
import com.damytech.STData.STServiceData;
import com.damytech.Utils.CitySelector;
import com.damytech.Utils.ResolutionSet;
import com.damytech.yilebang.MyActivity;
import com.damytech.yilebang.R;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-29
 * Time: 下午2:56
 * To change this template use File | Settings | File Templates.
 */
public class DiZhiGuanLi_EditActivity extends MyActivity {
    // UI control variables
    RelativeLayout m_rlDlgMain;
    TextView    m_txtTitle;
    EditText    m_edtReceiverName;
    EditText    m_edtReceiverPhone;
    EditText    m_edtRemark;
    Spinner     m_spinProvince;
    Spinner     m_spinCity;
    Spinner     m_spinDistrict;
    TextView    m_txtSubmit;

    CitySelector mCitySel = new CitySelector();

    boolean  m_bCreate;
    STReceiverInfo  m_stReceiverInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.huiyuanzhongxin_dizhiguanli_edititem);

        m_bCreate = getIntent().getExtras().getBoolean("create_flag");
        if ( m_bCreate == false ) {
            m_stReceiverInfo = getIntent().getExtras().getParcelable("receiver_info");
        } else {
            m_stReceiverInfo = new STReceiverInfo();
        }

        ResolutionSet._instance.iterateChild(findViewById(R.id.rlDlgMain));

        getControlVariables ();

        connectSignalHandlers();

        loadInitialData();
    }

    private void getControlVariables () {
        m_rlDlgMain = (RelativeLayout) findViewById(R.id.rlDlgMain);
        m_txtTitle = (TextView) findViewById(R.id.txtTitle);
        m_edtReceiverName = (EditText) findViewById(R.id.edtReceiverName);
        m_edtReceiverPhone = (EditText) findViewById(R.id.edtReceiverPhone);
        m_edtRemark = (EditText) findViewById(R.id.edtRemark);
        m_spinProvince = (Spinner) findViewById(R.id.spinProvince);
        m_spinCity = (Spinner) findViewById(R.id.spinCity);
        m_spinDistrict = (Spinner) findViewById(R.id.spinDistrict);
        m_txtSubmit = (TextView) findViewById(R.id.txtSubmit);

        mCitySel.SetParent(DiZhiGuanLi_EditActivity.this);
        mCitySel.SetWidget(m_spinProvince, m_spinCity, m_spinDistrict, m_edtRemark);
        mCitySel.loadSpinner();
    }

    private void connectSignalHandlers () {
        m_txtSubmit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( checkInput() == false )
                    return;

                if ( m_bCreate == true ) {
                    callAddReceiver();
                } else {
                    callUpdateReceiver();
                }
            }
        });
    }

    private void loadInitialData () {
        if ( m_bCreate == false ) {
            m_txtTitle.setText(getString(R.string.HuiYuanZhongXin_DiZhiGuanLi_XiuGaiShouHuoRenXinXi));
            m_txtSubmit.setText(R.string.HuiYuanZhongXin_DiZhiGuanLi_TiJiaoXiuGai);

            m_edtReceiverName.setText(m_stReceiverInfo.name);
            m_edtReceiverPhone.setText(m_stReceiverInfo.phone);
            m_edtRemark.setText(m_stReceiverInfo.addrDetail);

        } else {
            m_txtTitle.setText(getString(R.string.HuiYuanZhongXin_DiZhiGuanLi_XinZengShouHuoRenXinXi));
            m_txtSubmit.setText(R.string.HuiYuanZhongXin_DiZhiGuanLi_TiJiaoXinZeng);
        }
    }

    private boolean checkInput () {
        String  receiverName, receiverPhone, remark;
        String  province, city, district;

        receiverName = m_edtReceiverName.getText().toString();
        receiverPhone = m_edtReceiverPhone.getText().toString();
        remark = m_edtRemark.getText().toString();
        province = m_spinProvince.getSelectedItem().toString();
        city = m_spinCity.getSelectedItem().toString();
        district = m_spinDistrict.getSelectedItem().toString();

        // empty check of input text
        if ( receiverName.isEmpty() ) {
            GlobalData.showToast(DiZhiGuanLi_EditActivity.this, getString(R.string.HuiYuanZhongXin_DiZhiGuanLi_Msg_ReceiverNameEmpty));
            return false;
        }

        if ( receiverPhone.isEmpty() ) {
            GlobalData.showToast(DiZhiGuanLi_EditActivity.this, getString(R.string.HuiYuanZhongXin_DiZhiGuanLi_Msg_ReceiverPhoneEmpty));
            return false;
        }

        if (remark.isEmpty())
        {
            GlobalData.showToast(DiZhiGuanLi_EditActivity.this, getString(R.string.HuiYuanZhongXin_DiZhiGuanLi_Msg_ReceiverAddrEmpty));
            return false;
        }

        // pattern check of input text
        if (!GlobalData.isValidName(receiverName))
        {
            GlobalData.showToast(DiZhiGuanLi_EditActivity.this, getString(R.string.HuiYuanZhongXin_DiZhiGuanLi_Msg_ReceiverNameInvalid));
            return false;
        }

        if (!GlobalData.isValidPhone(receiverPhone))
        {
            GlobalData.showToast(DiZhiGuanLi_EditActivity.this, getString(R.string.MSG_Invalid_PhoneNum));
            return false;
        }

        m_stReceiverInfo.name = receiverName;
        m_stReceiverInfo.phone = receiverPhone;
        m_stReceiverInfo.addrDetail = remark;
        m_stReceiverInfo.province = province;
        m_stReceiverInfo.city = city;
        m_stReceiverInfo.area = district;

        return true;
    }

    private void callAddReceiver () {
        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = "";

                GlobalData.showToast(DiZhiGuanLi_EditActivity.this, getString(R.string.HuiYuanZhongXin_DiZhiGuanLi_Msg_ReceiverAddedSuccessfully));

                retMsg = CommMgr.commService.parseAddReceiver(object);
                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    result = STServiceData.ERR_SUCCESS;

                    finish();
                }
                else
                {
                    result = STServiceData.ERR_EXCEPTION;
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
                    GlobalData.showToast(DiZhiGuanLi_EditActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                DiZhiGuanLi_EditActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.AddReceiver(GlobalData.token, m_stReceiverInfo, handler);
    }

    private void callUpdateReceiver () {
        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = "";

                GlobalData.showToast(DiZhiGuanLi_EditActivity.this, getString(R.string.HuiYuanZhongXin_DiZhiGuanLi_Msg_ReceiverModifiedSuccessfully));

                retMsg = CommMgr.commService.parseUpdateReceiver(object);
                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    result = STServiceData.ERR_SUCCESS;

                    finish();
                }
                else
                {
                    result = STServiceData.ERR_EXCEPTION;
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
                    GlobalData.showToast(DiZhiGuanLi_EditActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                DiZhiGuanLi_EditActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.UpdateReceiver(GlobalData.token, m_stReceiverInfo, handler);
    }
}
