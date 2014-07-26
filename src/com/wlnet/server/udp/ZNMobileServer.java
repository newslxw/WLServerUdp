/**
 * 
 */
package com.wlnet.server.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;

import com.wlnet.server.buz.ZNServ;
import com.wlnet.server.utils.LogUtils;

/**服务器接收设备发过来的数据
 * 数据格式 devuuid,value 数据格式 devuuid#value value格式如下: 1,2,3,4
 * @author xinwen
 *
 */
public class ZNMobileServer implements Runnable{

	protected static Logger logger = Logger.getLogger(ZNMobileServer.class);
	private Integer mobileServerPort ;
	private ZNServ buzServ;
	private DatagramSocket serverSocket;
	
	public Integer getMobileServerPort() {
		return mobileServerPort;
	}

	public void setMobileServerPort(Integer mobileServerPort) {
		this.mobileServerPort = mobileServerPort;
	}

	public ZNServ getBuzServ() {
		return buzServ;
	}
	public void setBuzServ(ZNServ buzServ) {
		this.buzServ = buzServ;
	}
	
	/**
	 * 返回服务器是否监听状态
	 * @return
	 */
	public boolean getServerState(){
		return serverSocket!=null&&!serverSocket.isBound();
	}
	
	protected void startServer() throws SocketException{
		// 创建接收方的套接字,并制定端口号和IP地址
		if(this.getServerState()){
			serverSocket.close();
			serverSocket = null;
		}
		serverSocket = new DatagramSocket(mobileServerPort);
		LogUtils.log(logger, "znMobileServer start");
		// 创建接受类型的数据报，数据将存储在buf中
		try{
			while(true){
				try {
					byte[] buf = new byte[1024];
					DatagramPacket serverPacket = new DatagramPacket(buf, buf.length);
					serverSocket.receive(serverPacket);
					// 通过数据报得到发送方的IP和端口号，并打印
					MobileHandleThread thread = new MobileHandleThread(buzServ,serverPacket,buf);
					thread.start();
				} catch (Exception e) {
					LogUtils.log(logger, e);

				}
			}
		}finally{
			serverSocket.close();
			serverSocket = null;
			LogUtils.log(logger, "znMobileServer close");
		}
		
	}

	@Override
	public void run() {
		try {
			this.startServer();
		} catch (SocketException e) {
			LogUtils.log(logger, e);
		}finally{
			latch.countDown();
		}
	}
	
	public void setLatch(CountDownLatch latch) {
		this.latch = latch;
		
	}
	
	
	private CountDownLatch latch;
	
	public CountDownLatch getLatch() {
		return latch;
	}


	
	
}
