package sfdc;

import java.io.Serializable;

public class SFCompanyDto implements Serializable {

    private static final long serialVersionUID = -139515793667696991L;
    private String id;
    private String extId;
    private String Website;
    private String Type;
    private String name;
    private String Region;
    public String getId() {
          return id;
    }
    public void setId(String id) {
          this.id = id;
    }
    public String getExtId() {
        return extId;
    }
    public void setExtId(String extId) {
        this.extId = extId;
    }
    public String getWebsite() {
          return Website;
    }
    public void setWebsite(String website) {
          Website = website;
    }
    public String getType() {
          return Type;
    }
    public void setType(String type) {
          Type = type;
    }
    public String getName() {
          return name;
    }
    public void setName(String name) {
          this.name = name;
    }
    public String getRegion() {
          return Region;
    }
    public void setRegion(String region) {
          Region = region;
    }
    public String getMarket() {
          return Market;
    }
    public void setMarket(String market) {
          Market = market;
    }
    private String Market;
    
}

