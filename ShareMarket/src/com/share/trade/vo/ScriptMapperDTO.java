package com.share.trade.vo;

import java.util.Date;


public class ScriptMapperDTO {
	
		private Long scriptMapperkey;
		
		private String wtcherScript;
		
		private String brokerScript;
		
		private String isActive;
		
		private Date updatedDate;

		public Long getScriptMapperkey() {
			return scriptMapperkey;
		}

		public void setScriptMapperkey(Long scriptMapperkey) {
			this.scriptMapperkey = scriptMapperkey;
		}

		public String getWtcherScript() {
			return wtcherScript;
		}

		public void setWtcherScript(String wtcherScript) {
			this.wtcherScript = wtcherScript;
		}

		public String getBrokerScript() {
			return brokerScript;
		}

		public void setBrokerScript(String brokerScript) {
			this.brokerScript = brokerScript;
		}

		public String getIsActive() {
			return isActive;
		}

		public void setIsActive(String isActive) {
			this.isActive = isActive;
		}

		public Date getUpdatedDate() {
			return updatedDate;
		}

		public void setUpdatedDate(Date updatedDate) {
			this.updatedDate = updatedDate;
		}
		
		

}
