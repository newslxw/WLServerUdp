package com.wlnet.nio.nioserver.test;


import java.util.*;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.text.DateFormat;

import com.wlnet.nio.nioserver.Reader;
import com.wlnet.nio.nioserver.Request;
import com.wlnet.nio.nioserver.Response;
import com.wlnet.nio.nioserver.event.EventAdapter;

/**
 * 时间查询服务器
 */
public class TimeHandler extends EventAdapter {
    public TimeHandler() {
    }

    public void onWrite(SocketChannel sc,Request request, Response response) throws Exception {
        String command = new String(request.getDataInput());
        String time = null;
        Date date = new Date();

        // 判断查询命令
        if (command.equals("GB")) {
            // 中文格式
            DateFormat cnDate = DateFormat.getDateTimeInstance(DateFormat.FULL,
                DateFormat.FULL, Locale.CHINA);
            time = cnDate.format(date);
        }
        else {
            // 英文格式
            DateFormat enDate = DateFormat.getDateTimeInstance(DateFormat.FULL,
                DateFormat.FULL, Locale.US);
            time = enDate.format(date);
        }

        response.send(time.getBytes());
    }

	@Override
	public void onConnected(Socket socket) {
		// TODO Auto-generated method stub
		
	}

}
