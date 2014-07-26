/**
 * 
 */
package com.wlnet.server.tcp.dev.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketImpl;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.wlnet.nio.nioserver.Request;
import com.wlnet.nio.nioserver.Response;
import com.wlnet.nio.nioserver.Writer;
import com.wlnet.pojo.Dev;
import com.wlnet.pojo.Rev;
import com.wlnet.server.buz.ZNServ;
import com.wlnet.server.tcp.CmdMsg;
import com.wlnet.server.tcp.Const;
import com.wlnet.server.tcp.MsgQueueMgr;
import com.wlnet.server.tcp.dev.DevHandle;
import com.wlnet.server.tcp.mobile.MobileHandle;
import com.wlnet.server.udp.ServerMsgBack;
import com.wlnet.server.utils.LogUtils;

/**
 * 智能开关手机控制器，以后要实现在同一局域网内可以不经过服务器直接发送和查询插座状态
 * @author xinwen
 *
 */
public class ChazuoDevTcpHandle implements DevHandle {
	protected static Logger logger = Logger.getLogger(ChazuoDevTcpHandle.class);
	@Override
	public boolean devHandle(byte[] byteMsg, Dev dev, ZNServ buzServ,Request request) throws IOException {
		String msg = new String(byteMsg);
        String arr[] = msg.split(Const.Sym.cmd);
        if(arr.length!=2) return true;
        InetAddress ip = InetAddress.getByName(dev.getInetIp());
        CmdMsg cmdMsg=null;
        BlockingQueue<CmdMsg> queue = MsgQueueMgr.getQueue(dev.getDevUuid());
		//处理上一个命令的反馈信息
		byte back[]  = request.getDataInput();
    	CmdMsg oldCmd = (CmdMsg) request.attachment();
    	if(oldCmd!=null&&oldCmd.isFromMobile()){
        	synchronized(oldCmd){
        		oldCmd.setBackMsg(new String(back));
		        logger.info("dev:ChazuoDevTcpHandle.run recevie dev reply:"+ oldCmd.getBackMsg() +" from dev ["+dev.getDevUuid()+"] ip "+request.getAddress()+":"+request.getPort());
		        oldCmd.notifyAll();
			}
    	}
    	request.attach(null);
    	//等待手机发过来的新命令
    	try{
    		cmdMsg = queue.take();
    	}catch (InterruptedException e) {
    		cmdMsg = null;
    		logger.info("dev:ChazuoDevTcpHandle.run InterruptedException while waiting for mobile cmd  ");
			LogUtils.log(logger, e);
		}
    	if(cmdMsg!= null){
	    	//向设备发送新命令
    		request.setSendMsg(cmdMsg);
	    	logger.info("dev:ChazuoDevTcpHandle.run receive mobile msg:"+ cmdMsg.getCmd() +" and prepare sending to dev ["+dev.getDevUuid()+"] ip "+request.getAddress()+":"+request.getPort());
	    	
	    	//准备接受新反馈
    	}else{
    		logger.info("dev:ChazuoDevTcpHandle.run receive nothing from mobile ");
	    	cmdMsg = new CmdMsg();
	    	cmdMsg.setFromMobile(false);
	    	cmdMsg.setCmd(arr[0]+Const.Sym.cmd+Const.Cmd.czQ);
	    	request.setSendMsg(cmdMsg);
    	}
		return true;
	}
	@Override
	public boolean devHandle(byte[] byteMsg, Dev dev, ZNServ buzServ,
			Socket socket, InputStream in, OutputStream out) throws IOException {

		CmdMsg cmdMsg=null;
		boolean bNotify = false;
		String msg = new String(byteMsg);
        //String arr[] = msg.split(Const.Sym.cmd);
        InetAddress ip = InetAddress.getByName(dev.getInetIp());
        BlockingQueue<CmdMsg> queue = MsgQueueMgr.getQueue(dev.getDevUuid());
        ByteBuffer buffer = ByteBuffer.allocate(1024);
    	//等待手机发过来的新命令
        List<CmdMsg> list = new ArrayList<CmdMsg>();
		int qnum = 0;
		synchronized(queue){
			qnum = queue.drainTo(list);
			logger.error("queue num="+qnum);
		}
		for(int i=0; i<qnum; i++){
	    	try{
	    		bNotify = false;
	    		cmdMsg = list.get(i);
	    		if(cmdMsg==null){
        			cmdMsg = new CmdMsg();
        			cmdMsg.setValue(dev.getDevUuid(), dev.getDevUuid()+Const.Sym.cmd+Const.Cmd.czQ, null);
        			cmdMsg.setFromMobile(false);
        		}
	    		
		    	//向设备发送新命令
	    		out.write(cmdMsg.getCmd().getBytes());
	    		out.flush();
		    	logger.info("dev:ChazuoDevTcpHandle.run receive mobile msg:"+ cmdMsg.getCmd() +" and prepare sending to dev ["+dev.getDevUuid()+"] ip "+socket.getInetAddress()+":"+socket.getPort());
		    	buffer.clear();
		    	byte[] buf = buffer.array(); 
		    	int num = in.read(buf);
		    	if(num>0){
			        msg = new String(buf,0, num);
		    	}else{
		    		msg = null;
		    	}
		        //arr = msg.split(Const.Sym.cmd);
		        if(!cmdMsg.isFromMobile()) continue;
		        synchronized(cmdMsg){
		        	bNotify= true;
		        	cmdMsg.setBackMsg(msg);
			        logger.info("dev:ChazuoDevTcpHandle.run recevie dev reply:"+ cmdMsg.getBackMsg() +" from dev ["+dev.getDevUuid()+"] ip "+socket.getInetAddress()+":"+socket.getPort());
			        cmdMsg.notifyAll();
				}
		    	//准备接受新反馈
	    	}catch(SocketTimeoutException e){
	    		if(cmdMsg!=null) cmdMsg.setErrType("-2");
	    		logger.info("dev:ChazuoDevTcpHandle.run read msg from dev timeout ");
				LogUtils.log(logger, e);
			}catch (Exception e) {
	    		if(cmdMsg!=null) cmdMsg.setErrType("-2");
				LogUtils.log(logger, e);
			}finally{
				if(cmdMsg!=null&&!bNotify&&cmdMsg.isFromMobile()){
					synchronized(cmdMsg){
				       cmdMsg.notifyAll();
					}
				}
	    		cmdMsg = null;
			}
		}
		return true;
	
	}
	
}
