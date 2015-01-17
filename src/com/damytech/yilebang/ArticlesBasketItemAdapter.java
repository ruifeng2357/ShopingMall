package com.damytech.yilebang;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.*;
import com.damytech.CommService.CommMgr;
import com.damytech.Global.GlobalData;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.STData.STServiceData;
import com.damytech.STData.ShoppingCart.STBasketItemInfo;
import com.damytech.Utils.ResolutionSet;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: KimChg
 * Date: 13-11-22
 * Time: 下午3:50
 * To change this template use File | Settings | File Templates.
 */
public class ArticlesBasketItemAdapter extends BaseAdapter {
    Context mContext = null;
    Activity mActivity = null;
    private LayoutInflater mInflater;

    DisplayImageOptions options;

    private ArrayList<STBasketItemInfo> mInfos = null;

    private JsonHttpResponseHandler handler;
    private ProgressDialog progDialog;


    public ArticlesBasketItemAdapter(Activity activity, Context context) {
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
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.articlesbasketitem, null);
            ResolutionSet._instance.iterateChild(convertView.findViewById(R.id.rlBasketItemMain));
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
            CheckBox chkSelectItem = (CheckBox) convertView.findViewById(R.id.chkSelectItem);
            chkSelectItem.setTag(position);
            chkSelectItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Integer pos = (Integer) buttonView.getTag();
                    mInfos.get(pos).checked = isChecked;
                }
            });

            ImageView btnDelete = (ImageView) convertView.findViewById(R.id.imgDelete);
            btnDelete.setTag(position);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try
                    {
                        Integer pos = (Integer) v.getTag();
                        // call delete service
                        ArrayList<STBasketItemInfo> arrDel = new ArrayList<STBasketItemInfo>(0);
                        arrDel.add(mInfos.get(pos));
                        // show edit count dialog
                        ArticlesBasketActivity act = (ArticlesBasketActivity)(mActivity);
                        act.callDeleteShopCartsProducts(arrDel, GlobalData.token);
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }
            });

            ImageView imgItem = (ImageView) convertView.findViewById(R.id.imgItem);
            TextView txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
            TextView editCount = (TextView) convertView.findViewById(R.id.editCount);
            editCount.setTag(position);
            editCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer pos = (Integer)v.getTag();
                    STBasketItemInfo data = mInfos.get(pos);
                    // show edit count dialog
                    ArticlesBasketActivity act = (ArticlesBasketActivity)(mActivity);
                    act.ShowEditdlg(data);
                }
            });
            TextView txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);

            chkSelectItem.setChecked(preferInfo.checked);
            GlobalData.imageLoader.displayImage(preferInfo.image, imgItem, options);
            txtTitle.setText(preferInfo.name);
            editCount.setText(Integer.toString(preferInfo.count));

            // set product total price
            Double price = preferInfo.price * preferInfo.count;
            txtPrice.setText(Double.toString(price));
        }
        else
        {

        }
        return convertView;
    }

}
