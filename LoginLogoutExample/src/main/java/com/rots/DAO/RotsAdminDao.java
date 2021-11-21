package com.rots.DAO;

import java.sql.Time;
import java.util.Date;
import java.util.List;

import com.rots.contract.PaginationDtls;
import com.rots.entity.RotsBreakMaster;
import com.rots.entity.RotsMachineDataDetails;
import com.rots.entity.RotsMachineDataTest;
import com.rots.entity.RotsMachineLiveData;
import com.rots.entity.RotsMachineMaster;
import com.rots.entity.RotsOeeDetails;
import com.rots.entity.RotsOperatorMaster;
import com.rots.entity.RotsProductMaster;
import com.rots.entity.RotsReasonMaster;
import com.rots.entity.RotsScheduledStopTranDtls;
import com.rots.entity.RotsScrapCountDetails;
import com.rots.entity.RotsShiftMaster;
import com.rots.entity.RotsTargetDetails;
import com.rots.entity.RotsUserMaster;


public interface RotsAdminDao {

	
	public void saveRotsReasonMaster(RotsReasonMaster rotsReasonMaster);
	public List<RotsReasonMaster> getAllReasons(PaginationDtls paginationDtls);
	public void saveRotsScheduledStopTranDtls(RotsScheduledStopTranDtls rotsScheduledStopTranDtls);
	public void saveOrUpdateRotsReasonMaster(RotsScheduledStopTranDtls rotsScheduledStopTranDtls);
	public List<RotsScheduledStopTranDtls> getAllScheduledStops(PaginationDtls paginationDtls);
	public RotsUserMaster getUserByUserId(Integer userId);
	public List<RotsScheduledStopTranDtls> getAllActiveScheduledStops(Integer machineId, Date startDate, Date endDate);
	public List<RotsBreakMaster> getAllbreaksBeforeTime(Date time, Integer shiftId);
	public RotsOperatorMaster getOperatorById(Integer operatorId);
	public RotsReasonMaster getReasonById(Integer reasonId);
	public void saveRotsMachineDataDetails(RotsMachineDataDetails rotsMachineDataDetails);
	public RotsProductMaster getProdMasterById(Integer productId);
	public void saveRotsMachineDataTest(RotsMachineDataTest rotsMachineDataTest);
	public void saveRotsMachineLiveData(RotsMachineLiveData rotsMachineLiveData);
	public RotsMachineMaster getMachineById(Integer machineId);
	public RotsProductMaster getProductById(Integer productId);
	public void mergeRotsReasonMaster(RotsReasonMaster rotsReasonMaster);
	public RotsScheduledStopTranDtls getRotsScheduledStopTranDtlsByIdd(Integer stoppageId);
	public void mergeRotsScheduledStopTranDtls(RotsScheduledStopTranDtls rotsScheduledStopTranDtls);
	public RotsProductMaster getProdMasterByCode(String productCode);
	public List<RotsMachineMaster> getAllActiveMachines();
	public List<RotsShiftMaster> getOverlappingShift(Date startTime, Date endTime);
	public RotsReasonMaster getReasonByTitle(String reasonTitle);;
	public List<RotsBreakMaster> getBreakWithinTime(Date fromTime, Date toTime, Integer shiftId);
	public List<RotsProductMaster> getAllProductForMachine(Integer machineId);
	public void saveRotsTargetDetails(RotsTargetDetails rotsTargetDetails);
	public void mergeRotsTargetDetails(RotsTargetDetails rotsTargetDetails);
	public List<RotsTargetDetails> getTargetDetailsForDate(Date date, PaginationDtls paginationDtls);
	public RotsShiftMaster getShiftMasterByNumber(Integer shiftNumber);
	
	public RotsScrapCountDetails getScrapCountDetails(Date date, Integer shiftId, Integer machineId, Integer productId);
	public void saveRotsScrapDetails(RotsScrapCountDetails rotsScrapCountDetails);
	public void mergeRotsScrapDetails(RotsScrapCountDetails rotsScrapCountDetails);
	
	public List<RotsScrapCountDetails> getScrapDetailsForDate(Date date, Integer pageNumber, Integer pageSize);
	
	
	//Manage Shift APIs
	public Integer saveRotsShiftMaster(RotsShiftMaster rotsShiftMaster);
	public void mergeRotsShiftMaster(RotsShiftMaster rotsShiftMaster);
	public List<RotsShiftMaster> getrAllShifts(Integer pageNumber, Integer pageSize);
	public List<RotsBreakMaster> getAllBreaksForShift(Integer shiftId);
	public RotsShiftMaster getShiftMasterById(Integer shiftId);
	public void saveRotsBreakMaster(RotsBreakMaster rotsBreakMaster);
	public void mergeRotsBreakMaster(RotsBreakMaster rotsBreakMaster);
	public RotsBreakMaster getBreakMasterById(Integer breakId);
	public RotsTargetDetails getTargetDetails(Date date, Integer shiftId, Integer machineId, Integer productId);
	public RotsTargetDetails getTargetDetailsById(Integer targetId);
	public  List<RotsReasonMaster> getReasonsByQuery(String queryStr);
	
}
