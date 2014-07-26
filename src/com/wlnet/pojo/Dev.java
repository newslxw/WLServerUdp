package com.wlnet.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the m_dev database table.
 * 
 */
public class Dev implements Serializable {
	private static final long serialVersionUID = 1L;


	private String devMac;

	private String devName;
	private String devUuid;

	public String getDevUuid() {
		return devUuid;
	}

	public void setDevUuid(String devUuid) {
		this.devUuid = devUuid;
	}

	private String inetIp;
	private String devType;
	private String appType;
	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	private String revType;

	public String getRevType() {
		return revType;
	}

	public void setRevType(String revType) {
		this.revType = revType;
	}

	public String getDevType() {
		return devType;
	}

	public void setDevType(String devType) {
		this.devType = devType;
	}

	private Integer inetPort;

	private Short locked;

	private String lockedPhone;

	private Date lockedTime;

	private String wifiMac;
	
	private List<Rev> revList;
	
	private String measureTime;
	
	

	public String getMeasureTime() {
		return measureTime;
	}

	public void setMeasureTime(String measureTime) {
		this.measureTime = measureTime;
	}

	public List<Rev> getRevList() {
		return revList;
	}

	public void setRevList(List<Rev> revList) {
		this.revList = revList;
	}

	public Dev() {
	}


	public Dev(String devUuid, String ip, int sendPort) {
		this.devUuid = devUuid;
		this.inetIp = ip;
		this.inetPort = sendPort;
	}

	public String getDevMac() {
		return this.devMac;
	}

	public void setDevMac(String devMac) {
		this.devMac = devMac;
	}

	public String getDevName() {
		return this.devName;
	}

	public void setDevName(String devName) {
		this.devName = devName;
	}

	public String getInetIp() {
		return this.inetIp;
	}

	public void setInetIp(String inetIp) {
		this.inetIp = inetIp;
	}

	public Integer getInetPort() {
		return this.inetPort;
	}

	public void setInetPort(Integer inetPort) {
		this.inetPort = inetPort;
	}

	public Short getLocked() {
		return this.locked;
	}

	public void setLocked(Short locked) {
		this.locked = locked;
	}

	public String getLockedPhone() {
		return this.lockedPhone;
	}

	public void setLockedPhone(String lockedPhone) {
		this.lockedPhone = lockedPhone;
	}

	public Date getLockedTime() {
		return this.lockedTime;
	}

	public void setLockedTime(Date lockedTime) {
		this.lockedTime = lockedTime;
	}

	public String getWifiMac() {
		return this.wifiMac;
	}

	public void setWifiMac(String wifiMac) {
		this.wifiMac = wifiMac;
	}

}