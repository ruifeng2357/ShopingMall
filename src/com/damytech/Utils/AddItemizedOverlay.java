package com.damytech.Utils;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.drawable.Drawable;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.ItemizedOverlay;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.OverlayItem;

public class AddItemizedOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> mapOverlays = new ArrayList<OverlayItem>();	 
    @SuppressWarnings("unused")
	private Context context;

    public AddItemizedOverlay(Drawable defaultMarker) {
         super(boundCenterBottom(defaultMarker));
    }

    public AddItemizedOverlay(Drawable defaultMarker, Context context) {
         this(defaultMarker);
         this.context = context;
    }

    @Override
    protected OverlayItem createItem(int i) {
       return mapOverlays.get(i);
    }

    @Override
    public int size() {
       return mapOverlays.size();
    }

    @Override
    protected boolean onTap(int index) {

//        GeoPoint geoPoint = mapOverlays.get(index).getPoint();
//        // 获取大头针的宽高,
//        int x = marker.getIntrinsicHeight();
//        int y = marker.getIntrinsicWidth();
//        /***
//         * 显示泡泡 参数是要显示的view 水平多少有些偏差，
//         *
//         * 可以自己进行调整，垂直则让显示在大头针的中间部位即可.即-y/2.
//         */
//        mapView.updateViewLayout(mPopView, new MapView.LayoutParams(
//                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, geoPoint,
//                -3, -y / 2, MapView.LayoutParams.BOTTOM_CENTER));
//        mPopView.setVisibility(View.VISIBLE);
//        mapView.getController().animateTo(geoPoint);// 移动到中间
//        TextView textView = (TextView) mPopView.getTag();
//        textView.setText(overlayItems.get(i).getSnippet());
//        mPopView.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(mContext, overlayItems.get(i).getSnippet(), 1)
//                        .show();
//
//            }
//        });
//
//        return super.onTap(i);


        return true;
    }

    public void addOverlay(OverlayItem overlay) {
       mapOverlays.add(overlay);
       this.populate();
    }

}
