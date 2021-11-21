package com.rots.restController;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rots.constants.RotsErrorType;
import com.rots.contract.LiveMachineReportWrapper;
import com.rots.contract.LiveMachineStatusDetails;
import com.rots.contract.OEEDetails;
import com.rots.contract.ReportInputdetails;
import com.rots.contract.ShiftwiseActivityDtls;
import com.rots.exception.RunTimeException;
import com.rots.exception.ValidationException;
import com.rots.service.ReportsService;
import com.rots.util.DateUtils;

@RestController
@RequestMapping("/report")
public class ReportController extends BaseController{

	
	@Autowired
	private ReportsService reportsService;
	
	@Autowired
	private Environment env;
	
	private static final Logger logger = Logger.getLogger(ReportController.class);
	
	 @RequestMapping(value = "/getMachineHourlyActivityReport", method = RequestMethod.POST)
	    public List<ShiftwiseActivityDtls> getMachineHourlyActivityReport(@RequestBody ReportInputdetails reportInputdetails, @RequestHeader("Authorization") String authorizationToken) throws RunTimeException, ValidationException {
		 Integer userRoleId = null;
		 String token[] = authorizationToken.split(" ");
		 List<ShiftwiseActivityDtls>  listOfActivities = null;
		 userRoleId = this.authenticateToken(token[1]);
		
			if (null != userRoleId) {
				
			        try{
			            RotsErrorType rotsErrorType = this.reportsService.validateActivityReportInputDetails(reportInputdetails);
			            
			            if(null == rotsErrorType) {
			            
			            	Date fromDate = DateUtils.convertStringToDateWithTime(reportInputdetails.getFromDate());
			            	Date toDate = DateUtils.convertStringToDateWithTime(reportInputdetails.getToDate());
			            	
			            	listOfActivities =  this.reportsService.getHourlyMachineActivityReport(reportInputdetails.getMachineId(),
			            			fromDate,
			            			toDate);
			            }
			        }catch(Exception e){
			        	logger.error(ExceptionUtils.getFullStackTrace(e));	 
			       }
			 
			}else {
				 throw new ValidationException(env.getProperty(RotsErrorType.INVALID_TOKEN.getMessage()), 
							HttpStatus.FORBIDDEN);
			}
	       return listOfActivities;
	 }
	 
	 @RequestMapping(value = "/getLiveMachineStatusReport", method = RequestMethod.GET)
	    public LiveMachineReportWrapper getLiveMachineStatusReport() throws RunTimeException, ValidationException {
			 LiveMachineReportWrapper  liveMachineReportWrapper = null;
		
			
			        try{
			        	
			        	liveMachineReportWrapper =  this.reportsService.getListOfLiveMachineStatus();
			            
			        }catch(Exception e){
			        	logger.error(ExceptionUtils.getFullStackTrace(e));	       	
			        	 throw new ValidationException(env.getProperty(RotsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()), 
									HttpStatus.INTERNAL_SERVER_ERROR);
			       }
			 
			
	       return liveMachineReportWrapper;
	 }
	 
	 
	 
}
