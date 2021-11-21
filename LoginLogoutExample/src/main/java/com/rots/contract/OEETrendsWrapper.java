package com.rots.contract;

import java.util.List;

public class OEETrendsWrapper {

	List<OEEDetails> listOfMachineOEE;
	List<KeyValueWrapper> listOFAvgOEEParams;
	
	public List<OEEDetails> getListOfMachineOEE() {
		return listOfMachineOEE;
	}
	public void setListOfMachineOEE(List<OEEDetails> listOfMachineOEE) {
		this.listOfMachineOEE = listOfMachineOEE;
	}
	public List<KeyValueWrapper> getListOFAvgOEEParams() {
		return listOFAvgOEEParams;
	}
	public void setListOFAvgOEEParams(List<KeyValueWrapper> listOFAvgOEEParams) {
		this.listOFAvgOEEParams = listOFAvgOEEParams;
	}
	
	
}
