package com.wlnet.server.tcp;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import com.wlnet.server.utils.Utils;

public class MsgQueueMgr {

	private static int MAX_MSG_SIZE=200;
	private static Map<String, BlockingQueue<CmdMsg>> map = new HashMap<String, BlockingQueue<CmdMsg>>();
	private static Map<String, CmdMsg> cmdMap = new HashMap<String, CmdMsg>();
	
	
	
	public static Map<String, CmdMsg> getCmdMap() {
		return cmdMap;
	}
	
	public static void remove(String key){
		cmdMap.remove(key);
	}
	
	/**
	 * 返回22位长度的key 格式： R.<HHMMSS>.<10位长度uuid>.<3位并发编号>
	 * @param cmdMsg
	 * @return
	 */
	public static String put(CmdMsg cmdMsg){
		String id = cmdMsg.getDevUUid();
		while(id.length()<10){
			id="0"+id;
		}
		String time = Utils.getLocalTime(null).substring(8);
		String keyb = "R."+ time +"."+id;
		int nc=0;
		String sc = "00"+nc;
		String key = keyb +"."+sc; 
		synchronized(cmdMap){
			while(cmdMap.containsKey(key)){
				nc++;
				if(nc<10) sc = "00"+nc;
				else if(nc>=10&&nc<100) sc = "0"+nc;
				key = keyb +"."+sc; 
			}
			cmdMap.put(key, cmdMsg);
		}
		cmdMsg.setKey(key);
		return key;
	}
	
	public static CmdMsg get(String key){
		return cmdMap.get(key);
	}



	public static synchronized BlockingQueue<CmdMsg> getQueue(String devUUid){
		BlockingQueue<CmdMsg> queue;
		if(map.containsKey(devUUid)){
			queue = map.get(devUUid);
		}else{
			queue = new ArrayBlockingQueue<CmdMsg>(MAX_MSG_SIZE);
			map.put(devUUid, queue);
		}
		return queue;
	}
	
	
	
	
}
