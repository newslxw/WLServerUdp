package com.wlnet.server.tcp.mobile;

import org.apache.log4j.Logger;

import com.wlnet.nio.nioserver.Notifier;
import com.wlnet.nio.nioserver.Server;
import com.wlnet.nio.nioserver.event.LogHandler;
import com.wlnet.server.buz.ZNServ;
import com.wlnet.server.utils.LogUtils;

/**
 * 移动网block所有发现手机的UDP包，所以通过移动网给手机发送信息只能用TCP
 * @author Administrator
 *
 */
public class ZNMobileTcpServer {
	protected static Logger logger = Logger.getLogger(ZNMobileTcpServer.class);
	private Integer mobileTcpServerPort ;
	private ZNServ buzServ;
	
	public Integer getMobileTcpServerPort() {
		return mobileTcpServerPort;
	}

	public void setMobileTcpServerPort(Integer mobileTcpServerPort) {
		this.mobileTcpServerPort = mobileTcpServerPort;
	}

	public ZNServ getBuzServ() {
		return buzServ;
	}

	public void setBuzServ(ZNServ buzServ) {
		this.buzServ = buzServ;
	}
	
	public void startServer() {
		 try {
	            LogHandler netLoger = new LogHandler("ZNMobileTcpServer");
	            MobileTcpHandle handle = new MobileTcpHandle(buzServ);
	            Notifier notifier = Notifier.getNotifier("mobile");
	            notifier.addListener(netLoger);
	            notifier.addListener(handle);
	            Server server = new Server(mobileTcpServerPort, notifier);
	            Thread tServer = new Thread(server);
	            tServer.start();
	        }
	        catch (Exception e) {
	            LogUtils.log(logger, e);
	        }
	
	}

}
