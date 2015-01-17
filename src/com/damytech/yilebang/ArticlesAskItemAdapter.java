package com.damytech.yilebang;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.damytech.STData.ProductDetail.STProdConsult;
import com.damytech.Utils.ResolutionSet;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: KimChg
 * Date: 13-11-21
 * Time: 下午11:12
 * To change this template use File | Settings | File Templates.
 */
public class ArticlesAskItemAdapter extends BaseAdapter {
    Context mContext = null;
    Activity mActivity = null;
    private LayoutInflater mInflater;

    private ArrayList<STProdConsult> mInfos = null;

    public ArticlesAskItemAdapter(Activity activity, Context context) {
        mContext = context;
        mActivity = activity;
        // Cache the LayoutInflate to avoid asking for a new one each time.
        mInflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<STProdConsult> hisData)
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
        try {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.articlesaskitem, null);
                ResolutionSet._instance.iterateChild(convertView.findViewById(R.id.rlAskItemMain));
            } else {
            }

            STProdConsult preferInfo = null;
            if (mInfos != null)
            {
                preferInfo = mInfos.get(position);
            }

            // Bind the data efficiently with the holder.
            if (preferInfo != null)
            {
                TextView txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
                TextView txtDate = (TextView) convertView.findViewById(R.id.txtDate);
                TextView txtAskContent = (TextView) convertView.findViewById(R.id.txtAskContent);
                TextView txtContents = (TextView) convertView.findViewById(R.id.txtContents);

                // set data
                txtTitle.setText(preferInfo.name);
                txtDate.setText(preferInfo.asktime);
                txtAskContent.setText(preferInfo.question);
                txtContents.setText(preferInfo.reply);
            }else
            {

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return convertView;
    }
}
