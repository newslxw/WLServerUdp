package com.wlnet.test;

/////////////NIO写UDP----client
import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class ClientTest extends Thread {
	private String host;
	private int port;
	int j = 0;
	int number;

	public ClientTest(String host, int port, int numThreads) {
		this.host = host;
		this.port = port;

		for (int i = 0; i < numThreads; ++i) {
			new Thread(this).start();
			/*
			 * try { sleep(100); } catch (Exception e) {
			 * 
			 * }
			 */
		}
	}

	public void run() {
		// 构造一个数据报Socket
		DatagramChannel dc = null;
		try {
			dc = DatagramChannel.open();
		} catch (IOException ex4) {
		}
		SocketAddress address = new InetSocketAddress(host, port);
		try {
			dc.connect(address);
		} catch (IOException ex) {
		}
		// dc = Socket.getChannel();
		// 发送请求
		ByteBuffer bb = ByteBuffer.allocate(130);

		byte[] b = new byte[130];
		String s = "sdfas";

		s = "sss";

		b = s.getBytes();
		bb.clear();
		bb.put(b);
		bb.flip();
		// 测试
		if (bb.remaining() <= 0) {
			System.out.println("bb is null");
		}
		// while(true) {
		try {
			int num = dc.send(bb, address);
			number = number + num;

			System.out.println("number:::" + number);

			bb.clear();
			dc.receive(bb);
			bb.flip();
			byte[] by = new byte[bb.remaining()];
			for (int i = 0; i < bb.remaining(); i++) {
				by[i] = bb.get(i);
			}
			String ss = new String(by, "gb2312");
			System.out.println(ss);
		} catch (Exception ex1) {
		}
		// }
	}

	// start
	public static void main(String[] args) {
		String host = "127.0.0.1";// args[0];
		int port = 1111;// Integer.parseInt( args[1] );
		int numThreads = 3;// Integer.parseInt( args[2] );

		new ClientTest(host, port, numThreads);
	}

}
