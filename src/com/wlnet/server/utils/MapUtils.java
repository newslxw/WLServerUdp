package com.wlnet.server.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * @author xwlian
 *
 */
public final class MapUtils {
	
	private static Logger log = Logger.getLogger(MapUtils.class);
	
	/**
	 * 从map中返回String类型的值
	 * @param map
	 * @param key
	 * @return
	 */
	public static String getString(final Map map, String key){
		if(map == null || key == null || !map.containsKey(key)) return null;
		return TCUtils.toStr(map.get(key));
	}
	
	public static Date getDate(final Map map, String key){
		if(map == null || key == null || !map.containsKey(key)) return null;
		return TCUtils.toDate(map.get(key));
	}
	
	public static Integer getInteger(final Map map, String key){
		if(map == null || key == null || !map.containsKey(key)) return null;
		return TCUtils.toInteger(map.get(key));
	}
	
	public static BigDecimal getBigDecimal(final Map map, String key){
		if(map == null || key == null || !map.containsKey(key)) return null;
		return TCUtils.toBigDecimal(map.get(key));
	}
	

	public static Boolean getBoolean(final Map map, String key){
		if(map == null || key == null || !map.containsKey(key)) return null;
		return TCUtils.toBoolean(map.get(key));
	}
	
	public static void putNotNull(final Map map, String key, Object obj){
		if(obj != null) map.put(key, obj);
	}
	
	
	public static Map bean2Map(Object obj, Map map){
		if(map == null) map = new HashMap();
		try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
            	String prop = property.getName();
                String key = fieldClass2Db(prop);

//                if (map.containsKey(key)) {
                    // 得到property对应的setter方法
                    Method reader = property.getReadMethod();
                    if(reader == null){
                    	LogUtils.log(log,"bean2Map运行日志,属性："+prop +" 没有reader方法 ");
                    	continue;
                    }
                    MapUtils.putNotNull(map,key,reader.invoke(obj, null));
                /*}*/

            }

        } catch (Exception e) {
            LogUtils.log(log, e);
        }
		return map;
	}
	
	public static <T> T map2Bean(Map map,T obj){
		try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
            	String prop = property.getName();
                String key = fieldClass2Db(prop);

                if (map.containsKey(key)) {
                    Object value = map.get(key);
                    // 得到property对应的setter方法
                    Method setter = property.getWriteMethod();
                    if(setter == null){
                    	LogUtils.log(log,"属性："+prop +" 没有writer方法，无法注入map ");
                    	continue;
                    }
                    if(value == null){
                    	 setter.invoke(obj, value);
                    	 continue;
                    }
                    Class types[] = setter.getParameterTypes();
                    Class requiredType = types[0];
                    if (String.class.equals(requiredType)) {
                    	setter.invoke(obj, TCUtils.toStr(value));
            		}
            		else if (boolean.class.equals(requiredType) || Boolean.class.equals(requiredType)) {
            			setter.invoke(obj, TCUtils.toBoolean(value));
            		}
            		else if (byte.class.equals(requiredType) || Byte.class.equals(requiredType)) {
            			setter.invoke(obj, new Byte(TCUtils.toStr(value)));
            		}
            		else if (short.class.equals(requiredType) || Short.class.equals(requiredType)) {
            			setter.invoke(obj, new Short(TCUtils.toStr(value)));
            		}
            		else if (int.class.equals(requiredType) || Integer.class.equals(requiredType)) {
            			setter.invoke(obj, TCUtils.toInteger(value));
            		}
            		else if (long.class.equals(requiredType) || Long.class.equals(requiredType)) {
            			setter.invoke(obj, new Long(TCUtils.toStr(value)));
            		}
            		else if (float.class.equals(requiredType) || Float.class.equals(requiredType)) {
            			setter.invoke(obj, new Float(TCUtils.toStr(value)));
            		}
            		else if (double.class.equals(requiredType) || Double.class.equals(requiredType) ||
            				Number.class.equals(requiredType)) {
            			setter.invoke(obj, new Double(TCUtils.toStr(value)));
            		}
            		else if (byte[].class.equals(requiredType)) {
            			
            			setter.invoke(obj, TCUtils.toStr(value).getBytes());
            		}
            		else if(types[0] == java.util.Date.class){
                    	setter.invoke(obj, TCUtils.toDate(value));
                    }
                    else if(types[0] == BigDecimal.class){
                    	setter.invoke(obj, TCUtils.toBigDecimal(value));
                    }
                    else{
                    	setter.invoke(obj, value);
                    }
                }

            }

        } catch (Exception e) {
            LogUtils.log(log, e);
        }
		return obj;
	}
	
	/**
	 * 根据自动名称生成属性
	 * @param name
	 * @return
	 */
	public static String fieldDb2Class(String name){
		String arr[]=name.split("_");
		if(arr.length == 1) return name;
		String ret = arr[0];
		for(int i=1; i<arr.length; i++){
			ret = ret + arr[i].substring(0,1).toUpperCase()+arr[i].substring(1);
		}
		return ret;
	}
	
	/**
	 * 自动根据属性生成字段名称
	 * @param name
	 * @return
	 */
	public static String fieldClass2Db(String name){
		StringBuffer sb = new StringBuffer(name.length()+5);
		char ch ;
		for(int i=0; i<name.length(); i++){
			ch = name.charAt(i);
			if(Character.isUpperCase(ch)&&i>0) sb.append("_");
			sb.append(Character.toLowerCase(ch));
		}
		return sb.toString();
	}
	
	
	
}
