package com.rlms.tests;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.ValidationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.rots.config.DemoAppConfig;
import com.rots.entity.RotsMachineMaster;
import com.rots.entity.RotsProductMaster;
import com.rots.entity.RotsShiftMaster;
import com.rots.service.CommonAPIService;
import com.rots.service.OEEService;
import com.rots.service.ParameterCalculationService;
import com.rots.service.RotsAdminService;
import com.rots.util.DateUtils;

@WebAppConfiguration
@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan("com.rots")
@PropertySource(value = { "classpath:persistence-mysql.properties" })
@PropertySource(value = { "classpath:application.properties" })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DemoAppConfig.class, loader = AnnotationConfigWebContextLoader.class)
public class OEETest {
	
	 @Autowired
	 private OEEService oEEService;
	 
	 @Autowired
	 private RotsAdminService adminService;
	 
	 @Autowired
	 private ParameterCalculationService parameterCalculationService;
	 
	 @Autowired
	 private CommonAPIService commonAPIService;

	 @Test
	    public void getDistinctProductIds() throws ValidationException{
	    	List<RotsProductMaster> listOfProductIds = this.parameterCalculationService.getDistinctProductIds();
	    	
	    	for (RotsProductMaster productId : listOfProductIds) {
				System.out.println(" Product ID - " + productId.getProductId());
			}
	    }
	    
	    @Test
	    public void processRecordsByProductId() throws ValidationException{    	
	    	
				this.parameterCalculationService.processRecordsTest();
			
	    }
	    
	    @Test
	    public void getRunningTimeByPRodId() throws ValidationException{
	    	RotsMachineMaster rotsMachineMaster = this.adminService.getMachineById(1);
	    	
	    	Calendar cal = Calendar.getInstance();
	    	cal.setTime(new Date());
	    	cal.add(Calendar.DATE, -1);
	    	Date dateBefore4Days = cal.getTime();
	    	RotsShiftMaster rotsShiftMaster = this.commonAPIService.getShiftByNumber(3);
	    	if(null != rotsMachineMaster) {
	    		Long seconds = this.oEEService.getRunningTimeInSecondsByMachineId(rotsMachineMaster, dateBefore4Days, rotsShiftMaster, new Date());
	    		String  runningTime = DateUtils.convertTimeIntoDaysHrMin(seconds, TimeUnit.SECONDS);
	    		System.out.println("Running Time - " + runningTime);
	    	}
	    }
	    
	    @Test
	    public void insertData() throws ParseException {
	    	
	    	List<String> data = Arrays.asList("1,1,6,1,10,0,11,4,121120,180334".split(","));
	    	this.adminService.insertDataIntoDB(data);
	    }
	    
	    @Test
	    public void calculatePendnigOEE() throws ParseException {
	    	this.oEEService.getAllPendingShifts();
	    }
}
