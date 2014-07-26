package com.wlnet.server.tcp;


public class CmdMsg implements Cloneable {

	private String devUUid ;
	private String cmd ;
	private String backMsg ;
	private String errType="1";  //1 成功 -1 设备没反应 -2连接超时 -3 异常
	private boolean fromMobile=true ;
	private String key;
	
	
	
	
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public boolean isFromMobile() {
		return fromMobile;
	}

	public void setFromMobile(boolean fromMobile) {
		this.fromMobile = fromMobile;
	}

	public String getErrType() {
		return errType;
	}

	public void setErrType(String errType) {
		this.errType = errType;
	}

	public CmdMsg(){
		
	}
	
	public CmdMsg(String devUUid, String cmd, String backMsg){
		this.devUUid = devUUid;
		this.cmd = cmd;
		this.backMsg = backMsg;
	}
	
	public void setValue(String devUUid, String cmd, String backMsg){
		this.devUUid = devUUid;
		this.cmd = cmd;
		this.backMsg = backMsg;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	public String getDevUUid() {
		return devUUid;
	}

	public void setDevUUid(String devUUid) {
		this.devUUid = devUUid;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public String getBackMsg() {
		return backMsg;
	}

	public void setBackMsg(String backMsg) {
		this.backMsg = backMsg;
	}
	
	
	
	
}
