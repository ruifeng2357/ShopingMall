package com.damytech.yilebang;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.damytech.CommService.CommMgr;
import com.damytech.Global.CommonFunc;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.STData.STPreferDetInfo;
import com.damytech.STData.STServiceData;
import com.damytech.Global.GlobalData;
import com.damytech.Utils.ResolutionSet;
import com.google.zxing.client.android.CaptureActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * Company: D.M.Y
 * Date: 13-11-16
 * Time: 上午9:36
 * To change this template use File | Settings | File Templates.
 */
public class PreferentialDetActivity extends MyActivity {

    private WebView listPreferDetail;

    private final int HEIGHT_OF_ITEM = 300;

    private int mItemId = 0;
    private int IMG_WIDTH = 100;

    STPreferDetInfo mDetInfo = new STPreferDetInfo();

    private TextView _txtTitle;
    private TextView _txtAddress;
    private ImageView _imgItem;
    private TextView _txtPrice;
    private TextView _txtPhone;
    private TextView _txtTime;
    private TextView _txtDescription;

    private Button btnFreeGet;
    private Button btnTypeGet;

    private JsonHttpResponseHandler handler;
    private ProgressDialog progDialog;

    DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferentialdet);

        // initialize
        initControls();
        callGetCourtesyShopDetail();
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

        ResolutionSet._instance.iterateChild(findViewById(R.id.rlPreferentialDet));

        listPreferDetail = (WebView)findViewById(R.id.listItems);

        _txtTitle = (TextView) findViewById(R.id.txtTitle);
        _txtAddress = (TextView) findViewById(R.id.txtAddr);
        _imgItem = (ImageView) findViewById(R.id.imgItemPic);
        _txtPrice = (TextView) findViewById(R.id.txtChip);
        _txtPhone = (TextView) findViewById(R.id.txtPhoneNum);
        _txtTime = (TextView) findViewById(R.id.txtTimeLine);
        _txtDescription = (TextView) findViewById(R.id.txtDescription);

        btnFreeGet = (Button) findViewById(R.id.btnFreeGet);

        btnTypeGet = (Button) findViewById(R.id.btnTypeGet);
        btnTypeGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try
                {
                    if (mDetInfo.id > 0)
                        CommonFunc.showFenXiang(PreferentialDetActivity.this, mDetInfo.id, mDetInfo.imgUrl);
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }

            }
        });

//
//        RelativeLayout.LayoutParams rlLayoutParam = (RelativeLayout.LayoutParams)listPreferDetail.getLayoutParams();// RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, (int)(300 * ResolutionSet.fYpro * NUM_OF_ITEMCOUNT));
//        rlLayoutParam.height = (int)(300 * ResolutionSet.fYpro * NUM_OF_ITEMCOUNT);
//        listPreferDetail.setLayoutParams(rlLayoutParam);
//        listPreferDetail.setAdapter(mAdapter);

        RelativeLayout rlBlockDetail = (RelativeLayout)findViewById(R.id.rlBlock2);
        rlBlockDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreferentialDetActivity.this, PreferentialMapActivity.class);
                intent.putExtra("ItemId", mItemId);
                startActivity(intent);
            }
        });

        // ORCode reader
        ImageView rlQRCode = (ImageView) findViewById(R.id.imgTop_QRBar);
        rlQRCode.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreferentialDetActivity.this, CaptureActivity.class);
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
        _txtPhone.setText(mDetInfo.phone);
        _txtTime.setText(mDetInfo.timeRange);
        _txtDescription.setText(mDetInfo.contents);

        // check coupon id
        if (mDetInfo.coupid <= 0)
        {
            btnFreeGet.setVisibility(View.GONE);
        }



        WebSettings settings = listPreferDetail.getSettings();
        settings.setDefaultTextEncodingName("utf-8");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            String base64 = Base64.encodeToString(mDetInfo.intro.getBytes(), Base64.DEFAULT);
            listPreferDetail.loadData(base64, "text/html; charset=utf-8", "base64");
        } else {
            String header = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
            listPreferDetail.loadData(header + mDetInfo.intro, "text/html; chartset=UTF-8", null);
        }

        GlobalData.imageLoader.displayImage(mDetInfo.imgUrl, _imgItem, options);
    }


    /////////////////////////////////////// Service Relation //////////////////////////////////////
    /**
     * Call GetCourtesyShopDetail service
     */
    private void callGetCourtesyShopDetail()
    {
        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = STServiceData.MSG_SUCCESS;

                progDialog.dismiss();

                retMsg = CommMgr.commService.parseCourtesyShopDetail(object, mDetInfo);
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

                GlobalData.showToast(PreferentialDetActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                if (result == STServiceData.ERR_FAIL)
                {
                    progDialog.dismiss();
                    GlobalData.showToast(PreferentialDetActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                PreferentialDetActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.GetCourtesyShopDetail(mItemId, IMG_WIDTH, handler);

        return;
    }
}
