package com.damytech.yilebang;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.STData.STInteger;
import com.damytech.STData.STNewsInfo;
import com.damytech.STData.STServiceData;
import com.damytech.yilebang.HuiYuanZhongXin.XiaoXiActivity;
import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class NewsService extends IntentService {

    public final int ONE_SECOND = 1000;
    public final int MINUT_SECONDS = 60;
    public final int HOUR_MINUTS = 60;

    public static String g_strTokenFileName="YLBToken.log";
    public static String g_strMessageIDFileName = "YLBMessageID.log";

	public static Context m_ctxMain = null;
	private STNewsInfo stNewsInfo = new STNewsInfo();
    private STInteger stCount = new STInteger();

    private JsonHttpResponseHandler handler = null;

	public NewsService() {
		super("");
	}

	public NewsService(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
        while (true)
        {
            try
            {
                chkMsg(m_ctxMain);
                Thread.sleep(ONE_SECOND * MINUT_SECONDS);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
	}

	private void chkMsg(Context ctx)
	{
		m_ctxMain = ctx;
		RunBackGround();
	}

	private void RunBackGround()
    {
        try {
            String strToken = "";
            strToken = fileReadToken(m_ctxMain);
            if (strToken == null || strToken.length() < 1)
                return;
            URL url = new URL(STServiceData.serviceAddr +  STServiceData.cmdGetLatestNews +  "?token=" + strToken);
            URLConnection connection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection)connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK)
            {
                BufferedReader br = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line+"\n");
                }
                br.close();

                JSONObject mainObject = new JSONObject(sb.toString());
                JSONObject uniObject = mainObject.getJSONObject("SVCC_DATA");
                stCount.nVal = uniObject.getInt("Count");
                if ( stCount.nVal > 0 )
                {
                    JSONObject realJson = uniObject.getJSONObject("News");
                    stNewsInfo.uid = realJson.getInt("Uid");
                    stNewsInfo.title = realJson.getString("Title");
                    stNewsInfo.addTime = realJson.getString("AddTime");
                    stNewsInfo.contents = realJson.getString("Contents");

                    String strOldMessageID = fileReadMessageID(m_ctxMain);
                    if (strOldMessageID != null && strOldMessageID.length() == 0)
                    {
                        showMessage(stNewsInfo, stCount);
                        fileWriteMessageID(m_ctxMain, Integer.toString(stNewsInfo.uid));
                        return;
                    }

                    if (strOldMessageID != null && strOldMessageID.length() > 0)
                    {
                        try {
                            int nVal = Integer.parseInt(strOldMessageID);
                            if (nVal != stNewsInfo.uid)
                            {
                                showMessage(stNewsInfo, stCount);
                                fileWriteMessageID(m_ctxMain, Integer.toString(stNewsInfo.uid));
                            }
                        } catch(Exception e) {
                            return;
                        }
                    }
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            fileWriteMessageID(m_ctxMain, "");
        }
  	}

	private void showMessage(STNewsInfo item, STInteger count)
	{
		generateNotification(m_ctxMain, item, count);
	}

	private void generateNotification(Context context, STNewsInfo newsInfo, STInteger count)
	{
        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = new Notification(icon, newsInfo.contents, when);

        Intent notificationIntent = new Intent(context, XiaoXiActivity.class);
        notificationIntent.putExtra("index", newsInfo.uid);

        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent intent = PendingIntent.getActivity(context, newsInfo.uid + 1,
        		notificationIntent,
        		PendingIntent.FLAG_CANCEL_CURRENT);

        notification.setLatestEventInfo(context, newsInfo.title + "(" + Integer.toString(count.nVal) + ")", "", intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(newsInfo.uid, notification);
    }

    public static String fileReadToken(Context context) throws IOException {
        String strFilePath = "";
        strFilePath = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/" + g_strTokenFileName;

        File file = new File(strFilePath);
        if (file.exists() == false)
            return "";

        FileInputStream input = new FileInputStream(file);
        InputStream is = new BufferedInputStream(input);

        try {
            InputStreamReader rdr = new InputStreamReader(is, "UTF-8");
            StringBuilder contents = new StringBuilder();
            char[] buff = new char[256];
            int len = rdr.read(buff);
            return String.valueOf(buff, 0, len);
        } finally {
            try {
                is.close();
            } catch (Exception e) {
                return "";
            }
        }
    }

    public static void fileWriteMessageID(Context context, String strData)
    {
        String strFilePath = "";
        strFilePath = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/" + g_strMessageIDFileName;

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

    public static String fileReadMessageID(Context context) throws IOException {
        String strFilePath = "";
        strFilePath = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/" + g_strMessageIDFileName;

        File file = new File(strFilePath);
        if (file.exists() == false)
            return "";

        FileInputStream input = new FileInputStream(file);
        InputStream is = new BufferedInputStream(input);

        try {
            InputStreamReader rdr = new InputStreamReader(is, "UTF-8");
            StringBuilder contents = new StringBuilder();
            char[] buff = new char[256];
            int len = rdr.read(buff);
            return String.valueOf(buff, 0, len);
        } finally {
            try {
                is.close();
            } catch (Exception e) {
                return "";
            }
        }
    }
}
