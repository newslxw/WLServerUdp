package com.wlnet.nio.nioserver.event;


import java.net.Socket;

import org.apache.log4j.Logger;

import com.wlnet.nio.nioserver.Request;
import com.wlnet.nio.nioserver.Response;

/**
 * »’÷æ
 * @author Administrator
 *
 */
public class LogHandler extends EventAdapter {
	protected static Logger logger = Logger.getLogger(LogHandler.class);
	private String serveNname ;
	public LogHandler(String serveNname){
		this.serveNname = serveNname;
	}
	
	public String getServeNname() {
		return serveNname;
	}

	public void setServeNname(String serveNname) {
		this.serveNname = serveNname;
	}

	@Override
	public void onError(String error) {
		// TODO Auto-generated method stub
		super.onError(error);
		logger.error(error);
	}

	@Override
	public void onAccept() throws Exception {
		// TODO Auto-generated method stub
		super.onAccept();
		
	}

	@Override
	public void onAccepted(Request request) throws Exception {
		// TODO Auto-generated method stub
		super.onAccepted(request);
		String str = this.serveNname+" accept connect from "+request.getAddress()+":"+request.getPort();
		logger.info(str);
	}
	
	@Override
	public void onAccepted(Socket client) throws Exception {
		// TODO Auto-generated method stub
		super.onAccepted(client);
		String str = this.serveNname+" accept connect from "+client.getInetAddress()+":"+client.getPort();
		logger.info(str);
	}	

	@Override
	public void onRead(Request request) throws Exception {
		// TODO Auto-generated method stub
		super.onRead(request);
		String str = this.serveNname+" read from "+request.getAddress()+":"+request.getPort();
		logger.info(str);
	}

	@Override
	public void onWrite(Request request, Response response) throws Exception {
		// TODO Auto-generated method stub
		super.onWrite(request, response);

		String str = this.serveNname+" write to "+request.getAddress()+":"+request.getPort();
		logger.info(str);
	}

	@Override
	public void onClosed(Request request) throws Exception {
		// TODO Auto-generated method stub
		super.onClosed(request);
		String str = this.serveNname+" close connect at "+request.getAddress()+":"+request.getPort();
		logger.info(str);
	}

	@Override
	public void onConnected(Socket socket) {
		// TODO Auto-generated method stub
		
	}


}
