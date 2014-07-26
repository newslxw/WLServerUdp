/**
 * 
 */
package com.wlnet.server.udp;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.List;

import org.apache.log4j.Logger;

import com.wlnet.pojo.Dev;
import com.wlnet.pojo.Rev;
import com.wlnet.server.buz.ZNServ;
import com.wlnet.server.tcp.Const;
import com.wlnet.server.utils.LogUtils;
import com.wlnet.server.utils.SpringUtils;

/**
 * 处理手机请求
 * @author xinwen
 * 数据格式 function#devuuid#value value格式如下: 1,2,3,4
 */
public class MobileHandleThread extends Thread {
	protected static Logger logger = Logger.getLogger(MobileHandleThread.class);
	private ZNServ znServ;
	private DatagramPacket serverPacket;
	private byte[] buf;
	public MobileHandleThread(ZNServ znServ, DatagramPacket serverPacket,byte[] buf) {
	
		this.znServ = znServ;
		this.serverPacket = serverPacket;
		this.buf = buf;
		
	}

	@Override
	public void run() {
		if(serverPacket.getLength()==0) return;
		InetAddress sendIP = serverPacket.getAddress();
		int sendPort = serverPacket.getPort();
		String msg = new String(buf, 0, serverPacket.getLength());
		String arr[] = msg.split(Const.Sym.cmd);
		
		String requestType = arr[0].toString();
		logger.info("receive mobile ip:["+ sendIP.toString() +"] port:["+sendPort+"] requestType:"+requestType);
		if("devlist".equals(requestType)){
			List<Dev> list = znServ.getDevList();
			StringBuffer sb = new StringBuffer();
			/**
			 * 设备列表格式 uuid`name`type`ip`port[uuid`name`type`ip`port
			 */
			Dev tmp = null;
			for(int i=0; i<list.size(); i++){
				tmp = list.get(i);
				/**
				 * TODO:需要优化，多个设备是可能被切成多个包
				 */
				if(i>0) sb.append(Const.Sym.obj);
				sb.append(tmp.getDevUuid()).append(Const.Sym.prop).append(tmp.getDevName()).append(Const.Sym.prop)
				.append(tmp.getDevType()==null?" ":tmp.getDevType()).append(Const.Sym.prop)
				.append(tmp.getInetIp()==null?" ":tmp.getInetIp()).append(Const.Sym.prop)
				.append(tmp.getInetPort()==null?" ":tmp.getInetPort()).append(Const.Sym.prop);
			}
			try {
				ServerMsgBack.send(sendIP, sendPort, sb.toString());
			} catch (SocketException e) {
				LogUtils.log(logger, e);
			}
		}else if("lastrev".equals(requestType)){
			Dev dev = new Dev();
			String backmsg="";
			if(arr.length > 1) {
				dev.setDevUuid(arr[1]);
				Rev rev = znServ.getLastRev(dev);
				backmsg = rev.getDevUuid()+Const.Sym.prop+rev.getMeasureValue()+Const.Sym.prop+rev.getRevTime();
			}
			try {
				ServerMsgBack.send(sendIP, sendPort, backmsg);
			} catch (SocketException e) {
				LogUtils.log(logger, e);
			}
		}else{
		}
		
	}

}
