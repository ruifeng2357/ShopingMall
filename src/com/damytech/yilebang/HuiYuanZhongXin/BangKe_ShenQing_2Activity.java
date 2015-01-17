package com.damytech.yilebang.HuiYuanZhongXin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.damytech.CommService.CommMgr;
import com.damytech.Global.GlobalData;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.STData.STDouble;
import com.damytech.Utils.ResolutionSet;
import com.damytech.yilebang.MyActivity;
import com.damytech.yilebang.R;
import org.json.JSONObject;

import java.io.*;
import java.net.URLConnection;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-22
 * Time: 下午7:25
 * To change this template use File | Settings | File Templates.
 */
public class BangKe_ShenQing_2Activity extends MyActivity {
	private int REQUEST_PHOTO = 0;

    // UI control variables
    RelativeLayout  m_rlMain;
    RelativeLayout  m_rlHeader;
    ImageButton m_imgbtnRefresh;
    RelativeLayout m_rlBody;
    TextView  m_txtStep1No;
    TextView  m_txtStep1Name;
    TextView  m_txtStep2No;
    TextView  m_txtStep2Name;
    TextView  m_txtStep3No;
    TextView  m_txtStep3Name;
    TextView m_txtUploadIDPhoto;
    ImageView m_imgIDPhoto;
    RelativeLayout  m_rlFooter;
    ImageButton  m_imgbtnBack;
    ImageButton  m_imgbtnAccount;
    TextView  m_txtSubmit;
	ImageButton m_btnSelPhoto;
	ImageButton m_btnNext;

	Bitmap bmpPhoto = null;

	String szName = "", szIDNo = "", szBankCard = "", szBankName = "", szDesc = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.huiyuanzhongxin_bangke_shenqing_2);

        ResolutionSet._instance.iterateChild(findViewById(R.id.rlMain));

        getControlVariables ();

        connectSignalHandlers();

        loadInitialData();

		loadExtras();
    }

    private void getControlVariables () {
        m_rlMain = (RelativeLayout) findViewById(R.id.rlMain);
        m_rlHeader = (RelativeLayout) findViewById(R.id.rlHeader);
        m_imgbtnRefresh = (ImageButton) findViewById(R.id.imgbtnRefresh);
        m_rlBody = (RelativeLayout) findViewById(R.id.rlBody);
        m_txtStep1No = (TextView) findViewById(R.id.txtStep1No);
        m_txtStep1Name = (TextView) findViewById(R.id.txtStep1Name);
        m_txtStep2No = (TextView) findViewById(R.id.txtStep2No);
        m_txtStep2Name = (TextView) findViewById(R.id.txtStep2Name);
        m_txtStep3No = (TextView) findViewById(R.id.txtStep3No);
        m_txtStep3Name = (TextView) findViewById(R.id.txtStep3Name);
        m_txtUploadIDPhoto = (TextView) findViewById(R.id.txtUploadIDPhoto);
        m_imgIDPhoto = (ImageView) findViewById(R.id.imgIDPhoto);
        m_rlFooter = (RelativeLayout) findViewById(R.id.rlFooter);
        m_imgbtnBack = (ImageButton) findViewById(R.id.imgbtnBack);
        m_imgbtnAccount = (ImageButton) findViewById(R.id.imgbtnAccount);
        m_txtSubmit = (TextView) findViewById(R.id.txtSubmit);

		m_btnSelPhoto = (ImageButton)findViewById(R.id.btnSelPhoto);
		m_btnNext = (ImageButton)findViewById(R.id.btnNext);
    }

    private void connectSignalHandlers ()
	{
		m_btnSelPhoto.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onClickSelPhoto();
			}
		});

		m_btnNext.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				onClickNext();
			}
		});

		m_imgbtnBack.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(getApplicationContext(), HuiYuanZhongXinActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
    }

    private void loadInitialData () {

    }

	private void loadExtras()
	{
		szName = getIntent().getStringExtra("Name");
		szIDNo = getIntent().getStringExtra("IDNO");
		szBankCard = getIntent().getStringExtra("BankCard");
		szBankName = getIntent().getStringExtra("BankName");
		szDesc = getIntent().getStringExtra("Desc");
	}

	private void onClickSelPhoto()
	{
		Intent intent = new Intent(BangKe_ShenQing_2Activity.this, SelectPhotoActivity.class);
		startActivityForResult(intent, REQUEST_PHOTO);
	}

	private void onClickNext()
	{
		if (bmpPhoto == null)
		{
			GlobalData.showToast(BangKe_ShenQing_2Activity.this, getResources().getString(R.string.HuiYuanZhongXin_IDPhoto_CanNotEmpy));
			return;
		}

		showProgressDlg();

		ByteArrayOutputStream oStream = new ByteArrayOutputStream();
		bmpPhoto.compress(Bitmap.CompressFormat.JPEG, 100, oStream);
		byte[] bmpData = oStream.toByteArray();
		String bmpPhoto = Base64.encodeToString(bmpData, Base64.NO_WRAP);

		CommMgr.commService.UpgradeToBangKe(szName, szIDNo, szBankCard, szBankName, szDesc, bmpPhoto, GlobalData.token, upgrade_handler);
	}

	private AsyncHttpResponseHandler upgrade_handler = new AsyncHttpResponseHandler()
	{
		@Override
		public void onSuccess(String content)
		{
			hideProgressDlg();

			JSONObject jsonObj = null;

			try
			{
				jsonObj = new JSONObject(content);
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				GlobalData.showToast(BangKe_ShenQing_2Activity.this, getResources().getString(R.string.msg_encode_contents_failed));
				return;
			}

			super.onSuccess(content);    //To change body of overridden methods use File | Settings | File Templates.

			STDouble bangbi = new STDouble();
			String retMsg = CommMgr.commService.parseUpgradeBangKeResult(jsonObj, bangbi);
			if (retMsg.equals(""))
			{
				double fBangBi = bangbi.fVal;

				Intent intent = new Intent(BangKe_ShenQing_2Activity.this, BangKeZhongXinActivity.class);

				intent.putExtra("StateID", 0);
				intent.putExtra("BangBi", fBangBi);
				intent.putExtra("IdNo", szIDNo);
				intent.putExtra("BankCard", szBankCard);
				intent.putExtra("Bank", szBankName);

				startActivity(intent);
			}
			else
			{
				GlobalData.showToast(BangKe_ShenQing_2Activity.this, retMsg);
			}
		}

		@Override
		public void onFailure(Throwable e, String content)
		{
			hideProgressDlg();
			super.onFailure(e, content);    //To change body of overridden methods use File | Settings | File Templates.
			GlobalData.showToast(BangKe_ShenQing_2Activity.this, getResources().getString(R.string.server_connection_error));
		}
	};

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
			Bitmap bmp = null;
			Uri fileUri = null;

			if (objPath != null)
				szPath = (String)objPath;

			if (objUri != null)
				fileUri = (Uri)objUri;

			if (szPath != null && !szPath.equals(""))
				savePhotoPath(szPath);
			else if (fileUri != null)
				updateUserImageWithUri(fileUri);
		}
	}

	private void savePhotoPath(String szPath)
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

				Bitmap scaled = Bitmap.createScaledBitmap(bitmap, nScaledWidth, nScaledHeight, false);
				bmpPhoto = scaled;
				m_imgIDPhoto.setImageBitmap(bmpPhoto);
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
		Bitmap bmp = null;
		URLConnection conn = null;

		showProgressDlg();

		try
		{
			/* Update user photo info view */
			String szUrl = uri.toString();

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;

			is = getContentResolver().openInputStream(uri);
			bmp = BitmapFactory.decodeStream(is, null, options);
			if (bmp != null)
			{
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

				Bitmap scaled = Bitmap.createScaledBitmap(bmp, nScaledWidth, nScaledHeight, false);
				bmpPhoto = scaled;
				m_imgIDPhoto.setImageBitmap(bmpPhoto);
			}
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

	private void showProgressDlg()
	{
		progDialog = ProgressDialog.show(
				BangKe_ShenQing_2Activity.this,
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

	/************************************************************************************************/


}

