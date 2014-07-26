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

/**�����������豸������������
 * ���ݸ�ʽ devuuid,value ���ݸ�ʽ devuuid#value value��ʽ����: 1,2,3,4
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
	 * ���ط������Ƿ����״̬
	 * @return
	 */
	public boolean getServerState(){
		return serverSocket!=null&&!serverSocket.isBound();
	}
	
	protected void startServer() throws SocketException{
		// �������շ����׽���,���ƶ��˿ںź�IP��ַ
		if(this.getServerState()){
			serverSocket.close();
			serverSocket = null;
		}
		serverSocket = new DatagramSocket(mobileServerPort);
		LogUtils.log(logger, "znMobileServer start");
		// �����������͵����ݱ������ݽ��洢��buf��
		try{
			while(true){
				try {
					byte[] buf = new byte[1024];
					DatagramPacket serverPacket = new DatagramPacket(buf, buf.length);
					serverSocket.receive(serverPacket);
					// ͨ�����ݱ��õ����ͷ���IP�Ͷ˿ںţ�����ӡ
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
