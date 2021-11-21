package com.rots.contract;

import java.util.List;

import com.rots.entity.RotsReasonMaster;

public class ReasonresponseDtls {

	PaginationDtls paginationDtls;
	List<StoppageReasonDtls> listOFStoppageReasonDtls;
	List<StoppageReasonDtls> search;
	
	public PaginationDtls getPaginationDtls() {
		return paginationDtls;
	}
	public void setPaginationDtls(PaginationDtls paginationDtls) {
		this.paginationDtls = paginationDtls;
	}
	public List<StoppageReasonDtls> getListOFStoppageReasonDtls() {
		return listOFStoppageReasonDtls;
	}
	public void setListOFStoppageReasonDtls(List<StoppageReasonDtls> listOFStoppageReasonDtls) {
		this.listOFStoppageReasonDtls = listOFStoppageReasonDtls;
	}
	public List<StoppageReasonDtls> getSearch() {
		return search;
	}
	public void setSearch(List<StoppageReasonDtls> search) {
		this.search = search;
	}
	
	
}
