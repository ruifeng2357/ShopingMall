package com.damytech.yilebang;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.damytech.Global.GlobalData;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.STData.ShoppingCart.STBasketItemInfo;
import com.damytech.Utils.ResolutionSet;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: KimOC
 * Date: 11/29/13
 * Time: 10:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class OrderItemAdapter extends BaseAdapter {
    Context mContext = null;
    Activity mActivity = null;
    private LayoutInflater mInflater;

    DisplayImageOptions options;

    private ArrayList<STBasketItemInfo> mInfos = null;

    public OrderItemAdapter(Activity activity, Context context) {
        mContext = context;
        mActivity = activity;
        // Cache the LayoutInflate to avoid asking for a new one each time.
        mInflater = LayoutInflater.from(context);

        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_stub)
                .showImageOnFail(R.drawable.ic_stub)
                .cacheInMemory()
                .cacheOnDisc()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading()
                .build();
    }

    public void setData(ArrayList<STBasketItemInfo> hisData)
    {
        mInfos = hisData;
    }

    /**
     * The number of items in the list is determined by the number of speeches
     * in our array.
     *
     * @see android.widget.ListAdapter#getCount()
     */
    public int getCount() {
        if (mInfos == null)
            return 0;

        return mInfos.size();
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
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.orderconfirm_item, null);
                ResolutionSet._instance.iterateChild(convertView.findViewById(R.id.rlItemMain));
            } else {
            }

            STBasketItemInfo preferInfo = null;
            if (mInfos != null)
            {
                preferInfo = mInfos.get(position);
            }

            // Bind the data efficiently with the holder.
            if (preferInfo != null)
            {
                RelativeLayout rlMain = (RelativeLayout) convertView.findViewById(R.id.rlItemMain);
                rlMain.setTag(position);
                rlMain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer pos = (Integer)v.getTag();
                        STBasketItemInfo data = mInfos.get(pos);
                        // show edit count dialog

                    }
                });

                ImageView imgItem = (ImageView) convertView.findViewById(R.id.imgProduct);
                TextView txtTitle = (TextView) convertView.findViewById(R.id.txtProductDesc);
                TextView editCount = (TextView) convertView.findViewById(R.id.txtProductCount);
                TextView txtPrice = (TextView) convertView.findViewById(R.id.txtProductCost);

                GlobalData.imageLoader.displayImage(preferInfo.image, imgItem, options);
                txtTitle.setText(preferInfo.name);

                String sCount = String.format(mContext.getString(R.string.OrderConfirm_Count, preferInfo.count));
                editCount.setText(sCount);

                // calc total price
                Double price = preferInfo.count * preferInfo.price;
                txtPrice.setText(Double.toString(price));
            }
            else
            {

            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return convertView;
    }

}
