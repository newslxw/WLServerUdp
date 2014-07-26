package com.wlnet.test;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.*;

public class ServerTest implements Runnable {
	private int port;

	public ServerTest(int port) {
		this.port = port;
		new Thread(this).start();
	}

	public void run() {
		try {
			// 建立
			DatagramChannel dc = DatagramChannel.open();
			dc.configureBlocking(false);

			SocketAddress address = new InetSocketAddress(port);
			// 本地绑定端口
			DatagramSocket ds = dc.socket();
			ds.setReceiveBufferSize(20480);
			ds.bind(address);

			// 注册
			Selector select = Selector.open();
			dc.register(select, SelectionKey.OP_READ);
			System.out.println("Listening on port " + port);

			ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

			int number = 0; // 只为记录接受的字节数
			while (true) {
				int num = select.select();
				// 如果选择器数目为0，则结束循环
				if (num == 0) {
					continue;
				}
				// 得到选择键列表
				Set Keys = select.selectedKeys();
				Iterator it = Keys.iterator();

				while (it.hasNext()) {
					SelectionKey k = (SelectionKey) it.next();
					if ((k.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {

						DatagramChannel cc = (DatagramChannel) k.channel();
						// 非阻塞
						cc.configureBlocking(false);

						// 接收数据并读到buffer中
						buffer.clear();
						SocketAddress client = cc.receive(buffer);

						buffer.flip();
						if (buffer.remaining() <= 0) {
							System.out.println("bb is null");
						}
						// 记录接收到的字节总数
						number += buffer.remaining();
						byte b[] = new byte[buffer.remaining()];
						for (int i = 0; i < buffer.remaining(); i++) {
							b[i] = buffer.get(i);
						}
						String in = new String(b, "gb2312");
						System.out.println("number::::" + number);
						// 执行操作，并回发送
						String echo = "This is the reply message from 服务器。";
						ByteBuffer bf = Charset.defaultCharset().encode(echo);
						cc.send(bf,client);
					}
				}
				Keys.clear();
			}

		} catch (IOException ie) {
			System.err.println(ie);
		}
	}

	static public void main(String args[]) throws Exception {
		int port = 1111;// Integer.parseInt( args[0] );

		new ServerTest(port);
	}
}