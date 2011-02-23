package org.comet4j.core;

/**
 * 用于封装向客户端发送信息的数据格式
 * @author xiaojinghai
 * @date 2011-2-23
 */

public class CometMessage {

	private int state = CometMessageType.DATA;// 消息类型
	private String stateText = "";
	private long datetime;// 发送时间
	private Object data;// 包含数据

	public CometMessage() {

	}

	public CometMessage(Object anData) {
		data = anData;
		datetime = System.currentTimeMillis();
	}

	public long getDatetime() {
		return datetime;
	}

	public void setDatetime(long datetime) {
		this.datetime = datetime;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getStateText() {
		return stateText;
	}

	public void setStateText(String stateText) {
		this.stateText = stateText;
	}

	public void destroy() {
		data = null;
	}

}
