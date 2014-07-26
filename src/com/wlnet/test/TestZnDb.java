package com.wlnet.test;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.wlnet.pojo.Dev;
import com.wlnet.pojo.Rev;
import com.wlnet.server.buz.ZNServ;
import com.wlnet.server.utils.MapUtils;
import com.wlnet.server.utils.SpringUtils;

public class TestZnDb
{

	/**
	 * @param args
	 */
	public static void main(String []args)
	{
		//IBuzConfigServ buzServ = (IBuzConfigServ)SpringUtils.getBean("buzConfigServ");
	
		ZNServ serv = (ZNServ)SpringUtils.getBean("znServ");
		List<Dev> devs = serv.getDevList();
		Dev dev = devs.get(0);
		dev.setMeasureTime("20140110003000");
		Rev rev = new Rev();
		rev.setMeasureValue("20140110211800,20,0.4,17,20,1");
		serv.updateData(dev, rev);
		Rev rev2 = serv.getLastRev(dev);
		System.out.println("over");
		System.exit(1);
	}

}
