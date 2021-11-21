package com.rots.DAO;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rots.entity.AdminDetail;
import com.rots.entity.RotsUserMaster;

@Repository("adminDAO")
public class AdminDAOImpl implements AdminDAO {

	// Autowired SessionFactory Object So that we can get session object used for interaction with Database.
	@Autowired
	private SessionFactory sessionFactory;
		
	/*
	 * Register Admin Details. 
	*/
	public int saveAdminDetail(AdminDetail adminDetail) {
		
		Session session = null;
		try
		{
			session = sessionFactory.getCurrentSession();
			
			int id = (Integer) session.save(adminDetail);
			return id;
		}
		catch(Exception exception)
		{
			System.out.println("Excecption while saving admin Details : " + exception.getMessage());
			return 0;
		}
		finally
		{
			session.flush();
		}
	}

	public int adminLogin(String emailId, String password) {
		
		Session session = null;
		try
		{
			session = sessionFactory.getCurrentSession();
			
			Query query = session.createQuery("from AdminDetail where emailId=:emailId and password=:password");
			query.setParameter("emailId", emailId);
			query.setParameter("password", password);
			List<AdminDetail> list = query.list();
			
			int size = list.size();
			if(size == 1)
			{
				return list.get(0).getAdminID();
			}
			else
			{
				return -1;
			}
		}
		catch(Exception exception)
		{
			System.out.println("Excecption while saving admin Details : " + exception.getMessage());
			return 0;
		}
		finally
		{
			session.flush();
		}
		
	}
	
public int userLogin(String username, String password) {
		
		Session session = null;
		try
		{
			session = sessionFactory.getCurrentSession();
			
			Query query = session.createQuery("from RotsUserMaster where username=:username and password=:password");
			query.setParameter("username", username);
			query.setParameter("password", password);
			List<RotsUserMaster> list = query.list();
			
			int size = list.size();
			if(size == 1)
			{
				return list.get(0).getUserId();
			}
			else
			{
				return -1;
			}
		}
		catch(Exception exception)
		{
			System.out.println("Excecption while saving admin Details : " + exception.getMessage());
			return 0;
		}
		finally
		{
			session.flush();
		}
		
	}

	
	public List<AdminDetail> getAdminData() {
		Session session = null;
		try
		{
			session = sessionFactory.getCurrentSession();
			
			Query<AdminDetail> query = session.createQuery("from AdminDetail");
			List<AdminDetail> list = query.list();
			
			if(list.size() > 0)
			{
				return list;
			}
			else
			{
				return null;
			}
			
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
	
	public List<AdminDetail> getUserDetails() {
		Session session = null;
		try
		{
			session = sessionFactory.getCurrentSession();
			
			Query<AdminDetail> query = session.createQuery("from AdminDetail");
			List<AdminDetail> list = query.list();
			
			if(list.size() > 0)
			{
				return list;
			}
			else
			{
				return null;
			}
			
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

	
}
