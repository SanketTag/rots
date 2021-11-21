package com.rots.restController;

import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.rots.Token.GenerateToken;
import com.rots.constants.RotsErrorType;
import com.rots.contract.MachineDetails;
import com.rots.contract.PaginationDtls;
import com.rots.contract.ProductCountDetails;
import com.rots.contract.ProductDetails;
import com.rots.contract.ReasonresponseDtls;
import com.rots.contract.ResponseDto;
import com.rots.contract.ScheduledStopDtls;
import com.rots.contract.ShiftDetails;
import com.rots.contract.StoppageReasonDtls;
import com.rots.contract.StoppageResponseDtls;
import com.rots.contract.TargetDetails;
import com.rots.contract.TargetResponseDtls;
import com.rots.entity.RotsShiftMaster;
import com.rots.exception.ExceptionCode;
import com.rots.exception.RunTimeException;
import com.rots.exception.ValidationException;
import com.rots.service.RotsAdminService;
import com.rots.service.TokenService;
import com.rots.util.PropertyUtils;

@RestController
@RequestMapping("/admin")
public class AdministrationController extends BaseController{

	@Autowired
	private RotsAdminService adminService;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private Environment env;
	
	private static final Logger logger = Logger.getLogger(AdminController.class);
	
	 @RequestMapping(value = "/addNewScheduledStopReason", method = RequestMethod.POST)
	    public ResponseDto addNewScheduledStopReason(@RequestBody StoppageReasonDtls stoppageReasonDtls, @RequestHeader("Authorization") String authorizationToken) throws RunTimeException, ValidationException {
	     
		 Integer userRoleId = null;
		 ResponseDto responseDto = null;
		 String token[] = authorizationToken.split(" ");
		 
		 userRoleId = this.authenticateToken(token[1]);
		
			if (null != userRoleId) {
				
			        try{
			        	
			        	RotsErrorType rotsErrorType = this.adminService.validateReasonDtls(stoppageReasonDtls);
			          if(null == rotsErrorType) {
			        	  this.adminService.saveStoppageReasonDetails(stoppageReasonDtls, this.getMetaInfo(userRoleId));
			        	  responseDto = this.constructResponseDto(null);
			          }else {
			        	  
			        	  throw new ValidationException(env.getProperty(rotsErrorType.getMessage()), 
									HttpStatus.BAD_REQUEST);
			          }
			        }catch(Exception e){
			        	logger.error(ExceptionUtils.getFullStackTrace(e));	 
			        	 throw new ValidationException(env.getProperty(RotsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()), 
									HttpStatus.INTERNAL_SERVER_ERROR);	
			        }
			 
			}else {
				 throw new ValidationException(env.getProperty(RotsErrorType.INVALID_TOKEN.getMessage()), 
							HttpStatus.FORBIDDEN);
			}
	       return responseDto;
	    }
	 
	 @RequestMapping(value = "/getReasonDtlsById", method = RequestMethod.POST)
	 public StoppageReasonDtls getReasonDtlsById(@RequestBody StoppageReasonDtls stoppageReasonDtls, @RequestHeader("Authorization") String authorizationToken) throws RunTimeException, ValidationException {
		 Integer userRoleId = null;
			
		 String token[] = authorizationToken.split(" ");
		 StoppageReasonDtls reasonDtls = null;
		 userRoleId = this.authenticateToken(token[1]);
		
			if (null != userRoleId) {
				
				reasonDtls = this.adminService.getReasonDtlsById(stoppageReasonDtls.getReasonId());
				if(null == reasonDtls) {
					throw new ValidationException(env.getProperty(RotsErrorType.INVALID_REASON_ID.getMessage()), 
							HttpStatus.BAD_REQUEST);
				}
			}else {
				 throw new ValidationException(env.getProperty(RotsErrorType.INVALID_TOKEN.getMessage()), 
							HttpStatus.FORBIDDEN);
			}
			
			return reasonDtls;
	 }
	 
	 @RequestMapping(value = "/updateScheduledStopReason", method = RequestMethod.POST)
	    public  ResponseDto updateScheduledStopReason(@RequestBody StoppageReasonDtls stoppageReasonDtls, @RequestHeader("Authorization") String authorizationToken) throws RunTimeException, ValidationException {
	       
		 Integer userRoleId = null;
		 ResponseDto reponseDto = null;
		 String token[] = authorizationToken.split(" ");
		 
		 userRoleId = this.authenticateToken(token[1]);
		
			if (null != userRoleId) {
		        try{
		          if(this.adminService.validateReasonIdDtls(stoppageReasonDtls)) {
		        	  this.adminService.updateStoppageReasonDetails(stoppageReasonDtls, this.getMetaInfo(userRoleId));
		        	  reponseDto = this.constructResponseDto(null);   	
		          }else {
		        	  throw new ValidationException(env.getProperty(RotsErrorType.INVALID_REASON_ID.getMessage()), 
								HttpStatus.BAD_REQUEST);
		          }
		        }catch(Exception e){
		        	logger.error(ExceptionUtils.getFullStackTrace(e));	 
		        	 throw new ValidationException(env.getProperty(RotsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()), 
								HttpStatus.INTERNAL_SERVER_ERROR);	
		        }
		 
		       
			}else {
				 throw new ValidationException(env.getProperty(RotsErrorType.INVALID_TOKEN.getMessage()), 
							HttpStatus.FORBIDDEN);
			}
	       return reponseDto;
	       
	    }
	 
	 @RequestMapping(value = "/deleteScheduledStopReason", method = RequestMethod.POST)
	    public ResponseDto deleteScheduledStopReason(@RequestBody StoppageReasonDtls stoppageReasonDtls, @RequestHeader("Authorization") String authorizationToken) throws RunTimeException, ValidationException {
		 Integer userRoleId = null;
		 ResponseDto reponseDto = null;
		 String token[] = authorizationToken.split(" ");
		 
		 userRoleId = this.authenticateToken(token[1]);
		
			if (null != userRoleId) {
		        try{
		          if(this.adminService.validateReasonIdDtls(stoppageReasonDtls)) {
		        	  this.adminService.deleteStoppageReasonDetails(stoppageReasonDtls, this.getMetaInfo(userRoleId));
		        	  reponseDto = this.constructResponseDto(null);
		          }else {
		        	  throw new ValidationException(env.getProperty(RotsErrorType.INVALID_REASON_ID.getMessage()), 
								HttpStatus.BAD_REQUEST);
		          }
		        }catch(Exception e){
		        	logger.error(ExceptionUtils.getFullStackTrace(e));
		        	 throw new ValidationException(env.getProperty(RotsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()), 
								HttpStatus.INTERNAL_SERVER_ERROR);	  }
		 
			}else {
				 throw new ValidationException(env.getProperty(RotsErrorType.INVALID_TOKEN.getMessage()), 
							HttpStatus.FORBIDDEN);
			}
	       return reponseDto;
	    }
	 
	 @RequestMapping(value = "/getAllReasons", method = RequestMethod.POST)
	    public ReasonresponseDtls getAllReasons(@RequestBody PaginationDtls paginationDtls, @RequestHeader("Authorization") String authorizationToken) throws RunTimeException, ValidationException {
		 Integer userRoleId = null;
		 ReasonresponseDtls reasonresponseDtls = null;
		 String token[] = authorizationToken.split(" ");
		 List<StoppageReasonDtls> listOfAllReasonDtls = null;
		 userRoleId = this.authenticateToken(token[1]);
		
			if (null != userRoleId) {
		        try{
		        	logger.info("Method :: getAllReasons");
		        	listOfAllReasonDtls =  this.adminService.getAllReasons(paginationDtls);
		        	
		        	reasonresponseDtls = new ReasonresponseDtls();
		        	reasonresponseDtls.setPaginationDtls(paginationDtls);
		        	reasonresponseDtls.setListOFStoppageReasonDtls(listOfAllReasonDtls);
		        	
		        }catch(Exception e){
		        	logger.error(ExceptionUtils.getFullStackTrace(e));
		        	 throw new ValidationException(env.getProperty(RotsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()), 
								HttpStatus.INTERNAL_SERVER_ERROR);	
		        }
		 
		       
			}else {
				 throw new ValidationException(env.getProperty(RotsErrorType.INVALID_TOKEN.getMessage()), 
							HttpStatus.FORBIDDEN);
			}
	       return reasonresponseDtls;
	 }
	 
	 @RequestMapping(value = "/getReasonsByQuery/{query}", method = RequestMethod.POST)
	    public ReasonresponseDtls getReasonsByQuery(@PathVariable String query, @RequestHeader("Authorization") String authorizationToken) throws RunTimeException, ValidationException {
		 Integer userRoleId = null;
		 ReasonresponseDtls reasonresponseDtls = null;
		 String token[] = authorizationToken.split(" ");
		 List<StoppageReasonDtls> listOfAllReasonDtls = null;
		 userRoleId = this.authenticateToken(token[1]);
		
			if (null != userRoleId) {
		        try{
		        	logger.info("Method :: getAllReasons");
		        	listOfAllReasonDtls =  this.adminService.getReasonsByQuery(query);
		        	
		        	reasonresponseDtls = new ReasonresponseDtls();
		        	reasonresponseDtls.setSearch(listOfAllReasonDtls);
		        	
		        }catch(Exception e){
		        	logger.error(ExceptionUtils.getFullStackTrace(e));
		        	 throw new ValidationException(env.getProperty(RotsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()), 
								HttpStatus.INTERNAL_SERVER_ERROR);	
		        }
		 
		       
			}else {
				 throw new ValidationException(env.getProperty(RotsErrorType.INVALID_TOKEN.getMessage()), 
							HttpStatus.FORBIDDEN);
			}
	       return reasonresponseDtls;
	 }
	 
	//Scheduled Stoppage Methods
	 

	 @RequestMapping(value = "/addNewScheduledStoppage", method = RequestMethod.POST)
	    public ResponseDto addNewScheduledStoppage(@RequestBody ScheduledStopDtls scheduledStopDtls, @RequestHeader("Authorization") String authorizationToken) throws RunTimeException, ValidationException {
	       
		 Integer userRoleId = null;
		 ResponseDto reponseDto = null;
		 String token[] = authorizationToken.split(" ");
		 
		 userRoleId = this.authenticateToken(token[1]);
		
			if (null != userRoleId) {
				try {
			        RotsErrorType rotsErrorType = this.adminService.validateScheduledStoppageObject(scheduledStopDtls);	
			          if(null == rotsErrorType) {
			        	  this.adminService.saveScheduledStoppage(scheduledStopDtls, this.getMetaInfo(userRoleId));
			        	  reponseDto =  this.constructResponseDto(null);
			          }else {
			        	
			        	  throw new ValidationException(env.getProperty(rotsErrorType.getMessage()), 
									HttpStatus.BAD_REQUEST);
			        	  
			          }
			        }catch(Exception e){
			        	logger.error(ExceptionUtils.getFullStackTrace(e));	       	
			        	 throw new ValidationException(env.getProperty(RotsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()), 
									HttpStatus.INTERNAL_SERVER_ERROR);	
			        }
			 
			}else {
				 throw new ValidationException(env.getProperty(RotsErrorType.INVALID_TOKEN.getMessage()), 
							HttpStatus.FORBIDDEN);
			}
	       return reponseDto;
	    }
	 
	 @RequestMapping(value = "/getScheduledStoppagDtlsById", method = RequestMethod.POST)
	 public ScheduledStopDtls getScheduledStoppagDtlsById(@RequestBody ScheduledStopDtls scheduledStopDtls, @RequestHeader("Authorization") String authorizationToken) throws RunTimeException, ValidationException {
		 Integer userRoleId = null;
			
		 String token[] = authorizationToken.split(" ");
		 ScheduledStopDtls stoppDtls = null;
		 userRoleId = this.authenticateToken(token[1]);
		
			if (null != userRoleId) {
				
				stoppDtls = this.adminService.getScheduledStoppagDtlsById(scheduledStopDtls.getStoppageId());
				if(null == stoppDtls) {
					throw new ValidationException(env.getProperty(RotsErrorType.INVALID_STOPPAGE_ID.getMessage()), 
							HttpStatus.BAD_REQUEST);
				}
			}else {
				 throw new ValidationException(env.getProperty(RotsErrorType.INVALID_TOKEN.getMessage()), 
							HttpStatus.FORBIDDEN);
			}
			
			return stoppDtls;
	 }
	 
	 
	 @RequestMapping(value = "/updateScheduledStoppage", method = RequestMethod.POST)
	    public ResponseDto updateScheduledStoppage(@RequestBody ScheduledStopDtls scheduledStopDtls, @RequestHeader("Authorization") String authorizationToken) throws RunTimeException, ValidationException {
	       
		 Integer userRoleId = null;
		 ResponseDto reponseDto = null;
		 String token[] = authorizationToken.split(" ");
		 
		 userRoleId = this.authenticateToken(token[1]);
		
			if (null != userRoleId) {
				try {
		        RotsErrorType rotsErrorType = this.adminService.validateScheduledStoppageId(scheduledStopDtls);	
		          if(null == rotsErrorType) {
		        	  this.adminService.updateScheduledStoppage(scheduledStopDtls, this.getMetaInfo(userRoleId));
		        	  reponseDto =  this.constructResponseDto(null);
		          }else {
		        	   	
		        	  throw new ValidationException(env.getProperty(rotsErrorType.getMessage()), 
								HttpStatus.BAD_REQUEST);
		          }
		        }catch(Exception e){
		        	logger.error(ExceptionUtils.getFullStackTrace(e));	       	
		        	 throw new ValidationException(env.getProperty(RotsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()), 
								HttpStatus.INTERNAL_SERVER_ERROR);	
		        }
		 
		       
			}else {
				 throw new ValidationException(env.getProperty(RotsErrorType.INVALID_TOKEN.getMessage()), 
							HttpStatus.FORBIDDEN);
			}
	       return reponseDto;
	    }
	 
	 @RequestMapping(value = "/deleteScheduledStoppage", method = RequestMethod.POST)
	    public  ResponseDto deleteScheduledStoppage(@RequestBody ScheduledStopDtls scheduledStopDtls, @RequestHeader("Authorization") String authorizationToken) throws RunTimeException, ValidationException {
	       
		 Integer userRoleId = null;
		 ResponseDto reponseDto = null;
		 String token[] = authorizationToken.split(" ");
		 
		 userRoleId = this.authenticateToken(token[1]);
		
			if (null != userRoleId) {
				try {
		        RotsErrorType rotsErrorType = this.adminService.validateScheduledStoppageId(scheduledStopDtls);	
		          if(null == rotsErrorType) {
		        	  this.adminService.deleteScheduledStoppage(scheduledStopDtls, this.getMetaInfo(userRoleId));
		        	  reponseDto =  this.constructResponseDto(null);
		          }else {
		        	 	
		        	  throw new ValidationException(env.getProperty(rotsErrorType.getMessage()), 
								HttpStatus.BAD_REQUEST);
		          }
		        }catch(Exception e){
		        	logger.error(ExceptionUtils.getFullStackTrace(e));	       	
		        	 throw new ValidationException(env.getProperty(RotsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()), 
								HttpStatus.INTERNAL_SERVER_ERROR);	
		        }
		 
		       
			}else {
				 throw new ValidationException(env.getProperty(RotsErrorType.INVALID_TOKEN.getMessage()), 
							HttpStatus.FORBIDDEN);
			}
	       return reponseDto;
	    }
	 
	 @RequestMapping(value = "/getAllScheduledStoppages", method = RequestMethod.POST)
	    public  StoppageResponseDtls getAllScheduledStoppages(@RequestBody PaginationDtls paginationDtls, @RequestHeader("Authorization") String authorizationToken) throws RunTimeException, ValidationException {
		 
		 Integer userRoleId = null;
		 StoppageResponseDtls stoppageResponseDtls;
		 String token[] = authorizationToken.split(" ");
		 List<ScheduledStopDtls> listOfAllScheduledStopDtls = null;
		 userRoleId = this.authenticateToken(token[1]);
		
			if (null != userRoleId) {
		        try{
		        	logger.info("Method :: getAllScheduledStoppages");
		        	listOfAllScheduledStopDtls =  this.adminService.getAllScheduledStoppages(paginationDtls);
		        	stoppageResponseDtls = new StoppageResponseDtls();
		        	stoppageResponseDtls.setPaginationDtls(paginationDtls);
		        	stoppageResponseDtls.setListOfStoppageDtls(listOfAllScheduledStopDtls);
		        }catch(Exception e){
		        	 throw new ValidationException(env.getProperty(RotsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()), 
								HttpStatus.INTERNAL_SERVER_ERROR);	
		        	
		        }
		 
			}else {
				 throw new ValidationException(env.getProperty(RotsErrorType.INVALID_TOKEN.getMessage()), 
							HttpStatus.FORBIDDEN);
			}
			

	        return stoppageResponseDtls;
	 }
	 
	 
	//APIs for managing Shifts and Breaks
	 
	 @RequestMapping(value = "/getAllShifts", method = RequestMethod.POST)
	 public List<ShiftDetails> getAllShifts(@RequestBody PaginationDtls paginationDtls, @RequestHeader("Authorization") String authorizationToken) throws RunTimeException, ValidationException {
	        
		 Integer userRoleId = null;
		 String token[] = authorizationToken.split(" ");
		 List<ShiftDetails> listOfShiftDetails = null;
		 userRoleId = this.authenticateToken(token[1]);
		
			if (null != userRoleId) {
		        try{
		        	logger.info("Method :: getAllShifts");
		        	listOfShiftDetails =  this.adminService.getAllShifts(paginationDtls.getPageNumber(), paginationDtls.getPageSize());
		        	
		        }catch(Exception e){
		        	logger.error(ExceptionUtils.getFullStackTrace(e));
		        	 throw new ValidationException(env.getProperty(RotsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()), 
								HttpStatus.INTERNAL_SERVER_ERROR);	
		        }
		 
		       
			}else {
				 throw new ValidationException(env.getProperty(RotsErrorType.INVALID_TOKEN.getMessage()), 
							HttpStatus.FORBIDDEN);
			}
	       return listOfShiftDetails;
	 }

	 @RequestMapping(value = "/getShiftDtlsById", method = RequestMethod.POST)
	 public ShiftDetails getShiftDtlsById(@RequestBody ScheduledStopDtls scheduledStopDtls, @RequestHeader("Authorization") String authorizationToken) throws RunTimeException, ValidationException {
		 Integer userRoleId = null;
			
		 String token[] = authorizationToken.split(" ");
		 ShiftDetails shiftDetails = null;
		 userRoleId = this.authenticateToken(token[1]);
		
			if (null != userRoleId) {
				
				shiftDetails = this.adminService.getShiftDtlsById(shiftDetails.getShiftId());
				if(null == shiftDetails) {
					throw new ValidationException(env.getProperty(RotsErrorType.INVALID_SHIFT_NUMBER.getMessage()), 
							HttpStatus.BAD_REQUEST);
				}
			}else {
				 throw new ValidationException(env.getProperty(RotsErrorType.INVALID_TOKEN.getMessage()), 
							HttpStatus.FORBIDDEN);
			}
			
			return shiftDetails;
	 }
	 
	 
	 @RequestMapping(value = "/addNewShiftDetails", method = RequestMethod.POST)
	    public ResponseDto addNewShiftDetails(@RequestBody ShiftDetails shiftDetails, @RequestHeader("Authorization") String authorizationToken) throws RunTimeException, ValidationException {
	       
		 Integer userRoleId = null;
		 ResponseDto responseDto = new ResponseDto();
		 String token[] = authorizationToken.split(" ");
		 
		 userRoleId = this.authenticateToken(token[1]);
		
			if (null != userRoleId) {
			        try{
			        RotsErrorType rotsErrorType = this.adminService.validateShiftDetails(shiftDetails);	
			          if(null == rotsErrorType) {
			        	  this.adminService.saveShiftDetails(shiftDetails, this.getMetaInfo(userRoleId));
			        	 
			        	
			          }else {
			        	  throw new ValidationException(env.getProperty(rotsErrorType.getMessage()), 
									HttpStatus.BAD_REQUEST);
			          }
			        }catch(Exception e){
			        	logger.error(ExceptionUtils.getFullStackTrace(e));	       	
			        	//throw new RunTimeException(ExceptionCode.RUNTIME_EXCEPTION.getExceptionCode(), PropertyUtils.getPrpertyFromContext(RotsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
			        	 throw new ValidationException(env.getProperty(RotsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()), 
									HttpStatus.INTERNAL_SERVER_ERROR);		
			        }
			 
			      
			}else {
				 throw new ValidationException(env.getProperty(RotsErrorType.INVALID_TOKEN.getMessage()), 
							HttpStatus.FORBIDDEN);
			}
	       return responseDto;
	    }
	 
	 @RequestMapping(value = "/updateShiftDetails", method = RequestMethod.POST)
	    public ResponseDto updateShiftDetails(@RequestBody ShiftDetails shiftDetails, @RequestHeader("Authorization") String authorizationToken) throws RunTimeException, ValidationException {
	       
		 Integer userRoleId = null;
		 ResponseDto responseDto = new ResponseDto();
		 String token[] = authorizationToken.split(" ");
		 
		 userRoleId = this.authenticateToken(token[1]);
		
			if (null != userRoleId) {
		        try{
		        RotsErrorType rotsErrorType = this.adminService.validateShiftAndBreakIDs(shiftDetails);	
		          if(null == rotsErrorType) {
		        	  this.adminService.updateShiftDetails(shiftDetails, this.getMetaInfo(userRoleId));
		          }else {
		        	  throw new ValidationException(env.getProperty(rotsErrorType.getMessage()), 
								HttpStatus.BAD_REQUEST);        	 
		          }
		        }catch(Exception e){
		        	logger.error(ExceptionUtils.getFullStackTrace(e));	       	
		        	 throw new ValidationException(env.getProperty(RotsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()), 
								HttpStatus.INTERNAL_SERVER_ERROR);	
		        }
		 
			}else {
				 throw new ValidationException(env.getProperty(RotsErrorType.INVALID_TOKEN.getMessage()), 
							HttpStatus.FORBIDDEN);
			}
	       return responseDto;
	    }
	 
	 @RequestMapping(value = "/deleteShiftDetails", method = RequestMethod.POST)
	    public ResponseDto deleteShiftDetails(@RequestBody ShiftDetails shiftDetails, @RequestHeader("Authorization") String authorizationToken) throws RunTimeException, ValidationException {
	       
		 Integer userRoleId = null;
		 ResponseDto responseDto = new ResponseDto();
		 String token[] = authorizationToken.split(" ");
		 
		 userRoleId = this.authenticateToken(token[1]);
		
			if (null != userRoleId) {
		        try{
		        RotsErrorType rotsErrorType = this.adminService.validateShiftAndBreakIDs(shiftDetails);	
		          if(null == rotsErrorType) {
		        	  this.adminService.deleteShiftDetails(shiftDetails, this.getMetaInfo(userRoleId));
		          }else {
		        	  throw new ValidationException(env.getProperty(rotsErrorType.getMessage()), 
								HttpStatus.BAD_REQUEST); 
		          }
		        }catch(Exception e){
		        	logger.error(ExceptionUtils.getFullStackTrace(e));	       	
		        	 throw new ValidationException(env.getProperty(RotsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()), 
								HttpStatus.INTERNAL_SERVER_ERROR);	
		        }
		 
	 		}else {
	 			 throw new ValidationException(env.getProperty(RotsErrorType.INVALID_TOKEN.getMessage()), 
							HttpStatus.FORBIDDEN);
			}
	       return responseDto;
	    }
	 
	 @RequestMapping(value = "/getAllMachines", method = RequestMethod.GET)
	    public List<MachineDetails> getAllMachines(@RequestHeader("Authorization") String authorizationToken) throws RunTimeException, ValidationException {
		 Integer userRoleId = null;
		
		 String token[] = authorizationToken.split(" ");
		 List<MachineDetails> listOMachineDetails = null;
		 userRoleId = this.authenticateToken(token[1]);
		
			if (null != userRoleId) {
		        try{
		        	logger.info("Method :: getAllReasons");
		        	listOMachineDetails =  this.adminService.getAllActiveMachines();
		        	
		        }catch(Exception e){
		        	logger.error(ExceptionUtils.getFullStackTrace(e));
		        	 throw new ValidationException(env.getProperty(RotsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()), 
								HttpStatus.INTERNAL_SERVER_ERROR);	
		        }
		 
		       
			}else {
	 			 throw new ValidationException(env.getProperty(RotsErrorType.INVALID_TOKEN.getMessage()), 
							HttpStatus.FORBIDDEN);
			}
	       return listOMachineDetails;
	 }
	
	 @RequestMapping(value = "/getAllProductForMachine/{machineId}", method = RequestMethod.GET)
	    public List<ProductDetails> getAllProductForMachine(@PathVariable Integer machineId, @RequestHeader("Authorization") String authorizationToken) throws RunTimeException, ValidationException {
		 Integer userRoleId = null;
		
		 String token[] = authorizationToken.split(" ");
		 List<ProductDetails> listProductDetails = null;
		 userRoleId = this.authenticateToken(token[1]);
		
			if (null != userRoleId) {
		        try{
		        	logger.info("Method :: getAllReasons");
		        	listProductDetails =  this.adminService.getAllProductForMachine(machineId);
		        	
		        }catch(Exception e){
		        	logger.error(ExceptionUtils.getFullStackTrace(e));
		        	 throw new ValidationException(env.getProperty(RotsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()), 
								HttpStatus.INTERNAL_SERVER_ERROR);	
		        }
		 
		       
			}else {
	 			 throw new ValidationException(env.getProperty(RotsErrorType.INVALID_TOKEN.getMessage()), 
							HttpStatus.FORBIDDEN);
			}
	       return listProductDetails;
	 }
	 
	 //Manage target Details
	 
	 @RequestMapping(value = "/addNewTargetDtls", method = RequestMethod.POST)
	    public ResponseDto addNewTargetDtls(@RequestBody ProductCountDetails targetDetails, @RequestHeader("Authorization") String authorizationToken) throws RunTimeException, ValidationException {
	       
		 Integer userRoleId = null;
		 ResponseDto responseDto = new ResponseDto();
		 String token[] = authorizationToken.split(" ");
		 
		 userRoleId = this.authenticateToken(token[1]);
		
			if (null != userRoleId) {
			        try{
			        RotsErrorType rotsErrorType = this.adminService.validateProductDetails(targetDetails);
			          if(null == rotsErrorType) {
			        	  this.adminService.saveTargetDetails(targetDetails, this.getMetaInfo(userRoleId));
			        	  responseDto = this.constructResponseDto(null); 	
			        	
			          }else {
			        	  throw new ValidationException(env.getProperty(rotsErrorType.getMessage()), 
									HttpStatus.BAD_REQUEST);
			          }
			        }catch(Exception e){
			        	logger.error(ExceptionUtils.getFullStackTrace(e));	       	
			        	 throw new ValidationException(env.getProperty(RotsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()), 
									HttpStatus.INTERNAL_SERVER_ERROR);		
			        }
			 
			      
			}else {
				 throw new ValidationException(env.getProperty(RotsErrorType.INVALID_TOKEN.getMessage()), 
							HttpStatus.FORBIDDEN);
			}
	       return responseDto;
	    }
	 
	 @RequestMapping(value = "/deleteTargetDtls", method = RequestMethod.POST)
	    public ResponseDto deleteTargetDtls(@RequestBody ProductCountDetails targetDetails, @RequestHeader("Authorization") String authorizationToken) throws RunTimeException, ValidationException {
	       
		 Integer userRoleId = null;
		 ResponseDto responseDto = new ResponseDto();
		 String token[] = authorizationToken.split(" ");
		 
		 userRoleId = this.authenticateToken(token[1]);
		
			if (null != userRoleId) {
			        try{
			        RotsErrorType rotsErrorType = this.adminService.validateProductDetails(targetDetails);
			          if(null == rotsErrorType) {
			        	  this.adminService.deleteTargetDetails(targetDetails, this.getMetaInfo(userRoleId));
			        	 
			        	
			          }else {
			        	  throw new ValidationException(env.getProperty(rotsErrorType.getMessage()), 
									HttpStatus.BAD_REQUEST);
			          }
			        }catch(Exception e){
			        	logger.error(ExceptionUtils.getFullStackTrace(e));	       	
			        	 throw new ValidationException(env.getProperty(RotsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()), 
									HttpStatus.INTERNAL_SERVER_ERROR);	 	
			        }
			 
			      
			}else {
				 throw new ValidationException(env.getProperty(RotsErrorType.INVALID_TOKEN.getMessage()), 
							HttpStatus.FORBIDDEN);
			}
	       return responseDto;
	    }
	 
	 
	 @RequestMapping(value = "/getAllTargetDetails", method = RequestMethod.POST)
	 public TargetResponseDtls getAllTargetDetails(@RequestBody PaginationDtls paginationDtls,  @RequestHeader("Authorization") String authorizationToken) throws RunTimeException, ValidationException {
	     TargetResponseDtls targetResponseDtls;
		 Integer userRoleId = null;
		 String token[] = authorizationToken.split(" ");
		 List<TargetDetails> listOfTargetDtls = null;
		 userRoleId = this.authenticateToken(token[1]);
		
			if (null != userRoleId) {
		        try{
		        	logger.info("Method :: getAllShifts");
		        	listOfTargetDtls =  this.adminService.getAllTargetDetails(paginationDtls);
		        	targetResponseDtls = new TargetResponseDtls();
		        	targetResponseDtls.setPaginationDtls(paginationDtls);
		        	targetResponseDtls.setListOfTargetDtls(listOfTargetDtls);
		        	
		        }catch(Exception e){
		        	logger.error(ExceptionUtils.getFullStackTrace(e));
		        	 throw new ValidationException(env.getProperty(RotsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()), 
								HttpStatus.INTERNAL_SERVER_ERROR);	
		        }
		 
			}else {
				 throw new ValidationException(env.getProperty(RotsErrorType.INVALID_TOKEN.getMessage()), 
							HttpStatus.FORBIDDEN);
			}
	       return targetResponseDtls;
	 }
	 
	
	 
      //Manage Scrap Details
	 
	 @RequestMapping(value = "/addNewScrapDtls", method = RequestMethod.POST)
	    public ResponseDto addNewScrapDtls(@RequestBody ProductCountDetails scrapDetails, @RequestHeader("Authorization") String authorizationToken) throws RunTimeException, ValidationException {
	       
		 Integer userRoleId = null;
		 ResponseDto responseDto = new ResponseDto();
		 String token[] = authorizationToken.split(" ");
		 
		 userRoleId = this.authenticateToken(token[1]);
		
			if (null != userRoleId) {
			        try{
			        RotsErrorType rotsErrorType = this.adminService.validateScrapDetails(scrapDetails);
			          if(null == rotsErrorType) {
			        	  this.adminService.saveScrapCountDetails(scrapDetails, this.getMetaInfo(userRoleId));
			        	 
			        	
			          }else {
			        	  throw new ValidationException(env.getProperty(rotsErrorType.getMessage()), 
									HttpStatus.BAD_REQUEST);
			          }
			        }catch(Exception e){
			        	logger.error(ExceptionUtils.getFullStackTrace(e));	       	
			        	 throw new ValidationException(env.getProperty(RotsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()), 
									HttpStatus.INTERNAL_SERVER_ERROR);	
			        }
			 
			      
			}else {
				 throw new ValidationException(env.getProperty(RotsErrorType.INVALID_TOKEN.getMessage()), 
							HttpStatus.FORBIDDEN);
			}
	       return responseDto;
	    }
	 
	 @RequestMapping(value = "/deleteScrapDtls", method = RequestMethod.POST)
	    public ResponseDto deleteScrapDtls(@RequestBody ProductCountDetails scrapDetails, @RequestHeader("Authorization") String authorizationToken) throws RunTimeException, ValidationException {
	       
		 Integer userRoleId = null;
		 ResponseDto responseDto = new ResponseDto();
		 String token[] = authorizationToken.split(" ");
		 
		 userRoleId = this.authenticateToken(token[1]);
		
			if (null != userRoleId) {
			        try{
			        	 RotsErrorType rotsErrorType = this.adminService.validateScrapDetails(scrapDetails);
			          if(null == rotsErrorType) {
			        	  this.adminService.deleteScrapCountDetails(scrapDetails, this.getMetaInfo(userRoleId));
			        	 
			        	
			          }else {
			        	  throw new ValidationException(env.getProperty(rotsErrorType.getMessage()), 
									HttpStatus.BAD_REQUEST);
			          }
			        }catch(Exception e){
			        	logger.error(ExceptionUtils.getFullStackTrace(e));	       	
			        	 throw new ValidationException(env.getProperty(RotsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()), 
									HttpStatus.INTERNAL_SERVER_ERROR);	
			        }
			 
			      
			}else {
				 throw new ValidationException(env.getProperty(RotsErrorType.INVALID_TOKEN.getMessage()), 
							HttpStatus.FORBIDDEN);
			}
	       return responseDto;
	    }
	 
	 
	 @RequestMapping(value = "/getAllScrapDetails", method = RequestMethod.POST)
	 public List<TargetDetails> getAllScrapDetails(@RequestParam Integer pageNumber, @RequestParam Integer pageSize,  @RequestHeader("Authorization") String authorizationToken) throws RunTimeException, ValidationException {
	        
		 Integer userRoleId = null;
		 String token[] = authorizationToken.split(" ");
		 List<TargetDetails> listOfScrapDetails = null;
		 userRoleId = this.authenticateToken(token[1]);
		
			if (null != userRoleId) {
		        try{
		        	logger.info("Method :: getAllShifts");
		        	listOfScrapDetails =  this.adminService.getAllScrapDetails(pageNumber, pageSize);
		        	
		        }catch(Exception e){
		        	logger.error(ExceptionUtils.getFullStackTrace(e));
		        	 throw new ValidationException(env.getProperty(RotsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()), 
								HttpStatus.INTERNAL_SERVER_ERROR);	
		        }
		 
			}else {
				 throw new ValidationException(env.getProperty(RotsErrorType.INVALID_TOKEN.getMessage()), 
							HttpStatus.FORBIDDEN);
			}
	       return listOfScrapDetails;
	 }
}
