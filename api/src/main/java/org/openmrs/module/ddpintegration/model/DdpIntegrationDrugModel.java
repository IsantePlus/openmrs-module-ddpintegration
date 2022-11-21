package org.openmrs.module.ddpintegration.model;

import java.util.Date;

public class DdpIntegrationDrugModel {
	
	int patientId;
	
	int siteCode;
	
	Date visitDate;
	
	Date createdDate;
	
	Date modifyDate;
	
	int drugId;
	
	String drugName;
	
	Date dispenseDate;
	
	int numberDay;
	
	int numberDrug;
	
	public DdpIntegrationDrugModel() {
		
	}
	
	public DdpIntegrationDrugModel(int patientId, int siteCode, Date visitDate, Date createdDate, Date modifyDate,
	    int drugId, String drugName, Date dispenseDate, int numberDay, int numberDrug) {
		super();
		this.patientId = patientId;
		this.siteCode = siteCode;
		this.visitDate = visitDate;
		this.createdDate = createdDate;
		this.modifyDate = modifyDate;
		this.drugId = drugId;
		this.drugName = drugName;
		this.dispenseDate = dispenseDate;
		this.numberDay = numberDay;
		this.numberDrug = numberDrug;
	}
	
	public int getPatientId() {
		return patientId;
	}
	
	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}
	
	public int getSiteCode() {
		return siteCode;
	}
	
	public void setSiteCode(int siteCode) {
		this.siteCode = siteCode;
	}
	
	public Date getVisitDate() {
		return visitDate;
	}
	
	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}
	
	public Date getCreatedDate() {
		return createdDate;
	}
	
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	public Date getModifyDate() {
		return modifyDate;
	}
	
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	
	public int getDrugId() {
		return drugId;
	}
	
	public void setDrugId(int drugId) {
		this.drugId = drugId;
	}
	
	public String getDrugName() {
		return drugName;
	}
	
	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}
	
	public Date getDispenseDate() {
		return dispenseDate;
	}
	
	public void setDispenseDate(Date dispenseDate) {
		this.dispenseDate = dispenseDate;
	}
	
	public int getNumberDay() {
		return numberDay;
	}
	
	public void setNumberDay(int numberDay) {
		this.numberDay = numberDay;
	}
	
	public int getNumberDrug() {
		return numberDrug;
	}
	
	public void setNumberDrug(int numberDrug) {
		this.numberDrug = numberDrug;
	}
	
}
