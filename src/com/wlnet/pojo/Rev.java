package com.wlnet.pojo;

import java.io.Serializable;
import java.util.List;


/**
 * The persistent class for the m_rev database table.
 * 
 */
public class Rev implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long revId;

	private String devUuid;

	public String getDevUuid() {
		return devUuid;
	}

	public void setDevUuid(String devUuid) {
		this.devUuid = devUuid;
	}

	private String measureValue;

	private String revTime;

	public Rev() {
	}

	public Rev(String measureValue) {
		this.measureValue = measureValue;
	}
	
	public Long getRevId() {
		return this.revId;
	}

	public void setRevId(Long revId) {
		this.revId = revId;
	}


	public String getMeasureValue() {
		return this.measureValue;
	}

	public void setMeasureValue(String measureValue) {
		this.measureValue = measureValue;
	}

	public String getRevTime() {
		return this.revTime;
	}

	public void setRevTime(String revTime) {
		this.revTime = revTime;
	}

}