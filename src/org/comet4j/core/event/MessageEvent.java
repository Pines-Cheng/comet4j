package org.comet4j.core.event;

import org.comet4j.core.CometConnection;
import org.comet4j.core.CometEngine;
import org.comet4j.event.Event;


public class MessageEvent extends Event<CometEngine> {
	private CometConnection conn;
	private Object data;
	public MessageEvent(CometEngine target,CometConnection aConn,Object aData) {
		super(target);
		conn = aConn;
		data = aData;
	}
	public CometConnection getConn() {
		return conn;
	}
	public void setConn(CometConnection conn) {
		this.conn = conn;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	public void destroy() {
		super.destroy();
		conn = null;
		data = null;
	}
}
