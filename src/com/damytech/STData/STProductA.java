package com.damytech.STData;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-26
 * Time: 下午11:31
 * To change this template use File | Settings | File Templates.
 */
public class STProductA implements Parcelable {
    public int  uid;
    public String  name;
    public int  count;
    public double  price;
    public String  image;

    public String  userData;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(uid);
        dest.writeString(name);
        dest.writeInt(count);
        dest.writeDouble(price);
        dest.writeString(image);
        dest.writeString(userData);
    }

    public static final Parcelable.Creator<STProductA> CREATOR = new Parcelable.Creator<STProductA>() {
        @Override
        public STProductA createFromParcel(Parcel source) {
            return new STProductA(source);
        }

        @Override
        public STProductA[] newArray(int size) {
            return new STProductA[size];
        }
    };

    public STProductA () {}

    private STProductA ( Parcel src ) {
        uid = src.readInt();
        name = src.readString();
        count = src.readInt();
        price = src.readDouble();
        image = src.readString();
        userData = src.readString();
    }
}
