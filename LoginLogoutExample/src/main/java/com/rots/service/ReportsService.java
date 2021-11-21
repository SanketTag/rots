package com.rots.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.rots.constants.RotsErrorType;
import com.rots.contract.HourlyMachineActivityDtls;
import com.rots.contract.LiveMachineReportWrapper;
import com.rots.contract.LiveMachineStatusDetails;
import com.rots.contract.ReportInputdetails;
import com.rots.contract.ShiftwiseActivityDtls;

public interface ReportsService {

	public LiveMachineReportWrapper getListOfLiveMachineStatus() throws ParseException;
	public List<ShiftwiseActivityDtls> getHourlyMachineActivityReport(Integer machineId, Date fromDate, Date toDate) throws ParseException;
	public RotsErrorType validateActivityReportInputDetails(ReportInputdetails reportInputdetails);
}
