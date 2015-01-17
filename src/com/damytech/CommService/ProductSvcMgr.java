package com.damytech.CommService;

import com.damytech.HttpConn.AsyncHttpClient;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.HttpConn.RequestParams;
import com.damytech.STData.*;
import com.damytech.STData.ProductDetail.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Yunsungil
 * Date: 11/23/13
 * Time: 11:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProductSvcMgr {

    /**
     * Get all product types of list
     * @param handler [in/out], response handler
     */
    public void GetProductAllTypes(AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdGetProductAllTypes;
            // call service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public String parseProductAllTypes(JSONObject jsonObject, ArrayList<STSpecialArticleListInfo> dataList)
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
                    // make one item
                    STSpecialArticleListInfo stInfo = new STSpecialArticleListInfo();
                    stInfo.id = item.getInt("Id");
                    stInfo.categoryName = item.getString("Name");

                    // get sub list of json array
                    JSONArray jsonSubArray = item.getJSONArray("ChildClassList");
                    for (int j = 0; j < jsonSubArray.length(); j++)
                    {
                        JSONObject subitem = jsonSubArray.getJSONObject(j);
                        // make one sub item
                        STSpecialArticleSubListInfo stSubItem = new STSpecialArticleSubListInfo();
                        stSubItem.id = subitem.getInt("Id");
                        stSubItem.name = subitem.getString("Name");
                        // add sub item
                        stInfo.subItemsList.add(stSubItem);
                    }
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
     * Get products list of selected menu
     * @param token [in], token no
     * @param cardno [in], card no
     * @param cardpwd [in], card password
     * @param handler [in/out], response handler
     */
    public void RequestGetLPCardLeftMoney(String token, String cardno, String cardpwd, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdGetLPCardLeftMoney;
            // make param
            param.put("token", token);
            param.put("cardno", cardno);
            param.put("cardpwd", cardpwd);
            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public String parseGetLPCardLeftMoney(JSONObject jsonObject, STDouble fMoney)
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

                fMoney.fVal = jsonObj.getInt("Money");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }

    /**
     * Get products list of selected menu
     * @param parentid [in], menu index
     * @param width [in], image width
     * @param pageno [in], current page number
     * @param handler [in/out], response handler
     */
    public void GetProducts(int parentid, int width, int heigth, int pageno, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdGetProducts;
            // make param
            param.put("typeid", Integer.toString(parentid));
            param.put("width", Integer.toString(width));
            param.put("height", Integer.toString(heigth));
            param.put("pageno", Integer.toString(pageno));
            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public String parseGetProducts(JSONObject jsonObject, ArrayList<STSpecialArticleInfo> dataList)
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
                    // make one item
                    STSpecialArticleInfo stInfo = new STSpecialArticleInfo();
                    stInfo.id = item.getInt("Id");
                    stInfo.cid = item.getInt("CId");
                    stInfo.title = item.getString("Name");
                    stInfo.price = item.getDouble("Price");
                    if (item.getString("Image").contains("http://"))
                    {
                        stInfo.imgItemPic = item.getString("Image");
                    }
                    else
                    {
                        stInfo.imgItemPic = basePath + item.getString("Image");
                    }
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
     * Get products of keyword
     * @param keyword [in], keyword to be find
     * @param heigth [in], image height
     * @param pageno [in], page number
     * @param handler [in/out], response handler
     */
    public void GetProductOfKeyword(String keyword, int heigth, int pageno, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdGetProductOfKeyword;
            // make param
            param.put("keyword", keyword);
            param.put("height", Integer.toString(heigth));
            param.put("pageno", Integer.toString(pageno));
            // call service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public void GetYiYuanQuanOfKeyWord(String keyword, int width, int pageno, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdGetYiYuanQuanOfKeyWord;
            // make param
            param.put("keyword", keyword);
            param.put("width", Integer.toString(width));
            param.put("pageno", Integer.toString(pageno));
            // call service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public String parseGetProductOfKeyword(JSONObject jsonObject, ArrayList<STSpecialArticleInfo> dataList)
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
                    // make one item
                    STSpecialArticleInfo stInfo = new STSpecialArticleInfo();
                    stInfo.id = item.getInt("Id");
                    stInfo.cid = item.getInt("CId");
                    stInfo.title = item.getString("Name");
                    stInfo.price = item.getDouble("Price");
                    if (item.getString("Image").contains("http://"))
                    {
                        stInfo.imgItemPic = item.getString("Image");
                    }
                    else
                    {
                        stInfo.imgItemPic = basePath + item.getString("Image");
                    }
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
     * Get special products of teyou
     * @param width [in], image width
     * @param height [in], image height
     * @param handler [in/out], response handler
     */
    public void GetSpecialProducts(int width, int height, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdGetSpecialProducts;
            // make param
            param.put("width", Integer.toString(width));
            param.put("height", Integer.toString(height));
            // call service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public String parseSpecialProducts(JSONObject jsonObject, ArrayList<STSpecialArticleMainInfo> dataList)
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
                    // make one item
                    STSpecialArticleMainInfo stInfo = new STSpecialArticleMainInfo();
                    stInfo.typeid = item.getInt("TypeId");
                    stInfo.typeName = item.getString("TypeName");
                    JSONArray jsonprodArray = item.getJSONArray("Products");

                    for (int j = 0; j < jsonprodArray.length(); j++)
                    {
                        JSONObject jsonProduct = jsonprodArray.getJSONObject(j);
                        STProductList prodItem = new STProductList();
                        prodItem.id = jsonProduct.getInt("Id");
                        prodItem.cid = jsonProduct.getInt("CId");
                        prodItem.name = jsonProduct.getString("Name");
                        prodItem.price = jsonProduct.getDouble("Price");
                        if (jsonProduct.getString("Image").contains("http://"))
                        {
                            prodItem.image = jsonProduct.getString("Image");
                        }
                        else
                        {
                            prodItem.image = basePath + jsonProduct.getString("Image");
                        }

                        stInfo.arrProducts.add(prodItem);
                    }

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
     * Get detail information of product
     * @param token [in], user token
     * @param id [in], product id
     * @param width [in], image width
     * @param height [in], image height
     * @param handler [in/out], response handler
     */
    public void GetProductDetail(String token, int id, int width, int height, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdGetProductDetail;
            // make param
            param.put("token", token);
            param.put("id", Integer.toString(id));
            param.put("width", Integer.toString(width));
            param.put("height", Integer.toString(height));
            // call service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public String parseGetProductDetail(JSONObject jsonObject, STProdDetailInfo stDetailInfo)
    {
        int i = 0;
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
                stDetailInfo.id = jsonMainData.getInt("Id");
                stDetailInfo.cid = jsonMainData.getInt("CId");
                stDetailInfo.name = jsonMainData.getString("Name");
                // get first spec data
                JSONObject jsonMainSpecData = jsonMainData.getJSONObject("SpecData1");
                stDetailInfo.specData1Id = jsonMainSpecData.getInt("Id");
                stDetailInfo.specdata1Name = jsonMainSpecData.getString("Name");
                JSONArray jsonArrSpecData1 = jsonMainSpecData.getJSONArray("ChildSpecs");
                for (i = 0; i < jsonArrSpecData1.length(); i++)
                {
                    JSONObject jsonSpecData = jsonArrSpecData1.getJSONObject(i);
                    // make one spec data
                    STProdSpecData specData = new STProdSpecData();
                    specData.id = jsonSpecData.getInt("Id");
                    specData.name = jsonSpecData.getString("Name");
                    // add spec data
                    stDetailInfo.arrSpecData1.add(specData);
                }
                // get seconds spec data
                JSONObject jsonMainSpecData2 = jsonMainData.getJSONObject("SpecData2");
                stDetailInfo.specData2Id = jsonMainSpecData2.getInt("Id");
                stDetailInfo.specdata2Name = jsonMainSpecData2.getString("Name");
                JSONArray jsonArrSpecData2 = jsonMainSpecData2.getJSONArray("ChildSpecs");
                for (i = 0; i < jsonArrSpecData2.length(); i++)
                {
                    JSONObject jsonSpecData = jsonArrSpecData2.getJSONObject(i);
                    // make one spec data
                    STProdSpecData specData = new STProdSpecData();
                    specData.id = jsonSpecData.getInt("Id");
                    specData.name = jsonSpecData.getString("Name");
                    // add spec data
                    stDetailInfo.arrSpecData2.add(specData);
                }
                // get valid spec pairs
                JSONArray jsonArrSpecPair = jsonMainData.getJSONArray("ValidSpecPairs");
                for (i = 0; i < jsonArrSpecPair.length(); i++)
                {
                    JSONObject jsonSpecPair = jsonArrSpecPair.getJSONObject(i);
                    // make one spec data
                    STValidSpecPair specData = new STValidSpecPair();
                    specData.spec1id = jsonSpecPair.getInt("Spec1ID");
                    specData.spec2id = jsonSpecPair.getInt("Spec2ID");
                    specData.marketPrice = jsonSpecPair.getDouble("MarketPrice");
                    specData.price = jsonSpecPair.getDouble("Price");
                    specData.commission = jsonSpecPair.getDouble("Commission");
                    specData.inventory = jsonSpecPair.getDouble("Inventory");
                    // add valid spec pair data
                    stDetailInfo.arrValidSpecPairs.add(specData);
                }
                stDetailInfo.shopName = jsonMainData.getString("ShopName");
                stDetailInfo.shopAddr = jsonMainData.getString("ShopAddr");
                stDetailInfo.shopActivity = jsonMainData.getString("ShopActivity");
                stDetailInfo.latitude = jsonMainData.getDouble("Latitude");
                stDetailInfo.longitude = jsonMainData.getDouble("Longitude");
                stDetailInfo.intro = jsonMainData.getString("Intro");
                stDetailInfo.commentRate = jsonMainData.getDouble("CommentRate");
                stDetailInfo.goodRate = jsonMainData.getDouble("GoodRate");
                stDetailInfo.mediumRate = jsonMainData.getDouble("MediumRate");
                stDetailInfo.badRate = jsonMainData.getDouble("BadRate");
                // get comments array
                JSONArray jsonArrComments = jsonMainData.getJSONArray("Comments");
                for (i = 0; i < jsonArrComments.length(); i++)
                {
                    JSONObject jsonSpecPair = jsonArrComments.getJSONObject(i);
                    // make one comment
                    STProdComment specData = new STProdComment();
                    specData.id = jsonSpecPair.getInt("ID");
                    specData.username = jsonSpecPair.getString("UserName");
                    specData.anonimity = jsonSpecPair.getString("Anonimity");
                    specData.commenttime = jsonSpecPair.getString("CommentTime");
                    specData.rate = jsonSpecPair.getInt("Rate");
                    specData.content = jsonSpecPair.getString("Content");
                    // add valid spec pair data
                    stDetailInfo.arrComments.add(specData);
                }
                // get consults array
                JSONArray jsonArrConsults = jsonMainData.getJSONArray("Consults");
                for (i = 0; i < jsonArrConsults.length(); i++)
                {
                    JSONObject jsonConsult = jsonArrConsults.getJSONObject(i);
                    // make one comment
                    STProdConsult specData = new STProdConsult();
                    specData.id = jsonConsult.getInt("ID");
                    specData.name = jsonConsult.getString("Name");
                    specData.question = jsonConsult.getString("Question");
                    specData.asktime = jsonConsult.getString("AskTime");
                    specData.reply = jsonConsult.getString("Reply");
                    specData.replytime = jsonConsult.getString("ReplyTime");
                    // add valid spec pair data
                    stDetailInfo.arrConsult.add(specData);
                }
                // get image array
                JSONArray jsonArrImg = jsonMainData.getJSONArray("Image");
                for (i = 0; i < jsonArrImg.length(); i++)
                {
                    JSONObject jsonImage = jsonArrImg.getJSONObject(i);
                    // make one comment
                    String image = new String();
                    image = jsonImage.getString("Value");
                    if (!image.contains("http://"))
                    {
                        image = basePath + image;
                    }

                    // add valid spec pair data
                    stDetailInfo.arrImgUrl.add(image);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }

    /**
     * Get product of comments
     * @param id [in], product id
     * @param pageno [in], page number
     * @param handler [in/out], response handler
     */
    public void GetProductComments(int id, int pageno, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdGetProductComments;
            // make param
            param.put("id", Integer.toString(id));
            param.put("pageno", Integer.toString(pageno));
            // call service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public String parseProductComments(JSONObject jsonObject, ArrayList<STProdComment> dataList)
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
                    // make one item
                    STProdComment stInfo = new STProdComment();
                    stInfo.id = item.getInt("ID");
                    stInfo.username = item.getString("UserName");
                    stInfo.anonimity = item.getString("Anonimity");
                    stInfo.commenttime = item.getString("CommentTime");
                    stInfo.rate = item.getInt("Rate");
                    stInfo.content = item.getString("Content");

                    // add comment list item
                    dataList.add(stInfo);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }

    public void GetProductConsults(int id, int pageno, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdGetProductConsults;
            // make param
            param.put("id", Integer.toString(id));
            param.put("pageno", Integer.toString(pageno));
            // call service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public String parseProductConsults(JSONObject jsonObject, ArrayList<STProdConsult> dataList)
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
                    // make one item
                    STProdConsult stInfo = new STProdConsult();
                    stInfo.id = item.getInt("ID");
                    stInfo.name = item.getString("Name");
                    stInfo.question = item.getString("Question");
                    stInfo.asktime = item.getString("AskTime");
                    stInfo.reply = item.getString("Reply");
                    stInfo.replytime = item.getString("ReplyTime");

                    // add comment list item
                    dataList.add(stInfo);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }

    /**
     * Submit question of product
     * @param id [in], product id
     * @param query [in], question string
     * @param token [in], user token
     * @param handler [in/out], response handler
     */
    public void SubmitProductQuestion(int id, String query, String token, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdSubmitProductQuestion;
            // make param
            param.put("id", Integer.toString(id));
            param.put("query", query);
            param.put("token", token);
            // call service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public String parseSubmitProductQuestion(JSONObject jsonObject)
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
