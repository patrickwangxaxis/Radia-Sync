package radia;

import java.io.Serializable;
import java.util.List;

public class RadiaBuyPlacementDTO implements Serializable {

	private static final long serialVersionUID = -139515793667696991L;
    
	 
	
	private String placementName;
	private String placementId;
	private String supplierName;
	private String supplierCode;
	
	 
	private String MOAdvertiserName;
	private String MOAdvertiserCode;
	private String MOProductCode;
	private String MOProductName;
	private String MOEstimateCode;
	private String MOEstimateName;
	private String radiaBuylineName;
	
	private String radiaId;
	 
	private String startDate;
	private String endDate;
	private String lastModifiedBy;
	private String site;
	private String creativeSize;
	private String positioning;
	 
	private String unitType;
	private Double rate;
	private String vendorIONumber;
	private String servedBy;
	private String supplierProductName;
	//private String lastChangedInRadia; 
	private String adserverName;
	private String packageId;
	private String packageType;
	private String buylineCreationDate;
	private String buylineCreatingUser;
	private String lineItemType;
	private String budgetLineId;
	private Double margin;
	private Double marginValue;
	private String comments;
	private int plannedUnits;
	private Double plannedCost;
	private int actualUnits;
	private Double actualCost;
	private String agencyPlacement;
	private String buyPlacementId;
	private boolean needUpsert;
	private String lastChangedInRadia; 
	private String partnershipEngagementAcctId;
	private String costMethod;
	private String buyCategory;
	private String inventoryType;
	private String targetInfo;
	private String instanceName;
	private String creativeTypeDescription;
	private String allCreativeSize;
	
	//******************************
	private String adLabs;
	private String agencyBillingPlatform;
	private String channel;
	private String creativeDetail;
	private String creativeFormat;
	private String dataTactic;
	private String device;
	private String inventory;
	private String networkBillingPlatform;
	private String primaryKpi;
	private String productCode;
	//******************************
	 
	private List<RadiaPlacementMonthlyScheduleDTO> placementmonthlyschedule;
	 
	 
	
	
	public String getPlacementName() {
		return placementName;
	}
	public void setPlacementName(String placementName) {
		this.placementName = placementName;
	}
	public String getPlacementId() {
		return placementId;
	}
	public void setPlacementId(String placementId) {
		this.placementId = placementId;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	 
	 
	public String getMOAdvertiserName() {
		return MOAdvertiserName;
	}
	public void setMOAdvertiserName(String mOAdvertiserName) {
		MOAdvertiserName = mOAdvertiserName;
	}
	public String getMOAdvertiserCode() {
		return MOAdvertiserCode;
	}
	public void setMOAdvertiserCode(String mOAdvertiserCode) {
		MOAdvertiserCode = mOAdvertiserCode;
	}
	public String getMOProductCode() {
		return MOProductCode;
	}
	public void setMOProductCode(String mOProductCode) {
		MOProductCode = mOProductCode;
	}
	public String getMOProductName() {
		return MOProductName;
	}
	public void setMOProductName(String mOProductName) {
		MOProductName = mOProductName;
	}
	public String getMOEstimateCode() {
		return MOEstimateCode;
	}
	public void setMOEstimateCode(String mOEstimateCode) {
		MOEstimateCode = mOEstimateCode;
	}
	public String getMOEstimateName() {
		return MOEstimateName;
	}
	public void setMOEstimateName(String mOEstimateName) {
		MOEstimateName = mOEstimateName;
	}
	public String getRadiaBuylineName() {
		return radiaBuylineName;
	}
	public void setRadiaBuylineName(String radiaBuylineName) {
		this.radiaBuylineName = radiaBuylineName;
	}
	public String getRadiaId() {
		return radiaId;
	}
	public void setRadiaId(String radiaId) {
		this.radiaId = radiaId;
	}
	 
	 
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	 
	public String getUnitType() {
		return unitType;
	}
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public String getVendorIONumber() {
		return vendorIONumber;
	}
	public void setVendorIONumber(String vendorIONumber) {
		this.vendorIONumber = vendorIONumber;
	}
	public String getServedBy() {
		return servedBy;
	}
	public void setServedBy(String servedBy) {
		this.servedBy = servedBy;
	}
	public String getSupplierProductName() {
		return supplierProductName;
	}
	public void setSupplierProductName(String supplierProductName) {
		this.supplierProductName = supplierProductName;
	}
	 
	public String getAdserverName() {
		return adserverName;
	}
	public void setAdserverName(String adserverName) {
		this.adserverName = adserverName;
	}
	public String getPackageId() {
		return packageId;
	}
	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}
	public String getPackageType() {
		return packageType;
	}
	public void setPackageType(String packageType) {
		this.packageType = packageType;
	}
	public String getBuylineCreationDate() {
		return buylineCreationDate;
	}
	public void setBuylineCreationDate(String selllineCreationDate) {
		this.buylineCreationDate = selllineCreationDate;
	}
	public String getBuylineCreatingUser() {
		return buylineCreatingUser;
	}
	public void setBuylineCreatingUser(String selllineCreatingUser) {
		this.buylineCreatingUser = selllineCreatingUser;
	}
	public String getLineItemType() {
		return lineItemType;
	}
	public void setLineItemType(String lineItemType) {
		this.lineItemType = lineItemType;
	}
	public String getBudgetLineId() {
		return budgetLineId;
	}
	public void setBudgetLineId(String budgetLineId) {
		this.budgetLineId = budgetLineId;
	}
	public Double getMargin() {
		return margin;
	}
	public void setMargin(Double margin) {
		this.margin = margin;
	}
	public Double getMarginValue() {
		return marginValue;
	}
	public void setMarginValue(Double marginValue) {
		this.marginValue = marginValue;
	}
	public String getCreativeSize() {
		return creativeSize;
	}
	public void setCreativeSize(String creativeSize) {
		this.creativeSize = creativeSize;
	}
	public String getPositioning() {
		return positioning;
	}
	public void setPositioning(String positioning) {
		this.positioning = positioning;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public int getPlannedUnits() {
		return plannedUnits;
	}
	public void setPlannedUnits(int plannedUnits) {
		this.plannedUnits = plannedUnits;
	}
	public Double getPlannedCost() {
		return plannedCost;
	}
	public void setPlannedCost(Double plannedCost) {
		this.plannedCost = plannedCost;
	}
	public int getActualUnits() {
		return actualUnits;
	}
	public void setActualUnits(int actualUnits) {
		this.actualUnits = actualUnits;
	}
	public Double getActualCost() {
		return actualCost;
	}
	public void setActualCost(Double actualCost) {
		this.actualCost = actualCost;
	}
	public String getAgencyPlacement() {
		return agencyPlacement;
	}
	public void setAgencyPlacement(String agencyPlacement) {
		this.agencyPlacement = agencyPlacement;
	}
	public String getBuyPlacementId() {
		return buyPlacementId;
	}
	public void setBuyPlacementId(String buyPlacementId) {
		this.buyPlacementId = buyPlacementId;
	}
	public boolean getNeedUpsert() {
		return needUpsert;
	}
	public void setNeedUpsert(boolean needUpsert) {
		this.needUpsert = needUpsert;
	}
	 
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getLastChangedInRadia() {
		return lastChangedInRadia;
	}
	public void setLastChangedInRadia(String lastChangedInRadia) {
		this.lastChangedInRadia = lastChangedInRadia;
	}
	public String getPartnershipEngagementAcctId() {
		return partnershipEngagementAcctId;
	}
	public void setPartnershipEngagementAcctId(String partnershipEngagementAcctId) {
		this.partnershipEngagementAcctId = partnershipEngagementAcctId;
	}
	public String getCostMethod() {
		return costMethod;
	}
	public void setCostMethod(String costMethod) {
		this.costMethod = costMethod;
	}
	public String getBuyCategory() {
		return buyCategory;
	}
	public void setBuyCategory(String buyCategory) {
		this.buyCategory = buyCategory;
	}
	 
	public String getInventoryType() {
		return inventoryType;
	}
	public void setInventoryType(String inventoryType) {
		this.inventoryType = inventoryType;
	}
	public String getTargetInfo() {
		return targetInfo;
	}
	public void setTargetInfo(String targetInfo) {
		this.targetInfo = targetInfo;
	}
	public String getInstanceName() {
		return instanceName;
	}
	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}
	
	public String getCreativeTypeDescription() {
		return creativeTypeDescription;
	}
	public void setCreativeTypeDescription(String creativeTypeDescription) {
		this.creativeTypeDescription = creativeTypeDescription;
	}
	public String getAllCreativeSize() {
		return allCreativeSize;
	}
	public void setAllCreativeSize(String allCreativeSize) {
		this.allCreativeSize = allCreativeSize;
	}
	 
	public List<RadiaPlacementMonthlyScheduleDTO> getPlacementmonthlyscheduleData() {
		return placementmonthlyschedule;
	}
	public void setPlacementmonthlyscheduleData(List<RadiaPlacementMonthlyScheduleDTO> placementmonthlyschedule) {
		this.placementmonthlyschedule = placementmonthlyschedule;
	}
	
	public String getAdLabs() {
		return adLabs;
	}
	public void setAdLabs(String adLabs) {
		this.adLabs = adLabs;
	}
	public String getAgencyBillingPlatform() {
		return agencyBillingPlatform;
	}
	public void setAgencyBillingPlatform(String agencyBillingPlatform) {
		this.agencyBillingPlatform = agencyBillingPlatform;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getCreativeDetail() {
		return creativeDetail;
	}
	public void setCreativeDetail(String creativeDetail) {
		this.creativeDetail = creativeDetail;
	}
	public String getCreativeFormat() {
		return creativeFormat;
	}
	public void setCreativeFormat(String creativeFormat) {
		this.creativeFormat = creativeFormat;
	}
	public String getDataTactic() {
		return dataTactic;
	}
	public void setDataTactic(String dataTactic) {
		this.dataTactic = dataTactic;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public String getInventory() {
		return inventory;
	}
	public void setInventory(String inventory) {
		this.inventory = inventory;
	}
	public String getNetworkBillingPlatform() {
		return networkBillingPlatform;
	}
	public void setNetworkBillingPlatform(String networkBillingPlatform) {
		this.networkBillingPlatform = networkBillingPlatform;
	}
	public String getPrimaryKpi() {
		return primaryKpi;
	}
	public void setPrimaryKpi(String primaryKpi) {
		this.primaryKpi = primaryKpi;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	
}

