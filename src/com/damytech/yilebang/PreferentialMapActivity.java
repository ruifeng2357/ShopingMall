package com.damytech.yilebang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.baidu.mapapi.*;
import com.damytech.CommService.CommMgr;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.STData.STPreferDetInfo;
import com.damytech.STData.STServiceData;
import com.damytech.Utils.AddItemizedOverlay;
import com.damytech.Global.GlobalData;
import com.damytech.Utils.ResolutionSet;
import com.google.zxing.client.android.CaptureActivity;
import org.json.JSONObject;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: KimChg
 * Date: 13-11-18
 * Time: 下午5:12
 * To change this template use File | Settings | File Templates.
 */
public class PreferentialMapActivity extends MyMapActivity {
    private MapController mMapController;
    private MKSearch mSearch = null;
    private MyLocationOverlay mMyLocationOverlay;
    private BMapManager mBMapMan = null;
    private PoiOverlay mPoiOverlay = null;
    private RouteOverlay mRouteOverlay = null;
    private TransitOverlay mTransitOverlay = null;
    private MapView mMapView;
    public int MAP_ZOOM_VALUE = 15;

    private int mItemId = 0;
    private int IMG_WIDTH = 100;

    STPreferDetInfo mDetInfo = new STPreferDetInfo();

    private TextView _txtTitle;
    private TextView _txtAddress;
    private TextView _txtPrice;
    private TextView _txtPhone;
    private TextView _txtTime;

    private JsonHttpResponseHandler handler;
    private ProgressDialog progDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferentialmap);

        mItemId = getIntent().getExtras().getInt("ItemId");

        initControls();
        callGetCourtesyShopDetail();
    }

    private void initControls()
    {
        ResolutionSet._instance.iterateChild(findViewById(R.id.rlPreferentialMapMain));

        mBMapMan = new BMapManager(getApplication());
        mBMapMan.init("A737CDFA6D523EC18453F98BB399A8E04D9C51C8", new MKGeneralListener(){
            @Override
            public void onGetNetworkState(int arg0) {}
            @Override
            public void onGetPermissionState(int arg0) {}
        });
        super.initMapActivity(mBMapMan);

        mMapView = (MapView) findViewById(R.id.viewBaiduMap);
        mMapView.setBuiltInZoomControls(false);
        mMapController = mMapView.getController();
        mMapController.setZoom(MAP_ZOOM_VALUE);

        if (mMyLocationOverlay == null)
            mMyLocationOverlay = new MyLocationOverlay(PreferentialMapActivity.this, mMapView);

        _txtTitle = (TextView) findViewById(R.id.textView);
        _txtAddress = (TextView) findViewById(R.id.textView2);
        _txtPrice = (TextView) findViewById(R.id.txtChip);
        _txtPhone = (TextView) findViewById(R.id.txtPhoneNum);
        _txtTime = (TextView) findViewById(R.id.txtTimeLine);

        // ORCode reader
        ImageView rlQRCode = (ImageView) findViewById(R.id.imgTop_QRBar);
        rlQRCode.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreferentialMapActivity.this, CaptureActivity.class);
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
        _txtPrice.setText(mDetInfo.price);
        _txtPhone.setText(mDetInfo.phone);
        _txtTime.setText(mDetInfo.timeRange);

        updateShopPosition();
    }

    private void updateShopPosition()
    {
        GeoPoint point = new GeoPoint((int)(mDetInfo.latitude * 1E6), (int)(mDetInfo.longitude * 1E6));
        mMapController.setCenter(point);

        List<Overlay> mapOverlays = mMapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.address);

        AddItemizedOverlay itemizedOverlay = new AddItemizedOverlay(drawable, this);

        OverlayItem overlayItem = new OverlayItem(point, "Hello", "Sample");

        itemizedOverlay.addOverlay(overlayItem);
        mapOverlays.add(itemizedOverlay);

        mMapView.invalidate();
    }

    private boolean getGpsService() {
        String gs = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (gs.indexOf("gps", 0) < 0) {
            Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            startActivity(intent);
        }

        return true;
    }

    @Override
    protected void onResume() {

        if (mBMapMan != null) {
            mBMapMan.start();
        }
        super.onResume();

        getGpsService();
        mMyLocationOverlay.enableMyLocation();
        mMyLocationOverlay.enableCompass();
    }

    @Override
    protected void onPause(){
        if (mBMapMan != null) {
            mBMapMan.stop();
        }
        super.onPause();

        if (mMyLocationOverlay != null){
            mMyLocationOverlay.disableMyLocation();
        }
    }

    @Override
    protected void onDestroy() {
        if (mBMapMan != null) {
            mBMapMan.destroy();
            mBMapMan = null;
        }
        super.onDestroy();
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
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

                GlobalData.showToast(PreferentialMapActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                if (result == STServiceData.ERR_FAIL)
                {
                    progDialog.dismiss();
                    GlobalData.showToast(PreferentialMapActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                PreferentialMapActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.GetCourtesyShopDetail(mItemId, IMG_WIDTH, handler);

        return;
    }
}