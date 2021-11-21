package com.rots.constants;

public enum Status {

	/*
	 * PENDING(2, "Pending"), ASSIGNED(3,"Assigned"), INPROGESS(4,"In Progress"),
	 * RESOLVED(5,"Resolved"), PENDING_FOR_APPROVAL(9,"Pending For Approval"),
	 * COMPLETED(10,"Completed"), UNDER_WARRANTY(38,"Under Warranty"),
	 * RENEWAL_DUE(39,"Renewal Due"), AMC_PENDING(40,"AMC Pending"),
	 * UNDER_AMC(41,"Under AMC"),
	 */
	
	RUNNING(5,"RUNNING"),
	STOP(6, "STOP"),
	IDLE(7, "IDLE"),
	OFFLINE(8,"OFFLINE");
	
	private Integer statusId;
	private String statusMsg;
	
	private Status(Integer statusId, String statusMsg){
		this.statusId = statusId;
		this.statusMsg = statusMsg;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getStatusMsg() {
		return statusMsg;
	}

	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}
	
	public static String getStringFromID(Integer statusId){
		if(statusId == Status.RUNNING.getStatusId()){
			return Status.RUNNING.getStatusMsg();
		}else if(statusId == Status.STOP.getStatusId()){
			return Status.STOP.getStatusMsg();
		}else if(statusId == Status.IDLE.getStatusId()){
			return Status.IDLE.getStatusMsg();
		}else if(statusId == Status.OFFLINE.getStatusId()){
			return Status.OFFLINE.getStatusMsg();
		}
		
		return "";
	}
	
	
}
