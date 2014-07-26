package com.wlnet.test.pack;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.io.*;

public class WenduTcp {
	private static int count = 0;
    public WenduTcp() {
    }

    public static void main(String[] args) throws IOException {
        Socket client = null;
        DataOutputStream out = null;
        DataInputStream in = null;
        String cmds[]={"S#1#20,0.4,17,20,1,23.1200490000,113.3076500000","S#1#20,0.4,17,20,1,23.0957580000,113.3432680000","S#1#20,0.4,17,20,3,23.1223490000,113.3968790000"};
        int i=0;
        try{
        while(true){
	        try {
	        	//String strIp = "122.10.81.128";
	        	//String strIp="116.255.248.182";
	        	String cmd = cmds[count%3];
				String strIp = "localhost";
				InetAddress ip = InetAddress.getByName(strIp);
	            client = new Socket(ip,2034);
	            client.setReuseAddress(true);
	           // client.bind(new InetSocketAddress(ip,2036));
	            client.setSoTimeout(10000);
	            out = new DataOutputStream( (client.getOutputStream()));
	            String query = cmd;
	            byte[] request = query.getBytes();
	            out.write(request);
	            out.flush();
	            System.out.println(count+"send msg: " + query);
	            count++;
	            Thread.sleep(10000);
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	        }
        }
        }finally{
        	if(null!=client)client.shutdownOutput();
            if(null!=out)out.close();
            if(null!=client)client.close();
        }
        

    }

}
