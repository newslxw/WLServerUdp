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
			System.out.println("�Է����͵���Ϣ��" + getMes);
			// ͨ�����ݱ��õ����ͷ���IP�Ͷ˿ںţ�����ӡ
			InetAddress sendIP = getPacket.getAddress();
			int sendPort = getPacket.getPort();
			System.out.println("�Է���IP��ַ�ǣ�" + sendIP.getHostAddress());
			System.out.println("�Է��Ķ˿ں��ǣ�" + sendPort);

			msg = "devlist";
			buf = msg.getBytes();
			getPacket = new DatagramPacket(buf, buf.length, ip,port);
			getSocket.send(getPacket);
			buf = new byte[1024];
			getPacket = new DatagramPacket(buf, buf.length, ip,port);
			getSocket.receive(getPacket);
			getMes = new String(buf, 0, getPacket.getLength());
			System.out.println("�Է����͵���Ϣ��" + getMes);
			// �ر��׽���
			getSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
