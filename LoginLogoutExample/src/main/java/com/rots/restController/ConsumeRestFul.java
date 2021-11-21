package com.rots.restController;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Calendar;
import java.util.Date;

import com.google.api.client.util.Base64;
import com.rots.constants.ROTSConstants;
import com.rots.contract.StoppageReasonDtls;
import com.rots.entity.RotsReasonMaster;

public class ConsumeRestFul {

	// http://localhost:8080/RESTfulExample/json/product/post
		public static void main(String[] args) {
			//submitVisitDetails();
			//getAllComplaintsByTech();
			//uploadLiftPhoto();
			//updateLiftDetails();
			//addNewComplaint();
		//	getVisitDetails();
			//addEvent();
			//logIn();
			saveReason();
		}
		
		  public static void getAllComplaintsByTech(){
			  try{
			  /* 1.*/ // URL url = new URL("http://139.162.5.222:8000/RLMS/API/getAllComplaintsAssigned"); //(userRoleId 17)*/ 
				//URL url = new URL("http://139.162.5.222:8000/RLMS/API/register/registerMemeberDeviceByMblNo"); 
				/*3. */// URL url = new URL("http://139.162.5.222:8000/RLMS/API/register/registerTechnicianDeviceByMblNo");
				/* 4.*///URL url = new URL("http://localhost:8000/RLMS/API/lift/getAllLiftsForMember");
				 /*5.*/  URL url = new URL("http://139.162.5.222:8000/RLMS/API/complaints/getAllComplaintsByMember");
				//URL url = new URL("http://localhost:8000/RLMS/API/loginIntoApp");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/json");

			/*	2.*///String input = "{\"contactNumber\":\"9420192324\", \"latitude\":\"18.12457898\", \"longitude\":\"72.12457896\", \"appRegId\":\"AAAAGqJGmak:APA91bH5wu5DXT01MIyN2LF0n46WqR0ZXtuTCaV8qHGEe738r-fAfoIGG1ytz_k6oHiFEgo6nX9VSopGXi2qhylnjpXKdh4U-tzGoMIA78QDDqnxIVJQFo56AN1uKrmz0UiLo6_-lb3\", \"address\":\"WAKAD\"}";
				/* 3. */// String input = "{\"contactNumber\":\"9096136234\", \"latitude\":\"18.12457898\", \"longitude\":\"72.12457896\", \"appRegId\":\"AAAAGqJGmak:APA91bH5wu5DXT01MIyN2LF0n46WqR0ZXtuTCaV8qHGEe738r-fAfoIGG1ytz_k6oHiFEgo6nX9VSopGXi2qhylnjpXKdh4U-tzGoMIA78QDDqnxIVJQFo56AN1uKrmz0UiLo6_-lb3b\", \"address\":\"WAKAD\"}";
				// String input = "{\"userName\":\"admin\",\"password\":\"rlms1234\"}";
				/*/4 and 5.*/ String input = "{\"memberId\":\"5\"}";
				//String input = "{\"userRoleId\":\"43\"}";

				OutputStream os = conn.getOutputStream();
				os.write(input.getBytes());
				os.flush();

				if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
					//throw new RuntimeException("Failed : HTTP error code : "
					//	+ conn.getResponseCode());
				}

				BufferedReader br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));

				String output;
				System.out.println("Output from Server .... \n");
				while ((output = br.readLine()) != null) {
					System.out.println(output);
					
				}

				conn.disconnect();

			  } catch (MalformedURLException e) {

				e.printStackTrace();

			  } catch (IOException e) {

				e.printStackTrace();

			 }
		  }
		  
		  public static void uploadLiftPhoto(){
			  try{
				 URL url = new URL("http://localhost:9090/RLMS/API/lift/uploadPhoto");
				 HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true); 
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/json");
				byte[] machinePhotoBytes = null;
				File imgPath=new File("F:\\SpringProject\\RLMS\\src\\main\\java\\com\\rlms\\controller\\APP.jpg");
				machinePhotoBytes=Files.readAllBytes(imgPath.toPath());
				String base64String = Base64.encodeBase64String(machinePhotoBytes);
				String input="{\"liftCustomerMapId\":\"7\",\"photoType\":\"1\",\"machinePhoto\":\""+base64String+"\"}";
				
				OutputStream os = conn.getOutputStream();
				os.write(input.getBytes());
				os.flush();

				if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
				}

				BufferedReader br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));

				String output;
				System.out.println("Output from Server .... \n");
				while ((output = br.readLine()) != null) {
					System.out.println(output);
					
				}

				conn.disconnect();

			  } catch (MalformedURLException e) {

				e.printStackTrace();

			  } catch (IOException e) {

				e.printStackTrace();

			 }
		  }
		  
		  
		
		public static void logIn(){


			  try {
                  
				/* 1. */ URL url = new URL("http://localhost:8000/LoginLogoutExample/api/login"); //(userRoleId 17)*/ 
				
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/json");
			
				String input = "{\"userName\":\"admin\",\"password\":\"rlms1234\"}";

//				String input = "{\"complaintTechMapId\":\"3\",\"userRoleId\":\"2\",\"remark\":\"not done yet\"}";

				OutputStream os = conn.getOutputStream();
				os.write(input.getBytes());
				os.flush();

				if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
					//throw new RuntimeException("Failed : HTTP error code : "
					//	+ conn.getResponseCode());
				}

				BufferedReader br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));

				String output;
				System.out.println("Output from Server .... \n");
				while ((output = br.readLine()) != null) {
					System.out.println(output);
					
				}

				conn.disconnect();

			  } catch (MalformedURLException e) {

				e.printStackTrace();

			  } catch (IOException e) {

				e.printStackTrace();

			 }

			  //Example Response
			  
			//  Output from Server .... 

			  //{"status":true,"response":"Visit details updated successfully.","jsonElement":null,"jsonArray":null}

		}
		
		public static void saveReason() {
		try {
			/* 1. */ URL url = new URL("http://localhost:8000/LoginLogoutExample/admin/addNewScheduledStopReason"); //(userRoleId 17)*/ 
			
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "token hbGciOiJIUzI1NiJ9.eyJzdWIiOiJKV1QgVG9rZW4iLCJ1c2VyUm9sZUlkIjoxLCJpYXQiOjE2MDU4ODEyOTgsImlzcyI6IkphdmFUcG9pbnQiLCJleHAiOjE2MDU5MjQ0OTh9.3o7YvjXCWv-TYeB5h0OnjB-ZxiLhRGrThGqGTp1_jYk");
			String input = "{\"reasonTitle\":\"testReasonTitle\",\"reasonDesc\":\"testReasonDesc\"}";

//			String input = "{\"complaintTechMapId\":\"3\",\"userRoleId\":\"2\",\"remark\":\"not done yet\"}";

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();

			if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
				//throw new RuntimeException("Failed : HTTP error code : "
				//	+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
				
			}

			conn.disconnect();

		  } catch (MalformedURLException e) {

			e.printStackTrace();

		  } catch (IOException e) {

			e.printStackTrace();

		 }

		  //Example Response
		  
		//  Output from Server .... 

		  //{"status":true,"response":"Visit details updated successfully.","jsonElement":null,"jsonArray":null}

			
		
		}
}
