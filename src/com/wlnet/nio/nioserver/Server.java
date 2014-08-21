package com.wlnet.nio.nioserver;

import java.util.List;
import java.util.LinkedList;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.Selector;
import java.nio.channels.SelectionKey;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import com.wlnet.server.utils.LogUtils;




/**
 * <p>Title: ���ط����߳�</p>
 * @author starboy
 * @version 1.0
 */

public class Server implements Runnable {
    private static List wpool = new LinkedList();  // ��Ӧ��
    private static Selector selector;
    private ServerSocketChannel sschannel;
    private InetSocketAddress address;
    protected Notifier notifier;
    private int port;

    public Notifier getNotifier() {
		return notifier;
	}

	public void setNotifier(Notifier notifier) {
		this.notifier = notifier;
	}


	/**
     * �������ط����߳�
     * @param port ����˿�
     * @throws java.lang.Exception
     */
    private static int MAX_THREADS = 4;
    public Server(int port, Notifier notifier) throws Exception {
        this.port = port;

        // ��ȡ�¼�������
        this.notifier = notifier;

        // ������д�̳߳�
        for (int i = 0; i < MAX_THREADS; i++) {
            Thread r = new Reader(notifier);
            Thread w = new Writer(notifier);
            r.start();
            w.start();
        }

        // ���������������׽�
        selector = Selector.open();
        sschannel = ServerSocketChannel.open();
        sschannel.configureBlocking(false);
        address = new InetSocketAddress(port);
        ServerSocket ss = sschannel.socket();
        ss.bind(address);
        sschannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void run() {
        System.out.println("Mobile Server listening on port: " + port);
        // ����
        while (true) {
            try {
                int num = 0;
                num = selector.select();

                if (num > 0) {
                    Set selectedKeys = selector.selectedKeys();
                    Iterator it = selectedKeys.iterator();
                    while (it.hasNext()) {
                        SelectionKey key = (SelectionKey) it.next();
                        it.remove();
                        // ����IO�¼�
                        if ( (key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
                           // Accept the new connection
                           ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                           notifier.fireOnAccept();

                           SocketChannel sc = ssc.accept();
                           sc.configureBlocking(false);

                           // �������������¼�
                           Request request = new Request(sc);
                           notifier.fireOnAccepted(request);

                           // ע�������,�Խ�����һ���Ķ�����
                           sc.register(selector,  SelectionKey.OP_READ, request);
                       }
                       else if ( (key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ ) {
                           Reader.processRequest(key);  // �ύ�������̶߳�ȡ�ͻ�������
                           key.cancel();
                       }
                       else if ( (key.readyOps() & SelectionKey.OP_WRITE) == SelectionKey.OP_WRITE ) {
                           Writer.processRequest(key);  // �ύд�����߳���ͻ��˷��ͻ�Ӧ����
                           key.cancel();
                       }
                    }
                }
                else {
                    addRegister();  // ��Selector��ע���µ�дͨ��
                }
            }
            catch (Exception e) {
                notifier.fireOnError("Error occured in Server: " + e.getMessage());
                continue;
            }
        }
    }

    /**
     * ����µ�ͨ��ע��
     */
    private void addRegister() {
        synchronized (wpool) {
            while (!wpool.isEmpty()) {
                SelectionKey key = (SelectionKey) wpool.remove(0);
                SocketChannel schannel = (SocketChannel)key.channel();
                try {
                    schannel.register(selector,  SelectionKey.OP_WRITE, key.attachment());
                }
                catch (Exception e) {
                    try {
                        schannel.finishConnect();
                        schannel.close();
                        schannel.socket().close();
                        notifier.fireOnClosed((Request)key.attachment());
                    }
                    catch (Exception e1) {}
                    notifier.fireOnError("Error occured in addRegister: " + e.getMessage());
                }
            }
        }
    }


    /**
     * �ύ�µĿͻ���д�������������̵߳Ļ�Ӧ����
     */
    public static void processWriteRequest(SelectionKey key) {
        synchronized (wpool) {
            wpool.add(wpool.size(), key);
            wpool.notifyAll();
        }
        selector.wakeup();  // ���selector������״̬���Ա�ע���µ�ͨ��
    }
}

