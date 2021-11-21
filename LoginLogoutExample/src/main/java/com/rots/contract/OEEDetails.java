package com.rots.contract;

import java.util.Date;

public class OEEDetails {

	
	private Integer machineId;
	private Date currentDate;
	private Integer totalProdCount;
	private Integer totalScrapCount;
	
	//OEE details for OEE Trends dashboard
	private Integer runningTime;
	private Integer availableTime;
	private String startTime;
	private String endTime;
	
	private Double availability;
	private Double productivity;
	private Double quality;
	private Double oee;
	private Integer shiftId;
	private String lastCalculatedTime;
	private String systemDate;
	private String oeeDate;
	private String machineName;
	
	
	
	
	public Integer getMachineId() {
		return machineId;
	}
	public void setMachineId(Integer machineId) {
		this.machineId = machineId;
	}
	public Date getCurrentDate() {
		return currentDate;
	}
	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}
	public Integer getTotalProdCount() {
		return totalProdCount;
	}
	public void setTotalProdCount(Integer totalProdCount) {
		this.totalProdCount = totalProdCount;
	}
	public Integer getTotalScrapCount() {
		return totalScrapCount;
	}
	public void setTotalScrapCount(Integer totalScrapCount) {
		this.totalScrapCount = totalScrapCount;
	}
	public Integer getRunningTime() {
		return runningTime;
	}
	public void setRunningTime(Integer runningTime) {
		this.runningTime = runningTime;
	}
	public Integer getAvailableTime() {
		return availableTime;
	}
	public void setAvailableTime(Integer availableTime) {
		this.availableTime = availableTime;
	}
	
	public Double getAvailability() {
		return availability;
	}
	public void setAvailability(Double availability) {
		this.availability = availability;
	}
	public Double getProductivity() {
		return productivity;
	}
	public void setProductivity(Double productivity) {
		this.productivity = productivity;
	}
	public Double getQuality() {
		return quality;
	}
	public void setQuality(Double quality) {
		this.quality = quality;
	}
	public Double getOee() {
		return oee;
	}
	public void setOee(Double oee) {
		this.oee = oee;
	}
	public Integer getShiftId() {
		return shiftId;
	}
	public void setShiftId(Integer shiftId) {
		this.shiftId = shiftId;
	}
	public String getLastCalculatedTime() {
		return lastCalculatedTime;
	}
	public void setLastCalculatedTime(String lastCalculatedTime) {
		this.lastCalculatedTime = lastCalculatedTime;
	}
	
	public String getMachineName() {
		return machineName;
	}
	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getSystemDate() {
		return systemDate;
	}
	public void setSystemDate(String systemDate) {
		this.systemDate = systemDate;
	}
	public String getOeeDate() {
		return oeeDate;
	}
	public void setOeeDate(String oeeDate) {
		this.oeeDate = oeeDate;
	}
	
	
	
	
}
