package com.buaa.sensory.wsn_dr.model;

/**
 * Created by LZJing on 2015/11/7.
 */
public class NodeData {
    private int dataId;
    private int nodeId;
    private int userId;
    private int parentId;
    private int hop;
    private long lastTime;
    private double reserve;
    private double light;
    private double temperature;
    private double humidity;
    private double longitude;
    private boolean isEast;
    private double latitude;
    private boolean isNorth;
    private int gpsId;

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getHop() {
        return hop;
    }

    public void setHop(int hop) {
        this.hop = hop;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public int getDataId() {
        return dataId;
    }

    public void setDataId(int dataId) {
        this.dataId = dataId;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getReserve() {
        return reserve;
    }

    public void setReserve(double reserve) {
        this.reserve = reserve;
    }

    public double getLight() {
        return light;
    }

    public void setLight(double light) {
        this.light = light;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isEast() {
        return isEast;
    }

    public void setIsEast(boolean isEast) {
        this.isEast = isEast;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public boolean isNorth() {
        return isNorth;
    }

    public void setIsNorth(boolean isNorth) {
        this.isNorth = isNorth;
    }

    public int getGpsId() {
        return gpsId;
    }

    public void setGpsId(int gpsId) {
        this.gpsId = gpsId;
    }

    @Override
    public String toString() {
        return "NodeData{" +
                "dataId=" + dataId +
                ", nodeId=" + nodeId +
                ", userId=" + userId +
                ", parentId=" + parentId +
                ", hop=" + hop +
                ", lastTime=" + lastTime +
                ", reserve=" + reserve +
                ", light=" + light +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", longitude=" + longitude +
                ", isEast=" + isEast +
                ", latitude=" + latitude +
                ", isNorth=" + isNorth +
                ", gpsId=" + gpsId +
                '}';
    }
}
