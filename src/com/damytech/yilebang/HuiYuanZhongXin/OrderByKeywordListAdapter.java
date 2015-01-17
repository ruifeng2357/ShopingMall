package com.damytech.yilebang.HuiYuanZhongXin;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.damytech.Global.GlobalData;
import com.damytech.STData.STOrderByKeyword;
import com.damytech.STData.STProductA;
import com.damytech.Utils.ResolutionSet;
import com.damytech.yilebang.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-27
 * Time: 上午12:30
 * To change this template use File | Settings | File Templates.
 */
public class OrderByKeywordListAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    DisplayImageOptions options;
    Context mContext = null;
    Activity mActivity = null;

    static final int  PRODUCT_LIST_ITEM_HEIGHT = 110; // px
    static final int  ORDER_HISTORY_ITEM_HEIGHT = 70; // px

    ArrayList<STOrderByKeyword> m_arrOrdersByKeyword = null;

    public OrderByKeywordListAdapter(Activity activity, Context context) {

        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory()
                .cacheOnDisc()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading()
                .build();

        mContext = context;
        mActivity = activity;

        // Cache the LayoutInflate to avoid asking for a new one each time.
        mInflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<STOrderByKeyword> arrOrdersByKeyword)
    {
        m_arrOrdersByKeyword = arrOrdersByKeyword;
    }

    /**
     * The number of items in the list is determined by the number of speeches
     * in our array.
     *
     * @see android.widget.ListAdapter#getCount()
     */
    public int getCount() {
        if (m_arrOrdersByKeyword == null)
            return 0;

        return m_arrOrdersByKeyword.size();
    }

    /**
     * Since the data comes from an array, just returning the index is
     * sufficient to get at the data. If we were using a more complex data
     * structure, we would return whatever object represents one row in the
     * list.
     *
     * @see android.widget.ListAdapter#getItem(int)
     */
    public Object getItem(int position) {
        return position;
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
            STOrderByKeyword  anItem = null;
            if (m_arrOrdersByKeyword != null)
            {
                anItem = m_arrOrdersByKeyword.get(position);
            }

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.huiyuanzhongxin_dingdanzhuangtaichaxun_item, null);
                ResolutionSet._instance.iterateChild(convertView.findViewById(R.id.rlItemMain));

                TextView txtView = (TextView) convertView.findViewById(R.id.txtOrderNo);
                txtView.setText(anItem.orderNo);

                txtView = (TextView) convertView.findViewById(R.id.txtOrderTime);
                txtView.setText(anItem.orderTime);

                txtView = (TextView) convertView.findViewById(R.id.txtDistMethod);
                txtView.setText(anItem.deliverType);

                txtView = (TextView) convertView.findViewById(R.id.txtDistMethod2);
                txtView.setText(anItem.deliverType);

                RelativeLayout  rlProductList = (RelativeLayout) convertView.findViewById(R.id.rlProductList);
                getView_fillProductList(rlProductList, position, anItem.arrProducts);

                RelativeLayout  rlOrderHistory = (RelativeLayout) convertView.findViewById(R.id.rlOrderHistory);
                getView_fillOrderHistory ( rlOrderHistory, position, anItem );

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

    private void getView_fillProductList ( RelativeLayout rlProductList, int position, ArrayList<STProductA> arrProducts ) {
        for ( int i=0; i<arrProducts.size(); i++ ) {
            STProductA  aProduct = arrProducts.get(i);

            ImageView  imgProduct;
            TextView  txtProductDesc;
            TextView  txtProductCount;
            TextView  txtProductCost;
            ImageView  imgSeparator;

            int offset = (int)(PRODUCT_LIST_ITEM_HEIGHT*i*ResolutionSet.fYpro);

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
                imgProduct.setId(200000+300*i+position);

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
                txtProductDesc.setId(210000+300*i+position);

                txtProductDesc.setTextSize(TypedValue.COMPLEX_UNIT_PX, (22*ResolutionSet.fPro));
                txtProductDesc.setText(aProduct.name);

                rlProductList.addView(txtProductDesc);
            }

            // product count
            {
                txtProductCount = new TextView(rlProductList.getContext());

                RelativeLayout.LayoutParams  layoutParams = new RelativeLayout.LayoutParams(
                        (int)(75*ResolutionSet.fXpro), -1);
                layoutParams.addRule(RelativeLayout.RIGHT_OF, imgProduct.getId());
                layoutParams.addRule(RelativeLayout.BELOW, txtProductDesc.getId());
                txtProductCount.setLayoutParams(layoutParams);
                txtProductCount.setId(220000+300*i+position);

                txtProductCount.setGravity(Gravity.CENTER_VERTICAL);
                txtProductCount.setTextSize(TypedValue.COMPLEX_UNIT_PX, (22*ResolutionSet.fPro));
                txtProductCount.setTextColor(Color.parseColor("#606060"));
                txtProductCount.setText(String.format(mContext.getString(R.string.HuiYuanZhongXin_DingDanChaXun_ProductCount_Fmt), aProduct.count));

                rlProductList.addView(txtProductCount);
            }

            // product cost
            {
                txtProductCost = new TextView(rlProductList.getContext());

                RelativeLayout.LayoutParams  layoutParams = new RelativeLayout.LayoutParams(-2, -2);
                layoutParams.setMargins((int)(5*ResolutionSet.fXpro),0,0,0);
                layoutParams.addRule(RelativeLayout.RIGHT_OF, txtProductCount.getId());
                layoutParams.addRule(RelativeLayout.BELOW, txtProductDesc.getId());
                layoutParams.addRule(RelativeLayout.ALIGN_RIGHT, txtProductDesc.getId());
                layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, txtProductCount.getId());
                txtProductCost.setLayoutParams(layoutParams);
                txtProductCost.setId(230000+300*i+position);

                txtProductCost.setGravity(Gravity.CENTER_VERTICAL);
                txtProductCost.setTextSize(TypedValue.COMPLEX_UNIT_PX, (22*ResolutionSet.fPro));
                txtProductCost.setTextColor(Color.parseColor("#ff0000"));
                txtProductCost.setText(String.format(mContext.getString(R.string.HuiYuanZhongXin_DingDan_ProductCost_Fmt),aProduct.price));

                rlProductList.addView(txtProductCost);
            }

            // separator
            {
                imgSeparator = new ImageView(rlProductList.getContext());

                RelativeLayout.LayoutParams  layoutParams = new RelativeLayout.LayoutParams(-1, -2);
                layoutParams.setMargins(0,(int)(10*ResolutionSet.fYpro),0,0);
                layoutParams.addRule(RelativeLayout.ALIGN_LEFT, imgProduct.getId());
                layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, txtProductCount.getId());
                imgSeparator.setLayoutParams(layoutParams);
                imgSeparator.setId(240000+300*i+position);

                imgSeparator.setBackgroundResource(R.drawable.line_separator);

                rlProductList.addView(imgSeparator);
            }
        }
    }

    private void getView_fillOrderHistory ( RelativeLayout rlOrderHistory, int position, STOrderByKeyword anOrder ) {
        ImageView  imgClock;
        TextView  txtTime;
        TextView  txtMessage;

        int  offset = 0;

        if ( !anOrder.payTime.isEmpty() ) {
            {
                imgClock = new ImageView(rlOrderHistory.getContext());

                RelativeLayout.LayoutParams  layoutParams = new RelativeLayout.LayoutParams((int)(20*ResolutionSet.fXpro), (int)(25*ResolutionSet.fYpro));
                layoutParams.setMargins(0,(int)(offset+10*ResolutionSet.fYpro),0,0);
                imgClock.setLayoutParams(layoutParams);
                imgClock.setId(300000+position);

                imgClock.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imgClock.setImageResource(R.drawable.timer);

                rlOrderHistory.addView(imgClock);
            }

            {
                txtTime = new TextView(rlOrderHistory.getContext());

                RelativeLayout.LayoutParams  layoutParams = new RelativeLayout.LayoutParams(-2, (int)(25*ResolutionSet.fYpro));
                layoutParams.addRule(RelativeLayout.RIGHT_OF, imgClock.getId());
                layoutParams.addRule(RelativeLayout.ALIGN_TOP, imgClock.getId());
                layoutParams.setMargins((int)(5*ResolutionSet.fXpro),0,0,0);
                txtTime.setLayoutParams(layoutParams);
                txtTime.setId(310000+position);

                txtTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, (22*ResolutionSet.fPro));
                txtTime.setTextColor(Color.parseColor("#666666"));
                txtTime.setText(anOrder.payTime);

                rlOrderHistory.addView(txtTime);
            }

            {
                txtMessage = new TextView(rlOrderHistory.getContext());

                RelativeLayout.LayoutParams  layoutParams = new RelativeLayout.LayoutParams(-1, (int)(25*ResolutionSet.fYpro));
                layoutParams.addRule(RelativeLayout.BELOW, imgClock.getId());
                layoutParams.addRule(RelativeLayout.ALIGN_LEFT, imgClock.getId());
                txtMessage.setLayoutParams(layoutParams);
                txtMessage.setId(320000+position);

                txtMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, (22*ResolutionSet.fPro));
                txtMessage.setTextColor(Color.parseColor("#000000"));
                if ((anOrder.payType1==1||anOrder.payType1==3||anOrder.payType1==6) &&
                        (anOrder.payType2==1||anOrder.payType2==3||anOrder.payType2==6)) {
                    txtMessage.setText(mContext.getString(R.string.HuiYuanZhongXin_DingDanChaXun_PayTime_Msg_2));
                } else {
                    txtMessage.setText(mContext.getString(R.string.HuiYuanZhongXin_DingDanChaXun_PayTime_Msg_1));
                }

                rlOrderHistory.addView(txtMessage);
            }

            offset += ORDER_HISTORY_ITEM_HEIGHT;
        }

        if ( !anOrder.sndTime.isEmpty() ) {
            {
                imgClock = new ImageView(rlOrderHistory.getContext());

                RelativeLayout.LayoutParams  layoutParams = new RelativeLayout.LayoutParams((int)(20*ResolutionSet.fXpro), (int)(25*ResolutionSet.fYpro));
                layoutParams.setMargins(0,(int)(offset+10*ResolutionSet.fYpro),0,0);
                imgClock.setLayoutParams(layoutParams);
                imgClock.setId(330000+position);

                imgClock.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imgClock.setImageResource(R.drawable.timer);

                rlOrderHistory.addView(imgClock);
            }

            {
                txtTime = new TextView(rlOrderHistory.getContext());

                RelativeLayout.LayoutParams  layoutParams = new RelativeLayout.LayoutParams(-2, (int)(25*ResolutionSet.fYpro));
                layoutParams.addRule(RelativeLayout.RIGHT_OF, imgClock.getId());
                layoutParams.addRule(RelativeLayout.ALIGN_TOP, imgClock.getId());
                layoutParams.setMargins((int)(5*ResolutionSet.fXpro),0,0,0);
                txtTime.setLayoutParams(layoutParams);
                txtTime.setId(340000+position);

                txtTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, (22*ResolutionSet.fPro));
                txtTime.setTextColor(Color.parseColor("#666666"));
                txtTime.setText(anOrder.sndTime);

                rlOrderHistory.addView(txtTime);
            }

            {
                txtMessage = new TextView(rlOrderHistory.getContext());

                RelativeLayout.LayoutParams  layoutParams = new RelativeLayout.LayoutParams(-1, (int)(25*ResolutionSet.fYpro));
                layoutParams.addRule(RelativeLayout.BELOW, imgClock.getId());
                layoutParams.addRule(RelativeLayout.ALIGN_LEFT, imgClock.getId());
                txtMessage.setLayoutParams(layoutParams);
                txtMessage.setId(350000+position);

                txtMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, (22*ResolutionSet.fPro));
                txtMessage.setTextColor(Color.parseColor("#000000"));
                txtMessage.setText(anOrder.comment);

                rlOrderHistory.addView(txtMessage);
            }

            offset += ORDER_HISTORY_ITEM_HEIGHT;
        }

        if ( !anOrder.rcvTime.isEmpty() ) {
            {
                imgClock = new ImageView(rlOrderHistory.getContext());

                RelativeLayout.LayoutParams  layoutParams = new RelativeLayout.LayoutParams((int)(20*ResolutionSet.fXpro), (int)(25*ResolutionSet.fYpro));
                layoutParams.setMargins(0,(int)(offset+10*ResolutionSet.fYpro),0,0);
                imgClock.setLayoutParams(layoutParams);
                imgClock.setId(360000+position);

                imgClock.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imgClock.setImageResource(R.drawable.timer);

                rlOrderHistory.addView(imgClock);
            }

            {
                txtTime = new TextView(rlOrderHistory.getContext());

                RelativeLayout.LayoutParams  layoutParams = new RelativeLayout.LayoutParams(-2, (int)(25*ResolutionSet.fYpro));
                layoutParams.addRule(RelativeLayout.RIGHT_OF, imgClock.getId());
                layoutParams.addRule(RelativeLayout.ALIGN_TOP, imgClock.getId());
                layoutParams.setMargins((int)(5*ResolutionSet.fXpro),0,0,0);
                txtTime.setLayoutParams(layoutParams);
                txtTime.setId(370000+position);

                txtTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, (22*ResolutionSet.fPro));
                txtTime.setTextColor(Color.parseColor("#666666"));
                txtTime.setText(anOrder.rcvTime);

                rlOrderHistory.addView(txtTime);
            }

            {
                txtMessage = new TextView(rlOrderHistory.getContext());

                RelativeLayout.LayoutParams  layoutParams = new RelativeLayout.LayoutParams(-1, (int)(25*ResolutionSet.fYpro));
                layoutParams.addRule(RelativeLayout.BELOW, imgClock.getId());
                layoutParams.addRule(RelativeLayout.ALIGN_LEFT, imgClock.getId());
                txtMessage.setLayoutParams(layoutParams);
                txtMessage.setId(380000+position);

                txtMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, (22*ResolutionSet.fPro));
                txtMessage.setTextColor(Color.parseColor("#000000"));
                txtMessage.setText(mContext.getString(R.string.HuiYuanZhongXin_DingDanChaXun_RecvTime_Msg));

                rlOrderHistory.addView(txtMessage);
            }

            offset += ORDER_HISTORY_ITEM_HEIGHT;
        }

        if ( !anOrder.cancelTime.isEmpty() ) {
            {
                imgClock = new ImageView(rlOrderHistory.getContext());

                RelativeLayout.LayoutParams  layoutParams = new RelativeLayout.LayoutParams((int)(20*ResolutionSet.fXpro), (int)(25*ResolutionSet.fYpro));
                layoutParams.setMargins(0,(int)(offset+10*ResolutionSet.fYpro),0,0);
                imgClock.setLayoutParams(layoutParams);
                imgClock.setId(390000+position);

                imgClock.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imgClock.setImageResource(R.drawable.timer);

                rlOrderHistory.addView(imgClock);
            }

            {
                txtTime = new TextView(rlOrderHistory.getContext());

                RelativeLayout.LayoutParams  layoutParams = new RelativeLayout.LayoutParams(-2, (int)(25*ResolutionSet.fYpro));
                layoutParams.addRule(RelativeLayout.RIGHT_OF, imgClock.getId());
                layoutParams.addRule(RelativeLayout.ALIGN_TOP, imgClock.getId());
                layoutParams.setMargins((int)(5*ResolutionSet.fXpro),0,0,0);
                txtTime.setLayoutParams(layoutParams);
                txtTime.setId(400000+position);

                txtTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, (22*ResolutionSet.fPro));
                txtTime.setTextColor(Color.parseColor("#666666"));
                txtTime.setText(anOrder.cancelTime);

                rlOrderHistory.addView(txtTime);
            }

            {
                txtMessage = new TextView(rlOrderHistory.getContext());

                RelativeLayout.LayoutParams  layoutParams = new RelativeLayout.LayoutParams(-1, (int)(25*ResolutionSet.fYpro));
                layoutParams.addRule(RelativeLayout.BELOW, imgClock.getId());
                layoutParams.addRule(RelativeLayout.ALIGN_LEFT, imgClock.getId());
                txtMessage.setLayoutParams(layoutParams);
                txtMessage.setId(410000+position);

                txtMessage.setTextSize(TypedValue.COMPLEX_UNIT_PX, (22*ResolutionSet.fPro));
                txtMessage.setTextColor(Color.parseColor("#000000"));
                txtMessage.setText(mContext.getString(R.string.HuiYuanZhongXin_DingDanChaXun_CancelTime_Msg));

                rlOrderHistory.addView(txtMessage);
            }

            offset += ORDER_HISTORY_ITEM_HEIGHT;
        }
    }
}
