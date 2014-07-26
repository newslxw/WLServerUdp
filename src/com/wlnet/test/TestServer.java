package com.wlnet.test;

import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;

import com.wlnet.server.udp.ZNDevServer;
import com.wlnet.server.udp.ZNMobileServer;
import com.wlnet.server.utils.LogUtils;
import com.wlnet.server.utils.SpringUtils;

public class TestServer {
	protected static Logger logger = Logger.getLogger(TestServer.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CountDownLatch latch=new CountDownLatch(2);
		ZNDevServer znDevServ = (ZNDevServer)SpringUtils.getBean("znDevServer");
		znDevServ.setLatch(latch);
		ZNMobileServer znMobileServ = (ZNMobileServer)SpringUtils.getBean("znMobileServer");
		znMobileServ.setLatch(latch);
		Thread thread1 = new Thread(znDevServ);
		Thread thread2 = new Thread(znMobileServ);
		thread1.start();
		thread2.start();
		try {
			latch.await();
		} catch (InterruptedException e) {
			LogUtils.log(logger, e);
		}
	}

}
