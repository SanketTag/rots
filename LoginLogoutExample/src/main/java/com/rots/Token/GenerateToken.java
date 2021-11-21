package com.rots.Token;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import com.rots.util.DateUtils;

import java.security.Key;
import java.text.ParseException;
import java.util.Date;
import java.util.Random;

import io.jsonwebtoken.*;

public class GenerateToken {
	
	private static String SECRET_KEY = "ANGRobotics";
	
public String[] createJWT(String id, String issuer, String subject, Integer userRoleId , long ttlMillis) {
		
	    //The JWT signature algorithm we will be using to sign the token
	    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
	 
	    long nowMillis = System.currentTimeMillis();
	    Date now = new Date(nowMillis);
	    
		Random random = new Random();
		//String secretKey = id  + Integer.toString(random.nextInt(1000));
	
	    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
	    
	    Key signingKey = null;
	    try{
	    	
	    	signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
	    }
	    catch(Exception e)
	    {
	    	System.out.println("Exception while generating key " + e.getMessage() );
	    }
	    
	    Date exp  = null;
	    if (ttlMillis >= 0) {
		    long expMillis = nowMillis + ttlMillis;
		         exp = new Date(expMillis);
		       
		    }
	    
	    Claims claims = Jwts.claims().setSubject(id);
        claims.put("userRoleId", userRoleId);
        
	    JwtBuilder builder = Jwts.builder().setId(id)
	    							.setClaims(claims)
	                                .setIssuedAt(now)
	                                .setSubject(subject)
	                                .setIssuer(issuer)
	                                .signWith(signatureAlgorithm, signingKey);
	    
	    //if it has been specified, let's add the expiration
	    builder.setExpiration(exp);
	    
	    
	    
	    String[] tokenInfo = {builder.compact() , SECRET_KEY};
	    return tokenInfo;
	    
	}

   public static Integer decodeJWT(String token) throws ParseException {
    //This line will throw an exception if it is not a signed JWS (as expected)
	   SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		 
	    long nowMillis = System.currentTimeMillis();
	    Date now = new Date(nowMillis);
	    
		Random random = new Random();
		//String secretKey = id  + Integer.toString(random.nextInt(1000));
	
	    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
	    
	 
	    	
	   Key	signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
	    
	    Claims claims = Jwts.parser()
	            .setSigningKey(signingKey)
	            .parseClaimsJws(token).getBody();
	    
	   
	   Integer userRoleId = Integer.valueOf(claims.get("userRoleId").toString());
	   return userRoleId;
}
}

