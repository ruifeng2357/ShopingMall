package com.damytech.yilebang.HuiYuanZhongXin;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.*;
import com.damytech.CommService.CommMgr;
import com.damytech.Global.GlobalData;
import com.damytech.STData.STCheckedStatus;
import com.damytech.STData.STNewsInfo;
import com.damytech.Utils.ResolutionSet;
import com.damytech.yilebang.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-28
 * Time: 上午10:18
 * To change this template use File | Settings | File Templates.
 */
public class NewsListAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    DisplayImageOptions options;
    Context mContext = null;
    Activity mActivity = null;

    ArrayList<STNewsInfo> m_arrNews = null;
    ArrayList<STCheckedStatus>  m_arrCheckedItems = null;

    public NewsListAdapter(Activity activity, Context context) {

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

    public void setData(ArrayList<STNewsInfo> arrNews)
    {
        int  count = arrNews.size();

        m_arrNews = arrNews;

        m_arrCheckedItems = new ArrayList<STCheckedStatus>(count);

        for ( int i=0; i<count; i++ ) {
            STCheckedStatus  aNewStatus = new STCheckedStatus();

            aNewStatus.id = m_arrNews.get(i).uid;
            aNewStatus.checked = false;

            m_arrCheckedItems.add(aNewStatus);
        }
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
            STCheckedStatus anItem = m_arrCheckedItems.get(i);

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
        if (m_arrNews == null)
            return 0;

        return m_arrNews.size();
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
            STNewsInfo  anItem = null;
            if (m_arrNews != null)
            {
                anItem = m_arrNews.get(position);
            }

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.huiyuanzhongxin_xiaoxi_item, null);
                ResolutionSet._instance.iterateChild(convertView.findViewById(R.id.rlItemMain));
            } else {
            }

            CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.chkSelector);
            checkBox.setTag(position);
            checkBox.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int pos = (Integer) buttonView.getTag();
                    m_arrCheckedItems.get(pos).checked = isChecked;
                }
            });

            TextView txtView = (TextView) convertView.findViewById(R.id.txtSubject);
            txtView.setText(anItem.title);

            txtView = (TextView) convertView.findViewById(R.id.txtTime);
            txtView.setText(anItem.addTime);

            WebView  webView = (WebView) convertView.findViewById(R.id.webContents);
            webView.loadData(anItem.contents, "text/html", "utf-8");

            ImageView imgMoreDetail = (ImageView) convertView.findViewById(R.id.imgMoreDetail);
            imgMoreDetail.setTag(webView);
            webView.setTag(anItem.uid*10+0); // uid + already opened flag
            imgMoreDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView  imgDetail = (ImageView) v;
                    WebView webView1 = (WebView) v.getTag();
                    int tmp = (Integer)webView1.getTag();
                    int newsId = (int)(tmp/10.0);
                    int checked = tmp-newsId*10;

                    RelativeLayout.LayoutParams rlLayout = (RelativeLayout.LayoutParams) webView1.getLayoutParams();

                    if (rlLayout.height == 0) {
                        if (checked == 0) {
                            webView1.setTag(newsId*10+1); // set flag as opened

                            CommMgr.commService.SetNewsReadState(GlobalData.token, newsId, null);
                        }
                        rlLayout.height = -2;
                        imgDetail.setImageResource(R.drawable.directionicon_down);
                    } else {
                        rlLayout.height = 0;
                        imgDetail.setImageResource(R.drawable.directionicon);
                    }

                    webView1.setLayoutParams(rlLayout);
                }
            });

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
