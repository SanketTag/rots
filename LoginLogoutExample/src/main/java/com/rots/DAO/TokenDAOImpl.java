package com.rots.DAO;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rots.entity.RotsToken;
import com.rots.entity.Token;

@Repository("tokenDAO")
public class TokenDAOImpl implements TokenDAO  {
	
	@Autowired
	SessionFactory sessionFactory;

	public void saveUserEmail(String email, int adminId) {
		Session session = null; 
		try
		{
			session = sessionFactory.getCurrentSession();
			Token t = new Token();
			t.setUserID(adminId);
			t.setEmailId(email);
			session.save(t); 
		}
		catch(Exception exception)
		{
			System.out.println("Exception in saving UserEmail In Token Table :: " + exception.getMessage());
		}
		finally
		{
			session.flush();
		}
		
	}
	
	public void saveRotsUser(String username, int userRoleId) {
		Session session = null; 
		try
		{
			session = sessionFactory.getCurrentSession();
			RotsToken t = new RotsToken();
			t.setUserRoleId(userRoleId);
			t.setUsername(username);
			session.save(t); 
		}
		catch(Exception exception)
		{
			System.out.println("Exception in saving username In RotsToken Table :: " + exception.getMessage());
		}
		finally
		{
			session.flush();
		}
		
	}

	public boolean updateToken(String email, String authenticationToken, String secretKey) {
		Session session = null;
		try 
		{
			session = sessionFactory.getCurrentSession();
			Query theQuery = null;		
			
			theQuery = session.createQuery("Update Token set authenticationToken = :authenticationToken , secretKey = :secretKey where emailId =:userEmail ");
				
			theQuery.setParameter("authenticationToken", authenticationToken);
			theQuery.setParameter("userEmail", email);
			theQuery.setParameter("secretKey", secretKey);

			int result = theQuery.executeUpdate();
					
			if(result == 1)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch(Exception exception)
		{
			System.out.println("Error while updating token :: " + exception.getMessage());
			return false;
		}
		finally
		{
			session.flush();
		}			
	}
	
	public boolean updateRotsToken(String username, String authenticationToken, String secretKey) {
		Session session = null;
		try 
		{
			session = sessionFactory.getCurrentSession();
			Query theQuery = null;		
			
			theQuery = session.createQuery("Update RotsToken set authenticationToken = :authenticationToken , secretKey = :secretKey where username =:username ");
				
			theQuery.setParameter("authenticationToken", authenticationToken);
			theQuery.setParameter("username", username);
			theQuery.setParameter("secretKey", secretKey);

			int result = theQuery.executeUpdate();
					
			if(result == 1)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch(Exception exception)
		{
			System.out.println("Error while updating token :: " + exception.getMessage());
			return false;
		}
		finally
		{
			session.flush();
		}			
	}

	public int getTokenDetail(String email) {
		Session session = null;
		try
		{
			session = sessionFactory.getCurrentSession();
			Query<Token> query = session.createQuery("from Token where emailId =:userEmail");
			query.setParameter("userEmail", email);
			
			List<Token> tokenDetails = query.list();
			
			if(tokenDetails.size() > 0)
			{
				return tokenDetails.get(0).getTokenID();
			}
			else
			{
				return 0;
			}
			
		}
		catch(Exception exception)
		{
			System.out.println("Exception while getting token ID :: " + exception.getMessage());		
		}
		finally
		{
			session.flush();
		}
		
		return 0;
	}
	
	public int getRotsTokenDetail(String username) {
		Session session = null;
		try
		{
			session = sessionFactory.getCurrentSession();
			Query<RotsToken> query = session.createQuery("from RotsToken where username =:username");
			query.setParameter("username", username);
			
			List<RotsToken> tokenDetails = query.list();
			
			if(tokenDetails.size() > 0)
			{
				return tokenDetails.get(0).getTokenID();
			}
			else
			{
				return 0;
			}
			
		}
		catch(Exception exception)
		{
			System.out.println("Exception while getting token ID :: " + exception.getMessage());		
		}
		finally
		{
			session.flush();
		}
		
		return 0;
	}

	public int tokenAuthentication(String token, int emailId) {
		Session session = null;
		
		try 
		{
			session = sessionFactory.getCurrentSession();
			
			Query query = session.createQuery("from Token where userID =:userID and authenticationToken = :token");
			query.setParameter("userID", emailId);
			query.setParameter("token", token);
			List<Token> tokenDetails = query.list();
			
			if(tokenDetails.size() > 0)
			{
				return tokenDetails.get(0).getTokenID();
			}
			else
			{
				return 0;
			}

		}
		catch(Exception exception)
		{
			System.out.println("Exception while Authenticating token :: "+ exception);
			return 0;
		}
		finally
		{
			session.flush();
		}
		
		
	}
	
	public int tokenRotsAuthentication(String token, int userRoleId) {
		Session session = null;
		
		try 
		{
			session = sessionFactory.getCurrentSession();
			
			Query query = session.createQuery("from RotsToken where userRoleId =:userRoleId and authenticationToken = :token");
			query.setParameter("userRoleId", userRoleId);
			query.setParameter("token", token);
			List<RotsToken> tokenDetails = query.list();
			
			if(tokenDetails.size() > 0)
			{
				return tokenDetails.get(0).getTokenID();
			}
			else
			{
				return 0;
			}

		}
		catch(Exception exception)
		{
			System.out.println("Exception while Authenticating token :: "+ exception);
			return 0;
		}
		finally
		{
			session.flush();
		}
		
		
	}

}
