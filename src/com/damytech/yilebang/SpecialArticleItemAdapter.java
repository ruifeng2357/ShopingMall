package com.damytech.yilebang;

import android.app.Activity;
import android.app.ProgressDialog;
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
import com.damytech.CommService.CommMgr;
import com.damytech.Global.CommonFunc;
import com.damytech.HttpConn.JsonHttpResponseHandler;
import com.damytech.STData.STServiceData;
import com.damytech.STData.STSpecialArticleInfo;
import com.damytech.Global.GlobalData;
import com.damytech.Utils.ResolutionSet;
import com.damytech.yilebang.HuiYuanZhongXin.ShouCangActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: KimChg
 * Date: 13-11-20
 * Time: 上午9:25
 * To change this template use File | Settings | File Templates.
 */
public class SpecialArticleItemAdapter extends BaseAdapter {
    Context mContext = null;
    MyActivity mActivity = null;
    private LayoutInflater mInflater;

    private ArrayList<STSpecialArticleInfo> m_articlesInfo = null;
    private int mParentId = 0;

    DisplayImageOptions options;

    private JsonHttpResponseHandler handler;
    private JsonHttpResponseHandler handler2;
    private ProgressDialog progDialog = null;

    public SpecialArticleItemAdapter(MyActivity activity, Context context) {
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

    public void setData(ArrayList<STSpecialArticleInfo> hisData)
    {
        m_articlesInfo = hisData;
    }

    public void setParentId(int parentId)
    {
        mParentId = parentId;
    }

    /**
     * The number of items in the list is determined by the number of speeches
     * in our array.
     *
     * @see android.widget.ListAdapter#getCount()
     */
    public int getCount() {
        if (m_articlesInfo == null)
            return 0;

        return m_articlesInfo.size();
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
                convertView = mInflater.inflate(R.layout.specialarticleitem, null);
                ResolutionSet._instance.iterateChild(convertView.findViewById(R.id.rlArticlesItemMain));
            } else {
            }

            ImageView imgArticle = (ImageView)convertView.findViewById(R.id.imgArticleItem);
            TextView txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
            TextView txtValue = (TextView) convertView.findViewById(R.id.txtValue);
            TextView txtBasket = (TextView) convertView.findViewById(R.id.txtBasket);
            TextView txtShare = (TextView) convertView.findViewById(R.id.txtShare);
            txtShare.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer nVal = (Integer)v.getTag();
                    if (nVal ==null)
                        return;
                    RunBackgroundHandler(nVal.intValue());
                }
            });

            Button btnShare = (Button)convertView.findViewById(R.id.btnShare);
            btnShare.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer nVal = (Integer)v.getTag();
                    if (nVal ==null)
                        return;
                    RunBackgroundHandler(nVal.intValue());
                }
            });

            txtBasket.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer nVal = (Integer)v.getTag();
                    if (nVal ==null)
                        return;
                    callAddProductToShopCarts(nVal, 0, 0, 1, GlobalData.token);
                }
            });

            Button btnBasket = (Button)convertView.findViewById(R.id.btnBasket);
            btnBasket.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer nVal = (Integer)v.getTag();
                    if (nVal ==null)
                        return;
                    callAddProductToShopCarts(nVal, 0, 0, 1, GlobalData.token);
                }
            });


            imgArticle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try
                    {
                        Intent intent = new Intent(mActivity, ArticlesActivity.class);
                        intent.putExtra("ProductId", (Integer)v.getTag());
                        intent.putExtra("ParentId", mParentId);
                        mActivity.startActivity(intent);
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }
            });

            STSpecialArticleInfo preferInfo = null;
            if (m_articlesInfo != null)
            {
                preferInfo = m_articlesInfo.get(position);
            }

            // Bind the data efficiently with the holder.
            if (preferInfo != null)
            {
                txtTitle.setText(preferInfo.title);
                txtValue.setText(preferInfo.price.toString());
                GlobalData.imageLoader.displayImage(preferInfo.imgItemPic, imgArticle, options);
                imgArticle.setTag(preferInfo.id);
                btnShare.setTag(preferInfo.id);
                txtShare.setTag(preferInfo.id);
                txtBasket.setTag(preferInfo.id);
                btnBasket.setTag(preferInfo.id);

                if (mParentId == STServiceData.PROD_ID_1YUANQUAN)
                {
                    txtBasket.setVisibility(View.GONE);
                    btnBasket.setVisibility(View.GONE);
                }
                else
                {
                    txtBasket.setVisibility(View.VISIBLE);
                    btnBasket.setVisibility(View.VISIBLE);
                }
            }else
            {

            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }


        return convertView;
    }

    private void RunBackgroundHandler (int nProducdtId) {
        if (GlobalData.token.length() <= 0)
        {
            Intent intent = new Intent(mActivity, PersonInfoActivity.class);
            mActivity.startActivity(intent);
            return;
        }

        handler = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;
            String retMsg = "";

            @Override
            public void onSuccess(JSONObject object)
            {
                progDialog.dismiss();

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
//                    progDialog.dismiss();
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
//                    progDialog.dismiss();
                    GlobalData.showToast(mActivity, mActivity.getResources().getString(R.string.server_connection_error));
                }
                else if (result != STServiceData.ERR_SUCCESS)
                {
//                    progDialog.dismiss();
                    GlobalData.showToast(mActivity, retMsg);
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

    /**
     * Add Product to shopping cart
     * @param prodId [in], product id
     * @param gid1 [in], first spec id of product
     * @param gid2 [in], second spec id of product
     * @param count [in], count of product
     * @param token [in], user token
     */
    private void callAddProductToShopCarts(int prodId, int gid1, int gid2, int count, String token)
    {
        if (token.length() <= 0)
        {
            Intent intent = new Intent(mActivity, PersonInfoActivity.class);
            mActivity.startActivity(intent);
            return;
        }

        handler2 = new JsonHttpResponseHandler()
        {
            int result = STServiceData.ERR_FAIL;

            @Override
            public void onSuccess(JSONObject object)
            {
                String retMsg = STServiceData.MSG_SUCCESS;

                progDialog.dismiss();

                retMsg = CommMgr.commService.parseAddProductToShopCarts(object);
                if (retMsg.equals(STServiceData.MSG_SUCCESS))
                {
                    result = STServiceData.ERR_SUCCESS;
                    retMsg = mContext.getString(R.string.MSG_Success);

                    // update shop cart badge number
                    mActivity.updateCartNum();
                }
                else
                {
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
                    GlobalData.showToast(mActivity, mActivity.getString(R.string.server_connection_error));
                }

                result = 0;
            }

        };

        progDialog = ProgressDialog.show(
                mActivity,
                "",
                mActivity.getString(R.string.waiting),
                true,
                false,
                null);

        CommMgr.commService.AddProductToShopCarts(prodId, gid1, gid2, count, token, handler2);

        return;
    }
}
