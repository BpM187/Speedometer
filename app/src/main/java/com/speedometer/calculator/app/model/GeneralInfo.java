package com.speedometer.calculator.app.model;

import android.os.Parcel;
import android.os.Parcelable;

public class GeneralInfo implements Parcelable {

    private String brand;
    private String model;
    private byte[] photoBrand;
    private byte[] photoModel;
    private String generation;
    private String changeMotorType;

    public GeneralInfo() {
    }

    protected GeneralInfo(Parcel in) {
        brand = in.readString();
        model = in.readString();
        photoBrand = in.createByteArray();
        photoModel = in.createByteArray();
        generation = in.readString();
        changeMotorType = in.readString();
    }

    public static final Creator<GeneralInfo> CREATOR = new Creator<GeneralInfo>() {
        @Override
        public GeneralInfo createFromParcel(Parcel in) {
            return new GeneralInfo(in);
        }

        @Override
        public GeneralInfo[] newArray(int size) {
            return new GeneralInfo[size];
        }
    };

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public byte[] getPhotoBrand() {
        return photoBrand;
    }

    public void setPhotoBrand(byte[] photoBrand) {
        this.photoBrand = photoBrand;
    }

    public byte[] getPhotoModel() {
        return photoModel;
    }

    public void setPhotoModel(byte[] photoModel) {
        this.photoModel = photoModel;
    }

    public String getGeneration() {
        return generation;
    }

    public void setGeneration(String generation) {
        this.generation = generation;
    }


    public String getChangeMotorType() {
        return changeMotorType;
    }

    public void setChangeMotorType(String changeMotorType) {
        this.changeMotorType = changeMotorType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(brand);
        parcel.writeString(model);
        parcel.writeByteArray(photoBrand);
        parcel.writeByteArray(photoModel);
        parcel.writeString(generation);
        parcel.writeString(changeMotorType);
    }
}
