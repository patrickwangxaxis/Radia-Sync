package radia;

import java.io.Serializable;

public class RadiaPlacementMonthlyScheduleDTO implements Serializable {

	private static final long serialVersionUID = -139515793667696991L;
    
	private String startDate;
	private String endDate;
	private String scheduleDate; 
	private Double billablePlannedAmount;
	private Double plannedAmount;
	private boolean needUpsert;	
	private String radiaId;	
	private String placementNumber;
	private String packageType;
	private Double plannedUnits;
	private Double supplierUnits; 
	private Double plannedFees;
	

	
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
	public String getScheduleDate() {
		return scheduleDate;
	}
	public void setScheduleDate(String scheduleDate) {
		this.scheduleDate = scheduleDate;
	}
	 
	public boolean isNeedUpsert() {
		return needUpsert;
	}
	public void setNeedUpsert(boolean needUpsert) {
		this.needUpsert = needUpsert;
	}
	public String getRadiaId() {
		return radiaId;
	}
	public void setRadiaId(String radiaId) {
		this.radiaId = radiaId;
	}
	 
	public void setPlacementNumber(String placementNumber) {
		this.placementNumber = placementNumber;
	}
	public String getPlacementNumber() {
		return placementNumber;
	}
	 
	public Double getBillablePlannedAmount() {
		return billablePlannedAmount;
	}
	public void setBillablePlannedAmount(Double billablePlannedAmount) {
		this.billablePlannedAmount = billablePlannedAmount;
	}
	public Double getPlannedAmount() {
		return plannedAmount;
	}
	public void setPlannedAmount(Double plannedAmount) {
		this.plannedAmount = plannedAmount;
	}
	public String getPackageType() {
		return packageType;
	}
	public void setPackageType(String packageType) {
		this.packageType = packageType;
	}
	public Double getPlannedUnits() {
		return plannedUnits;
	}
	public void setPlannedUnits(Double plannedUnits) {
		this.plannedUnits = plannedUnits;
	}
	 
	public Double getPlannedFees() {
		return plannedFees;
	}
	public void setPlannedFees(Double plannedFees) {
		this.plannedFees = plannedFees;
	}
	 
	public Double getSupplierUnits() {
		return supplierUnits;
	}
	public void setSupplierUnits(Double supplierUnits) {
		this.supplierUnits = supplierUnits;
	}
	 
}


