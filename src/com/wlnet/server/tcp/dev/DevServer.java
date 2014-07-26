package com.wlnet.server.tcp.dev;

import com.wlnet.nio.nioserver.Notifier;
import com.wlnet.socket.Server;

public class DevServer extends Server {

	public void init(){
    }
	
	public DevServer(int port, Notifier notifier) throws Exception {
		super(port, notifier);
	}

}
