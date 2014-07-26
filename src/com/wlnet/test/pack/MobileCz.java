package com.wlnet.test.pack;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.io.*;

public class MobileCz {
    public MobileCz() {
    }

    public static void main(String[] args) {
        Socket client = null;
        DataOutputStream out = null;
        DataInputStream in = null;
        try {
        	//String strIp = "122.10.81.128";
			//String strIp = "localhost";
			String strIp = "116.255.248.182";
			InetAddress ip = InetAddress.getByName(strIp);
            client = new Socket(ip,2036);
            //client.setReuseAddress(true);
           // client.bind(new InetSocketAddress(ip,2036));
            //client.setSoTimeout(10000);
            out = new DataOutputStream( (client.getOutputStream()));

            String query = "2#AA";
            byte[] request = query.getBytes();
            out.write(request);
            out.flush();
            client.shutdownOutput();

            in = new DataInputStream(client.getInputStream());
            byte[] reply = new byte[40];
            in.read(reply);
            System.out.println("MobileCz receive: " + new String(reply, "UTF-8"));

            in.close();
            out.close();
            client.close();
            
            client = new Socket(ip,2036);
            //client.setReuseAddress(true);
            //client.bind(new InetSocketAddress(ip,2036));
            client.setSoTimeout(10000);
            out = new DataOutputStream( (client.getOutputStream()));

            query = "2#02";
            request = query.getBytes();
            out.write(request);
            out.flush();
            client.shutdownOutput();

            in = new DataInputStream(client.getInputStream());
            reply = new byte[40];
            in.read(reply);
            System.out.println("MobileCz receive " + new String(reply, "UTF-8"));

            in.close();
            out.close();
            client.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);

    }

}
