package com.damytech.STData;

import android.os.Parcel;
import android.os.Parcelable;
import com.damytech.STData.ShoppingCart.STBasketItemInfo;

/**
 * Created with IntelliJ IDEA.
 * User: KimOC
 * Date: 11/29/13
 * Time: 11:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class STParcelableBasketItem implements Parcelable {
    private final STBasketItemInfo itemInfo;

    private STParcelableBasketItem(Parcel parcel)
    {
        itemInfo = new STBasketItemInfo();
        itemInfo.pid = parcel.readInt();
        itemInfo.spec1 = parcel.readInt();
        itemInfo.spec2 = parcel.readInt();
        itemInfo.name= parcel.readString();
        itemInfo.count = parcel.readInt();
        itemInfo.price = parcel.readDouble();
        itemInfo.image = parcel.readString();
    }

    public STParcelableBasketItem(STBasketItemInfo item)
    {
        this.itemInfo = item;
    }

    public STBasketItemInfo GetItem()
    {
        return itemInfo;
    }

    @Override
    public int describeContents() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    // This is the method where you disassembly your object to pieces
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(itemInfo.pid);
        parcel.writeInt(itemInfo.spec1);
        parcel.writeInt(itemInfo.spec2);
        parcel.writeString(itemInfo.name);
        parcel.writeInt(itemInfo.count);
        parcel.writeDouble(itemInfo.price);
        parcel.writeString(itemInfo.image);
    }

    public static final Creator<STParcelableBasketItem> CREATOR = new Creator<STParcelableBasketItem>() {

        // And here you create a new instance from a parcel using the first constructor
        @Override
        public STParcelableBasketItem createFromParcel(Parcel parcel) {
            return new STParcelableBasketItem(parcel);
        }

        @Override
        public STParcelableBasketItem[] newArray(int size) {
            return new STParcelableBasketItem[size];
        }

    };
}
