package com.damytech.yilebang;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.damytech.CommService.CommMgr;
import com.damytech.Global.GlobalData;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.STData.STParcelableBasketItem;
import com.damytech.STData.STServiceData;
import com.damytech.STData.ShoppingCart.STBasketItemInfo;
import com.damytech.STData.ShoppingCart.STShopCartInfo;
import com.damytech.Utils.ResolutionSet;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: KimChg
 * Date: 13-11-22
 * Time: 下午3:50
 * To change this template use File | Settings | File Templates.
 */
public class ArticlesBasketActivity extends MyActivity implements View.OnTouchListener {
    private ArticlesBasketItemAdapter mBasketAdapter;
    private ListView mListView;

    private STShopCartInfo mShopCart = new STShopCartInfo();
    private ArrayList<STBasketItemInfo> mBasketInfoArray;
    private int nCurPageNumber = 0;
    public boolean bexistNext = true;

    final int ITEMS_REFRESH_COUNT = 10;

    private JsonHttpResponseHandler handler;
    private JsonHttpResponseHandler handler1;
    private JsonHttpResponseHandler handler2;
    private ProgressDialog progDialog;

    private RelativeLayout mBtnDelete = null;
    private CheckBox mSelectAll = null;
    private Button mBtnOrder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.articlesbasket);

        initControls();
    }

    /**
     * Initialize all controls
     */
    private void initControls()
    {
        ResolutionSet._instance.iterateChild(findViewById(R.id.rlArticlesBasketMain));

        mListView = (ListView)findViewById(R.id.listBasketItems);

        mBasketInfoArray = new ArrayList<STBasketItemInfo>();

        mListView.setDivider(new ColorDrawable(Color.TRANSPARENT));
        mListView.setCacheColorHint(Color.parseColor("#FFF1F1F1"));
        mListView.setDividerHeight(2);
        mListView.setOnTouchListener(this);
        mBasketAdapter = new ArticlesBasketItemAdapter(ArticlesBasketActivity.this, this.getApplicationContext());
        mBasketAdapter.setData(mBasketInfoArray);

        mListView.setAdapter(mBasketAdapter);

        mSelectAll = (CheckBox) findViewById(R.id.chkSelectAll);
        mSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // change all check box state
                for (int i = 0; i < mBasketInfoArray.size(); i++)
                {
                    STBasketItemInfo item = mBasketInfoArray.get(i);
                    item.checked = isChecked;
                }
                // update check box state
                updateUI();
            }
        });

        mBtnDelete = (RelativeLayout) findViewById(R.id.rlBottom_Packet);
        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSelectedItem();
            }
        });

        mBtnOrder = (Button) findViewById(R.id.btnGoGoods);
        mBtnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoOrderConfirm();
            }
        });

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
     * Update UI
     */
    private void updateUI()
    {
        try
        {
            TextView txtChip = (TextView) findViewById(R.id.txtChip);
            txtChip.setText(Double.toString(mShopCart.totalPrice/* + mShopCart.transPrice*/));
            TextView txtArtPrice = (TextView) findViewById(R.id.txtArtPrice);
            txtArtPrice.setText(Double.toString(mShopCart.totalPrice));
//            TextView txtVaryPrice = (TextView) findViewById(R.id.txtVaryPrice);
//            txtVaryPrice.setText(Double.toString(mShopCart.transPrice));
            TextView  txtCount = (TextView) findViewById(R.id.txtCount);
            txtCount.setText(Integer.toString(mShopCart.arrProducts.size()));

            mSelectAll.setChecked(false);

            // update list
            mBasketInfoArray = mShopCart.arrProducts;
            mBasketAdapter.setData(mBasketInfoArray);
            mBasketAdapter.notifyDataSetChanged();
            mListView.invalidate();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    /**
     * Show edit dialog of product
     * @param data [in], product data
     */
    public void ShowEditdlg(STBasketItemInfo data)
    {
        ShopcartEditDlg dlg = null;
        dlg = new ShopcartEditDlg(ArticlesBasketActivity.this, data);
        dlg.show();
    }

    private void deleteSelectedItem()
    {
        ArrayList<STBasketItemInfo> arrDel = null;
        try
        {
            arrDel = new ArrayList<STBasketItemInfo>(0);
            for (int i = 0; i < mBasketInfoArray.size(); i++)
            {
                STBasketItemInfo info = mBasketInfoArray.get(i);
                if (info.checked)
                {
                    arrDel.add(info);
                }
            }

            if (arrDel.size() > 0) {
                callDeleteShopCartsProducts(arrDel, GlobalData.token);
            } else {
                GlobalData.showToast(ArticlesBasketActivity.this, getString(R.string.Articles_Basket_NoSelected));
                return;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    /**
     * Go to order confirm
     */
    private void gotoOrderConfirm()
    {
        double totalPrice = 0;

        try
        {
//            if ( mBasketInfoArray.size() <= 0 ) {
//                GlobalData.showToast(ArticlesBasketActivity.this, getString(R.string.Articles_Basket_NoSelected));
//                return;
//            }

            // make basket item parcelable array
            ArrayList<STParcelableBasketItem> parcelArray = new ArrayList<STParcelableBasketItem>(0);
            for (STBasketItemInfo item : mBasketInfoArray)
            {
                if (item.checked)
                {
                    STParcelableBasketItem parcelitem = new STParcelableBasketItem(item);
                    parcelArray.add(parcelitem);

                    totalPrice += item.price * item.count;
                }
            }

            // check selected count
            if ( parcelArray.size() <= 0 ) {
                GlobalData.showToast(ArticlesBasketActivity.this, getString(R.string.Articles_Basket_NoSelected));
                return;
            }

            Intent intent = new Intent(ArticlesBasketActivity.this, OrderConfirmActivity.class);
            intent.putParcelableArrayListExtra("ItemArray", parcelArray);
            intent.putExtra("TotalPrice", totalPrice);
            intent.putExtra("TransPrice", mShopCart.transPrice);
            startActivity(intent);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onResume()
    {
        super.onResume();

        callGetCartsProducts(GlobalData.token, 1);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private float mLastMotionX;
    private float mLastMotionY;

    private boolean mIsBeingDragged;

    private View mDownView;

    @Override
    public boolean onTouch(View v, MotionEvent ev) {

        final int action = ev.getAction();
        final float x = ev.getX();
        final float y = ev.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // Find the child view that was touched (perform a hit test)
                Rect rect = new Rect();
                int childCount = mListView.getChildCount();
                int[] listViewCoords = new int[2];
                mListView.getLocationOnScreen(listViewCoords);
                int posx = (int) ev.getRawX() - listViewCoords[0];
                int posy = (int) ev.getRawY() - listViewCoords[1];
                View child;
                mDownView = null;
                for (int i = 0; i < childCount; i++) {
                    child = mListView.getChildAt(i);
                    child.getHitRect(rect);
                    if (rect.contains(posx, posy)) {
                        mDownView = child; // This is your down view
                        break;
                    }
                }

                if (mDownView != null)
                {
                    mLastMotionX = x;
                    mLastMotionY = y;
                }

//                if (mDownView != null) {
//                    mDownX = ev.getRawX();
//                    mDownPosition = mListView.getPositionForView(mDownView);
//
//                }

                break;
            case MotionEvent.ACTION_MOVE:
                final float dx = x - mLastMotionX;
                final float xDiff = Math.abs(dx);
                final float yDiff = Math.abs(y - mLastMotionY);
                if (/*xDiff > mTouchSlop && */xDiff > yDiff) {
                    mIsBeingDragged = true;
//                    mLastMotionX = x;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                final float deltaX = mLastMotionX - x;
                if (mIsBeingDragged && (mDownView != null)) {
                    // get selected state ( visible state of delete button )
                    Boolean bSel = (Boolean)mDownView.getTag();
                    if (bSel == null) bSel = false;
                    if (deltaX > 0)     // left move event
                    {
                        if (!bSel)
                        {
                            showDelBtnOfList(mDownView);
                            mDownView.setTag(true);
                        }
                    }
                    else                // right move event
                    {
                        if (bSel)
                        {
                            resetDelBtnOfList(mDownView);
                            mDownView.setTag(false);
                        }
                    }

                }

                break;

        }

        return super.onTouchEvent(ev);
    }

    /**
     * Show delete button of selected list item
     * @param view [in], child view of list
     */
    private void showDelBtnOfList(View view)
    {
//        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(view, "alpha", 0f);
//        fadeOut.setDuration(2000);
//        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
//        fadeIn.setDuration(2000);

        RelativeLayout rlLeft = (RelativeLayout) view.findViewById(R.id.rlLeftLayout);
        ImageView imgDel = (ImageView) view.findViewById(R.id.imgDelete);

        float destPos = ResolutionSet.fXpro * 150;
        ObjectAnimator mover1 = ObjectAnimator.ofFloat(rlLeft, "translationX", 0f, -destPos);
        mover1.setDuration(500);
        ObjectAnimator mover2 = ObjectAnimator.ofFloat(imgDel, "translationX", 0f, -destPos);
        mover2.setDuration(500);
        AnimatorSet animatorSet = new AnimatorSet();

//        animatorSet.play(mover).with(fadeIn).after(fadeOut);
        animatorSet.play(mover1).with(mover2);
        animatorSet.start();


    }

    /**
     * Reset position of delete button of list
     * @param view [in], child view of list
     */
    private void resetDelBtnOfList(View view)
    {
        RelativeLayout rlLeft = (RelativeLayout) view.findViewById(R.id.rlLeftLayout);
        ImageView imgDel = (ImageView) view.findViewById(R.id.imgDelete);

        float destPos = ResolutionSet.fXpro * 150;
        ObjectAnimator mover1 = ObjectAnimator.ofFloat(rlLeft, "translationX", -destPos, 0f);
        mover1.setDuration(500);
        ObjectAnimator mover2 = ObjectAnimator.ofFloat(imgDel, "translationX", -destPos, 0f);
        mover2.setDuration(500);
        AnimatorSet animatorSet = new AnimatorSet();

//        animatorSet.play(mover).with(fadeIn).after(fadeOut);
        animatorSet.play(mover1).with(mover2);
        animatorSet.start();
    }



    /////////////////////////////////////// Service Relation //////////////////////////////////////
    /**
     * Call GetCartsProducts service
     */
    public void callGetCartsProducts(String token, int pageno)
    {
        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = STServiceData.MSG_SUCCESS;

                progDialog.dismiss();

                retMsg = CommMgr.commService.parseCartsProducts(object, mShopCart);
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

                GlobalData.showToast(ArticlesBasketActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                progDialog.dismiss();
                if (result == STServiceData.ERR_FAIL)
                {
                    GlobalData.showToast(ArticlesBasketActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                ArticlesBasketActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.GetCartsProducts(token, pageno, handler);

        return;
    }


    /**
     * Call UpdateCartProductCount service
     */
    private void callUpdateCartProductCount(int pid, int gid1, int gid2, int count, String token)
    {
        handler1 = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = STServiceData.MSG_SUCCESS;

                progDialog.dismiss();

                retMsg = CommMgr.commService.parseUpdateCartProductCount(object);
                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    result = STServiceData.ERR_SUCCESS;
                    callGetCartsProducts(GlobalData.token, 1);
                    return;
                }
                else
                {
                    result = STServiceData.ERR_EXCEPTION;
                }

                GlobalData.showToast(ArticlesBasketActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                progDialog.dismiss();
                if (result == STServiceData.ERR_FAIL)
                {
                    GlobalData.showToast(ArticlesBasketActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                ArticlesBasketActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.UpdateCartProductCount(pid, gid1, gid2, count, token, handler1);

        return;
    }

    /**
     * call DeleteShopCartsProducts service
     * @param dataList [in], array of product id to be deleted
     * @param token [in], user token
     */
    public void callDeleteShopCartsProducts(ArrayList<STBasketItemInfo> dataList, String token)
    {
        handler2 = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = STServiceData.MSG_SUCCESS;

                progDialog.dismiss();

                retMsg = CommMgr.commService.parseDeleteShopCartsProducts(object);
                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    result = STServiceData.ERR_SUCCESS;
                    callGetCartsProducts(GlobalData.token, 1);
                    return;
                }
                else
                {
                    result = STServiceData.ERR_EXCEPTION;
                }

                GlobalData.showToast(ArticlesBasketActivity.this, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                progDialog.dismiss();
                if (result == STServiceData.ERR_FAIL)
                {
                    GlobalData.showToast(ArticlesBasketActivity.this, getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                ArticlesBasketActivity.this,
                "",
                getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.DeleteShopCartsProducts(dataList, token, handler2);

        return;
    }

    ////////////////////////////////////////////// Edit Dialog ////////////////////////////////////////////
    /**
     * Edit dialog of product count in shopping cart
     */
    public final class ShopcartEditDlg extends Dialog {

        Context mContext = null;
        STBasketItemInfo mItemInfo = null;

        TextView lblCurCount = null;
        TextView lblPrice = null;
        Button btnConfirm;
        Button btnCancel;

        ImageView imgMinus;
        ImageView imgPlus;

        public ShopcartEditDlg(Context context)
        {
            super(context);
            mContext = context;
        }

        public ShopcartEditDlg(Context context, STBasketItemInfo itemInfo)
        {
            super(context);
            mContext = context;
            mItemInfo = itemInfo;
        }

        @Override
        public void onCreate(Bundle $savedInstanceState) {
            super.onCreate( $savedInstanceState );
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.shopcart_editdlg);

            // initialize
            initControls();
        }

        /**
         * Initialize all controls
         */
        private void initControls()
        {
            try
            {
                ResolutionSet._instance.iterateChild(findViewById(R.id.rlShopCartEditDlgMain));

                lblCurCount = (TextView) findViewById(R.id.editCount);
                lblCurCount.setText(Integer.toString(mItemInfo.count));
                lblPrice = (TextView) findViewById(R.id.txtPrice);
                lblPrice.setText(Double.toString(mItemInfo.price));

                imgPlus = (ImageView) findViewById(R.id.imgbtnplus);
                imgPlus.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String strData = lblCurCount.getText().toString();
                        int nData = 0;
                        try {
                            nData = Integer.parseInt(strData);
                        }catch ( Exception e) {
                            nData = 0;
                        }

                        nData += 1;
                        lblCurCount.setText(Integer.toString(nData));
                    }
                });

                imgMinus = (ImageView) findViewById(R.id.imgbtnminus);
                imgMinus.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String strData = lblCurCount.getText().toString();
                        int nData = 0;
                        try {
                            nData = Integer.parseInt(strData);
                        }catch ( Exception e) {
                            nData = 0;
                        }

                        nData -= 1;
                        if (nData < 1)
                            nData = 1;
                        lblCurCount.setText(Integer.toString(nData));
                    }
                });

                btnCancel = (Button) findViewById(R.id.btnCancel);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });

                btnConfirm = (Button) findViewById(R.id.btnConfirm);
                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try
                        {
                            int count = Integer.parseInt(lblCurCount.getText().toString());
                            callUpdateCartProductCount(mItemInfo.pid, mItemInfo.spec1, mItemInfo.spec2, count, GlobalData.token);
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }
                        dismiss();
                    }
                });
            }
            catch (Exception ex)
            {
                ex.fillInStackTrace();
            }
        }
    }
}
