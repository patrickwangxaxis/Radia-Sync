package radia;

import java.io.Serializable;
import java.util.List;

public class RadiaSellLineDTO implements Serializable {

	private static final long serialVersionUID = -139515793667696991L;
    
	 
	
	private String placementName;
	private String placementId;
	private String supplierName;
	private String supplierCode;
	private String lineType;
	private String mediaName;
	private String MOAdvertiserName;
	private String MOAdvertiserCode;
	private String MOProductCode;
	private String MOProductName;
	private String MOEstimateCode;
	private String MOEstimateName;
	private String radiaBuylineName;
	private String campaignId;
	private String radiaId;
	private String category;
	private String startDate;
	private String endDate;
	private String lastModifiedBy;
	private String site;
	private String costMethod;
	private String unitType;
	private Double rate;
	private String vendorIONumber;
	private String servedBy;
	private String supplierProductName;
	private String lastChangedInRadia;
	private String adserverName;
	private String packageId;
	private String packageType;
	private String selllineCreationDate;
	private String selllineCreatingUser;
	private String lineItemType;
	private String budgetLineId;
	private Double margin;
	private Double marginValue;
	private String sellLineId;
	private String buyCategory;
	private String inventoryType;
	private String targetInfo;
	private boolean needUpsert;
	
	//**************************
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
	//*************************
	
	
	private List<RadiaBuyPlacementDTO> buyplacement;
	private List<RadiaSLMonthlyScheduleDTO> slmonthlyschedule;
	 
	
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
	public String getLineType() {
		return lineType;
	}
	public void setLineType(String lineType) {
		this.lineType = lineType;
	}
	public String getMediaName() {
		return mediaName;
	}
	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
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
	public String getCampaignId() {
		return campaignId;
	}
	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
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
	public String getCostMethod() {
		return costMethod;
	}
	public void setCostMethod(String costMethod) {
		this.costMethod = costMethod;
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
	public String getLastChangedInRadia() {
		return lastChangedInRadia;
	}
	public void setLastChangedInRadia(String lastChangedInRadia) {
		this.lastChangedInRadia = lastChangedInRadia;
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
	public String getSelllineCreationDate() {
		return selllineCreationDate;
	}
	public void setSelllineCreationDate(String selllineCreationDate) {
		this.selllineCreationDate = selllineCreationDate;
	}
	public String getSelllineCreatingUser() {
		return selllineCreatingUser;
	}
	public void setSelllineCreatingUser(String selllineCreatingUser) {
		this.selllineCreatingUser = selllineCreatingUser;
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
	public String getSellLineId() {
		return sellLineId;
	}
	public void setSellLineId(String sellLineId) {
		this.sellLineId = sellLineId;
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
	 
	public boolean getNeedUpsert() {
		return needUpsert;
	}
	public void setNeedUpsert(boolean needUpsert) {
		this.needUpsert = needUpsert;
	}
	 
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public List<RadiaBuyPlacementDTO> getBuyPlacementData() {
        return buyplacement;
	}
	public void setBuyPlacementData(List<RadiaBuyPlacementDTO> buyplacement) {
        this.buyplacement = buyplacement;
	}
	public List<RadiaSLMonthlyScheduleDTO> getSlmonthlyscheduleData() {
		return slmonthlyschedule;
	}
	public void setSlmonthlyscheduleData(List<RadiaSLMonthlyScheduleDTO> slmonthlyschedule) {
		this.slmonthlyschedule = slmonthlyschedule;
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

