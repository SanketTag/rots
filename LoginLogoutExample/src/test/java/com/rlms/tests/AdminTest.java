package com.rlms.tests;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.rots.DAO.OEEDao;
import com.rots.config.DemoAppConfig;
import com.rots.config.MySpringMvcDispatcherServletInitializer;
import com.rots.entity.RotsMachineActivityDetails;
import com.rots.entity.RotsProductMaster;

import org.springframework.test.context.junit4.*;
import org.springframework.test.context.web.*;


@WebAppConfiguration
@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan("com.rots")
@PropertySource(value = { "classpath:persistence-mysql.properties" })
@PropertySource(value = { "classpath:application.properties" })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DemoAppConfig.class, loader = AnnotationConfigWebContextLoader.class)
public class AdminTest {
	
//	@Autowired
	//private AdminController adminController;
	
//	@Autowired
	//private RotsUserService rotsUserService;
	
	@Autowired
	private OEEDao OEEDao;
	
	/*
	 * @Test public void addNewScheduledStopReason() throws ValidationException{
	 * 
	 * RotsUserMetaInfo metaInfo = this.getMetaInfo(); StoppageReasonDtls
	 * stoppageReasonDtls = new StoppageReasonDtls(); //
	 * stoppageReasonDtls.setReasonTitle("Routine Maintainance");
	 * stoppageReasonDtls.
	 * setReasonDesc("Maintainance scheduled every quarter. This is for March to May 2020"
	 * );
	 * 
	 * try { ResponseDto responseDto =
	 * this.adminController.addNewScheduledStopReason(stoppageReasonDtls);
	 * System.out.println(responseDto.getResponse()); } catch (RunTimeException |
	 * ValidationException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } }
	 * 
	 * @Test public void getAllReasons() { try { List<StoppageReasonDtls>
	 * listOfReasons = this.adminController.getAllReasons();
	 * System.out.println(listOfReasons); } catch (RunTimeException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } }
	 * 
	 * private RotsUserMetaInfo getMetaInfo(){ RotsUserRoles rotsUserRoles =
	 * this.rotsUserService.getUserRoleObjhById(1); RotsUserMetaInfo metaInfo = new
	 * RotsUserMetaInfo();
	 * metaInfo.setUserId(rotsUserRoles.getRotsUserMaster().getUserId());
	 * metaInfo.setUserName(rotsUserRoles.getUsername());
	 * metaInfo.setRotsUserRoles(rotsUserRoles); return metaInfo; }
	 * 
	 * @Test public void addNewScheduledStoppage() throws ValidationException{
	 * 
	 * RotsUserMetaInfo metaInfo = this.getMetaInfo(); StoppageReasonDtls
	 * stoppageReasonDtls = new StoppageReasonDtls();
	 * stoppageReasonDtls.setReasonId(1);
	 * stoppageReasonDtls.setReasonTitle("Routine Maintainance");
	 * stoppageReasonDtls.
	 * setReasonDesc("Maintainance scheduled every quarter. This is for March to May 2020"
	 * ); ScheduledStopDtls scheduledStopDtls = new ScheduledStopDtls();
	 * 
	 * try { ResponseDto responseDto =
	 * this.adminController.addNewScheduledStopReason(stoppageReasonDtls);
	 * System.out.println(responseDto.getResponse()); } catch (RunTimeException |
	 * ValidationException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } }
	 * 
	 * 
	 * 
	 * 
	 */
	
	@Test
	public void getRunningRecordsByProductId() {
		
		RotsProductMaster rotsProductMaster = new RotsProductMaster();
		rotsProductMaster.setProductId(1);
		
		 Calendar date = new GregorianCalendar();
		   // reset hour, minutes, seconds and millis
		   date.set(Calendar.HOUR_OF_DAY, 8);
		   date.set(Calendar.MINUTE, 0);
		   date.set(Calendar.SECOND, 0);
		   date.set(Calendar.MILLISECOND, 0);
		   
		   
		   
		List<RotsMachineActivityDetails> list = this.OEEDao.getRunningRecordsByMachineId(rotsProductMaster,  date.getTime());
		
		System.out.println(list.size());
	}
	
}
