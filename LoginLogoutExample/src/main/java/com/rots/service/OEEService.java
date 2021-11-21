package com.rots.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.rots.contract.OEEDetails;
import com.rots.contract.OEETrendsWrapper;
import com.rots.entity.RotsMachineActivityDetails;
import com.rots.entity.RotsMachineMaster;
import com.rots.entity.RotsProductMaster;
import com.rots.entity.RotsScrapCountDetails;
import com.rots.entity.RotsShiftMaster;
import com.rots.entity.RotsTargetDetails;

public interface OEEService {
	public Long getRunningTimeInSecondsByMachineId(RotsMachineMaster rotsMachineMaster, Date date, RotsShiftMaster rotsShiftMaster, Date currentDate);
	public List<RotsMachineActivityDetails> getAllRecordsAsPerCurrentShift(RotsMachineMaster rotsMachineMaster);
	public Long getAvailableTime(RotsMachineMaster rotsMachineMaster, Date date, RotsShiftMaster rotsShiftMaster, Date currentDate);
	public void calculateOEE() throws ParseException;
	 public OEETrendsWrapper getCurrentMonthOEEForAllMachines() throws ParseException;
	 public OEETrendsWrapper getCurrentOEE(Integer oeeType) throws ParseException;
		public List<RotsScrapCountDetails> getScrapCountForMachineForShift(Integer mchineId, Integer shiftId, Date date);
		public List<RotsTargetDetails> getTargetCountForShiftForMachine(Integer mchineId, Integer shiftId, Date date);
		public List<RotsScrapCountDetails> getScrapCountForMachineForShift(Integer mchineId, Integer shiftId, Date date, Integer productId);
		public List<RotsTargetDetails> getTargetCountForShiftForMachine(Integer mchineId, Integer shiftId, Date date, Integer productId);
		public void getAllPendingShifts() throws ParseException;
}
