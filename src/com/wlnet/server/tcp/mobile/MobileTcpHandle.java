package com.wlnet.server.tcp.mobile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.channels.SocketChannel;
import java.text.DateFormat;
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
import com.wlnet.server.tcp.Const;
import com.wlnet.server.tcp.HandleFactory;
import com.wlnet.server.tcp.Const.Cmd;
import com.wlnet.server.tcp.Const.Sym;
import com.wlnet.server.tcp.mobile.impl.ChazuoHandle;
import com.wlnet.server.utils.LogUtils;
import com.wlnet.server.utils.StreamUtils;

public class MobileTcpHandle extends EventAdapter{

	protected static Logger logger = Logger.getLogger(MobileTcpHandle.class);
	private ZNServ buzServ;
	
	public ZNServ getBuzServ() {
		return buzServ;
	}

	public void setBuzServ(ZNServ buzServ) {
		this.buzServ = buzServ;
	}

	public MobileTcpHandle(ZNServ buzServer){
		this.buzServ = buzServer;
	}
	
	 public void onWrite(final Request request, final Response response) throws Exception {
		 
		 /*Thread thread1 = new Thread(new Runnable(){
			@Override
			public void run() {*/
		 /*try{*/
			byte byteMsg[]  = request.getDataInput();
	        String msg = new String(byteMsg);
	        String arr[] = msg.split(Const.Sym.cmd);
	        String requestType = arr[0].toString();
			if(Const.Cmd.devlist.equals(requestType)){
				List<Dev> list = buzServ.getDevList();
				StringBuffer sb = new StringBuffer();
				/**
				 * 设备列表格式 uuid`name`type`ip`port[uuid`name`type`ip`port
				 */
				Dev tmp = null;
				for(int i=0; i<list.size(); i++){
					tmp = list.get(i);
					/**
					 * TODO:需要优化，多个设备是可能被切成多个包
					 */
					if(i>0) sb.append(Const.Sym.obj);
					sb.append(tmp.getDevUuid()).append(Const.Sym.prop).append(tmp.getDevName()).append(Const.Sym.prop)
					.append(tmp.getDevType()==null?" ":tmp.getDevType()).append(Const.Sym.prop)
					.append(tmp.getAppType()==null?" ":tmp.getAppType()).append(Const.Sym.prop)
					.append(tmp.getInetIp()==null?" ":tmp.getInetIp()).append(Const.Sym.prop)
					.append(tmp.getInetPort()==null?" ":tmp.getInetPort()).append(Const.Sym.prop);
				}
				try {
					
					response.send(sb.toString().getBytes("UTF-8"));
					logger.info("send to mobile "+request.getAddress()+":"+ request.getPort() + " devlist: "+sb.toString());
				} catch (UnsupportedEncodingException e) {
					LogUtils.log(logger, e);
				} catch (IOException e) {
					LogUtils.log(logger, e);
				}
			}else{
				Dev dev = null;
				if(arr.length > 1) {
					dev = buzServ.getDev(arr[0]);
					if(dev!=null){
						MobileHandle handle = HandleFactory.getHandle(dev.getDevType());
						try {
							if(handle.mobileHandle(byteMsg,dev, buzServ, request, response)) return ;
						} catch (IOException e) {
							LogUtils.log(logger, e);
						}
					}
				}
			}
		/* }finally{
			 request.getSc().finishConnect();
			 request.getSc().socket().close();
			 request.getSc().close();
		 }*/
			/*}
			 
		 });
		 thread1.start();*/
	 }

	@Override
	public void onConnected(Socket socket) throws Exception{
		InputStream in=null;
		OutputStream out = null;
		 try{
			 
			 in = new BufferedInputStream(socket.getInputStream());
			 out = new BufferedOutputStream(socket.getOutputStream());
			 byte buffer[]=new byte[100];
			 int num =0;
			 num = in.read(buffer);
			 /*while(in.available()>0){
			 }*/
			 
			byte byteMsg[]  = new byte[num];
			System.arraycopy(buffer, 0, byteMsg, 0, num);
			
	        String msg = new String(byteMsg);
	        String arr[] = msg.split(Const.Sym.cmd);
	        String requestType = arr[0].toString();
	        logger.error("receive mobile msg:"+ msg);
			if(Const.Cmd.devlist.equals(requestType)){
				socket.setSoTimeout(10000);
				List<Dev> list = buzServ.getDevList();
				StringBuffer sb = new StringBuffer();
				/**
				 * 设备列表格式 uuid`name`type`ip`port[uuid`name`type`ip`port
				 */
				Dev tmp = null;
				for(int i=0; i<list.size(); i++){
					tmp = list.get(i);
					/**
					 * TODO:需要优化，多个设备是可能被切成多个包
					 */
					if(i>0) sb.append(Const.Sym.obj);
					sb.append(tmp.getDevUuid()).append(Const.Sym.prop).append(tmp.getDevName()).append(Const.Sym.prop)
					.append(tmp.getDevType()==null?" ":tmp.getDevType()).append(Const.Sym.prop)
					.append(tmp.getInetIp()==null?" ":tmp.getInetIp()).append(Const.Sym.prop)
					.append(tmp.getInetPort()==null?" ":tmp.getInetPort()).append(Const.Sym.prop);
				}
				try {
					logger.error("devlist:"+ sb.toString());
					byte bmsg[] = sb.toString().getBytes("UTF-8");
					out.write(bmsg);
					out.flush();
				} catch (UnsupportedEncodingException e) {
					LogUtils.log(logger, e);
				} catch (IOException e) {
					LogUtils.log(logger, e);
				}
			}else{
				Dev dev = null;
				if(arr.length > 1) {
					dev = buzServ.getDev(arr[0]);
					if(dev!=null){
						MobileHandle handle = HandleFactory.getHandle(dev.getDevType());
						try {
							if(handle.mobileHandle(byteMsg,dev, buzServ, socket, in, out)) return ;
						} catch (IOException e) {
							LogUtils.log(logger, e);
						}
					}
				}
			}
		}finally{
/*			 StreamUtils.closeStream(in);
			 StreamUtils.closeStream(out);
			 StreamUtils.closeSocket(socket);*/
		 }
			/*}
			 
		 });
		 thread1.start();*/
	 
		
	}
}
