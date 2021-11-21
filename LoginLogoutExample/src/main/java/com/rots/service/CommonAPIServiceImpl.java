package com.rots.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rots.DAO.CommonAPIDao;
import com.rots.constants.ShiftType;
import com.rots.entity.RotsShiftMaster;

@Service("CommonAPIService")
public class CommonAPIServiceImpl implements CommonAPIService{

	@Autowired
	private CommonAPIDao commonAPIDao;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public RotsShiftMaster getCurrentShift(Date currentDate) {
		RotsShiftMaster rotsShiftMaster = this.commonAPIDao.getCurrentShift(currentDate);
		
		return rotsShiftMaster;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public RotsShiftMaster getShiftByNumber(Integer shiftNumber) {
		return this.commonAPIDao.getShiftByNumber(shiftNumber);
	}
}
