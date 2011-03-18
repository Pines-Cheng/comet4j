package org.comet4j.core.event;

import org.comet4j.core.CometConnection;
import org.comet4j.core.CometEngine;
import org.comet4j.event.Event;


public class DropEvent extends Event<CometEngine> {
	private CometConnection conn;
	public DropEvent(CometEngine target,CometConnection aConn) {
		super(target);
		conn = aConn;
	}
	public CometConnection getConn() {
		return conn;
	}
	public void setConn(CometConnection conn) {
		this.conn = conn;
	}
	public void destroy() {
		super.destroy();
		conn = null;
	}
}
