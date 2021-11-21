package com.rots.contract;

import java.util.Date;
import java.util.List;

public class ShiftwiseActivityDtls {

	List<HourlyMachineActivityDtls> listOfStatuswiseActivity;
	Integer targetCount;
	Integer scrapCount;
	Integer totalProdCount;
	Integer goodUnits;
	String scrapPercentage;
	Integer shiftId;
	String shiftName;
	String activityDate;
	public List<HourlyMachineActivityDtls> getListOfStatuswiseActivity() {
		return listOfStatuswiseActivity;
	}
	public void setListOfStatuswiseActivity(List<HourlyMachineActivityDtls> listOfStatuswiseActivity) {
		this.listOfStatuswiseActivity = listOfStatuswiseActivity;
	}
	public Integer getTargetCount() {
		return targetCount;
	}
	public void setTargetCount(Integer targetCount) {
		this.targetCount = targetCount;
	}
	public Integer getScrapCount() {
		return scrapCount;
	}
	public void setScrapCount(Integer scrapCount) {
		this.scrapCount = scrapCount;
	}
	public Integer getTotalProdCount() {
		return totalProdCount;
	}
	public void setTotalProdCount(Integer totalProdCount) {
		this.totalProdCount = totalProdCount;
	}
	public Integer getGoodUnits() {
		return goodUnits;
	}
	public void setGoodUnits(Integer goodUnits) {
		this.goodUnits = goodUnits;
	}
	public String getScrapPercentage() {
		return scrapPercentage;
	}
	public void setScrapPercentage(String scrapPercentage) {
		this.scrapPercentage = scrapPercentage;
	}
	public Integer getShiftId() {
		return shiftId;
	}
	public void setShiftId(Integer shiftId) {
		this.shiftId = shiftId;
	}
	public String getShiftName() {
		return shiftName;
	}
	public void setShiftName(String shiftName) {
		this.shiftName = shiftName;
	}
	public String getActivityDate() {
		return activityDate;
	}
	public void setActivityDate(String activityDate) {
		this.activityDate = activityDate;
	}
	
	
}
