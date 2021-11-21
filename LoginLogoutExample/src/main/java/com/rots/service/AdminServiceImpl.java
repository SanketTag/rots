package com.rots.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rots.DAO.AdminDAO;
import com.rots.entity.AdminDetail;

@Service("adminService")
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	private AdminDAO adminDAO;

	@Transactional
	public int saveAdminDetail(AdminDetail adminDetail) {
		return adminDAO.saveAdminDetail(adminDetail);
	}
	
	@Transactional
	public int adminLogin(String emailId, String password) {
		return adminDAO.adminLogin(emailId, password);
	}
	
	@Transactional
	public int userLogin(String emailId, String password) {
		return adminDAO.userLogin(emailId, password);
	}


	@Transactional
	public List<AdminDetail> getAdminData() {
		return adminDAO.getAdminData();
	}
	
	

}
