package com.wlnet.test.findip;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class BroadCastTest {
	
	public static void main(String args[])throws Exception{
		//sendBroadcast();
		receiveBroadcast();
	}
	
	public static void sendBroadcast()throws Exception{
		DatagramSocket socket;
		DatagramPacket packet;
		byte[] data="1234".getBytes();
		
		socket = new DatagramSocket();
		socket.setBroadcast(true); //有没有没啥不同
		//send端指定接受端的端口，自己的端口是随机的
		packet = new DatagramPacket(data,data.length,InetAddress.getByName("255.255.255.255"),8500);
		for(int i = 0 ; i < 50 ; i++){
			Thread.sleep(1000);
			socket.send(packet);
		}
	}
	
	public static void receiveBroadcast()throws Exception{
		byte[] buffer = new byte[65507];
		DatagramSocket server = new DatagramSocket(8500);
		DatagramPacket packet = new DatagramPacket(buffer , buffer.length);
		for(;;){
			server.receive(packet);
			String s = new String(packet.getData( ), 0, packet.getLength( ));
			System.out.println(packet.getAddress( ) + " at port " 
			           + packet.getPort( ) + " says " + s);
		}
	}

}
