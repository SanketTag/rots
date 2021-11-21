package com.rots.constants;

public enum RotsErrorType {
	UNNKOWN_EXCEPTION_OCCHURS(1,"unknown_exception_occur"),
	REASON_TITLE_EMPTY(2,"reason_title_empty"),
	INVALID_REASON_ID(3,"invalid_reason_id"),
	INVALID_MACHINE_ID(4,"invalid_machine_id"),
	INVALID_SCHEDULEDSTOPDTLS(5, "invalid_scheduledstopdtls"),
	INVALID_STOPPAGE_ID(6,"invalid_stoppage_id"),
	INVALID_SHIFT_NUMBER(7, "invalid_shift_number"),
	INVLAID_SHIFT_START_TIME(8,"invlaid_shift_start_time"),
	INVLAID_SHIFT_END_TIME(9,"invlaid_shift_end_time"),
	INVALID_BREAK_TYPE(10, "invalid_break_type"),
	INVALID_BREAK_START_TIME(11,"invlaid_break_start_time"),
	INVALID_BREAK_END_TIME(12,"invlaid_break_end_time"),
	EMPTY_BREAK_DETAILS(13,"empty_break_details"),
	INVALID_SHIFT(14,"invalid_shift"),
	INVALID_BREAK(15,"invalid_break"),
	INVALID_TOKEN(16,"invalid_token"),
	OVERLAPPING_SHIFT(16,"overlapping_shift"),
	DUPLICATE_REASON_TITLE(17, "duplicate_reason_title"),
	INCOMPLETE_DETAILS(404,"invalid_reason"),
	INVALID_DATE(19,"invalid_date"),
	INVALID_PRODUCT_ID(20,"invalid_product_id"),
	INVALID_TARGET_COUNT(21, "invalid_target_count"),
	INVALID_SCRAP_COUNT(21, "invalid_scrap_count"),
	INVALID_TARGET_ID(22,"invalid_target_id");
	private Integer code;
	private String message;
	private RotsErrorType(int code, String message){
		this.code = code;
		this.message = message;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
