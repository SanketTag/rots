package com.rots.contract;

import java.util.List;

public class LiveMachineReportWrapper {
	
	
	List<LiveMachineStatusDetails> listOfLiveMachinedata;
	List<MachineCountMap> machineCountMap;
	
	
	public List<LiveMachineStatusDetails> getListOfLiveMachinedata() {
		return listOfLiveMachinedata;
	}
	public void setListOfLiveMachinedata(List<LiveMachineStatusDetails> listOfLiveMachinedata) {
		this.listOfLiveMachinedata = listOfLiveMachinedata;
	}
	public List<MachineCountMap> getMachineCountMap() {
		return machineCountMap;
	}
	public void setMachineCountMap(List<MachineCountMap> machineCountMap) {
		this.machineCountMap = machineCountMap;
	}
	
}
