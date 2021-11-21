package com.rots.constants;

public enum ROTSConstants {

	ACTIVE(1,"Active"),
	INACTIVE(0,"In Active"),
	RESET_COUNT(1,"Reset Count"),
	ZERO(0,"Zero"),
	SYSTEM(1,"System"),
	UPDATED_RECORD(1, "Updated Record From Screen"),
	CURRENT_SHIFT(1,"CURRENT_SHIFT"),
	CURRENT_DAY(2,"CURRENT_DAY"),
	CURRENT_MONTH(3, "CURRENT_MONTH"),
	LIVE_DATA(1,"live"),
	SCRAP_DETAILS(2, "scrap");
	private Integer id;
	private String name;
	
	ROTSConstants(Integer id, String name){
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
	
	


}
