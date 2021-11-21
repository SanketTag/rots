package com.rots.contract;

import java.util.List;

public class ShiftDetails {

	private Integer shiftNumber;
	private Integer shiftId;
	private String startTime;
	private String endTime;
	private String shiftName;
	private Integer activeFlag;
	private Integer isUpdatedBreaks;
	public List<ShiftWiseBreakDetails> listOfBreaks;
	
	
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
	public String getShiftName() {
		return shiftName;
	}
	public void setShiftName(String shiftName) {
		this.shiftName = shiftName;
	}
	public List<ShiftWiseBreakDetails> getListOfBreaks() {
		return listOfBreaks;
	}
	public void setListOfBreaks(List<ShiftWiseBreakDetails> listOfBreaks) {
		this.listOfBreaks = listOfBreaks;
	}
	public Integer getActiveFlag() {
		return activeFlag;
	}
	public void setActiveFlag(Integer activeFlag) {
		this.activeFlag = activeFlag;
	}
	
	public Integer getIsUpdatedBreaks() {
		return isUpdatedBreaks;
	}
	public void setIsUpdatedBreaks(Integer isUpdatedBreaks) {
		this.isUpdatedBreaks = isUpdatedBreaks;
	}
	public Integer getShiftNumber() {
		return shiftNumber;
	}
	public void setShiftNumber(Integer shiftNumber) {
		this.shiftNumber = shiftNumber;
	}
	
	
	
}
