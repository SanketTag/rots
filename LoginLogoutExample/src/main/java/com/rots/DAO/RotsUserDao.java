package com.rots.DAO;

import com.rots.entity.RotsUserRoles;

public interface RotsUserDao {

	public RotsUserRoles getUserByUserName(String username);
	public RotsUserRoles getUserRoleObj(Integer userroleID);
}
