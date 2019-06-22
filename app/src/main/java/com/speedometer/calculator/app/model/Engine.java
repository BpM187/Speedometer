package com.speedometer.calculator.app.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Engine implements Parcelable {
    private String power;
    private String torque;

    public Engine() {
    }

    protected Engine(Parcel in) {
        power = in.readString();
        torque = in.readString();
    }

    public static final Creator<Engine> CREATOR = new Creator<Engine>() {
        @Override
        public Engine createFromParcel(Parcel in) {
            return new Engine(in);
        }

        @Override
        public Engine[] newArray(int size) {
            return new Engine[size];
        }
    };

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getTorque() {
        return torque;
    }

    public void setTorque(String torque) {
        this.torque = torque;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(power);
        parcel.writeString(torque);
    }
}
