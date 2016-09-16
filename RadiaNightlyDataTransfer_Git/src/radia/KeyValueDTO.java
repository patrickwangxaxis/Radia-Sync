package radia;

import java.io.Serializable;
 

public class KeyValueDTO implements Serializable {

	private static final long serialVersionUID = -139515793667696991L;
	private String radiaId;
	private String key;
    private String value;    
	
    public String getRadiaId() {
		return radiaId;
	}
	public void setRadiaId(String radiaId) {
		this.radiaId = radiaId;
	}
    public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
 
}