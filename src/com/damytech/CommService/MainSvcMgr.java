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
 * Date: 11/20/13
 * Time: 5:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class MainSvcMgr {

    /**
     * Get Advertisement data list
     * @param type [in], page type
     * @param width [in], image width
     * @param handler [in/out], response handler
     */
    public void GetAdvertList(int type, int width, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdGetAdvertList;
            // make param
            param.put("type", Integer.toString(type));
            param.put("width", Integer.toString(width));
            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        }
		catch (Exception e)
		{
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public String parseGetAdvertList(JSONObject jsonObject, ArrayList<STAdvertInfo> dataList)
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
                    STAdvertInfo stInfo = new STAdvertInfo();
                    stInfo.id = item.getInt("Id");
                    stInfo.imagepath = basePath + item.getString("Image");
                    // add advertisement item
                    dataList.add(stInfo);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }

    /**
     * Get product list of home page (2 type)
     * @param count [in], product count
     * @param width [in], image width
     * @param handler [in/out], response handler
     */
    public void GetHomeProducts(String count, String width, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdGetHomeProducts;
            // make param
            param.put("product_count", count);
            param.put("product_width", width);
            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public String parseGetHomeProducts(JSONObject jsonObject, STHomeProdDataInfo homeProd1, STHomeProdDataInfo homeProd2)
    {
        String retMsg = "";
        String basePath = "";
        int retResult = STServiceData.ERR_FAIL;

        homeProd1.arrProducts.clear();
        homeProd2.arrProducts.clear();

        try {
            retResult = jsonObject.getInt("SVCC_RET");
            basePath = jsonObject.getString("SVCC_BASEURL");
            if (STServiceData.ERR_SUCCESS != retResult)
            {
                retMsg = jsonObject.getString("SVCC_RETMSG");
            }
            else
            {
                // get service body data of json array
                JSONArray jsonArray = jsonObject.getJSONArray("SVCC_DATA");
                // get home product 1
                JSONObject jsonProd1 = jsonArray.getJSONObject(0);
                homeProd1.homeProdId = jsonProd1.getInt("Id");
                homeProd1.homeProdName = jsonProd1.getString("Title");
                {
                    JSONArray jsonProd1List = jsonProd1.getJSONArray("Products");
                    // get first product list
                    for (int i = 0; i < jsonProd1List.length(); i++)
                    {
                        JSONObject item = jsonProd1List.getJSONObject(i);
                        // make one product info
                        STProductList stInfo = new STProductList();
                        stInfo.id = item.getInt("Id");
                        stInfo.cid = item.getInt("CId");
                        stInfo.name = item.getString("Name");
                        stInfo.image = basePath + item.getString("Image");
                        stInfo.price = item.getDouble("Price");
                        stInfo.commission = item.getDouble("Commission");
                        // add advertisement item
                        homeProd1.arrProducts.add(stInfo);
                    }
                }

                // get home product 2
                JSONObject jsonProd2 = jsonArray.getJSONObject(1);
                homeProd2.homeProdId = jsonProd2.getInt("Id");
                homeProd2.homeProdName = jsonProd2.getString("Title");
                {
                    JSONArray jsonProd2List = jsonProd2.getJSONArray("Products");
                    // get first product list
                    for (int i = 0; i < jsonProd2List.length(); i++)
                    {
                        JSONObject item = jsonProd2List.getJSONObject(i);
                        // make one product info
                        STProductList stInfo = new STProductList();
                        stInfo.id = item.getInt("Id");
                        stInfo.cid = item.getInt("CId");
                        stInfo.name = item.getString("Name");
                        stInfo.image = basePath + item.getString("Image");
                        stInfo.price = item.getDouble("Price");
                        stInfo.commission = item.getDouble("Commission");
                        // add advertisement item
                        homeProd2.arrProducts.add(stInfo);
                    }
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }


    /**
     * Get product list of WaiKuai
     * @param heigth [in], image height
     * @param pageno [in], page no
     * @param handler [in/out] response handler
     */
    public void GetProductOfComment(int heigth, int pageno, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdGetProductOfComment;
            // make param
            param.put("height", Integer.toString(heigth));
            param.put("pageno", Integer.toString(pageno));
            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public String parseProductOfComment(JSONObject jsonObject, ArrayList<STEarnMoneyInfo> dataList)
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
                    // make one WaiKuai item
                    STEarnMoneyInfo stInfo = new STEarnMoneyInfo();
                    stInfo.id = item.getInt("Id");
                    stInfo.cid = item.getInt("CId");
                    stInfo.imgUrl = basePath + item.getString("Image");
                    stInfo.title = item.getString("Name");
                    stInfo.preferValue = (float)item.getDouble("Price");
                    stInfo.profitValue = (float)item.getDouble("Commission");
                    // add item
                    dataList.add(stInfo);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }
}
