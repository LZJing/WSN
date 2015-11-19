package com.buaa.sensory.wsn_dr.model;

/**
 * Created by LZJing on 2015/11/7.
 */
public class GPSData {

    private int gpsId;
    private long dateTime;
    private double gpsLo;
    private boolean gpsIsEast;
    private double gpsLa;
    private boolean gpsIsNorth;
    private double altitude;
    private double speed;

    public int getGpsId() {
        return gpsId;
    }

    public void setGpsId(int gpsId) {
        this.gpsId = gpsId;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public double getGpsLo() {
        return gpsLo;
    }

    public void setGpsLo(double gpsLo) {
        this.gpsLo = gpsLo;
    }

    public boolean isGpsIsEast() {
        return gpsIsEast;
    }

    public void setGpsIsEast(boolean gpsIsEast) {
        this.gpsIsEast = gpsIsEast;
    }

    public double getGpsLa() {
        return gpsLa;
    }

    public void setGpsLa(double gpsLa) {
        this.gpsLa = gpsLa;
    }

    public boolean isGpsIsNorth() {
        return gpsIsNorth;
    }

    public void setGpsIsNorth(boolean gpsIsNorth) {
        this.gpsIsNorth = gpsIsNorth;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "GPSData{" +
                "gpsId=" + gpsId +
                ", dateTime=" + dateTime +
                ", gpsLo=" + gpsLo +
                ", gpsIsEast=" + gpsIsEast +
                ", gpsLa=" + gpsLa +
                ", gpsIsNorth=" + gpsIsNorth +
                ", altitude=" + altitude +
                ", speed=" + speed +
                '}';
    }
}
