package com.rots.service;

import java.text.ParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rots.DAO.ReportsDao;
import com.rots.constants.RotsErrorType;
import com.rots.constants.Status;
import com.rots.contract.HourlyMachineActivityDtls;
import com.rots.contract.KeyValueWrapper;
import com.rots.contract.LiveMachineReportWrapper;
import com.rots.contract.LiveMachineStatusDetails;
import com.rots.contract.MachineCountMap;
import com.rots.contract.ReportInputdetails;
import com.rots.contract.ShiftwiseActivityDtls;
import com.rots.contract.StatusDetails;
import com.rots.entity.RotsBreakMaster;
import com.rots.entity.RotsHourlyMachineDetails;
import com.rots.entity.RotsMachineActivityDetails;
import com.rots.entity.RotsMachineLiveData;
import com.rots.entity.RotsMachineMaster;
import com.rots.entity.RotsOperatorMaster;
import com.rots.entity.RotsProductMaster;
import com.rots.entity.RotsReasonMaster;
import com.rots.entity.RotsScheduledStopTranDtls;
import com.rots.entity.RotsScrapCountDetails;
import com.rots.entity.RotsShiftMaster;
import com.rots.entity.RotsTargetDetails;
import com.rots.util.DateUtils;

@Service("ReportsService")
public class ReportsServiceImpl implements ReportsService{

	@Autowired
	private ReportsDao reportsDao;
	
	@Autowired
	private RotsAdminService adminService;
	
	
	@Autowired
	private OEEService oeeService;
	
	@Autowired
	private CommonAPIService commonAPIService;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public RotsErrorType validateActivityReportInputDetails(ReportInputdetails reportInputdetails) {
		
		if(null == reportInputdetails.getMachineId()) {
			return RotsErrorType.INVALID_MACHINE_ID;
		}else if(null == reportInputdetails.getFromDate() || null == reportInputdetails.getToDate()){
			return RotsErrorType.INVALID_DATE;
		}
		
		RotsMachineMaster rotsMachineMaster = this.adminService.getMachineById(reportInputdetails.getMachineId());
		
		if(null == rotsMachineMaster) {
			return RotsErrorType.INVALID_MACHINE_ID;
		}
		
		return null;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<ShiftwiseActivityDtls> getHourlyMachineActivityReport(Integer machineId, Date fromDate, Date toDate) throws ParseException{
		
		List<ShiftwiseActivityDtls> listOfShiftActivities = new ArrayList<ShiftwiseActivityDtls>();
		
		List<RotsMachineActivityDetails> listOfActivities = this.reportsDao.getAllActivityRecordsForMachine(machineId, fromDate, toDate);
		List<HourlyMachineActivityDtls> listOfHourlyActivity = null;
		if(null != listOfActivities && listOfActivities.size() > 0) {
			
			listOfHourlyActivity = new ArrayList<HourlyMachineActivityDtls>();
			List<StatusDetails> listOfStatusDtls = new ArrayList<StatusDetails>();
			Long totalTime = 0L;
			HourlyMachineActivityDtls hourlyMachineActivityDtls = new HourlyMachineActivityDtls();
			ShiftwiseActivityDtls shiftwiseActivityDtls = new ShiftwiseActivityDtls();
			Integer totalProdCount = 0;
			Integer statusWiseCount = 0;
			String statusWiseToTime = "";
			for (RotsMachineActivityDetails rotsMachineActivityDetails : listOfActivities) {
				
				if(null == hourlyMachineActivityDtls.getStatusId()) {
					
					statusWiseCount = 0;
					statusWiseToTime = "";
					hourlyMachineActivityDtls.setStatusId(rotsMachineActivityDetails.getStatus());
					hourlyMachineActivityDtls.setStatus(Status.getStringFromID(rotsMachineActivityDetails.getStatus()));
					hourlyMachineActivityDtls.setShiftId(rotsMachineActivityDetails.getShiftId());
					hourlyMachineActivityDtls.setFromTime(DateUtils.convertDateTimestampToStringWithTime(rotsMachineActivityDetails.getStartDate()));
					
				}else if(hourlyMachineActivityDtls.getStatusId() != rotsMachineActivityDetails.getStatus() ||
						hourlyMachineActivityDtls.getShiftId() != rotsMachineActivityDetails.getShiftId() ||
						listOfActivities.get(listOfActivities.size() - 1).getId().equals(rotsMachineActivityDetails.getId())){
					
					if(listOfActivities.get(listOfActivities.size() - 1).getId().equals(rotsMachineActivityDetails.getId())
							&& hourlyMachineActivityDtls.getStatusId() == rotsMachineActivityDetails.getStatus()) {
						
						   totalTime = totalTime + DateUtils.getDateDiff(rotsMachineActivityDetails.getStartDate(), rotsMachineActivityDetails.getEndDate(), TimeUnit.SECONDS);
						
						   //Get shift object
						
					    	StatusDetails statusDtls = new StatusDetails();
						
						   //Set production Count for this reord if not null
							totalProdCount = this.setTotalProdCount(totalProdCount, rotsMachineActivityDetails, statusDtls);
							if(rotsMachineActivityDetails.getStatus().equals(Status.RUNNING.getStatusId())) {
							   statusWiseCount = statusWiseCount + rotsMachineActivityDetails.getLastrecordProdCount() - rotsMachineActivityDetails.getFirstRecordProdCount();
							}
							statusWiseToTime = DateUtils.convertDateTimestampToStringWithTime(rotsMachineActivityDetails.getEndDate());
							
							//Set status details for this record
							this.setStatusDtls(machineId, rotsMachineActivityDetails, statusDtls);
						
							if(listOfStatusDtls.size() > 0) {
							    StatusDetails lastStatusDtls = listOfStatusDtls.get(listOfStatusDtls.size() - 1);
							
							    this.addToListIfChangeInParams(listOfStatusDtls, statusDtls, lastStatusDtls);
							}else {
								listOfStatusDtls.add(statusDtls);
							}
							
					}
					
					    this.constructHourlyActivityObject(listOfHourlyActivity, listOfStatusDtls, totalTime,
								hourlyMachineActivityDtls, statusWiseCount,statusWiseToTime);
					    
					if(listOfActivities.get(listOfActivities.size() - 1).getId().equals(rotsMachineActivityDetails.getId())
							&& hourlyMachineActivityDtls.getStatusId() != rotsMachineActivityDetails.getStatus()) {
						
						listOfStatusDtls = new ArrayList<StatusDetails>();
						statusWiseCount = 0;
						statusWiseToTime = "";
						totalTime = 0L;
						hourlyMachineActivityDtls = new HourlyMachineActivityDtls();
						
					
						hourlyMachineActivityDtls.setStatusId(rotsMachineActivityDetails.getStatus());
						hourlyMachineActivityDtls.setStatus(Status.getStringFromID(rotsMachineActivityDetails.getStatus()));
						hourlyMachineActivityDtls.setShiftId(rotsMachineActivityDetails.getShiftId());
						hourlyMachineActivityDtls.setFromTime(DateUtils.convertDateTimestampToStringWithTime(rotsMachineActivityDetails.getStartDate()));
						totalTime = totalTime + DateUtils.getDateDiff(rotsMachineActivityDetails.getStartDate(), rotsMachineActivityDetails.getEndDate(), TimeUnit.SECONDS);
						
						
							
							
							StatusDetails statusDtls = new StatusDetails();
							
						   //Set production Count for this reord if not null
							totalProdCount = this.setTotalProdCount(totalProdCount, rotsMachineActivityDetails, statusDtls);
							if(rotsMachineActivityDetails.getStatus().equals(Status.RUNNING.getStatusId())) {
							 statusWiseCount = statusWiseCount + (rotsMachineActivityDetails.getLastrecordProdCount() - rotsMachineActivityDetails.getFirstRecordProdCount()) + 1;
							}
							statusWiseToTime = DateUtils.convertDateTimestampToStringWithTime(rotsMachineActivityDetails.getEndDate());
							//Set status details for this record
							this.setStatusDtls(machineId, rotsMachineActivityDetails, statusDtls);
						
							if(listOfStatusDtls.size() > 0) {
							    StatusDetails lastStatusDtls = listOfStatusDtls.get(listOfStatusDtls.size() - 1);
							
							    this.addToListIfChangeInParams(listOfStatusDtls, statusDtls, lastStatusDtls);
							}else {
								listOfStatusDtls.add(statusDtls);
							}
							
							  this.constructHourlyActivityObject(listOfHourlyActivity, listOfStatusDtls, totalTime,
										hourlyMachineActivityDtls, statusWiseCount,statusWiseToTime);
						
					}
					if(hourlyMachineActivityDtls.getShiftId() != rotsMachineActivityDetails.getShiftId()
							|| listOfActivities.get(listOfActivities.size() - 1).getId().equals(rotsMachineActivityDetails.getId())) {
						
					
							
						
						shiftwiseActivityDtls = this.setShiftLevelParams(listOfShiftActivities, listOfHourlyActivity,
								hourlyMachineActivityDtls, shiftwiseActivityDtls, totalProdCount,
								rotsMachineActivityDetails);
							
							
					}
					//Initialize details for next run
					listOfStatusDtls = new ArrayList<StatusDetails>();
					statusWiseCount = 0;
					statusWiseToTime = "";
					totalTime = 0L;
					hourlyMachineActivityDtls = new HourlyMachineActivityDtls();
					
				
					hourlyMachineActivityDtls.setStatusId(rotsMachineActivityDetails.getStatus());
					hourlyMachineActivityDtls.setStatus(Status.getStringFromID(rotsMachineActivityDetails.getStatus()));
					hourlyMachineActivityDtls.setShiftId(rotsMachineActivityDetails.getShiftId());
					hourlyMachineActivityDtls.setFromTime(DateUtils.convertDateTimestampToStringWithTime(rotsMachineActivityDetails.getStartDate()));
				}
				totalTime = totalTime + DateUtils.getDateDiff(rotsMachineActivityDetails.getStartDate(), rotsMachineActivityDetails.getEndDate(), TimeUnit.SECONDS);
				
				if(hourlyMachineActivityDtls.getStatusId() == rotsMachineActivityDetails.getStatus()) {
					
					
					StatusDetails statusDtls = new StatusDetails();
					
				   //Set production Count for this reord if not null
					totalProdCount = this.setTotalProdCount(totalProdCount, rotsMachineActivityDetails, statusDtls);
					if(rotsMachineActivityDetails.getStatus().equals(Status.RUNNING.getStatusId())) {
					   statusWiseCount = statusWiseCount + (rotsMachineActivityDetails.getLastrecordProdCount() - rotsMachineActivityDetails.getFirstRecordProdCount()) + 1;
					} 
					statusWiseToTime = DateUtils.convertDateTimestampToStringWithTime(rotsMachineActivityDetails.getEndDate());
					//Set status details for this record
					this.setStatusDtls(machineId, rotsMachineActivityDetails, statusDtls);
				
					if(listOfStatusDtls.size() > 0) {
					    StatusDetails lastStatusDtls = listOfStatusDtls.get(listOfStatusDtls.size() - 1);
					
					    this.addToListIfChangeInParams(listOfStatusDtls, statusDtls, lastStatusDtls);
					}else {
						listOfStatusDtls.add(statusDtls);
					}
					
				}
			}
		}
		return listOfShiftActivities;
	}

	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<ShiftwiseActivityDtls> getHourlyMachineActivityReport1(Integer machineId, Date fromDate, Date toDate) throws ParseException{
		
		List<ShiftwiseActivityDtls> listOfShiftActivities = new ArrayList<ShiftwiseActivityDtls>();
		
		List<RotsMachineActivityDetails> listOfActivities = this.reportsDao.getAllActivityRecordsForMachine(machineId, fromDate, toDate);
		List<HourlyMachineActivityDtls> listOfHourlyActivity = null;
		if(null != listOfActivities && listOfActivities.size() > 0) {
			
			listOfHourlyActivity = new ArrayList<HourlyMachineActivityDtls>();
			List<StatusDetails> listOfStatusDtls = new ArrayList<StatusDetails>();
			Long totalTime = 0L;
			HourlyMachineActivityDtls hourlyMachineActivityDtls = new HourlyMachineActivityDtls();
			ShiftwiseActivityDtls shiftwiseActivityDtls = new ShiftwiseActivityDtls();
			Integer totalProdCount = 0;
			Integer statusWiseCount = 0;
			String statusWiseToTime = "";
			for (RotsMachineActivityDetails rotsMachineActivityDetails : listOfActivities) {
				
				if(null == hourlyMachineActivityDtls.getStatusId()) {
					
					statusWiseCount = 0;
					statusWiseToTime = "";
					hourlyMachineActivityDtls.setStatusId(rotsMachineActivityDetails.getStatus());
					hourlyMachineActivityDtls.setStatus(Status.getStringFromID(rotsMachineActivityDetails.getStatus()));
					hourlyMachineActivityDtls.setShiftId(rotsMachineActivityDetails.getShiftId());
					
					
				}
				
				totalTime = totalTime + DateUtils.getDateDiff(rotsMachineActivityDetails.getStartDate(), rotsMachineActivityDetails.getEndDate(), TimeUnit.SECONDS);
				
				if(hourlyMachineActivityDtls.getStatusId() == rotsMachineActivityDetails.getStatus()) {
					
					
					StatusDetails statusDtls = new StatusDetails();
					
				   //Set production Count for this reord if not null
					totalProdCount = this.setTotalProdCount(totalProdCount, rotsMachineActivityDetails, statusDtls);
					if(rotsMachineActivityDetails.getStatus().equals(Status.RUNNING.getStatusId())) {
					  statusWiseCount = statusWiseCount + (rotsMachineActivityDetails.getLastrecordProdCount() - rotsMachineActivityDetails.getFirstRecordProdCount()) + 1;
					}
					statusWiseToTime = DateUtils.convertDateTimestampToStringWithTime(rotsMachineActivityDetails.getEndDate());
					
					//Set status details for this record
					this.setStatusDtls(machineId, rotsMachineActivityDetails, statusDtls);
				
					if(listOfStatusDtls.size() > 0) {
					    StatusDetails lastStatusDtls = listOfStatusDtls.get(listOfStatusDtls.size() - 1);
					
					    this.addToListIfChangeInParams(listOfStatusDtls, statusDtls, lastStatusDtls);
					}else {
						listOfStatusDtls.add(statusDtls);
					}
					
				}else if(hourlyMachineActivityDtls.getStatusId() != rotsMachineActivityDetails.getStatus() ||
						hourlyMachineActivityDtls.getShiftId() != rotsMachineActivityDetails.getShiftId() ||
						listOfActivities.get(listOfActivities.size() - 1).getId().equals(rotsMachineActivityDetails.getId())){
					
				/*	if(listOfActivities.get(listOfActivities.size() - 1).getId().equals(rotsMachineActivityDetails.getId())) {
						
						   totalTime = totalTime + DateUtils.getDateDiff(rotsMachineActivityDetails.getStartDate(), rotsMachineActivityDetails.getEndDate(), TimeUnit.SECONDS);
						
						   //Get shift object
						
					    	StatusDetails statusDtls = new StatusDetails();
						
						   //Set production Count for this reord if not null
							totalProdCount = this.setTotalProdCount(totalProdCount, rotsMachineActivityDetails, statusDtls);
							
							statusWiseCount = statusWiseCount + rotsMachineActivityDetails.getLastrecordProdCount() - rotsMachineActivityDetails.getFirstRecordProdCount();
							//Set status details for this record
							this.setStatusDtls(machineId, rotsMachineActivityDetails, statusDtls);
						
							listOfStatusDtls.add(statusDtls);
							
					}*/
					
					    this.constructHourlyActivityObject(listOfHourlyActivity, listOfStatusDtls, totalTime,
								hourlyMachineActivityDtls, statusWiseCount,statusWiseToTime);
					
					if(hourlyMachineActivityDtls.getShiftId() != rotsMachineActivityDetails.getShiftId()
							|| listOfActivities.get(listOfActivities.size() - 1).getId().equals(rotsMachineActivityDetails.getId())) {
						
					
							
						
						shiftwiseActivityDtls = this.setShiftLevelParams(listOfShiftActivities, listOfHourlyActivity,
								hourlyMachineActivityDtls, shiftwiseActivityDtls, totalProdCount,
								rotsMachineActivityDetails);
							
							
					}
					//Initialize details for next run
					listOfStatusDtls = new ArrayList<StatusDetails>();
					statusWiseCount = 0;
					statusWiseToTime = "";
					totalTime = 0L;
					hourlyMachineActivityDtls = new HourlyMachineActivityDtls();
					
				
					hourlyMachineActivityDtls.setStatusId(rotsMachineActivityDetails.getStatus());
					hourlyMachineActivityDtls.setStatus(Status.getStringFromID(rotsMachineActivityDetails.getStatus()));
					hourlyMachineActivityDtls.setShiftId(rotsMachineActivityDetails.getShiftId());
				}
				
			}
		}
		return listOfShiftActivities;
	}

	private void addToListIfChangeInParams(List<StatusDetails> listOfStatusDtls, StatusDetails statusDtls,
			StatusDetails lastStatusDtls) {
		if(((null == statusDtls.getProductId() && null == lastStatusDtls.getProductId() ) || lastStatusDtls.getProductId().equals(statusDtls.getProductId())) &&
		   ((null == statusDtls.getOperatorId() && null == lastStatusDtls.getOperatorId() ) || lastStatusDtls.getOperatorId().equals(statusDtls.getOperatorId())) &&
		   ((null == statusDtls.getReason() && null == lastStatusDtls.getReason() ) || lastStatusDtls.getReason().equals(statusDtls.getReason()))) {
			lastStatusDtls.setToTime(statusDtls.getToTime());
		}else {

			listOfStatusDtls.add(statusDtls);
		}
	}
	
	private void constructHourlyActivityObject(List<HourlyMachineActivityDtls> listOfHourlyActivity,
			List<StatusDetails> listOfStatusDtls, Long totalTime, HourlyMachineActivityDtls hourlyMachineActivityDtls,
			Integer statusWiseCount, String statusWiseToTime) {
		hourlyMachineActivityDtls.setTotalProdCount(statusWiseCount + " Units");
		hourlyMachineActivityDtls.setListOfStatusDtls(listOfStatusDtls);
		hourlyMachineActivityDtls.setToTime(statusWiseToTime);
		hourlyMachineActivityDtls.setTotalTime(DateUtils.convertTimeIntoDaysHrMin(totalTime, TimeUnit.SECONDS));
		if(Status.RUNNING.getStatusId().equals(hourlyMachineActivityDtls.getStatusId())) {
			Double avgCycleTime = null;
			if(statusWiseCount > 0) {
				avgCycleTime = (double) ((totalTime)/statusWiseCount);
			}else {
				avgCycleTime = 0.0;
				
			}
		    hourlyMachineActivityDtls.setAvgCycleTime(avgCycleTime + " seconds");
		}else {
			  hourlyMachineActivityDtls.setAvgCycleTime("NA");
		}
		
		listOfHourlyActivity.add(hourlyMachineActivityDtls);
	}

	private ShiftwiseActivityDtls setShiftLevelParams(List<ShiftwiseActivityDtls> listOfShiftActivities,
			List<HourlyMachineActivityDtls> listOfHourlyActivity, HourlyMachineActivityDtls hourlyMachineActivityDtls,
			ShiftwiseActivityDtls shiftwiseActivityDtls, Integer totalProdCount,
			RotsMachineActivityDetails rotsMachineActivityDetails) throws ParseException {
		RotsShiftMaster shiftMaster = this.adminService.getShiftMasterById(hourlyMachineActivityDtls.getShiftId());
		String shiftDetails = "";
		if(null != shiftMaster) {
			String fromTime = DateUtils.convertDateTimestampToStringWithTime(shiftMaster.getStartTime());
			String toTime = DateUtils.convertDateTimestampToStringWithTime(shiftMaster.getEndTime());
			shiftDetails = shiftMaster.getShiftName() + " (" + fromTime + " to " + toTime + " )";
		}
		//Get scrap count for current shift
		Date date = DateUtils.convertToOnlyDateWithoutTime(rotsMachineActivityDetails.getEndDate());
		
		List<RotsScrapCountDetails> listOfScrapCounts = this.oeeService.getScrapCountForMachineForShift(rotsMachineActivityDetails.getMachineId(), hourlyMachineActivityDtls.getShiftId(), date);
		
		Integer totalScrapCount = 0;
		if(null != listOfScrapCounts) {
			for (RotsScrapCountDetails rotsScrapCountDetails : listOfScrapCounts) {
				totalScrapCount = totalScrapCount + rotsScrapCountDetails.getScrapCount();
			}
		}
		
		//Count target units for current shift for machine
		
		List<RotsTargetDetails> listOfTargetcount = this.oeeService.getTargetCountForShiftForMachine(rotsMachineActivityDetails.getMachineId(), hourlyMachineActivityDtls.getShiftId(), date);
		Integer targetCount = 0;
		if(null != listOfTargetcount) {
			for (RotsTargetDetails rotsTargetDetails : listOfTargetcount) {
				targetCount = targetCount + rotsTargetDetails.getTargetCount();
			}
		}
		shiftwiseActivityDtls.setTargetCount(targetCount);
		
		
		shiftwiseActivityDtls.setScrapCount(totalScrapCount);
		
		//Good Units
		
		Integer goodUnits = totalProdCount - totalScrapCount;
		
		String scrapPercentage = "";
		
			Double scrapPer = (double) ((totalScrapCount * 100) / totalProdCount);
			if(null != scrapPer) {
				scrapPercentage = DateUtils.convertToTwoDecimalPoint(scrapPer) + " %";
			}
		
			shiftwiseActivityDtls.setTotalProdCount(totalProdCount);
			shiftwiseActivityDtls.setScrapCount(totalScrapCount);
			shiftwiseActivityDtls.setScrapPercentage(scrapPercentage);
			shiftwiseActivityDtls.setGoodUnits(goodUnits);
			shiftwiseActivityDtls.setListOfStatuswiseActivity(listOfHourlyActivity);
			shiftwiseActivityDtls.setShiftId(shiftMaster.getShiftId());
			shiftwiseActivityDtls.setShiftName(shiftDetails);
			shiftwiseActivityDtls.setActivityDate(DateUtils.convertDateToStringWithoutTime(date));
			listOfShiftActivities.add(shiftwiseActivityDtls);
			
			shiftwiseActivityDtls = new ShiftwiseActivityDtls();
		return shiftwiseActivityDtls;
	}

	

	private void setStatusDtls(Integer machineId, RotsMachineActivityDetails rotsMachineActivityDetails,
			StatusDetails statusDtls) {
 		statusDtls.setFromTime(DateUtils.convertDateTimestampToStringWithTime(rotsMachineActivityDetails.getStartDate()));
		statusDtls.setToTime(DateUtils.convertDateTimestampToStringWithTime(rotsMachineActivityDetails.getEndDate()));
		//Set Product Details
		if(null != rotsMachineActivityDetails.getRotsProductMaster()) {
			statusDtls.setProductId(rotsMachineActivityDetails.getRotsProductMaster().getProductId());
			statusDtls.setProductName(rotsMachineActivityDetails.getRotsProductMaster().getProductName());
		}
		RotsOperatorMaster operatorMaster = this.adminService.getOperatorById(rotsMachineActivityDetails.getOperatorId());
		if(null != operatorMaster) {
			
			String operator = operatorMaster.getFirstName() + " " + operatorMaster.getLastName() + " (" + 
					operatorMaster.getContactNumber() + ")";
			statusDtls.setOperator(operator);	
			statusDtls.setOperatorId(operatorMaster.getId());
		}
		
		statusDtls.setProductType(rotsMachineActivityDetails.getRotsProductMaster().getProductYpe());
		
		 String reasonDtls = "";
		 
		 if(!Status.RUNNING.getStatusId().equals(rotsMachineActivityDetails.getStatus())) {
			 List<RotsScheduledStopTranDtls> listOfScheduledStops = this.adminService.getAllActiveScheduledStops(machineId, rotsMachineActivityDetails.getStartDate(), rotsMachineActivityDetails.getEndDate());
			
			 for (RotsScheduledStopTranDtls rotsScheduledStopTranDtls : listOfScheduledStops) {
				 if(!reasonDtls.isEmpty()) {
					 reasonDtls = reasonDtls + ", " + rotsScheduledStopTranDtls.getReasonMaster().getReasonTitle();
				 }else {
					 reasonDtls = rotsScheduledStopTranDtls.getReasonMaster().getReasonTitle();
				 }
			}
			 
			 //Get All breaks for time period
			 List<RotsBreakMaster> listOfBrtheaks = this.adminService.getBreakWithinTime(rotsMachineActivityDetails.getStartDate(), rotsMachineActivityDetails.getEndDate(), rotsMachineActivityDetails.getShiftId());
			 for (RotsBreakMaster rotsBreakMaster : listOfBrtheaks) {
				 if(!reasonDtls.isEmpty()) {
					 reasonDtls = reasonDtls + ", " + rotsBreakMaster.getBreakName();
				 }else {
					 reasonDtls =rotsBreakMaster.getBreakName();
				 }
			}
			 
			
			//Set status reason
			RotsReasonMaster rotsReasonMaster = this.adminService.getReasonById(rotsMachineActivityDetails.getReasonId());
			
			if(null != rotsReasonMaster) {
				 if(!reasonDtls.isEmpty()) {
					 reasonDtls = reasonDtls + ", " + rotsReasonMaster.getReasonTitle();
				 }else {
					 reasonDtls = rotsReasonMaster.getReasonTitle();
				 }
			}
		 }
		 statusDtls.setReason(reasonDtls);
	}

	private Integer setTotalProdCount(Integer totalProdCount, RotsMachineActivityDetails rotsMachineActivityDetails,
			StatusDetails statusDtls) {
		String prodCount = "";
		Integer tempProdCount = 0;
		if(null != rotsMachineActivityDetails.getLastrecordProdCount() && rotsMachineActivityDetails.getStatus().equals(Status.RUNNING.getStatusId())) {
			tempProdCount = ( rotsMachineActivityDetails.getLastrecordProdCount() - rotsMachineActivityDetails.getFirstRecordProdCount() ) + 1;
			prodCount = tempProdCount + " Units";
		}
		statusDtls.setProdCount(prodCount);
		totalProdCount= totalProdCount +  tempProdCount ;
		return totalProdCount;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public LiveMachineReportWrapper getListOfLiveMachineStatus() throws ParseException{
		 
		List<MachineCountMap> listOfMachineCountMap = new ArrayList<MachineCountMap>();
		LiveMachineReportWrapper liveMachineReportWrapper = new LiveMachineReportWrapper();
		
		List<RotsMachineLiveData> listOfLiveRecords = this.reportsDao.getLiveRecordsForAllMachines();
		List<LiveMachineStatusDetails> listOfLiveData = new ArrayList<LiveMachineStatusDetails>();
		Integer runningCount = 0;
		Integer stopCount = 0;
		Integer idleCount = 0;
		Integer offlineCount = 0;
		Integer totalCount;
		for (RotsMachineLiveData rotsMachineLiveData : listOfLiveRecords) {
			LiveMachineStatusDetails liveMachineStatusDetails = new LiveMachineStatusDetails();
			
			//Set current status of Machine. If last record was before 5 Minutes then it will consider as OFFLINE
			if(DateUtils.getDateDiff(rotsMachineLiveData.getRecordDateTime(), Calendar.getInstance().getTime(), TimeUnit.MINUTES) > 1440) {
				liveMachineStatusDetails.setStatus(Status.OFFLINE.getStatusMsg());
				offlineCount++;
			}else {
				liveMachineStatusDetails.setStatus(Status.getStringFromID(rotsMachineLiveData.getStatus()));
				if(rotsMachineLiveData.getStatus().equals(Status.RUNNING.getStatusId())) {
					runningCount++;
				}else if(rotsMachineLiveData.getStatus().equals(Status.STOP.getStatusId())) {
					stopCount++;
				}else if(rotsMachineLiveData.getStatus().equals(Status.IDLE.getStatusId())) {
					idleCount++;
				}
			}
			
			//Set Machine details
			
			RotsMachineMaster rotsMachineMaster = this.adminService.getMachineById(rotsMachineLiveData.getMachineId());
			if(null != rotsMachineMaster) {
				liveMachineStatusDetails.setMachineName(rotsMachineMaster.getMachineName());
			}
			
			//Set Operator Details
			RotsOperatorMaster rotsOperatorMaster = this.adminService.getOperatorById(rotsMachineLiveData.getOperatorId());
			if(null != rotsOperatorMaster) {
				liveMachineStatusDetails.setOperatorName(this.adminService.getDisplayName(rotsOperatorMaster));
			}
			
			//Set Product Details
			RotsProductMaster rotsProductMaster = this.adminService.getProdMasterByCode(rotsMachineLiveData.getProductCode());
			if(null != rotsProductMaster) {
				
				liveMachineStatusDetails.setProductName(rotsProductMaster.getProductCode());
				
			}
			
			//LiveProd Count
			if(null != rotsMachineLiveData.getProdCount()) {
				liveMachineStatusDetails.setProductionCount(String.valueOf(rotsMachineLiveData.getProdCount()));
			}
			
			//Set ScrapCount
		   RotsShiftMaster rotsShiftMaster =  this.commonAPIService.getCurrentShift(rotsMachineLiveData.getRecordDateTime());
		   Date date = DateUtils.convertToOnlyDateWithoutTime(rotsMachineLiveData.getRecordDateTime());
		   Integer totalScrapCount = 0;
		   if(null != rotsShiftMaster) {
				List<RotsScrapCountDetails> listOfScrapCounts = this.oeeService.getScrapCountForMachineForShift(rotsMachineLiveData.getMachineId(), rotsShiftMaster.getShiftId(), date, rotsProductMaster.getProductId());
				
				
				if(null != listOfScrapCounts) {
					for (RotsScrapCountDetails rotsScrapCountDetails : listOfScrapCounts) {
						totalScrapCount = totalScrapCount + rotsScrapCountDetails.getScrapCount();
					}
				}
		   }
		   liveMachineStatusDetails.setScrapCount(totalScrapCount + " Units");
		   
			//Count target units for current shift for machine
			
			List<RotsTargetDetails> listOfTargetcount = this.oeeService.getTargetCountForShiftForMachine(rotsMachineLiveData.getMachineId(), rotsShiftMaster.getShiftId(), date, rotsProductMaster.getProductId());
			Integer targetCount = 0;
			if(null != listOfTargetcount) {
				for (RotsTargetDetails rotsTargetDetails : listOfTargetcount) {
					targetCount = targetCount + rotsTargetDetails.getTargetCount();
				}
			}
			liveMachineStatusDetails.setTarget(targetCount + " Units");
			
			//Set Good Count
			if(null != rotsMachineLiveData.getScrapCount() &&  null != rotsMachineLiveData.getProdCount()) {
				liveMachineStatusDetails.setGoodCount(String.valueOf(rotsMachineLiveData.getProdCount() - totalScrapCount));
			}
			
			//Set Scrap Percentage
			String scrapPercentage = "";
			if(null != totalScrapCount && null != rotsMachineLiveData.getProdCount()) {
				Double scrapPer = (double) ((totalScrapCount * 100) / rotsMachineLiveData.getProdCount());
				if(null != scrapPer) {
					scrapPercentage = DateUtils.convertToTwoDecimalPoint(scrapPer) + " %";
				}
			}
			liveMachineStatusDetails.setScrapPercentage(scrapPercentage);
			
			listOfLiveData.add(liveMachineStatusDetails);
			
			
		}
		
		//Data for blocks
		
		
		
		MachineCountMap runningCountMap = new MachineCountMap();
		runningCountMap.setKey("RUNNING");
		runningCountMap.setValue(runningCount);		
		listOfMachineCountMap.add(runningCountMap);
		
		MachineCountMap stopCountMap = new MachineCountMap();
		stopCountMap.setKey("STOP");
		stopCountMap.setValue(stopCount);		
		listOfMachineCountMap.add(stopCountMap);
		
		MachineCountMap idleCountMap = new MachineCountMap();
		idleCountMap.setKey("IDLE");
		idleCountMap.setValue(idleCount);		
		listOfMachineCountMap.add(idleCountMap);
		
		MachineCountMap offlineCountMap = new MachineCountMap();
		offlineCountMap.setKey("OFFLINE");
		offlineCountMap.setValue(offlineCount);		
		listOfMachineCountMap.add(offlineCountMap);
		
		totalCount = runningCount + stopCount + idleCount + offlineCount;
		
		MachineCountMap totalCountMap = new MachineCountMap();
		totalCountMap.setKey("Total");
		totalCountMap.setValue(totalCount);		
		listOfMachineCountMap.add(totalCountMap);
		
		liveMachineReportWrapper.setMachineCountMap(listOfMachineCountMap);
		liveMachineReportWrapper.setListOfLiveMachinedata(listOfLiveData);
		
		return liveMachineReportWrapper;
	}
	
}
