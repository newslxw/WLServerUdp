package com.wlnet.server.utils;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class JtUtils {

	/**
	 * ִ��insert��������������ID
	 * @param sql
	 * @param obj
	 * @return
	 */
	public static <T> Long insertAndGetId(SimpleJdbcTemplate jt, String sql, T obj){
		SqlParameterSource ps=new BeanPropertySqlParameterSource(obj);
		KeyHolder key=new GeneratedKeyHolder();
		jt.getNamedParameterJdbcOperations().update(sql, ps, key);
		return key.getKey().longValue();
	}
	
	/**
	 * ��bean��ʽִ��sql
	 * @param sql ��ʽ insert into table(dev_id)values(:devId) devId�����Ա����
	 * @param obj
	 * @return
	 */
	public static <T> Integer update(SimpleJdbcTemplate jt,String sql, T obj){
		SqlParameterSource ps=new BeanPropertySqlParameterSource(obj);
		return jt.getNamedParameterJdbcOperations().update(sql, ps);
	}
	
	/**
	 * �������н���б�
	 * @param jt
	 * @param sql ��ʽ select * from m_dev where dev_id=:devId devId�����Ա����
	 * @param cond ��ѯ��Bean
	 * @param returnType ���ص�bean����
	 * @return 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> List<T> queryList(SimpleJdbcTemplate jt,String sql, Object cond, Class<T> returnType){
		SqlParameterSource ps=new BeanPropertySqlParameterSource(cond); 
		return jt.getNamedParameterJdbcOperations().query(sql, ps, new BeanPropertyRowMapper(returnType)); 
	}
	
	/**
	 * �������н���б�
	 * @param jt
	 * @param sql ��ʽ select * from m_dev where dev_id=:devId devId�����Ա����
	 * @param cond ��ѯ��Bean
	 * @param returnType ���ص�bean����
	 * @return 
	 */
	public static <T> T queryOneBean(SimpleJdbcTemplate jt,String sql, Object cond, Class<T> returnType){
		List<T> list = JtUtils.queryList(jt, sql, cond, returnType);
		if(list==null||list.size()==0) return null;
		return list.get(0);
	}
	
}
