/**
 * 
 */
package org.comet4j.core;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;



/**
 * 消息发送器
 * 负责消息的发送
 */

public class CometSender {
	private ExpiresCache cacher ;
	public CometSender(long timespan ,long frequency ) {
		cacher = new ExpiresCache(timespan,frequency);
	}

	/** 为一个连接发送一条消息
	 * @param c
	 * @param e
	 */
	public void sendTo(CometConnection c,CometMessage msg){
		if(c == null){
			return;
		}
		if(c.getResponse() == null){
			cacher.push(c, msg);
			return;
		}
		try {
			writeData(c, msg);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	/** 为一个连接发送多条消息
	 * @param c
	 * @param list
	 */
	public void sendTo(CometConnection c,List<CometMessage> list){
		if(c == null || list.isEmpty()){
			return;
		}
		for(CometMessage msg : list){
			sendTo(c,msg);
		}
	}
	/** 为多个连接发送一条消息
	 * @param list
	 * @param e
	 */
	public void sendTo(List<CometConnection> list,CometMessage msg){
		if(list == null || list.isEmpty()){
			return;
		}
		for(CometConnection c : list){
			sendTo(c,msg);
		}
	}
	
	private void writeData(CometConnection c,CometMessage msg) throws IOException{
		c.setDyingTime(System.currentTimeMillis());
		c.setState(CometProtocol.STATE_ALIVE);
		PrintWriter writer;
		writer = c.getResponse().getWriter();
		writer.print(CometProtocol.encode(msg));
		close(c);

	}
	
	
	private void close(CometConnection c) throws IOException{
		if(c.getWorkStyle().equals(CometProtocol.WORKSTYLE_LLOOP)){
			c.getResponse().getWriter().close();
			c.setResponse(null);
		}else{
			c.getResponse().getWriter().flush();
		}
		
	}
	
	//友员
	List<CometMessage> getCacheMessage(CometConnection conn){
		return cacher.get(conn);
	}
	
	
	
	public void destroy() {
		cacher = null;
	}
}
