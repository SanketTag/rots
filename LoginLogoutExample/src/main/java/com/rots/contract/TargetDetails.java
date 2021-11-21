package com.rots.contract;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TargetDetails {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy", timezone = "Asia/Calcutta")
	private Date date;
    private String dateStr;
	private Integer shiftId;
	private String shiftName;
	List<ProductCountDetails> listOfProductWiseCount;
	private String productName;
	private String machineName;
	private Integer targetCount;
	private Integer machineId;
	private Integer targetId;
	
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
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
	public List<ProductCountDetails> getListOfProductWiseCount() {
		return listOfProductWiseCount;
	}
	public void setListOfProductWiseCount(List<ProductCountDetails> listOfProductWiseCount) {
		this.listOfProductWiseCount = listOfProductWiseCount;
	}
	public String getDateStr() {
		return dateStr;
	}
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getMachineName() {
		return machineName;
	}
	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}
	public Integer getTargetCount() {
		return targetCount;
	}
	public void setTargetCount(Integer targetCount) {
		this.targetCount = targetCount;
	}
	public Integer getMachineId() {
		return machineId;
	}
	public void setMachineId(Integer machineId) {
		this.machineId = machineId;
	}
	public Integer getTargetId() {
		return targetId;
	}
	public void setTargetId(Integer targetId) {
		this.targetId = targetId;
	}
	
	
}
