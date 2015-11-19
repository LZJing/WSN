package com.buaa.sensory.wsn_dr.model;

import java.util.List;

/**
 * Created by LZJing on 2015/11/10.
 */
public class ResponseMsg {
    private boolean result;
    private String msg;
    private List<NodeData> data;

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

	public List<NodeData> getData() {
		return data;
	}

	public void setData(List<NodeData> data) {
		this.data = data;
	}

    
}
