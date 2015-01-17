package com.damytech.yilebang;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import com.damytech.STData.STPreferDetInfo;
import com.damytech.STData.STPreferInfo;
import com.damytech.Utils.ResolutionSet;

/**
 * Created with IntelliJ IDEA.
 * User: KimChg
 * Date: 13-11-18
 * Time: 下午12:10
 * To change this template use File | Settings | File Templates.
 */
public class PreferentialDetItemAdapter extends BaseAdapter {
    Context mContext = null;
    PreferentialDetActivity mActivity = null;
    private LayoutInflater mInflater;

    private STPreferDetInfo m_preferDetInfos[] = null;

    public PreferentialDetItemAdapter(PreferentialDetActivity activity, Context context) {
        mContext = context;
        mActivity = activity;
        // Cache the LayoutInflate to avoid asking for a new one each time.
        mInflater = LayoutInflater.from(context);
    }

    public void setData(STPreferDetInfo hisData[])
    {
        m_preferDetInfos = hisData;
    }

    /**
     * The number of items in the list is determined by the number of speeches
     * in our array.
     *
     * @see android.widget.ListAdapter#getCount()
     */
    public int getCount() {
        if (m_preferDetInfos == null)
            return 0;

        return m_preferDetInfos.length;
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
            convertView = mInflater.inflate(R.layout.preferentialdetitem, null);
            ResolutionSet._instance.iterateChild(convertView.findViewById(R.id.rlPreferDetItemMain));
        } else {
        }

        STPreferDetInfo preferInfo = null;
        if (m_preferDetInfos != null)
        {
            preferInfo = m_preferDetInfos[position];
        }

        // Bind the data efficiently with the holder.
        if (preferInfo != null)
        {

        }else
        {

        }
        return convertView;
    }
}
