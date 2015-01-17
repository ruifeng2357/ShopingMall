package com.damytech.yilebang;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import com.damytech.STData.ProductDetail.STProdComment;
import com.damytech.Utils.ResolutionSet;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: KimChg
 * Date: 13-11-21
 * Time: 下午11:12
 * To change this template use File | Settings | File Templates.
 */
public class ArticlesEvalItemAdapter extends BaseAdapter {
    Context mContext = null;
    Activity mActivity = null;
    private LayoutInflater mInflater;

    private ArrayList<STProdComment> mInfos = null;

    public ArticlesEvalItemAdapter(Activity activity, Context context) {
        mContext = context;
        mActivity = activity;
        // Cache the LayoutInflate to avoid asking for a new one each time.
        mInflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<STProdComment> hisData)
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
            convertView = mInflater.inflate(R.layout.articlesevalitem, null);
            ResolutionSet._instance.iterateChild(convertView.findViewById(R.id.rlEvaluateItemMain));
        } else {
        }

        RatingBar ratingBar = (RatingBar)convertView.findViewById(R.id.ratingBar);
        STProdComment preferInfo = null;
        if (mInfos != null)
        {
            preferInfo = mInfos.get(position);
        }

        // Bind the data efficiently with the holder.
        if (preferInfo != null)
        {
            ratingBar.setNumStars((int)preferInfo.rate);
            TextView txtName = (TextView) convertView.findViewById(R.id.txtName);
            TextView txtDate = (TextView) convertView.findViewById(R.id.txtDate);
            TextView txtDescription = (TextView) convertView.findViewById(R.id.txtDescription);
            // set data info
            txtName.setText(preferInfo.username);
            txtDate.setText(preferInfo.commenttime);
            txtDescription.setText(preferInfo.content);
        }else
        {

        }
        return convertView;
    }
}
