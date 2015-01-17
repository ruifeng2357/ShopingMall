package com.damytech.yilebang;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.damytech.CommService.CommMgr;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.STData.STServiceData;
import com.damytech.STData.STShopCoupon;
import com.damytech.Global.GlobalData;
import com.damytech.Utils.ResolutionSet;
import com.damytech.yilebang.HuiYuanZhongXin.YouHuiQuanActivity;
import com.google.zxing.client.android.CaptureActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: KimChg
 * Date: 13-11-18
 * Time: 下午9:32
 * To change this template use File | Settings | File Templates.
 */
public class PreferentialInfoActivity extends MyActivity {

    private int mItemId = 0;
    private int IMG_WIDTH = 100;

    STShopCoupon mDetInfo = new STShopCoupon();

    private TextView _txtTitle;
    private TextView _txtAddress;
    private ImageView _imgItem;
    private TextView _txtPrice;
    private TextView _txtDate;
    private TextView _txtRule;

    private Button btnFreeCoupon;

    private JsonHttpResponseHandler handler;
    private JsonHttpResponseHandler handlerFreeCoupon;
    private ProgressDialog progDialog;

    DisplayImageOptions options;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferentialinfo);

        // initialize
        initControls();
        callGetCouponDetail();
    }

    /**
     * Initialize all controls
     */
    private void initControls()
    {
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_stub)
                .showImageOnFail(R.drawable.ic_stub)
                .cacheInMemory()
                .cacheOnDisc()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading()
                .build();

        mItemId = getIntent().getExtras().getInt("ItemId");

        ResolutionSet._instance.iterateChild(findViewById(R.id.rlPreferInfoMain));


        _txtTitle = (TextView) findViewById(R.id.txtTitle);
        _txtAddress = (TextView) findViewById(R.id.txtAddress);
        _imgItem = (ImageView) findViewById(R.id.imgInfoView);
        _txtPrice = (TextView) findViewById(R.id.txtChip);
        _txtDate = (TextView) findViewById(R.id.textView1);
        _txtRule = (TextView) findViewById(R.id.txtContent);
        btnFreeCoupon = (Button) findViewById(R.id.btnTypeGet);
        btnFreeCoupon.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDetInfo.id > 0)
                    RunBackgroundHandler(mDetInfo.id);
                return;
            }
        });

        // ORCode reader
        ImageView rlQRCode = (ImageView) findViewById(R.id.imgTop_QRBar);
        rlQRCode.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreferentialInfoActivity.this, CaptureActivity.class);
                startActivity(intent);
            }
        });

        initCartBadge();
        initMainMenu();
    }


    private void initMainMenu()
    {
        RelativeLayout rlBottom_Home = (RelativeLayout) findViewById(R.id.rlBottom_Home);
        rlBottom_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgbtnHomeClicked();
            }
        });

        RelativeLayout rlBootom_Back = (RelativeLayout) findViewById(R.id.rlBottom_BackArraw);
        rlBootom_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgbtnBackClicked();
            }
        });

        RelativeLayout rlBottom_MainMenu = (RelativeLayout) findViewById(R.id.rlBottom_MainMenu);
        rlBottom_MainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgbtnMainMenuClicked();
            }
        });

        RelativeLayout rlBottom_Packet = (RelativeLayout) findViewById(R.id.rlBottom_Packet);
        rlBottom_Packet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgbtnCartClicked();
            }
        });

        RelativeLayout rlBottom_PersonInfo = (RelativeLayout) findViewById(R.id.rlBottom_PersonInfo);
        rlBottom_PersonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgbtnAccountClicked();
            }
        });
    }

    /**
     * Initialize badge icon of shopping cart
     */
    private void initCartBadge()
    {
        ImageView imgPacket = (ImageView) findViewById(R.id.imgBottom_Packet);
        this.setBadgeParent(imgPacket);
    }

    /**
     * Update UI using service data
     */
    private void updateUI()
    {
        _txtTitle.setText(mDetInfo.name);
        _txtAddress.setText(mDetInfo.address);
        _txtPrice.setText(mDetInfo.price.toString());
        _txtDate.setText(mDetInfo.endDate);
        _txtRule.setText(mDetInfo.rule);

        GlobalData.imageLoader.displayImage(mDetInfo.imgUrl, _imgItem, options);
    }


    /////////////////////////////////////// Service Relation //////////////////////////////////////
    /**
     * Call GetCourtesyChildTypes service
     */
    private void callGetCouponDetail()
    {
        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = STServiceData.MSG_SUCCESS;

                progDialog.dismiss();

                retMsg = CommMgr.commService.parseCouponDetail(object, mDetInfo);
                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    result = STServiceData.ERR_SUCCESS;
                    // update ui
                    updateUI();
                    return;
                }
                else
                {
                    progDialog.dismiss();
                    result = STServiceData.ERR_EXCEPTION;
                }

                GlobalData.showToast(PreferentialInfoActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                if (result == STServiceData.ERR_FAIL)
                {
                    progDialog.dismiss();
                    GlobalData.showToast(PreferentialInfoActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                PreferentialInfoActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.GetCouponDetail(mItemId, IMG_WIDTH, handler);

        return;
    }

    private void RunBackgroundHandler( int nCouponID )
    {
        handlerFreeCoupon = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;
            String retMsg = STServiceData.MSG_SUCCESS;

            @Override
            public void onSuccess(JSONObject object)
            {
                progDialog.dismiss();

                retMsg = CommMgr.commService.parseBuyFreeCoupon(object);
                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    result = STServiceData.ERR_SUCCESS;

                    Intent intent = new Intent(PreferentialInfoActivity.this, YouHuiQuanActivity.class);
                    startActivity(intent);

                    return;
                }
                else
                {
                    progDialog.dismiss();
                    result = STServiceData.ERR_EXCEPTION;
                }

                GlobalData.showToast(PreferentialInfoActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                if (result == STServiceData.ERR_FAIL)
                {
                    progDialog.dismiss();
                    GlobalData.showToast(PreferentialInfoActivity.this, getString(R.string.server_connection_error));
                }
                else if (result != STServiceData.ERR_SUCCESS)
                {
                    progDialog.dismiss();
                    GlobalData.showToast(PreferentialInfoActivity.this, retMsg);
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                PreferentialInfoActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.RequestBuyFreeCoupon(GlobalData.token, nCouponID, handlerFreeCoupon);

        return;
    }
}
