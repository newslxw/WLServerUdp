package com.wlnet.server.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;


public class TCUtils {

	public final static String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static String[] datePattern = { "yyyy-MM-dd","yyyyMMdd","yyyy-MM-dd HH:mm:ss","yyyyMMddHHmmss" };
	protected static Logger log = Logger.getLogger(TCUtils.class);
	
	/**
	 * 转换成字符串
	 * @param obj
	 * @return
	 */
	public static String toStr(Object obj){
		if(obj==null) return null;
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
		if(obj instanceof java.util.Date ){
			return sdf.format(obj);
		}
		return obj.toString();
	}
	
	public static Integer toInteger(Object obj){
		if(obj == null) return null;
		if(obj instanceof Integer){
			return (Integer)obj;
		}
		//把true返回1，false返回0
		if(obj instanceof String && ("true".equalsIgnoreCase(obj.toString()) || "false".equalsIgnoreCase(obj.toString()))){
			return "true".equalsIgnoreCase(obj.toString()) ? 1:0;
		}
		Integer ret = null;
		try{
			String str = toStr(obj).trim();
			if("".equals(str))return null;
			ret = Integer.parseInt(str);
		}catch(NumberFormatException nfe){
			ret = null;
			LogUtils.log(log, nfe);
			
		}
		return ret;
	}
	
	public static Date toDate(Object obj){
		if(obj == null) return null;
		if(obj instanceof Date){
			return (Date)obj;
		}
		Date ret = null;
		String str = toStr(obj);
		String pattern = matchDatePattern(str);
		if(pattern !=null ){
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			try {
				ret = sdf.parse(str);
			} catch (ParseException e) {
				ret = null;
				LogUtils.log(log, e);
			}
		}
		return ret;
	}
	
	public static BigDecimal toBigDecimal(Object obj){
		if(obj == null) return null;
		if(obj instanceof BigDecimal){
			return (BigDecimal)obj;
		}
		BigDecimal ret = null;
		String str = toStr(obj);
		try{
			ret = new BigDecimal(str);
		}catch(NumberFormatException nfe){
			ret = null;
			LogUtils.log(log, nfe);
		}
		return ret;
	}
	
	public static Boolean toBoolean(Object obj){
		if(obj == null) return null;
		if(obj instanceof Boolean){
			return (Boolean)obj;
		}
		String str = toStr(obj);
		return Boolean.parseBoolean(str);
	}
	
	private static String matchDatePattern(String strDate){
		int pos1=strDate.indexOf('-');
		int pos2 = strDate.indexOf(':');
		if(pos1>-1&&pos2>-1) return datePattern[2];
		if(pos1==-1&&pos2==-1) return datePattern[strDate.length()==14?3:1];
		if(pos1>-1&&pos2==-1) return datePattern[0];
		return null;
	}
}
