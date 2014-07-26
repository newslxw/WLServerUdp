/**
 * 
 */
package com.wlnet.server.tcp.mobile.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;

import org.apache.log4j.Logger;

import com.wlnet.nio.nioserver.Request;
import com.wlnet.nio.nioserver.Response;
import com.wlnet.pojo.Dev;
import com.wlnet.pojo.Rev;
import com.wlnet.server.buz.ZNServ;
import com.wlnet.server.tcp.CmdMsg;
import com.wlnet.server.tcp.Const;
import com.wlnet.server.tcp.mobile.MobileHandle;
import com.wlnet.server.udp.ServerMsgBack;
import com.wlnet.server.utils.LogUtils;

/**
 * 智能开关手机控制器，以后要实现在同一局域网内可以不经过服务器直接发送和查询插座状态
 * @author xinwen
 *
 */
public class ChazuoTcpHandle implements MobileHandle {
//0A 01 01 01 AA A1 0B
	/*byte[] ctrlOpen = {0X0A, 0X01, 0X01, 0X01, -0X56,-0X5F, 0X0B};
	byte[] ctrlStop = {0X0A, 0X01, 0X01, 0X01,  0X55, 0X5E, 0X0B};
	byte[] qStatus =  {0X0A, 0X02, 0X01, 0X01,  0X00, 0X08, 0X0B};
	byte[] stuOpen =  {0X0A, 0X03, 0X01, 0X01, -0X56,-0X5D, 0X0B};
	byte[] stuStop =  {0X0A, 0X03, 0X01, 0X01,  0X55, 0X5C, 0X0B};*/
	//2#AA 2#55 2#02
	protected static Logger logger = Logger.getLogger(ChazuoTcpHandle.class);
	@Override
	public boolean mobileHandle(byte[] byteMsg, Dev dev, ZNServ buzServ,
			Request request, Response response) throws IOException {
        String devUUid = dev.getDevUuid();
        CmdMsg cmdMsg=null;
        logger.info("mobile:ChazuoTcpHandle.handle receive mobile msg "+ new String(byteMsg)+ " and waiting for dev reply ");
		cmdMsg = ServerMsgBack.handMsg( byteMsg, devUUid);
		byte buf[] ;
        if(cmdMsg.getBackMsg()==null||!"1".equals(cmdMsg.getErrType())){
        	buf = cmdMsg.getErrType().getBytes();
        	logger.info("mobile:ChazuoTcpHandle.handle receive reply timeout ["+cmdMsg.getErrType()+"] from dev ["+cmdMsg.getDevUUid()+"] to ip :"+request.getAddress()+" port:"+request.getPort());
        }else{
        	buf = cmdMsg.getBackMsg().getBytes(); 
        	logger.info("mobile:ChazuoTcpHandle.handle receive reply ["+cmdMsg.getBackMsg()+"] from dev ["+cmdMsg.getDevUUid()+"] and send to ip :"+request.getAddress()+" port:"+request.getPort());
        }
        response.send(buf);
		return true;
	}
	@Override
	public boolean mobileHandle(byte[] byteMsg, Dev dev, ZNServ buzServ,
			Socket socket, InputStream in, OutputStream out) throws IOException {
		String devUUid = dev.getDevUuid();
        CmdMsg cmdMsg=null;
        logger.info("mobile:ChazuoTcpHandle.handle receive mobile msg "+ new String(byteMsg)+ " and waiting for dev reply ");
		cmdMsg = ServerMsgBack.handMsg( byteMsg, devUUid);
		byte buf[] ;
        if(cmdMsg.getBackMsg()==null||!"1".equals(cmdMsg.getErrType())){
        	buf = cmdMsg.getErrType().getBytes();
        	logger.info("mobile:ChazuoTcpHandle.handle receive reply timeout ["+cmdMsg.getErrType()+"] from dev ["+cmdMsg.getDevUUid()+"] to ip :"+ socket.getInetAddress() +" port:"+socket.getPort());
        }else{
        	buf = cmdMsg.getBackMsg().getBytes(); 
        	logger.info("mobile:ChazuoTcpHandle.handle receive reply ["+cmdMsg.getBackMsg()+"] from dev ["+cmdMsg.getDevUUid()+"] and send to ip :"+ socket.getInetAddress() +" port:"+socket.getPort());
        }
        out.write(buf);
        out.flush();
		return true;
	}
	
}
