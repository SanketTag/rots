package com.rots.DAO;

import java.util.List;

import com.rots.entity.AdminDetail;

public interface AdminDAO {

	public int saveAdminDetail(AdminDetail adminDetail);
	
	public int adminLogin(String emailId , String password);
	
	public List<AdminDetail> getAdminData();
	
	public int userLogin(String username, String password);
}
