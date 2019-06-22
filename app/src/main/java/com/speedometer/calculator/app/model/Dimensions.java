package com.speedometer.calculator.app.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Dimensions implements Parcelable {

    private String length;
    private String width;
    private String widthWithMirrors;
    private String height;
    private String wheelbase;
    private String gaugeFront;
    private String gaugeBack;

    public Dimensions() {
    }

    protected Dimensions(Parcel in) {
        length = in.readString();
        width = in.readString();
        widthWithMirrors = in.readString();
        height = in.readString();
        wheelbase = in.readString();
        gaugeFront = in.readString();
        gaugeBack = in.readString();
    }

    public static final Creator<Dimensions> CREATOR = new Creator<Dimensions>() {
        @Override
        public Dimensions createFromParcel(Parcel in) {
            return new Dimensions(in);
        }

        @Override
        public Dimensions[] newArray(int size) {
            return new Dimensions[size];
        }
    };

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getWidthWithMirrors() {
        return widthWithMirrors;
    }

    public void setWidthWithMirrors(String widthWithMirrors) {
        this.widthWithMirrors = widthWithMirrors;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWheelbase() {
        return wheelbase;
    }

    public void setWheelbase(String wheelbase) {
        this.wheelbase = wheelbase;
    }

    public String getGaugeFront() {
        return gaugeFront;
    }

    public void setGaugeFront(String gaugeFront) {
        this.gaugeFront = gaugeFront;
    }

    public String getGaugeBack() {
        return gaugeBack;
    }

    public void setGaugeBack(String gaugeBack) {
        this.gaugeBack = gaugeBack;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(length);
        parcel.writeString(width);
        parcel.writeString(widthWithMirrors);
        parcel.writeString(height);
        parcel.writeString(wheelbase);
        parcel.writeString(gaugeFront);
        parcel.writeString(gaugeBack);
    }
}
