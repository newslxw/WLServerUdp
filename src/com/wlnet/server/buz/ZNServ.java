package com.wlnet.server.buz;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wlnet.pojo.Dev;
import com.wlnet.pojo.Rev;
import com.wlnet.server.BaseServ;
import com.wlnet.server.utils.JtUtils;
import com.wlnet.server.utils.LogUtils;
import com.wlnet.server.utils.Utils;

@Transactional(readOnly = true)
public class ZNServ extends BaseServ{
	protected static Logger logger = Logger.getLogger(ZNServ.class);
	final static String SQL_UPD_DEV="update m_dev set inet_ip=:inetIp,inet_port=:inetPort where dev_uuid=:devUuid";
	final static String SQL_Q_DEV="select * from m_dev where dev_uuid=:devUuid";
	final static String SQL_INS_REV = "insert into m_rev(dev_uuid,measure_value,rev_time)values(:devUuid,:measureValue,:revTime)";
	final static String SQL_Q_REV_NEWEST="select * from m_rev where dev_uuid=:devUuid order by rev_time desc limit 0,1";
	final static String SQL_Q_DEV_USER = "select * from m_dev ";
	/**
	 * 保存设备发过来的数据入库
	 * @param dev 设备信息
	 * @param list
	 */
	@SuppressWarnings("unused")
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void updateData(Dev dev,Rev rev){
		if(dev==null||dev.getDevUuid()==null||rev==null||rev.getMeasureValue()==null) {
			LogUtils.log(logger, "没有指定智能设备信息！");
			return ;
		}
		Dev dev2 = JtUtils.queryOneBean(jt, SQL_Q_DEV, dev, Dev.class);
		if(dev2==null) {
			LogUtils.log(logger, "没有注册的智能设备["+dev.getDevUuid()+"]！");
			return;
		}else{
			JtUtils.update(jt, SQL_UPD_DEV, dev);
			rev.setDevUuid(dev2.getDevUuid());
			rev.setRevTime(Utils.getLocalTime(null));
			Long revId = JtUtils.insertAndGetId(jt, SQL_INS_REV, rev);
			rev.setRevId(revId);
		}
		
	}
	
	public Dev getDev(String devUuid){
		Dev dev = new Dev();
		dev.setDevUuid(devUuid);;
		Dev dev2 = JtUtils.queryOneBean(jt, SQL_Q_DEV, dev, Dev.class);
		return dev2;
	}
	
	/**
	 * 查询设备列表
	 * @return
	 */
	public List<Dev> getDevList(){
		List<Dev> list = JtUtils.queryList(jt, SQL_Q_DEV_USER, new Dev(), Dev.class);
		return list;
	}
	

	
	/**
	 * 获取最新接收的设备数据
	 * @return
	 */
	public Rev getLastRev(Dev dev){
		Rev rev = JtUtils.queryOneBean(jt, SQL_Q_REV_NEWEST, dev, Rev.class);
		return rev;
	}
	
	
	
}
