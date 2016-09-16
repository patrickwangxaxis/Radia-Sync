package sfdc;

import java.io.Serializable;
import java.util.List;

import radia.RadiaOpportunityDTO;

public class SFBasicRequestDto implements Serializable {
                private static final long serialVersionUID = 916834276156457905L;
                //private String apiapp;
    private List<RadiaOpportunityDTO> inf;
      //private RadiaOpportunityDTO inf;
    			/*
                public String getApiapp() {
                                return apiapp;
                }
                public void setApiapp(String apiapp) {
                                this.apiapp = apiapp;
                }
                */
      			
                public List<RadiaOpportunityDTO> getOppData() {
                                return inf;
                }
                public void setOppData(List<RadiaOpportunityDTO> data) {
                                inf = data;
                }
      			 
      			/*
      			public RadiaOpportunityDTO getOppData() {
      					return inf;
      				}
      			public void setOppData(RadiaOpportunityDTO data) {
      				inf = data;
                
      			}*/
}
				

