package com.wlnet.server.tcp.dev;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.channels.SocketChannel;

import com.wlnet.nio.nioserver.Request;
import com.wlnet.nio.nioserver.Response;
import com.wlnet.nio.nioserver.Writer;
import com.wlnet.pojo.Dev;
import com.wlnet.server.buz.ZNServ;

public interface DevHandle {
	boolean devHandle(byte[] byteMsg,Dev dev, ZNServ buzServ,Request request) throws IOException;

	boolean devHandle(byte[] byteMsg, Dev dev, ZNServ buzServ, Socket socket,
			InputStream in, OutputStream out) throws IOException;
}
