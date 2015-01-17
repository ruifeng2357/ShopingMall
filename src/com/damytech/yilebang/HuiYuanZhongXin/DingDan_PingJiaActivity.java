package com.damytech.yilebang.HuiYuanZhongXin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.damytech.CommService.CommMgr;
import com.damytech.Global.GlobalData;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.STData.STProductA;
import com.damytech.STData.STServiceData;
import com.damytech.Utils.AutoSizeRatingBar;
import com.damytech.Utils.ResolutionSet;
import com.damytech.yilebang.MyActivity;
import com.damytech.yilebang.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-30
 * Time: 下午11:34
 * To change this template use File | Settings | File Templates.
 */
public class DingDan_PingJiaActivity extends MyActivity {
    // UI control variables
    RelativeLayout  m_rlDlgMain;
    RelativeLayout m_rlProductInfo;
    ImageView m_imgProduct;
    TextView  m_txtProductDesc;
    TextView  m_txtProductCount;
    TextView  m_txtProductCost;
    EditText m_edtVoteText;
    AutoSizeRatingBar m_ratingBar;
    TextView  m_txtRatings;
    TextView m_txtOk;

    String  m_orderNo;
    STProductA m_productInfo;
    float  m_nStar;
    String  m_szContents;

    DisplayImageOptions  options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        m_orderNo = getIntent().getExtras().getString("orderNo");
        m_productInfo = getIntent().getExtras().getParcelable("productInfo");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.huiyuanzhongxin_dingdan_pingjia);

        ResolutionSet._instance.iterateChild(findViewById(R.id.rlDlgMain));

        getControlVariables ();

        connectSignalHandlers();

        updateUI();
    }

    private void getControlVariables () {
        m_rlDlgMain = (RelativeLayout) findViewById(R.id.rlDlgMain);
        m_rlProductInfo = (RelativeLayout) findViewById(R.id.rlProductInfo);
        m_imgProduct = (ImageView) findViewById(R.id.imgProduct);
        m_txtProductDesc = (TextView) findViewById(R.id.txtProductDesc);
        m_txtProductCount = (TextView) findViewById(R.id.txtProductCount);
        m_txtProductCost = (TextView) findViewById(R.id.txtProductCost);
        m_edtVoteText = (EditText) findViewById(R.id.edtVoteText);
        m_txtRatings = (TextView) findViewById(R.id.txtRatings);
        m_txtOk = (TextView) findViewById(R.id.txtOk);

        m_ratingBar = (AutoSizeRatingBar) findViewById(R.id.ratingBar);
        m_ratingBar.setHeight((int)(48*ResolutionSet.fYpro));
        m_ratingBar.setMaxRate(5);
        m_ratingBar.setRate((float)5);
    }

    private void connectSignalHandlers () {
        m_txtOk.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( inputIsValid() == false ) {
                    return;
                }
                callEvaluateProduct ();
            }
        });

        m_ratingBar.setOnRatingBarChangeListener(new AutoSizeRatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(AutoSizeRatingBar ratingBar, float rating) {
                m_txtRatings.setText(String.format("%.1f分", rating));
            }
        });
    }

    private void updateUI () {
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_stub)
                .showImageOnFail(R.drawable.ic_stub)
                .cacheInMemory()
                .cacheOnDisc()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading()
                .build();

        GlobalData.imageLoader.displayImage(m_productInfo.image, m_imgProduct);
        m_txtProductDesc.setText(m_productInfo.name);
        m_txtProductCount.setText(String.format(getString(R.string.HuiYuanZhongXin_DingDanChaXun_ProductCount_Fmt), m_productInfo.count));
        m_txtProductCost.setText(String.format(getString(R.string.HuiYuanZhongXin_DingDan_ProductCost_Fmt), m_productInfo.price));
    }

    private boolean inputIsValid () {
        m_nStar = m_ratingBar.getNumStars();
        m_szContents = m_edtVoteText.getText().toString();

        if ( m_szContents.isEmpty() ) {
            GlobalData.showToast(DingDan_PingJiaActivity.this, getString(R.string.HuiYuanZhongXin_DingDan_Msg_InputComment));
            return false;
        }

        return true;
    }

    private void callEvaluateProduct () {
        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = "";

                progDialog.dismiss();

                retMsg = CommMgr.commService.parseEvaluateProduct(object);
                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    // hide soft keyboard
                    InputMethodManager inputMethodManager = (InputMethodManager)  getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                    result = STServiceData.ERR_SUCCESS;
                    GlobalData.showToast(DingDan_PingJiaActivity.this, getString(R.string.HuiYuanZhongXin_DingDan_Msg_CommentUploaded));
                    finish();
                    return;
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
                    GlobalData.showToast(DingDan_PingJiaActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                DingDan_PingJiaActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.EvaluateProduct(GlobalData.token, m_productInfo.uid, m_orderNo, (int)m_nStar, m_szContents, handler);
    }
}
