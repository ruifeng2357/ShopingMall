package com.damytech.yilebang.HuiYuanZhongXin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.damytech.STData.STCheckedStatus;
import com.damytech.STData.STReceiverInfo;
import com.damytech.Utils.ResolutionSet;
import com.damytech.yilebang.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-28
 * Time: 下午4:08
 * To change this template use File | Settings | File Templates.
 */
public class ReceiverListAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    DisplayImageOptions options;
    Context mContext = null;
    Activity mActivity = null;

    ArrayList<STReceiverInfo> m_arrReceivers = null;
    ArrayList<STCheckedStatus>  m_arrCheckedItems = null;

    public ReceiverListAdapter(Activity activity, Context context) {

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

    public void setData(ArrayList<STReceiverInfo> arrReceivers)
    {
        if ( arrReceivers != null ) {
            m_arrReceivers = arrReceivers;
        }

        if ( m_arrReceivers == null )
            return;

        int  count = m_arrReceivers.size();

        if ( m_arrCheckedItems == null ) {
            m_arrCheckedItems = new ArrayList<STCheckedStatus>(count);
        } else {
            m_arrCheckedItems.clear();
        }

        for ( int i=0; i<count; i++ ) {
            STCheckedStatus  aNewStatus = new STCheckedStatus();

            aNewStatus.id = m_arrReceivers.get(i).uid;
            aNewStatus.checked = false;

            m_arrCheckedItems.add(aNewStatus);
        }
    }

    @Override
    public void notifyDataSetChanged() {
        setData(null);

        super.notifyDataSetChanged();
    }

    /**
     * Get a string that represents selected items in JSON format.
     *
     * @return if there are no selected items, returns an empty string.
     *         ,or returns a json array. i.e.: [{'id':1},{'id':5},...]
     */
    public String getSelectedIds () {
        String  selIds = "[";

        for ( int i=0; i<m_arrCheckedItems.size(); i++ ) {
            STCheckedStatus  anItem = m_arrCheckedItems.get(i);

            if ( anItem.checked == true ) {
                if ( selIds.equals("[") ) { // if it's the first item checked
                    selIds = selIds + "{'id':" + anItem.id + "}";
                } else {
                    selIds = selIds + ",{'id':" + anItem.id + "}";
                }
            }
        }

        if ( selIds.equals("[") ) {
            selIds = "";
        } else {
            selIds = selIds + "]";
        }

        return selIds;
    }

    /**
     * The number of items in the list is determined by the number of speeches
     * in our array.
     *
     * @see android.widget.ListAdapter#getCount()
     */
    public int getCount() {
        if (m_arrReceivers == null)
            return 0;

        return m_arrReceivers.size();
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
            STReceiverInfo  anItem = null;
            if (m_arrReceivers != null)
            {
                anItem = m_arrReceivers.get(position);
            }

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.huiyuanzhongxin_dizhiguanli_item, null);
                ResolutionSet._instance.iterateChild(convertView.findViewById(R.id.rlItemMain));
            } else {
            }

            CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.chkSelector);
            checkBox.setTag(position);
            checkBox.setChecked(m_arrCheckedItems.get(position).checked);
            checkBox.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int pos = (Integer) buttonView.getTag();
                    m_arrCheckedItems.get(pos).checked = isChecked;
                }
            });

            TextView txtView = (TextView) convertView.findViewById(R.id.txtReceiverName);
            txtView.setText(anItem.name);

            txtView = (TextView) convertView.findViewById(R.id.txtReceiverPhone);
            txtView.setText(anItem.phone);

            txtView = (TextView) convertView.findViewById(R.id.txtReceiverAddress);
            txtView.setText("              "+anItem.province+" "+anItem.city+" "+anItem.area);

            convertView.setTag(position);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try
                    {
                        int  pos = (Integer)v.getTag();
                        STReceiverInfo  aReceiver = m_arrReceivers.get(pos);

                        Intent intent = new Intent(mActivity, DiZhiGuanLi_EditActivity.class);
                        intent.putExtra("create_flag", false);
                        intent.putExtra("receiver_info", aReceiver);

                        mActivity.startActivity(intent);
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
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
