package com.damytech.yilebang;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.*;
import com.damytech.CommService.CommMgr;
import com.damytech.Global.CommonFunc;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.STData.ProductDetail.STProdDetailInfo;
import com.damytech.STData.ProductDetail.STValidSpecPair;
import com.damytech.STData.STParcelableBasketItem;
import com.damytech.STData.STServiceData;
import com.damytech.Global.GlobalData;
import com.damytech.STData.STString;
import com.damytech.STData.ShoppingCart.STBasketItemInfo;
import com.damytech.Utils.HorizontalPager;
import com.damytech.Utils.ResolutionSet;
import com.damytech.yilebang.HuiYuanZhongXin.ShouCangActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA.
 * User: KimChg
 * Date: 13-11-21
 * Time: 下午4:58
 * To change this template use File | Settings | File Templates.
 */
public class ArticlesActivity extends MyActivity {

    private ArticlesAskItemAdapter mAskAdapter;
    private ArticlesEvalItemAdapter mEvalAdapter;

    private ListView listAsk;
    private ListView listEval;

    private RelativeLayout m_rlStandards1;
    private RelativeLayout m_rlStandards2;

    private RelativeLayout m_rlSmallPopup;
    private boolean m_bViewSmallPopup = false;
    private ImageView m_imgPacket;
    private TextView m_lblSmallPopup_Go;
    private TextView m_lblSmallPopup_Add;

    private TextView lblPlusFunc = null;
    private TextView lblMinusFunc = null;
    private TextView lblBuyAmount = null;

    private TextView lblShouCang = null;
    private TextView lblFenXiang = null;

    private TextView lblTitle = null;
    private TextView lblShopPrice = null;
    private TextView lblHuiYuanPrice = null;
    private TextView lblExtraPrice = null;
    private TextView lblAmount = null;
    private TextView lblStandard = null;
    private TextView lblStandard2 = null;

    private Button btnSubmitQuery = null;

    RelativeLayout rlTabHeader = null;
    RelativeLayout rlBody1 = null, rlBody2 = null, rlBody3 = null;
    private HorizontalPager mGallery = null;
    private ImageView mImgSlidePos[];
    private static int TIME_INTERVAL = 2000;
    Timer mTimer = new Timer();

    final int nMaxSlideImgCnt = 5;
    int nCurSlideImgCnt = 5;

    final int NUM_OF_ITEMCOUNT = 3;

    private StandardsNode []m_arrStandards1;
    private StandardsNode []m_arrStandards2;

    private JsonHttpResponseHandler handler;
    private JsonHttpResponseHandler handler1;
    private JsonHttpResponseHandler handler2;
    private JsonHttpResponseHandler handler1Yuan;
    private ProgressDialog progDialog;

    private int BANNER_IMG_WIDTH = 100;
    private int ITEM_IMG_HEIGHT = 100;
    private int ITEM_IMG_WIDTH = 100;

    private int mProdId = 0;
    private int mParentId = 0;

    private int mSpecCount1 = 0;
    private int mSpecCount2 = 0;

    private int mCurSpec1 = -1;
    private int mCurSpec2 = -1;

    private int mMinStock = 0;

    private STProdDetailInfo mDetailInfo = new STProdDetailInfo();

    DisplayImageOptions options;

    private Boolean bAddAndGo = false;

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
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.articles);

        mProdId = getIntent().getExtras().getInt("ProductId");
        mParentId = getIntent().getIntExtra("ParentId", 0);
//        mProdId = 1732;

        // initialize
        initControls();
        callGetProductDetail(GlobalData.token, mProdId, ITEM_IMG_WIDTH, ITEM_IMG_HEIGHT);
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

        ResolutionSet._instance.iterateChild(findViewById(R.id.rlArticlesMain));

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
                if (nData > CommonFunc.g_MaxCount)
                    nData = CommonFunc.g_MaxCount;

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
                } catch ( Exception e) {
                    nData = 0;
                }

                nData -= 1;
                if (nData < mMinStock)
                    nData = mMinStock;
                lblBuyAmount.setText(Integer.toString(nData));
            }
        });
        lblBuyAmount = (TextView) findViewById(R.id.txtBuyAmount);

        rlTabHeader = (RelativeLayout)findViewById(R.id.rlTabHeader);
        rlBody1 = (RelativeLayout)findViewById(R.id.rlTabBody1);
        rlBody2 = (RelativeLayout)findViewById(R.id.rlTabBody2);
        rlBody3 = (RelativeLayout)findViewById(R.id.rlTabBody3);

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

        /*
            Popup다이얼로그사건들을 여기에서 련결한다.
         */
        m_lblSmallPopup_Add = (TextView) findViewById(R.id.lblSmallPopup_Add);
        m_lblSmallPopup_Add.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bAddAndGo = false;
                onClickGoGoods();
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

        TextView txtTab1 = (TextView)findViewById(R.id.txtTabHeader1);
        txtTab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlTabHeader.setBackgroundResource(R.drawable.article_tab_back1);
                ChangeTabContent(0);
            }
        });

        TextView txtTab2 = (TextView)findViewById(R.id.txtTabHeader2);
        txtTab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlTabHeader.setBackgroundResource(R.drawable.article_tab_back2);
                ChangeTabContent(1);
            }
        });

        TextView txtTab3 = (TextView)findViewById(R.id.txtTabHeader3);
        txtTab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlTabHeader.setBackgroundResource(R.drawable.article_tab_back3);
                ChangeTabContent(2);
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

                    CommonFunc.showFenXiang(ArticlesActivity.this, mDetailInfo.id, imgUrl);
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });

        mImgSlidePos = new ImageView[nMaxSlideImgCnt];
        mImgSlidePos[0] = (ImageView)findViewById(R.id.imgSlidePos1);
        mImgSlidePos[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGallery.setCurrentScreen(0, false);
                setSeparateColor(0);
            }
        });
        mImgSlidePos[1] = (ImageView)findViewById(R.id.imgSlidePos2);
        mImgSlidePos[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGallery.setCurrentScreen(1, false);
                setSeparateColor(1);
            }
        });
        mImgSlidePos[2] = (ImageView)findViewById(R.id.imgSlidePos3);
        mImgSlidePos[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGallery.setCurrentScreen(2, false);
                setSeparateColor(2);
            }
        });
        mImgSlidePos[3] = (ImageView)findViewById(R.id.imgSlidePos4);
        mImgSlidePos[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGallery.setCurrentScreen(3, false);
                setSeparateColor(3);
            }
        });
        mImgSlidePos[4] = (ImageView)findViewById(R.id.imgSlidePos5);
        mImgSlidePos[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGallery.setCurrentScreen(4, false);
                setSeparateColor(4);
            }
        });

        mGallery = (HorizontalPager)findViewById(R.id.hpBaseService_Body);
        mGallery.setCurrentScreen(0, false);
        mGallery.setOnScreenSwitchListener(new HorizontalPager.OnScreenSwitchListener()	{
            @Override
            public void onScreenSwitched(int screen) {
                mGallery.setCurrentScreen(screen, false);
                setSeparateColor(screen);
            }
        });

        listAsk = (ListView)findViewById(R.id.listAsksView);
        listEval = (ListView)findViewById(R.id.listEvalsView);

        TextView txtShopYuan = (TextView)findViewById(R.id.txtShopPriceYuan);
        lblShopPrice = (TextView)findViewById(R.id.txtShopPrice);

        txtShopYuan.setPaintFlags(txtShopYuan.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        lblShopPrice.setPaintFlags(txtShopYuan.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        lblHuiYuanPrice = (TextView) findViewById(R.id.txtServePrice);
        lblExtraPrice = (TextView) findViewById(R.id.txtExtraPrice);
        lblAmount = (TextView) findViewById(R.id.txReserveAmount);
        lblTitle = (TextView) findViewById(R.id.txtTitle);
        lblStandard = (TextView) findViewById(R.id.txtStandard);
        lblStandard2 = (TextView) findViewById(R.id.txtStandard2);

        btnSubmitQuery = (Button) findViewById(R.id.btnAsk);
        btnSubmitQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSubmitQuery();
            }
        });


        Button btnGoGoods = (Button)findViewById(R.id.btnGoGoods);
        btnGoGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mParentId != STServiceData.PROD_ID_1YUANQUAN)
                {
                    onClickBuy();
                }
                else
                {
                    onClick1YuanBuy();
                }

            }
        });


        // check parent id for 1yuan
        if (mParentId == STServiceData.PROD_ID_1YUANQUAN)
        {
            m_imgPacket.setVisibility(View.GONE);
        }

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
        if (mParentId != STServiceData.PROD_ID_1YUANQUAN)
        {
            ImageView imgPacket = (ImageView) findViewById(R.id.imgBottom_Packet);
            this.setBadgeParent(imgPacket);
        }
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
            lblStandard.setText(mDetailInfo.specdata1Name);
            lblStandard2.setText(mDetailInfo.specdata2Name);
            // update description of product
            updateDescription();
            // update consult information of product
            updateConsult();
            // update evaluation data of product
            updateEvaluation();

            lblShouCang.setTag(mDetailInfo.id);
            lblFenXiang.setTag(mDetailInfo.id);

            RelativeLayout rlViewMap = (RelativeLayout)findViewById(R.id.rlViewMap);
            rlViewMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ArticlesActivity.this, ArticlesMapActivity.class);
                    intent.putExtra("ProductId", mProdId);
                    startActivity(intent);
                }
            });

            TextView txtMoreEval = (TextView)findViewById(R.id.txtViewMoreEval);
            txtMoreEval.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ArticlesActivity.this, ArticlesEvalDetailActivity.class);
                    intent.putExtra("ProductId", mProdId);
                    intent.putExtra("ProductName", mDetailInfo.name);
                    intent.putExtra("CommonRate", mDetailInfo.commentRate);
                    intent.putExtra("GoodRate", mDetailInfo.goodRate);
                    intent.putExtra("MediumRate", mDetailInfo.mediumRate);
                    intent.putExtra("BadRate", mDetailInfo.badRate);

                    startActivity(intent);
                }
            });

            TextView txtMoreAsk = (TextView)findViewById(R.id.txtViewMoreAsk);
            txtMoreAsk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ArticlesActivity.this, ArticlesAskDetailActivity.class);
                    intent.putExtra("ProductId", mProdId);
                    intent.putExtra("ProductName", mDetailInfo.name);
                    startActivity(intent);
                }
            });


            loadAdvertiseView();

            // initialize evaluation progress bar
            ChangeProgress((int)(mDetailInfo.commentRate), (int)(mDetailInfo.goodRate), (int)mDetailInfo.mediumRate, (int)mDetailInfo.badRate);
//            ChangeProgress(30, 40, 10, 90);

            updateStandardsInfo();
            updateStandardsInfo2();

            refreshSpecValue();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Update description of product
     */
    private void updateDescription()
    {
        WebView webContentView = (WebView) findViewById(R.id.webContentView);
        WebSettings settings = webContentView.getSettings();
        settings.setDefaultTextEncodingName("utf-8");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            String base64 = Base64.encodeToString(mDetailInfo.intro.getBytes(), Base64.DEFAULT);
            webContentView.loadData(base64, "text/html; charset=utf-8", "base64");
        } else {
            String header = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
            webContentView.loadData(header + mDetailInfo.intro, "text/html; chartset=UTF-8", null);

        }
    }

    /**
     * Update evaluation data of product
     */
    private void updateEvaluation()
    {
        listEval.setDivider(new ColorDrawable(Color.TRANSPARENT));
        listEval.setCacheColorHint(Color.parseColor("#FFF1F1F1"));
        listEval.setDividerHeight(2);
        mEvalAdapter = new ArticlesEvalItemAdapter(ArticlesActivity.this, this.getApplicationContext());
        mEvalAdapter.setData(mDetailInfo.arrComments);

        RelativeLayout.LayoutParams rlLayoutParam1 = (RelativeLayout.LayoutParams)listEval.getLayoutParams();
        rlLayoutParam1.height = (int)(150 * ResolutionSet.fYpro * NUM_OF_ITEMCOUNT);
        listEval.setLayoutParams(rlLayoutParam1);

        listEval.setAdapter(mEvalAdapter);
    }

    /**
     * Update consult of product
     */
    private void updateConsult()
    {
        listAsk.setDivider(new ColorDrawable(Color.TRANSPARENT));
        listAsk.setCacheColorHint(Color.parseColor("#FFF1F1F1"));
        listAsk.setDividerHeight(2);
        mAskAdapter = new ArticlesAskItemAdapter(ArticlesActivity.this, this.getApplicationContext());
        mAskAdapter.setData(mDetailInfo.arrConsult);

        RelativeLayout.LayoutParams rlLayoutParam = (RelativeLayout.LayoutParams)listAsk.getLayoutParams();
        rlLayoutParam.height = (int)(150 * ResolutionSet.fYpro * NUM_OF_ITEMCOUNT);
        listAsk.setLayoutParams(rlLayoutParam);

        listAsk.setAdapter(mAskAdapter);
    }

    /**
     * Change tab content when user clicked tab
      * @param nViewId [in], tab id (detail, comment, consult)
     */
    private void ChangeTabContent(int nViewId)
    {
        switch (nViewId)
        {
            case 0:
                rlBody1.setVisibility(View.VISIBLE);
                rlBody2.setVisibility(View.GONE);
                rlBody3.setVisibility(View.GONE);
                break;
            case 1:
                rlBody1.setVisibility(View.GONE);
                rlBody2.setVisibility(View.VISIBLE);
                rlBody3.setVisibility(View.GONE);
                break;
            case 2:
                rlBody1.setVisibility(View.GONE);
                rlBody2.setVisibility(View.GONE);
                rlBody3.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        stopTimer();
    }

    /////////////////////////////////////////// Spec Data Relation ////////////////////////////////////////
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
        mCurSpec2 = -1;

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
                    break;
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
            lblShopPrice.setText(Double.toString(info.marketPrice));
            if (GlobalData.token.length() > 0)
            {
                lblHuiYuanPrice.setText(Double.toString(info.price));
            }
            else
            {
                lblHuiYuanPrice.setText(getString(R.string.Articles_PermissionPrice));
            }
            lblExtraPrice.setText(Double.toString(info.commission));
            lblAmount.setText(Double.toString(info.inventory));

            if (info.inventory > 0)
            {
                mMinStock = 1;
                lblBuyAmount.setText("1");
            }
            else
            {
                mMinStock = 0;
                lblBuyAmount.setText("0");
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// Advertisement Image /////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    private TimerTask intro_timer_task = new TimerTask()
    {
        @Override
        public void run() {
            runOnUiThread(mRunnable);
        }
    };

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run()
        {
            int nSelection = mGallery.getCurrentScreen();
            if (nSelection == nCurSlideImgCnt - 1)
                nSelection = 0;
            else
                nSelection++;

            mGallery.setCurrentScreen(nSelection, true);
            setSeparateColor(nSelection);
        }
    };

    public void loadAdvertiseView()
    {
        nCurSlideImgCnt = mDetailInfo.arrImgUrl.size();
        if (nCurSlideImgCnt == 0)
            return;

        try
        {
            int paddingWidth = (int)(10 * ResolutionSet.fXpro);
            int imgWidth = (int)(12 * ResolutionSet.fPro), imgHeight = (int)(12 * ResolutionSet.fPro);
            int baseLeft = (ResolutionSet.nWidth - imgWidth * nCurSlideImgCnt - paddingWidth * (nCurSlideImgCnt - 1)) / 2;
            int baseTop = (int)(265 * ResolutionSet.fYpro);
            for (int i = 0; i < nCurSlideImgCnt; i++)
            {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(imgWidth, imgHeight);
                layoutParams.leftMargin = baseLeft;
                layoutParams.topMargin = baseTop;
                mImgSlidePos[i].setBackgroundResource(R.drawable.gray_balloon);
                mImgSlidePos[i].setLayoutParams(layoutParams);
                mImgSlidePos[i].setVisibility(View.VISIBLE);

                ImageView imgView = new ImageView(mGallery.getContext());
//            imgView.setBackgroundResource(mImageIDs[i]);
                imgView.setLayoutParams(new RelativeLayout.LayoutParams(mGallery.getWidth(), mGallery.getHeight()));
                GlobalData.imageLoader.displayImage(mDetailInfo.arrImgUrl.get(i), imgView, options);
                mGallery.addView(imgView);

                baseLeft += imgWidth;
                baseLeft += paddingWidth;

            }

            setSeparateColor(0);

            startTimer();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void startTimer()
    {
        mTimer.schedule(intro_timer_task, TIME_INTERVAL, TIME_INTERVAL);
    }

    public void stopTimer()
    {
        mTimer.cancel();
    }

    private void setSeparateColor(int nNo)
    {
        for ( int i = 0; i < nCurSlideImgCnt; i++ )
        {
            if ( i == nNo )
                mImgSlidePos[i].setImageResource(R.drawable.red_balloon);
            else
                mImgSlidePos[i].setImageResource(R.drawable.gray_balloon);
        }
    }

    /**
     * Change total evaluation progress bar
     * @param highpro [in], good rate value
     * @param mediumpro [in], medium rate value
     * @param lowpro [in], bad rate value
     */
    private void ChangeProgress(int commonpro, int highpro, int mediumpro, int lowpro)
    {
        int proWidth = 200;

        commonpro = (commonpro > 100) ? 100 : commonpro;
        commonpro = (commonpro < 0) ? 0 : commonpro;
        highpro = (highpro > 100) ? 100 : highpro;
        highpro = (highpro < 0) ? 0 : highpro;
        mediumpro = (mediumpro > 100) ? 100 : mediumpro;
        mediumpro = (mediumpro < 0) ? 0 : mediumpro;
        lowpro = (lowpro > 100) ? 100 : lowpro;
        lowpro = (lowpro < 0) ? 0 : lowpro;

        TextView txtCommon = (TextView) findViewById(R.id.txtEvalGoodPro);
        txtCommon.setText(Integer.toString(commonpro) + "%");

        RelativeLayout rlHighBar = (RelativeLayout)findViewById(R.id.rlHaoPingBar);
        RelativeLayout rlMediumBar = (RelativeLayout)findViewById(R.id.rlZhongPingBar);
        RelativeLayout rlSmallBar = (RelativeLayout)findViewById(R.id.rlChaPingBar);

        RelativeLayout.LayoutParams layoutHighBar = (RelativeLayout.LayoutParams)rlHighBar.getLayoutParams();
        RelativeLayout.LayoutParams layoutMediumBar = (RelativeLayout.LayoutParams)rlMediumBar.getLayoutParams();
        RelativeLayout.LayoutParams layoutSmalBar = (RelativeLayout.LayoutParams)rlSmallBar.getLayoutParams();

        layoutHighBar.width = (int)(proWidth * highpro * ResolutionSet.fXpro / 100 );
        layoutMediumBar.width = (int)(proWidth * mediumpro * ResolutionSet.fXpro / 100 );
        layoutSmalBar.width = (int)(proWidth * lowpro * ResolutionSet.fXpro / 100 );

        rlHighBar.setLayoutParams(layoutHighBar);
        rlHighBar.setBackgroundColor(Color.RED);
        rlMediumBar.setLayoutParams(layoutMediumBar);
        rlMediumBar.setBackgroundColor(Color.RED);
        rlSmallBar.setLayoutParams(layoutSmalBar);
        rlSmallBar.setBackgroundColor(Color.RED);
    }

    ////////////////////////////////// Button Clicked Event ///////////////////////////////////////
    /**
     * Submit button clicked event
     */
    private void onClickSubmitQuery()
    {
        TextView editTxtEval = (TextView) findViewById(R.id.editTxtEval);
        String query = editTxtEval.getText().toString();
        if (query.length() > 0)
        {
            // hide keyboard before calling service.
            InputMethodManager inputMethodManager = (InputMethodManager)  getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            callSubmitProductQuestion(mProdId, query, GlobalData.token);
        }
        else
        {
            editTxtEval.setFocusable(true);
            editTxtEval.requestFocus();

            final InputMethodManager inputMethodManager;
            inputMethodManager = (InputMethodManager) this
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            boolean b = inputMethodManager.showSoftInput(editTxtEval, InputMethodManager.SHOW_IMPLICIT);
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
            GlobalData.showToast(ArticlesActivity.this, getString(R.string.MSG_NoSel_Data));
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
            GlobalData.showToast(ArticlesActivity.this, getString(R.string.MSG_NoSel_Data));
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

        if (GlobalData.token.length() <= 0)
        {
            Intent intent = new Intent(ArticlesActivity.this, PersonInfoActivity.class);
            startActivity(intent);
            return;
        }

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
            item.price = Double.parseDouble(lblHuiYuanPrice.getText().toString());
            item.image = mDetailInfo.arrImgUrl.get(0);
            item.count = Integer.parseInt(lblBuyAmount.getText().toString());
            item.name = mDetailInfo.name;

            // make basket item parcelable array ( with one item )
            ArrayList < STParcelableBasketItem > parcelArray = new ArrayList<STParcelableBasketItem>(0);
            {
                STParcelableBasketItem parcelitem = new STParcelableBasketItem(item);
                parcelArray.add(parcelitem);
            }

            Intent intent = new Intent(ArticlesActivity.this, OrderConfirmActivity.class);
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

    /**
     * Clicked buy button when it is 1yuanquan
     */
    private void onClick1YuanBuy()
    {
        int spec1Id = 0;
        int spec2Id = 0;

        if (GlobalData.token.length() <= 0)
        {
            Intent intent = new Intent(ArticlesActivity.this, PersonInfoActivity.class);
            startActivity(intent);
            return;
        }

        try
        {
            // make shopping item data
            STBasketItemInfo item = new STBasketItemInfo();
            item.pid = mDetailInfo.id;
            item.spec1 = spec1Id;
            item.spec2 = spec2Id;
            item.price = Double.parseDouble(lblHuiYuanPrice.getText().toString());
            item.image = mDetailInfo.arrImgUrl.get(0);
            item.count = Integer.parseInt(lblBuyAmount.getText().toString());
            item.name = mDetailInfo.name;

            // make basket item parcelable array ( with one item )
            ArrayList < STBasketItemInfo > itemArray = new ArrayList<STBasketItemInfo>(0);
            {
                itemArray.add(item);
            }

            callGiveOrder(itemArray, 1, 1, "", item.price * item.count, 0.0);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    /**
     * Go to pay activity
     */
    private void gotoPay(String orderNo)
    {
        double price = 0.0f;
        int count = 0;

        try
        {
            price = Double.parseDouble(lblHuiYuanPrice.getText().toString());
            count = Integer.parseInt(lblBuyAmount.getText().toString());

            Intent intent = new Intent(ArticlesActivity.this, PaymentActivity.class);
            intent.putExtra("OrderNo", orderNo);
            // intent.putExtra("TotalPrice", price * count); // get the total price in PaymentActivity.
            startActivity(intent);
            finish();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

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

                // clear previous data before update
                mDetailInfo.arrComments.clear();
                mDetailInfo.arrConsult.clear();
                mDetailInfo.arrImgUrl.clear();
                mDetailInfo.arrSpecData1.clear();
                mDetailInfo.arrSpecData2.clear();
                mDetailInfo.arrValidSpecPairs.clear();

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

                GlobalData.showToast(ArticlesActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                progDialog.dismiss();
                if (result == STServiceData.ERR_FAIL)
                {
                    GlobalData.showToast(ArticlesActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                ArticlesActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.GetProductDetail(token, prodId, width, height, handler);

        return;
    }

    /**
     * Call submit question of product
     * @param prodId [in], product id
     * @param query [in], string of query
     * @param token [in], user token
     */
    private void callSubmitProductQuestion(int prodId, String query, String token)
    {
        if (token.length() <= 0)
        {
            Intent intent = new Intent(ArticlesActivity.this, PersonInfoActivity.class);
            startActivity(intent);
            return;
        }

        handler1 = new JsonHttpResponseHandler()
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

                    // clear text input
                    TextView editTxtEval = (TextView) findViewById(R.id.editTxtEval);
                    editTxtEval.setText("");

                    // show success alert
                    //retMsg = getString(R.string.MSG_Success);
                    callGetProductDetail(GlobalData.token, mProdId, ITEM_IMG_WIDTH, ITEM_IMG_HEIGHT);

                    return;
                }
                else
                {
                    result = STServiceData.ERR_EXCEPTION;
                }

                GlobalData.showToast(ArticlesActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                progDialog.dismiss();
                if (result == STServiceData.ERR_FAIL)
                {
                    GlobalData.showToast(ArticlesActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                ArticlesActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.SubmitProductQuestion(prodId, query, token, handler1);

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
            Intent intent = new Intent(ArticlesActivity.this, PersonInfoActivity.class);
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

                retMsg = CommMgr.commService.parseAddProductToShopCarts(object);
                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    // update shopcart badge number
                    updateCartNum();

                    result = STServiceData.ERR_SUCCESS;
                    // show success alert
                    if (bAddAndGo)
                    {
                        CommonFunc.GoToShopCart(ArticlesActivity.this);
                    }
                    else
                    {
                        retMsg = getString(R.string.MSG_Success);
                        GlobalData.showToast(ArticlesActivity.this, retMsg);
                    }

                    bAddAndGo = false;
                    return;
                }
                else
                {
                    result = STServiceData.ERR_EXCEPTION;
                }

                GlobalData.showToast(ArticlesActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                progDialog.dismiss();
                if (result == STServiceData.ERR_FAIL)
                {
                    GlobalData.showToast(ArticlesActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                ArticlesActivity.this,
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
            Intent intent = new Intent(ArticlesActivity.this, PersonInfoActivity.class);
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

                    Intent intent = new Intent(ArticlesActivity.this, ShouCangActivity.class);
                    startActivity(intent);
                    return;
                }
                else
                {
                    result = STServiceData.ERR_EXCEPTION;
                }

                GlobalData.showToast(ArticlesActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                progDialog.dismiss();
                if (result == STServiceData.ERR_FAIL)
                {
                    GlobalData.showToast(ArticlesActivity.this, getString(R.string.server_connection_error));
                }
                else if (result != STServiceData.ERR_SUCCESS)
                {
                    GlobalData.showToast(ArticlesActivity.this, retMsg);
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                ArticlesActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.RequestCollectProduct(GlobalData.token, nProducdtId, handler);
    }



    /**
     * Call GetReceivers service
     * This is only used for 1yuanquan of buy
     */
    private void callGiveOrder(ArrayList<STBasketItemInfo> arrProd, int payment, int recvtype,
                               String comment, Double totalprice, Double transprice) {
        handler1Yuan = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = "";

                progDialog.dismiss();

                STString retVal = new STString();
                retMsg = CommMgr.commService.parseGiveOrder(object, retVal);
                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    result = STServiceData.ERR_SUCCESS;

                    gotoPay(retVal.szVal);
                    return;
                }
                else
                {
                    result = STServiceData.ERR_EXCEPTION;
                }

                GlobalData.showToast(ArticlesActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                if (result == STServiceData.ERR_FAIL)
                {
                    progDialog.dismiss();
                    GlobalData.showToast(ArticlesActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                ArticlesActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.GiveOrder(arrProd, payment, recvtype, comment, totalprice, transprice, GlobalData.token, handler1Yuan);
    }
}
