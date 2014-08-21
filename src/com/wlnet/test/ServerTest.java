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
			// ����
			DatagramChannel dc = DatagramChannel.open();
			dc.configureBlocking(false);

			SocketAddress address = new InetSocketAddress(port);
			// ���ذ󶨶˿�
			DatagramSocket ds = dc.socket();
			ds.setReceiveBufferSize(20480);
			ds.bind(address);

			// ע��
			Selector select = Selector.open();
			dc.register(select, SelectionKey.OP_READ);
			System.out.println("Listening on port " + port);

			ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

			int number = 0; // ֻΪ��¼���ܵ��ֽ���
			while (true) {
				int num = select.select();
				// ���ѡ������ĿΪ0�������ѭ��
				if (num == 0) {
					continue;
				}
				// �õ�ѡ����б�
				Set Keys = select.selectedKeys();
				Iterator it = Keys.iterator();

				while (it.hasNext()) {
					SelectionKey k = (SelectionKey) it.next();
					if ((k.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {

						DatagramChannel cc = (DatagramChannel) k.channel();
						// ������
						cc.configureBlocking(false);

						// �������ݲ�����buffer��
						buffer.clear();
						SocketAddress client = cc.receive(buffer);

						buffer.flip();
						if (buffer.remaining() <= 0) {
							System.out.println("bb is null");
						}
						// ��¼���յ����ֽ�����
						number += buffer.remaining();
						byte b[] = new byte[buffer.remaining()];
						for (int i = 0; i < buffer.remaining(); i++) {
							b[i] = buffer.get(i);
						}
						String in = new String(b, "gb2312");
						System.out.println("number::::" + number);
						// ִ�в��������ط���
						String echo = "This is the reply message from ��������";
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