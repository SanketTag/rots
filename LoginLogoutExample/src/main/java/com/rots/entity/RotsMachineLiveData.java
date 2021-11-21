package com.rots.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rots_machine_live_data")
public class RotsMachineLiveData implements java.io.Serializable {

	private Integer id;
	private Integer machineId;
	private String productCode;
	private Date recordDateTime;
	private Integer prodCount;
	private Integer scrapCount;
	private Integer operatorId;
	private Integer status;
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
	
	@Column(name = "machine_id", nullable = true)
	public Integer getMachineId() {
		return machineId;
	}
	public void setMachineId(Integer machineId) {
		this.machineId = machineId;
	}
	
	@Column(name = "product_code", nullable = true)
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	
	
	@Column(name = "prod_count", nullable = true)
	public Integer getProdCount() {
		return prodCount;
	}
	
	
	public void setProdCount(Integer prodCount) {
		this.prodCount = prodCount;
	}
	
	@Column(name = "scrap_count", nullable = true)
	public Integer getScrapCount() {
		return scrapCount;
	}
	public void setScrapCount(Integer scrapCount) {
		this.scrapCount = scrapCount;
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

	@Column(name = "updated_by", nullable = true)
	public Integer getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
	@Column(name = "updated_date", nullable = true)
	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	@Column(name = "record_date", nullable = true)
	public Date getRecordDateTime() {
		return recordDateTime;
	}
	public void setRecordDateTime(Date recordDateTime) {
		this.recordDateTime = recordDateTime;
	}
	
	@Column(name = "operator_id", nullable = true)
	public Integer getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(Integer operatorId) {
		this.operatorId = operatorId;
	}
	
	@Column(name = "status", nullable = true)
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
	
	

}
