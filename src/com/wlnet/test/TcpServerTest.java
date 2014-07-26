package com.wlnet.test;

import java.net.InetAddress;
import java.net.Socket;
import java.io.*;

public class TcpServerTest {
    public TcpServerTest() {
    }

    public static void main(String[] args) {
        Socket client = null;
        DataOutputStream out = null;
        DataInputStream in = null;
        try {
        	//String strIp = "122.10.81.128";
			String strIp = "192.168.1.8";
			InetAddress ip = InetAddress.getByName(strIp);
            client = new Socket(ip,2036);
            client.setSoTimeout(10000);
            out = new DataOutputStream( (client.getOutputStream()));

            String query = "lastrev#1";
            byte[] request = query.getBytes();
            out.write(request);
            out.flush();
            client.shutdownOutput();

            in = new DataInputStream(client.getInputStream());
            byte[] reply = new byte[40];
            in.read(reply);
            System.out.println("Time: " + new String(reply, "UTF-8"));

            in.close();
            out.close();
            client.close();
            
            client = new Socket("localhost",2036);
            client.setSoTimeout(10000);
            out = new DataOutputStream( (client.getOutputStream()));

            query = "devlist";
            request = query.getBytes();
            out.write(request);
            out.flush();
            client.shutdownOutput();

            in = new DataInputStream(client.getInputStream());
            reply = new byte[40];
            in.read(reply);
            System.out.println("Time: " + new String(reply, "UTF-8"));

            in.close();
            out.close();
            client.close();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}
