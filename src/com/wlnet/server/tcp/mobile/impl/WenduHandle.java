/**
 * 
 */
package com.wlnet.server.tcp.mobile.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.channels.SocketChannel;

import org.apache.log4j.Logger;

import com.wlnet.nio.nioserver.Request;
import com.wlnet.nio.nioserver.Response;
import com.wlnet.pojo.Dev;
import com.wlnet.pojo.Rev;
import com.wlnet.server.buz.ZNServ;
import com.wlnet.server.tcp.Const;
import com.wlnet.server.tcp.mobile.MobileHandle;

/**
 * @author Administrator
 *
 */
public class WenduHandle implements MobileHandle {
	protected static Logger logger = Logger.getLogger(WenduHandle.class);
	@Override
	public boolean mobileHandle(byte[] byteMsg, Dev dev, ZNServ buzServ,
			Request request, Response response) throws IOException {
		String backmsg="";
        Rev rev = buzServ.getLastRev(dev);
		if(rev!=null){
			backmsg = rev.getDevUuid()+Const.Sym.cmd+rev.getMeasureValue()+Const.Sym.cmd+rev.getRevTime();
		}
		response.send(backmsg.getBytes("UTF-8"));
		logger.info("mobile:WenduHandle.handle send reply ["+backmsg+"] to ip :"+request.getAddress()+" port:"+request.getPort());
		return true;
	}
	@Override
	public boolean mobileHandle(byte[] byteMsg, Dev dev, ZNServ buzServ,
			Socket socket, InputStream in, OutputStream out) throws IOException {
		String backmsg="";
        Rev rev = buzServ.getLastRev(dev);
		if(rev!=null){
			backmsg = rev.getDevUuid()+Const.Sym.cmd+rev.getMeasureValue()+Const.Sym.cmd+rev.getRevTime();
		}
		out = socket.getOutputStream();
		out.write(backmsg.getBytes("UTF-8"));
		out.flush();
		logger.info("mobile:WenduHandle.handle send reply ["+backmsg+"] to ip :"+socket.getInetAddress()+" port:"+socket.getPort());
		return true;
	}
	
}
