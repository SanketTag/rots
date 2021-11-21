package com.rots.service;

import com.rots.entity.RotsUserRoles;

public interface RotsUserService {

	public RotsUserRoles getUserByUserName(String userName);
	public RotsUserRoles getUserRoleObjhById(Integer userRoleId);
}
