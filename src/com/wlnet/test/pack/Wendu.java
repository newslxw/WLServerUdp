package com.wlnet.test.pack;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.io.*;

public class Wendu {
	private static int count = 0;
    public Wendu() {
    }

    public static void main(String[] args) throws IOException {
    	String cmds[]={"S#1#20,0.4,17,20,1,23.1200490000,113.3076500000","S#1#20,0.4,17,20,1,23.0957580000,113.3432680000","S#1#20,0.4,17,20,3,23.1223490000,113.3968790000"};
		String msg = cmds[0];
		double lg = 113.3076500000;
		String prev = "S#1#20,0.4,17,20,1,23.1200490000";
    	while(true){
    		try {
    			// 创建发送方的套接字，IP默认为本地，端口号随机
    			DatagramSocket sendSocket = new DatagramSocket();
    			// 确定要发送的消息：
    			lg = lg + 0.0001;
    			msg = (prev+","+ lg);
    			byte[] buf = msg.getBytes();
    			int port = 2034;
    			String strIp = "122.10.81.128";
    			//String strIp = "localhost";
    			//String strIp = "116.255.248.182";
    			InetAddress ip = InetAddress.getByName(strIp);
    			//SocketAddress addr = new InetSocketAddress(51252) ;
    			//sendSocket.bind(addr );
    			DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, ip,port);
    			sendSocket.send(sendPacket);
    			System.out.println("发送消息：" + msg);
    			// 关闭套接字
    			/*byte buf2[] = new byte[1024];
    			DatagramPacket getPacket = new DatagramPacket(buf2, buf.length);
    			sendSocket.receive(getPacket);
    			System.out.println("收到消息：" + new String(getPacket.getData()));
    			*/
    			sendSocket.close();
    			Thread.sleep(1000);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		
    	}
		

	}

}
