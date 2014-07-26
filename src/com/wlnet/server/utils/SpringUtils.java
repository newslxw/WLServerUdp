package com.wlnet.server.utils;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.orm.ibatis.SqlMapClientFactoryBean;
import org.springframework.orm.ibatis.SqlMapClientTemplate;



/**
 * Spring 
 * @author brand
 * 2010-5-27
 */
public final class SpringUtils
{
    private static ApplicationContext ctx = null;
    private static Logger logger = Logger.getLogger(SpringUtils.class);
    static
    {
    	//String configPath = Config.get
    	 ctx = new ClassPathXmlApplicationContext("classpath:conf/spring/spring*.xml");
    	 //ctx = new ClassPathXmlApplicationContext("/config/spring/spring*.xml",SpringUtils.class);
    }
    
    public static Object getBean(String beanId)
    {
        return ctx.getBean(beanId);
    }
    
    public static void init()
    {
    	
    }
    
    
    /**
     * 获取ibatis连接
     * @return
     */
    public static DataSource getDataSource()
    {
    	DataSource fb = (DataSource)ctx.getBean(SpringID.SYNDATASOURCE);
    	return fb;
    }
    

	
	
    
}

