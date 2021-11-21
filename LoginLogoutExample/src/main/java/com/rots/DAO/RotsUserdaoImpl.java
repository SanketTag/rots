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
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rots.constants.MachineStatus;
import com.rots.constants.ROTSConstants;
import com.rots.entity.RotsMachineActivityDetails;
import com.rots.entity.RotsUserRoles;


@Repository
public class RotsUserdaoImpl implements RotsUserDao{

	@Autowired
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	
	
	public RotsUserRoles getUserByUserName(String username)
	{
		 Session session = this.sessionFactory.getCurrentSession();
			 
		 CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		 CriteriaQuery<RotsUserRoles> criteriaQuery = criteriaBuilder.createQuery(RotsUserRoles.class);
		 Root<RotsUserRoles> root = criteriaQuery.from(RotsUserRoles.class);
		
		// root.fetch(RotsMachineActivityDetails.class, JoinType.LEFT);
		 
		 List<Predicate> restrictions = new ArrayList<Predicate>();
		  // Add the active start/end date restrictions
		  restrictions.add(criteriaBuilder.equal(root.get("username"), username));
		  restrictions.add(criteriaBuilder.equal(root.get("activeFlag"), ROTSConstants.ACTIVE.getId()));
		  // Add the restrictions to the criteria query
		  criteriaQuery.where(restrictions.toArray(new Predicate[restrictions.size()]));
		                               
		 
		 Query<RotsUserRoles> query = session.createQuery(criteriaQuery);

		 return (RotsUserRoles) query.uniqueResult();
	}
	
	
	public RotsUserRoles getUserRoleObj(Integer userroleID){
		 Session session = this.sessionFactory.getCurrentSession();
		 
		 CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		 CriteriaQuery<RotsUserRoles> criteriaQuery = criteriaBuilder.createQuery(RotsUserRoles.class);
		 Root<RotsUserRoles> root = criteriaQuery.from(RotsUserRoles.class);
		
		// root.fetch(RotsMachineActivityDetails.class, JoinType.LEFT);
		 
		 List<Predicate> restrictions = new ArrayList<Predicate>();
		  // Add the active start/end date restrictions
		  restrictions.add(criteriaBuilder.equal(root.get("userRoleId"), userroleID));
		  restrictions.add(criteriaBuilder.equal(root.get("activeFlag"), ROTSConstants.ACTIVE.getId()));
		  // Add the restrictions to the criteria query
		  criteriaQuery.where(restrictions.toArray(new Predicate[restrictions.size()]));                              
		 
		 Query<RotsUserRoles> query = session.createQuery(criteriaQuery);

		 return (RotsUserRoles) query.uniqueResult();
	}
}
