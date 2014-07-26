/**
 * 
 */
package com.wlnet.server.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import org.apache.log4j.Logger;

import com.wlnet.pojo.Dev;
import com.wlnet.pojo.Rev;
import com.wlnet.server.buz.ZNServ;
import com.wlnet.server.tcp.CmdMsg;
import com.wlnet.server.tcp.Const;
import com.wlnet.server.tcp.MsgQueueMgr;

/**
 * @author xinwen
 * 数据格式 devuuid#value value格式如下: 1,2,3,4
 */
public class DevHandleThread extends Thread {

	protected static Logger logger = Logger.getLogger(DevHandleThread.class);
	private ZNServ znServ;
	private DatagramPacket serverPacket;
	private byte[] buf;
	
	public DevHandleThread(ZNServ znServ,  DatagramPacket serverPacket,byte[] buf) {
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
		logger.info("DevHandleThread.run receive msg from dev ip "+sendIP+":"+sendPort+" msg:"+msg);
		String arr[] = msg.split(Const.Sym.cmd);
		if(arr.length !=3) {
			logger.error("DevHandleThread.run receive msg from dev not in format R123213#2#AA OR S#1#10.1.1");
			return;
		}
		String key = arr[0];
		if("S".equals(key)){
			Dev dev = new Dev(arr[1],sendIP.getHostAddress(), sendPort);
			Rev rev = new Rev(arr[2]);
			znServ.updateData(dev, rev);
			if("9999".equals(dev.getDevUuid())){
				try {
					ServerMsgBack.send(sendIP, sendPort, "ok");
				} catch (SocketException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else{
			CmdMsg cmdMsg = MsgQueueMgr.get(key);
			if(cmdMsg!=null){
				synchronized(cmdMsg){
					cmdMsg.setBackMsg(arr[1]+Const.Sym.cmd+arr[2]);
					cmdMsg.notifyAll();
				}
			}
		}
	}

}
