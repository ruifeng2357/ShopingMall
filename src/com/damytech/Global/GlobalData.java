package com.damytech.Global;

import java.io.*;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;
import com.damytech.STData.STUserInfo;
import com.damytech.yilebang.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class GlobalData
{
    public static ImageLoader imageLoader = ImageLoader.getInstance();

    public static STUserInfo g_UserInfo = new STUserInfo();
    // Test value
    public static String    token = "";
    public static int       cartProdCount = 0;

    public static String g_strTokenFileName="YLBToken.log";
    public static String g_strMessageIDFileName = "YLBMessageID.log";

    private static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
	          "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
	          "\\@" +
	          "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
	          "(" +
	          "\\." +
	          "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
	          ")+"
	      );

    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^[+]?[0-9]{10,13}$");
	
	private static Toast g_Toast = null;
	public static void showToast(Context context, String toastStr)
	{
		if ((g_Toast == null) || (g_Toast.getView().getWindowVisibility() != View.VISIBLE))
		{
			g_Toast = Toast.makeText(context, toastStr, Toast.LENGTH_SHORT);
			g_Toast.show();
		}

		return;
	}
		
	public static boolean isValidEmail(String strEmail)
	{
		return EMAIL_ADDRESS_PATTERN.matcher(strEmail).matches();
	}

    public static boolean isValidPhone(String strPhone)
    {
        return PHONE_NUMBER_PATTERN.matcher(strPhone).matches();
    }

    public static boolean isValidName(String strName)
    {
        if ((strName.length() >= 2) && (strName.length() <= 25))
            return true;

        return false;
    }

	public static String BkStateID2String(Context context, int nStateID)
	{
		String szText = "";

		switch (nStateID)
		{
			case 0:
				szText = context.getResources().getString(R.string.HuiYuanZhongXin_BKState0);
				break;
			case 1:
				szText = context.getResources().getString(R.string.HuiYuanZhongXin_BKState1);
				break;
			case 2:
				szText = context.getResources().getString(R.string.HuiYuanZhongXin_BKState2);
				break;
			case 3:
				szText = context.getResources().getString(R.string.HuiYuanZhongXin_BKState3);
				break;
			case 4:
				szText = context.getResources().getString(R.string.HuiYuanZhongXin_BKState4);
				break;
			case 5:
				szText = context.getResources().getString(R.string.HuiYuanZhongXin_BKState5);
				break;
			default:
				szText = "";
				break;
		}

		return szText;
	}

    public static final String md5(final String s)
    {
        try
        {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
            {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }

            return hexString.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        return "";

    }


    /**
     * Check online connectivity
     * @param ctx [in], current context
     * @return true : has connectivity
     * 			false : not have
     */
    public static boolean isOnline(Context ctx)
    {
        ConnectivityManager cm =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public static void fileWriteToken(Context context, String strData)
    {
        String strFilePath = "";
        strFilePath = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/" + GlobalData.g_strTokenFileName;

        File file = new File(strFilePath);
        if (file.exists() == false)
        {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            FileOutputStream output = new FileOutputStream(file);
            byte[] outByte = strData.getBytes();

            output.write(outByte);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}