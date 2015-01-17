package com.damytech.yilebang.HuiYuanZhongXin;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.damytech.Global.GlobalData;
import com.damytech.STData.STCouponA;
import com.damytech.Utils.ResolutionSet;
import com.damytech.yilebang.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-28
 * Time: 上午12:17
 * To change this template use File | Settings | File Templates.
 */
public class CouponListAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    DisplayImageOptions options;
    Context mContext = null;
    Activity mActivity = null;

    ArrayList<STCouponA> m_arrCoupons = null;

    public CouponListAdapter(Activity activity, Context context) {

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

    public void setData(ArrayList<STCouponA> arrCoupons)
    {
        m_arrCoupons = arrCoupons;
    }

    /**
     * The number of items in the list is determined by the number of speeches
     * in our array.
     *
     * @see android.widget.ListAdapter#getCount()
     */
    public int getCount() {
        if (m_arrCoupons == null)
            return 0;

        return m_arrCoupons.size();
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
            STCouponA  anItem = null;
            if (m_arrCoupons != null)
            {
                anItem = m_arrCoupons.get(position);
            }

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.huiyuanzhongxin_youhuiquan_item, null);
                ResolutionSet._instance.iterateChild(convertView.findViewById(R.id.rlItemMain));
            } else {
            }

            ImageView imgCoupon = (ImageView)convertView.findViewById(R.id.imgCoupon);

            GlobalData.imageLoader.displayImage(anItem.image, imgCoupon, options);

            TextView txtView = (TextView) convertView.findViewById(R.id.txtCouponDesc);
            txtView.setText(anItem.name);

            txtView = (TextView) convertView.findViewById(R.id.txtCouponCost);
            txtView.setText(anItem.price);

            txtView = (TextView) convertView.findViewById(R.id.txtExpire);
            txtView.setText(anItem.endDate);

            /*convertView.setTag(anItem.uid);
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
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return convertView;
    }
}
