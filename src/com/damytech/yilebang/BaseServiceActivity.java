package com.damytech.yilebang;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.*;
import cn.sharesdk.framework.ShareSDK;
import com.damytech.CommService.CommMgr;
import com.damytech.Global.CommonFunc;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.STData.STAdvertInfo;
import com.damytech.STData.STHomeProdDataInfo;
import com.damytech.STData.STProductList;
import com.damytech.STData.STServiceData;
import com.damytech.Global.GlobalData;
import com.damytech.Utils.HorizontalPager;
import com.damytech.Utils.HorizontalPager.OnScreenSwitchListener;
import com.damytech.Utils.ResolutionSet;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import com.google.zxing.client.android.CaptureActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import org.json.JSONObject;

public class BaseServiceActivity extends MyActivity {
	
	private HorizontalPager mGallery = null;
	private static int TIME_INTERVAL = 2000;
	Timer mTimer = new Timer();

    public static final int HOME_PRODCOUNT = 3;

    private RelativeLayout imgBaseService_Footer_Packet = null;

	//private Integer[] mImageIDs = {R.drawable.bmp1,  R.drawable.bmp2, R.drawable.bmp3, R.drawable.bmp4};
	private RelativeLayout mSeparate[];
    private TextView mZhuanWaiKuaiTitle;
	private RelativeLayout mSeparate_ZhuanWaiKuai_Goods[];
	private RelativeLayout mZhuanWaiKuai_Goods[];
    private ImageView mImgZhuanWaiKuai_Goods[];
    private TextView mPriceZhuanWaiKuai_Goods[];
    private TextView mCommentZhuanWaiKuai_Goods;
    private TextView mCommmissionZhuanWaiKuai_Goods;

    private ImageView m_imgQRCode;

    private TextView mQianChaTitle;
    private RelativeLayout mSeparate_QianCha_Goods[];
    private RelativeLayout mQianCha_Goods[];
    private ImageView mImgQianCha_Goods[];
    private TextView mPriceQianCha_Goods[];
    private TextView mCommentQianCha_Goods;
    private TextView mCommmissionQianCha_Goods;

    private RelativeLayout mFooters[];
    private int nSlideImgCnt = 0;

    final int SEPERATOR_WIDTH = 88;
    final int SEPERATOR_HEIGHT = 5;
    final int SEPERATOR_TOPMARGIN = 200;
	//private TextView lblZhuanWaiKuai;

    private ArrayList<STAdvertInfo> mAdvertList;

    private int ADVERT_IMG_WDITH = 100;

    private JsonHttpResponseHandler handler;
    private JsonHttpResponseHandler handler1;
    private ProgressDialog progDialog;

    DisplayImageOptions options;

    private STHomeProdDataInfo mProdList1 = new STHomeProdDataInfo();
    private STHomeProdDataInfo mProdList2 = new STHomeProdDataInfo();

    private Button btnWaiKuai_Share = null;
    private Button btnWaiKuai_CopyUrl = null;

    private int nCurWaiKuai = 0;
    private int nCurQianCha = 0;

    @Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baseservice);

        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_stub)
                .showImageOnFail(R.drawable.ic_stub)
                .cacheInMemory()
                .cacheOnDisc()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading()
                .build();

        m_imgQRCode = (ImageView) findViewById(R.id.imgBaseService_QRBar);
        m_imgQRCode.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaseServiceActivity.this, CaptureActivity.class);
                startActivity(intent);
            }
        });

        mGallery = (HorizontalPager)findViewById(R.id.hpBaseService_Body);
		mGallery.setCurrentScreen(0, false);
		mGallery.setOnScreenSwitchListener(new OnScreenSwitchListener()	{
			@Override
			public void onScreenSwitched(int screen) {
				mGallery.setCurrentScreen(screen, false);
				setSeparateColor(screen);
			}

		});
		/*
		lblZhuanWaiKuai =(TextView) findViewById(R.id.lblBaseService_ZhuanWaiKuai);
		Typeface tf = Typeface.createFromAsset(getAssets(), "simsunb.ttf");
		lblZhuanWaiKuai.setTypeface(tf);
		*/
		
		mSeparate = new RelativeLayout[5];
		mSeparate[0] = (RelativeLayout) findViewById(R.id.rlBaseService_Separatre1);
		mSeparate[1] = (RelativeLayout) findViewById(R.id.rlBaseService_Separatre2);
		mSeparate[2] = (RelativeLayout) findViewById(R.id.rlBaseService_Separatre3);
		mSeparate[3] = (RelativeLayout) findViewById(R.id.rlBaseService_Separatre4);
		mSeparate[4] = (RelativeLayout) findViewById(R.id.rlBaseService_Separatre5);

        mSeparate[0].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mGallery.setCurrentScreen(0, false);
                setSeparateColor(0);
            }
        });

        mSeparate[1].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mGallery.setCurrentScreen(1, false);
                setSeparateColor(1);
            }
        });

        mSeparate[2].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mGallery.setCurrentScreen(2, false);
                setSeparateColor(2);
            }
        });

        mSeparate[3].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mGallery.setCurrentScreen(3, false);
                setSeparateColor(3);
            }
        });

        mSeparate[4].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mGallery.setCurrentScreen(4, false);
                setSeparateColor(4);
            }
        });

        mFooters = new RelativeLayout[5];
		mFooters[0] = (RelativeLayout) findViewById(R.id.rlBaseService_Footer_Home);
		mFooters[0].setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v)
			{
			}
		});
		mFooters[1] = (RelativeLayout) findViewById(R.id.rlBaseService_Footer_BackArraw);
		mFooters[1].setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v)
			{
			}
		});
		mFooters[2] = (RelativeLayout) findViewById(R.id.rlBaseService_Footer_MainMenu);
		mFooters[2].setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v)
			{				
				Intent intent = new Intent(BaseServiceActivity.this, MainMenuActivity.class);
				startActivity(intent);
			}
		});
		mFooters[3] = (RelativeLayout) findViewById(R.id.rlBaseService_Footer_Packet);
		mFooters[3].setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v)
			{
                CommonFunc.GoToShopCart(BaseServiceActivity.this);
			}
		});
		mFooters[4] = (RelativeLayout) findViewById(R.id.rlBaseService_Footer_PersonInfo);
		mFooters[4].setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				CommonFunc.GoToAccount(BaseServiceActivity.this);
			}
		});

        //////////////////////////////////////// ZhuanWaiKuai Relation controls
        mZhuanWaiKuaiTitle = (TextView) findViewById(R.id.lblBaseService_ZhuanWaiKaui_GoodTitle);

		mSeparate_ZhuanWaiKuai_Goods = new RelativeLayout[3];
		mSeparate_ZhuanWaiKuai_Goods[0] = (RelativeLayout) findViewById(R.id.rlBaseService_ZhuanWaiKuai_GoodKindFrame_Separate1);
		mSeparate_ZhuanWaiKuai_Goods[1] = (RelativeLayout) findViewById(R.id.rlBaseService_ZhuanWaiKuai_GoodKindFrame_Separate2);
		mSeparate_ZhuanWaiKuai_Goods[2] = (RelativeLayout) findViewById(R.id.rlBaseService_ZhuanWaiKuai_GoodKindFrame_Separate3);

        mImgZhuanWaiKuai_Goods = new ImageView[3];
        mImgZhuanWaiKuai_Goods[0] = (ImageView) findViewById(R.id.imgBaseService_ZhuanWaiKuai_Good_1);
        mImgZhuanWaiKuai_Goods[1] = (ImageView) findViewById(R.id.imgBaseService_ZhuanWaiKuai_Good_2);
        mImgZhuanWaiKuai_Goods[2] = (ImageView) findViewById(R.id.imgBaseService_ZhuanWaiKuai_Good_3);

        mPriceZhuanWaiKuai_Goods = new TextView[3];
        mPriceZhuanWaiKuai_Goods[0] = (TextView) findViewById(R.id.imgBaseService_ZhuanWaiKuai_GoodPrice_1);
        mPriceZhuanWaiKuai_Goods[1] = (TextView) findViewById(R.id.imgBaseService_ZhuanWaiKuai_GoodPrice_2);
        mPriceZhuanWaiKuai_Goods[2] = (TextView) findViewById(R.id.imgBaseService_ZhuanWaiKuai_GoodPrice_3);

        mCommentZhuanWaiKuai_Goods = (TextView) findViewById(R.id.lblBaseService_ZhuanWaiKuai_Goods_Commend);
        mCommentZhuanWaiKuai_Goods.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickedWaiKuai_Comment(nCurWaiKuai);
            }
        });
        mCommmissionZhuanWaiKuai_Goods = (TextView) findViewById(R.id.lblBaseService_ZhuanWaiKuai_Goods_PriceValue);

        mZhuanWaiKuai_Goods = new RelativeLayout[3];
		mZhuanWaiKuai_Goods[0] = (RelativeLayout) findViewById(R.id.rlBaseService_ZhuanWaiKuai_Goods_1);
		mZhuanWaiKuai_Goods[0].setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v)
			{
                OnClickedWaiKuai_Goods(0);
			}
		});
		mZhuanWaiKuai_Goods[1] = (RelativeLayout) findViewById(R.id.rlBaseService_ZhuanWaiKuai_Goods_2);
		mZhuanWaiKuai_Goods[1].setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				OnClickedWaiKuai_Goods(1);
			}
		});
		mZhuanWaiKuai_Goods[2] = (RelativeLayout) findViewById(R.id.rlBaseService_ZhuanWaiKuai_Goods_3);
        mZhuanWaiKuai_Goods[2].setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				OnClickedWaiKuai_Goods(2);
			}
		});


        //////////////////////////////////////// QianCha Relation controls
        mQianChaTitle = (TextView) findViewById(R.id.lblBaseService_QianCha_GoodTitle);

        mSeparate_QianCha_Goods = new RelativeLayout[3];
        mSeparate_QianCha_Goods[0] = (RelativeLayout) findViewById(R.id.rlBaseService_QianCha_GoodKindFrame_Separate1);
        mSeparate_QianCha_Goods[1] = (RelativeLayout) findViewById(R.id.rlBaseService_QianCha_GoodKindFrame_Separate2);
        mSeparate_QianCha_Goods[2] = (RelativeLayout) findViewById(R.id.rlBaseService_QianCha_GoodKindFrame_Separate3);

        mImgQianCha_Goods = new ImageView[3];
        mImgQianCha_Goods[0] = (ImageView) findViewById(R.id.imgBaseService_QianCha_Good_1);
        mImgQianCha_Goods[1] = (ImageView) findViewById(R.id.imgBaseService_QianCha_Good_2);
        mImgQianCha_Goods[2] = (ImageView) findViewById(R.id.imgBaseService_QianCha_Good_3);

        mPriceQianCha_Goods = new TextView[3];
        mPriceQianCha_Goods[0] = (TextView) findViewById(R.id.imgBaseService_QianCha_GoodPrice_1);
        mPriceQianCha_Goods[1] = (TextView) findViewById(R.id.imgBaseService_QianCha_GoodPrice_2);
        mPriceQianCha_Goods[2] = (TextView) findViewById(R.id.imgBaseService_QianCha_GoodPrice_3);

        mCommentQianCha_Goods = (TextView) findViewById(R.id.lblBaseService_QianCha_Goods_Commend);
        mCommentQianCha_Goods.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickedQianCha_Comment(nCurQianCha);
            }
        });
        mCommmissionQianCha_Goods = (TextView) findViewById(R.id.lblBaseService_QianCha_Goods_PriceValue);

        mQianCha_Goods = new RelativeLayout[3];
        mQianCha_Goods[0] = (RelativeLayout) findViewById(R.id.rlBaseService_QianCha_Goods_1);
        mQianCha_Goods[0].setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v)
            {
                OnClickedQianCha_Goods(0);
            }
        });
        mQianCha_Goods[1] = (RelativeLayout) findViewById(R.id.rlBaseService_QianCha_Goods_2);
        mQianCha_Goods[1].setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v)
            {
                OnClickedQianCha_Goods(1);
            }
        });
        mQianCha_Goods[2] = (RelativeLayout) findViewById(R.id.rlBaseService_QianCha_Goods_3);
        mQianCha_Goods[2].setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v)
            {
                OnClickedQianCha_Goods(2);
            }
        });


        //////////////////////////////////////// Main Control Data
        RelativeLayout llMainFunc1 = (RelativeLayout)findViewById(R.id.llFunc1);
        llMainFunc1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BaseServiceActivity.this, EarnMoneyActivity.class));
            }
        });

        RelativeLayout llMainFunc2 = (RelativeLayout)findViewById(R.id.llFunc2);
        llMainFunc2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaseServiceActivity.this, SpecialArticleActivity.class);
                intent.putExtra("SearchMode", 1);   // search by group id
                intent.putExtra("PROD_ID", STServiceData.PROD_ID_1YUANQUAN);
                startActivity(intent);
            }
        });

        RelativeLayout llMainFunc3 = (RelativeLayout)findViewById(R.id.llFunc3);
        llMainFunc3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaseServiceActivity.this, PreferentialActivity.class);
                intent.putExtra("WithPos", 0);
                startActivity(intent);
            }
        });

        RelativeLayout llMainFunc4 = (RelativeLayout)findViewById(R.id.llFunc4);
        llMainFunc4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaseServiceActivity.this, SpecialArticleActivity.class);
                intent.putExtra("SearchMode", 1);   // search by group id
                intent.putExtra("PROD_ID", STServiceData.PROD_ID_CHAHUI);
                startActivity(intent);
            }
        });

        RelativeLayout llMainFunc5 = (RelativeLayout)findViewById(R.id.llFunc5);
        llMainFunc5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaseServiceActivity.this, SpecialArticleActivity.class);
                intent.putExtra("SearchMode", 1);   // search by group id
                intent.putExtra("PROD_ID", STServiceData.PROD_ID_JIUHUI);
                startActivity(intent);
            }
        });

        RelativeLayout llMainFunc6 = (RelativeLayout)findViewById(R.id.llFunc6);
        llMainFunc6.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaseServiceActivity.this, SpecialArticleActivity.class);
                intent.putExtra("SearchMode", 1);   // search by group id
                intent.putExtra("PROD_ID", STServiceData.PROD_ID_TECHAN);
                startActivity(intent);
            }
        });

        ///////////////////////////////// More Text Event /////////////////////////////////
        TextView txtWaikuai = (TextView) findViewById(R.id.lblBaseService_ZhuanWaiKaui_GoodDetailTitle);
        txtWaikuai.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BaseServiceActivity.this, EarnMoneyActivity.class));
            }
        });

        TextView txtTeChan = (TextView) findViewById(R.id.lblBaseService_QianCha_GoodDetailTitle);
        txtTeChan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaseServiceActivity.this, SpecialArticleActivity.class);
                intent.putExtra("SearchMode", 1);   // search by group id
                intent.putExtra("PROD_ID", STServiceData.PROD_ID_JIUHUI);
                startActivity(intent);
            }
        });

        ResolutionSet._instance.iterateChild(findViewById(R.id.rlBaseServiceLayout));

        // shop cart badge
        imgBaseService_Footer_Packet = (RelativeLayout) findViewById(R.id.rlBaseService_Footer_Packet);
        this.setBadgeParent(imgBaseService_Footer_Packet);


        btnWaiKuai_Share = (Button) findViewById(R.id.btnBaseService_ZhuanWaiKuai_Share);
        btnWaiKuai_Share.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    STProductList info = mProdList1.arrProducts.get(nCurWaiKuai);
                    CommonFunc.showFenXiang(BaseServiceActivity.this, info.id, info.image);
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });

        btnWaiKuai_CopyUrl = (Button) findViewById(R.id.btnBaseService_ZhuanWaiKuai_Duplicate);
        btnWaiKuai_CopyUrl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    STProductList info = mProdList1.arrProducts.get(nCurWaiKuai);
                    CommonFunc.Copy2Clipboard(BaseServiceActivity.this, info.id, info.image);
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });


        callGetHomeProdService(Integer.toString(HOME_PRODCOUNT), "100");
	}
	
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
			if (nSelection == mAdvertList.size() - 1)
				nSelection = 0;
			else
				nSelection++;

			mGallery.setCurrentScreen(nSelection, true);
			setSeparateColor(nSelection);
		}
	};
	
	private void setSeparateColor(int nNo)
	{
		for ( int i = 0; i < 5; i++ )
		{
			if ( i == nNo )
				mSeparate[i].setBackgroundColor(getResources().getColor(R.color.red));
			else
				mSeparate[i].setBackgroundColor(getResources().getColor(R.color.gray));
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

    /**
     * load advertisement images
     */
	public void loadAdvertiseView()
	{
        for (int i = 0; i < 5; i++)
        {
            mSeparate[i].setVisibility(View.GONE);
        }

        if (mAdvertList.size() == 0)
			return;

		for (int i = 0; i < mAdvertList.size(); i++)
		{
			ImageView imgView = new ImageView(mGallery.getContext());
			//imgView.setBackgroundResource(mAdvertImgs.get(i));
            imgView.setLayoutParams(new RelativeLayout.LayoutParams(mGallery.getWidth(), mGallery.getHeight()));
            GlobalData.imageLoader.displayImage(mAdvertList.get(i).imagepath, imgView, options);
            mGallery.addView(imgView);
		}

        int paddingwidth = (int)((480 - SEPERATOR_WIDTH * mAdvertList.size()) / (mAdvertList.size() + 1));
        int left = paddingwidth, top = SEPERATOR_TOPMARGIN;
        for (int i = 0; i < mAdvertList.size(); i++)
        {
            RelativeLayout.LayoutParams newLayout = new RelativeLayout.LayoutParams(SEPERATOR_WIDTH, SEPERATOR_HEIGHT);
            newLayout.leftMargin = left;
            newLayout.topMargin = SEPERATOR_TOPMARGIN;
            mSeparate[i].setLayoutParams(newLayout);
            mSeparate[i].setVisibility(View.VISIBLE);

            left += SEPERATOR_WIDTH + paddingwidth;

            ResolutionSet._instance.iterateChild(mSeparate[i]);
        }

		startTimer();
	}

    /**
     * ZhanWaiKuai product clicked event
     * @param position [in], product position
     */
    private void OnClickedWaiKuai_Goods(int position)
    {
        try
        {
            STProductList info = mProdList1.arrProducts.get(position);
            // set selected text
            mCommentZhuanWaiKuai_Goods.setText(info.name);
            String sCommmission =  Double.toString(info.commission) + getString(R.string.str_money_yuan);
            mCommmissionZhuanWaiKuai_Goods.setText(sCommmission);

            // set position of selected waikuai
            nCurWaiKuai = position;

            // show selected mark
            for (int i = 0; i < HOME_PRODCOUNT; i++)
            {
                if (position == i)
                {
                    mSeparate_ZhuanWaiKuai_Goods[i].setBackgroundResource(R.drawable.selecttriangle);
                }
                else
                {
                    mSeparate_ZhuanWaiKuai_Goods[i].setBackgroundResource(R.drawable.grayline);
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void OnClickedWaiKuai_Comment(int position)
    {
        try
        {
            STProductList info = mProdList1.arrProducts.get(position);

            // go to product detail
            try
            {
                Intent intent = new Intent(BaseServiceActivity.this, ArticlesActivity.class);
                intent.putExtra("ProductId", info.id);
                intent.putExtra("ParentId", info.cid);
                startActivity(intent);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    /**
     * QianCha product clicked event
     * @param position [in], product position
     */
    private void OnClickedQianCha_Goods(int position)
    {
        try
        {
            STProductList info = mProdList2.arrProducts.get(position);
            // set selected text
            mCommentQianCha_Goods.setText(info.name);
            String sCommmission =  Double.toString(info.commission) + getString(R.string.str_money_yuan);
            mCommmissionQianCha_Goods.setText(sCommmission);

            // set position of selected waikuai
            nCurQianCha = position;


            // show selected mark
            for (int i = 0; i < HOME_PRODCOUNT; i++)
            {
                if (position == i)
                {
                    mSeparate_QianCha_Goods[i].setBackgroundResource(R.drawable.selecttriangle);
                }
                else
                {
                    mSeparate_QianCha_Goods[i].setBackgroundResource(R.drawable.grayline);
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void OnClickedQianCha_Comment(int position)
    {
        try
        {
            STProductList info = mProdList2.arrProducts.get(position);

            // go to product detail
            try
            {
                Intent intent = new Intent(BaseServiceActivity.this, ArticlesActivity.class);
                intent.putExtra("ProductId", info.id);
                intent.putExtra("ParentId", info.cid);
                startActivity(intent);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Update UI using service data
     */
    private void UpdateUI()
    {
        try
        {
            loadAdvertiseView();

            mZhuanWaiKuaiTitle.setText(mProdList1.homeProdName);
            for (int i = 0; i < HOME_PRODCOUNT; i++)
            {
                ///////////////////////////////// Set ZhuanWaiKuai Data
                try
                {
                    // get zhuanwaikuai data
                    STProductList info = mProdList1.arrProducts.get(i);
                    // set image
                    GlobalData.imageLoader.displayImage(info.image, mImgZhuanWaiKuai_Goods[i], options);
                    // set price
                    String price = getString(R.string.str_money_yuansymbol) + Double.toString(info.price);
                    mPriceZhuanWaiKuai_Goods[i].setText(price);
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }

            mQianChaTitle.setText(mProdList2.homeProdName);
            for (int i = 0; i < HOME_PRODCOUNT; i++)
            {
                ///////////////////////////////// Set QianCha Data
                try
                {
                    // get zhuanwaikuai data
                    STProductList info1 = mProdList2.arrProducts.get(i);
                    // set image
                    GlobalData.imageLoader.displayImage(info1.image, mImgQianCha_Goods[i], options);
                    // set price
                    String price = getString(R.string.str_money_yuansymbol) + Double.toString(info1.price);
                    mPriceQianCha_Goods[i].setText(price);
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }

            // set one WaiKuai, QianCha item
            OnClickedWaiKuai_Goods(1);
            OnClickedQianCha_Goods(1);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }



    /////////////////////////////// Service Relation ///////////////////////////////////////////
    /**
     * Call GetHomeProducts Service
     * @param count [in], product count
     * @param width [in], image width
     */
    private void callGetHomeProdService(String count, String width)
    {
        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = STServiceData.MSG_SUCCESS;

                retMsg = CommMgr.commService.parseGetHomeProducts(object, mProdList1, mProdList2);
                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    result = STServiceData.ERR_SUCCESS;
                    callGetAdvertList(STServiceData.ADVERT_HOME, ADVERT_IMG_WDITH);
                    return;
                }
                else
                {
                    progDialog.dismiss();
                    result = STServiceData.ERR_EXCEPTION;
                }

                GlobalData.showToast(BaseServiceActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                if (result == STServiceData.ERR_FAIL)
                {
                    progDialog.dismiss();
                    GlobalData.showToast(BaseServiceActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                BaseServiceActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.GetHomeProducts(count, width, handler);

        return;
    }


    /**
     * Call GetAdvertList service
     * @param type [in], advertisement type
     * @param width [in], image width
     */
    private void callGetAdvertList(int type, int width)
    {
        mAdvertList = new ArrayList<STAdvertInfo>();
        handler1 = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = STServiceData.MSG_SUCCESS;

                progDialog.dismiss();

                retMsg = CommMgr.commService.parseGetAdvertList(object, mAdvertList);
                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    result = STServiceData.ERR_SUCCESS;
                    UpdateUI();
                    return;
                }
                else
                {
                    result = STServiceData.ERR_EXCEPTION;
                }

                GlobalData.showToast(BaseServiceActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                progDialog.dismiss();
                if (result == STServiceData.ERR_FAIL)
                    GlobalData.showToast(BaseServiceActivity.this, getString(R.string.server_connection_error));

                result = 0;
            }

        };

        CommMgr.commService.GetAdvertList(type, width, handler1);

        return;
    }

    ////////////////////////////////////////// User Action /////////////////////////////////
    /**
     * Exit Current Application
     */
    private void ExitApp()
    {
        boolean twiceClicked = BaseServiceActivity.showExitToast(this, getString(R.string.MainMenu_Msg_Exit));

        try
        {
            if ( twiceClicked == true )
            {
                finish();
                System.exit(0);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void onBackPressed()
    {
        ExitApp();
    }

    private static Toast exitToast = null;
    public static boolean showExitToast(Context context, String toastStr)
    {
        if ((exitToast == null) || (exitToast.getView().getWindowVisibility() != View.VISIBLE))
        {
            exitToast = Toast.makeText(context, toastStr, Toast.LENGTH_SHORT);
            exitToast.show();
            return false;
        }

        return true;
    }
}
