package com.damytech.CommService;

import com.damytech.HttpConn.AsyncHttpClient;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.HttpConn.RequestParams;
import com.damytech.STData.STServiceData;
import com.damytech.STData.STString;
import com.damytech.STData.ShoppingCart.STBasketItemInfo;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: KimOC
 * Date: 11/29/13
 * Time: 2:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class OrderSvcMgr {

    /**
     * Send one order
     * @param dataList [in], product array of order
     * @param payment [in], mode of payment
     * @param recvtype [in], receive mode
     * @param comments [in], comment string to be set
     * @param productprice [in], total price of products
     * @param transprice [in], trans price
     * @param token [in], user token
     * @param handler [in/out[, response handler
     */
    public void GiveOrder(ArrayList<STBasketItemInfo> dataList, int payment, int recvtype, String comments,
                                        double productprice, double transprice, String token,
                                        AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdGiveOrder;
            // make param
            JSONArray arrProd = new JSONArray();
            for (int i = 0; i < dataList.size(); i++)
            {
                STBasketItemInfo item = dataList.get(i);
                JSONObject jsonItem = new JSONObject();
                jsonItem.put("uid", item.pid);
                jsonItem.put("name", item.name);
                jsonItem.put("count", item.count);
                jsonItem.put("gid1", item.spec1);
                jsonItem.put("gid2", item.spec2);
                jsonItem.put("price", item.price);
                // add to json array
                arrProd.put(i, jsonItem);
            }
            param.put("payment", Integer.toString(payment));
            param.put("recvtype", Integer.toString(recvtype));
            param.put("products", arrProd.toString());
            param.put("token", token);
            param.put("comments", comments);
            param.put("productprice", Double.toString(productprice));
            param.put("transprice", Double.toString(transprice));
            // call service
            String temp = param.toString();

            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public String parseGiveOrder(JSONObject jsonObject, STString orderNum)
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
                // get order number
                JSONObject jsonItem = jsonObject.getJSONObject("SVCC_DATA");
                orderNum.szVal = jsonItem.getString("OrderNo");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }
}
