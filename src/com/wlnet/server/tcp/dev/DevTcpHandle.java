package com.wlnet.server.tcp.dev;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.channels.SocketChannel;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.wlnet.nio.nioserver.Reader;
import com.wlnet.nio.nioserver.Request;
import com.wlnet.nio.nioserver.Response;
import com.wlnet.nio.nioserver.Writer;
import com.wlnet.nio.nioserver.event.EventAdapter;
import com.wlnet.pojo.Dev;
import com.wlnet.pojo.Rev;
import com.wlnet.server.buz.ZNServ;
import com.wlnet.server.tcp.CmdMsg;
import com.wlnet.server.tcp.Const;
import com.wlnet.server.tcp.HandleFactory;
import com.wlnet.server.tcp.Const.Cmd;
import com.wlnet.server.tcp.Const.Sym;
import com.wlnet.server.tcp.mobile.MobileHandle;
import com.wlnet.server.tcp.mobile.impl.ChazuoHandle;
import com.wlnet.server.utils.LogUtils;
import com.wlnet.server.utils.StreamUtils;

public class DevTcpHandle extends EventAdapter{

	protected static Logger logger = Logger.getLogger(DevTcpHandle.class);
	private ZNServ buzServ;
	
	public ZNServ getBuzServ() {
		return buzServ;
	}

	public void setBuzServ(ZNServ buzServ) {
		this.buzServ = buzServ;
	}

	public DevTcpHandle(ZNServ buzServer){
		this.buzServ = buzServer;
	}
	
	 public void onRead(final Request request) throws Exception {
		byte byteMsg[]  = request.getDataInput();
        String msg = new String(byteMsg);
        String arr[] = msg.split(Const.Sym.cmd);
		Dev dev = null;
		if(arr.length > 1) {
			dev = buzServ.getDev(arr[0]);
			if(dev!=null){
				DevHandle handle = HandleFactory.getDevHandle(dev.getDevType());
				try {
					if(handle.devHandle(byteMsg,dev, buzServ, request)) return ;
				} catch (IOException e) {
					LogUtils.log(logger, e);
				}
			}else{
				logger.error("devTcpHandle dev ["+arr[1]+"] not regist receive from ip:"+request.getAddress()+"  port:"+request.getPort());
			}
		}else{
			logger.error("devTcpHandle msg ["+msg+"] not in format uuid#value receive from ip:"+request.getAddress()+"  port:"+request.getPort());
		}
	
	 }
	 
	 public void onWrite(final Request request, final Response response) throws Exception {
		 if(request.getSendMsg()!=null){
         	CmdMsg cmdMsg = (CmdMsg) request.getSendMsg();
         	request.attach(cmdMsg);
         	request.setSendMsg(null);
         	response.send(cmdMsg.getCmd().getBytes());
         }
	 }

	@Override
	public void onConnected(Socket socket)throws Exception {
		InputStream in=null;
		OutputStream out = null;
		try{
			in = socket.getInputStream();
			out = socket.getOutputStream();
			byte buffer[]=new byte[1024];
			int num =0;
			byte initByte = 0;
			socket.setSoTimeout(10000);  //10s timeout
			//while(true){
			Arrays.fill(buffer, initByte);
			num = in.read(buffer);
			byte byteMsg[]  = new byte[num];
			System.arraycopy(buffer, 0, byteMsg, 0, num);
	        String msg = new String(buffer ,0, num);
	        String arr[] = msg.split(Const.Sym.cmd);
			Dev dev = null;
			if(arr.length > 1) {
				dev = buzServ.getDev(arr[0]);
				if(dev!=null){
					DevHandle handle = HandleFactory.getDevHandle(dev.getDevType());
					try {
						handle.devHandle(byteMsg,dev, buzServ, socket, in, out);
					} catch (IOException e) {
						LogUtils.log(logger, e);
					}
				}else{
					logger.error("devTcpHandle dev ["+arr[1]+"] not regist receive from ip:"+socket.getInetAddress()+"  port:"+socket.getPort());
				}
			}else{
				logger.error("devTcpHandle msg ["+msg+"] not in format uuid#value receive from ip:"+socket.getInetAddress()+"  port:"+socket.getPort());
			}
		}finally{
			 StreamUtils.closeStream(in);
			 StreamUtils.closeStream(out);
			 StreamUtils.closeSocket(socket);
		 }
	}


	
}
