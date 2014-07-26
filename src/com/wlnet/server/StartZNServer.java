package com.wlnet.server;

import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;

import com.wlnet.server.tcp.dev.DevTcpServer;
import com.wlnet.server.tcp.mobile.ZNMobileTcpServer;
import com.wlnet.server.udp.ZNDevServer;
import com.wlnet.server.udp.ZNMobileServer;
import com.wlnet.server.utils.LogUtils;
import com.wlnet.server.utils.SpringUtils;

public class StartZNServer {
	protected static Logger logger = Logger.getLogger(StartZNServer.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CountDownLatch latch=new CountDownLatch(1);
		/*
		ZNMobileTcpServer tcpServer = (ZNMobileTcpServer)SpringUtils.getBean("znMobileTcpServer");
		DevTcpServer devTcpServer = (DevTcpServer)SpringUtils.getBean("devTcpServer");
		tcpServer.startServer();
		devTcpServer.startServer();
		*/
		ZNDevServer znDevServ = (ZNDevServer)SpringUtils.getBean("znDevServer");
		znDevServ.setLatch(latch);
		//ZNMobileServer znMobileServ = (ZNMobileServer)SpringUtils.getBean("znMobileServer");
		//znMobileServ.setLatch(latch);
		ZNMobileTcpServer tcpServer = (ZNMobileTcpServer)SpringUtils.getBean("znMobileTcpServer");
		Thread thread1 = new Thread(znDevServ);
		thread1.start();
		tcpServer.startServer();
		try {
			latch.await();
		} catch (InterruptedException e) {
			LogUtils.log(logger, e);
		}
	}

}
