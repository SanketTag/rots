package com.rots.restController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rots.Token.GenerateToken;
import com.rots.contract.UserDetailsDto;
import com.rots.entity.AdminDetail;
import com.rots.entity.RotsUserMaster;
import com.rots.entity.RotsUserRoles;
import com.rots.service.AdminService;
import com.rots.service.RotsUserService;
import com.rots.service.TokenService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", exposedHeaders = "Authorization")
public class AdminController {
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private RotsUserService rotsUserService;
	
	GenerateToken generateToken = new GenerateToken();
	
	@PostMapping("/saveAdmin")
	public int saveAdminDetail(@RequestBody AdminDetail adminDetail) {
		return adminService.saveAdminDetail(adminDetail);
	}
	
	@PostMapping("/login")
	public UserDetailsDto login(@RequestBody UserDetailsDto userDetailsDto)
	{
		int status;
		HttpHeaders httpHeader = null;
	    UserDetailsDto response ;
		// Authenticate User.
		//status = adminService.adminLogin(adminDetail.getEmailId(), adminDetail.getPassword());
		status = adminService.userLogin(userDetailsDto.getUserName(), userDetailsDto.getPassword());
		/*
		 * If User is authenticated then Do Authorization Task.
		 */
		String token = "";
		if (status > 0) 
		{
			/*
			 * Generate token.
			 */
			
			RotsUserRoles rotsUserRoles = this.rotsUserService.getUserByUserName(userDetailsDto.getUserName());
			String tokenData[] = generateToken.createJWT(userDetailsDto.getUserName(), "JavaTpoint", "JWT Token",
					rotsUserRoles.getUserRoleId(), 7200000);
			//120000 43200000
			// get Token.
			 token = tokenData[0];
			
			System.out.println("Authorization :: " + token);

			// Create the Header Object
			httpHeader = new HttpHeaders();

			// Add token to the Header.
			httpHeader.add("Authorization", token);

			// Check if token is already exist.
			long isTokenExist = tokenService.getRotsTokenDetail(userDetailsDto.getUserName());
			
			/*
			 * If token exist then update Token else create and insert the token.
			 */
			if (isTokenExist > 0) 
			{
				tokenService.updateRotsToken(userDetailsDto.getUserName(), token, tokenData[1]);
			} 
			else 
			{
				tokenService.saveRotsUser(userDetailsDto.getUserName(), rotsUserRoles.getUserRoleId());
				tokenService.updateRotsToken(userDetailsDto.getUserName(), token, tokenData[1]);
			}

			 System.out.println("httpHeader - " + httpHeader);
			 
			 System.out.println("token - " + token);
			 response = new UserDetailsDto();
			 response.setToken("token " + token);
			 response.setFirstName(rotsUserRoles.getRotsUserMaster().getFirstName());
			 response.setLastName(rotsUserRoles.getRotsUserMaster().getLastName());
			 response.setUserRoleId(rotsUserRoles.getUserRoleId());
			 response.setUserName(rotsUserRoles.getUsername());
			 response.setRole(rotsUserRoles.getRole());
			 response.setEmailId(rotsUserRoles.getRotsUserMaster().getEmailId());
			 
			 
			 return response;
			//return new ResponseEntity<String>(token, httpHeader, HttpStatus.OK);
		} 
		
		// if not authenticated return  status what we get.
		else 
		{
			return null;
			//return new ResponseEntity<String>(token, httpHeader, HttpStatus.OK);
		}
		

	}
	
	
	@GetMapping("/getAdminData/{userRoleId}")
	public List<AdminDetail> getAdminData(@PathVariable int userRoleId, @RequestHeader("Authorization") String authorizationToken)
	{
		String token[] = authorizationToken.split(" ");
		int result = tokenService.tokenRotsAuthentication(token[1], userRoleId);
		
		if (result > 0) {
			return adminService.getAdminData();
		} else {
			return null;
		}
	}
	
	
	
	
}
