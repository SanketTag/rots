package com.rots.DAO;

import java.util.List;

import com.rots.entity.RotsMachineActivityDetails;
import com.rots.entity.RotsMachineDataDetails;
import com.rots.entity.RotsMachineDataTest;
import com.rots.entity.RotsProductMaster;



public interface ParameterCalculationDao {
	public List<RotsMachineDataDetails> getDistinctProductIds();
	public List<RotsMachineDataDetails> getUnprocessedRecords();
	public void saveRotsMachineActivityDetails(RotsMachineActivityDetails rotsMachineActivityDetails);
	public void mergeAllRotsMachineDataDetails(List<RotsMachineDataDetails> listOfRotsMachineDataDetails);
	public RotsProductMaster getProductMasterByProductId(Integer productId);
	public List<RotsMachineDataTest> getUnprocessedRecordsTest();//For testing pupose
	public void mergeAllRotsMachineDataTestDetails(List<RotsMachineDataTest> listOfRotsMachineDataDetails);
}
