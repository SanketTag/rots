package com.rots.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.rots.service.AdminService;
import com.rots.service.RotsAdminService;

@Component
@Scope("prototype")
public class GnxDataqueue  extends Thread{
	//private static Logger logger=Logger.getLogger("GnxDataqueue");
	private static Logger logger=Logger.getLogger("GLOBAL");
	private static Logger loggerRawPkts=Logger.getLogger("rawpktLogger");
	
	@Autowired
	private RotsAdminService adminService;
	// private List  gnxV1queue = new LinkedList();  //Original working but with warning
	private List<String> gnxV1queue = new LinkedList<String>();
	
	int count = 0; // This is to be deleted
	
	//Constructor
	public GnxDataqueue(){
		// Have database handler here initialized.
		
	}
	
	public void addDatatoQueue(String data){
		loggerRawPkts.info(data);  //Added to packet logger
		logger.info("Added to gnxV1queue" + data);
		synchronized(gnxV1queue) {
			//Sanjay updated Dec28-15 for checking if error during adding data into queue
			//After few days doesn't seem to be added in the queue and reflected further.
			
			if(gnxV1queue.add(new String(data))){
				gnxV1queue.notify();
				count ++;  // This is to be removed after testing
			}
			else 
				logger.error("GnxDataQueue:addDatatoQueue: adding to gnxV1queue throwing error");
		}		
	}

	public void run() {
		String data = "";
		System.out.println("Entered RunMethod----------------------------------------" + Calendar.getInstance().getTimeInMillis());
		while(true) {
			// Wait for data to become available
			synchronized(gnxV1queue) {
				while(gnxV1queue.isEmpty()) {
					try {
						gnxV1queue.wait();
					} catch (InterruptedException e) {
					}
				}

				data += (String) gnxV1queue.remove(0);	//changed for adding broken record
				// Get size of data to be removed
				logger.info("GnxDataqueue Run(): Size of data is ="+String.valueOf(data.length())+"Size of LinkedList(gnxV1Queue)="+ String.valueOf(gnxV1queue.size()));
				// end size of data
				
				//* Buffer Print in Numeric of ISCII we get here, first 2 and last 3 // to be removed
				int length = data.length(); //So last char is at length -1
				logger.info("GnxDataqueue Run(): char(0)="+ String.valueOf(((int)data.charAt(0)))+" char(1)=" + String.valueOf(((int)data.charAt(1))) +				
				" char(END)=" + String.valueOf(((int)data.charAt(length-1)))+ " char(End-1)=" + String.valueOf(((int)data.charAt(length-2))) + " char(End-2)=" + String.valueOf(((int)data.charAt(length-3))));
				
				 //*/
				logger.info("Received by gnxV1queue Run(): "+data);
				//Break data into multiple records, its observed some times multiple records are joined together
				//THis is GNX Specific
				int count = data.length() - data.replace("*", "").length(); // String ends like this "*\r\n"
				if(count == 0){
					logger.info("No * found, seems broken packet, check above data :"+data);
				}
				String subData; 
				int end =0;
				for (int i = 1 ; i <= count; i++){   //assuming count = no of packets in this buffer					
					//following message is to check where program goes wrong, it doesnt seems to be coming here atall
					logger.info("GnxDataqueue Run(): Packet# "+ String.valueOf(i)+" Out of " + String.valueOf(count));
					end = data.indexOf("*"); //Should provide the index first instance of "*"
					subData = data.substring(0, end+1); //charAt(end+1) will not be part of the string as per the documentation
					
					//* Buffer Print in Numeric of ISCII we get here, first 4 and last 3
					int length1 = subData.length(); //So last char (*) is at length -1
//					logger.info("GnxDataqueue Run(): Inside for loop- char(0)="+ String.valueOf(((int)subData.charAt(0)))+" char(1)=" + String.valueOf(((int)subData.charAt(1))) +				
	//						" char(END *42)=" + String.valueOf(((int)subData.charAt(length1-1)))+ " char(End+1 LF10)=" + String.valueOf(((int)subData.charAt(length1))) + " char(End+2 CR13)=" + String.valueOf(((int)subData.charAt(length1+1))));
					logger.info("GnxDataqueue Run(): Inside for loop- char(0)="+ String.valueOf(((int)subData.charAt(0)))+" char(1)=" + String.valueOf(((int)subData.charAt(1))) +				
							" char(END *42)=" + String.valueOf(((int)subData.charAt(length1-1))));
					
					 //*/

					//Parse records
					if(ifGnxValidData(subData)){
						// No subData doesn't have attached 0d0a so add
						subData = subData + '\r'+'\n';
						List<String> data1=interPreateData(subData);
						boolean isInserted = false;
						try {
							isInserted = this.adminService.insertDataIntoDB(data1);
						}catch(Exception ex) {
							logger.error("GnxDataqueue :Some Error Happened in inserting  data : "+ ex);
							return;
						}
						if(isInserted){  // to be restored
							//System.out.println("Data Stored Successfully");
						//	logger.info("GnxDataqueue :Data Stored Successfully count = " + String.valueOf(count));
						//	logger.info("Raw Packet:"+data1);    // Generate Log , comment out if log not to be generated
						}else{
							//System.out.println("Some Error Happened in interpreating data : "+data1);
							logger.info("GnxDataqueue :Some Error Happened in interpreating data : "+data1);
						}
						
					}  //If not valid packet just forget
					else{
						logger.info("GNXDataqueue:InvalidPacket : "+data);
					}	
					
					//Trim the processed packet, if it contains multiple packet as big chunk
					if (i == count) {
						if(data.length() > subData.length()+2){
							data = new String(data.substring(end+1, data.length()));
						}
						else
							data = new String("");
					}else{  //Applicable to GNX only
						//expected end = *, end+1 = 0a, end+2 = 0d, end+3 = $ in case of next record of GNX
						int n=0;
						int dlength = data.length();
						for(n=1; n < (dlength-end-1);n++){
							if(data.charAt(end+n)=='$')   //Skip until next $ or end of line
								break;
						}
						
						if((dlength-end-n) < 2){  //If reaches to end of packet, it means there is no next record exist, may be a broken record
							logger.info("GNXDataqueue:BrokenPacket : "+data.substring(end+1, data.length()));
							data="";
							break ;    //possible broken record, Break the loop.
						}else{
					//	data = data.substring(end+n, data.length());  //Working but Memory consuming
						data = new String(data.substring(end+n, data.length()));
						}

					}
						
					//Carry forward rest of the data
				//		logger.info("GNXDataqueue:end+2 =" + String.valueOf(end+2) + "  data.length()="+String.valueOf(data.length()));  //Sanjay To be removed
				//	data = data.substring(end+2, data.length()); // end = actual position of * in the buffer
				} //End for loop
			}
			//ProcessData(data);$GNX_DIO,865733022675813,112,0,204821,251015,204820,251015,1,18.677270,N,073.750676,E,1,1,0,0,0,0,0,GNX04008,$GNX_LOC,865733022675607,141,0,204929,251015,204927,251015,1,18.520577,N,073.906205,E,000,136.50,05,48,0,c,C,2,000000.6,000542.1,GNX04008,F7*
		}		
	}

	//@Scheduled(cron = "0 15 10 15 * ?", zone = "Europe/Paris") 
	/*With this configuration, Spring will schedule the annotated method to run at 10:15 AM on the 15th day of every month in Paris time. */
	
	/* The task will be executed the first time after the initialDelay value, and it will continue to be executed according to the fixedDelay. */
	//@Scheduled(fixedDelay = 1000, initialDelay = 1000)
	
	/**
	 * Business Logic
	 * Gets first message from the list,
	 * Collects individual item seperated as ',' and adds in to list as separate list item and returns this list.
	 * @param String
	 * @return
	 */
	
	private List<String> interPreateData(String dataIn) {
		    logger.info("GNXDataqueue:interPreateData :Raw Packet :"+dataIn);
		    String data;
		   // workaround arrangement for previous character as any whitespace
		    //But find out the correct way
		    System.out.println("interPreateData ->" + dataIn.length());
		    if(dataIn.charAt(4) == 'P')
		    	data = dataIn.substring(6, dataIn.length()-1); // Trading off $mtr  & last *0d0a from data

		    else 
		    	data = dataIn.substring(5, dataIn.length()-1); // Trading off $mtr  & last *0d0a from data
			/*String[] dataList = new String[14]*/; 
					/*dataList=*/
			List<String> dataList=Arrays.asList(data.split(","));
			List<String> dataList1 = new ArrayList<String>();
			dataList1.addAll(dataList);
			String imei = dataList.get(0);
			if(checkForImeiNo(imei)){
			//	dataList1.add(dto.getDate().toString());  //Insertion Date
			//	dataList1.add(String.valueOf(dto.getId())); //Insertion ID
				return dataList1;			
			}
			return null;
			//ProcessData(data);$GNX_DIO,865733022675813,112,0,204821,251015,204820,251015,1,18.677270,N,073.750676,E,1,1,0,0,0,0,0,GNX04008,$GNX_LOC,865733022675607,141,0,204929,251015,204927,251015,1,18.520577,N,073.906205,E,000,136.50,05,48,0,c,C,2,000000.6,000542.1,GNX04008,F7*
	}

	private boolean checkForImeiNo(String imei) {
		// TODO Auto-generated method stub
		return true;
	}

	private boolean ifGnxValidData(String data) {
		if(data.charAt(0)== '$'){
			//if (((data.charAt(data.length()-3)== '*')) || (data.charAt(data.length()-2)== '*') || (data.charAt(data.length()-1)== '*') ||(data.charAt(data.length())== '*')){
			if (data.charAt(data.length()-1)== '*'){
						return true;
			} else{
				//Start char '*' is missing
//				logger.error("GNXDataqueue:ifGnxValidData :End character '*' is missing\nData:"+data+"\nChar3="+data.charAt(data.length()-3)+"Char2="+data.charAt(data.length()-2)+"Char1="+data.charAt(data.length()-1));
				logger.error("GNXDataqueue:ifGnxValidData :End character '*' is missing\nData:"+data+"\nChar3="+data.charAt(data.length()-1)+"Char2="+data.charAt(data.length()-2)+"Char1="+data.charAt(data.length()-1));
				
				return false; 		
			}
		}else {
			////End character 'S' is missing
			
			if(data.length()>2){
				logger.error("GNXDataqueue:ifGnxValidData :Start char '$' is missing\nData:"+data+"\nChar0[ASCII]="+String.valueOf(((int)data.charAt(0)))+"Char1="+data.charAt(1)+"Char2="+data.charAt(2));
			}else
				logger.error("Invalid Packet"+data);
			return false;	
		}		
		
	}
	
}
