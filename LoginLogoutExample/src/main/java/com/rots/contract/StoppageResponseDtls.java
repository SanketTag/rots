package com.rots.contract;

import java.util.List;

public class StoppageResponseDtls {

	PaginationDtls paginationDtls;
	List<ScheduledStopDtls> listOfStoppageDtls;
	
	public PaginationDtls getPaginationDtls() {
		return paginationDtls;
	}
	public void setPaginationDtls(PaginationDtls paginationDtls) {
		this.paginationDtls = paginationDtls;
	}
	public List<ScheduledStopDtls> getListOfStoppageDtls() {
		return listOfStoppageDtls;
	}
	public void setListOfStoppageDtls(List<ScheduledStopDtls> listOfStoppageDtls) {
		this.listOfStoppageDtls = listOfStoppageDtls;
	}
	
}
