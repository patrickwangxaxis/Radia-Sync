package radia;

import java.io.Serializable;
import java.util.List;

public class RadiaOpportunityDTO implements Serializable {

	private static final long serialVersionUID = -139515793667696991L;
    private String name;
    private String oppId;
    public String accountId;
    private java.util.Date CloseDate;    
    private String stage;          
    private String radiaCampaignId;
    private String radiaCampaignNumber;
    
	//private String externalSource;
	//private String externalSyncNote;
	//private String externalSyncStatus;
	//private java.util.Date lastExternalPull;
	//private java.util.Date lastExternalPush;
	//private String MOClientCode;
	//private String MOEstimateCode;
	//private java.util.Date MOEstimateEndDate;
	//private java.util.Date MOEstimateStartDate;
	//private String MOProductCode;
	//private Boolean opportunityApproved;
	private String radiaAdvertiserName;
	private Double radiaBudget;
	private Boolean radiaBudgetApproved;
	private String radiaCampaignCreatedDate;
	//private java.util.Date radiaCampaignModifiedDate;
	private String radiaCampaignEndDate;
	private String radiaCampaignStartDate;
	private Boolean needUpsert;
	private List<String> parentIdList;
	private List<RadiaSellLineDTO> sellline;
	 
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOppId() {
		return oppId;
	}
	public void setOppId(String oppId) {
		this.oppId = oppId;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public java.util.Date getCloseDate() {
		return CloseDate;
	}
	public void setCloseDate(java.util.Date closeDate) {
		CloseDate = closeDate;
	}
	public String getStage() {
		return stage;
	}
	public void setStage(String stage) {
		this.stage = stage;
	}
	public String getRadiaCampaignId() {
		return radiaCampaignId;
	}
	public void setRadiaCampaignId(String radiaCampaignId) {
		this.radiaCampaignId = radiaCampaignId;
	}
	public String getRadiaCampaignNumber() {
		return radiaCampaignNumber;
	}
	public void setRadiaCampaignNumber(String radiaCampaignNumber) {
		this.radiaCampaignNumber = radiaCampaignNumber;
	}
	 /* 
	public String getExternalSource() {
		return externalSource;
	}
	public void setExternalSource(String externalSource) {
		this.externalSource = externalSource;
	}
	public String getExternalSyncNote() {
		return externalSyncNote;
	}
	public void setExternalSyncNote(String externalSyncNote) {
		this.externalSyncNote = externalSyncNote;
	}
	public String getExternalSyncStatus() {
		return externalSyncStatus;
	}
	public void setExternalSyncStatus(String externalSyncStatus) {
		this.externalSyncStatus = externalSyncStatus;
	}
	public java.util.Date getLastExternalPull() {
		return lastExternalPull;
	}
	public void setLastExternalPull(java.util.Date lastExternalPull) {
		this.lastExternalPull = lastExternalPull;
	}
	public java.util.Date getLastExternalPush() {
		return lastExternalPush;
	}
	public void setLastExternalPush(java.util.Date lastExternalPush) {
		this.lastExternalPush = lastExternalPush;
	}
	public String getMOClientCode() {
		return MOClientCode;
	}
	public void setMOClientCode(String mOClientCode) {
		MOClientCode = mOClientCode;
	}
	public String getMOEstimateCode() {
		return MOEstimateCode;
	}
	public void setMOEstimateCode(String mOEstimateCode) {
		MOEstimateCode = mOEstimateCode;
	}
	public java.util.Date getMOEstimateEndDate() {
		return MOEstimateEndDate;
	}
	public void setMOEstimateEndDate(java.util.Date mOEstimateEndDate) {
		MOEstimateEndDate = mOEstimateEndDate;
	}
	public java.util.Date getMOEstimateStartDate() {
		return MOEstimateStartDate;
	}
	public void setMOEstimateStartDate(java.util.Date mOEstimateStartDate) {
		MOEstimateStartDate = mOEstimateStartDate;
	}
	public String getMOProductCode() {
		return MOProductCode;
	}
	public void setMOProductCode(String mOProductCode) {
		MOProductCode = mOProductCode;
	}
	public Boolean getOpportunityApproved() {
		return opportunityApproved;
	}
	public void setOpportunityApproved(Boolean opportunityApproved) {
		this.opportunityApproved = opportunityApproved;
	}
	*/ 
	public String getRadiaAdvertiserName() {
		return radiaAdvertiserName;
	}
	public void setRadiaAdvertiserName(String radiaAdvertiserName) {
		this.radiaAdvertiserName = radiaAdvertiserName;
	}
	public Double getRadiaBudget() {
		return radiaBudget;
	}
	public void setRadiaBudget(Double radiaBudget) {
		this.radiaBudget = radiaBudget;
	}
	public Boolean getRadiaBudgetApproved() {
		return radiaBudgetApproved;
	}
	public void setRadiaBudgetApproved(Boolean radiaBudgetApproved) {
		this.radiaBudgetApproved = radiaBudgetApproved;
	}
	public String getRadiaCampaignCreatedDate() {
		return radiaCampaignCreatedDate;
	}
	public void setRadiaCampaignCreatedDate(String radiaCampaignCreatedDate) {
		this.radiaCampaignCreatedDate = radiaCampaignCreatedDate;
	}
	/*
	public java.util.Date getRadiaCampaignModifiedDate() {
		return radiaCampaignModifiedDate;
	}
	public void setRadiaCampaignModifiedDate(
			java.util.Date radiaCampaignModifiedDate) {
		this.radiaCampaignModifiedDate = radiaCampaignModifiedDate;
	}*/
	public String getRadiaCampaignEndDate() {
		return radiaCampaignEndDate;
	}
	public void setRadiaCampaignEndDate(String radiaCampaignEndDate) {
		this.radiaCampaignEndDate = radiaCampaignEndDate;
	}
	public String getRadiaCampaignStartDate() {
		return radiaCampaignStartDate;
	}
	public void setRadiaCampaignStartDate(String radiaCampaignStartDate) {
		this.radiaCampaignStartDate = radiaCampaignStartDate;
	}
	public Boolean getNeedUpsert() {
		return needUpsert;
	}
	public void setNeedUpsert(Boolean needUpsert) {
		this.needUpsert = needUpsert;
	}
	 
	public List<String> getParentIdList() {
        return parentIdList;
	}
	public void setParentIdList(List<String> parentIdList) {
        this.parentIdList = parentIdList;
	}
     
	public List<RadiaSellLineDTO> getSellLineData() {
        return sellline;
	}
	public void setSellLineData(List<RadiaSellLineDTO> sellline) {
        this.sellline = sellline;
	}
	
	 
}

