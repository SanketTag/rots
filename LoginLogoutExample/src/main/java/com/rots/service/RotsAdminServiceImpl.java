package com.rots.service;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.persistence.criteria.Order;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Predicates;
import com.rots.DAO.RotsAdminDao;
import com.rots.constants.BreakType;
import com.rots.constants.ROTSConstants;
import com.rots.constants.ReasonType;
import com.rots.constants.RotsErrorType;
import com.rots.constants.ScheduledStopLevel;
import com.rots.constants.ShiftType;
import com.rots.constants.Status;
import com.rots.contract.ProductDetails;
import com.rots.contract.RotsUserMetaInfo;
import com.rots.contract.ScheduledStopDtls;
import com.rots.contract.ShiftDetails;
import com.rots.contract.ShiftNumberPredicate;
import com.rots.contract.ShiftWiseBreakDetails;
import com.rots.contract.StoppageReasonDtls;
import com.rots.contract.TargetDetails;
import com.rots.contract.MachineDetails;
import com.rots.contract.PaginationDtls;
import com.rots.contract.ProductCountDetails;
import com.rots.entity.RotsBreakMaster;
import com.rots.entity.RotsMachineDataTest;
import com.rots.entity.RotsMachineLiveData;
import com.rots.entity.RotsMachineMaster;
import com.rots.entity.RotsOperatorMaster;
import com.rots.entity.RotsProductMaster;
import com.rots.entity.RotsReasonMaster;
import com.rots.entity.RotsScheduledStopTranDtls;
import com.rots.entity.RotsScrapCountDetails;
import com.rots.entity.RotsShiftMaster;
import com.rots.entity.RotsTargetDetails;
import com.rots.entity.RotsUserMaster;
import com.rots.entity.RotsUserRoles;
import com.rots.restController.AdminController;
import com.rots.util.DateUtils;


@Service("AdminService")
public class RotsAdminServiceImpl implements RotsAdminService{
	
	@Autowired
	private RotsAdminDao adminDao;

	private static final Logger logger = Logger.getLogger(RotsAdminServiceImpl.class);
	
	//APIs for Manage Stop Reasons
	@Transactional(propagation = Propagation.REQUIRED)
	public List<StoppageReasonDtls> getAllReasons(PaginationDtls paginationDtls){
		
		
		List<RotsReasonMaster> listOfAllResons = this.adminDao.getAllReasons(paginationDtls);
		
		List<StoppageReasonDtls> listOfReasons = new ArrayList<StoppageReasonDtls>();
		
		for (RotsReasonMaster rotsReasonMaster : listOfAllResons) {
			
			listOfReasons.add(this.constructStoppageReasonDtls(rotsReasonMaster));
		}
		
		return listOfReasons;
	}
	
	private StoppageReasonDtls constructStoppageReasonDtls(RotsReasonMaster rotsReasonMaster) {
		StoppageReasonDtls stoppageReasonDtls = new StoppageReasonDtls();
		
		stoppageReasonDtls.setReasonId(rotsReasonMaster.getId());
		stoppageReasonDtls.setReasonDesc(rotsReasonMaster.getReasonDesc());
		stoppageReasonDtls.setReasonTitle(rotsReasonMaster.getReasonTitle());
		if(null != rotsReasonMaster.getReasonType()) {
			stoppageReasonDtls.setReasonType(ReasonType.getStringFromID(rotsReasonMaster.getReasonType()));
		}
		stoppageReasonDtls.setCreatedByUser(this.getDisplayName(this.adminDao.getUserByUserId(rotsReasonMaster.getCreatedBy())));
		return stoppageReasonDtls;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public StoppageReasonDtls getReasonDtlsById(Integer reasonId) {
		RotsReasonMaster rotsReasonMaster = this.adminDao.getReasonById(reasonId);
		StoppageReasonDtls stoppageReasonDtls = null;
		if(null != rotsReasonMaster) {
			stoppageReasonDtls =  this.constructStoppageReasonDtls(rotsReasonMaster);
		}
		return stoppageReasonDtls;		
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveStoppageReasonDetails(StoppageReasonDtls stoppageReasonDtls, RotsUserMetaInfo rotsUserMetaInfo) {
		
		RotsReasonMaster rotsReasonMaster = new RotsReasonMaster();
		rotsReasonMaster.setReasonTitle(stoppageReasonDtls.getReasonTitle());
		rotsReasonMaster.setReasonDesc(stoppageReasonDtls.getReasonDesc());
		
		if(null != stoppageReasonDtls.getReasonType()) {
			rotsReasonMaster.setReasonType(ReasonType.getIDFromString(stoppageReasonDtls.getReasonType()));
		}
		rotsReasonMaster.setActiveFlag(ROTSConstants.ACTIVE.getId());
		rotsReasonMaster.setCreatedBy(rotsUserMetaInfo.getRotsUserRoles().getRotsUserMaster().getUserId());
		rotsReasonMaster.setCreatedDate(Calendar.getInstance().getTime());
		rotsReasonMaster.setUpdatedBy(rotsUserMetaInfo.getRotsUserRoles().getRotsUserMaster().getUserId());
		rotsReasonMaster.setUpdatedDate(Calendar.getInstance().getTime());
		
		this.adminDao.saveRotsReasonMaster(rotsReasonMaster);
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED)
    public RotsErrorType validateReasonDtls(StoppageReasonDtls stoppageReasonDtls){	
       
		if(null == stoppageReasonDtls.getReasonTitle() || stoppageReasonDtls.getReasonTitle().isEmpty() || null == stoppageReasonDtls.getReasonType()){
			return RotsErrorType.INCOMPLETE_DETAILS;
		}
		
		RotsReasonMaster rotsReasonMaster = this.adminDao.getReasonByTitle(stoppageReasonDtls.getReasonTitle());
		if(null != rotsReasonMaster) {
			return RotsErrorType.DUPLICATE_REASON_TITLE; 
		}
		
		return null;
		
	} 
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteStoppageReasonDetails(StoppageReasonDtls stoppageReasonDtls,  RotsUserMetaInfo rotsUserMetaInfo) {
		RotsReasonMaster rotsReasonMaster = this.adminDao.getReasonById(stoppageReasonDtls.getReasonId());
		rotsReasonMaster.setActiveFlag(ROTSConstants.INACTIVE.getId());
		rotsReasonMaster.setUpdatedBy(rotsUserMetaInfo.getRotsUserRoles().getRotsUserMaster().getUserId());
		rotsReasonMaster.setUpdatedDate(Calendar.getInstance().getTime());
		
		this.adminDao.mergeRotsReasonMaster(rotsReasonMaster);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateStoppageReasonDetails(StoppageReasonDtls stoppageReasonDtls, RotsUserMetaInfo rotsUserMetaInfo) {
		RotsReasonMaster rotsReasonMaster = this.adminDao.getReasonById(stoppageReasonDtls.getReasonId());
		rotsReasonMaster.setReasonTitle(stoppageReasonDtls.getReasonTitle());
		rotsReasonMaster.setReasonDesc(stoppageReasonDtls.getReasonDesc());
		rotsReasonMaster.setReasonType(ReasonType.getIDFromString(stoppageReasonDtls.getReasonType()));
		rotsReasonMaster.setActiveFlag(ROTSConstants.ACTIVE.getId());
		rotsReasonMaster.setUpdatedBy(rotsUserMetaInfo.getRotsUserRoles().getRotsUserMaster().getUserId());
		rotsReasonMaster.setUpdatedDate(Calendar.getInstance().getTime());
		
		this.adminDao.mergeRotsReasonMaster(rotsReasonMaster);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public  List<StoppageReasonDtls> getReasonsByQuery(String queryStr){
		
		List<RotsReasonMaster> listOfAllResons = this.adminDao.getReasonsByQuery(queryStr);
		 
		List<StoppageReasonDtls> listOfReasons = new ArrayList<StoppageReasonDtls>();
			
		for (RotsReasonMaster rotsReasonMaster : listOfAllResons) {
				
			listOfReasons.add(this.constructStoppageReasonDtls(rotsReasonMaster));
		}
			
		return listOfReasons;
			
	}
	@Transactional(propagation = Propagation.REQUIRED)
	 public boolean validateReasonIdDtls(StoppageReasonDtls stoppageReasonDtls){	
	       
			if(null != stoppageReasonDtls.getReasonId()){
				RotsReasonMaster rotsReasonMaster = this.adminDao.getReasonById(stoppageReasonDtls.getReasonId());
				
				if(null != rotsReasonMaster) {
				  return true;
				}else{
					//errorMessage = "Please provide emailID";
					//throw new ValidationException(ExceptionCode.VALIDATION_EXCEPTION.getExceptionCode(),PropertyUtils.getPrpertyFromContext(RlmsErrorType.PLEASE_PROVIDE_EMAILID.getMessage()));
					return false;
				}
			}
			
			return false;
		} 
	
	//APIs for Manage Scheduled Stoppages
	
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<ScheduledStopDtls> getAllScheduledStoppages(PaginationDtls paginationDtls){
		List<RotsScheduledStopTranDtls> listOfAllscheduledStops = this.adminDao.getAllScheduledStops(paginationDtls);
		
		List<ScheduledStopDtls> listOfStoppages = new ArrayList<ScheduledStopDtls>();
		
		for (RotsScheduledStopTranDtls rotsScheduledStopTranDtls : listOfAllscheduledStops) {
			ScheduledStopDtls scheduledStopDtls = new ScheduledStopDtls();
			
			scheduledStopDtls.setStoppageReasonDtls(this.constructStoppageReasonDtls(rotsScheduledStopTranDtls.getReasonMaster()));
			scheduledStopDtls.setStoppageId(rotsScheduledStopTranDtls.getId());
			if(null != rotsScheduledStopTranDtls.getRotsMachineMaster()) {
				scheduledStopDtls.setMachineName(rotsScheduledStopTranDtls.getRotsMachineMaster().getMachineName());
				scheduledStopDtls.setMachineId(rotsScheduledStopTranDtls.getRotsMachineMaster().getMachineId());
			}
			scheduledStopDtls.setStopDescription(rotsScheduledStopTranDtls.getReasonDesc());
			if(null != rotsScheduledStopTranDtls.getStartDate()) {
			 scheduledStopDtls.setStartDate(DateUtils.convertDateToStringWithTime(rotsScheduledStopTranDtls.getStartDate()));
			}
			if(null != rotsScheduledStopTranDtls.getEndDate()) {
				scheduledStopDtls.setEndDate(DateUtils.convertDateToStringWithTime(rotsScheduledStopTranDtls.getEndDate()));
			}
			scheduledStopDtls.setStopLevel(ScheduledStopLevel.getStringFromID(rotsScheduledStopTranDtls.getStopLevel()));
			scheduledStopDtls.setCreatedByUser(this.getDisplayName(this.adminDao.getUserByUserId(rotsScheduledStopTranDtls.getCreatedBy())));
			listOfStoppages.add(scheduledStopDtls);
		}
		
		return listOfStoppages;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public ScheduledStopDtls getScheduledStoppagDtlsById(Integer stoppageId){
		
		RotsScheduledStopTranDtls rotsScheduledStopTranDtls = this.adminDao.getRotsScheduledStopTranDtlsByIdd(stoppageId);
		ScheduledStopDtls scheduledStopDtls = new ScheduledStopDtls();
		
		if(null != rotsScheduledStopTranDtls) {	
			scheduledStopDtls.setStoppageReasonDtls(this.constructStoppageReasonDtls(rotsScheduledStopTranDtls.getReasonMaster()));
			scheduledStopDtls.setStoppageId(rotsScheduledStopTranDtls.getId());
			if(null != rotsScheduledStopTranDtls.getRotsMachineMaster()) {
				scheduledStopDtls.setMachineName(rotsScheduledStopTranDtls.getRotsMachineMaster().getMachineName());
				scheduledStopDtls.setMachineId(rotsScheduledStopTranDtls.getRotsMachineMaster().getMachineId());
			}
			scheduledStopDtls.setStopDescription(rotsScheduledStopTranDtls.getReasonDesc());
			if(null != rotsScheduledStopTranDtls.getStartDate()) {
			 scheduledStopDtls.setStartDate(DateUtils.convertDateToStringWithTime(rotsScheduledStopTranDtls.getStartDate()));
			}
			if(null != rotsScheduledStopTranDtls.getEndDate()) {
				scheduledStopDtls.setEndDate(DateUtils.convertDateToStringWithTime(rotsScheduledStopTranDtls.getEndDate()));
			}
			scheduledStopDtls.setStopLevel(ScheduledStopLevel.getStringFromID(rotsScheduledStopTranDtls.getStopLevel()));
			scheduledStopDtls.setCreatedByUser(this.getDisplayName(this.adminDao.getUserByUserId(rotsScheduledStopTranDtls.getCreatedBy())));
		}
			return scheduledStopDtls;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveScheduledStoppage(ScheduledStopDtls scheduledStopDtls, RotsUserMetaInfo rotsUserMetaInfo) throws ParseException {
		RotsScheduledStopTranDtls rotsScheduledStopTranDtls = new RotsScheduledStopTranDtls();
		
		RotsReasonMaster rotsReasonMaster = this.adminDao.getReasonById(scheduledStopDtls.getStoppageReasonDtls().getReasonId());
		if(null != rotsReasonMaster) {
			rotsScheduledStopTranDtls.setReasonMaster(rotsReasonMaster);
		}
		
		rotsScheduledStopTranDtls.setActiveFlag(ROTSConstants.ACTIVE.getId());
		rotsScheduledStopTranDtls.setReasonDesc(scheduledStopDtls.getStopDescription());
		if(null != scheduledStopDtls.getEndDate()) {
			rotsScheduledStopTranDtls.setEndDate(DateUtils.convertStringToDateWithTime(scheduledStopDtls.getEndDate()));
		}
		if(null != scheduledStopDtls.getStartDate()) {
			rotsScheduledStopTranDtls.setStartDate(DateUtils.convertStringToDateWithTime(scheduledStopDtls.getStartDate()));
		}
		
		if(null != scheduledStopDtls.getMachineId()) {
			RotsMachineMaster rotsMachineMaster = this.adminDao.getMachineById(scheduledStopDtls.getMachineId());
			
			if(null != rotsMachineMaster) {
				rotsScheduledStopTranDtls.setRotsMachineMaster(rotsMachineMaster);
			}
		}
		rotsScheduledStopTranDtls.setStopLevel(ScheduledStopLevel.getIDFromString(scheduledStopDtls.getStopLevel()));
		rotsScheduledStopTranDtls.setCreatedBy(rotsUserMetaInfo.getRotsUserRoles().getRotsUserMaster().getUserId());
		rotsScheduledStopTranDtls.setCreatedDate(Calendar.getInstance().getTime());
		rotsScheduledStopTranDtls.setUpdatedBy(rotsUserMetaInfo.getRotsUserRoles().getRotsUserMaster().getUserId());
		rotsScheduledStopTranDtls.setUpdatedDate(Calendar.getInstance().getTime());
		
		this.adminDao.saveRotsScheduledStopTranDtls(rotsScheduledStopTranDtls);
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public RotsErrorType validateScheduledStoppageObject(ScheduledStopDtls scheduledStopDtls) {	
	       
			if(null != scheduledStopDtls.getStopDescription() && null != scheduledStopDtls.getStoppageReasonDtls().getReasonId()){
				RotsReasonMaster rotsReasonMaster = this.adminDao.getReasonById(scheduledStopDtls.getStoppageReasonDtls().getReasonId());
				
			
				if(null == rotsReasonMaster) {
					return RotsErrorType.INVALID_REASON_ID;
				}
				
				
				//RotsMachineMaster rotsMachineMaster = this.adminDao.getMachineById(scheduledStopDtls.getMachineId());
				
				
			//	if(null != rotsMachineMaster) {
				//	return RotsErrorType.INVALID_MACHINE_ID;
				//}
				
				
			}else {
				return RotsErrorType.INVALID_SCHEDULEDSTOPDTLS;
			}
		return null;
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public RotsErrorType validateScheduledStoppageId(ScheduledStopDtls scheduledStopDtls) {	
	       
			if(null != scheduledStopDtls.getStoppageReasonDtls() && null != scheduledStopDtls.getStoppageReasonDtls().getReasonId()){
				RotsScheduledStopTranDtls rotsScheduledStopTranDtls = this.adminDao.getRotsScheduledStopTranDtlsByIdd(scheduledStopDtls.getStoppageId());
				
			
				if(null == rotsScheduledStopTranDtls) {
					return RotsErrorType.INVALID_STOPPAGE_ID;
				
				
				}
				
				RotsReasonMaster rotsReasonMaster = this.adminDao.getReasonById(scheduledStopDtls.getStoppageReasonDtls().getReasonId());
				if(null == rotsReasonMaster) {
					return RotsErrorType.INVALID_REASON_ID;
				}
			}else {
				return RotsErrorType.INVALID_SCHEDULEDSTOPDTLS;
			}
		return null;
		
	}
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateScheduledStoppage(ScheduledStopDtls scheduledStopDtls, RotsUserMetaInfo rotsUserMetaInfo) throws ParseException {
		
	
			RotsScheduledStopTranDtls rotsScheduledStopTranDtls = this.adminDao.getRotsScheduledStopTranDtlsByIdd(scheduledStopDtls.getStoppageId());
		     
			if(null != rotsScheduledStopTranDtls) {
			
				rotsScheduledStopTranDtls.setActiveFlag(ROTSConstants.ACTIVE.getId());
				rotsScheduledStopTranDtls.setReasonDesc(scheduledStopDtls.getStopDescription());
				if(null != scheduledStopDtls.getEndDate()) {
					rotsScheduledStopTranDtls.setEndDate(DateUtils.convertStringToDateWithTime(scheduledStopDtls.getEndDate()));
				}
				if(null != scheduledStopDtls.getStartDate()) {
					rotsScheduledStopTranDtls.setStartDate(DateUtils.convertStringToDateWithTime(scheduledStopDtls.getStartDate()));
				}
				if(null != scheduledStopDtls.getMachineId()) {
					RotsMachineMaster rotsMachineMaster = this.adminDao.getMachineById(scheduledStopDtls.getMachineId());
					
					if(null != rotsMachineMaster) {
						rotsScheduledStopTranDtls.setRotsMachineMaster(rotsMachineMaster);
					}
				}
				
				RotsReasonMaster rotsReasonMaster = this.adminDao.getReasonById(scheduledStopDtls.getStoppageReasonDtls().getReasonId());
				if(null != rotsReasonMaster) {
					rotsScheduledStopTranDtls.setReasonMaster(rotsReasonMaster);
				}
				
				rotsScheduledStopTranDtls.setStopLevel(ScheduledStopLevel.getIDFromString(scheduledStopDtls.getStopLevel()));
				rotsScheduledStopTranDtls.setUpdatedBy(rotsUserMetaInfo.getRotsUserRoles().getRotsUserMaster().getUserId());
				rotsScheduledStopTranDtls.setUpdatedDate(Calendar.getInstance().getTime());
				
				this.adminDao.mergeRotsScheduledStopTranDtls(rotsScheduledStopTranDtls);
			}
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteScheduledStoppage(ScheduledStopDtls scheduledStopDtls, RotsUserMetaInfo rotsUserMetaInfo) {
		
	
			RotsScheduledStopTranDtls rotsScheduledStopTranDtls = this.adminDao.getRotsScheduledStopTranDtlsByIdd(scheduledStopDtls.getStoppageId());
			
			if(null != rotsScheduledStopTranDtls) {
				rotsScheduledStopTranDtls.setActiveFlag(ROTSConstants.INACTIVE.getId());
				rotsScheduledStopTranDtls.setUpdatedBy(rotsUserMetaInfo.getRotsUserRoles().getRotsUserMaster().getUserId());
				rotsScheduledStopTranDtls.setUpdatedDate(Calendar.getInstance().getTime());
				
				this.adminDao.mergeRotsScheduledStopTranDtls(rotsScheduledStopTranDtls);
			}
		
	}
	
	public String getDisplayName(RotsUserMaster rotsUserMaster) {
		String displayName = "";
		if(null != rotsUserMaster) {
			displayName =  rotsUserMaster.getFirstName()+ " " + rotsUserMaster.getLastName() + " (" + rotsUserMaster.getContactNumber() + ")";
		}
		
		return displayName;
	}
	
	public String getDisplayName(RotsOperatorMaster rotsOperatorMaster) {
		String displayName = "";
		if(null != rotsOperatorMaster) {
			displayName =  rotsOperatorMaster.getFirstName()+ " " + rotsOperatorMaster.getLastName() + " (" + rotsOperatorMaster.getEmpNo() + ")";
		}
		
		return displayName;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<RotsBreakMaster> getAllbreaksBeforeTime(Date time, Integer shiftId){
		return this.adminDao.getAllbreaksBeforeTime(time,shiftId);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<RotsBreakMaster> getBreakWithinTime(Date fromTime, Date toTime, Integer shiftId){
		return this.adminDao.getBreakWithinTime(fromTime, toTime, shiftId);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public RotsOperatorMaster getOperatorById(Integer operatorId) {
		return this.adminDao.getOperatorById(operatorId);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public RotsReasonMaster getReasonById(Integer reasonId) {
		return this.adminDao.getReasonById(reasonId);
	}
	
	//This API helps Parser to parse and insert data into DB
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean insertDataIntoDB(List<String> data) throws ParseException {
		boolean isSuccess = false;
		if(Integer.valueOf(data.get(0)).equals(ROTSConstants.LIVE_DATA.getId())) {
			this.insertLiveData(data);
			isSuccess = true;
		}else if(Integer.valueOf(data.get(0)).equals(ROTSConstants.SCRAP_DETAILS.getId())){
			this.insertScrapDtls(data);
			isSuccess = true;
		}
		return isSuccess;
	}

	private boolean insertLiveData(List<String> data) throws ParseException {
		RotsMachineDataTest rotsMachineDataTest = new RotsMachineDataTest();
		RotsMachineLiveData rotsMachineLiveData = new RotsMachineLiveData();
		
		if(null != data.get(1)) {
			//Set Machine ID
			rotsMachineDataTest.setMachineId(Integer.valueOf(data.get(1)));
			rotsMachineLiveData.setMachineId(Integer.valueOf(data.get(1)));
			
			//Set Product Master 
			if(null != data.get(2)) {
				
				rotsMachineDataTest.setProductCode(String.valueOf(data.get(2)));	
				
				rotsMachineLiveData.setProductCode(String.valueOf(data.get(2)));
			}
			
			
			//Set Status ID
			if(null != data.get(3)) {
				rotsMachineDataTest.setStatus(Integer.valueOf(data.get(3)));	
				rotsMachineLiveData.setStatus(Integer.valueOf(data.get(3)));
			}
			
			//Set Reason ID
			if(null != data.get(4)) {
				rotsMachineDataTest.setReasonId(Integer.valueOf(data.get(4)));				
			}
			
			//Set Live PROD Count
			if(null != data.get(5)) {
				rotsMachineDataTest.setLiveProdCount(Integer.valueOf(data.get(5)));		
				rotsMachineLiveData.setProdCount(Integer.valueOf(data.get(5)));
			}
			
			//Set Reset Count Count
			if(null != data.get(6)) {
				rotsMachineDataTest.setResetCount(Integer.valueOf(data.get(6)));				
			}
			
			//Set operator ID
			if(null != data.get(7)) {
				rotsMachineDataTest.setOperatorId(Integer.valueOf(data.get(7)));
				rotsMachineLiveData.setOperatorId(Integer.valueOf(data.get(7)));
			}
			
			//Set scrap count
			if(null != data.get(8)) {
				rotsMachineDataTest.setScrapCount(Integer.valueOf(data.get(8)));
				rotsMachineLiveData.setScrapCount(Integer.valueOf(data.get(8)));
			}
			
			//Set record date time
			if(null != data.get(9) && null != data.get(10)) {
				 // (position 6) gps_dateTime (merging indexes 8 & 9   Logged Date and Time 15 Oct 15
				 String DateTime = data.get(9) + data.get(10);
				 
				 DateFormat input = new SimpleDateFormat("ddMMyyHHmmss");
				 Date date = null;
				 
				
					date = input.parse(DateTime);
					Timestamp gpsDate = new java.sql.Timestamp(date.getTime());
					rotsMachineDataTest.setRecordDateTime(gpsDate);	
					rotsMachineLiveData.setRecordDateTime(gpsDate);
				 // ("022310141505");
							 
							
			}
			
			Date currentDate = Calendar.getInstance().getTime();
			
			rotsMachineDataTest.setProcessFlag(0);
			rotsMachineDataTest.setSystemDateTime(currentDate);
			rotsMachineDataTest.setCreatedDate(currentDate);
			rotsMachineDataTest.setUpdatedDate(currentDate);
			rotsMachineDataTest.setCreatedBy(ROTSConstants.SYSTEM.getId());
			rotsMachineDataTest.setUpdatedBy(ROTSConstants.SYSTEM.getId());
			
			//Crete Live data object
			rotsMachineLiveData.setCreatedBy(ROTSConstants.SYSTEM.getId());
			rotsMachineLiveData.setUpdatedBy(ROTSConstants.SYSTEM.getId());
			rotsMachineLiveData.setCreatedDate(currentDate);
			rotsMachineLiveData.setUpdatedDate(currentDate);
			
			
			
			this.adminDao.saveRotsMachineDataTest(rotsMachineDataTest);
			
			
			this.adminDao.saveRotsMachineLiveData(rotsMachineLiveData);
			
			
			
			System.out.println("Inserted into DB " + new Date());
		
			return true;
		}
		
		return false;
	}
	
	private boolean insertScrapDtls(List<String> data) throws ParseException {
	  RotsScrapCountDetails rotsScrapCountDetails = new RotsScrapCountDetails();
	//  "$HTTP,scrap,machineId,productId,shiftNumber,date,recordDate,recordTime,scrapCount,11,4,121120,135719*"	
	//  "$HTTP,scrap,1,6,1,10,0,11,4,121120,135719*"
	
		
		if(null != data.get(1)) {
			//Set Machine ID
			rotsScrapCountDetails.setMachineId(Integer.valueOf(data.get(1)));
			
			//Set Product Master 
			if(null != data.get(2)) {
				
				RotsProductMaster produMaster = this.adminDao.getProdMasterByCode(data.get(2));
				if(null != produMaster) {
					rotsScrapCountDetails.setProductId(produMaster.getProductId());
				}
				
			}
			
			
			//Set Status ID
			if(null != data.get(3)) {
				RotsShiftMaster shiftM = this.adminDao.getShiftMasterByNumber(Integer.valueOf(data.get(3)));
				if(null != shiftM) {
					rotsScrapCountDetails.setShiftId(shiftM.getShiftId());
				}
			}
			
			//Set Reason ID
			if(null != data.get(4)) {    			
			
					rotsScrapCountDetails.setDate(DateUtils.convertStringToDateWithoutTimeParser(data.get(4)));		
				
			}
			
			//Set Reset Count Count
			if(null != data.get(5)) {
				rotsScrapCountDetails.setScrapCount(Integer.valueOf(data.get(5)));				
			}
			
			//Set Live PROD Count
			if(null != data.get(6)  && null != data.get(7)) {
				
				 // (position 6) gps_dateTime (merging indexes 8 & 9   Logged Date and Time 15 Oct 15
				 String DateTime = data.get(6) + data.get(7);
				 
				 DateFormat input = new SimpleDateFormat("ddMMyyHHmmss");
				 Date date = null;
				 
				
					date = input.parse(DateTime);
					Timestamp gpsDate = new java.sql.Timestamp(date.getTime());
					rotsScrapCountDetails.setSystemDate(gpsDate);
				// ("022310141505");
				 
				
			}
			
			  rotsScrapCountDetails.setActiveFlag(ROTSConstants.ACTIVE.getId());
			  rotsScrapCountDetails.setUpdatedBy(ROTSConstants.SYSTEM.getId());
			  rotsScrapCountDetails.setCreatedBy(ROTSConstants.SYSTEM.getId());
			  rotsScrapCountDetails.setCreatedDate(new Date());
			  rotsScrapCountDetails.setUpdatedDate(new Date());
			
			try {
			
				this.adminDao.saveRotsScrapDetails(rotsScrapCountDetails);
			
			}catch(Exception e) {
				logger.debug(e.getStackTrace());
			}
			
			System.out.println("Inserted into DB " + new Date());
		
			return true;
		}
		
		return false;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public RotsMachineMaster getMachineById(Integer machineId) {
		return this.adminDao.getMachineById(machineId);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public RotsProductMaster getProductById(Integer productId){
		return this.adminDao.getProdMasterById(productId);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public RotsProductMaster getProdMasterByCode(String productCode) {
		return this.adminDao.getProdMasterByCode(productCode);
	}
	
	//APIs for Manage Shifts and Breaks
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<ShiftDetails> getAllShifts(Integer pageNumber, Integer pageSize){
		List<RotsShiftMaster> listOfShiftMaster = this.adminDao.getrAllShifts(pageNumber, pageSize);
		
		List<ShiftDetails> listOfShiftDetails = new ArrayList<ShiftDetails>();
		
		for (RotsShiftMaster rotsShiftMaster : listOfShiftMaster) {
			ShiftDetails shiftDetails = new ShiftDetails();
			shiftDetails.setShiftId(rotsShiftMaster.getShiftId());
			shiftDetails.setShiftName(rotsShiftMaster.getShiftName());
			shiftDetails.setShiftNumber(rotsShiftMaster.getShiftNumber());
			if(null != rotsShiftMaster.getStartTime()) {
			 shiftDetails.setStartTime(DateUtils.convertTimeToString(rotsShiftMaster.getStartTime()));
			}
			
			if(null != rotsShiftMaster.getEndTime()) {
				shiftDetails.setEndTime(DateUtils.convertTimeToString(rotsShiftMaster.getEndTime()));
			}
			
			shiftDetails.setActiveFlag(rotsShiftMaster.getActiveFlag());
			
			//Set All breaks for the current shift
			
			List<RotsBreakMaster> listOfAllBreaks = this.adminDao.getAllBreaksForShift(rotsShiftMaster.getShiftId());
			List<ShiftWiseBreakDetails> listOfBreaks = new ArrayList<ShiftWiseBreakDetails>();
			for (RotsBreakMaster rotsBreakMaster : listOfAllBreaks) {
				
				listOfBreaks.add(this.constructShiftWiseBreakDetails(rotsBreakMaster));
			}
			
			shiftDetails.setListOfBreaks(listOfBreaks);
			listOfShiftDetails.add(shiftDetails);
		}
		
		return listOfShiftDetails;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public ShiftDetails getShiftDtlsById(Integer shiftId){
	
		RotsShiftMaster rotsShiftMaster = this.adminDao.getShiftMasterById(shiftId);
		
		ShiftDetails shiftDetails = new ShiftDetails();
		
		if(null != rotsShiftMaster) {
			shiftDetails.setShiftId(rotsShiftMaster.getShiftId());
			shiftDetails.setShiftName(rotsShiftMaster.getShiftName());
			shiftDetails.setShiftNumber(rotsShiftMaster.getShiftNumber());
			if(null != rotsShiftMaster.getStartTime()) {
			 shiftDetails.setStartTime(DateUtils.convertTimeToString(rotsShiftMaster.getStartTime()));
			}
			
			if(null != rotsShiftMaster.getEndTime()) {
				shiftDetails.setEndTime(DateUtils.convertTimeToString(rotsShiftMaster.getEndTime()));
			}
			
			shiftDetails.setActiveFlag(rotsShiftMaster.getActiveFlag());
			
			//Set All breaks for the current shift
			
			List<RotsBreakMaster> listOfAllBreaks = this.adminDao.getAllBreaksForShift(rotsShiftMaster.getShiftId());
			List<ShiftWiseBreakDetails> listOfBreaks = new ArrayList<ShiftWiseBreakDetails>();
			for (RotsBreakMaster rotsBreakMaster : listOfAllBreaks) {
				
				listOfBreaks.add(this.constructShiftWiseBreakDetails(rotsBreakMaster));
			}
			
			shiftDetails.setListOfBreaks(listOfBreaks);
			
		}
		return shiftDetails;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public RotsErrorType validateShiftAndBreakIDs(ShiftDetails shiftDetails) {
		RotsShiftMaster rotsShiftMaster = this.adminDao.getShiftMasterById(shiftDetails.getShiftId());
		
		if(null == rotsShiftMaster) {
		  return RotsErrorType.INVALID_SHIFT;	
		}
		
		if(null == shiftDetails.getShiftNumber()) {
			return RotsErrorType.INVALID_SHIFT_NUMBER;
		}
		
		if(null == shiftDetails.getStartTime()) {
			return RotsErrorType.INVLAID_SHIFT_START_TIME;
		}
			
		if(null == shiftDetails.getEndTime()) {
			return RotsErrorType.INVLAID_SHIFT_END_TIME;
		}
		
		if(null != shiftDetails.getListOfBreaks() && !shiftDetails.getListOfBreaks().isEmpty()) {
			for (ShiftWiseBreakDetails shiftWiseBreakDetails : shiftDetails.getListOfBreaks()) {
				
				RotsBreakMaster rotsBreakMaster = this.adminDao.getBreakMasterById(shiftWiseBreakDetails.getBreakId());
				if(null == rotsBreakMaster) {
					return RotsErrorType.INVALID_BREAK;
				}
				
				if(null == shiftWiseBreakDetails.getBreakType()) {
					return RotsErrorType.INVALID_BREAK_TYPE;
				}
				if(null == shiftWiseBreakDetails.getStartTime()) {
					return RotsErrorType.INVALID_BREAK_START_TIME;
				}
					
				if(null == shiftWiseBreakDetails.getEndTime()) {
					return RotsErrorType.INVALID_BREAK_END_TIME;
				}
			}
		}else {
			return RotsErrorType.EMPTY_BREAK_DETAILS;
		}
		
		
		return null;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public RotsErrorType validateShiftDetails(ShiftDetails shiftDetails) throws ParseException {
		
		
		
		if(null == shiftDetails.getShiftNumber()) {
			return RotsErrorType.INVALID_SHIFT_NUMBER;
		}
		
		if(null == shiftDetails.getStartTime()) {
			return RotsErrorType.INVLAID_SHIFT_START_TIME;
		}
			
		if(null == shiftDetails.getEndTime()) {
			return RotsErrorType.INVLAID_SHIFT_END_TIME;
		}
		
       Date startTime = DateUtils.convertStringToTime24Hour(shiftDetails.getStartTime());
		
		Date endTime = DateUtils.convertStringToTime24Hour(shiftDetails.getEndTime());
		
		List<RotsShiftMaster> listOfShift = this.adminDao.getOverlappingShift(startTime, endTime);
		
		if(null != listOfShift) {
			return RotsErrorType.OVERLAPPING_SHIFT;
		}
		
		if(null != shiftDetails.getListOfBreaks() && !shiftDetails.getListOfBreaks().isEmpty()) {
			for (ShiftWiseBreakDetails shiftWiseBreakDetails : shiftDetails.getListOfBreaks()) {
				
				if(null == shiftWiseBreakDetails.getBreakType()) {
					return RotsErrorType.INVALID_BREAK_TYPE;
				}
				if(null == shiftWiseBreakDetails.getStartTime()) {
					return RotsErrorType.INVALID_BREAK_START_TIME;
				}
					
				if(null == shiftWiseBreakDetails.getEndTime()) {
					return RotsErrorType.INVALID_BREAK_END_TIME;
				}
			}
		}else {
			return RotsErrorType.EMPTY_BREAK_DETAILS;
		}
		
		
		
		return null;
		
	}
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveShiftDetails(ShiftDetails shiftDetails, RotsUserMetaInfo rotsUserMetaInfo) throws ParseException{
		
		RotsShiftMaster rotsShiftMaster = new RotsShiftMaster();
		
		if(null != shiftDetails.getShiftNumber()) {
			rotsShiftMaster.setShiftName(ShiftType.getStringFromID(shiftDetails.getShiftNumber()));
			rotsShiftMaster.setShiftNumber(shiftDetails.getShiftNumber());
		}
		
		if(null != shiftDetails.getStartTime()) {
			rotsShiftMaster.setStartTime(DateUtils.convertStringToTime24Hour(shiftDetails.getStartTime()));
		}
			
		if(null != shiftDetails.getEndTime()) {
				rotsShiftMaster.setEndTime(DateUtils.convertStringToTime24Hour(shiftDetails.getEndTime()));
		}
		
		rotsShiftMaster.setActiveFlag(ROTSConstants.ACTIVE.getId());	
		
		rotsShiftMaster.setUpdatedBy(rotsUserMetaInfo.getRotsUserRoles().getRotsUserMaster().getUserId());
		rotsShiftMaster.setUpdatedDate(Calendar.getInstance().getTime());
		
		rotsShiftMaster.setCreatedBy(rotsUserMetaInfo.getRotsUserRoles().getRotsUserMaster().getUserId());
		rotsShiftMaster.setCreatedDate(Calendar.getInstance().getTime());
		
		Integer shiftId = this.adminDao.saveRotsShiftMaster(rotsShiftMaster);
		
		rotsShiftMaster.setShiftId(shiftId);
		
		
   	  this.saveBreak(rotsShiftMaster, shiftDetails, rotsUserMetaInfo);

	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveBreak(RotsShiftMaster rotsShiftMaster, ShiftDetails shiftDetails, RotsUserMetaInfo rotsUserMetaInfo) throws ParseException {
		for (ShiftWiseBreakDetails shiftWiseBreakDetails : shiftDetails.getListOfBreaks()) {
			RotsBreakMaster rotsBreakMaster = this.constructRotsBreakMaster(shiftWiseBreakDetails, rotsUserMetaInfo.getRotsUserRoles(), rotsShiftMaster);
			this.adminDao.saveRotsBreakMaster(rotsBreakMaster);
		}
	}
	
	
	//User should not able to update Shift Id and Name. Only start and end time should be editable field.
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateShiftDetails(ShiftDetails shiftDetails, RotsUserMetaInfo rotsUserMetaInfo) throws ParseException{
		
		RotsShiftMaster rotsShiftMaster = this.adminDao.getShiftMasterById(shiftDetails.getShiftId());
		
		if(null != shiftDetails.getStartTime()) {
			rotsShiftMaster.setStartTime(DateUtils.convertStringToTime24Hour(shiftDetails.getStartTime()));
		}
			
		if(null != shiftDetails.getEndTime()) {
				rotsShiftMaster.setEndTime(DateUtils.convertStringToTime24Hour(shiftDetails.getEndTime()));
		}
		
		rotsShiftMaster.setUpdatedBy(rotsUserMetaInfo.getRotsUserRoles().getRotsUserMaster().getUserId());
		rotsShiftMaster.setUpdatedDate(Calendar.getInstance().getTime());
		
		
		if(null != shiftDetails.getIsUpdatedBreaks() && ROTSConstants.UPDATED_RECORD.getId().equals(shiftDetails.getIsUpdatedBreaks())) {
			this.updateBreakDetailsForShift(shiftDetails, rotsUserMetaInfo.getRotsUserRoles());
		}
		
		this.adminDao.mergeRotsShiftMaster(rotsShiftMaster);	
	}
	
	private void updateBreakDetailsForShift(ShiftDetails shiftDetails, RotsUserRoles rotsUserRoles) throws ParseException {
		
		for (ShiftWiseBreakDetails shiftWiseBreakDetails : shiftDetails.getListOfBreaks()) {
			
			if(null != shiftWiseBreakDetails.getIsUpdatedBreak() && ROTSConstants.UPDATED_RECORD.getId().equals(shiftWiseBreakDetails.getIsUpdatedBreak())){
			RotsBreakMaster rotsBreakMaster = this.adminDao.getBreakMasterById(shiftWiseBreakDetails.getBreakId());
			
			if(null != shiftWiseBreakDetails.getStartTime()) {
				rotsBreakMaster.setStartTime(DateUtils.convertStringToTime24Hour(shiftWiseBreakDetails.getStartTime()));
			}
				
			if(null != shiftWiseBreakDetails.getEndTime()) {
				rotsBreakMaster.setEndTime(DateUtils.convertStringToTime24Hour(shiftWiseBreakDetails.getEndTime()));
			}
			
			if(null != rotsBreakMaster.getStartTime() && null != rotsBreakMaster.getEndTime()) {
				Long totalDuration = DateUtils.getDateDiff(rotsBreakMaster.getStartTime(), rotsBreakMaster.getEndTime(), TimeUnit.MINUTES);
				
				if(null != totalDuration) {
					rotsBreakMaster.setTotalMinues(totalDuration.intValue());		
				}
			}
			
			rotsBreakMaster.setActiveFlag(ROTSConstants.ACTIVE.getId());
			
			rotsBreakMaster.setUpdatedBy(rotsUserRoles.getRotsUserMaster().getUserId());
			rotsBreakMaster.setUpdatedDate(Calendar.getInstance().getTime());
			
			this.adminDao.mergeRotsBreakMaster(rotsBreakMaster);
		}
	  }
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteShiftDetails(ShiftDetails shiftDetails, RotsUserMetaInfo rotsUserMetaInfo) throws ParseException{
		
		RotsShiftMaster rotsShiftMaster = this.adminDao.getShiftMasterById(shiftDetails.getShiftId());
		
		if(null != rotsShiftMaster) {
			
			List<RotsBreakMaster> listOfAllBreaks = this.adminDao.getAllBreaksForShift(rotsShiftMaster.getShiftId());
			
			for (RotsBreakMaster rotsBreakMaster : listOfAllBreaks) {
				rotsBreakMaster.setActiveFlag(ROTSConstants.INACTIVE.getId());
				rotsBreakMaster.setUpdatedBy(rotsUserMetaInfo.getRotsUserRoles().getRotsUserMaster().getUserId());
				rotsBreakMaster.setUpdatedDate(Calendar.getInstance().getTime());
				this.adminDao.mergeRotsBreakMaster(rotsBreakMaster);
				
			}
			
			rotsShiftMaster.setUpdatedBy(rotsUserMetaInfo.getRotsUserRoles().getRotsUserMaster().getUserId());
			rotsShiftMaster.setUpdatedDate(Calendar.getInstance().getTime());
			rotsShiftMaster.setActiveFlag(ROTSConstants.INACTIVE.getId());
			this.adminDao.mergeRotsShiftMaster(rotsShiftMaster);
		}
	}
	
	
	private ShiftWiseBreakDetails constructShiftWiseBreakDetails(RotsBreakMaster rotsBreakMaster) {
		
		ShiftWiseBreakDetails shiftWiseBreakDetails = new ShiftWiseBreakDetails();
		
		shiftWiseBreakDetails.setBreakId(rotsBreakMaster.getId());
		shiftWiseBreakDetails.setBreakName(rotsBreakMaster.getBreakName());
		shiftWiseBreakDetails.setBreakType(BreakType.getIDFromString(rotsBreakMaster.getBreakName()));
		shiftWiseBreakDetails.setShiftId(rotsBreakMaster.getRotsShiftMaster().getShiftId());
		shiftWiseBreakDetails.setShiftName(rotsBreakMaster.getRotsShiftMaster().getShiftName());
		if(null != rotsBreakMaster.getStartTime()) {
			shiftWiseBreakDetails.setStartTime(DateUtils.convertTimeToString(rotsBreakMaster.getStartTime()));
		}
			
		if(null != rotsBreakMaster.getEndTime()) {
			shiftWiseBreakDetails.setEndTime(DateUtils.convertTimeToString(rotsBreakMaster.getEndTime()));
		}
		
		if(null != rotsBreakMaster.getTotalMinues()) {
			shiftWiseBreakDetails.setTotalTime(DateUtils.convertTimeIntoDaysHrMin(Long.valueOf(rotsBreakMaster.getTotalMinues()), TimeUnit.MINUTES));
		}
		
		shiftWiseBreakDetails.setActiveFlag(rotsBreakMaster.getActiveFlag());
	    return shiftWiseBreakDetails;
	}
	
	private RotsBreakMaster constructRotsBreakMaster(ShiftWiseBreakDetails shiftWiseBreakDetails, RotsUserRoles rotsUserRoles, RotsShiftMaster rotsShiftMaster) throws ParseException {
		RotsBreakMaster rotsBreakMaster = new RotsBreakMaster();
		
		rotsBreakMaster.setBreakName(BreakType.getStringFromID(shiftWiseBreakDetails.getBreakType()));
		if(null != shiftWiseBreakDetails.getStartTime()) {
			rotsBreakMaster.setStartTime(DateUtils.convertStringToTime24Hour(shiftWiseBreakDetails.getStartTime()));
		}
			
		if(null != shiftWiseBreakDetails.getEndTime()) {
			rotsBreakMaster.setEndTime(DateUtils.convertStringToTime24Hour(shiftWiseBreakDetails.getEndTime()));
		}
		
		if(null != rotsBreakMaster.getStartTime() && null != rotsBreakMaster.getEndTime()) {
			Long totalDuration = DateUtils.getDateDiff(rotsBreakMaster.getStartTime(), rotsBreakMaster.getEndTime(), TimeUnit.MINUTES);
			
			if(null != totalDuration) {
				rotsBreakMaster.setTotalMinues(totalDuration.intValue());		
			}
		}
		
		rotsBreakMaster.setActiveFlag(ROTSConstants.ACTIVE.getId());
		rotsBreakMaster.setRotsShiftMaster(rotsShiftMaster);
		
		rotsBreakMaster.setUpdatedBy(rotsUserRoles.getRotsUserMaster().getUserId());
		rotsBreakMaster.setUpdatedDate(Calendar.getInstance().getTime());
		
		rotsBreakMaster.setCreatedBy(rotsUserRoles.getRotsUserMaster().getUserId());
		rotsBreakMaster.setCreatedDate(Calendar.getInstance().getTime());
		
		return rotsBreakMaster;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public RotsShiftMaster getShiftMasterById(Integer shiftId) {
		return this.adminDao.getShiftMasterById(shiftId);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<MachineDetails> getAllActiveMachines(){
		List<RotsMachineMaster> listOfActiveMAchines =  this.adminDao.getAllActiveMachines();
		List<MachineDetails> listOFMachineDtkls = new ArrayList<MachineDetails>();
		
		for (RotsMachineMaster rotsMachineMaster : listOfActiveMAchines) {
			MachineDetails machineDetails = new MachineDetails();
			machineDetails.setMachineId(rotsMachineMaster.getMachineId());
			machineDetails.setMachineName(rotsMachineMaster.getMachineName());
			machineDetails.setMachineType(rotsMachineMaster.getMachineType());
			machineDetails.setTotalProducts(rotsMachineMaster.getTotalProducts());
			machineDetails.setAvgIdealCycleTime(rotsMachineMaster.getAvgIdealCycleTime());
			listOFMachineDtkls.add(machineDetails);
		}
		
		return listOFMachineDtkls;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<RotsScheduledStopTranDtls> getAllActiveScheduledStops(Integer machineId, Date startDate, Date endDate){
		return this.adminDao.getAllActiveScheduledStops(machineId, startDate, endDate);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<ProductDetails> getAllProductForMachine(Integer machineId){
		List<ProductDetails> listOfProductDtls = new ArrayList<ProductDetails>();
		
		if(null != machineId && machineId > 0) {
			List<RotsProductMaster> listOfProducts =  this.adminDao.getAllProductForMachine(machineId);
			
			for (RotsProductMaster rotsProductMaster : listOfProducts) {
				ProductDetails productDetails = new ProductDetails();
				productDetails.setProductCode(rotsProductMaster.getProductCode());
				productDetails.setProductId(rotsProductMaster.getProductId());
				productDetails.setProductName(rotsProductMaster.getProductName());
				listOfProductDtls.add(productDetails);
			}
		}
		return listOfProductDtls;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public RotsErrorType validateProductDetails(ProductCountDetails targetDetails) {
		if(null == targetDetails) {
			
		}
		
		if(null == targetDetails.getDate()) {
			return RotsErrorType.INVALID_DATE;
		}
		
		if(null == targetDetails.getShiftId()) {
			return RotsErrorType.INVALID_SHIFT;
		}else {
			RotsShiftMaster shiftMaster = this.adminDao.getShiftMasterById(targetDetails.getShiftId());
			if(null == shiftMaster) {
				return RotsErrorType.INVALID_SHIFT;
			}
		}
		
		if(null == targetDetails.getMachineId()) {
			return RotsErrorType.INVALID_MACHINE_ID;
		}else {
			RotsMachineMaster rotsMachineMaster = this.adminDao.getMachineById(targetDetails.getMachineId());
			if(null == rotsMachineMaster) {
				return RotsErrorType.INVALID_MACHINE_ID;
			}
		}
		
		if(null != targetDetails.getProductId() ) {
					
			RotsProductMaster rotsProductMaster = this.adminDao.getProdMasterById(targetDetails.getProductId());
			if(null == rotsProductMaster) {
				return RotsErrorType.INVALID_PRODUCT_ID;
			}
		}
		
		if(null == targetDetails.getTargetCount()) {
			return RotsErrorType.INVALID_TARGET_COUNT;
		}
		
		if(targetDetails.getTargetCount() <= 0) {
			return RotsErrorType.INVALID_TARGET_COUNT;
		}
		
		
		return null;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public RotsErrorType validateScrapDetails(ProductCountDetails scrapDetails) {
		if(null == scrapDetails) {
			
		}
		
		if(null == scrapDetails.getDate()) {
			return RotsErrorType.INVALID_DATE;
		}
		
		if(null == scrapDetails.getShiftId()) {
			return RotsErrorType.INVALID_SHIFT;
		}else {
			RotsShiftMaster shiftMaster = this.adminDao.getShiftMasterById(scrapDetails.getShiftId());
			if(null == shiftMaster) {
				return RotsErrorType.INVALID_SHIFT;
			}
		}
		
		if(null == scrapDetails.getMachineId()) {
			return RotsErrorType.INVALID_MACHINE_ID;
		}else {
			RotsMachineMaster rotsMachineMaster = this.adminDao.getMachineById(scrapDetails.getMachineId());
			if(null == rotsMachineMaster) {
				return RotsErrorType.INVALID_MACHINE_ID;
			}
		}
		
		if(null == scrapDetails.getProductId() ) {
			return RotsErrorType.INVALID_PRODUCT_ID;
		}else {
			RotsProductMaster rotsProductMaster = this.adminDao.getProdMasterById(scrapDetails.getProductId());
			if(null == rotsProductMaster) {
				return RotsErrorType.INVALID_PRODUCT_ID;
			}
		}
		
		if(null == scrapDetails.getScrapCount()) {
			return RotsErrorType.INVALID_SCRAP_COUNT;
		}
		
		if(scrapDetails.getScrapCount() <= 0) {
			return RotsErrorType.INVALID_SCRAP_COUNT;
		}
		
		
		return null;
	}
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveTargetDetails(ProductCountDetails targetDetails, RotsUserMetaInfo rotsUserMetaInfo) throws ParseException {
		
		RotsTargetDetails rotsTargetDetails = null;
		Date date = DateUtils.convertToOnlyDateWithoutTime(targetDetails.getDate());
		rotsTargetDetails = this.adminDao.getTargetDetails(date, targetDetails.getShiftId(), 
				targetDetails.getMachineId(), targetDetails.getProductId());
		
		if(null == rotsTargetDetails) {
			rotsTargetDetails = new RotsTargetDetails(); 
			this.constructRotsTargetDtls(targetDetails, rotsUserMetaInfo, rotsTargetDetails);
			rotsTargetDetails.setCreatedBy(rotsUserMetaInfo.getRotsUserRoles().getUserRoleId());
			rotsTargetDetails.setCreatedDate(new Date());
			this.adminDao.saveRotsTargetDetails(rotsTargetDetails);
		}else {
			
			rotsTargetDetails.setSystemDate(new Date());			
			rotsTargetDetails.setTargetCount(targetDetails.getTargetCount());
			rotsTargetDetails.setUpdatedBy(rotsUserMetaInfo.getRotsUserRoles().getUserRoleId());
			rotsTargetDetails.setUpdatedDate(new Date());
			this.adminDao.mergeRotsTargetDetails(rotsTargetDetails);
		}	
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteTargetDetails(ProductCountDetails targetDetails, RotsUserMetaInfo rotsUserMetaInfo) throws ParseException {
		
		RotsTargetDetails rotsTargetDetails = null;
		
		Date date = DateUtils.convertToOnlyDateWithoutTime(targetDetails.getDate());
		rotsTargetDetails = this.adminDao.getTargetDetails(date, targetDetails.getShiftId(), 
				targetDetails.getMachineId(), targetDetails.getProductId());
		
		if(null != rotsTargetDetails) {
			rotsTargetDetails.setActiveFlag(ROTSConstants.INACTIVE.getId());
			rotsTargetDetails.setUpdatedBy(rotsUserMetaInfo.getRotsUserRoles().getUserRoleId());
			rotsTargetDetails.setUpdatedDate(new Date());
			this.adminDao.mergeRotsTargetDetails(rotsTargetDetails);
		}	
		
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<TargetDetails> getAllTargetDetails(PaginationDtls paginationDtls) {
		
		List<TargetDetails> listOfTargets = new ArrayList<TargetDetails>();
		
			Date date = this.getDatexDaysBefore(7);
			
			List<RotsTargetDetails> listOfTargetDtls = this.adminDao.getTargetDetailsForDate(date, paginationDtls);
					
				for (RotsTargetDetails rotsTargetDetails : listOfTargetDtls) {
					
					TargetDetails targetDetails = new TargetDetails();
					
					RotsShiftMaster rotsShiftMaster = this.adminDao.getShiftMasterById(rotsTargetDetails.getShiftId());
					
					
					targetDetails.setDateStr(DateUtils.convertDateToStringWithoutTime(rotsTargetDetails.getDate()));
					
					String fromTime = DateUtils.convertTimeToString(rotsShiftMaster.getStartTime());
					String toTime = DateUtils.convertTimeToString(rotsShiftMaster.getEndTime());
					targetDetails.setShiftName(rotsShiftMaster.getShiftName() + " (" + fromTime + "-" + toTime + ")");
					targetDetails.setShiftId(rotsShiftMaster.getShiftId());
					targetDetails.setTargetId(rotsTargetDetails.getId());
					targetDetails.setDate(rotsTargetDetails.getDate());
					if(null != rotsTargetDetails.getProductId()) {
						RotsProductMaster prodMaster = this.adminDao.getProdMasterById(rotsTargetDetails.getProductId());
						
						if(null != prodMaster) {
						
							targetDetails.setProductName(prodMaster.getProductName());
							targetDetails.setMachineName(prodMaster.getMachineMaster().getMachineName());
						}
					}else {
						RotsMachineMaster machineM = this.adminDao.getMachineById(rotsTargetDetails.getMachineId());
						if(null != machineM) {
							targetDetails.setMachineName(machineM.getMachineName());
							targetDetails.setMachineId(machineM.getMachineId());
						}
					}
					
					targetDetails.setTargetCount(rotsTargetDetails.getTargetCount());
				
					listOfTargets.add(targetDetails);
				}
				
		return listOfTargets;
	
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public TargetDetails getTragetDtlsById(Integer targetId) {
		
			RotsTargetDetails rotsTargetDetails = this.adminDao.getTargetDetailsById(targetId);	
			TargetDetails targetDetails = new TargetDetails();
					
			if(null != rotsTargetDetails) {
				
					RotsShiftMaster rotsShiftMaster = this.adminDao.getShiftMasterById(rotsTargetDetails.getShiftId());
					
					
					targetDetails.setDateStr(DateUtils.convertDateToStringWithoutTime(rotsTargetDetails.getDate()));
					
					String fromTime = DateUtils.convertTimeToString(rotsShiftMaster.getStartTime());
					String toTime = DateUtils.convertTimeToString(rotsShiftMaster.getEndTime());
					targetDetails.setShiftName(rotsShiftMaster.getShiftName() + " (" + fromTime + "-" + toTime + ")");
					targetDetails.setShiftId(rotsShiftMaster.getShiftId());
					if(null != rotsTargetDetails.getProductId()) {
						RotsProductMaster prodMaster = this.adminDao.getProdMasterById(rotsTargetDetails.getProductId());
						
						if(null != prodMaster) {
						
							targetDetails.setProductName(prodMaster.getProductName());
							targetDetails.setMachineName(prodMaster.getMachineMaster().getMachineName());
						}
					}else {
						RotsMachineMaster machineM = this.adminDao.getMachineById(rotsTargetDetails.getMachineId());
						if(null != machineM) {
							targetDetails.setMachineName(machineM.getMachineName());
							targetDetails.setMachineId(machineM.getMachineId());
						}
					}
					
					targetDetails.setTargetCount(rotsTargetDetails.getTargetCount());
				
					
				}
				
		return targetDetails;
	
	}
	
	private RotsTargetDetails constructRotsTargetDtls(ProductCountDetails targetDetails, RotsUserMetaInfo rotsUserMetaInfo,
			RotsTargetDetails rotsTargetDetails) throws ParseException {
		
		rotsTargetDetails.setActiveFlag(ROTSConstants.ACTIVE.getId());	
		
		rotsTargetDetails.setDate(DateUtils.convertToOnlyDateWithoutTime(targetDetails.getDate()));	
			
		rotsTargetDetails.setMachineId(targetDetails.getMachineId());		

		rotsTargetDetails.setProductId(targetDetails.getProductId());
		
		rotsTargetDetails.setShiftId(targetDetails.getShiftId());
		
		rotsTargetDetails.setSystemDate(new Date());			
		rotsTargetDetails.setTargetCount(targetDetails.getTargetCount());
		rotsTargetDetails.setUpdatedBy(rotsUserMetaInfo.getRotsUserRoles().getUserRoleId());
		rotsTargetDetails.setUpdatedDate(new Date());
		
		return rotsTargetDetails;
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveScrapCountDetails(ProductCountDetails scrapCountDtls, RotsUserMetaInfo rotsUserMetaInfo) throws ParseException {
		
		RotsScrapCountDetails rotsScrapCountDetails = null;
		
		rotsScrapCountDetails = this.adminDao.getScrapCountDetails(scrapCountDtls.getDate(), scrapCountDtls.getShiftId(), 
				scrapCountDtls.getMachineId(), scrapCountDtls.getProductId());
		
		if(null == rotsScrapCountDetails) {
			rotsScrapCountDetails = new RotsScrapCountDetails(); 
			this.constructRotsScrapCountDetails(scrapCountDtls, rotsUserMetaInfo, rotsScrapCountDetails);
			rotsScrapCountDetails.setCreatedBy(rotsUserMetaInfo.getRotsUserRoles().getUserRoleId());
			rotsScrapCountDetails.setCreatedDate(new Date());
			this.adminDao.saveRotsScrapDetails(rotsScrapCountDetails);
		}else {
			
			//this.constructRotsScrapCountDetails(scrapCountDtls, rotsUserMetaInfo, rotsScrapCountDetails);
			Integer totalScrapCount = 0;
			if(null != rotsScrapCountDetails.getScrapCount()) {
				totalScrapCount = rotsScrapCountDetails.getScrapCount() + scrapCountDtls.getScrapCount();			
			}else {
				totalScrapCount = scrapCountDtls.getScrapCount();
			}
			rotsScrapCountDetails.setScrapCount(totalScrapCount);
			this.adminDao.mergeRotsScrapDetails(rotsScrapCountDetails);
		}	
		
	}

	private void constructRotsScrapCountDetails(ProductCountDetails scrapCountDtls, RotsUserMetaInfo rotsUserMetaInfo,
			RotsScrapCountDetails rotsScrapCountDetails) throws ParseException {
		rotsScrapCountDetails.setActiveFlag(ROTSConstants.ACTIVE.getId());	
		
		rotsScrapCountDetails.setDate(DateUtils.convertToOnlyDateWithoutTime(scrapCountDtls.getDate()));	
			
		rotsScrapCountDetails.setMachineId(scrapCountDtls.getMachineId());		

		rotsScrapCountDetails.setProductId(scrapCountDtls.getProductId());
		
		rotsScrapCountDetails.setShiftId(scrapCountDtls.getShiftId());
		
		rotsScrapCountDetails.setSystemDate(new Date());
		
		rotsScrapCountDetails.setScrapCount(scrapCountDtls.getScrapCount());
		rotsScrapCountDetails.setUpdatedBy(rotsUserMetaInfo.getRotsUserRoles().getUserRoleId());
		rotsScrapCountDetails.setUpdatedDate(new Date());
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteScrapCountDetails(ProductCountDetails scrapCountDtls, RotsUserMetaInfo rotsUserMetaInfo) throws ParseException {
		
		RotsScrapCountDetails rotsScrapCountDetails = null;
		
		rotsScrapCountDetails = this.adminDao.getScrapCountDetails(scrapCountDtls.getDate(), scrapCountDtls.getShiftId(), 
				scrapCountDtls.getMachineId(), scrapCountDtls.getProductId());
		
		if(null != rotsScrapCountDetails) {
			rotsScrapCountDetails.setActiveFlag(ROTSConstants.INACTIVE.getId());
			rotsScrapCountDetails.setUpdatedBy(rotsUserMetaInfo.getRotsUserRoles().getUserRoleId());
			rotsScrapCountDetails.setUpdatedDate(new Date());
			this.adminDao.mergeRotsScrapDetails(rotsScrapCountDetails);
		}	
		
	}
	
	
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<TargetDetails> getAllScrapDetails(Integer pageNumber, Integer pageSize) {
		
		List<TargetDetails> listOfScrpaCounts = new ArrayList<TargetDetails>();
		
			Date date = this.getDatexDaysBefore(7);
			
			List<RotsScrapCountDetails> listOfScrapDtls = this.adminDao.getScrapDetailsForDate(date, pageNumber, pageSize);
					
				for (RotsScrapCountDetails rotsScrapCountDetails : listOfScrapDtls) {
					
					TargetDetails scrapCountDtls = new TargetDetails();
					
					RotsShiftMaster rotsShiftMaster = this.adminDao.getShiftMasterById(rotsScrapCountDetails.getShiftId());
					
					
					scrapCountDtls.setDateStr(DateUtils.convertDateToStringWithoutTime(date));
					
					String fromTime = DateUtils.convertTimeToString(rotsShiftMaster.getStartTime());
					String toTime = DateUtils.convertTimeToString(rotsShiftMaster.getEndTime());
					scrapCountDtls.setShiftName(rotsShiftMaster.getShiftName() + " (" + fromTime + "-" + toTime + ")");
					
					if(null != rotsScrapCountDetails.getProductId()) {
						RotsProductMaster prodMaster = this.adminDao.getProdMasterById(rotsScrapCountDetails.getProductId());
						
						if(null != prodMaster) {
						
							scrapCountDtls.setProductName(prodMaster.getProductName());
							scrapCountDtls.setMachineName(prodMaster.getMachineMaster().getMachineName());
						}
					}else {
						RotsMachineMaster machineM = this.adminDao.getMachineById(rotsScrapCountDetails.getMachineId());
						if(null != machineM) {
							scrapCountDtls.setMachineName(machineM.getMachineName());
						}
					}
					
				
					listOfScrpaCounts.add(scrapCountDtls);
				}
				
		return listOfScrpaCounts;
	
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public RotsShiftMaster getShiftMasterByNumber(Integer shiftNumber) {
		return this.adminDao.getShiftMasterByNumber(shiftNumber);
	}
	
	  private Date getDatexDaysBefore(int x) {
			// today    
			   Calendar date = new GregorianCalendar();
			   // reset hour, minutes, seconds and millis
			   date.add(Calendar.DAY_OF_YEAR, -x);
			   date.set(Calendar.HOUR_OF_DAY, 8);
			   date.set(Calendar.MINUTE, 0);
			   date.set(Calendar.SECOND, 0);
			   date.set(Calendar.MILLISECOND, 0);
			   
			   return date.getTime();
	   }
}
