package org.comet4j.core;


public class CometMessage {
	private int state = CometMessageType.DATA;
	private String stateText = "";
	private long datetime;
	private Object data;
	public CometMessage(){
		
	}
	public CometMessage(Object anData){
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
