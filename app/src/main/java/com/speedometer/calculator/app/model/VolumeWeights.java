package com.speedometer.calculator.app.model;

import android.os.Parcel;
import android.os.Parcelable;

public class VolumeWeights implements Parcelable {

    private String weight;
    private String weightMaxAuthorized;
    private String volumeMinTrunk;
    private String volumeMaxTrunk;
    private String volumeTank;
    private String adBlueTank;

    public VolumeWeights() {
    }

    protected VolumeWeights(Parcel in) {
        weight = in.readString();
        weightMaxAuthorized = in.readString();
        volumeMinTrunk = in.readString();
        volumeMaxTrunk = in.readString();
        volumeTank = in.readString();
        adBlueTank = in.readString();
    }

    public static final Creator<VolumeWeights> CREATOR = new Creator<VolumeWeights>() {
        @Override
        public VolumeWeights createFromParcel(Parcel in) {
            return new VolumeWeights(in);
        }

        @Override
        public VolumeWeights[] newArray(int size) {
            return new VolumeWeights[size];
        }
    };

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWeightMaxAuthorized() {
        return weightMaxAuthorized;
    }

    public void setWeightMaxAuthorized(String weightMaxAuthorized) {
        this.weightMaxAuthorized = weightMaxAuthorized;
    }

    public String getVolumeMinTrunk() {
        return volumeMinTrunk;
    }

    public void setVolumeMinTrunk(String volumeMinTrunk) {
        this.volumeMinTrunk = volumeMinTrunk;
    }

    public String getVolumeMaxTrunk() {
        return volumeMaxTrunk;
    }

    public void setVolumeMaxTrunk(String volumeMaxTrunk) {
        this.volumeMaxTrunk = volumeMaxTrunk;
    }

    public String getVolumeTank() {
        return volumeTank;
    }

    public void setVolumeTank(String volumeTank) {
        this.volumeTank = volumeTank;
    }

    public String getAdBlueTank() {
        return adBlueTank;
    }

    public void setAdBlueTank(String adBlueTank) {
        this.adBlueTank = adBlueTank;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(weight);
        parcel.writeString(weightMaxAuthorized);
        parcel.writeString(volumeMinTrunk);
        parcel.writeString(volumeMaxTrunk);
        parcel.writeString(volumeTank);
        parcel.writeString(adBlueTank);
    }
}
