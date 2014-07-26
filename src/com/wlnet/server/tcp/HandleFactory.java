/**
 * 
 */
package com.wlnet.server.tcp;

import java.util.HashMap;
import java.util.Map;

import com.wlnet.server.tcp.dev.DevHandle;
import com.wlnet.server.tcp.dev.impl.ChazuoDevTcpHandle;
import com.wlnet.server.tcp.dev.impl.WenduDevHandle;
import com.wlnet.server.tcp.mobile.MobileHandle;
import com.wlnet.server.tcp.mobile.impl.ChazuoHandle;
import com.wlnet.server.tcp.mobile.impl.ChazuoTcpHandle;
import com.wlnet.server.tcp.mobile.impl.WenduHandle;

/**
 * @author Administrator
 *
 */
public class HandleFactory {

	private static final Map<String,MobileHandle> handleMap = new HashMap<String,MobileHandle>();
	private static final Map<String,DevHandle> devMap = new HashMap<String,DevHandle>();
	public  static MobileHandle getHandle(String devType){
		MobileHandle handle = handleMap.get(devType);
		if(handle == null){
			synchronized(handleMap){
				if(handle == null){
					if("test".equals(devType)){
						handle = new WenduHandle();
						handleMap.put(devType, handle);
					}else if("chazuo".equals(devType)){
						handle = new ChazuoHandle();
						handleMap.put(devType, handle);
					}
				}
			}
		}
		return handle;
	}
	
	public  static DevHandle getDevHandle(String devType){
		DevHandle handle = devMap.get(devType);
		if(handle == null){
			synchronized(devMap){
				if(handle == null){
					if("test".equals(devType)){
						handle = new WenduDevHandle();
						devMap.put(devType, handle);
					}else if("chazuo".equals(devType)){
						handle = new ChazuoDevTcpHandle();
						devMap.put(devType, handle);
					}
				}
			}
		}
		return handle;
	}
	
}
