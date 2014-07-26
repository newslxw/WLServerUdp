package com.wlnet.test.pack;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.*;

import com.wlnet.server.utils.StreamUtils;

public class DevTcpCz {
	private static int count = 0;
    public DevTcpCz() {
    }

    public static void main(String[] args) throws IOException {
        Socket client = null;
        DataOutputStream out = null;
        DataInputStream in = null;
        int i=0;
        String cmds[]={"2#AA", "2#55"};

    	String strIp = "122.10.81.128";
		//String strIp = "localhost";
		//String strIp = "119.129.95.4";
		InetAddress ip = InetAddress.getByName(strIp);
	    while(true){
	        try {
	        	client = new Socket(ip,2037);
	            client.setReuseAddress(true);
	           // client.bind(new InetSocketAddress(ip,2036));
	            client.setSoTimeout(5000);
	            out = new DataOutputStream( (client.getOutputStream()));
	            in = new DataInputStream(client.getInputStream());
	
	        	String query = "2#AA";
	            byte[] request = query.getBytes();
	            out.write(request);
	            out.flush();
	            System.out.println(count+".send msg: " + query);
	            
	        	String cmd = cmds[count%2];
	            byte[] reply = new byte[40];
	            query = cmd;
	            in.read(reply);
	            System.out.println(count+".receive msg: " + new String(reply, "UTF-8"));
	            
	            query = cmd;
	            request = query.getBytes();
	            out.write(request);
	            out.flush();
	            System.out.println(count+".send msg: " + query);
	            count++;
	            Thread.sleep(2000);
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	        }finally{
	        	StreamUtils.closeStream(in);
	        	StreamUtils.closeStream(out);
	        	StreamUtils.closeSocket(client);
	        }
	    }

    }

}
