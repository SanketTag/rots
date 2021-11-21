package com.rots.contract;

import java.util.Date;


public class StoppageReasonDtls {

	private Integer reasonId;
	private String reasonTitle;
	private String reasonDesc;
	private String createdByUser;
	private String reasonType;
	
	
	public Integer getReasonId() {
		return reasonId;
	}
	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}
	public String getReasonDesc() {
		return reasonDesc;
	}
	public void setReasonDesc(String reasonDesc) {
		this.reasonDesc = reasonDesc;
	}
	public String getCreatedByUser() {
		return createdByUser;
	}
	public void setCreatedByUser(String createdByUser) {
		this.createdByUser = createdByUser;
	}
	public String getReasonTitle() {
		return reasonTitle;
	}
	public void setReasonTitle(String reasonTitle) {
		this.reasonTitle = reasonTitle;
	}
	public String getReasonType() {
		return reasonType;
	}
	public void setReasonType(String reasonType) {
		this.reasonType = reasonType;
	}
	
	
}
