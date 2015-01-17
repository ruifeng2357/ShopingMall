package com.damytech.yilebang.HuiYuanZhongXin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.damytech.CommService.CommMgr;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.STData.*;
import com.damytech.Global.GlobalData;
import com.damytech.Utils.ResolutionSet;
import com.damytech.yilebang.MyActivity;
import com.damytech.yilebang.PersonInfoActivity;
import com.damytech.yilebang.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-22
 * Time: 下午4:12
 * To change this template use File | Settings | File Templates.
 */
public class HuiYuanZhongXinActivity extends MyActivity {
    // UI control variables
    ImageView  m_imgUserPhoto;
	ImageButton m_btnSelPhoto;
    TextView  m_txtUserName;
    TextView  m_txtNewsCount;
    ImageView  m_imgBangBi;
	ImageButton m_btnBangBi;
    TextView  m_txtBangBiAmount;
    ImageButton  m_imgbtnLogout;
    ImageView  m_imgOrderHistory;
    ImageView  m_imgUnpaiedOrders;
    TextView  m_txtUnpaiedOrdersCount;
    ImageView  m_imgNews;
    ImageView  m_imgBangKe;
    ImageView  m_imgCoupon;
    TextView  m_txtCouponCount;
    ImageView  m_imgFavorite;
    TextView  m_txtFavoriteCount;
    ImageView  m_imgReceiverManage;
    ImageView  m_imgChangePassword;
    ImageButton  m_imgbtnGiftCard;
    ImageButton  m_imgbtnReviewAndAdvice;
    ImageButton  m_imgbtnSuggestions;

    ImageButton  m_imgbtnHome;
    ImageButton  m_imgbtnBack;
    ImageButton  m_imgbtnMainMenu;
    ImageButton  m_imgbtnCart;
    ImageButton  m_imgbtnAccount;

//    STDouble m_fBangBi = new STDouble();
//    STInteger m_nOrdersUnpaied = new STInteger();
//    STInteger  m_nNewsUnread = new STInteger();
//    STInteger  m_nCouponCount = new STInteger();
//    STInteger  m_nFavoriteCount = new STInteger();

	private int REQUEST_PHOTO = 0;
	STMemberBasicValue m_stGemMemberInfo = new STMemberBasicValue();
    DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.huiyuanzhongxin);

        ResolutionSet._instance.iterateChild(findViewById(R.id.rlMain));

        getControlVariables ();

        connectSignalHandlers();

        initCartBadge();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        loadInitialData();
    }

    /**
     * Initialize badge icon of shopping cart
     */
    private void initCartBadge()
    {
        ImageButton imgPacket = (ImageButton) findViewById(R.id.imgbtnCart);
        this.setBadgeParent(imgPacket);
    }

    private void getControlVariables () {

        m_imgUserPhoto = (ImageView) findViewById(R.id.imgUserPhoto);
		m_btnSelPhoto = (ImageButton)findViewById(R.id.imgSelPhoto);
        m_txtUserName = (TextView) findViewById(R.id.txtUserName);
        m_txtNewsCount = (TextView) findViewById(R.id.txtNewsCount);
        m_imgBangBi = (ImageView) findViewById(R.id.imgBangBi);
		m_btnBangBi = (ImageButton)findViewById(R.id.btnBangBi);
        m_txtBangBiAmount = (TextView) findViewById(R.id.txtBangBiAmount);
        m_imgbtnLogout = (ImageButton) findViewById(R.id.imgbtnRefresh);
        m_imgOrderHistory = (ImageView) findViewById(R.id.imgOrderHistory);
        m_imgUnpaiedOrders = (ImageView) findViewById(R.id.imgUnpaiedOrders);
        m_txtUnpaiedOrdersCount = (TextView) findViewById(R.id.txtUnpaiedOrdersCount);
        m_imgNews = (ImageView) findViewById(R.id.imgNews);
        m_imgBangKe = (ImageView) findViewById(R.id.imgBangKe);
        m_imgCoupon = (ImageView) findViewById(R.id.imgCoupon);
        m_txtCouponCount = (TextView) findViewById(R.id.txtCouponCount);
        m_imgFavorite = (ImageView) findViewById(R.id.imgFavorite);
        m_txtFavoriteCount = (TextView) findViewById(R.id.txtFavoriteCount);
        m_imgReceiverManage = (ImageView) findViewById(R.id.imgReceiverManage);
        m_imgChangePassword = (ImageView) findViewById(R.id.imgChangePassword);
        m_imgbtnGiftCard = (ImageButton) findViewById(R.id.imgbtnGiftCard);
        m_imgbtnReviewAndAdvice = (ImageButton) findViewById(R.id.imgbtnReviewAndAdvice);
        m_imgbtnSuggestions = (ImageButton) findViewById(R.id.imgbtnSuggestions);
        m_imgbtnHome = (ImageButton) findViewById(R.id.imgbtnHome);
        m_imgbtnBack = (ImageButton) findViewById(R.id.imgbtnBack);
        m_imgbtnMainMenu = (ImageButton) findViewById(R.id.imgbtnMainMenu);
        m_imgbtnCart = (ImageButton) findViewById(R.id.imgbtnCart);
        m_imgbtnAccount = (ImageButton) findViewById(R.id.imgbtnAccount);

        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.membercentericon)
                .showImageForEmptyUri(R.drawable.membercentericon)
                .showImageOnFail(R.drawable.membercentericon)
                .cacheInMemory()
                .cacheOnDisc()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading()
                .build();
    }

    private void connectSignalHandlers () {
		m_btnSelPhoto.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				showPhotoSelectList();
			}
		});

        m_imgOrderHistory.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDingDanZhuangTaiChaXun();
            }
        });

        m_imgUnpaiedOrders.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDingDan();
            }
        });

        m_imgNews.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v) {
                goToXiaoXi();
            }
        });

        m_imgBangKe.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBangKe();
            }
        });

        m_imgCoupon.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v) {
                goToYouHuiQuan();
            }
        });

        m_imgFavorite.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v) {
                goToShouCang();
            }
        });

        m_imgReceiverManage.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDiZhiGuanLi();
            }
        });

        m_imgChangePassword.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v) {
                goToXiuGaiMiMa();
            }
        });

        m_imgbtnGiftCard.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLiPinKaList();
            }
        });

        m_imgbtnReviewAndAdvice.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPingLun();
            }
        });

        m_imgbtnSuggestions.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v) {
                goToJianYi();
            }
        });

        m_imgbtnLogout.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        m_btnBangBi.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBangKe();
            }
        });

        m_imgbtnBack.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgbtnBackClicked();
            }
        });

        m_imgbtnHome.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgbtnHomeClicked();
            }
        });

        m_imgbtnMainMenu.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgbtnMainMenuClicked();
            }
        });

        m_imgbtnCart.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v) {
                onImgbtnCartClicked();
            }
        });
    }

	private void showProgressDlg()
	{
		progDialog = ProgressDialog.show(
				HuiYuanZhongXinActivity.this,
				"",
				getString(R.string.waiting),
				true,
				false,
				null);
	}

	private void hideProgressDlg()
	{
		progDialog.dismiss();
	}

    private void loadInitialData ()
	{
		showProgressDlg();

//        callGetBangbiValue();
//       callGetUnpaiedCount();
//        callGetUnreadNewsCount();
//        callGetCouponCount();
//        callGetFavoriteCount();
		callGetMemberInfo();

        hideProgressDlg();
        //callGetBangbiValue();
        //callGetUnpaiedCount();
        //callGetUnreadNewsCount();
        //callGetCouponCount();
        //callGetFavoriteCount();
    }




    private void updateUI () {
        try {
            m_txtUserName.setText(m_stGemMemberInfo.name);
            GlobalData.imageLoader.displayImage(m_stGemMemberInfo.image, m_imgUserPhoto, options);
            m_txtBangBiAmount.setText(String.format("%.2f", m_stGemMemberInfo.bangbi));
            m_txtUnpaiedOrdersCount.setText(String.format("%d", m_stGemMemberInfo.unpaid_cnt));
            m_txtNewsCount.setText(String.format("%d", m_stGemMemberInfo.unreadnews_cnt));
            m_txtCouponCount.setText(String.format("%d", m_stGemMemberInfo.coupon_cnt));
            m_txtFavoriteCount.setText(String.format("%d", m_stGemMemberInfo.favorite_cnt));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);    //To change body of overridden methods use File | Settings | File Templates.

		if (requestCode == REQUEST_PHOTO && resultCode == RESULT_OK)
		    updateUserImage(data);
	}

	/* Image mamagement methods */
	private void updateUserImage(Intent data)
	{
		if (data.getIntExtra(SelectPhotoActivity.szRetCode, -999) == SelectPhotoActivity.nRetSuccess)
		{
			Object objPath = data.getExtras().get(SelectPhotoActivity.szRetPath);
			Object objUri = data.getExtras().get(SelectPhotoActivity.szRetUri);

			String szPath = "";
			Uri fileUri = null;

			if (objPath != null)
				szPath = (String)objPath;

			if (objUri != null)
				fileUri = (Uri)objUri;

			if (szPath != null && !szPath.equals(""))
				updateUserImageWithPath(szPath);
			else if (fileUri != null)
				updateUserImageWithUri(fileUri);
		}
	}

	private void updateUserImageWithPath(String szPath)
	{
		showProgressDlg();

		try {
			/* Update user photo info view */
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			Bitmap bitmap = BitmapFactory.decodeFile(szPath, options);

			if (bitmap != null)
			{
				int nWidth = bitmap.getWidth(), nHeight = bitmap.getHeight();
				int nScaledWidth = 0, nScaledHeight = 0;
				if (nWidth > nHeight)
				{
					nScaledWidth = SelectPhotoActivity.IMAGE_WIDTH;
					nScaledHeight = nScaledWidth * nHeight / nWidth;
				}
				else
				{
					nScaledHeight = SelectPhotoActivity.IMAGE_HEIGHT;
					nScaledWidth = nScaledHeight * nWidth / nHeight;
				}

				Bitmap scaledBmp = Bitmap.createScaledBitmap(bitmap, nScaledWidth, nScaledHeight, false);
//				m_imgUserPhoto.setImageBitmap(scaledBmp);

				/* Update user photo info using service */
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				scaledBmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
				byte[] byteArray = stream.toByteArray();
				String imgEncoded = Base64.encodeToString(byteArray, Base64.NO_WRAP);

				CommMgr.commService.UpdateUserPhoto(GlobalData.token, imgEncoded, updatePhotoHandler);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		hideProgressDlg();
	}

	private void updateUserImageWithUri(Uri uri)
	{
		BufferedInputStream bis = null;
		InputStream is = null;
		Bitmap bmp = null, scaledBmp = null;
		URLConnection conn = null;

		showProgressDlg();

		try {
			/* Update user photo info view */
			String szUrl = uri.toString();

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;

			is = getContentResolver().openInputStream(uri);
			bmp = BitmapFactory.decodeStream(is, null, options);

			int nWidth = bmp.getWidth(), nHeight = bmp.getHeight();
			int nScaledWidth = 0, nScaledHeight = 0;
			if (nWidth > nHeight)
			{
				nScaledWidth = SelectPhotoActivity.IMAGE_WIDTH;
				nScaledHeight = nScaledWidth * nHeight / nWidth;
			}
			else
			{
				nScaledHeight = SelectPhotoActivity.IMAGE_HEIGHT;
				nScaledWidth = nScaledHeight * nWidth / nHeight;
			}

			scaledBmp = Bitmap.createScaledBitmap(bmp, nScaledWidth, nScaledHeight, false);

//			m_imgUserPhoto.setImageBitmap(scaledBmp);

			/* Update user photo info using service */
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			scaledBmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			byte[] byteArray = stream.toByteArray();
			String imgEncoded = Base64.encodeToString(byteArray, Base64.NO_WRAP);

			CommMgr.commService.UpdateUserPhoto(GlobalData.token, imgEncoded, updatePhotoHandler);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			hideProgressDlg();
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/************************************************************************************************/

	private AsyncHttpResponseHandler updatePhotoHandler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content)
		{
			super.onSuccess(content);
			String szMsg = CommMgr.commService.parseUpdatePhotoResult(content);
			hideProgressDlg();

			if (szMsg.equals(""))
			{
				GlobalData.showToast(HuiYuanZhongXinActivity.this, getResources().getString(R.string.MSG_Success));

                // get member basic info
                callGetMemberInfo();
			}
			else
			{
				GlobalData.showToast(HuiYuanZhongXinActivity.this, szMsg);
			}
		}

		@Override
		public void onFailure(Throwable error, String content)
		{
			super.onFailure(error, content);
			hideProgressDlg();
			GlobalData.showToast(HuiYuanZhongXinActivity.this, getResources().getString(R.string.server_connection_error));
		}
	};

	private JsonHttpResponseHandler bkstate_handler = new JsonHttpResponseHandler()
	{
		@Override
		public void onSuccess(JSONObject response)
		{
			hideProgressDlg();
			super.onSuccess(response);    //To change body of overridden methods use File | Settings | File Templates.

			final STBangKeState bkInfo = CommMgr.commService.parseBangKeStateResult(response);
			if (bkInfo != null)
			{
				boolean bNeedSubmit = false;
				switch (bkInfo.state_id)
				{
					case 0:				// Now considering
						bNeedSubmit = false;
						break;
					case 1:				// Failed to consider. It is able to submit again
						bNeedSubmit = true;
						break;
					case 2:				// Succeeded to consider
						bNeedSubmit = false;
						break;
					case 3:				// Cancelled right
						bNeedSubmit = false;
						break;
					case 4:				// Considering stopped temporarily. It is able to submit again
						bNeedSubmit = true;
						break;
					case 5:				// Right temporarily stopped. It is able to submit again
						bNeedSubmit = true;
						break;
					case -1:
						bNeedSubmit = true;
						break;
					default:
						GlobalData.showToast(HuiYuanZhongXinActivity.this, bkInfo.retMsg);
						return;
				}

				if (bNeedSubmit)
				{
					Intent intent = new Intent(HuiYuanZhongXinActivity.this, BangKeActivity.class);
					startActivity(intent);
				}
				else
				{
                    if (bkInfo.state_id == 0 || bkInfo.state_id == 2)
                    {
                        gotoBangKeActivity(bkInfo);
                    }
                    else
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(HuiYuanZhongXinActivity.this);
                        builder.setMessage(getResources().getString(R.string.app_name));
                        builder.setMessage(getResources().getString(R.string.ShenQingFailed));
                        builder.setPositiveButton(getResources().getString(R.string.button_ok), new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                gotoBangKeActivity(bkInfo);
                            }
                        });
                        builder.show();
                    }
				}
			}
			else
			{
				GlobalData.showToast(HuiYuanZhongXinActivity.this, getResources().getString(R.string.server_connection_error));
			}
		}

		@Override
		public void onFailure(Throwable error, String content)
		{
			super.onFailure(error, content);    //To change body of overridden methods use File | Settings | File Templates.
			hideProgressDlg();
			GlobalData.showToast(HuiYuanZhongXinActivity.this, getResources().getString(R.string.server_connection_error));
		}
	};

    private void gotoBangKeActivity(STBangKeState bkInfo)
    {
        Intent intent = new Intent(HuiYuanZhongXinActivity.this, BangKeZhongXinActivity.class);

        intent.putExtra("StateID", bkInfo.state_id);
        intent.putExtra("BangBi", bkInfo.bangbi);
        intent.putExtra("IdNo", bkInfo.id_no);
        intent.putExtra("BankCard", bkInfo.card_no);
        intent.putExtra("Bank", bkInfo.bank);

        startActivity(intent);
    }

	private void showPhotoSelectList()
	{
		Intent intent = new Intent(HuiYuanZhongXinActivity.this, SelectPhotoActivity.class);
		HuiYuanZhongXinActivity.this.startActivityForResult(intent, REQUEST_PHOTO);
	}

    private void goToDingDanZhuangTaiChaXun () {
        Intent intent = new Intent(HuiYuanZhongXinActivity.this, DingDanZhuangTaiChaXunActivity.class);
        startActivity(intent);
    }

    private void goToDingDan () {
        Intent intent = new Intent(HuiYuanZhongXinActivity.this, DingDanActivity.class);
        startActivity(intent);
    }

    private void goToXiaoXi () {
        Intent intent = new Intent(HuiYuanZhongXinActivity.this, XiaoXiActivity.class);
        startActivity(intent);
    }

    private void goToBangKe ()
	{
		showProgressDlg();
		CommMgr.commService.GetBangKeState(GlobalData.token, bkstate_handler);
    }

    private void goToYouHuiQuan () {
        Intent intent = new Intent(HuiYuanZhongXinActivity.this, YouHuiQuanActivity.class);
        startActivity(intent);
    }

    private void goToShouCang () {
        Intent intent = new Intent(HuiYuanZhongXinActivity.this, ShouCangActivity.class);
        startActivity(intent);
    }

    private void goToDiZhiGuanLi () {
        Intent intent = new Intent(HuiYuanZhongXinActivity.this, DiZhiGuanLiActivity.class);
        startActivity(intent);
    }

    private void goToXiuGaiMiMa () {
        Intent intent = new Intent(HuiYuanZhongXinActivity.this, XiuGaiMiMaActivity.class);
        startActivity(intent);
    }

    private void goToLiPinKaList () {
        Intent intent = new Intent(HuiYuanZhongXinActivity.this, LiPinKa_LieBiaoActivity.class);
        startActivity(intent);
    }

    private void goToPingLun () {
        Intent intent = new Intent(HuiYuanZhongXinActivity.this, PingLunActivity.class);
        startActivity(intent);
    }

    private void goToJianYi () {
        Intent intent = new Intent(HuiYuanZhongXinActivity.this, JianYiActivity.class);
        startActivity(intent);
    }

    private void logout()
    {
        Intent intent = new Intent(HuiYuanZhongXinActivity.this, PersonInfoActivity.class);
        startActivity(intent);
    }

    /**
     * call get member info service
     */
    private void callGetMemberInfo () {
        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;
            String retMsg = "";

            @Override
            public void onSuccess(JSONObject object)
            {
                retMsg = CommMgr.commService.parseMemberBasicValue(object, m_stGemMemberInfo);
                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    result = STServiceData.ERR_SUCCESS;

                    updateUI();
                    return;
                }
                else
                {
                    result = STServiceData.ERR_EXCEPTION;
                }

                //GlobalData.showToast(HuiYuanZhongXinActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                progDialog.dismiss();

                if (result == STServiceData.ERR_FAIL)
                {
                    GlobalData.showToast(HuiYuanZhongXinActivity.this, getString(R.string.server_connection_error));
                }
                else if (result != STServiceData.ERR_SUCCESS)
                {
                    GlobalData.showToast(HuiYuanZhongXinActivity.this, retMsg);
                }

                result = 0;
            }

        };

        CommMgr.commService.GetMemberBasicValues( GlobalData.token, handler);
    }

    // removed by Kimoc
//    private void callGetMemberInfo () {
//        handler = new JsonHttpResponseHandler()
//        {
//            int result = STServiceData.ERR_FAIL;
//            String retMsg = "";
//
//            @Override
//            public void onSuccess(JSONObject object)
//            {
//                retMsg = CommMgr.commService.parseGetMemberInfo(object, m_stGemMemberInfo);
//                if (retMsg.equals(STServiceData.MSG_SUCCESS))
//                {
//                    result = STServiceData.ERR_SUCCESS;
//
//                    updateUI();
//                    callGetBangbiValue();
//                    return;
//                }
//                else
//                {
//                    result = STServiceData.ERR_EXCEPTION;
//                }
//
//                //GlobalData.showToast(HuiYuanZhongXinActivity.this, retMsg);
//            }
//
//            @Override
//            public void onFailure(Throwable ex, String exception) {}
//
//            @Override
//            public void onFinish()
//            {
//                if (result == STServiceData.ERR_FAIL)
//                {
//                    GlobalData.showToast(HuiYuanZhongXinActivity.this, getString(R.string.server_connection_error));
//                    progDialog.dismiss();
//                    updateUI ();
//                }
//                else if (result != STServiceData.ERR_SUCCESS)
//                {
//                    GlobalData.showToast(HuiYuanZhongXinActivity.this, retMsg);
//                    callGetBangbiValue();
//                }
//
//                result = 0;
//            }
//
//        };
//
//        CommMgr.commService.RequestGetMemberInfo( GlobalData.token, handler );
//    }

//    private void callGetBangbiValue () {
//        handler = new JsonHttpResponseHandler()
//        {
//            int result = STServiceData.ERR_FAIL;
//            String retMsg = "";
//
//            @Override
//            public void onSuccess(JSONObject object)
//            {
//                retMsg = CommMgr.commService.parseGetBangbiValue(object, m_fBangBi);
//                if (retMsg.equals(STServiceData.MSG_SUCCESS))
//                {
//                    result = STServiceData.ERR_SUCCESS;
//
//                    updateUI();
//                    callGetUnpaiedCount();
//                    return;
//                }
//                else
//                {
//                    result = STServiceData.ERR_EXCEPTION;
//                }
//
//                //GlobalData.showToast(HuiYuanZhongXinActivity.this, retMsg);
//            }
//
//            @Override
//            public void onFailure(Throwable ex, String exception) {}
//
//            @Override
//            public void onFinish()
//            {
//                if (result == STServiceData.ERR_FAIL)
//                {
//                    GlobalData.showToast(HuiYuanZhongXinActivity.this, getString(R.string.server_connection_error));
//                    progDialog.dismiss();
//                    updateUI();
//                }
//                else if (result != STServiceData.ERR_SUCCESS)
//                {
//                    GlobalData.showToast(HuiYuanZhongXinActivity.this, retMsg);
//                    callGetUnpaiedCount();
//                }
//
//                result = 0;
//            }
//
//        };
//
//        CommMgr.commService.GetBangbiValue ( GlobalData.token, handler );
//    }
//
//    private void callGetUnpaiedCount () {
//        handler = new JsonHttpResponseHandler()
//        {
//            int result = STServiceData.ERR_FAIL;
//            String retMsg = "";
//
//            @Override
//            public void onSuccess(JSONObject object)
//            {
//                retMsg = CommMgr.commService.parseGetUnpaiedCount(object, m_nOrdersUnpaied);
//                if (retMsg.equals(STServiceData.MSG_SUCCESS))
//                {
//                    result = STServiceData.ERR_SUCCESS;
//
//                    updateUI();
//                    callGetUnreadNewsCount();
//                    return;
//                }
//                else
//                {
//                    result = STServiceData.ERR_EXCEPTION;
//                }
//
//                //GlobalData.showToast(HuiYuanZhongXinActivity.this, retMsg);
//            }
//
//            @Override
//            public void onFailure(Throwable ex, String exception) {}
//
//            @Override
//            public void onFinish()
//            {
//                if (result == STServiceData.ERR_FAIL)
//                {
//                    GlobalData.showToast(HuiYuanZhongXinActivity.this, getString(R.string.server_connection_error));
//                    progDialog.dismiss();
//                    updateUI();
//                }
//                else if (result != STServiceData.ERR_SUCCESS)
//                {
//                    GlobalData.showToast(HuiYuanZhongXinActivity.this, retMsg);
//                    callGetUnreadNewsCount();
//                }
//
//                result = 0;
//            }
//
//        };
//
//        CommMgr.commService.GetUnpaiedCount(GlobalData.token, handler);
//    }
//
//    private void callGetUnreadNewsCount () {
//        handler = new JsonHttpResponseHandler()
//        {
//            int result = STServiceData.ERR_FAIL;
//            String retMsg = "";
//
//            @Override
//            public void onSuccess(JSONObject object)
//            {
//
//                retMsg = CommMgr.commService.parseGetUnreadNewsCount(object, m_nNewsUnread);
//                if (retMsg.equals(STServiceData.MSG_SUCCESS))
//                {
//                    result = STServiceData.ERR_SUCCESS;
//
//                    updateUI();
//                    callGetCouponCount();
//                    return;
//                }
//                else
//                {
//                    result = STServiceData.ERR_EXCEPTION;
//                }
//
//                //GlobalData.showToast(HuiYuanZhongXinActivity.this, retMsg);
//            }
//
//            @Override
//            public void onFailure(Throwable ex, String exception) {}
//
//            @Override
//            public void onFinish()
//            {
//                if (result == STServiceData.ERR_FAIL)
//                {
//                    GlobalData.showToast(HuiYuanZhongXinActivity.this, getString(R.string.server_connection_error));
//                    progDialog.dismiss();
//                    updateUI();
//                }
//                else if (result != STServiceData.ERR_SUCCESS)
//                {
//                    GlobalData.showToast(HuiYuanZhongXinActivity.this, retMsg);
//                    callGetCouponCount();
//                }
//
//                result = 0;
//            }
//
//        };
//
//        CommMgr.commService.GetUnreadNewsCount(GlobalData.token, handler);
//    }
//
//    private void callGetCouponCount () {
//        handler = new JsonHttpResponseHandler()
//        {
//            int result = STServiceData.ERR_FAIL;
//            String retMsg = "";
//
//            @Override
//            public void onSuccess(JSONObject object)
//            {
//
//                retMsg = CommMgr.commService.parseGetCouponCount(object, m_nCouponCount);
//                if (retMsg.equals(STServiceData.MSG_SUCCESS))
//                {
//                    result = STServiceData.ERR_SUCCESS;
//
//                    updateUI();
//                    callGetFavoriteCount();
//                    return;
//                }
//                else
//                {
//                    result = STServiceData.ERR_EXCEPTION;
//                }
//
//                //GlobalData.showToast(HuiYuanZhongXinActivity.this, retMsg);
//            }
//
//            @Override
//            public void onFailure(Throwable ex, String exception) {}
//
//            @Override
//            public void onFinish()
//            {
//                if (result == STServiceData.ERR_FAIL)
//                {
//                    GlobalData.showToast(HuiYuanZhongXinActivity.this, getString(R.string.server_connection_error));
//                    progDialog.dismiss();
//                    updateUI();
//                }
//                else if (result != STServiceData.ERR_SUCCESS)
//                {
//                    GlobalData.showToast(HuiYuanZhongXinActivity.this, retMsg);
//                    callGetFavoriteCount();
//                }
//
//                result = 0;
//            }
//
//        };
//
//        CommMgr.commService.GetCouponCount(GlobalData.token, handler);
//    }
//
//    private void callGetFavoriteCount() {
//        handler = new JsonHttpResponseHandler()
//        {
//            int result = STServiceData.ERR_FAIL;
//            String retMsg = "";
//
//            @Override
//            public void onSuccess(JSONObject object)
//            {
//                progDialog.dismiss();
//                retMsg = CommMgr.commService.parseGetFavoriteCount(object, m_nFavoriteCount);
//                if (retMsg.equals(STServiceData.MSG_SUCCESS))
//                {
//                    result = STServiceData.ERR_SUCCESS;
//
//                    updateUI();
////                    updateUI ();
//                    return;
//                }
//                else
//                {
//                    result = STServiceData.ERR_EXCEPTION;
//                }
//
//                //GlobalData.showToast(HuiYuanZhongXinActivity.this, retMsg);
//            }
//
//            @Override
//            public void onFailure(Throwable ex, String exception) {}
//
//            @Override
//            public void onFinish()
//            {
//                progDialog.dismiss();
//                if (result == STServiceData.ERR_FAIL)
//                {
//                    GlobalData.showToast(HuiYuanZhongXinActivity.this, getString(R.string.server_connection_error));
//                    updateUI();
//                }
//                else if (result != STServiceData.ERR_SUCCESS)
//                {
//                    GlobalData.showToast(HuiYuanZhongXinActivity.this, retMsg);
//                    updateUI ();
//                }
//
//                result = 0;
//            }
//
//        };
//
//        CommMgr.commService.GetFavoriteCount(GlobalData.token, handler);
//    }
}
