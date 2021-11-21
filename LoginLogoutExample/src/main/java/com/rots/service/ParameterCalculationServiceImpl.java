package com.rots.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rots.DAO.ParameterCalculationDao;
import com.rots.constants.ROTSConstants;
import com.rots.contract.MachineActivityDetails;
import com.rots.entity.RotsMachineActivityDetails;
import com.rots.entity.RotsMachineDataDetails;
import com.rots.entity.RotsMachineDataTest;
import com.rots.entity.RotsProductMaster;
import com.rots.entity.RotsShiftMaster;
import com.rots.restController.AdminController;
import com.rots.util.DateUtils;

@Service("ParameterCalculationService")
public class ParameterCalculationServiceImpl implements ParameterCalculationService{
	
	
	@Autowired
	private ParameterCalculationDao parameterCalculationDao;
	
	@Autowired
	private RotsAdminService rotsAdminService;
	
	@Autowired
	private CommonAPIService commonAPIService;
	
	private static final Logger logger = Logger.getLogger(ParameterCalculationServiceImpl.class);
	
	@Transactional(propagation = Propagation.REQUIRED)
	public List<RotsProductMaster> getDistinctProductIds(){
		//return this.parameterCalculationDao.getDistinctProductIds();
		return null;
	}
	
	
	
	@Transactional(propagation = Propagation.REQUIRED)
	//@Scheduled(fixedDelay = 300000)
	public void processRecords(){
		
		logger.info("ParameterCalculationServiceImpl.processRecords " + new Date());	 
		  List<RotsMachineDataDetails> listOfUnprocessedRecords = this.parameterCalculationDao.getUnprocessedRecords();
		  
			  if(null != listOfUnprocessedRecords && listOfUnprocessedRecords.size() > 0) {
				  
				//  RotsMachineDataDetails startRecord = listOfUnprocessedRecords.get(0);
				  RotsMachineDataDetails lastRecord = null;
				  RotsMachineDataDetails startRecord = null;
				  Set<Integer> setOfReasonIds = new HashSet<Integer>();
				  List<RotsMachineDataDetails> listOfProcessedRecord = new ArrayList<RotsMachineDataDetails>();
				  for (int i = 0; i < listOfUnprocessedRecords.size();  i++) {
					  RotsMachineActivityDetails machineActivityDetails = new RotsMachineActivityDetails();
					 
					  if (startRecord == null) {
						startRecord = listOfUnprocessedRecords.get(i);
						startRecord.setProcessFlag(ROTSConstants.ACTIVE.getId());
						listOfProcessedRecord.add(startRecord);
						i++;
					}
					
					 
					  
					  //Set All parameter values for current activity
					  
					  if (i < listOfUnprocessedRecords.size()) {
						RotsMachineDataDetails currentRecord = listOfUnprocessedRecords.get(i);
						setOfReasonIds.add(currentRecord.getReasonId());
						if (currentRecord.getStatus() != startRecord.getStatus()
								|| currentRecord.getOperatorId() != startRecord.getOperatorId()
								|| currentRecord.getReasonId() != startRecord.getReasonId()
								|| currentRecord.getProductCode() != startRecord.getProductCode()
								|| currentRecord.getResetCount() != ROTSConstants.ACTIVE.getId()
								|| this.isStartOfNewShift(startRecord, currentRecord)
								|| i == (listOfUnprocessedRecords.size() - 1)) {
							
							//If this is not last record and if successful because other condition then consider i-1 as last Record
							if(i == (listOfUnprocessedRecords.size() - 1) || currentRecord.getResetCount() != ROTSConstants.ACTIVE.getId())
							 {
								 lastRecord = currentRecord;
							 }else {
								 lastRecord = listOfUnprocessedRecords.get(i-1);		
					     	 }
							lastRecord.setProcessFlag(ROTSConstants.ACTIVE.getId());
							listOfProcessedRecord.add(startRecord);
						} else {
							//Ids of processed records to uopdate process flag in RotsMachineDataDetails
							currentRecord.setProcessFlag(ROTSConstants.ACTIVE.getId());
							listOfProcessedRecord.add(currentRecord);
							if( i < listOfUnprocessedRecords.size()) {
							 continue;
							}
						}
						
						RotsProductMaster productMaster = this.rotsAdminService.getProdMasterByCode(lastRecord.getProductCode());
						RotsShiftMaster rotsShiftMaster = this.commonAPIService.getCurrentShift(lastRecord.getRecordDateTime());
						
						machineActivityDetails.setRotsProductMaster(productMaster);
						machineActivityDetails.setStatus(lastRecord.getStatus());
						machineActivityDetails.setStartDate(startRecord.getRecordDateTime());
						machineActivityDetails.setEndDate(lastRecord.getRecordDateTime());
						machineActivityDetails.setFirstRecordProdCount(startRecord.getLiveProdCount());
						machineActivityDetails.setLastrecordProdCount(lastRecord.getLiveProdCount());
						machineActivityDetails.setScrapCount(lastRecord.getScrapCount());
						machineActivityDetails.setOperatorId(lastRecord.getOperatorId());
						machineActivityDetails.setReasonId(lastRecord.getReasonId());
						machineActivityDetails.setCreatedBy(ROTSConstants.SYSTEM.getId());
						machineActivityDetails.setCreatedDate(new Date());
						machineActivityDetails.setUpdatedBy(ROTSConstants.SYSTEM.getId());
						machineActivityDetails.setUpdatedDate(new Date());
						machineActivityDetails.setMachineId(productMaster.getMachineMaster().getMachineId());
						if(null != rotsShiftMaster) {
							machineActivityDetails.setShiftId(rotsShiftMaster.getShiftId());
						}
						this.parameterCalculationDao.saveRotsMachineActivityDetails(machineActivityDetails);
						
						//Update process_flag for all processed Records
						this.parameterCalculationDao.mergeAllRotsMachineDataDetails(listOfProcessedRecord);
						
					}
					  
					//Reset All values for next run
					  startRecord = null;
					  lastRecord = null;
					  i--;
					  
			  }
				  
		        
			}
		  
	}
	
	private boolean isStartOfNewShift(RotsMachineDataDetails startRecord, RotsMachineDataDetails lastRecord) {
		Date firstShiftStart = this.getStartOfFirstShit();
		
		Date secShiftStart = this.getStartOfSecondShit();
		
		Date thirdtShiftStart = this.getStartOfThirdShit();
		
		if(startRecord.getRecordDateTime().before(firstShiftStart) && firstShiftStart.after(lastRecord.getRecordDateTime())) {
			return true;
		}
		
		if(startRecord.getRecordDateTime().before(secShiftStart) && secShiftStart.after(lastRecord.getRecordDateTime())) {
			return true;
		}
		
		if(startRecord.getRecordDateTime().before(thirdtShiftStart) && thirdtShiftStart.after(lastRecord.getRecordDateTime())) {
			return true;
		}
		
		return false;
	}
	
	@Scheduled(fixedDelay = 60000)
	@Transactional(propagation = Propagation.REQUIRED)
	public void processRecordsTest(){
		
		logger.info("Executing ParameterCalculationServiceImpl.processRecordsTest " + new Date());	
		
		  List<RotsMachineDataTest> listOfUnprocessedRecords = this.parameterCalculationDao.getUnprocessedRecordsTest();
		  
			  if(null != listOfUnprocessedRecords && listOfUnprocessedRecords.size() > 0) {
				  
				//  RotsMachineDataDetails startRecord = listOfUnprocessedRecords.get(0);
				  RotsMachineDataTest lastRecord = null;
				  RotsMachineDataTest startRecord = null;
				  Set<Integer> setOfReasonIds = new HashSet<Integer>();
				  List<RotsMachineDataTest> listOfProcessedRecord = new ArrayList<RotsMachineDataTest>();
				  for (int i = 0; i < listOfUnprocessedRecords.size();  i++) {
					  RotsMachineActivityDetails machineActivityDetails = new RotsMachineActivityDetails();
					 
					  if (startRecord == null) {
						startRecord = listOfUnprocessedRecords.get(i);
						startRecord.setProcessFlag(ROTSConstants.ACTIVE.getId());
						listOfProcessedRecord.add(startRecord);
						i++;
					}
					
					 
					  
					  //Set All parameter values for current activity
					  
					  if (i < listOfUnprocessedRecords.size()) {
						  RotsMachineDataTest currentRecord = listOfUnprocessedRecords.get(i);
						setOfReasonIds.add(currentRecord.getReasonId());
						if (currentRecord.getStatus() != startRecord.getStatus()
								|| !currentRecord.getOperatorId().equals(startRecord.getOperatorId())
								|| currentRecord.getReasonId() != startRecord.getReasonId()
								|| !currentRecord.getProductCode().equals(startRecord.getProductCode())
								|| currentRecord.getRecordDateTime().equals(this.getStartDateOfDay())) {
							
							//If this is not last record and if successful because other condition then consider i-1 as last Record
						
							lastRecord = listOfUnprocessedRecords.get(i-1);	
							i--;
					     	 
							lastRecord.setProcessFlag(ROTSConstants.ACTIVE.getId());
							listOfProcessedRecord.add(startRecord);
						}else if(currentRecord.getResetCount() == ROTSConstants.ACTIVE.getId() || i == (listOfUnprocessedRecords.size() - 1)) { 
							lastRecord = currentRecord;
						}else {
							//Ids of processed records to uopdate process flag in RotsMachineDataDetails
							currentRecord.setProcessFlag(ROTSConstants.ACTIVE.getId());
							listOfProcessedRecord.add(currentRecord);
							if( i < listOfUnprocessedRecords.size()) {
							 continue;
							}
						}
						
						RotsProductMaster productMaster = this.rotsAdminService.getProdMasterByCode(lastRecord.getProductCode());
						RotsShiftMaster rotsShiftMaster = this.commonAPIService.getCurrentShift(lastRecord.getRecordDateTime());
						
						machineActivityDetails.setRotsProductMaster(productMaster);
						machineActivityDetails.setStatus(lastRecord.getStatus());
						machineActivityDetails.setStartDate(startRecord.getRecordDateTime());
						machineActivityDetails.setEndDate(lastRecord.getRecordDateTime());
						machineActivityDetails.setFirstRecordProdCount(startRecord.getLiveProdCount());
						machineActivityDetails.setLastrecordProdCount(lastRecord.getLiveProdCount());
						machineActivityDetails.setScrapCount(lastRecord.getScrapCount());
						machineActivityDetails.setOperatorId(lastRecord.getOperatorId());
						machineActivityDetails.setReasonId(lastRecord.getReasonId());
						machineActivityDetails.setCreatedBy(ROTSConstants.SYSTEM.getId());
						machineActivityDetails.setCreatedDate(new Date());
						machineActivityDetails.setUpdatedBy(ROTSConstants.SYSTEM.getId());
						machineActivityDetails.setUpdatedDate(new Date());
						machineActivityDetails.setMachineId(productMaster.getMachineMaster().getMachineId());
						machineActivityDetails.setResetCount(lastRecord.getResetCount());
						
						if(null != rotsShiftMaster) {
							machineActivityDetails.setShiftId(rotsShiftMaster.getShiftId());
						}
						
						this.parameterCalculationDao.saveRotsMachineActivityDetails(machineActivityDetails);
						
						//Update process_flag for all processed Records
						this.parameterCalculationDao.mergeAllRotsMachineDataTestDetails(listOfProcessedRecord);
						
					}
					  
					//Reset All values for next run
					  startRecord = null;
					  lastRecord = null;
					     
					 
			  }
				  
		        
			}
		  
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public RotsProductMaster getProductMasterByProductId(Integer productId) {
		return this.parameterCalculationDao.getProductMasterByProductId(productId);
	}

	 private Date getStartDateOfDay() {
			// today    
			   Calendar date = new GregorianCalendar();
			   // reset hour, minutes, seconds and millis
			   date.set(Calendar.HOUR_OF_DAY, 8);
			   date.set(Calendar.MINUTE, 0);
			   date.set(Calendar.SECOND, 0);
			   date.set(Calendar.MILLISECOND, 0);
			   
			   return date.getTime();
		   }
	 
	 private Date getStartOfFirstShit() {
		  Calendar date = Calendar.getInstance();
		   // reset hour, minutes, seconds and millis
		   date.set(Calendar.HOUR_OF_DAY, 8);
		   date.set(Calendar.MINUTE, 0);
		   date.set(Calendar.SECOND, 0);
		   date.set(Calendar.MILLISECOND, 0);
		   
		   return date.getTime();
	 }
	 
	 private Date getStartOfSecondShit() {
		  Calendar date = Calendar.getInstance();
		   // reset hour, minutes, seconds and millis
		   date.set(Calendar.HOUR_OF_DAY, 16);
		   date.set(Calendar.MINUTE, 0);
		   date.set(Calendar.SECOND, 0);
		   date.set(Calendar.MILLISECOND, 0);
		   
		   return date.getTime();
	 }
	 
	 private Date getStartOfThirdShit() {
		  Calendar date = Calendar.getInstance();
		   // reset hour, minutes, seconds and millis
		   date.set(Calendar.HOUR_OF_DAY, 0);
		   date.set(Calendar.MINUTE, 0);
		   date.set(Calendar.SECOND, 1);
		   date.set(Calendar.MILLISECOND, 0);
		   
		   return date.getTime();
	 }
}
