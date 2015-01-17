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
import com.damytech.STData.STConsultingInfo;
import com.damytech.Utils.ResolutionSet;
import com.damytech.yilebang.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-28
 * Time: 下午3:25
 * To change this template use File | Settings | File Templates.
 */
public class ConsultingListAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    DisplayImageOptions options;
    Context mContext = null;
    Activity mActivity = null;

    ArrayList<STConsultingInfo>  m_arrConsultings = null;

    public ConsultingListAdapter(Activity activity, Context context) {

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

    public void setData(ArrayList<STConsultingInfo> arrConsultings)
    {
        m_arrConsultings = arrConsultings;
    }

    /**
     * The number of items in the list is determined by the number of speeches
     * in our array.
     *
     * @see android.widget.ListAdapter#getCount()
     */
    public int getCount() {
        if (m_arrConsultings == null)
            return 0;

        return m_arrConsultings.size();
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
            STConsultingInfo  anItem = null;
            if (m_arrConsultings != null)
            {
                anItem = m_arrConsultings.get(position);
            }

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.huiyuanzhongxin_pinglun_zixun_item, null);
                ResolutionSet._instance.iterateChild(convertView.findViewById(R.id.rlItemMain));
            } else {
            }

            ImageView imageView = (ImageView) convertView.findViewById(R.id.imgProduct);
            GlobalData.imageLoader.displayImage(anItem.image, imageView, options);

            TextView txtView = (TextView) convertView.findViewById(R.id.txtProductDesc);
            txtView.setText(anItem.name);

            txtView = (TextView) convertView.findViewById(R.id.txtProductCost);
            txtView.setText(String.format(mContext.getString(R.string.HuiYuanZhongXin_DingDan_ProductCost_Fmt), anItem.price));

            txtView = (TextView) convertView.findViewById(R.id.txtConsultingContent);
            txtView.setText(anItem.query);

            txtView = (TextView) convertView.findViewById(R.id.txtConsultingTime);
            txtView.setText(anItem.queryTime);

            txtView = (TextView) convertView.findViewById(R.id.txtYilebangReply);
            txtView.setText("                           "+anItem.response);

            txtView = (TextView) convertView.findViewById(R.id.txtReplyTime);
            txtView.setText(anItem.respTime);

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
