package dj.example.main.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 09-02-2018.
 */

public class HeaderSubHeaderData implements Parcelable {

    private int viewType;
    private int cardBgColorResId;
    private String title;
    private String title1;
    private String title2;
    private int space;
    private boolean isDisplayArrow;

    private float subheaderTxtSize;
    private int subheaderTxtColor;
    private boolean isSubheaderBold;

    public HeaderSubHeaderData(int viewType) {
        this.viewType = viewType;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public int getCardBgColorResId() {
        return cardBgColorResId;
    }

    public void setCardBgColorResId(int cardBgColorResId) {
        this.cardBgColorResId = cardBgColorResId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle1() {
        return title1;
    }

    public void setTitle1(String title1) {
        this.title1 = title1;
    }

    public String getTitle2() {
        return title2;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }

    public int getSpace() {
        return space;
    }

    public void setSpace(int space) {
        this.space = space;
    }

    public boolean isDisplayArrow() {
        return isDisplayArrow;
    }

    public void setDisplayArrow(boolean displayArrow) {
        isDisplayArrow = displayArrow;
    }

    public void setSubheaderBold(boolean subheaderBold) {
        isSubheaderBold = subheaderBold;
    }

    public boolean isSubheaderBold() {
        return isSubheaderBold;
    }

    public void setSubheaderTxtColor(int subheaderTxtColor) {
        this.subheaderTxtColor = subheaderTxtColor;
    }

    public int getSubheaderTxtColor() {
        return subheaderTxtColor;
    }

    public void setSubheaderTxtSize(float subheaderTxtSize) {
        this.subheaderTxtSize = subheaderTxtSize;
    }

    public float getSubheaderTxtSize() {
        return subheaderTxtSize;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected HeaderSubHeaderData(Parcel in) {
        viewType = in.readInt();
        cardBgColorResId = in.readInt();
        title = in.readString();
        title1 = in.readString();
        title2 = in.readString();
        space = in.readInt();
        isDisplayArrow = in.readByte() != 0x00;
        subheaderTxtSize = in.readFloat();
        subheaderTxtColor = in.readInt();
        isSubheaderBold = in.readByte() != 0x00;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(viewType);
        dest.writeInt(cardBgColorResId);
        dest.writeString(title);
        dest.writeString(title1);
        dest.writeString(title2);
        dest.writeInt(space);
        dest.writeByte((byte) (isDisplayArrow ? 0x01 : 0x00));
        dest.writeFloat(subheaderTxtSize);
        dest.writeInt(subheaderTxtColor);
        dest.writeByte((byte) (isSubheaderBold ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Creator<HeaderSubHeaderData> CREATOR = new Creator<HeaderSubHeaderData>() {
        @Override
        public HeaderSubHeaderData createFromParcel(Parcel in) {
            return new HeaderSubHeaderData(in);
        }

        @Override
        public HeaderSubHeaderData[] newArray(int size) {
            return new HeaderSubHeaderData[size];
        }
    };
}
