package com.rots.DAO;

public interface TokenDAO {
	
	public void saveUserEmail(String email , int adminId);
	
	public boolean updateToken(String email , String authenticationToken , String secretKey);
	
	public int getTokenDetail(String email );

	public int tokenAuthentication(String token , int emailId);
	
	public int getRotsTokenDetail(String username);
	
	public boolean updateRotsToken(String username, String authenticationToken, String secretKey);
	
	public void saveRotsUser(String username, int userId);
	
	public int tokenRotsAuthentication(String token, int userRoleId);

}
