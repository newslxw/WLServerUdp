package com.wlnet.server.utils;

import java.io.BufferedReader;   
import java.io.InputStreamReader;   
import java.util.regex.Matcher;   
import java.util.regex.Pattern;   

import org.apache.log4j.Logger;

  
/**  
 * 根据ip获取MAC地址.  
 */  
public class GetMacFromIp {   
  
	private static Logger logger = Logger.getLogger(GetMacFromIp.class);
	private static String localIP[] ={"localhost","127.0.0.1",""};
	static
	{
		localIP[2] = 	LocalIPMacInfo.getLocalIP();
	}
	
    /**  
     *  
     * @param cmd  第一个命令  
     * @param another 第二个命令  
     * @param ip  目标ip,一般在局域网内  
     * @param macSeparator mac分隔符号  
     * @return   mac  
     */  
    public static String callCmd(String[] cmd,String[] another,final String ip, final String macSeparator) {   
        String result = "";   
        String line = "";   
        try {   
            Runtime rt = Runtime.getRuntime();   
            Process proc = rt.exec(cmd);   
            int exitVal = proc.waitFor();  //已经执行完第一个命令，准备执行第二个命令   
            proc = rt.exec(another);   
            InputStreamReader is = new InputStreamReader(proc.getInputStream());   
            BufferedReader br = new BufferedReader (is);   
            while ((line = br.readLine ()) != null) {   
            	int pos = line.indexOf(ip);
            	if(pos == -1) continue;
            	String regExp = "((([0-9,A-F,a-f]{1,2}" + macSeparator + "){1,5})[0-9,A-F,a-f]{1,2})";   
                Pattern pattern = Pattern.compile(regExp);   
                Matcher matcher = pattern.matcher(line);
                if(matcher.find())
                {
                	result = matcher.group(1);
                	break;
                }
            }   
        }   
        catch(Exception e) {   
        	LogUtils.log(logger, e);  
        }   
        result = result.replace(macSeparator, "");
        return result;   
    }   

  
    /**  
     *  
     * @param ip 目标ip  
     * @return   Mac Address  
     *  
     */  
    public static String getMacInWindows(final String ip){   
        String result = "";   
        String[] cmd = {   
                "cmd",   
                "/c",   
                "ping " +  ip   
                };   
        String[] another = {   
                "cmd",   
                "/c",   
                "arp -a"  
                };   
  
        result = callCmd(cmd,another, ip, "-");   
  
        return result;   
    }   
  
  
    /**  
    *  
    * @param ip 目标ip  
    * @return   Mac Address  
    *  
    */  
    public static String getMacInLinux(final String ip){   
        String result = ""; 
        /*
        String[] cmd = {   
                "/bin/sh",   
                "-c",   
                "ping " +  ip + " -c 2 && arp -a"  
                };   
                */
        String[] cmd = {   
                "/bin/sh",   
                "-c",   
                " ping " +  ip + " -c 2 "  
                };   
        String[] other = {   
                "/bin/sh",   
                "-c",   
                " arp -a"  
                };   

         result = callCmd(cmd, other, ip, ":");   
  
        return result;   
    }   
  
    public static String getMac( String ip)
    {
    	boolean bLocal = false;
    	//ip = "10.67.6.82";
    	for(int i=0; i<localIP.length;i++)
    	{
    		if(localIP[i].equals(ip))
    		{
    			bLocal = true;
    			break;
    		}
    	}
    	if(ip == null) bLocal = true;
    	if(bLocal)
    	{
			return 	LocalIPMacInfo.getMacAddr();
    	}
    	else
    	{
	    	if(Utils.isLinux())
	    		return getMacInLinux(ip);
	    	else 
	    		return getMacInWindows(ip);
    	}
    		
    }
  
    /**  
    * 测试  
    */  
    public static void main(String[] args) { 
    	  String ip = null;
    	  if(args!=null && args.length > 0)
    		  ip = args[0];
          if(Utils.isLinux())
          {
        	  System.out.println(getMac(ip));
          }
          else
          {
        	  System.out.println(getMac(ip));
          }
    }   
  
}  
