package com.damytech.yilebang.HuiYuanZhongXin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.damytech.CommService.CommMgr;
import com.damytech.Global.GlobalData;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.STData.STOrderByState;
import com.damytech.STData.STProductA;
import com.damytech.STData.STServiceData;
import com.damytech.STData.STSuggestionInfo;
import com.damytech.Utils.ResolutionSet;
import com.damytech.yilebang.OrderDetailActivity;
import com.damytech.yilebang.PaymentActivity;
import com.damytech.yilebang.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-30
 * Time: 上午1:00
 * To change this template use File | Settings | File Templates.
 */
public class OrderByStateListAdapter extends ArrayAdapter<STOrderByState> {
    protected JsonHttpResponseHandler handler;
    protected ProgressDialog progDialog;

    LayoutInflater mInflater;
    DisplayImageOptions options;
    Context mContext = null;

    static final int  PRODUCT_LIST_ITEM_HEIGHT = 110; // px

    ArrayList<STOrderByState> m_arrOrdersByState = null;

    int  m_nState; // 1:待付款, 2:待发货, 3:待收货, 4:待评价

    public OrderByStateListAdapter(Context context, int resourceId, ArrayList<STOrderByState> list, int state) {
        super(context, resourceId, list);

        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory()
                .cacheOnDisc()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading()
                .build();

        mContext = context;
        m_arrOrdersByState = list;
        m_nState = state;

        // Cache the LayoutInflate to avoid asking for a new one each time.
        mInflater = LayoutInflater.from(context);
    }

    /**
     * The number of items in the list is determined by the number of speeches
     * in our array.
     *
     * @see android.widget.ListAdapter#getCount()
     */
    public int getCount() {
        if (m_arrOrdersByState == null)
            return 0;

        return m_arrOrdersByState.size();
    }

    /**
     * Use the array index as a unique id.
     *
     * @see android.widget.ListAdapter#getItemId(int)
     */
    public long getItemId(int position) {
        return position;
    }

    /**
     * Make a view to hold each row.
     *
     * @see android.widget.ListAdapter#getView(int, android.view.View,
     *      android.view.ViewGroup)
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        // When convertView is not null, we can reuse it directly, there is no need
        // to reinflate it. We only inflate a new View when the convertView supplied
        // by ListView is null.
        try
        {
            STOrderByState  anItem = null;
            if (m_arrOrdersByState != null)
            {
                anItem = m_arrOrdersByState.get(position);
            }

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.huiyuanzhongxin_dingdan_item, null);
                ResolutionSet._instance.iterateChild(convertView.findViewById(R.id.rlItemMain));

                RelativeLayout rlItemMain = (RelativeLayout) convertView.findViewById(R.id.rlItemMain);
                rlItemMain.setTag(anItem.orderNo);

                rlItemMain.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try
                        {
                            String orderNo = (String)v.getTag();
                            Intent intent = new Intent(mContext, OrderDetailActivity.class);
                            intent.putExtra("OrderNo", orderNo);
                            intent.putExtra("State", m_nState);
                            mContext.startActivity(intent);
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                });

                TextView txtView = (TextView) convertView.findViewById(R.id.txtOrderNo);
                txtView.setText(anItem.orderNo);

                txtView = (TextView) convertView.findViewById(R.id.txtOrderTime);
                txtView.setText(anItem.orderTime);

                txtView = (TextView) convertView.findViewById(R.id.txtDistMethod);
                txtView.setText(anItem.deliverType);

                TextView  txtOrderStatus;
                Button btnOrderOperation1, btnOrderOperation2;
                txtOrderStatus = (TextView) convertView.findViewById(R.id.txtOrderStatus);
                btnOrderOperation1 = (Button) convertView.findViewById(R.id.btnOrderOperation1);
                btnOrderOperation2 = (Button) convertView.findViewById(R.id.btnOrderOperation2);
                btnOrderOperation1.setTag(anItem.orderNo);
                btnOrderOperation2.setTag(anItem.orderNo);
                switch ( m_nState ) {
                    case 1: // 待付款
                        txtOrderStatus.setText(mContext.getString(R.string.HuiYuanZhongXin_DingDan_State1));
                        btnOrderOperation1.setText(mContext.getString(R.string.HuiYuanZhongXin_DingDan_State1_Oper1));
                        btnOrderOperation2.setText(mContext.getString(R.string.HuiYuanZhongXin_DingDan_State1_Oper2));
                        btnOrderOperation1.setBackgroundResource(R.drawable.orange_btn_back_2);
                        btnOrderOperation2.setBackgroundResource(R.drawable.gray_btn_back);
                        break;
                    case 2: // 待发货
                        txtOrderStatus.setText(mContext.getString(R.string.HuiYuanZhongXin_DingDan_State2));
                        btnOrderOperation1.setText(mContext.getString(R.string.HuiYuanZhongXin_DingDan_State2_Oper));
                        btnOrderOperation2.setText("");
                        btnOrderOperation1.setBackgroundResource(R.drawable.orange_btn_back_2);
                        btnOrderOperation2.setBackgroundResource(android.R.color.transparent);
                        break;
                    case 3: // 待收货
                        txtOrderStatus.setText(mContext.getString(R.string.HuiYuanZhongXin_DingDan_State3));
                        btnOrderOperation1.setText(mContext.getString(R.string.HuiYuanZhongXin_DingDan_State3_Oper));
                        btnOrderOperation2.setText("");
                        btnOrderOperation1.setBackgroundResource(R.drawable.light_blue_btn_back);
                        btnOrderOperation2.setBackgroundResource(android.R.color.transparent);
                        break;
                    case 4: // 待评价
                        txtOrderStatus.setText(mContext.getString(R.string.HuiYuanZhongXin_DingDan_State4));
                        btnOrderOperation1.setText("");
                        btnOrderOperation2.setText("");
                        btnOrderOperation1.setBackgroundResource(android.R.color.transparent);
                        btnOrderOperation2.setBackgroundResource(android.R.color.transparent);
                        break;
                    default:
                        break;
                }

                RelativeLayout rlProductList = (RelativeLayout) convertView.findViewById(R.id.rlProductList);
                getView_fillProductList(rlProductList, position, anItem.orderNo, anItem.arrProducts);

                switch ( m_nState ) {
                    case 1:
                        btnOrderOperation1.setOnClickListener( new View.OnClickListener() {
                            @Override
                            public void onClick(View v) { // btn 付款
                                Intent intent = new Intent(mContext, PaymentActivity.class);
                                intent.putExtra("OrderNo", (String)v.getTag());
                                mContext.startActivity(intent);
                            }
                        });

                        btnOrderOperation2.setOnClickListener( new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) { // btn 取消
                                new AlertDialog.Builder(mContext)
                                        .setTitle(mContext.getString(R.string.HuiYuanZhongXin_DingDan_Msg_Title))
                                        .setMessage(mContext.getString(R.string.HuiYuanZhongXin_DingDan_Msg_Contents))
                                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                callCancelOrder((String)v.getTag());
                                                dialog.dismiss();
                                            }
                                        })
                                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .show();
                            }
                        });
                        break;
                    case 2:
                        btnOrderOperation1.setOnClickListener( new View.OnClickListener() {
                            @Override
                            public void onClick(View v) { // btn 提醒发货

                                // 2014.02.26 kimoc
                                // This is not customer 's permission , this is permission of site
                                /*
                                callRequestPayedProduct((String) v.getTag());
                                */
                            }
                        });
                        break;
                    case 3:
                        btnOrderOperation1.setOnClickListener( new View.OnClickListener() {
                            @Override
                            public void onClick(View v) { // btn 确认收货
                                callConfirmReception((String)v.getTag());
                            }
                        });
                        break;
                    default:
                        break;
                }
                /*convertView.setTag(anItem.name);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try
                        {
                            Intent intent = new Intent(mActivity, ArticlesActivity.class);
                            intent.putExtra("ProductId", (Integer)v.getTag());
                            mActivity.startActivity(intent);
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                });*/
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return convertView;
    }

    private void getView_fillProductList ( RelativeLayout rlProductList, int position, String orderNo, ArrayList<STProductA> arrProducts ) {
        for ( int i=0; i<arrProducts.size(); i++ ) {
            STProductA  aProduct = arrProducts.get(i);

            ImageView imgProduct;
            TextView  txtProductDesc;
            TextView  txtProductCount;
            TextView  txtProductCost;
            TextView  txtDoComment;

            int offset = (int)((PRODUCT_LIST_ITEM_HEIGHT*i)*ResolutionSet.fYpro);

            // product image
            {
                imgProduct = new ImageView(rlProductList.getContext());

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        (int)(100*ResolutionSet.fXpro), (int)(100*ResolutionSet.fYpro));
                layoutParams.setMargins((int)(10*ResolutionSet.fXpro), (int)(offset+5*ResolutionSet.fYpro), 0, 0);
                imgProduct.setLayoutParams(layoutParams);
                imgProduct.setPadding((int)(10*ResolutionSet.fXpro),(int)(10*ResolutionSet.fYpro),
                        (int)(10*ResolutionSet.fXpro),(int)(10*ResolutionSet.fYpro));
                imgProduct.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imgProduct.setId(100000+3000*i+m_nState*300+position);

                GlobalData.imageLoader.displayImage(aProduct.image, imgProduct, options);

                rlProductList.addView(imgProduct);
            }

            // product desc
            {
                txtProductDesc = new TextView(rlProductList.getContext());

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        -1, (int)(50*ResolutionSet.fYpro));
                layoutParams.addRule(RelativeLayout.RIGHT_OF, imgProduct.getId());
                layoutParams.addRule(RelativeLayout.ALIGN_TOP, imgProduct.getId());
                layoutParams.setMargins(0, 0, (int)(15*ResolutionSet.fXpro), 0);
                txtProductDesc.setLayoutParams(layoutParams);
                txtProductDesc.setId(110000+3000*i+m_nState*300+position);

                txtProductDesc.setTextSize(TypedValue.COMPLEX_UNIT_PX, (22*ResolutionSet.fPro));
                txtProductDesc.setText(aProduct.name);

                rlProductList.addView(txtProductDesc);
            }

            // product count
            {
                txtProductCount = new TextView(rlProductList.getContext());

                RelativeLayout.LayoutParams  layoutParams = new RelativeLayout.LayoutParams(
                        (int)(75*ResolutionSet.fXpro), (int)(30*ResolutionSet.fYpro));
                layoutParams.addRule(RelativeLayout.RIGHT_OF, imgProduct.getId());
                layoutParams.addRule(RelativeLayout.BELOW, txtProductDesc.getId());
                txtProductCount.setLayoutParams(layoutParams);
                txtProductCount.setId(120000+3000*i+m_nState*300+position);

                txtProductCount.setGravity(Gravity.CENTER_VERTICAL);
                txtProductCount.setTextSize(TypedValue.COMPLEX_UNIT_PX, (22*ResolutionSet.fPro));
                txtProductCount.setTextColor(Color.parseColor("#606060"));
                txtProductCount.setText(String.format(mContext.getString(R.string.HuiYuanZhongXin_DingDanChaXun_ProductCount_Fmt), aProduct.count));

                rlProductList.addView(txtProductCount);
            }

            // product cost
            {
                txtProductCost = new TextView(rlProductList.getContext());

                RelativeLayout.LayoutParams  layoutParams = new RelativeLayout.LayoutParams((int)(100*ResolutionSet.fXpro), -2);
                layoutParams.setMargins((int)(5*ResolutionSet.fXpro),0,0,0);
                layoutParams.addRule(RelativeLayout.RIGHT_OF, txtProductCount.getId());
                layoutParams.addRule(RelativeLayout.BELOW, txtProductDesc.getId());
                layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, txtProductCount.getId());
                txtProductCost.setLayoutParams(layoutParams);
                txtProductCost.setId(130000+3000*i+m_nState*300+position);

                txtProductCost.setGravity(Gravity.CENTER_VERTICAL);
                txtProductCost.setTextSize(TypedValue.COMPLEX_UNIT_PX, (22*ResolutionSet.fPro));
                txtProductCost.setTextColor(Color.parseColor("#ff0000"));
                txtProductCost.setText(String.format(mContext.getString(R.string.HuiYuanZhongXin_DingDan_ProductCost_Fmt),aProduct.price));

                rlProductList.addView(txtProductCost);
            }

            // comment button
            {
                if ( m_nState != 4 ) {
                    continue;
                }

                txtDoComment = new TextView(rlProductList.getContext());

                RelativeLayout.LayoutParams  layoutParams = new RelativeLayout.LayoutParams(-2, -2);
                layoutParams.setMargins((int)(5*ResolutionSet.fXpro),0,0,0);
                layoutParams.addRule(RelativeLayout.RIGHT_OF, txtProductCost.getId());
                layoutParams.addRule(RelativeLayout.BELOW, txtProductDesc.getId());
                layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, txtProductCount.getId());
                txtDoComment.setLayoutParams(layoutParams);
                txtDoComment.setId(140000+300*i+position);

                txtDoComment.setGravity(Gravity.CENTER);
                txtDoComment.setTextSize(TypedValue.COMPLEX_UNIT_PX, (22*ResolutionSet.fPro));
                txtDoComment.setTextColor(Color.parseColor("#ffffff"));
                txtDoComment.setText(mContext.getString(R.string.HuiYuanZhongXin_DingDan_State4_Oper));
                txtDoComment.setBackgroundResource(R.drawable.dark_orange_btn_back);
                txtDoComment.setPadding((int)(3*ResolutionSet.fXpro),0,(int)(5*ResolutionSet.fXpro),0);

                aProduct.userData = orderNo;
                txtDoComment.setTag(aProduct);
                txtDoComment.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        STProductA productInfo = (STProductA) v.getTag();
                        String orderNo = productInfo.userData;

                        Intent  intent = new Intent(mContext, DingDan_PingJiaActivity.class);
                        intent.putExtra("orderNo", orderNo);
                        intent.putExtra("productInfo", productInfo);
                        mContext.startActivity(intent);
                    }
                });

                rlProductList.addView(txtDoComment);
            }

            // separator
            /*{
                imgSeparator = new ImageView(rlProductList.getContext());

                RelativeLayout.LayoutParams  layoutParams = new RelativeLayout.LayoutParams(-1, -2);
                layoutParams.addRule(RelativeLayout.ALIGN_LEFT, imgProduct.getId());
                layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, txtProductCount.getId());
                imgSeparator.setLayoutParams(layoutParams);
                imgSeparator.setId(100004+offset);

                imgSeparator.setBackgroundResource(R.drawable.line_separator);

                rlProductList.addView(imgSeparator);
            }*/
        }
    }

    private void callCancelOrder (String orderNo) {
        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = "";

                progDialog.dismiss();

                retMsg = CommMgr.commService.parseCancelOrder(object);
                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    result = STServiceData.ERR_SUCCESS;
                    GlobalData.showToast(mContext,mContext.getString(R.string.HuiYuanZhongXin_DingDan_Msg_DingDanCancel));

                    DingDanActivity  dingDanActivity = (DingDanActivity)mContext;
                    dingDanActivity.loadInitialData();

                    return;
                }
                else
                {
                    result = STServiceData.ERR_EXCEPTION;
                }
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                progDialog.dismiss();

                if (result == STServiceData.ERR_FAIL)
                {
                    GlobalData.showToast(mContext, mContext.getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                mContext,
                "",
                mContext.getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.CancelOrder(GlobalData.token, orderNo, handler);
    }

    private void callRequestPayedProduct (String orderNo) {
        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = "";

                progDialog.dismiss();

                retMsg = CommMgr.commService.parseRequestPayedProduct(object);
                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    result = STServiceData.ERR_SUCCESS;
                    GlobalData.showToast(mContext,mContext.getString(R.string.HuiYuanZhongXin_DingDan_Msg_RequireDelivery));

                    DingDanActivity  dingDanActivity = (DingDanActivity)mContext;
                    dingDanActivity.loadInitialData();
                    return;
                }
                else
                {
                    result = STServiceData.ERR_EXCEPTION;
                }
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                progDialog.dismiss();

                if (result == STServiceData.ERR_FAIL)
                {
                    GlobalData.showToast(mContext, mContext.getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                mContext,
                "",
                mContext.getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.RequestPayedProduct(GlobalData.token, orderNo, handler);
    }

    private void callConfirmReception (String orderNo) {
        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = "";

                progDialog.dismiss();

                retMsg = CommMgr.commService.parseConfirmReception(object);
                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    result = STServiceData.ERR_SUCCESS;
                    GlobalData.showToast(mContext,mContext.getString(R.string.HuiYuanZhongXin_DingDan_Msg_ConfirmReception));

                    DingDanActivity  dingDanActivity = (DingDanActivity)mContext;
                    dingDanActivity.loadInitialData();
                    return;
                }
                else
                {
                    result = STServiceData.ERR_EXCEPTION;
                }
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                progDialog.dismiss();

                if (result == STServiceData.ERR_FAIL)
                {
                    GlobalData.showToast(mContext, mContext.getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                mContext,
                "",
                mContext.getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.ConfirmReception(GlobalData.token, orderNo, handler);
    }
}
