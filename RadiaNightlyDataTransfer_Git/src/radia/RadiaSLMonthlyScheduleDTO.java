package radia;

import java.io.Serializable;

public class RadiaSLMonthlyScheduleDTO implements Serializable {

	private static final long serialVersionUID = -139515793667696991L;
    
	 
	private String startDate;
	private String endDate;
	private String scheduleDate; 
	private Double cost;
	private boolean needUpsert;	
	private String radiaId;	
	private String placementId;
	private String placementNumber;
	private Double plannedBillableUnits; 
	
	
	
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
	public Double getCost() {
		return cost;
	}
	public void setCost(Double cost) {
		this.cost = cost;
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
	public String getPlacementId() {
		return placementId;
	}
	public void setPlacementId(String placementId) {
		this.placementId = placementId;
	}
	public String getPlacementNumber() {
		return placementNumber;
	}
	public void setPlacementNumber(String placementNumber) {
		this.placementNumber = placementNumber;
	}
	public Double getPlannedBillableUnits() {
		return plannedBillableUnits;
	}
	public void setPlannedBillableUnits(Double plannedBillableUnits) {
		this.plannedBillableUnits = plannedBillableUnits;
	}
	
	 
}

