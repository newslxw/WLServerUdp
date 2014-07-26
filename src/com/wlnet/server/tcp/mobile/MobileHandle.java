package com.wlnet.server.tcp.mobile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.channels.SocketChannel;

import com.wlnet.nio.nioserver.Request;
import com.wlnet.nio.nioserver.Response;
import com.wlnet.pojo.Dev;
import com.wlnet.server.buz.ZNServ;

public interface MobileHandle {
	boolean mobileHandle(byte[] byteMsg,Dev dev, ZNServ buzServ,Request request, Response response) throws IOException;

	boolean mobileHandle(byte[] byteMsg, Dev dev, ZNServ buzServ, Socket socket, InputStream in, OutputStream out) throws IOException;

}
