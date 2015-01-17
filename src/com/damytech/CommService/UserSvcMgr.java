package com.damytech.CommService;

/**
 * Created with IntelliJ IDEA.
 * User: Yunsungil
 * Date: 11/19/13
 * Time: 5:38 PM
 * To change this template use File | Settings | File Templates.
 */

import com.damytech.Global.GlobalData;
import com.damytech.HttpConn.AsyncHttpClient;
import com.damytech.HttpConn.AsyncHttpResponseHandler;
import com.damytech.HttpConn.RequestParams;
import com.damytech.STData.*;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.UnsupportedEncodingException;

public class UserSvcMgr {

    public UserSvcMgr()
    {
    }

    /**
     * Call Login Service
     * @param username [in], login user name
     * @param password [in], login user password
     * @param handler [in/out], response handler
     */
    public void LoginUser(String username, String password, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdLoginUser;
            // make param
            param.put("username", username);
            param.put("password", password);
            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public String parseLoginUser(JSONObject jsonObject)
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
                GlobalData.token = jsonObject.getString("SVCC_TOKEN");

                JSONObject jsonData = jsonObject.getJSONObject("SVCC_DATA");

                GlobalData.g_UserInfo.id = jsonData.getInt("Id");
                GlobalData.g_UserInfo.photo = jsonData.getString("Photo");
                if (!GlobalData.g_UserInfo.photo.contains("http://"))
                {
                    GlobalData.g_UserInfo.photo = basePath + GlobalData.g_UserInfo.photo;
                }
                GlobalData.g_UserInfo.transPrice = jsonData.getDouble("TransPrice");
                GlobalData.g_UserInfo.name = jsonData.getString("Name");
                GlobalData.g_UserInfo.phone = jsonData.getString("Phone");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
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
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdRegisterUser;
            // make param
            param.put("username", username);
            param.put("password", password);
            param.put("vericode", "");
            param.put("phonenum", phonenum);
            param.put("recommend", recommend);
            // call get service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public String parseRegisterUser(JSONObject jsonObject)
    {
        String retMsg = "";
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

	public void UpdateUserPhoto(String token, String photo, AsyncHttpResponseHandler handler)
	{
		String url = "";
		AsyncHttpClient client = new AsyncHttpClient();

		try
		{
			/* make service url */
			url = STServiceData.serviceAddr + STServiceData.cmdUpdateUserPhoto;

			/* make param */
			JSONObject jsonParams = new JSONObject();
			jsonParams.put("photo", photo);
			jsonParams.put("token", token);
			StringEntity entity = new StringEntity(jsonParams.toString());

			/* call get service */
			client.setTimeout(STServiceData.connectTimeout);
			client.post(null, url, entity, "application/json", handler);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public String ParseUserPhotoResult(String content)
	{
		String retMsg = "";

		try
		{
			JSONObject jsonObject = new JSONObject(content);
			int retResult = jsonObject.getInt("SVCC_RET");

			if (STServiceData.ERR_SUCCESS != retResult)
				retMsg = jsonObject.getString("SVCC_RETMSG");
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return retMsg;
	}

    /**
     * @param phoneNum [in], user phone number
     * @param handler [in/out], response handler
     */
    public void RequestResetVerifyKey(String phoneNum, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdRequestResetVerifyKey;
            // make param
            param.put("phone", phoneNum);
            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public String parseRequestResetVerifyKey(JSONObject jsonObject)
    {
        String retMsg = "";
        int retResult = STServiceData.ERR_FAIL;

        try {
            retResult = jsonObject.getInt("SVCC_RET");
            if (STServiceData.ERR_SUCCESS != retResult)
            {
                retMsg = jsonObject.getString("SVCC_RETMSG");
            }
            else
            {
                GlobalData.token = jsonObject.getString("SVCC_TOKEN");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }

	/**
	 * Call Login Service
	 * @param handler [in/out], response handler
	 */
	public void GetBangKeState(String token, AsyncHttpResponseHandler handler)
	{
		String url = "";
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams param = new RequestParams();

		try {
			// make service url
			url = STServiceData.serviceAddr + STServiceData.cmdGetBangKeState;
			// make param
			param.put("token", token);
			// call post service
			client.setTimeout(STServiceData.connectTimeout);
			client.get(url, param, handler);
		} catch (Exception e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}

	}

	public STBangKeState parseBangKeStateResult(JSONObject jsonObject)
	{
		String retMsg = "";
		int retResult = STServiceData.ERR_FAIL;
		STBangKeState state_info = new STBangKeState();

		try
		{
			retResult = jsonObject.getInt("SVCC_RET");
			if (STServiceData.ERR_SUCCESS != retResult)
			{
				retMsg = jsonObject.getString("SVCC_RETMSG");
				state_info.state_id = STServiceData.ERR_EXCEPTION;
				state_info.retMsg = jsonObject.getString("SVCC_RETMSG");
			}
			else
			{
				JSONObject jsonData = jsonObject.getJSONObject("SVCC_DATA");

				state_info.state_id = jsonData.getInt("State");
				state_info.bangbi = jsonData.getDouble("BangbiValue");
				state_info.id_no = jsonData.getString("IDNO");
				state_info.card_no = jsonData.getString("BankCard");
				state_info.bank = jsonData.getString("Bank");
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			state_info = null;
		}

		return state_info;
	}

	public void UpgradeToBangKe(String name, String idno, String bankcard, String bank, String desc, String bmpPhoto, String token, AsyncHttpResponseHandler handler)
	{
		String url = "";
		AsyncHttpClient client = new AsyncHttpClient();

		try
		{
			/* make service url */
			url = STServiceData.serviceAddr + STServiceData.cmdUpgradeToBangKe;

			/* make param */
			JSONObject jsonParams = new JSONObject();
			jsonParams.put("name", name);
			jsonParams.put("identifierno", idno);
			jsonParams.put("bankcard", bankcard);
			jsonParams.put("bank", bank);
			jsonParams.put("description", desc);
			jsonParams.put("photo", bmpPhoto);
			jsonParams.put("token", token);
			StringEntity entity = new StringEntity(jsonParams.toString());

			/* call get service */
			client.setTimeout(STServiceData.connectTimeout);
			client.post(null, url, entity, "application/json", handler);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public String parseUpgradeBangKeResult(JSONObject jsonObject, STDouble retBangBi)
	{
		String retMsg = "";
		int retResult = STServiceData.ERR_FAIL;

		try {
			retResult = jsonObject.getInt("SVCC_RET");
			if (STServiceData.ERR_SUCCESS != retResult)
			{
				retMsg = jsonObject.getString("SVCC_RETMSG");
			}
			else
			{
				JSONObject jsonData = jsonObject.getJSONObject("SVCC_DATA");
				retBangBi.fVal = jsonData.getDouble("BangBi");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return retMsg;
	}

    /**
     * Call Set default Receiver
     * @param token [in], login user token
     * @param Uid [in], Receiver's Uid
     * @param handler [in/out], response handler
     */
    public void RequestSetDefaultReceiver(String token, int Uid, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdSetDefaultReceiver;
            // make param
            param.put("token", token);
            param.put("Uid", Integer.toString(Uid));
            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public String parseSetDefaultReceiver(JSONObject jsonObject, STDouble transPrice)
    {
        String retMsg = "";
        int retResult = STServiceData.ERR_FAIL;

        try {
            retResult = jsonObject.getInt("SVCC_RET");
            if (STServiceData.ERR_SUCCESS == retResult)
            {
                retMsg = "";

                JSONObject  svccData = jsonObject.getJSONObject("SVCC_DATA");
                transPrice.fVal = svccData.getDouble("TransPrice");
            } else {
                retMsg = jsonObject.getString("SVCC_RETMSG");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }

    public void RequestDrawBangbi(String token, String identifierno, String cardno, double bangbi, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try
		{
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdDrawBangbi;

            // make param
            param.put("token", token);
            param.put("identifierno", identifierno);
            param.put("cardno", cardno);
            param.put("bangbi", Double.toString(bangbi));

            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        }
		catch (Exception e)
		{
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public String parseDrawBangbi(JSONObject jsonObject)
    {
        String retMsg = "";
        int retResult = STServiceData.ERR_FAIL;

        try {
            retResult = jsonObject.getInt("SVCC_RET");
            if (STServiceData.ERR_SUCCESS != retResult)
            {
                retMsg = jsonObject.getString("SVCC_RETMSG");
            }
            else
            {
                GlobalData.token = jsonObject.getString("SVCC_TOKEN");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return retMsg;
    }

    /**
     * Call IsValidVerifKey Service
     * @param phone [in], Phone Number
     * @param vkey [in], Verification Key
     * @param handler [in/out], response handler
     */
    public void RequestIsValidVerifKey(String phone, String vkey, AsyncHttpResponseHandler handler)
    {
        String url = "";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams param = new RequestParams();

        try {
            // make service url
            url = STServiceData.serviceAddr + STServiceData.cmdIsValidVerifKey;
            // make param
            param.put("phone", phone);
            param.put("vkey", vkey);
            // call post service
            client.setTimeout(STServiceData.connectTimeout);
            client.get(url, param, handler);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public String parseRequestIsValidVerifKey(JSONObject jsonObject)
    {
        String retMsg = "";
        //String basePath = "";
        int retResult = STServiceData.ERR_FAIL;

        try
        {
            retResult = jsonObject.getInt("SVCC_RET");
            //basePath = jsonObject.getString("SVCC_BASEURL");
            if (STServiceData.ERR_SUCCESS != retResult)
            {
                retMsg = jsonObject.getString("SVCC_RETMSG");
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return retMsg;
    }
}
