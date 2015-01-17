package com.damytech.CommService;

import com.damytech.HttpConn.AsyncHttpClient;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.HttpConn.RequestParams;
import com.damytech.STData.*;
import com.damytech.STData.ProductDetail.STProdComment;
import com.damytech.STData.ProductDetail.STProdConsult;
import com.damytech.STData.ProductDetail.STProdDetailInfo;
import com.damytech.STData.ShoppingCart.STBasketItemInfo;
import com.damytech.STData.ShoppingCart.STShopCartInfo;
import org.apache.http.entity.ByteArrayEntity;
import org.json.JSONObject;

import java.util.ArrayList;

public class CommMgr {
	
	public static CommMgr commService = new CommMgr();

	UserSvcMgr userMgr = new UserSvcMgr();
    MainSvcMgr mainMgr = new MainSvcMgr();
    PreferSvcMgr preferMgr = new PreferSvcMgr();
    ProductSvcMgr prodMgr = new ProductSvcMgr();
    OrderSvcMgr orderMgr = new OrderSvcMgr();
    AccountSvcMgr  acctSvcMgr = new AccountSvcMgr();
    ShopCartSvcMgr shopcartMgr = new ShopCartSvcMgr();

	public CommMgr()
	{		
	}

    /**
     * Get member 's basic value
     * @param token [in], User access token
     * @param handler [in/out], response handler
     */
    public void GetMemberBasicValues ( String token, AsyncHttpResponseHandler handler ) {
        acctSvcMgr.GetMemberBasicValues(token, handler);
    }

    public String parseMemberBasicValue( JSONObject jsonObject, STMemberBasicValue memberInfo ) {
        return acctSvcMgr.parseMemberBasicValues(jsonObject, memberInfo);
    }


    /**
     * Get BangBi value Service
     * @param token [in], User access token
     * @param handler [in/out], response handler
     */
    public void GetBangbiValue ( String token, AsyncHttpResponseHandler handler ) {
        acctSvcMgr.GetBangbiValue(token, handler);
    }

    public String parseGetBangbiValue ( JSONObject jsonObject, STDouble bangbi ) {
        return acctSvcMgr.parseGetBangbiValue(jsonObject, bangbi);
    }

    /**
     * Call GetMemberInfo Service
     * @param token [in], user access token
     * @param handler [in/out], response handler
     */
    public void RequestGetMemberInfo(String token, AsyncHttpResponseHandler handler)
    {
        acctSvcMgr.RequestGetMemberInfo(token, handler);
    }

    public String parseGetMemberInfo(JSONObject jsonObject, STGetMemberInfo stGetMemberInfo)
    {
        return acctSvcMgr.parseGetMemberInfo(jsonObject, stGetMemberInfo);
    }

    /**
     * Get the count of orders unpaied
     * @param token [in], User access token
     * @param handler [in/out], response handler
     */
    public void GetUnpaiedCount ( String token, AsyncHttpResponseHandler handler ) {
        acctSvcMgr.GetUnpaiedCount(token, handler);
    }

    public String parseGetUnpaiedCount ( JSONObject jsonObject, STInteger count ) {
        return acctSvcMgr.parseGetUnpaiedCount(jsonObject, count);
    }

    /**
     * Get the count of news unread.
     * @param token [in], User access token
     * @param handler [in/out], response handler
     */
    public void GetUnreadNewsCount ( String token, AsyncHttpResponseHandler handler ) {
        acctSvcMgr.GetUnreadNewsCount(token, handler);
    }

    public String parseGetUnreadNewsCount ( JSONObject jsonObject, STInteger count ) {
        return acctSvcMgr.parseGetUnreadNewsCount(jsonObject, count);
    }



    /**
     * Get the count of coupons you have.
     * @param token [in], User access token
     * @param handler [in/out], response handler
     */
    public void GetCouponCount ( String token, AsyncHttpResponseHandler handler ) {
        acctSvcMgr.GetCouponCount(token, handler);
    }

    public String parseGetCouponCount ( JSONObject jsonObject, STInteger count ) {
        return acctSvcMgr.parseGetCouponCount(jsonObject, count);
    }

    /**
     * Get the count of favorites you selected.
     * @param token [in], User access token
     * @param handler [in/out], response handler
     */
    public void GetFavoriteCount ( String token, AsyncHttpResponseHandler handler ) {
        acctSvcMgr.GetFavoriteCount(token, handler);
    }

    public String parseGetFavoriteCount ( JSONObject jsonObject, STInteger count ) {
        return acctSvcMgr.parseGetFavoriteCount(jsonObject, count);
    }

    /**
     * Call GetCollectionProducts Service
     * @param token [in], user access token
     * @param pid [in/out], product id
     */
    public void RequestCollectProduct(String token, int pid, AsyncHttpResponseHandler handler)
    {
        acctSvcMgr.RequestCollectProduct(token, pid, handler);
    }

    public String parseCollectProduct(JSONObject jsonObject)
    {
        return acctSvcMgr.parseCollectProduct(jsonObject);
    }

    /**
     * Get favorite items.
     * @param token [in], User access token
     * @param handler [in/out], response handler
     */
    public void GetCollectionProducts ( String token, int width, int height, int pageno, AsyncHttpResponseHandler handler ) {
        acctSvcMgr.GetCollectionProducts(token, width, height, pageno, handler);
    }

    public String parseGetFavoriteItems ( JSONObject jsonObject, ArrayList<STFavoriteInfo> arrFavoriteItems ) {
        return acctSvcMgr.parseGetFavoriteItems(jsonObject, arrFavoriteItems);
    }

    /**
     * Call GetUnreadNewsCount Service
     * @param token [in], user access token
     * @param handler [in/out], response handler
     */
    public void RequestGetLatestNews(String token, AsyncHttpResponseHandler handler)
    {
        acctSvcMgr.RequestGetLatestNews(token, handler);
    }

    public String parseGetLatestNews(JSONObject jsonObject, STNewsInfo newsInfo, STInteger stCount)
    {
        return acctSvcMgr.parseGetLatestNews(jsonObject, newsInfo, stCount);
    }

    /**
     * Get the count of favorites you selected.
     * @param token [in], User access token
     * @param verifkey [in]
     * @param phone [in]
     * @param password [in]
     * @param handler [in/out], response handler
     */
    public void ResetPassword ( String token, String verifkey, String phone, String password, AsyncHttpResponseHandler handler ) {
        acctSvcMgr.ResetPassword(token, verifkey, phone, password, handler);
    }

    public String parseResetPassword ( JSONObject jsonObject ) {
        return acctSvcMgr.parseResetPassword(jsonObject);
    }

    /**
     * Get the list of gift cards.
     * @param token [in], User access token
     * @param pageno [in]
     * @param handler [in/out], response handler
     */
    public void GetLPCardList ( String token, int pageno, AsyncHttpResponseHandler handler ) {
        acctSvcMgr.GetLPCardList(token, pageno, handler);
    }

    public String parseGetLPCardList ( JSONObject jsonObject, ArrayList<STGiftCardInfo> arrGiftCards ) {
        return acctSvcMgr.parseGetLPCardList(jsonObject, arrGiftCards);
    }

    /**
     * Bind the present card to my account
     * @param token [in], user access token
     * @param cardno [in]
     * @param pwd [in]
     * @param phone [in]
     * @param verifkey [in]
     * @param handler [in/out], response handler
     */
    public void BindPresentCard (String token, String cardno, String pwd, String phone, String verifkey, AsyncHttpResponseHandler handler)
    {
        acctSvcMgr.BindPresentCard(token, cardno, pwd, phone, verifkey, handler);
    }

    public String parseBindPresentCard ( JSONObject jsonObject ) {
        return acctSvcMgr.parseBindPresentCard(jsonObject);
    }

    /**
     * Get order infos by a keyword
     * @param token [in], user access token
     * @param keyword [in]
     * @param pageno [in]
     * @param handler [in/out], response handler
     */
    public void GetOrderInfoOfKeyword (String token, String keyword, int pageno, AsyncHttpResponseHandler handler) {
        acctSvcMgr.GetOrderInfoOfKeyword(token, keyword, pageno, handler);
    }

    public String parseGetOrderInfoOfKeyword(JSONObject jsonObject, ArrayList<STOrderByKeyword> arrOrderByKeyword) {
        return acctSvcMgr.parseGetOrderInfoOfKeyword(jsonObject, arrOrderByKeyword);
    }

    /**
     * Get order infos by a state
     * @param token [in], user access token
     * @param state [in]
     * @param pageno [in]
     * @param handler [in/out], response handler
     */
    public void GetOrderInfoOfState (String token, int state, int pageno, AsyncHttpResponseHandler handler) {
        acctSvcMgr.GetOrderInfoOfState(token, state, pageno, handler);
    }

    public String parseGetOrderInfoOfState(JSONObject jsonObject, ArrayList<STOrderByState> arrOrderByState) {
        return acctSvcMgr.parseGetOrderInfoOfState(jsonObject, arrOrderByState);
    }

    /**
     * Get coupon list
     * @param token [in], user access token
     * @param width [in]
     * @param height [in]
     * @param pageno [in]
     * @param handler [in/out], response handler
     */
    public void GetCouponLogList (String token, int width, int height, int pageno, AsyncHttpResponseHandler handler) {
        acctSvcMgr.GetCouponLogList(token, width, height, pageno, handler);
    }

    public String parseGetCouponLogList(JSONObject jsonObject, ArrayList<STCouponA> arrCoupons) {
        return acctSvcMgr.parseGetCouponLogList(jsonObject, arrCoupons);
    }

    /**
     * Get news list
     * @param token [in], user access token
     * @param pageno [in]
     * @param handler [in/out], response handler
     */
    public void GetNewsInfo (String token, int pageno, AsyncHttpResponseHandler handler) {
        acctSvcMgr.GetNewsInfo(token, pageno, handler);
    }

    public String parseGetNewsInfo(JSONObject jsonObject, ArrayList<STNewsInfo> arrNews) {
        return acctSvcMgr.parseGetNewsInfo(jsonObject, arrNews);
    }

    /**
     * Get suggestions list
     * @param token [in], user access token
     * @param pageno [in]
     * @param handler [in/out], response handler
     */
    public void GetProposeInfo (String token, int pageno, AsyncHttpResponseHandler handler) {
        acctSvcMgr.GetProposeInfo(token, pageno, handler);
    }

    public String parseGetProposeInfo(JSONObject jsonObject, ArrayList<STSuggestionInfo> arrSuggestions) {
        return acctSvcMgr.parseGetProposeInfo(jsonObject, arrSuggestions);
    }

    /**
     * Get comments list
     * @param token [in], user access token
     * @param pageno [in]
     * @param handler [in/out], response handler
     */
    public void GetCommentedProducts (String token, int pageno, AsyncHttpResponseHandler handler) {
        acctSvcMgr.GetCommentedProducts(token, pageno, handler);
    }

    public String parseGetCommentedProducts(JSONObject jsonObject, ArrayList<STCommentInfo> arrComments) {
        return acctSvcMgr.parseGetCommentedProducts(jsonObject, arrComments);
    }

    /**
     * Get consultings list
     * @param token [in], user access token
     * @param pageno [in]
     * @param handler [in/out], response handler
     */
    public void GetConsultProducts (String token, int pageno, AsyncHttpResponseHandler handler) {
        acctSvcMgr.GetConsultProducts(token, pageno, handler);
    }

    public String parseGetConsultProducts(JSONObject jsonObject, ArrayList<STConsultingInfo> arrConsultings) {
        return acctSvcMgr.parseGetConsultProducts(jsonObject, arrConsultings);
    }

    /**
     * Get receivers list
     * @param token [in], user access token
     * @param handler [in/out], response handler
     */
    public void GetReceivers (String token, AsyncHttpResponseHandler handler) {
        acctSvcMgr.GetReceivers(token, handler);
    }

    public String parseGetReceivers(JSONObject jsonObject, ArrayList<STReceiverInfo> arrReceivers) {
        return acctSvcMgr.parseGetReceivers(jsonObject, arrReceivers);
    }

    public void DeleteCollectedProducts (String token, String id_array, AsyncHttpResponseHandler handler) {
        acctSvcMgr.DeleteCollectedProducts(token, id_array, handler);
    }

    public String parseDeleteCollectedProducts(JSONObject jsonObject) {
        return acctSvcMgr.parseDeleteCollectedProducts(jsonObject);
    }

    public void DeleteNewsInfo (String token, String id_array, AsyncHttpResponseHandler handler) {
        acctSvcMgr.DeleteNewsInfo(token, id_array, handler);
    }

    public String parseDeleteNewsInfo(JSONObject jsonObject) {
        return acctSvcMgr.parseDeleteNewsInfo(jsonObject);
    }

    public void DelReceiver (String token, String id_array, AsyncHttpResponseHandler handler) {
        acctSvcMgr.DelReceiver(token, id_array, handler);
    }

    public String parseDelReceiver(JSONObject jsonObject) {
        return acctSvcMgr.parseDelReceiver(jsonObject);
    }

    public void AddReceiver (String token, STReceiverInfo receiverInfo, AsyncHttpResponseHandler handler) {
        acctSvcMgr.AddReceiver(token, receiverInfo, handler);
    }

    public String parseAddReceiver(JSONObject jsonObject) {
        return acctSvcMgr.parseAddReceiver(jsonObject);
    }

    public void UpdateReceiver (String token, STReceiverInfo receiverInfo, AsyncHttpResponseHandler handler) {
        acctSvcMgr.UpdateReceiver(token, receiverInfo, handler);
    }

    public String parseUpdateReceiver(JSONObject jsonObject) {
        return acctSvcMgr.parseUpdateReceiver(jsonObject);
    }

    public void AdvanceOpinion (String token, String title, String email, String content, AsyncHttpResponseHandler handler) {
        acctSvcMgr.AdvanceOpinion(token, title, email, content, handler);
    }

    public String parseAdvanceOpinion(JSONObject jsonObject) {
        return acctSvcMgr.parseAdvanceOpinion(jsonObject);
    }

    /**
     * Get detail information of order
     * @param token [in], user token
     * @param orderNo [in], order number
     * @param handler [in/out], response handler
     */
    public void GetOrderInfo(String token, String orderNo, AsyncHttpResponseHandler handler)
    {
        acctSvcMgr.GetOrderInfo(token, orderNo, handler);
    }

    public String parseGetOrderInfo(JSONObject jsonObject, STOrderDetail stInfo)
    {
        return acctSvcMgr.parseGetOrderInfo(jsonObject, stInfo);
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
        acctSvcMgr.PayOrder(token, orderNo, paytype1, cardno, cardpwd, pay_money1, paytype2, pay_money2, handler);
    }

    public String parsePayOrder(JSONObject jsonObject)
    {
        return acctSvcMgr.parsePayOrder(jsonObject);
    }

    /**
     * Cancel current order
     * @param token [in], user token
     * @param orderNo [in], order number
     * @param handler [in/out], response handler
     */
    public void CancelOrder(String token, String orderNo, AsyncHttpResponseHandler handler)
    {
        acctSvcMgr.CancelOrder(token, orderNo, handler);
    }

    public String parseCancelOrder(JSONObject jsonObject)
    {
        return acctSvcMgr.parseCancelOrder(jsonObject);
    }

    /**
     * Request pay product
     * @param token [in], user token
     * @param orderNo [in], order number to be pay
     * @param handler [in/out], response handler
     */
    public void RequestPayedProduct(String token, String orderNo, AsyncHttpResponseHandler handler)
    {
        acctSvcMgr.RequestPayedProduct(token, orderNo, handler);
    }

    public String parseRequestPayedProduct(JSONObject jsonObject)
    {
        return acctSvcMgr.parseRequestPayedProduct(jsonObject);
    }

    /**
     * Confirm reception of order
     * @param token [in], user token
     * @param orderNo [in], order number to be confirm
     * @param handler [in/out], response handler
     */
    public void ConfirmReception(String token, String orderNo, AsyncHttpResponseHandler handler)
    {
        acctSvcMgr.ConfirmReception(token, orderNo, handler);
    }

    public String parseConfirmReception(JSONObject jsonObject)
    {
        return acctSvcMgr.parseConfirmReception(jsonObject);
    }

    public void EvaluateProduct(String token, int pid, String order_no, int star, String content, AsyncHttpResponseHandler handler)
    {
        acctSvcMgr.EvaluateProduct(token, pid, order_no, star, content, handler);
    }

    public String parseEvaluateProduct(JSONObject jsonObject)
    {
        return acctSvcMgr.parseEvaluateProduct(jsonObject);
    }

    public void SetNewsReadState(String token, int newsId, AsyncHttpResponseHandler handler) {
        acctSvcMgr.SetNewsReadState(token, newsId, handler);
    }





    /**
     * Login User Service
     * @param username [in], login user name
     * @param password [in], login user password
     * @param handler [in/out], response handler
     */
    public void LoginUser(String username, String password, AsyncHttpResponseHandler handler)
    {
        userMgr.LoginUser(username, password, handler);
    }

    public String parseLoginUser(JSONObject jsonObject)
    {
        return userMgr.parseLoginUser(jsonObject);
    }

    /**
     * Register new user
     * @param username [in], user login name
     * @param password [in], user login password
     * @param phonenum [in], user phone number
     * @param handler [in/out], response handler
     */
    public void RegisterUser(String username, String password, String phonenum, String recommend, AsyncHttpResponseHandler handler)
    {
        userMgr.RegisterUser(username, password, phonenum, recommend, handler);
    }

    public String parseRegisterUser(JSONObject jsonObject)
    {
        return userMgr.parseRegisterUser(jsonObject);
    }

    /**
     * Call RequestResetVerifyKey Service
     * @param phoneNum [in], user phone number
     * @param handler [in/out], response handler
     */
    public void RequestResetVerifyKey(String phoneNum, AsyncHttpResponseHandler handler)
    {
        userMgr.RequestResetVerifyKey(phoneNum, handler);
    }

    public String parseRequestResetVerifyKey(JSONObject jsonObject)
    {
        return userMgr.parseRequestResetVerifyKey(jsonObject);
    }

    /**
     * Call IsValidVerifKey Service
     * @param phone [in], user phone number
     * @param vkey [in], verification key
     * @param handler [in/out], response handler
     */
    public void RequestIsValidVerifKey(String phone, String vkey, AsyncHttpResponseHandler handler)
    {
        userMgr.RequestIsValidVerifKey(phone, vkey, handler);
    }

    public String parseRequestIsValidVerifKey(JSONObject jsonObject)
    {
        return userMgr.parseRequestIsValidVerifKey(jsonObject);
    }

    /**
     * Call Set default Receiver
     * @param token [in], login user token
     * @param Uid [in], Receiver's Uid
     * @param handler [in/out], response handler
     */
    public void RequestSetDefaultReceiver(String token, int Uid, AsyncHttpResponseHandler handler)
    {
        userMgr.RequestSetDefaultReceiver(token, Uid, handler);
    }

    public String parseSetDefaultReceiver(JSONObject jsonObject, STDouble transPrice)
    {
        return userMgr.parseSetDefaultReceiver(jsonObject, transPrice);
    }

    /**
     * Get Advertisement data list
     * @param type [in], advertisement type id
     * @param width [in], image width
     * @param handler [in/out], response handler
     */
    public void GetAdvertList(int type, int width, AsyncHttpResponseHandler handler)
    {
        mainMgr.GetAdvertList(type, width, handler);
    }

    public String parseGetAdvertList(JSONObject jsonObject, ArrayList<STAdvertInfo> dataList)
    {
        return mainMgr.parseGetAdvertList(jsonObject, dataList);
    }


	/**
	 * Get Advertisement data list
	 * @param token [in], user token info
	 * @param photo [in], image data
	 * @param handler [in/out], response handler
	 */
	public void UpdateUserPhoto(String token, String photo, AsyncHttpResponseHandler handler)
	{
		userMgr.UpdateUserPhoto(token, photo, handler);
	}

	public String parseUpdatePhotoResult(String szContent)
	{
		return userMgr.ParseUserPhotoResult(szContent);
	}

	/**
	 * @param handler [in/out], response handler
	*/
	public void GetBangKeState(String token, AsyncHttpResponseHandler handler)
	{
		userMgr.GetBangKeState(token, handler);
	}

	public STBangKeState parseBangKeStateResult(JSONObject jsonObject)
	{
		return userMgr.parseBangKeStateResult(jsonObject);
	}

	public void UpgradeToBangKe(String name, String idno, String bankcard, String bank, String desc, String bmpPhoto, String token, AsyncHttpResponseHandler handler)
	{
		userMgr.UpgradeToBangKe(name, idno, bankcard, bank, desc, bmpPhoto, token, handler);
	}

	public String parseUpgradeBangKeResult(JSONObject jsonObject, STDouble retVal)
	{
		return userMgr.parseUpgradeBangKeResult(jsonObject, retVal);
	}

    public void RequestDrawBangbi(String token, String identifierno, String cardno, double bangbi, AsyncHttpResponseHandler handler)
    {
        userMgr.RequestDrawBangbi(token, identifierno, cardno, bangbi, handler);
    }

    public String parseDrawBangbi(JSONObject jsonObject)
    {
        return userMgr.parseDrawBangbi(jsonObject);
    }

	/**
     * Get product list of home page (2 type)
     * @param count [in], product count
     * @param width [in], image width
     * @param handler [in/out], response handler
     */
    public void GetHomeProducts(String count, String width, AsyncHttpResponseHandler handler)
    {
        mainMgr.GetHomeProducts(count, width, handler);
    }

    public String parseGetHomeProducts(JSONObject jsonObject, STHomeProdDataInfo homeProd1, STHomeProdDataInfo homeProd2)
    {
        return mainMgr.parseGetHomeProducts(jsonObject, homeProd1, homeProd2);
    }

    /////////////////////////////////////////// Prefer Relation /////////////////////////////////////////
    public void GetCourtesyParentTypes(AsyncHttpResponseHandler handler)
    {
        preferMgr.GetCourtesyParentTypes(handler);
    }

    public String parseCourtesyParentTypes(JSONObject jsonObject, ArrayList<STPreferListItemInfo> datalist)
    {
        return preferMgr.parseCourtesyParentTypes(jsonObject, datalist);
    }

    /**
     * Get child list of courtesy
     * @param parentId [in], parent id
     * @param handler [in/out], response handler
     */
    public void GetCourtesyChildTypes(int parentId, AsyncHttpResponseHandler handler)
    {
        preferMgr.GetCourtesyChildTypes(parentId, handler);
    }

    public String parseCourtesyChildTypes(JSONObject jsonObject, ArrayList<STPreferListItemInfo> datalist)
    {
        return preferMgr.parseCourtesyChildTypes(jsonObject, datalist);
    }

    /**
     * Get courtesy shop by keyword
     * @param token [in], access token
     * @param coup_id [in], coup_id
     * @param handler [in/out], response handler
     */
    public void RequestBuyFreeCoupon(String token, int coup_id, AsyncHttpResponseHandler handler)
    {
        preferMgr.RequestBuyFreeCoupon(token, coup_id, handler);
    }

    public String parseBuyFreeCoupon(JSONObject jsonObject)
    {
        return preferMgr.parseBuyFreeCoupon(jsonObject);
    }

    /**
     * Get courtesy shop list
     * @param type_id [in], index of shop type
     * @param height [in], image height
     * @param pageno [in], current page number
     * @param handler [in/out], response handler
     */
    public void GetCourtesyShopsOfType(int type_id, int height, int pageno, AsyncHttpResponseHandler handler)
    {
        preferMgr.GetCourtesyShopsOfType(type_id, height, pageno, handler);
    }

    public String parseCourtesyShopsOfType(JSONObject jsonObject, ArrayList<STPreferInfo> dataList)
    {
        return preferMgr.parseCourtesyShopsOfType(jsonObject, dataList);
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
        preferMgr.GetCourtesyShopsOfKeyWord(keyword, height, pageno, handler);
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
        preferMgr.RequestGetNearbyCourtesyShops(latitude, longitude, width, height, pageno, handler);
    }

    /**
     * Get detail information of courtesy shop
     * @param uid [in], shop id
     * @param width [in], image width
     * @param handler [in/out] response handler
     */
    public void GetCourtesyShopDetail(int uid, int width, AsyncHttpResponseHandler handler)
    {
        preferMgr.GetCourtesyShopDetail(uid, width, handler);
    }

    public String parseCourtesyShopDetail(JSONObject jsonObject, STPreferDetInfo stInfo)
    {
        return preferMgr.parseCourtesyShopDetail(jsonObject, stInfo);
    }

    /**
     * Get detail information of coupon
     * @param uid [in], index of shop
     * @param width [in], width of image
     * @param handler [in/out], response handler
     */
    public void GetCouponDetail(int uid, int width, AsyncHttpResponseHandler handler)
    {
        preferMgr.GetCouponDetail(uid, width, handler);
    }

    public String parseCouponDetail(JSONObject jsonObject, STShopCoupon stInfo)
    {
        return preferMgr.parseCouponDetail(jsonObject, stInfo);
    }
    ////////////////////////////////////// WaiKuai Relation ///////////////////////////////////
    /**
     * Get product list of WaiKuai
     * @param heigth [in], image height
     * @param pageno [in], page no
     * @param handler [in/out] response handler
     */
    public void GetProductOfComment(int heigth, int pageno, AsyncHttpResponseHandler handler)
    {
        mainMgr.GetProductOfComment(heigth, pageno, handler);
    }

    public String parseProductOfComment(JSONObject jsonObject, ArrayList<STEarnMoneyInfo> dataList)
    {
        return mainMgr.parseProductOfComment(jsonObject, dataList);
    }

    /////////////////////////////////// Product Relation ////////////////////////////////////
    /**
     * Get all product types of list
     * @param handler [in/out], response handler
     */
    public void GetProductAllTypes(AsyncHttpResponseHandler handler)
    {
        prodMgr.GetProductAllTypes(handler);
    }

    public String parseProductAllTypes(JSONObject jsonObject, ArrayList<STSpecialArticleListInfo> dataList)
    {
        return prodMgr.parseProductAllTypes(jsonObject, dataList);
    }
    /**
     * Get products list of selected menu
     * @param parentid [in], menu index
     * @param width [in], image width
     * @param height [in], image height
     * @param pageno [in], current page number
     * @param handler [in/out], response handler
     */
    public void GetProducts(int parentid, int width, int height, int pageno, AsyncHttpResponseHandler handler)
    {
        prodMgr.GetProducts(parentid, width, height, pageno, handler);
    }

    public String parseGetProducts(JSONObject jsonObject, ArrayList<STSpecialArticleInfo> dataList)
    {
        return prodMgr.parseGetProducts(jsonObject, dataList);
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
        prodMgr.GetProductOfKeyword(keyword, heigth, pageno, handler);
    }

    /**
     * Get yiyuan products of keyword
     * @param keyword [in], keyword to be find
     * @param width [in], image width
     * @param pageno [in], page number
     * @param handler [in/out], response handler
     */
    public void GetYiYuanQuanOfKeyWord(String keyword, int width, int pageno, AsyncHttpResponseHandler handler)
    {
        prodMgr.GetYiYuanQuanOfKeyWord(keyword, width, pageno, handler);
    }

    public String parseGetProductOfKeyword(JSONObject jsonObject, ArrayList<STSpecialArticleInfo> dataList)
    {
        return prodMgr.parseGetProductOfKeyword(jsonObject, dataList);
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
        prodMgr.RequestGetLPCardLeftMoney(token, cardno, cardpwd, handler);
    }

    public String parseGetLPCardLeftMoney(JSONObject jsonObject, STDouble fMoney)
    {
        return prodMgr.parseGetLPCardLeftMoney(jsonObject, fMoney );
    }

    /**
     * Get special products of teyou
     * @param width [in], image width
     * @param height [in], image height
     * @param handler [in/out], response handler
     */
    public void GetSpecialProducts(int width, int height, AsyncHttpResponseHandler handler)
    {
        prodMgr.GetSpecialProducts(width, height, handler);
    }

    public String parseSpecialProducts(JSONObject jsonObject, ArrayList<STSpecialArticleMainInfo> dataList)
    {
        return prodMgr.parseSpecialProducts(jsonObject, dataList);
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
        prodMgr.GetProductDetail(token, id, width, height, handler);
    }

    public String parseGetProductDetail(JSONObject jsonObject, STProdDetailInfo stDetailInfo)
    {
        return prodMgr.parseGetProductDetail(jsonObject, stDetailInfo);
    }

    /**
     * Get product of comments
     * @param id [in], product id
     * @param pageno [in], page number
     * @param handler [in/out], response handler
     */
    public void GetProductComments(int id, int pageno, AsyncHttpResponseHandler handler)
    {
        prodMgr.GetProductComments(id, pageno, handler);
    }

    public String parseProductComments(JSONObject jsonObject, ArrayList<STProdComment> dataList)
    {
        return prodMgr.parseProductComments(jsonObject, dataList);
    }

    /**
     * Get product of consult
     * @param id [in], product id
     * @param pageno [in], page number
     * @param handler [in/out], response handler
     */
    public void GetProductConsults(int id, int pageno, AsyncHttpResponseHandler handler)
    {
        prodMgr.GetProductConsults(id, pageno, handler);
    }

    public String parseProductConsults(JSONObject jsonObject, ArrayList<STProdConsult> dataList)
    {
        return prodMgr.parseProductConsults(jsonObject, dataList);
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
        prodMgr.SubmitProductQuestion(id, query, token, handler);
    }

    public String parseSubmitProductQuestion(JSONObject jsonObject)
    {
        return prodMgr.parseSubmitProductQuestion(jsonObject);
    }


    ////////////////////////////////////////// Shopping Cart Relation /////////////////////////////////////

    public void GetCartProductsCount(String token, AsyncHttpResponseHandler handler)
    {
        shopcartMgr.GetCartProductsCount(token, handler);
    }

    public String parseCartProductsCount(JSONObject jsonObject)
    {
        return shopcartMgr.parseCartProductsCount(jsonObject);
    }

    /**
     * Add Product to shopping cart
     * @param pid [in], product id
     * @param gid1 [in], first spec id of product
     * @param gid2 [in], second spec id of product
     * @param count [in], count of product
     * @param token [in], user token
     * @param handler [in/out], response handler
     */
    public void AddProductToShopCarts(int pid, int gid1, int gid2, int count, String token, AsyncHttpResponseHandler handler)
    {
        shopcartMgr.AddProductToShopCarts(pid, gid1, gid2, count, token, handler);
    }

    public String parseAddProductToShopCarts(JSONObject jsonObject)
    {
        return shopcartMgr.parseAddProductToShopCarts(jsonObject);
    }

    /**
     * Delete product from shopping cart
     * @param dataList [in], array of product id
     * @param token [in], user token
     * @param handler [in/out], response handler
     */
    public void DeleteShopCartsProducts(ArrayList<STBasketItemInfo> dataList, String token, AsyncHttpResponseHandler handler)
    {
        shopcartMgr.DeleteShopCartsProducts(dataList, token, handler);
    }

    public String parseDeleteShopCartsProducts(JSONObject jsonObject)
    {
        return shopcartMgr.parseDeleteShopCartsProducts(jsonObject);
    }

    public void GetCartsProducts(String token, int pageno, AsyncHttpResponseHandler handler)
    {
        shopcartMgr.GetCartsProducts(token, pageno, handler);
    }

    public String parseCartsProducts(JSONObject jsonObject, STShopCartInfo dataInfo)
    {
        return shopcartMgr.parseCartsProducts(jsonObject, dataInfo);
    }

    public void UpdateCartProductCount(int pid, int gid1, int gid2, int count, String token, AsyncHttpResponseHandler handler)
    {
        shopcartMgr.UpdateCartProductCount(pid, gid1, gid2, count, token, handler);
    }

    public String parseUpdateCartProductCount(JSONObject jsonObject)
    {
        return shopcartMgr.parseUpdateCartProductCount(jsonObject);
    }

    /////////////////////////////////////////// Order Relation ///////////////////////////////////////////
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
        orderMgr.GiveOrder(dataList, payment, recvtype, comments, productprice, transprice, token, handler);
    }

    public String parseGiveOrder(JSONObject jsonObject, STString orderNum)
    {
        return orderMgr.parseGiveOrder(jsonObject, orderNum);
    }
}
