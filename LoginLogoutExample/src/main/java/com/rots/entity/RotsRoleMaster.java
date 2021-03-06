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
 * RotsRoleMaster generated by hbm2java
 */
@Entity
@Table(name = "rots_role_master")
public class RotsRoleMaster implements java.io.Serializable {

	private Integer id;
	private String roleName;
	private Date createdDate;
	private String createdBy;
	private Date updatedDate;
	private String updatedBy;

	public RotsRoleMaster() {
	}

	public RotsRoleMaster(String roleName) {
		this.roleName = roleName;
	}

	public RotsRoleMaster(String roleName, Date createdDate, String createdBy, Date updatedDate, String updatedBy) {
		this.roleName = roleName;
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

	@Column(name = "role_name", nullable = false)
	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Column(name = "created_date", nullable = true)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "created_by", nullable = true)
	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
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
	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

}
