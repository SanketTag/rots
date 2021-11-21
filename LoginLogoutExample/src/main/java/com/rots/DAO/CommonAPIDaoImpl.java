package com.rots.DAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rots.constants.ROTSConstants;
import com.rots.entity.RotsProductMaster;
import com.rots.entity.RotsShiftMaster;

@Repository("CommonAPIDao")
public class CommonAPIDaoImpl implements CommonAPIDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	public RotsShiftMaster getCurrentShift(Date currentDate) {
		
		     Session session = this.sessionFactory.getCurrentSession();
	         
	         CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
			 CriteriaQuery<RotsShiftMaster> criteriaQuery = criteriaBuilder.createQuery(RotsShiftMaster.class);
			 Root<RotsShiftMaster> root = criteriaQuery.from(RotsShiftMaster.class);
			
			 List<Predicate> restrictions = new ArrayList<Predicate>();
			  // Add the active start/end date restrictions
			  restrictions.add(criteriaBuilder.lessThanOrEqualTo(root.get("startTime").as(Date.class), currentDate));
			  restrictions.add(criteriaBuilder.greaterThanOrEqualTo(root.get("endTime").as(Date.class), currentDate));
			  restrictions.add(criteriaBuilder.equal(root.get("activeFlag"), ROTSConstants.ACTIVE.getId()));
			  // Add the restrictions to the criteria query
			  criteriaQuery.where(restrictions.toArray(new Predicate[restrictions.size()]));
			  
			 Query<RotsShiftMaster> query = session.createQuery(criteriaQuery);
			 RotsShiftMaster rotsShiftMaster = query.uniqueResult();
			 return rotsShiftMaster;
	}
	
	public RotsShiftMaster getShiftByNumber(Integer shiftNumber) {
		
	     Session session = this.sessionFactory.getCurrentSession();
        
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		 CriteriaQuery<RotsShiftMaster> criteriaQuery = criteriaBuilder.createQuery(RotsShiftMaster.class);
		 Root<RotsShiftMaster> root = criteriaQuery.from(RotsShiftMaster.class);
		
		 List<Predicate> restrictions = new ArrayList<Predicate>();
		  // Add the active start/end date restrictions
		  restrictions.add(criteriaBuilder.equal(root.get("shiftNumber"), shiftNumber));
		  restrictions.add(criteriaBuilder.equal(root.get("activeFlag"), ROTSConstants.ACTIVE.getId()));
		  // Add the restrictions to the criteria query
		  criteriaQuery.where(restrictions.toArray(new Predicate[restrictions.size()]));
		  
		 Query<RotsShiftMaster> query = session.createQuery(criteriaQuery);
		 RotsShiftMaster rotsShiftMaster = query.uniqueResult();
		 return rotsShiftMaster;
}
	
}
