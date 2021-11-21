package com.rots.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "rots_scheduled_stop_tran_dtls")
public class RotsScheduledStopTranDtls implements java.io.Serializable{

	private Integer id;
	private RotsReasonMaster reasonMaster;
	private Integer stopLevel;
	private RotsMachineMaster rotsMachineMaster;
	private String reasonDesc;
	private Date startDate;
	private Date endDate;
	private Integer activeFlag;
	private Date createdDate;
	private Integer createdBy;
	private Date updatedDate;
	private Integer updatedBy;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "reason_id")
	public RotsReasonMaster getReasonMaster() {
		return reasonMaster;
	}
	public void setReasonMaster(RotsReasonMaster reasonMaster) {
		this.reasonMaster = reasonMaster;
	}
	
	
	@Column(name = "stop_level", nullable = true)
	public Integer getStopLevel() {
		return stopLevel;
	}
	public void setStopLevel(Integer stopLevel) {
		this.stopLevel = stopLevel;
	}
	
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "machine_id")
	public RotsMachineMaster getRotsMachineMaster() {
		return rotsMachineMaster;
	}
	public void setRotsMachineMaster(RotsMachineMaster rotsMachineMaster) {
		this.rotsMachineMaster = rotsMachineMaster;
	}
	
	@Column(name = "reason_desc", nullable = true)
	public String getReasonDesc() {
		return reasonDesc;
	}
	public void setReasonDesc(String reasonDesc) {
		this.reasonDesc = reasonDesc;
	}
	
	@Column(name = "start_date", nullable = true)
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@Column(name = "end_date", nullable = true)
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	@Column(name = "active_flag", nullable = true)
	public Integer getActiveFlag() {
		return activeFlag;
	}
	public void setActiveFlag(Integer activeFlag) {
		this.activeFlag = activeFlag;
	}
	
	@Column(name = "ceated_date", nullable = true)
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	@Column(name = "crteated_by", nullable = true)
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
