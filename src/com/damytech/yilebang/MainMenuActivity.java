package com.damytech.yilebang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.damytech.CommService.CommMgr;
import com.damytech.Global.GlobalData;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.STData.STAdvertInfo;
import com.damytech.STData.STServiceData;
import com.damytech.Utils.ResolutionSet;

import android.os.Bundle;
import android.app.Activity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainMenuActivity extends MyActivity {

    private ArrayList<STAdvertInfo> mAdvertList;
    private ImageView[] mAdvertImgs = null;

    private int mMaxAdvertCnt = 6;
    private int mCurAdvertCnt = 4;

    DisplayImageOptions options;

    private int IMG_WIDTH = 100;

    @Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainmenu);

        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_stub)
                .showImageOnFail(R.drawable.ic_stub)
                .cacheInMemory()
                .cacheOnDisc()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading()
                .build();

        TextView txtMainMenu = (TextView)findViewById(R.id.lblMainMenu_Body_MainMenuHome);
        //txtMainMenu.setPaintFlags(txtMainMenu.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

		ResolutionSet._instance.iterateChild(findViewById(R.id.rlMainMenuLayout));

        RelativeLayout rlMainBasket = (RelativeLayout)findViewById(R.id.rlMainMenu_Basket);
        rlMainBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenuActivity.this, SpecialArticleMainActivity.class));
            }
        });

        RelativeLayout rlMainWaiKuai = (RelativeLayout)findViewById(R.id.rlMainMenu_WaiKuai);
        rlMainWaiKuai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenuActivity.this, EarnMoneyActivity.class));
            }
        });

        RelativeLayout rlMainYiYuan = (RelativeLayout)findViewById(R.id.rlMainMenu_YiYuan);
        rlMainYiYuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, SpecialArticleActivity.class);
                intent.putExtra("SearchMode", 1);   // search by group id
                intent.putExtra("PROD_ID", STServiceData.PROD_ID_1YUANQUAN);
                startActivity(intent);
            }
        });

        RelativeLayout rlMainPrefer = (RelativeLayout)findViewById(R.id.rlMainMenu_Preferential);
        rlMainPrefer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, PreferentialActivity.class);
                intent.putExtra("WithPos", 0);
                startActivity(intent);
            }
        });

        RelativeLayout rlMainCaiHui = (RelativeLayout)findViewById(R.id.rlMainMenu_CaiProduct);
        rlMainCaiHui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, SpecialArticleActivity.class);
                intent.putExtra("SearchMode", 1);   // search by group id
                intent.putExtra("PROD_ID", STServiceData.PROD_ID_CHAHUI);
                startActivity(intent);
            }
        });

        RelativeLayout rlMainJiuHui = (RelativeLayout)findViewById(R.id.rlMainMenu_JiuProduct);
        rlMainJiuHui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, SpecialArticleActivity.class);
                intent.putExtra("SearchMode", 1);   // search by group id
                intent.putExtra("PROD_ID", STServiceData.PROD_ID_JIUHUI);
                startActivity(intent);
            }
        });

        RelativeLayout rlMainTeChan = (RelativeLayout)findViewById(R.id.rlMainMenu_TeChanProduct);
        rlMainTeChan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, SpecialArticleActivity.class);
                intent.putExtra("SearchMode",1);   // search by group id
                intent.putExtra("PROD_ID", STServiceData.PROD_ID_TECHAN);
                startActivity(intent);
            }
        });

        RelativeLayout rlMainRestaurant = (RelativeLayout)findViewById(R.id.rlMainMenu_Restaurant);
        rlMainRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, PreferentialActivity.class);
                intent.putExtra("WithPos", 1);
                startActivity(intent);
            }
        });

        initCartBadge();
        initAdvertView();
        callGetAdvertList(STServiceData.ADVERT_HOME, IMG_WIDTH);
        initMainMenu();
    }

    private void initMainMenu()
    {
        RelativeLayout rlBottom_Home = (RelativeLayout) findViewById(R.id.rlMainMenu_Footer_Home);
        rlBottom_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgbtnHomeClicked();
            }
        });

        RelativeLayout rlBootom_Back = (RelativeLayout) findViewById(R.id.rlMainMenu_Footer_BackArraw);
        rlBootom_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgbtnBackClicked();
            }
        });

//        RelativeLayout rlBottom_MainMenu = (RelativeLayout) findViewById(R.id.rlBottom_MainMenu);
//        rlBottom_MainMenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onImgbtnMainMenuClicked();
//            }
//        });

        RelativeLayout rlBottom_Packet = (RelativeLayout) findViewById(R.id.rlMainMenu_Footer_Packet);
        rlBottom_Packet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgbtnCartClicked();
            }
        });

        RelativeLayout rlBottom_PersonInfo = (RelativeLayout) findViewById(R.id.rlMainMenu_Footer_PersonInfo);
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
        ImageView imgPacket = (ImageView) findViewById(R.id.imgMainMenu_Footer_Packet);
        this.setBadgeParent(imgPacket);
    }

    private void initAdvertView()
    {
        mAdvertImgs = new ImageView[mMaxAdvertCnt];
        mAdvertImgs[0] = (ImageView) findViewById(R.id.imgItem1);
        mAdvertImgs[1] = (ImageView) findViewById(R.id.imgItem2);
        mAdvertImgs[2] = (ImageView) findViewById(R.id.imgItem3);
        mAdvertImgs[3] = (ImageView) findViewById(R.id.imgItem4);
        mAdvertImgs[4] = (ImageView) findViewById(R.id.imgItem5);
        mAdvertImgs[5] = (ImageView) findViewById(R.id.imgItem6);
    }

    private void UpdateUI()
    {
        loadAdvertiseView();
    }

    /**
     * load advertisement images
     */
    public void loadAdvertiseView()
    {
        mCurAdvertCnt = (mAdvertList.size() > mMaxAdvertCnt) ? mMaxAdvertCnt : mAdvertList.size();

        if (mCurAdvertCnt == 0)
            return;

        for (int i = 0; i < mCurAdvertCnt; i++)
        {
            mAdvertImgs[i].setVisibility(View.VISIBLE);
            GlobalData.imageLoader.displayImage(mAdvertList.get(i).imagepath, mAdvertImgs[i], options);
        }
    }

    /////////////////////////////////////// Service Relation //////////////////////////////////
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

                GlobalData.showToast(MainMenuActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                progDialog.dismiss();
                if (result == STServiceData.ERR_FAIL)
                    GlobalData.showToast(MainMenuActivity.this, getString(R.string.server_connection_error));

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                MainMenuActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.GetAdvertList(type, width, handler1);

        return;
    }
}
