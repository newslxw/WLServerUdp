package com.wlnet.nio.nioserver.event;

import java.net.Socket;
import java.nio.channels.SocketChannel;

import com.wlnet.nio.nioserver.Reader;
import com.wlnet.nio.nioserver.Request;
import com.wlnet.nio.nioserver.Response;
import com.wlnet.nio.nioserver.ServerListener;
import com.wlnet.nio.nioserver.Writer;

/**
 * <p>Title: ÊÂ¼þÊÊÅäÆ÷</p>
 * @author starboy
 * @version 1.0
 */

public abstract class EventAdapter implements ServerListener {
    public EventAdapter() {
    }
    public void onError(String error) {
    }

    public void onError(Exception error) {
    }
    public void onAccept() throws Exception {
    }
    public void onAccepted(Request request)  throws Exception {
    }
    public void onRead(Request request)  throws Exception {
    }
    public void onWrite(Request request, Response response)  throws Exception {
    }
    public void onClosed(Request request)  throws Exception{
    }
    

    public void onAccepted(Socket client) throws Exception {
    }
}
