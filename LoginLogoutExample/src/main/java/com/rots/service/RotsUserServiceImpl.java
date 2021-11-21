package com.rots.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rots.DAO.RotsUserDao;
import com.rots.entity.RotsUserRoles;


@Service("RotsUserService")
public class RotsUserServiceImpl implements RotsUserService{

	
	@Autowired
	private RotsUserDao rotsUserDao;
	
	
	@Transactional(propagation = Propagation.REQUIRED)
	public RotsUserRoles getUserByUserName(String userName) {
		return this.rotsUserDao.getUserByUserName(userName);
	} 
	
	@Transactional(propagation = Propagation.REQUIRED)
	public RotsUserRoles getUserRoleObjhById(Integer userRoleId) {
		return this.rotsUserDao.getUserRoleObj(userRoleId);
	}

}
