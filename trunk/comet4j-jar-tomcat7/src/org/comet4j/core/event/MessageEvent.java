package org.comet4j.core.event;

import org.comet4j.core.CometConnection;
import org.comet4j.core.CometEngine;
import org.comet4j.core.CometMessage;
import org.comet4j.event.Event;

public class MessageEvent extends Event<CometEngine> {

	private CometConnection conn;
	private CometMessage data;

	public MessageEvent(CometEngine target, CometConnection aConn, CometMessage aData) {
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

	public CometMessage getData() {
		return data;
	}

	public void setData(CometMessage data) {
		this.data = data;
	}

	@Override
	public void destroy() {
		super.destroy();
		conn = null;
		data = null;
	}
}
