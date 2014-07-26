package com.wlnet.server.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.Logger;

/**
 * 错误日志管理工具类
 * @author xlian
 *
 */
public final class LogUtils
{

	/**
	 * 记录日志
	 * @param logger
	 * @param e
	 */
	public static void log(Logger logger, Exception e)
	{
		StringWriter writer = new StringWriter();
		e.printStackTrace(new PrintWriter(writer,true));
		logger.error(writer.toString());
	}
	
    public static void log(Logger logger, Exception e, String msg)
    {
    	StringWriter writer = new StringWriter();
		e.printStackTrace(new PrintWriter(writer,true));
    	logger.error(msg + "\r\n" + writer.toString());
    }

	public static void log(Logger log, String msg) {
		log.error(msg);
	}
	
}
