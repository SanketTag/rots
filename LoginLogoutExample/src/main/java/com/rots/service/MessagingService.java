package com.rots.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.jivesoftware.smack.SmackException;

import com.rots.entity.RotsUserRoles;


public interface MessagingService {
	//public void sendEmail(MailDTO mailDto) throws UnsupportedEncodingException;
	//public EmailTemplate getEmailTemplate(Integer templateId);
	//public MailDTO constructMailDto(List<String> toList, String subject, String content);
	//public String replaceDyanamicValue(List<String> listOfDyanamicValues, String template);
	/*
	 * public String encryptToByteArr(Integer id) throws IOException,
	 * InvalidKeyException, Exception; public String decryptToInteger(String data)
	 * throws InvalidKeyException, Exception; public void sendAssgnRoleEmail(String
	 * userRoleId, RotsUserRoles userRole) throws UnsupportedEncodingException;
	 * 
	 * public void sendNotification(String regId, String message, Map<String,
	 * String> dataPayload, String fcmProjectSenderId, String fcmServerKey, String
	 * messageId) throws SmackException, IOException, RunTimeException; public
	 * String encrypt(String value) throws UnsupportedEncodingException,
	 * IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException,
	 * NoSuchPaddingException, InvalidKeyException,
	 * InvalidAlgorithmParameterException; public String decrypt(String encrypted)
	 * throws UnsupportedEncodingException, NoSuchAlgorithmException,
	 * NoSuchPaddingException, InvalidKeyException,
	 * InvalidAlgorithmParameterException, IllegalBlockSizeException,
	 * BadPaddingException; public void sendNotification(String appRegId,
	 * Map<String, String> dataPayload) throws SmackException, IOException; public
	 * void sendAMCMail(List<String> listOfDyanamicValues, List<String> toList,
	 * Integer mailTemplateId) throws UnsupportedEncodingException;
	 */
}
