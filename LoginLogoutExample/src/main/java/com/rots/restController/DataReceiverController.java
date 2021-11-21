package com.rots.restController;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.util.Base64;
import com.rots.constants.RotsErrorType;
import com.rots.contract.ProductCountDetails;
import com.rots.contract.ResponseDto;
import com.rots.contract.RotsUserMetaInfo;
import com.rots.exception.ExceptionCode;
import com.rots.exception.RunTimeException;
import com.rots.exception.ValidationException;
import com.rots.parser.GnxDataqueue;
import com.rots.util.PropertyUtils;

@RestController
@RequestMapping(value = "/data")
public class DataReceiverController extends BaseController {

	@Autowired
	private GnxDataqueue gnxDataqueue;

	 @Autowired
	    private ApplicationContext context;


	private static final Logger log = Logger.getLogger(DataReceiverController.class);

	/*
	 * @RequestMapping(value = "/addEvents", method = RequestMethod.POST)
	 * public @ResponseBody ResponseDto addEvents(@RequestBody EventDtlsDto
	 * eventDetailsDto) {
	 * 
	 * ObjectMapper mapper = new ObjectMapper(); ResponseDto dto = new
	 * ResponseDto();
	 * 
	 * try {
	 * dto.setResponse(mapper.writeValueAsString(this.dashboardService.addEvent(
	 * eventDetailsDto))); dto.setStatus(true); }catch(Exception e){
	 * dto.setStatus(false); log.error("some Unknown exception occurs.");
	 * dto.setResponse(PropertyUtils.getPrpertyFromContext(RlmsErrorType.
	 * UNNKOWN_EXCEPTION_OCCHURS.getMessage())); }
	 * 
	 * return dto; }
	 */

	@RequestMapping(value = "/saveMachineData", method = RequestMethod.POST, produces = "application/json")
	public ResponseDto saveMachineData(@RequestBody String data) {
		// 17
		ResponseDto dto = new ResponseDto();

		try {
			this.gnxDataqueue.addDatatoQueue(data);
			// this.gnxDataqueue.start();

			if (!this.gnxDataqueue.isAlive()) {
				System.out.println("Not alive. Starting thread --------------------------------------------");
				if(this.gnxDataqueue.getState() == Thread.State.NEW) {
					this.gnxDataqueue.start();
				}else {
					this.gnxDataqueue = context.getBean(GnxDataqueue.class);
					this.gnxDataqueue.addDatatoQueue(data);
					this.gnxDataqueue.start();
				}
					
				System.out.println("Thread started --------------------------------------------------------");
			}

		} catch (Exception e) {
			dto.setStatus(false);
			dto.setResponse(PropertyUtils.getPrpertyFromContext(RotsErrorType.UNNKOWN_EXCEPTION_OCCHURS.getMessage()));
			e.printStackTrace();
		}

		return dto;

	}

	
	/*
	 * @RequestMapping("/loginIntoApp") public LoginDtlsDto
	 * loginIntoApp(@RequestBody LoginDtlsDto loginDtlsDto, HttpServletRequest
	 * request, HttpServletResponse response) {
	 * 
	 * UsernamePasswordAuthenticationToken token = new
	 * UsernamePasswordAuthenticationToken(loginDtlsDto.getUserName(),
	 * loginDtlsDto.getPassword());
	 * 
	 * LoginDtlsDto dto = new LoginDtlsDto(); RotsUserMetaInfo rotsUserMetaInfo =
	 * null; // generate session if one doesn't exist request.getSession();
	 * 
	 * token.setDetails(new WebAuthenticationDetails(request)); try{ Authentication
	 * auth = authenticationManager.authenticate(token);
	 * 
	 * SecurityContextHolder.getContext().setAuthentication(auth);
	 * 
	 * SecurityContext context = new SecurityContextImpl();
	 * context.setAuthentication(auth);
	 * 
	 * 
	 * rotsUserMetaInfo = this.getMetaInfo();
	 * dto.setCompanyId(rotsUserMetaInfo.getRotsUserRoles().getRotsBranchMaster().
	 * getRotsCompanyMaster().getCompanyId());
	 * dto.setUserId(rotsUserMetaInfo.getUserId());
	 * dto.setUserRoleId(rotsUserMetaInfo.getRotsUserRoles().getUserRoleId());
	 * dto.setToken(token.toString()); } catch(Exception e){ e.printStackTrace(); }
	 * 
	 * return dto;
	 * 
	 * }
	 */

	/*
	 * @RequestMapping("/isLoggedIn") public @ResponseBody String
	 * isUserAlreadyLoggedIn() { try{ Authentication auth =
	 * SecurityContextHolder.getContext().getAuthentication();
	 * 
	 * 
	 * if (!(auth instanceof AnonymousAuthenticationToken)) { The user is logged in
	 * :) return "1"; }else{ return "0"; } }catch(Exception e) { return "0"; } }
	 */

}