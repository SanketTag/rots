package com.rots.constants;

public enum BreakType {

	LUNCH_BREAK(1,"Lunch Break"),
	TEA_BREAK(2, "Tea Break");
	
	private Integer id;
	private String name;
	
	BreakType(Integer id, String name){
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
		if(id == BreakType.LUNCH_BREAK.getId()){
			return BreakType.LUNCH_BREAK.getName();
		}else if(id == BreakType.TEA_BREAK.getId()){
			return BreakType.TEA_BREAK.getName();
		}
		
		return "";
	}
	
	public static Integer getIDFromString(String name){
		if(name.equalsIgnoreCase(BreakType.LUNCH_BREAK.getName())){
			return BreakType.LUNCH_BREAK.getId();
		}else if(name.equalsIgnoreCase(BreakType.TEA_BREAK.getName())){
			return BreakType.TEA_BREAK.getId();
		}
		
		return null;
	}
}
