package com.rots.contract;

import java.util.Date;

public class ProductCountDetails {

	private Integer machineId;
	private String machineName;
	private Date date;
	private Integer shiftId;
	private Integer productId;
	private String productName;
	private Integer targetCount;
	private Integer scrapCount;
	private String dateStr;
	private String shiftName;
	
	
	public Integer getMachineId() {
		return machineId;
	}
	public void setMachineId(Integer machineId) {
		this.machineId = machineId;
	}
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
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
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
	public String getMachineName() {
		return machineName;
	}
	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getDateStr() {
		return dateStr;
	}
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
	public String getShiftName() {
		return shiftName;
	}
	public void setShiftName(String shiftName) {
		this.shiftName = shiftName;
	}
	
	
}
