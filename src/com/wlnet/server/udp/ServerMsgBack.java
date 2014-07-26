package com.wlnet.server.udp;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.wlnet.pojo.Dev;
import com.wlnet.server.buz.ZNServ;
import com.wlnet.server.tcp.CmdMsg;
import com.wlnet.server.tcp.Const;
import com.wlnet.server.tcp.MsgQueueMgr;
import com.wlnet.server.utils.LogUtils;
import com.wlnet.server.utils.SpringUtils;

/**
 * 发回数据给客户端
 * @author xinwen
 *
 */
public class ServerMsgBack {
	protected static Logger logger = Logger.getLogger(ServerMsgBack.class);
	public static void send(InetAddress ip,Integer port, String msg) throws SocketException {
		//DatagramSocket sendSocket = new DatagramSocket();
		ZNDevServer znDevServ = (ZNDevServer)SpringUtils.getBean("znDevServer");
		try {
			// 创建发送方的套接字，IP默认为本地，端口号随机
			byte[] buf = msg.getBytes("UTF-8");
			// 确定发送方的IP地址及端口号，地址为本地机器地址
			DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, ip,port);
			for(int i=0; i<10; i++){
				znDevServ.getServerSocket().send(sendPacket);
			}
			logger.info("ServerMsgBack.send send msg["+msg+"] to "+ip.toString()+":"+port);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtils.log(logger, e);
		}finally{
			//sendSocket.close();
		}

	}
	
	public static void send(InetAddress ip,Integer port, byte[] buf) throws SocketException {
		ZNDevServer znDevServ = (ZNDevServer)SpringUtils.getBean("znDevServer");
		DatagramSocket sendSocket = znDevServ.getServerSocket();
		try {
			String msg = new String(buf);
			// 创建发送方的套接字，IP默认为本地，端口号随机
			// 确定发送方的IP地址及端口号，地址为本地机器地址
			DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, ip,port);
			sendSocket.send(sendPacket);
			logger.info("send msg["+msg+"] to "+ip.toString()+":"+port);
		} catch (Exception e) {
			LogUtils.log(logger, e);
		}finally{
			//sendSocket.close();
		}

	}
	
	public static CmdMsg handMsg(byte[] buf, String devUUid){
		String cmd = new String(buf);
		CmdMsg msg = new CmdMsg(devUUid, cmd, null);
		BlockingQueue<CmdMsg> queue = MsgQueueMgr.getQueue(devUUid);
		try {
			synchronized(queue){
				boolean bin = queue.offer(msg, 100, TimeUnit.MILLISECONDS);
				logger.error("put: queue num="+queue.size());
			}
		} catch (InterruptedException e1) {
			msg.setErrType("-2");
			LogUtils.log(logger, e1);
			return msg;
		}
		synchronized(msg){
			try {
				msg.wait();
			} catch (InterruptedException e) {
				msg.setErrType("-1");
				LogUtils.log(logger, e);
			}finally{
			//	queue.remove(msg);
			}
		}
		return msg;
	}
	
	public static CmdMsg sendWithBack(InetAddress ip,Integer port, byte[] buf, Dev dev) throws SocketException {
		String cmd = new String(buf);
		CmdMsg cmdMsg = new CmdMsg(dev.getDevUuid(), cmd, null);
		String key = MsgQueueMgr.put(cmdMsg);
		ZNDevServer znDevServ = (ZNDevServer)SpringUtils.getBean("znDevServer");
		DatagramSocket sendSocket = znDevServ.getServerSocket();
		try {
			String newCmd = key+Const.Sym.cmd+cmd;
			byte buf2[]=newCmd.getBytes("UTF-8");
			DatagramPacket sendPacket = new DatagramPacket(buf2, buf2.length, ip,port);
			int tryCount = 0;
			int maxTry=3;
			while(tryCount<maxTry){
				try{
					tryCount++;
					sendSocket.send(sendPacket);
					logger.info("sendWithBack tryCount:"+tryCount+" send msg["+newCmd+"] to chazuo dev "+ip.toString()+":"+port);
					synchronized(cmdMsg){
						cmdMsg.wait(10000);
					}
					logger.info("sendWithBack tryCount:"+tryCount+" receive msg "+cmdMsg.getBackMsg()+" from  chazuo dev "+ip.toString()+":"+port);
					break;
				}catch(SocketTimeoutException e){
					logger.error("send msg to dev and receive reply from dev timeout "+tryCount);
				}
			}
		}catch (Exception e) {
			LogUtils.log(logger, e);
		}finally{
			MsgQueueMgr.remove(key);
		}
		return cmdMsg;

	}
	
}
