package com.rots.DAO;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rots.constants.MachineStatus;
import com.rots.constants.ROTSConstants;
import com.rots.contract.PaginationDtls;
import com.rots.contract.ReasonresponseDtls;
import com.rots.entity.RotsBreakMaster;
import com.rots.entity.RotsMachineActivityDetails;
import com.rots.entity.RotsMachineDataDetails;
import com.rots.entity.RotsMachineDataTest;
import com.rots.entity.RotsMachineLiveData;
import com.rots.entity.RotsMachineMaster;
import com.rots.entity.RotsOeeDetails;
import com.rots.entity.RotsOperatorMaster;
import com.rots.entity.RotsProductMaster;
import com.rots.entity.RotsReasonMaster;
import com.rots.entity.RotsScheduledStopTranDtls;
import com.rots.entity.RotsScrapCountDetails;
import com.rots.entity.RotsShiftMaster;
import com.rots.entity.RotsTargetDetails;
import com.rots.entity.RotsUserMaster;
import com.rots.entity.RotsUserRoles;
import com.rots.restController.AdminController;



@Repository("AdminDao")
public class RotsAdminDaoImpl implements RotsAdminDao{

	@Autowired
	private SessionFactory sessionFactory;
	
	private static final Logger logger = Logger.getLogger(RotsAdminDaoImpl.class);
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public void saveRotsReasonMaster(RotsReasonMaster rotsReasonMaster) {
		this.sessionFactory.getCurrentSession().save(rotsReasonMaster);  
	}
	
	public void mergeRotsReasonMaster(RotsReasonMaster rotsReasonMaster) {
		this.sessionFactory.getCurrentSession().merge(rotsReasonMaster);  
	}
	
	
	
	@SuppressWarnings("unchecked")
	public  List<RotsReasonMaster> getAllReasons(PaginationDtls paginationDtls){
		 Session session = this.sessionFactory.getCurrentSession();
		 ReasonresponseDtls reasonresponseDtls = new ReasonresponseDtls();
		 CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		 CriteriaQuery<RotsReasonMaster> criteriaQuery = criteriaBuilder.createQuery(RotsReasonMaster.class);
		 Root<RotsReasonMaster> root = criteriaQuery.from(RotsReasonMaster.class);
		
		
		 List<Predicate> restrictions = new ArrayList<Predicate>();
		  // Add the active start/end date restrictions
		
		  restrictions.add(criteriaBuilder.equal(root.get("activeFlag"), ROTSConstants.ACTIVE.getId()));
		 
		  // Add the restrictions to the criteria query
		  criteriaQuery.where(restrictions.toArray(new Predicate[restrictions.size()]));
		  
		
		 List<Order> orderList = new ArrayList<Order>();
		 orderList.add(criteriaBuilder.desc(root.get("createdDate")));
		 criteriaQuery.orderBy(orderList);		 
		 
		
				 
		 Query<RotsReasonMaster> query = session.createQuery(criteriaQuery);
		 
		// total rows count
		 Integer totalElements = query.getResultList().size();
		 paginationDtls.setTotalElements(totalElements);
		 
		 query.setFirstResult((paginationDtls.getPageNumber()-1) * paginationDtls.getPageSize()); 
		 query.setMaxResults(paginationDtls.getPageSize());
		 
		 List<RotsReasonMaster> listOfAllReasons =  query.getResultList();		 
		 
		 return listOfAllReasons;
	}
	
	@SuppressWarnings("unchecked")
	public  List<RotsReasonMaster> getReasonsByQuery(String queryStr){
		 Session session = this.sessionFactory.getCurrentSession();
		 ReasonresponseDtls reasonresponseDtls = new ReasonresponseDtls();
		 CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		 CriteriaQuery<RotsReasonMaster> criteriaQuery = criteriaBuilder.createQuery(RotsReasonMaster.class);
		 Root<RotsReasonMaster> root = criteriaQuery.from(RotsReasonMaster.class);
		
		
		 List<Predicate> restrictions = new ArrayList<Predicate>();
		  // Add the active start/end date restrictions
		
		  restrictions.add(criteriaBuilder.equal(root.get("activeFlag"), ROTSConstants.ACTIVE.getId()));
		  restrictions.add(criteriaBuilder.like(root.get("reasonTitle"), "%" + queryStr + "%"));
		 
		  // Add the restrictions to the criteria query
		  criteriaQuery.where(restrictions.toArray(new Predicate[restrictions.size()]));
		  
		
		 List<Order> orderList = new ArrayList<Order>();
		 orderList.add(criteriaBuilder.asc(root.get("reasonTitle")));
		 criteriaQuery.orderBy(orderList);		 
		 
		
				 
		 Query<RotsReasonMaster> query = session.createQuery(criteriaQuery);
		 
			 
		 List<RotsReasonMaster> listOfAllReasons =  query.getResultList();		 
		 
		 return listOfAllReasons;
	}
	
	public void saveRotsScheduledStopTranDtls(RotsScheduledStopTranDtls rotsScheduledStopTranDtls) {
		this.sessionFactory.getCurrentSession().save(rotsScheduledStopTranDtls);  
	}
	
	public void mergeRotsScheduledStopTranDtls(RotsScheduledStopTranDtls rotsScheduledStopTranDtls) {
		this.sessionFactory.getCurrentSession().merge(rotsScheduledStopTranDtls);  
	}
	
	
	public void saveOrUpdateRotsReasonMaster(RotsScheduledStopTranDtls rotsScheduledStopTranDtls) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(rotsScheduledStopTranDtls);  
	}
	
	@SuppressWarnings("unchecked")
	public List<RotsScheduledStopTranDtls> getAllScheduledStops(PaginationDtls paginationDtls){
		 Session session = this.sessionFactory.getCurrentSession();
		 CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		 CriteriaQuery<RotsScheduledStopTranDtls> criteriaQuery = criteriaBuilder.createQuery(RotsScheduledStopTranDtls.class);
		 Root<RotsScheduledStopTranDtls> root = criteriaQuery.from(RotsScheduledStopTranDtls.class);
		
		 List<Predicate> restrictions = new ArrayList<Predicate>();
		  // Add the active start/end date restrictions
		
		  restrictions.add(criteriaBuilder.equal(root.get("activeFlag"), ROTSConstants.ACTIVE.getId()));
		 
		  // Add the restrictions to the criteria query
		  criteriaQuery.where(restrictions.toArray(new Predicate[restrictions.size()]));
		  
		
		 List<Order> orderList = new ArrayList<Order>();
		 orderList.add(criteriaBuilder.desc(root.get("createdDate")));
		 criteriaQuery.orderBy(orderList);		 
		 
		 Query<RotsScheduledStopTranDtls> query = session.createQuery(criteriaQuery);
		 
			// total rows count
		 Integer totalElements = query.getResultList().size();
		 paginationDtls.setTotalElements(totalElements);
		 
		 query.setFirstResult((paginationDtls.getPageNumber()-1) * paginationDtls.getPageSize()); 
		 query.setMaxResults(paginationDtls.getPageSize());

		 List<RotsScheduledStopTranDtls> listOfAllScheduledStops =  query.getResultList();
		 return listOfAllScheduledStops;
		 
	}
	
	public RotsUserMaster getUserByUserId(Integer userId) {
		 Session session = this.sessionFactory.getCurrentSession();
			
		 
		 CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		 CriteriaQuery<RotsUserMaster> criteriaQuery = criteriaBuilder.createQuery(RotsUserMaster.class);
		 Root<RotsUserMaster> root = criteriaQuery.from(RotsUserMaster.class);
		
		// root.fetch(RotsMachineActivityDetails.class, JoinType.LEFT);
		 
		 
		 List<Predicate> restrictions = new ArrayList<Predicate>();
		  // Add the active start/end date restrictions
		  restrictions.add(criteriaBuilder.equal(root.get("userId"), userId));
		  restrictions.add(criteriaBuilder.equal(root.get("activeFlag"), ROTSConstants.ACTIVE.getId()));
		
		  // Add the restrictions to the criteria query
		  criteriaQuery.where(restrictions.toArray(new Predicate[restrictions.size()]));
		  
		  
		 
		 Query<RotsUserMaster> query = session.createQuery(criteriaQuery);

		 RotsUserMaster rotsUserMaster = (RotsUserMaster) query.uniqueResult();
		 return rotsUserMaster;
		 
	}
	
	
	public RotsScheduledStopTranDtls getRotsScheduledStopTranDtlsByIdd(Integer stoppageId) {
		 Session session = this.sessionFactory.getCurrentSession();
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		 CriteriaQuery<RotsScheduledStopTranDtls> criteriaQuery = criteriaBuilder.createQuery(RotsScheduledStopTranDtls.class);
		 Root<RotsScheduledStopTranDtls> root = criteriaQuery.from(RotsScheduledStopTranDtls.class);
		
		// root.fetch(RotsMachineActivityDetails.class, JoinType.LEFT);
		 
		 List<Predicate> restrictions = new ArrayList<Predicate>();
		  // Add the active start/end date restrictions
		  restrictions.add(criteriaBuilder.equal(root.get("id"), stoppageId));
		  restrictions.add(criteriaBuilder.equal(root.get("activeFlag"), ROTSConstants.ACTIVE.getId()));
		
		  // Add the restrictions to the criteria query
		  criteriaQuery.where(restrictions.toArray(new Predicate[restrictions.size()]));
		                               
		 
		 Query<RotsScheduledStopTranDtls> query = session.createQuery(criteriaQuery);

		 RotsScheduledStopTranDtls rotsScheduledStopTranDtls = (RotsScheduledStopTranDtls) query.uniqueResult();
		 return rotsScheduledStopTranDtls;
	}
	
	@SuppressWarnings("unchecked")
	public List<RotsScheduledStopTranDtls> getAllActiveScheduledStops(Integer machineId, Date startDate, Date endDate){
		 Session session = this.sessionFactory.getCurrentSession();
		
		 
		 CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		 CriteriaQuery<RotsScheduledStopTranDtls> criteriaQuery = criteriaBuilder.createQuery(RotsScheduledStopTranDtls.class);
		 Root<RotsScheduledStopTranDtls> root = criteriaQuery.from(RotsScheduledStopTranDtls.class);
	 List<Predicate> restrictions = new ArrayList<Predicate>();
		  // Add the active start/end date restrictions
		  restrictions.add(criteriaBuilder.isNull(root.get("rotsMachineMaster").get("machineId")));
		  restrictions.add(criteriaBuilder.equal(root.get("rotsMachineMaster").get("machineId"), machineId));
		  restrictions.add(criteriaBuilder.greaterThan(root.get("endDate").as(Date.class), startDate));
		  restrictions.add(criteriaBuilder.lessThan(root.get("startDate").as(Date.class), endDate));
		  restrictions.add(criteriaBuilder.equal(root.get("activeFlag"), ROTSConstants.ACTIVE.getId()));
		  // Add the restrictions to the criteria query
		  criteriaQuery.where(restrictions.toArray(new Predicate[restrictions.size()]));
		  
		 Query<RotsScheduledStopTranDtls> query = session.createQuery(criteriaQuery);

		 List<RotsScheduledStopTranDtls> listOfAllScheduledStops  =  query.getResultList();
		 return listOfAllScheduledStops;
	}
	
	@SuppressWarnings("unchecked")
	public List<RotsBreakMaster> getAllbreaksBeforeTime(Date time, Integer shiftId){
		 Session session = this.sessionFactory.getCurrentSession();
	 
		 CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		 CriteriaQuery<RotsBreakMaster> criteriaQuery = criteriaBuilder.createQuery(RotsBreakMaster.class);
		 Root<RotsBreakMaster> root = criteriaQuery.from(RotsBreakMaster.class);
	    
		 List<Predicate> restrictions = new ArrayList<Predicate>();
		  // Add the active start/end date restrictions
		
		  restrictions.add(criteriaBuilder.lessThan(root.get("startTime").as(Date.class), time));
		  restrictions.add(criteriaBuilder.equal(root.get("rotsShiftMaster").get("shiftId"), shiftId));
		  restrictions.add(criteriaBuilder.equal(root.get("activeFlag"), ROTSConstants.ACTIVE.getId()));
		  // Add the restrictions to the criteria query
		  criteriaQuery.where(restrictions.toArray(new Predicate[restrictions.size()]));
		  
		  
		 List<Order> orderList = new ArrayList<Order>();
		 orderList.add(criteriaBuilder.desc(root.get("startTime")));
		 criteriaQuery.orderBy(orderList);
		 
		 Query<RotsBreakMaster> query = session.createQuery(criteriaQuery);

		 List<RotsBreakMaster> listOfAllBreaks = query.getResultList();
		 return listOfAllBreaks;
	}
	
	@SuppressWarnings("unchecked")
	public List<RotsBreakMaster> getBreakWithinTime(Date fromTime, Date toTime, Integer shiftId){
		 Session session = this.sessionFactory.getCurrentSession();
	 
		 CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		 CriteriaQuery<RotsBreakMaster> criteriaQuery = criteriaBuilder.createQuery(RotsBreakMaster.class);
		 Root<RotsBreakMaster> root = criteriaQuery.from(RotsBreakMaster.class);
	    
		 List<Predicate> restrictions = new ArrayList<Predicate>();
		  // Add the active start/end date restrictions
		
		  restrictions.add(criteriaBuilder.lessThan(root.get("startTime").as(Date.class), toTime));
		  restrictions.add(criteriaBuilder.greaterThan(root.get("endTime").as(Date.class), fromTime));
		  restrictions.add(criteriaBuilder.equal(root.get("rotsShiftMaster").get("shiftId"), shiftId));
		  restrictions.add(criteriaBuilder.equal(root.get("activeFlag"), ROTSConstants.ACTIVE.getId()));
		  // Add the restrictions to the criteria query
		  criteriaQuery.where(restrictions.toArray(new Predicate[restrictions.size()]));
		  
		  
		 List<Order> orderList = new ArrayList<Order>();
		 orderList.add(criteriaBuilder.desc(root.get("startTime")));
		 criteriaQuery.orderBy(orderList);
		 
		 Query<RotsBreakMaster> query = session.createQuery(criteriaQuery);

		 List<RotsBreakMaster> listOfAllBreaks = query.getResultList();
		 return listOfAllBreaks;
	}
	
	@SuppressWarnings("unchecked")
	public RotsOperatorMaster getOperatorById(Integer operatorId){
		 Session session = this.sessionFactory.getCurrentSession();
		
		 CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		 CriteriaQuery<RotsOperatorMaster> criteriaQuery = criteriaBuilder.createQuery(RotsOperatorMaster.class);
		 Root<RotsOperatorMaster> root = criteriaQuery.from(RotsOperatorMaster.class);
		
		// root.fetch(RotsMachineActivityDetails.class, JoinType.LEFT);
		 
		 criteriaQuery.where(criteriaBuilder.equal(root.get("id"), operatorId));
	
		 Query<RotsOperatorMaster> query = session.createQuery(criteriaQuery);

		 RotsOperatorMaster operatorMaster = (RotsOperatorMaster) query.uniqueResult();
		 return operatorMaster;
		 
	}
	
	@SuppressWarnings("unchecked")
	public RotsMachineMaster getMachineById(Integer machineId){
		 Session session = this.sessionFactory.getCurrentSession();
			 
		 CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		 CriteriaQuery<RotsMachineMaster> criteriaQuery = criteriaBuilder.createQuery(RotsMachineMaster.class);
		 Root<RotsMachineMaster> root = criteriaQuery.from(RotsMachineMaster.class);
		
		// root.fetch(RotsMachineActivityDetails.class, JoinType.LEFT);
		 
		 criteriaQuery.where(criteriaBuilder.equal(root.get("machineId"), machineId));
	
		 Query<RotsMachineMaster> query = session.createQuery(criteriaQuery);

		 RotsMachineMaster rotsMachineMaster  = (RotsMachineMaster) query.uniqueResult();
		 return rotsMachineMaster;
	}
	
	@SuppressWarnings("unchecked")
	public RotsReasonMaster getReasonById(Integer reasonId){
		 Session session = this.sessionFactory.getCurrentSession();
	
		 CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		 CriteriaQuery<RotsReasonMaster> criteriaQuery = criteriaBuilder.createQuery(RotsReasonMaster.class);
		 Root<RotsReasonMaster> root = criteriaQuery.from(RotsReasonMaster.class);
		
		// root.fetch(RotsMachineActivityDetails.class, JoinType.LEFT);
		 List<Predicate> restrictions = new ArrayList<Predicate>();
		  // Add the active start/end date restrictions
		  restrictions.add(criteriaBuilder.equal(root.get("id"), reasonId));
		  restrictions.add(criteriaBuilder.equal(root.get("activeFlag"), ROTSConstants.ACTIVE.getId()));
		  
		  // Add the restrictions to the criteria query
		  criteriaQuery.where(restrictions.toArray(new Predicate[restrictions.size()]));
		  
		  
		 Query<RotsReasonMaster> query = session.createQuery(criteriaQuery);

		 RotsReasonMaster rotsReasonMaster  = (RotsReasonMaster) query.uniqueResult();
		 return rotsReasonMaster;
	}
	
	@SuppressWarnings("unchecked")
	public RotsReasonMaster getReasonByTitle(String reasonTitle){
		 Session session = this.sessionFactory.getCurrentSession();
	
		 CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		 CriteriaQuery<RotsReasonMaster> criteriaQuery = criteriaBuilder.createQuery(RotsReasonMaster.class);
		 Root<RotsReasonMaster> root = criteriaQuery.from(RotsReasonMaster.class);
		
		// root.fetch(RotsMachineActivityDetails.class, JoinType.LEFT);
		 List<Predicate> restrictions = new ArrayList<Predicate>();
		  // Add the active start/end date restrictions
		  restrictions.add(criteriaBuilder.equal(root.get("reasonTitle"), reasonTitle));
		  restrictions.add(criteriaBuilder.equal(root.get("activeFlag"), ROTSConstants.ACTIVE.getId()));
		  
		  // Add the restrictions to the criteria query
		  criteriaQuery.where(restrictions.toArray(new Predicate[restrictions.size()]));
		  
		  
		 Query<RotsReasonMaster> query = session.createQuery(criteriaQuery);

		 RotsReasonMaster rotsReasonMaster  = (RotsReasonMaster) query.uniqueResult();
		 return rotsReasonMaster;
	}
	
	
	@SuppressWarnings("unchecked")
	public RotsProductMaster getProductById(Integer productId){
		 Session session = this.sessionFactory.getCurrentSession();
		 CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		 CriteriaQuery<RotsProductMaster> criteriaQuery = criteriaBuilder.createQuery(RotsProductMaster.class);
		 Root<RotsProductMaster> root = criteriaQuery.from(RotsProductMaster.class);
		
		// root.fetch(RotsMachineActivityDetails.class, JoinType.LEFT);
		 
		 criteriaQuery.where(criteriaBuilder.equal(root.get("productId"), productId));
		 
		 Query<RotsProductMaster> query = session.createQuery(criteriaQuery);

		 RotsProductMaster rotsProductMaster  = (RotsProductMaster) query.uniqueResult();
		 return rotsProductMaster;
	}
	
	public void saveRotsMachineDataDetails(RotsMachineDataDetails rotsMachineDataDetails) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(rotsMachineDataDetails);  
	}
	
	@SuppressWarnings("unchecked")
	public RotsProductMaster getProdMasterById(Integer productId){
		 Session session = this.sessionFactory.getCurrentSession();
		 CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		 CriteriaQuery<RotsProductMaster> criteriaQuery = criteriaBuilder.createQuery(RotsProductMaster.class);
		 Root<RotsProductMaster> root = criteriaQuery.from(RotsProductMaster.class);
		
		// root.fetch(RotsMachineActivityDetails.class, JoinType.LEFT);
		 
		 criteriaQuery.where(criteriaBuilder.equal(root.get("productId"), productId));
		 
		 Query<RotsProductMaster> query = session.createQuery(criteriaQuery);

		 RotsProductMaster rotsProductMaster  = (RotsProductMaster) query.uniqueResult();
		 return rotsProductMaster;
	}
	
	@SuppressWarnings("unchecked")
	public RotsProductMaster getProdMasterByCode(String productCode){
		 Session session = this.sessionFactory.getCurrentSession();
		 CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		 CriteriaQuery<RotsProductMaster> criteriaQuery = criteriaBuilder.createQuery(RotsProductMaster.class);
		 Root<RotsProductMaster> root = criteriaQuery.from(RotsProductMaster.class);
		
		// root.fetch(RotsMachineActivityDetails.class, JoinType.LEFT);
		 
		 criteriaQuery.where(criteriaBuilder.equal(root.get("productCode"), productCode));
		 
		 Query<RotsProductMaster> query = session.createQuery(criteriaQuery);

		 RotsProductMaster rotsProductMaster  = (RotsProductMaster) query.uniqueResult();
		 return rotsProductMaster;
	}
	
	public void saveRotsMachineDataTest(RotsMachineDataTest rotsMachineDataTest) {
		this.sessionFactory.getCurrentSession().saveOrUpdate(rotsMachineDataTest);  
	}
	
	public void saveRotsMachineLiveData(RotsMachineLiveData rotsMachineLiveData) {
		
		Session session = this.sessionFactory.getCurrentSession();
		
	    
	    Query theQuery = null;		
		
		theQuery = session.createQuery("Update RotsMachineLiveData set productCode = :productCode, "
				+ "recordDateTime = :recordDateTime,"
				+ " prodCount = :prodCount,"
				+ " scrapCount = :scrapCount,"
				+ " operatorId = :operatorId,"
				+ " status = :status,"
				+ " createdDate = :createdDate,"
				+ " createdBy = :createdBy,"
				+ " updatedDate = :updatedDate,"
				+ " updatedBy = :updatedBy"
				+ " where machineId =:machineId ");
		theQuery.setParameter("productCode", rotsMachineLiveData.getProductCode());
		theQuery.setParameter("recordDateTime", rotsMachineLiveData.getRecordDateTime());
		theQuery.setParameter("prodCount", rotsMachineLiveData.getProdCount());
		theQuery.setParameter("scrapCount", rotsMachineLiveData.getScrapCount());
		theQuery.setParameter("operatorId", rotsMachineLiveData.getOperatorId());
		theQuery.setParameter("status", rotsMachineLiveData.getStatus());
		theQuery.setParameter("createdDate", rotsMachineLiveData.getCreatedDate());
		theQuery.setParameter("createdBy", rotsMachineLiveData.getCreatedBy());
		theQuery.setParameter("updatedDate", rotsMachineLiveData.getUpdatedDate());
		theQuery.setParameter("updatedBy", rotsMachineLiveData.getUpdatedBy());
		theQuery.setParameter("machineId", rotsMachineLiveData.getMachineId());

		try {
		int result = theQuery.executeUpdate();
		}catch(Exception e) {
			logger.debug(e.getStackTrace());
		}
		
   
	}
	
	
	//Manage Shift APIS
	@SuppressWarnings("unchecked")
	public List<RotsShiftMaster> getrAllShifts(Integer pageNumber, Integer pageSize){
		 Session session = this.sessionFactory.getCurrentSession();
		  
		 CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		 CriteriaQuery<RotsShiftMaster> criteriaQuery = criteriaBuilder.createQuery(RotsShiftMaster.class);
		 Root<RotsShiftMaster> root = criteriaQuery.from(RotsShiftMaster.class);
		
		 
		 List<Predicate> restrictions = new ArrayList<Predicate>();
		  // Add the active start/end date restrictions
		
		  restrictions.add(criteriaBuilder.equal(root.get("activeFlag"), ROTSConstants.ACTIVE.getId()));
		 
		  // Add the restrictions to the criteria query
		  criteriaQuery.where(restrictions.toArray(new Predicate[restrictions.size()]));
		  
		
		 List<Order> orderList = new ArrayList<Order>();
		 orderList.add(criteriaBuilder.asc(root.get("createdDate")));
		 criteriaQuery.orderBy(orderList);		 		
		 
		 Query<RotsShiftMaster> query = session.createQuery(criteriaQuery);
		 query.setFirstResult((pageNumber-1) * pageSize); 
		 query.setMaxResults(pageSize);

		 List<RotsShiftMaster> listOfShiftMaster = query.getResultList();
		 return listOfShiftMaster;
	}
	
	@SuppressWarnings("unchecked")
	public List<RotsBreakMaster> getAllBreaksForShift(Integer shiftId){
		 Session session = this.sessionFactory.getCurrentSession();
		
		 
		 CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		 CriteriaQuery<RotsBreakMaster> criteriaQuery = criteriaBuilder.createQuery(RotsBreakMaster.class);
		 Root<RotsBreakMaster> root = criteriaQuery.from(RotsBreakMaster.class);
		
		 List<Predicate> restrictions = new ArrayList<Predicate>();
		  // Add the active start/end date restrictions
		  restrictions.add(criteriaBuilder.equal(root.get("rotsShiftMaster").get("shiftId"), shiftId));
		  restrictions.add(criteriaBuilder.equal(root.get("activeFlag"), ROTSConstants.ACTIVE.getId()));
		
		  // Add the restrictions to the criteria query
		  criteriaQuery.where(restrictions.toArray(new Predicate[restrictions.size()]));
		  
		  
			 
		 Query<RotsBreakMaster> query = session.createQuery(criteriaQuery);

		 List<RotsBreakMaster> listOfAllBreaks = query.getResultList();
		 return listOfAllBreaks;
	}
	
	public Integer saveRotsShiftMaster(RotsShiftMaster rotsShiftMaster) {
		return (Integer) this.sessionFactory.getCurrentSession().save(rotsShiftMaster);  
	}
	
	public void mergeRotsShiftMaster(RotsShiftMaster rotsShiftMaster) {
		this.sessionFactory.getCurrentSession().merge(rotsShiftMaster);  
	}
	
	@SuppressWarnings("unchecked")
	public RotsShiftMaster getShiftMasterById(Integer shiftId){
		 Session session = this.sessionFactory.getCurrentSession();
				 CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
				 CriteriaQuery<RotsShiftMaster> criteriaQuery = criteriaBuilder.createQuery(RotsShiftMaster.class);
				 Root<RotsShiftMaster> root = criteriaQuery.from(RotsShiftMaster.class);
				
				// root.fetch(RotsMachineActivityDetails.class, JoinType.LEFT);
				 
				 List<Predicate> restrictions = new ArrayList<Predicate>();
				  // Add the active start/end date restrictions
				  restrictions.add(criteriaBuilder.equal(root.get("shiftId"), shiftId));
				  restrictions.add(criteriaBuilder.equal(root.get("activeFlag"), ROTSConstants.ACTIVE.getId()));
				  
				  // Add the restrictions to the criteria query
				  criteriaQuery.where(restrictions.toArray(new Predicate[restrictions.size()]));
				  
				  
				 Query<RotsShiftMaster> query = session.createQuery(criteriaQuery);

				 RotsShiftMaster rotsShiftMaster  = (RotsShiftMaster) query.uniqueResult();
				 
		          return rotsShiftMaster;
		 
		 
	}
	
	@SuppressWarnings("unchecked")
	public RotsShiftMaster getShiftMasterByNumber(Integer shiftNumber){
		 Session session = this.sessionFactory.getCurrentSession();
				 CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
				 CriteriaQuery<RotsShiftMaster> criteriaQuery = criteriaBuilder.createQuery(RotsShiftMaster.class);
				 Root<RotsShiftMaster> root = criteriaQuery.from(RotsShiftMaster.class);
				
				// root.fetch(RotsMachineActivityDetails.class, JoinType.LEFT);
				 
				 List<Predicate> restrictions = new ArrayList<Predicate>();
				  // Add the active start/end date restrictions
				  restrictions.add(criteriaBuilder.equal(root.get("shiftNumber"), shiftNumber));
				  restrictions.add(criteriaBuilder.equal(root.get("activeFlag"), ROTSConstants.ACTIVE.getId()));
				  
				  // Add the restrictions to the criteria query
				  criteriaQuery.where(restrictions.toArray(new Predicate[restrictions.size()]));
				  
				  
				 Query<RotsShiftMaster> query = session.createQuery(criteriaQuery);

				 RotsShiftMaster rotsShiftMaster  = (RotsShiftMaster) query.uniqueResult();
				 
		          return rotsShiftMaster;
		 
		 
	}
	
	@SuppressWarnings("unchecked")
	public RotsBreakMaster getBreakMasterById(Integer breakId){
		 Session session = this.sessionFactory.getCurrentSession();
		 
		 CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		 CriteriaQuery<RotsBreakMaster> criteriaQuery = criteriaBuilder.createQuery(RotsBreakMaster.class);
		 Root<RotsBreakMaster> root = criteriaQuery.from(RotsBreakMaster.class);
		
		// root.fetch(RotsMachineActivityDetails.class, JoinType.LEFT);
		 
		 List<Predicate> restrictions = new ArrayList<Predicate>();
		  // Add the active start/end date restrictions
		  restrictions.add(criteriaBuilder.equal(root.get("id"), breakId));
		  restrictions.add(criteriaBuilder.equal(root.get("activeFlag"), ROTSConstants.ACTIVE.getId()));
		  // Add the restrictions to the criteria query
		  criteriaQuery.where(restrictions.toArray(new Predicate[restrictions.size()]));
		  
		 Query<RotsBreakMaster> query = session.createQuery(criteriaQuery);

		 RotsBreakMaster rotsBreakMaster  = (RotsBreakMaster) query.uniqueResult();
		 
          return rotsBreakMaster;
	}
	
	public void saveRotsBreakMaster(RotsBreakMaster rotsBreakMaster) {
		this.sessionFactory.getCurrentSession().save(rotsBreakMaster);  
	}
	
	public void mergeRotsBreakMaster(RotsBreakMaster rotsBreakMaster) {
		this.sessionFactory.getCurrentSession().merge(rotsBreakMaster);  
	}
	
	@SuppressWarnings("unchecked")
	public List<RotsMachineMaster> getAllActiveMachines(){
		 Session session = this.sessionFactory.getCurrentSession();
			 
		 CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		 CriteriaQuery<RotsMachineMaster> criteriaQuery = criteriaBuilder.createQuery(RotsMachineMaster.class);
		 Root<RotsMachineMaster> root = criteriaQuery.from(RotsMachineMaster.class);
		
		// root.fetch(RotsMachineActivityDetails.class, JoinType.LEFT);
		 
		 criteriaQuery.where(criteriaBuilder.equal(root.get("activeFlag"), ROTSConstants.ACTIVE.getId()));
	
		 Query<RotsMachineMaster> query = session.createQuery(criteriaQuery);

		 List<RotsMachineMaster> listOfActiveMachines  =  query.getResultList();
		 return listOfActiveMachines;
	}
	
	@SuppressWarnings("unchecked")
	public List<RotsShiftMaster> getOverlappingShift(Date startTime, Date endTime){
		 Session session = this.sessionFactory.getCurrentSession();
			 
		 CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		 CriteriaQuery<RotsShiftMaster> criteriaQuery = criteriaBuilder.createQuery(RotsShiftMaster.class);
		 Root<RotsShiftMaster> root = criteriaQuery.from(RotsShiftMaster.class);
		
		 List<Predicate> restrictions = new ArrayList<Predicate>();
		  // Add the active start/end date restrictions
		
		  restrictions.add(criteriaBuilder.lessThanOrEqualTo(root.get("startTime").as(Date.class), endTime));
		  restrictions.add(criteriaBuilder.lessThanOrEqualTo(root.get("endTime").as(Date.class), startTime));
		  restrictions.add(criteriaBuilder.equal(root.get("activeFlag"), ROTSConstants.ACTIVE.getId()));
		  // Add the restrictions to the criteria query
		  criteriaQuery.where(restrictions.toArray(new Predicate[restrictions.size()]));
		  
		 Query<RotsShiftMaster> query = session.createQuery(criteriaQuery);

		 List<RotsShiftMaster> listOfOverlappingShift  =  query.getResultList();
		 return listOfOverlappingShift;
	}
	
	@SuppressWarnings("unchecked")
	public List<RotsProductMaster> getAllProductForMachine(Integer machineId){
		 Session session = this.sessionFactory.getCurrentSession();
	 CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		 CriteriaQuery<RotsProductMaster> criteriaQuery = criteriaBuilder.createQuery(RotsProductMaster.class);
		 Root<RotsProductMaster> root = criteriaQuery.from(RotsProductMaster.class);		
		 Join<RotsProductMaster, RotsMachineMaster> join = root.join("rotsMachineMaster");
		// Join<Object, Object> machineMaster = root.join("rotsMachineMaster");
		 //root.join("rotsMachineMaster", JoinType.LEFT).alias("rm");
		 criteriaQuery.where(criteriaBuilder.equal(join.get("machineId"), machineId));
		 criteriaQuery.where(criteriaBuilder.equal(root.get("activeFlag"), ROTSConstants.ACTIVE.getId()));
		
		 
		 Query<RotsProductMaster> query = session.createQuery(criteriaQuery);

		 List<RotsProductMaster> listOfProducts  =  query.getResultList();
		 return listOfProducts;
	}
	
	@SuppressWarnings("unchecked")
	public RotsTargetDetails getTargetDetails(Date date, Integer shiftId, Integer machineId, Integer productId){
		 Session session = this.sessionFactory.getCurrentSession();
			 
		 CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		 CriteriaQuery<RotsTargetDetails> criteriaQuery = criteriaBuilder.createQuery(RotsTargetDetails.class);
		 Root<RotsTargetDetails> root = criteriaQuery.from(RotsTargetDetails.class);
		
		// root.fetch(RotsMachineActivityDetails.class, JoinType.LEFT);
		 
		
		 List<Predicate> restrictions = new ArrayList<Predicate>();
		  // Add the active start/end date restrictions
		
		  restrictions.add(criteriaBuilder.equal(root.get("date"), date));
		  restrictions.add(criteriaBuilder.equal(root.get("shiftId"), shiftId));
		  restrictions.add(criteriaBuilder.equal(root.get("machineId"), machineId));
		  if(null != productId) {
			  restrictions.add(criteriaBuilder.equal(root.get("productId"), productId));
		  }
		  restrictions.add(criteriaBuilder.equal(root.get("activeFlag"), ROTSConstants.ACTIVE.getId()));
		  // Add the restrictions to the criteria query
		  criteriaQuery.where(restrictions.toArray(new Predicate[restrictions.size()]));
		  
		 Query<RotsTargetDetails> query = session.createQuery(criteriaQuery);

		 RotsTargetDetails rotsTargetDetails =  query.uniqueResult();
		 return rotsTargetDetails;
	}
	
	@SuppressWarnings("unchecked")
	public RotsTargetDetails getTargetDetailsById(Integer targetId){
		 Session session = this.sessionFactory.getCurrentSession();
			 
		 CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		 CriteriaQuery<RotsTargetDetails> criteriaQuery = criteriaBuilder.createQuery(RotsTargetDetails.class);
		 Root<RotsTargetDetails> root = criteriaQuery.from(RotsTargetDetails.class);
		
		// root.fetch(RotsMachineActivityDetails.class, JoinType.LEFT);
		 
		
		 List<Predicate> restrictions = new ArrayList<Predicate>();
		  // Add the active start/end date restrictions
		
		  restrictions.add(criteriaBuilder.equal(root.get("id"), targetId));
		  restrictions.add(criteriaBuilder.equal(root.get("activeFlag"), ROTSConstants.ACTIVE.getId()));
		  // Add the restrictions to the criteria query
		  criteriaQuery.where(restrictions.toArray(new Predicate[restrictions.size()]));
		  
		 Query<RotsTargetDetails> query = session.createQuery(criteriaQuery);

		 RotsTargetDetails rotsTargetDetails =  query.uniqueResult();
		 return rotsTargetDetails;
	}
	
	public List<RotsTargetDetails> getTargetDetailsForDate(Date date, PaginationDtls paginationDtls){
		 Session session = this.sessionFactory.getCurrentSession();
			 
		 CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		 CriteriaQuery<RotsTargetDetails> criteriaQuery = criteriaBuilder.createQuery(RotsTargetDetails.class);
		 Root<RotsTargetDetails> root = criteriaQuery.from(RotsTargetDetails.class);
		
		// root.fetch(RotsMachineActivityDetails.class, JoinType.LEFT);
		 
		 List<Predicate> restrictions = new ArrayList<Predicate>();
		  // Add the active start/end date restrictions
		
		  restrictions.add(criteriaBuilder.greaterThanOrEqualTo(root.get("date"), date));
		  restrictions.add(criteriaBuilder.equal(root.get("activeFlag"), ROTSConstants.ACTIVE.getId()));
		 
		  // Add the restrictions to the criteria query
		  criteriaQuery.where(restrictions.toArray(new Predicate[restrictions.size()]));
		  
		
		 List<Order> orderList = new ArrayList<Order>();
		 orderList.add(criteriaBuilder.desc(root.get("date")));
		 orderList.add(criteriaBuilder.desc(root.get("shiftId")));
		 orderList.add(criteriaBuilder.asc(root.get("machineId")));
		 criteriaQuery.orderBy(orderList);
		 
		 Query<RotsTargetDetails> query = session.createQuery(criteriaQuery);
		 
			// total rows count
		 Integer totalElements = query.getResultList().size();
		 paginationDtls.setTotalElements(totalElements);
		 
		 query.setFirstResult((paginationDtls.getPageNumber()-1) * paginationDtls.getPageSize()); 
		 query.setMaxResults(paginationDtls.getPageSize());
		 
		 List<RotsTargetDetails> listOfTargetDtls =  query.getResultList();
		 return listOfTargetDtls;
	}
	
	public List<RotsScrapCountDetails> getScrapDetailsForDate(Date date, Integer pageNumber, Integer pageSize){
		 Session session = this.sessionFactory.getCurrentSession();
			 
		 CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		 CriteriaQuery<RotsScrapCountDetails> criteriaQuery = criteriaBuilder.createQuery(RotsScrapCountDetails.class);
		 Root<RotsScrapCountDetails> root = criteriaQuery.from(RotsScrapCountDetails.class);
		
		// root.fetch(RotsMachineActivityDetails.class, JoinType.LEFT);
		 
		 criteriaQuery.where(criteriaBuilder.greaterThanOrEqualTo(root.get("date"), date));
		 criteriaQuery.where(criteriaBuilder.equal(root.get("activeFlag"), ROTSConstants.ACTIVE.getId()));
		 
		 List<Order> orderList = new ArrayList<Order>();
		 orderList.add(criteriaBuilder.desc(root.get("date")));
		 orderList.add(criteriaBuilder.asc(root.get("shiftId")));
		 criteriaQuery.orderBy(orderList);
		 
		 Query<RotsScrapCountDetails> query = session.createQuery(criteriaQuery);
		 
		 query.setFirstResult((pageNumber-1) * pageSize); 
		 query.setMaxResults(pageSize);
		 
		 List<RotsScrapCountDetails> listOfScrapDtls =  query.getResultList();
		 return listOfScrapDtls;
	}
	
	
	
	public void saveRotsTargetDetails(RotsTargetDetails rotsTargetDetails) {
		this.sessionFactory.getCurrentSession().save(rotsTargetDetails);  
	}
	
	public void mergeRotsTargetDetails(RotsTargetDetails rotsTargetDetails) {
		this.sessionFactory.getCurrentSession().merge(rotsTargetDetails);  
	}
	
	
	public RotsScrapCountDetails getScrapCountDetails(Date date, Integer shiftId, Integer machineId, Integer productId){
		 Session session = this.sessionFactory.getCurrentSession();
			 
		 CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		 CriteriaQuery<RotsScrapCountDetails> criteriaQuery = criteriaBuilder.createQuery(RotsScrapCountDetails.class);
		 Root<RotsScrapCountDetails> root = criteriaQuery.from(RotsScrapCountDetails.class);
		
		// root.fetch(RotsMachineActivityDetails.class, JoinType.LEFT);
		 
		 criteriaQuery.where(criteriaBuilder.equal(root.get("date"), date));
		 criteriaQuery.where(criteriaBuilder.equal(root.get("shiftId"), shiftId));
		 criteriaQuery.where(criteriaBuilder.equal(root.get("machineId"), machineId));
		 criteriaQuery.where(criteriaBuilder.equal(root.get("productId"), productId));
		 criteriaQuery.where(criteriaBuilder.equal(root.get("activeFlag"), ROTSConstants.ACTIVE.getId()));
		 Query<RotsScrapCountDetails> query = session.createQuery(criteriaQuery);

		 RotsScrapCountDetails rotsScrapCountDetails =  query.uniqueResult();
		 return rotsScrapCountDetails;
	}
	
	public void saveRotsScrapDetails(RotsScrapCountDetails rotsScrapCountDetails) {
		this.sessionFactory.getCurrentSession().save(rotsScrapCountDetails);  
	}
	
	public void mergeRotsScrapDetails(RotsScrapCountDetails rotsScrapCountDetails) {
		this.sessionFactory.getCurrentSession().merge(rotsScrapCountDetails);  
	}
	
	
}
