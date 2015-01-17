package com.damytech.yilebang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import com.baidu.mapapi.*;
import com.damytech.CommService.CommMgr;
import com.damytech.Global.CommonFunc;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.STData.ProductDetail.STProdDetailInfo;
import com.damytech.STData.ProductDetail.STValidSpecPair;
import com.damytech.STData.STParcelableBasketItem;
import com.damytech.STData.STServiceData;
import com.damytech.STData.ShoppingCart.STBasketItemInfo;
import com.damytech.Utils.AddItemizedOverlay;
import com.damytech.Global.GlobalData;
import com.damytech.Utils.ResolutionSet;
import com.damytech.yilebang.HuiYuanZhongXin.ShouCangActivity;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: KimChg
 * Date: 13-11-22
 * Time: 上午11:16
 * To change this template use File | Settings | File Templates.
 */
public class ArticlesMapActivity extends MyMapActivity {
    private MapController mMapController;
    private MKSearch mSearch = null;
    private MyLocationOverlay mMyLocationOverlay;
    private BMapManager mBMapMan = null;
    private PoiOverlay mPoiOverlay = null;
    private RouteOverlay mRouteOverlay = null;
    private TransitOverlay mTransitOverlay = null;
    private MapView mMapView;
    public int MAP_ZOOM_VALUE = 15;

    private RelativeLayout m_rlSmallPopup;
    private boolean m_bViewSmallPopup = false;
    private ImageView m_imgPacket;
    private TextView m_lblSmallPopup_Go;
    private TextView m_lblSmallPopup_Add;

    private RelativeLayout m_rlStandards1;
    private RelativeLayout m_rlStandards2;

    private TextView lblPlusFunc = null;
    private TextView lblMinusFunc = null;
    private TextView lblBuyAmount = null;

    private TextView lblShouCang = null;
    private TextView lblFenXiang = null;

    private TextView lblTitle = null;
    private TextView lblExtraPrice = null;
    private TextView lblAmount = null;
    private TextView lblStandard = null;
    private TextView lblStandard2 = null;
    private TextView lblAddress = null;

    private Double mPrice = 0.0;

    private JsonHttpResponseHandler handler;
    private JsonHttpResponseHandler handler2;
    private ProgressDialog progDialog;

    private Boolean bAddAndGo = false;

    private int ITEM_IMG_HEIGHT = 100;
    private int ITEM_IMG_WIDTH = 100;

    private String USER_TOKEN = "";
    private int mProdId = 0;

    private int mSpecCount1 = 0;
    private int mSpecCount2 = 0;

    private int mCurSpec1 = -1;
    private int mCurSpec2 = -1;

    private STProdDetailInfo mDetailInfo = new STProdDetailInfo();

    private StandardsNode []m_arrStandards1;
    private StandardsNode []m_arrStandards2;

    public class StandardsNode {
        RelativeLayout rlFrame;
        TextView lblStandardsValue;
        boolean bSelected;
        int nSpecId;

        public StandardsNode() {
            rlFrame = null;
            lblStandardsValue = null;
            bSelected = false;
            nSpecId = 0;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.articlesmap);

        mProdId = getIntent().getExtras().getInt("ProductId");

        ResolutionSet._instance.iterateChild(findViewById(R.id.rlArticlesMapMain));

        mBMapMan = new BMapManager(getApplication());
        mBMapMan.init("A737CDFA6D523EC18453F98BB399A8E04D9C51C8", new MKGeneralListener(){
            @Override
            public void onGetNetworkState(int arg0) {}
            @Override
            public void onGetPermissionState(int arg0) {}
        });
        super.initMapActivity(mBMapMan);

        mMapView = (MapView) findViewById(R.id.viewBaiduMap_Articles);
        mMapView.setBuiltInZoomControls(false);
        mMapController = mMapView.getController();
        mMapController.setZoom(MAP_ZOOM_VALUE);

        if (mMyLocationOverlay == null)
            mMyLocationOverlay = new MyLocationOverlay(ArticlesMapActivity.this, mMapView);

        // initialize
        initControls();
        callGetProductDetail(USER_TOKEN, mProdId, ITEM_IMG_WIDTH, ITEM_IMG_HEIGHT);
    }


    /**
     * Initialize all controls
     */
    private void initControls()
    {
        lblPlusFunc = (TextView) findViewById(R.id.txtPlus);
        lblPlusFunc.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strData = lblBuyAmount.getText().toString();
                int nData = 0;
                try {
                    nData = Integer.parseInt(strData);
                }catch ( Exception e) {
                    nData = 0;
                }

                nData += 1;
                lblBuyAmount.setText(Integer.toString(nData));
            }
        });

        lblMinusFunc = (TextView) findViewById(R.id.txtMinus);
        lblMinusFunc.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strData = lblBuyAmount.getText().toString();
                int nData = 0;
                try {
                    nData = Integer.parseInt(strData);
                }catch ( Exception e) {
                    nData = 0;
                }

                nData -= 1;
                if (nData < 1)
                    nData = 1;
                lblBuyAmount.setText(Integer.toString(nData));
            }
        });
        lblBuyAmount = (TextView) findViewById(R.id.txtBuyAmount);

        lblExtraPrice = (TextView) findViewById(R.id.txtExtraPrice);
        lblAmount = (TextView) findViewById(R.id.txReserveAmount);
        lblTitle = (TextView) findViewById(R.id.txtTitle);
        lblStandard = (TextView) findViewById(R.id.txtStandard);
        lblStandard2 = (TextView) findViewById(R.id.txtStandard2);
        lblAddress = (TextView) findViewById(R.id.txtAddress);

        m_rlSmallPopup = (RelativeLayout) findViewById(R.id.rlSmalPopupBack);
        m_rlSmallPopup.setVisibility(View.GONE);
        m_bViewSmallPopup = false;

        m_imgPacket = (ImageView) findViewById(R.id.imgBottom_Packet);
        m_imgPacket.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m_bViewSmallPopup == true)
                {
                    m_rlSmallPopup.setVisibility(View.GONE);
                    m_bViewSmallPopup = false;
                }
                else
                {
                    m_rlSmallPopup.setVisibility(View.VISIBLE);
                    m_bViewSmallPopup = true;
                }
            }
        });


        lblShouCang = (TextView) findViewById(R.id.txtReceive);
        lblShouCang.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer nVal = (Integer) v.getTag();
                if (nVal == null)
                    return;
                RunBackgroundHandler(nVal.intValue());
            }
        });

        lblFenXiang = (TextView) findViewById(R.id.txtShare);
        lblFenXiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    Integer nVal = (Integer) v.getTag();
                    if (nVal == null || nVal <= 0)
                        return;

                    String imgUrl = "";
                    if (mDetailInfo.arrImgUrl.size() > 0)
                        imgUrl = mDetailInfo.arrImgUrl.get(0);

                    CommonFunc.showFenXiang(ArticlesMapActivity.this, mDetailInfo.id, imgUrl);
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });


        /*
            Popup다이얼로그사건들을 여기에서 련결한다.
         */
        m_lblSmallPopup_Add = (TextView) findViewById(R.id.lblSmallPopup_Add);
        m_lblSmallPopup_Add.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bAddAndGo = false;
                onClickGoGoods();;
            }
        });

        m_lblSmallPopup_Go = (TextView) findViewById(R.id.lblSmallPopup_Go);
        m_lblSmallPopup_Go.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bAddAndGo = true;
                onClickGoGoods();
            }
        });

        initCartBadge();
        initMainMenu();
    }

    private void initMainMenu()
    {
        RelativeLayout rlBack = (RelativeLayout) findViewById(R.id.rlBottom_BackArraw);
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgbtnBackClicked();
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
        try
        {
            // update title
            lblTitle.setText(mDetailInfo.name);
            lblAddress.setText(mDetailInfo.shopAddr);
            lblStandard.setText(mDetailInfo.specdata1Name);
            lblStandard2.setText(mDetailInfo.specdata2Name);

            Button btnGoGoods = (Button)findViewById(R.id.btnGoGoods);
            btnGoGoods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickBuy();
                }
            });

            updateShopPosition();
            updateStandardsInfo();
            updateStandardsInfo2();

            lblShouCang.setTag(mDetailInfo.id);
            lblFenXiang.setTag(mDetailInfo.id);

            refreshSpecValue();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void updateShopPosition()
    {
        GeoPoint point = new GeoPoint((int)(mDetailInfo.latitude * 1E6), (int)(mDetailInfo.longitude * 1E6));
        mMapController.setCenter(point);

        List<Overlay> mapOverlays = mMapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.address);

        AddItemizedOverlay itemizedOverlay = new AddItemizedOverlay(drawable, this);

        OverlayItem overlayItem = new OverlayItem(point, "Hello", "Sample");

        itemizedOverlay.addOverlay(overlayItem);
        mapOverlays.add(itemizedOverlay);

        mMapView.invalidate();
    }

    /**
     * Update first standard spec datas
     */
    private void updateStandardsInfo()
    {
        mSpecCount1 = mDetailInfo.arrSpecData1.size();
        m_rlStandards1 = (RelativeLayout) findViewById(R.id.rlArticles_Standards_1);
        if (mSpecCount1 == 0)
        {
            RelativeLayout rlStandard1 = (RelativeLayout) findViewById(R.id.rlStandard1);
            rlStandard1.setVisibility(View.GONE);
        }

        m_arrStandards1 = new StandardsNode[mSpecCount1];
        for ( int i =0; i < mSpecCount1; i++ )
        {
            m_arrStandards1[i] = new StandardsNode();
            if ( i == 0 )
                m_arrStandards1[i].bSelected = true;

            m_arrStandards1[i].rlFrame = new RelativeLayout(m_rlStandards1.getContext());
            m_arrStandards1[i].nSpecId = mDetailInfo.arrSpecData1.get(i).id;
            m_arrStandards1[i].rlFrame.setTag(i);
            m_arrStandards1[i].rlFrame.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer retVal = (Integer)v.getTag();
                    mCurSpec1 = retVal;
                    for (int i = 0; i < mSpecCount1; i++)
                    {
                        if (i == retVal)
                        {
                            m_arrStandards1[i].rlFrame.setBackgroundResource(R.drawable.article_red_back);
                            m_arrStandards1[i].lblStandardsValue.setTextColor(Color.parseColor("#FF0000"));
                            m_arrStandards1[i].bSelected = true;
                        }
                        else
                        {
                            m_arrStandards1[i].rlFrame.setBackgroundResource(R.drawable.article_normal_back);
                            m_arrStandards1[i].lblStandardsValue.setTextColor(Color.parseColor("#000000"));
                            m_arrStandards1[i].bSelected = false;
                        }
                    }
                    updateSpecData();
                }
            });
            m_arrStandards1[i].rlFrame.setId(i+1);
            if (m_arrStandards1[i].bSelected)
                m_arrStandards1[i].rlFrame.setBackgroundResource(R.drawable.article_red_back);
            else
                m_arrStandards1[i].rlFrame.setBackgroundResource(R.drawable.article_normal_back);

            RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(70, RelativeLayout.LayoutParams.MATCH_PARENT);
            if (i != 0)
            {
                rlParams.setMargins(20, 0, 0, 0);
                rlParams.addRule(RelativeLayout.RIGHT_OF, m_arrStandards1[i-1].rlFrame.getId());
            }
            else
            {
                rlParams.setMargins(0, 0, 0, 0);
            }
            m_arrStandards1[i].rlFrame.setLayoutParams(rlParams);

            RelativeLayout.LayoutParams txtParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            txtParams.setMargins(0, 0, 20, 0);

            m_arrStandards1[i].lblStandardsValue = new TextView(m_arrStandards1[i].rlFrame.getContext());
            m_arrStandards1[i].lblStandardsValue.setLayoutParams(txtParams);
            m_arrStandards1[i].lblStandardsValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, 20);
            if ( i == 0 )
                m_arrStandards1[i].lblStandardsValue.setTextColor(Color.parseColor("#FF0000"));
            else
                m_arrStandards1[i].lblStandardsValue.setTextColor(Color.parseColor("#000000"));
            m_arrStandards1[i].lblStandardsValue.setGravity(Gravity.CENTER);
//            m_arrStandards1[i].lblStandardsValue.setText(GOOD_NAME);
            m_arrStandards1[i].lblStandardsValue.setText(mDetailInfo.arrSpecData1.get(i).name);

            m_arrStandards1[i].rlFrame.addView(m_arrStandards1[i].lblStandardsValue);
            m_rlStandards1.addView(m_arrStandards1[i].rlFrame);
        }

        ResolutionSet._instance.iterateChild(m_rlStandards1);
    }

    /**
     * Update second standard spec datas
     */
    private void updateStandardsInfo2()
    {
        mSpecCount2 = mDetailInfo.arrSpecData2.size();
        m_rlStandards2 = (RelativeLayout) findViewById(R.id.rlArticles_Standards_2);
        if (mSpecCount2 == 0)
        {
            RelativeLayout rlStandard2 = (RelativeLayout) findViewById(R.id.rlStandard2);
            rlStandard2.setVisibility(View.GONE);
        }

        m_arrStandards2 = new StandardsNode[mSpecCount2];
        for ( int i =0; i < mSpecCount2; i++ )
        {
            m_arrStandards2[i] = new StandardsNode();
            if ( i == 0 )
                m_arrStandards2[i].bSelected = true;

            m_arrStandards2[i].rlFrame = new RelativeLayout(m_rlStandards1.getContext());
            m_arrStandards2[i].nSpecId = mDetailInfo.arrSpecData2.get(i).id;
            m_arrStandards2[i].rlFrame.setTag(i);
            m_arrStandards2[i].rlFrame.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer retVal = (Integer)v.getTag();
                    mCurSpec2 = retVal;
                    for (int i = 0; i < mSpecCount2; i++)
                    {
                        if (i == retVal)
                        {
                            m_arrStandards2[i].rlFrame.setBackgroundResource(R.drawable.article_red_back);
                            m_arrStandards2[i].lblStandardsValue.setTextColor(Color.parseColor("#FF0000"));
                            m_arrStandards2[i].bSelected = true;
                        }
                        else
                        {
                            m_arrStandards2[i].rlFrame.setBackgroundResource(R.drawable.article_normal_back);
                            m_arrStandards2[i].lblStandardsValue.setTextColor(Color.parseColor("#000000"));
                            m_arrStandards2[i].bSelected = false;
                        }
                    }
                    refreshSpecValue();
                }
            });
            m_arrStandards2[i].rlFrame.setId(i+1);
            if (m_arrStandards2[i].bSelected)
                m_arrStandards2[i].rlFrame.setBackgroundResource(R.drawable.article_red_back);
            else
                m_arrStandards2[i].rlFrame.setBackgroundResource(R.drawable.article_normal_back);

            RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(70, RelativeLayout.LayoutParams.MATCH_PARENT);
            if (i != 0)
            {
                rlParams.setMargins(20, 0, 0, 0);
                rlParams.addRule(RelativeLayout.RIGHT_OF, m_arrStandards2[i-1].rlFrame.getId());
            }
            else
            {
                rlParams.setMargins(0, 0, 0, 0);
            }
            m_arrStandards2[i].rlFrame.setLayoutParams(rlParams);

            RelativeLayout.LayoutParams txtParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            txtParams.setMargins(0, 0, 20, 0);

            m_arrStandards2[i].lblStandardsValue = new TextView(m_arrStandards2[i].rlFrame.getContext());
            m_arrStandards2[i].lblStandardsValue.setLayoutParams(txtParams);
            m_arrStandards2[i].lblStandardsValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, 20);
            if ( i == 0 )
                m_arrStandards2[i].lblStandardsValue.setTextColor(Color.parseColor("#FF0000"));
            else
                m_arrStandards2[i].lblStandardsValue.setTextColor(Color.parseColor("#000000"));
            m_arrStandards2[i].lblStandardsValue.setGravity(Gravity.CENTER);
//            m_arrStandards2[i].lblStandardsValue.setText(GOOD_NAME);
            m_arrStandards2[i].lblStandardsValue.setText(mDetailInfo.arrSpecData2.get(i).name);

            m_arrStandards2[i].rlFrame.addView(m_arrStandards2[i].lblStandardsValue);
            m_rlStandards2.addView(m_arrStandards2[i].rlFrame);
        }

        ResolutionSet._instance.iterateChild(m_rlStandards2);
    }

    /**
     * Update second spec datas by first spec
     */
    private void updateSpecData()
    {
        int i = 0;

        try
        {
            int spec1Id = m_arrStandards1[mCurSpec1].nSpecId;
            // disable second specs
            for (i = 0; i < mSpecCount2; i++)
            {
                m_arrStandards2[i].rlFrame.setEnabled(false);
                m_arrStandards2[i].rlFrame.setBackgroundResource(R.drawable.article_normal_back);
                m_arrStandards2[i].lblStandardsValue.setTextColor(Color.parseColor("#000000"));
                m_arrStandards2[i].bSelected = false;
            }

            // search available second spec
            ArrayList<STValidSpecPair> arrSpecPair = mDetailInfo.arrValidSpecPairs;
            for (i = 0; i < arrSpecPair.size(); i++)
            {
                STValidSpecPair specData = arrSpecPair.get(i);
                if (spec1Id == specData.spec1id)
                {
                    // enable second spec of pair
                    int pos = getSpec2PosById(specData.spec2id);
                    if (pos >= 0)
                        m_arrStandards2[pos].rlFrame.setEnabled(true);
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    /**
     * Get second spec position by id
     * @param spec2Id [in], second spec id
     * @return spec position
     */
    private int getSpec2PosById(int spec2Id)
    {
        int retVal = -1;
        for (int i = 0; i < mSpecCount2; i++)
        {
            if (m_arrStandards2[i].nSpecId == spec2Id)
            {
                retVal = i;
                break;
            }
        }

        return retVal;
    }

    /**
     * Refresh product value by selected spec
     */
    private void refreshSpecValue()
    {
        int spec1Id = 0;
        int spec2Id = 0;
        try
        {
            // get selected spec ids
            if (mCurSpec1 >= 0)
                spec1Id = mDetailInfo.arrSpecData1.get(mCurSpec1).id;
            if (mCurSpec2 >= 0)
                spec2Id = mDetailInfo.arrSpecData2.get(mCurSpec2).id;
            // check spec ids
            ArrayList<STValidSpecPair> arrSpecPair = mDetailInfo.arrValidSpecPairs;
            for (int i = 0; i < arrSpecPair.size(); i++)
            {
                STValidSpecPair specData = arrSpecPair.get(i);
                if ((specData.spec1id == spec1Id) && (specData.spec2id == spec2Id))
                {
                    setSpecValue(specData);
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * set spec value
     * @param info [in], spec data to be set
     */
    private void setSpecValue(STValidSpecPair info)
    {
        try
        {
            mPrice = info.price;
            lblExtraPrice.setText(Double.toString(info.commission));
            lblAmount.setText(Double.toString(info.inventory));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }



    /**
     * Get selected id of first spec
     * @return spec id
     */
    private int getFirstSpecId()
    {
        int nRetVal = 0;
        if ((mSpecCount1 > 0) && (mCurSpec1 == -1))
        {
            GlobalData.showToast(ArticlesMapActivity.this, getString(R.string.MSG_NoSel_Data));
            nRetVal = -1;
        }
        else if (mSpecCount1 != 0)
        {
            nRetVal = m_arrStandards1[mCurSpec1].nSpecId;
        }

        return nRetVal;
    }

    /**
     * Get selected id of second spec
     * @return spec id
     */
    private int getSecondSepcId()
    {
        int nRetVal = 0;
        if ((mSpecCount2 > 0) && (mCurSpec2 == -1))
        {
            GlobalData.showToast(ArticlesMapActivity.this, getString(R.string.MSG_NoSel_Data));
            nRetVal = -1;
        }
        else if (mSpecCount2 != 0)
        {
            nRetVal = m_arrStandards2[mCurSpec2].nSpecId;
        }

        return nRetVal;
    }

    private void onClickGoGoods()
    {
        int spec1Id = 0;
        int spec2Id = 0;

        try
        {
            spec1Id = getFirstSpecId();
            spec2Id = getSecondSepcId();
            if ((spec1Id < 0) || (spec2Id < 0))
                return;

            int nCount = Integer.parseInt(lblBuyAmount.getText().toString());
            callAddProductToShopCarts(mProdId, spec1Id, spec2Id, nCount, GlobalData.token);

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }



    /**
     * Go to order confirm
     */
    private void onClickBuy()
    {
        int spec1Id = 0;
        int spec2Id = 0;

        try
        {
            spec1Id = getFirstSpecId();
            spec2Id = getSecondSepcId();
            if ((spec1Id < 0) || (spec2Id < 0))
                return;

            // make shopping item data
            STBasketItemInfo item = new STBasketItemInfo();
            item.pid = mDetailInfo.id;
            item.spec1 = spec1Id;
            item.spec2 = spec2Id;
            item.price = mPrice;
            item.image = mDetailInfo.arrImgUrl.get(0);
            item.count = Integer.parseInt(lblBuyAmount.getText().toString());
            item.name = mDetailInfo.name;

            // make basket item parcelable array ( with one item )
            ArrayList <STParcelableBasketItem> parcelArray = new ArrayList<STParcelableBasketItem>(0);
            {
                STParcelableBasketItem parcelitem = new STParcelableBasketItem(item);
                parcelArray.add(parcelitem);
            }

            Intent intent = new Intent(ArticlesMapActivity.this, OrderConfirmActivity.class);
            intent.putParcelableArrayListExtra("ItemArray", parcelArray);
            intent.putExtra("TotalPrice", item.price * item.count);
            intent.putExtra("TransPrice", GlobalData.g_UserInfo.transPrice);
            startActivity(intent);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////

    private boolean getGpsService() {
        String gs = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (gs.indexOf("gps", 0) < 0) {
            Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            startActivity(intent);
        }

        return true;
    }

    /////////////////////////////////////// Service Relation //////////////////////////////////////

    /**
     * Call GetProductDetail service
     * @param token [in], user token
     * @param prodId [in], current product id
     * @param width [in], image width
     * @param height [in], image height
     */
    private void callGetProductDetail(String token, int prodId, int width, int height)
    {
        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = STServiceData.MSG_SUCCESS;

                progDialog.dismiss();

                retMsg = CommMgr.commService.parseGetProductDetail(object, mDetailInfo);
                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    result = STServiceData.ERR_SUCCESS;
                    // get advertisement list of main page
                    updateUI();
                    return;
                }
                else
                {
                    result = STServiceData.ERR_EXCEPTION;
                }

                GlobalData.showToast(ArticlesMapActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                progDialog.dismiss();
                if (result == STServiceData.ERR_FAIL)
                {
                    GlobalData.showToast(ArticlesMapActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                ArticlesMapActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.GetProductDetail(token, prodId, width, height, handler);

        return;
    }

    /**
     * Add Product to shopping cart
     * @param prodId [in], product id
     * @param gid1 [in], first spec id of product
     * @param gid2 [in], second spec id of product
     * @param count [in], count of product
     * @param token [in], user token
     */
    private void callAddProductToShopCarts(int prodId, int gid1, int gid2, int count, String token)
    {
        if (token.length() <= 0)
        {
            Intent intent = new Intent(ArticlesMapActivity.this, PersonInfoActivity.class);
            startActivity(intent);
            return;
        }

        handler2 = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = STServiceData.MSG_SUCCESS;

                progDialog.dismiss();

                retMsg = CommMgr.commService.parseSubmitProductQuestion(object);
                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    result = STServiceData.ERR_SUCCESS;
                    // show success alert
                    if (bAddAndGo)
                        CommonFunc.GoToShopCart(ArticlesMapActivity.this);
                    bAddAndGo = false;
                    return;
                }
                else
                {
                    result = STServiceData.ERR_EXCEPTION;
                }

                GlobalData.showToast(ArticlesMapActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                progDialog.dismiss();
                if (result == STServiceData.ERR_FAIL)
                {
                    GlobalData.showToast(ArticlesMapActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                ArticlesMapActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.AddProductToShopCarts(prodId, gid1, gid2, count, token, handler2);

        return;
    }


    private void RunBackgroundHandler (int nProducdtId)
    {
        if (GlobalData.token.length() <= 0)
        {
            Intent intent = new Intent(ArticlesMapActivity.this, PersonInfoActivity.class);
            startActivity(intent);
            return;
        }

        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;
            String retMsg = "";

            @Override
            public void onSuccess(JSONObject object)
            {
                progDialog.dismiss();
                retMsg = CommMgr.commService.parseCollectProduct(object);
                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    result = STServiceData.ERR_SUCCESS;

                    Intent intent = new Intent(ArticlesMapActivity.this, ShouCangActivity.class);
                    startActivity(intent);
                    return;
                }
                else
                {
                    result = STServiceData.ERR_EXCEPTION;
                }

                GlobalData.showToast(ArticlesMapActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                progDialog.dismiss();
                if (result == STServiceData.ERR_FAIL)
                {
                    GlobalData.showToast(ArticlesMapActivity.this, getString(R.string.server_connection_error));
                }
                else if (result != STServiceData.ERR_SUCCESS)
                {
                    GlobalData.showToast(ArticlesMapActivity.this, retMsg);
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                ArticlesMapActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.RequestCollectProduct(GlobalData.token, nProducdtId, handler);
    }




    ///////////////////////////////////////////////////////////////////////////////////////
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
}
