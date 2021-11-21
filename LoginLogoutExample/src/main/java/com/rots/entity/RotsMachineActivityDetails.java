package com.rots.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "rots_machine_activity_details")
public class RotsMachineActivityDetails {

	private Integer id;
	private RotsProductMaster rotsProductMaster;
	private Integer machineId;
	private Integer status;
	private Integer reasonId;
	private Date startDate;
	private Date endDate;
	private Integer firstRecordProdCount;
	private Integer lastrecordProdCount;
	private Integer scrapCount;
	private Integer operatorId;
	private Integer resetCount;
	private Integer shiftId;
	private Date createdDate;
	private Integer createdBy;
	private Date updatedDate;
	private Integer updatedBy;

	public RotsMachineActivityDetails() {
	}

	public RotsMachineActivityDetails(RotsProductMaster rotsProductMaster,  Integer status, Integer reasonId,
			Date startDate, Date endDate, Integer lastrecordProdCount, Integer scrapCount, Integer operatorId,
			Date createdDate, Integer createdBy, Date updatedDate, Integer updatedBy) {
		this.rotsProductMaster = rotsProductMaster;		
		this.status = status;
		this.reasonId = reasonId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.lastrecordProdCount = lastrecordProdCount;
		this.scrapCount = scrapCount;
		this.operatorId = operatorId;
		this.createdDate = createdDate;
		this.createdBy = createdBy;
		this.updatedDate = updatedDate;
		this.updatedBy = updatedBy;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "product_id")
	public RotsProductMaster getRotsProductMaster() {
		return this.rotsProductMaster;
	}

	public void setRotsProductMaster(RotsProductMaster rotsProductMaster) {
		this.rotsProductMaster = rotsProductMaster;
	}
	
	
	@Column(name = "machine_id", nullable = true)
	public Integer getMachineId() {
		return machineId;
	}

	public void setMachineId(Integer machineId) {
		this.machineId = machineId;
	}

	@Column(name = "status", nullable = true)
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "reason_id", nullable = true)
	public Integer getReasonId() {
		return this.reasonId;
	}

	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}

	@Column(name = "start_date", nullable = true)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Column(name = "end_date", nullable = true)
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "first_record_prod_count", nullable = true)
	public Integer getFirstRecordProdCount() {
		return firstRecordProdCount;
	}

	public void setFirstRecordProdCount(Integer firstRecordProdCount) {
		this.firstRecordProdCount = firstRecordProdCount;
	}

	@Column(name = "last_record_prod_count", nullable = true)
	public Integer getLastrecordProdCount() {
		return lastrecordProdCount;
	}

	public void setLastrecordProdCount(Integer lastrecordProdCount) {
		this.lastrecordProdCount = lastrecordProdCount;
	}

	@Column(name = "scrap_count", nullable = true)
	public Integer getScrapCount() {
		return this.scrapCount;
	}

	public void setScrapCount(Integer scrapCount) {
		this.scrapCount = scrapCount;
	}

	@Column(name = "operator_id", nullable = true)
	public Integer getOperatorId() {
		return this.operatorId;
	}

	public void setOperatorId(Integer operatorId) {
		this.operatorId = operatorId;
	}


	@Column(name = "reset_count", nullable = true)
	public Integer getResetCount() {
		return resetCount;
	}

	public void setResetCount(Integer resetCount) {
		this.resetCount = resetCount;
	}
	
	@Column(name = "created_date", nullable = true)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "created_by", nullable = true)
	public Integer getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "updated_date", nullable = true)
	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Column(name = "updated_by", nullable = true)
	public Integer getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getShiftId() {
		return shiftId;
	}

	public void setShiftId(Integer shiftId) {
		this.shiftId = shiftId;
	}
	
	



}
