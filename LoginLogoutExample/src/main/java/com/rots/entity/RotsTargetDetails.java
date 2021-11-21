package com.rots.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "rots_target_count_details")
public class RotsTargetDetails {

	private Integer id;
	private Integer productId;
	private Integer machineId;
	private Integer shiftId;
	private Date date;
	private Integer targetCount;
	private Date systemDate;
	private Integer activeFlag;
	private Date createdDate;
	private Integer createdBy;
	private Date updatedDate;
	private Integer updatedBy;
	

	public RotsTargetDetails() {
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "product_id",  nullable = true)
	public Integer getProductId() {
		return productId;
	}


	public void setProductId(Integer productId) {
		this.productId = productId;
	}


	@Column(name = "machine_id",  nullable = true)
	public Integer getMachineId() {
		return machineId;
	}


	public void setMachineId(Integer machineId) {
		this.machineId = machineId;
	}


	@Column(name = "shift_id",  nullable = true)
	public Integer getShiftId() {
		return shiftId;
	}


	public void setShiftId(Integer shiftId) {
		this.shiftId = shiftId;
	}


	@Column(name = "date",  nullable = true)
	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}



	@Column(name = "target_count",  nullable = true)
	public Integer getTargetCount() {
		return targetCount;
	}

	public void setTargetCount(Integer targetCount) {
		this.targetCount = targetCount;
	}

	@Column(name = "system_date",  nullable = true)
	public Date getSystemDate() {
		return systemDate;
	}


	public void setSystemDate(Date systemDate) {
		this.systemDate = systemDate;
	}

	@Column(name = "active_flag",  nullable = true)
	public Integer getActiveFlag() {
		return activeFlag;
	}


	public void setActiveFlag(Integer activeFlag) {
		this.activeFlag = activeFlag;
	}
	
	@Column(name = "created_date", nullable = true)
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	@Column(name = "created_by", nullable = true)
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	
	@Column(name = "updated_date", nullable = true)
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	@Column(name = "updated_by", nullable = true)
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}


}
