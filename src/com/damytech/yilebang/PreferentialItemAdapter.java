package com.damytech.yilebang;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.damytech.STData.STPreferInfo;
import com.damytech.Global.GlobalData;
import com.damytech.Utils.ResolutionSet;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * Company: D.M.Y
 * Date: 13-11-15
 * Time: 下午10:30
 * To change this template use File | Settings | File Templates.
 */
public class PreferentialItemAdapter extends BaseAdapter {
    Context mContext = null;
    Activity mActivity = null;
    private LayoutInflater mInflater;

    DisplayImageOptions options;

    private ArrayList<STPreferInfo> m_preferInfos = new ArrayList<STPreferInfo>(0);

    public PreferentialItemAdapter(Activity activity, Context context) {

        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_stub)
                .showImageOnFail(R.drawable.ic_stub)
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

    public void setData(ArrayList<STPreferInfo> hisData)
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
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.preferentialitem, null);
                ResolutionSet._instance.iterateChild(convertView.findViewById(R.id.rlPreferItemMain));
            } else {
            }

            convertView.setTag(m_preferInfos.get(position).id);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentDetail = new Intent(mActivity, PreferentialDetActivity.class);
                    intentDetail.putExtra("ItemId", (Integer)v.getTag());
                    mActivity.startActivity(intentDetail);
                }
            });

            Button btnFreeGet = (Button)convertView.findViewById(R.id.btnFreeBuy);
            btnFreeGet.setTag(m_preferInfos.get(position).id);
            btnFreeGet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentDetail = new Intent(mActivity, PreferentialInfoActivity.class);
                    intentDetail.putExtra("ItemId", (Integer)v.getTag());
                    mActivity.startActivity(intentDetail);
                }
            });

            STPreferInfo preferInfo = null;
            if (m_preferInfos != null)
            {
                preferInfo = m_preferInfos.get(position);
            }

            // Bind the data efficiently with the holder.
            if (preferInfo != null)
            {
                if (preferInfo.coupId <= 0)
                {
                    btnFreeGet.setVisibility(View.GONE);
                }
                else
                {
                    btnFreeGet.setVisibility(View.VISIBLE);
                }

                // initialize controls
                TextView txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
                TextView txtPhoneNum = (TextView) convertView.findViewById(R.id.txtPhoneNum);
                TextView txtChip = (TextView) convertView.findViewById(R.id.txtChip);
                TextView txtDate = (TextView) convertView.findViewById(R.id.txtDate);
                TextView txtAddress = (TextView) convertView.findViewById(R.id.txtAddress);
                ImageView imgItemPic = (ImageView)convertView.findViewById(R.id.imgItemPic);

                txtTitle.setText(preferInfo.title);
                txtPhoneNum.setText(preferInfo.phoneNum);
                txtChip.setText(preferInfo.chip);
                txtDate.setText(preferInfo.date);
                txtAddress.setText(preferInfo.address);
                GlobalData.imageLoader.displayImage(preferInfo.imgUrl, imgItemPic, options);
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
