package org.comet4j.core.demo.talker;

import org.comet4j.core.CometConnection;
import org.comet4j.core.event.DroppedEvent;
import org.comet4j.core.listener.DropListener;

/**
 * 下线侦听
 * @author jinghai.xiao@gmail.com
 * @date 2011-3-3
 */

public class DownListener extends DropListener {

	/*
	 * (non-Jsdoc)
	 * @see org.comet4j.event.Listener#handleEvent(org.comet4j.event.Event)
	 */
	@Override
	public boolean handleEvent(DroppedEvent anEvent) {
		CometConnection conn = anEvent.getConn();
		if (conn != null) {
			DownDTO dto = new DownDTO(conn.getId());
			anEvent.getTarget().sendToAll(Constant.APP_MODULE_KEY, dto);
			System.out.println("dwon");
		} else {
			System.out.println("null dwon");
		}

		return true;
	}

}
