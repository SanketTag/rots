package com.rots.service;

import java.sql.Time;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.rots.constants.RotsErrorType;
import com.rots.contract.MachineDetails;
import com.rots.contract.PaginationDtls;
import com.rots.contract.ProductCountDetails;
import com.rots.contract.ProductDetails;
import com.rots.contract.RotsUserMetaInfo;
import com.rots.contract.ScheduledStopDtls;
import com.rots.contract.ShiftDetails;
import com.rots.contract.StoppageReasonDtls;
import com.rots.contract.TargetDetails;
import com.rots.entity.RotsBreakMaster;
import com.rots.entity.RotsMachineMaster;
import com.rots.entity.RotsOperatorMaster;
import com.rots.entity.RotsProductMaster;
import com.rots.entity.RotsReasonMaster;
import com.rots.entity.RotsScheduledStopTranDtls;
import com.rots.entity.RotsShiftMaster;
import com.rots.entity.RotsUserMaster;

public interface RotsAdminService {

	public List<RotsBreakMaster> getAllbreaksBeforeTime(Date time, Integer shiftId);
	public String getDisplayName(RotsUserMaster rotsUserMaster);
	public RotsOperatorMaster getOperatorById(Integer operatorId);
	public String getDisplayName(RotsOperatorMaster rotsOperatorMaster);
	public RotsReasonMaster getReasonById(Integer reasonId);
	public boolean insertDataIntoDB(List<String> data) throws ParseException;
	public RotsMachineMaster getMachineById(Integer machineId);
	public RotsProductMaster getProductById(Integer productId);
	public RotsProductMaster getProdMasterByCode(String productCode);
	public RotsShiftMaster getShiftMasterById(Integer shiftId);
	public List<MachineDetails> getAllActiveMachines();
	public List<RotsScheduledStopTranDtls> getAllActiveScheduledStops(Integer machineId, Date startDate, Date endDate);
	public List<RotsBreakMaster> getBreakWithinTime(Date fromTime, Date toTime, Integer shiftId);
	public List<ProductDetails> getAllProductForMachine(Integer machineId);
	public RotsShiftMaster getShiftMasterByNumber(Integer shiftNumber);
	
	//Stoppage Reason Methods
	public void saveStoppageReasonDetails(StoppageReasonDtls stoppageReasonDtls, RotsUserMetaInfo rotsUserMetaInfo);
	public List<StoppageReasonDtls> getAllReasons(PaginationDtls paginationDtls);
	public void updateStoppageReasonDetails(StoppageReasonDtls stoppageReasonDtls, RotsUserMetaInfo rotsUserMetaInfo);
	public void deleteStoppageReasonDetails(StoppageReasonDtls stoppageReasonDtls,  RotsUserMetaInfo rotsUserMetaInfo);
	public StoppageReasonDtls getReasonDtlsById(Integer reasonId);
	
	//Scheduled Stoppage Methods
	public void saveScheduledStoppage(ScheduledStopDtls scheduledStopDtls, RotsUserMetaInfo rotsUserMetaInfo) throws ParseException;
	public void updateScheduledStoppage(ScheduledStopDtls scheduledStopDtls, RotsUserMetaInfo rotsUserMetaInfo) throws ParseException;
	public void deleteScheduledStoppage(ScheduledStopDtls scheduledStopDtls, RotsUserMetaInfo rotsUserMetaInfo);
	public List<ScheduledStopDtls> getAllScheduledStoppages(PaginationDtls paginationDtls);
	public ScheduledStopDtls getScheduledStoppagDtlsById(Integer stoppageId);
	public  List<StoppageReasonDtls> getReasonsByQuery(String queryStr);
	
	//Manage Shifts and Breaks
	public List<ShiftDetails> getAllShifts(Integer pageNumber, Integer pageSize);
	public void saveShiftDetails(ShiftDetails shiftDetails, RotsUserMetaInfo rotsUserMetaInfo) throws ParseException;
	public void updateShiftDetails(ShiftDetails shiftDetails, RotsUserMetaInfo rotsUserMetaInfo) throws ParseException;
	public void deleteShiftDetails(ShiftDetails shiftDetails, RotsUserMetaInfo rotsUserMetaInfo) throws ParseException;
	public void saveBreak(RotsShiftMaster rotsShiftMaster, ShiftDetails shiftDetails, RotsUserMetaInfo rotsUserMetaInfo) throws ParseException;
	public ShiftDetails getShiftDtlsById(Integer shiftId);
	
	//Validation Methods
	public RotsErrorType validateReasonDtls(StoppageReasonDtls stoppageReasonDtls);
	 public boolean validateReasonIdDtls(StoppageReasonDtls stoppageReasonDtls);
	 public RotsErrorType validateScheduledStoppageObject(ScheduledStopDtls scheduledStopDtls);
	 public RotsErrorType validateScheduledStoppageId(ScheduledStopDtls scheduledStopDtls);
	 public RotsErrorType validateShiftDetails(ShiftDetails shiftDetails) throws ParseException;
	 public RotsErrorType validateShiftAndBreakIDs(ShiftDetails shiftDetails);
	 
	 
	 //Manage Target Dtls
	 public RotsErrorType validateProductDetails(ProductCountDetails targetDetails);
	 public void saveTargetDetails(ProductCountDetails targetDetails, RotsUserMetaInfo rotsUserMetaInfo) throws ParseException;
	 public void deleteTargetDetails(ProductCountDetails targetDetails, RotsUserMetaInfo rotsUserMetaInfo) throws ParseException;
	 public List<TargetDetails> getAllTargetDetails(PaginationDtls paginationDtls);
	 public TargetDetails getTragetDtlsById(Integer targetId);
	 
	 //Manage Scrap Deetails
	 public RotsErrorType validateScrapDetails(ProductCountDetails scrapDetails);
	 public void saveScrapCountDetails(ProductCountDetails scrapCountDtls, RotsUserMetaInfo rotsUserMetaInfo) throws ParseException;
	 public void deleteScrapCountDetails(ProductCountDetails scrapCountDtls, RotsUserMetaInfo rotsUserMetaInfo) throws ParseException;
	 public List<TargetDetails> getAllScrapDetails(Integer pageNumber, Integer pageSize);
	 
}
