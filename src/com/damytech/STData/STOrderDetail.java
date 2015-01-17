package com.damytech.STData;

import com.damytech.STData.ShoppingCart.STBasketItemInfo;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: KimOC
 * Date: 11/30/13
 * Time: 12:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class STOrderDetail {

    public int  stateID;
    public String rcvName = "";
    public String rcvPhone = "";
    public String rcvAddress = "";
    public int payment = 1;
    public int rcvType = 1;
    public String  comment = "";
    public Double prodPrice = 0.0;
    public Double transPrice = 0.0;
    public ArrayList<STBasketItemInfo> arrProducts = new ArrayList<STBasketItemInfo>(0);
}
