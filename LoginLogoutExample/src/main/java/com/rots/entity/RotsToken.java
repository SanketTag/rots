package com.rots.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="rots_token")
public class RotsToken {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="token_id")
	private int tokenID;
	
	@Column(name="user_role_id" , unique=true)
	private int userRoleId;
	 
	@Column(name="authenticationToken")
	private String authenticationToken;
	
	@Column(name="secretKey")
	private String secretKey;
	
	@Column(name="username")
	private String username;
	
	public RotsToken() { }

	public RotsToken(int tokenID, int userRoleId, String authenticationToken, String secretKey, String username) {
		super();
		this.tokenID = tokenID;
		this.userRoleId = userRoleId;
		this.authenticationToken = authenticationToken;
		this.secretKey = secretKey;
		this.username = username;
	}

	public int getTokenID() {
		return tokenID;
	}

	public void setTokenID(int tokenID) {
		this.tokenID = tokenID;
	}

	public int getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(int userRoleId) {
		this.userRoleId = userRoleId;
	}

	public String getAuthenticationToken() {
		return authenticationToken;
	}

	public void setAuthenticationToken(String authenticationToken) {
		this.authenticationToken = authenticationToken;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "Token [tokenID=" + tokenID + ", userRoleId=" + userRoleId + ", authenticationToken=" + authenticationToken
				+ ", secretKey=" + secretKey + ", username=" + username + "]";
	}

	
}
