package com.damytech.CommService;

import com.damytech.HttpConn.AsyncHttpClient;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.HttpConn.RequestParams;
import com.damytech.STData.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Yunsungil
 * Date: 11/21/13
 * Time: 9:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class PreferSvcMgr {

    /**
     * Get main courtesy list data
     * @param handler [in], response handler
     */
    public void GetCourtesyParentTypes(AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdGetCourtesyParentTypes;
            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public String parseCourtesyParentTypes(JSONObject jsonObject, ArrayList<STPreferListItemInfo> dataList)
    {
        String retMsg = "";
        String basePath = "";
        int retResult = STServiceData.ERR_FAIL;

        dataList.clear();
        try {
            retResult = jsonObject.getInt("SVCC_RET");
            basePath = jsonObject.getString("SVCC_BASEURL");
            if (STServiceData.ERR_SUCCESS != retResult)
            {
                retMsg = jsonObject.getString("SVCC_RETMSG");
            }
            else
            {
                // get data list of json array
                JSONArray jsonArray = jsonObject.getJSONArray("SVCC_DATA");
                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject item = jsonArray.getJSONObject(i);
                    // make one advertisement item
                    STPreferListItemInfo stInfo = new STPreferListItemInfo();
                    stInfo.id = item.getInt("Id");
                    stInfo.title = item.getString("Name");
                    stInfo.imgUrl = basePath + item.getString("Image");
                    // add main prefer list item
                    dataList.add(stInfo);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }

    /**
     * Get child list of courtesy
     * @param parentId [in], parent id
     * @param handler [in/out], response handler
     */
    public void GetCourtesyChildTypes(int parentId, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdGetCourtesyChildTypes;
            // make param
            param.put("parentid", Integer.toString(parentId));
            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public String parseCourtesyChildTypes(JSONObject jsonObject, ArrayList<STPreferListItemInfo> dataList)
    {
        String retMsg = "";
        String basePath = "";
        int retResult = STServiceData.ERR_FAIL;

        dataList.clear();
        try {
            retResult = jsonObject.getInt("SVCC_RET");
            basePath = jsonObject.getString("SVCC_BASEURL");
            if (STServiceData.ERR_SUCCESS != retResult)
            {
                retMsg = jsonObject.getString("SVCC_RETMSG");
            }
            else
            {
                // get data list of json array
                JSONArray jsonArray = jsonObject.getJSONArray("SVCC_DATA");
                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject item = jsonArray.getJSONObject(i);
                    // make one advertisement item
                    STPreferListItemInfo stInfo = new STPreferListItemInfo();
                    stInfo.id = item.getInt("Id");
                    stInfo.title = item.getString("Name");
                    // add main prefer list item
                    dataList.add(stInfo);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }

    /**
     * Get courtesy shop by keyword
     * @param token [in], access token
     * @param coup_id [in], coup_id
     * @param handler [in/out], response handler
     */
    public void RequestBuyFreeCoupon(String token, int coup_id, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdBuyFreeCoupon;
            // make param
            param.put("token", token);
            param.put("coup_id", Integer.toString(coup_id));
            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public String parseBuyFreeCoupon(JSONObject jsonObject)
    {
        String  retMsg = "";
        int retResult = STServiceData.ERR_FAIL;

        try {
            retResult = jsonObject.getInt("SVCC_RET");
            if (STServiceData.ERR_SUCCESS != retResult)
            {
                retMsg = jsonObject.getString("SVCC_RETMSG");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }

    /**
     * Get prefer shop list by sub list id
     * @param type_id [in], sub list id
     * @param height [in], image height
     * @param handler [in/out], response handler
     */
    public void GetCourtesyShopsOfType(int type_id, int height, int pageno, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdGetCourtesyShopsOfType;
            // make param
            param.put("type_id", Integer.toString(type_id));
            param.put("height", Integer.toString(height));
            param.put("pageno", Integer.toString(pageno));
            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public String parseCourtesyShopsOfType(JSONObject jsonObject, ArrayList<STPreferInfo> dataList)
    {
        String retMsg = "";
        String basePath = "";
        int retResult = STServiceData.ERR_FAIL;

        try {
            retResult = jsonObject.getInt("SVCC_RET");
            basePath = jsonObject.getString("SVCC_BASEURL");
            if (STServiceData.ERR_SUCCESS != retResult)
            {
                retMsg = jsonObject.getString("SVCC_RETMSG");
            }
            else
            {
                // get data list of json array
                JSONArray jsonArray = jsonObject.getJSONArray("SVCC_DATA");
                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject item = jsonArray.getJSONObject(i);
                    // make one advertisement item
                    STPreferInfo stInfo = new STPreferInfo();
                    stInfo.id = item.getInt("Id");
                    stInfo.coupId = item.getInt("coupId");
                    stInfo.title = item.getString("Name");
                    stInfo.phoneNum = item.getString("Phone");
                    if (item.getString("Image").contains("http://"))
                    {
                        stInfo.imgUrl = item.getString("Image");
                    }
                    else
                    {
                        stInfo.imgUrl = basePath + item.getString("Image");
                    }
                    stInfo.chip = item.getString("Price");
                    stInfo.address = item.getString("Address");
                    stInfo.date = item.getString("StartDate") + " ~ " + item.getString("EndDate");
                    // add main prefer list item
                    dataList.add(stInfo);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }

    /**
     * Get courtesy shop by keyword
     * @param latitude [in], latitude
     * @param longitude [in], longitude
     * @param height [in], image height
     * @param pageno [in], current page number
     * @param handler [in/out], response handler
     */
    public void RequestGetNearbyCourtesyShops(double latitude, double longitude, int width, int height, int pageno, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdGetNearbyCourtesyShops;
            // make param
            param.put("latitude", Double.toString(latitude));
            param.put("longitude", Double.toString(longitude));
            param.put("width", Integer.toString(width));
            param.put("height", Integer.toString(height));
            param.put("pageno", Integer.toString(pageno));
            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     * Get courtesy shop by keyword
     * @param keyword [in], keyword of shop
     * @param height [in], image height
     * @param pageno [in], current page number
     * @param handler [in/out], response handler
     */
    public void GetCourtesyShopsOfKeyWord(String keyword, int height, int pageno, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdGetCourtesyShopsOfKeyWord;
            // make param
            param.put("keyword", keyword);
            param.put("height", Integer.toString(height));
            param.put("pageno", Integer.toString(pageno));
            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
    // use default parse function

    /**
     * Get detail information of courtesy shop
     * @param uid [in], shop id
     * @param width [in], image width
     * @param handler [in/out] response handler
     */
    public void GetCourtesyShopDetail(int uid, int width, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdGetCourtesyShopDetail;
            // make param
            param.put("uid", Integer.toString(uid));
            param.put("width", Integer.toString(width));
            // call service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public String parseCourtesyShopDetail(JSONObject jsonObject, STPreferDetInfo stInfo)
    {
        String retMsg = "";
        String basePath = "";
        int retResult = STServiceData.ERR_FAIL;

        try {
            retResult = jsonObject.getInt("SVCC_RET");
            basePath = jsonObject.getString("SVCC_BASEURL");
            if (STServiceData.ERR_SUCCESS != retResult)
            {
                retMsg = jsonObject.getString("SVCC_RETMSG");
            }
            else
            {
                // get data list of json array
                JSONObject item = jsonObject.getJSONObject("SVCC_DATA");

                // make one prefer item
                stInfo.id = item.getInt("Id");
                stInfo.name = item.getString("Name");
                stInfo.address = item.getString("Address");
                stInfo.price = item.getString("Price");
                stInfo.phone = item.getString("Phone");
                stInfo.timeRange = item.getString("TimeRange");
                stInfo.latitude = item.getDouble("Latitude");
                stInfo.longitude = item.getDouble("Longitude");
                stInfo.intro = item.getString("Intro");
                stInfo.contents = item.getString("Contents");
                stInfo.coupid = item.getInt("CoupId");

                if (item.getString("Image").contains("http://"))
                {
                    stInfo.imgUrl = item.getString("Image");
                }
                else
                {
                    stInfo.imgUrl = basePath + item.getString("Image");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }

    /**
     * Get detail information of coupon
     * @param uid [in], index of shop
     * @param width [in], width of image
     * @param handler [in/out], response handler
     */
    public void GetCouponDetail(int uid, int width, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdGetCouponDetail;
            // make param
            param.put("uid", Integer.toString(uid));
            param.put("width", Integer.toString(width));
            // call service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public String parseCouponDetail(JSONObject jsonObject, STShopCoupon stInfo)
    {
        String retMsg = "";
        String basePath = "";
        int retResult = STServiceData.ERR_FAIL;

        try {
            retResult = jsonObject.getInt("SVCC_RET");
            basePath = jsonObject.getString("SVCC_BASEURL");
            if (STServiceData.ERR_SUCCESS != retResult)
            {
                retMsg = jsonObject.getString("SVCC_RETMSG");
            }
            else
            {
                // get data list of json array
                JSONObject item = jsonObject.getJSONObject("SVCC_DATA");

                // make one prefer item
                stInfo.id = item.getInt("Id");
                stInfo.name = item.getString("Name");
                stInfo.address = item.getString("Address");
                stInfo.price = item.getString("Price");
                stInfo.endDate = item.getString("EndDate");
                stInfo.rule = item.getString("Rule");

                if (item.getString("Image").contains("http://"))
                {
                    stInfo.imgUrl = item.getString("Image");
                }
                else
                {
                    stInfo.imgUrl = basePath + item.getString("Image");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }
}
