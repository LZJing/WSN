package com.buaa.sensory.wsn_dr.entity;

public class NodeClass {
	int id;
	String time;
	double GPSLong;
	boolean GPSeast;
	double GPSLati;
	boolean GPSnorth;
	double high;
	double speed;
	double longi;
	boolean east;
	double lati;
	boolean north;
	double longiError;
	double latiError;
	int turnnode;// 转发节点id
	float tempture;

	public void setTurnnode(int turnnode) {
		this.turnnode = turnnode;
	}

	public int getTurnnode() {
		return turnnode;
	}

	public NodeClass() {
		super();
	}

	public NodeClass(int id, String time, double gPSLong, boolean gPSeast,
			double gPSLati, boolean gPSnorth, double high, double speed,
			double longi, boolean east, double lati, boolean north) {
		super();
		this.id = id;
		this.time = time;
		GPSLong = gPSLong;
		GPSeast = gPSeast;
		GPSLati = gPSLati;
		GPSnorth = gPSnorth;
		this.high = high;
		this.speed = speed;
		this.longi = longi;
		this.east = east;
		this.lati = lati;
		this.north = north;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public double getGPSLong() {
		return GPSLong;
	}

	public void setGPSLong(double gPSLong) {
		GPSLong = gPSLong;
	}

	public boolean isGPSeast() {
		return GPSeast;
	}

	public void setGPSeast(boolean gPSeast) {
		GPSeast = gPSeast;
	}

	public double getGPSLati() {
		return GPSLati;
	}

	public void setGPSLati(double gPSLati) {
		GPSLati = gPSLati;
	}

	public boolean isGPSnorth() {
		return GPSnorth;
	}

	public void setGPSnorth(boolean gPSnorth) {
		GPSnorth = gPSnorth;
	}

	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getLongi() {
		return longi;
	}

	public void setLongi(double longi) {
		this.longi = longi;
	}

	public boolean isEast() {
		return east;
	}

	public void setEast(boolean east) {
		this.east = east;
	}

	public double getLati() {
		return lati;
	}

	public void setLati(double lati) {
		this.lati = lati;
	}

	public boolean isNorth() {
		return north;
	}

	public void setNorth(boolean north) {
		this.north = north;
	}

	public double getLongiError() {
		return longiError;
	}

	public void setLongiError(double longiError) {
		this.longiError = longiError;
	}

	public double getLatiError() {
		return latiError;
	}

	public void setLatiError(double latiError) {
		this.latiError = latiError;
	}

	public float getTempture() {
		return tempture;
	}

	public void setTempture(float tempture) {
		this.tempture = tempture;
	}

}
