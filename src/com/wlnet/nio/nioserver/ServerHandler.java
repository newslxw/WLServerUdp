package com.wlnet.nio.nioserver;

import java.net.Socket;
import java.nio.channels.SocketChannel;

import com.wlnet.nio.nioserver.event.EventAdapter;


/**
 * <p>Title: ������¼�������</p>
 * <p>Description: ���������������ͻ���ʾ�ĸ����¼�, ������ʵ�־���Ӧ��</p>
 * @author starboy
 * @version 1.0
 */

public class ServerHandler extends EventAdapter {
    public ServerHandler() {
    }

    public void onAccept() throws Exception {
        System.out.println("#onAccept()");
    }

    public void onAccepted(Request request) throws Exception {
        System.out.println("#onAccepted()");
    }

    public void onRead(Request request, Reader reader) throws Exception {
    }
    

    public void onWrite(Request request, Response response) throws Exception {
    }

    public void onClosed(Request request) throws Exception {
    }

    public void onError(String error) {
        System.out.println("#onAError(): " + error);
    }

	@Override
	public void onConnected(Socket socket) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAccepted(Socket client) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
