package com.rots.DAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rots.constants.ROTSConstants;
import com.rots.entity.RotsMachineActivityDetails;
import com.rots.entity.RotsMachineLiveData;



@Repository("ReportsDao")
public class ReportsDaoImpl implements ReportsDao{

	@Autowired
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@SuppressWarnings("unchecked")
	public List<RotsMachineActivityDetails> getAllActivityRecordsForMachine(Integer machineId, Date fromDate, Date toDate){
		 Session session = this.sessionFactory.getCurrentSession();
		
		 CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		 CriteriaQuery<RotsMachineActivityDetails> criteriaQuery = criteriaBuilder.createQuery(RotsMachineActivityDetails.class);
		 Root<RotsMachineActivityDetails> root = criteriaQuery.from(RotsMachineActivityDetails.class);
		
		// root.fetch(RotsMachineActivityDetails.class, JoinType.LEFT);
		 
		 
		 List<Predicate> restrictions = new ArrayList<Predicate>();
		  // Add the active start/end date restrictions
		  restrictions.add(criteriaBuilder.equal(root.get("machineId"), machineId));
		  restrictions.add(criteriaBuilder.greaterThanOrEqualTo(root.get("startDate").as(Date.class), fromDate));
		  restrictions.add(criteriaBuilder.lessThanOrEqualTo(root.get("endDate").as(Date.class), toDate));
		
		  // Add the restrictions to the criteria query
		  criteriaQuery.where(restrictions.toArray(new Predicate[restrictions.size()]));
		  
		  
		 List<Order> orderList = new ArrayList<Order>();
		 orderList.add(criteriaBuilder.asc(root.get("startDate")));
		 criteriaQuery.orderBy(orderList);
		 
		 Query<RotsMachineActivityDetails> query = session.createQuery(criteriaQuery);

		 List<RotsMachineActivityDetails> listOFMachineActivities = query.getResultList();
		 return listOFMachineActivities;
	}
	
	@SuppressWarnings("unchecked")
	public List<RotsMachineLiveData> getLiveRecordsForAllMachines(){
		 Session session = this.sessionFactory.getCurrentSession();
				 
		 CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		 CriteriaQuery<RotsMachineLiveData> criteriaQuery = criteriaBuilder.createQuery(RotsMachineLiveData.class);
		 Root<RotsMachineLiveData> root = criteriaQuery.from(RotsMachineLiveData.class);
		
		 Query<RotsMachineLiveData> query = session.createQuery(criteriaQuery);

		 List<RotsMachineLiveData> listOfLiveRecords = query.getResultList();
		 
		 
		 return listOfLiveRecords;
	}
	
	
}
