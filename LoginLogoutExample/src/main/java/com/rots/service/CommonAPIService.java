package com.rots.service;

import java.util.Date;

import com.rots.entity.RotsShiftMaster;

public interface CommonAPIService {

	public RotsShiftMaster getCurrentShift(Date currentDate);
	public RotsShiftMaster getShiftByNumber(Integer shiftNumber);
}
