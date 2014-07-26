/**
 * 
 */
package com.wlnet.server.tcp.dev.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import org.apache.log4j.Logger;

import com.wlnet.nio.nioserver.Request;
import com.wlnet.nio.nioserver.Response;
import com.wlnet.nio.nioserver.Writer;
import com.wlnet.pojo.Dev;
import com.wlnet.pojo.Rev;
import com.wlnet.server.buz.ZNServ;
import com.wlnet.server.tcp.Const;
import com.wlnet.server.tcp.dev.DevHandle;
import com.wlnet.server.tcp.mobile.MobileHandle;
import com.wlnet.server.utils.LogUtils;

/**
 * @author Administrator
 *
 */
public class WenduDevHandle implements DevHandle {
	protected static Logger logger = Logger.getLogger(WenduDevHandle.class);
	@Override
	public boolean devHandle(byte[] byteMsg, Dev dev, ZNServ buzServ,Request request) throws IOException {
			//sc.configureBlocking(true);
			String msg = new String(byteMsg);
	        String arr[] = msg.split(Const.Sym.cmd);
	        if(arr.length !=2) return false;
			Rev rev = new Rev(arr[1]);
			logger.info("dev:WenduDevHandle.run receive dev reply:"+msg +" from dev ["+dev.getDevUuid()+"] ip "+dev.getInetIp()+":"+dev.getInetPort());
			buzServ.updateData(dev, rev);
		//	request.getSc().register(writer.getServer().getSelector(), SelectionKey.OP_READ, request);
			return true;
	}
	@Override
	public boolean devHandle(byte[] byteMsg, Dev dev, ZNServ buzServ,
			Socket socket, InputStream in, OutputStream out) throws IOException {
		String msg = new String(byteMsg);
        String arr[] = msg.split(Const.Sym.cmd);
        if(arr.length !=2) return false;
		Rev rev = new Rev(arr[1]);
		logger.info("dev:WenduDevHandle.run receive dev reply:"+msg +" from dev ["+dev.getDevUuid()+"] ip "+dev.getInetIp()+":"+dev.getInetPort());
		buzServ.updateData(dev, rev);
		return true;
	}
	
}
