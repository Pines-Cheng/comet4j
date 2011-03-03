package org.comet4j.core.demo.talker;

import org.comet4j.core.CometConnection;
import org.comet4j.core.event.ConnectedEvent;
import org.comet4j.core.listener.ConnectListener;

/**
 * 上线侦听
 * @author jinghai.xiao@gmail.com
 * @date 2011-3-3
 */

public class UpListener extends ConnectListener {

	/*
	 * (non-Jsdoc)
	 * @see org.comet4j.event.Listener#handleEvent(org.comet4j.event.Event)
	 */
	@Override
	public boolean handleEvent(ConnectedEvent anEvent) {
		CometConnection conn = anEvent.getConn();
		UpDTO dto = new UpDTO(conn.getId());
		anEvent.getTarget().sendToAll(Constant.APP_MODULE_KEY, dto);
		System.out.println("up");
		return true;
	}

}
