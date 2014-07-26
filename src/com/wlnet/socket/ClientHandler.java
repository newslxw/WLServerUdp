package com.wlnet.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.apache.log4j.Logger;

import com.wlnet.nio.nioserver.Notifier;
import com.wlnet.server.utils.LogUtils;

public class ClientHandler extends Thread {
	
	private static Logger logger = Logger.getLogger(ClientHandler.class);
	private Socket socket;
    protected Notifier notifier;
	
	ClientHandler(Socket socket, Notifier notifier){
		this.socket = socket;
		this.notifier = notifier;
	}
	
	
	public void run() {
		try {
			this.notifier.fireOnConnected(socket);
		} catch (Exception e) {
			LogUtils.log(logger, e);
		}
	}
	
    /**
     * 读取客户端发出请求数据
     * @param sc 套接通道
     */
    private static int BUFFER_SIZE = 1024;
    public static byte[] readRequest(SocketChannel sc) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        int off = 0;
        int r = 0;
        byte[] data = new byte[BUFFER_SIZE * 10];

        while ( true ) {
            buffer.clear();
            r = sc.read(buffer);
            if (r == -1) {
            	break ;
            }
            if (r == 0 ) break;
            if ( (off + r) > data.length) {
                data = grow(data, BUFFER_SIZE * 10);
            }
            byte[] buf = buffer.array();
            System.arraycopy(buf, 0, data, off, r);
            off += r;
        }
        byte[] req = new byte[off];
        System.arraycopy(data, 0, req, 0, off);
        return req;
    }
    
    /**
     * 数组扩容
     * @param src byte[] 源数组数据
     * @param size int 扩容的增加量
     * @return byte[] 扩容后的数组
     */
    public static byte[] grow(byte[] src, int size) {
        byte[] tmp = new byte[src.length + size];
        System.arraycopy(src, 0, tmp, 0, src.length);
        return tmp;
    }
    
}
