package com.wlnet.server.tcp.dev;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.wlnet.nio.nioserver.Notifier;
import com.wlnet.socket.Server;
import com.wlnet.nio.nioserver.event.LogHandler;
import com.wlnet.server.buz.ZNServ;
import com.wlnet.server.utils.LogUtils;

/**
 * �ƶ���block���з����ֻ���UDP��������ͨ���ƶ������ֻ�������Ϣֻ����TCP
 * @author Administrator
 *
 */
public class DevTcpServer {
	protected static Logger logger = Logger.getLogger(DevTcpServer.class);
	private Integer tcpServerPort ;
	private ZNServ buzServ;
	

	public Integer getTcpServerPort() {
		return tcpServerPort;
	}

	public void setTcpServerPort(Integer tcpServerPort) {
		this.tcpServerPort = tcpServerPort;
	}

	public ZNServ getBuzServ() {
		return buzServ;
	}

	public void setBuzServ(ZNServ buzServ) {
		this.buzServ = buzServ;
	}
	
	public void startServer() {
		 try {
	            LogHandler netLoger = new LogHandler("DevTcpServer");
	            DevTcpHandle handle = new DevTcpHandle(buzServ);
	            Notifier notifier = Notifier.getNotifier("dev");
	            notifier.addListener(netLoger);
	            notifier.addListener(handle);
	            Server server = new DevServer(tcpServerPort, notifier);
	            Thread tServer = new Thread(server);
	            tServer.start();
	        }
	        catch (Exception e) {
	            LogUtils.log(logger, e);
	        }
	
	}

}
