package com.rots.DAO;

import java.util.Date;

import com.rots.entity.RotsShiftMaster;

public interface CommonAPIDao {

	public RotsShiftMaster getCurrentShift(Date currentDate) ;
	public RotsShiftMaster getShiftByNumber(Integer shiftNumber);
}
