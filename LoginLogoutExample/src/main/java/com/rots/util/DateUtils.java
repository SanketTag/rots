package com.rots.util;

import java.sql.Time;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.google.api.client.util.DateTime;
import com.rots.contract.DatePeriods;

public class DateUtils {

	public static String convertDateToStringWithoutTime(Date inputDate){
		Format formatter = new SimpleDateFormat("dd-MMM-yyyy");
		String s = formatter.format(inputDate);
		return s;
	}
	public static Date convertStringToDateWithoutTimeParser(String inputDate) throws ParseException{
		DateFormat formatter = new SimpleDateFormat("ddMMyy");
		Date s = formatter.parse(inputDate);
		return s;
	}
	
	public static String convertDateToStringWithTime(Date inputDate){
		Format formatter = new SimpleDateFormat("dd-MMM-yyyy hh:MM:ss a");
		String s = formatter.format(inputDate);
		return s;
	}
	
	public static String convertDateTimestampToStringWithTime(Date inputDate){
		Format formatter = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
		String s = formatter.format(inputDate);
		return s;
	}
	
	public static Date convertStringToDateWithoutTime(String inputDate) throws ParseException{
		Format formatter = new SimpleDateFormat("dd-MMM-yyyy");
		Date s = (Date) formatter.parseObject(inputDate);
		return s;
	}
	
	public static Date convertToOnlyDateWithoutTime(Date inputDate) throws ParseException{		
		Format formatter = new SimpleDateFormat("dd-MMM-yyyy");
		Date s = (Date) formatter.parseObject(convertDateToStringWithoutTime(inputDate));
		return s;
	}
	
	public static Date convertStringToDateWithTime(String inputDate) throws ParseException{
		Format formatter = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
		Date s = (Date) formatter.parseObject(inputDate);
		return s;
	}
	
	public static Date convertStringToDateWithTimezone(String inputDate) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = sdf.parse(inputDate);
		return d;
	}
	
	//Convert Time to and from Date-String
	public static String convertTimeToString(Date inputDate){
		Format formatter = new SimpleDateFormat("hh:mm:ss a");
		String s = formatter.format(inputDate);
		return s;
	}
	public static Date convertStringToTime(String inputDate) throws ParseException{
		Format formatter = new SimpleDateFormat("hh:mm:ss a");
		Date s = (Date) formatter.parseObject(inputDate);
		return s;
	}
	
	public static Date convertStringToTime24Hour(String inputDate) throws ParseException{
		Format formatter = new SimpleDateFormat("hh:mm:ss");
		Date s = (Date) formatter.parseObject(inputDate);
		return s;
	}
	
	
	public static Date addDaysToDate(Date date, int days){
		Calendar c = Calendar.getInstance();
		c.setTime(date); // Now use today date.
		c.add(Calendar.DATE, days);
		return c.getTime();
	}
	
	public static boolean isBeforeToDate(Date beforeDate, Date afterDate){
		if(null != beforeDate && null != afterDate){
			if(beforeDate.before(afterDate)){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
	
	public static boolean isAfterToDate(Date beforeDate, Date afterDate){
		if(null != beforeDate && null != afterDate){
			if(afterDate.after(beforeDate)){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
	
	public static boolean isBeforeOrEqualToDate(Date beforeDate, Date afterDate){
		if(null != beforeDate && null != afterDate){
			if(beforeDate.before(afterDate)){
				return true;
			}else if(beforeDate.equals(afterDate)){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
	
	public static boolean isAfterOrEqualTo(Date beforeDate, Date afterDate){
		if(null != beforeDate && null != afterDate){
			if(afterDate.after(beforeDate)){
				return true;
			}else if(afterDate.equals(beforeDate)){
				return true;
			}
			else{
				return false;
			}
		}
		return false;
	}
	
	public static Integer daysBetween(Date d1, Date d2){
		 return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
	}
	
	public static Long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
	    long diffInMillies = date2.getTime() - date1.getTime();
	    return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
	}
	
	
	
	 public static String convertTimeIntoDaysHrMin(Long duration, TimeUnit timeUnit)
	 {
		 String durationInDaysHrMin;
		 Long days = timeUnit.toDays(duration);
		 Long hrs = timeUnit.toHours(duration) - (days * 24);
		 Long mins = timeUnit.toMinutes(duration)- (timeUnit.toHours(duration)*60);
		 Long seconds = timeUnit.toSeconds(duration)- (timeUnit.toMinutes(duration)*60);
		 if(days > 0)
		 {
		   durationInDaysHrMin = days.toString() + " days " + hrs.toString() + " hours " + mins.toString() + " minutes";
		 }else if(hrs > 0)
		 {
			 durationInDaysHrMin = hrs.toString() + " hours " + mins.toString() + " minutes";
		 }else if(mins > 0){
			 durationInDaysHrMin = mins.toString() + " minutes " + seconds.toString() + " seconds";
		 }else {
			 durationInDaysHrMin = seconds.toString() + " seconds";
		 }
		 return durationInDaysHrMin;
		
	 }
	 
	 public static long getOverlappingPeriodInMinutes(Date a1Start, Date a1End,
			 Date b2Start, Date b2End)
		{
		 
		 long numberOfOverlappingMinutes = 0;
		 
		//Getting the default zone id
			ZoneId defaultZoneId = ZoneId.systemDefault();
				
			//Converting the date to Instant and the Date to LocalDate
			Instant a1SInstant = a1Start.toInstant();
			LocalDate aStart = a1SInstant.atZone(defaultZoneId).toLocalDate();
			
			Instant a1EInstant = a1End.toInstant();
			LocalDate aEnd = a1EInstant.atZone(defaultZoneId).toLocalDate();
			
			Instant b2SInstant = b2Start.toInstant();
			LocalDate bStart = b2SInstant.atZone(defaultZoneId).toLocalDate();
			
			Instant b2EInstant = b2End.toInstant();
			LocalDate bEnd = b2EInstant.atZone(defaultZoneId).toLocalDate();
			
			

		    if (aEnd.isBefore(aStart) || bEnd.isBefore(bStart)) {
		        System.out.println("Not proper intervals");
		    } else {
		        
		        if (aEnd.isBefore(aStart) || bEnd.isBefore(bStart)) {
		            // no overlap
		        	numberOfOverlappingMinutes = 0;
		        } else {
		            LocalDate laterStart = Collections.max(Arrays.asList(aStart, bStart));
		            LocalDate earlierEnd = Collections.min(Arrays.asList(aEnd, bEnd));
		            numberOfOverlappingMinutes = ChronoUnit.MINUTES.between(laterStart, earlierEnd);
		        }
		        System.out.println("" + numberOfOverlappingMinutes + " days of overlap");
		    }
		    
		    return numberOfOverlappingMinutes;
		}
	 
	 public static DatePeriods getOverlappingPeriod(Date a1Start, Date a1End,
			 Date b2Start, Date b2End)
		{
		 DatePeriods datePeriods = null;
		 long numberOfOverlappingMinutes = 0;
		 
		//Getting the default zone id
			ZoneId defaultZoneId = ZoneId.systemDefault();
				
			//Converting the date to Instant and the Date to LocalDate
			Instant a1SInstant = a1Start.toInstant();
			LocalDate aStart = a1SInstant.atZone(defaultZoneId).toLocalDate();
			
			Instant a1EInstant = a1End.toInstant();
			LocalDate aEnd = a1EInstant.atZone(defaultZoneId).toLocalDate();
			
			Instant b2SInstant = b2Start.toInstant();
			LocalDate bStart = b2SInstant.atZone(defaultZoneId).toLocalDate();
			
			Instant b2EInstant = b2End.toInstant();
			LocalDate bEnd = b2EInstant.atZone(defaultZoneId).toLocalDate();
			
			

		    if (aEnd.isBefore(aStart) || bEnd.isBefore(bStart)) {
		        System.out.println("Not proper intervals");
		    } else {
		        
		        if (aEnd.isBefore(aStart) || bEnd.isBefore(bStart)) {
		            // no overlap
		        	numberOfOverlappingMinutes = 0;
		        } else {
		            LocalDate laterStart = Collections.max(Arrays.asList(aStart, bStart));
		            LocalDate earlierEnd = Collections.min(Arrays.asList(aEnd, bEnd));
		            datePeriods = new DatePeriods();
		            datePeriods.setEndDate(earlierEnd);
		            datePeriods.setStartDate(laterStart);
		        }
		        System.out.println("" + numberOfOverlappingMinutes + " days of overlap");
		    }
		    
		    return datePeriods;
		}
	 
	 public static DatePeriods getOverlappingTimePeriod(Date a1Start, Date a1End,
			 Date b2Start, Date b2End)
		{
		 DatePeriods datePeriods = null;
		 long numberOfOverlappingMinutes = 0;
		 
		//Getting the default zone id
			ZoneId defaultZoneId = ZoneId.systemDefault();
				
			//Converting the date to Instant and the Date to LocalDate
			Instant a1SInstant = a1Start.toInstant();
			LocalTime aStart = a1SInstant.atZone(defaultZoneId).toLocalTime();
			
			Instant a1EInstant = a1End.toInstant();
			LocalTime aEnd = a1EInstant.atZone(defaultZoneId).toLocalTime();
			
			Instant b2SInstant = b2Start.toInstant();
			LocalTime bStart = b2SInstant.atZone(defaultZoneId).toLocalTime();
			
			Instant b2EInstant = b2End.toInstant();
			LocalTime bEnd = b2EInstant.atZone(defaultZoneId).toLocalTime();
			
			

		    if (aEnd.isBefore(aStart) || bEnd.isBefore(bStart)) {
		        System.out.println("Not proper intervals");
		    } else {
		        
		        if (aEnd.isBefore(bStart) || bEnd.isBefore(aStart)) {
		            // no overlap
		        	numberOfOverlappingMinutes = 0;
		        } else {
		            LocalTime laterStart = Collections.max(Arrays.asList(aStart, bStart));
		            LocalTime earlierEnd = Collections.min(Arrays.asList(aEnd, bEnd));
		            datePeriods = new DatePeriods();
		            datePeriods.setEndTime(earlierEnd);
		            datePeriods.setStartTime(laterStart);
		        }
		        System.out.println("" + numberOfOverlappingMinutes + " minutes of overlap");
		    }
		    
		    return datePeriods;
		}
	 
	 public static long getOverlappingLocalTimePeriod(LocalTime aStart, LocalTime aEnd,
			 LocalTime bStart, LocalTime bEnd)
		{
		 DatePeriods datePeriods = null;
		 long numberOfOverlappingSeconds = 0;
		 
		//Getting the default zone id
			ZoneId defaultZoneId = ZoneId.systemDefault();
				
			//Converting the date to Instant and the Date to LocalDate			

		    if (aEnd.isBefore(aStart) || bEnd.isBefore(bStart)) {
		        System.out.println("Not proper intervals");
		    } else {
		        
		        if (aEnd.isBefore(bStart) || bEnd.isBefore(aStart)) {
		            // no overlap
		        	numberOfOverlappingSeconds = 0;
		        } else {
		            LocalTime laterStart = Collections.max(Arrays.asList(aStart, bStart));
		            LocalTime earlierEnd = Collections.min(Arrays.asList(aEnd, bEnd));
		            numberOfOverlappingSeconds = ChronoUnit.SECONDS.between(laterStart, earlierEnd);
		        }
		        System.out.println("" + numberOfOverlappingSeconds + " seconds of overlap");
		    }
		    
		    return numberOfOverlappingSeconds;
		}
	 
	 public static Time convertTo24HourFormat(Time time) {
		 DateFormat outputformat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
		Time convertedTime = null;
		if(null != time) {
			try {
				convertedTime = (Time) outputformat.parse(outputformat.format(time));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return convertedTime;
	 }
	 
	 public static String convertToTwoDecimalPoint(Double value) {
		 DecimalFormat df2 = new DecimalFormat("#.##");
		 return df2.format(value);
	 }
}
