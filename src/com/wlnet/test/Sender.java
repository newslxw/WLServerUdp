package com.wlnet.test;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import com.wlnet.server.utils.Utils;

public class Sender {
	public static void main(String[] args) {
		try {
			// 创建发送方的套接字，IP默认为本地，端口号随机
			DatagramSocket sendSocket = new DatagramSocket();
			// 确定要发送的消息：
			String msg = "S#1#20,0.4,17,20,1,1003.03,2300.20";
			byte[] buf = msg.getBytes();
			int port = 2034;
			//String strIp = "122.10.81.128";
			//String strIp = "localhost";
			String strIp = "116.255.248.182";
			InetAddress ip = InetAddress.getByName(strIp);
			//SocketAddress addr = new InetSocketAddress(51252) ;
			//sendSocket.bind(addr );
			DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, ip,port);
			sendSocket.send(sendPacket);
			System.out.println("发送消息：" + msg);
			// 关闭套接字
			byte buf2[] = new byte[1024];
			DatagramPacket getPacket = new DatagramPacket(buf2, buf.length);
			sendSocket.receive(getPacket);
			System.out.println("收到消息：" + new String(getPacket.getData()));
			
			sendSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
