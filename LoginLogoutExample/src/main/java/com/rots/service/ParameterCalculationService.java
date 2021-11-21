package com.rots.service;

import java.util.List;

import com.rots.entity.RotsProductMaster;


public interface ParameterCalculationService {
	public List<RotsProductMaster> getDistinctProductIds();
	public void processRecords();
	public RotsProductMaster getProductMasterByProductId(Integer productId);
	public void processRecordsTest();
}
