package com.speedometer.calculator.app.model;

public class Result {

    private long time;
    private double speed;
    private double altitude;
    private double distance;
    private double Rr;
    private double Ra;
    private double Rd;
    private double Rp;
    private double SumR;
    private double Pr;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getRr() {
        return Rr;
    }

    public void setRr(double rr) {
        Rr = rr;
    }

    public double getRa() {
        return Ra;
    }

    public void setRa(double ra) {
        Ra = ra;
    }

    public double getRd() {
        return Rd;
    }

    public void setRd(double rd) {
        Rd = rd;
    }

    public double getRp() {
        return Rp;
    }

    public void setRp(double rp) {
        Rp = rp;
    }

    public double getSumR() {
        return SumR;
    }

    public void setSumR(double sumR) {
        SumR = sumR;
    }

    public double getPr() {
        return Pr;
    }

    public void setPr(double pr) {
        Pr = pr;
    }
}
