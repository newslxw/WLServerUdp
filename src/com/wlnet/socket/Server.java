package com.wlnet.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.wlnet.nio.nioserver.Notifier;
import com.wlnet.server.utils.LogUtils;


public class Server implements Runnable {
	protected static Logger logger = Logger.getLogger(Server.class);
    private InetSocketAddress address;
    private int port;
    private ServerSocket serverSocket ;
    protected Notifier notifier;
    
    public Server(int port, Notifier notifier) {
        this.port = port;
        this.notifier = notifier;
    }
    
    public void run() {
    	logger.info("Server started ...");
    	logger.info("Server listening on port: " + port);
        address = new InetSocketAddress(port);
        try {
			serverSocket = new ServerSocket(this.port);
			serverSocket.setReuseAddress(true);
			while(true){
	        	try{
	        		Socket client = serverSocket.accept();
	        		ClientHandler clh = new ClientHandler(client, this.notifier);
	        		this.notifier.fireOnAccepted(client);
	        		clh.start();
	        	}catch(Exception e){
	        		LogUtils.log(logger, e);
	        	}
	        }
		} catch (IOException e1) {
			LogUtils.log(logger, e1);
		}
        
        
    	
    }
	
}
