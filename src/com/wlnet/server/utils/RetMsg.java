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
	 * �������
	 */
	private int flag;
	private String title;
	/**
	 * �������
	 */
	private String msg;
	private Exception exception;
	/**
	 * �ɹ�
	 */
	private static int SUCCESS = 1;
	/**
	 * ʧ��
	 */
	private static int FAIL = 0;
	/**
	 * �쳣
	 */
	private static int EXCEPTION = -1;
	
	private static int MACILLEGAL = -3;//�ŵ���ݴ���MAC����
	private static int PWDILLEGAL = -2;//�ŵ���ݴ����������
	private static int AUTHILLEGAL = -4;//�ŵ���ݴ����ŵ겻����
	
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
	 * ����MAC�����־
	 */
	public void MACIllegal()
	{
		this.flag = MACILLEGAL;
	}
	
	/**
	 * ������������ʶ
	 */
	public void PwdIllegal()
	{
		this.flag = PWDILLEGAL;
	}
	
	/**
	 * �����ŵ�ID�����ʾ
	 */
	public void AuthIllegal()
	{
		this.flag = AUTHILLEGAL;
	}

	/**
	 * �Ƿ�ɹ�
	 */
	public boolean isSuccess()
	{
		return SUCCESS == this.flag;
	}

	/**
	 * �Ƿ�ʧ��
	 */
	public boolean isFail()
	{
		return !this.isSuccess();
	}

	public RetMsg()
	{
		this.flag = SUCCESS;
		this.title = "�����ɹ�";
		this.msg = "�����ɹ�";
	}
	
	/**
	 * ���óɹ�
	 */
	public void success(String title,String msg)
	{
		this.flag = SUCCESS;
		this.title = title;
		this.msg = msg;
	}

	/**
	 * ����ʧ��
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
	 * �����쳣
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