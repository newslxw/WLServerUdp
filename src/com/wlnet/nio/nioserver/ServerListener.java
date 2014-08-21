package com.wlnet.nio.nioserver;

import java.net.Socket;
import java.nio.channels.SocketChannel;


/**
 * <p>Title: �������¼�������</p>
 * @author starboy
 */

public interface ServerListener {

   /**
    * �������˴����������ʱ�������¼�
    * @param error ������Ϣ
    */
   public void onError(String error);
   /**
    * �������˴����������ʱ�������¼�
    * @param error ������Ϣ
    */
   public void onError(Exception error);

   /**
    * ���пͻ��˷�������ʱ�������¼�
    */
   public void onAccept() throws Exception;

   /**
    * ������˽��ܿͻ�������󴥷����¼�
    * @param request �ͻ�������
    */
   public void onAccepted(Request request) throws Exception;

   /**
    * ���ͻ��˷������ݣ����ѱ������������߳���ȷ��ȡʱ���������¼�
    * @param request �ͻ�������
    */
   public void onRead(Request request) throws Exception;

   /**
    * ��������ͻ��˷������󴥷����¼�
 * @param sc 
    * @param request �ͻ�������
    */
   public void onWrite(Request request, Response response) throws Exception;

   /**
    * ���ͻ�����������������Ӻ󴥷����¼�
    * @param request �ͻ�������
    */
   public void onClosed(Request request) throws Exception;
   
   public void onConnected(Socket socket) throws Exception;
   
   public void onAccepted(Socket client) throws Exception;
   
}
