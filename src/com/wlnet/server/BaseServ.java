package com.wlnet.server;


import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public abstract class BaseServ {
	protected static Logger logger = Logger.getLogger(BaseServ.class);
	public SimpleJdbcTemplate jt ;
	public DataSource dataSource;
	
	
	
	public SimpleJdbcTemplate getJt() {
		return jt;
	}
	public void setJt(SimpleJdbcTemplate jt) {
		this.jt = jt;
	}
	public DataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	

	
    
}
