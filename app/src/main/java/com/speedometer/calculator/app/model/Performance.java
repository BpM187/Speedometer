package com.speedometer.calculator.app.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Performance implements Parcelable {
    private String fuelConsume;
    private String fuelType;
    private String acceleration0to100;
    private String maximSpeed;

    public Performance() {
    }

    protected Performance(Parcel in) {
        fuelConsume = in.readString();
        fuelType = in.readString();
        acceleration0to100 = in.readString();
        maximSpeed = in.readString();
    }

    public static final Creator<Performance> CREATOR = new Creator<Performance>() {
        @Override
        public Performance createFromParcel(Parcel in) {
            return new Performance(in);
        }

        @Override
        public Performance[] newArray(int size) {
            return new Performance[size];
        }
    };

    public String getFuelConsume() {
        return fuelConsume;
    }

    public void setFuelConsume(String fuelConsume) {
        this.fuelConsume = fuelConsume;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getAcceleration0to100() {
        return acceleration0to100;
    }

    public void setAcceleration0to100(String acceleration0to100) {
        this.acceleration0to100 = acceleration0to100;
    }

    public String getMaximSpeed() {
        return maximSpeed;
    }

    public void setMaximSpeed(String maximSpeed) {
        this.maximSpeed = maximSpeed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(fuelConsume);
        parcel.writeString(fuelType);
        parcel.writeString(acceleration0to100);
        parcel.writeString(maximSpeed);
    }
}
