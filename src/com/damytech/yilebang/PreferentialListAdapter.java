package com.damytech.yilebang;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.damytech.STData.STPreferListItemInfo;
import com.damytech.Global.GlobalData;
import com.damytech.Utils.ResolutionSet;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: KimChg
 * Date: 13-11-19
 * Time: 上午9:32
 * To change this template use File | Settings | File Templates.
 */
public class PreferentialListAdapter extends BaseAdapter {
    Context mContext = null;
    Activity mActivity = null;
    LayoutInflater mInflater;
    boolean mBShowImg = false;

    DisplayImageOptions options;

    private ArrayList<STPreferListItemInfo> m_preferInfos = null;

    public PreferentialListAdapter(Activity activity, Context context, boolean bShowImg) {

        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_stub)
                .showImageOnFail(R.drawable.ic_stub)
                .cacheInMemory()
                .cacheOnDisc()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading()
                .build();

        mContext = context;
        mActivity = activity;
        mBShowImg = bShowImg;
        // Cache the LayoutInflate to avoid asking for a new one each time.
        mInflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<STPreferListItemInfo> hisData)
    {
        m_preferInfos = hisData;
    }

    /**
     * The number of items in the list is determined by the number of speeches
     * in our array.
     *
     * @see android.widget.ListAdapter#getCount()
     */
    public int getCount() {
        if (m_preferInfos == null)
            return 0;

        return m_preferInfos.size();
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
            STPreferListItemInfo preferInfo = null;
            if (m_preferInfos != null)
            {
                preferInfo = m_preferInfos.get(position);
            }

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.preferentiallistitem, null);
                ResolutionSet._instance.iterateChild(convertView.findViewById(R.id.rlPreferListItemMain));
            } else {
            }

            ImageView imgItemMark = (ImageView)convertView.findViewById(R.id.imgBranch);
            if (mBShowImg == false)
            {
                imgItemMark.setVisibility(View.GONE);
            }
            else
            {
                imgItemMark.setVisibility(View.VISIBLE);
                GlobalData.imageLoader.displayImage(preferInfo.imgUrl, imgItemMark, options);
            }
            TextView txtView = (TextView) convertView.findViewById(R.id.txtItemName);
            txtView.setText(preferInfo.title);

            convertView.setTag(preferInfo.id);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mBShowImg == true)
                    {
                        Intent intentDetail = new Intent(mActivity, PreferentialListDetailActivity.class);
                        intentDetail.putExtra("ItemId", (Integer)v.getTag());
                        mActivity.startActivity(intentDetail);
                    }
                    else
                    {
                        Intent intentDetail = new Intent(mActivity, PreferentialListInfoActivity.class);
                        intentDetail.putExtra("SearchMode", 1);  // search by parent id
                        intentDetail.putExtra("ItemId", (Integer)v.getTag());
                        mActivity.startActivity(intentDetail);
                    }
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return convertView;
    }
}
