package com.rots.DAO;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rots.entity.RotsMachineActivityDetails;
import com.rots.entity.RotsMachineDataDetails;
import com.rots.entity.RotsMachineDataTest;
import com.rots.entity.RotsProductMaster;


@Repository("ParameterCalculationDao")
public class ParameterCalculationDaoImpl implements ParameterCalculationDao{

	@Autowired
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@SuppressWarnings("unchecked")
	public List<RotsMachineDataDetails> getDistinctProductIds(){
		 Session session = this.sessionFactory.getCurrentSession();
			 
			
		 CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		 CriteriaQuery<RotsMachineDataDetails> criteriaQuery = criteriaBuilder.createQuery(RotsMachineDataDetails.class);
		 Root<RotsMachineDataDetails> root = criteriaQuery.from(RotsMachineDataDetails.class);
		
		// root.fetch(RotsMachineActivityDetails.class, JoinType.LEFT);
		 
		 criteriaQuery.where(criteriaBuilder.equal(root.get("processFlag"), 0));
		 criteriaQuery.select(root.get("rotsProductMaster").get("productId"));
		 
		 Query<RotsMachineDataDetails> query = session.createQuery(criteriaQuery);
		 List<RotsMachineDataDetails> listOfProductIds = query.getResultList();
		 
		 return listOfProductIds;
	}
	
	@SuppressWarnings("unchecked")
	public List<RotsMachineDataDetails> getUnprocessedRecords(){
		 Session session = this.sessionFactory.getCurrentSession();
		
		 
		 CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		 CriteriaQuery<RotsMachineDataDetails> criteriaQuery = criteriaBuilder.createQuery(RotsMachineDataDetails.class);
		 Root<RotsMachineDataDetails> root = criteriaQuery.from(RotsMachineDataDetails.class);
		
		 criteriaQuery.where(criteriaBuilder.equal(root.get("processFlag"), 0));
		 
		 List<Order> orderList = new ArrayList<Order>();
		 orderList.add(criteriaBuilder.asc(root.get("recordDateTime")));
		 criteriaQuery.orderBy(orderList);
		 
		 Query<RotsMachineDataDetails> query = session.createQuery(criteriaQuery);
		 List<RotsMachineDataDetails> listOfUnprocessedRecords = query.getResultList();
		 return listOfUnprocessedRecords;
	}
	
	@SuppressWarnings("unchecked")
	public List<RotsMachineDataTest> getUnprocessedRecordsTest(){
		 Session session = this.sessionFactory.getCurrentSession();
		
		 
		 CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		 CriteriaQuery<RotsMachineDataTest> criteriaQuery = criteriaBuilder.createQuery(RotsMachineDataTest.class);
		 Root<RotsMachineDataTest> root = criteriaQuery.from(RotsMachineDataTest.class);
		
		 criteriaQuery.where(criteriaBuilder.equal(root.get("processFlag"), 0));
		 
		 List<Order> orderList = new ArrayList<Order>();
		 orderList.add(criteriaBuilder.asc(root.get("recordDateTime")));
		 criteriaQuery.orderBy(orderList);
		 
		 Query<RotsMachineDataTest> query = session.createQuery(criteriaQuery);
		 List<RotsMachineDataTest> listOfUnprocessedRecords = query.getResultList();
		 return listOfUnprocessedRecords;
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveRotsMachineActivityDetails(RotsMachineActivityDetails rotsMachineActivityDetails){
		this.sessionFactory.getCurrentSession().save(rotsMachineActivityDetails);
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void mergeAllRotsMachineDataDetails(List<RotsMachineDataDetails> listOfRotsMachineDataDetails){
		for (RotsMachineDataDetails rotsMachineDataDetails : listOfRotsMachineDataDetails) {
			this.sessionFactory.getCurrentSession().merge(rotsMachineDataDetails);
		}
		
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void mergeAllRotsMachineDataTestDetails(List<RotsMachineDataTest> listOfRotsMachineDataDetails){
		for (RotsMachineDataTest rotsMachineDataDetails : listOfRotsMachineDataDetails) {
			this.sessionFactory.getCurrentSession().merge(rotsMachineDataDetails);
		}
		
	}
	
	public RotsProductMaster getProductMasterByProductId(Integer productId) {
		         Session session = this.sessionFactory.getCurrentSession();
		         
		         CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
				 CriteriaQuery<RotsProductMaster> criteriaQuery = criteriaBuilder.createQuery(RotsProductMaster.class);
				 Root<RotsProductMaster> root = criteriaQuery.from(RotsProductMaster.class);
				
				 criteriaQuery.where(criteriaBuilder.equal(root.get("productId"), productId));
				 
				 Query<RotsProductMaster> query = session.createQuery(criteriaQuery);
				 RotsProductMaster rotsProductMaster = query.uniqueResult();
				 return rotsProductMaster;
	}
	
	
	
}
