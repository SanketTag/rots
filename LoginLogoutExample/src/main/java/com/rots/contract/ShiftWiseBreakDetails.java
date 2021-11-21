package com.rots.contract;

public class ShiftWiseBreakDetails {

	private Integer breakId;
	private Integer breakType;
	private String breakName;
	private Integer shiftId;
	private String shiftName;
	private String startTime;
	private String endTime;
	private String totalTime;
	private Integer activeFlag;
	private Integer isUpdatedBreak;
	public Integer getBreakId() {
		return breakId;
	}
	public void setBreakId(Integer breakId) {
		this.breakId = breakId;
	}
	
	
	public Integer getBreakType() {
		return breakType;
	}
	public void setBreakType(Integer breakType) {
		this.breakType = breakType;
	}
	public String getShiftName() {
		return shiftName;
	}
	public void setShiftName(String shiftName) {
		this.shiftName = shiftName;
	}
	public String getBreakName() {
		return breakName;
	}
	public void setBreakName(String breakName) {
		this.breakName = breakName;
	}
	public Integer getShiftId() {
		return shiftId;
	}
	public void setShiftId(Integer shiftId) {
		this.shiftId = shiftId;
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
	public String getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}
	public Integer getActiveFlag() {
		return activeFlag;
	}
	public void setActiveFlag(Integer activeFlag) {
		this.activeFlag = activeFlag;
	}
	public Integer getIsUpdatedBreak() {
		return isUpdatedBreak;
	}
	public void setIsUpdatedBreak(Integer isUpdatedBreak) {
		this.isUpdatedBreak = isUpdatedBreak;
	}
	
	
}
