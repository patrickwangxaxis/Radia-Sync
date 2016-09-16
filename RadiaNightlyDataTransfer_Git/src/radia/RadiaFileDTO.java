package radia;

import java.io.Serializable;
 

public class RadiaFileDTO implements Serializable {

	private static final long serialVersionUID = -139515793667696991L;
	private String name;
    private java.util.Date fileDate;
    private String fileDateStr;    
	
   
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public java.util.Date getFileDate() {
		return fileDate;
	}
	public void setFileDate(java.util.Date fileDate) {
		this.fileDate = fileDate;
	}
	 public String getFileDateStr() {
			return fileDateStr;
	}
	public void setFileDateStr(String fileDateStr) {
			this.fileDateStr = fileDateStr;
	}
	 
}

