package com.wlnet.nio.nioserver;

import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

/**
 * <p>Title: 事件触发器</p>
 * @author starboy
 * @version 1.0
 */
public class Notifier {
    private ArrayList listeners = null;
    private static Notifier instanceMobile = null;
    private static Notifier instanceDev = null;

    private Notifier() {
        listeners = new ArrayList();
    }

    /**
     * 获取事件触发器
     * @return 返回事件触发器
     */
    public static synchronized Notifier getNotifier(String type) {
    	if("dev".equals(type)){
    		if (instanceDev == null) {
    			instanceDev = new Notifier();
                return instanceDev;
            }
            else return instanceDev;
    	}else{
    		if (instanceMobile == null) {
    			instanceMobile = new Notifier();
                return instanceMobile;
            }
            else return instanceMobile;
    	}
        
    }

    /**
     * 添加事件监听器
     * @param l 监听器
     */
    public void addListener(ServerListener l) {
        synchronized (listeners) {
            if (!listeners.contains(l))
                listeners.add(l);
        }
    }

    public void fireOnAccept() throws Exception {
        for (int i = listeners.size() - 1; i >= 0; i--)
            ( (ServerListener) listeners.get(i)).onAccept();
    }

    public void fireOnAccepted(Request request) throws Exception {
        for (int i = listeners.size() - 1; i >= 0; i--)
            ( (ServerListener) listeners.get(i)).onAccepted(request);
    }

    public void fireOnRead(Request request) throws Exception {
        for (int i = listeners.size() - 1; i >= 0; i--)
            ( (ServerListener) listeners.get(i)).onRead(request);

    }
    

    public void fireOnWrite(Request request, Response response)  throws Exception  {
        for (int i = listeners.size() - 1; i >= 0; i--)
            ( (ServerListener) listeners.get(i)).onWrite(request, response);

    }

    public void fireOnClosed(Request request) throws Exception {
        for (int i = listeners.size() - 1; i >= 0; i--)
            ( (ServerListener) listeners.get(i)).onClosed(request);
    }

    public void fireOnError(String error) {
        for (int i = listeners.size() - 1; i >= 0; i--)
            ( (ServerListener) listeners.get(i)).onError(error);
    }
    

    public void fireOnError(Exception e) {
        for (int i = listeners.size() - 1; i >= 0; i--)
            ( (ServerListener) listeners.get(i)).onError(e);
    }

	public void fireOnConnected(Socket socket)throws Exception {
        for (int i = listeners.size() - 1; i >= 0; i--)
            ( (ServerListener) listeners.get(i)).onConnected(socket);
		
	}

	public void fireOnAccepted(Socket client) throws Exception{
        for (int i = listeners.size() - 1; i >= 0; i--)
            ( (ServerListener) listeners.get(i)).onAccepted(client);
		
	}
}
