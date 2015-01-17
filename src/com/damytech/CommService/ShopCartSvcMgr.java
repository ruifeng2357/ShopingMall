package com.damytech.CommService;

import com.damytech.Global.GlobalData;
import com.damytech.HttpConn.AsyncHttpClient;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.HttpConn.RequestParams;
import com.damytech.STData.ProductDetail.STProdSpecData;
import com.damytech.STData.STInteger;
import com.damytech.STData.STProductList;
import com.damytech.STData.STServiceData;
import com.damytech.STData.STSpecialArticleMainInfo;
import com.damytech.STData.ShoppingCart.STBasketItemInfo;
import com.damytech.STData.ShoppingCart.STShopCartInfo;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Yunsungil
 * Date: 11/27/13
 * Time: 3:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class ShopCartSvcMgr {

    public void GetCartProductsCount(String token, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdGetCartProductsCount;
            // make param
            param.put("token", token);
            // call service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public String parseCartProductsCount(JSONObject jsonObject)
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
                // parse detail product info
                JSONObject jsonMainData = jsonObject.getJSONObject("SVCC_DATA");
                GlobalData.cartProdCount = jsonMainData.getInt("Count");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }


    /**
     * Add product to shopping cart
     * @param pid [in], product id
     * @param gid1 [in], product first spec id
     * @param gid2 [in], product second spec id
     * @param count [in], product count
     * @param token [in], user token
     * @param handler [in/out], response handler
     */
    public void AddProductToShopCarts(int pid, int gid1, int gid2, int count, String token, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdAddProductToShopCarts;
            // make param
            param.put("pid", Integer.toString(pid));
            param.put("gid1", Integer.toString(gid1));
            param.put("gid2", Integer.toString(gid2));
            param.put("count", Integer.toString(count));
            param.put("token", token);
            // call service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public String parseAddProductToShopCarts(JSONObject jsonObject)
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
                // only check result string
                JSONObject jsonMainData = jsonObject.getJSONObject("SVCC_DATA");
                GlobalData.cartProdCount = jsonMainData.getInt("Count");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }

    /**
     * Delete one product from shopping cart
     * @param dataList [in], array of product id
     * @param token [in], user token
     * @param handler [in/out], response handler
     */
    public void DeleteShopCartsProducts(ArrayList<STBasketItemInfo> dataList, String token, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdDeleteShopCartsProducts;
            // make param
            JSONArray arrProd = new JSONArray();
            for (int i = 0; i < dataList.size(); i++)
            {
                STBasketItemInfo item = dataList.get(i);
                JSONObject jsonItem = new JSONObject();
                jsonItem.put("id", item.pid);
                jsonItem.put("gid1", item.spec1);
                jsonItem.put("gid2", item.spec2);
                // add to json array
                arrProd.put(i, jsonItem);
            }
            param.put("id_array", arrProd.toString());
            param.put("token", token);
            // call service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public String parseDeleteShopCartsProducts(JSONObject jsonObject)
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
                // only check result string
                JSONObject jsonMainData = jsonObject.getJSONObject("SVCC_DATA");
                GlobalData.cartProdCount = jsonMainData.getInt("Count");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }

    /**
     * Get all products in shopping cart
     * @param token [in], user token
     * @param pageno [in], N/A
     * @param handler [in/out], response handler
     */
    public void GetCartsProducts(String token, int pageno, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdGetCartsProducts;
            // make param
            param.put("token", token);
            param.put("pageno", Integer.toString(pageno));
            // call service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public String parseCartsProducts(JSONObject jsonObject, STShopCartInfo dataInfo)
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
                dataInfo.arrProducts.clear();
                // parse detail product info
                JSONObject jsonMainData = jsonObject.getJSONObject("SVCC_DATA");
                dataInfo.totalPrice = jsonMainData.getDouble("TotalPrice");
                dataInfo.transPrice = jsonMainData.getDouble("TransPrice");
                JSONArray jsonArrProduct = jsonMainData.getJSONArray("Products");
                for (int i = 0; i < jsonArrProduct.length(); i++)
                {
                    JSONObject jsonSpecData = jsonArrProduct.getJSONObject(i);
                    // make one spec data
                    STBasketItemInfo item = new STBasketItemInfo();
                    item.pid = jsonSpecData.getInt("Pid");
                    item.spec1 = jsonSpecData.getInt("Gid1");
                    item.spec2 = jsonSpecData.getInt("Gid2");
                    item.name = jsonSpecData.getString("Name");
                    item.count = jsonSpecData.getInt("Count");
                    item.price = jsonSpecData.getDouble("Price");
                    item.image = jsonSpecData.getString("Image");
                    if (!item.image.contains("http://"))
                    {
                        item.image = basePath + item.image;
                    }
                    // add product data
                    dataInfo.arrProducts.add(item);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }

    /**
     * Update count of one product in shopping cart
     * @param pid [in], product id
     * @param gid1 [in], first spec id
     * @param gid2 [in], second spec id
     * @param count [in], product count to be set
     * @param token [in], user token
     * @param handler [in/out], response handler
     */
    public void UpdateCartProductCount(int pid, int gid1, int gid2, int count, String token, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdUpdateCartProductCount;
            // make param
            param.put("pid", Integer.toString(pid));
            param.put("gid1", Integer.toString(gid1));
            param.put("gid2", Integer.toString(gid2));
            param.put("count", Integer.toString(count));
            param.put("token", token);
            // call service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public String parseUpdateCartProductCount(JSONObject jsonObject)
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
                // only check result string
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }
}
