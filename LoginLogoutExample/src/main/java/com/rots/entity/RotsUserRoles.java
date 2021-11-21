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
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "rots_user_roles")
public class RotsUserRoles  implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer userRoleId;
	//private RlmsCompanyRoleMap rlmsCompanyRolMap;
	private RotsUserMaster rotsUserMaster;
    private RotsBranchMaster rotsBranchMaster;
	private RotsSpocRoleM rotsSpocRoleM;
	private String username;
	private String role;
	private Integer activeFlag;
	private Date createdDate;
	private Integer createdBy;
	private Date updatedDate;
	private Integer updatedBy;
	
	
	public RotsUserRoles() {
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "user_role_id", unique = true, nullable = false)
	public Integer getUserRoleId() {
		return userRoleId;
	}

	
	public void setUserRoleId(Integer userRoleId) {
		this.userRoleId = userRoleId;
	}

	

	

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "user_id")
	public RotsUserMaster getRotsUserMaster() {
		return rotsUserMaster;
	}

	public void setRotsUserMaster(RotsUserMaster rotsUserMaster) {
		this.rotsUserMaster = rotsUserMaster;
	}

	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "branch_id")
	public RotsBranchMaster getRotsBranchMaster() {
		return rotsBranchMaster;
	}

	public void setRotsBranchMaster(RotsBranchMaster rotsBranchMaster) {
		this.rotsBranchMaster = rotsBranchMaster;
	}

	@Column(name = "active_flag", unique = true, nullable = false)
	public Integer getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(Integer activeFlag) {
		this.activeFlag = activeFlag;
	}

	@Column(name = "created_date", unique = true, nullable = false)
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "created_by", unique = true, nullable = false)
	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "updated_date", unique = true, nullable = false)
	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Column(name = "updated_by", unique = true, nullable = false)
	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Column(name = "username", unique = true, nullable = true)
	public String getUsername() {
		return username;
	}

	public void setUsername(String userName) {
		this.username = userName;
	}

	@Column(name = "role", unique = true, nullable = true)
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "spoc_role_id")
	public RotsSpocRoleM getRotsSpocRoleM() {
		return rotsSpocRoleM;
	}

	public void setRotsSpocRoleM(RotsSpocRoleM rotsSpocRoleM) {
		this.rotsSpocRoleM = rotsSpocRoleM;
	}



}
