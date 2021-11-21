package com.rots.DAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rots.constants.MachineStatus;
import com.rots.constants.ROTSConstants;
import com.rots.constants.Status;
import com.rots.contract.OEEDetails;
import com.rots.entity.RotsBreakMaster;
import com.rots.entity.RotsMachineActivityDetails;
import com.rots.entity.RotsMachineDataDetails;
import com.rots.entity.RotsMachineMaster;
import com.rots.entity.RotsOeeDetails;
import com.rots.entity.RotsProductMaster;
import com.rots.entity.RotsScrapCountDetails;
import com.rots.entity.RotsShiftMaster;
import com.rots.entity.RotsTargetDetails;
import com.rots.entity.RotsUserMaster;
import com.rots.util.DateUtils;

import javax.persistence.criteria.*;
import org.hibernate.*;
import org.hibernate.query.Query;

@Repository("OEEDao")
public class OEEDaoImpl implements OEEDao{

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public List<RotsMachineActivityDetails> getRunningrecordsForMAchineForTimePeriod(RotsMachineMaster rotsMachineMaster, Date startOFDay, Date endOfDay) {
		 Session session = this.sessionFactory.getCurrentSession();
		
		 CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		 CriteriaQuery<RotsMachineActivityDetails> criteriaQuery = criteriaBuilder.createQuery(RotsMachineActivityDetails.class);
		 Root<RotsMachineActivityDetails> root = criteriaQuery.from(RotsMachineActivityDetails.class);
		
			 
		 List<Predicate> restrictions = new ArrayList<Predicate>();
		  // Add the active start/end date restrictions
		  restrictions.add(criteriaBuilder.equal(root.get("status"), Status.RUNNING.getStatusId()));
		  restrictions.add(criteriaBuilder.greaterThan(root.get("endDate").as(Date.class), startOFDay));
		  restrictions.add(criteriaBuilder.lessThan(root.get("startDate").as(Date.class), endOfDay));
		  restrictions.add(criteriaBuilder.equal(root.get("machineId"), rotsMachineMaster.getMachineId()));
		  // Add the restrictions to the criteria query
		  criteriaQuery.where(restrictions.toArray(new Predicate[restrictions.size()]));
		  
		 List<Order> orderList = new ArrayList<Order>();
		 orderList.add(criteriaBuilder.asc(root.get("startDate")));
		 criteriaQuery.orderBy(orderList);
		 
		 Query<RotsMachineActivityDetails> query = session.createQuery(criteriaQuery);

		 List<RotsMachineActivityDetails> results = query.getResultList();
		 
		 return results;

	}
	
	public List<RotsMachineActivityDetails> getActivityRecForTimePeriodByMachine(RotsMachineMaster rotsMachineMaster, Date startOFDay, Date endOfDay, Integer resetCount) {
		 Session session = this.sessionFactory.getCurrentSession();
		
		 CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		 CriteriaQuery<RotsMachineActivityDetails> criteriaQuery = criteriaBuilder.createQuery(RotsMachineActivityDetails.class);
		 Root<RotsMachineActivityDetails> root = criteriaQuery.from(RotsMachineActivityDetails.class);
		
			 
		 List<Predicate> restrictions = new ArrayList<Predicate>();
		  // Add the active start/end date restrictions
		  restrictions.add(criteriaBuilder.greaterThan(root.get("endDate").as(Date.class), startOFDay));
		  restrictions.add(criteriaBuilder.lessThan(root.get("startDate").as(Date.class), endOfDay));
		  restrictions.add(criteriaBuilder.equal(root.get("machineId"), rotsMachineMaster.getMachineId()));
		  
		  if(null != resetCount) {
		   restrictions.add(criteriaBuilder.equal(root.get("resetCount"), resetCount));
		  }
		  // Add the restrictions to the criteria query
		  criteriaQuery.where(restrictions.toArray(new Predicate[restrictions.size()]));
		  
		 List<Order> orderList = new ArrayList<Order>();
		 orderList.add(criteriaBuilder.desc(root.get("startDate")));
		 criteriaQuery.orderBy(orderList);
		 
		 Query<RotsMachineActivityDetails> query = session.createQuery(criteriaQuery);

		 List<RotsMachineActivityDetails> results = query.getResultList();
		 
		 return results;

	}
	
	public RotsOeeDetails getOEEForMachineForShiftOnDate(Integer mchineId, Integer shiftId, Date date) {
		 Session session = this.sessionFactory.getCurrentSession();
		
		 CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		 CriteriaQuery<RotsOeeDetails> criteriaQuery = criteriaBuilder.createQuery(RotsOeeDetails.class);
		 Root<RotsOeeDetails> root = criteriaQuery.from(RotsOeeDetails.class);
		
			 
		 List<Predicate> restrictions = new ArrayList<Predicate>();
		  // Add the active start/end date restrictions
		  restrictions.add(criteriaBuilder.equal(root.get("oeeDate").as(Date.class), date));
		  restrictions.add(criteriaBuilder.equal(root.get("shiftId"), shiftId));
		  restrictions.add(criteriaBuilder.equal(root.get("machineId"), mchineId));
		  
		
		  // Add the restrictions to the criteria query
		  criteriaQuery.where(restrictions.toArray(new Predicate[restrictions.size()]));
		  
		
		 Query<RotsOeeDetails> query = session.createQuery(criteriaQuery);

		 RotsOeeDetails rotsOeeDetails = query.uniqueResult();
		 
		 return rotsOeeDetails;

	}
	
	public List<RotsOeeDetails> getOEEForMachineForShiftForMonth(Integer mchineId, Integer shiftId, Date date) {
		 Session session = this.sessionFactory.getCurrentSession();
		
		 CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		 CriteriaQuery<RotsOeeDetails> criteriaQuery = criteriaBuilder.createQuery(RotsOeeDetails.class);
		 Root<RotsOeeDetails> root = criteriaQuery.from(RotsOeeDetails.class);
		
			 
		 List<Predicate> restrictions = new ArrayList<Predicate>();
		  // Add the active start/end date restrictions
		  restrictions.add(criteriaBuilder.greaterThanOrEqualTo(root.get("oeeDate").as(Date.class), date));
		  restrictions.add(criteriaBuilder.equal(root.get("shiftId"), shiftId));
		  restrictions.add(criteriaBuilder.equal(root.get("machineId"), mchineId));
		  
		
		  // Add the restrictions to the criteria query
		  criteriaQuery.where(restrictions.toArray(new Predicate[restrictions.size()]));
		  
		
		 Query<RotsOeeDetails> query = session.createQuery(criteriaQuery);

		 List<RotsOeeDetails> listOfMachineOeeForMonth = query.getResultList();
		 
		 return listOfMachineOeeForMonth;

	}
	
	public void saveRotsOeeDetails(RotsOeeDetails rotsOeeDetails) {
		this.sessionFactory.getCurrentSession().save(rotsOeeDetails);  
	}
	
	public void mergeRotsOeeDetails(RotsOeeDetails rotsOeeDetails) {
		this.sessionFactory.getCurrentSession().merge(rotsOeeDetails);  
	}
	
	public List<RotsScrapCountDetails> getScrapCountForMachineForShift(Integer mchineId, Integer shiftId, Date date) {
		 Session session = this.sessionFactory.getCurrentSession();
		
		 CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		 CriteriaQuery<RotsScrapCountDetails> criteriaQuery = criteriaBuilder.createQuery(RotsScrapCountDetails.class);
		 Root<RotsScrapCountDetails> root = criteriaQuery.from(RotsScrapCountDetails.class);
		
			 
		 List<Predicate> restrictions = new ArrayList<Predicate>();
		  // Add the active start/end date restrictions
		  restrictions.add(criteriaBuilder.equal(root.get("date").as(Date.class), date));
		  restrictions.add(criteriaBuilder.equal(root.get("shiftId"), shiftId));
		  restrictions.add(criteriaBuilder.equal(root.get("machineId"), mchineId));
		  
		
		  // Add the restrictions to the criteria query
		  criteriaQuery.where(restrictions.toArray(new Predicate[restrictions.size()]));
		  
		
		 Query<RotsScrapCountDetails> query = session.createQuery(criteriaQuery);

		 List<RotsScrapCountDetails> listOFAllScrapCounts = query.getResultList();
		 
		 return listOFAllScrapCounts;

	}
	
	public List<RotsScrapCountDetails> getScrapCountForMachineForShift(Integer mchineId, Integer shiftId, Date date, Integer productId) {
		 Session session = this.sessionFactory.getCurrentSession();
		
		 CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		 CriteriaQuery<RotsScrapCountDetails> criteriaQuery = criteriaBuilder.createQuery(RotsScrapCountDetails.class);
		 Root<RotsScrapCountDetails> root = criteriaQuery.from(RotsScrapCountDetails.class);
		
			 
		 List<Predicate> restrictions = new ArrayList<Predicate>();
		  // Add the active start/end date restrictions
		  restrictions.add(criteriaBuilder.equal(root.get("date").as(Date.class), date));
		  restrictions.add(criteriaBuilder.equal(root.get("shiftId"), shiftId));
		  restrictions.add(criteriaBuilder.equal(root.get("machineId"), mchineId));
		  restrictions.add(criteriaBuilder.equal(root.get("productId"), productId));
		  
		
		  // Add the restrictions to the criteria query
		  criteriaQuery.where(restrictions.toArray(new Predicate[restrictions.size()]));
		  
		
		 Query<RotsScrapCountDetails> query = session.createQuery(criteriaQuery);

		 List<RotsScrapCountDetails> listOFAllScrapCounts = query.getResultList();
		 
		 return listOFAllScrapCounts;

	}
	
	public List<RotsTargetDetails> getTargetCountForShiftForMachine(Integer mchineId, Integer shiftId, Date date) {
		 Session session = this.sessionFactory.getCurrentSession();
		
		 CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		 CriteriaQuery<RotsTargetDetails> criteriaQuery = criteriaBuilder.createQuery(RotsTargetDetails.class);
		 Root<RotsTargetDetails> root = criteriaQuery.from(RotsTargetDetails.class);
		
			 
		 List<Predicate> restrictions = new ArrayList<Predicate>();
		  // Add the active start/end date restrictions
		  restrictions.add(criteriaBuilder.equal(root.get("date").as(Date.class), date));
		  restrictions.add(criteriaBuilder.equal(root.get("shiftId"), shiftId));
		  restrictions.add(criteriaBuilder.equal(root.get("machineId"), mchineId));
		  
		
		  // Add the restrictions to the criteria query
		  criteriaQuery.where(restrictions.toArray(new Predicate[restrictions.size()]));
		  
		
		 Query<RotsTargetDetails> query = session.createQuery(criteriaQuery);

		 List<RotsTargetDetails> listOfTarget = query.getResultList();
		 
		 return listOfTarget;

	}
	
	public List<RotsTargetDetails> getTargetCountForShiftForMachine(Integer mchineId, Integer shiftId, Date date, Integer productId) {
		 Session session = this.sessionFactory.getCurrentSession();
		
		 CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		 CriteriaQuery<RotsTargetDetails> criteriaQuery = criteriaBuilder.createQuery(RotsTargetDetails.class);
		 Root<RotsTargetDetails> root = criteriaQuery.from(RotsTargetDetails.class);
		
			 
		 List<Predicate> restrictions = new ArrayList<Predicate>();
		  // Add the active start/end date restrictions
		  restrictions.add(criteriaBuilder.equal(root.get("date").as(Date.class), date));
		  restrictions.add(criteriaBuilder.equal(root.get("shiftId"), shiftId));
		  restrictions.add(criteriaBuilder.equal(root.get("machineId"), mchineId));
		  restrictions.add(criteriaBuilder.equal(root.get("productId"), productId));
		
		  // Add the restrictions to the criteria query
		  criteriaQuery.where(restrictions.toArray(new Predicate[restrictions.size()]));
		  
		
		 Query<RotsTargetDetails> query = session.createQuery(criteriaQuery);

		 List<RotsTargetDetails> listOfTarget = query.getResultList();
		 
		 return listOfTarget;

	}
	
    public OEEDetails getOeeDetailsForMonthForMachine(Integer machineId, Date oeeDate) {
		
    	OEEDetails oeeDetails = null;
		Session session = null;
		try
		{
			session = sessionFactory.getCurrentSession();
			String queryStr = "select AVG(oee), \r\n"
					+ "       AVG(availability), \r\n"
					+ "       AVG(productivity),\r\n"
					+ "       AVG(quality), \r\n"
					+ "	   SUM(runningTime),\r\n"
					+ "       SUM(availableTime), \r\n"
					+ "	   SUM(totalCounts),\r\n"
					+ "       SUM(scrapCount),\r\n"
					+ "       machineId,\r\n"
					+ "       updatedDate\r\n"
					+ "from RotsOeeDetails where \r\n"
					+ "\r\n"
					+ "oeeDate > :oeeDate\r\n"
					+ "and machineId = :machineId";
			
			Query query = session.createQuery(queryStr);
			query.setParameter("oeeDate", oeeDate);
			query.setParameter("machineId", machineId);
			
			List<Object[]> collection1 = query.getResultList();
			
			
			for(Object[] oee: collection1)
			 {
				oeeDetails = new OEEDetails();
				oeeDetails.setAvailability((Double)oee[1]);
				oeeDetails.setProductivity((Double)oee[2]);
				oeeDetails.setQuality((Double)oee[3]);
				oeeDetails.setOee((Double)oee[0]);
				oeeDetails.setRunningTime(((Long)oee[4]).intValue());
				oeeDetails.setAvailableTime(((Long)oee[5]).intValue());
				oeeDetails.setTotalProdCount(((Long)oee[7]).intValue());
				oeeDetails.setTotalScrapCount(((Long)oee[7]).intValue());
				oeeDetails.setMachineId((Integer)oee[8]);
				oeeDetails.setLastCalculatedTime(DateUtils.convertDateTimestampToStringWithTime((Date)oee[9]));
			}
			
			return oeeDetails;
		}
		catch(Exception exception)
		{
			System.out.println("Excecption while saving admin Details : " + exception.getMessage());
			return null;
		}
		finally
		{
			session.flush();
		}
		
	}
    
  public OEEDetails getOeeDetailsForTodayForMachine(Integer machineId, Date oeeDate) {
		
    	OEEDetails oeeDetails = null;
		Session session = null;
		try
		{
			session = sessionFactory.getCurrentSession();
			String queryStr = "select AVG(oee), \r\n"
					+ "       AVG(availability), \r\n"
					+ "       AVG(productivity),\r\n"
					+ "       AVG(quality), \r\n"
					+ "	   SUM(runningTime),\r\n"
					+ "       SUM(availableTime), \r\n"
					+ "	   SUM(totalCounts),\r\n"
					+ "       SUM(scrapCount),\r\n"
					+ "       machineId\r\n"
					+ "from RotsOeeDetails where \r\n"
					+ "\r\n"
					+ "oeeDate = :oeeDate\r\n"
					+ "and machineId = :machineId";
			
			Query query = session.createQuery(queryStr);
			query.setParameter("oeeDate", oeeDate);
			query.setParameter("machineId", machineId);
			
			List<Object[]> collection1 = query.getResultList();
			
			
			for(Object[] oee: collection1)
			 {
				oeeDetails = new OEEDetails();
				oeeDetails.setAvailability((Double)oee[1]);
				oeeDetails.setProductivity((Double)oee[2]);
				oeeDetails.setQuality((Double)oee[3]);
				oeeDetails.setOee((Double)oee[0]);
				oeeDetails.setRunningTime(((Long)oee[4]).intValue());
				oeeDetails.setAvailableTime(((Long)oee[5]).intValue());
				oeeDetails.setTotalProdCount(((Long)oee[7]).intValue());
				oeeDetails.setTotalScrapCount(((Long)oee[7]).intValue());
				oeeDetails.setMachineId((Integer)oee[8]);
			}
			
			return oeeDetails;
		}
		catch(Exception exception)
		{
			System.out.println("Excecption while saving admin Details : " + exception.getMessage());
			return null;
		}
		finally
		{
			session.flush();
		}
		
	}

  @SuppressWarnings("unchecked")
	public List<RotsOeeDetails> getAllPendingOEEDtls(Date currentDate, RotsShiftMaster currentShift){
		 Session session = this.sessionFactory.getCurrentSession();
		  
		 CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		 CriteriaQuery<RotsOeeDetails> criteriaQuery = criteriaBuilder.createQuery(RotsOeeDetails.class);
		 Root<RotsOeeDetails> root = criteriaQuery.from(RotsOeeDetails.class);
		
		 
		// List<Predicate> restrictions = new ArrayList<Predicate>();
		  // Add the active start/end date restrictions
		//  restrictions.add(criteriaBuilder.equal(root.get("isFinalFlag"), ROTSConstants.INACTIVE.getId()));
		  
		//  restrictions.add(criteriaBuilder.notEqual(root.get("oeeDate"), currentDate));
		//  restrictions.add(criteriaBuilder.notEqual(root.get("shiftId"), currentShift.getShiftId()));
		//  criteriaBuilder.and(criteriaBuilder.notEqual(root.get("oeeDate"), currentDate),
		//		               criteriaBuilder.notEqual(root.get("shiftId"), currentShift.getShiftId()));
		  
		  // Add the restrictions to the criteria query
		 
		
		  Predicate predicateForCurrentDay
		  = criteriaBuilder.and(criteriaBuilder.equal(root.get("oeeDate"), currentDate),
	               criteriaBuilder.notEqual(root.get("shiftId"), currentShift.getShiftId()),
	               criteriaBuilder.equal(root.get("isFinalFlag"), ROTSConstants.INACTIVE.getId()));
		  
		  Predicate predicateForPreviousDays
		  = criteriaBuilder.and(criteriaBuilder.lessThan(root.get("oeeDate"), currentDate),
	               criteriaBuilder.equal(root.get("isFinalFlag"), ROTSConstants.INACTIVE.getId()));
		  
		  Predicate finalPredicate
		  = criteriaBuilder
		  .or(predicateForCurrentDay, predicateForPreviousDays);
		  
		 criteriaQuery.where(finalPredicate);
		 Query<RotsOeeDetails> query = session.createQuery(criteriaQuery);
	
		 List<RotsOeeDetails> listOfPendingOEE = query.getResultList();
		 return listOfPendingOEE;
	}
  
  
  public void mergeRotsOEEDtls(RotsOeeDetails rotsOeeDetails) {
		this.sessionFactory.getCurrentSession().merge(rotsOeeDetails);  
  }
  

}
