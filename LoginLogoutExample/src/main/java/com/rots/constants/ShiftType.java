package com.rots.constants;

public enum ShiftType {

	SHIFT_1(1, "1st Shift"),
	SHIFT_2(2, "2nd Shift"),
	SHIFT_3(3, "3rd Shift");
	
	
	private Integer id;
	private String name;
	
	ShiftType(Integer id, String name){
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
		if(id == ShiftType.SHIFT_1.getId()){
			return ShiftType.SHIFT_1.getName();
		}else if(id == ShiftType.SHIFT_2.getId()){
			return ShiftType.SHIFT_2.getName();
		}else if(id == ShiftType.SHIFT_3.getId()){
			return ShiftType.SHIFT_3.getName();
		}
		
		return "";
	}
	
	public static Integer getIDFromString(String name){
		if(name.equalsIgnoreCase(ShiftType.SHIFT_1.getName())){
			return ShiftType.SHIFT_1.getId();
		}else if(name.equalsIgnoreCase(ShiftType.SHIFT_2.getName())){
			return ShiftType.SHIFT_2.getId();
		}else if(name.equalsIgnoreCase(ShiftType.SHIFT_3.getName())){
			return ShiftType.SHIFT_3.getId();
		}
		
		return null;
	}
}
