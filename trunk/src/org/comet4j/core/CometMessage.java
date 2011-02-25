package org.comet4j.core;

/**
 * 用于封装向客户端发送信息的数据格式
 * @author xiaojinghai
 * @date 2011-2-23
 */

public class CometMessage {

	/** 应用模块标识 */
	private String amk;
	private long time;// 发送时间
	private Object data;// 包含数据

	public CometMessage(Object anData, String anAppModuleKye) {
		data = anData;
		amk = anAppModuleKye;
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

	public String getAmk() {
		return amk;
	}

	public void setAmk(String amk) {
		this.amk = amk;
	}

	public void destroy() {
		data = null;
	}

}
