package com.rots.contract;

import java.util.Date;


public class ScheduledStopDtls {


	private Integer stoppageId;
	private StoppageReasonDtls stoppageReasonDtls;
	private String stopDescription;
	private String startDate;
	private String endDate;
	private String createdByUser;
	private String MachineName;
	private String stopLevel;
	private Integer machineId;

	
	
	public Integer getStoppageId() {
		return stoppageId;
	}
	public void setStoppageId(Integer stoppageId) {
		this.stoppageId = stoppageId;
	}
	public StoppageReasonDtls getStoppageReasonDtls() {
		return stoppageReasonDtls;
	}
	public void setStoppageReasonDtls(StoppageReasonDtls stoppageReasonDtls) {
		this.stoppageReasonDtls = stoppageReasonDtls;
	}
	public String getStopDescription() {
		return stopDescription;
	}
	public void setStopDescription(String stopDescription) {
		this.stopDescription = stopDescription;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getCreatedByUser() {
		return createdByUser;
	}
	public void setCreatedByUser(String createdByUser) {
		this.createdByUser = createdByUser;
	}
	public String getMachineName() {
		return MachineName;
	}
	public void setMachineName(String machineName) {
		MachineName = machineName;
	}
	public String getStopLevel() {
		return stopLevel;
	}
	public void setStopLevel(String stopLevel) {
		this.stopLevel = stopLevel;
	}
	public Integer getMachineId() {
		return machineId;
	}
	public void setMachineId(Integer machineId) {
		this.machineId = machineId;
	}
	
	
	
}
