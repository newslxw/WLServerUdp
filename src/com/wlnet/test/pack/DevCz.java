package com.wlnet.test.pack;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;

public class DevCz {
	static Thread thread1 = null;
	static Thread thread2 = null;
	static DatagramPacket sendPacket;
	static DatagramSocket sendSocket;
	private static DatagramPacket getPacket;
	private static DatagramSocket getSocket;
	private static CountDownLatch latch;
	private static int localPort = 51250;
	public static void main(String[] args) {
		latch = new CountDownLatch(2);
		try {
			// 创建发送方的套接字，IP默认为本地，端口号随机
			sendSocket = new DatagramSocket();
			sendSocket.setReuseAddress(true);
			//getSocket = new DatagramSocket(localPort);
			// 确定要发送的消息：
			final String msg = "S#2#AA";
			byte[] buf = msg.getBytes();
			byte[] buf2 = new byte[1024];
			int port = 2034;
			//String strIp = "122.10.81.128";
			String strIp = "116.255.248.182";
			//String strIp = "localhost";
			InetAddress ip = InetAddress.getByName(strIp);
			InetSocketAddress address = new InetSocketAddress(ip,port);
			sendPacket = new DatagramPacket(buf, buf.length, address);
			getPacket = new DatagramPacket(buf2, buf2.length);
			thread1 = new Thread(new Runnable(){
				@Override
				public void run() {
					try{
						if(true){
							try {
								sendSocket.send(sendPacket);
								//System.out.println("发送消息：" + msg);
								try {
									Thread.sleep(2000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}finally{
						latch.countDown();
					}
				}
				
			});
			thread2 = new Thread(new Runnable(){
				@Override
				public void run() {
					try{
						int count=0;
						String cmds[]={"2#AA", "2#55"};
						while(true){
							try {
								sendSocket.receive(getPacket);
								String msg = new String(getPacket.getData());
								String arr[]=msg.split("#");
								System.out.println("收到消息：" + msg);
								String cmd = arr[0]+"#"+cmds[count%2];
								DatagramPacket p = new DatagramPacket(cmd.getBytes(),cmd.getBytes().length,getPacket.getAddress(),getPacket.getPort());
								sendSocket.send(p);
								System.out.println("返回消息：" + cmd);
								count++;
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}finally{
						latch.countDown();
					}
				}
				
			});
			thread1.start();
			thread2.start();
			latch.await();
			// 关闭套接字
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(sendSocket!=null)sendSocket.close();
			//if(getSocket!=null)getSocket.close();
		}


	}

}
