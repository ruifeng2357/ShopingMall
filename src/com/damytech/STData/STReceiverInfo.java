package com.damytech.STData;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-28
 * Time: 下午4:02
 * To change this template use File | Settings | File Templates.
 */
public class STReceiverInfo implements Parcelable {
    public int  uid;
    public int  isDefault;
    public String  name;
    public String  phone;
    public String  addrDetail;
    public String  province;
    public String  city;
    public String  area;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(uid);
        dest.writeInt(isDefault);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(addrDetail);
        dest.writeString(province);
        dest.writeString(city);
        dest.writeString(area);
    }

    public static final Creator<STReceiverInfo> CREATOR = new Creator<STReceiverInfo>() {
        @Override
        public STReceiverInfo createFromParcel(Parcel source) {
            return new STReceiverInfo(source);
        }

        @Override
        public STReceiverInfo[] newArray(int size) {
            return new STReceiverInfo[size];
        }
    };

    public STReceiverInfo () {}

    private STReceiverInfo ( Parcel src ) {
        uid = src.readInt();
        isDefault = src.readInt();
        name = src.readString();
        phone = src.readString();
        addrDetail = src.readString();
        province = src.readString();
        city = src.readString();
        area = src.readString();
    }
}
