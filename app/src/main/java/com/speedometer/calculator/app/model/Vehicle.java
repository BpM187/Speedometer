package com.speedometer.calculator.app.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Vehicle implements Parcelable {

    private int id;
    private GeneralInfo generalInfo;
    private VolumeWeights volumeWeights;
    private Dimensions dimensions;
    private Performance performance;
    private Engine engine;
    private String coefficient;

    public transient  boolean selected;
    public transient boolean maximized = true;


    public Vehicle() {

    }


    protected Vehicle(Parcel in) {
        id = in.readInt();
        generalInfo = in.readParcelable(GeneralInfo.class.getClassLoader());
        volumeWeights = in.readParcelable(VolumeWeights.class.getClassLoader());
        dimensions = in.readParcelable(Dimensions.class.getClassLoader());
        performance = in.readParcelable(Performance.class.getClassLoader());
        engine = in.readParcelable(Engine.class.getClassLoader());
        coefficient = in.readString();
    }

    public static final Creator<Vehicle> CREATOR = new Creator<Vehicle>() {
        @Override
        public Vehicle createFromParcel(Parcel in) {
            return new Vehicle(in);
        }

        @Override
        public Vehicle[] newArray(int size) {
            return new Vehicle[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public GeneralInfo getGeneralInfo() {
        return generalInfo;
    }

    public void setGeneralInfo(GeneralInfo generalInfo) {
        this.generalInfo = generalInfo;
    }

    public VolumeWeights getVolumeWeights() {
        return volumeWeights;
    }

    public void setVolumeWeights(VolumeWeights volumeWeights) {
        this.volumeWeights = volumeWeights;
    }

    public Dimensions getDimensions() {
        return dimensions;
    }

    public void setDimensions(Dimensions dimensions) {
        this.dimensions = dimensions;
    }

    public Performance getPerformance() {
        return performance;
    }

    public void setPerformance(Performance performance) {
        this.performance = performance;
    }

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public String getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(String coefficient) {
        this.coefficient = coefficient;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isMaximized() {
        return maximized;
    }

    public void setMaximized(boolean maximized) {
        this.maximized = maximized;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeParcelable(generalInfo, i);
        parcel.writeParcelable(volumeWeights, i);
        parcel.writeParcelable(dimensions, i);
        parcel.writeParcelable(performance, i);
        parcel.writeParcelable(engine, i);
        parcel.writeString(coefficient);
    }
}
