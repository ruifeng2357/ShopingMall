package com.damytech.CommService;

import com.damytech.Global.GlobalData;
import com.damytech.HttpConn.AsyncHttpClient;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.HttpConn.RequestParams;
import com.damytech.STData.*;
import com.damytech.STData.ShoppingCart.STBasketItemInfo;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-25
 * Time: 上午11:47
 * To change this template use File | Settings | File Templates.
 */
public class AccountSvcMgr {
    public AccountSvcMgr()
    {
    }

    /**
     * Get member basic value set
     * @param token [in], user access token
     * @param handler [in/out], response handler
     */
    public void GetMemberBasicValues(String token, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdGetMemberBasicValues;
            // make param
            param.put("token", token);
            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String parseMemberBasicValues(JSONObject jsonObject, STMemberBasicValue memInfo)
    {
        String  retMsg = "";
        int retResult = STServiceData.ERR_FAIL;

        try {
            retResult = jsonObject.getInt("SVCC_RET");
            if (STServiceData.ERR_SUCCESS != retResult)
            {
                retMsg = jsonObject.getString("SVCC_RETMSG");
            } else {
                JSONObject jsonObj = jsonObject.getJSONObject("SVCC_DATA");

                String szBaseUrl = jsonObject.getString("SVCC_BASEURL");

                memInfo.name = jsonObj.getString("Name");
                memInfo.image = szBaseUrl + jsonObj.getString("Image");
                memInfo.bangbi = jsonObj.getDouble("BangBiValue");
                memInfo.unpaid_cnt = jsonObj.getInt("UnpaidCount");
                memInfo.unreadnews_cnt = jsonObj.getInt("UnreadNewsCount");
                memInfo.coupon_cnt = jsonObj.getInt("CouponCount");
                memInfo.favorite_cnt = jsonObj.getInt("FavouriteCount");

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }

    /**
     * Call GetBangbiValue Service
     * @param token [in], user access token
     * @param handler [in/out], response handler
     */
    public void GetBangbiValue(String token, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdGetBangbiValue;
            // make param
            param.put("token", token);
            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String parseGetBangbiValue(JSONObject jsonObject, STDouble bangbi)
    {
        String  retMsg = "";
        int retResult = STServiceData.ERR_FAIL;

        try {
            retResult = jsonObject.getInt("SVCC_RET");
            if (STServiceData.ERR_SUCCESS != retResult)
            {
                retMsg = jsonObject.getString("SVCC_RETMSG");
            } else {
                JSONObject jsonObj = jsonObject.getJSONObject("SVCC_DATA");

                bangbi.fVal = jsonObj.getDouble("BangbiValue");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }

    /**
     * Call GetMemberInfo Service
     * @param token [in], user access token
     * @param handler [in/out], response handler
     */
    public void RequestGetMemberInfo(String token, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdGetMemberInfo;
            // make param
            param.put("token", token);
            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String parseGetMemberInfo(JSONObject jsonObject, STGetMemberInfo stGetMemberInfo)
    {
        String  retMsg = "";
        int retResult = STServiceData.ERR_FAIL;

        try {
            retResult = jsonObject.getInt("SVCC_RET");
            if (STServiceData.ERR_SUCCESS != retResult)
            {
                retMsg = jsonObject.getString("SVCC_RETMSG");
            }
			else
			{
                JSONObject jsonObj = jsonObject.getJSONObject("SVCC_DATA");
				String szBaseUrl = jsonObject.getString("SVCC_BASEURL");

                stGetMemberInfo.Name = jsonObj.getString("Name");
                stGetMemberInfo.Image = szBaseUrl + jsonObj.getString("Image");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }

    /**
     * Call GetUnpaiedCount Service
     * @param token [in], user access token
     * @param handler [in/out], response handler
     */
    public void GetUnpaiedCount(String token, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdGetUnpaiedCount;
            // make param
            param.put("token", token);
            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String parseGetUnpaiedCount(JSONObject jsonObject, STInteger count)
    {
        String  retMsg = "";
        int retResult = STServiceData.ERR_FAIL;

        try {
            retResult = jsonObject.getInt("SVCC_RET");
            if (STServiceData.ERR_SUCCESS != retResult)
            {
                retMsg = jsonObject.getString("SVCC_RETMSG");
            } else {
                JSONObject jsonObj = jsonObject.getJSONObject("SVCC_DATA");

                count.nVal = jsonObj.getInt("Count");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }

    /**
     * Call GetUnreadNewsCount Service
     * @param token [in], user access token
     * @param handler [in/out], response handler
     */
    public void GetUnreadNewsCount(String token, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdGetUnreadNewsCount;
            // make param
            param.put("token", token);
            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String parseGetUnreadNewsCount(JSONObject jsonObject, STInteger count)
    {
        String  retMsg = "";
        int retResult = STServiceData.ERR_FAIL;

        try {
            retResult = jsonObject.getInt("SVCC_RET");
            if (STServiceData.ERR_SUCCESS != retResult)
            {
                retMsg = jsonObject.getString("SVCC_RETMSG");
            } else {
                JSONObject jsonObj = jsonObject.getJSONObject("SVCC_DATA");

                count.nVal = jsonObj.getInt("Count");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }

    /**
     * Call GetUnreadNewsCount Service
     * @param token [in], user access token
     * @param handler [in/out], response handler
     */
    public void RequestGetLatestNews(String token, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdGetLatestNews;
            // make param
            param.put("token", token);
            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String parseGetLatestNews(JSONObject jsonObject, STNewsInfo newsInfo, STInteger stCount)
    {
        String  retMsg = "";
        int retResult = STServiceData.ERR_FAIL;

        try {
            retResult = jsonObject.getInt("SVCC_RET");
            if (STServiceData.ERR_SUCCESS != retResult)
            {
                retMsg = jsonObject.getString("SVCC_RETMSG");
            } else {
                JSONObject jsonObj = jsonObject.getJSONObject("SVCC_DATA");

                JSONObject realJson = jsonObj.getJSONObject("News");
                newsInfo.uid = realJson.getInt("Uid");
                newsInfo.title = realJson.getString("Title");
                newsInfo.addTime = realJson.getString("AddTime");
                newsInfo.contents = realJson.getString("Contents");

                stCount.nVal = jsonObj.getInt("Count");

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }

    /**
     * Call GetCouponCount Service
     * @param token [in], user access token
     * @param handler [in/out], response handler
     */
    public void GetCouponCount(String token, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdGetCouponCount;
            // make param
            param.put("token", token);
            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String parseGetCouponCount(JSONObject jsonObject, STInteger count)
    {
        String  retMsg = "";
        int retResult = STServiceData.ERR_FAIL;

        try {
            retResult = jsonObject.getInt("SVCC_RET");
            if (STServiceData.ERR_SUCCESS != retResult)
            {
                retMsg = jsonObject.getString("SVCC_RETMSG");
            } else {
                JSONObject jsonObj = jsonObject.getJSONObject("SVCC_DATA");

                count.nVal = jsonObj.getInt("Count");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }

    /**
     * Call GetCollectionCount Service
     * @param token [in], user access token
     * @param handler [in/out], response handler
     */
    public void GetFavoriteCount(String token, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdGetCollectionCount;
            // make param
            param.put("token", token);
            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String parseGetFavoriteCount(JSONObject jsonObject, STInteger count)
    {
        String  retMsg = "";
        int retResult = STServiceData.ERR_FAIL;

        try {
            retResult = jsonObject.getInt("SVCC_RET");
            if (STServiceData.ERR_SUCCESS != retResult)
            {
                retMsg = jsonObject.getString("SVCC_RETMSG");
            } else {
                JSONObject jsonObj = jsonObject.getJSONObject("SVCC_DATA");

                count.nVal = jsonObj.getInt("Count");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }

    /**
     * Call RequestCollectProduct Service
     * @param token [in], user access token
     * @param pid [in/out], product id
     */

    public void RequestCollectProduct(String token, int pid, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdCollectProduct;
            // make param
            param.put("token", token);
            param.put("pid", ""+pid);
            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String parseCollectProduct(JSONObject jsonObject)
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
     * Call GetCollectionProducts Service
     * @param token [in], user access token
     * @param width [in], the width of the image to be returned.
     * @param height [in], the height of the image to be returned.
     * @param pageno [in]
     * @param handler [in/out], response handler
     */
    public void GetCollectionProducts(String token, int width, int height, int pageno, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            url = STServiceData.serviceAddr + STServiceData.cmdGetCollectionProducts;
            // make service url
            // make param
            param.put("token", token);
            param.put("width", ""+width);
            param.put("height", ""+height);
            param.put("pageno", ""+pageno);
            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String parseGetFavoriteItems(JSONObject jsonObject, ArrayList<STFavoriteInfo> arrFavoriteItems)
    {
        String  retMsg = "";
        String basePath = "";
        int retResult = STServiceData.ERR_FAIL;

        try {
            retResult = jsonObject.getInt("SVCC_RET");
            basePath = jsonObject.getString("SVCC_BASEURL");
            if (STServiceData.ERR_SUCCESS != retResult)
            {
                retMsg = jsonObject.getString("SVCC_RETMSG");
            } else {
                JSONArray jsonArray = jsonObject.getJSONArray("SVCC_DATA");

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject item = jsonArray.getJSONObject(i);
                    // make one advertisement item
                    STFavoriteInfo stInfo = new STFavoriteInfo();
                    stInfo.uid = item.getInt("Id");
                    stInfo.name = item.getString("Name");
                    if (item.getString("Image").contains("http://"))
                    {
                        stInfo.url = item.getString("Image");
                    }
                    else
                    {
                        stInfo.url = basePath + item.getString("Image");
                    }
                    stInfo.price = item.getDouble("Price");

                    // add to the favorite list.
                    arrFavoriteItems.add(stInfo);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }

    /**
     * Call ResetPassword Service
     * @param token [in], user access token
     * @param verifkey [in]
     * @param phone [in]
     * @param password [in]
     * @param handler [in/out], response handler
     */
    public void ResetPassword (String token, String verifkey, String phone, String password, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdResetPassword;
            // make param
            param.put("token", token);
            param.put("verifkey", verifkey);
            param.put("phone", phone);
            param.put("password", password);
            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String parseResetPassword(JSONObject jsonObject)
    {
        String  retMsg = "";
        int retResult = STServiceData.ERR_FAIL;

        try {
            retResult = jsonObject.getInt("SVCC_RET");
            if (STServiceData.ERR_SUCCESS != retResult)
            {
                retMsg = jsonObject.getString("SVCC_RETMSG");
            } else {

                JSONObject item = jsonObject.getJSONObject("SVCC_DATA");
                GlobalData.token = item.getString("token");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }

    /**
     * Call GetLPCardList Service
     * @param token [in], user access token
     * @param pageno [in]
     * @param handler [in/out], response handler
     */
    public void GetLPCardList(String token, int pageno, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdGetLPCardList;
            // make param
            param.put("token", token);
            param.put("pageno", ""+pageno);
            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String parseGetLPCardList(JSONObject jsonObject, ArrayList<STGiftCardInfo> arrGiftCards)
    {
        String  retMsg = "";
        int retResult = STServiceData.ERR_FAIL;

        try {
            retResult = jsonObject.getInt("SVCC_RET");
            if (STServiceData.ERR_SUCCESS != retResult)
            {
                retMsg = jsonObject.getString("SVCC_RETMSG");
            } else {
                JSONArray jsonArray = jsonObject.getJSONArray("SVCC_DATA");

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject item = jsonArray.getJSONObject(i);
                    // make one advertisement item
                    STGiftCardInfo stInfo = new STGiftCardInfo();
                    stInfo.name = item.getString("Name");
                    stInfo.orgPrice = item.getDouble("OrgPrice");
                    stInfo.remPrice = item.getDouble("RemPrice");

                    // add to the favorite list.
                    arrGiftCards.add(stInfo);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }

    /**
     * Call BindPresentCard Service
     * @param token [in], user access token
     * @param cardno [in]
     * @param pwd [in]
     * @param phone [in]
     * @param verifkey [in]
     * @param handler [in/out], response handler
     */
    public void BindPresentCard (String token, String cardno, String pwd, String phone, String verifkey, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdBindPresentCard;

            // make param
            param.put("token", token);
            param.put("cardno", cardno);
            param.put("pwd", pwd);
            param.put("phone", phone);
            param.put("verifkey", verifkey);

            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String parseBindPresentCard(JSONObject jsonObject)
    {
        String  retMsg = "";
        int retResult = STServiceData.ERR_FAIL;

        try {
            retResult = jsonObject.getInt("SVCC_RET");
            if (STServiceData.ERR_SUCCESS != retResult)
            {
                retMsg = jsonObject.getString("SVCC_RETMSG");
            } else {
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }

    /**
     * Call GetOrderInfoOfKeyword Service
     * @param token [in], user access token
     * @param keyword [in]
     * @param pageno [in]
     * @param handler [in/out], response handler
     */
    public void GetOrderInfoOfKeyword (String token, String keyword, int pageno, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdGetOrderInfoOfKeyword;

            // make param
            param.put("token", token);
            param.put("keyword", keyword);
            param.put("pageno", ""+pageno);

            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String parseGetOrderInfoOfKeyword(JSONObject jsonObject, ArrayList<STOrderByKeyword> arrOrderByKeyword)
    {
        String  retMsg = "";
        int retResult = STServiceData.ERR_FAIL;

        try {
            retResult = jsonObject.getInt("SVCC_RET");
            if (STServiceData.ERR_SUCCESS != retResult)
            {
                retMsg = jsonObject.getString("SVCC_RETMSG");
            } else {
                JSONArray jsonArray = jsonObject.getJSONArray("SVCC_DATA");
                String szBaseUrl = jsonObject.getString("SVCC_BASEURL");

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject item = jsonArray.getJSONObject(i);
                    // make one advertisement item
                    STOrderByKeyword stInfo = new STOrderByKeyword();
                    stInfo.stateID = item.getInt("StateID");
                    stInfo.orderNo = item.getString("OrderNo");
                    stInfo.orderTime = item.getString("OrderTime");
                    stInfo.payTime = item.getString("PayTime");
                    stInfo.sndTime = item.getString("SndTime");
                    stInfo.rcvTime = item.getString("RcvTime");
                    stInfo.cancelTime = item.getString("CancelTime");
                    stInfo.deliverType = item.getString("DeliverType");
                    stInfo.payType1 = item.getInt("PayType1");
                    stInfo.payType2 = item.getInt("PayType2");
                    stInfo.comment = item.getString("Comment"); // have a value only when the 'SndTime' exists.

                    JSONArray  jsonArrayProduct = item.getJSONArray("Products");
                    for ( int j=0; j<jsonArrayProduct.length(); j++ ) {
                        JSONObject aProduct = jsonArrayProduct.getJSONObject(j);
                        STProductA  stProductA = new STProductA();

                        stProductA.uid = aProduct.getInt("Uid");
                        stProductA.name = aProduct.getString("Name");
                        stProductA.count = aProduct.getInt("Count");
                        stProductA.price = aProduct.getDouble("Price");
                        stProductA.image = szBaseUrl + aProduct.getString("Image");

                        stInfo.arrProducts.add(stProductA);
                    }

                    // add to the order list.
                    arrOrderByKeyword.add(stInfo);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }

    /**
     * Call GetOrderInfoOfState Service
     * @param token [in], user access token
     * @param state [in]
     * @param pageno [in]
     * @param handler [in/out], response handler
     */
    public void GetOrderInfoOfState (String token, int state, int pageno, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdGetOrderInfoOfState;

            // make param
            param.put("token", token);
            param.put("state", ""+state);
            param.put("pageno", ""+pageno);

            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String parseGetOrderInfoOfState(JSONObject jsonObject, ArrayList<STOrderByState> arrOrderByState)
    {
        String  retMsg = "";
        int retResult = STServiceData.ERR_FAIL;

        try {
            retResult = jsonObject.getInt("SVCC_RET");
            if (STServiceData.ERR_SUCCESS != retResult)
            {
                retMsg = jsonObject.getString("SVCC_RETMSG");
            } else {
                JSONArray jsonArray = jsonObject.getJSONArray("SVCC_DATA");

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject item = jsonArray.getJSONObject(i);
                    String szBaseUrl = jsonObject.getString("SVCC_BASEURL");

                    // make one advertisement item
                    STOrderByState stInfo = new STOrderByState();
                    stInfo.orderNo = item.getString("OrderNo");
                    stInfo.orderTime = item.getString("OrderTime");
                    stInfo.deliverType = item.getString("DeliverType");

                    JSONArray  jsonArrayProduct = item.getJSONArray("Products");
                    for ( int j=0; j<jsonArrayProduct.length(); j++ ) {
                        JSONObject aProduct = jsonArrayProduct.getJSONObject(j);
                        STProductA  stProductA = new STProductA();

                        stProductA.uid = aProduct.getInt("Uid");
                        stProductA.name = aProduct.getString("Name");
                        stProductA.count = aProduct.getInt("Count");
                        stProductA.price = aProduct.getDouble("Price");
                        stProductA.image = szBaseUrl + aProduct.getString("Image");

                        stInfo.arrProducts.add(stProductA);
                    }

                    // add to the order list.
                    arrOrderByState.add(stInfo);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }

    /**
     * Call GetCouponLogList Service
     * @param token [in], user access token
     * @param width [in]
     * @param height [in]
     * @param pageno [in]
     * @param handler [in/out], response handler
     */
    public void GetCouponLogList (String token, int width, int height, int pageno, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdGetCouponLogList;

            // make param
            param.put("token", token);
            param.put("width", ""+width);
            param.put("height", ""+height);
            param.put("pageno", ""+pageno);

            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String parseGetCouponLogList(JSONObject jsonObject, ArrayList<STCouponA> arrCoupons)
    {
        String  retMsg = "";
        int retResult = STServiceData.ERR_FAIL;

        try {
            retResult = jsonObject.getInt("SVCC_RET");
            if (STServiceData.ERR_SUCCESS != retResult)
            {
                retMsg = jsonObject.getString("SVCC_RETMSG");
            } else {
                JSONArray jsonArray = jsonObject.getJSONArray("SVCC_DATA");
                String szBaseUrl = jsonObject.getString("SVCC_BASEURL");

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject item = jsonArray.getJSONObject(i);

                    // make one advertisement item
                    STCouponA stInfo = new STCouponA();
                    stInfo.name = item.getString("Name");
                    stInfo.price = item.getString("Price");
                    stInfo.isUsed = item.getInt("IsUsed");
                    stInfo.usedDate = item.getString("UsedDate");
                    stInfo.endDate = item.getString("EndDate");
                    stInfo.image = szBaseUrl + item.getString("Image");

                    // add to the coupon list.
                    arrCoupons.add(stInfo);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }

    /**
     * Call GetNewsInfo Service
     * @param token [in], user access token
     * @param pageno [in]
     * @param handler [in/out], response handler
     */
    public void GetNewsInfo (String token, int pageno, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdGetNewsInfo;

            // make param
            param.put("token", token);
            param.put("pageno", ""+pageno);

            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String parseGetNewsInfo(JSONObject jsonObject, ArrayList<STNewsInfo> arrNews)
    {
        String  retMsg = "";
        int retResult = STServiceData.ERR_FAIL;

        try {
            retResult = jsonObject.getInt("SVCC_RET");
            if (STServiceData.ERR_SUCCESS != retResult)
            {
                retMsg = jsonObject.getString("SVCC_RETMSG");
            } else {
                JSONArray jsonArray = jsonObject.getJSONArray("SVCC_DATA");

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject item = jsonArray.getJSONObject(i);

                    // make one advertisement item
                    STNewsInfo stInfo = new STNewsInfo();
                    stInfo.uid = item.getInt("Uid");
                    stInfo.title = item.getString("Title");
                    stInfo.addTime = item.getString("AddTime");
                    stInfo.contents = item.getString("Contents");

                    // add to the news list.
                    arrNews.add(stInfo);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }

    /**
     * Call GetProposeInfo Service
     * @param token [in], user access token
     * @param pageno [in]
     * @param handler [in/out], response handler
     */
    public void GetProposeInfo (String token, int pageno, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdGetProposeInfo;

            // make param
            param.put("token", token);
            param.put("pageno", ""+pageno);

            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String parseGetProposeInfo(JSONObject jsonObject, ArrayList<STSuggestionInfo> arrSuggestions)
    {
        String  retMsg = "";
        int retResult = STServiceData.ERR_FAIL;

        try {
            retResult = jsonObject.getInt("SVCC_RET");
            if (STServiceData.ERR_SUCCESS != retResult)
            {
                retMsg = jsonObject.getString("SVCC_RETMSG");
            } else {
                JSONArray jsonArray = jsonObject.getJSONArray("SVCC_DATA");

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject item = jsonArray.getJSONObject(i);

                    // get a suggestion.
                    STSuggestionInfo stInfo = new STSuggestionInfo();
                    stInfo.id = item.getInt("Id");
                    stInfo.title = item.getString("Title");
                    stInfo.time = item.getString("Time");
                    stInfo.content = item.getString("Content");

                    // add to the news list.
                    arrSuggestions.add(stInfo);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }

    /**
     * Call GetCommentedProducts Service
     * @param token [in], user access token
     * @param pageno [in]
     * @param handler [in/out], response handler
     */
    public void GetCommentedProducts (String token, int pageno, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdGetCommentedProducts;

            // make param
            param.put("token", token);
            param.put("pageno", ""+pageno);

            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String parseGetCommentedProducts(JSONObject jsonObject, ArrayList<STCommentInfo> arrComments)
    {
        String  retMsg = "";
        int retResult = STServiceData.ERR_FAIL;

        try {
            retResult = jsonObject.getInt("SVCC_RET");
            if (STServiceData.ERR_SUCCESS != retResult)
            {
                retMsg = jsonObject.getString("SVCC_RETMSG");
            } else {
                JSONArray jsonArray = jsonObject.getJSONArray("SVCC_DATA");
                String szBaseUrl = jsonObject.getString("SVCC_BASEURL");

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject item = jsonArray.getJSONObject(i);

                    // get a comment the others write.
                    STCommentInfo stInfo = new STCommentInfo();
                    stInfo.id = item.getInt("Id");
                    stInfo.name = item.getString("Name");
                    stInfo.price = item.getDouble("Price");
                    stInfo.time = item.getString("Time");
                    stInfo.comment = item.getString("Comment");
                    stInfo.image = szBaseUrl + item.getString("Image");

                    // add to the news list.
                    arrComments.add(stInfo);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }

    /**
     * Call GetConsultProducts Service
     * @param token [in], user access token
     * @param pageno [in]
     * @param handler [in/out], response handler
     */
    public void GetConsultProducts (String token, int pageno, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdGetConsultProducts;

            // make param
            param.put("token", token);
            param.put("pageno", ""+pageno);

            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String parseGetConsultProducts(JSONObject jsonObject, ArrayList<STConsultingInfo> arrConsultings)
    {
        String  retMsg = "";
        int retResult = STServiceData.ERR_FAIL;

        try {
            retResult = jsonObject.getInt("SVCC_RET");
            if (STServiceData.ERR_SUCCESS != retResult)
            {
                retMsg = jsonObject.getString("SVCC_RETMSG");
            } else {
                JSONArray jsonArray = jsonObject.getJSONArray("SVCC_DATA");
                String szBaseUrl = jsonObject.getString("SVCC_BASEURL");

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject item = jsonArray.getJSONObject(i);

                    // get a consultant.
                    STConsultingInfo stInfo = new STConsultingInfo();
                    stInfo.id = item.getInt("Id");
                    stInfo.name = item.getString("Name");
                    stInfo.price = item.getDouble("Price");
                    stInfo.query = item.getString("Query");
                    stInfo.queryTime = item.getString("QueryTime");
                    stInfo.response = item.getString("Response");
                    stInfo.respTime = item.getString("RespTime");
                    stInfo.image = szBaseUrl + item.getString("Image");

                    // add to the news list.
                    arrConsultings.add(stInfo);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }

    /**
     * Call GetReceivers Service
     * @param token [in], user access token
     * @param handler [in/out], response handler
     */
    public void GetReceivers (String token, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdGetReceivers;

            // make param
            param.put("token", token);

            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String parseGetReceivers(JSONObject jsonObject, ArrayList<STReceiverInfo> arrReceivers)
    {
        String  retMsg = "";
        int retResult = STServiceData.ERR_FAIL;

        try {
            retResult = jsonObject.getInt("SVCC_RET");
            if (STServiceData.ERR_SUCCESS != retResult)
            {
                retMsg = jsonObject.getString("SVCC_RETMSG");
            } else {
                JSONArray jsonArray = jsonObject.getJSONArray("SVCC_DATA");

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject item = jsonArray.getJSONObject(i);

                    // get a receiver info.
                    STReceiverInfo stInfo = new STReceiverInfo();
                    stInfo.uid = item.getInt("Uid");
                    stInfo.isDefault = item.getInt("IsDefault");
                    stInfo.name = item.getString("Name");
                    stInfo.phone = item.getString("Phone");
                    stInfo.addrDetail = item.getString("AddrDetail");
                    stInfo.province = item.getString("Province");
                    stInfo.city = item.getString("City");
                    stInfo.area = item.getString("Area");

                    // add to the receiver list.
                    arrReceivers.add(stInfo);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }

    /**
     * Call DeleteCollectedProducts Service
     * @param token [in], user access token
     * @param id_array [in], favorite item ids to be deleted.
     * @param handler [in/out], response handler
     */
    public void DeleteCollectedProducts (String token, String id_array, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdDeleteCollectedProducts;

            // make param
            param.put("token", token);
            param.put("id_array", id_array);

            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String parseDeleteCollectedProducts(JSONObject jsonObject)
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
     * Call DeleteNewsInfo Service
     * @param token [in], user access token
     * @param id_array [in], news item ids to be deleted.
     * @param handler [in/out], response handler
     */
    public void DeleteNewsInfo (String token, String id_array, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdDeleteNewsInfo;

            // make param
            param.put("token", token);
            param.put("id_array", id_array);

            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String parseDeleteNewsInfo(JSONObject jsonObject)
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
     * Call DelReceiver Service
     * @param token [in], user access token
     * @param id_array [in], receiver item ids to be deleted.
     * @param handler [in/out], response handler
     */
    public void DelReceiver (String token, String id_array, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdDelReceiver;

            // make param
            param.put("token", token);
            param.put("id_array", id_array);

            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String parseDelReceiver(JSONObject jsonObject)
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
     * Call AddReceiver Service
     * @param token [in], user access token
     * @param receiverInfo [in], receiver item to add.
     * @param handler [in/out], response handler
     */
    public void AddReceiver (String token, STReceiverInfo receiverInfo, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdAddReceiver;

            // make param
            param.put("token", token);
            param.put("name", receiverInfo.name);
            param.put("phone", receiverInfo.phone);
            param.put("province", receiverInfo.province);
            param.put("city", receiverInfo.city);
            param.put("area", receiverInfo.area);
            param.put("addr_detail", receiverInfo.addrDetail);
            param.put("is_default", ""+receiverInfo.isDefault);

            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String parseAddReceiver(JSONObject jsonObject)
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
     * Call UpdateReceiver Service
     * @param token [in], user access token
     * @param receiverInfo [in], receiver item to add.
     * @param handler [in/out], response handler
     */
    public void UpdateReceiver (String token, STReceiverInfo receiverInfo, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdUpdateReceiver;

            // make param
            param.put("token", token);
            param.put("uid", ""+receiverInfo.uid);
            param.put("name", receiverInfo.name);
            param.put("phone", receiverInfo.phone);
            param.put("province", receiverInfo.province);
            param.put("city", receiverInfo.city);
            param.put("area", receiverInfo.area);
            param.put("addr_detail", receiverInfo.addrDetail);

            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String parseUpdateReceiver(JSONObject jsonObject)
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

    public void AdvanceOpinion (String token, String title, String email, String content, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdAdvanceOpinion;

            // make param
            param.put("token", token);
            param.put("title", title);
            param.put("email", email);
            param.put("content", content);

            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String parseAdvanceOpinion(JSONObject jsonObject)
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
     * Get detail information of order
     * @param token [in], user token
     * @param orderNo [in], order number
     * @param handler [in/out], response handler
     */
    public void GetOrderInfo(String token, String orderNo, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdGetOrderInfo;

            // make param
            param.put("token", token);
            param.put("orderNo", orderNo);

            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String parseGetOrderInfo(JSONObject jsonObject, STOrderDetail stInfo)
    {
        String  retMsg = "";
        int retResult = STServiceData.ERR_FAIL;

        try {
            retResult = jsonObject.getInt("SVCC_RET");
            if (STServiceData.ERR_SUCCESS != retResult)
            {
                retMsg = jsonObject.getString("SVCC_RETMSG");
            } else {
                JSONObject item = jsonObject.getJSONObject("SVCC_DATA");
                String szBaseUrl = jsonObject.getString("SVCC_BASEURL");

                // make one advertisement item
                stInfo.stateID = item.getInt("StateID");
                stInfo.rcvName = item.getString("RcvName");
                stInfo.rcvPhone = item.getString("RcvPhone");
                stInfo.rcvPhone = item.getString("RcvAddress");
                stInfo.payment = item.getInt("Payment");
                stInfo.rcvType = item.getInt("RcvType");
                stInfo.comment = item.getString("Comments"); // have a value only when the 'SndTime' exists.
                stInfo.prodPrice = item.getDouble("ProductPrice");
                stInfo.transPrice = item.getDouble("TransPrice");

                JSONArray  jsonArrayProduct = item.getJSONArray("Products");
                stInfo.arrProducts.clear();
                for ( int j=0; j<jsonArrayProduct.length(); j++ ) {
                    JSONObject aProduct = jsonArrayProduct.getJSONObject(j);
                    STBasketItemInfo stProductA = new STBasketItemInfo();

                    stProductA.pid = aProduct.getInt("Id");
                    stProductA.spec1 = aProduct.getInt("Gid1");
                    stProductA.spec2 = aProduct.getInt("Gid2");
                    stProductA.name = aProduct.getString("Name");
                    stProductA.count = aProduct.getInt("Count");
                    stProductA.price = aProduct.getDouble("Price");
                    stProductA.image = szBaseUrl + aProduct.getString("Image");

                    stInfo.arrProducts.add(stProductA);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }

    /**
     * Pay current order
     * @param token [in], user token
     * @param orderNo [in], order number to be pay
     * @param paytype1 [in], first pay type
     * @param cardno [in], card number
     * @param cardpwd [in], card password
     * @param pay_money1 [in], first money to be pay
     * @param paytype2 [in], second pay type
     * @param pay_money2 [in], second money to be pay
     * @param handler [in/out]. response handler
     */
    public void PayOrder(String token, String orderNo, int paytype1, String cardno, String cardpwd, double pay_money1,
                                int paytype2, double pay_money2, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdPayOrder;

            // make param
            param.put("token", token);
            param.put("orderNo", orderNo);
            param.put("paytype1", Integer.toString(paytype1));
            param.put("cardno", cardno);
            param.put("cardpwd", cardpwd);
            param.put("pay_money1", Double.toString(pay_money1));
            param.put("paytype2", Integer.toString(paytype2));
            param.put("pay_money2", Double.toString(pay_money2));
            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);

            String temp = param.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String parsePayOrder(JSONObject jsonObject)
    {
        String  retMsg = "";
        int retResult = STServiceData.ERR_FAIL;

        try {
            retResult = jsonObject.getInt("SVCC_RET");
            if (STServiceData.ERR_SUCCESS != retResult)
            {
                retMsg = jsonObject.getString("SVCC_RETMSG");
            } else {
                // only check result
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }

    /**
     * Cancel current order
     * @param token [in], user token
     * @param orderNo [in], order number
     * @param handler [in/out], response handler
     */
    public void CancelOrder(String token, String orderNo, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdCancelOrder;

            // make param
            param.put("token", token);
            param.put("orderNo", orderNo);
            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String parseCancelOrder(JSONObject jsonObject)
    {
        String  retMsg = "";
        int retResult = STServiceData.ERR_FAIL;

        try {
            retResult = jsonObject.getInt("SVCC_RET");
            if (STServiceData.ERR_SUCCESS != retResult)
            {
                retMsg = jsonObject.getString("SVCC_RETMSG");
            } else {
                // only check result
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }

    /**
     * Request pay product
     * @param token [in], user token
     * @param orderNo [in], order number to be pay
     * @param handler [in/out], response handler
     */
    public void RequestPayedProduct(String token, String orderNo, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdRequestPayedProduct;

            // make param
            param.put("token", token);
            param.put("orderNo", orderNo);
            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String parseRequestPayedProduct(JSONObject jsonObject)
    {
        String  retMsg = "";
        int retResult = STServiceData.ERR_FAIL;

        try {
            retResult = jsonObject.getInt("SVCC_RET");
            if (STServiceData.ERR_SUCCESS != retResult)
            {
                retMsg = jsonObject.getString("SVCC_RETMSG");
            } else {
                // only check result
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }

    /**
     * Confirm reception of order
     * @param token [in], user token
     * @param orderNo [in], order number to be confirm
     * @param handler [in/out], response handler
     */
    public void ConfirmReception(String token, String orderNo, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdConfirmReception;

            // make param
            param.put("token", token);
            param.put("orderNo", orderNo);
            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String parseConfirmReception(JSONObject jsonObject)
    {
        String  retMsg = "";
        int retResult = STServiceData.ERR_FAIL;

        try {
            retResult = jsonObject.getInt("SVCC_RET");
            if (STServiceData.ERR_SUCCESS != retResult)
            {
                retMsg = jsonObject.getString("SVCC_RETMSG");
            } else {
                // only check result
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }


    public void EvaluateProduct(String token, int pid, String order_no, int star, String content, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdEvaluateProduct;

            // make param
            param.put("token", token);
            param.put("pid", Integer.toString(pid));
            param.put("order_no", order_no);
            param.put("star", Integer.toString(star));
            param.put("content", content);
            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String parseEvaluateProduct(JSONObject jsonObject)
    {
        String  retMsg = "";
        int retResult = STServiceData.ERR_FAIL;

        try {
            retResult = jsonObject.getInt("SVCC_RET");
            if (STServiceData.ERR_SUCCESS != retResult)
            {
                retMsg = jsonObject.getString("SVCC_RETMSG");
            } else {
                // only check result
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }

    /**
     * Inform to the server that one news is read.
     * @param token [in], user token
     * @param newsId [in], news identifier
     * @param handler [in/out], response handler
     */
    public void SetNewsReadState(String token, int newsId, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdSetNewsReadState;

            // make param
            param.put("token", token);
            param.put("news_id", ""+newsId);
            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
