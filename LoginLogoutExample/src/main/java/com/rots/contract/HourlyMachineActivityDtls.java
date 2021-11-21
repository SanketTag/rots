package com.rots.contract;

import java.util.Date;
import java.util.List;

public class HourlyMachineActivityDtls {

	private Integer statusId;
	private String shiftNumber;
	private Integer shiftId;
	private String status;
	private String totalTime;
	private Date recordDate;
	private String totalProdCount;
	private String avgCycleTime;
	private String fromTime;
	private String toTime;
	
	List<StatusDetails> listOfStatusDtls;
	
	
	
	public Integer getStatusId() {
		return statusId;
	}
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}
	public List<StatusDetails> getListOfStatusDtls() {
		return listOfStatusDtls;
	}
	public void setListOfStatusDtls(List<StatusDetails> listOfStatusDtls) {
		this.listOfStatusDtls = listOfStatusDtls;
	}
	public Integer getShiftId() {
		return shiftId;
	}
	public void setShiftId(Integer shiftId) {
		this.shiftId = shiftId;
	}
	public Date getRecordDate() {
		return recordDate;
	}
	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}
	public String getTotalProdCount() {
		return totalProdCount;
	}
	public void setTotalProdCount(String totalProdCount) {
		this.totalProdCount = totalProdCount;
	}
	public String getAvgCycleTime() {
		return avgCycleTime;
	}
	public void setAvgCycleTime(String avgCycleTime) {
		this.avgCycleTime = avgCycleTime;
	}
	public String getShiftNumber() {
		return shiftNumber;
	}
	public void setShiftNumber(String shiftNumber) {
		this.shiftNumber = shiftNumber;
	}
	public String getFromTime() {
		return fromTime;
	}
	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}
	public String getToTime() {
		return toTime;
	}
	public void setToTime(String toTime) {
		this.toTime = toTime;
	}
	
	
	
	
	
}
