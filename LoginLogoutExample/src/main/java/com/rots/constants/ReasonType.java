package com.rots.constants;

public enum ReasonType {


	PLANNED(1, "Planned"),
	UNPLANNED(2, "Unplanned");
	
	
	private Integer id;
	private String name;
	
	ReasonType(Integer id, String name){
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
		if(id == ReasonType.PLANNED.getId()){
			return ReasonType.PLANNED.getName();
		}else if(id == ReasonType.UNPLANNED.getId()){
			return ReasonType.UNPLANNED.getName();
		}
		
		return "";
	}
	
	public static Integer getIDFromString(String name){
		if(name.equalsIgnoreCase(ReasonType.PLANNED.getName())){
			return ReasonType.PLANNED.getId();
		}else if(name.equalsIgnoreCase(ReasonType.UNPLANNED.getName())){
			return ReasonType.UNPLANNED.getId();
		}
		
		return null;
	}

}
