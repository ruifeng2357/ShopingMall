package com.damytech.STData;

public class STServiceData {
	// Service Address	
    public static String serviceAddr = "http://42.121.114.33:8010/Service.svc/";
//    public static String serviceAddr = "http://192.168.1.38:8055/Service.svc/";

    // error code
    public static final int ERR_SUCCESS = 0;
    public static final int ERR_EXCEPTION = -1000;
    public static final int ERR_FAIL = -2000;

    public static String MSG_SUCCESS = "";

    public static String cmdLoginUser = "LoginUser";
    public static String cmdRegisterUser = "RegisterUser";
    public static String cmdGetAdvertList = "GetAdvertList";
    public static String cmdGetHomeProducts = "GetHomeProducts";
    public static String cmdGetCourtesyParentTypes = "GetCourtesyParentTypes";
    public static String cmdGetCourtesyChildTypes = "GetCourtesyChildTypes";
    public static String cmdGetCourtesyShopsOfType = "GetCourtesyShopsOfType";
    public static String cmdGetCourtesyShopsOfKeyWord = "GetCourtesyShopsOfKeyWord";
    public static String cmdGetSpecialProducts = "GetSpecialProducts";
    public static String cmdGetNearbyCourtesyShops = "GetNearbyCourtesyShops";
    public static String cmdGetCourtesyShopDetail = "GetCourtesyShopDetail";
    public static String cmdGetCouponDetail = "GetCouponDetail";
    public static String cmdGetCouponLogList = "GetCouponLogList";
    public static String cmdBuyFreeCoupon = "BuyFreeCoupon";
    public static String cmdGetProductAllTypes = "GetProductAllTypes";
    public static String cmdGetProducts = "GetProducts";
    public static String cmdGetProductOfKeyword = "GetProductOfKeyword";
    public static String cmdGetYiYuanQuanOfKeyWord = "GetYiYuanQuanOfKeyWord";
    public static String cmdGetProductOfComment = "GetProductOfComment";
    public static String cmdGetProductDetail = "GetProductDetail";
    public static String cmdGetProductComments = "GetProductComments";
    public static String cmdGetProductConsults = "GetProductConsults";
    public static String cmdSubmitProductQuestion = "SubmitProductQuestion";
    public static String cmdGetCartsProducts = "GetCartsProducts";

    public static String cmdGetCartProductsCount = "GetCartProductsCount";

    public static String cmdUpdateCartProductCount = "UpdateCartProductCount";
    public static String cmdDeleteShopCartsProducts = "DeleteShopCartsProducts";
    public static String cmdAddProductToShopCarts = "AddProductToShopCarts";
    public static String cmdGiveOrder = "GiveOrder";
    public static String  cmdGetOrderInfo = "GetOrderInfo";
    public static String  cmdPayOrder = "PayOrder";
    public static String  cmdCancelOrder = "CancelOrder";
    public static String  cmdRequestPayedProduct = "RequestPayedProduct";
    public static String  cmdConfirmReception = "ConfirmReception";
    public static String  cmdEvaluateProduct = "EvaluateProduct";
    public static String  cmdGetOrderInfoOfKeyword = "GetOrderInfoOfKeyword";
    public static String  cmdGetUnpaiedCount = "GetUnpaiedCount";
    public static String  cmdGetOrderInfoOfState = "GetOrderInfoOfState";
    public static String  cmdGetReceivers = "GetReceivers";
    public static String  cmdAddReceiver = "AddReceiver";
    public static String  cmdDelReceiver = "DelReceiver";
    public static String  cmdUpdateReceiver = "UpdateReceiver";
    public static String cmdSetDefaultReceiver = "SetDefaultReceiver";
    public static String  cmdGetNewsInfo = "GetNewsInfo";
    public static String  cmdSetNewsReadState = "SetNewsReadState";
    public static String  cmdDeleteNewsInfo = "DeleteNewsInfo";
    public static String  cmdGetUnreadNewsCount = "GetUnreadNewsCount";
    public static String cmdGetLatestNews = "GetLatestNews";
	public static String cmdUpgradeToBangKe = "UpgradeToBangKe";
	public static String  cmdGetBangKeState = "GetBangKeState";

	public static String cmdGetCouponCount = "GetCouponCount";
    public static String cmdGetCollectionCount = "GetCollectionCount";
    public static String cmdGetCollectionProducts = "GetCollectionProducts";
    public static String cmdCollectProduct = "CollectProduct";
    public static String cmdDeleteCollectedProducts = "DeleteCollectedProducts";
    public static String cmdResetPassword = "ResetPassword";
    public static String cmdRequestResetVerifyKey = "RequestResetVerifKey";
    public static String cmdGetLPCardLeftMoney = "GetLPCardLeftMoney";
    public static String cmdGetBangbiValue = "GetBangbiValue";

    public static String cmdGetMemberBasicValues = "GetMemberBasicValues";

    public static String  cmdDrawBangbi = "DrawBangbi";
    public static String cmdGetMemberInfo = "GetMemberInfo";
    public static String  cmdGetLPCardList = "GetLPCardList";
    public static String  cmdBindPresentCard = "BindPresentCard";
    public static String  cmdGetCommentedProducts = "GetCommentedProducts";
    public static String  cmdGetConsultProducts = "GetConsultProducts";
    public static String  cmdGetProposeInfo = "GetProposeInfo";
    public static String  cmdAdvanceOpinion = "AdvanceOpinion";
    public static String cmdUpdateUserPhoto = "UpdateUserPhoto";
    public static String  cmdIsValidVerifKey = "IsValidVerifKey";

    // Service Parameter
    public static int ADVERT_HOME = 1;
    public static int ADVERT_PREFER = 2;
    public static int ADVERT_EXTRA = 3;

    public static int PROD_ID_TECHAN = 2883;
    public static int PROD_ID_JIUHUI = 2884;
    public static int PROD_ID_CHAHUI = 2885;
    public static int PROD_ID_1YUANQUAN = 2458;


	// Connection Info
	public static int connectTimeout = 4 * 1000; // 5 Seconds
	public static int emailTimeout = 19 * 1000; // 20 Seconds
}
