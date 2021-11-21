package com.rots.service;

import java.sql.Time;
import java.text.ParseException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rots.DAO.OEEDao;
import com.rots.DAO.RotsAdminDao;
import com.rots.constants.ROTSConstants;
import com.rots.constants.ShiftType;
import com.rots.constants.Status;
import com.rots.contract.DatePeriods;
import com.rots.contract.KeyValueWrapper;
import com.rots.contract.OEEDetails;
import com.rots.contract.OEETrendsWrapper;
import com.rots.contract.ShiftNumberPredicate;
import com.rots.entity.RotsBreakMaster;
import com.rots.entity.RotsMachineActivityDetails;
import com.rots.entity.RotsMachineLiveData;
import com.rots.entity.RotsMachineMaster;
import com.rots.entity.RotsOeeDetails;
import com.rots.entity.RotsProductMaster;
import com.rots.entity.RotsScheduledStopTranDtls;
import com.rots.entity.RotsScrapCountDetails;
import com.rots.entity.RotsShiftMaster;
import com.rots.entity.RotsTargetDetails;
import com.rots.util.DateUtils;
import java.time.ZoneId;

@Service("OEEService")
public class OEEServiceImpl implements OEEService{

	@Autowired
	private OEEDao oeeDao;
	
	@Autowired
	private RotsAdminDao adminDao;
	
	@Autowired
	private RotsAdminService adminService;
	
	@Autowired
	private CommonAPIService commonAPIService;
	
	//This method get all running records as per current shift number
	@Transactional(propagation = Propagation.REQUIRED)
	public List<RotsMachineActivityDetails> getAllRecordsAsPerCurrentShift(RotsMachineMaster rotsMachineMaster){
		
		RotsShiftMaster rotsShiftMaster = this.commonAPIService.getCurrentShift(new Date());
		
		List<RotsMachineActivityDetails> listOfRunningRecords = null;
		
		/*
		 * if(rotsShiftMaster.getShiftNumber() < ShiftType.SHIFT_3.getId()) {
		 * listOfRunningRecords =
		 * this.oeeDao.getRunningRecordsByMachineId(rotsMachineMaster,
		 * this.getStartDateOfDay()); }else {
		 * 
		 * //Get all running records from 8 am of previous date
		 * List<RotsMachineActivityDetails> listOfPreviousDay =
		 * this.oeeDao.getRunningRecordsByMachineId(rotsMachineMaster,
		 * this.getMidnightToday());
		 * 
		 * listOfRunningRecords =
		 * this.oeeDao.getRunningRecordsByMachineId(rotsMachineMaster,
		 * this.getMidnightToday());
		 * 
		 * if(null != listOfRunningRecords) {
		 * listOfRunningRecords.addAll(listOfPreviousDay); }
		 * 
		 * }
		 */
		
		return listOfRunningRecords;
	}
	
	//This method calculate Running time of Machine for mentioned product from start of Day i.e., 8 am
	@Transactional(propagation = Propagation.REQUIRED)
	public Long getRunningTimeInSecondsByMachineId(RotsMachineMaster rotsMachineMaster, Date date, RotsShiftMaster rotsShiftMaster, Date tillTime) {
		
		//RotsShiftMaster rotsShiftMaster = this.commonAPIService.getCurrentShift(new Date());
		Date startOfShift = this.copyTimeToDate(date, rotsShiftMaster.getStartTime());
		Date currentTime = this.copyTimeToDate(date, tillTime);
		
		//fetch all records for current shift
		List<RotsMachineActivityDetails> listOfRunningRecords = this.oeeDao.getRunningrecordsForMAchineForTimePeriod(rotsMachineMaster, startOfShift, currentTime);
		Long totalTime = 0L;
		
		if(null != listOfRunningRecords && listOfRunningRecords.size() > 0) {
			RotsMachineActivityDetails firstRecord = listOfRunningRecords.get(0);
			DatePeriods datePeriods = DateUtils.getOverlappingTimePeriod(startOfShift, currentTime, firstRecord.getStartDate(), firstRecord.getEndDate());
			
			if(null != datePeriods) {
				totalTime =  totalTime + ChronoUnit.SECONDS.between(datePeriods.getStartTime(), datePeriods.getEndTime());
			}else {
				totalTime =  totalTime + DateUtils.getDateDiff(firstRecord.getStartDate(), firstRecord.getEndDate(), TimeUnit.SECONDS);
			}
		}
		if(null != listOfRunningRecords && listOfRunningRecords.size() > 1) {
		
			for (int i = 1; i < listOfRunningRecords.size() - 1 ; i++) {
				totalTime = totalTime + DateUtils.getDateDiff(listOfRunningRecords.get(i).getStartDate(), listOfRunningRecords.get(i).getEndDate(), TimeUnit.SECONDS);
			}
			
		}
		if(null != listOfRunningRecords && listOfRunningRecords.size() > 0) {
		
			RotsMachineActivityDetails lastRecord = listOfRunningRecords.get(listOfRunningRecords.size() - 1);
			DatePeriods datePeriods = DateUtils.getOverlappingTimePeriod(startOfShift, currentTime, lastRecord.getStartDate(), lastRecord.getEndDate());
			
			if(null != datePeriods) {
				totalTime =  totalTime + ChronoUnit.SECONDS.between(datePeriods.getStartTime(), datePeriods.getEndTime());
			}else {
				totalTime =  totalTime + DateUtils.getDateDiff(lastRecord.getStartDate(), lastRecord.getEndDate(), TimeUnit.SECONDS);
			}
			
		}
		
		
		
		System.out.println(DateUtils.convertTimeIntoDaysHrMin(totalTime, TimeUnit.SECONDS));
		
		return totalTime;
	}
	
	
	//This method calculaye available of machine since start of day i.e., 8 am
	// totalAvailableTime = totalTimeSinceMorning  - (breaktime + ScheduledStoppageTime(For machine level stoppage and Stoppages applicable for all machines))
	@Transactional(propagation = Propagation.REQUIRED)
	public Long getAvailableTime(RotsMachineMaster rotsMachineMaster, Date date, RotsShiftMaster rotsShiftMaster, Date tillTime) {
	
	   long totalPlannedStopSeconds = 0;
	   
	   
		Date startOfShift = this.copyTimeToDate(date, rotsShiftMaster.getStartTime());
		Date currentTime = this.copyTimeToDate(date, tillTime);// uncomment after testing
		//Date currentTime = this.getStartOFMonth(); //comment after testing
	   List<DatePeriods> listOfOverlappingPeriods = new ArrayList<DatePeriods>();
	   List<RotsScheduledStopTranDtls> listOfScheduledStops = this.adminDao.getAllActiveScheduledStops(rotsMachineMaster.getMachineId(), startOfShift, currentTime);
	  
	 //  Date currentDate = Calendar.getInstance().getTime();
	   for (RotsScheduledStopTranDtls rotsScheduledStopTranDtls : listOfScheduledStops) {
		   DatePeriods datePeriods = DateUtils.getOverlappingTimePeriod(startOfShift, tillTime, rotsScheduledStopTranDtls.getStartDate(), rotsScheduledStopTranDtls.getEndDate());
		   if(null != datePeriods) {
			   listOfOverlappingPeriods.add(datePeriods);
			   totalPlannedStopSeconds = totalPlannedStopSeconds + ChronoUnit.SECONDS.between(datePeriods.getStartTime(), datePeriods.getEndTime());
		   }
		 //  totalPlannedStopMinutes = totalPlannedStopMinutes + DateUtils.getOverlappingPeriodInMinutes(startOfShift, currentDate, rotsScheduledStopTranDtls.getStartDate(), rotsScheduledStopTranDtls.getEndDate());
	   }
	   
	 //Get all breaks for current shift which are already started before current time. Calculate overlapping period of each beak and remove 
	   // the time from total time which is overlapped with sceduled stoppages
	   
	   List<RotsBreakMaster> listOfAllBreaks = this.adminService.getAllbreaksBeforeTime(tillTime, rotsShiftMaster.getShiftId());
	   
	   if(null != listOfAllBreaks && listOfAllBreaks.size() > 0) {
		  
		   
		   for (RotsBreakMaster rotsBreakMaster : listOfAllBreaks) {
			
			   DatePeriods datePeriods = DateUtils.getOverlappingTimePeriod(startOfShift, tillTime, rotsBreakMaster.getStartTime(), rotsBreakMaster.getEndTime());
			   if(null != datePeriods) {
				   listOfOverlappingPeriods.add(datePeriods);
				   totalPlannedStopSeconds = totalPlannedStopSeconds + ChronoUnit.SECONDS.between(datePeriods.getStartTime(), datePeriods.getEndTime());
			   }
			   
		   }
	   }
	   
	   //Remove period of time which is considered again from overlapping scheduled stops
	   for (int i = 0; i < listOfOverlappingPeriods.size(); i++) {
		  for(int j = i + 1; j < listOfOverlappingPeriods.size(); j++) {
			  
			  totalPlannedStopSeconds = totalPlannedStopSeconds - DateUtils.getOverlappingLocalTimePeriod(listOfOverlappingPeriods.get(i).getStartTime(), 
					  listOfOverlappingPeriods.get(i).getEndTime(), 
					  listOfOverlappingPeriods.get(j).getStartTime(), 
					  listOfOverlappingPeriods.get(j).getEndTime());
			  
		  }
	   }
	   
	   
	   //get All breaks of today whose start time is  before current time in descending order
	  // Time currentTime2 = DateUtils.convertTo24HourFormat((Time)Calendar.getInstance().getTime());
	  
	  
		 
	      
	       Long totalSeconds = DateUtils.getDateDiff(startOfShift, currentTime, TimeUnit.SECONDS);
	       
	       Long totalAvailableSeconds = totalSeconds - totalPlannedStopSeconds ;
	       
	       
	       return totalAvailableSeconds;	 
	   
	   
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public Integer getTotalProductionCountForMachineByDates(RotsMachineMaster rotsMachineMaster, Date date, RotsShiftMaster rotsShiftMaster, Date currentDate) {
		
	//	OEEDetails oeeDetails = new OEEDetails();
		Date startOfShift = this.copyTimeToDate(date, rotsShiftMaster.getStartTime());
		Date currentTime = this.copyTimeToDate(date, currentDate);
		Integer totalProdCount = 0;
	//	Date startTimeForAllRecords = null;
	//	Integer totalScrapCount = 0;
		
		//Get all records with REset flag and count prod and scrap count for that
	//	List<RotsMachineActivityDetails> listOfResetRecords = this.oeeDao.getActivityRecForTimePeriodByMachine(rotsMachineMaster, startOfShift, currentTime, ROTSConstants.RESET_COUNT.getId());
	//	for (RotsMachineActivityDetails rotsMachineActivityDetails : listOfResetRecords) {
	//		totalProdCount = totalProdCount + rotsMachineActivityDetails.getLiveProdCount();
	//		totalScrapCount = totalScrapCount + rotsMachineActivityDetails.getScrapCount();
	//	}
	//	//Need to get all records after the latest reset record
	//	//If no reset records yet for this timeperiod, then get all records from start of shift
	//	if(null != listOfResetRecords && listOfResetRecords.size() > 0) {
	//		RotsMachineActivityDetails rotsMachineActivityDetails = listOfResetRecords.get(0);
	//		startTimeForAllRecords = this.copyTimeToDate(date, rotsMachineActivityDetails.getEndDate());
			
	//	}else {
	//		startTimeForAllRecords = startOfShift;
	//	/}
		
		
		List<RotsMachineActivityDetails> listOFActivities = this.oeeDao.getActivityRecForTimePeriodByMachine(rotsMachineMaster, startOfShift, currentTime, null);
	
		if(null != listOFActivities && listOFActivities.size() > 0) {
			
			RotsMachineActivityDetails startRecord = null;
			for (RotsMachineActivityDetails rotsMachineActivityDetails : listOFActivities) {
				/* if(null == startRecord) {					
					startRecord = rotsMachineActivityDetails;				
				}				
				if(rotsMachineActivityDetails.getResetCount().equals(ROTSConstants.RESET_COUNT.getId())){
					totalProdCount = totalProdCount + ( rotsMachineActivityDetails.getLastrecordProdCount() - startRecord.getFirstRecordProdCount() );
					startRecord = null;
				}*/
				
				totalProdCount = totalProdCount + ( rotsMachineActivityDetails.getLastrecordProdCount() - rotsMachineActivityDetails.getFirstRecordProdCount() );
				
			}
			
			
			
			//totalProdCount = totalProdCount + listOFActivities.get(0).getLiveProdCount();
		//	totalScrapCount = totalScrapCount + listOFActivities.get(0).getScrapCount();
		}
		
	//	oeeDetails.setTotalProdCount(totalProdCount);
	//	oeeDetails.setTotalScrapCount(totalScrapCount);
		
		return totalProdCount;
	}
	
	@Scheduled(initialDelay = 60, fixedDelay = 30)
	@Transactional(propagation = Propagation.REQUIRED)
	public void calculateOEE() throws ParseException {
		List<RotsMachineMaster> listOfActiveMachines = this.adminDao.getAllActiveMachines();
		Date currentDate = new Date(); //uncomment after testing
		//Date currentDate = this.getStartOFMonthforTest(); //comment after testing
		RotsShiftMaster rotsShiftMaster = this.commonAPIService.getCurrentShift(currentDate);
		
		for (RotsMachineMaster rotsMachineMaster : listOfActiveMachines) {
			
			
			
			Long totalRunningTimeInSec = this.getRunningTimeInSecondsByMachineId(rotsMachineMaster, currentDate, rotsShiftMaster, currentDate);
		//	Long totalRunningTimeInSec = 13740L;
			Long availableTimeInSec = this.getAvailableTime(rotsMachineMaster, currentDate, rotsShiftMaster, currentDate);
			//Long availableTimeInSec = 13799L;
			Integer prodCount = this.getTotalProductionCountForMachineByDates(rotsMachineMaster, currentDate, rotsShiftMaster, currentDate);
			Integer scrapCount = 0;
			
			List<RotsScrapCountDetails> listOfScrapCount = this.oeeDao.getScrapCountForMachineForShift(rotsMachineMaster.getMachineId(), rotsShiftMaster.getShiftId(), currentDate);
			
			if(null != listOfScrapCount) {
				for (RotsScrapCountDetails rotsScrapCountDetails : listOfScrapCount) {
					scrapCount = scrapCount + rotsScrapCountDetails.getScrapCount();
				}
			}
			
			
			//Calculate Availability
			Double availability = null;
			
			if(null != availableTimeInSec && availableTimeInSec > 0) {
			   availability =  ((double)totalRunningTimeInSec/(double)availableTimeInSec) * 100;
			}else {
				
				//Available time is zero or null
				availability  = 0.00;
			}
			
			// Calculate productivity
			Double productivity = null;
			
			if(null != totalRunningTimeInSec && totalRunningTimeInSec > 0) {
				double targetFactor = ((double)rotsMachineMaster.getAvgIdealCycleTime()/(double)totalRunningTimeInSec) *100;
				
				if(null != prodCount && prodCount > 0) {
					productivity = targetFactor * prodCount;
				}else {
					//production count is zero or null
					productivity = 0.00;
				}
			}else {
				//If running time is null or zero
				productivity = 0.00;
			}
			
			
			// Calculate quality
			Double quality = null;
			
			if(null != scrapCount && scrapCount > 0) { 
					 if(null !=  prodCount &&  prodCount > 0) {
						 	Integer goodUnits = prodCount - scrapCount;
				
						 	quality = (double)(goodUnits/prodCount);
					 }else {
						 //If total prod count is null or zero
						 quality = 100.00;
					 }
			}else if(null !=  prodCount &&  prodCount > 0) {
				//If scrap count is null or zero
				quality = 100.00;
			}else {
				quality = 0.00;
			}
			
			Double tempAvgAvailability = availability != 0 ? (double)(availability/100) : 0;
			Double tempAvgProductivity = productivity != 0 ? (double)(productivity/100) : 0;
			Double tempAvgQuality = quality != 0 ? (double)(quality/100) : 0;
			
			Double oee = tempAvgAvailability * tempAvgProductivity * tempAvgQuality * 100;
			
			Date startOfShift = this.copyTimeToDate(currentDate, rotsShiftMaster.getStartTime());
			Date oeeDate = DateUtils.convertToOnlyDateWithoutTime(currentDate);
			
			RotsOeeDetails rotsOeeDetails = this.oeeDao.getOEEForMachineForShiftOnDate(rotsMachineMaster.getMachineId(), rotsShiftMaster.getShiftId(), oeeDate);
			
			if(null == rotsOeeDetails) {
			   this.constructOEEDetails(currentDate, rotsShiftMaster, rotsMachineMaster, totalRunningTimeInSec,
					availableTimeInSec, prodCount, scrapCount, availability, productivity, quality, oee, startOfShift);
			}else {
				//Update existing OEE details for current shift 
				
				this.updateRotsOEEDetails(currentDate, totalRunningTimeInSec, availableTimeInSec, prodCount, scrapCount,
						availability, productivity, quality, oee, startOfShift, rotsOeeDetails);
			}
		}
	}

	private void updateRotsOEEDetails(Date currentDate, Long totalRunningTimeInSec, Long availableTimeInSec,
			Integer prodCount, Integer scrapCount, Double availability, Double productivity, Double quality, Double oee,
			Date startOfShift, RotsOeeDetails rotsOeeDetails) {
		rotsOeeDetails.setAvailability(availability);
		rotsOeeDetails.setEndTime(currentDate);
		rotsOeeDetails.setOee(oee);
		rotsOeeDetails.setProductivity(productivity);
		rotsOeeDetails.setQuality(quality);
		
		if(null != availableTimeInSec) {
			rotsOeeDetails.setAvailableTime(availableTimeInSec.intValue());
		}
		
		if(null != totalRunningTimeInSec) {
			rotsOeeDetails.setRunningTime(totalRunningTimeInSec.intValue());
		}
		
		rotsOeeDetails.setScrapCount(scrapCount);
		rotsOeeDetails.setStartTime(startOfShift);
		rotsOeeDetails.setSystemDateTime(currentDate);
		rotsOeeDetails.setTotalCounts(prodCount);
		rotsOeeDetails.setUpdatedBy(ROTSConstants.SYSTEM.getId());
		rotsOeeDetails.setUpdatedDate(currentDate);
		this.oeeDao.mergeRotsOeeDetails(rotsOeeDetails);
	}

	
	private void constructOEEDetails(Date currentDate, RotsShiftMaster rotsShiftMaster,
			RotsMachineMaster rotsMachineMaster, Long totalRunningTimeInSec, Long availableTimeInSec,
			Integer prodCount, Integer scrapCount, Double availability, Double productivity, Double quality, Double oee, Date startOfShift) {
		
		
		
		RotsOeeDetails rotsOeeDetails = new RotsOeeDetails();
		rotsOeeDetails.setAvailability(availability);
		rotsOeeDetails.setProductivity(productivity);
		rotsOeeDetails.setQuality(quality);
		rotsOeeDetails.setOee(oee);
		
		if(null != availableTimeInSec) {
			rotsOeeDetails.setAvailableTime(availableTimeInSec.intValue());
		}
		
		if(null != totalRunningTimeInSec) {
			rotsOeeDetails.setRunningTime(totalRunningTimeInSec.intValue());
		}
		
		
		
		rotsOeeDetails.setStartTime(startOfShift);
		
		rotsOeeDetails.setEndTime(currentDate);
		
		rotsOeeDetails.setMachineId(rotsMachineMaster.getMachineId());
		rotsOeeDetails.setScrapCount(scrapCount);
		rotsOeeDetails.setTotalCounts(prodCount);
		rotsOeeDetails.setShiftId(rotsShiftMaster.getShiftId());
		rotsOeeDetails.setSystemDateTime(currentDate);
		rotsOeeDetails.setOeeDate(currentDate);
		rotsOeeDetails.setUpdatedBy(ROTSConstants.SYSTEM.getId());
		rotsOeeDetails.setUpdatedDate(currentDate);
		rotsOeeDetails.setCreatedBy(ROTSConstants.SYSTEM.getId());
		rotsOeeDetails.setCreatedDate(currentDate);
		
		this.oeeDao.saveRotsOeeDetails(rotsOeeDetails);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public OEETrendsWrapper getCurrentOEE(Integer oeeType) throws ParseException{
		
			if(null != oeeType && ROTSConstants.CURRENT_SHIFT.getId().equals(oeeType)) {
				
				return this.getCurrentOEEForAllMachines();
				
			}else if(null != oeeType && ROTSConstants.CURRENT_DAY.getId().equals(oeeType)) {
				
				return this.getCurrentDayOEEForAllMachines();
				
			}else if(null != oeeType && ROTSConstants.CURRENT_MONTH.getId().equals(oeeType)) {
				
				return this.getCurrentMonthOEEForAllMachines();
			}
			
			return null;
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED)
	public OEETrendsWrapper getCurrentOEEForAllMachines() throws ParseException{
		
		OEETrendsWrapper oeeTrendsWrapper = new OEETrendsWrapper();
		
		List<KeyValueWrapper> listOFAvgParams = new ArrayList<KeyValueWrapper>();
		Date currentDate = new Date();
		
		 List<OEEDetails> listOfOEEDetails = new ArrayList<OEEDetails>();
		 
		RotsShiftMaster currentShift = this.commonAPIService.getCurrentShift(currentDate);
		Date oeeDate = DateUtils.convertToOnlyDateWithoutTime(currentDate);
		
		List<RotsMachineMaster> listOFActiveMachines = this.adminDao.getAllActiveMachines();
		
	
		Double availability = 0.0;
		Double productivity = 0.0;
		Double quality = 0.0;
		Integer availabCount = 0;
		Integer prodCount = 0;
		Integer qualCount = 0;
		
		for (RotsMachineMaster rotsMachineMaster : listOFActiveMachines) {
			RotsOeeDetails rotsOeeDetails = this.oeeDao.getOEEForMachineForShiftOnDate(rotsMachineMaster.getMachineId(), currentShift.getShiftId(), oeeDate);
			
			listOfOEEDetails.add(this.constructOEEDetails(rotsOeeDetails));
			if(null != rotsOeeDetails.getAvailability() && rotsOeeDetails.getAvailability() > 0) {
				availabCount++;
				availability = availability + rotsOeeDetails.getAvailability();
			}
			
			if(null != rotsOeeDetails.getProductivity() && rotsOeeDetails.getProductivity() > 0) {
				prodCount++;
				productivity = productivity + rotsOeeDetails.getProductivity();
			}
			
			if(null != rotsOeeDetails.getQuality() && rotsOeeDetails.getQuality() > 0) {
				qualCount++;
				quality = quality + rotsOeeDetails.getQuality();
			}
			
		}
		
		this.constructAvgParamsList(listOFAvgParams, availability, productivity, quality, availabCount, prodCount,
				qualCount);
		
		
		oeeTrendsWrapper.setListOFAvgOEEParams(listOFAvgParams);
        oeeTrendsWrapper.setListOfMachineOEE(listOfOEEDetails);
		
		return oeeTrendsWrapper;
	}

	private void constructAvgParamsList(List<KeyValueWrapper> listOFAvgParams, Double availability, Double productivity,
			Double quality, Integer availabCount, Integer prodCount, Integer qualCount) {
		Double avgAvailability = 0.0;
		if(availabCount > 0) {
			avgAvailability = availability/availabCount;
		}
		Double avgProductivity = 0.0;
		
		if(prodCount > 0) {
			avgProductivity = productivity/prodCount;
		}
		
		Double avgQuality = 0.0;
		if(qualCount > 0) {
			avgQuality = quality/qualCount;
		}
		
		Double tempAvgAvailability = avgAvailability != 0 ? (double)(avgAvailability/100) : 0;
		Double tempAvgProductivity = avgProductivity != 0 ? (double)(avgProductivity/100) : 0;
		Double tempAvgQuality = avgQuality != 0 ? (double)(avgQuality/100) : 0;
		
		Double OEE = tempAvgAvailability * tempAvgProductivity * tempAvgQuality * 100;
		
		KeyValueWrapper avgAvailabilityWrapper = new KeyValueWrapper();
		avgAvailabilityWrapper.setKey("Availability");
		avgAvailabilityWrapper.setValue(avgAvailability);		
		listOFAvgParams.add(avgAvailabilityWrapper);
		
		KeyValueWrapper avgQualityWrapper = new KeyValueWrapper();
		avgQualityWrapper.setKey("Quality");
		avgQualityWrapper.setValue(avgQuality);		
		listOFAvgParams.add(avgQualityWrapper);
		
		KeyValueWrapper avgProductivityWrapper = new KeyValueWrapper();
		avgProductivityWrapper.setKey("Productivity");
		avgProductivityWrapper.setValue(avgProductivity);		
		listOFAvgParams.add(avgProductivityWrapper);
		
		KeyValueWrapper avgOEEWrapper = new KeyValueWrapper();
		avgProductivityWrapper.setKey("OEE");
		avgProductivityWrapper.setValue(OEE);		
		listOFAvgParams.add(avgOEEWrapper);
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED)
	public OEETrendsWrapper getCurrentDayOEEForAllMachines() throws ParseException{
		
        OEETrendsWrapper oeeTrendsWrapper = new OEETrendsWrapper();
		
		List<KeyValueWrapper> listOFAvgParams = new ArrayList<KeyValueWrapper>();
		
		Date currentDate = new Date();
		
		List<OEEDetails> listOfOEEDetails = new ArrayList<OEEDetails>();
		 
		Date oeeDate = DateUtils.convertToOnlyDateWithoutTime(currentDate);
		
		List<RotsMachineMaster> listOFActiveMachines = this.adminDao.getAllActiveMachines();
		
	
		Double availability = 0.0;
		Double productivity = 0.0;
		Double quality = 0.0;
		Integer availabCount = 0;
		Integer prodCount = 0;
		Integer qualCount = 0;
		
		
		for (RotsMachineMaster rotsMachineMaster : listOFActiveMachines) {
			
           OEEDetails oeeDetails = this.oeeDao.getOeeDetailsForTodayForMachine(rotsMachineMaster.getMachineId(), oeeDate);
			
           if(null != oeeDetails) {
			String currentDateStr = DateUtils.convertDateToStringWithTime(currentDate);
			
			oeeDetails.setLastCalculatedTime(currentDateStr);
			
			oeeDetails.setMachineName(rotsMachineMaster.getMachineName());
           }
			
           if(null != oeeDetails.getAvailability() && oeeDetails.getAvailability() > 0) {
				availabCount++;
				availability = availability + oeeDetails.getAvailability();
			}
			
			if(null != oeeDetails.getProductivity() && oeeDetails.getProductivity() > 0) {
				prodCount++;
				productivity = productivity + oeeDetails.getProductivity();
			}
			
			if(null != oeeDetails.getQuality() && oeeDetails.getQuality() > 0) {
				qualCount++;
				quality = quality + oeeDetails.getQuality();
			}
			
			listOfOEEDetails.add(oeeDetails);
		}
		
		this.constructAvgParamsList(listOFAvgParams, availability, productivity, quality, availabCount, prodCount,
				qualCount);
		
		
		oeeTrendsWrapper.setListOFAvgOEEParams(listOFAvgParams);
        oeeTrendsWrapper.setListOfMachineOEE(listOfOEEDetails);
		
		return oeeTrendsWrapper;
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED)
    public OEETrendsWrapper getCurrentMonthOEEForAllMachines() throws ParseException{
		
		 OEETrendsWrapper oeeTrendsWrapper = new OEETrendsWrapper();
			
		 List<KeyValueWrapper> listOFAvgParams = new ArrayList<KeyValueWrapper>();
			
		
		 List<OEEDetails> listOfOEEDetails = new ArrayList<OEEDetails>();
		 
		
		Date oeeDate = this.getStartOFMonth();
		
		List<RotsMachineMaster> listOFActiveMachines = this.adminDao.getAllActiveMachines();
		
		Double availability = 0.0;
		Double productivity = 0.0;
		Double quality = 0.0;
		Integer availabCount = 0;
		Integer prodCount = 0;
		Integer qualCount = 0;
		
		for (RotsMachineMaster rotsMachineMaster : listOFActiveMachines) {
			
			OEEDetails oeeDetails = this.oeeDao.getOeeDetailsForMonthForMachine(rotsMachineMaster.getMachineId(), oeeDate);
			
			String currentDate = DateUtils.convertDateToStringWithTime(new Date());
			if(null == oeeDetails) {
				oeeDetails = new OEEDetails();
			}
			
			oeeDetails.setMachineName(rotsMachineMaster.getMachineName());
			
			 if(null != oeeDetails.getAvailability() && oeeDetails.getAvailability() > 0) {
					availabCount++;
					availability = availability + oeeDetails.getAvailability();
			  }
				
			 if(null != oeeDetails.getProductivity() && oeeDetails.getProductivity() > 0) {
					prodCount++;
					productivity = productivity + oeeDetails.getProductivity();
			 }
				
			 if(null != oeeDetails.getQuality() && oeeDetails.getQuality() > 0) {
					qualCount++;
					quality = quality + oeeDetails.getQuality();
			 }
			
			listOfOEEDetails.add(oeeDetails);
		}
		
		this.constructAvgParamsList(listOFAvgParams, availability, productivity, quality, availabCount, prodCount,
				qualCount);
		
		
		oeeTrendsWrapper.setListOFAvgOEEParams(listOFAvgParams);
        oeeTrendsWrapper.setListOfMachineOEE(listOfOEEDetails);
		
		return oeeTrendsWrapper;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	//@Scheduled(fixedDelay = 300000)
	public void getAllPendingShifts() throws ParseException {
		
		Date currentDate = DateUtils.convertToOnlyDateWithoutTime(new Date());
		
		RotsShiftMaster currentShift = this.commonAPIService.getCurrentShift(new Date());
		
		List<RotsOeeDetails> listOfPendingOEEDtls = this.oeeDao.getAllPendingOEEDtls(currentDate, currentShift);
		
		for (RotsOeeDetails rotsOeeDetails : listOfPendingOEEDtls) {
			
			//System.out.println("Pending OEE IDs" + rotsOeeDetails.getId());
			RotsShiftMaster shiftMaster = this.adminDao.getShiftMasterById(rotsOeeDetails.getShiftId());
			
			this.calculateOEEForPendingShift(rotsOeeDetails.getOeeDate(), shiftMaster.getEndTime(), shiftMaster, rotsOeeDetails.getMachineId());
			
			 rotsOeeDetails.setIsFinalFlag(ROTSConstants.ACTIVE.getId());

		     this.oeeDao.mergeRotsOeeDetails(rotsOeeDetails);
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void calculateOEEForPendingShift(Date date, Date tillTime, RotsShiftMaster shiftMaster, Integer machineId) throws ParseException {
		
		   RotsMachineMaster rotsMachineMaster = this.adminService.getMachineById(machineId);
		
			
			Long totalRunningTimeInSec = this.getRunningTimeInSecondsByMachineId(rotsMachineMaster, date, shiftMaster, tillTime);
			
			Long availableTimeInSec = this.getAvailableTime(rotsMachineMaster, date, shiftMaster, tillTime);
			
			Integer prodCount = this.getTotalProductionCountForMachineByDates(rotsMachineMaster, date, shiftMaster, tillTime);
			Integer scrapCount = 0;
			
			List<RotsScrapCountDetails> listOfScrapCount = this.oeeDao.getScrapCountForMachineForShift(rotsMachineMaster.getMachineId(), shiftMaster.getShiftId(), date);
			
			if(null != listOfScrapCount) {
				for (RotsScrapCountDetails rotsScrapCountDetails : listOfScrapCount) {
					scrapCount = scrapCount + rotsScrapCountDetails.getScrapCount();
				}
			}
			
			
			//Calculate Availability
			Double availability = null;
			Integer devideFactor = 0;
			if(null != availableTimeInSec && availableTimeInSec > 0) {
			   availability = (double) ((double)totalRunningTimeInSec/(double)availableTimeInSec) * 100;
			   devideFactor++;
			}else {
				
				//Available time is zero or null
				availability  = 0.00;
			}
			
			// Calculate productivity
			Double productivity = null;
			
			if(null != totalRunningTimeInSec && totalRunningTimeInSec > 0) {
				double targetFactor = ((double)rotsMachineMaster.getAvgIdealCycleTime()/(double)totalRunningTimeInSec) * 100;
				
				if(null != prodCount && prodCount > 0) {
					productivity = targetFactor * prodCount;
					 devideFactor++;
				}else {
					//production count is zero or null
					productivity = 0.00;
				}
			}else {
				//If running time is null or zero
				productivity = 0.00;
			}
			
			
			// Calculate quality
			Double quality = null;
			
			if(null != scrapCount && scrapCount > 0) { 
					 if(null !=  prodCount &&  prodCount > 0) {
						 	Integer goodUnits = prodCount - scrapCount;
				
						 	quality = (double)((double)goodUnits/(double)prodCount) * 100;
						 	 devideFactor++;
					 }else {
						 //If total prod count is null or zero
						 quality = 100.00;
					 }
			}else if(null !=  prodCount &&  prodCount > 0) {
				//If scrap count is null or zero
				quality = 100.00;
			}else {
				quality = 0.00;
			}
			
			Double oee = (availability + productivity + quality)/devideFactor;
			Date startOfShift = this.copyTimeToDate(date, shiftMaster.getStartTime());
			Date endTime= this.copyTimeToDate(date, tillTime);
			Date oeeDate = DateUtils.convertToOnlyDateWithoutTime(date);
			
			RotsOeeDetails rotsOeeDetails = this.oeeDao.getOEEForMachineForShiftOnDate(rotsMachineMaster.getMachineId(), shiftMaster.getShiftId(), oeeDate);
			
			if(null == rotsOeeDetails) {
			   this.constructOEEDetails(endTime, shiftMaster, rotsMachineMaster, totalRunningTimeInSec,
					availableTimeInSec, prodCount, scrapCount, availability, productivity, quality, oee, startOfShift);
			}else {
				//Update existing OEE details for current shift 
				
				this.updateRotsOEEDetails(endTime, totalRunningTimeInSec, availableTimeInSec, prodCount, scrapCount,
						availability, productivity, quality, oee, startOfShift, rotsOeeDetails);
			}
		
	}
   
	private OEEDetails constructOEEDetails(RotsOeeDetails rotsOeeDetails) {
		
		OEEDetails oeeDetails = new OEEDetails();
		
		RotsMachineMaster rotsMachineMaster = this.adminDao.getMachineById(rotsOeeDetails.getMachineId());
		
		oeeDetails.setAvailability(rotsOeeDetails.getAvailability());
		oeeDetails.setAvailableTime(rotsOeeDetails.getAvailableTime());
		oeeDetails.setRunningTime(rotsOeeDetails.getRunningTime());
		
		oeeDetails.setTotalProdCount(rotsOeeDetails.getTotalCounts());
		oeeDetails.setProductivity(rotsOeeDetails.getProductivity());
		
		oeeDetails.setTotalScrapCount(rotsOeeDetails.getScrapCount());
		
		oeeDetails.setQuality(rotsOeeDetails.getQuality());
		oeeDetails.setOee(rotsOeeDetails.getOee());
		
		String startTimeStr = DateUtils.convertDateToStringWithTime(rotsOeeDetails.getStartTime());
		oeeDetails.setStartTime(startTimeStr);
		
		String ensTimeStr = DateUtils.convertDateToStringWithTime(rotsOeeDetails.getEndTime());
		oeeDetails.setEndTime(ensTimeStr);
		
		String calculatedTime = DateUtils.convertDateToStringWithTime(rotsOeeDetails.getEndTime());
		
		oeeDetails.setLastCalculatedTime(calculatedTime);
		
		oeeDetails.setMachineId(rotsOeeDetails.getMachineId());
		oeeDetails.setMachineName(rotsMachineMaster.getMachineName());
		
		oeeDetails.setShiftId(rotsOeeDetails.getShiftId());
		
		String systemDateStr = DateUtils.convertDateToStringWithTime(rotsOeeDetails.getEndTime());
		oeeDetails.setSystemDate(systemDateStr);
		
		String oeeDateStr = DateUtils.convertDateToStringWithTime(rotsOeeDetails.getEndTime());
		oeeDetails.setOeeDate(oeeDateStr);
		
		
		return oeeDetails;
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<RotsScrapCountDetails> getScrapCountForMachineForShift(Integer mchineId, Integer shiftId, Date date){
		return this.oeeDao.getScrapCountForMachineForShift(mchineId, shiftId, date);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<RotsTargetDetails> getTargetCountForShiftForMachine(Integer mchineId, Integer shiftId, Date date){
		return this.oeeDao.getTargetCountForShiftForMachine(mchineId, shiftId, date);
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<RotsScrapCountDetails> getScrapCountForMachineForShift(Integer mchineId, Integer shiftId, Date date, Integer productId){
	   return this.oeeDao.getScrapCountForMachineForShift(mchineId, shiftId, date, productId);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)  
	public List<RotsTargetDetails> getTargetCountForShiftForMachine(Integer mchineId, Integer shiftId, Date date, Integer productId){
		return this.oeeDao.getTargetCountForShiftForMachine(mchineId, shiftId, date, productId);
	}
	
	public Date copyTimeToDate(Date date, Date time) {
	    Calendar t = Calendar.getInstance();
	    t.setTime(time);

	    Calendar c = Calendar.getInstance();
	    c.setTime(date);
	    c.set(Calendar.HOUR_OF_DAY, t.get(Calendar.HOUR_OF_DAY));
	    c.set(Calendar.MINUTE, t.get(Calendar.MINUTE));
	    c.set(Calendar.SECOND, t.get(Calendar.SECOND));
	    c.set(Calendar.MILLISECOND, t.get(Calendar.MILLISECOND));
	    return c.getTime();
	}
	
	private Date getStartDateOfDay(Date time) {
		// today    
		   Calendar date = new GregorianCalendar();
		   date.setTime(time);
		   // reset hour, minutes, seconds and millis
		   System.out.println(date.getTime());
		   return date.getTime();
	   }
	   
	   private Date getStartDateOfDayFor3rdShift() {
			// today    
			   Calendar date = new GregorianCalendar();
			   // reset hour, minutes, seconds and millis
			   date.add(Calendar.DAY_OF_YEAR, -1);
			   date.set(Calendar.HOUR_OF_DAY, 8);
			   date.set(Calendar.MINUTE, 0);
			   date.set(Calendar.SECOND, 0);
			   date.set(Calendar.MILLISECOND, 0);
			   
			   return date.getTime();
	   }
	   
	   private Date getMidnightToday() {
		   
		   Calendar date = new GregorianCalendar();
		   // reset hour, minutes, seconds and millis
		   date.set(Calendar.HOUR_OF_DAY, 0);
		   date.set(Calendar.MINUTE, 0);
		   date.set(Calendar.SECOND, 0);
		   date.set(Calendar.MILLISECOND, 0);
		   
		   return date.getTime();
	   }
	   
     private Date getStartOFMonth() {
		   
		   Calendar date = Calendar.getInstance();
		   // reset hour, minutes, seconds and millis\
		   date.set(Calendar.DAY_OF_MONTH, 1);
		   date.set(Calendar.HOUR_OF_DAY, 0);
		   date.set(Calendar.MINUTE, 0);
		   date.set(Calendar.SECOND, 0);
		   date.set(Calendar.MILLISECOND, 0);
		   
		  
		   return date.getTime();
	   }

     private Date getStartOFMonthforTest() {
		   
		   Calendar date = Calendar.getInstance();
		   // reset hour, minutes, seconds and millis\
		   date.set(Calendar.DAY_OF_MONTH, 20);
		   date.set(Calendar.MONTH, 10);
		   date.set(Calendar.YEAR, 2020);
		   date.set(Calendar.HOUR_OF_DAY, 12);
		   date.set(Calendar.MINUTE, 0);
		   date.set(Calendar.SECOND, 0);
		   date.set(Calendar.MILLISECOND, 0);
		   
		  
		   return date.getTime();
	   }
	   
	}

