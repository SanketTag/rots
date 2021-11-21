package com.rots.DAO;

import java.util.Date;
import java.util.List;

import com.rots.contract.OEEDetails;
import com.rots.entity.RotsMachineActivityDetails;
import com.rots.entity.RotsMachineMaster;
import com.rots.entity.RotsOeeDetails;
import com.rots.entity.RotsProductMaster;
import com.rots.entity.RotsScrapCountDetails;
import com.rots.entity.RotsShiftMaster;
import com.rots.entity.RotsTargetDetails;

public interface OEEDao {

	public List<RotsMachineActivityDetails> getRunningrecordsForMAchineForTimePeriod(RotsMachineMaster rotsMachineMaster, Date startOFDay, Date endOfDay);
	public List<RotsMachineActivityDetails> getActivityRecForTimePeriodByMachine(RotsMachineMaster rotsMachineMaster, Date startOFDay, Date endOfDay, Integer resetCount);
	public void saveRotsOeeDetails(RotsOeeDetails rotsOeeDetails);
	public RotsOeeDetails getOEEForMachineForShiftOnDate(Integer mchineId, Integer shiftId, Date date);
	public List<RotsScrapCountDetails> getScrapCountForMachineForShift(Integer mchineId, Integer shiftId, Date date);
	public void mergeRotsOeeDetails(RotsOeeDetails rotsOeeDetails);
	public List<RotsOeeDetails> getOEEForMachineForShiftForMonth(Integer mchineId, Integer shiftId, Date date);
	  public OEEDetails getOeeDetailsForMonthForMachine(Integer machineId, Date oeeDate);
	  public OEEDetails getOeeDetailsForTodayForMachine(Integer machineId, Date oeeDate);
	  public List<RotsTargetDetails> getTargetCountForShiftForMachine(Integer mchineId, Integer shiftId, Date date);
	  public List<RotsScrapCountDetails> getScrapCountForMachineForShift(Integer mchineId, Integer shiftId, Date date, Integer productId);
	  public List<RotsTargetDetails> getTargetCountForShiftForMachine(Integer mchineId, Integer shiftId, Date date, Integer productId);
	  public List<RotsOeeDetails> getAllPendingOEEDtls(Date currentDate, RotsShiftMaster currentShift);
	  public void mergeRotsOEEDtls(RotsOeeDetails rotsOeeDetails);
}
