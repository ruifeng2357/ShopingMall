package com.damytech.yilebang;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.damytech.CommService.CommMgr;
import com.damytech.Global.CommonFunc;
import com.damytech.Global.GlobalData;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.STData.STServiceData;
import com.damytech.Utils.BadgeView;
import com.damytech.yilebang.HuiYuanZhongXin.HuiYuanZhongXinActivity;
import com.damytech.yilebang.HuiYuanZhongXin.LiPinKaActivity;
import com.damytech.yilebang.HuiYuanZhongXin.LiPinKa_LieBiaoActivity;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-25
 * Time: 下午5:31
 * To change this template use File | Settings | File Templates.
 */
public class MyActivity extends Activity {
    protected JsonHttpResponseHandler  handler;
    protected JsonHttpResponseHandler  handler1;
    protected JsonHttpResponseHandler  cartCntHandler;
    protected ProgressDialog  cartprogDialog;
    protected ProgressDialog  progDialog;
    protected BadgeView cartBadge = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartNum();
//        callGetCartProductsCount();
    }

    @Override
    public void onBackPressed() {
        onImgbtnBackClicked();
    }

    public void onImgbtnBackClicked () {
        finish();
    }

    public void setBadgeParent(View view)
    {
        cartBadge = new BadgeView(this, view);
        cartBadge.setBadgePosition(BadgeView.POSITION_TOP_CENTER);
        cartBadge.setBadgeMargin(-1);
    }

    public void onImgbtnHomeClicked () {
        Intent intent = new Intent(MyActivity.this, BaseServiceActivity.class);
        startActivity(intent);
    }

    public void onImgbtnMainMenuClicked () {
        Intent intent = new Intent(MyActivity.this, MainMenuActivity.class);
        startActivity(intent);
    }

    public void onImgbtnCartClicked () {
        CommonFunc.GoToShopCart(MyActivity.this);
    }

    public void onImgbtnAccountClicked () {
        CommonFunc.GoToAccount(MyActivity.this);
    }

    public void updateCartNum()
    {
        if (cartBadge != null)
            CommonFunc.UpdateCartNum(cartBadge);
    }

//    public void callGetCartProductsCount()
//    {
//        if (cartBadge == null)
//            return;
//
//        cartCntHandler = new JsonHttpResponseHandler()
//        {
//            int result = STServiceData.ERR_FAIL;
//
//            @Override
//            public void onSuccess(JSONObject object)
//            {
//                String retMsg = "";
//
//                cartprogDialog.dismiss();
//
//                retMsg = CommMgr.commService.parseCartProductsCount(object);
//                if (retMsg.equals(STServiceData.MSG_SUCCESS))
//                {
//                    result = STServiceData.ERR_SUCCESS;
//                    updateCartNum();
//                    return;
//                }
//                else
//                {
//                    result = STServiceData.ERR_EXCEPTION;
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable ex, String exception) {
//                cartprogDialog.dismiss();
//            }
//
//            @Override
//            public void onFinish()
//            {
//                cartprogDialog.dismiss();
//                if (result == STServiceData.ERR_FAIL)
//                {
//                    GlobalData.cartProdCount = 0;
//                }
//
//                result = 0;
//            }
//
//        };
//
//        cartprogDialog = ProgressDialog.show(
//                MyActivity.this,
//                "",
//                getString(R.string.waiting),
//                true,
//                false,
//                null);
//
//        CommMgr.commService.GetCartProductsCount(GlobalData.token, cartCntHandler);
//    }
}
