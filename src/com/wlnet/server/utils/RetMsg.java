package com.wlnet.server.utils;

/***********************************************************************
 * Module:  RetMsgBean.java
 * Author:  xlian
 * Purpose: Defines the Class RetMsgBean
 ***********************************************************************/
import java.io.Serializable;

public class RetMsg  implements Serializable
{
	/**
	 * 结果类型
	 */
	private int flag;
	private String title;
	/**
	 * 结果描述
	 */
	private String msg;
	private Exception exception;
	/**
	 * 成功
	 */
	private static int SUCCESS = 1;
	/**
	 * 失败
	 */
	private static int FAIL = 0;
	/**
	 * 异常
	 */
	private static int EXCEPTION = -1;
	
	private static int MACILLEGAL = -3;//门店身份错误，MAC错误
	private static int PWDILLEGAL = -2;//门店身份错误，密码错误
	private static int AUTHILLEGAL = -4;//门店身份错误，门店不存在
	
	public boolean isMACIllegal()
	{
		return MACILLEGAL == this.flag; 
	}
	
	public boolean isPwdIllegal()
	{
		return PWDILLEGAL == this.flag;
	}
	
	public boolean isAuthIllegal()
	{
		return AUTHILLEGAL == this.flag;
	}
	
	/**
	 * 设置MAC错误标志
	 */
	public void MACIllegal()
	{
		this.flag = MACILLEGAL;
	}
	
	/**
	 * 设置密码错误标识
	 */
	public void PwdIllegal()
	{
		this.flag = PWDILLEGAL;
	}
	
	/**
	 * 设置门店ID错误表示
	 */
	public void AuthIllegal()
	{
		this.flag = AUTHILLEGAL;
	}

	/**
	 * 是否成功
	 */
	public boolean isSuccess()
	{
		return SUCCESS == this.flag;
	}

	/**
	 * 是否失败
	 */
	public boolean isFail()
	{
		return !this.isSuccess();
	}

	public RetMsg()
	{
		this.flag = SUCCESS;
		this.title = "操作成功";
		this.msg = "操作成功";
	}
	
	/**
	 * 设置成功
	 */
	public void success(String title,String msg)
	{
		this.flag = SUCCESS;
		this.title = title;
		this.msg = msg;
	}

	/**
	 * 设置失败
	 * 
	 * @param title
	 * @param msg
	 */
	public void fail(String title, String msg)
	{
		this.flag = FAIL;
		this.title = title;
		this.msg = msg;
	}

	/**
	 * 设置异常
	 * 
	 * @param title
	 * @param msg
	 * @param exception
	 */
	public void exception(String title, String msg, Exception exception)
	{
		this.flag = EXCEPTION;
		this.exception = exception;
		this.title = title;
		this.msg = msg;
	}

	public int getFlag()
	{
		return flag;
	}

	public void setFlag(int flag)
	{
		this.flag = flag;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	public Exception getException()
	{
		return exception;
	}

	public void setException(Exception exception)
	{
		this.exception = exception;
	}

}