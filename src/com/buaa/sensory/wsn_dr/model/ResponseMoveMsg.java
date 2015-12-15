package com.buaa.sensory.wsn_dr.model;

import java.util.List;

/**
 * Created by LZJing on 2015/11/10.
 */
public class ResponseMoveMsg {
    private boolean result;
    private String msg;
    private MoveType data;

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

	public MoveType getData() {
		return data;
	}

	public void setData(MoveType data) {
		this.data = data;
	}







    
}
