package com.buaa.sensory.wsn_dr.model;

public class UserData {
	private int userId;
	private String userName;
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Override
	public String toString() {
		return "UserData [userId=" + userId + ", userName=" + userName + "]";
	}
	
	

}
