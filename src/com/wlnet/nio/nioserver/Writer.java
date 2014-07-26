package com.wlnet.nio.nioserver;

import java.util.List;
import java.util.LinkedList;
import java.nio.channels.SocketChannel;
import java.nio.channels.SelectionKey;

import com.wlnet.server.tcp.CmdMsg;

/**
 * <p>Title: ��Ӧ�߳�</p>
 * <p>Description: ������ͻ��˷�����Ϣ</p>
 * @author starboy
 * @version 1.0
 */

public final class Writer extends Thread {
    private static List pool = new LinkedList();
    private static Notifier notifier ;

    
    public static Notifier getNotifier() {
		return notifier;
	}

	public static void setNotifier(Notifier notifier) {
		Writer.notifier = notifier;
	}

	public Writer(Notifier notifier) {
		this.notifier = notifier;
    }

    /**
     * SMS�����߳����ط��񷽷�,������������������
     */
    public void run() {
        while (true) {
            try {
                SelectionKey key;
                synchronized (pool) {
                    while (pool.isEmpty()) {
                        pool.wait();
                    }
                    key = (SelectionKey) pool.remove(0);
                }

                // ����д�¼�
                write(key);
            }
            catch (Exception e) {
                continue;
            }
        }
    }

    /**
     * ������ͻ���������
     * @param key SelectionKey
     */
    public void write(SelectionKey key) {
        try {
            SocketChannel sc = (SocketChannel) key.channel();
            Response response = new Response(sc);

            // ����onWrite�¼�
            notifier.fireOnWrite((Request)key.attachment(), response);

            // ����onClosed�¼�
            notifier.fireOnClosed((Request)key.attachment());
            // �ر�
            sc.finishConnect();
            sc.socket().close();
            sc.close();

        }
        catch (Exception e) {
        	e.printStackTrace();
            notifier.fireOnError("Error occured in Writer: " + e.getMessage());
        }
    }

    /**
     * ����ͻ�����,�����û��������,�����Ѷ����е��߳̽��д���
     */
    public static void processRequest(SelectionKey key) {
        synchronized (pool) {
            pool.add(pool.size(), key);
            pool.notifyAll();
        }
    }
}
