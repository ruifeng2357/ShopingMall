package com.damytech.yilebang;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.damytech.CommService.CommMgr;
import com.damytech.Global.CommonFunc;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.STData.STEarnMoneyInfo;
import com.damytech.Global.GlobalData;
import com.damytech.STData.STServiceData;
import com.damytech.Utils.ResolutionSet;
import com.damytech.yilebang.HuiYuanZhongXin.ShouCangActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: KimChg
 * Date: 13-11-19
 * Time: 下午10:01
 * To change this template use File | Settings | File Templates.
 */
public class EarnMoneyItemAdapter extends BaseAdapter {
    Context mContext = null;
    Activity mActivity = null;
    private LayoutInflater mInflater;

    private ArrayList<STEarnMoneyInfo> mInfos = null;

    final int DISPLAY_MAX_ITEMS = 10;
    final int START_GRAY_NUMBER = 3;

    private ProgressDialog progDialog;
    private JsonHttpResponseHandler handler;

    DisplayImageOptions options;

    public EarnMoneyItemAdapter(Activity activity, Context context) {
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

    public void setData(ArrayList<STEarnMoneyInfo> hisData)
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

        if (mInfos.size() >= DISPLAY_MAX_ITEMS)
            return DISPLAY_MAX_ITEMS;

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
            convertView = mInflater.inflate(R.layout.earnmoneyitem, null);
            ResolutionSet._instance.iterateChild(convertView.findViewById(R.id.rlEarnmoneyItem));
        } else {
        }

        STEarnMoneyInfo preferInfo = null;
        if (mInfos != null)
        {
            preferInfo = mInfos.get(position);
        }

        TextView txtItemNumber = (TextView)convertView.findViewById(R.id.txtNumber);
        txtItemNumber.setText(Integer.toString(position + 1));

        RelativeLayout rlItemNumber = (RelativeLayout)convertView.findViewById(R.id.rlImgNumber);
        if (position >= START_GRAY_NUMBER)
            rlItemNumber.setBackgroundResource(R.drawable.ticket_gray);
        else
            rlItemNumber.setBackgroundResource(R.drawable.ticket_red);

        // Bind the data efficiently with the holder.
        if (preferInfo != null)
        {
            ImageView imgItemPic = (ImageView) convertView.findViewById(R.id.imgItemPic);
            imgItemPic.setTag(preferInfo.id);
            imgItemPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try
                    {
                        Intent intent = new Intent(mActivity, ArticlesActivity.class);
                        intent.putExtra("ProductId", (Integer)v.getTag());
                        intent.putExtra("ParentId", STServiceData.PROD_ID_TECHAN);
                        mActivity.startActivity(intent);
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }
            });
            TextView txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
            TextView txtValue = (TextView) convertView.findViewById(R.id.txtValue);
            TextView txtProfit = (TextView) convertView.findViewById(R.id.txtProfit);
            Button btnSaveHis = (Button) convertView.findViewById(R.id.btnSaveHis);
            btnSaveHis.setTag(preferInfo.id);
            btnSaveHis.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer nVal = (Integer)v.getTag();
                    if (nVal == null)
                        return;
                    RunBackgroundHandler(nVal.intValue());
                }
            });

            Button btnShare = (Button) convertView.findViewById(R.id.btnShare);
            btnShare.setTag(position);
            btnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try
                    {
                        Integer pos = (Integer)v.getTag();

                        STEarnMoneyInfo preferInfo = null;
                        if (mInfos != null)
                        {
                            preferInfo = mInfos.get(pos);
                            CommonFunc.showFenXiang(mActivity.getBaseContext(), preferInfo.id, preferInfo.imgUrl);
                        }
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }
            });

            // set value
            GlobalData.imageLoader.displayImage(preferInfo.imgUrl, imgItemPic, options);
            txtTitle.setText(preferInfo.title);
            txtValue.setText(Float.toString(preferInfo.preferValue));
            txtProfit.setText(Float.toString(preferInfo.profitValue));
        }
        else
        {

        }
        return convertView;
    }

    private void RunBackgroundHandler (int nProducdtId) {
        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;
            String retMsg = "";

            @Override
            public void onSuccess(JSONObject object)
            {
                retMsg = CommMgr.commService.parseCollectProduct(object);
                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    result = STServiceData.ERR_SUCCESS;

                    Intent intent = new Intent(mActivity, ShouCangActivity.class);
                     mActivity.startActivity(intent);
                    return;
                }
                else
                {
                    progDialog.dismiss();
                    result = STServiceData.ERR_EXCEPTION;
                }

                GlobalData.showToast(mActivity, retMsg);
            }

            @Override
            public void onFailure(Throwable ex, String exception) {}

            @Override
            public void onFinish()
            {
                progDialog.dismiss();
                if (result == STServiceData.ERR_FAIL)
                {
                    GlobalData.showToast(mActivity, mActivity.getResources().getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                mActivity,
                "",
                mActivity.getResources().getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.RequestCollectProduct(GlobalData.token, nProducdtId, handler);
    }
}
