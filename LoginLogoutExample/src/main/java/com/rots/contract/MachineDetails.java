package com.rots.contract;

public class MachineDetails {

	private Integer machineId;
	private String machineName;
	private String machineType;
	private Integer totalProducts;
	private Double avgIdealCycleTime;
	public Integer getMachineId() {
		return machineId;
	}
	public void setMachineId(Integer machineId) {
		this.machineId = machineId;
	}
	public String getMachineName() {
		return machineName;
	}
	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}
	public String getMachineType() {
		return machineType;
	}
	public void setMachineType(String machineType) {
		this.machineType = machineType;
	}
	public Integer getTotalProducts() {
		return totalProducts;
	}
	public void setTotalProducts(Integer totalProducts) {
		this.totalProducts = totalProducts;
	}
	public Double getAvgIdealCycleTime() {
		return avgIdealCycleTime;
	}
	public void setAvgIdealCycleTime(Double avgIdealCycleTime) {
		this.avgIdealCycleTime = avgIdealCycleTime;
	} 
	
	
	
}
