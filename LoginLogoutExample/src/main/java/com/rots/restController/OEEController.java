package com.rots.restController;

import java.util.List;


import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rots.constants.RotsErrorType;
import com.rots.contract.OEEDetails;
import com.rots.contract.OEETrendsWrapper;
import com.rots.contract.ResponseDto;
import com.rots.contract.StoppageReasonDtls;
import com.rots.exception.ExceptionCode;
import com.rots.exception.RunTimeException;
import com.rots.exception.ValidationException;
import com.rots.service.OEEService;
import com.rots.util.PropertyUtils;

@RestController
@RequestMapping("/oee")
public class OEEController extends BaseController{

	@Autowired
	private OEEService oeeService;
	
	@Autowired
	private Environment env;
	
	private static final Logger logger = Logger.getLogger(OEEController.class);
	
	 @RequestMapping(value = "/getCurrentOee", method = RequestMethod.POST)
	    public OEETrendsWrapper getCurrentMonthOee(@RequestBody Integer oeeType, @RequestHeader("Authorization") String authorizationToken) throws RunTimeException, ValidationException {
		 Integer userRoleId = null;
		 String token[] = authorizationToken.split(" ");
		 OEETrendsWrapper  oeeTrendsWrapper = null;
		 userRoleId = this.authenticateToken(token[1]);
		
			if (null != userRoleId) {
				
			        try{
			         
			        	oeeTrendsWrapper =  this.oeeService.getCurrentOEE(oeeType);
			         
			        }catch(Exception e){
			        	logger.error(ExceptionUtils.getFullStackTrace(e));	 
			       }
			 
			}
	       return oeeTrendsWrapper;
	 }
	 
	 @RequestMapping(value = "/calculateOEE", method = RequestMethod.POST)
	    public OEETrendsWrapper calculateOEE(@RequestBody Integer oeeType, @RequestHeader("Authorization") String authorizationToken) throws RunTimeException, ValidationException {
		 Integer userRoleId = null;
		 String token[] = authorizationToken.split(" ");
		 OEETrendsWrapper  oeeTrendsWrapper = null;
		 userRoleId = this.authenticateToken(token[1]);
		
			if (null != userRoleId) {
				
			        try{
			         
			        	this.oeeService.calculateOEE();
			         
			        }catch(Exception e){
			        	logger.error(ExceptionUtils.getFullStackTrace(e));	 
			       }
			 
			}
	       return oeeTrendsWrapper;
	 }
}
