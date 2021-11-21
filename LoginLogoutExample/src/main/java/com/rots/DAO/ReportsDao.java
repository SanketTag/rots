package com.rots.DAO;

import java.util.Date;
import java.util.List;

import com.rots.entity.RotsMachineActivityDetails;
import com.rots.entity.RotsMachineLiveData;



public interface ReportsDao {
	public List<RotsMachineActivityDetails> getAllActivityRecordsForMachine(Integer machineId, Date fromDate, Date toDate);
	public List<RotsMachineLiveData> getLiveRecordsForAllMachines();
}
