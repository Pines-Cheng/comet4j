/*
 * Comet4J Copyright(c) 2011, http://code.google.com/p/comet4j/ This code is
 * licensed under BSD license. Use it as you wish, but keep this copyright
 * intact.
 */
package org.comet4j.core;

/**
 * 用于封装向客户端发送信息的数据格式
 * @author xiaojinghai
 * @date 2011-2-23
 */

public class CometMessage {

	/** 应用模块标识 */
	private String channel;
	private long time;// 发送时间
	private Object data;// 包含数据

	public CometMessage(Object anData, String aChannel) {
		data = anData;
		channel = aChannel;
		time = System.currentTimeMillis();
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public void destroy() {
		data = null;
	}

}
