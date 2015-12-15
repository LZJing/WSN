package com.buaa.sensory.wsn_dr.model;

import java.util.List;

public class ResponseGPSMsg {
    private boolean result;
    private String msg;
    private GPSData data;
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public GPSData getData() {
		return data;
	}
	public void setData(GPSData data) {
		this.data = data;
	}

    

}
