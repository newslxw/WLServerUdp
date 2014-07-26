package com.wlnet.nio.nioserver.test;


import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.Date;

import org.apache.log4j.Logger;

import com.wlnet.nio.nioserver.Reader;
import com.wlnet.nio.nioserver.Request;
import com.wlnet.nio.nioserver.Response;
import com.wlnet.nio.nioserver.event.EventAdapter;
import com.wlnet.server.utils.LogUtils;


/**
 * ÈÕÖ¾¼ÇÂ¼
 */
public class LogHandler extends EventAdapter {
	protected static Logger logger = Logger.getLogger(LogHandler.class);
    public LogHandler() {
    }

    public void onClosed(SocketChannel sc,Request request) throws Exception {
        String log = new Date().toString() + " from " + request.getAddress().toString();
        logger.info(log);
    }

    public void onError(Exception e) {
        LogUtils.log(logger, e);
    }

	@Override
	public void onConnected(Socket socket) {
		// TODO Auto-generated method stub
		
	}

}
