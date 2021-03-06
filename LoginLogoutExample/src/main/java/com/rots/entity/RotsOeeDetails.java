package com.rots.entity;
// Generated Nov 5, 2020, 2:30:46 PM by Hibernate Tools 5.4.21.Final

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * RotsOeeDetails generated by hbm2java
 */
@Entity
@Table(name = "rots_oee_details")
public class RotsOeeDetails implements java.io.Serializable {

	private Integer id;
	private Integer productId;
	private Integer machineId;
	private Integer runningTime;
	private Integer availableTime;
	private Date startTime;
	private Date endTime;
	private Integer totalCounts;
	private Integer scrapCount;
	private Double availability;
	private Double productivity;
	private Double quality;
	private Double oee;
	private Integer shiftId;
	private Date oeeDate;
	private Date systemDateTime;	
	private Integer isFinalFlag;
	private Date createdDate;
	private Integer createdBy;
	private Date updatedDate;
	private Integer updatedBy;

	public RotsOeeDetails() {
	}

	public RotsOeeDetails(Integer productId, Integer machineId, Integer runningTime, Integer availableTime,
			Date startTime, Date endTime, Integer totalCounts, Integer scrapCount, Date systemDateTime,
			Date createdDate, Integer createdBy, Date updatedDate, Integer updatedBy) {
		this.productId = productId;
		this.machineId = machineId;
		this.runningTime = runningTime;
		this.availableTime = availableTime;
		this.startTime = startTime;
		this.endTime = endTime;
		this.totalCounts = totalCounts;
		this.scrapCount = scrapCount;
		this.systemDateTime = systemDateTime;
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

	@Column(name = "product_id", nullable = true)
	public Integer getProductId() {
		return this.productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	@Column(name = "machine_id", nullable = true)
	public Integer getMachineId() {
		return this.machineId;
	}

	public void setMachineId(Integer machineId) {
		this.machineId = machineId;
	}

	@Column(name = "running_time", nullable = true)
	public Integer getRunningTime() {
		return this.runningTime;
	}

	public void setRunningTime(Integer runningTime) {
		this.runningTime = runningTime;
	}

	@Column(name = "available_time", nullable = true)
	public Integer getAvailableTime() {
		return this.availableTime;
	}

	public void setAvailableTime(Integer availableTime) {
		this.availableTime = availableTime;
	}

	@Column(name = "start_time", nullable = true)
	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Column(name = "end_time", nullable = true)
	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Column(name = "total_counts", nullable = true)
	public Integer getTotalCounts() {
		return this.totalCounts;
	}

	public void setTotalCounts(Integer totalCounts) {
		this.totalCounts = totalCounts;
	}

	@Column(name = "scrap_count", nullable = true)
	public Integer getScrapCount() {
		return this.scrapCount;
	}

	public void setScrapCount(Integer scrapCount) {
		this.scrapCount = scrapCount;
	}

	@Column(name = "system_date_time", nullable = true)
	public Date getSystemDateTime() {
		return this.systemDateTime;
	}

	public void setSystemDateTime(Date systemDateTime) {
		this.systemDateTime = systemDateTime;
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

	@Column(name = "availability", nullable = true)
	public Double getAvailability() {
		return availability;
	}

	public void setAvailability(Double availability) {
		this.availability = availability;
	}

	@Column(name = "performence", nullable = true)
	public Double getProductivity() {
		return productivity;
	}

	public void setProductivity(Double performence) {
		this.productivity = performence;
	}

	@Column(name = "quality", nullable = true)
	public Double getQuality() {
		return quality;
	}

	public void setQuality(Double quality) {
		this.quality = quality;
	}

	@Column(name = "oee", nullable = true)
	public Double getOee() {
		return oee;
	}

	public void setOee(Double oee) {
		this.oee = oee;
	}

	@Column(name = "shift_id", nullable = true)
	public Integer getShiftId() {
		return shiftId;
	}

	public void setShiftId(Integer shiftId) {
		this.shiftId = shiftId;
	}

	@Column(name = "oee_date", nullable = true)
	public Date getOeeDate() {
		return oeeDate;
	}

	public void setOeeDate(Date oeeDate) {
		this.oeeDate = oeeDate;
	}

	@Column(name = "is_final_flag", nullable = true)
	public Integer getIsFinalFlag() {
		return isFinalFlag;
	}

	public void setIsFinalFlag(Integer isFinalFlag) {
		this.isFinalFlag = isFinalFlag;
	}

	
}
