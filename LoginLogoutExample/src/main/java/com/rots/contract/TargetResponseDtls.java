package com.rots.contract;

import java.util.List;

public class TargetResponseDtls {
	
	PaginationDtls paginationDtls;
	List<TargetDetails> listOfTargetDtls;
	public PaginationDtls getPaginationDtls() {
		return paginationDtls;
	}
	public void setPaginationDtls(PaginationDtls paginationDtls) {
		this.paginationDtls = paginationDtls;
	}
	public List<TargetDetails> getListOfTargetDtls() {
		return listOfTargetDtls;
	}
	public void setListOfTargetDtls(List<TargetDetails> listOfTargetDtls) {
		this.listOfTargetDtls = listOfTargetDtls;
	}
	
	

}
