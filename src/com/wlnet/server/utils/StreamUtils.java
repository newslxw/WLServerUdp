package com.wlnet.server.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.log4j.Logger;

public class StreamUtils {

	private static Logger logger = Logger.getLogger(StreamUtils.class);
	
	public static void closeStream(InputStream in){
		if(in!=null){
			try {
				in.close();
			} catch (IOException e) {
				LogUtils.log(logger, e);
			}
		}
		
	}
	
	public static void closeStream(OutputStream out){
		if(out!=null){
			try {
				out.close();
			} catch (IOException e) {
				LogUtils.log(logger, e);
			}
		}
		
	}
	
	public static void closeSocket(Socket socket){
		if(socket!=null){
			try {
				socket.close();
			} catch (IOException e) {
				LogUtils.log(logger, e);
			}
		}
		
	}
}
