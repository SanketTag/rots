package com.rots.constants;

public enum ScheduledStopLevel {

	MACHINE_LEVEL(1,"Machine Level Stop"),
	COMPANY_LEVEL(2,"Company Level Stop");
	
	
	
	private Integer id;
	private String name;
	
	ScheduledStopLevel(Integer id, String name){
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	public static String getStringFromID(Integer id){
		if(id == ScheduledStopLevel.MACHINE_LEVEL.getId()){
			return ScheduledStopLevel.MACHINE_LEVEL.getName();
		}else if(id == ScheduledStopLevel.COMPANY_LEVEL.getId()){
			return ScheduledStopLevel.COMPANY_LEVEL.getName();
		}
		
		return "";
	}
	
	public static Integer getIDFromString(String name){
		if(name.equalsIgnoreCase(ScheduledStopLevel.MACHINE_LEVEL.getName())){
			return ScheduledStopLevel.MACHINE_LEVEL.getId();
		}else if(name.equalsIgnoreCase(ScheduledStopLevel.COMPANY_LEVEL.getName())){
			return ScheduledStopLevel.COMPANY_LEVEL.getId();
		}
		
		return null;
	}
	
}
