package com.rots.restController;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rots.Token.GenerateToken;
import com.rots.constants.RotsErrorType;
import com.rots.contract.ResponseDto;
import com.rots.contract.RotsUserMetaInfo;
import com.rots.entity.RotsUserRoles;
import com.rots.exception.RunTimeException;
import com.rots.exception.ValidationException;
import com.rots.service.RotsUserService;
import com.rots.service.TokenService;
import com.rots.util.PropertyUtils;

import io.jsonwebtoken.ExpiredJwtException;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", exposedHeaders = "Authorization")
public class BaseController{
	
	@Autowired 
	private TokenService tokenService;
	
	@Autowired
	private RotsUserService rotsUserService;
	
	@Autowired
	private Environment env;
	
	private RotsUserMetaInfo rotsUserMetaInfo;
	
	private static final Logger logger = Logger.getLogger(BaseController.class);
	
    public RotsUserRoles getLoggedInUser(Integer userRoleId) {
    	
    	return this.rotsUserService.getUserRoleObjhById(userRoleId);
    }
    
    public void setMetaInfo(RotsUserRoles rotsUserRoles){
    	this.rotsUserMetaInfo = new RotsUserMetaInfo();
    	this.rotsUserMetaInfo.setUserId(rotsUserRoles.getRotsUserMaster().getUserId());
    	this.rotsUserMetaInfo.setUserName(rotsUserRoles.getUsername());
    	this.rotsUserMetaInfo.setRotsUserRoles(rotsUserRoles);
    }
    
    
    public RotsUserMetaInfo getMetaInfo(Integer userRoleId){
    	this.setMetaInfo(this.getLoggedInUser(userRoleId));
    	return this.rotsUserMetaInfo;
    }
	
    public Integer authenticateToken(String token) throws RunTimeException, ValidationException {
		Integer userRoleId = null;
		Integer result = 0;
		 try {
		   userRoleId = GenerateToken.decodeJWT(token);
		   
		   if(null != userRoleId && userRoleId > 0) {
				result = tokenService.tokenRotsAuthentication(token, userRoleId);
			}  
		  
		 }catch(ExpiredJwtException e) {
			 throw new ValidationException(env.getProperty(RotsErrorType.INVALID_TOKEN.getMessage()), 
						HttpStatus.FORBIDDEN);
		 }
		 catch(Exception e) {			 
			 logger.error(e.getStackTrace()); 
			 throw new ValidationException(env.getProperty(RotsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()), 
						HttpStatus.EXPECTATION_FAILED);
			
		 }
		 
		 if (result > 0) {
			   return userRoleId;
		   }else {
			   return null;
		   }
	}
	
    public ResponseDto constructResponseDto(RotsErrorType errorType) {
		ResponseDto responseDto = new ResponseDto();
		if(null != errorType) {
			responseDto.setStatus(false);
			responseDto.setResponse(env.getProperty(errorType.getMessage()));
		}else {
			responseDto.setStatus(true);
		}
		return responseDto;
	}
}
