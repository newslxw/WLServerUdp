package com.wlnet.test;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;

import com.wlnet.server.utils.Utils;

public class Receive {
	public static void main(String[] args) {
		try {
			//InetAddress ip = InetAddress.getLocalHost();
			//String strIp = "122.10.81.128";
			//String strIp = "192.168.1.8";
			String strIp = "116.255.248.182";
			InetAddress ip = InetAddress.getByName(strIp);
			int port = 2036;
			DatagramSocket getSocket = new DatagramSocket();
			String msg = "lastrev#1";
			byte[] buf = msg.getBytes();
			DatagramPacket getPacket = new DatagramPacket(buf, buf.length, ip,port);
			getSocket.send(getPacket);
			buf = new byte[1024];
			getPacket = new DatagramPacket(buf, buf.length, ip,port);
			getSocket.receive(getPacket);
			String getMes = new String(buf, 0, getPacket.getLength());
			System.out.println("对方发送的消息：" + getMes);
			// 通过数据报得到发送方的IP和端口号，并打印
			InetAddress sendIP = getPacket.getAddress();
			int sendPort = getPacket.getPort();
			System.out.println("对方的IP地址是：" + sendIP.getHostAddress());
			System.out.println("对方的端口号是：" + sendPort);

			msg = "devlist";
			buf = msg.getBytes();
			getPacket = new DatagramPacket(buf, buf.length, ip,port);
			getSocket.send(getPacket);
			buf = new byte[1024];
			getPacket = new DatagramPacket(buf, buf.length, ip,port);
			getSocket.receive(getPacket);
			getMes = new String(buf, 0, getPacket.getLength());
			System.out.println("对方发送的消息：" + getMes);
			// 关闭套接字
			getSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
