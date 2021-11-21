package com.rots.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rots.DAO.TokenDAO;

@Service("tokenService")
public class TokenServiceImpl implements TokenService {

	@Autowired
	private TokenDAO tokenDAO;
	
	@Transactional
	public void saveUserEmail(String email, int adminId) {
		tokenDAO.saveUserEmail(email, adminId);
	}

	@Transactional
	public boolean updateToken(String email, String authenticationToken, String secretKey) {
		return tokenDAO.updateToken(email, authenticationToken, secretKey);
	}
	
	@Transactional
	public boolean updateRotsToken(String username, String authenticationToken, String secretKey) {
		return tokenDAO.updateRotsToken(username, authenticationToken, secretKey);
	}

	@Transactional
	public int getTokenDetail(String email) {
		return tokenDAO.getTokenDetail(email);
	}
	
	@Transactional
	public int getRotsTokenDetail(String username) {
		return tokenDAO.getRotsTokenDetail(username);
	}
	

	@Transactional
	public int tokenAuthentication(String token, int emailId) {
		return tokenDAO.tokenAuthentication(token, emailId);
	}
	
	@Transactional
	public void saveRotsUser(String username, int userRoleId) {
		 this.tokenDAO.saveRotsUser(username, userRoleId);
	}
	
	@Transactional
	public int tokenRotsAuthentication(String token, int userRoleId) {
		return this.tokenDAO.tokenRotsAuthentication(token, userRoleId);
	}

}
