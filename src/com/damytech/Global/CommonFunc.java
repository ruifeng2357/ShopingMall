package com.damytech.Global;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.ShareAllGird;
import com.damytech.CommService.CommMgr;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.STData.STInteger;
import com.damytech.STData.STServiceData;
import com.damytech.Utils.BadgeView;
import com.damytech.yilebang.ArticlesBasketActivity;
import com.damytech.yilebang.HuiYuanZhongXin.HuiYuanZhongXinActivity;
import com.damytech.yilebang.PersonInfoActivity;
import com.damytech.yilebang.R;
import com.damytech.yilebang.onekeyshare.OnekeyShare;
import com.damytech.yilebang.onekeyshare.ShareContentCustomizeDemo;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: Yunsungil
 * Date: 11/27/13
 * Time: 10:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class CommonFunc {

    public static String g_baseUrl = "http://yilebang.com/proInfo.aspx?";
    public static String g_sUserPref = "UserInfo";

    public static int g_MaxCount = 99;

    /**
     * Go to shopping cart activity
     * @param context [in], context param
     */
    public static void GoToShopCart(Context context)
    {
        if (GlobalData.token.length() <= 0)
        {
            Intent intent = new Intent(context, PersonInfoActivity.class);
            context.startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(context, ArticlesBasketActivity.class);
            context.startActivity(intent);
        }
    }

    /**
     * go to account activity
     * @param context [in], current context
     */
    public static void GoToAccount(Context context)
    {
        if (GlobalData.token.length() <= 0)
        {
            Intent intent = new Intent(context, PersonInfoActivity.class);
            context.startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(context, HuiYuanZhongXinActivity.class);
            context.startActivity(intent);
        }
    }

    /**
     * Update products count of shopping cart
     * @param view [in/out], parent view of badge
     */
    public static void UpdateCartNum(BadgeView view)
    {
        if (GlobalData.cartProdCount > 0)
        {
            view.setText(Integer.toString(GlobalData.cartProdCount));
            view.show();
        }
        else
        {
            view.hide();
        }
    }

//    public static void showFenXiang(Context ctx, int prodId, String imgUrl)
//    {
//        try
//        {
//            if (GlobalData.token.length() <= 0)
//            {
//                Intent intent = new Intent(ctx, PersonInfoActivity.class);
//                ctx.startActivity(intent);
//                return;
//            }
//
//            // make fenxiang url
//            String url = g_baseUrl;
//            url += "id=" + prodId;
//            url += "&tg=zwk";
//            url += "&bkmd5=" + GlobalData.md5(Integer.toString(GlobalData.g_UserInfo.id));
//            url += "&bk=" + (GlobalData.g_UserInfo.id * 8527);
//
//            // start fenxiang
//            Intent i = new Intent(ctx, ShareAllGird.class);
//            i.putExtra("notif_icon", R.drawable.ic_launcher);
//            i.putExtra("notif_title", ctx.getString(R.string.app_name));
//
////        i.putExtra("title", ctx.getString(R.string.share));
//            i.putExtra("title", url);
//            i.putExtra("titleUrl", url);
//            i.putExtra("text", url);
//            i.putExtra("imagePath", imgUrl);
//            i.putExtra("url", url);
//            i.putExtra("thumbPath", imgUrl);
//            i.putExtra("appPath", imgUrl);
//            i.putExtra("comment", "");
//            i.putExtra("site", ctx.getString(R.string.app_name));
//            i.putExtra("siteUrl", url);
//
//            i.putExtra("silent", true);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            ctx.startActivity(i);
//        }
//        catch (Exception ex)
//        {
//            ex.printStackTrace();
//        }
//
//    }


    // 使用快捷分享完成分享（请务必仔细阅读位于SDK解压目录下Docs文件夹中OnekeyShare类的JavaDoc）
    /**ShareSDK集成方法有两种</br>
     * 1、第一种是引用方式，例如引用onekeyshare项目，onekeyshare项目再引用mainlibs库</br>
     * 2、第二种是把onekeyshare和mainlibs集成到项目中，本例子就是用第二种方式</br>
     * 请看“ShareSDK 使用说明文档”，SDK下载目录中 </br>
     * 或者看网络集成文档 http://wiki.sharesdk.cn/Android_%E5%BF%AB%E9%80%9F%E9%9B%86%E6%88%90%E6%8C%87%E5%8D%97
     * 3、混淆时，把sample或者本例子的混淆代码copy过去，在proguard-project.txt文件中
     *
     *
     * 平台配置信息有三种方式：
     * 1、在我们后台配置各个微博平台的key
     * 2、在代码中配置各个微博平台的key，http://sharesdk.cn/androidDoc/cn/sharesdk/framework/ShareSDK.html
     * 3、在配置文件中配置，本例子里面的assets/ShareSDK.conf,
     */
    public static void showFenXiang(Context ctx, int prodId, String imgUrl) {

        boolean silent = false;
        String platform = null;

        try
        {
            if (GlobalData.token.length() <= 0)
            {
                Intent intent = new Intent(ctx, PersonInfoActivity.class);
                ctx.startActivity(intent);
                return;
            }

            // make fenxiang url
            String url = g_baseUrl;
            url += "id=" + prodId;
            url += "&tg=zwk";
            url += "&bkmd5=" + GlobalData.md5(Integer.toString(GlobalData.g_UserInfo.id));
            url += "&bk=" + (GlobalData.g_UserInfo.id * 8527);

//            // start fenxiang
//            Intent i = new Intent(ctx, ShareAllGird.class);
//            i.putExtra("notif_icon", R.drawable.ic_launcher);
//            i.putExtra("notif_title", ctx.getString(R.string.app_name));
//
////        i.putExtra("title", ctx.getString(R.string.share));
//            i.putExtra("title", url);
//            i.putExtra("titleUrl", url);
//            i.putExtra("text", url);
//            i.putExtra("imagePath", imgUrl);
//            i.putExtra("url", url);
//            i.putExtra("thumbPath", imgUrl);
//            i.putExtra("appPath", imgUrl);
//            i.putExtra("comment", "");
//            i.putExtra("site", ctx.getString(R.string.app_name));
//            i.putExtra("siteUrl", url);

            ShareSDK.initSDK(ctx);

            final OnekeyShare oks = new OnekeyShare();
            oks.setNotification(R.drawable.ic_launcher, ctx.getString(R.string.app_name));
            oks.setAddress("12345678901");
            oks.setTitle(ctx.getString(R.string.app_name));
            oks.setTitleUrl(url);
            oks.setText(ctx.getString(R.string.FenXing_Content));
//        oks.setImagePath(MainActivity.TEST_IMAGE);
            oks.setImageUrl(imgUrl);
            oks.setUrl(url);
//        oks.setFilePath(MainActivity.TEST_IMAGE);
            oks.setComment("comment share");
            oks.setSite(ctx.getString(R.string.app_name));
            oks.setSiteUrl(url);
            oks.setVenueName("ShareSDK");
            oks.setVenueDescription("This is a beautiful place!");
//        oks.setLatitude(23.056081f);
//        oks.setLongitude(113.385708f);
            oks.setSilent(silent);
            if (platform != null) {
                oks.setPlatform(platform);
            }

            // 去除注释，可令编辑页面显示为Dialog模式
//		oks.setDialogMode();

            // 去除注释，在自动授权时可以禁用SSO方式
//		oks.disableSSOWhenAuthorize();

            // 去除注释，则快捷分享的操作结果将通过OneKeyShareCallback回调
//		oks.setCallback(new OneKeyShareCallback());
            oks.setShareContentCustomizeCallback(new ShareContentCustomizeDemo());

            // 去除注释，演示在九宫格设置自定义的图标
//		Bitmap logo = BitmapFactory.decodeResource(menu.getResources(), R.drawable.ic_launcher);
//		String label = menu.getResources().getString(R.string.app_name);
//		OnClickListener listener = new OnClickListener() {
//			public void onClick(View v) {
//				String text = "Customer Logo -- ShareSDK " + ShareSDK.getSDKVersionName();
//				Toast.makeText(menu.getContext(), text, Toast.LENGTH_SHORT).show();
//				oks.finish();
//			}
//		};
//		oks.setCustomerLogo(logo, label, listener);

            oks.show(ctx);

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    public static void Copy2Clipboard(Context ctx, int prodId, String imgUrl) {

        boolean silent = true;
        String platform = null;

        try
        {
            if (GlobalData.token.length() <= 0)
            {
                Intent intent = new Intent(ctx, PersonInfoActivity.class);
                ctx.startActivity(intent);
                return;
            }

            // make fenxiang url
            String url = g_baseUrl;
            url += "id=" + prodId;
            url += "&tg=zwk";
            url += "&bkmd5=" + GlobalData.md5(Integer.toString(GlobalData.g_UserInfo.id));
            url += "&bk=" + (GlobalData.g_UserInfo.id * 8527);

            if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(url);
            } else {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", url);
                clipboard.setPrimaryClip(clip);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    public static void SaveAccount(Context ctx, String name, String pwd)
    {
        try
        {
            SharedPreferences savePref = ctx.getSharedPreferences(g_sUserPref, ctx.MODE_PRIVATE);
            SharedPreferences.Editor editor = savePref.edit();
            editor.putString("Name", name);
            editor.putString("Password", pwd);
            editor.commit();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static String LoadAccountName(Context ctx)
    {
        String name = "";

        try
        {
            SharedPreferences savePref = ctx.getSharedPreferences(g_sUserPref, ctx.MODE_PRIVATE);
            name = savePref.getString("Name", "");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return  name;
    }

    public static String LoadAccountPwd(Context ctx)
    {
        String password = "";

        try
        {
            SharedPreferences savePref = ctx.getSharedPreferences(g_sUserPref, ctx.MODE_PRIVATE);
            password = savePref.getString("Password", "");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return  password;
    }

}
