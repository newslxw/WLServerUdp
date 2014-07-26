package com.wlnet.server.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;


public class Utils
{
	private static Logger logger = Logger.getLogger(Utils.class);

	/**
	 * ��ȡ��������ǰʱ�������ʽYYYYMMDDHH24MISS
	 * @param d  ʱ��
	 * @return
	 */
	public static String getLocalTime(Date d)
	{
		Date tmp = d;
		if (tmp == null)
			tmp = new Date();
		String ret = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		ret = df.format(tmp);
		return ret;
	}

	/**
	 * ����ʽΪyyyyMMddHHmmss��14λ��ʱ���ַ���ת��ΪDate
	 * @param date yyyyMMddHHmmss��14λ��ʱ��
	 * @return
	 */
	public static Date parse2Time(String date)
	{
		Date d = null;
		try
		{
			if (date.length() == 8)
				date = date + "000000";
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			d = df.parse(date);
		}
		catch (ParseException e)
		{
			LogUtils.log(logger, e);
		}
		return d;
	}


	/**
	 * ��ȡ��serverTime����ǰ���ڵ����������ַ�������ʽ8λ����
	 * @param serverTime 14λ���ָ�ʽ���ַ�������ʽYYYYMMDDHHMISS
	 * @return
	 */
	public static List<String> getDatesBetween(String serverTime)
	{
		List<String> list = new ArrayList<String>();
		Date d = Utils.parse2Time(serverTime);
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		Calendar ical = Calendar.getInstance();
		Calendar ccal = Calendar.getInstance();
		ccal.setTime(d);
		ccal.set(Calendar.YEAR, ical.get(Calendar.YEAR));
		ccal.set(Calendar.MONTH, ical.get(Calendar.MONTH));
		ccal.set(Calendar.DAY_OF_MONTH, ical.get(Calendar.DAY_OF_MONTH));
		while (cal.before(ccal))
		{
			Date td = cal.getTime();
			list.add(Utils.getLocalTime(td).substring(0, 8));
			cal.add(Calendar.DATE, 1);
		}

		return list;
	}

	

	/**
	 * �������������
	 * @return
	 */
	public static Date yesterday()
	{
		Date d = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * �жϲ���ϵͳ
	 * @return
	 */
	public static boolean isLinux()
	{
		boolean isLinux = true;
		/*
		 * URL resource = Utils.class.getResource("Utils.class"); String
		 * classPath = resource.getPath(); String className =
		 * Utils.class.getName().replace(".", "/") + ".class"; String
		 * classesPath = classPath.substring(0, classPath.indexOf(className));
		 * if( System.getProperty("os.name").toUpperCase().indexOf("WINDOWS") !=
		 * -1 ) { classesPath = classesPath.substring(1); isLinux = false; }
		 */
		String os = System.getProperty("os.name").toLowerCase();
		if (os != null)
			os = os.toLowerCase();
		if (os.startsWith("windows"))
		{
			isLinux = false;
		}
		return isLinux;
	}
	
	

}
