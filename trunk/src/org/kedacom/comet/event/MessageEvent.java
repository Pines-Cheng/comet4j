package org.kedacom.comet.event;

import org.kedacom.comet.CometConnection;
import org.kedacom.comet.CometEngine;
import org.kedacom.event.Event;


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
