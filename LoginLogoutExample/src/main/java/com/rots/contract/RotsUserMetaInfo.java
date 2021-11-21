package com.rots.contract;

import com.rots.entity.RotsUserRoles;

public class RotsUserMetaInfo {

	private Integer userId;
	private String userName;
	private RotsUserRoles rotsUserRoles;
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public RotsUserRoles getRotsUserRoles() {
		return rotsUserRoles;
	}
	public void setRotsUserRoles(RotsUserRoles rotsUserRoles) {
		this.rotsUserRoles = rotsUserRoles;
	}
	
	
	


}
