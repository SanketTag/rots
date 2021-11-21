package com.rots.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.sql.Time;
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
@Table(name = "rots_break_master")
public class RotsBreakMaster implements java.io.Serializable{

	private Integer id;
	private String breakName;
	private RotsShiftMaster rotsShiftMaster;
	private Date startTime;
	private Date endTime;
	private Integer totalMinues;
	private Integer activeFlag;
	private Integer createdBy;
	private Date createdDate;
	private Integer updatedBy;
	private Date updatedDate;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "break_name", nullable = true)
	public String getBreakName() {
		return breakName;
	}
	public void setBreakName(String breakName) {
		this.breakName = breakName;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "shift_id")
	public RotsShiftMaster getRotsShiftMaster() {
		return rotsShiftMaster;
	}
	public void setRotsShiftMaster(RotsShiftMaster rotsShiftMaster) {
		this.rotsShiftMaster = rotsShiftMaster;
	}
	
	
	
	
	@Column(name = "start_time", nullable = true)
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	@Column(name = "end_time", nullable = true)
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@Column(name = "total_minutes", nullable = true)
	public Integer getTotalMinues() {
		return totalMinues;
	}
	public void setTotalMinues(Integer totalMinues) {
		this.totalMinues = totalMinues;
	}
	
	@Column(name = "active_flag", nullable = true)
	public Integer getActiveFlag() {
		return activeFlag;
	}
	public void setActiveFlag(Integer activeFlag) {
		this.activeFlag = activeFlag;
	}
	
	@Column(name = "created_by", nullable = true)
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	
	@Column(name = "created_date", nullable = true)
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	@Column(name = "updated_by", nullable = true)
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	@Column(name = "updated_date", nullable = true)
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	
	
	
}
