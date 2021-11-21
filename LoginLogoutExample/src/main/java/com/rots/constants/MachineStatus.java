package com.rots.constants;

public enum MachineStatus {

	RUNNING(5,"RUNNING"),
	STOP(6, "STOP"),
	IDLE(7, "IDLE"),
	OFFLINE(8,"OFFLINE");
	
	private Integer statusId;
	private String statusMsg;
	
	private MachineStatus(Integer statusId, String statusMsg){
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
}
