package org.openmrs.module.ddpintegration.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.openmrs.module.isanteplus.BaseOpenmrsDataObject;

@Entity
@Table(name = "interconnection_execution")
public class DdpIntegrationLastExecution extends BaseOpenmrsDataObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "last_execution_id", nullable = false)
	private Integer lastExecutionId;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "last_execution_date", nullable = false)
	private Date lastExecutionDate;
	
	//private LocalDateTime lastExecutionDate;
	
	@Column(name = "uuid", unique = true, nullable = false, insertable = false, updatable = false)
	private String lastExecutionUuid;
	
	public DdpIntegrationLastExecution() {
		
	}
	
	public DdpIntegrationLastExecution(Integer lastExecutionId, String description, Date lastExecutionDate,
	    String lastExecutionUuid) {
		super();
		this.lastExecutionId = lastExecutionId;
		this.description = description;
		this.lastExecutionDate = lastExecutionDate;
		this.lastExecutionUuid = lastExecutionUuid;
	}
	
	public Integer getLastExecutionId() {
		return lastExecutionId;
	}
	
	public void setLastExecutionId(Integer lastExecutionId) {
		this.lastExecutionId = lastExecutionId;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Date getLastExecutionDate() {
		return lastExecutionDate;
	}
	
	public void setLastExecutionDate(Date lastExecutionDate) {
		this.lastExecutionDate = lastExecutionDate;
	}
	
	public String getLastExecutionUuid() {
		return lastExecutionUuid;
	}
	
	public void setLastExecutionUuid(String lastExecutionUuid) {
		this.lastExecutionUuid = lastExecutionUuid;
	}
	
}
