package com.damytech.STData;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-25
 * Time: 下午9:18
 * To change this template use File | Settings | File Templates.
 */
public class STOrderByKeyword {
    public String  orderNo;
    public String  orderTime;
    public String  deliverType;

    public int  stateID;
    public String  payTime;
    public String  sndTime;
    public String  rcvTime;
    public String  cancelTime;
    public int  payType1;
    public int  payType2;
    public String  comment;
    public ArrayList<STProductA> arrProducts = new ArrayList<STProductA>(0);
}
