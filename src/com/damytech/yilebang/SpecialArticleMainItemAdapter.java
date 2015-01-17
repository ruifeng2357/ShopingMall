package com.damytech.yilebang;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.damytech.STData.STServiceData;
import com.damytech.STData.STSpecialArticleMainInfo;
import com.damytech.Global.GlobalData;
import com.damytech.Utils.ResolutionSet;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: KimChg
 * Date: 13-11-20
 * Time: 下午2:24
 * To change this template use File | Settings | File Templates.
 */
public class SpecialArticleMainItemAdapter extends BaseAdapter {
    Context mContext = null;
    Activity mActivity = null;
    private LayoutInflater mInflater;

    private ArrayList<STSpecialArticleMainInfo> m_articlesInfo = null;

    DisplayImageOptions options;

    public SpecialArticleMainItemAdapter(Activity activity, Context context) {
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

    public void setData(ArrayList<STSpecialArticleMainInfo> hisData)
    {
        m_articlesInfo = hisData;
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
        try {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.specialarticlemainitem, null);
                ResolutionSet._instance.iterateChild(convertView.findViewById(R.id.rlSpecialMainItem));
            } else {
            }

            STSpecialArticleMainInfo preferInfo = null;
            if (m_articlesInfo != null)
            {
                preferInfo = m_articlesInfo.get(position);
            }

            // Bind the data efficiently with the holder.
            if (preferInfo != null)
            {
                TextView txtTypeTitle = (TextView) convertView.findViewById(R.id.txtTypeTitle);
                ImageView mainImg = (ImageView) convertView.findViewById(R.id.imgItem1);
                TextView txtItem1Title = (TextView) convertView.findViewById(R.id.txtItem1Title);
                TextView txtItem1Price = (TextView) convertView.findViewById(R.id.txtItem1Price);
                ImageView image1 = (ImageView) convertView.findViewById(R.id.imgArticle1);
                ImageView image2 = (ImageView) convertView.findViewById(R.id.imgArticle2);
                ImageView image3 = (ImageView) convertView.findViewById(R.id.imgArticle3);
                ImageView image4 = (ImageView) convertView.findViewById(R.id.imgArticle4);
                ImageView image5 = (ImageView) convertView.findViewById(R.id.imgArticle5);
                ImageView image6 = (ImageView) convertView.findViewById(R.id.imgArticle6);
                ImageView image7 = (ImageView) convertView.findViewById(R.id.imgArticle7);
                ImageView image8 = (ImageView) convertView.findViewById(R.id.imgArticle8);

                // reset content
                txtTypeTitle.setText("");
                mainImg.setImageResource(R.drawable.ic_stub);
                image1.setImageResource(R.drawable.ic_stub);
                image2.setImageResource(R.drawable.ic_stub);
                image3.setImageResource(R.drawable.ic_stub);
                image4.setImageResource(R.drawable.ic_stub);
                image5.setImageResource(R.drawable.ic_stub);
                image6.setImageResource(R.drawable.ic_stub);
                image7.setImageResource(R.drawable.ic_stub);
                image8.setImageResource(R.drawable.ic_stub);

                // set service data
                mainImg.setTag(preferInfo.arrProducts.get(0).id);
                image1.setTag(preferInfo.arrProducts.get(1).id);
                image2.setTag(preferInfo.arrProducts.get(2).id);
                image3.setTag(preferInfo.arrProducts.get(3).id);
                image4.setTag(preferInfo.arrProducts.get(4).id);
                image5.setTag(preferInfo.arrProducts.get(5).id);
                image6.setTag(preferInfo.arrProducts.get(6).id);
                image7.setTag(preferInfo.arrProducts.get(7).id);
                image8.setTag(preferInfo.arrProducts.get(8).id);

                txtTypeTitle.setText(preferInfo.typeName);
                GlobalData.imageLoader.displayImage(preferInfo.arrProducts.get(0).image, mainImg, options);
                txtItem1Title.setText(preferInfo.arrProducts.get(0).name);
                txtItem1Price.setText(Double.toString(preferInfo.arrProducts.get(0).price));
                GlobalData.imageLoader.displayImage(preferInfo.arrProducts.get(1).image, image1, options);
                GlobalData.imageLoader.displayImage(preferInfo.arrProducts.get(2).image, image2, options);
                GlobalData.imageLoader.displayImage(preferInfo.arrProducts.get(3).image, image3, options);
                GlobalData.imageLoader.displayImage(preferInfo.arrProducts.get(4).image, image4, options);
                GlobalData.imageLoader.displayImage(preferInfo.arrProducts.get(5).image, image5, options);
                GlobalData.imageLoader.displayImage(preferInfo.arrProducts.get(6).image, image6, options);
                GlobalData.imageLoader.displayImage(preferInfo.arrProducts.get(7).image, image7, options);
                GlobalData.imageLoader.displayImage(preferInfo.arrProducts.get(8).image, image8, options);

                // set implementation of item clicked event
                mainImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //To change body of implemented methods use File | Settings | File Templates.
                        Integer prodId = (Integer) v.getTag();
                        gotoArticle(prodId);
                    }
                });

                image1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //To change body of implemented methods use File | Settings | File Templates.
                        Integer prodId = (Integer) v.getTag();
                        gotoArticle(prodId);
                    }
                });

                image2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //To change body of implemented methods use File | Settings | File Templates.
                        Integer prodId = (Integer) v.getTag();
                        gotoArticle(prodId);
                    }
                });

                image3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //To change body of implemented methods use File | Settings | File Templates.
                        Integer prodId = (Integer) v.getTag();
                        gotoArticle(prodId);
                    }
                });

                image4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //To change body of implemented methods use File | Settings | File Templates.
                        Integer prodId = (Integer) v.getTag();
                        gotoArticle(prodId);
                    }
                });

                image5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //To change body of implemented methods use File | Settings | File Templates.
                        Integer prodId = (Integer) v.getTag();
                        gotoArticle(prodId);
                    }
                });

                image6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //To change body of implemented methods use File | Settings | File Templates.
                        Integer prodId = (Integer) v.getTag();
                        gotoArticle(prodId);
                    }
                });

                image7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //To change body of implemented methods use File | Settings | File Templates.
                        Integer prodId = (Integer) v.getTag();
                        gotoArticle(prodId);
                    }
                });

                image8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //To change body of implemented methods use File | Settings | File Templates.
                        Integer prodId = (Integer) v.getTag();
                        gotoArticle(prodId);
                    }
                });

            }else
            {

            }
        } catch (Exception ex) {
            ex.printStackTrace();

            convertView = mInflater.inflate(R.layout.specialarticlemainitem, null);
            ResolutionSet._instance.iterateChild(convertView.findViewById(R.id.rlSpecialMainItem));
        }

        return convertView;
    }

    private void gotoArticle(int prodId)
    {
        Intent intent = new Intent(mActivity, ArticlesActivity.class);
        intent.putExtra("ProductId", prodId);
        intent.putExtra("ParentId", STServiceData.PROD_ID_TECHAN);
        mActivity.startActivity(intent);
    }
}
