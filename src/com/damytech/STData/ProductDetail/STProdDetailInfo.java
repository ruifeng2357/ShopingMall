package com.damytech.STData.ProductDetail;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Yunsungil
 * Date: 11/25/13
 * Time: 3:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class STProdDetailInfo {
    public Integer  id;
    public Integer  cid;
    public String   name;
    public Integer  specData1Id;
    public String   specdata1Name;
    public ArrayList<STProdSpecData> arrSpecData1 = new ArrayList<STProdSpecData>(0);
    public Integer  specData2Id;
    public String   specdata2Name;
    public ArrayList<STProdSpecData> arrSpecData2 = new ArrayList<STProdSpecData>(0);
    public ArrayList<STValidSpecPair> arrValidSpecPairs = new ArrayList<STValidSpecPair>(0);
    public String   shopName;
    public String   shopAddr;
    public String   shopActivity;
    public Double   latitude;
    public Double   longitude;
    public String   intro;       // html string
    public double   commentRate;
    public double   goodRate;
    public double   mediumRate;
    public double   badRate;
    public ArrayList<STProdComment> arrComments = new ArrayList<STProdComment>(0);
    public ArrayList<STProdConsult> arrConsult = new ArrayList<STProdConsult>(0);
    public ArrayList<String> arrImgUrl = new ArrayList<String>(0);
}
