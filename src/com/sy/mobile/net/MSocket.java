package com.sy.mobile.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * socket通讯
 * @author Administrator
 *
 */
public class MSocket {
	/**
	 * 通讯
	 * @param buf 传入的值
	 * @param byName 服务端地址
	 * @param port 端口号
	 * @return
	 */
	public byte[] socketCom(byte[] buf,String byName,int port){
		try {
			InetAddress address = InetAddress.getByName(byName);  //服务器地址
			DatagramPacket dataGramPacket = new DatagramPacket(buf, buf.length, address, port);
			DatagramSocket socket = new DatagramSocket();  //创建套接字
			socket.send(dataGramPacket);  //通过套接字发送数据

			byte[] backbuf = new byte[10240];
			DatagramPacket backPacket = new DatagramPacket(backbuf, backbuf.length);
			socket.receive(backPacket);  //接收返回数据
//	      //转换
//	      String backMsg = new String(backbuf, 0, backPacket.getLength(),"utf-8");
//	      int i1 = backMsg.indexOf("["),i2 = backMsg.indexOf("]")+1;
//	      if(i1!=-1&&i2!=-1){
//	    	  backMsg = backMsg.substring(i1, i2);
//	      }
			socket.close();
			return backbuf;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
